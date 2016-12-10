package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SaleTagEntity;

public interface SaleTagService
{
    /**
     * 保存saleTag信息
     * 
     * @param brand
     * @return
     * @throws Exception
     */
    int saveOrUpdate(SaleTagEntity saleTag)
        throws Exception;
    
    /**
     * 根据para查询特卖标签信息，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonSaleTagInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据特卖ID查询特码信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    SaleTagEntity findSaleTagById(int id)
        throws Exception;
    
    /**
     * 查询特卖标签
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<SaleTagEntity> findAllSaleTag(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询SaleWindow所关联的tagids
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public List<Integer> findTagIdsBySaleWindowId(int id)
        throws Exception;
    
    public void saveSaleWindowAndTag(Map<String, Object> para)
        throws Exception;
}
