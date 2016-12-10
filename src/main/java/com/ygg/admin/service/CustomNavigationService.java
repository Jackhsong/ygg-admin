package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface CustomNavigationService {

	/**
	 * 查询首页导航列表
	 * 
	 * @param id 导航ID
	 * @param name 导航名称
	 * @param isDisplay 是否展示
	 * @return
	 */
	List<Map<String, Object>> findNavigationList(int id, String name, int isDisplay, int page, int rows) throws Exception;
	
	/**
	 * 根据导航ID查找导航信息
	 * @param id 导航ID
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> findById(int id) throws Exception;
	
	/**
	 * 根据条件更新导航
	 * 
	 * @param id 导航ID
	 * @param sequence 导航排序
	 * @param isDisplay 是否展示
	 * 
	 * @return
	 * @throws Exception
	 */
	int updateByParam(int id, int sequence, int isDisplay) throws Exception;
	
	/**
	 * 保存首页导航信息
	 * 
	 * @param name 导航名称
	 * @param remark 备注信息
	 * @param type 类型 1：组合，2：网页自定义活动，3：原生自定义活动
	 * @param displayId 类型对应的ID
	 * @param isDisplay 是否展示
	 * 
	 * @return
	 * @throws Exception
	 */
	int save(String name, String remark, int type, int displayId, int isDisplay) throws Exception;
	
	/**
	 * 更新首页导航信息
	 * @param id 导航ID
	 * @param name 导航名称
	 * @param remark 备注信息
	 * @param type 类型 1：组合，2：网页自定义活动，3：原生自定义活动
	 * @param displayId 类型对应的ID
	 * @param isDisplay 是否展示
	 * @return
	 * @throws Exception
	 */
	int update(int id, String name, String remark, int type, int displayId, int isDisplay) throws Exception;

	/**
	 * 查询导航支持的地区信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> findNavigationSupportAreaInfo(int id)
		throws Exception;

	int updateNavAreaInfo(int id, List<Integer> provinceId, int supportAreaType)
		throws Exception;
}
