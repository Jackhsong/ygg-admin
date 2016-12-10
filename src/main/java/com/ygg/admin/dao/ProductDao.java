package com.ygg.admin.dao;

import com.ygg.admin.entity.ProductCommonEntity;
import com.ygg.admin.entity.ProductCountEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ProductMobileDetailEntity;

import java.util.List;
import java.util.Map;

public interface ProductDao
{
    
    /**
     * 插入product
     * 
     * @param product
     * @return
     * @throws Exception
     */
    int save(ProductEntity product)
        throws Exception;
    
    /**
     * 插入ProductCommon
     * 
     * @param productCommon
     * @return
     * @throws Exception
     */
    int saveProductCommon(ProductCommonEntity productCommon)
        throws Exception;
    
    /**
     * 插入商品库存信息
     * 
     * @param productCount
     * @return
     * @throws Exception
     */
    int saveProductCount(ProductCountEntity productCount)
        throws Exception;
    
    /**
     * 插入商品ProductMobileDetail
     * 
     * @param mobileDetail
     * @return
     * @throws Exception
     */
    int saveProductMobileDetail(ProductMobileDetailEntity mobileDetail)
        throws Exception;
    
    /**
     * 更新商品信息
     * 
     * @param product
     * @return
     * @throws Exception
     */
    int updateProduct(ProductEntity product)
        throws Exception;
    
    /**
     * 更新productCommon信息
     * 
     * @param product
     * @return
     * @throws Exception
     */
    int updateProductCommon(ProductCommonEntity productCommon)
        throws Exception;
    
    /**
     * 更新productCount
     * 
     * @return
     * @throws Exception
     */
    int updateProductCount(ProductCountEntity productCount)
        throws Exception;
    
    int updateProductCountByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查找商品信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ProductEntity findProductByID(int id, Integer type)
        throws Exception;
    
    /**
     * 根据ID查找商品信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    List<ProductEntity> batchFindProductByIDs(List<Integer> ids, Integer type, Integer max)
        throws Exception;
    
    /**
     * 根据para查询商品信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<ProductEntity> findAllProductByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询商品&商家信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findProAndSellerInfoByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据商品id查询商品常用信息表
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ProductCommonEntity findProductCommonByProductId(int id)
        throws Exception;
    
    /**
     * 根据商品ID查询商品库存信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ProductCountEntity findProductCountByProductId(int id)
        throws Exception;
    
    /**
     * 根据para查询商品移动端详情
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<ProductMobileDetailEntity> findProductMobileDetailByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据id查询商品移动端详情
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ProductMobileDetailEntity findProductMobileDetailById(int id)
        throws Exception;
    
    /**
     * 商品管理页面查询商品信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductInfoForManage(Map<String, Object> para)
        throws Exception;
    
    /**
     * 商品管理页面查询商品数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countProductInfoForManage(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除mobileDetail
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int deleteProductMobileDetail(int id)
        throws Exception;
    
    /**
     * 更新mobileDetail
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int updateProductMobileDetail(ProductMobileDetailEntity entity)
        throws Exception;
    
    /**
     * 可买
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int forSale(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改para.idsList所包含的所有商品的可用状态
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int forAvailable(Map<String, Object> para)
        throws Exception;
    
    /**
     * 计算商品总库存
     * 
     * @param ids
     * @return
     * @throws Exception
     */
    int countStockByProductIds(List<Integer> ids)
        throws Exception;
    
    /**
     * 根据ID查询商品销售时间
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ProductEntity findProductSaleTimeById(int id)
        throws Exception;
    
    int updateProductTime(ProductEntity productEntity)
        throws Exception;
    
    int updateProductByPara(Map<String, Object> para)
        throws Exception;
    
    int updateProductCommonByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据商品ID查询商品信息和库存信息 -- 简略
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProductAndCountInfoByProductId(int id)
        throws Exception;
    
    /**
     * 增加商品销量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int addProductSellNum(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据商品ID查询商品温馨提示
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProductTipById(int id)
        throws Exception;
    
    /**
     * 根据商品ID更新商品温馨提示
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductTipByPara(Map<String, Object> para)
        throws Exception;
    
    int updatePartnerDistributionPriceByPara(Map<String, Object> para)
        throws Exception;
    
    // -------------------------------- 商品数据统计 begin-------------------
    
    /**
     * 商品数据统计 根据para 查询 所有已下单记录
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductSalesRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 环球商品数据统计 根据para 查询 所有已下单记录
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findQqbsProductSalesRecord(Map<String, Object> para)
        throws Exception;
    
    // -------------------------------- 商品数据统计 end-------------------
    
    /**
     * 查找最大Id
     * 
     * @return
     */
    int findMaxProductId()
        throws Exception;
    
