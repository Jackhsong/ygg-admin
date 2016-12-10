package com.ygg.admin.service;

import java.util.Map;

public interface CustomRegion24Service
{
    
    Map<String, Object> jsonCustomRegionInfo(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateCustonRegion(Map<String, Object> para)
        throws Exception;
    
    int updateCustomRegionSequence(Map<String, Object> para)
        throws Exception;
    
    int updateCustomRegionDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int updateCustomRegionAvailableStatus(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findCustomRegionById(int regionId)
        throws Exception;
    
    Map<String, Object> jsonCustomLayoutInfo(Map<String, Object> para)
        throws Exception;
    
    int updateCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int updateCustomLayoutSequence(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateCustomLayout(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findCustomLayoutBydId(Map<String, Object> para)
        throws Exception;
    
    String deleteCustomLayout(int id)
        throws Exception;
    
}
