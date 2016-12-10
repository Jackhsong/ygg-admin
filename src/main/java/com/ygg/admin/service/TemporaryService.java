package com.ygg.admin.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.entity.TempProductEntity;

public interface TemporaryService
{
    
    int updateImageFromUpaiyunToAliyun()
        throws Exception;
    
    int updateSellerIsNeedIdCardNumber()
        throws Exception;
    
    int addSaleWindowToSellerSettlementTable()
        throws Exception;
    
    int resetOverseasProductInfo()
        throws Exception;
    
    int insertProductBaseInfo(MultipartFile file)
        throws Exception;
    
    int resetOrderRefundCardInfo()
        throws Exception;
    
    int updateProductBaseDetailOrder()
        throws Exception;
    
    int updateMallProductCode()
        throws Exception;
    
    int resetInviteIntegral(MultipartFile file)
        throws Exception;
    
    int resetOrderProductCost()
        throws Exception;
    
    Map<String, Object> moveData()
        throws Exception;
    
    int updateProductBaseSaleFlag(List<Map<String, Object>> dataList)
        throws Exception;
    
    int qualityPromiseType()
        throws Exception;
    
    int movePartner(String phoneNumber)
        throws Exception;
    
    int updateProductCommentBaseId()
        throws Exception;
    
    int updateProductAndSellerDeliverArea()
        throws Exception;
    
    int updateOrderQuestionDealDetail()
        throws Exception;
    
    int updateQuestionProgressImage()
        throws Exception;
    
    List<Map<String, Object>> findBaseProductSimpleInfo()
        throws Exception;
    
    int importBaseProductTipDate(MultipartFile file)
        throws Exception;
    
    int addClassNameAndMethodForPermission()
        throws Exception;
    
    int updateClassNameAndMethodForPermission()
        throws Exception;
    
    List<TempProductEntity> importBaseProductStock(MultipartFile file)
        throws Exception;
    
    void returnStock(TempProductEntity temp)
        throws Exception;
    
    String insertSellerDeliverTemplate()
        throws Exception;
    
    String updateProductBaseProposalSalesPrice()
        throws Exception;
    
    String updateDeliverTemplate()
        throws Exception;
    
    String filterRepeatArea()
        throws Exception;
    
    String deleteProductBaseDeliverTemplate()
        throws Exception;
    
    Map<String, String> findHBOrderNumber()
        throws Exception;
    
    String saveAccountLevelInfo()
        throws Exception;
    
    int updateSellerEdbInfo()
        throws Exception;
    
    String updateProductBaseGroupCount()
        throws Exception;
    
    String updateProductBaseTotalSales()
        throws Exception;
    
    String updateProductBaseProviderProductId()
        throws Exception;
    
    int updateOrderProductInfo()
        throws Exception;
    
    String updateOrderExpireTime()
        throws Exception;
    
    String updateAccountBirthDay()
        throws Exception;
    
    String updateAccountPetname()
        throws Exception;
    
    List<Map<String, Object>> findOrderRefundExplain(String string)
        throws Exception;
    
    String updateOrderLogisticsTimeout()
        throws Exception;
    
    String updateOrderProductComment()
        throws Exception;
    
    String updateLogisticsTimeoutOrderType()
        throws Exception;
    
    String updateDeliverAreaAndExtraPostage()
        throws Exception;
    
    String updateProductBaseStock()
        throws Exception;
        
    List<Map<String, String>> findOrderTimes(int minDay,int maxDay)
        throws Exception;
    int updateAccountSecretKey(Map<String, Object> para)
            throws Exception;
    
    String updateProductDetailImge()
        throws Exception;

    String insertProductDetailImge()
        throws Exception;

    String insertProductHistorySalesVolume(String start, String end)
        throws Exception;
}
