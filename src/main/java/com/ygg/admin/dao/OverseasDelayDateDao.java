package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OverseasDelayRemindDateEntity;

public interface OverseasDelayDateDao
{
    
    int delete(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findOverseasDelayDateInfo(Map<String, Object> para)
        throws Exception;
    
    int countOverseasDelayDateInfo(Map<String, Object> para)
        throws Exception;
    
    int deleteByYear(String year)
        throws Exception;
    
    OverseasDelayRemindDateEntity findDelayDateByDay(String day)
        throws Exception;
    
    int isExist(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
    
}
