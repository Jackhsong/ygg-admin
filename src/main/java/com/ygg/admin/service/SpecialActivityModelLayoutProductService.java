package com.ygg.admin.service;

import java.util.Map;
import java.util.Set;

public interface SpecialActivityModelLayoutProductService
{
    Map<String, Object> findListByParam(Map<String, Object> param) throws Exception;
    
    int saveOrUpdate(Map<String, Object> param) throws Exception;

    int saveByQuickAdd(Set<Integer> productIds,int layoutId) throws Exception;
}
