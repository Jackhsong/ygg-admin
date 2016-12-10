package com.ygg.admin.service;

import com.ygg.admin.entity.PageModelBannerEntity;
import com.ygg.admin.entity.PageModelCustomLayoutEntity;
import com.ygg.admin.entity.PageModelEntity;

import java.util.List;
import java.util.Map;

/**
 * 自定义拼装页面 service
 */
public interface PageService
{
    /** 根据id查询自定义拼装页面 */
    Map<String, Object> findPageById(int id)
        throws Exception;

    /** 根据para查询所有自定义拼装页面 */
    Map<String, Object> findAllPageByPara(Map<String, Object> para)
        throws Exception;

    /** 新增 or 更新 页面 */
    int insertOrUpdatePage(Map<String,Object> para)
        throws Exception;

    /** 根据页面ID查询模块信息 */
    Map<String, Object> findPageModelByPara(Map<String,Object> para)
        throws Exception;

    /** 新增 更新 页面模块 */
    int insertOrUpdatePageModel(Map<String,Object> para)
        throws Exception;

    /** 根据ID查询模块信息 */
    PageModelEntity findPageModelById(int id)
        throws Exception;

    /** 查询自定义的模块信息 */
    Map<String,Object> findModelCustomLayoutInfo(int modelId)
        throws Exception;

    /** 查询自定义的模块信息 */
    PageModelCustomLayoutEntity findCustomLayoutInfoById(int id)
        throws Exception;

    /** 新增 or 更新 自定义的模块 */
    int insertOrUpdatePageModelCustomLayout(PageModelCustomLayoutEntity entity)
        throws Exception;

    /** 新增 or 更新 自定义的模块 */
    int updatePageModelCustomLayoutSimpleData(Map<String,Object> para)
        throws Exception;

    /** 查询 模块滚动商品信息 */
    Map<String,Object> findModelRollProductInfo(Map<String,Object> para)
        throws Exception;

    /** 更新 模块滚动商品*/
    int updatePageModelRelationRollProduct(Map<String,Object> para)
        throws Exception;

    /** 批量删除滚动商品 */
    int deletePageModelRelationRollProduct(List<Integer> list)
        throws Exception;

    /** 批量插入滚动商品 */
    int insertBatchPageModelRelationRollProduct(List<Map<String, Object>> list, int pageModelId)
        throws Exception;

    /** 查询 模块两栏商品信息 */
    Map<String,Object> findModelColumnProductInfo(Map<String,Object> para)
        throws Exception;

    /** 更新 模块两栏商品*/
    int updatePageModelRelationColumnProduct(Map<String,Object> para)
        throws Exception;

    /** 批量删除两栏商品 */
    int deletePageModelRelationColumnProduct(List<Integer> list)
        throws Exception;

    /** 批量插入两栏商品 */
    int insertBatchPageModelRelationColumnProduct(List<Map<String, Object>> list, int pageModelId)
        throws Exception;

    /** 查询模块管理banner */
    Map<String, Object> findAllPageModelBanner(Map<String,Object> para)
        throws Exception;

    /** 新增 or 更新 模块Banner*/
    int insertOrUpdatePageModelBanner(PageModelBannerEntity entity)
        throws Exception;

    int updateSimplePageModelBanner(Map<String,Object> para)
        throws Exception;

    /** 根据ID查询banner */
    PageModelBannerEntity findPageModelBannerById(int id)
        throws Exception;

    /** 查询模块对应的可供添加的商品列表 */
    Map<String,Object> findProductListForAdd(Map<String,Object> para)
        throws Exception;
}
