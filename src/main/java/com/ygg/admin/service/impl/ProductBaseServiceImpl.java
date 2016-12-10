package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.code.ProductBaseEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.CommonService;
import com.ygg.admin.service.ProductBaseService;
import com.ygg.admin.service.ProductCommentService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.ImageUtil;
import org.apache.commons.beanutils.BeanPropertyValueChangeClosure;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service("productBaseService")
public class ProductBaseServiceImpl implements ProductBaseService
{
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private CategoryDao categoryDao;
    
    @Resource
    private RelationDeliverAreaTemplateDao relationDeliverAreaTemplateDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private MwebGroupProductDao mwebGroupProductDao;
    
    @Resource
    private CommonService commonService;

    @Resource
    private ProductCommentService productCommentService;
    
    private Logger logger = Logger.getLogger(ProductBaseServiceImpl.class);
    
    @Override
    public ResultEntity ajaxPageDataProductBase(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> productInfoList = productBaseDao.queryAllProductBaseInfo(para);
        List<Integer> productIds = new ArrayList<>();
        for (Map<String, Object> mp:productInfoList)
        {
            productIds.add(Integer.parseInt(mp.get("productId").toString()));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("productBaseIds", productIds);
        params.put("endTime", Integer.parseInt(DateTime.now().toString("yyyyMMddHH")));
        params.put("startTime", Integer.parseInt(DateTime.now().minusDays(7).toString("yyyyMMddHH")));
        List<Map<String, Object>> salesVolumeIn7 = new ArrayList<>();
        if (productIds.size() > 0)
        {
            salesVolumeIn7.addAll(productBaseDao.findProductBaseSalesVolumeByPara(params));
        }
        params.put("startTime", Integer.parseInt(DateTime.now().minusDays(30).toString("yyyyMMddHH")));
        List<Map<String, Object>> salesVolumeIn30 = new ArrayList<>();
        if (productIds.size() > 0)
        {
            salesVolumeIn30.addAll(productBaseDao.findProductBaseSalesVolumeByPara(params));//30日销量
        }

        Map<String, Object> salesVolumeIn7Map = new HashMap<>();
        for (Map<String, Object> it : salesVolumeIn7) {
            salesVolumeIn7Map.put(it.get("productBaseId").toString(), it.get("salesVolume"));
        }

        Map<String, Object> salesVolumeIn30Map = new HashMap<>();
        for (Map<String, Object> it : salesVolumeIn30) {
            salesVolumeIn30Map.put(it.get("productBaseId").toString(), it.get("salesVolume"));
        }

        for (Map<String, Object> curr : productInfoList)
        {
            int id = Integer.valueOf(curr.get("productId") + "").intValue();
            int sendType = Integer.valueOf(curr.get("sendType") + "");
            curr.put("sendType", SellerEnum.SellerTypeEnum.getDescByCode(sendType));
            if (CommonConstant.COMMON_NO == Integer.parseInt(curr.get("sellerIsAvailable").toString()))
            {
                curr.put("sellerName", curr.get("sellerName") + "<span style=\"color:red\">(已停用)</span>");
            }
            int submitType = Integer.parseInt(curr.get("submitType").toString());
            curr.put("submitType", ProductBaseEnum.SUbMIT_TYPE.getDescByCode(submitType));
            curr.put("submitContent", submitType == ProductBaseEnum.SUbMIT_TYPE.SUPPLY_PRICE.getCode() ? curr.get("wholesalePrice") : submitType == ProductBaseEnum.SUbMIT_TYPE.DISCOUNTED_POINT.getCode() ? curr.get("deduction") + "%" : curr.get("selfPurchasePrice"));
            int freightType = Integer.parseInt(curr.get("freightType") + "");
            String type = "";
            switch (freightType)
            {
                case 1:
                    type = "包邮";
                    break;
                case 2:
                    type = "满" + curr.get("freightMoney") + "包邮";
                    break;
                case 3:
                    type = "不包邮";
                    break;
                case 4:
                    type = curr.get("freightOther") + "";
                    break;
            }
            curr.put("freightType", type);
            curr.put("isAvailable", (Integer.valueOf(curr.get("isAvailable") + "")) == 1 ? "可用" : "不可用");
            List<CategoryEntity> categoryList = categoryDao.findCategoryByProductBaseId(id);
            curr.put("categoryList",categoryList);
            //组装分类名称，给导出用
            StringBuilder categoryName = new StringBuilder();
            for (CategoryEntity ce : categoryList)
            {
                categoryName.append(ce.getCategoryName()).append(";");
            }
            curr.put("categoryName", categoryName.length() > 0 ? categoryName.substring(0, categoryName.length() - 1) : categoryName);
            if (curr.get("expireDate") == null)
            {
                curr.put("expireDate","");
            }
            else {
                curr.put("expireDate", curr.get("expireDate").toString());
            }

            curr.put("goodCommentRate", productCommentService.getProductBaseCommentRateById(id, ProductEnum.PRODUCT_COMMENT_TYPE.GOOD.getCode()));
            curr.put("salesVolumeIn7", salesVolumeIn7Map.get(id + "") == null ? "0" : salesVolumeIn7Map.get(id + ""));
            curr.put("salesVolumeIn30", salesVolumeIn30Map.get(id + "") == null ? "0" : salesVolumeIn30Map.get(id + ""));
            curr.put("saleStatus", getProductBaseSaleStatus(id));
        }
        return ResultEntity.getResultList(productBaseDao.countProductBaseInfo(para), productInfoList);
    }

    private int getProductBaseSaleStatus(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("productBaseId", id);
        List<ProductEntity> pes = productDao.findProductByPara(param);
        if (pes == null || pes.isEmpty())
        {
            return 1;
        }
        else
        {
            //停用、已经下架的商品是否考虑
            for (Iterator<ProductEntity> iterator = pes.iterator(); iterator.hasNext();)
            {
                if (iterator.next().getType() != ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    iterator.remove();
                }
            }
            if (pes.isEmpty())
            {
                //基本商品关联的商品只有商城，怎么算？？？
                return -1;
            }
            else
            {
                return 2;
                /*for (ProductEntity pe:pes)
                {
                    DateTime startTime = DateTime.parse(pe.getStartTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    DateTime endTime = DateTime.parse(pe.getEndTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    if (startTime.isBeforeNow() && endTime.isAfterNow())
                    {
                        //只要有一个商品出售中，则基本商品的状态为出售中
                        return 2;
                    }
                }

                for (ProductEntity pe:pes)
                {
                    DateTime startTime = DateTime.parse(pe.getStartTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    DateTime endTime = DateTime.parse(pe.getEndTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    if (startTime.isBefore(endTime) && startTime.isAfterNow())
                    {
                        //只要有一个商品即将出售，则基本商品的状态为即将出售
                        return 3;
                    }
                }

                for (ProductEntity pe:pes)
                {
                    DateTime startTime = DateTime.parse(pe.getStartTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    DateTime endTime = DateTime.parse(pe.getEndTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
                    if (startTime.isBeforeNow() && endTime.isAfterNow())
                    {
                        //只要有一个商品出售中，则基本商品的状态为出售中
                        return 2;
                    }
                }*/
            }
        }
    }

    @Override
    public ProductBaseEntity queryProductBaseById(int editId)
        throws Exception
    {
        
        return productBaseDao.queryProductBaseById(editId);
    }
    
    @Override
    public int updateProductBase(ProductBaseEntity product, List<Map<String, Object>> mobileDetailsImageList, List<CategoryEntity> categoryList, int saveType)
        throws Exception
    {
        // 更新商品基本信息
        logger.info("开始更新商品信息: ");
        product.setImage1(adjustImageSize(product.getImage1(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage2(adjustImageSize(product.getImage2(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage3(adjustImageSize(product.getImage3(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage4(adjustImageSize(product.getImage4(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage5(adjustImageSize(product.getImage5(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setMediumImage(adjustImageSize(product.getMediumImage(), ImageUtil.getSuffix(ImageTypeEnum.v1brandProduct.ordinal())));
        product.setSmallImage(adjustImageSize(product.getSmallImage(), ImageUtil.getSuffix(ImageTypeEnum.v1cartProduct.ordinal())));
        if (product.getSellerDeliverAreaTemplateId() == 0)
        {
            product.setDeliverAreaType(1);
            product.setDeliverAreaDesc("");
        }
        productBaseDao.updateProductBase(product);
        logger.info("更新商品信息成功: ");
        
        List<ProductMobileDetailEntity> pmdes = new ArrayList<>();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        List<Integer> mobileDetailIds = new ArrayList<Integer>();
        paraMap.put("productId", product.getId());
        byte order = 22;
        for (Map<String, Object> curr : mobileDetailsImageList)
        {
            // 最多允许上传22张
            if (order < 0)
            {
                break;
            }
            ProductBaseMobileDetailEntity entity = new ProductBaseMobileDetailEntity();
            String content = curr.get("content") + "";
            int id = Integer.valueOf(curr.get("id") + "");
            entity.setOrder(order);
            entity.setProductId(product.getId());
            
            if (id == 0)
            {
                entity.setContent(adjustImageSize(content, ImageUtil.getSuffix(ImageTypeEnum.v1detail.ordinal())));
                entity.setContentType((byte)1);
                
                Map<String, Object> propertysMap = ImageUtil.getProperty(entity.getContent());
                entity.setHeight(Short.valueOf((String)propertysMap.get("height")));
                entity.setWidth(Short.valueOf((String)propertysMap.get("width")));
                productBaseDao.saveProductMobileDetail(entity);
                mobileDetailIds.add(entity.getId());
                
            }
            else
            {
                mobileDetailIds.add(id);
                if ("".equals(content))
                {
                    // 删除
                    productBaseDao.deleteProductBaseMobileDetail(id);
                    logger.debug("删除MobileDetail信息成功");
                    
                }
                else
                {
                    // 更新
                    entity.setId(id);
                    
                    entity.setContent(adjustImageSize(content, ImageUtil.getSuffix(ImageTypeEnum.v1detail.ordinal())));
                    entity.setContentType((byte)1);
                    
                    // 根据ID查询一下product_base_mobile_detail，假如类型一致，且宽高也相同，则不再修改宽高
                    ProductBaseMobileDetailEntity detailEntity = productBaseDao.findProductBaseMobileDetailById(id);
                    if ((!entity.getContent().equals(detailEntity.getContent())) || detailEntity.getHeight() <= 1 || detailEntity.getWidth() <= 1)
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
                    productBaseDao.updateProducBasetMobileDetail(entity);
                    logger.debug("更新MobileDetail信息1成功: ");
                }
            }
            order--;
            
            //详情图有修改则同步到特卖/商城商品
            if (saveType == 2 && StringUtils.isNotEmpty(content))
            {
                ProductMobileDetailEntity pmde = new ProductMobileDetailEntity();
                pmde.setOrder(pmde.getOrder());
                pmde.setContent(entity.getContent());
                pmde.setContentType((byte)1);
                pmde.setHeight(entity.getHeight());
                pmde.setWidth(entity.getWidth());
                pmdes.add(pmde);
            }
        }
        if (mobileDetailIds.size() > 0)
        {
            paraMap.put("idList", mobileDetailIds);
            productBaseDao.deleteProductBaseMobileDetailIdInList(paraMap);
        }
        
        // 更新关联基本商品的特卖商品信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("brandId", product.getBrandId());
        map.put("sellerId", product.getSellerId());
        if (StringUtils.isEmpty(product.getCode()))
        {
            throw new Exception("商品编码为空");
        }
        map.put("code", product.getCode());
        map.put("barcode", StringUtils.isEmpty(product.getBarcode()) ? "" : product.getBarcode());
        map.put("netVolume", StringUtils.isEmpty(product.getNetVolume()) ? "" : product.getNetVolume());
        map.put("placeOfOrigin", StringUtils.isEmpty(product.getPlaceOfOrigin()) ? "" : product.getPlaceOfOrigin());
        map.put("manufacturerDate", StringUtils.isEmpty(product.getManufacturerDate()) ? "" : product.getManufacturerDate());
        map.put("storageMethod", StringUtils.isEmpty(product.getStorageMethod()) ? "" : product.getStorageMethod());
        map.put("durabilityPeriod", StringUtils.isEmpty(product.getDurabilityPeriod()) ? "" : product.getDurabilityPeriod());
        map.put("peopleFor", StringUtils.isEmpty(product.getPeopleFor()) ? "" : product.getPeopleFor());
        map.put("foodMethod", StringUtils.isEmpty(product.getFoodMethod()) ? "" : product.getFoodMethod());
        map.put("useMethod", StringUtils.isEmpty(product.getUseMethod()) ? "" : product.getUseMethod());
        map.put("baseId", product.getId());
        if (saveType == 2)
        {
            //同步基本商品主图到特卖和商城商品
            map.put("image1", product.getImage1());
            map.put("image2", product.getImage2());
            map.put("image3", product.getImage3());
            map.put("image4", product.getImage4());
            map.put("image5", product.getImage5());
            productBaseDao.updateProductCommonImage(product.getId(), product.getMediumImage(), product.getSmallImage());
        }
        productBaseDao.updateRelationProductByPara(map);
        
        // 更新关联的左岸城堡信息表
        mwebGroupProductDao.updateRelationProductByPara(map);
        
        //同步基本商品详情图到特卖和商城商品
        if (saveType == 2 && pmdes.size() > 0)
        {
            List<Integer> productIds = productDao.findProductIdsByPid(product.getId());
            for (Integer productId : productIds)
            {
                productDao.deleteProductMobileDetailByProductId(productId);
                BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure("productId", productId);
                CollectionUtils.forAllDo(pmdes, closure);
                productDao.insertProductMobileDetail(pmdes);
            }
        }
        // 更新分类信息
        boolean isUpdateCategory = false;
        List<CategoryEntity> oldCategoryList = categoryDao.findCategoryByProductBaseId(product.getId());
        if (categoryList.size() != oldCategoryList.size())
        {
            isUpdateCategory = true;
        }
        else
        {
            for (int i = 0; i < categoryList.size(); i++)
            {
                for (int j = 0; j < oldCategoryList.size(); j++)
                {
                    if (!categoryList.get(i).equals(oldCategoryList.get(j)))
                    {
                        isUpdateCategory = true;
                        break;
                    }
                }
                if (isUpdateCategory)
                {
                    break;
                }
                
            }
        }
        if (isUpdateCategory)
        {
            List<Integer> categoryIdList = new ArrayList<Integer>();
            for (CategoryEntity category : categoryList)
            {
                category.setProductBaseId(product.getId());
                if (category.getId() == 0)
                {
                    boolean isExist = categoryDao.checkProductBaseCategoryIsExist(category);
                    if (!isExist)
                    {
                        categoryDao.insertCategory(category);
                    }
                }
                else
                {
                    categoryDao.updateCategory(category);
                }
                categoryIdList.add(category.getId());
            }
            if (categoryIdList.size() > 0)
            {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("productBaseId", product.getId());
                mp.put("idList", categoryIdList);
                categoryDao.deleteCategory(mp);
            }
            
            // 更新关联的商品分类信息
            List<Integer> productIdList = productBaseDao.findProductIdByProductBaseId(product.getId(), ProductEnum.PRODUCT_TYPE.MALL.getCode());
            Map<String, Object> mp = new HashMap<String, Object>();
            mp.put("productBaseId", product.getId());
            categoryDao.deleteRelationCategoryAndProduct(mp);
            
            List<Map<String, Object>> productCategoryList = new ArrayList<Map<String, Object>>();
            for (Integer productId : productIdList)
            {
                for (CategoryEntity ce : categoryList)
                {
                    if (ce.getCategoryThirdId() != 0)
                    {
                        ProductEntity pe = productDao.findProductByID(productId, ProductEnum.PRODUCT_TYPE.MALL.getCode());
                        if (pe != null && pe.getIsShowInMall() == 1)
                        {
                            Map<String, Object> caeMap = new HashMap<String, Object>();
                            caeMap.put("productBaseId", product.getId());
                            caeMap.put("productId", productId);
                            caeMap.put("categoryThirdId", ce.getCategoryThirdId());
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
        
        // 更新配送地区
        List<Integer> productIdList = new ArrayList<Integer>();
        productIdList.add(product.getId());
        productBaseDao.deleteRelationProductBaseDeliverArea(productIdList);
        int templateId = product.getSellerDeliverAreaTemplateId();
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("sellerDeliverAreaTemplateId", templateId);
        
        mp.put("isExcept", 0);
        List<RelationDeliverAreaTemplateEntity> areaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(mp);
        
        mp.put("isExcept", 1);
        List<RelationDeliverAreaTemplateEntity> exceptAreaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(mp);
        List<RelationDeliverAreaTemplateEntity> allAreas = filterExceptArea(areaList, exceptAreaList);
        List<RelationProductBaseDeliverAreaEntity> rsdaeList = new ArrayList<RelationProductBaseDeliverAreaEntity>();
        
        copyProperties(allAreas, rsdaeList, product.getId());
        if (!rsdaeList.isEmpty())
        {
            productBaseDao.insertRelationProductBaseDeliverArea(rsdaeList);
        }
        
        return 1;
    }
    
    @Override
    public int saveProductBase(ProductBaseEntity product, List<Map<String, Object>> mobileDetailsImageList, List<CategoryEntity> categoryList)
        throws Exception
    {
        // 插入商品
        logger.debug("开始插入商品基本信息: ");
        product.setImage1(adjustImageSize(product.getImage1(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage2(adjustImageSize(product.getImage2(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage3(adjustImageSize(product.getImage3(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage4(adjustImageSize(product.getImage4(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setImage5(adjustImageSize(product.getImage5(), ImageUtil.getSuffix(ImageTypeEnum.v1product.ordinal())));
        product.setMediumImage(adjustImageSize(product.getMediumImage(), ImageUtil.getSuffix(ImageTypeEnum.v1brandProduct.ordinal())));
        product.setSmallImage(adjustImageSize(product.getSmallImage(), ImageUtil.getSuffix(ImageTypeEnum.v1cartProduct.ordinal())));
        product.setAvailableStock(product.getTotalStock());
        productBaseDao.saveProductBase(product);
        
        logger.debug("插入商品基本信息成功: ");
        
        // 插入ProductBaseMobileDetailEntity
        byte order = 22;
        for (Map<String, Object> map : mobileDetailsImageList)
        {
            // 图片最多允许上传22张
            if (order < 0)
            {
                break;
            }
            ProductBaseMobileDetailEntity entity = new ProductBaseMobileDetailEntity();
            entity.setProductId(product.getId());
            entity.setContent(adjustImageSize(map.get("content") + "", ImageUtil.getSuffix(ImageTypeEnum.v1detail.ordinal())));
            entity.setContentType((byte)1);
            Map<String, Object> propertysMap = ImageUtil.getProperty(entity.getContent());
            entity.setHeight(Short.valueOf((String)propertysMap.get("height")));
            entity.setWidth(Short.valueOf((String)propertysMap.get("width")));
            entity.setOrder(order);
            productBaseDao.saveProductMobileDetail(entity);
            logger.debug("插入ProductBaseMobileDetail成功: ");
            order--;
        }
        
        // 插入分类信息
        for (CategoryEntity category : categoryList)
        {
            category.setProductBaseId(product.getId());
            categoryDao.insertCategory(category);
        }
        
        // 插入配送地区
        /*
         * List<RelationProductBaseDeliverAreaEntity> rsdaeList =
         * (ArrayList<RelationProductBaseDeliverAreaEntity>)para.get("rsdaeList"); for
         * (RelationProductBaseDeliverAreaEntity rpbae : rsdaeList) { rpbae.setProductBaseId(product.getId());
         * productBaseDao.insertRelationProductBaseDeliverArea(rpbae); }
         */
        
        // 设置配送地区
        if (product.getSellerDeliverAreaTemplateId() > 0)
        {
            int templateId = product.getSellerDeliverAreaTemplateId();
            Map<String, Object> mp = new HashMap<String, Object>();
            mp.put("sellerDeliverAreaTemplateId", templateId);
            
            mp.put("isExcept", 0);
            List<RelationDeliverAreaTemplateEntity> areaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(mp);
            
            mp.put("isExcept", 1);
            List<RelationDeliverAreaTemplateEntity> exceptAreaList = relationDeliverAreaTemplateDao.findRelationDeliverAreaTemplateByPara(mp);
            List<RelationDeliverAreaTemplateEntity> allAreas = filterExceptArea(areaList, exceptAreaList);
            
            List<RelationProductBaseDeliverAreaEntity> rsdaeList = new ArrayList<RelationProductBaseDeliverAreaEntity>();
            
            copyProperties(allAreas, rsdaeList, product.getId());
            if (!rsdaeList.isEmpty())
            {
                productBaseDao.insertRelationProductBaseDeliverArea(rsdaeList);
            }
        }
        return 1;
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
            //防止用户选如下：浙江省-杭州市-全部区、浙江省-杭州市-上城区
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
    
    /**
     * 修改所需图片的尺寸
     * 
     * @param imageUrl
     * @param postfix
     * @return
     */
    private String adjustImageSize(String imageUrl, String postfix)
    {
    	return imageUrl;
//        if (imageUrl == null || "".equals(imageUrl))
//        {
//            return "";
//        }
//        return (imageUrl.indexOf("!") > 0) ? imageUrl : (imageUrl + "!" + postfix);
    }
    
    @Override
    public List<ProductBaseMobileDetailEntity> findProductBaseMobileDetailsByProductBaseId(int id)
        throws Exception
    {
        return productBaseDao.findProductBaseMobileDetailsByProductBaseId(id);
    }
    
    /**
     * 复制商品
     */
    @Override
    public ResultEntity copyProduct(int id, String code)
        throws Exception
    {
        
        ProductBaseEntity src = productBaseDao.queryProductBaseById(id);
        if (src == null)
        {
            return ResultEntity.getFailResult(String.format("ID=%d的基本商品不存在", id));
        }
        
        if (StringUtils.isEmpty(code))
        {
            return ResultEntity.getFailResult("商品编码不能为空");
        }
        
        //判断商品同一个商家是否存在相同编码，因为之前的老数据存在较多重复，故不能用建唯一约束
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("sellerId", src.getSellerId());
        param.put("code", code);
        List<Integer> idList = productBaseDao.checkCodeAndBarCode(param);
        if (!idList.isEmpty())
        {
            return ResultEntity.getFailResult(String.format("编码=%s的商品已经存在", code));
        }
        
        if (code.indexOf("%") > -1)
        {
            String suffix = code.substring(code.lastIndexOf("%") + 1);
            if (!suffix.matches("^\\d+$"))
            {
                return ResultEntity.getFailResult("商品编码含有%时，%后面必须是数字");
            }
        }
        
        logger.info(String.format("开始从基本商品Id=%d复制信息...>>>", id));
        
        //复制基本信息
        src.setId(0);
        src.setCode(code);
        src.setRemark("从商品:" + id + " 复制而来");
        src.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        src.setTotalStock(0);
        src.setSaleStock(0);
        src.setMallStock(0);
        src.setDistributionStock(0);
        src.setAvailableStock(0);
        productBaseDao.saveProductBase(src);
        
        //复制详情图
        List<ProductBaseMobileDetailEntity> mobileList = productBaseDao.findProductBaseMobileDetailsByProductBaseId(id);
        for (ProductBaseMobileDetailEntity mobile : mobileList)
        {
            mobile.setProductId(src.getId());
            productBaseDao.saveProductMobileDetail(mobile);
        }
        logger.info(String.format("从基本商品Id=%d复制详情图成功... ", id));
        
        //复制分类信息
        List<CategoryEntity> categoryList = categoryDao.findCategoryByProductBaseId(id);
        Set<CategoryEntity> categorySet = new HashSet<CategoryEntity>(categoryList);
        for (CategoryEntity category : categorySet)
        {
            category.setProductBaseId(src.getId());
            categoryDao.insertCategory(category);
        }
        logger.info(String.format("从基本商品Id=%d复制分类信息成功...", id));
        
        logger.info(String.format("从基本商品Id=%d复制成功，复制后的商品Id=%d，复制后的商品编码=%s ", id, src.getId(), code));
        return ResultEntity.getSuccessResult(src.getId());
    }
    
    @Override
    public List<ProductBaseEntity> queryAllProductBase(Map<String, Object> para)
        throws Exception
    {
        List<ProductBaseEntity> list = productBaseDao.queryAllProductBase(para);
        return list == null ? new ArrayList<ProductBaseEntity>() : list;
    }
    
    @Override
    public int forAvailable(Map<String, Object> para)
        throws Exception
    {
        return productBaseDao.forAvailable(para);
    }
    
    @Override
    public List<Map<String, Object>> querySaleProductInfoByBaseId(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        List<Map<String, Object>> resultList = productBaseDao.querySaleProductInfoByBaseId(para);
        if (resultList != null)
        {
            for (Map<String, Object> map : resultList)
            {
                map.put("type", "特卖商品");
            }
        }
        else
        {
            resultList = new ArrayList<Map<String, Object>>();
        }
        return resultList;
    }
    
    @Override
    public String adjustStock(int productBaseId, int productId, int productType, int changeStock)
        throws Exception
    {
        ProductBaseEntity base = productBaseDao.findProductBaseByIdForUpdate(productBaseId);
        if (base == null)
        {
            return JSON.toJSONString(ResultEntity.getFailResult("基本商品不存在"));
        }
        if (base.getAvailableStock() - changeStock < 0)
        {
            return JSON.toJSONString(ResultEntity.getFailResult("基本商品可用库存不足"));
        }
        
        ProductCountEntity pce = productDao.findProductCountByProductId(productId);
        if (pce == null)
        {
            return JSON.toJSONString(ResultEntity.getFailResult(ProductEnum.PRODUCT_TYPE.getDescByCode(productType) + "不存在"));
        }
        if (pce.getStock() + changeStock < pce.getLock())
        {
            return JSON.toJSONString(ResultEntity.getFailResult("调整的库存数量必须大于或等于可用库存"));
        }
        
        //先调商品库存，再条基本商品库存，productType=1需要更新productBase中的sale_stock,productType=2需要更新productBase中的mall_stock
        if (productDao.updateProductStock(productId, pce.getStock(), changeStock) > 0)
        {
            int status = 0;
            Map<String, Object> para = new HashMap<>();
            para.put("changeStock", changeStock);
            para.put("oldAvailableStock", base.getAvailableStock());
            para.put("id", productBaseId);
            if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                status = productBaseDao.adjustSaleStock(para);
            }
            else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                status = productBaseDao.adjustMallStock(para);
            }
            if (status > 0)
            {
                return JSON.toJSONString(ResultEntity.getSuccessResult());
            }
            else
            {
                return JSON.toJSONString(ResultEntity.getFailResult("库存调整失败，请稍后再试"));
            }
        }
        else
        {
            return JSON.toJSONString(ResultEntity.getFailResult("库存调整失败，请稍后再试"));
        }
    }
    
    @Override
    public int addTotalStock(Map<String, Object> map)
        throws Exception
    {
        return productBaseDao.addTotalStock(map);
    }
    
    @Override
    public int findMaxProductId()
        throws Exception
    {
        
        int id = productBaseDao.findMaxProductId();
        return id + 1;
    }
    
    @Override
    public List<Integer> checkCodeAndBarCode(int sellerId, String code)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerId", sellerId);
        para.put("code", code);
        return productBaseDao.checkCodeAndBarCode(para);
    }
    
    @Override
    public Map<String, Object> findJsonProductInfoBybaseId(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> reList = productBaseDao.querySaleProductInfoByBaseId(para);
        int total = 0;
        
        for (Map<String, Object> map : reList)
        {
            map.put("type", (int)map.get("typeCode") == 1 ? "特卖商品" : "商城商品");
            map.put("available", Integer.valueOf(map.get("stock") + "").intValue() - Integer.valueOf(map.get("lock") + "").intValue());
            map.put("isAvailable", (int)map.get("isAvailable") == 1 ? "可用" : "停用");
            if ((int)map.get("typeCode") == 1)
            {
                map.put("time", (Timestamp)map.get("startTime") + "~" + (Timestamp)map.get("endTime"));
            }
            else
            {
                map.put("time", "-");
            }
        }
        total = productBaseDao.countSaleProductInfoByBaseId(para);
        resultMap.put("rows", reList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int checkIsInUse(int id)
        throws Exception
    {
        return productBaseDao.checkIsInUse(id);
    }
    
    @Override
    public int checkBatchUpdateProductCostPrice(String productBaseId, String submitType, String wholesalePrice, String deduction, String proposalPrice, String selfPurchasePrice,
        List<Map<String, Object>> simulationList)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int status = 0;
        String msg = "成功";
        
        ProductBaseEntity pb = null;
        if (StringUtils.isNumeric(productBaseId))
        {
            pb = productBaseDao.queryProductBaseById(Integer.parseInt(productBaseId));
        }
        if (pb == null)
        {
            msg = "商品id不存在";
        }
        else if (!"供货价".equals(submitType) && !"扣点".equals(submitType) && !"自营采购价".equals(submitType))
        {
            msg = "结算类型不存在";
        }
        else if ("扣点".equals(submitType) && !Pattern.matches("[0-9]+(\\.?)[0-9]*", deduction))
        {
            msg = "扣点不正确";
        }
        else if ("扣点".equals(submitType) && !Pattern.matches("[0-9]+(\\.?)[0-9]*", proposalPrice))
        {
            msg = "建议价不正确";
        }
        else if ("供货价".equals(submitType) && !Pattern.matches("[0-9]+(\\.?)[0-9]*", wholesalePrice))
        {
            msg = "供货价不正确";
        }
        else if ("自营采购价".equals(submitType) && !Pattern.matches("[0-9]+(\\.?)[0-9]*", selfPurchasePrice))
        {
            msg = "采购价不正确";
        }
        else
        {
            status = 1;
            // 覆盖or新增
            if (pb.getSubmitType() == 1 && pb.getWholesalePrice() == 0)
            {
                msg = "新增";
            }
            else
            {
                msg = "覆盖";
            }
        }
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("msg", msg);
        result.put("productBaseId", productBaseId);
        result.put("submitType", submitType);
        result.put("wholesalePrice", wholesalePrice);
        result.put("deduction", deduction);
        result.put("proposalPrice", proposalPrice);
        result.put("selfPurchasePrice", selfPurchasePrice);
        simulationList.add(result);
        return status;
    }
    
    @Override
    public int saveBatchUpdateProductCostPrice(String productBaseId, String submitType, String wholesalePrice, String deduction, String proposalPrice, String selfPurchasePrice,
        List<Map<String, Object>> confirmList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", productBaseId);
        if ("扣点".equals(submitType))
        {
            para.put("submitType", 2);
            para.put("deduction", deduction);
            para.put("proposalPrice", proposalPrice);
        }
        else if ("供货价".equals(submitType))
        {
            para.put("submitType", 1);
            para.put("wholesalePrice", wholesalePrice);
        }
        else if ("自营采购价".equals(submitType))
        {
            para.put("submitType", 3);
            para.put("selfPurchasePrice", selfPurchasePrice);
        }
        int status = productBaseDao.updateProductBaseCost(para);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("msg", "");
        result.put("productBaseId", productBaseId);
        result.put("submitType", submitType);
        result.put("wholesalePrice", wholesalePrice);
        result.put("deduction", deduction);
        result.put("proposalPrice", proposalPrice);
        result.put("selfPurchasePrice", selfPurchasePrice);
        confirmList.add(result);
        return status;
    }
    
    @Override
    public List<RelationProductBaseDeliverAreaEntity> findRelationSellerDeliverAreaByProductBaseId(int id)
        throws Exception
    {
        return productBaseDao.findRelationSellerDeliverAreaByProductBaseId(id);
    }
    
    @Override
    public Map<String, Object> jsonQualityPromiseInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> reList = productBaseDao.findAllQualityPromise(para);
        int total = 0;
        for (Map<String, Object> map : reList)
        {
            int type = Integer.valueOf(map.get("type") + "").intValue();
            map.put("typeDesc", ProductEnum.QUALITY_PROMISE_TYPE.getDescByCode(type));
        }
        total = productBaseDao.countQualityPromise(para);
        resultMap.put("rows", reList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateQualityPromise(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        /*
         * Map<String, Object> imageMap = ImageUtil.getProperty(para.get("image") + ""); int width =
         * Integer.valueOf(imageMap.get("width") + "").intValue(); int height = Integer.valueOf(imageMap.get("height") +
         * "").intValue(); if (width != 333 && height != 333) { return 2; }
         */
        boolean isExist = productBaseDao.IsExistQualityPromise(para);
        if (isExist)
        {
            return 3;
        }
        if (id == 0)
        {
            return productBaseDao.insertQualityPromise(para);
        }
        else
        {
            return productBaseDao.updateQualityPromise(para);
        }
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
            // 检查基本商品分类是否存在
            boolean isExist = categoryDao.checkProductBaseCategoryIsExist(ce);
            if (isExist)
            {
                continue;
            }
            else
            {
                // 插入基本商品分了信息
                categoryDao.insertCategory(ce);
                if (ce.getCategoryThirdId() != 0)// 有三级分类，则对关联的商城商品进行分类
                {
                    List<Map<String, Object>> rcpMap = new ArrayList<Map<String, Object>>();
                    List<Integer> productIdList = productBaseDao.findProductIdByProductBaseId(ce.getProductBaseId(), ProductEnum.PRODUCT_TYPE.MALL.getCode());
                    for (Integer productId : productIdList)
                    {
                        Map<String, Object> para = new HashMap<String, Object>();
                        para.put("productBaseId", ce.getProductBaseId());
                        para.put("productId", productId);
                        para.put("categoryThirdId", ce.getCategoryThirdId());
                        boolean exist = categoryDao.checkProductCategoryIsExist(para);
                        if (exist)
                        {
                            continue;
                        }
                        else
                        {
                            ProductEntity pe = productDao.findProductByID(productId, ProductEnum.PRODUCT_TYPE.MALL.getCode());
                            if (pe != null && pe.getIsShowInMall() == 1)
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
        }
        return 1;
    }
    
    @Override
    public Map<String, Object> findAllExpireProduct(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> productInfoList = productBaseDao.findAllExpireProduct(para);
        int total = 0;
        if (productInfoList.size() > 0)
        {
            for (Map<String, Object> map : productInfoList)
            {
                int remainDay = Integer.parseInt(map.get("remainDay") == null ? "0" : map.get("remainDay") + "");
                if (remainDay < 0)
                {
                    map.put("remainDay", "已过期" + (-remainDay) + "天");
                }
                map.put("expireDate", map.get("expireDate").toString());
            }
            total = productBaseDao.countAllExpireProduct(para);
        }
        resultMap.put("rows", productInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int findAllottedStockById(int productBaseId)
        throws Exception
    {
        return productBaseDao.findAllottedStockById(productBaseId);
    }
    
    @Override
    public List<Map<String, Object>> findProductBaseRelationInfoByProductBaseId(List<Integer> productBaseIds)
        throws Exception
    {
        return productBaseDao.findProductBaseRelationInfoByProductBaseId(productBaseIds);
    }
    
    @Override
    public int insertWholesalePriceUpdatelog(int id, float oldPrice, float newPrice)
        throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("productBaseId", id);
        params.put("oldPrice", oldPrice);
        params.put("newPrice", newPrice);
        params.put("username", commonService.getCurrentRealName());
        return productBaseDao.insertWholesalePriceUpdatelog(params);
    }
    
    @Override
    public ResultEntity jsonWholeSalePriceHistory(Map<String, Object> para)
        throws Exception
    {
        return ResultEntity.getResultList(productBaseDao.countWholeSalePriceLogByPara(para), productBaseDao.findWholeSalePriceLogByPara(para));
    }
    
    @Override
    public List<Map<String, Object>> findProductBaseIdBySellerProductId(List<Object> sellerProductIds)
        throws Exception 
    {
        return productBaseDao.findProductBaseIdBySellerProductId(sellerProductIds);
    }

    @Override
    public Object previewPicture(int id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", 1);
        ProductBaseEntity pbe = productBaseDao.queryProductBaseById(id);
        if (pbe == null) {
            resultMap.put("status", 0);
            return resultMap;
        }

        List<Map<String, Object>> mainImages = new ArrayList<>();
        if (StringUtils.isNotEmpty(pbe.getImage1())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getImage1());
            map.put("url", pbe.getImage1());
            mainImages.add(map);
        }
        if (StringUtils.isNotEmpty(pbe.getImage2())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getImage2());
            map.put("url", pbe.getImage2());
            mainImages.add(map);
        }
        if (StringUtils.isNotEmpty(pbe.getImage3())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getImage3());
            map.put("url", pbe.getImage3());
            mainImages.add(map);
        }
        if (StringUtils.isNotEmpty(pbe.getImage4())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getImage4());
            map.put("url", pbe.getImage4());
            mainImages.add(map);
        }
        if (StringUtils.isNotEmpty(pbe.getImage5())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getImage5());
            map.put("url", pbe.getImage5());
            mainImages.add(map);
        }
        /*if (StringUtils.isNotEmpty(pbe.getSmallImage())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getSmallImage());
            map.put("url", pbe.getSmallImage());
            mainImages.add(map);
        }
        if (StringUtils.isNotEmpty(pbe.getMediumImage())) {
            Map<String, Object> map = ImageUtil.getProperty(pbe.getMediumImage());
            map.put("url", pbe.getMediumImage());
            mainImages.add(map);
        }*/

        List<Map<String, Object>> detailImages = new ArrayList<>();
        List<ProductBaseMobileDetailEntity> mobileDetails = productBaseDao.findProductBaseMobileDetailsByProductBaseId(id);
        for (ProductBaseMobileDetailEntity mobile : mobileDetails) {
            Map<String, Object> map = new HashMap<>();
            map.put("url", mobile.getContent());
            map.put("width", mobile.getWidth());
            map.put("height", mobile.getHeight());
            detailImages.add(map);
        }
        resultMap.put("mainImages", mainImages);
        resultMap.put("detailImages", detailImages);
        return resultMap;
    }


    @Override
    public Map<String, Object> findHistorySalesVolumeById(int id, String startTime, String endTime)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("productBaseId", id);
        if (StringUtils.isNotEmpty(startTime))
        {
            para.put("startTime", DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).toString("yyyyMMddHH"));
        }
        if (StringUtils.isNotEmpty(endTime))
        {
            para.put("endTime", DateTime.parse(endTime, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).toString("yyyyMMddHH"));
        }
        
        int totalSales = 0;
        List<Map<String, Object>> salesVolumes = productBaseDao.findProductBaseHistorySalesVolume(para);
        TreeMap<Integer, Integer> groupByDay = new TreeMap<>();
        for (Map<String, Object> it : salesVolumes)
        {
            Integer day = Integer.parseInt(it.get("sales_day_hour").toString().substring(0, 8));
            int salesVolume = Integer.parseInt(it.get("sales_volume").toString());
            if (groupByDay.get(day) == null)
            {
                groupByDay.put(day, salesVolume);
            }
            else
            {
                groupByDay.put(day, groupByDay.get(day) + salesVolume);
            }
            totalSales += salesVolume;
        }
        
        List<String> labels = new ArrayList<>();
        for (Integer day : groupByDay.keySet())
        {
            labels.add(DateTime.parse(day + "", DateTimeFormat.forPattern("yyyyMMdd")).toString("yyyy年MM月dd日"));
        }
        if (labels.isEmpty())
        {
            DateTime start = DateTime.now().minusDays(30);
            DateTime end = DateTime.now();
            while (end.isAfter(start))
            {
                labels.add(start.toString("yyyy年MM月dd日"));
                start = start.plusDays(1);
            }
        }
        List<Integer> data = new ArrayList<>(groupByDay.values());
        Map<String, Object> result = new HashMap<>();
        result.put("productName", productBaseDao.queryProductBaseById(id).getName());
        result.put("labels", labels);
        result.put("data", data);
        result.put("totalSales", totalSales);
        result.put("begin", labels.get(0));
        result.put("end", labels.get(labels.size() - 1));
        return result;
    }
}
