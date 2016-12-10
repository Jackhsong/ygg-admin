package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ServiceLog;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.*;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.service.CommonService;
import com.ygg.admin.service.ProductCheckLogService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

@Service("productService")
public class ProductServiceImpl implements ProductService
{
    
    Logger logger = Logger.getLogger(ProductServiceImpl.class);
    
    @Resource
    ProductDao productDao;
    
    @Resource
    PageCustomDao pageCustomDao;
    
    @Resource
    private CategoryDao categoryDao;
    
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Resource
    private BrandDao brandDao;
    
    @Resource
    private SystemLogDao logDao;
    
    @Resource
    private ProductCheckLogService productCheckLogService;

    @Resource
    private SystemLogService logService;

    @Resource
    private CommonService commonService;
    
    @Override
    public int saveProduct(Map<String, Object> para)
        throws Exception
    {
        logger.debug("开始插入商品信息: ");
        // 插入商品
        ProductEntity product = (ProductEntity)para.get("product");
        ProductBaseEntity pbe = productBaseDao.queryProductBaseById(product.getProductBaseId());
        BrandEntity be = brandDao.findBrandById(pbe.getBrandId());
        product.setReturnDistributionProportionType('1');
        product.setImage1(product.getImage1());
        product.setImage2(product.getImage2());
        product.setImage3(product.getImage3());
        product.setImage4(product.getImage4());
        product.setImage5(product.getImage5());
        productDao.save(product);
        logger.debug("插入商品信息成功: ");
        
        // 插入ProductCommonEntity
        ProductCommonEntity productCommon = (ProductCommonEntity)para.get("productCommon");
        productCommon.setProductId(product.getId());
        productCommon.setType(product.getType());
        productCommon.setMediumImage(adjustImageSize(productCommon.getMediumImage(), ImageUtil.getSuffix(ImageTypeEnum.v1brandProduct.ordinal())));
        productCommon.setSmallImage(adjustImageSize(productCommon.getSmallImage(), ImageUtil.getSuffix(ImageTypeEnum.v1cartProduct.ordinal())));
        productDao.saveProductCommon(productCommon);
        logger.debug("插入商品常用信息成功: ");
        
        // 插入商品数量表
        ProductCountEntity productCount = (ProductCountEntity)para.get("productCount");
        productCount.setProductId(product.getId());
        productDao.saveProductCount(productCount);
        logger.debug("插入商品数量成功: ");
        
        // 关联自定义页面
        RelationProductAndPageCustom relation1 = (para.get("relation1") == null) ? null : (RelationProductAndPageCustom)para.get("relation1");
        RelationProductAndPageCustom relation2 = (para.get("relation2") == null) ? null : (RelationProductAndPageCustom)para.get("relation2");
        if (relation1 != null)
        {
            relation1.setProductId(product.getId());
            pageCustomDao.saveRelationProductAndPageCustom(relation1);
            logger.debug("操作自定义页面信息成功: ");
        }
        if (relation2 != null)
        {
            relation2.setProductId(product.getId());
            pageCustomDao.saveRelationProductAndPageCustom(relation2);
            logger.debug("操作自定义页面信息成功: ");
        }
        
        // 插入ProductMobileDetailEntity
        DetailPicAndText detailPicAndText = (DetailPicAndText)para.get("detailPicAndText");
        List<Map<String, Object>> obileDetailList = getDetailListPara(detailPicAndText);
        for (Map<String, Object> curr : obileDetailList)
        {
            ProductMobileDetailEntity entity = new ProductMobileDetailEntity();
            byte order = (byte)(curr.get("order"));
            String pic = curr.get("pic") + "";
            String text = curr.get("text") + "";
            entity.setOrder(order);
            entity.setProductId(product.getId());
            
            if (!pic.equals(""))
            {
                entity.setContent(pic);
                entity.setContentType((byte)1);
                
                Map<String, Object> propertysMap = ImageUtil.getProperty(entity.getContent());
                entity.setHeight(Short.valueOf((String)propertysMap.get("height")));
                entity.setWidth(Short.valueOf((String)propertysMap.get("width")));
                productDao.saveProductMobileDetail(entity);
                logger.debug("插入MobileDetail信息1成功: ");
            }
            else if (!text.equals(""))
            {
                entity.setContent(text);
                entity.setContentType((byte)2);
                productDao.saveProductMobileDetail(entity);
                logger.debug("插入MobileDetail信息2成功: ");
            }
        }
        
        // 只有商品放入商城(isShowInMall=1)时，才会放入relation_category_and_product中
        /*
         * if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode()) { List<CategoryEntity> categoryList =
         * categoryDao.findCategoryByProductBaseId(product.getProductBaseId()); //去重，测试数据较多重复 Set<CategoryEntity>
         * categorySet = new HashSet<CategoryEntity>(categoryList); List<Map<String, Object>> productCategoryList = new
         * ArrayList<Map<String, Object>>(); if (product.getIsShowInMall() == 1) { for (CategoryEntity cae :
         * categorySet) { if (cae.getCategoryThirdId() != 0) {
         * 
         * Map<String, Object> caeMap = new HashMap<String, Object>(); caeMap.put("productBaseId",
         * product.getProductBaseId()); caeMap.put("productId", product.getId()); caeMap.put("categoryThirdId",
         * cae.getCategoryThirdId()); boolean exist = categoryDao.checkProductCategoryIsExist(caeMap); if (!exist) {
         * productCategoryList.add(caeMap); } } } } if (productCategoryList.size() > 0) {
         * categoryDao.insertRelationCategoryAndProduct(productCategoryList); } }
         */
        return 1;
    }
    
