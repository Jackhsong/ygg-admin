package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CustomCenterEntity;

public interface CustomCenterDao
{
    
    List<CustomCenterEntity> findAllCustomCenter(Map<String, Object> para)
        throws Exception;
    
    int countCustomCenter(Map<String, Object> para)
        throws Exception;
    
    int saveCustomCenter(CustomCenterEntity center)
        throws Exception;
    
    int updateCustomCenter(CustomCenterEntity center)
        throws Exception;
    
    int updateDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    CustomCenterEntity findCustomCenterById(int id)
        throws Exception;
    
}
