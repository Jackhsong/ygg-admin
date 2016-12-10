package com.ygg.admin.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.service.CustomGGRecommendService;

@Controller
@RequestMapping("customGGRecommend")
public class CustomGGRecommendController {

	Logger logger = Logger.getLogger(CustomGGRecommendController.class);
	
	@Resource
	private CustomGGRecommendService customGGRecommendService;
	
	/**
	 * 格格推荐入口
	 * @return
	 */
	@RequestMapping("list")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("customGGRecommend/list");
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
	public Object listInfo(String status,
			@RequestParam(value = "isDisplay", defaultValue = "-1", required = false) int isDisplay,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
		try {
			page = page == 0 ? 1 : page;
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("isDisplay", isDisplay);
			param.put("status", status);
			param.put("start", rows * (page - 1));
			param.put("size", rows);
			return JSON.toJSONStringWithDateFormat(customGGRecommendService.findRecommendListInfo(param), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
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
			resultMap.put("data", customGGRecommendService.findById(id));
			//return resultMap;
			PrintWriter writer = response.getWriter();
			writer.write(JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
			//return JSON.toJSONStringWithDateFormat(resultMap, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
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
			resultMap.put("data", customGGRecommendService.saveOrUpdateRecommend(param));
			return resultMap;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}
	
}