    /**
     * 复制商品
     */
    @Override
    public int copyProduct(int productId, Integer productType, int source, Map<String, Object> map)
        throws Exception
    {
        logger.info("开始复制商品信息: ");
        ProductEntity product = productDao.findProductByID(productId, productType);
        productType = productType == null ? product.getType() : productType;
        if (product == null)
        {
            if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == productType)
            {
                return -2;
            }
            else if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == productType)
            {
                return -3;
            }
            else
            {
                return -1;
            }
        }
        product.setId(0);
        product.setRemark("从商品ID:" + productId + "复制而来");
        product.setType(productType);
        if (source == 2)
        {
            product.setName("[售后专用]" + product.getName());
        }
        if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == productType)
        {
            product.setStartTime("0000-00-00 00:00:00");
            product.setEndTime("0000-00-00 00:00:00");
        }
        else if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == productType && source == 2)
        {
            product.setStartTime(DateTimeUtil.now());
            product.setEndTime(DateTime.now().plusDays(1).toString("yyyy-MM-dd HH:mm:ss"));
        }
        productDao.save(product);
        logger.debug("复制商品信息成功，新商品ID：" + product.getId());
        
        ProductCommonEntity productCommon = productDao.findProductCommonByProductId(productId);
        productCommon.setSellCount(0);
        productCommon.setProductId(product.getId());
        productCommon.setType(productType);
        if (source == 2)
        {
            productCommon.setName("[售后专用]" + productCommon.getName());
        }
        productDao.saveProductCommon(productCommon);
        logger.info("复制商品常用信息成功: ");
        
        ProductCountEntity productCount = productDao.findProductCountByProductId(productId);
        productCount.setSell(0);
        productCount.setLock(0);
        productCount.setStock(0);
        productCount.setProductId(product.getId());
        if (source == 2)
        {
            int nums = Integer.parseInt(map.get("nums") == null ? "1" : map.get("nums") + "");
            productCount.setStock(nums);
        }
        productDao.saveProductCount(productCount);
        logger.info("复制商品数量成功: ");
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("productId", productId);
        List<RelationProductAndPageCustom> rpapc = pageCustomDao.findRelationProductAndPageCustom(para);
        for (RelationProductAndPageCustom relation : rpapc)
        {
            relation.setProductId(product.getId());
            pageCustomDao.saveRelationProductAndPageCustom(relation);
            logger.info("复制自定义页面信息成功: ");
        }
        
        logger.info("开始复制MobileDetail: ");
        List<ProductMobileDetailEntity> mobileList = productDao.findProductMobileDetailByPara(para);
        for (ProductMobileDetailEntity mobile : mobileList)
        {
            mobile.setProductId(product.getId());
            productDao.saveProductMobileDetail(mobile);
            logger.info("插入MobileDetail信息成功: ");
        }
        
        /*
         * if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode()) { if (product.getIsShowInMall() == 1) {
         * List<Map<String, Object>> caList = categoryDao.findMallProductById(product.getId()); List<Map<String,
         * Object>> productCategoryList = new ArrayList<Map<String, Object>>(); for (Map<String, Object> it : caList) {
         * it.put("productId", product.getId()); boolean exist = categoryDao.checkProductCategoryIsExist(it); if
         * (!exist) { productCategoryList.add(it); } } if (productCategoryList != null && productCategoryList.size() >
         * 0) { categoryDao.insertRelationCategoryAndProduct(productCategoryList); } } }
         */
        return product.getId();
    }
    
    @Override
    public int updateProduct(Map<String, Object> para)
        throws Exception
    {
        logger.debug("开始更新商品信息: ");
        ProductEntity product = (ProductEntity)para.get("product");
        ProductEntity oldpe = productDao.findProductByID(product.getId(), null);
        int oldProductBaseId = oldpe.getProductBaseId();
        product.setImage1(adjustImageSize(product.getImage1(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage2(adjustImageSize(product.getImage2(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage3(adjustImageSize(product.getImage3(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage4(adjustImageSize(product.getImage4(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage5(adjustImageSize(product.getImage5(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        productDao.updateProduct(product);
        logger.info("更新id=" + product.getId() + "商品信息成功: ");
        // 更新ProductCommonEntity
        ProductCommonEntity productCommon = (ProductCommonEntity)para.get("productCommon");
        productCommon.setProductId(product.getId());
        productCommon.setMediumImage(adjustImageSize(productCommon.getMediumImage(), ImageUtil.getSuffix(ImageTypeEnum.v1brandProduct.ordinal())));
        productCommon.setSmallImage(adjustImageSize(productCommon.getSmallImage(), ImageUtil.getSuffix(ImageTypeEnum.v1cartProduct.ordinal())));
        if (productCommon.getId() == 0)
        {
            productCommon.setType(product.getType());
            productDao.saveProductCommon(productCommon);
            logger.debug("更新商品常用信息有误，重新插入商品常用信息表记录: ");
        }
        else
        {
            productDao.updateProductCommon(productCommon);
            logger.debug("更新商品常用信息成功: ");
        }
        
        // 插入商品数量表 根据productId找到ProductCount，再做相应修改
        ProductCountEntity productCount_exists = productDao.findProductCountByProductId(product.getId());
        ProductCountEntity productCount = (ProductCountEntity)para.get("productCount");
        if (productCount_exists == null)
        {
            productCount.setProductId(product.getId());
            productDao.saveProductCount(productCount);
            logger.debug("插入商品数量成功: ");
        }
        else
        {
            productCount_exists.setRestriction(productCount.getRestriction());
            // productCount_exists.setStock(productCount.getStock());
            productCount_exists.setStockAlgorithm(productCount.getStockAlgorithm());
            productDao.updateProductCount(productCount_exists);
            logger.debug("更新商品数量成功: ");
        }
        
        // 关联自定义页面
        RelationProductAndPageCustom relation1 = (RelationProductAndPageCustom)para.get("relation1");
        RelationProductAndPageCustom relation2 = (RelationProductAndPageCustom)para.get("relation2");
        
        if (relation1.getId() != 0)
        {
            if (!"".equals(relation1.getMarks()) && (relation1.getPageCustomId() != 0))
            {
                pageCustomDao.updateRelationProductAndPageCustom(relation1);
                logger.debug("更新自定义页面关联信息成功: ");
            }
            else
            {
                pageCustomDao.deleteRelationProductAndPageCustomById(relation1.getId());
                logger.debug("删除自定义页面关联信息成功: ");
            }
        }
        else
        {
            if (!"".equals(relation1.getMarks()) && (relation1.getPageCustomId() != 0))
            {
                // 新增
                relation1.setProductId(product.getId());
                pageCustomDao.saveRelationProductAndPageCustom(relation1);
                logger.debug("插入自定义页面关联信息成功: ");
            }
        }
        
        if (relation2.getId() != 0)
        {
            if (!"".equals(relation2.getMarks()) && (relation2.getPageCustomId() != 0))
            {
                pageCustomDao.updateRelationProductAndPageCustom(relation2);
                logger.debug("更新自定义页面关联信息成功: ");
            }
            else
            {
                pageCustomDao.deleteRelationProductAndPageCustomById(relation2.getId());
                logger.debug("删除自定义页面关联信息成功: ");
            }
        }
        else
        {
            if (!"".equals(relation2.getMarks()) && (relation2.getPageCustomId() != 0))
            {
                // 新增
                relation2.setProductId(product.getId());
                pageCustomDao.saveRelationProductAndPageCustom(relation2);
                logger.debug("插入自定义页面关联信息成功: ");
            }
        }
        
        // 更新ProductMobileDetailEntity
        DetailPicAndText detailPicAndText = (DetailPicAndText)para.get("detailPicAndText");
        List<Map<String, Object>> obileDetailList = getDetailListPara(detailPicAndText);
        for (Map<String, Object> curr : obileDetailList)
        {
            ProductMobileDetailEntity entity = new ProductMobileDetailEntity();
            byte order = (byte)(curr.get("order"));
            String pic = curr.get("pic") + "";
            String text = curr.get("text") + "";
            int id = (int)curr.get("id");
            entity.setOrder(order);
            entity.setProductId(product.getId());
            
            if (id == 0)
            {
                if (!pic.equals(""))
                {
                    entity.setContent(adjustImageSize(pic, ImageUtil.getSuffix(ImageTypeEnum.v1detail.ordinal())));
                    entity.setContentType((byte)1);
                    
                    Map<String, Object> propertysMap = ImageUtil.getProperty(entity.getContent());
                    entity.setHeight(Short.valueOf((String)propertysMap.get("height")));
                    entity.setWidth(Short.valueOf((String)propertysMap.get("width")));
                    productDao.saveProductMobileDetail(entity);
                    logger.debug("插入MobileDetail信息1成功: ");
                }
                else if (!text.equals(""))
                {
                    entity.setContent(text);
                    entity.setContentType((byte)2);
                    productDao.saveProductMobileDetail(entity);
                    logger.debug("插入MobileDetail信息2成功: ");
                }
            }
            else
            {
                if ("".equals(pic) && "".equals(text))
                {
                    logger.debug("删除MobileDetail信息成功");
                    // 删除
                    productDao.deleteProductMobileDetail(id);
                    
                }
                else
                {
                    // 更新
                    entity.setId(id);
                    if (!pic.equals(""))
                    {
                        entity.setContent(pic);
                        entity.setContentType((byte)1);
                        
                        // 根据ID查询一下product_mobile_detail，假如类型一致，且宽高也相同，则不再修改宽高
                        ProductMobileDetailEntity detailEntity = productDao.findProductMobileDetailById(id);
                        if (detailEntity.getContentType() == (byte)2 || (!entity.getContent().equals(detailEntity.getContent())) || detailEntity.getHeight() <= 1
                            || detailEntity.getWidth() <= 1)
                        {
                            Map<String, Object> propertysMap = ImageUtil.getProperty(entity.getContent());
                            entity.setHeight(Short.valueOf((String)propertysMap.get("height")));
                            entity.setWidth(Short.valueOf((String)propertysMap.get("width")));
                        }
                        else
                        {
                            entity.setHeight(Short.valueOf(detailEntity.getHeight()));
                            entity.setWidth(Short.valueOf(detailEntity.getWidth()));
                        }
                        productDao.updateProductMobileDetail(entity);
                        logger.debug("更新MobileDetail信息1成功: ");
                    }
                    else if (!text.equals(""))
                    {
                        entity.setContent(text);
                        entity.setContentType((byte)2);
                        productDao.updateProductMobileDetail(entity);
                        logger.debug("更新MobileDetail信息2成功: ");
                    }
                }
            }
        }
        
        if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode() && product.getIsShowInMall() == 1)
        {
            // 只有当商品关联的基本商品Id被修改时，商品的类目才会被修改
            if (oldProductBaseId != product.getProductBaseId())
            {
                // 先删除之前的基本商品关联分类信息
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("productBaseId", oldProductBaseId);
                mp.put("productId", product.getId());
                categoryDao.deleteRelationCategoryAndProduct(mp);
                
                // 再插入新的基本商品关联分类信息
                List<CategoryEntity> categoryList = categoryDao.findCategoryByProductBaseId(product.getProductBaseId());
                // 去重(测试数据较多重复)
                Set<CategoryEntity> categorySet = new HashSet<CategoryEntity>(categoryList);
                List<Map<String, Object>> productCategoryList = new ArrayList<Map<String, Object>>();
                for (CategoryEntity cae : categorySet)
                {
                    if (cae.getCategoryThirdId() != 0)
                    {
                        Map<String, Object> caeMap = new HashMap<String, Object>();
                        caeMap.put("productBaseId", product.getProductBaseId());
                        caeMap.put("productId", product.getId());
                        caeMap.put("categoryThirdId", cae.getCategoryThirdId());
                        boolean exist = categoryDao.checkProductCategoryIsExist(caeMap);
                        if (!exist)
                        {
                            productCategoryList.add(caeMap);
                        }
                    }
                }
                if (productCategoryList.size() > 0)
                {
                    categoryDao.insertRelationCategoryAndProduct(productCategoryList);
                }
            }
        }
        ProductEntity newpe = productDao.findProductByID(oldpe.getId(),null);
        logService.loggerUpdate("商品", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal(), CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal(), CommonEnum.OperationTypeEnum.MODIFY_PRODUCT.ordinal(), oldpe, newpe);
        return 1;
    }
    
    /**
     * 修改所需图片的尺寸
     * 
     * @param imageUrl
     * @param postfix
     * @return
     */
    private String adjustImageSize(String imageUrl, String postfix)
    {
    	return imageUrl;/*
        if (imageUrl == null || "".equals(imageUrl))
        {
            return "";
        }
        return (imageUrl.indexOf(ImageUtil.getPrefix()) > 0) ? imageUrl : (imageUrl + ImageUtil.getPrefix() + postfix);
*/    }
    
    List<Map<String, Object>> getDetailListPara(DetailPicAndText detailPicAndText)
    {
        byte order = 22;
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        resetData(detailPicAndText.getDetail_id_1(), detailPicAndText.getDetail_pic_1(), detailPicAndText.getDetail_text_1(), order--, resList);
        resetData(detailPicAndText.getDetail_id_2(), detailPicAndText.getDetail_pic_2(), detailPicAndText.getDetail_text_2(), order--, resList);
        resetData(detailPicAndText.getDetail_id_3(), detailPicAndText.getDetail_pic_3(), detailPicAndText.getDetail_text_3(), order--, resList);
        resetData(detailPicAndText.getDetail_id_4(), detailPicAndText.getDetail_pic_4(), detailPicAndText.getDetail_text_4(), order--, resList);
        resetData(detailPicAndText.getDetail_id_5(), detailPicAndText.getDetail_pic_5(), detailPicAndText.getDetail_text_5(), order--, resList);
        resetData(detailPicAndText.getDetail_id_6(), detailPicAndText.getDetail_pic_6(), detailPicAndText.getDetail_text_6(), order--, resList);
        resetData(detailPicAndText.getDetail_id_7(), detailPicAndText.getDetail_pic_7(), detailPicAndText.getDetail_text_7(), order--, resList);
        resetData(detailPicAndText.getDetail_id_8(), detailPicAndText.getDetail_pic_8(), detailPicAndText.getDetail_text_8(), order--, resList);
        resetData(detailPicAndText.getDetail_id_9(), detailPicAndText.getDetail_pic_9(), detailPicAndText.getDetail_text_9(), order--, resList);
        resetData(detailPicAndText.getDetail_id_10(), detailPicAndText.getDetail_pic_10(), detailPicAndText.getDetail_text_10(), order--, resList);
        resetData(detailPicAndText.getDetail_id_11(), detailPicAndText.getDetail_pic_11(), detailPicAndText.getDetail_text_11(), order--, resList);
        resetData(detailPicAndText.getDetail_id_12(), detailPicAndText.getDetail_pic_12(), detailPicAndText.getDetail_text_12(), order--, resList);
        resetData(detailPicAndText.getDetail_id_13(), detailPicAndText.getDetail_pic_13(), detailPicAndText.getDetail_text_13(), order--, resList);
        resetData(detailPicAndText.getDetail_id_14(), detailPicAndText.getDetail_pic_14(), detailPicAndText.getDetail_text_14(), order--, resList);
        resetData(detailPicAndText.getDetail_id_15(), detailPicAndText.getDetail_pic_15(), detailPicAndText.getDetail_text_15(), order--, resList);
        resetData(detailPicAndText.getDetail_id_16(), detailPicAndText.getDetail_pic_16(), detailPicAndText.getDetail_text_16(), order--, resList);
        resetData(detailPicAndText.getDetail_id_17(), detailPicAndText.getDetail_pic_17(), detailPicAndText.getDetail_text_17(), order--, resList);
        resetData(detailPicAndText.getDetail_id_18(), detailPicAndText.getDetail_pic_18(), detailPicAndText.getDetail_text_18(), order--, resList);
        resetData(detailPicAndText.getDetail_id_19(), detailPicAndText.getDetail_pic_19(), detailPicAndText.getDetail_text_19(), order--, resList);
        resetData(detailPicAndText.getDetail_id_20(), detailPicAndText.getDetail_pic_20(), detailPicAndText.getDetail_text_20(), order--, resList);
        resetData(detailPicAndText.getDetail_id_21(), detailPicAndText.getDetail_pic_21(), detailPicAndText.getDetail_text_21(), order--, resList);
        resetData(detailPicAndText.getDetail_id_22(), detailPicAndText.getDetail_pic_22(), detailPicAndText.getDetail_text_22(), order--, resList);
        return resList;
    }
    
    void resetData(int id, String pic, String text, byte order, List<Map<String, Object>> list)
    {
        Map<String, Object> result = null;
        if ("".equals(pic) && "".equals(text) && id == 0)
        {
            return;
        }
        result = new HashMap<String, Object>();
        result.put("id", id);
        result.put("pic", pic);
        result.put("text", text);
        result.put("order", order);
        list.add(result);
    }
    
    @Override
    public ProductEntity findProductById(int id)
        throws Exception
    {
        return productDao.findProductByID(id, null);
    }
    
    @Override
    public List<ProductEntity> batchFindProductByIds(List<Integer> ids)
        throws Exception
    {
        return productDao.batchFindProductByIDs(ids, null, ids.size());
    }
    
    public ProductEntity findProductById(int id, int type)
        throws Exception
    {
        return productDao.findProductByID(id, type);
    }

    public List<ProductEntity> batchFindProductByIds(List<Integer> ids, int type)
            throws Exception
    {
        return productDao.batchFindProductByIDs(ids, type, ids.size());
    }
    
    @Override
    public ProductCommonEntity findProductCommonByProductId(int id)
        throws Exception
    {
        return productDao.findProductCommonByProductId(id);
    }
    
    @Override
    public ProductCountEntity findProductCountByProductId(int id)
        throws Exception
    {
        return productDao.findProductCountByProductId(id);
    }
    
    @Override
    public Map<String, Object> findProductAndPageCustomInfo(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("productId", id);
        List<RelationProductAndPageCustom> rpapc = pageCustomDao.findRelationProductAndPageCustom(para);
        Map<String, Object> relationPageMap = new HashMap<String, Object>();
        if (rpapc.size() > 0)
        {
            for (int i = 0; i < rpapc.size(); i++)
            {
                RelationProductAndPageCustom pCustom = rpapc.get(i);
                relationPageMap.put("id" + i, pCustom.getId());
                relationPageMap.put("name" + i, pCustom.getMarks());
                relationPageMap.put("pageId" + i, pCustom.getPageCustomId());
            }
        }
        return relationPageMap;
    }
    
    @Override
    public Map<String, Object> findProductMobileDetailInfo(int id)
        throws Exception
    {
        Map<String, Object> mobileDetailMap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productId", id);
        List<ProductMobileDetailEntity> mobileDetails = productDao.findProductMobileDetailByPara(map);
        if (mobileDetails.size() > 0)
        {
            for (int i = 0; i < mobileDetails.size(); i++)
            {
                int index = i + 1;
                ProductMobileDetailEntity currDetail = mobileDetails.get(i);
                mobileDetailMap.put("id" + index, currDetail.getId());
                if (currDetail.getContentType() == 1)
                { // 图片
                    mobileDetailMap.put("pic" + index, currDetail.getContent());
                }
                else
                {// 文字
                    mobileDetailMap.put("text" + index, currDetail.getContent());
                }
            }
        }
        return mobileDetailMap;
    }
    
    @Override
    @ServiceLog(description = "加载特卖商品列表")
    public Map<String, Object> jsonProductForManage(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> productInfoList = productDao.findProductInfoForManage(para);
        int total = 0;
        for (Map<String, Object> map : productInfoList)
        {
            String sellerName = (String) map.get("sellerName");
            if (map.get("sellerIsAvailable") != null && ((Integer) map.get("sellerIsAvailable")) == 0) {
                sellerName += "<span style=\"color:red\">(已停用)</span>";
            }
            map.put("sellerName", sellerName);
            map.put("editId", map.get("id"));
            String remark = map.get("remark") + "";
            if (!StringUtils.isEmpty(remark) && remark.length() > 15)
            {
                remark = remark.substring(0, 13) + "...";
            }
            if(map.get("image1") != null){
                String img = "<img src='" + map.get("image1") + "' style='max-width:80px;max-height:80px' />";
                String url = "<a href='" + map.get("image1") + "' target='_blank'> " + img + " </a>";
                map.put("mainImage", url);
            }

            map.put("remark", remark);
            map.put("isAvailable", ((int)map.get("isAvailableCode") == 1) ? "可用" : "停用");// 使用状态
            map.put("isOffShelves", ((int)map.get("isOffShelvesCode") == 1) ? "已下架" : "出售中");// 商品状态
            map.put("remindStock", ((int)map.get("isRemindStock") == 1) ? "开启" : "关闭");// 库存提醒状态
            int productType = Integer.valueOf(map.get("type") + "").intValue();
//            StringBuffer sb = new StringBuffer("<a target='_blank' href='http://m.gegejia.com/");
            StringBuffer sb = new StringBuffer("");
            if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == productType)
            {
//                sb.append("item-");
            }
            else if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == productType)
            {
//                sb.append("mitem-");
            }
//            sb.append(map.get("id")).append(".htm'>").append(map.get("name")).append("</a>");
            sb.append(map.get("name"));
            map.put("pName", sb.toString());
            
            int baseId = Integer.valueOf(map.get("baseId") + "").intValue();
            int id = Integer.valueOf(map.get("id") + "").intValue();
            // 附加商品的所属分类信息
            appendCategoryInfo(baseId, id, map);
            
            String submitContent = "";
            int submitType = Integer.parseInt(map.get("submitType") == null ? "1" : map.get("submitType") + "");
            float wholesalePrice = Float.parseFloat(map.get("wholesalePrice") == null ? "0.00" : map.get("wholesalePrice") + "");
            float deduction = Float.parseFloat(map.get("deduction") == null ? "0.00" : map.get("deduction") + "");
            float selfPurchasePrice = Float.parseFloat(map.get("selfPurchasePrice") == null ? "0.00" : map.get("selfPurchasePrice") + "");
            float salesPrice = Float.parseFloat(map.get("salesPrice") == null ? "0.00" : map.get("salesPrice") + "");
            if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())// 正常结算,使用wholesalePrice
            {
                submitContent = "供货价 " + MathUtil.round(wholesalePrice, 2);
            }
            else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())// 扣点结算，salesPrice*(1-deduction/100)
            {
                submitContent = "扣点 " + MathUtil.round(salesPrice * (1 - deduction / 100), 2);
            }
            else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())// 自营采购价，selfPurchasePrice
            {
                submitContent = "自营采购价" + MathUtil.round(selfPurchasePrice, 2);
            }
            map.put("submitContent", submitContent);

            if(map.get("startTime") != null)
                map.put("startTime", DateTimeUtil.timestampObjectToWebString(map.get("startTime")));
            if(map.get("endTime") != null)
                map.put("endTime", DateTimeUtil.timestampObjectToWebString(map.get("endTime")));
            // 本期销量
            if(StringUtils.isNotEmpty((String) map.get("startTime"))  && StringUtils.isNotEmpty((String) map.get("endTime")) ){
               Integer startDayHour = DateTimeUtil.getDayHour((String) map.get("startTime"));
               Integer endDayHour = DateTimeUtil.getDayHour((String) map.get("endTime"));
                Map<String, Object> qPara = new HashMap<>();
                qPara.put("startDayHour", startDayHour);
                qPara.put("endDayHour", endDayHour);
                qPara.put("productId", map.get("id"));
                qPara.put("productType", map.get("type"));
                map.put("periodSaleVolume", productDao.countSaleVolumeByDayHours(qPara));
            }

        }
        total = productDao.countProductInfoForManage(para);
        resultMap.put("rows", productInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int forSale(Map<String, Object> para)
        throws Exception
    {
        int code = (int)para.get("code");
        List<Integer> idList = (List<Integer>)para.get("idList");
        int result = productDao.forSale(para);
        if (result > 0)
        {
            // 下架时回收库存
            if (code == CommonConstant.COMMON_YES)
            {
                for (Integer productId : idList)
                {
                    ProductEntity product = productDao.findProductByID(productId, null);
                    ProductCountEntity productCount = productDao.findProductCountByProductIdForUpdate(productId);
                    if (product != null)
                    {
                        ProductBaseEntity productBase = productBaseDao.findProductBaseByIdForUpdate(product.getProductBaseId());
                        if (productCount.getStock() > productCount.getLock())
                        {
                            int change = productCount.getStock() - productCount.getLock();
                            
                            // 将product的stock减少change,productBase的available_stock增加change
                            if (productDao.updateProductStock(productId, productCount.getStock(), -change) > 0)
                            {
                                int status = 0;
                                Map<String, Object> update = new HashMap<>();
                                update.put("changeStock", -change);
                                update.put("oldAvailableStock", productBase.getAvailableStock());
                                update.put("id", productBase.getId());
                                if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                                {
                                    status = productBaseDao.adjustSaleStock(update);
                                }
                                else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                                {
                                    status = productBaseDao.adjustMallStock(update);
                                }
                                if (status > 0)
                                {
                                    logger.info("************商品Id=" + productId + "下架，回收库存" + change + "***********");
                                }
                                else
                                {
                                    logger.info("************商品Id=" + productId + "下架，回收库存失败***********");
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int forAvailable(Map<String, Object> para)
        throws Exception
    {
        int code = (int)para.get("code");
        List<Integer> idList = (List<Integer>)para.get("idList");
        int result = productDao.forAvailable(para);
        if (result > 0)
        {
            // 停用时回收库存
            if (code == CommonConstant.COMMON_NO)
            {
                for (Integer productId : idList)
                {
                    ProductEntity product = productDao.findProductByID(productId, null);
                    ProductCountEntity productCount = productDao.findProductCountByProductIdForUpdate(productId);
                    if (product != null)
                    {
                        ProductBaseEntity productBase = productBaseDao.findProductBaseByIdForUpdate(product.getProductBaseId());
                        if (productCount.getStock() > productCount.getLock())
                        {
                            int change = productCount.getStock() - productCount.getLock();
                            
                            // 将product的stock减少change,product的available_stock增加change
                            if (productDao.updateProductStock(productId, productCount.getStock(), -change) > 0)
                            {
                                int status = 0;
                                Map<String, Object> update = new HashMap<>();
                                update.put("changeStock", -change);
                                update.put("oldAvailableStock", productBase.getAvailableStock());
                                update.put("id", productBase.getId());
                                if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                                {
                                    status = productBaseDao.adjustSaleStock(update);
                                }
                                else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                                {
                                    status = productBaseDao.adjustMallStock(update);
                                }
                                if (status > 0)
                                {
                                    logger.info("************商品Id=" + productId + "停用，回收库存" + change + "***********");
                                }
                                else
                                {
                                    logger.info("************商品Id=" + productId + "停用，回收库存失败***********");
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    @Override
    public int updateProductTime(ProductEntity productEntity)
        throws Exception
    {
        return productDao.updateProductTime(productEntity);
    }
    
    @Override
    public Map<String, Object> findProductAndCountInfoByProductId(int id)
        throws Exception
    {
        Map<String, Object> rMap = productDao.findProductAndCountInfoByProductId(id);
        rMap.put("id", rMap.get("id") == null ? "" : rMap.get("id") + "");
        rMap.put("shortName", rMap.get("shortName") == null ? "" : rMap.get("shortName") + "");
        rMap.put("name", rMap.get("name") == null ? "" : rMap.get("name") + "");
        rMap.put("sell", rMap.get("sell") == null ? "" : rMap.get("sell") + "");
        rMap.put("stock", rMap.get("stock") == null ? "" : rMap.get("stock") + "");
        rMap.put("lockNum", rMap.get("lockNum") == null ? "" : rMap.get("lockNum") + "");
        return rMap;
    }
    
    @Override
    public int addProductSellNum(Map<String, Object> para)
        throws Exception
    {
        return productDao.addProductSellNum(para);
    }
    
    @Override
    public int updateProductPara(Map<String, Object> para, boolean isUpdateProductCommon)
        throws Exception
    {
        Map<String, Object> upPara = new HashMap<>();
        Integer id = para.get("id") == null ? null : Integer.parseInt(para.get("id") + "");
        List<Integer> ids = para.get("ids") == null ? null : (List<Integer>) para.get("ids");
        Object salesPrice = para.get("salesPrice");
        Object marketPrice = para.get("marketPrice");
        Object shortName = para.get("shortName");
        Object name = para.get("name");
        Object activitiesStatus = para.get("activitiesStatus");
        if (id != null)
        {
            upPara.put("id", id);
        }else if(ids != null){
            upPara.put("idList", ids);
        }
        if (salesPrice != null)
        {
            upPara.put("salesPrice", salesPrice);
        }
        if (marketPrice != null)
        {
            upPara.put("marketPrice", marketPrice);
        }
        if (shortName != null)
        {
            upPara.put("shortName", shortName);
        }
        if (name != null)
        {
            upPara.put("name", name);
        }
        if (activitiesStatus != null)
        {
            upPara.put("activitiesStatus", activitiesStatus);
        }
        productDao.updateProductByPara(upPara);
        if (isUpdateProductCommon)
        {
            productDao.updateProductCommonByPara(upPara);
        }
        
        return 1;
        
    }
    
    @Override
    public int batchUpdateProductTip(List<Integer> idList, String tip)
        throws Exception
    {
        int num = 0;
        Map<String, Object> para = new HashMap<String, Object>();
        for (Integer id : idList)
        {
            Map<String, Object> map = productDao.findProductTipById(id);
            if (map != null)
            {
                Object tipObj = map.get("tip");
                String newTip = tipObj == null ? tip : String.valueOf(tipObj).toString() + tip;
                para.put("id", id);
                para.put("tip", newTip);
                int status = productDao.updateProductTipByPara(para);
                num += status;
            }
        }
        return num;
    }
    
    @Override
    public int findMaxProductId()
        throws Exception
    {
        
        int id = productDao.findMaxProductId();
        return id + 1;
    }
    
    @Override
    public Map<String, Object> canEditProduct(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        
        ProductEntity product = (ProductEntity)para.get("product");
        
        ProductEntity oldProduct = productDao.findProductByID(product.getId(), null);
        
        // 1 若商品已有销量或已经被锁定，则不允许修改商品 条形码 和 商品编码
        if (!oldProduct.getBarcode().equals(product.getBarcode()) || !oldProduct.getCode().equals(product.getCode()))
        {
            Map<String, Object> rMap = productDao.findProductAndCountInfoByProductId(product.getId());
            Integer sell = rMap.get("sell") == null ? null : Integer.valueOf(rMap.get("sell") + "");
            Integer lockNum = rMap.get("lockNum") == null ? null : Integer.valueOf(rMap.get("lockNum") + "");
            if (sell != null && lockNum != null)
            {
                if (sell > 0 || lockNum > 0)
                {
                    result.put("status", 0);
                    result.put("msg", "该商品已经卖出或已被添加至购物车中，无法修改条形码和编码");
                    return result;
                }
            }
        }
        result.put("status", 1);
        return result;
    }
    
    @Override
    public Map<String, Object> copyProductFromOtherType(String ids, int fromType, int toType)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        StringBuffer msg = new StringBuffer();
        String[] productIdStrArr = ids.split(",");
        for (String productIdStr : productIdStrArr)
        {
            productIdStr = productIdStr.trim();
            if (!Pattern.matches("\\d+", productIdStr))
            {
                msg.append("Id=").append(productIdStr).append("的").append(ProductEnum.PRODUCT_TYPE.getDescByCode(fromType)).append("不存在<br/>");
            }
            else
            {
                int productId = Integer.valueOf(productIdStr).intValue();
                ProductEntity product = productDao.findProductByID(productId, fromType);
                if (product == null)
                {
                    msg.append("Id=").append(productIdStr).append("的").append(ProductEnum.PRODUCT_TYPE.getDescByCode(fromType)).append("不存在<br/>");
                }
                else
                {
                    product.setId(0);
                    product.setRemark("从" + (ProductEnum.PRODUCT_TYPE.getDescByCode(fromType)) + ":" + productId + "复制而来");
                    product.setType(toType);
                    if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == toType)
                    {
                        product.setStartTime("0000-00-00 00:00:00");
                        product.setEndTime("0000-00-00 00:00:00");
                    }
                    if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == toType)
                    {
                        product.setStartTime(DateTime.now().toString("yyyy-MM-dd 10:00:00"));
                        product.setEndTime(DateTime.now().plusDays(1).toString("yyyy-MM-dd 10:00:00"));
                    }
                    productDao.save(product);
                    logger.debug("复制商品信息成功，新商品ID：" + product.getId());
                    
                    ProductCommonEntity productCommon = productDao.findProductCommonByProductId(productId);
                    productCommon.setSellCount(0);
                    productCommon.setProductId(product.getId());
                    productCommon.setType(toType);
                    productDao.saveProductCommon(productCommon);
                    logger.info("复制商品常用信息成功: ");
                    
                    ProductCountEntity productCount = productDao.findProductCountByProductId(productId);
                    productCount.setSell(0);
                    productCount.setLock(0);
                    productCount.setStock(0);
                    productCount.setProductId(product.getId());
                    productDao.saveProductCount(productCount);
                    logger.info("复制商品数量成功: ");
                    
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("productId", productId);
                    List<RelationProductAndPageCustom> rpapc = pageCustomDao.findRelationProductAndPageCustom(para);
                    for (RelationProductAndPageCustom relation : rpapc)
                    {
                        relation.setProductId(product.getId());
                        pageCustomDao.saveRelationProductAndPageCustom(relation);
                        logger.info("复制自定义页面信息成功: ");
                    }
                    
                    logger.info("开始复制MobileDetail: ");
                    List<ProductMobileDetailEntity> mobileList = productDao.findProductMobileDetailByPara(para);
                    for (ProductMobileDetailEntity mobile : mobileList)
                    {
                        mobile.setProductId(product.getId());
                        productDao.saveProductMobileDetail(mobile);
                        logger.info("插入MobileDetail信息成功: ");
                    }
                }
            }
        }
        resultMap.put("status", 1);
        if (msg.length() > 0)
        {
            resultMap.put("msg", msg.toString());
        }
        else
        {
            resultMap.put("msg", "复制成功");
        }
        return resultMap;
    }
    
    @Override
    public int changeEmailRemind(Map<String, Object> para)
        throws Exception
    {
        productDao.changeEmailRemind(para);
        return 1;
    }
    
    @Override
    public Map<String, Object> findGegeWelfareInfo(Map<String, Object> para)
        throws Exception
    {
        /**
         * p.id AS productId, p.name AS productName, p.type AS productType, p.sales_price AS gegePrice, pc.stock AS
         * stock, gwp.limit_price AS limitPrice, gwp.remark AS remark
         */
        Map<String, Object> result = new HashMap<String, Object>();
        int total = 0;
        List<Map<String, Object>> rows = productDao.findAllGegeWelfareProductByPara(para);
        if (!rows.isEmpty())
        {
            total = productDao.countAllGegeWelfareProductByPara(para);
            for (Map<String, Object> it : rows)
            {
                int productType = Integer.parseInt(it.get("productType") + "");
                int productId = Integer.parseInt(it.get("productId") + "");
                String link = "";
                if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    link = "http://m.gegejia.com/item-" + productId + ".htm";
                }
                else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    link = "http://m.gegejia.com/mitem-" + productId + ".htm";
                }
                it.put("productLink", link);
                StringBuffer brand = new StringBuffer();
                List<String> brandIds = Arrays.asList(it.get("brandIds").toString().split(","));
                for (String brandId : brandIds)
                {
                    BrandEntity be = brandDao.findBrandById(StringUtils.isEmpty(brandId) ? 0 : Integer.parseInt(brandId));
                    brand.append(be == null ? "" : be.getName()).append(";");
                }
                it.put("brand", brand.toString().endsWith(";") ? brand.toString().substring(0, brand.toString().lastIndexOf(";")) : brand.toString());
                it.put("payTimeBegin", it.get("payTimeBegin") == null ? "" : ((Timestamp)it.get("payTimeBegin")).toString());
                it.put("payTimeEnd", it.get("payTimeEnd") == null ? "" : ((Timestamp)it.get("payTimeEnd")).toString());
            }
        }
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map<String, Object> saveOrUpdateGegeWelfareProduct(int id, int productId, double salesPrice, double limitPrice, int limitNum, String remark, String brandIds,
        String payTimeBegin, String payTimeEnd)
            throws Exception
    {
        int status = 0;
        String msg = "";
        if (productDao.findProductByID(productId, null) == null)
        {
            msg = "商品ID不存在";
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", msg);
            return result;
        }
        
        if (id == 0)
        {
            // 新增
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("productId", productId);
            List<Map<String, Object>> reList = productDao.findAllGegeWelfareProductByPara(para);
            if (reList.size() > 0)
            {
                msg = "已经存在该商品";
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", status);
                result.put("msg", msg);
                return result;
            }
        }
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("salesPrice", salesPrice);
        para.put("activitiesStatus", ProductEnum.PRODUCT_ACTIVITY_STATUS.GEGE_WELFARE.getCode());
        para.put("id", productId);
        // 新增 修改product & productCommon 的salesPrice ； 插入 product_gege_welfare 表
        status = productDao.updateProductByPara(para);
        if (status == 0)
        {
            throw new ServiceException();
        }
        status = productDao.updateProductCommonByPara(para);
        if (status == 0)
        {
            throw new ServiceException();
        }
        // 同步修改商品限购数量为limit
        Map<String, Object> updateProductCount = new HashMap<>();
        updateProductCount.put("restriction", limitNum);
        updateProductCount.put("productId", productId);
        status = productDao.updateProductCountByPara(updateProductCount);
        if (status == 0)
        {
            throw new ServiceException();
        }
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("productId", productId);
        insertMap.put("limitPrice", limitPrice);
        insertMap.put("limitNum", limitNum);
        insertMap.put("remark", remark);
        insertMap.put("brandIds", brandIds.startsWith(",") ? brandIds.replaceFirst(",", "") : brandIds);
        insertMap.put("payTimeBegin", payTimeBegin);
        insertMap.put("payTimeEnd", payTimeEnd);
        if (id == 0)
        {
            status = productDao.addGegeWelfareProduct(insertMap);
            logService.loggerInsert("新增格格福利商品", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal(), CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal(), CommonEnum.OperationTypeEnum.ADD_GEGEWELFARE_PRODUCT.ordinal(), insertMap);
        }
        else
        {
            insertMap.put("id", id);
            status = productDao.updateGegeWelfareProduct(insertMap);
            try
            {
                if (status == 1)
                {
                    String username = SecurityUtils.getSubject().getPrincipal() + "";
                    Map<String, Object> logPara = new HashMap<String, Object>();
                    logPara.put("username", username);
                    logPara.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                    logPara.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_GEGEWELFARE_PRODUCT.ordinal());
                    logPara.put("content", "用户【" + username + "】更新了格格福利商品，商品Id=" + productId + ",福利价格：" + salesPrice + ",购买要求：站内消费额满" + limitPrice);
                    logPara.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                    logDao.logger(logPara);
                }
            }
            catch (Exception e)
            {
                logger.error("更新格格福利商品记录日志出错了", e);
            }
            
        }
        if (status == 0)
        {
            throw new ServiceException();
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", msg);
        return result;
    }
    
    @Override
    public Map<String, Object> deleteGegeWelfareProduct(List<Integer> productIdList)
        throws Exception
    {
        int nums = 0;
        int status = 0;
        for (Integer it : productIdList)
        {
            // 新增 修改product.activities_status = 1 ；删除 product_gege_welfare 表
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("activitiesStatus", ProductEnum.PRODUCT_ACTIVITY_STATUS.NORMAL.getCode());
            para.put("id", it);
            status = productDao.updateProductByPara(para);
            if (status == 0)
            {
                throw new ServiceException();
            }
            status = productDao.deleteGegeWelfareProductByProductId(it);
            if (status == 0)
            {
                throw new ServiceException();
            }
            nums++;
            logService.loggerDelete("删除格格福利商品", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal(), CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal(), CommonEnum.OperationTypeEnum.DELETE_GEGEWELFARE_PRODUCT.ordinal(),it.intValue());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", "成功删除了" + nums + "个格格福利商品。");
        return result;
    }
    
    @Override
    public Map<String, Object> jsonMallProductForManage(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> productInfoList = productDao.findMallProductForManage(para);
        int total = 0;
        for (Map<String, Object> map : productInfoList)
        {
            String sellerName = (String) map.get("sellerName");
            if (map.get("sellerIsAvailable") != null && ((Integer) map.get("sellerIsAvailable")) == 0) {
                sellerName += "<span style=\"color:red\">(已停用)</span>";
            }
            map.put("sellerName", sellerName);

            map.put("editId", map.get("id"));
            String remark = map.get("remark") + "";
            if (!StringUtils.isEmpty(remark) && remark.length() > 15)
            {
                remark = remark.substring(0, 13) + "...";
            }
            if(map.get("image1") != null){
                String img = "<img src='" + map.get("image1") + "' style='max-width:80px;max-height:80px' />";
                String url = "<a href='" + map.get("image1") + "' target='_blank'> " + img + " </a>";
                map.put("mainImage", url);
            }
            map.put("remark", remark);
            map.put("isAvailable", ((int)map.get("isAvailableCode") == 1) ? "可用" : "停用");// 使用状态
            map.put("isOffShelves", ((int)map.get("isOffShelvesCode") == 1) ? "已下架" : "出售中");// 商品状态
            map.put("remindStock", ((int)map.get("isRemindStock") == 1) ? "开启" : "关闭");// 库存提醒状态
            int productType = Integer.valueOf(map.get("type") + "").intValue();
            StringBuffer sb = new StringBuffer("<a target='_blank' href='http://m.gegejia.com/");
            if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == productType)
            {
                sb.append("item-");
            }
            else if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == productType)
            {
                sb.append("mitem-");
            }
            sb.append(map.get("id")).append(".htm'>").append(map.get("name")).append("</a>");
            map.put("pName", sb.toString());
            map.put("isShowInMallDesc", ((int)map.get("isShowInMall") == 1) ? "是" : "否");
            int baseId = Integer.valueOf(map.get("baseId") + "").intValue();
            int id = Integer.valueOf(map.get("id") + "").intValue();
            
            // 附加商品的所属分类信息
            appendCategoryInfo(baseId, id, map);
            
            String submitContent = "";
            int submitType = Integer.parseInt(map.get("submitType") == null ? "1" : map.get("submitType") + "");
            float wholesalePrice = Float.parseFloat(map.get("wholesalePrice") == null ? "0.00" : map.get("wholesalePrice") + "");
            float deduction = Float.parseFloat(map.get("deduction") == null ? "0.00" : map.get("deduction") + "");
            float selfPurchasePrice = Float.parseFloat(map.get("selfPurchasePrice") == null ? "0.00" : map.get("selfPurchasePrice") + "");
            float salesPrice = Float.parseFloat(map.get("salesPrice") == null ? "0.00" : map.get("salesPrice") + "");
            if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())// 正常结算,使用wholesalePrice
            {
                submitContent = "供货价 " + MathUtil.round(wholesalePrice, 2);
            }
            else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())// 扣点结算，salesPrice*(1-deduction/100)
            {
                submitContent = "扣点 " + MathUtil.round(salesPrice * (1 - deduction / 100), 2);
            }
            else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())// 自营采购价，selfPurchasePrice
            {
                submitContent = "自营" + MathUtil.round(selfPurchasePrice, 2);
            }
            map.put("submitContent", submitContent);
            // 30天销量
            int startDayHour = Integer.valueOf(DateTime.now().minusDays(30).toString("yyyyMMddHH"));
            int endDayHour = Integer.valueOf(DateTime.now().toString("yyyyMMddHH"));
            Map<String, Object> qPara = new HashMap<>();
            qPara.put("startDayHour", startDayHour);
            qPara.put("endDayHour", endDayHour);
            qPara.put("productId", map.get("id"));
            qPara.put("productType", map.get("type"));
            map.put("thirtyDaysSaleVolume", productDao.countSaleVolumeByDayHours(qPara));
        }
        total = productDao.countMallProductForManage(para);
        resultMap.put("rows", productInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    /**
     * 附加商品的所属分类信息
     * 
     * @param baseId
     * @param id
     * @param map
     * @throws Exception
     */
    private void appendCategoryInfo(int baseId, int id, Map<String, Object> map)
        throws Exception
    {
        List<CategoryEntity> categoryList = categoryDao.findCategoryByProductBaseId(baseId);
        List<RelationCategoryAndProductEntity> categoryThirdList = categoryDao.findThirdCatetoryByProductId(id);
        for (CategoryEntity ce : categoryList)
        {
            ce.setSequence(0);
            for (RelationCategoryAndProductEntity rcp : categoryThirdList)
            {
                if (ce.getCategoryThirdId() == rcp.getCategoryThirdId())
                {
                    ce.setSequence(rcp.getSequence());
                }
            }
        }
        StringBuilder categoryName = new StringBuilder();
        StringBuilder categorySeq = new StringBuilder();
        for (CategoryEntity ce : categoryList)
        {
            categoryName.append(ce.getCategoryName()).append(";");
            categorySeq.append(ce.getSequence()).append(";");
        }
        map.put("categoryName", categoryName.length() > 0 ? categoryName.substring(0, categoryName.length() - 1) : categoryName);
        map.put("categorySeq", categorySeq.length() > 0 ? categorySeq.substring(0, categorySeq.length() - 1) : categorySeq);
    }
    
    @Override
    public int updateShowInMall(Map<String, Object> para)
        throws Exception
    {
        int isShow = Integer.valueOf(para.get("isShow") + "").intValue();
        List<String> productIdList = (List<String>)para.get("idList");
        int result = productDao.updateShowInMall(para);
        if (result > 0)
        {
            if (isShow == 1)// 放入商城
            {
                List<Map<String, Object>> productCategoryList = new ArrayList<Map<String, Object>>();
                for (String idStr : productIdList)
                {
                    int productId = Integer.valueOf(idStr).intValue();
                    ProductEntity pe = productDao.findProductByID(productId, ProductEnum.PRODUCT_TYPE.MALL.getCode());
                    if (pe != null)
                    {
                        // 再插入新的基本商品关联分类信息
                        List<CategoryEntity> categoryList = categoryDao.findCategoryByProductBaseId(pe.getProductBaseId());
                        // 去重(测试数据较多重复)
                        Set<CategoryEntity> categorySet = new HashSet<CategoryEntity>(categoryList);
                        
                        for (CategoryEntity cae : categorySet)
                        {
                            if (cae.getCategoryThirdId() != 0)
                            {
                                Map<String, Object> caeMap = new HashMap<String, Object>();
                                caeMap.put("productBaseId", pe.getProductBaseId());
                                caeMap.put("productId", productId);
                                caeMap.put("categoryThirdId", cae.getCategoryThirdId());
                                boolean exist = categoryDao.checkProductCategoryIsExist(caeMap);
                                if (!exist)
                                {
                                    productCategoryList.add(caeMap);
                                }
                            }
                        }
                    }
                }
                if (productCategoryList.size() > 0)
                {
                    categoryDao.insertRelationCategoryAndProduct(productCategoryList);
                }
            }
            else if (isShow == 0)// 从商城移除
            {
                Map<String, Object> mp = new HashMap<String, Object>();
                for (String idStr : productIdList)
                {
                    mp.put("productId", idStr);
                    categoryDao.deleteRelationCategoryAndProduct(mp);
                }
            }
        }
        return 1;
    }
    
    @Override
    public int classifyProduct(List<CategoryEntity> categoryList)
        throws Exception
    {
        if (categoryList == null)
        {
            return -1;
        }
        for (CategoryEntity ce : categoryList)
        {
            boolean isExist = categoryDao.checkProductBaseCategoryIsExist(ce);
            if (isExist)
            {
                continue;
            }
            else
            {
                categoryDao.insertCategory(ce);
            }
            if (ce.getCategoryThirdId() != 0)
            {
                List<Map<String, Object>> rcpMap = new ArrayList<Map<String, Object>>();
                List<Integer> productIdList = productBaseDao.findProductIdByProductBaseId(ce.getProductBaseId(), ProductEnum.PRODUCT_TYPE.MALL.getCode());
                for (Integer productId : productIdList)
                {
                    ProductEntity pe = productDao.findProductByID(productId, ProductEnum.PRODUCT_TYPE.MALL.getCode());
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("productBaseId", ce.getProductBaseId());
                    para.put("productId", productId);
                    para.put("categoryThirdId", ce.getCategoryThirdId());
                    if (pe != null && pe.getIsShowInMall() == 1)
                    {
                        boolean exist = categoryDao.checkProductCategoryIsExist(para);
                        if (exist)
                        {
                            continue;
                        }
                        else
                        {
                            rcpMap.add(para);
                        }
                    }
                }
                if (rcpMap.size() > 0)
                {
                    categoryDao.insertRelationCategoryAndProduct(rcpMap);
                }
            }
        }
        return 1;
    }
    
    @Override
    public String exportProductImage(int productId)
        throws Exception
    {
        String nowStr = DateTime.now().toString("yyyy-MM-dd_HH_mm_ss");
        File fileDir = new File(YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
        File newDir = new File(fileDir, nowStr + "_" + new Random().nextInt(10000) + "image");
        newDir.mkdir();
        ProductEntity pe = productDao.findProductByID(productId, null);
        
        if (pe != null)
        {
            if (!"".equals(pe.getImage1()))
            {
                String image = pe.getImage1();
                if (image.indexOf(ImageUtil.getPrefix()) > 0)
                {
                    image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                }
                String fileName = "主图1";
                if (image.lastIndexOf(".") > -1)
                {
                    fileName += image.substring(image.lastIndexOf("."), image.length());
                }
                else
                {
                    fileName += ".jpg";
                }
                File file = new File(newDir, fileName);
                ImageUtil.saveToFile(image, file.getAbsolutePath());
            }
            if (!"".equals(pe.getImage2()))
            {
                String image = pe.getImage2();
                if (image.indexOf(ImageUtil.getPrefix()) > 0)
                {
                    image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                }
                String fileName = "主图2";
                if (image.lastIndexOf(".") > -1)
                {
                    fileName += image.substring(image.lastIndexOf("."), image.length());
                }
                else
                {
                    fileName += ".jpg";
                }
                File file = new File(newDir, fileName);
                ImageUtil.saveToFile(image, file.getAbsolutePath());
            }
            if (!"".equals(pe.getImage3()))
            {
                String image = pe.getImage3();
                if (image.indexOf(ImageUtil.getPrefix()) > 0)
                {
                    image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                }
                String fileName = "主图3";
                if (image.lastIndexOf(".") > -1)
                {
                    fileName += image.substring(image.lastIndexOf("."), image.length());
                }
                else
                {
                    fileName += ".jpg";
                }
                File file = new File(newDir, fileName);
                ImageUtil.saveToFile(image, file.getAbsolutePath());
            }
            if (!"".equals(pe.getImage4()))
            {
                String image = pe.getImage4();
                if (image.indexOf(ImageUtil.getPrefix()) > 0)
                {
                    image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                }
                String fileName = "主图4";
                if (image.lastIndexOf(".") > -1)
                {
                    fileName += image.substring(image.lastIndexOf("."), image.length());
                }
                else
                {
                    fileName += ".jpg";
                }
                File file = new File(newDir, fileName);
                ImageUtil.saveToFile(image, file.getAbsolutePath());
            }
            if (!"".equals(pe.getImage5()))
            {
                String image = pe.getImage5();
                if (image.indexOf(ImageUtil.getPrefix()) > 0)
                {
                    image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                }
                String fileName = "主图5";
                if (image.lastIndexOf(".") > -1)
                {
                    fileName += image.substring(image.lastIndexOf("."), image.length());
                }
                else
                {
                    fileName += ".jpg";
                }
                File file = new File(newDir, fileName);
                ImageUtil.saveToFile(image, file.getAbsolutePath());
            }
            ProductCommonEntity pce = productDao.findProductCommonByProductId(productId);
            if (pce != null)
            {
                if (!"".equals(pce.getMediumImage()))
                {
                    String image = pce.getMediumImage();
                    if (image.indexOf(ImageUtil.getPrefix()) > 0)
                    {
                        image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                    }
                    String fileName = "组合特卖图";
                    if (image.lastIndexOf(".") > -1)
                    {
                        fileName += image.substring(image.lastIndexOf("."), image.length());
                    }
                    else
                    {
                        fileName += ".jpg";
                    }
                    File file = new File(newDir, fileName);
                    ImageUtil.saveToFile(image, file.getAbsolutePath());
                }
            }
            Map<String, Object> para = new HashMap<>();
            para.put("productId", productId);
            List<ProductMobileDetailEntity> mobileDetailEntities = productDao.findProductMobileDetailByPara(para);
            for (int i = 0; i < mobileDetailEntities.size(); i++)
            {
                ProductMobileDetailEntity pmde = mobileDetailEntities.get(i);
                if (pmde.getContentType() == 1)
                {
                    String image = pmde.getContent();
                    if (image.indexOf(ImageUtil.getPrefix()) > 0)
                    {
                        image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
                    }
                    String fileName = "详情图" + (i + 1);
                    if (image.lastIndexOf(".") > -1)
                    {
                        fileName += image.substring(image.lastIndexOf("."), image.length());
                    }
                    else
                    {
                        fileName += ".jpg";
                    }
                    File file = new File(newDir, fileName);
                    ImageUtil.saveToFile(image, file.getAbsolutePath());
                }
            }
        }
        
        return newDir.getAbsolutePath();
    }
    
    @Override
    public Map<String, Object> jsonProductStockInfo(Map<String, Object> para)
        throws Exception
    {
        int total = 0;
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, String>> resultList = new ArrayList<>();
        
        String nowTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        para.put("nowTime", nowTime);
        
        int start = Integer.valueOf(para.get("start") + "").intValue();
        int max = Integer.valueOf(para.get("max") + "").intValue();
        int saleProductTotal = productDao.countSaleProductStock(para);
        int mallProductTotal = 0;
        
        resultList = wrapData(productDao.findSaleProductStock(para));
        
        boolean isNeedFindOtherType = false;
        if (resultList.size() == 0)
        {
            start = start - saleProductTotal;
            para.put("start", start);
            isNeedFindOtherType = true;
        }
        else if (resultList.size() < max)
        {
            max = max - (saleProductTotal % max);
            para.put("start", 0);
            para.put("max", max);
            isNeedFindOtherType = true;
        }
        if (isNeedFindOtherType)
        {
            List<Map<String, String>> mallProductInfoList = wrapData(productDao.findMallProductStock(para));
            resultList.addAll(mallProductInfoList);
        }
        mallProductTotal = productDao.countMallProductStock(para);
        total = saleProductTotal + mallProductTotal;
        
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    private List<Map<String, String>> wrapData(List<Map<String, Object>> data)
    {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (Map<String, Object> map : data)
        {
            Map<String, String> item = new HashMap<>();
            int type = Integer.valueOf(map.get("type") + "").intValue();
            String startTimeStr = "";
            String endTimeStr = "";
            String typeStr = "";
            if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                typeStr = "放入商城";
            }
            else if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                startTimeStr = ((Timestamp)map.get("startTime")).toString();
                endTimeStr = ((Timestamp)map.get("endTime")).toString();
                typeStr = "特卖商品";
            }
            String remark = map.get("remark") + "";
            if (!StringUtils.isEmpty(remark) && remark.length() > 15)
            {
                remark = remark.substring(0, 13) + "...";
            }
            
            item.put("baseId", map.get("baseId") + "");
            item.put("id", map.get("id") + "");
            item.put("typeStr", typeStr);
            // item.put("isOffShelves", ((int)map.get("isOffShelvesCode") == 1) ? "已下架" : "出售中");
            item.put("productName", map.get("name") + "");
            item.put("remark", remark);
            item.put("startTime", startTimeStr);
            item.put("endTime", endTimeStr);
            item.put("stock", map.get("stock") + "");
            item.put("lock", map.get("lockNum") + "");
            item.put("availableStock", (Integer.valueOf(map.get("stock") + "") - Integer.valueOf(map.get("lockNum") + "")) + "");
            item.put("salesPrice", map.get("salesPrice") + "");
            item.put("sellerName", map.get("sellerName") + "");
            item.put("sendAddress", map.get("sendAddress") + "");
            resultList.add(item);
        }
        return resultList;
    }
    
    @Override
    public boolean checkProductIsInMall(int productId)
        throws Exception
    {
        List<RelationCategoryAndProductEntity> rcpeList = categoryDao.findThirdCatetoryByProductId(productId);
        if (rcpeList == null || rcpeList.size() == 0)
        {
            return false;
        }
        ProductEntity pe = productDao.findProductByID(productId, ProductEnum.PRODUCT_TYPE.MALL.getCode());
        if (pe == null || pe.getIsShowInMall() == 0)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public List<Map<String, String>> exportStockList(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, String>> resultList = new ArrayList<>();
        
        String nowTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        para.put("nowTime", nowTime);
        
        resultList = wrapData(productDao.findSaleProductStock(para));
        
        List<Map<String, String>> mallProductInfoList = wrapData(productDao.findMallProductStock(para));
        resultList.addAll(mallProductInfoList);
        
        return resultList;
    }
    
    @Override
    public String updateProductRemark(String ids, String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(ids))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要操作的商品");
            return JSON.toJSONString(resultMap);
        }
        if (StringUtils.isEmpty(remark))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入备注");
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", Arrays.asList(ids.split(",")));
        para.put("remark", remark);
        if (productDao.updateProductRemark(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
            return JSON.toJSONString(resultMap);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @Override
    public String deleteProductNewbie(String ids)
        throws Exception
    {
        Map<String, Object> resuMap = new HashMap<>();
        List<String> idList = Arrays.asList(ids.split(","));
        if (productDao.deleteProductNewbie(idList) > 0)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("activitiesStatus", ProductEnum.PRODUCT_ACTIVITY_STATUS.NORMAL.getCode());
            para.put("idList", idList);
            productDao.updateProductByPara(para);
            resuMap.put("status", 1);
            resuMap.put("msg", "删除成功");
        }
        else
        {
            resuMap.put("status", 0);
            resuMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resuMap);
    }
    
    @Override
    public Object findProductNewbieInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", productDao.findAllProductNewbie(para));
        resultMap.put("total", productDao.countProductNewbie(para));
        return resultMap;
    }
    
    @Override
    public String saveOrUpdateProductNewbie(int id, int productId, double salesPrice, int isDisplay)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        ProductEntity pe = productDao.findProductByID(productId, null);
        if (pe == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("Id=%d的商品不存在", productId));
            return JSON.toJSONString(resultMap);
        }
        if (salesPrice > pe.getMarketPrice())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "新人专享价不能大于等于商品原价");
            return JSON.toJSONString(resultMap);
        }
        
        Map<String, Object> para = new HashMap<>();
        para.put("id", productId);
        para.put("salesPrice", salesPrice);
        para.put("activitiesStatus", ProductEnum.PRODUCT_ACTIVITY_STATUS.NEWBIE.getCode());
        if (productDao.updateProductByPara(para) > 0)
        {
            if (productDao.updateProductCommonByPara(para) > 0)
            {
                if (id == 0)
                {
                    Map<String, Object> add = new HashMap<>();
                    add.put("productId", productId);
                    add.put("isDisplay", isDisplay);
                    if (productDao.insertProductNewbie(add) > 0)
                    {
                        resultMap.put("status", 1);
                        resultMap.put("msg", "保存成功");
                    }
                    else
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", "保存失败");
                    }
                }
                else
                {
                    Map<String, Object> update = new HashMap<>();
                    update.put("id", id);
                    update.put("productId", productId);
                    update.put("isDisplay", isDisplay);
                    if (productDao.updateProductNewbie(update) > 0)
                    {
                        resultMap.put("status", 1);
                        resultMap.put("msg", "保存成功");
                    }
                    else
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", "保存失败");
                    }
                }
            }
            else
            {
                throw new ServiceException();
            }
        }
        else
        {
            throw new ServiceException();
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateProductNewbieSequence(int id, int sequence)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("sequence", sequence);
        if (productDao.updateProductNewbie(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateProductNewbieDisplayStatus(int id, int isDisplay)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isDisplay", isDisplay);
        if (productDao.updateProductNewbie(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }

    @Override
    public ResultEntity updateProductActivityWholesalePrice(int id, String startTime, String endTime, float activityWholesalePrice)
        throws Exception
    {
        ProductEntity pe = productDao.findProductByID(id, null);
        if (pe == null)
        {
            return ResultEntity.getFailResult("商品不存在");
        }
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime))
        {
            return ResultEntity.getFailResult("活动生效时间不能为空");
        }
        Date start = CommonUtil.string2Date(startTime, "yyyy-MM-dd HH:mm:ss");
        Date end = CommonUtil.string2Date(endTime, "yyyy-MM-dd HH:mm:ss");
        if (start.after(end))
        {
            return ResultEntity.getFailResult("开始时间不能在结束时间之后");
        }
        if (activityWholesalePrice <= 0)
        {
            return ResultEntity.getFailResult("活动供货价必须大于0");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("activityWholesalePrice", activityWholesalePrice);
        param.put("activityWholesalePriceStartTime", startTime);
        param.put("activityWholesalePriceEndTime", endTime);
        if (productDao.updateProductActivityWholesalePrice(param) > 0)
        {
            StringBuffer before = new StringBuffer();
            before.append("活动供货价：").
                    append(pe.getActivityWholesalePrice()).
                    append("  生效时间：").
                    append(pe.getActivityWholesalePriceStartTime()).
                    append(" 至 ").
                    append(pe.getActivityWholesalePriceEndTime());

            StringBuffer after = new StringBuffer();
            after.append("活动供货价：").
                    append(activityWholesalePrice).
                    append("  生效时间：").
                    append(startTime).
                    append(" 至 ").
                    append(endTime);

            param.clear();
            param.put("productId", id);
            param.put("before", before.toString());
            param.put("after", after.toString());
            param.put("username", commonService.getCurrentRealName());
            productDao.inserProductActivityWholesalePriceLog(param);
            return ResultEntity.getSuccessResult("修改成功");
        }
        else
        {
            return ResultEntity.getFailResult("修改失败");
        }
    }
}
