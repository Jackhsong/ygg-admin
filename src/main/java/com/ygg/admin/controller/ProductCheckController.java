package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.CategorySecondEntity;
import com.ygg.admin.entity.CategoryThirdEntity;
import com.ygg.admin.entity.ProductCheckSnapshotEntity;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.ProductCheckLogService;

/**
 * 商品审核处理类
 * 
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("productCheck")
public class ProductCheckController {

	@Resource
	private ProductCheckLogService productCheckLogService;
	@Resource
	private CategoryService categoryService;

	@RequestMapping(value = "/checkList")
	public ModelAndView checkList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("productBase/checkList");
		return mv;
	}
	
	/**
	 * 展示配送地区模板
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/showDeliverAreaTemplate")
	@ResponseBody
	public Object showDeliverAreaTemplate(int templateId, int type) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 1);
			resultMap.put("info", productCheckLogService.findDeliverAreaTemplate(templateId, type));
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("message", ex.getMessage());
			return resultMap;
		}
	}
	
	/**
	 * 展示运费模板
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/showFreightTemplate")
	@ResponseBody
	public Object showFreightTemplate(int templateId, int type) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 1);
			resultMap.put("info", productCheckLogService.findFreightTemplate(templateId, type));
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("message", ex.getMessage());
			return resultMap;
		}
	}

	/**
	 * 审核列表
	 * 
	 * @param submitStartTime 提交审核时间
	 * @param submitEndTime
	 * @param checkStartTime 审核时间
	 * @param checkEndTime
	 * @param checker 审核负责人
	 * @param status 审核状态
	 * @param sellerProductId 商品ID
	 * @param categoryFirstid
	 * @param categorySecondId 商品类目
	 * @param name 商品名称
	 * @param sellerName 商家名称
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkListInfo")
	@ResponseBody
	public Object checkListInfo(String submitStartTime, String submitEndTime, String checkStartTime, String checkEndTime, String checker,
			@RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "0") int sellerProductId,
			@RequestParam(required = false, defaultValue = "0") int firstCategory,
			@RequestParam(required = false, defaultValue = "0") int secondCategory, String name, String sellerName,
			@RequestParam(required = false, defaultValue = "0") int waitingStatus,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = false, defaultValue = "50") int rows) throws Exception {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser == null) {
				return "";// 未登陆
			}
			page = page == 0 ? 1 : page;
			return JSON.toJSONStringWithDateFormat(productCheckLogService.findCheckLogList(submitStartTime, submitEndTime, checkStartTime,
					checkEndTime, checker, status, sellerProductId, firstCategory, secondCategory, name, sellerName,waitingStatus, page, rows), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception ex) {
			ex.printStackTrace();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("message", ex.getMessage());
			return resultMap;
		}
	}

	/**
	 * 待审核的商品信息
	 * 
	 * @param sellerProductId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkProductInfo/{id}/{type}")
	public ModelAndView checkProductInfo(@PathVariable int id, @PathVariable int type) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("productBase/checkInfo");
		Map<String, Object> info = null;
		if (type == 1) {
			info = productCheckLogService.findCheckInfo(id);
			mv.addObject("categoryInfos", info.get("categoryInfos"));
	        mv.addObject("productBaseInfo", info.get("productBaseInfo"));
	        mv.addObject("productBaseDetailImage", info.get("productBaseDetailImage"));
	        mv.addObject("productBaseCategoryInfos", info.get("productBaseCategoryInfos"));
		} else {
			info = productCheckLogService.findSnapshotInfo(id);
			long firstCategoryId = (long) info.get("category_first_id");
	        long secondCategoryId = (long) info.get("category_second_id");
	        long thirdCategoryId = (long) info.get("category_third_id");
	        CategoryFirstEntity f = categoryService.findCategoryFirstById((int) firstCategoryId);
	        CategorySecondEntity s = categoryService.findCategorySecondById((int) secondCategoryId);
	        CategoryThirdEntity t = categoryService.findCategoryThirdById((int) thirdCategoryId);
	        info.put("firstName", f == null ? null : f.getName());
	        info.put("secondName", s == null ? null : s.getName());
	        info.put("thirdName", t == null ? null : t.getName());
		}
		mv.addObject("productInfo", info);
		mv.addObject("type", type);
		return mv;
	}

	/**
	 * 保存商品审核快照
	 * 
	 * @param entity 商品审核快照对象
	 * @return
	 */
	@RequestMapping(value = "/saveCheckSnapshot")
	@ResponseBody
	@ControllerLog
	public Object saveCheckSnapshot(ProductCheckSnapshotEntity entity) {
		try {
			productCheckLogService.saveCheckSnapshot(entity);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 1);
			return resultMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("message", ex.getMessage());
			return resultMap;
		}
	}
	
	/**
     * 更新商家商品排期状态
     * 
     * @param 
     * @return
     */
    @RequestMapping(value = "/updateWaitingStatus")
    @ResponseBody
    @ControllerLog
    public Object updateWaitingStatus(String sellerProductId, String waitingStatus) {
        try {
            productCheckLogService.updateWaitingStatus(Integer.valueOf(sellerProductId), waitingStatus);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("message", ex.getMessage());
            return resultMap;
        }
    }
}
