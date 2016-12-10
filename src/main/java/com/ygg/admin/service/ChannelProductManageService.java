package com.ygg.admin.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.entity.ChannelProductEntity;

public interface ChannelProductManageService
{
    
    /**
     *新增第三方商品
     */
    Map<String, Object> addChannelProduct(ChannelProductEntity product)
            throws Exception;
    
    /**
     *更新第三方商品
     */
    Map<String, Object> updateChannelProduct(ChannelProductEntity product)
            throws Exception;
    
    /**
     *获取商品名字 
     */
    Map<String, Object> getProductName(Map<String, Object> para)
            throws Exception;
    /**
     * 获取商品列表
     */
    Map<String, Object> jsonChannelProInfo(Map<String, Object> para)
            throws Exception;
    
    /**导入第三方商品*/
    Map<String, Object> uploadChannelProFile(MultipartFile file) 
            throws Exception;
    
}
