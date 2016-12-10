package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SaleFlagDao
{
    
    List<Map<String, Object>> findAllSaleFlagInfo(Map<String, Object> para)
        throws Exception;
    
    int countSaleFlagInfoInfo(Map<String, Object> para)
        throws Exception;
    
    int countFlagIdFromSaleWindow(int id)
        throws Exception;
    
    int updateFlag(Map<String, Object> para)
        throws Exception;
    
    int saveFlag(Map<String, Object> para)
        throws Exception;
    
    String findFlagNameById(int id)
        throws Exception;
    
}
