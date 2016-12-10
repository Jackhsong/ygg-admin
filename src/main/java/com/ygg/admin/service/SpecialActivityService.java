package com.ygg.admin.service;

import com.ygg.admin.entity.SpecialActivityEntity;

import java.util.List;
import java.util.Map;

public interface SpecialActivityService
{
    /**
     * 查找特卖活动
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonSpecialActivityInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改特卖活动
     * @param sae
     * @return
     * @throws Exception
     */
    int saveOrUpdateSpecialActivity(SpecialActivityEntity sae)
        throws Exception;
    
    /**
     * 修改特卖活动展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityAvailableStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找特卖活动
     * @param activityId
     * @return
     * @throws Exception
     */
    Map<String, Object> findSpecialActivityById(int activityId)
        throws Exception;
    
    /**
     * 特卖活动板块列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonActivityLayoutInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改特卖活动板块
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdateSpecialActivityLayout(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改特卖活动板块排序值
     * @param para
     * @return
     */
    int updateSpecialActivityLayoutSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改特卖活动板块展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityLayoutDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找板块信息
     * @param layoutId
     * @return
     * @throws Exception
     */
    Map<String, Object> findSpecialActivityLayout(int layoutId)
        throws Exception;
    
    /**
     * 板块商品布局
     * @param para
     * @return
     */
    Map<String, Object> jsonSpecialActivityLayoutProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改布局排序
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityLayoutProductSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更改展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityLayoutProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找板块商品布局
     * @param layoutProductId
     * @return
     */
    Map<String, Object> findSpecialActivityLayoutProduct(int layoutProductId)
        throws Exception;
    
    /**
     * 新增/修改商品布局
     * @param para
     * @return
     */
    int saveOrUpdateSpecialActivityLayoutProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有可用特卖活动
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSpecialActivity()
        throws Exception;

    /**
     * 快速添加布局为左右布局的商品
     */
    int saveByQuickAdd(List<List<Integer>> idsPartition, int layoutId) throws Exception;
    
}
