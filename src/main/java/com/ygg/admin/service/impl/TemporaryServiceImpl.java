package com.ygg.admin.service.impl;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.ygg.admin.dao.*;
import org.apache.commons.beanutils.BeanPropertyValueChangeClosure;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.PartnerEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.AccountRecommendRelationEntity;
import com.ygg.admin.entity.AccountSuccessOrderRecordEntity;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.OrderQuestionProgressEntity;
import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.ProductBaseMobileDetailEntity;
import com.ygg.admin.entity.ProductCommonEntity;
import com.ygg.admin.entity.ProductCountEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ProductMobileDetailEntity;
import com.ygg.admin.entity.RefundEntity;
import com.ygg.admin.entity.RelationDeliverAreaTemplateEntity;
import com.ygg.admin.entity.RelationProductBaseDeliverAreaEntity;
import com.ygg.admin.entity.SellerDeliverAreaTemplateEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.TempProductEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.service.TemporaryService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.POIUtil;

@Service("temporaryService")
public class TemporaryServiceImpl implements TemporaryService
{
    
    private static Logger logger = Logger.getLogger(TemporaryServiceImpl.class);
    
    @Resource
    private TemporaryDao temporaryDao;
    
    @Resource
    private OverseasOrderDao overseasOrderDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Resource
    private RefundDao refundDao;
    
    @Resource
    private PartnerDao partnerDao;
    
    @Resource
    private AccountDao accountDao;
    
    @Resource
    private SaleFlagDao saleFlagDao;
    
    @Resource
    private OrderQuestionDao orderQuestionDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private AdminDao adminDao;
    
    @Resource
    private SellerDeliverAreaDao sellerDeliverAreaDao;
    
    @Resource
    private RelationDeliverAreaTemplateDao relationDeliverAreaTemplateDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private PurchaseDao purchaseDao;
    
    @Override
    public int updateImageFromUpaiyunToAliyun()
        throws Exception
    {
        // 更新account表的image字段的图片
        temporaryDao.updateTableAccountImage();
        
        logger.info("**********account表更新成功**********");
        
        // 更新activities_common表的image字段的图片
        temporaryDao.updateTableActivitiesCommonImage();
        
        logger.info("**********activities_common表更新成功**********");
        
        //更新banner_window表的image字段的图片
        temporaryDao.updateTableBannerWindowImage();
        
        logger.info("**********banner_window表更新成功**********");
        
        //更新brand表的image字段的图片
        temporaryDao.updateTableBrandImage();
        
        logger.info("**********brand表更新成功**********");
        
        //更新gege_image_activities表的image字段的图片
        temporaryDao.updateTableGegeImageActivitiesImage();
        
        logger.info("**********gege_image_activities表更新成功**********");
        
        //更新gege_image_product表的image字段的图片
        temporaryDao.updateTableGegeImageProductImage();
        
        logger.info("**********gege_image_product表更新成功**********");
        
        //更新order_product表的small_image字段的图片
        temporaryDao.updateTableOrderProductImage();
        
        logger.info("**********order_product表更新成功**********");
        
        //更新order_product_refund表的image1，image2，image3字段的图片
        temporaryDao.updateTableOrderProductRefundImage();
        
        logger.info("**********order_product_refund表更新成功**********");
        
        //更新product表的image1, image2, image3, image4, image5字段的图片
        temporaryDao.updateTableProductImage();
        
        logger.info("**********product表更新成功**********");
        
        //更新product_common表的medium_image, small_image字段的图片
        temporaryDao.updateTableProductCommonImage();
        
        logger.info("**********product_common表更新成功**********");
        
        //更新product_mobile_detail表的content字段的图片
        temporaryDao.updateTableProductMobileDetailImage();
        
        logger.info("**********product_mobile_detail表更新成功**********");
        
        //更新sale_tag表的image字段的图片
        temporaryDao.updateTableSaleTagImage();
        
        logger.info("**********sale_tag表更新成功**********");
        
        //更新sale_window表的image字段的图片
        temporaryDao.updateTableSaleWindowImage();
        
        logger.info("**********sale_window表更新成功**********");
        
        return 1;
        
    }
    
    @Override
    public int updateSellerIsNeedIdCardNumber()
        throws Exception
    {
        int result = temporaryDao.updateSellerIsNeedIdCardNumber();
        return result > 0 ? result : 0;
    }
    
    @Override
    public int addSaleWindowToSellerSettlementTable()
        throws Exception
    {
        int result = temporaryDao.addSaleWindowToSellerSettlementTable();
        return result > 0 ? result : 0;
    }
    
