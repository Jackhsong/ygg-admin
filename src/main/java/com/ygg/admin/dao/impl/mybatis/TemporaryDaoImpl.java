package com.ygg.admin.dao.impl.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.*;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.TemporaryDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.exception.DaoException;

@Repository("temporaryDao")
public class TemporaryDaoImpl extends BaseDaoImpl implements TemporaryDao
{
    @Override
    public int updateTableAccountImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableAccountImage");
    }
    
    /**
     * 更新activities_common表的image字段的图片
     */
    @Override
    public int updateTableActivitiesCommonImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableActivitiesCommonImage");
    }
    
    /**
     * 更新banner_window表的image字段的图片
     */
    @Override
    public int updateTableBannerWindowImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableBannerWindowImage");
    }
    
    /**
     * 更新brand表的image字段的图片
     */
    @Override
    public int updateTableBrandImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableBrandImage");
    }
    
    /**
     * 更新gege_image_activities表的image字段的图片
     */
    @Override
    public int updateTableGegeImageActivitiesImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableGegeImageActivitiesImage");
    }
    
    /**
     * 更新gege_image_product表的image字段的图片
     */
    @Override
    public int updateTableGegeImageProductImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableGegeImageProductImage");
    }
    
    /**
     * 更新order_product表的small_image字段的图片
     */
    @Override
    public int updateTableOrderProductImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableOrderProductImage");
    }
    
    /**
     * 更新order_product_refund表的image1，image2，image3字段的图片
     */
    @Override
    public int updateTableOrderProductRefundImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableOrderProductRefundImage");
    }
    
    /**
     * 更新product表的image1, image2, image3, image4, image5字段的图片
     */
    @Override
    public int updateTableProductImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableProductImage");
    }
    
    /**
     * 更新product_common表的medium_image, small_image字段的图片
     */
    @Override
    public int updateTableProductCommonImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableProductCommonImage");
    }
    
    /**
     * 更新product_mobile_detail表的content字段的图片
     */
    @Override
    public int updateTableProductMobileDetailImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableProductMobileDetailImage");
    }
    
    /**
     * 更新sale_tag表的image字段的图片
     */
    @Override
    public int updateTableSaleTagImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableSaleTagImage");
    }
    
    /**
     * 更新sale_window表的image字段的图片
     */
    @Override
    public int updateTableSaleWindowImage()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateTableSaleWindowImage");
    }
    
    @Override
    public int updateSellerIsNeedIdCardNumber()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateSellerIsNeedIdCardNumber");
    }
    
    @Override
    public int addSaleWindowToSellerSettlementTable()
        throws Exception
    {
        return getSqlSession().insert("TemporaryMapper.addSaleWindowToSellerSettlementTable");
    }
    
    @Override
    public int countStockByProductIdList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("TemporaryMapper.countStockByProductIdList", para);
    }
    
    @Override
    public int updateProductBaseIdForProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateProductBaseIdForProduct", para);
    }
    
    @Override
    public List<Integer> findAllProductBaseIdAfterTime(String createTime)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findAllProductBaseIdAfterTime", createTime);
    }
    
    @Override
    public List<ProductBaseMobileDetailEntity> findProductMobileDetailByProductBaseId(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findProductMobileDetailByProductBaseId", id);
    }
    
    @Override
    public int updateProductMobileDetailOrderById(int id, byte order)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("order", order);
        return getSqlSession().update("TemporaryMapper.updateProductMobileDetailOrderById", para);
    }
    
    @Override
    public List<Map<String, Object>> findMallProductIdAndCode()
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionRead().selectList("TemporaryMapper.findMallProductIdAndCode");
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public int updateMallProductCode(Map<String, Object> map)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateMallProductCode", map);
    }
    
    @Override
    public int updateOrderManualProductCost(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().update("TemporaryMapper.updateOrderManualProductCost", para);
    }
    
    @Override
    public int updateOrderProductCost(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateOrderProductCost", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllProductCostInfo()
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findAllProductCostInfo");
    }
    
    @Override
    public List<Map<String, Object>> findAllGameReceivedMobile()
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionRead().selectList("TemporaryMapper.findAllGameReceivedMobile");
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public int insertRelationActivityAndReceivedMobile(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("TemporaryMapper.insertRelationActivityAndReceivedMobile", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllGameAndAccount()
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionRead().selectList("TemporaryMapper.findAllGameAndAccount");
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public int insertRelationActivityAndAccount(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("TemporaryMapper.insertRelationActivityAndAccount", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllChannelReceivedMobile()
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionRead().selectList("TemporaryMapper.findAllChannelReceivedMobile");
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public List<Map<String, Object>> findAllChannelAndAccount()
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionRead().selectList("TemporaryMapper.findAllChannelAndAccount");
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public int updateProductBase(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateProductBase", para);
    }
    
    @Override
    public List<ProductBaseEntity> findAllProductBase(Map<String, Object> para)
        throws Exception
    {
        List<ProductBaseEntity> pbeList = this.getSqlSessionRead().selectList("TemporaryMapper.findAllProductBase", para);
        return pbeList == null ? new ArrayList<ProductBaseEntity>() : pbeList;
    }
    
    @Override
    public int updateProductBaseQualityPromiseType(ProductBaseEntity pbe)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateProductBaseQualityPromiseType", pbe);
    }
    
    @Override
    public int findAatherAccountIdFromAccountPartnerRelationByCurrAccountId(int currentAccountId)
        throws Exception
    {
        Integer fatherAccountId = this.getSqlSessionRead().selectOne("TemporaryMapper.findAatherAccountIdFromAccountPartnerRelationByCurrAccountId", currentAccountId);
        return fatherAccountId == null ? -1 : fatherAccountId.intValue();
    }
    
    @Override
    public boolean isExistPartnerRelation(Map<String, Object> para)
        throws Exception
    {
        int count = this.getSqlSessionRead().selectOne("TemporaryMapper.isExistPartnerRelation", para);
        return count > 0;
    }
    
    @Override
    public List<Map<String, Object>> findAllProductCommentProductId()
        throws Exception
    {
        List<Map<String, Object>> productList = this.getSqlSessionRead().selectList("TemporaryMapper.findAllProductCommentProductId");
        return productList == null ? new ArrayList<Map<String, Object>>() : productList;
    }
    
    @Override
    public int updateProductCommentProductBaseId(int commentId, int productBaseId)
        throws Exception
    {
        Map<String, Integer> para = new HashMap<String, Integer>();
        para.put("id", commentId);
        para.put("productBaseId", productBaseId);
        return this.getSqlSession().update("TemporaryMapper.updateProductCommentProductBaseId", para);
    }
    
    @Override
    public int updateProductDeliverAreaProvinceCode()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateProductDeliverAreaProvinceCode");
    }
    
    @Override
    public int updateProductDeliverAreaCityceCode()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateProductDeliverAreaCityceCode");
    }
    
    @Override
    public int updateProductDeliverAreaDistrictCode()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateProductDeliverAreaDistrictCode");
    }
    
    @Override
    public int updateSellerDeliverAreaProvinceCode()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateSellerDeliverAreaProvinceCode");
    }
    
    @Override
    public int updateSellerDeliverAreaCityceCode()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateSellerDeliverAreaCityceCode");
    }
    
    @Override
    public int updateSellerDeliverAreaDistrictCode()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateSellerDeliverAreaDistrictCode");
    }
    
    @Override
    public int updateOrderQuestionDealDetail(OrderQuestionEntity oq)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateOrderQuestionDealDetail", oq);
    }
    
    @Override
    public List<Map<String, Object>> findAllOrderQuestionCustomerImage()
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("TemporaryMapper.findAllOrderQuestionCustomerImage");
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<OrderQuestionProgressEntity> findAllOrderQuestionCustomerProgress()
        throws Exception
    {
        List<OrderQuestionProgressEntity> reList = this.getSqlSessionRead().selectList("TemporaryMapper.findAllOrderQuestionCustomerProgress");
        return reList == null ? new ArrayList<OrderQuestionProgressEntity>() : reList;
    }
    
    @Override
    public int updateOrderQuestionCustomerProgressNoImage(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateOrderQuestionCustomerProgressNoImage", para);
    }
    
    @Override
    public int deleteOrderQuestionCustomerImage(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().delete("TemporaryMapper.deleteOrderQuestionCustomerImage", para);
    }
    
    @Override
    public int updateOrderQuestionCustomerImage(Map<String, Object> map)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateOrderQuestionCustomerImage", map);
    }
    
    @Override
    public List<Map<String, Object>> findAllOrderQuestionSellerImage()
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("TemporaryMapper.findAllOrderQuestionSellerImage");
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<OrderQuestionProgressEntity> findAllOrderQuestionSellerProgress()
        throws Exception
    {
        List<OrderQuestionProgressEntity> reList = this.getSqlSessionRead().selectList("TemporaryMapper.findAllOrderQuestionSellerProgress");
        return reList == null ? new ArrayList<OrderQuestionProgressEntity>() : reList;
    }
    
    @Override
    public int updateOrderQuestionSellerProgressNoImage(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateOrderQuestionSellerProgressNoImage", para);
    }
    
    @Override
    public int deleteOrderQuestionSellerImage(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().delete("TemporaryMapper.deleteOrderQuestionSellerImage", para);
    }
    
    @Override
    public int updateOrderQuestionSellerImage(Map<String, Object> map)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateOrderQuestionSellerImage", map);
    }
    
    @Override
    public List<Map<String, Object>> findBaseProductSimpleInfo()
        throws Exception
    {
        return this.getSqlSessionRead().selectList("TemporaryMapper.findBaseProductSimpleInfo");
    }
    
    @Override
    public int updateBaseProductInfo(Map<String, Object> it)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateBaseProductInfo", it);
    }
    
    @Override
    public List<ProductEntity> findProductInfoBybid(String productBaseId)
        throws Exception
    {
        List<ProductEntity> reList = this.getSqlSessionRead().selectList("TemporaryMapper.findProductInfoBybid", productBaseId);
        return reList == null ? new ArrayList<ProductEntity>() : reList;
    }
    
    @Override
    public ProductCountEntity findProductCountByIdForUpdate(String productId)
        throws Exception
    {
        return this.getSqlSession().selectOne("TemporaryMapper.findProductCountByIdForUpdate", productId);
    }
    
    @Override
    public List<ProductCountEntity> findProductCountListByIdForUpdate(List<String> saleProductIdList)
        throws Exception
    {
        List<ProductCountEntity> reList = this.getSqlSession().selectList("TemporaryMapper.findProductCountListByIdForUpdate", saleProductIdList);
        return reList == null ? new ArrayList<ProductCountEntity>() : reList;
    }
    
    @Override
    public int updateProductCount(ProductCountEntity pce)
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateProductCount", pce);
    }
    
    @Override
    public int countSellerDeliverAreaTemplate(int sellerId)
        throws Exception
    {
        int count = getSqlSession().selectOne("TemporaryMapper.countSellerDeliverAreaTemplate", sellerId);
        return count + 1;
    }
    
    @Override
    public int updateBaseProductDeliverAreaTemplateId(ProductBaseEntity pbe)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateBaseProductDeliverAreaTemplateId", pbe);
    }
    
    @Override
    public int updateProductBaseProposalSalesPrice()
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateProductBaseProposalSalesPrice");
    }
    
    @Override
    public int updateDeliverTemplateDesc(SellerDeliverAreaTemplateEntity template)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateDeliverTemplateDesc", template);
    }
    
    @Override
    public List<Integer> findAllDistinctSellerId()
        throws Exception
    {
        List<Integer> reList = getSqlSessionRead().selectList("TemporaryMapper.findAllDistinctSellerId");
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public List<ProductBaseEntity> findProductBaseBySidAndTid(Integer sellerId, int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerId", sellerId);
        para.put("templateId", id);
        return getSqlSessionRead().selectList("TemporaryMapper.findProductBaseBySidAndTid", para);
    }
    
    @Override
    public int updateBaseProductDeliverAreaTemplateBysid(int sellerId, int templateId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerId", sellerId);
        para.put("templateId", templateId);
        return getSqlSession().update("TemporaryMapper.updateBaseProductDeliverAreaTemplateBysid", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSellerDeliverAreaTemplate()
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("TemporaryMapper.findAllSellerDeliverAreaTemplate");
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int updateBaseProductDeliverAreaType(ProductBaseEntity pbe)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateBaseProductDeliverAreaType", pbe);
    }
    
    @Override
    public List<String> findAllSonNumber(String time)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findAllSonNumber", time);
    }
    
    @Override
    public List<Map<String, Object>> findAllHBOrderNumber(String sonNumber)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findAllHBOrderNumber", sonNumber);
    }
    
    @Override
    public List<Map<String, Object>> findSuccessOrderInfo()
        throws Exception
    {
        return getSqlSession().selectList("TemporaryMapper.findSuccessOrderInfo");
    }
    
    @Override
    public int addAccountSuccessOrderRecord(AccountSuccessOrderRecordEntity record)
        throws DaoException
    {
        return getSqlSession().insert("TemporaryMapper.addAccountSuccessOrderRecord", record);
    }
    
    @Override
    public int updateAccountInfoById(Map<String, Object> para)
        throws DaoException
    {
        return this.getSqlSession().update("TemporaryMapper.updateAccountInfoById", para);
    }
    
    @Override
    public int updateSellerEdbInfo()
        throws Exception
    {
        return this.getSqlSession().update("TemporaryMapper.updateSellerEdbInfo");
    }
    
    @Override
    public int countTotalSalesByProductBaseId(int id)
        throws Exception
    {
        Integer result = getSqlSession().selectOne("TemporaryMapper.countTotalSalesByProductBaseId", id);
        return result == null ? 0 : result.intValue();
    }
    
    @Override
    public List<Map<String, Object>> findAllSelfProductBase()
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findAllSelfProductBase");
    }
    
    @Override
    public int updateProductBaseProviderProductId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateProductBaseProviderProductId", para);
    }
    
    @Override
    public int updateRefundFinancialAffairsCardId(int id, int cardId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("financialAffairsCardId", cardId);
        return getSqlSession().update("TemporaryMapper.updateRefundFinancialAffairsCardId", para);
    }
    
    @Override
    public String findIdCardByAccountId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("TemporaryMapper.findIdCardByAccountId", id);
    }
    
    @Override
    public int updateAccountBirthDay(int id, String birthDay)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("birthDay", birthDay);
        return getSqlSession().update("TemporaryMapper.updateAccountBirthDay", param);
    }

    @Override
    public int updateAccountSecret(int id, String SecretKey)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("secretKey", SecretKey);
        return getSqlSession().update("TemporaryMapper.updateAccountSecret", param);
    }

    @Override
    public String findOrderRefundTimeByOrderId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("TemporaryMapper.findOrderRefundTimeByOrderId", id);
    }
    
    @Override
    public int updateAccountPetname(int id, String petname)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("petname", petname);
        return getSqlSession().update("TemporaryMapper.updateAccountPetname", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderRefundExplain(String date)
        throws Exception
    {
        return getSqlSession().selectList("TemporaryMapper.findOrderRefundExplain", date);
    }
    
    @Override
    public List<Map<String, Object>> findSellerOrderInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findSellerOrderInfo", para);
    }
    
    @Override
    public Timestamp findLogisticsTime(String channel, String number)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("channel", channel);
        para.put("number", number);
        Map<String, Object> reMap = getSqlSessionRead().selectOne("TemporaryMapper.findLogisticsTime", para);
        if (reMap == null || reMap.isEmpty() || reMap.get("create_time") == null)
        {
            return null;
        }
        else
        {
            return (Timestamp)reMap.get("create_time");
        }
    }
    
    @Override
    public int insertLogisticsTimeout(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("TemporaryMapper.insertLogisticsTimeout", para);
    }
    
    @Override
    public Integer findSendTimeoutComplain(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("TemporaryMapper.findSendTimeoutComplain", orderId);
    }
    
    @Override
    public List<Map<String, Object>> findOrderProductComment()
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findOrderProductComment");
    }
    
    @Override
    public int updateOrderProductComment(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateOrderProductComment", para);
    }
    
    @Override
    public List<Map<String, Object>> findLogisticsTimeout(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findLogisticsTimeout", para);
    }
    
    @Override
    public int updateLogisticsTimeout(Map<String, Object> order)
        throws Exception
    {
        return getSqlSession().update("TemporaryMapper.updateLogisticsTimeout", order);
    }
    
    @Override
    public List<Map<String, Object>> findOrderLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("TemporaryMapper.findOrderLogisticsTimeoutOrders", para);
    }
    
    @Override
    public int findProductSumStockByPidAndType(int productBaseId, int productType)
    {
        Map<String, Object> param = new HashMap<>();
        param.put("productBaseId", productBaseId);
        param.put("productType", productType);
        Integer total = getSqlSessionRead().selectOne("TemporaryMapper.findProductSumStockByPidAndType", param);
        return total == null ? 0 : total.intValue();
    }

    @Override
    public List<Map<String, Object>> findMaxCreateTimeGroupByAccountId(Map<String,Object> para) throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findMaxCreateTimeGroupByAccountId",para);
    }

    @Override
    public List<Map<String, Object>> findAccountIdAndTimesByaids(List<Integer> accountIds) throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findAccountIdAndTimesByaids",accountIds);
    }

    @Override
    public List<ProductBaseEntity> findProductBaseBySellerIds(List<String> sellerIds) throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findProductBaseBySellerIds",sellerIds);
    }

    @Override
    public List<ProductBaseMobileDetailEntity> findProductBaseMobileDetailsByProductId(int productId) throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findProductBaseMobileDetailsByProductBaseId",productId);
    }

    @Override
    public List<ProductEntity> findProductByProductBaseId(int productBaseId) throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findProductByProductBaseId",productBaseId);
    }

    @Override
    public List<ProductMobileDetailEntity> findProductMobileDetailByProducId(int productId) throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findProductMobileDetailByProducId",productId);
    }

    @Override
    public List<Integer> findLackImageProductIds() throws Exception {
        return getSqlSessionRead().selectList("TemporaryMapper.findLackImageProductIds");
    }
}
