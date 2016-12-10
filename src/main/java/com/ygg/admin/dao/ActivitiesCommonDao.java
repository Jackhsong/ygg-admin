package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ProductInfoForGroupSale;
import com.ygg.admin.entity.RelationActivitiesCommonAndProduct;
import com.ygg.admin.exception.DaoException;

public interface ActivitiesCommonDao
{
    /**
     * 根据para查询通用专场
     * 
     * @return
     * @throws Exception
     */
    List<ActivitiesCommonEntity> findAllAcCommonByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据id查询通用专场
     * 
     * @return
     * @throws Exception
     */
    ActivitiesCommonEntity findAcCommonById(int id)
        throws Exception;
    
    /**
     * 根据para统计通用专场数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllAcCommonByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int save(ActivitiesCommonEntity entity)
        throws Exception;
    
    /**
     * 更新
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int update(ActivitiesCommonEntity entity)
        throws Exception;
    
    /**
     * 更新排序值
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrder(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据特卖ID 查询对应所有商品相关信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<ProductInfoForGroupSale> findProductInfoForGroupSaleByActivitiesCommonId(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据特卖ID统计对应所有商品数量
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int countProductNumByActivitiesCommonId(int id)
        throws Exception;
    
    /**
     * 根据通用专场ID查询所有商品ID
     * 
     * @return
     */
    List<Integer> findAllProductIdByActivitiesCommonId(int id)
        throws DaoException;
    
    /**
     * 根据特卖 可关联的所有商品相关信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<ProductInfoForGroupSale> findProductInfoForGroupSale(Map<String, Object> para)
        throws Exception;
    
    int countProductInfoForGroupSale(Map<String, Object> para)
        throws Exception;
    
    int deleteRelationActivitiesCommonAndProduct(int id)
        throws Exception;
    
    /**
     * 根据特卖ID 查询对应所有商品最大排序值
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int findMaxOrderByActivitiesCommonId(int id)
        throws Exception;
    
    int saveRelationActivitiesCommonAndProduct(RelationActivitiesCommonAndProduct para)
        throws Exception;
    
    int updateProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int batchUpdateGroupProductTime(Map<String, Object> para)
        throws Exception;
    
    ActivitiesCommonEntity findActivitiesCommonById(int id)
        throws Exception;
    
    List<Integer> findProductIdByActivitiesCommonId(int id)
        throws Exception;
    
    int countProductsByActivityCommonId(int activityCommonId);
    
    List<Map<String, Object>> findProductsByActivityCommonId(Map<String, Object> params);
}