    @Override
    public int resetOverseasProductInfo()
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> reList = overseasOrderDao.findAllOverseasProductInfo(map);
        for (Map<String, Object> it : reList)
        {
            String orderNumber = it.get("orderNumber") == null ? null : it.get("orderNumber") + "";
            Integer id = Integer.valueOf(it.get("id") + "");
            if (orderNumber == null || orderNumber.equals(""))
            {
                //删除此条记录
                overseasOrderDao.deleteOverseasProductInfoById(id);
            }
            else
            {
                //更新sellerName
                OrderEntity order = orderDao.findOrderByNumber(orderNumber);
                SellerEntity seller = sellerDao.findSellerSimpleById(order.getSellerId());
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("id", id);
                para.put("sellerName", seller.getRealSellerName());
                para.put("sendAddress", seller.getSendAddress() + "(" + seller.getWarehouse() + ")");
                overseasOrderDao.updateOverseasProInfoForYY(para);
            }
        }
        return 1;
    }
    
    @Override
    public int insertProductBaseInfo(MultipartFile file)
        throws Exception
    {
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        
        int startNum = sheet.getFirstRowNum();
        int lastNum = sheet.getLastRowNum();
        int successNum = 0;
        if (startNum == lastNum)
        {
            if (workbook != null)
            {
                workbook.close();
            }
            return 0;
        }
        
        Map<Integer, List<Integer>> productGroupMap = new HashMap<Integer, List<Integer>>();
        for (int i = startNum + 1; i <= lastNum; i++)
        {
            Row row = sheet.getRow(i);
            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);
            
            if (cell0 != null)
            {
                cell0.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            if (cell1 != null)
            {
                cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            
            int productId = (int)cell0.getNumericCellValue();
            int groupId = (int)cell1.getNumericCellValue();
            
            List<Integer> productIdList = productGroupMap.get(groupId);
            if (productIdList == null)
            {
                productIdList = new ArrayList<Integer>();
                productIdList.add(productId);
                productGroupMap.put(groupId, productIdList);
            }
            else
            {
                productIdList.add(productId);
            }
            
        }
        
        for (Map.Entry<Integer, List<Integer>> entry : productGroupMap.entrySet())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> productIdList = entry.getValue();
            int productId = productIdList.get(0);
            para.put("productId", productId);
            para.put("idList", productIdList);
            
            ProductEntity product = productDao.findProductByID(productId, ProductEnum.PRODUCT_TYPE.SALE.getCode());
            if (product == null)
            {
                continue;
            }
            ProductCommonEntity common = productDao.findProductCommonByProductId(productId);
            List<ProductMobileDetailEntity> productMobileDetailList = productDao.findProductMobileDetailByPara(para);
            int totStock = temporaryDao.countStockByProductIdList(para);
            
            //插入基本商品表
            ProductBaseEntity base = new ProductBaseEntity();
            base.setBrandId(product.getBrandId());
            base.setSellerId(product.getSellerId());
            //            base.setSecondCategoryId(product.getSecondCategoryId());
            base.setGegeImageId(product.getGegeImageId());
            base.setGegeSay(product.getDesc() == null ? "" : product.getDesc());
            base.setCode(product.getCode() == null ? "" : product.getCode());
            base.setBarcode(product.getBarcode() == null ? "" : product.getBarcode());
            base.setName(product.getName() == null ? "" : product.getName());
            base.setSubmitType(1);
            base.setTotalStock(totStock);
            base.setSaleStock(totStock);
            base.setNetVolume(product.getNetVolume() == null ? "" : product.getNetVolume());
            base.setPlaceOfOrigin(product.getPlaceOfOrigin() == null ? "" : product.getPlaceOfOrigin());
            base.setManufacturerDate(product.getManufacturerDate() == null ? "" : product.getManufacturerDate());
            base.setStorageMethod(product.getStorageMethod() == null ? "" : product.getStorageMethod());
            base.setDurabilityPeriod(product.getDurabilityPeriod() == null ? "" : product.getDurabilityPeriod());
            base.setPeopleFor(product.getPeopleFor() == null ? "" : product.getPeopleFor());
            base.setFoodMethod(product.getFoodMethod() == null ? "" : product.getFoodMethod());
            base.setUseMethod(product.getUseMethod() == null ? "" : product.getUseMethod());
            base.setTip(product.getTip() == null ? "" : product.getTip());
            base.setImage1(product.getImage1() == null ? "" : product.getImage1());
            base.setImage2(product.getImage2() == null ? "" : product.getImage2());
            base.setImage3(product.getImage3() == null ? "" : product.getImage3());
            base.setImage4(product.getImage4() == null ? "" : product.getImage4());
            base.setImage5(product.getImage5() == null ? "" : product.getImage5());
            base.setMediumImage(common == null ? "" : common.getMediumImage() == null ? "" : common.getMediumImage());
            base.setSmallImage(common == null ? "" : common.getSmallImage() == null ? "" : common.getSmallImage());
            base.setRemark(product.getRemark() == null ? "" : product.getRemark());
            base.setIsAvailable(1);
            productBaseDao.saveProductBase(base);
            successNum++;
            logger.info("*************正在导入第 " + successNum + " 条记录，productId=" + productId + "************");
            
            //插入基本商品详情表
            for (ProductMobileDetailEntity pmd : productMobileDetailList)
            {
                
                //如果是文字类型，不插入
                if (pmd.getContentType() == 2)
                {
                    continue;
                }
                ProductBaseMobileDetailEntity pbmd = new ProductBaseMobileDetailEntity();
                pbmd.setProductId(base.getId());
                pbmd.setContent(pmd.getContent() == null ? "" : pmd.getContent());
                pbmd.setContentType(pmd.getContentType());
                pbmd.setHeight(pmd.getHeight());
                pbmd.setWidth(pmd.getWidth());
                pbmd.setIsLink(pmd.getIsLink());
                pbmd.setLink(pmd.getLink() == null ? "" : pmd.getLink());
                pbmd.setLinkType(pmd.getLinkType());
                pbmd.setOrder(pmd.getOrder());
                productBaseDao.saveProductMobileDetail(pbmd);
            }
            
            //更新特卖商品关联表
            para.put("productBaseId", base.getId());
            temporaryDao.updateProductBaseIdForProduct(para);
            
        }
        if (workbook != null)
        {
            workbook.close();
        }
        return successNum;
    }
    
    @Override
    public int resetOrderRefundCardInfo()
        throws Exception
    {
        int num = 0;
        Map<String, Object> para = new HashMap<String, Object>();
        List<RefundEntity> reList = refundDao.findAllRefundByPara(para);
        for (RefundEntity refund : reList)
        {
            // 用户退款银行信息
            Map<String, Object> cardInfo = refundDao.findAccountCardById(refund.getAccountCardId());
            if (cardInfo == null)
            {
                logger.warn("查询不到卡号信息，跳过，refundId:" + refund.getId());
                continue;
            }
            Map<String, Object> updatePara = new HashMap<String, Object>();
            updatePara.put("id", refund.getId());
            updatePara.put("cardType", cardInfo.get("type"));
            updatePara.put("bankType", cardInfo.get("bankType"));
            updatePara.put("cardName", cardInfo.get("cardName"));
            updatePara.put("cardNumber", cardInfo.get("cardNumber"));
            int status = refundDao.updateRefund(updatePara);
            if (status == 1)
            {
                num++;
            }
            else
            {
                logger.warn("更新退款退货记录失败，refundId:" + refund.getId());
            }
        }
        return num;
    }
    
    @Override
    public int updateProductBaseDetailOrder()
        throws Exception
    {
        int count = 0;
        List<Integer> idList = temporaryDao.findAllProductBaseIdAfterTime("2015-06-26 19:50:21");
        for (int id : idList)
        {
            List<ProductBaseMobileDetailEntity> productMobileDetailList = temporaryDao.findProductMobileDetailByProductBaseId(id);
            byte order = 22;
            for (ProductBaseMobileDetailEntity pm : productMobileDetailList)
            {
                if (order < 0)
                {
                    break;
                }
                logger.info("**************将baseId=" + id + ",id=" + pm.getId() + "的详情图order从" + pm.getOrder() + "改成" + order + "*************");
                temporaryDao.updateProductMobileDetailOrderById(pm.getId(), order);
                order--;
                count++;
            }
        }
        logger.info("**************成功执行 " + count + "条*************");
        return count;
    }
    
    @Override
    public int updateMallProductCode()
        throws Exception
    {
        int num = 0;
        List<Map<String, Object>> list = temporaryDao.findMallProductIdAndCode();
        for (Map<String, Object> map : list)
        {
            temporaryDao.updateMallProductCode(map);
            logger.info("*******************将Id=" + map.get("productId") + "的商品编码设为=" + map.get("code") + ",条码设为=" + map.get("barCode") + "*****************");
            num++;
        }
        return num;
    }
    
    @Override
    public int resetInviteIntegral(MultipartFile file)
        throws Exception
    {
        int nums = 0;
        Map<String, Object> data = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
        List<Map<String, Object>> rowList = (List<Map<String, Object>>)data.get("data");
        List<Integer> idList = new ArrayList<Integer>();
        for (Map<String, Object> it : rowList)
        {
            Integer orderId = Integer.parseInt(it.get("cell0") == null ? "0" : it.get("cell0") + "");
            if (orderId > 0)
            {
                idList.add(orderId);
            }
        }
        if (idList.size() > 0)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("idList", idList);
            List<Map<String, Object>> reList = partnerDao.findAccountInfoByAccountRecommendedReturnPoint(para);
            List<Integer> succList = new ArrayList<Integer>();
            for (Map<String, Object> it : reList)
            {
                Integer accountId = Integer.parseInt(it.get("accountId") + "");
                Integer reId = Integer.parseInt(it.get("id") + "");
                AccountEntity ace = accountDao.findAccountById(accountId);
                int oldAvailablePoint = ace.getAvailablePoint();
                int newAvailablePoint = 0;
                int operatePoint = 2000;
                if (oldAvailablePoint <= 2000)
                {
                    //积分全部扣完
                    newAvailablePoint = 0;
                    operatePoint = oldAvailablePoint;
                }
                else
                {
                    //积分多余
                    newAvailablePoint = oldAvailablePoint - 2000;
                }
                
                Map<String, Object> upPara = new HashMap<String, Object>();
                upPara.put("oldAvailablePoint", oldAvailablePoint);
                upPara.put("id", accountId);
                upPara.put("newAvailablePoint", newAvailablePoint);
                int status = accountDao.updateIntegral(upPara);
                if (status == 1)
                {
                    nums++;
                    succList.add(reId);//记录主键id
                    //新增积分record
                    Map<String, Object> integralPara = new HashMap<String, Object>();
                    integralPara.put("accountId", accountId);
                    integralPara.put("operatePoint", operatePoint);
                    integralPara.put("totalPoint", oldAvailablePoint - operatePoint);
                    integralPara.put("operateType", AccountEnum.INTEGRAL_OPERATION_TYPE.ADMIN_OPERATION.getCode());
                    integralPara.put("arithmeticType", 2);
                    integralPara.put("createTime", DateTimeUtil.now());
                    accountDao.insertIntegralRecord(integralPara);
                    logger.warn("积分扣除success,accountId:" + accountId);
                }
                else
                {
                    logger.warn("积分扣除fail,accountId:" + accountId);
                }
            }
            //删除record
            if (succList.size() > 0)
            {
                Map<String, Object> deletePara = new HashMap<String, Object>();
                deletePara.put("idList", succList);
                int dNums = partnerDao.deleteAccountInfoByAccountRecommendedReturnPoint(deletePara);
                logger.info("成功删除" + dNums + "条记录");
            }
        }
        return nums;
    }
    
    @Override
    public int resetOrderProductCost()
        throws Exception
    {
        List<Map<String, Object>> reList = temporaryDao.findAllProductCostInfo();
        Map<String, Object> productCost = new HashMap<String, Object>();
        int nums = 0;
        for (Map<String, Object> base : reList)
        {
            String productId = base.get("id") + "";
            int submitType = Integer.parseInt(base.get("submit_type") + "");
            double wholesalePrice = Double.parseDouble(base.get("wholesale_price") + "");
            double proposalPrice = Double.parseDouble(base.get("proposal_price") + "");
            double deduction = Double.parseDouble(base.get("deduction") + "");
            double selfPurchasePrice = Double.parseDouble(base.get("self_purchase_price") + "");
            //计算 单位供货价
            double cost = 0;
            if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
            {
                cost = wholesalePrice;
            }
            else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
            {
                cost = (100 - deduction) * proposalPrice / 100;
            }
            else
            {
                cost = selfPurchasePrice;
            }
            productCost.put(productId, cost);
        }
        for (Map.Entry<String, Object> e : productCost.entrySet())
        {
            String productId = e.getKey();
            String cost = e.getValue() + "";
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("productId", productId);
            para.put("cost", cost);
            int _res = temporaryDao.updateOrderProductCost(para);
            nums += _res;
            int res = temporaryDao.updateOrderManualProductCost(para);
            nums += res;
        }
        return nums;
    }
    
    @Override
    public Map<String, Object> moveData()
        throws Exception
    {
        int gameReceivedCount = 0;
        int gameRegisterCount = 0;
        int channelReceivedCount = 0;
        int channelRegisterCount = 0;
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> gameReceivedMobileList = temporaryDao.findAllGameReceivedMobile();
        for (Map<String, Object> para : gameReceivedMobileList)
        {
            para.put("activityId", para.get("gameId"));
            para.put("acitvityType", 1);
            String mobileNumber = para.get("mobileNumber") + "";
            if (!StringUtils.isEmpty(mobileNumber))
            {
                temporaryDao.insertRelationActivityAndReceivedMobile(para);
                gameReceivedCount++;
            }
        }
        
        List<Map<String, Object>> gameAndAccountList = temporaryDao.findAllGameAndAccount();
        for (Map<String, Object> para : gameAndAccountList)
        {
            para.put("activityId", para.get("gameId"));
            para.put("acitvityType", 1);
            int accountId = Integer.valueOf(para.get("accountId") + "").intValue();
            if (accountId != 0)
            {
                temporaryDao.insertRelationActivityAndAccount(para);
                gameRegisterCount++;
            }
        }
        
        List<Map<String, Object>> channelReceivedMobileList = temporaryDao.findAllChannelReceivedMobile();
        for (Map<String, Object> para : channelReceivedMobileList)
        {
            para.put("activityId", para.get("channelId"));
            para.put("acitvityType", 2);
            String mobileNumber = para.get("mobileNumber") + "";
            if (!StringUtils.isEmpty(mobileNumber))
            {
                temporaryDao.insertRelationActivityAndReceivedMobile(para);
                channelReceivedCount++;
            }
        }
        List<Map<String, Object>> channelAndAccountList = temporaryDao.findAllChannelAndAccount();
        for (Map<String, Object> para : channelAndAccountList)
        {
            para.put("activityId", para.get("channelId"));
            para.put("acitvityType", 2);
            int accountId = Integer.valueOf(para.get("accountId") + "").intValue();
            if (accountId != 0)
            {
                temporaryDao.insertRelationActivityAndAccount(para);
                channelRegisterCount++;
            }
        }
        result.put("游戏领取人数", gameReceivedCount);
        result.put("游戏注册人数", gameRegisterCount);
        result.put("渠道领取人数", channelReceivedCount);
        result.put("渠道注册人数", channelRegisterCount);
        return result;
    }
    
    @Override
    public int updateProductBaseSaleFlag(List<Map<String, Object>> dataList)
        throws Exception
    {
        int result = 0;
        for (Map<String, Object> map : dataList)
        {
            int id = Integer.valueOf(map.get("cell0") == null ? "0" : map.get("cell0") + "");
            int saleFlagId = Integer.valueOf(map.get("cell1") == null ? "0" : map.get("cell1") + "");
            if (id > 0 && saleFlagId > 0)
            {
                int qualityPromiseType = 1;
                if (saleFlagId == 3)
                {
                    qualityPromiseType = 2;
                }
                Map<String, Object> para = new HashMap<>();
                para.put("id", id);
                para.put("qualityPromiseType", qualityPromiseType);
                para.put("saleFlagId", saleFlagId);
                int ss = temporaryDao.updateProductBase(para);
                if (ss == 1)
                {
                    result++;
                }
            }
        }
        return result;
    }
    
    @Override
    public int qualityPromiseType()
        throws Exception
    {
        int count = 0;
        List<ProductBaseEntity> pblist = temporaryDao.findAllProductBase(null);
        for (ProductBaseEntity pbe : pblist)
        {
            //除中国外，都设为进口商品
            String flagName = saleFlagDao.findFlagNameById(pbe.getSaleFlagId());
            if ("中国".equals(flagName.trim()))
            {
                pbe.setQualityPromiseType(ProductEnum.QUALITY_PROMISE_TYPE.HOMEMADE.getCode());
            }
            else
            {
                pbe.setQualityPromiseType(ProductEnum.QUALITY_PROMISE_TYPE.IMPORT.getCode());
            }
            if (temporaryDao.updateProductBaseQualityPromiseType(pbe) > 0)
            {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public int movePartner(String phoneNumber)
        throws Exception
    {
        int count = 0;
        AccountEntity ae = accountDao.findAccountByNameAndType(phoneNumber, AccountEnum.ACCOUNT_TYPE.MOBILE.getCode());
        if (ae == null || ae.getPartnerStatus() != PartnerEnum.PARTNER_STATUS.YES.getCode())
        {
            return count;
        }
        //查找ae邀请的小伙伴
        List<AccountRecommendRelationEntity> directInviteList = partnerDao.findRecommendRelationByFatherId(ae.getId());
        for (AccountRecommendRelationEntity arre : directInviteList)
        {
            
            if (arre.getCurrentIsPartner() == 0)
            {
                Map<String, Object> para = new HashMap<>();
                para.put("currAccountId", arre.getCurrentAccountId());
                para.put("fatherAccountId", ae.getId());
                //检查ae和当前邀请的小伙伴是否已经存在account_partner_relation中
                boolean isExist = temporaryDao.isExistPartnerRelation(para);
                if (!isExist)
                {
                    //ae和arre关系不存在，则检查arre是否存在邀请关系
                    int fatherAccountId = temporaryDao.findAatherAccountIdFromAccountPartnerRelationByCurrAccountId(arre.getCurrentAccountId());
                    
                    //arre有邀请关系，则删除原来的邀请关系，并且将account中id=fatherAccountId的sub_recommended_count减1
                    if (fatherAccountId != -1)
                    {
                        para.put("accountId", arre.getCurrentAccountId());
                        para.put("parentId", fatherAccountId);
                        //删除arre与fatherAccountId的邀请关系
                        partnerDao.deleteFromAccountPartnerRelation(para);
                        //将account中id=fatherAccountId的sub_recommended_count减1
                        partnerDao.updateAccountSubRecommendedCount(fatherAccountId);
                        
                        //将account中id=ae.getId()的recommended_count加1
                        partnerDao.updateAccountRecommendedCountForAddOne(ae.getId());
                        
                    }
                    
                    para.put("accountId", arre.getCurrentAccountId());
                    para.put("parentId", ae.getId());
                    //将ae和arre的邀请关系插入account_partner_relation
                    partnerDao.insertAccountPartnerRelation(para);
                    count++;
                }
            }
        }
        return count;
    }
    
    @Override
    public int updateProductCommentBaseId()
        throws Exception
    {
        int count = 0;
        List<Map<String, Object>> productIdList = temporaryDao.findAllProductCommentProductId();
        for (Map<String, Object> it : productIdList)
        {
            int commentId = Integer.valueOf(it.get("id") + "").intValue();
            int productId = Integer.valueOf(it.get("productId") + "").intValue();
            ProductEntity pe = productDao.findProductByID(productId, null);
            if (pe == null)
            {
                logger.info("Id=" + productId + "的商品不存在");
                continue;
            }
            if (temporaryDao.updateProductCommentProductBaseId(commentId, pe.getProductBaseId()) == 1)
            {
                count++;
                logger.info("将Id=" + commentId + "评论的基本商品Id修改为" + pe.getProductBaseId());
            }
        }
        return count;
    }
    
    @Override
    public int updateProductAndSellerDeliverArea()
        throws Exception
    {
        int count1 = temporaryDao.updateProductDeliverAreaProvinceCode();
        int count2 = temporaryDao.updateProductDeliverAreaCityceCode();
        int count3 = temporaryDao.updateProductDeliverAreaDistrictCode();
        int count4 = temporaryDao.updateSellerDeliverAreaProvinceCode();
        int count5 = temporaryDao.updateSellerDeliverAreaCityceCode();
        int count6 = temporaryDao.updateSellerDeliverAreaDistrictCode();
        return count1 + count2 + count3 + count4 + count5 + count6;
    }
    
    @Override
    public int updateOrderQuestionDealDetail()
        throws Exception
    {
        int count = 0;
        //顾客问题处理进度
        List<OrderQuestionEntity> reList = orderQuestionDao.findAllOrderQuestion();
        for (OrderQuestionEntity oq : reList)
        {
            User user = userDao.findUserById(oq.getCreateUser());
            StringBuilder sbCustomerDesc = new StringBuilder();
            sbCustomerDesc.append(new DateTime(CommonUtil.string2Date(oq.getCreateTime(), "yyyy-MM-dd HH:mm:ss")).toString("yyyy-MM-dd HH:mm:ss"))
                .append("&nbsp;")
                .append(user == null ? "匿名用户" : user.getRealname())
                .append("&nbsp;")
                .append(oq.getQuestionDesc())
                .append("<br/>");
            ;
            List<Map<String, Object>> cProgressList = orderQuestionDao.findCustomerProgressListByqid(oq.getId());
            for (Map<String, Object> it : cProgressList)
            {
                String cpCreateTime = new DateTime(CommonUtil.string2Date(it.get("createTime") + "", "yyyy-MM-dd HH:mm:ss")).toString("yyyy-MM-dd HH:mm:ss");
                int cpCreateUserId = Integer.parseInt(it.get("createUser") + "");
                User cpCreateUser = userDao.findUserById(cpCreateUserId);
                sbCustomerDesc.append(cpCreateTime)
                    .append("&nbsp;")
                    .append(cpCreateUser == null ? "匿名用户" : cpCreateUser.getRealname())
                    .append("&nbsp;")
                    .append(it.get("content") + "")
                    .append("<br/>");
            }
            oq.setCustomerDealDetail(sbCustomerDesc.toString());
            
            //商家问题处理进度
            StringBuilder sbSellerDesc = new StringBuilder();
            List<Map<String, Object>> sProgressList = orderQuestionDao.findSellerProgressListByqid(oq.getId());
            for (Map<String, Object> it : sProgressList)
            {
                String spCreateTime = new DateTime(CommonUtil.string2Date(it.get("createTime") + "", "yyyy-MM-dd HH:mm:ss")).toString("yyyy-MM-dd HH:mm:ss");
                int spCreateUserId = Integer.parseInt(it.get("createUser") + "");
                User spCreateUser = userDao.findUserById(spCreateUserId);
                sbSellerDesc.append(spCreateTime)
                    .append("&nbsp;")
                    .append(spCreateUser == null ? "匿名用户" : spCreateUser.getRealname())
                    .append("&nbsp;")
                    .append(it.get("content") + "")
                    .append("<br/>");
            }
            oq.setSellerDealDetail(sbSellerDesc.toString());
            if (temporaryDao.updateOrderQuestionDealDetail(oq) == 1)
            {
                count++;
            }
        }
        
        return count;
    }
    
    @Override
    public int updateQuestionProgressImage()
        throws Exception
    {
        //查询所有的order_question_customer_image
        List<Map<String, Object>> cImageList = temporaryDao.findAllOrderQuestionCustomerImage();
        //查询所有的order_question_customer_progress
        List<OrderQuestionProgressEntity> cProgressList = temporaryDao.findAllOrderQuestionCustomerProgress();
        
        Set<Integer> forUpdateNoImage = new HashSet<Integer>();
        Set<Integer> forDeleteEmptyImage = new HashSet<Integer>();
        Set<String> forUpdateImageProgress = new HashSet<String>();
        Map<String, Object> para = new HashMap<String, Object>();
        for (OrderQuestionProgressEntity pe : cProgressList)
        {
            for (Map<String, Object> mp : cImageList)
            {
                String image = mp.get("image").toString();
                String createTime = mp.get("create_time").toString();
                int id = Integer.parseInt(mp.get("id") + "");
                if (pe.getCreateTime().equals(createTime))
                {
                    
                    if (StringUtils.isEmpty(image))
                    {
                        forUpdateNoImage.add(pe.getId());//更新order_question_customer_progress中has_image=0
                        forDeleteEmptyImage.add(id);//删除order_question_customer_image中image=''的数据
                    }
                    else
                    {
                        forUpdateImageProgress.add(id + "#" + pe.getId());
                    }
                }
            }
        }
        int count1 = 0;
        if (forUpdateNoImage.size() > 0)
        {
            para.put("idList", new ArrayList<Integer>(forUpdateNoImage));
            para.put("hasImage", 0);
            count1 = temporaryDao.updateOrderQuestionCustomerProgressNoImage(para);
            logger.info("****************更新order_question_customer_progress共" + count1 + "为没有图片***************");
            para.clear();
            
        }
        int count2 = 0;
        if (forDeleteEmptyImage.size() > 0)
        {
            para.put("idList", new ArrayList<Integer>(forDeleteEmptyImage));
            count2 = temporaryDao.deleteOrderQuestionCustomerImage(para);
            logger.info("****************删除order_question_customer_image共" + count2 + "记录***************");
            
        }
        int count3 = 0;
        for (String it : forUpdateImageProgress)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", it.split("#")[0]);
            map.put("progressId", it.split("#")[1]);
            count3 += temporaryDao.updateOrderQuestionCustomerImage(map);
        }
        logger.info("****************更新order_question_customer_image共" + count3 + "记录***************");
        
        forUpdateNoImage.clear();
        forDeleteEmptyImage.clear();
        forUpdateImageProgress.clear();
        para.clear();
        
        //查询所有的order_question_seller_image
        List<Map<String, Object>> sImageList = temporaryDao.findAllOrderQuestionSellerImage();
        //查询所有的order_question_seller_progress
        List<OrderQuestionProgressEntity> sProgressList = temporaryDao.findAllOrderQuestionSellerProgress();
        
        for (OrderQuestionProgressEntity pe : sProgressList)
        {
            for (Map<String, Object> mp : sImageList)
            {
                String image = mp.get("image").toString();
                String createTime = mp.get("create_time").toString();
                int id = Integer.parseInt(mp.get("id") + "");
                if (pe.getCreateTime().equals(createTime))
                {
                    
                    if (StringUtils.isEmpty(image))
                    {
                        forUpdateNoImage.add(pe.getId());//更新order_question_seller_progress中has_image=0
                        forDeleteEmptyImage.add(id);//删除order_question_seller_image中image=''的数据
                    }
                    else
                    {
                        forUpdateImageProgress.add(id + "#" + pe.getId());
                    }
                }
            }
        }
        int count4 = 0;
        if (forUpdateNoImage.size() > 0)
        {
            para.put("idList", new ArrayList<Integer>(forUpdateNoImage));
            para.put("hasImage", 0);
            count4 = temporaryDao.updateOrderQuestionSellerProgressNoImage(para);
            logger.info("****************更新order_question_seller_progress共" + count4 + "为没有图片***************");
            para.clear();
            
        }
        int count5 = 0;
        if (forDeleteEmptyImage.size() > 0)
        {
            para.put("idList", new ArrayList<Integer>(forDeleteEmptyImage));
            count5 = temporaryDao.deleteOrderQuestionSellerImage(para);
            logger.info("****************删除order_question_seller_image共" + count5 + "记录***************");
            
        }
        int count6 = 0;
        for (String it : forUpdateImageProgress)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", it.split("#")[0]);
            map.put("progressId", it.split("#")[1]);
            count3 += temporaryDao.updateOrderQuestionSellerImage(map);
        }
        logger.info("****************更新order_question_seller_image共" + count6 + "记录***************");
        
        return count1 + count2 + count3 + count4 + count5 + count6;
    }
    
    @Override
    public List<Map<String, Object>> findBaseProductSimpleInfo()
        throws Exception
    {
        return temporaryDao.findBaseProductSimpleInfo();
    }
    
    @Override
    public int importBaseProductTipDate(MultipartFile file)
        throws Exception
    {
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        
        int startNum = sheet.getFirstRowNum();
        int lastNum = sheet.getLastRowNum();
        int successNum = 0;
        if (startNum == lastNum)
        {
            if (workbook != null)
            {
                workbook.close();
            }
            return 0;
        }
        
        List<Map<String, Object>> infoMap = new ArrayList<Map<String, Object>>();
        for (int i = startNum + 1; i <= lastNum; i++)
        {
            Row row = sheet.getRow(i);
            Cell cell0 = row.getCell(0);
            Cell cell3 = row.getCell(3);
            Cell cell4 = row.getCell(4);
            Cell cell5 = row.getCell(5);
            
            if (cell0 != null)
            {
                cell0.setCellType(Cell.CELL_TYPE_STRING);
            }
            if (cell3 != null)
            {
                cell3.setCellType(Cell.CELL_TYPE_STRING);
            }
            if (cell4 != null)
            {
                cell4.setCellType(Cell.CELL_TYPE_STRING);
            }
            if (cell5 != null)
            {
                cell5.setCellType(Cell.CELL_TYPE_STRING);
            }
            
            String id = cell0.getStringCellValue();
            String manufactureDate = cell3.getStringCellValue();
            String durabilityPeriod = cell4.getStringCellValue();
            String expireDate = cell5.getStringCellValue();
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("manufactureDate", manufactureDate);
            map.put("durabilityPeriod", durabilityPeriod);
            if (StringUtils.isNotEmpty(expireDate))
            {
                map.put("expireDate", expireDate);
            }
            infoMap.add(map);
        }
        
        for (Map<String, Object> it : infoMap)
        {
            int i = temporaryDao.updateBaseProductInfo(it);
            if (i > 0)
            {
                logger.info("更新成功：" + it.toString());
            }
            successNum += i;
            
        }
        if (workbook != null)
        {
            workbook.close();
        }
        return successNum;
    }
    
    @Override
    public int addClassNameAndMethodForPermission()
        throws Exception
    {
        int count = 0;
        List<String> classNameList = new ArrayList<String>()
        {
            {
                add("com.ygg.admin.controller.AccountController");
                add("com.ygg.admin.controller.ActivitySimplifyController");
                add("com.ygg.admin.controller.AdminController");
                add("com.ygg.admin.controller.AnalyzeController");
                add("com.ygg.admin.controller.BannerController");
                add("com.ygg.admin.controller.BirdexController");
                add("com.ygg.admin.controller.BrandController");
                add("com.ygg.admin.controller.CategoryController");
                add("com.ygg.admin.controller.CacheController");
                add("com.ygg.admin.controller.CouponCodeController");
                add("com.ygg.admin.controller.CouponController");
                add("com.ygg.admin.controller.CustomActivitiesController");
                add("com.ygg.admin.controller.CustomRegionController");
                add("com.ygg.admin.controller.FinanceController");
                add("com.ygg.admin.controller.GameActivityController");
                add("com.ygg.admin.controller.GateActivityController");
                add("com.ygg.admin.controller.GegeImageController");
                add("com.ygg.admin.controller.GroupBuyController");
                add("com.ygg.admin.controller.IndexSettingController");
                add("com.ygg.admin.controller.LotteryActivityController");
                add("com.ygg.admin.controller.MallWindowController");
                add("com.ygg.admin.controller.MenuController");
                add("com.ygg.admin.controller.NotSendMsgProductController");
                add("com.ygg.admin.controller.OrderController");
                add("com.ygg.admin.controller.OrderManualController");
                add("com.ygg.admin.controller.OrderQuestionController");
                add("com.ygg.admin.controller.OverseasDelayRemindDateController");
                add("com.ygg.admin.controller.OverseasOrderController");
                add("com.ygg.admin.controller.PageCustomController");
                add("com.ygg.admin.controller.PartnerController");
                add("com.ygg.admin.controller.PostageController");
                add("com.ygg.admin.controller.ProductBaseController");
                add("com.ygg.admin.controller.ProductBlacklistController");
                add("com.ygg.admin.controller.ProductCommentController");
                add("com.ygg.admin.controller.ProductController");
                add("com.ygg.admin.controller.RefundController");
                add("com.ygg.admin.controller.SaleController");
                add("com.ygg.admin.controller.SaleFlagController");
                add("com.ygg.admin.controller.SaleTagController");
                add("com.ygg.admin.controller.SaleWindowController");
                add("com.ygg.admin.controller.SellerController");
                add("com.ygg.admin.controller.SearchController");
                add("com.ygg.admin.controller.SignInController");
                add("com.ygg.admin.controller.SpecialActivityController");
                add("com.ygg.admin.controller.SmsController");
                add("com.ygg.admin.controller.SpecialActivityController");
                add("com.ygg.admin.controller.SpreadChannelController");
                add("com.ygg.admin.controller.SystemConfigController");
                add("com.ygg.admin.controller.SystemLogController");
                add("com.ygg.admin.controller.UserController");
            }
        };
        
        for (String className : classNameList)
        {
            String controllerName = className.substring(className.lastIndexOf(".") + 1);
            Class clazz = Class.forName(className);
            if (clazz != null)
            {
                Method[] methods = clazz.getDeclaredMethods();
                logger.info("控制器" + controllerName + "共有" + methods.length + "个方法");
                List<Map<String, String>> permissions = new ArrayList<>();
                for (Method method : methods)
                {
                    if (method.isAnnotationPresent(RequestMapping.class))
                    {
                        RequestMapping ann = method.getAnnotation(RequestMapping.class);
                        String[] values = ann.value();
                        String mapping = Arrays.toString(values).replaceAll("\\[", "").replaceAll("\\]", "").replaceFirst("/", "");
                        logger.info("mapping:" + values[0] + " <----> method:" + method.getName());
                        Map<String, String> cuPer = new HashMap<>();
                        cuPer.put("category", controllerName);
                        cuPer.put("permission", controllerName + "_" + mapping.split("/")[0]);
                        cuPer.put("description", method.getName());
                        permissions.add(cuPer);
                    }
                    
                }
                if (permissions.size() > 0)
                {
                    count += adminDao.insertPermission(permissions);
                }
            }
        }
        return count;
    }
    
    @Override
    public int updateClassNameAndMethodForPermission()
        throws Exception
    {
        int count = 0;
        List<String> classNameList = new ArrayList<String>()
        {
            {
                add("com.ygg.admin.controller.AccountController");
                add("com.ygg.admin.controller.ActivitySimplifyController");
                add("com.ygg.admin.controller.AdminController");
                add("com.ygg.admin.controller.AnalyzeController");
                add("com.ygg.admin.controller.BannerController");
                add("com.ygg.admin.controller.BirdexController");
                add("com.ygg.admin.controller.BrandController");
                add("com.ygg.admin.controller.CategoryController");
                add("com.ygg.admin.controller.CacheController");
                add("com.ygg.admin.controller.CouponCodeController");
                add("com.ygg.admin.controller.CouponController");
                add("com.ygg.admin.controller.CustomActivitiesController");
                add("com.ygg.admin.controller.CustomRegionController");
                add("com.ygg.admin.controller.FinanceController");
                add("com.ygg.admin.controller.GameActivityController");
                add("com.ygg.admin.controller.GateActivityController");
                add("com.ygg.admin.controller.GegeImageController");
                add("com.ygg.admin.controller.GroupBuyController");
                add("com.ygg.admin.controller.IndexSettingController");
                add("com.ygg.admin.controller.LotteryActivityController");
                add("com.ygg.admin.controller.MallWindowController");
                add("com.ygg.admin.controller.MenuController");
                add("com.ygg.admin.controller.NotSendMsgProductController");
                add("com.ygg.admin.controller.OrderController");
                add("com.ygg.admin.controller.OrderManualController");
                add("com.ygg.admin.controller.OrderQuestionController");
                add("com.ygg.admin.controller.OverseasDelayRemindDateController");
                add("com.ygg.admin.controller.OverseasOrderController");
                add("com.ygg.admin.controller.PageCustomController");
                add("com.ygg.admin.controller.PartnerController");
                add("com.ygg.admin.controller.PostageController");
                add("com.ygg.admin.controller.ProductBaseController");
                add("com.ygg.admin.controller.ProductBlacklistController");
                add("com.ygg.admin.controller.ProductCommentController");
                add("com.ygg.admin.controller.ProductController");
                add("com.ygg.admin.controller.RefundController");
                add("com.ygg.admin.controller.SaleController");
                add("com.ygg.admin.controller.SaleFlagController");
                add("com.ygg.admin.controller.SaleTagController");
                add("com.ygg.admin.controller.SaleWindowController");
                add("com.ygg.admin.controller.SellerController");
                add("com.ygg.admin.controller.SearchController");
                add("com.ygg.admin.controller.SignInController");
                add("com.ygg.admin.controller.SpecialActivityController");
                add("com.ygg.admin.controller.SmsController");
                add("com.ygg.admin.controller.SpecialActivityController");
                add("com.ygg.admin.controller.SpreadChannelController");
                add("com.ygg.admin.controller.SystemConfigController");
                add("com.ygg.admin.controller.SystemLogController");
                add("com.ygg.admin.controller.UserController");
                add("com.ygg.admin.controller.PictureController");
            }
        };
        
        for (String className : classNameList)
        {
            String controllerName = className.substring(className.lastIndexOf(".") + 1);
            Class clazz = Class.forName(className);
            if (clazz != null)
            {
                logger.info("**********扫描" + controllerName + "的方法开始**********");
                Method[] methods = clazz.getDeclaredMethods();
                List<Map<String, String>> permissions = new ArrayList<>();
                for (Method method : methods)
                {
                    if (method.isAnnotationPresent(RequestMapping.class))
                    {
                        RequestMapping ann = method.getAnnotation(RequestMapping.class);
                        String[] values = ann.value();
                        String mapping = Arrays.toString(values).replaceAll("\\[", "").replaceAll("\\]", "").replaceFirst("/", "");
                        logger.info("mapping:" + values[0] + " <----> method:" + method.getName());
                        Map<String, String> cuPer = new HashMap<>();
                        cuPer.put("category", controllerName);
                        cuPer.put("permission", controllerName + "_" + mapping.split("/")[0]);
                        cuPer.put("description", mapping.split("/")[0]);
                        permissions.add(cuPer);
                    }
                    
                }
                logger.info("**********扫描" + controllerName + "的方法结束**********");
                /*if (permissions.size() > 0)
                {
                    count += adminDao.updatePermissionDesc(permissions);
                }*/
            }
        }
        return count;
    }
    
    @Override
    public List<TempProductEntity> importBaseProductStock(MultipartFile file)
        throws Exception
    {
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        
        int startNum = sheet.getFirstRowNum();
        int lastNum = sheet.getLastRowNum();
        if (startNum == lastNum)
        {
            if (workbook != null)
            {
                workbook.close();
            }
            return null;
        }
        
        List<TempProductEntity> tempProductList = new ArrayList<TempProductEntity>();
        
        for (int i = startNum + 1; i <= lastNum; i++)
        {
            Row row = sheet.getRow(i);
            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);
            
            if (cell0 != null)
            {
                cell0.setCellType(Cell.CELL_TYPE_STRING);
            }
            if (cell1 != null)
            {
                cell1.setCellType(Cell.CELL_TYPE_STRING);
            }
            
            String productBaseId = cell0.getStringCellValue();
            String stock = cell1.getStringCellValue() == null ? "0" : cell1.getStringCellValue();
            //            System.out.println("第" + i + "行为:" + productBaseId + "," + stock);
            if (StringUtils.isNotEmpty(productBaseId) && StringUtils.isNotEmpty(stock))
            {
                TempProductEntity temp = new TempProductEntity();
                temp.setProductBaseId(productBaseId);
                temp.setReturnStock(Integer.parseInt(stock));
                
                List<ProductEntity> productList = temporaryDao.findProductInfoBybid(productBaseId);
                
                if (productList != null && productList.size() > 0)
                {
                    for (ProductEntity pe : productList)
                    {
                        if (pe.getType() == 1)
                        {
                            temp.getSaleProductList().add(pe.getId() + "");
                        }
                        else
                        {
                            temp.getMallProductList().add(pe.getId() + "");
                        }
                    }
                }
                
                tempProductList.add(temp);
            }
        }
        if (workbook != null)
        {
            workbook.close();
        }
        return tempProductList;
    }
    
    public void returnStock(TempProductEntity temp)
        throws Exception
    {
        String baseId = temp.getProductBaseId();
        int returnStock = temp.getReturnStock();
        List<String> saleProductIdList = temp.getSaleProductList();
        List<String> mallProductIdList = temp.getMallProductList();
        if (mallProductIdList != null && mallProductIdList.size() > 0)
        {
            Collections.sort(mallProductIdList, new Comparator<String>()
            {
                @Override
                public int compare(String o1, String o2)
                {
                    return Integer.parseInt(o2) - Integer.parseInt(o1);
                }
            });
            
            ProductCountEntity pce = temporaryDao.findProductCountByIdForUpdate(mallProductIdList.get(0));
            int adjust = pce.getStock();
            if (adjust >= returnStock)
            {
                adjust = returnStock;
            }
            returnStock -= adjust;
            pce.setStock(pce.getStock() - adjust);
            temporaryDao.updateProductCount(pce);
            logger.info("还原成功：商品Id=" + pce.getProductId() + "的库存还原为" + pce.getStock());
        }
        
        if (returnStock > 0)
        {
            if (saleProductIdList != null && saleProductIdList.size() > 0)
            {
                Collections.sort(saleProductIdList, new Comparator<String>()
                {
                    @Override
                    public int compare(String o1, String o2)
                    {
                        return Integer.parseInt(o1) - Integer.parseInt(o2);
                    }
                });
                
                List<ProductCountEntity> pceList = temporaryDao.findProductCountListByIdForUpdate(saleProductIdList);
                if (pceList != null)
                {
                    for (ProductCountEntity pce : pceList)
                    {
                        if (returnStock > 0)
                        {
                            int adjust = pce.getStock();
                            if (adjust >= returnStock)
                            {
                                adjust = returnStock;
                            }
                            pce.setStock(pce.getStock() - adjust);
                            returnStock -= adjust;
                        }
                    }
                    
                    for (ProductCountEntity pce : pceList)
                    {
                        temporaryDao.updateProductCount(pce);
                        logger.info("还原成功：商品Id=" + pce.getProductId() + "的库存还原为" + pce.getStock());
                    }
                }
            }
        }
        
        if (returnStock > 0)
        {
            logger.info("超卖：基本商品ID=" + baseId + "卖超" + returnStock);
        }
    }
    
    @Override
    public String insertSellerDeliverTemplate()
        throws Exception
    {
        int success = 0;
        List<ProductBaseEntity> productBaseList = temporaryDao.findAllProductBase(null);
        for (ProductBaseEntity pbe : productBaseList)
        {
            logger.info(String.format("正在同步基本商品Id=%d的配送地区模版", pbe.getId()));
            List<RelationDeliverAreaTemplateEntity> templateAreaList = new ArrayList<RelationDeliverAreaTemplateEntity>();
            List<RelationProductBaseDeliverAreaEntity> areaList = productBaseDao.findRelationSellerDeliverAreaByProductBaseId(pbe.getId());
            if (areaList != null && areaList.size() > 0)
            {
                for (RelationProductBaseDeliverAreaEntity area : areaList)
                {
                    if (!"1".equals(area.getProvinceCode()))
                    {
                        RelationDeliverAreaTemplateEntity tArea = new RelationDeliverAreaTemplateEntity();
                        tArea.setProvinceCode(area.getProvinceCode());
                        tArea.setCityCode(area.getCityCode());
                        tArea.setDistrictCode(area.getDistrictCode());
                        templateAreaList.add(tArea);
                    }
                }
            }
            if (!templateAreaList.isEmpty())
            {
                
                SellerDeliverAreaTemplateEntity template = new SellerDeliverAreaTemplateEntity();
                SellerEntity seller = sellerDao.findSellerById(pbe.getSellerId());
                if (seller != null)
                {
                    int count = temporaryDao.countSellerDeliverAreaTemplate(seller.getId());
                    template.setSellerId(seller.getId());
                    template.setName(seller.getRealSellerName() + "配送地区模版" + count);
                    template.setDesc(pbe.getDeliverAreaDesc());
                    template.setType((byte)pbe.getDeliverAreaType());
                    if (sellerDeliverAreaDao.insertSellerDeliverAreaTemplate(template) > 0)
                    {
                        BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure("sellerDeliverAreaTemplateId", template.getId());
                        CollectionUtils.forAllDo(templateAreaList, closure);
                        
                        pbe.setSellerDeliverAreaTemplateId(template.getId());
                        temporaryDao.updateBaseProductDeliverAreaTemplateId(pbe);
                        
                        relationDeliverAreaTemplateDao.insertRelationDeliverAreaTemplate(templateAreaList);
                        
                        success++;
                    }
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", String.format("成功同步%d条基本商品", success));
        return JSON.toJSONString(result);
    }
    
    @Override
    public String updateProductBaseProposalSalesPrice()
        throws Exception
    {
        int count = temporaryDao.updateProductBaseProposalSalesPrice();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "更新" + count + "条");
        return JSON.toJSONString(result);
    }
    
    @Override
    public String updateDeliverTemplate()
        throws Exception
    {
        int count = 0;
        int page = 1;
        int rows = 1000;
        while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            List<ProductBaseEntity> productBaseList = temporaryDao.findAllProductBase(para);
            if (productBaseList == null || productBaseList.isEmpty())
            {
                break;
            }
            else
            {
                for (ProductBaseEntity pbe : productBaseList)
                {
                    if (pbe.getSellerDeliverAreaTemplateId() > 0)
                    {
                        List<Integer> productIdList = new ArrayList<Integer>();
                        productIdList.add(pbe.getId());
                        productBaseDao.deleteRelationProductBaseDeliverArea(productIdList);
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("sellerDeliverAreaTemplateId", pbe.getSellerDeliverAreaTemplateId());
                        
                        mp.put("isExcept", 0);
                        List<RelationDeliverAreaTemplateEntity> areaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(mp);
                        
                        mp.put("isExcept", 1);
                        List<RelationDeliverAreaTemplateEntity> exceptAreaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(mp);
                        List<RelationDeliverAreaTemplateEntity> allAreas = filterExceptArea(areaList, exceptAreaList);
                        List<RelationProductBaseDeliverAreaEntity> rsdaeList = new ArrayList<RelationProductBaseDeliverAreaEntity>();
                        
                        copyProperties(allAreas, rsdaeList, pbe.getId());
                        if (!rsdaeList.isEmpty())
                        {
                            productBaseDao.insertRelationProductBaseDeliverArea(rsdaeList);
                        }
                        count++;
                        logger.info("基本商品Id=" + pbe.getId() + "配送地区模版更新完毕");
                    }
                    else
                    {
                        logger.info("基本商品Id=" + pbe.getId() + "不存在，跳过。。。");
                    }
                }
            }
            page++;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", "更新" + count + "条");
        return JSON.toJSONString(resultMap);
    }
    
    private boolean isEqualList(List<RelationProductBaseDeliverAreaEntity> baseAreaList, List<RelationDeliverAreaTemplateEntity> templateAreaList)
    {
        if (baseAreaList.size() != templateAreaList.size())
        {
            return false;
        }
        else
        {
            Collections.sort(baseAreaList, new Comparator<RelationProductBaseDeliverAreaEntity>()
            {
                
                @Override
                public int compare(RelationProductBaseDeliverAreaEntity o1, RelationProductBaseDeliverAreaEntity o2)
                {
                    if (o1.getProvinceCode().equals(o2.getProvinceCode()))
                    {
                        if (o1.getCityCode().equals(o2.getCityCode()))
                        {
                            return Integer.parseInt(o1.getDistrictCode()) - Integer.parseInt(o2.getDistrictCode());
                        }
                        else
                        {
                            return Integer.parseInt(o1.getCityCode()) - Integer.parseInt(o2.getCityCode());
                        }
                    }
                    else
                    {
                        return Integer.parseInt(o1.getProvinceCode()) - Integer.parseInt(o2.getProvinceCode());
                    }
                }
                
            });
            
            Collections.sort(templateAreaList, new Comparator<RelationDeliverAreaTemplateEntity>()
            {
                
                @Override
                public int compare(RelationDeliverAreaTemplateEntity o1, RelationDeliverAreaTemplateEntity o2)
                {
                    if (o1.getProvinceCode().equals(o2.getProvinceCode()))
                    {
                        if (o1.getCityCode().equals(o2.getCityCode()))
                        {
                            return Integer.parseInt(o1.getDistrictCode()) - Integer.parseInt(o2.getDistrictCode());
                        }
                        else
                        {
                            return Integer.parseInt(o1.getCityCode()) - Integer.parseInt(o2.getCityCode());
                        }
                    }
                    else
                    {
                        return Integer.parseInt(o1.getProvinceCode()) - Integer.parseInt(o2.getProvinceCode());
                    }
                }
                
            });
            
            for (int i = 0; i < baseAreaList.size(); i++)
            {
                RelationProductBaseDeliverAreaEntity baseArea = baseAreaList.get(i);
                RelationDeliverAreaTemplateEntity templateArea = templateAreaList.get(i);
                if (!StringUtils.equals(baseArea.toString(), templateArea.toString()))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String filterRepeatArea()
        throws Exception
    {
        List<Integer> sellerIdList = temporaryDao.findAllDistinctSellerId();
        Map<String, String> result = new HashMap<String, String>();
        for (Integer sellerId : sellerIdList)
        {
            int count = 0;
            List<SellerDeliverAreaTemplateEntity> templateList = sellerDeliverAreaDao.findSellerDeliverAreaTemplateBysid(sellerId);
            Collections.sort(templateList, new Comparator<SellerDeliverAreaTemplateEntity>()
            {
                @Override
                public int compare(SellerDeliverAreaTemplateEntity o1, SellerDeliverAreaTemplateEntity o2)
                {
                    return o1.getId() - o2.getId();
                }
            });
            
            if (templateList.isEmpty() || templateList.size() == 1)
            {
                continue;
            }
            for (int i = 0; i < templateList.size(); i++)
            {
                SellerDeliverAreaTemplateEntity first = templateList.get(i);
                List<RelationDeliverAreaTemplateEntity> firstAreaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByTemplateId(first.getId());
                for (int j = i + 1; j < templateList.size(); j++)
                {
                    SellerDeliverAreaTemplateEntity second = templateList.get(j);
                    List<RelationDeliverAreaTemplateEntity> secondAreaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByTemplateId(second.getId());
                    if (isEqualCollection(firstAreaList, secondAreaList))
                    {
                        if (sellerDeliverAreaDao.deleteSellerDeliverAreaTemplate(second.getId()) > 0)
                        {
                            List<ProductBaseEntity> productBaseList = temporaryDao.findProductBaseBySidAndTid(sellerId, second.getId());
                            if (productBaseList != null && productBaseList.size() > 0)
                            {
                                for (ProductBaseEntity base : productBaseList)
                                {
                                    base.setSellerDeliverAreaTemplateId(first.getId());
                                    temporaryDao.updateBaseProductDeliverAreaTemplateId(base);
                                }
                            }
                            count++;
                            relationDeliverAreaTemplateDao.deleteRelationDeliverAreaTemplateByTemplateId(second.getId());
                        }
                    }
                    else
                    {
                        continue;
                    }
                }
            }
            result.put(String.format("商家Id=%-5d", sellerId), String.format("成功删除%-3d条", count));
            logger.info(String.format("商家Id=%-5d成功去掉%-3d条重复模版", sellerId, count));
        }
        return JSON.toJSONString(result);
    }
    
    private boolean isEqualCollection(List<RelationDeliverAreaTemplateEntity> a, List<RelationDeliverAreaTemplateEntity> b)
    {
        if (a.size() != b.size())
        {
            return false;
        }
        
        Collections.sort(a, new Comparator<RelationDeliverAreaTemplateEntity>()
        {
            @Override
            public int compare(RelationDeliverAreaTemplateEntity o1, RelationDeliverAreaTemplateEntity o2)
            {
                return o1.getId() - o2.getId();
            }
        });
        
        Collections.sort(b, new Comparator<RelationDeliverAreaTemplateEntity>()
        {
            @Override
            public int compare(RelationDeliverAreaTemplateEntity o1, RelationDeliverAreaTemplateEntity o2)
            {
                return o1.getId() - o2.getId();
            }
        });
        
        for (int i = 0; i < a.size(); i++)
        {
            if (!a.get(i).equals(b.get(i)))
            {
                return false;
            }
        }
        return true;
    }
    
    private List<RelationDeliverAreaTemplateEntity> filterExceptArea(List<RelationDeliverAreaTemplateEntity> exceptAreaList)
        throws Exception
    {
        Set<String> provinceSet = new HashSet<String>();
        Set<String> citySet = new HashSet<String>();
        Set<String> districtSet = new HashSet<String>();
        
        Iterator<RelationDeliverAreaTemplateEntity> it = exceptAreaList.iterator();
        while (it.hasNext())
        {
            RelationDeliverAreaTemplateEntity area = it.next();
            if (StringUtils.equals(area.getDistrictCode(), "1"))
            {
                citySet.add(area.getCityCode());
                provinceSet.add(area.getProvinceCode());
                it.remove();
            }
        }
        
        List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
        if (!provinceSet.isEmpty() && !citySet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            areaList.addAll(areaDao.findCityCodeByPara(para));
        }
        
        provinceSet.clear();
        citySet.clear();
        
        for (RelationDeliverAreaTemplateEntity area : exceptAreaList)
        {
            provinceSet.add(area.getProvinceCode());
            citySet.add(area.getCityCode());
            districtSet.add(area.getDistrictCode());
        }
        if (!provinceSet.isEmpty() && !citySet.isEmpty() && !districtSet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            if (districtSet.size() > 0)
            {
                para.put("districtIdList", new ArrayList<String>(districtSet));
            }
            areaList.addAll(areaDao.findDistrictCodeByPara(para));
        }
        
        List<RelationDeliverAreaTemplateEntity> result = new ArrayList<RelationDeliverAreaTemplateEntity>();
        for (Map<String, Object> map : areaList)
        {
            RelationDeliverAreaTemplateEntity area = new RelationDeliverAreaTemplateEntity();
            area.setProvinceCode(map.get("provinceId") == null ? "1" : map.get("provinceId").toString());
            area.setCityCode(map.get("cityId") == null ? "1" : map.get("cityId").toString());
            area.setDistrictCode(map.get("districtId") == null ? "1" : map.get("districtId").toString());
            result.add(area);
        }
        return result;
    }
    
    @Override
    public String deleteProductBaseDeliverTemplate()
        throws Exception
    {
        int count = 0;
        List<ProductBaseEntity> productBaseList = temporaryDao.findAllProductBase(null);
        for (ProductBaseEntity pbe : productBaseList)
        {
            if (pbe.getSellerId() == 75 || pbe.getSellerId() == 251)
            {
                pbe.setSellerDeliverAreaTemplateId(0);
                pbe.setDeliverAreaType(1);
                pbe.setDeliverAreaDesc("");
                if (temporaryDao.updateBaseProductDeliverAreaType(pbe) > 0)
                {
                    count++;
                    List<Integer> idList = new ArrayList<Integer>();
                    idList.add(pbe.getId());
                    productBaseDao.deleteRelationProductBaseDeliverArea(idList);
                    logger.info(String.format("成功删除基本商品Id=%-5d的配送地区模版", pbe.getId()));
                }
            }
        }
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("msg", "成功删除" + count + "条");
        return JSON.toJSONString(mp);
    }
    
    @Override
    public Map<String, String> findHBOrderNumber()
        throws Exception
    {
        Set<String> fatherSet = new HashSet<String>();
        List<String> sonNumberList = temporaryDao.findAllSonNumber("2015-12-01 00:00:00");
        Map<String, String> hbMap = new HashMap<String, String>();
        for (String sonNumber : sonNumberList)
        {
            List<Map<String, Object>> fatherNumberList = temporaryDao.findAllHBOrderNumber(sonNumber);
            for (Map<String, Object> it : fatherNumberList)
            {
                String son = it.get("son_number") + "";
                String father = it.get("hb_number") + "";
                hbMap.put(son, father);
                fatherSet.add(father);
            }
        }
        Set<String> firstSet = new HashSet<String>(hbMap.values());
        //fatherSet.removeAll(firstSet);
        return hbMap;
    }
    
    @Override
    public String saveAccountLevelInfo()
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        //        while (true)
        //        {
        List<Map<String, Object>> orderList = temporaryDao.findSuccessOrderInfo();
        System.out.println("orderList:" + JSON.toJSONString(orderList));
        //            if (orderList.size() <= 0)
        //            {
        //                break;
        //            }
        for (Map<String, Object> map : orderList)
        {
            int accountId = Integer.valueOf(map.get("account_id") + "");
            int orderId = Integer.valueOf(map.get("id") + "");
            OrderEntity oe = orderDao.findOrderById(orderId);
            // 计算会员等级信息
            if (oe.getRealPrice() > 0)
            {
                AccountEntity ae = accountDao.findAccountById(accountId);
                // 计算等级并更新用户总交易成功金额 插入成功交易金额记录
                float totalSuccessPriceAndNowOrderPrice = ae.getTotalSuccessPrice() + oe.getRealPrice();
                int level = 0;
                if (totalSuccessPriceAndNowOrderPrice >= AccountEnum.LEVEL.V3.getLimitMoney())
                {
                    level = AccountEnum.LEVEL.V3.getCode();
                }
                else if (totalSuccessPriceAndNowOrderPrice >= AccountEnum.LEVEL.V2.getLimitMoney())
                {
                    level = AccountEnum.LEVEL.V2.getCode();
                }
                else if (totalSuccessPriceAndNowOrderPrice >= AccountEnum.LEVEL.V1.getLimitMoney())
                {
                    level = AccountEnum.LEVEL.V1.getCode();
                }
                else
                {
                    level = AccountEnum.LEVEL.V0.getCode();
                }
                
                Map<String, Object> para = new HashMap<>();
                para.put("id", ae.getId());
                para.put("level", level);
                para.put("totalSuccessPrice", totalSuccessPriceAndNowOrderPrice);
                if (temporaryDao.updateAccountInfoById(para) == 0)
                {
                    logger.error("updateAccountInfoById失败");
                    ServiceException se = new ServiceException("更新用户会员等级信息异常");
                    throw se;
                }
                
                AccountSuccessOrderRecordEntity record = new AccountSuccessOrderRecordEntity();
                record.setAccountId(ae.getId());
                record.setOperateType(1); // 用户确认
                record.setOrderId(oe.getId());
                record.setRealPrice(oe.getRealPrice());
                record.setTotalRealPrice(totalSuccessPriceAndNowOrderPrice);
                if (temporaryDao.addAccountSuccessOrderRecord(record) == 0)
                {
                    logger.error("addAccountSuccessOrderRecord失败");
                    ServiceException se = new ServiceException("交易成功订单记录插入异常");
                    throw se;
                }
            }
            
            //            }
        }
        return JSON.toJSONString(result);
    }
    
    @Override
    public int updateSellerEdbInfo()
        throws Exception
    {
        return temporaryDao.updateSellerEdbInfo();
    }
    
    @Override
    public String updateProductBaseGroupCount()
        throws Exception
    {
        List<ProductBaseEntity> list = productBaseDao.queryAllProductBase(new HashMap<String, Object>());
        int result = 0;
        for (ProductBaseEntity pbe : list)
        {
            if (pbe.getCode().indexOf("%") > -1)
            {
                logger.info("商品ID=" + pbe.getId() + "--------编码code=" + pbe.getCode());
                Map<String, Object> para = new HashMap<String, Object>();
                String suffix = pbe.getCode().substring(pbe.getCode().lastIndexOf("%") + 1);
                if (suffix.matches("^\\d+$"))
                {
                    int groupCount = Integer.parseInt(suffix);
                    para.put("groupCount", groupCount);
                    para.put("id", pbe.getId());
                    result += temporaryDao.updateProductBase(para);
                    logger.info(String.format("将基本商品%-5d的组合件数更新为%-5d", pbe.getId(), groupCount));
                }
            }
        }
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    @Override
    public String updateProductBaseTotalSales()
        throws Exception
    {
        int result = 0;
        List<ProductBaseEntity> list = productBaseDao.queryAllProductBase(new HashMap<String, Object>());
        for (ProductBaseEntity pbe : list)
        {
            int totalSales = temporaryDao.countTotalSalesByProductBaseId(pbe.getId());
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", pbe.getId());
            para.put("totalSales", totalSales);
            result += temporaryDao.updateProductBase(para);
            logger.info(String.format("将基本商品%-5d的累计销量更新为%-5d", pbe.getId(), totalSales));
        }
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    @Override
    public String updateProductBaseProviderProductId()
        throws Exception
    {
        int result = 0;
        List<Map<String, Object>> reList = temporaryDao.findAllSelfProductBase();
        for (Map<String, Object> it : reList)
        {
            int id = Integer.parseInt(it.get("id").toString());
            String code = it.get("code").toString();
            if (code.indexOf("%") > -1)
            {
                code = code.substring(0, code.lastIndexOf("%"));
                Map<String, Object> reMap = purchaseDao.findProviderProductByBarCode(code);
                if (reMap != null && !reMap.isEmpty())
                {
                    int providerProductId = Integer.parseInt(reMap.get("id") == null ? "0" : reMap.get("id").toString());
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("id", id);
                    para.put("providerProductId", providerProductId);
                    result += temporaryDao.updateProductBaseProviderProductId(para);
                    logger.info(String.format("将基本商品%-5d的采购商品ID更新为%-5d", id, providerProductId));
                }
            }
        }
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    /** 财务打款账户选择 */
    private int getTargetFinancialAffairsCardId(int appChannel, String appVersion, int payChannel, int orderType, float realPrice)
    {
        int targetFinancialAffairsCardId = 4; // 默认打款账户
        double version = 0;
        if (appVersion != null && !"".equals(appVersion))
        {
            version = Double.valueOf(appVersion);
        }
        
        if (payChannel == OrderEnum.PAY_CHANNEL.UNION.getCode())
        {
            targetFinancialAffairsCardId = 8;
        }
        else if (payChannel == OrderEnum.PAY_CHANNEL.ALIPAY.getCode())
        {
            targetFinancialAffairsCardId = 5; // 萧剑
            if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
            {
                if (version == 0)
                {
                    // 移动网站
                    if (realPrice <= 0.1f)
                    {
                        targetFinancialAffairsCardId = 5;
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 14; // 左岸城堡国际支付宝
                    }
                }
                else if (version >= 1.8)
                {
                    if (realPrice <= 0.1f)
                    {
                        targetFinancialAffairsCardId = 13; // 左岸城堡国内支付宝
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 14; // 左岸城堡国际支付宝
                    }
                }
            }
            else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode() || orderType == OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode())
            {
                targetFinancialAffairsCardId = 13; // 左岸城堡国内支付宝
            }
            
        }
        else if (payChannel == OrderEnum.PAY_CHANNEL.WEIXIN.getCode())
        {
            if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
            {
                if (version == 0)
                {
                    if (appChannel == 28) // 左岸城堡 移动网站
                    {
                        targetFinancialAffairsCardId = 18;
                    }
                    else if (appChannel == 29) // 燕网 移动网站
                    {
                        targetFinancialAffairsCardId = 19;
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 6;
                    }
                }
                else
                {
                    if (version >= 2.0)
                    {
                        if (appChannel == 19 || appChannel == 25)
                        {
                            targetFinancialAffairsCardId = 11;
                        }
                        else if (appChannel == 20)
                        {
                            targetFinancialAffairsCardId = 12;
                        }
                        else
                        {
                            targetFinancialAffairsCardId = 9;
                        }
                    }
                    else if (version >= 1.7)
                    {
                        if (appChannel == 19)
                        {
                            targetFinancialAffairsCardId = 11;
                        }
                        else if (appChannel == 20)
                        {
                            targetFinancialAffairsCardId = 12;
                        }
                        else
                        {
                            targetFinancialAffairsCardId = 9;
                        }
                    }
                    else if (version >= 1.6)
                    {
                        targetFinancialAffairsCardId = 9;
                    }
                    else
                    {
                        targetFinancialAffairsCardId = 7;
                    }
                }
            }
            else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode() || orderType == OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode())
            {
                if (appChannel == 22)
                {
                    targetFinancialAffairsCardId = 15;
                }
                else
                {
                    targetFinancialAffairsCardId = 16;
                }
            }
            else if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
            {
                targetFinancialAffairsCardId = 18;
            }
            else if (orderType == OrderEnum.ORDER_TYPE.YANWANG.getCode())
            {
                targetFinancialAffairsCardId = 19;
            }
        }
        return targetFinancialAffairsCardId;
    }
    
    @Override
    public int updateOrderProductInfo()
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("statusList", Arrays.asList(1, 2, 3));
        List<RefundEntity> refundEntityList = refundDao.findAllRefundByPara(para);
        int status = 0;
        for (RefundEntity r : refundEntityList)
        {
            OrderEntity oe = orderDao.findOrderById(r.getOrderId());
            int targetFinancialAffairsCardId =
                getTargetFinancialAffairsCardId(Integer.valueOf(oe.getAppChannel()), oe.getAppVersion(), oe.getPayChannel(), oe.getType(), oe.getRealPrice());
            
            if (temporaryDao.updateRefundFinancialAffairsCardId(r.getId(), targetFinancialAffairsCardId) == 1)
            {
                status++;
            }
            else
            {
                logger.warn("更新失败!refundId: " + r.getId());
            }
        }
        return status;
    }
    
    @Override
    public String updateOrderExpireTime()
        throws Exception
    {
        int result = 0;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("startTimeBegin", "2016-02-18 00:00:00");
        
        List<Integer> orderIdList = orderDao.findAllOrderIdList(param);
        for (int orderId : orderIdList)
        {
            OrderEntity order = orderDao.findOrderById(orderId);
            if (order.getType() == 2)
            {
                continue;
            }
            
            Integer id = temporaryDao.findSendTimeoutComplain(orderId);
            if (id != null)
            {
                continue;
            }
            if (order.getStatus() == 2 || order.getStatus() == 3 || order.getStatus() == 4 || order.getStatus() == 5)
            {
                if (StringUtils.isNotEmpty(order.getPayTime()))
                {
                    
                    DateTime beginTime = new DateTime(DateTimeUtil.string2Date(order.getPayTime()).getTime());
                    DateTime endTime = null;
                    SellerEntity seller = sellerDao.findSellerById(order.getSellerId());
                    if (seller != null)
                    {
                        // 当天15点前订单当天打包并提供物流单号，24小时内有物流信息，超时时间为当天24点
                        if (seller.getSendTimeType() == SellerEnum.SellerSendTimeTypeEnum.IN_15_HOUR.getCode())
                        {
                            int plusDay = 0;
                            int dayOfHour = beginTime.getHourOfDay();
                            int dayOfWeek = beginTime.getDayOfWeek();
                            
                            if (seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.SEND_ON_SUNDAY.getCode())
                            {
                                // 周六不发货
                                if (dayOfWeek == 5)
                                {
                                    if (dayOfHour >= 15)
                                    {
                                        plusDay += 2;
                                    }
                                }
                                else if (dayOfWeek == 6)
                                {
                                    plusDay += 1;
                                }
                                else
                                {
                                    if (dayOfHour >= 15)
                                    {
                                        plusDay += 1;
                                    }
                                }
                            }
                            else if (seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.SEND_ON_SATURDAY.getCode())
                            {
                                // 周天不发货
                                if (dayOfWeek == 6)
                                {
                                    if (dayOfHour >= 15)
                                    {
                                        plusDay += 2;
                                    }
                                }
                                else if (dayOfWeek == 7)
                                {
                                    plusDay += 1;
                                }
                                else
                                {
                                    if (dayOfHour >= 15)
                                    {
                                        plusDay += 1;
                                    }
                                }
                            }
                            else if (seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.NOT_SEND_ON_WEEKEND.getCode())
                            {
                                // 周末不发货
                                if (dayOfWeek == 5)
                                {
                                    if (dayOfHour >= 15)
                                    {
                                        plusDay += 3;
                                    }
                                }
                                else if (dayOfWeek == 6)
                                {
                                    plusDay += 2;
                                }
                                else if (dayOfWeek == 7)
                                {
                                    plusDay += 1;
                                }
                                else
                                {
                                    if (dayOfHour >= 15)
                                    {
                                        plusDay += 1;
                                    }
                                }
                            }
                            else
                            {
                                if (dayOfHour >= 15)
                                {
                                    plusDay += 1;
                                }
                            }
                            endTime = beginTime.withTimeAtStartOfDay().plusDays(plusDay).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
                        }
                        else
                        {
                            int plusHour = 0;
                            if (seller.getSendTimeType() == SellerEnum.SellerSendTimeTypeEnum.IN_24_HOUR.getCode())
                            {
                                plusHour = 24;
                            }
                            else if (seller.getSendTimeType() == SellerEnum.SellerSendTimeTypeEnum.IN_48_HOUR.getCode())
                            {
                                plusHour = 48;
                            }
                            else if (seller.getSendTimeType() == SellerEnum.SellerSendTimeTypeEnum.IN_72_HOUR.getCode())
                            {
                                plusHour = 72;
                            }
                            endTime = beginTime.plusHours(plusHour);
                            while (!beginTime.isAfter(endTime))
                            {
                                if (beginTime.getDayOfWeek() == 6)
                                {
                                    if (seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.SEND_ON_SUNDAY.getCode()
                                        || seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.NOT_SEND_ON_WEEKEND.getCode())
                                    {
                                        endTime = endTime.plusHours(24);
                                    }
                                }
                                if (beginTime.getDayOfWeek() == 7)
                                {
                                    if (seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.SEND_ON_SATURDAY.getCode()
                                        || seller.getIsSendWeekend() == SellerEnum.WeekendSendTypeEnum.NOT_SEND_ON_WEEKEND.getCode())
                                    {
                                        endTime = endTime.plusHours(24);
                                    }
                                }
                                beginTime = beginTime.plusDays(1);
                            }
                        }
                        
                        if (endTime != null)
                        {
                            int isTimeout = 0;
                            Map<String, Object> para = new HashMap<String, Object>();
                            para.put("id", orderId);
                            para.put("expireTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
                            if (StringUtils.isNotEmpty(order.getSendTime()))
                            {
                                DateTime sendTime = new DateTime(DateTimeUtil.string2Date(order.getSendTime()));
                                if (endTime.isBefore(sendTime))
                                {
                                    isTimeout = 1;
                                }
                                else
                                {
                                    isTimeout = 0;
                                }
                            }
                            else
                            {
                                //查看是否有退款退货的记录，取退款退货的创建时间
                                String refundTime = temporaryDao.findOrderRefundTimeByOrderId(order.getId());
                                if (StringUtils.isNotEmpty(refundTime))
                                {
                                    if (endTime.isBefore(CommonUtil.string2Date(refundTime, "yyyy-MM-dd HH:mm:ss").getTime()))
                                    {
                                        isTimeout = 1;
                                    }
                                    else
                                    {
                                        isTimeout = 0;
                                    }
                                }
                                else
                                {
                                    if (endTime.isBeforeNow())
                                    {
                                        isTimeout = 1;
                                    }
                                    else
                                    {
                                        isTimeout = 0;
                                    }
                                }
                            }
                            para.put("isTimeout", isTimeout);
                            result += orderDao.updateOrder(para);
                            logger.info(String.format("将订单ID=%d过期时间设为%s，是否超时：%s", orderId, endTime.toString("yyyy-MM-dd HH:mm:ss"), isTimeout == 1 ? "是" : "否"));
                        }
                    }
                }
            }
        }
        logger.info("成功更新：" + result + "条订单");
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    @Override
    public String updateAccountBirthDay()
        throws Exception
    {
        int result = 0;
        int page = 1;
        int rows = 1000;
        while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            List<AccountEntity> aeList = accountDao.findAllAccountByPara(para);
            if (aeList == null || aeList.isEmpty())
            {
                break;
            }
            for (AccountEntity ae : aeList)
            {
                String idCard = temporaryDao.findIdCardByAccountId(ae.getId());
                
                String birthDay = getBirthDay(idCard);
                if (birthDay != null)
                {
                    result += temporaryDao.updateAccountBirthDay(ae.getId(), birthDay);
                    logger.info(String.format("将用户Id=%-8d的生日更新为：%s", ae.getId(), birthDay));
                }
            }
            page++;
        }
        return "\"msg\":\"成功更新" + result + "条\"";
    }

//    @Override
//    public String updateAccountSecretKey()
//        throws Exception
//    {
//        int result = 0;
//        for (int i = 1 ; i <= 5; i++)
//        {
//            int page = 1;
//            int rows = 5000;
//            while (true)
//            {
//                Map<String, Object> para = new HashMap<>();
//                para.put("start", rows * (page - 1));
//                para.put("max", rows);
//                para.put("type", i);
//                List<AccountEntity> aeList = accountDao.findAllAccountByPara(para);
//                if (aeList == null || aeList.isEmpty())
//                {
//                    break;
//                }
//                for (AccountEntity ae : aeList)
//                {
//                    if (ae.getSecretKey() == null || "".equals(ae.getSecretKey()) || "yangegegegeyan".equals(ae.getSecretKey()))
//                    {
//                        ae.setSecretKey(CommonUtil.generateAccountSign(ae.getId()));
//                        System.out.println("setSecretKey:" + ae.getSecretKey());
//                        result += temporaryDao.updateAccountSecret(ae.getId(), ae.getSecretKey());
//                        logger.info(String.format("将用户Id=%-8d的秘钥更新为：%s", ae.getId(), ae.getSecretKey()));
//                    }
//                }
//                page++;
//            }
//        }
//        return result;
//    }

    @Override
    public int updateAccountSecretKey(Map<String, Object> para)
            throws Exception
    {
        int result = 0;
        List<AccountEntity> aeList = accountDao.findAllAccountByPara(para);
        if (aeList == null || aeList.isEmpty())
        {
            return 0;
        }
        for (AccountEntity ae : aeList)
        {
            if (ae.getSecretKey() == null || "".equals(ae.getSecretKey()) || "yangegegegeyan".equals(ae.getSecretKey()))
            {
                ae.setSecretKey(CommonUtil.generateAccountSign(ae.getId()));
                System.out.println("setSecretKey:" + ae.getSecretKey());
                result += temporaryDao.updateAccountSecret(ae.getId(), ae.getSecretKey());
                logger.info(String.format("将用户Id=%-8d的秘钥更新为：%s", ae.getId(), ae.getSecretKey()));
            }
        }
        return result;
    }

    @Override
    public String updateAccountPetname()
        throws Exception
    {
        int result = 0;
        int page = 1;
        int rows = 1000;
        while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            List<AccountEntity> aeList = accountDao.findAllAccountByPara(para);
            if (aeList == null || aeList.isEmpty())
            {
                break;
            }
            for (AccountEntity ae : aeList)
            {
                String petname = "";
                if (ae.getType() == AccountEnum.ACCOUNT_TYPE.MOBILE.getCode())
                {
                    petname = URLEncoder.encode(ae.getName(), "UTF-8");
                }
                else if (ae.getType() == AccountEnum.ACCOUNT_TYPE.QQ.getCode() || ae.getType() == AccountEnum.ACCOUNT_TYPE.WEIXIN.getCode()
                    || ae.getType() == AccountEnum.ACCOUNT_TYPE.SINA.getCode() || ae.getType() == AccountEnum.ACCOUNT_TYPE.ALIPAY.getCode())
                {
                    petname = URLEncoder.encode(ae.getNickname(), "UTF-8");
                }
                result += temporaryDao.updateAccountPetname(ae.getId(), petname);
                logger.info(String.format("将用户Id=%-8d的昵称更新为：%s", ae.getId(), petname));
            }
            page++;
        }
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    private String getBirthDay(String code)
    {
        if (StringUtils.isEmpty(code))
            return null;
        Map<String, Integer> dateMap = new HashMap<String, Integer>();
        dateMap.put("01", 31);
        dateMap.put("02", null);
        dateMap.put("03", 31);
        dateMap.put("04", 30);
        dateMap.put("05", 31);
        dateMap.put("06", 30);
        dateMap.put("07", 31);
        dateMap.put("08", 31);
        dateMap.put("09", 30);
        dateMap.put("10", 31);
        dateMap.put("11", 30);
        dateMap.put("12", 31);
        
        boolean validate = false;
        // 验证月份
        String month = code.substring(10, 12);
        String dayCode = code.substring(12, 14);
        String yearCode = code.substring(6, 10);
        if (dateMap.containsKey(month))
        {
            // 验证日期
            Integer day = dateMap.get(month);
            Integer year = Integer.valueOf(yearCode);
            
            // 非2月的情况
            if (day != null)
            {
                if (Integer.valueOf(dayCode) >= 1 && Integer.valueOf(dayCode) <= day)
                {
                    validate = true;
                }
            }
            // 2月的情况
            else
            {
                // 闰月的情况
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
                {
                    if (Integer.valueOf(dayCode) >= 1 && Integer.valueOf(dayCode) <= 29)
                    {
                        validate = true;
                    }
                }
                // 非闰月的情况
                else
                {
                    if (Integer.valueOf(dayCode) >= 1 && Integer.valueOf(dayCode) <= 28)
                    {
                        validate = true;
                    }
                }
            }
        }
        if (validate)
        {
            return yearCode + "-" + month + "-" + dayCode;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public List<Map<String, Object>> findOrderRefundExplain(String date)
        throws Exception
    {
        
        return temporaryDao.findOrderRefundExplain(date);
    }
    
    @Override
    public String updateOrderLogisticsTimeout()
        throws Exception
    {
        int result = 0;
        int page = 1;
        int rows = 1000;
        while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("isTimeout", 1);
            List<Map<String, Object>> timeoutOrders = temporaryDao.findOrderLogisticsTimeoutOrders(para);
            if (timeoutOrders == null || timeoutOrders.isEmpty())
            {
                break;
            }
            for (Map<String, Object> order : timeoutOrders)
            {
                Timestamp expireTime = (Timestamp)order.get("expireTime");
                Timestamp logisticsTime = order.get("logisticsTime") == null ? null : (Timestamp)order.get("logisticsTime");
                if (logisticsTime == null)
                {
                    if (new DateTime(expireTime.getTime()).isBeforeNow())
                    {
                        order.put("isTimeout", CommonConstant.COMMON_YES);
                    }
                    else
                    {
                        order.put("isTimeout", CommonConstant.COMMON_NO);
                    }
                }
                else
                {
                    if (new DateTime(expireTime.getTime()).isBefore(new DateTime(logisticsTime.getTime())))
                    {
                        order.put("isTimeout", CommonConstant.COMMON_YES);
                    }
                    else
                    {
                        order.put("isTimeout", CommonConstant.COMMON_NO);
                    }
                }
                result += temporaryDao.updateLogisticsTimeout(order);
                logger.info(order);
            }
            page++;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", "成功插入" + result + "条");
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateOrderProductComment()
        throws Exception
    {
        int result = 0;
        List<Map<String, Object>> comments = temporaryDao.findOrderProductComment();
        Map<String, Object> para = new HashMap<String, Object>();
        for (Map<String, Object> comment : comments)
        {
            int id = Integer.parseInt(comment.get("id") == null ? "0" : comment.get("id").toString());
            String content = comment.get("content") == null ? "" : comment.get("content").toString();
            para.put("id", id);
            para.put("content", URLEncoder.encode(HtmlUtils.htmlUnescape(URLDecoder.decode(content, "UTF-8")), "UTF-8"));
            result += temporaryDao.updateOrderProductComment(para);
            logger.info("****************************" + id + "**************************");
            logger.info("更新前：" + URLDecoder.decode(content, "UTF-8"));
            //logger.info("更新后：" + StringEscapeUtils.unescapeHtml(URLDecoder.decode(content, "UTF-8")));
            logger.info("更新后：" + HtmlUtils.htmlUnescape(URLDecoder.decode(content, "UTF-8")));
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("msg", "成功更新" + result + "条");
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateLogisticsTimeoutOrderType()
        throws Exception
    {
        int result = 0;
        int page = 1;
        int rows = 1000;
        /*while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("orderType", 1);
            List<Map<String, Object>> orders = temporaryDao.findLogisticsTimeout(para);
            if (orders == null || orders.isEmpty())
            {
                break;
            }
            for (Map<String, Object> order : orders)
            {
                if (Integer.parseInt(order.get("orderType").toString()) == 2)
                {
                    result += temporaryDao.updateLogisticsTimeout(order);
                    logger.info(order);
                }
            }
            page++;
        }*/
        logger.info("成功更新：" + result + "条");
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    @Override
    public String updateDeliverAreaAndExtraPostage()
        throws Exception
    {
        int result = 0;
        int page = 1;
        int rows = 50;
        Set<String> allProvinces = areaDao.findAllProvinceCode();
        while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            List<Map<String, Object>> templates = sellerDeliverAreaDao.findAllSellerDeliverAreaTemplate(para);
            if (templates == null || templates.isEmpty())
            {
                break;
            }
            
            for (Map<String, Object> template : templates)
            {
                int id = Integer.parseInt(template.get("id").toString());
                para.put("id", id);
                
                SellerDeliverAreaTemplateEntity areaTemplate = sellerDeliverAreaDao.findSellerDeliverAreaTemplateByPara(para);
                List<RelationDeliverAreaTemplateEntity> areas = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByTemplateId(id);
                
                logger.info(String.format("扫描模版：%s的配送地区开始", areaTemplate.getName()));
                Set<String> provinceCodes = new HashSet<>();
                if (areaTemplate.getType() == SellerEnum.DELIVER_AREA_SUPPORT_TYPE.SUPPORT.getCode())
                {
                    for (RelationDeliverAreaTemplateEntity area : areas)
                    {
                        if (area.getIsExcept() == CommonConstant.COMMON_NO)
                        {
                            provinceCodes.add(area.getProvinceCode());
                        }
                    }
                }
                else if (areaTemplate.getType() == SellerEnum.DELIVER_AREA_SUPPORT_TYPE.UNSUPPORT.getCode())
                {
                    
                    provinceCodes.addAll(allProvinces);
                    for (RelationDeliverAreaTemplateEntity area : areas)
                    {
                        if (area.getIsExcept() == CommonConstant.COMMON_NO)
                        {
                            provinceCodes.remove(area.getProvinceCode());
                        }
                        else
                        {
                            provinceCodes.add(area.getProvinceCode());
                        }
                    }
                }
                logger.info(String.format("扫描模版：%s的配送地区共%d条", areaTemplate.getName(), provinceCodes.size()));
                if (!provinceCodes.isEmpty())
                {
                    result += sellerDeliverAreaDao.insertRelationTemplateDeliverAreaAndExtraPostage(areaTemplate, provinceCodes);
                }
                logger.info(String.format("扫描模版：%s的配送地区结束", areaTemplate.getName()));
            }
            page++;
        }
        logger.info("成功更新：" + result + "条");
        return "\"msg\":\"成功更新" + result + "条\"";
    }
    
    private List<RelationDeliverAreaTemplateEntity> filterExceptArea(List<RelationDeliverAreaTemplateEntity> areas, List<RelationDeliverAreaTemplateEntity> exceptAreas)
        throws Exception
    {
        
        //去除非例外地区中包含的例外的省份
        Set<RelationDeliverAreaTemplateEntity> removeSet = new HashSet<>();
        for (RelationDeliverAreaTemplateEntity area : areas)
        {
            for (RelationDeliverAreaTemplateEntity except : exceptAreas)
            {
                if (area.getProvinceCode().equals(except.getProvinceCode()))
                {
                    removeSet.add(area);
                }
            }
        }
        areas.removeAll(removeSet);
        
        Set<String> provinceSet = new HashSet<String>();
        Set<String> citySet = new HashSet<String>();
        Set<String> districtSet = new HashSet<String>();
        
        //取出例外地区中精确到市的地区
        Set<String> allCity = new HashSet<>();
        Iterator<RelationDeliverAreaTemplateEntity> it = exceptAreas.iterator();
        while (it.hasNext())
        {
            RelationDeliverAreaTemplateEntity area = it.next();
            citySet.add(area.getCityCode());
            provinceSet.add(area.getProvinceCode());
            if (StringUtils.equals(area.getDistrictCode(), "1"))
            {
                allCity.add(area.getCityCode());
                it.remove();
            }
        }
        List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
        if (!provinceSet.isEmpty() && !citySet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            areaList.addAll(areaDao.findCityCodeByPara(para));
        }
        
        //取出例外地区中精确到区的地区
        provinceSet.clear();
        citySet.clear();
        for (RelationDeliverAreaTemplateEntity area : exceptAreas)
        {
            if (allCity.contains(area.getCityCode()))
            {
                continue;
            }
            provinceSet.add(area.getProvinceCode());
            citySet.add(area.getCityCode());
            districtSet.add(area.getDistrictCode());
        }
        if (!provinceSet.isEmpty() && !citySet.isEmpty() && !districtSet.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (provinceSet.size() > 0)
            {
                para.put("provinceIdList", new ArrayList<String>(provinceSet));
            }
            if (citySet.size() > 0)
            {
                para.put("cityIdList", new ArrayList<String>(citySet));
            }
            if (districtSet.size() > 0)
            {
                para.put("districtIdList", new ArrayList<String>(districtSet));
            }
            areaList.addAll(areaDao.findDistrictCodeByPara(para));
        }
        
        for (Map<String, Object> map : areaList)
        {
            RelationDeliverAreaTemplateEntity area = new RelationDeliverAreaTemplateEntity();
            area.setProvinceCode(map.get("provinceId") == null ? "1" : map.get("provinceId").toString());
            area.setCityCode(map.get("cityId") == null ? "1" : map.get("cityId").toString());
            area.setDistrictCode(map.get("districtId") == null ? "1" : map.get("districtId").toString());
            areas.add(area);
        }
        return areas;
    }
    
    private void copyProperties(List<RelationDeliverAreaTemplateEntity> orig, List<RelationProductBaseDeliverAreaEntity> dest, int id)
        throws Exception
    {
        if (orig == null || orig.isEmpty())
        {
            return;
        }
        for (RelationDeliverAreaTemplateEntity sellerArea : orig)
        {
            RelationProductBaseDeliverAreaEntity productArea = new RelationProductBaseDeliverAreaEntity();
            PropertyUtils.copyProperties(productArea, sellerArea);
            dest.add(productArea);
        }
        
        BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure("productBaseId", id);
        CollectionUtils.forAllDo(dest, closure);
    }
    
    @Override
    public String updateProductBaseStock()
        throws Exception
    {
        
        int count = 0;
        int page = 1;
        int rows = 1000;
        while (true)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            List<ProductBaseEntity> productBaseList = temporaryDao.findAllProductBase(para);
            if (productBaseList == null || productBaseList.isEmpty())
            {
                break;
            }
            else
            {
                for (ProductBaseEntity pbe : productBaseList)
                {
                    int saleStock = temporaryDao.findProductSumStockByPidAndType(pbe.getId(), ProductEnum.PRODUCT_TYPE.SALE.getCode());
                    int mallStock = temporaryDao.findProductSumStockByPidAndType(pbe.getId(), ProductEnum.PRODUCT_TYPE.MALL.getCode());
                    if (pbe.getSaleStock() >= saleStock && pbe.getMallStock() >= mallStock)
                    {
                        continue;
                    }
                    
                    ProductBaseEntity old = productBaseDao.findProductBaseByIdForUpdate(pbe.getId());
                    if (saleStock > pbe.getSaleStock())
                    {
                        int change = saleStock - pbe.getSaleStock();
                        pbe.setTotalStock(pbe.getTotalStock() + change);
                        pbe.setSaleStock(pbe.getSaleStock() + change);
                    }
                    if (mallStock > pbe.getMallStock())
                    {
                        int change = mallStock - pbe.getMallStock();
                        pbe.setTotalStock(pbe.getTotalStock() + change);
                        pbe.setMallStock(pbe.getMallStock() + change);
                    }
                    
                    Map<String, Object> params = new HashMap<>();
                    params.put("totalStock", pbe.getTotalStock());
                    params.put("saleStock", pbe.getSaleStock());
                    params.put("mallStock", pbe.getMallStock());
                    params.put("id", pbe.getId());
                    if (temporaryDao.updateProductBase(params) > 0)
                    {
                        logger.info(String.format("成功将基本商品ID=%-6d的总库存增加%-3d,特卖库存增加%-3d,商城库存增加%-3d", pbe.getId(), pbe.getTotalStock() - old.getTotalStock(), pbe.getSaleStock()
                            - old.getSaleStock(), pbe.getMallStock() - old.getMallStock()));
                        count++;
                    }
                }
            }
            page++;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", "更新" + count + "条");
        logger.info(resultMap);
        return JSON.toJSONString(resultMap);
        
    }

    @Override
    public List<Map<String, String>> findOrderTimes(int minDay,int maxDay)
        throws Exception
    {
        DateTime now = DateTime.now();
        List<Map<String, Map<String, String>>> groupList = new ArrayList<>();
        int page = 1;
        int rows = 2000;
        long a = System.currentTimeMillis();
        while (true)
        {
            Map<String, Object> para = new HashMap<>();
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("minDay",minDay);
            para.put("maxDay",maxDay);
            long ll = System.currentTimeMillis();
            List<Map<String, Object>> accountIdAndCreateTimes = temporaryDao.findMaxCreateTimeGroupByAccountId(para);
            logger.info(String.format("开始查询第： %-5d页数据，共查到：%-5d条，耗时：%s",
                page,
                accountIdAndCreateTimes.size(),
                (System.currentTimeMillis() - ll)) + "");
            if (accountIdAndCreateTimes == null || accountIdAndCreateTimes.isEmpty())
            {
                break;
            }
            else
            {
                List<Integer> accountIds = new ArrayList<>();
                for (Map<String, Object> it : accountIdAndCreateTimes)
                {
                    int accountId = Integer.parseInt(it.get("accountId").toString());
                    accountIds.add(accountId);
                }
                if (!accountIds.isEmpty())
                {
                    List<Map<String, Object>> accountIdAndTimes = temporaryDao.findAccountIdAndTimesByaids(accountIds);
                    Map<String, Map<String, String>> groupMap = new HashMap<>();
                    for (Map<String, Object> map : accountIdAndTimes)
                    {
                        String accountId = map.get("oaccountid").toString();
                        String type = map.get("type").toString();
                        String totalPrice = map.get("totalPrice").toString();
                        Map<String, String> acInfo = groupMap.get(accountId);
                        if (acInfo == null)
                        {
                            acInfo = new HashMap<>();
                            acInfo.put("count", "1");
                            acInfo.put("type", type);
                            acInfo.put("totalPrice", totalPrice);
                            groupMap.put(accountId, acInfo);
                        }
                        else
                        {
                            acInfo.put("count", (Integer.parseInt(acInfo.get("count")) + 1) + "");
                        }
                    }
                    groupList.add(groupMap);
                }
            }
            page++;
        }
        long b = System.currentTimeMillis();
        logger.info("数据查询完毕，耗时：" + (b - a) + "毫秒");
        
        logger.info("第一次分组开始。。。。");
        Map<String, Map<String, String>> groupMap = new HashMap<>();
        for (Map<String, Map<String, String>> mp : groupList)
        {
            for (String key : mp.keySet())
            {
                Map<String, String> temp = groupMap.get(key);
                if (temp == null)
                {
                    groupMap.put(key, mp.get(key));
                }
                else
                {
                    int count = Integer.parseInt(temp.get("count"));
                    int count_ = Integer.parseInt(mp.get(key).get("count"));
                    temp.put("count", (count + count_) + "");
                }
            }
        }
        long c = System.currentTimeMillis();
        logger.info("第一次分组结束，耗时：" + (c - b) + "毫秒");
        
        logger.info("第二次分组开始。。。。");
        Map<String, List<Map<String, String>>> timesGroup = new HashMap<>();
        timesGroup.put("1-3次/低", new ArrayList<Map<String, String>>());
        timesGroup.put("4-24次/中", new ArrayList<Map<String, String>>());
        timesGroup.put("24次以上/高", new ArrayList<Map<String, String>>());
        for (Map<String, String> mp : groupMap.values())
        {
            int count = Integer.parseInt(mp.get("count"));
            if (count <= 3)
            {
                timesGroup.get("1-3次/低").add(mp);
            }
            else if (count <= 24)
            {
                timesGroup.get("4-24次/中").add(mp);
            }
            else
            {
                timesGroup.get("24次以上/高").add(mp);
            }
        }
        long d = System.currentTimeMillis();
        logger.info("第二次分组结束，耗时：" + (d - c) + "毫秒");
        
        logger.info("第三次分组开始。。。。");
        Map<String, List<Map<String, String>>> moneyGroup = new HashMap<>();
        moneyGroup.put("1-3次/低:0-499低", new ArrayList<Map<String, String>>());
        moneyGroup.put("1-3次/低:500-1999中", new ArrayList<Map<String, String>>());
        moneyGroup.put("1-3次/低:2000-9999高", new ArrayList<Map<String, String>>());
        moneyGroup.put("1-3次/低:10000以上", new ArrayList<Map<String, String>>());
        moneyGroup.put("4-24次/中:0-499低", new ArrayList<Map<String, String>>());
        moneyGroup.put("4-24次/中:500-1999中", new ArrayList<Map<String, String>>());
        moneyGroup.put("4-24次/中:2000-9999高", new ArrayList<Map<String, String>>());
        moneyGroup.put("4-24次/中:10000以上", new ArrayList<Map<String, String>>());
        moneyGroup.put("24次以上/高:0-499低", new ArrayList<Map<String, String>>());
        moneyGroup.put("24次以上/高:500-1999中", new ArrayList<Map<String, String>>());
        moneyGroup.put("24次以上/高:2000-9999高", new ArrayList<Map<String, String>>());
        moneyGroup.put("24次以上/高:10000以上", new ArrayList<Map<String, String>>());
        for (String key : timesGroup.keySet())
        {
            List<Map<String, String>> tempList = timesGroup.get(key);
            for (Map<String, String> mp : tempList)
            {
                float totalPrice = Float.parseFloat(mp.get("totalPrice"));
                if (totalPrice < 500)
                {
                    moneyGroup.get(key + ":0-499低").add(mp);
                }
                else if (totalPrice < 2000)
                {
                    moneyGroup.get(key + ":500-1999中").add(mp);
                }
                else if (totalPrice < 10000)
                {
                    moneyGroup.get(key + ":2000-9999高").add(mp);
                }
                else
                {
                    moneyGroup.get(key + ":10000以上").add(mp);
                }
            }
        }
        long e = System.currentTimeMillis();
        logger.info("第三次分组结束，耗时：" + (e - d) + "毫秒");
        
        logger.info("组装数据开始。。。。");
        List<Map<String, String>> resultList = new ArrayList<>();
        for (String key : moneyGroup.keySet())
        {
            Map<String, String> mp = new HashMap<>();
            mp.put("totalTimes", key.split(":")[0]);
            mp.put("totalPrice", key.split(":")[1]);
            int mobileCount = 0;
            int unionCount = 0;
            for (Map<String, String> it : moneyGroup.get(key))
            {
                int type = Integer.parseInt(it.get("type"));
                if (type == 1)
                {
                    mobileCount++;
                }
                else
                {
                    unionCount++;
                }
            }
            mp.put("mobileCount", mobileCount + "");
            mp.put("unionCount", unionCount + "");
            resultList.add(mp);
        }
        long f = System.currentTimeMillis();
        logger.info("组装数据结束，耗时：" + (f - e) + "毫秒");
        
        Collections.sort(resultList, new Comparator<Map<String, String>>()
        {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2)
            {
                if (o1.get("totalTimes").compareTo(o2.get("totalTimes")) == 0)
                {
                    return o1.get("totalPrice").compareTo(o2.get("totalPrice"));
                }
                else
                {
                    return o1.get("totalTimes").compareTo(o2.get("totalTimes"));
                }
            }
        });
        return resultList;
    }

    @Override
    public String updateProductDetailImge()
        throws Exception
    {
        String image = "http://yangege.b0.upaiyun.com/product/d4bc8b11779.jpg!v1detail";
        short height = 174;
        short width = 620;
        List<ProductBaseEntity> productBases = temporaryDao.findProductBaseBySellerIds(Arrays.asList(new String[]{"231"}));

        for (ProductBaseEntity pbe : productBases)
        {
            /**
             * 1、找出基本商品所有的详情图
             * 2、插入一张顺序最大的图
             * 3、将原来所有顺序减1
             * 4、如果超过22张，则将最后一张删除
             */
            List<ProductBaseMobileDetailEntity> pbeMobileDetails = temporaryDao.findProductBaseMobileDetailsByProductId(pbe.getId());
            logger.info(String.format("基本商品Id=%-10d共找到详情图%-5d张", pbe.getId(), pbeMobileDetails.size()));
            
            ProductBaseMobileDetailEntity pbeMobileDetail = new ProductBaseMobileDetailEntity();
            pbeMobileDetail.setProductId(pbe.getId());
            pbeMobileDetail.setContent(image);
            pbeMobileDetail.setContentType((byte)1);
            pbeMobileDetail.setHeight(height);
            pbeMobileDetail.setWidth(width);
            pbeMobileDetail.setOrder((byte)22);
            productBaseDao.saveProductMobileDetail(pbeMobileDetail);
            
            logger.info(String.format("基本商品Id=%-10d插入一张详情图，id=%-10d", pbe.getId(), pbeMobileDetail.getId()));
            for (ProductBaseMobileDetailEntity detail : pbeMobileDetails)
            {
                detail.setOrder((byte)(detail.getOrder() - 1));
                if (detail.getOrder() <= 0)
                {
                    productBaseDao.deleteProductBaseMobileDetail(detail.getId());
                    logger.info(String.format("基本商品Id=%-10d删除一张详情图，id=%-10d", pbe.getId(), detail.getId()));
                }
                else
                {
                    productBaseDao.updateProducBasetMobileDetail(detail);
                    logger.info(String.format("基本商品Id=%-10d更新一张详情图，id=%-10d", pbe.getId(), detail.getId()));
                }
            }
            
            List<ProductEntity> products = temporaryDao.findProductByProductBaseId(pbe.getId());
            logger.info(String.format("基本商品Id=%-10d共找到关联商品%-5d个", pbe.getId(), products.size()));
            
            for (ProductEntity product : products)
            {
                /**
                 * 1、找出基本商品所有的详情图
                 * 2、插入一张顺序最大的图
                 * 3、将原来所有顺序减1
                 * 4、如果超过22张，则将最后一张删除
                 */
                List<ProductMobileDetailEntity> productMobileDetails = temporaryDao.findProductMobileDetailByProducId(product.getId());
                logger.info(String.format("基本商品Id=%-10d关联的商品Id=%-10d共找到详情图%-5d张", pbe.getId(), product.getId(), productMobileDetails.size()));
                
                ProductMobileDetailEntity entity = new ProductMobileDetailEntity();
                entity.setProductId(product.getId());
                entity.setContent(image);
                entity.setContentType((byte)1);
                entity.setHeight(height);
                entity.setWidth(width);
                entity.setOrder((byte)22);
                productDao.saveProductMobileDetail(entity);
                logger.info(String.format("基本商品Id=%-10d关联的商品Id=%-10d插入一张详情图，id=%-10d", pbe.getId(), product.getId(), entity.getId()));
                
                for (ProductMobileDetailEntity detail : productMobileDetails)
                {
                    detail.setOrder((byte)(detail.getOrder() - 1));
                    if (detail.getOrder() <= 0)
                    {
                        productDao.deleteProductMobileDetail(detail.getId());
                        logger.info(String.format("基本商品Id=%-10d关联的商品Id=%-10d删除一张详情图，id=%-10d", pbe.getId(), product.getId(), detail.getId()));
                    }
                    else
                    {
                        productDao.updateProductMobileDetail(detail);
                        logger.info(String.format("基本商品Id=%-10d关联的商品Id=%-10d更新一张详情图，id=%-10d", pbe.getId(), product.getId(), detail.getId()));
                        
                    }
                }
            }
        }
        return "成功处理：" + productBases.size() + "条";
    }

    @Override
    public String insertProductDetailImge() throws Exception {
        List<ProductBaseEntity> productBases = temporaryDao.findProductBaseBySellerIds(Arrays.asList(new String[]{"231"}));

        List<Integer> productIds = temporaryDao.findLackImageProductIds();
        for (Integer productId:productIds)
        {
            ProductEntity pe = productDao.findProductByID(productId,null);
            List<ProductBaseMobileDetailEntity> pbeMobileDetails = temporaryDao.findProductBaseMobileDetailsByProductId(pe.getProductBaseId());
            if (pbeMobileDetails != null && pbeMobileDetails.size()>0)
            {
                List<ProductMobileDetailEntity> productMobileDetails = temporaryDao.findProductMobileDetailByProducId(productId);
                for (ProductMobileDetailEntity detail : productMobileDetails)
                {
                    productDao.deleteProductMobileDetail(detail.getId());
                    logger.info(String.format("商品Id=%-10d删除一张详情图，id=%-10d",  productId, detail.getId()));
                }

            }

            for (ProductBaseMobileDetailEntity detail:pbeMobileDetails)
            {

                ProductMobileDetailEntity entity = new ProductMobileDetailEntity();
                entity.setProductId(productId);
                entity.setContent(detail.getContent());
                entity.setContentType((byte)1);
                entity.setHeight(detail.getHeight());
                entity.setWidth(detail.getWidth());
                entity.setOrder(detail.getOrder());
                productDao.saveProductMobileDetail(entity);
                logger.info(String.format("商品Id=%-10d插入一张详情图，id=%-10d", pe.getId(), entity.getId()));
            }
        }
        return "成功处理：" + productBases.size() + "条";
    }

    @Override
    public String insertProductHistorySalesVolume(String start, String end)
        throws Exception
    {
        int size = 0;
        DateTime startDT = DateTime.parse(start + "0000", DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        DateTime endDT = DateTime.parse(end + "0000", DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        while (startDT.isBefore(endDT))
        {
            String startTime = startDT.minusHours(1).toString("yyyy-MM-dd HH:00:00");
            String endTime = startDT.toString("yyyy-MM-dd HH:00:00");
            
            int dayAndHour = Integer.valueOf(startDT.minusHours(1).toString("yyyyMMddHH"));
            Map<String, Object> qPara = new HashMap<>();
            qPara.put("startTime", startTime);
            qPara.put("endTime", endTime);
            List<Map<String, Object>> volumeInfos = productDao.getSaleVolumeInfoByPayTime(qPara);
            logger.info(startTime + " 至 " + endTime + "查询到：" + volumeInfos.size() + "条数据");
            for (Map<String, Object> volumeInfo : volumeInfos)
            {
                try
                {
                    volumeInfo.put("salesDayHour", dayAndHour);
                    size += productDao.saveProductSaleVolume(volumeInfo);
                }
                catch (Exception e)
                {
                    if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_product_id_and_product_type_and_sales_day"))
                    {
                        logger.info(dayAndHour + "数据已经存在，跳过。。。。");
                    }
                    else
                    {
                        throw new Exception(e);
                    }
                }
            }
            startDT = startDT.plusHours(1);
        }
        return "成功处理：插入" + size + "条";
    }
}
