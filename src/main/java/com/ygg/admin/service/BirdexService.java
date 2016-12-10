package com.ygg.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BirdexService
{
    Map<String, Object> findAllBirdexOrder(Map<String, Object> para)
        throws Exception;
    
    Map findBirdexProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增 or 更新 笨鸟商品导出信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertOrUpdateBirdexProductInfo(Map<String, Object> para)
        throws Exception;
    
    int deleteBirdexProductInfoById(int id)
        throws Exception;
    
    /**
     * 笨鸟订单 确认推送
     * @param orderIds
     * @return
     * @throws Exception
     */
    Map<String, Object> sendBirdex(List<Integer> idList)
        throws Exception;
    
    /**
     * 查找所有笨鸟发货商家
     * @return
     * @throws Exception
     */
    List<Integer> findBirdexSellerId()
        throws Exception;
    
    /**
     * 查找所有推送的笨鸟订单
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllBirdexOrderPushList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 取消笨鸟订单
     * @param para
     * @return
     * @throws Exception
     */
    String cancelBirdexOrder(Map<String, String> para)
        throws Exception;
    
    /**
     * 取消推送笨鸟订单
     * @param orderId
     * @param pushStatus
     * @return
     * @throws Exception
     */
    String cancelPushBirdexOrder(int orderId, int pushStatus)
        throws Exception;
    
    /**
     * 根据订单编号查找收货人信息
     * @param orderNumber
     * @return
     * @throws Exception
     */
    String findReceiveInfo(String orderNumber)
        throws Exception;
    
    /**
     * 修改收货地址
     * @param para
     * @return
     * @throws Exception
     */
    String updateAddress(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改身份证
     * @param para
     * @return
     * @throws Exception
     */
    String updateIdCard(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找笨鸟订单变更记录
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllBirdexOrderChangeList(Map<String, Object> para)
        throws Exception;
    
    String findAllBirdexProductList(Map<String, Object> para)
        throws Exception;
    
    String saveBirdexProduct(int productId, float price)
        throws Exception;
    
    String updateBirdexProduct(int id, int productId, float price)
        throws Exception;
    
    String deleteBirdexProduct(int id)
        throws Exception;
    
    String findAllBirdexStockList(Map<String, Object> para)
        throws Exception;
    
    String refreshBirdexStock()
        throws Exception;
    
    List<Map<String, Object>> findAllBirdexStock(HashMap<String, Object> para)
        throws Exception;
    
    /**
     * 查询所有已取消笨鸟订单
     * @param para
     * @return
     * @throws Exception
     */
    String findAllBirdexCancelOrder(Map<String, Object> para)
        throws Exception;
    
    String updateBirdexOrderConfirmPushStatus(int orderId)
        throws Exception;
}
