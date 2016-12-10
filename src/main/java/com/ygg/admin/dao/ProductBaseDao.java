package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.ProductBaseMobileDetailEntity;
import com.ygg.admin.entity.RelationProductBaseDeliverAreaEntity;

public interface ProductBaseDao
{
    
    List<Map<String, Object>> queryAllProductBaseInfo(Map<String, Object> para)
        throws Exception;
    
    int countProductBaseInfo(Map<String, Object> para)
        throws Exception;
    
    int saveProductBase(ProductBaseEntity product)
        throws Exception;
    
    int saveProductMobileDetail(ProductBaseMobileDetailEntity entity)
        throws Exception;

    List<ProductBaseMobileDetailEntity> findProductBaseMobileDetailsByProductBaseId(int productBaseId)
            throws Exception;
    
    ProductBaseEntity queryProductBaseById(int editId)
        throws Exception;
    
    int updateProductBase(ProductBaseEntity product)
        throws Exception;
    
    int updateProductBaseCost(Map<String, Object> para)
        throws Exception;
    
    int deleteProductBaseMobileDetail(int id)
        throws Exception;
    
    ProductBaseMobileDetailEntity findProductBaseMobileDetailById(int id)
        throws Exception;
    
    int updateProducBasetMobileDetail(ProductBaseMobileDetailEntity entity)
        throws Exception;
    
    List<ProductBaseEntity> queryAllProductBase(Map<String, Object> para)
        throws Exception;
    
    int forAvailable(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> querySaleProductInfoByBaseId(Map<String, Object> para)
        throws Exception;
    
    int adjustSaleStock(Map<String, Object> para)
        throws Exception;
    
    int addTotalStock(Map<String, Object> map)
        throws Exception;
    
    /**
     * 查找最大Id
     * 
     * @return
     */
    int findMaxProductId()
        throws Exception;
    
    List<Integer> checkCodeAndBarCode(Map<String, Object> para)
        throws Exception;
    
    int countSaleProductInfoByBaseId(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新与基本商品先关联的特卖商品
     * @param map
     * @return
     */
    int updateRelationProductByPara(Map<String, Object> map)
        throws Exception;
    
    /**
     * 判断基本商品是否在使用中
     * @param id
     * @return
     * @throws Exception
     */
    int checkIsInUse(int id)
        throws Exception;
    
    /**
     * 删除详情页图片
     * @param paraMap
     * @return
     */
    int deleteProductBaseMobileDetailIdInList(Map<String, Object> paraMap)
        throws Exception;
    
    int adjustMallStock(Map<String, Object> paraMap)
        throws Exception;
    
    /**
     * 根据基本商品Id查找关联的商品Id
     * @param id
     * @return
     * @throws Exception
     */
    List<Integer> findProductIdByProductBaseId(int id, Integer type)
        throws Exception;
    
    /**
     * 根据基本商品Id查询商品发货/不发货地区
     * @param id
     * @return
     */
    List<RelationProductBaseDeliverAreaEntity> findRelationSellerDeliverAreaByProductBaseId(int id)
        throws Exception;
    
    /**
     * 新增基本商品配送地区
     * @param rpbae
     * @return
     */
    int insertRelationProductBaseDeliverArea(List<RelationProductBaseDeliverAreaEntity> list)
        throws Exception;
    
    /**
     * 查找商品正品保证信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllQualityPromise(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计商品正品保证信息
     * @param para
     * @return
     * @throws Exception
     */
    int countQualityPromise(Map<String, Object> para)
        throws Exception;
    
    /**
     * 判断商品正品保证信息是否存在
     * @param para
     * @return
     */
    boolean IsExistQualityPromise(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增商品正品保证信息
     * @param para
     * @return
     * @throws Exception
     */
    int insertQualityPromise(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改商品正品保证信息
     * @param para
     * @return
     * @throws Exception
     */
    int updateQualityPromise(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有过期基本商品
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllExpireProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计所有过期基本商品
     * @param para
     * @return
     * @throws Exception
     */
    int countAllExpireProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据配送地区模版查找基本商品ID
     * @param id
     * @return
     * @throws Exception
     */
    List<Integer> findProductBaseIdBySellerTemplateId(int id)
        throws Exception;
    
    /**
     * 更新商品配送地区限制类型
     * @param type
     * @param productBaseIdList
     * @return
     * @throws Exception
     */
    int batchUpdateProductBaseDeliverAreaType(byte type, List<Integer> productBaseIdList)
        throws Exception;
    
    /**
     * 删除商品配送地区
     * @param productBaseIdList
     * @return
     * @throws Exception
     */
    int deleteRelationProductBaseDeliverArea(List<Integer> productBaseIdList)
        throws Exception;
    
    /**
     * 查找基本商品已分配库存
     * @param productBaseId
     * @return
     * @throws Exception
     */
    int findAllottedStockById(int productBaseId)
        throws Exception;
    
    List<Map<String, Object>> findProductBaseRelationInfoByProductBaseId(List<Integer> productBaseIds)
        throws Exception;
    
    ProductBaseEntity findProductBaseByIdForUpdate(int productBaseId)
        throws Exception;
    
    int updateProductBaseStock(int productBaseId, int change)
        throws Exception;
    
    /**
     * 基本商品供货价修改时记录日志
     * @param params
     * @return
     * @throws Exception
     */
    int insertWholesalePriceUpdatelog(Map<String, Object> params)
        throws Exception;
    
    int countWholeSalePriceLogByPara(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findWholeSalePriceLogByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据基本商品Id更新商品medium_image、small_image
     * @param id
     * @param mediumImage
     * @param smallImage
     * @return
     * @throws Exception
     */
    int updateProductCommonImage(int id, String mediumImage, String smallImage)
        throws Exception;
    
    /**
     * 根据商家商品id查询基本商品id
     * @param sellerProductIds
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductBaseIdBySellerProductId(List<Object> sellerProductIds)
        throws Exception;

    /**
     * 查找基本商品销量
     * @param params
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductBaseSalesVolumeByPara(Map<String, Object> params)
        throws Exception;

    /**
     * 根据基本商品id查找历史销量
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductBaseHistorySalesVolume(Map<String, Object> para)
        throws Exception;
}
