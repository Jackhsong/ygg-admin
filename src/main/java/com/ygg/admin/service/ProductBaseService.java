package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CategoryEntity;
import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.ProductBaseMobileDetailEntity;
import com.ygg.admin.entity.RelationProductBaseDeliverAreaEntity;
import com.ygg.admin.entity.ResultEntity;

public interface ProductBaseService
{
    
    ResultEntity ajaxPageDataProductBase(Map<String, Object> para)
        throws Exception;
    
    ProductBaseEntity queryProductBaseById(int editId)
        throws Exception;
    
    /**
     * @param product：基本商品
     * @param mobileDetailsImageList：详情图信息
     * @param categoryList：分类信息
     * @param saveType：保存类型，1仅保存，2保存并更新关联的特卖/商城商品
     * @return
     * @throws Exception
     */
    int updateProductBase(ProductBaseEntity product, List<Map<String, Object>> mobileDetailsImageList, List<CategoryEntity> categoryList, int saveType)
        throws Exception;
    
    /**
     * @param product：基本商品
     * @param mobileDetailsImageList：详情图信息
     * @param categoryList：分类信息
     * @return
     * @throws Exception
     */
    int saveProductBase(ProductBaseEntity product, List<Map<String, Object>> mobileDetailsImageList, List<CategoryEntity> categoryList)
        throws Exception;
    
    List<ProductBaseMobileDetailEntity> findProductBaseMobileDetailsByProductBaseId(int id)
        throws Exception;
    
    /**
     * 复制基本商品
     * @param id：基本商品Id
     * @param code：商品编码
     * @return
     * @throws Exception
     */
    ResultEntity copyProduct(int id, String code)
        throws Exception;
    
    List<ProductBaseEntity> queryAllProductBase(Map<String, Object> para)
        throws Exception;
    
    int forAvailable(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> querySaleProductInfoByBaseId(int id)
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
    
    List<Integer> checkCodeAndBarCode(int sellerId, String code)
        throws Exception;
    
    Map<String, Object> findJsonProductInfoBybaseId(Map<String, Object> para)
        throws Exception;
    
    /**
     * 判断关联的基本商品是否在使用中
     * @param id
     * @return
     */
    int checkIsInUse(int id)
        throws Exception;
    
    /**
     * 导入批量修改供货价数据  模拟
     */
    int checkBatchUpdateProductCostPrice(String productBaseId, String submitType, String wholesalePrice, String deduction, String proposalPrice, String selfPurchasePrice,
        List<Map<String, Object>> simulationList)
        throws Exception;
    
    /**
     * 导入批量修改供货价数据  确认
     */
    int saveBatchUpdateProductCostPrice(String productBaseId, String submitType, String wholesalePrice, String deduction, String proposalPrice, String selfPurchasePrice,
        List<Map<String, Object>> confirmList)
        throws Exception;
    
    /**
     * 根据基本商品Id查询商品发货/不发货地区
     * @param id
     * @return
     */
    List<RelationProductBaseDeliverAreaEntity> findRelationSellerDeliverAreaByProductBaseId(int id)
        throws Exception;
    
    /**
     * 商品正品保证列表
     * @param para
     * @return
     */
    Map<String, Object> jsonQualityPromiseInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改商品正品保证信息
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdateQualityPromise(Map<String, Object> para)
        throws Exception;
    
    /**
     * 基本商品批量分类
     * @param addCategoryList
     * @return
     * @throws Exception
     */
    int classifyProduct(List<CategoryEntity> addCategoryList)
        throws Exception;
    
    Map<String, Object> findAllExpireProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有已分配库存
     * @param productBaseId
     * @return
     * @throws Exception
     */
    int findAllottedStockById(int productBaseId)
        throws Exception;
    
    List<Map<String, Object>> findProductBaseRelationInfoByProductBaseId(List<Integer> productBaseIds)
        throws Exception;
    
    /**
     * 
     * @param productBaseId：基本商品ID
     * @param productId：商品ID
     * @param productType：商品类型，1特卖商品，2商城商品
     * @param changeStock：调整库存数量
     * @return
     * @throws Exception
     */
    String adjustStock(int productBaseId, int productId, int productType, int changeStock)
        throws Exception;
    
    /**
     * 记录基本商品供货价修改日志
     * @param id：基本商品Id
     * @param oldPrice：修改之前的价格
     * @param newPrice：修改之后的价格
     * @return
     */
    int insertWholesalePriceUpdatelog(int id, float oldPrice, float newPrice)
        throws Exception;
    
    /**
     * 基本商品供货价历史列表
     * @param para
     * @return
     * @throws Exception
     */
    ResultEntity jsonWholeSalePriceHistory(Map<String, Object> para)
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
     * 预览基本商品图片
     * @param id
     * @return
     * @throws Exception
     */
    Object previewPicture(int id)
        throws Exception;

    /**
     * 查找基本商品历史记录
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findHistorySalesVolumeById(int id, String startTime, String endTime)
        throws Exception;
}
