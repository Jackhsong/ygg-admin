package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderManualEntity;

public interface OrderManualDao
{
    /**
     * 插入手动订单
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    public int insertOrderManual(OrderManualEntity entity)
        throws Exception;
    
    /**
     * 更新手动订单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int updateOrderManual(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入订单商品数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int insertOrderManualProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询所有手动订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public List<OrderManualEntity> findAllOrderManual(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计 所有手动订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int countOrderManual(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查询手动订单信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public OrderManualEntity findOrderManualById(int id)
        throws Exception;
    
    /**
     * 根据手动订单ID查询商品信息
     * 
     * @param manualId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findAllProductInfoByOrderManualId(int manualId)
        throws Exception;
    
    /**
     * 查询手动创创建订单和商品
     * 
     * @param para
     * @return
     */
    public List<Map<String, Object>> findAllOrderManualAndProduct(Map<String, Object> para)
        throws Exception;
    
    int findOrderManualIdByNumber(long number)
        throws Exception;
    
    Map<String, Object> findOrderManualSettlementByNumber(long number)
        throws Exception;
    
    int insertOrderManualSettlement(Map<String, Object> para)
        throws Exception;
    
    int deleteOrderManualSettlement(int orderManualId)
        throws Exception;
    
    int updateOrderProductCost(Map<String, Object> para)
        throws Exception;
    
    int addOverseasManualProduct(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findOverseasManualProduct(Map<String, Object> para)
        throws Exception;
    
    int countOverseasManualProduct(Map<String, Object> para)
        throws Exception;
}
