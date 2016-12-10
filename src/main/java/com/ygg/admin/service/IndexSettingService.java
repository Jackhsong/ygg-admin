package com.ygg.admin.service;

import java.util.Map;

public interface IndexSettingService
{
    
    Map<String, String> findSettingByKey(Map<String, Object> para)
        throws Exception;
    
    int updateConfigStatus(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonAdvertiseInfo(Map<String, Object> para)
        throws Exception;
    
    int updateAdvertiseSequence(Map<String, Object> para)
        throws Exception;
    
    int updateAdvertiseDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int deleteAdvertise(int id)
        throws Exception;
    
    int saveOrUpdateAdvertise(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findAdvertiseById(int id)
        throws Exception;
    
    int updateAdvertiseVersion(Map<String, Object> para)
        throws Exception;
    
    int updatePlatformVersion(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonVestAppInfo(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateVestApp(Map<String, Object> para)
        throws Exception;
    
    int updateVestAppStatus(Map<String, Object> para)
        throws Exception;
    
    int deleteVestApp(int id)
        throws Exception;
    
    int updateWeiXin(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更改马甲app自定义板块展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateVestAppCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int updateSearchIndex()
        throws Exception;
    
    int updatePlatformConfigById(Map<String, Object> para)
        throws Exception;
    
}
