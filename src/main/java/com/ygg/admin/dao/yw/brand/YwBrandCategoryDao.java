package com.ygg.admin.dao.yw.brand;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.yw.YwBrandCategoryEntity;

public interface YwBrandCategoryDao
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
    List<YwBrandCategoryEntity> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception;
    
    /**
     *count品牌栏目
     */
    int countBrandCategoryInfo()
            throws Exception;
}
