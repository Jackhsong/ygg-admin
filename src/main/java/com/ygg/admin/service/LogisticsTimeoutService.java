package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderDetailInfoForSeller;

public interface LogisticsTimeoutService
{
    
    List<Map<String, Object>> findAllLogisticsTimeoutOrders(String startTime, String endTime, int orderType)
        throws Exception;
    
    Map<String, Object> jsonLogisticsOrders(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonLogisticsTimeoutReasonInfo(Map<String, Object> para)
        throws Exception;
    
    String saveTimeoutReason(int id, String name)
        throws Exception;
    
    String updateLogisticsTimeoutReasonStatus(Map<String, Object> para)
        throws Exception;
    
    String jsonComplainOrderInfo(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findLogisticsTimeoutByOid(int orderId)
        throws Exception;
    
    String findLogisticsTimeoutReasonById(int reasonId)
        throws Exception;
    
    List<Map<String, Object>> findComplainDetailsByOrderId(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    String dealComplain(int orderId, int complainId, int timeoutReasonId, int dealResult, String remark)
        throws Exception;
    
    String logisticsTimeOutComplain(int orderId, String reason)
        throws Exception;
    
    String addTimeoutOrderQcDeal(int orderId, int reasonId, String result, String remark)
        throws Exception;
    
    String addBatchTimeoutOrderQcDeal(String orderIds, int reasonId, String result, String remark)
        throws Exception;
    
    int getExportOrderNums(Map<String, Object> para)
        throws Exception;
    
    List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * status deal_user is_timeout logistics_timeout_reason_id
     */
    int updateLogisticsTimeout(Map<String, Object> para)
        throws Exception;
    
}
