package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.service.GegetuanService;

@Controller
@RequestMapping("gegetuan")
public class GegetuanController {

	Logger logger = Logger.getLogger(GegetuanController.class);
	
	@Resource
	private GegetuanService gegetuanService;
	
	@RequestMapping("toListInfo")
	public ModelAndView toListInfo() {
		ModelAndView mv = new ModelAndView("gegetuan/listInfo");
		return mv;
	}
	
	
	@RequestMapping("listInfo")
	@ResponseBody
	public Object listInfo(String name,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
		try {
			page = page == 0 ? 1 : page;
			return JSON.toJSONStringWithDateFormat(gegetuanService.findListInfo(name, page, rows), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", ex.getMessage());
			return resultMap;
		}
	}
}
