package com.ygg.admin.service.yw.brand;

import java.util.Map;

import com.ygg.admin.entity.yw.YwBrandCategoryEntity;


public interface YwBrandCategoryService
{
    /**
     *新增品牌栏目
     */
    int addBrandCategory(YwBrandCategoryEntity category)
            throws Exception;
    
    /**
     *更新品牌栏目
     */
    int updateBrandCategory(YwBrandCategoryEntity category)
            throws Exception;
    
    /**
     *品牌栏目列表
     */
    Map<String,Object> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception;
}
