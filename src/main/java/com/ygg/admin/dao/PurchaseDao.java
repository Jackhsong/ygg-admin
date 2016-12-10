package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ProviderEntity;
import com.ygg.admin.entity.PurchaseOrderInfoEntity;

public interface PurchaseDao
{
    
    Map<String, Object> findProviderProductByBarCode(String productCode)
        throws Exception;
    
    /**
     * 新增供货商信息
     * 
     * @param entity
     *            供货商对象
     * 
     * @return
     * @throws Exception
     */
    int saveProvider(ProviderEntity entity)
        throws Exception;
    
    /**
     * 更新供货商信息
     * 
     * @param entity
     *            供货商对象
     * @return
     * @throws Exception
     */
    int updateProvider(ProviderEntity entity)
        throws Exception;
    
    /**
     * 根据条件统计供应商是否存在
     * @param name
     * @return
     * @throws Exception
     */
    int countProviderByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据ID查询供货商信息
     * 
     * @param id
     *            供货商ID
     * 
     * @return
     * @throws Exception
     */
    ProviderEntity findProviderByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 供应商信息
     * @param id
     *            供应商ID
     * @param brandId
     *            品牌ID
     * @param purchaseSubmitType
     *            采购结算方式
     * @return
     * @throws Exception
     */
    List<ProviderEntity> findProviderListInfoByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 统计供应商总条数
     * @param id
     * @param brandId
     * @param purchaseSubmitType
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    int countTotalProviderByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 供应商下拉列表
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProviderList(int type)
        throws Exception;
    
    /**
     * 新增分仓信息
     * 
     * @param name
     *            名称
     * @param detailAddress
     *            详细地址
     * @param contactPerson
     *            联系人呢
     * @param contactPhone
     *            联系方式
     * 
     * @return
     * @throws Exception
     */
    int saveStorage(String name, String detailAddress, String contactPerson, String contactPhone, String sellerId, short type)
        throws Exception;
    
    /**
     * 新增分仓信息
     * 
     * @param id
     * @param name
     *            名称
     * @param detailAddress
     *            详细地址
     * @param contactPerson
     *            联系人呢
     * @param contactPhone
     *            联系方式
     * 
     * @return
     * @throws Exception
     */
    int updateStorage(int id, String name, String detailAddress, String contactPerson, String contactPhone, String sellerId, short type)
        throws Exception;
    
    /**
     * 根据ID查询分仓信息
     * 
     * @param id
     *            分仓ID
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> findStorageByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据条件进行统计分仓个数
     * 
     * @param param
     * @return
     * @throws Exception
     */
    int countStorageByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 查询分仓列表信息
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findStorageListInfo(int page, int rows, int type)
        throws Exception;
    
    /**
     * 分仓列表
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findStorageList(int type)
        throws Exception;
    
    /**
     * 根据条件查找供应商商品列表
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProviderProductListInfoByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据条件统计供应商商品总数
     * @param param
     * @return
     * @throws Exception
     */
    int countTotalProviderProductByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据供应商商品ID查询供应商商品信息
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProviderProductById(int id)
        throws Exception;
    
