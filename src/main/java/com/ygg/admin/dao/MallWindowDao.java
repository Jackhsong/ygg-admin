package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MallPageEntity;
import com.ygg.admin.entity.MallWindowEntity;

public interface MallWindowDao
{
    
    List<Map<String, Object>> findJsonMallWindowInfo(Map<String, Object> para)
        throws Exception;
    
    int countMallWindowInfo(Map<String, Object> para)
        throws Exception;
    
    int updateMallWindowByPara(Map<String, Object> para)
        throws Exception;
    
    int saveMallWindow(MallWindowEntity mallWindow)
        throws Exception;
    
    int getMallWindowMaxSequence()
        throws Exception;
    
    List<Map<String, Object>> findAllMallPage(Map<String, Object> para)
        throws Exception;
    
    List<MallWindowEntity> findMallWindowByPara(Map<String, Object> para)
        throws Exception;
    
    int countMallPageInfo(Map<String, Object> para)
        throws Exception;
    
    int saveMallPage(MallPageEntity mallPage)
        throws Exception;
    
    int updateMallPage(MallPageEntity mallPage)
        throws Exception;
    
    List<Map<String, Object>> findAllMallPageFloor(Map<String, Object> para)
        throws Exception;
    
    int countMallPageFloor(Map<String, Object> para)
        throws Exception;
    
    int savePageFloor(Map<String, Object> para)
        throws Exception;
    
    int updatePageFloor(Map<String, Object> para)
        throws Exception;
    
    int getPageFloorMaxSequence(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findJsonMallFloorProductInfo(Map<String, Object> para)
        throws Exception;
    
    int countMallFloorProductInfo(Map<String, Object> para)
        throws Exception;
    
    int updateFloorProductSequence(Map<String, Object> para)
        throws Exception;
    
    int deleteRelationMallPageFloorAndProduct(int id)
        throws Exception;
    
    List<Integer> findAllProductIdByMallPageId(int i)
        throws Exception;
    
    List<Map<String, Object>> findProductInfoForAdd(Map<String, Object> para)
        throws Exception;
    
    int countProductInfoForAdd(Map<String, Object> para)
        throws Exception;
    
    int findMaxSequenceRelationMallPageFloorAndProduct(int floorId)
        throws Exception;
    
    List<Integer> findAllProductIdByPageFloorId(int floorId)
        throws Exception;
    
    int saveRelationMallPageFloorAndProduct(Map<String, Object> map)
        throws Exception;
    
    List<Map<String, Object>> findAllCurrentHotProductInfo(Map<String, Object> para)
        throws Exception;
    
    int countCurrentHotProductInfo(Map<String, Object> para)
        throws Exception;
    
    int updateHotProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteHotProduct(Map<String, Object> result)
        throws Exception;
    
    List<Integer> findAllHotProductId()
        throws Exception;
    
    int findMaxSequenceProductHot()
        throws Exception;
    
    int saveProductHot(Map<String, Object> map)
        throws Exception;
    
    List<Map<String, Object>> findAllCurrentHotDisplayProductList(Map<String, Object> para)
        throws Exception;
    
    int countCurrentHotDisplayProduct(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findProductHotTimeFactor()
        throws Exception;
    
    List<Map<String, Object>> findMallProductInfo(String beginTime)
        throws Exception;
    
    Map<String, Object> findHotProductInfoById(Map<String, Object> para)
        throws Exception;
    
    int updateHotProductCustomFactor(Map<String, Object> para)
        throws Exception;
    
    int updateSaleTimeFactor(Map<String, Object> para)
        throws Exception;
    
    int countProductHotId(int productId)
        throws Exception;
    
    int updateHotProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
}
