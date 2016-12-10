package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ProductInfoForGroupSale;

public interface ActivitiesCommonService
{
    
    /**
     * 返回json信息供通用专场管理使用
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonAcCommonInfo(Map<String, Object> para)
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
     * 根据ID查找ActivitiesCommonEntity
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ActivitiesCommonEntity findAcCommonById(int id)
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
     * 返回json信息供某个通用专场商品管理使用
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonGroupSaleProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 返回商品json信息供某个通用专场添加
     * 
     * @param para
     * @return
     * @throws Exception
     */
    String jsonProductListForAdd(Map<String, Object> para)
        throws Exception;
    
    int deleteRelationActivitiesCommonAndProduct(int id)
        throws Exception;
    
    int deleteRelationActivitiesCommonAndProductList(List<Integer> ids)
        throws Exception;
    
    /**
     * 添加特卖商品
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int addGroupSaleProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询通用专场
     * 
     * @return
     * @throws Exception
     */
    List<ActivitiesCommonEntity> findAllAcCommonByPara(Map<String, Object> para)
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
    
    int countProductNumByActivitiesCommonId(int id)
        throws Exception;
    
    /**
     * 更新商品展现状态
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 批量修改组合商品时间
     * 
     * @param groupIds
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    String batchUpdateGroupProductTime(String groupIds, String startTime, String endTime)
        throws Exception;
    
    List<Map<String, Object>> findProductsByActivityCommonId(Map<String, Object> params);
    
    int countProductsByActivityCommonId(int activityCommonId);
}
