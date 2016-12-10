package com.ygg.admin.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.entity.DetailPicAndText;
import com.ygg.admin.entity.MwebGroupProductEntity;

public interface MwebGroupProductService
{
    
    public JSONArray findProductAndStock(Map<String, Object> parameter)
        throws Exception;
        
    public JSONObject findProductAndStock2(Map<String, Object> parameter)
        throws Exception;
        
    public int addProduct(MwebGroupProductEntity mwebGroupProductEntity, int stock, DetailPicAndText detailPicAndText, int baseId)
        throws Exception;
        
    public JSONObject getProduct(int id)
        throws Exception;
        
    public JSONObject updateProduct(MwebGroupProductEntity mwebGroupProductEntity)
        throws Exception;
        
    public int forSale(Map<String, Object> para)
        throws Exception;
        
    public int updateOrder(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据条件查询团购管理列表
     * 
     * @param parameter
     * @return
     */
    JSONObject findProductAndStockForTeam(Map<String, Object> parameter)
        throws Exception;
        
    int updateProductForTeam(Map<String, Object> parameter)
        throws Exception;
        
    JSONObject findProductAndStockForTeamById(Map<String, Object> parameter)
        throws Exception;
        
    public MwebGroupProductEntity getProductById(int id)
        throws Exception;
}
