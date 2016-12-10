package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface AnalyzeDao
{
    
    /**
     * 查询城市省份成交订单信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProvinceAndCityTurnOver(Map<String, Object> para)
        throws Exception;
    
    /**
     * 商品(特卖/商城)成交统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductTurnOverAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找具有完整分类的商品成交信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllFullCategoryProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找只有二级分类的商品成交信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> finSecondCategoryProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询没有分类的商品成交信息
     * @param para
     * @return
     */
    List<Map<String, Object>> findNoCategoryProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计以一级分类
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllFirstCategoryProduct(Map<String, Object> para)
        throws Exception;
    
}
