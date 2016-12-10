package com.ygg.admin.service;

import java.util.Map;

public interface GegetuanService {

	/**
	 * 团购信息列表
	 * 
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findListInfo(String name, int page, int rows) throws Exception;
}
