package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SellerAdminOrderDao
{
    
    List<Map<String, Object>> findSellerOrderList(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSellerOrderQuestionInfo(Map<String, Object> para)
        throws Exception;
    
    int countSellerOrderQuestionInfo(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSellerLogisticsOrderList(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSendTimeoutComplainOrder(Map<String, Object> para)
        throws Exception;
    
    int countSendTimeoutComplainOrder(Map<String, Object> para)
        throws Exception;
    
}
