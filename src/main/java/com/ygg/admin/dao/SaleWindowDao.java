package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SaleWindowEntity;

public interface SaleWindowDao
{
    
    int save(SaleWindowEntity saleWindow)
        throws Exception;
    
    int update(SaleWindowEntity saleWindow)
        throws Exception;
    
    int countSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<SaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception;
    
    SaleWindowEntity findSaleWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    int updateLaterOrder(Map<String, Object> para)
        throws Exception;
    
    int updateNowOrder(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    int hideSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有明日即将开始特卖
     * 
     * @param startTime
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllTomorrowSaleWindow(int startTime)
        throws Exception;
    
    /**
     * 查找当前在售特卖
     * 
     * @param currentTime
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllCurrentSaleWindow(int currentTime)
        throws Exception;
    
    /**
     * 根据条件查找所有类型为单品的特卖Id
     * 
     * @param parameter
     * @return
     * @throws Exception
     */
    List<Integer> findSaleWindowSingleIdsByPara(Map<String, Object> parameter)
        throws Exception;
    
    /**
     * 根据条件查找所有类型为组合的特卖Id
     * 
     * @param parameter
     * @return
     * @throws Exception
     */
    List<Integer> findSaleWindowGroupIdsByPara(Map<String, Object> parameter)
        throws Exception;
    
    /**
     * 根据条件查找所有特卖
     * 
     * @param params
     * @return
     * @throws Exception
     */
    List<SaleWindowEntity> findAllSaleWindowByPara(Map<String, Object> params)
        throws Exception;
    
    /**
     * 根据特卖id查找一级分类名称
     */
    List<Map<String, Object>> findCategoryFirstNamesBySwids(List<Integer> ids)
        throws Exception;
    
    /**
     * 根据单品特卖Id批量查询商家名称
     */
    List<Map<String, Object>> findSellerNameBySingleSwids(List<Integer> ids)
        throws Exception;
    
    /**
     * 根据组合特卖Id批量查询商家名称
     * 
     * @param ids
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSellerNameByGroupSwids(List<Integer> ids)
        throws Exception;
    
    /**
     * 根据单品特卖Id批量查询单品库存
     * 
     * @param ids
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findStockBySingleSwids(List<Integer> ids)
        throws Exception;
    
    List<SaleWindowEntity> findSaleWindowListByPara(Map<String, Object> params)
        throws Exception;
    
    int countSaleWindowByPara(Map<String, Object> params)
        throws Exception;
    
    /**
     * 根据单品特卖Id批量查询基本商品Id
     * 
     * @param ids
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductBasesBySingleswids(List<Integer> ids)
        throws Exception;
    
    /**
     * 根据自定义活动特卖id查询自定义活动链接
     * 
     * @param ids
     * @return
     */
    List<Map<String, Object>> findCustomActivityShareUrlBySwids(List<Integer> ids)
        throws Exception;
    
    /**
     * 根据条件查找特卖
     * 
     * @param params
     * @return
     * @throws Exception
     */
    SaleWindowEntity findSaleWindowByPara(Map<String, Object> params)
        throws Exception;
    
}