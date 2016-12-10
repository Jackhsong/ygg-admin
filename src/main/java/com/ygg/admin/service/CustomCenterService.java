package com.ygg.admin.service;

import java.util.Map;

import com.ygg.admin.entity.CustomCenterEntity;

public interface CustomCenterService
{
    
    Map<String, Object> jsonCustomCenterInfo(Map<String, Object> para)
        throws Exception;
    
    String saveCustomCenter(CustomCenterEntity center)
        throws Exception;
    
    String updateCustomCenter(CustomCenterEntity center)
        throws Exception;
    
    int updateDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    CustomCenterEntity findCustomCenterById(int id)
        throws Exception;
    
}
