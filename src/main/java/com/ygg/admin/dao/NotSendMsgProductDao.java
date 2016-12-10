package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface NotSendMsgProductDao
{
    /**
     * 查询所有不发短信的商品Id
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> queryAllProductId()
        throws Exception;
    
    /**
     * 新增不发短信商品
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int add(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除不发短信商品
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int delete(Map<String, Object> para)
        throws Exception;
    
    /**
     * 检查商品是否存在
     * 
     * @param productId
     * @return
     * @throws Exception
     */
    boolean checkIsExist(String productId)
        throws Exception;
    
    /**
     * 根据商品Id查询商品是否是不发短信商品
     * 
     * @param orderForProductIdList
     * @return
     */
    List<Integer> findProductById(List<Integer> orderForProductIdList)
        throws Exception;
}
