
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
package com.ygg.admin.controller.hotlist;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.controller.brandrecommend.BrandRecommendController;
import com.ygg.admin.entity.hotlist.SelaBrandEntity;
import com.ygg.admin.service.hotlist.SaleBrandService;

/**
  * 热卖品牌
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: SaleBrandController.java 9544 2016-03-31 10:56:11Z zhanglide $   
  * @since 2.0
  */
@Controller
@RequestMapping("/saleBrand")
public class SaleBrandController {
	/**日志工具    */
	Logger logger = Logger.getLogger(BrandRecommendController.class);
	 /**    */
	@Resource(name="saleBrandService")
	private SaleBrandService saleBrandService;
	/**
	 * 格格福利团推荐
	 * @return ModelAndView
	 */
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("hotlist/salebrandlist");
        mv.addObject("stateId", 0);
        mv.addObject("categoryFirstId",0);
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
	public Object listInfo(String name,
			@RequestParam(value = "isDisplay", defaultValue = "-1", required = false) int isDisplay,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
		try {
			page = page == 0 ? 1 : page;
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDisplay", isDisplay);
			if(StringUtils.isNotBlank(name)){
				param.put("name", "%" + name + "%");
			}
			param.put("start", rows * (page - 1));
			param.put("size", rows);
			return JSON.toJSONString(saleBrandService.findListInfo(param));
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
			resultMap.put("data", saleBrandService.findByIdOrProductBaseId(id,-1));
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
			Map<String, Object> para = new HashMap<String, Object>();
			String headImage = String.valueOf(param.get("headImage")+"");
			if(StringUtils.isBlank(headImage)){
				int sequence = Integer.valueOf(param.get("sequence")+"").intValue();
		        para.put("sequence", sequence);
		        int count = saleBrandService.getCount(para);
		        if(count<5){
		        	resultMap.put("status", 0);
					resultMap.put("msg", "排序在前五时,请上传品牌头图!");
		        }else{
		        	resultMap.put("data", saleBrandService.saveOrUpdateInfo(param));
		        }
			}else{
				resultMap.put("data", saleBrandService.saveOrUpdateInfo(param));
			}
			return resultMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}
	
	/**
	 * 新增
	 * @param param
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(@RequestParam Map<String, Object> param) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 1);
			Map<String, Object> para = new HashMap<String, Object>();
			int brandId = Integer.valueOf(param.get("brandId")+"").intValue();
	        para.put("brandId", brandId);
	        int count = saleBrandService.getCount(para);
	        if(count>0){
	        	//已添加
	        	resultMap.put("status", 0);
				resultMap.put("msg", "基本商品ID["+brandId+"]已经存在列表中!");
	        }else{
	        	resultMap.put("data", saleBrandService.saveOrUpdateInfo(param));
	        }
			return resultMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", "保存失败");
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
        int resultStatus = saleBrandService.delete(id);
        if (resultStatus != 1){
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }else{
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        return JSON.toJSONString(resultMap);
    }
	
	/**
     * 根据基本商品id获取符合要求的热卖单品
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getBrandInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getBrandInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("brandId", id);
        int count = saleBrandService.getCount(para);
        SelaBrandEntity se;
        resultMap.put("status", 1);
        if(count>0){
        	//已添加
        	resultMap.put("status", 0);
			resultMap.put("msg", "品牌ID["+id+"]已经存在列表中!");
			se = new SelaBrandEntity();
        }else{
        	se = saleBrandService.getBrandInfo(para);
    		if(se == null){
    			resultMap.put("status", 0);
    			resultMap.put("msg", "品牌ID["+id+"]不符合要求!");
    			se = new SelaBrandEntity();
    		}
        }
		resultMap.put("brandId", se.getBrandId());
		resultMap.put("name", se.getName());
		resultMap.put("stateId", se.getStateId());
		resultMap.put("image", se.getImage());
		resultMap.put("headImage", se.getHeadImage());
		resultMap.put("categoryFirstId", se.getCategoryFirstId());
		resultMap.put("sequence", se.getSequence());
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/deleteBat", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "批量删除")
    public String deleteProductGroupStatus(@RequestParam(value = "ids", required = false, defaultValue = "0") String ids)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            int total = 0;
            String[] arr = ids.split(",");
            for (String it : arr)
            {
                total += saleBrandService.delete(Integer.valueOf(it));
            }
            resultMap.put("status", 1);
            resultMap.put("msg", "成功删除" + total + "条记录。");
            return JSON.toJSONString(resultMap);
        }catch (Exception e){
        	logger.error("热卖商品批量删除失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
}
