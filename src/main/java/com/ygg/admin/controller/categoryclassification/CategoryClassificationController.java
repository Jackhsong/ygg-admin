
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡
APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.controller.categoryclassification;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.categoryclassification.CategoryClassificationService;
import com.ygg.admin.service.categorymanager.CateGoryManagerService;

/**
  * 品类馆分类管理
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryClassificationController.java 8541 2016-03-09 02:00:26Z zhanglide $   
  * @since 2.0
  */
@Controller
@RequestMapping("/categoryClassification")
public class CategoryClassificationController {
	/**日志工具    */
	Logger logger = Logger.getLogger(CategoryClassificationController.class);
	
	
//	@Resource(name="categoryClassificationService")
//	private categoryClassificationService categoryClassificationService;
	/**品类馆管理服务接口*/
    @Resource(name="cateGoryManagerService")
    private CateGoryManagerService cateGoryManagerService;
    
     /**    */
    @Resource(name="categoryClassificationService")
    private CategoryClassificationService categoryClassificationService;
    
	/**
	 * 格格福利团推荐
	 * @return ModelAndView
	 */
	@RequestMapping("list")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("categoryClassification/list");
		return mv;
	}
	
	 /** 自定义拼装页面管理 */
    @RequestMapping("/listClassification/{modelId}/{pageId}")
    public ModelAndView pageManage(@PathVariable("modelId") int modelId,
    		@PathVariable("pageId") int pageId)
        throws Exception{
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("categoryClassification/list");
        Map<String, Object> pageInfo = cateGoryManagerService.findPageById(pageId);
        mv.addObject("pageId", pageId + "");
        mv.addObject("pageName", pageInfo.get("name") + "");
        mv.addObject("modelId", modelId + "");
        return mv;
    }
	
	/**
	 * 列表查询
	 * @param status
	 * @param isDisplay
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/listInfo")
	@ResponseBody
	public Object listInfo(String title,
			@RequestParam(value = "isDisplay", defaultValue = "-1", required = false) int isDisplay,
			@RequestParam(value = "page2ModelId", defaultValue = "-1", required = false) int page2ModelId,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
		try {
			page = page == 0 ? 1 : page;
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDisplay", isDisplay);
			param.put("page2ModelId", page2ModelId);
			param.put("name", title);
			param.put("start", rows * (page - 1));
			param.put("size", rows);
			return JSON.toJSONString(categoryClassificationService.findListInfo(param));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}
	
	/**
	 * 查询单条
	 * @param id
	 * @return
	 */
	@RequestMapping("/findById")
	@ResponseBody
	public Object findById(HttpServletResponse response, int id) {
		try {
			response.setContentType("application/json;charset=UTF-8");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 1);
			resultMap.put("data", categoryClassificationService.findById(id));
			PrintWriter writer = response.getWriter();
			writer.write(JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
			writer.close();
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}
	
	/**
	 * 新增、修改
	 * @param param
	 * @return
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public Object saveOrUpdate(@RequestParam Map<String, Object> param) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 1);
			resultMap.put("data", categoryClassificationService.saveOrUpdateInfo(param));
			return resultMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "删除")
    public String delete(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map resultMap = new HashMap();
        int resultStatus = categoryClassificationService.delete(id);
        if (resultStatus != 1){
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }else{
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        return JSON.toJSONString(resultMap);
    }
}