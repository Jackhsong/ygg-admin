package com.ygg.admin.service;

import java.util.Map;

public interface GroupBuyService
{
    /**
     * 团购商品 统计
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> analyzeGroupProduct(Map<String, Object> para)
        throws Exception;
}
