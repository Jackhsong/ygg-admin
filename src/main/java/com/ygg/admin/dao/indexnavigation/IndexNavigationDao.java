
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
package com.ygg.admin.dao.indexnavigation;

import java.util.List;
import java.util.Map;

/**
  * 新版首页自定义导航Dao接口
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: IndexNavigationDao.java 9215 2016-03-24 11:50:02Z zhangyibo $   
  * @since 2.0
  */
public interface IndexNavigationDao {
	/**
     * 查询首页导航列表
     * 
     * @param id 导航ID
     * @param name 导航名称
     * @param isDisplay 是否展示
     * @return
     */
    List<Map<String, Object>> findNavigationList(int id, String name, int isDisplay, int page, int rows)
        throws Exception;
    
    /**
     * 根据导航ID查找导航信息
     * @param id 导航ID
     * @return
     * @throws Exception
     */
    Map<String, Object> findById(int id)
        throws Exception;
    
    /**
     * 更新导航排序值
     * 
     * @param id 导航ID
     * @param sequence 导航排序
     * @param isDisplay 是否展示
     * 
     * @return
     * @throws Exception
     */
    int updateByParam(int id, int sequence, int isDisplay)
        throws Exception;
    
    /**
     * 根据ID加载导航信息
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findNavigationById(int id)
        throws Exception;
    
    /**
     * 保存首页导航信息
     * @param name
     * @param remark
     * @param type
     * @param displayId
     * @param isDisplay
     * @return
     * @throws Exception
     */
    int save(String name, String remark, int type, int displayId, int isDisplay)
        throws Exception;
    
    /**
     * 更新首页导航信息
     * @param id
     * @param name
     * @param remark
     * @param type
     * @param displayId
     * @param isDisplay
     * @return
     * @throws Exception
     */
    int update(int id, String name, String remark, int type, int displayId, int isDisplay)
        throws Exception;
    
    int update(int id, int supportAreaType)
        throws Exception;
    
    /**
     * 批量插入导航和省份关系
     * @param list
     * @return
     * @throws Exception
     */
    int batchInsertRelationCustomNavigationAndProvince(List<Map<String, Object>> list)
        throws Exception;

    /**
     * 批量插入导航和城市关系
     * @param list
     * @return
     * @throws Exception
     */
    int batchInsertRelationCustomNavigationAndCity(List<Map<String, Object>> list)
            throws Exception;
    
    /**
     * 删除导航和省份对应关系
     * @param naId
     * @return
     * @throws Exception
     */
    int deleteRelationCustomNavigationAndProvinceById(int naId)
        throws Exception;

    int deleteRelationCustomNavigationAndCityById(int naId)
        throws Exception;
    
    List<String> findRelationProvinceIdByNavId(int naId)
        throws Exception;

    List<Map<String, Object>> findRelationCityIdByNavId(int naId)
            throws Exception;
    
    List<Map<String, Object>> findAllCustomNavigationByPara(Map<String, Object> para)
        throws Exception;

    /**
     * 查询首页热门城市信息
     *
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findHotCityInfo(Map<String,Object> para)
            throws Exception;

    int countHotCityInfo()
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
     * @return
     * @throws Exception
     */
    int updateHotCityInfo(Map<String,Object> para)
            throws Exception;
}
