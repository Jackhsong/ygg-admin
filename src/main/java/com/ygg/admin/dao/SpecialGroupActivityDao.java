package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SpecialGroupActivityDao
{
    
    List<Map<String, Object>> findAllSpecialGroupActivity(Map<String, Object> para)
        throws Exception;
    
    int countSpecialGroupActivity(Map<String, Object> para)
        throws Exception;
    
    int saveSpecialGroup(Map<String, Object> para)
        throws Exception;
    
    int updateSpecialGroup(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSpecialGroupActivityProductByPara(Map<String, Object> para)
        throws Exception;
    
    int countSpecialGroupActivityProduct(Map<String, Object> para)
        throws Exception;
    
    int insertSpecialGroupActivityProduct(Map<String, Object> para)
        throws Exception;
    
    int updateSpecialGroupActivityProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteSpecialGroupActivityProduct(int id)
        throws Exception;
    
}
