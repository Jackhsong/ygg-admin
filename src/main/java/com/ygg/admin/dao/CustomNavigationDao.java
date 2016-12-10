package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface CustomNavigationDao
{
    
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
     * 删除导航和省份对应关系
     * @param naId
     * @return
     * @throws Exception
     */
    int deleteRelationCustomNavigationAndProvinceById(int naId)
        throws Exception;
    
    List<String> findRelationProvinceIdByNavId(int naId)
        throws Exception;
    
    List<Map<String, Object>> findAllCustomNavigationByPara(Map<String, Object> para)
        throws Exception;
}
