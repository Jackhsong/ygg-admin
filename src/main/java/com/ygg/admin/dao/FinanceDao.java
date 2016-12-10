package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderListDetailView;
import com.ygg.admin.entity.OrderListView;

public interface FinanceDao
{
    /**
     * 检查订单号是否存在，包括正常订单和手工订单
     * 
     * @return
     * @throws Exception
     */
    boolean existsOrderNumber(String number)
        throws Exception;
    
    /**
     * 检查订单号对应订单是否已经结算
     * 
     * @param number
     * @return
     * @throws Exception
     */
    boolean isSettlement(String number)
        throws Exception;
    
    /**
     * 插入订单结算信息
     * 
     * @param number
     * @param status
     * @return
     * @throws Exception
     */
    int insertOrderSettlement(long number, String freightMoney, String date)
        throws Exception;
    
    int deleteOrderSettlement(long number)
        throws Exception;
    
    /**
     * 获取订单相关信息
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderListView> findOrderInfo(Map<String, Object> para)
        throws Exception;
    
    int countOrderInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 获取有结算信息的订单 相关信息
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderListView> findOrderInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    int countOrderInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    /**
     * 获取有结算信息的手工订单 相关信息
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderListView> findOrderManualInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    int countOrderManualInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询订单结算信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderSettlementByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 在退款退货中查询订单ID
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findOrderIdFromRefundByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询订单详细情况，for 导出
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderListDetailView> findOrderInfoDetail(Map<String, Object> para)
        throws Exception;
    
    int countOrderInfoDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询订单商品详细情况，for 导出
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderProductInfoDetail(Map<String, Object> para)
        throws Exception;

    /**
     * 查询左岸城堡
订单返利信息，for 导出
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderHQBSInfoDetail(List<Integer> idList)
            throws Exception;
    
    /**
     * 查询订单商品退款详细情况，for 导出
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderRefundInfoDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询订单详细情况，for 导出
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderListDetailView> findOrderManualInfoDetail(Map<String, Object> para)
        throws Exception;
    
    int countOrderManualInfoDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ids查询商品信息，for导出
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductInfoDetail(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findProductBaseInfoDetail(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSellerSettlementStatistics(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findOrderProductInfoForSellerSettlement(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSellerRefundSettlementStatistics(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findOrderProductIdByIdAndOrderIdNotInList(Map<String, Object> para)
        throws Exception;
    
    String existsOrderProductByNumberAndProductId(long number, int productId)
        throws Exception;
    
    int updateOrderProductCost(Map<String, Object> para)
        throws Exception;

    List<Map<String, Object>> findOrderInfoForSellerSettlementPeriod(Map<String,Object> para)
        throws Exception;

    List<Map<String, Object>> sumOrderInfoForSellerSettlementPeriod(Map<String,Object> para)
        throws Exception;
    
    List<Map<String, Object>> findSellerRefundProportion(Map<String, Object> para)
        throws Exception;

    List<Integer> findOrderIdByRefundSettlementComfirmDate(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findOrderProductByOrderSettlementTime(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findOrderSettlementInfoByComfirmDate(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findRefundSettlementInfoByComfirmDate(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findOrderManualProductByOrderSettlementTime(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findOrderManualSettlementInfoByComfirmDate(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findOrderProductAndSettlementByOrderSettlementTime(Map<String, Object> para)
        throws Exception;

    List<Map<String,Object>> findOrderManualProductAndSettlementByOrderSettlementTime(Map<String, Object> para)
        throws Exception;

}
