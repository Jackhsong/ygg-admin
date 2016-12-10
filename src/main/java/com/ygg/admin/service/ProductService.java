package com.ygg.admin.service;

import com.ygg.admin.entity.*;

import java.util.List;
import java.util.Map;

public interface ProductService
{
    
    /**
     * 插入product
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 跟新product
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查找商品信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ProductEntity findProductById(int id)
        throws Exception;

    /**
     * 根据IDS查找商品信息
     *
     * @param ids
     * @return
     * @throws Exception
     */
    List<ProductEntity> batchFindProductByIds(List<Integer> ids)
            throws Exception;
    
    /**
     * 根据ProductId查找商品常用信息
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
     * 根据商品ID查询商品与自定义页面的关联关系
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProductAndPageCustomInfo(int id)
        throws Exception;
    
    /**
     * 根据商品ID查询商品移动端详情info
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProductMobileDetailInfo(int id)
        throws Exception;
    
    /**
     * 根据para查询商品，
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonProductForManage(Map<String, Object> para)
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
     * 更新商品时间
     * 
     * @param productEntity
     * @return
     * @throws Exception
     */
    int updateProductTime(ProductEntity productEntity)
        throws Exception;
    
    /**
     * 根据para更新商品
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductPara(Map<String, Object> para, boolean isUpdateProductCommon)
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
     * 批量修改商品温馨提示
     * 
     * @param idList
     * @param tip
     * @return
     * @throws Exception
     */
    int batchUpdateProductTip(List<Integer> idList, String tip)
        throws Exception;
    
    /**
     * 复制商品
     * 
     * @param productId
     * @param productType：商品类型，1特卖商品，2商城商品
     * @param source：复制来源：1商品列表，2售后自动生成
     * @return
     * @throws Exception
     */
    int copyProduct(int productId, Integer productType, int source, Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找最大Id
     * 
     * @return
     */
    int findMaxProductId()
        throws Exception;
    
    /**
     * 检查商品是否满足修改条件
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> canEditProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找指定类型的商品
     * @param id：商品Id
     * @param productType：商品类型，1特卖商品，2商城商品
     * @return
     * @throws Exception
     */
    ProductEntity findProductById(int id, int productType)
        throws Exception;
    
    /**
     * 批量将商品从fromType类型复制成toType类型
     * @param ids
     * @param fromType
     * @param toType
     * @return
     */
    Map<String, Object> copyProductFromOtherType(String ids, int fromType, int toType)
        throws Exception;
    
    int changeEmailRemind(Map<String, Object> para)
        throws Exception;
    
    /**
     * 获取格格福利商品信息
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findGegeWelfareInfo(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> saveOrUpdateGegeWelfareProduct(int id, int productId, double salesPrice, double limitPrice, int limitNum, String remark, String brandIds,
        String payTimeBegin, String payTimeEnd)
        throws Exception;
    
    Map<String, Object> deleteGegeWelfareProduct(List<Integer> productIdList)
        throws Exception;
    
    /**
     * 查询商城商品列表
     * @param para
     * @return
     */
    Map<String, Object> jsonMallProductForManage(Map<String, Object> para)
        throws Exception;
    
    /**
     * 放入商城/移除商城
     * @param para
     * @return
     */
    int updateShowInMall(Map<String, Object> para)
        throws Exception;
    
    /**
     * 批量添加分类
     * @param categoryList
     * @return
     */
    int classifyProduct(List<CategoryEntity> categoryList)
        throws Exception;
    
    /**
     * 导出商品图片
     * @param productId
     * @return
     * @throws Exception
     */
    String exportProductImage(int productId)
        throws Exception;
    
    /**
     * 异步加载库存告急商品列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonProductStockInfo(Map<String, Object> para)
        throws Exception;
    
    boolean checkProductIsInMall(int productId)
        throws Exception;
    
    /**
     * 导出库存告急列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, String>> exportStockList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改商品备注
     * @param ids
     * @param remark
     * @return
     * @throws Exception
     */
    String updateProductRemark(String ids, String remark)
        throws Exception;
    
    Object findProductNewbieInfo(Map<String, Object> para)
        throws Exception;
    
    String saveOrUpdateProductNewbie(int id, int productId, double salesPrice, int isDisplay)
        throws Exception;
    
    String deleteProductNewbie(String ids)
        throws Exception;
    
    String updateProductNewbieSequence(int id, int sequence)
        throws Exception;
    
    String updateProductNewbieDisplayStatus(int id, int isDisplay)
        throws Exception;

    /**
     * 修改商品活动供货价
     * @param id：商品id
     * @param startTime：活动供货价开始时间
     * @param endTime：活动供货价结束时间
     * @param activityWholesalePrice：活动供货价
     * @return
     * @throws Exception
     */
    ResultEntity updateProductActivityWholesalePrice(int id, String startTime, String endTime, float activityWholesalePrice)
        throws Exception;
}
