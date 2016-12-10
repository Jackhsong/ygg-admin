package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface SystemConfigService
{
    
    Map<String, Object> jsonWhiteIpInfo(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonWhiteURLInfo(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateWhiteIp(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateWhiteURL(Map<String, Object> para)
        throws Exception;
    
    int updateWhiteURLStatus(Map<String, Object> para)
        throws Exception;
    
    int updateWhiteIpStatus(Map<String, Object> para)
        throws Exception;
    
    List<String> findAllWhiteIPList()
        throws Exception;
    
    List<String> findAllWhiteURLList()
        throws Exception;
    
}
