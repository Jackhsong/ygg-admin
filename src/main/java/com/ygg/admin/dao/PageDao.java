package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.PageEntity;
import com.ygg.admin.entity.PageModelBannerEntity;
import com.ygg.admin.entity.PageModelCustomLayoutEntity;
import com.ygg.admin.entity.PageModelEntity;

/**
 * 自定义拼装页面 DAO
 */
public interface PageDao
{
    /** 根据para查询所有自定义拼装页面 */
    List<Map<String, Object>> findAllPageByPara(Map<String, Object> para)
        throws Exception;
    
    /** 统计自定义拼装页面总数 */
    int countAllPageByPara(Map<String, Object> para)
        throws Exception;
    
    /** 根据id查询自定义拼装页面 */
    Map<String, Object> findPageById(int id)
        throws Exception;
    
    /** 插入自定义拼装页面 */
    int insertPage(Map<String, Object> para)
        throws Exception;
    
    /** 更新自定义拼装页面信息 */
    int updatePageById(Map<String, Object> para)
        throws Exception;
    
    /** 根据Para查询页面模块信息 */
    List<PageModelEntity> findAllPageModelByPara(Map<String, Object> para)
        throws Exception;
    
    /** 根据ID查询模块信息 */
    PageModelEntity findPageModelById(int id)
        throws Exception;
    
    /** 插入页面模块 */
    int insertPageModel(Map<String, Object> para)
        throws Exception;
    
    /** 更新页面模块 */
    int updatePageModelById(Map<String, Object> para)
        throws Exception;
    
    /** 根据模块ID查询自定义模块内容 */
    List<PageModelCustomLayoutEntity> findAllPageModelCustomLayoutByModelId(int modelId)
        throws Exception;
    
    int countAllPageModelCustomLayoutByModelId(int modelId)
        throws Exception;
    
    int findPageModelCustomLayoutMaxSequence(int modelId)
        throws Exception;
    
    /** 根据ID查询自定义模块内容 */
    PageModelCustomLayoutEntity findPageModelCustomLayoutById(int id)
        throws Exception;
    
    /** 插入自定义模块内容 */
    int insertPageModelCustomLayout(PageModelCustomLayoutEntity e)
        throws Exception;
    
    /** 更新自定义模块内容 */
    int updatePageModelCustomLayout(PageModelCustomLayoutEntity e)
        throws Exception;
    
    /** 更新自定义模块部分内容 */
    int updatePageModelCustomLayoutSimpleData(Map<String, Object> para)
        throws Exception;
    
    /** 批量插入滚动商品 */
    int insertBatchPageModelRelationRollProduct(List<Map<String, Object>> list)
        throws Exception;
    
    /** 更新滚动商品信息 */
    int updatePageModelRelationRollProduct(Map<String, Object> para)
        throws Exception;
    
    /** 批量删除滚动商品 */
    int deletePageModelRelationRollProduct(List<Integer> list)
        throws Exception;
    
    /** 查询滚动商品信息 */
    List<Map<String, Object>> findAllPageModelRelationRollProduct(Map<String, Object> para)
        throws Exception;
    
    /** 统计滚动商品总数 */
    int countAllPageModelRelationRollProduct(Map<String, Object> para)
        throws Exception;
    
    /** 查询最大sequence */
    int findMaxSequencePageModelRelationRollProduct(int pageModelId)
        throws Exception;
    
    /** 批量插入两栏商品 */
    int insertBatchPageModelRelationColumnProduct(List<Map<String, Object>> list)
        throws Exception;
    
    /** 更新两栏商品信息 */
    int updatePageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception;
    
    /** 批量删除两栏商品 */
    int deletePageModelRelationColumnProduct(List<Integer> list)
        throws Exception;
    
    /** 查询两栏商品信息 */
    List<Map<String, Object>> findAllPageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception;
    
    /** 统计两栏商品总数 */
    int countAllPageModelRelationColumnProduct(Map<String, Object> para)
        throws Exception;
    
    /** 查询最大sequence */
    int findMaxSequencePageModelRelationColumnProduct(int pageModelId)
        throws Exception;
    
    /** 插入模块关联banner */
    int insertPageModelBanner(PageModelBannerEntity entity)
        throws Exception;
    
    /** 更新模块关联banner */
    int updatePageModelBanner(PageModelBannerEntity entity)
        throws Exception;
    
    /** 更新banner展现状态 */
    int updateSimplePageModelBanner(Map<String, Object> para)
        throws Exception;
    
    /** 查询banner */
    List<PageModelBannerEntity> findAllPageModelBanner(Map<String, Object> para)
        throws Exception;
    
    /** 统计banner */
    int countAllPageModelBanner(Map<String, Object> para)
        throws Exception;
    
    /** 根据ID查询banner */
    PageModelBannerEntity findPageModelBannerById(int id)
        throws Exception;
    
    PageEntity findPageBypid(int id)
        throws Exception;
    
    List<PageModelEntity> findPageModelBypid(int id)
        throws Exception;
    
    List<PageModelCustomLayoutEntity> findPageModelCustomLayoutByModelId(int id)
        throws Exception;
}