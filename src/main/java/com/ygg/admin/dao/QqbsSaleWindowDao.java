package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.QqbsSaleWindowEntity;

public interface QqbsSaleWindowDao
{
    
    int save(QqbsSaleWindowEntity saleWindow)
        throws Exception;
    
    int update(QqbsSaleWindowEntity saleWindow)
        throws Exception;
    
    int countSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<QqbsSaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception;
    
    QqbsSaleWindowEntity findSaleWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    int hideSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    List<QqbsSaleWindowEntity> findAllSingleSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countSingleSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<QqbsSaleWindowEntity> findAllGroupSaleWinodw(Map<String, Object> para)
        throws Exception;
    
    int countGroupSaleWinodw(Map<String, Object> para)
        throws Exception;

    List<QqbsSaleWindowEntity> findAllSaleWindowByParam(Map<String, Object> para);

    int countAllSaleWindowByParam(Map<String, Object> para);

    int updateOrder(int id, int order);
}