package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SystemConfigDao
{
    
    List<Map<String, Object>> findWhiteIpInfo(Map<String, Object> para)
        throws Exception;
    
    int countWhiteIpInfo(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findWhiteURLInfo(Map<String, Object> para)
        throws Exception;
    
    int countWhiteURLInfo(Map<String, Object> para)
        throws Exception;
    
    int addWhiteIp(Map<String, Object> para)
        throws Exception;
    
    int updateWhiteIp(Map<String, Object> para)
        throws Exception;
    
    int addWhiteURL(Map<String, Object> para)
        throws Exception;
    
    int updateWhiteURL(Map<String, Object> para)
        throws Exception;
    
    int updateWhiteURLStatus(Map<String, Object> para)
        throws Exception;
    
    int updateWhiteIpStatus(Map<String, Object> para)
        throws Exception;
    
}
