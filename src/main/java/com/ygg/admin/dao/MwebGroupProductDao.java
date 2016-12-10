package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.MwebGroupProductEntity;
import com.ygg.admin.entity.ProductEntity;

public interface MwebGroupProductDao
{
    public List<JSONObject> findProductAndStock(Map<String, Object> parameter);
    
    int countProductAndStock(Map<String, Object> parameter);
    
    public List<MwebGroupProductEntity> findProduct(MwebGroupProductEntity mwebGroupProductEntity);
    
    public int addProduct(MwebGroupProductEntity mwebGroupProductEntity);
    
    public int updateProduct(MwebGroupProductEntity mwebGroupProductEntity);
    
    // 更新mwebGroupProduct 上下架信息
    public int forSale(Map<String, Object> para)
        throws Exception;
        
    // 更新mwebGroupProduct 排序值
    public int updateOrder(Map<String, Object> para)
        throws Exception;
        
    public int addProductEntity(ProductEntity productEntity);
    
    public int updateProductEntity(ProductEntity productEntity);
    
    public int updateRelationProductByPara(Map<String, Object> map)
        throws Exception;
        
    List<JSONObject> findProductAndStockForTeam(Map<String, Object> parameter)
        throws Exception;
        
    int countProductAndStockForTeam(Map<String, Object> parameter)
        throws Exception;
        
    int updateProductForTeam(Map<String, Object> parameter)
        throws Exception;
        
    public MwebGroupProductEntity getProduct(int id)
        throws Exception;
        
    public MwebGroupProductEntity getProductByProductId(int id)
        throws Exception;
}