    /**
     * 根据商品id集合获取商家真实名称
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<String> findRealSellerNameByProductIdList(List<Integer> para)
        throws Exception;
    
    int changeEmailRemind(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据 para 查询商品ID
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllProductSimpleByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询格格福利商品信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllGegeWelfareProductByPara(Map<String, Object> para)
        throws Exception;
    
    int countAllGegeWelfareProductByPara(Map<String, Object> para)
        throws Exception;
    
    int addGegeWelfareProduct(Map<String, Object> para)
        throws Exception;
    
    int updateGegeWelfareProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteGegeWelfareProductByProductId(int id)
        throws Exception;
    
    /**
     * 查询所有商品 for Elasticsearch 更新索引
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllProductInfoForElasticsearchIndex(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据基本商品IDs查询分类信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllProductCategoryInfoByProductBaseIds(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询全部商城商品
     * @param para
     * @return
     */
    List<Map<String, Object>> findMallProductForManage(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计商城商品
     * @param para
     * @return
     * @throws Exception
     */
    int countMallProductForManage(Map<String, Object> para)
        throws Exception;
    
    /**
     * 放入或移除商城
     * @param para
     * @return
     */
    int updateShowInMall(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新商品 返分销毛利百分比类型
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductReturnDistributionProportionTypeByBrandId(Map<String, Object> para)
        throws Exception;
    
    /**   
     * 查找商品库存信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllProductStock(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找特卖商品库存信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findSaleProductStock(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计特卖商品库存信息
     * @param para
     * @return
     * @throws Exception
     */
    int countSaleProductStock(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找商城商品库存信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findMallProductStock(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计商城商品库存信息
     * @param para
     * @return
     * @throws Exception
     */
    int countMallProductStock(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据编码列表查询对应的品牌信息
     * @param codeList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProductBrandsByCodeList(List<String> codeList)
        throws Exception;
    
    List<Integer> findProductIdByNameAndRemark(Map<String, Object> para)
        throws Exception;
    
    ProductCountEntity findProductCountByProductIdForUpdate(Integer productId)
        throws Exception;
    
    /**
     * 调整商品库存
     * @param productId
     * @param oldStock：调整之前的库存
     * @param changeStock：要调整的库存
     * @return
     * @throws Exception
     */
    int updateProductStock(Integer productId, int oldStock, int changeStock)
        throws Exception;
    
    int updateProductRemark(Map<String, Object> para)
        throws Exception;
    
    int deleteProductNewbie(List<String> idList)
        throws Exception;
    
    List<Map<String, Object>> findAllProductNewbie(Map<String, Object> para)
        throws Exception;
    
    int countProductNewbie(Map<String, Object> para)
        throws Exception;
    
    int insertProductNewbie(Map<String, Object> para)
        throws Exception;
    
    int updateProductNewbie(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据条件查找商品
     * @param para
     * @return
     * @throws Exception
     */
    List<ProductEntity> findProductByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据基本商品Id查询商品Id
     * @param productBaseId
     * @return
     */
    List<Integer> findProductIdsByPid(int productBaseId)
        throws Exception;
    
    /**
     * 根据商品Id删除商品详情图
     * @param productId
     * @return
     * @throws Exception
     */
    int deleteProductMobileDetailByProductId(Integer productId)
        throws Exception;
    
    /**
     * 批量插入商品详情图
     * @param list
     * @return
     * @throws Exception
     */
    int insertProductMobileDetail(List<ProductMobileDetailEntity> list)
        throws Exception;

    /**
     * 根据订单付款时间统计销量信息
     */
    List<Map<String, Object>> getSaleVolumeInfoByPayTime(Map<String, Object> para);

    /**
     * 保存商品销量信息
     */
    int saveProductSaleVolume(Map<String, Object> data);

    /**
     * 更新商品销量信息
     */
    int updateProductSaleVolume(Map<String, Object> data);

    /**
     * 查询销量相关信息
     */
     List<Map<String, Object>> selectSaleVolumeInfoByDays(Map<String, Object> para);

    int countSaleVolumeByDayHours(Map<String, Object> para);
    
    
    /**
     *  燕网商品数据统计 根据para查询 所有已下单记录
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findYWProductSalesRecord(Map<String, Object> para)
            throws Exception;

    /**
     * 修改商品活动供货价
     * @param param
     * @return
     * @throws Exception
     */
    int updateProductActivityWholesalePrice(Map<String, Object> param)
        throws Exception;

    /**
     * 插入商品活动供货价日志记录
     * @param param
     * @return
     * @throws Exception
     */
    int inserProductActivityWholesalePriceLog(Map<String, Object> param)
        throws Exception;
}
