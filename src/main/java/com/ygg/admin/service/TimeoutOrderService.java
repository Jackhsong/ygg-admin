package com.ygg.admin.service;

import com.ygg.admin.entity.OrderDetailInfoForSeller;

import java.util.List;
import java.util.Map;

public interface TimeoutOrderService
{
    /**
     * 订单超时申诉
     * @param orderId：订单Id
     * @param reason：申诉理由
     * @return
     * @throws Exception
     */
    String sendTimeOutComplain(int orderId, String reason)
        throws Exception;
    
    /**
     * 加载订单申诉列表
     * @param para
     * @return
     * @throws Exception
     */
    String jsonComplainOrderInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找超时原因
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    /**
     * 订单申诉处理
     * @param orderId
     * @param complainId
     * @param timeoutReasonId
     * @param dealResult
     * @param remark
     * @return
     * @throws Exception
     */
    String dealComplain(int orderId, int complainId, int timeoutReasonId, int dealResult, String remark)
        throws Exception;
    
    /**
     * 查找订单汇总信息
     * @param startTime
     * @param endTime
     * @param orderType ：订单类型，1左岸城堡，2左岸城堡，3全球购，4左岸城堡
，5燕网
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllOrderSummaryInfo(String startTime, String endTime, int orderType)
        throws Exception;


    /**
     * 查找订单申诉明细
     * @param orderId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findComplainDetailsByOrderId(int orderId)
        throws Exception;
    
    /**
     * 根据订单Id查找订单申诉结果
     * @param orderId
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderTimeoutComplainResultByOId(int orderId)
        throws Exception;
    
    String findOrderTimeoutReasonNameById(int id)
        throws Exception;
    
    Map<String, Object> jsonTimeoutReasonInfo(Map<String, Object> para)
        throws Exception;
    
    String saveTimeoutReason(int id, String name)
        throws Exception;
    
    String updateTimeoutReasonStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * QC处理超时订单
     * @param orderId：订单Id
     * @param reasonId：超时原因Id
     * @param result：处理结果
     * @param remark：处理备注
     * @return
     * @throws Exception
     */
    String addTimeoutOrderQcDeal(int orderId, int reasonId, String result, String remark)
        throws Exception;
    
    /**
     * QC批量处理超时订单
     * @param orderIds：订单列表
     * @param reasonId：超时原因Id
     * @param result：处理结果
     * @param remark：处理备注
     * @return
     * @throws Exception
     */
    String addBatchTimeoutOrderQcDeal(String orderIds, int reasonId, String result, String remark)
        throws Exception;
    
    List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception;
    
    int getExportOrderNums(Map<String, Object> para)
        throws Exception;
    
}
