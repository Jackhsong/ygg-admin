package com.ygg.admin.service.qqbsbrand;

import java.util.Map;

import com.ygg.admin.entity.qqbs.QqbsBrandEntity;

public interface QqbsBrandService
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
    Map<String,Object> findBrandInfo(Map<String,Object> para)
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
