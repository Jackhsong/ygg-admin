package com.ygg.admin.dao.yw.sale;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.yw.YwSaleEntity;

public interface YwSaleDao
{
    
    int save(YwSaleEntity ywSale)
        throws Exception;
    
    int update(YwSaleEntity ywSale)
        throws Exception;
    
    int countSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<YwSaleEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception;
    
    YwSaleEntity findSaleWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    int hideSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    List<YwSaleEntity> findAllSingleSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countSingleSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<YwSaleEntity> findAllGroupSaleWinodw(Map<String, Object> para)
        throws Exception;
    
    int countGroupSaleWinodw(Map<String, Object> para)
        throws Exception;

    List<YwSaleEntity> findAllSaleWindowByParam(Map<String, Object> para);

    int countAllSaleWindowByParam(Map<String, Object> para);

    int updateOrder(int id, int order);
}