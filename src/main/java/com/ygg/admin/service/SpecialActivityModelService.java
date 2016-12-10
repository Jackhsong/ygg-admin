package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface SpecialActivityModelService
{
    Map<String, Object> findListByParam(Map<String, Object> param)
        throws Exception;
    
    int saveOrUpdate(Map<String, Object> param)
        throws Exception;
    
    Map<String, Object> findById(String id)
        throws Exception;
    
    List<Map<String, Object>> findAllSpecialActivityModel(Map<String, Object> para)
        throws Exception;
}