    /**
     * 保存供应商商品
     * @param list
     * @return
     * @throws Exception
     */
    //int saveProviderProduct(List<Map<String, Object>> list) throws Exception;
    int saveProviderProduct(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新供应商
     * @param param
     * @return
     * @throws Exception
     */
    int updateProviderProduct(Map<String, Object> param)
        throws Exception;
    
    /**
     * 采购单列表
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderListInfoByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 删除采购单
     * @param orderId
     * @return
     * @throws Exception
     */
    int deleteOrderById(int orderId)
        throws Exception;
    
    /**
     * 删除采购单商品
     * @param orderId
     * @return
     * @throws Exception
     */
    int deleteOrderProductByOrderId(int orderId)
        throws Exception;
    
    /**
     * 保存采购单
     * @param param
     * @return
     * @throws Exception
     */
    int savePurchaseOrder(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新采购单
     * @param param
     * @return
     * @throws Exception
     */
    int updatePurchaseOrder(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据ID查询采购单信息
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderByPurchaseCode(String purchaseCode)
        throws Exception;
    
    /**
     * 统计采购单总条数
     * @return
     * @throws Exception
     */
    int countTotalOrder(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据采购单ID查询采购单商品列表
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderProductListInfo(Map<String, Object> param)
        throws Exception;
    
    /**
     * 查询采购单可以新增的商品商品
     * @param providerId
     * @param purchaseCode
     * @param brandId
     * @param productId
     * @param barcode
     * @param productName
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProviderProduct4OrderListInfo(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode,
        String productName)
        throws Exception;
    
    /**
     * 统计采购单可以新增的商品商品总数
     * @param providerId
     * @param purchaseCode
     * @param brandId
     * @param productId
     * @param barcode
     * @param productName
     * @return
     * @throws Exception
     */
    int countTotalProviderProduct4OrderList(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode, String productName)
        throws Exception;
    
    /**
     * 保存采购单商品
     * @param param
     * @return
     * @throws Exception
     */
    int saveOrderProduct(Map<String, Object> param)
        throws Exception;
    
    /**
     * 
     * @param list
     * @return
     * @throws Exception
     */
    int saveOrderProductForImport(List<Map<String, Object>> list)
        throws Exception;
    
    /**
     * 更新采购单商品
     * @param param
     * @return
     * @throws Exception
     */
    int updateOrderProduct(Map<String, Object> param)
        throws Exception;
    
    /**
     * 删除采购单
     * @param ids
     * @return
     * @throws Exception
     */
    int removeOrderProduct(List<Integer> ids)
        throws Exception;
    
    /**
     * 查询采购单编码
     * @param day
     * @return
     * @throws Exception
     */
    int findPurchaseCode(String day)
        throws Exception;
    
    /**
     * 更新采购单编码
     * @param day
     * @param code
     * @return
     * @throws Exception
     */
    int updatePurchaseCode(String day, int code)
        throws Exception;
    
    /**
     * 每日新增一条记录
     * @param day
     * @return
     * @throws Exception
     */
    int savePurchaseCode(String day)
        throws Exception;
    
    /**
     * 统计采购单付款信息
     * @param purchaseCode
     * @return
     * @throws Exception
     */
    Map<String, Object> sumPurchasePayDetail(String purchaseCode)
        throws Exception;
    
    /**
     * 查询采购单付款信息
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findPurchasePayDetailByParam(Map<String, Object> param)
        throws Exception;
    
    int countTotalPurchasePayDetail(Map<String, Object> param)
        throws Exception;
    
    /**
     * 保存采购单付款信息
     * @param param
     * @return
     * @throws Exception
     */
    int savePurchasePayDetail(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新采购单付款信息
     * @param param
     * @return
     * @throws Exception
     */
    int updatePurchasePayDetail(Map<String, Object> param)
        throws Exception;
    
    /**
     * 删除一条采购单付款信息
     * @param id
     * @return
     * @throws Exception
     */
    int deletePurchasePayDetail(int id)
        throws Exception;
    
    /**
     * 查询采购单入库信息
     * @param purchaseCode
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findPurchaseProductStoring(String purchaseCode)
        throws Exception;
    
    /**
     * 保存采购单入库信息
     * @param list
     * @return
     * @throws Exception
     */
    int savePurchaseProductStoring(List<Map<String, Object>> list)
        throws Exception;
    
    List<Map<String, Object>> findBatchListInfo(Map<String, Object> param)
        throws Exception;
    
    int countTotalBatch(Map<String, Object> param)
        throws Exception;
    
    List<Map<String, Object>> findBatchStoringTime(String providerProductId, List<Object> purchaseCodes)
        throws Exception;
    
    List<PurchaseOrderInfoEntity> findExportPurchaseOrder(List<String> list)
        throws Exception;
    
    /**
     * 查询供应商、仓库、供应商商品关联关系
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findRelationProviderStorageProviderProductByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 保存供应商、仓库、供应商商品关联关系
     * @param list
     * @return
     * @throws Exception
     */
    int saveRelationProviderStorageProviderProduct(List<Map<String, Object>> list)
        throws Exception;
    
    /**
     * 根据基本商品ID删除与供应商商品相关联的信息
     * @param barcode
     * @return
     * @throws Exception
     */
    int deleteRelationProviderStorageProviderProduct(String barcode)
        throws Exception;
    
    /**
     * 统计采购单中，指定商品入库数
     * 
     * @param param
     * @return
     * @throws Exception
     */
    int sumStoringCountByParam(Map<String, Object> param)
        throws Exception;
    
    List<Map<String, Object>> findAllProviderProduct(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findStorageByBarCode(String barCode)
        throws Exception;
}
