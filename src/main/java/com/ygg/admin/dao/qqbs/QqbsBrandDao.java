package com.ygg.admin.dao.qqbs;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.qqbs.QqbsBrandEntity;

public interface QqbsBrandDao
{
    /**
     *新增品牌
     */
    int addBrand(QqbsBrandEntity brand)
            throws Exception;
    
    /**
     *更新品牌
     */
    int updateBrand(QqbsBrandEntity brand)
            throws Exception;
    
    /**
     *品牌列表
     */
    List<QqbsBrandEntity> findBrandInfo(Map<String,Object> para)
            throws Exception;
    
    /**
     *count品牌
     */
    int countBrandInfo(Map<String, Object> para)
            throws Exception;
    
    /**
     *更新品牌展现与否
     */
    int updateBrandDisplay(QqbsBrandEntity brand)
            throws Exception;
    
    /**
     *获取品牌馆名称
     */
    String getBrandCategoryName(int categoryId)
            throws Exception;
}
