package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CustomFunctionEntity;

public interface CustomFunctionDao
{
    
    List<CustomFunctionEntity> findAllCustomFunction(Map<String, Object> para)
        throws Exception;
    
    int countCustomFunction(Map<String, Object> para)
        throws Exception;
    
    int saveCustomFunction(CustomFunctionEntity function)
        throws Exception;
    
    int updateCustomFunction(Map<String, Object> para)
        throws Exception;
    
    int updateDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    CustomFunctionEntity findCustomFunctionById(int id)
        throws Exception;
    
}
