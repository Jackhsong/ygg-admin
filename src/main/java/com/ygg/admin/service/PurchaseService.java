package com.ygg.admin.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.entity.ProviderEntity;
import com.ygg.admin.entity.PurchaseOrderInfoEntity;

public interface PurchaseService
{
    
    /**
     * 新增、更新供货商信息
     * 
     * @param entity
     *            供货商对象
     * 
     * @return
     * @throws Exception
     */
    int saveOrUpdateProvider(ProviderEntity entity)
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
    ProviderEntity findProviderById(int id)
        throws Exception;
    
    /**
     * @param id
     *            供应商ID
     * @param purchaseSubmitType
     *            采购结算方式
     * @return
     * @throws Exception
     */
    Map<String, Object> findProviderListInfo(int id, String remark, int purchaseSubmitType, int page, int rows, int type)
        throws Exception;
    
    /**
     * 供应商下拉列表
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findProviderList(int type)
        throws Exception;
    
    /**
     * 新增或更新分仓信息
     * 
     * @param id
     *            新增时ID为0
     * @param name
     *            名称
     * @param detailAddress
     *            详细地址
     * @param concatPerson
     *            联系人呢
     * @param concatPhone
     *            联系方式
     * 
     * @return
     * @throws Exception
     */
    int saveOrUpdateStorage(int id, String name, String detailAddress, String concatPerson, String concatPhone, String sellerId, short type)
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
    Map<String, Object> findStorageById(int id)
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
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findStorageList(int type)
        throws Exception;
    
    /**
     * 查询供应商商品列表信息
     * 
     * @param productBaseId
     * @param barcode
     * @param name
     * @param brandId
     * @param providerId
     * @param remark
     * @param page
     * @param rows
     * @return
     */
    Map<String, Object> findProviderProductListInfo(int id, String barcode, String name, int brandId, int providerId, int storageId, String remark, int page, int rows, int type)
        throws Exception;
    
    /**
     * 查询供应商商品
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProviderProductInfoById(int id)
        throws Exception;
    
    /**
     * 新增修改供应商商品信息
     * 
     * @param param
     * @return
     * @throws Exception
     */
    int saveOrUpdateProviderProduct(HttpServletRequest req, Map<String, Object> param)
        throws Exception;
    
    /**
     * 导入供应商商品
     * 
     * @param file
     * @return
     * @throws Exception
     */
    int importProviderProduct(MultipartFile file)
        throws Exception;
    
    /**
     * 采购单列表
     * 
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderListInfo(String purchaseCode, String status, String providerId, String storageId, String startTime, String endTime, int page, int rows, int type)
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
     * 根据ID查询订单详情
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderByPurchaseCode(String purchaseCode)
        throws Exception;
    
    /**
     * 根据采购单ID查询采购单商品列表
     * 
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderProductListInfo(String purchaseCode, String isDetail)
        throws Exception;
    
    /**
     * 新增采购单
     * @param param
     * @return
     * @throws Exception
     */
    int savePurchaseOrder(Map<String, Object> param)
        throws Exception;
    
    /**
     * 修改采购单
     * @param param
     * @return
     * @throws Exception
     */
    int updatePurchaseOrder(Map<String, Object> param)
        throws Exception;
    
    /**
     * 采购单可以新增的商品
     * 
     * @param providerId
     * @return
     * @throws Exception
     */
    Map<String, Object> findProviderProduct4OrderListInfo(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode, String productName)
        throws Exception;
    
    /**
     * 保存采购单商品
     * 
     * @param purchaseCode
     * @param providerProductIds
     * @return
     * @throws Exception
     */
    int saveOrderProduct(String purchaseCode, String providerProductIds)
        throws Exception;
    
    /**
     * 更新采购单商品信息
     * 
     * @param id
     * @param barcode
     * @param purchaseQuantity
     * @param providerPrice
     * @param totalPrice
     * @param manufacturerDate
     * @param durabilityPeriod
     * @return
     * @throws Exception
     */
    int updateOrderProduct(int id, String barcode, String purchaseQuantity, String providerPrice, String manufacturerDate, String durabilityPeriod)
        throws Exception;
    
    /**
     * 删除采购单商品ID
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int removeOrderProduct(String ids)
        throws Exception;
    
    /**
     * 查询采购单编码
     * @param day
     * @return
     * @throws Exception
     */
    int createPurchaseCode(String day)
        throws Exception;
    
    int savePurchaseCode(String day)
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
    Map<String, Object> findPurchasePayDetailByParam(int id, String purchaseCode, String status, String providerId, String startTime, String endTime, int page, int rows)
        throws Exception;
    
    /**
     * 保存、修改采购单付款信息
     * @param param
     * @return
     * @throws Exception
     */
    int saveOrUpdatePurchasePayDetail(Map<String, Object> param)
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
     * @param params
     * @return
     * @throws Exception
     */
    int savePurchaseProductStoring(String params, String status, String storingRemark)
        throws Exception;
    
    /**
     * 查询批次
     * @param providerProductId
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    Map<String, Object> findBatchListInfo(String providerProductId, int page, int rows)
        throws Exception;
    
    /**
     * 批量更新采购单商品信息
     * @param file
     * @throws Exception
     */
    void importOrderProduct(MultipartFile file)
        throws Exception;
    
    /**
     * 导入采购单商品
     * @param file
     * @throws Exception
     */
    void importNewOrderProduct(MultipartFile file, String purchaseCode, String providerId)
        throws Exception;
    
    List<PurchaseOrderInfoEntity> findExportPurchaseOrder(String ids)
        throws Exception;
    
    void exportOrder(File parentFile, PurchaseOrderInfoEntity orderInfo)
        throws Exception;
    
    Map<String, Object> findProviderProductInfoByBarCode(String barCode)
        throws Exception;
    
    String findProductInfoByppId(int start, int max, int id)
        throws Exception;
    
    Map<String, Object> findProviderProductStockInfoById(int id)
        throws Exception;
    
    List<Map<String, Object>> findAllProviderProduct(Map<String, Object> para)
        throws Exception;
}
