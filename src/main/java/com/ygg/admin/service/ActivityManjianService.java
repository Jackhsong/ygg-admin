package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ActivitiyRelationProductEntity;
import com.ygg.admin.entity.ActivityManjianEntity;

public interface ActivityManjianService
{
    
    Map<String, Object> jsonActivityManjianInfo(Map<String, Object> para)
        throws Exception;
    
    String saveActivityManjian(ActivityManjianEntity activity)
        throws Exception;
    
    String updateActivityManjian(ActivityManjianEntity activity)
        throws Exception;
    
    String updateDisplayStatus(int id, int isAvailable)
        throws Exception;
    
    String jsonProductInfo(Map<String, Object> para)
        throws Exception;
    
    String jsonProductListForAdd(Map<String, Object> para)
        throws Exception;
    
    String addProductForActivityManjian(int type, int typeId, String productIds)
        throws Exception;
    
    int deleteActivityManjianProduct(List<String> idList)
        throws Exception;
    
    int updateProductTime(String productIds, String startTime, String endTime)
        throws Exception;
    
    List<ActivitiyRelationProductEntity> findActivitiyRelationProduct(Map<String, Object> para)
        throws Exception;
    
}
