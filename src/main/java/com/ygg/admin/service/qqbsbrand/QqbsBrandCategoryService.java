package com.ygg.admin.service.qqbsbrand;

import java.util.Map;

import com.ygg.admin.entity.qqbs.QqbsBrandCategoryEntity;

public interface QqbsBrandCategoryService
{
    /**
     *新增品牌栏目
     */
    int addBrandCategory(QqbsBrandCategoryEntity category)
            throws Exception;
    
    /**
     *更新品牌栏目
     */
    int updateBrandCategory(QqbsBrandCategoryEntity category)
            throws Exception;
    
    /**
     *品牌栏目列表
     */
    Map<String,Object> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception;
}
