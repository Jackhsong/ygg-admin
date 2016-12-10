package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface GegetuanDao {

	int countListInfo(Map<String, Object> param) throws Exception;
	
	List<Map<String, Object>> findListInfo(Map<String, Object> param) throws Exception;
}
