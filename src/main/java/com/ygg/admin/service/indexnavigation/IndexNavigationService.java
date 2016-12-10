
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service.indexnavigation;

import java.util.List;
import java.util.Map;

/**
  * 新版首页自定义导航服务接口
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: IndexNavigationService.java 9333 2016-03-28 08:07:03Z zhangyibo $   
  * @since 2.0
  */
public interface IndexNavigationService {
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

	int updateNavAreaInfo(int id, List<Integer> provinceId, int supportAreaType, List<Map<String, Object>> provinceHotCityList)
		throws Exception;

    /**
     * 查询首页热门城市信息
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> findHotCityInfo(int start, int max)
        throws Exception;
        
    /**
     * 保存热门城市
     * 
     * @param provinceCode
     * @param cityCode
     * @return
     * @throws Exception
     */
    int saveHotCityInfo(String provinceCode, String cityCode)
        throws Exception;
        
    /**
     * 修改热门城市信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int updateHotCityInfo(int id, int isAvailable, int sequence)
        throws Exception;

	boolean existsHotCityInfo(String provinceCode, String cityCode)
			throws Exception;
}
