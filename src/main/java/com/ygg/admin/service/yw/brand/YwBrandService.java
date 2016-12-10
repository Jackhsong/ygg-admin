package com.ygg.admin.service.yw.brand;

import java.util.Map;

import com.ygg.admin.entity.yw.YwBrandEntity;

public interface YwBrandService
{

    /**
     *新增品牌
     */
    int addBrand(YwBrandEntity brand)
            throws Exception;
    
    /**
     *更新品牌
     */
    int updateBrand(YwBrandEntity brand)
            throws Exception;
    
    /**
     *品牌列表
     */
    Map<String,Object> findBrandInfo(Map<String,Object> para)
            throws Exception;
    
    
    /**
     *更新品牌展现与否
     */
    int updateBrandDisplay(YwBrandEntity brand)
            throws Exception;
    
    /**
     *获取品牌馆名称
     */
    String getBrandCategoryName(int categoryId)
            throws Exception;

}
