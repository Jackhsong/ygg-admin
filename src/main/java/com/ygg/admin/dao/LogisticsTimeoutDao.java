package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.LogisticsTimeoutEntity;
import com.ygg.admin.entity.OrderDetailInfoForSeller;

public interface LogisticsTimeoutDao
{
    
    List<Map<String, Object>> findAllLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception;
    
    int countLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception;
    
    String findLogisticsTimeoutReasonById(int id)
        throws Exception;
    
    int countLogisticsTimeoutOrders2(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findLogisticsTimeoutOrders2(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllLogisticsTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    int countLogisticsTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    int insertTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    int updateTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllComplainOrder(Map<String, Object> para)
        throws Exception;
    
    int countComplainOrder(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findLogisticsTimeoutByOid(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findLogisticsTimeoutComplainListByOrderId(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findRecentLogisticsTimeoutComplainByOrderId(int orderId)
        throws Exception;
    
    int updateLogisticsTimeoutComplain(Map<String, Object> para)
        throws Exception;
    
    int updateLogisticsTimeout(Map<String, Object> para)
        throws Exception;
    
    int insertLogisticsTimeoutComplain(Map<String, Object> para)
        throws Exception;
    
    int addTimeoutOrderQcDeal(Map<String, Object> para)
        throws Exception;
    
    LogisticsTimeoutEntity findLogisticsTimeoutByOrderId(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findOrderLogisticsTimeoutQcListByOid(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findOrderLogisticsTimeoutQcListByOids(List<Integer> orderIdList)
        throws Exception;
    
    int batchAddTimeoutOrderQcDeal(List<Map<String, Object>> params)
        throws Exception;
    
    List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception;
    
    int countTimeoutOrderDetail(Map<String, Object> para)
        throws Exception;
    
}
