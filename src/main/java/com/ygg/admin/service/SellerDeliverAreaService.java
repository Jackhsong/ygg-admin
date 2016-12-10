package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SellerDeliverAreaTemplateEntity;

public interface SellerDeliverAreaService
{
    /**
     * 查找商家配送地区模版
     * @param name
     * @return
     * @throws Exception
     */
    SellerDeliverAreaTemplateEntity findSellerDeliverAreaTemplateByName(String name)
        throws Exception;
    
    /**
     * 新增商家配送地区模板
     * @param areaTemplate
     * @param areaCodes
     * @param other
     * @return
     * @throws Exception
     */
    int insertSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate, String areaCodes, String other)
        throws Exception;
    
    /**
     * 修改商家配送地区模版
     * @param areaTemplate
     * @param areaCodes
     * @param other
     * @return
     * @throws Exception
     */
    int updateSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate, String areaCodes, String other)
        throws Exception;
    
    /**
     * 删除商家配送地区模版
     * @param id
     * @return
     * @throws Exception
     */
    int deleteSellerDeliverAreaTemplate(int id)
        throws Exception;
    
    /**
     * 根据商家ID查找商家配送地区模版
     * @param sellerId
     * @return
     * @throws Exception
     */
    List<SellerDeliverAreaTemplateEntity> findSellerDeliverAreaTemplateBysid(int sellerId)
        throws Exception;
    
    /**
     * 异步加载商家配送地区模板
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonSellerDeliverAreaTemplate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计基本商品使用模版
     * @param id
     * @return
     * @throws Exception
     */
    int countDeliverAreaTemplateFromProductBase(int id)
        throws Exception;
    
    /**
     * 查找商家配送地区模版跟关联地区
     * @param id
     * @return
     * @throws Exception
     */
    SellerDeliverAreaTemplateEntity getSellerDeliverAreaTemplateAndRelationArea(int id)
        throws Exception;
    
    /**
     * 根据模版Id查找模版
     * @param id
     * @return
     * @throws Exception
     */
    SellerDeliverAreaTemplateEntity findSellerDeliverAreaTemplateById(int id)
        throws Exception;
    
    String addCommonPostage(String provinceCode, int freightMoney)
        throws Exception;
    
    Map<String, Object> jsonCommonPostage(Map<String, Object> para)
        throws Exception;
    
    String updateCommonPostage(int id, int freightMoney)
        throws Exception;
    
    Map<String, Object> jsonExtraPostage(Map<String, Object> para)
        throws Exception;
    
    String updateExtraPostage(int id, int isExtra, int freightMoney)
        throws Exception;
    
    int findCommonExtraPostage(String provinceCode)
        throws Exception;
    
}
