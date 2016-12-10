package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ygg.admin.entity.SellerDeliverAreaTemplateEntity;

public interface SellerDeliverAreaDao
{
    
    /**
     * 查找商家配送地区模版
     * @param para
     * @return
     * @throws Exception
     */
    SellerDeliverAreaTemplateEntity findSellerDeliverAreaTemplateByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增商家配送地区模版
     * @param areaTemplate
     * @return
     * @throws Exception
     */
    int insertSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate)
        throws Exception;
    
    /**
     * 修改商家配送地区模版
     * @param areaTemplate
     * @return
     * @throws Exception
     */
    int updateSellerDeliverAreaTemplate(SellerDeliverAreaTemplateEntity areaTemplate)
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
     * 根据商家Id查找商家配送地区模版
     * @param sellerId
     * @return
     * @throws Exception
     */
    List<SellerDeliverAreaTemplateEntity> findSellerDeliverAreaTemplateBysid(int sellerId)
        throws Exception;
    
    /**
     * 查找所有商家配送地区模版
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllSellerDeliverAreaTemplate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计商家配送地区模版
     * @param para
     * @return
     * @throws Exception
     */
    int countSellerDeliverAreaTemplate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int countDeliverAreaTemplateFromProductBase(int id)
        throws Exception;
    
    int addCommonPostage(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllCommonPostage(Map<String, Object> para)
        throws Exception;
    
    int countCommonPostage(Map<String, Object> para)
        throws Exception;
    
    int updateCommonPostage(Map<String, Object> para)
        throws Exception;
    
    int insertRelationTemplateDeliverAreaAndExtraPostage(SellerDeliverAreaTemplateEntity areaTemplate, Set<String> provinceCodes)
        throws Exception;
    
    List<String> findRelationTemplateDeliverAreaProvinceCodeBytid(int tid)
        throws Exception;
    
    int deleteRelationTemplateDeliverAreaAndExtraPostage(SellerDeliverAreaTemplateEntity areaTemplate, Set<String> provinceCodes)
        throws Exception;
    
    int deleteRelationTemplateDeliverAreaAndExtraPostage(int id)
        throws Exception;
    
    int countExtraPostage(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findExtraPostage(Map<String, Object> para)
        throws Exception;
    
    int updateExtraPostage(Map<String, Object> para)
        throws Exception;
    
    Integer findCommonExtraPostage(String provinceCode)
        throws Exception;
    
    List<Map<String, Object>> findExtraPostageBytids(List<Integer> templateIds)
        throws Exception;
    
}
