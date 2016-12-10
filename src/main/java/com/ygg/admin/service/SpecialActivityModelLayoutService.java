package com.ygg.admin.service;

import java.util.Map;

public interface SpecialActivityModelLayoutService
{
    Map<String, Object> findListByParam(Map<String, Object> param) throws Exception;
    
    int saveOrUpdate(Map<String, Object> param) throws Exception;
    
    Map<String, Object> findById(String id) throws Exception;
}
