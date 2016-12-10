package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface NotSendMsgProductService
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
     * 检查商品是否已经添加
     * 
     * @param productId
     * @return
     * @throws Exception
     */
    boolean checkIsExist(String productId)
        throws Exception;
}
