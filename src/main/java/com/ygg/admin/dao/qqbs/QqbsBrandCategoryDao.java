package com.ygg.admin.dao.qqbs;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.qqbs.QqbsBrandCategoryEntity;

public interface QqbsBrandCategoryDao
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
    List<QqbsBrandCategoryEntity> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception;
    
    /**
     *count品牌栏目
     */
    int countBrandCategoryInfo()
            throws Exception;
}
