package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ThirdPartyProductEntity;

public interface ThirdPartyProductDao
{
    
    List<Map<String, Object>> findAllThirdPartyProduct(Map<String, Object> para)
        throws Exception;
    
    int countThirdPartyProduct(Map<String, Object> para)
        throws Exception;
    
    int saveThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception;
    
    int updateThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception;
    
    int batchSaveThirdPartyProduct(List<ThirdPartyProductEntity> productList)
        throws Exception;
    
    int updateThirdPartyProductStatus(Map<String, Object> param)
        throws Exception;
    
    int updateThirdPartyProductStock(Map<String, Object> para)
        throws Exception;
    
    int updateThirdPartyProductSales(Map<String, Object> para)
        throws Exception;
    
}
