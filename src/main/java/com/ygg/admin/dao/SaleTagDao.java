package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SaleTagEntity;

public interface SaleTagDao
{
    /**
     * 根据ID查询特卖标签
     * 
     * @param id
     * @return
     * @throws Exception
     */
    SaleTagEntity findSaleTagById(int id)
        throws Exception;
    
    /**
     * 保存特卖标签
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int save(SaleTagEntity entity)
        throws Exception;
    
    /**
     * 更新特卖标签
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int update(SaleTagEntity entity)
        throws Exception;
    
    /**
     * 查询特卖标签
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<SaleTagEntity> findAllSaleTag(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计特卖标签数量
     * 
     * @return
     * @throws Exception
     */
    int countSaleTag(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入特卖位与特卖标签关联信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveRelation(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据特卖ID删除该特卖所关联的特卖标签
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int deleteAllRelationBySaleWindowId(int id)
        throws Exception;
    
    /**
     * 根据特卖ID查询该特卖所关联的特卖标签
     * 
     * @param id
     * @return
     * @throws Exception
     */
    List<Integer> findTagIdsBySaleWindowId(int id)
        throws Exception;
}
