package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.BrandEntity;

public interface BrandService
{
    
    /**
     * 保存brand信息
     * 
     * @param brand
     * @return
     * @throws Exception
     */
    int saveOrUpdate(BrandEntity brand)
        throws Exception;
    
    /**
     * 根据para查询品牌，并封装成json字符串返回
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonBrandInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查找BrandEntity
     * 
     * @param id
     * @return
     * @throws Exception
     */
    BrandEntity findBrandById(int id)
        throws Exception;
    
    /**
     * 查询可用的品牌信息
     * 
     * @return
     * @throws Exception
     */
    List<BrandEntity> findBrandIsAvailable()
        throws Exception;
    
    /**
     * 查询所有品牌信息
     * 
     * @return
     * @throws Exception
     */
    List<BrandEntity> findAllBrand(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Name查询brand数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countBrandByName(Map<String, Object> para)
        throws Exception;
    public int delete(int id) throws Exception;
    public int updateInfo(Map<String, Object> param) throws Exception;
}
