package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CustomActivityEntity;

public interface CustomActivitiesDao
{
    
    List<Map<String, Object>> findCustomActivitiesInfo(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findCustomActivitiesById(int id)
        throws Exception;
    
    int countCustomActivitiesInfo(Map<String, Object> para)
        throws Exception;
    
    int updateCustomActivitiesStatus(Map<String, Object> para)
        throws Exception;
    
    int add(Map<String, Object> para)
        throws Exception;
    
    int update(Map<String, Object> para)
        throws Exception;
    
    CustomActivityEntity findCustomActivitiesId(int oneDisplayId)
        throws Exception;
}
