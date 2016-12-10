package com.ygg.admin.dao;

import com.ygg.admin.entity.BrandEntity;

import java.util.List;
import java.util.Map;

public interface BrandDao
{
    
    /**
     * 插入品牌信息
     * 
     * @param brand
     * @return
     * @throws Exception
     */
    int save(BrandEntity brand)
        throws Exception;

    int saveRelationBrandCategory(Map<String, Object> para) throws Exception;

    int deleteRelationBrandCategory(Map<String, Object> para) ;
    
    /**
     * 根据para查询brand信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<BrandEntity> findAllBrandByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查询brand信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    BrandEntity findBrandById(int id)
        throws Exception;
    
    /**
     * 根据para更新brand信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateBrandByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询brand数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countBrandByPara(Map<String, Object> para)
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
    public int update(Map<String, Object> param)
            throws Exception;

    /**
     * 查询关联的分类
     */
    List<Map<String, Object>> findRelationBrandCategory(Map<String, Object> para);

    List<Integer> findRelationBrandCategoryIdsByBrandId(int brandId);
}
