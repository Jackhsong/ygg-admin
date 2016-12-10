package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MallPageEntity;
import com.ygg.admin.entity.MallWindowEntity;

public interface MallWindowService
{
    
    Map<String, Object> findAllMallWindow(Map<String, Object> para)
        throws Exception;
    
    int updateMallWindowByPara(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdate(MallWindowEntity mallWindow)
        throws Exception;
    
    List<Map<String, Object>> findAllMallPage(Map<String, Object> para)
        throws Exception;
    
    MallWindowEntity findMallWindowById(int id)
        throws Exception;
    
    Map<String, Object> findAllMallPageJsonInfo(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateMallPage(MallPageEntity mallPage)
        throws Exception;
    
    MallPageEntity findAllMallPageById(int id)
        throws Exception;
    
    Map<String, Object> findAllMallPageFloorJsonInfo(Map<String, Object> para)
        throws Exception;
    
    int updatePageFloorByPara(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdatePageFloor(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonMallFloorProductInfo(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findMallPageFloorCode(Map<String, Object> para)
        throws Exception;
    
    int updateFloorProductSequence(Map<String, Object> para)
        throws Exception;
    
    int deleteRelationMallPageFloorAndProduct(int id)
        throws Exception;
    
    int deleteRelationMallPageFloorAndProductList(List<Integer> idList)
        throws Exception;
    
    Map<String, Object> jsonProductListForAdd(Map<String, Object> para)
        throws Exception;
    
    int addProductForPageFloor(Map<String, Object> map)
        throws Exception;
    
    Map<String, Object> findAllCurrentHotProductList(Map<String, Object> para)
        throws Exception;
    
    int updateHotProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteHotProduct(Map<String, Object> result)
        throws Exception;
    
    Map<String, Object> jsonHotProductListForAdd(Map<String, Object> para)
        throws Exception;
    
    int addProductForHotProduct(Map<String, Object> map)
        throws Exception;
    
    Map<String, Object> findAllCurrentHotDisplayProductList(Map<String, Object> para)
        throws Exception;
    
    int updateHotProductCustomFactor(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> jsonSaleTimeDiscount()
        throws Exception;
    
    int updateSaleTimeFactor(Map<String, Object> para)
        throws Exception;
    
    int updateHotProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> quickAddProduct(List<Integer> idList)
        throws Exception;
    
    /**
     * 添加明日特卖商品到最热商品池
     * @param selectedDate ：所选日期
     * @return
     * @throws Exception
     */
    int addSaleWindowToHotProduct(String selectedDate)
        throws Exception;
    
}
