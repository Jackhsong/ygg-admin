package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface CustomActivitiesService
{
    
    Map<String, Object> jsonCustomActivitiesInfo(Map<String, Object> para)
        throws Exception;
    
    int updateCustomActivitiesStatus(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findCustomActivitiesById(int id)
        throws Exception;
    
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllCustomActivities(Map<String, Object> para)
        throws Exception;

}
