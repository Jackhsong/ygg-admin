package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface ProductBlacklistService
{
    /**
     * 查询商品黑名单
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增商品黑名单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int add(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除商品黑名单
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int delete(Map<String, Object> para)
        throws Exception;
    
    /**
     * 检查商品黑名单是否已经添加
     * 
     * @param para
     * @return
     * @throws Exception
     */
    boolean exist(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修正不发积分的订单数据
     * @return
     * @throws Exception
     */
    Map<String, Object> reduceOrderProductBlacklist()
        throws Exception;
}
