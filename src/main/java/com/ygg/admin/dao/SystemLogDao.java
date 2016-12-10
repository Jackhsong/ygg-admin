package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SystemLogDao
{
    
    List<Map<String, Object>> findAllSystemLogList(Map<String, Object> para)
        throws Exception;
    
    int countSystemLog(Map<String, Object> para)
        throws Exception;
    
    public int logger(Map<String, Object> para)
        throws Exception;
    
    int log(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllLogList(Map<String, Object> para)
        throws Exception;
    
    int countLog(Map<String, Object> para)
        throws Exception;
}
