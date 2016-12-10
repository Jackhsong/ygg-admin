package com.ygg.admin.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.entity.ThirdPartyProductEntity;

public interface ThirdPartyProductService
{
    
    String jsonThirdProductInfo(Map<String, Object> para)
        throws Exception;
    
    String saveThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception;
    
    String updateThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception;
    
    String importThirdPartyProduct(MultipartFile file)
        throws Exception;
    
    String updateThirdPartyProductStatus(String ids, int isAvailable)
        throws Exception;
    
    /**
     * 调库存
     * @param id：第三方商品ID
     * @param stock：库存
     * @param providerProductId：采购商品ID
     * @param groupCount：组合件数
     * @return
     * @throws Exception
     */
    String updateThirdPartyProductStock(int id, int stock, int providerProductId, int groupCount)
        throws Exception;
    
    String updateThirdPartyProductSales(int id, int sales)
        throws Exception;
    
}
