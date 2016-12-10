package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface IndexSettingDao
{
    
    List<Map<String, Object>> findSetting(Map<String, Object> para)
        throws Exception;
    
    int updateConfigStatus(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllAdvertise(Map<String, Object> para)
        throws Exception;
    
    int countAdvertise(Map<String, Object> para)
        throws Exception;
    
    int updateAdvertise(Map<String, Object> para)
        throws Exception;
    
    int deleteAdvertise(int id)
        throws Exception;
    
    int addAdvertise(Map<String, Object> para)
        throws Exception;
    
    int findAdvertiseMaxSequence()
        throws Exception;
    
    int updateAdvertiseVersion(Map<String, Object> para)
        throws Exception;
    
    int updatePlatformVersion(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllVestAppInfo(Map<String, Object> para)
        throws Exception;
    
    int countVestAppInfo(Map<String, Object> para)
        throws Exception;
    
    int addVestApp(Map<String, Object> para)
        throws Exception;
    
    int updateVestApp(Map<String, Object> para)
        throws Exception;
    
    int deleteVestApp(int id)
        throws Exception;
    
    int updateWeiXin(Map<String, Object> para)
        throws Exception;

    int updatePlatformConfigById(Map<String,Object> para)
        throws Exception;
}
