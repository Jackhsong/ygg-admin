package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderDetailInfoForSeller;

public interface TimeoutOrderDao
{
    /**
     * 根据订单Id查找订单申诉结果
     * @param orderIdList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findTimeOutOrderInfoByOidList(List<Integer> orderIdList)
        throws Exception;
    
    /**
     * 插入订单申诉
     * @param para
     * @return
     * @throws Exception
     */
    int insertOrderTimeoutComplain(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入订单申诉记录结果
     * @param orderId
     * @return
     * @throws Exception
     */
    int insertOrderTimeoutComplainResult(int orderId)
        throws Exception;
    
    /**
     * 修改订单申诉结果
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderTimeoutComplainResult(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找申诉订单
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllComplainOrder(Map<String, Object> para)
        throws Exception;
    
    int countComplainOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 
     * @param id
     * @return
     * @throws Exception
     */
    String findOrderTimeoutReasonName(int id)
        throws Exception;
    
    List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据订单ID查找最新的一条申诉记录
     * @param parseInt
     * @return
     * @throws Exception
     */
    Map<String, Object> findRecentOrderTimeoutComplainByOrderId(int orderId)
        throws Exception;
    
    /**
     * 更新订单申诉
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderTimeoutComplain(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据订单Id查找订单明细
     * @param orderId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderTimeoutComplainListByOrderId(int orderId)
        throws Exception;
    
    /**
     * 根据订单Id查找订单申诉处理结果
     * @param orderId
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderTimeoutComplainResultByOId(int orderId)
        throws Exception;
    
    int countTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    int insertTimeoutReason(String name)
        throws Exception;
    
    int updateTimeoutReason(Map<String, Object> para)
        throws Exception;
    
    /**
     * QC处理订单发货超时
     * @param para
     * @return
     * @throws Exception
     */
    int addTimeoutOrderQcDeal(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findOrderTimeoutQcDealListByOid(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findOrderTimeoutQcDealListByOids(List<Integer> orderIdList)
        throws Exception;
    
    int batchAddTimeoutOrderQcDeal(List<Map<String, Object>> params)
        throws Exception;
    
    List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception;
    
    int countTimeoutOrderDetail(Map<String, Object> para)
        throws Exception;
    
}
