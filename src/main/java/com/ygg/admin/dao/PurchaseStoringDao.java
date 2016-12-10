package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface PurchaseStoringDao
{
    
    /**
     * 根据条件查询库存
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findPurchaseStoringByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据条件查询库存
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findPurchaseStoringByIds(List<Object> list)
        throws Exception;
    
    /**
     * 更新库存信息
     * @param param
     * @return
     * @throws Exception
     */
    int updatePurchaseStoringByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 已付款未推送商品信息
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findUnpushOrder()
        throws Exception;
    
    /**
     * 商品信息与采购商品之间的关联关系
     * @param list 商品ID
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProviderProduct(List<Object> list)
        throws Exception;
}
