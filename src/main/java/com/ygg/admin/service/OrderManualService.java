package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderDetailInfoForSeller;

public interface OrderManualService
{
    /**
     * 检查productIdList 是否都是sellerId下的商品
     * 
     * @param sellerId
     * @param productIdList
     * @return
     * @throws Exception
     */
    public boolean validateProduct(int sellerId, List<Integer> productIdList)
        throws Exception;
    
    /**
     * 新增手动订单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public Map<String, Object> saveOrderManual(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新手动订单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public Map<String, Object> updateOrderManual(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询所有手动订单信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public Map<String, Object> findAllOrderManual(Map<String, Object> para)
        throws Exception;
    
    /**
     * 手动订单发货处理
     * 
     * @return
     * @throws Exception
     */
    public Map<String, Object> sendOrderManual(int orderId, String channel, String number)
        throws Exception;
    
    /**
     * 查询手动创建订单和商品信息
     * 
     * @param para
     * @return
     */
    public List<OrderDetailInfoForSeller> findAllOrderManualAndProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 手动订单详细信息
     * 
     * @param orderManualId
     * @return
     * @throws Exception
     */
    public Map<String, Object> findOrderDetailInfo(int orderManualId)
        throws Exception;
    
    /**
     * 导出商家发货表
     * 
     * @param para
     * @return
     */
    public String getExportForSeller(Map<String, Object> para)
        throws Exception;
    
    /**
     * 生成 客服操作链接
     * @param para
     * @return
     * @throws Exception
     */
    public Map<String, Object> addOverseasManualProduct(int sellerId, int productId, int nums, String remark)
        throws Exception;
    
    /**
     * 查找手动订单商品列表
     * @param page：页数
     * @param rows：每页条数
     * @param code：商品编码
     * @param name：商品名称
     * @return
     * @throws Exception
     */
    public String jsonOverseasManualProduct(int page, int rows, String code, String name)
        throws Exception;
    
}
