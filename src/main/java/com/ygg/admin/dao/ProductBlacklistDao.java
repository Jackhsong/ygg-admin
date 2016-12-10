package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface ProductBlacklistDao
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
     * count 查询商品黑名单
     * 
     * @return
     * @throws Exception
     */
    int countAllProduct(Map<String, Object> para)
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
     * 检查商品黑名单是否存在
     * 
     * @param productId
     * @return
     * @throws Exception
     */
    boolean exist(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findReduceOrderId()
        throws Exception;
}
