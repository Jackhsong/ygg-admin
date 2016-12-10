package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface SaleFlagService
{
    
    Map<String, Object> jsonSaleFlagInfo(Map<String, Object> para)
        throws Exception;
    
    boolean checkFlagIsInUse(int id)
        throws Exception;
    
    int updateFlag(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateFlag(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> jsonSaleFlagCode(Map<String, Object> para)
        throws Exception;
    
    String findFlagNameById(int id)
        throws Exception;
}
