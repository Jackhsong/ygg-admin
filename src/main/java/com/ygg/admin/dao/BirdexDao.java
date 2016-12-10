package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface BirdexDao
{
    
    List<Map<String, Object>> findAllBirdexProduct(Map<String, Object> para)
        throws Exception;
    
    int countBirdexProduct(Map<String, Object> para)
        throws Exception;
    
    int saveBirdexProduct(Map<String, Object> para)
        throws Exception;
    
    int updateBirdexProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteBirdexProduct(int id)
        throws Exception;
    
    int countBirdexStock(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllBirdexStock(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findBirdexCancelOrder(Map<String, Object> para)
        throws Exception;
    
    int countBirdexCancelOrder(Map<String, Object> para)
        throws Exception;
    
    int updateBirdexOrderConfirmPushStatus(int orderId)
        throws Exception;
    
}
