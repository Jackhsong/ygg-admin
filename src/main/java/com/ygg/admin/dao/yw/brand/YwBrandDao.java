package com.ygg.admin.dao.yw.brand;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.yw.YwBrandEntity;

public interface YwBrandDao
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
    List<YwBrandEntity> findBrandInfo(Map<String,Object> para)
            throws Exception;
    
    /**
     *count品牌
     */
    int countBrandInfo(Map<String, Object> para)
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
