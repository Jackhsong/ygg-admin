package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ActivitiyRelationProductEntity;
import com.ygg.admin.entity.ActivityManjianEntity;

public interface ActivityManjianDao
{
    
    List<ActivityManjianEntity> findAllActivityManjian(Map<String, Object> para)
        throws Exception;
    
    int countActivityManjian(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findProductIdByTypeAndTypeId(Map<String, Object> para)
        throws Exception;
    
    int saveActivityManjian(ActivityManjianEntity activity)
        throws Exception;
    
    int updateActivityManjian(ActivityManjianEntity activity)
        throws Exception;
    
    ActivityManjianEntity findActivityManjianById(int id)
        throws Exception;
    
    List<ActivityManjianEntity> findActivityManjianByMap(Map<String, Object> para)
        throws Exception;
    
    int updateDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    List<ActivitiyRelationProductEntity> findActivitiyRelationProduct(Map<String, Object> para)
        throws Exception;
    
    int countActivitiyRelationProduct(Map<String, Object> para)
        throws Exception;
    
    List<ActivitiyRelationProductEntity> findProductForAddToActivityManjian(Map<String, Object> para)
        throws Exception;
    
    int countProductForAddToActivityManjian(Map<String, Object> para)
        throws Exception;
    
    int addProductForActivityManjian(List<ActivitiyRelationProductEntity> productList)
        throws Exception;
    
    int deleteActivityManjianProduct(List<String> idList)
        throws Exception;
    
}
