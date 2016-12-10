package com.ygg.admin.service;

import java.util.Map;

public interface CustomGGRecommend24Service {

	/**
	 * 查询推荐列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findRecommendListInfo(Map<String, Object> param) throws Exception;
	
	/**
	 * 新增或更新推荐
	 * @param param
	 * @return
	 * @throws Exception
	 */
	int saveOrUpdateRecommend(Map<String, Object> param) throws Exception;
	
	/**
	 * 查询单条
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findById(int id) throws Exception;
	
}
