package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.*;

public interface RefundDao
{
    
    // ######################## begin ################ 退款信息 ################
    
    /**
     * 新增退款信息
     * 
     * @param refund
     * @return
     * @throws Exception
     */
    int saveRefund(RefundEntity refund)
        throws Exception;
        
    /**
     * 更新退款信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateRefund(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据用户信息查询订单ids，包括用户名，收货人信息
     * 
     * @return
     * @throws Exception
     */
    List<Integer> findAllOrderIdsByUserInfo(Map<String, Object> para)
        throws Exception;
        
    /**
     * 查询退款表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<RefundEntity> findAllRefundByPara(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据id查询退款信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    RefundEntity findRefundById(int id)
        throws Exception;
        
    /**
     * count 退款 数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllRefundByPara(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据ID列表 查询 退款信息 是否收货状态
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Map<String, Object>> findAllRefundIsReceiveStatusByIds(Map<String, Object> para)
        throws Exception;
        
    /**
     * 查询退款物流单号信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findRefundLogisticsByRefundId(int id)
        throws Exception;
        
    /**
     * 根据para查询退款退货物流单号信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findRefundLogisticsByPara(Map<String, Object> para)
        throws Exception;
        
    /**
     * 插入退款退货物流单号
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveRefundLogistics(Map<String, Object> para)
        throws Exception;
        
    /**
     * 更新退款退货物流信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateRefundLogistics(Map<String, Object> para)
        throws Exception;
        
    // ###################### end ################# 退款信息
    // ########################
    
    // ######################## begin ################ 订单商品信息 ################
    
    /**
     * 根据id查询订单商品信息 order_product_refund
     * 
     * @param id
     * @return
     */
    Map<String, Object> findOrderProductInfoByOrderProductId(int id);
    
    /**
     * 根据ID列表 查询 商品名称、购买数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Map<String, Object>> findAllOrderProductInfoByIds(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据OrderId列表 查询 购买人 收货人信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Map<String, Object>> findAllOrderReceiveInfoByIds(Map<String, Object> para)
        throws Exception;
        
    // ######################## end ################ 订单商品信息 ################
    
    // ######################## begin ################ 退款用户信息 ################
    /**
     * 根据id查询 退款用户信息 卡号信息
     * 
     * @param id
     * @return
     */
    Map<String, Object> findAccountCardById(int id);
    
    // ######################## end ################ 退款用户信息 ################
    
    // ######################## begin ################ 产品退款流程 记录
    // ################
    
    /**
     * 插入订单产品退款流程
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int saveOrderProductRefundTeack(OrderProductRefundTeackEntity entity)
        throws Exception;
        
    /**
     * 查询订单产品退款流程
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderProductRefundTeackEntity> findOrderProductRefundTeack(Map<String, Object> para)
        throws Exception;
        
    public int updateOrderProductRefundTeack(OrderProductRefundTeackEntity entity)
        throws Exception;
        
    // ######################## end ################ 产品退款流程 记录 ################
    
    // ######################## begin ################ 财务打款银行账户 ################
    
    /**
     * 插入财务打款账户
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveFinancialAffairsCard(Map<String, Object> para)
        throws Exception;
        
    /**
     * 删除财务打款账户
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int deleteFinancialAffairsCardByIds(List<Integer> idList)
        throws Exception;
        
    /**
     * 查询 财务打款账户 list
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllFinancialAffairsCard(Map<String, Object> para)
        throws Exception;
        
    /**
     * count 财务打款账户 数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllFinancialAffairsCard(Map<String, Object> para)
        throws Exception;
        
    List<Map<String, Object>> findAllFinancialAffairsCardById(int transferAccount)
        throws Exception;
        
    // ######################## end ################ 财务打款银行账户 ################
    
    /**
     * 查询退款分摊记录信息
     * 
     * @param refundId
     * @return
     * @throws Exception
     */
    RefundProportionEntity findRefundProportionByRefundId(int refundId)
        throws Exception;
        
    /**
     * 根据para 查询退款分摊记录信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<RefundProportionEntity> findAllRefundProportionByPara(Map<String, Object> para)
        throws Exception;
        
    /**
     * 更新退款分摊记录信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateRefundProportionByRefundId(Map<String, Object> para)
        throws Exception;
        
    /**
     * 保存退款分摊记录信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveRefundProportionByRefundId(Map<String, Object> para)
        throws Exception;
        
    Map<String, Object> findOrderProductCostById(int id)
        throws Exception;
        
    int findOtherNotExistsRefund(Map<String, Object> para)
        throws Exception;
        
    int findOtherNotExistsRefundForCancelOrder(Map<String, Object> para)
        throws Exception;
        
    int findOtherNotExistsRefundForCancelOrderForStep1(Map<String, Object> para)
        throws Exception;
        
    int countRefundByOrderProductId(int id)
        throws Exception;
        
    List<Map<String, Object>> findRefundForEveryday(Map<String, Object> param)
        throws Exception;
        
    List<Map<String, Object>> findRefundSellerIdForSeller(Map<String, Object> param)
        throws Exception;
        
    int countRefundSellerIdForSeller(Map<String, Object> param)
        throws Exception;
        
    List<Map<String, Object>> findRefundForSeller(Map<String, Object> param)
        throws Exception;
        
    List<MwebAutomaticRefundEntity> findMwebAutomaticRefund(Map<String, Object> param)
        throws Exception;

    FinancialAffairsCardEntity findFinancialAffairsCardById(int id)
        throws Exception;
}
