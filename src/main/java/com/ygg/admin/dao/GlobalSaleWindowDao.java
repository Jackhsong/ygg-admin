package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.GlobalSaleWindowEntity;

public interface GlobalSaleWindowDao
{
    
    int save(GlobalSaleWindowEntity saleWindow)
        throws Exception;
    
    int update(GlobalSaleWindowEntity saleWindow)
        throws Exception;
    
    int countSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<GlobalSaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception;
    
    GlobalSaleWindowEntity findSaleWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    int hideSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception;
    
    List<GlobalSaleWindowEntity> findAllSingleSaleWindow(Map<String, Object> para)
        throws Exception;
    
    int countSingleSaleWindow(Map<String, Object> para)
        throws Exception;
    
    List<GlobalSaleWindowEntity> findAllGroupSaleWinodw(Map<String, Object> para)
        throws Exception;
    
    int countGroupSaleWinodw(Map<String, Object> para)
        throws Exception;
}