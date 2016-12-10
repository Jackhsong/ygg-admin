package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SpecialActivityModelDao
{
    List<Map<String, Object>> findListByParam(Map<String, Object> param) throws Exception;
    
    int countByParam(Map<String, Object> param) throws Exception;
    
    int updateByParam(Map<String, Object> param) throws Exception;
    
    int save(Map<String, Object> param) throws Exception;
    
    Map<String, Object> findById(String id) throws Exception;
}
