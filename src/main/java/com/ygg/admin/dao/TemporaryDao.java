package com.ygg.admin.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.*;
import com.ygg.admin.exception.DaoException;

public interface TemporaryDao
{
    
    int updateTableAccountImage()
        throws Exception;
    
    int updateTableActivitiesCommonImage()
        throws Exception;
    
    int updateTableBannerWindowImage()
        throws Exception;
    
    int updateTableBrandImage()
        throws Exception;
    
    int updateTableGegeImageActivitiesImage()
        throws Exception;
    
    int updateTableGegeImageProductImage()
        throws Exception;
    
    int updateTableOrderProductImage()
        throws Exception;
    
    int updateTableOrderProductRefundImage()
        throws Exception;
    
    int updateTableProductImage()
        throws Exception;
    
    int updateTableProductCommonImage()
        throws Exception;
    
    int updateTableProductMobileDetailImage()
        throws Exception;
    
    int updateTableSaleTagImage()
        throws Exception;
    
    int updateTableSaleWindowImage()
        throws Exception;
    
    int updateSellerIsNeedIdCardNumber()
        throws Exception;
    
    int addSaleWindowToSellerSettlementTable()
        throws Exception;
    
    int countStockByProductIdList(Map<String, Object> para)
        throws Exception;
    
    int updateProductBaseIdForProduct(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findAllProductBaseIdAfterTime(String createTime)
        throws Exception;
    
    List<ProductBaseMobileDetailEntity> findProductMobileDetailByProductBaseId(int id)
        throws Exception;
    
    int updateProductMobileDetailOrderById(int id, byte order)
        throws Exception;
    
    List<Map<String, Object>> findMallProductIdAndCode()
        throws Exception;
    
    int updateMallProductCode(Map<String, Object> map)
        throws Exception;
    
    int updateOrderManualProductCost(Map<String, Object> para)
        throws Exception;
    
    int updateOrderProductCost(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllProductCostInfo()
        throws Exception;
    
    List<Map<String, Object>> findAllGameReceivedMobile()
        throws Exception;
    
    List<Map<String, Object>> findAllGameAndAccount()
        throws Exception;
    
    int insertRelationActivityAndReceivedMobile(Map<String, Object> para)
        throws Exception;
    
    int insertRelationActivityAndAccount(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllChannelReceivedMobile()
        throws Exception;
    
    List<Map<String, Object>> findAllChannelAndAccount()
        throws Exception;
    
    int updateProductBase(Map<String, Object> para)
        throws Exception;
    
    List<ProductBaseEntity> findAllProductBase(Map<String, Object> para)
        throws Exception;
    
    int updateProductBaseQualityPromiseType(ProductBaseEntity pbe)
        throws Exception;
    
    boolean isExistPartnerRelation(Map<String, Object> para)
        throws Exception;
    
    int findAatherAccountIdFromAccountPartnerRelationByCurrAccountId(int currentAccountId)
        throws Exception;
    
    List<Map<String, Object>> findAllProductCommentProductId()
        throws Exception;
    
    int updateProductCommentProductBaseId(int commentId, int productBaseId)
        throws Exception;
    
    int updateProductDeliverAreaProvinceCode()
        throws Exception;
    
    int updateProductDeliverAreaCityceCode()
        throws Exception;
    
    int updateProductDeliverAreaDistrictCode()
        throws Exception;
    
    int updateSellerDeliverAreaProvinceCode()
        throws Exception;
    
    int updateSellerDeliverAreaCityceCode()
        throws Exception;
    
    int updateSellerDeliverAreaDistrictCode()
        throws Exception;
    
    int updateOrderQuestionDealDetail(OrderQuestionEntity oq)
        throws Exception;
    
    List<Map<String, Object>> findAllOrderQuestionCustomerImage()
        throws Exception;
    
    List<OrderQuestionProgressEntity> findAllOrderQuestionCustomerProgress()
        throws Exception;
    
    int updateOrderQuestionCustomerProgressNoImage(Map<String, Object> para)
        throws Exception;
    
    int deleteOrderQuestionCustomerImage(Map<String, Object> para)
        throws Exception;
    
    int updateOrderQuestionCustomerImage(Map<String, Object> map)
        throws Exception;
    
    List<Map<String, Object>> findAllOrderQuestionSellerImage()
        throws Exception;
    
    List<OrderQuestionProgressEntity> findAllOrderQuestionSellerProgress()
        throws Exception;
    
    int updateOrderQuestionSellerProgressNoImage(Map<String, Object> para)
        throws Exception;
    
    int deleteOrderQuestionSellerImage(Map<String, Object> para)
        throws Exception;
    
    int updateOrderQuestionSellerImage(Map<String, Object> map)
        throws Exception;
    
    List<Map<String, Object>> findBaseProductSimpleInfo()
        throws Exception;
    
    int updateBaseProductInfo(Map<String, Object> it)
        throws Exception;
    
    List<ProductEntity> findProductInfoBybid(String productBaseId)
        throws Exception;
    
    ProductCountEntity findProductCountByIdForUpdate(String productId)
        throws Exception;
    
    List<ProductCountEntity> findProductCountListByIdForUpdate(List<String> saleProductIdList)
        throws Exception;
    
    int updateProductCount(ProductCountEntity pce)
        throws Exception;
    
    int countSellerDeliverAreaTemplate(int id)
        throws Exception;
    
    int updateBaseProductDeliverAreaTemplateId(ProductBaseEntity pbe)
        throws Exception;
    
    int updateProductBaseProposalSalesPrice()
        throws Exception;
    
    int updateDeliverTemplateDesc(SellerDeliverAreaTemplateEntity template)
        throws Exception;
    
    List<Integer> findAllDistinctSellerId()
        throws Exception;
    
    List<ProductBaseEntity> findProductBaseBySidAndTid(Integer sellerId, int id)
        throws Exception;
    
    int updateBaseProductDeliverAreaTemplateBysid(int sellerId, int templateId)
        throws Exception;
    
    List<Map<String, Object>> findAllSellerDeliverAreaTemplate()
        throws Exception;
    
    int updateBaseProductDeliverAreaType(ProductBaseEntity pbe)
        throws Exception;
    
    List<String> findAllSonNumber(String time)
        throws Exception;
    
    List<Map<String, Object>> findAllHBOrderNumber(String sonNumber)
        throws Exception;
    
    List<Map<String, Object>> findSuccessOrderInfo()
        throws Exception;
    
    int addAccountSuccessOrderRecord(AccountSuccessOrderRecordEntity record)
        throws DaoException;
    
    int updateAccountInfoById(Map<String, Object> para)
        throws DaoException;
    
    int updateSellerEdbInfo()
        throws Exception;
    
    int countTotalSalesByProductBaseId(int id)
        throws Exception;
    
    List<Map<String, Object>> findAllSelfProductBase()
        throws Exception;
    
    int updateProductBaseProviderProductId(Map<String, Object> para)
        throws Exception;
    
    int updateRefundFinancialAffairsCardId(int id, int cardId)
        throws Exception;
    
    String findIdCardByAccountId(int id)
        throws Exception;
    
    int updateAccountBirthDay(int id, String birthDay)
        throws Exception;
    
    String findOrderRefundTimeByOrderId(int id)
        throws Exception;
    
    int updateAccountPetname(int id, String petname)
        throws Exception;
    
    List<Map<String, Object>> findOrderRefundExplain(String date)
        throws Exception;
    
    List<Map<String, Object>> findSellerOrderInfo(Map<String, Object> para)
        throws Exception;
    
    Timestamp findLogisticsTime(String channel, String number)
        throws Exception;
    
    int insertLogisticsTimeout(Map<String, Object> para)
        throws Exception;
    
    Integer findSendTimeoutComplain(int orderId)
        throws Exception;
    
    List<Map<String, Object>> findOrderProductComment()
        throws Exception;
    
    int updateOrderProductComment(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findLogisticsTimeout(Map<String, Object> para)
        throws Exception;
    
    int updateLogisticsTimeout(Map<String, Object> order)
        throws Exception;
    
    List<Map<String, Object>> findOrderLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception;
    
    int findProductSumStockByPidAndType(int productBaseId, int productType);

    List<Map<String, Object>> findMaxCreateTimeGroupByAccountId(Map<String,Object> para) throws  Exception;

    List<Map<String,Object>> findAccountIdAndTimesByaids(List<Integer> accountIds) throws Exception;

    int updateAccountSecret(int id, String SecretKey)
        throws Exception;

    List<ProductBaseEntity> findProductBaseBySellerIds(List<String> sellerIds) throws Exception;

    List<ProductBaseMobileDetailEntity> findProductBaseMobileDetailsByProductId(int productBaseId) throws Exception;

    List<ProductEntity> findProductByProductBaseId(int productBaseId) throws Exception;

    List<ProductMobileDetailEntity> findProductMobileDetailByProducId(int productId) throws Exception;

    List<Integer> findLackImageProductIds() throws Exception;
}
