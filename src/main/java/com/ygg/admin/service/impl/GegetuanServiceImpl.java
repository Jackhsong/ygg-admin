package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.GegetuanDao;
import com.ygg.admin.service.GegetuanService;

@Service("gegetuanService")
public class GegetuanServiceImpl implements GegetuanService {
	
	@Resource
	private GegetuanDao gegetuanDao;

	@Override
	public Map<String, Object> findListInfo(String name, int page, int rows) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", name);
		param.put("start", rows * (page - 1));
		param.put("size", rows);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", gegetuanDao.countListInfo(param));
		result.put("rows", gegetuanDao.findListInfo(param));
		
		return result;
	}

}
