package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ProductBaseEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.*;
import com.ygg.admin.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/productBase")
public class ProductBaseController
{
    Logger log = Logger.getLogger(ProductBaseController.class);
    
    @Resource
    private SellerService sellerService;
    
    @Resource
    private BrandService brandService;
    
    @Resource
    private ProductBaseService productBaseService;
 
    @Resource
    private ProductService productService;
    
    @Resource
    private CategoryService categoryService;
    
    @Resource
    private GegeImageService geGeImageService;
    
    @Resource
    private SystemLogService logService;
    
    @Resource
    private AreaService areaService;
    
    @Resource
    private PageCustomService pageCustomService;
    
    @Resource
    private SaleFlagService saleFlagService;
    
    @Resource
    private SellerDeliverAreaService sellerDeliverAreaService;
    
    /**
     * 可用商品管理列表
     * 
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("productBase/list");
            mv.addObject("isAvailable", isAvailable);
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", 1);
            // 一级分类
            List<CategoryFirstEntity> catetgoryFirstList = categoryService.findAllCategoryFirst(para);
            mv.addObject("catetgoryFirstList", catetgoryFirstList);
            
            // 二级分类
            List<CategorySecondEntity> categorySecondList = categoryService.findAllCategorySecond(para);
            mv.addObject("categorySecondList", categorySecondList);
            
            //三级分类
            List<CategoryThirdEntity> categoryThirdList = categoryService.findAllCategoryThird(para);
            mv.addObject("categoryThirdList", categoryThirdList);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }


    /**
     * 异步加载基本商品列表
     * @param page：页
     * @param rows：页大小
     * @param productId：基本商品id
     * @param code：基本商品编码
     * @param barCode：基本商品条码
     * @param name：基本商品名称
     * @param firstCategory：一级分类
     * @param secondCategory：二级分类
     * @param sellerId：商家id
     * @param brandId：品牌id
     * @param isAvailable:是否可用，1是，0否
     * @param remark：备注
     * @return
     */
    @RequestMapping(value = "/jsonProductBaseInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object jsonProductBaseInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "productId", required = false, defaultValue = "-1") int productId, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, //
        @RequestParam(value = "barCode", required = false, defaultValue = "") String barCode, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String name,
        @RequestParam(value = "firstCategory", required = false, defaultValue = "-1") int firstCategory,
        @RequestParam(value = "secondCategory", required = false, defaultValue = "-1") int secondCategory,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    {
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (productId != -1)
            {
                para.put("productId", productId);
            }
            if (!"".equals(code))
            {
                //商品编码用like查询，部分编码中包含%，需要转义
                if (code.indexOf("%") > -1)
                {
                    code = code.replace("%", "\\%");
                }
                para.put("likecode", "%" + code + "%");
            }
            if (!"".equals(barCode))
            {
                para.put("barCode", barCode);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            
            if (firstCategory != -1)
            {
                if (firstCategory == 0)
                {
                    para.put("noCategory", 1);
                }
                else
                {
                    para.put("firstCategory", firstCategory);
                }
            }
            if (secondCategory != -1)
            {
                if (secondCategory == 0)
                {
                    para.put("noCategory", 1);
                }
                else
                {
                    para.put("secondCategory", secondCategory);
                }
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            if (!"".equals(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            return productBaseService.ajaxPageDataProductBase(para);
        }
        catch (Exception e)
        {
            log.error("异步加载基本商品列表出错, " + e.getMessage(), e);
            return ResultEntity.getFailResultList();
        }
    }
    
    /**
     * 转发到商品添加页面
     * 
     * @param request
     * @return
     */
    @RequestMapping("/toAddOrUpdate")
    public ModelAndView add(HttpServletRequest request, @RequestParam(value = "id", required = false, defaultValue = "-1") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productBase/update");
        
        // 卖家信息
        List<SellerEntity> sEntities = sellerService.findSellerIsAvailable();
        mv.addObject("sellerList", sEntities);
        
        // 品牌信息
        List<BrandEntity> bEntities = brandService.findBrandIsAvailable();
        mv.addObject("brandList", bEntities);
        
        ProductBaseEntity product = productBaseService.queryProductBaseById(id);
        if (product == null)
        {
            product = new ProductBaseEntity();
        }
        mv.addObject("canEdit", productBaseService.checkIsInUse(id));
        mv.addObject("product", product);
        mv.addObject("wholesalePrice", product.getWholesalePrice() == 0 ? "" : product.getWholesalePrice() + "");
        mv.addObject("deduction", product.getDeduction() == 0 ? "" : product.getDeduction() + "");
        mv.addObject("proposalPrice", product.getProposalPrice() == 0 ? "" : product.getProposalPrice() + "");
        mv.addObject("totalStock", product.getTotalStock() == 0 ? "" : product.getTotalStock() + "");
        mv.addObject("availableStock", product.getAvailableStock() == 0 ? "" : product.getAvailableStock() + "");
        mv.addObject("selfPurchasePrice", product.getSelfPurchasePrice() == 0 ? "" : product.getSelfPurchasePrice() + "");
        mv.addObject("proposalMarketPrice", product.getProposalMarketPrice() == 0 ? "" : product.getProposalMarketPrice() + "");
        mv.addObject("proposalSalesPrice", product.getProposalSalesPrice() == 0 ? "" : product.getProposalSalesPrice() + "");
        
        SellerEntity seller = sellerService.findSellerById(product.getSellerId());
        if (seller == null)
        {
            seller = new SellerEntity();
        }
        seller.setSendTimeRemark(SellerEnum.SellerSendTimeTypeEnum.getDescByCode(seller.getSendTimeType()));
        
        String sendCodeRemark = seller.getSendCodeType() == 4 ? seller.getSendCodeRemark() : SellerEnum.SellerSendCodeTypeEnum.getDescByCode(seller.getSendCodeType());
        if (seller.getSendCodeType() == SellerEnum.SellerSendCodeTypeEnum.PRODUCTBARCODE.getCode())
        {
            sendCodeRemark += "<font color='red'>(所选商家发货类型为按商品条码发货，商品编码自动替换为商品条码)</font>";
        }
        seller.setSendCodeRemark(sendCodeRemark);
        seller.setSendWeekendRemark(SellerEnum.WeekendSendTypeEnum.getDescByCode(seller.getIsSendWeekend()));
        mv.addObject("seller", seller);
        
        //商品分类信息
        List<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>();
        
        List<CategoryEntity> ceList = categoryService.findCategoryByProductBaseId(product.getId());
        for (CategoryEntity ce : ceList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", ce.getId() + "");
            map.put("categoryFirstId", ce.getCategoryFirstId());
            
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", 1);
            map.put("catetgoryFirstList", categoryService.findAllCategoryFirst(para));
            
            para.put("categoryFirstId", ce.getCategoryFirstId());
            map.put("categorySecondId", ce.getCategorySecondId());
            map.put("categorySecondList", categoryService.findAllCategorySecond(para));
            
            para.put("categorySecondId", ce.getCategorySecondId());
            map.put("categoryThirdId", ce.getCategoryThirdId());
            map.put("categoryThirdList", categoryService.findAllCategoryThird(para));
            
            categoryList.add(map);
        }
        mv.addObject("categoryList", categoryList);
        if (categoryList.size() == 0)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", 1);
            // 一级分类
            List<CategoryFirstEntity> catetgoryFirstList = categoryService.findAllCategoryFirst(para);
            mv.addObject("catetgoryFirstList", catetgoryFirstList);
            
            // 二级分类
            List<CategorySecondEntity> categorySecondList = categoryService.findAllCategorySecond(para);
            mv.addObject("categorySecondList", categorySecondList);
            
            //三级分类
            List<CategoryThirdEntity> categoryThirdList = categoryService.findAllCategoryThird(para);
            mv.addObject("categoryThirdList", categoryThirdList);
        }
        
        // 格格头像
        List<GegeImageEntity> imageList = geGeImageService.findAllGegeImage("product");
        mv.addObject("gegeImageList", imageList);
        
        GegeImageEntity geGeImageEntity = geGeImageService.findGegeImageById(product.getGegeImageId(), "product");
        if (geGeImageEntity == null)
        {
            geGeImageEntity = new GegeImageEntity();
        }
        if (geGeImageEntity.getImage() == null)
        {
            geGeImageEntity.setImage("http://www.cilu.com.cn/climg/LOGO/logo_01.jpg");
        }
        mv.addObject("geGeImageEntity", geGeImageEntity);
        
        // ProductBaseMobileDetails
        List<ProductBaseMobileDetailEntity> mobileDetails = productBaseService.findProductBaseMobileDetailsByProductBaseId(id);
        mv.addObject("mobileDetails", mobileDetails);
        
        // 发货地区
        /*
         * List<Map<String, Object>> areasList = new ArrayList<Map<String, Object>>();
         * List<RelationProductBaseDeliverAreaEntity> rpbaeList =
         * productBaseService.findRelationSellerDeliverAreaByProductBaseId(id); for
         * (RelationProductBaseDeliverAreaEntity rpbae : rpbaeList) { Map<String, Object> map = new HashMap<String,
         * Object>(); map.put("id", rpbae.getId() + ""); map.put("provinceCode", rpbae.getProvinceCode());
         * map.put("provinceList", areaService.allProvince());
         * 
         * Map<String, Object> searchMap = new HashMap<String, Object>();
         * 
         * map.put("cityCode", rpbae.getCityCode()); if (rpbae.getProvinceCode() == 1) { map.put("cityList",
         * areaService.findAllCity(searchMap)); } else { searchMap.put("provincId", rpbae.getProvinceCode());
         * map.put("cityList", areaService.findAllCity(searchMap)); }
         * 
         * map.put("districtCode", rpbae.getDistrictCode()); if (rpbae.getCityCode() == 1) { map.put("districtList",
         * areaService.findAllDistrict(searchMap)); } else { searchMap.put("cityId", rpbae.getCityCode());
         * map.put("districtList", areaService.findAllDistrict(searchMap)); } areasList.add(map); }
         * mv.addObject("areasList", areasList);
         * 
         * if (areasList.size() == 0) { //省 List<ProvinceEntity> provinceList = areaService.allProvince();
         * mv.addObject("provinceList", provinceList);
         * 
         * //市 List<CityEntity> cityList = areaService.findAllCity(new HashMap<String, Object>());
         * mv.addObject("cityList", cityList);
         * 
         * //区 List<DistrictEntity> districtList = areaService.findAllDistrict(new HashMap<String, Object>());
         * mv.addObject("districtList", districtList); }
         */
        return mv;
    }
    
    /**
     * @param product
     * @param mIdAndContent
     * @param categoryIds
     * @param saveType：保存类型，1仅保存，2保存并更新关联的商城特卖商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-新增/编辑基本商品")
    public String saveOrUpdate(ProductBaseEntity product, @RequestParam(value = "mIdAndContent", required = false, defaultValue = "") String mIdAndContent,
        @RequestParam(value = "categoryIds", required = false, defaultValue = "") String categoryIds,
        @RequestParam(value = "saveType", required = false, defaultValue = "1") int saveType)
            throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        categoryIds = "0";
        try
        {
            if (StringUtils.isEmpty(categoryIds))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "分类信息必填");
                return JSON.toJSONString(resultMap);
            }
            
            if (product.getSellerDeliverAreaTemplateId() > 0)
            {
                SellerDeliverAreaTemplateEntity template = sellerDeliverAreaService.findSellerDeliverAreaTemplateById(product.getSellerDeliverAreaTemplateId());
                if (template == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "所选商家配送地区模版不存在，请重新选择");
                    return JSON.toJSONString(resultMap);
                }
                product.setDeliverAreaType(template.getType() > Byte.parseByte("2") ? template.getType() - 2 : template.getType());
                
                if (StringUtils.isEmpty(product.getDeliverAreaDesc()))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "请填写配送地区描述");
                    return JSON.toJSONString(resultMap);
                }
            }
            // 除中国外，都设为进口商品
//            String flagName = saleFlagService.findFlagNameById(product.getSaleFlagId());
//            if ("中国".equals(flagName.trim()))
//            {
                product.setQualityPromiseType(ProductEnum.QUALITY_PROMISE_TYPE.HOMEMADE.getCode());
//            }
//            else
//            {
//                product.setQualityPromiseType(ProductEnum.QUALITY_PROMISE_TYPE.IMPORT.getCode());
//            }
//            
            // 商品图片后缀更换
            product.setSmallImage(product.getImage1());
            if (product.getSmallImage().indexOf(ImageUtil.getPrefix()) > 0)
            {
                String image = product.getSmallImage().substring(0, product.getSmallImage().indexOf(ImageUtil.getPrefix()));
                product.setSmallImage(image);
            }
            
            product.setMediumImage(product.getImage1());
            if (product.getMediumImage().indexOf(ImageUtil.getPrefix()) > 0)
            {
                String image = product.getMediumImage().substring(0, product.getMediumImage().indexOf(ImageUtil.getPrefix()));
                product.setMediumImage(image);
            }
            
            List<Map<String, Object>> mobileDetailsImageList = new ArrayList<Map<String, Object>>();
            if (!"".equals(mIdAndContent))
            {
                String[] detailsArr = mIdAndContent.split(";");
                for (String str : detailsArr)
                {
                    String[] detailsInfo = str.split(",");
                    // 如果详情页图片一张没传，则传过来的mIdAndContent="0,",解析后只有一个参数
                    if (detailsInfo.length == 2)
                    {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("id", "".equals(detailsInfo[0]) ? "0" : detailsInfo[0]);
                        mp.put("content", detailsInfo[1]);
                        mobileDetailsImageList.add(mp);
                    }
                }
            }
            
            Set<CategoryEntity> categorySet = new HashSet<CategoryEntity>();
            if (!"".equals(categoryIds))
            {
                String[] categorysArr = categoryIds.split(";");
                for (String str : categorysArr)
                {
                    String[] idArr = str.split(",");
                    CategoryEntity category = new CategoryEntity();
                    category.setId(Integer.valueOf(idArr[0]));
                    category.setCategoryFirstId(0);
                    category.setCategorySecondId(0);
                    category.setCategoryThirdId(0);
                    category.setProductBaseId(product.getId());
                    categorySet.add(category);
                }
            }
            
            if (product.getId() == 0)// 新增
            {
                productBaseService.saveProductBase(product, mobileDetailsImageList, new ArrayList<CategoryEntity>(categorySet));
            }
            else
            {
                // 修改
                ProductBaseEntity oldProduct = productBaseService.queryProductBaseById(product.getId());
                
                productBaseService.updateProductBase(product, mobileDetailsImageList, new ArrayList<CategoryEntity>(categorySet), saveType);
                
                // 记日志
                try
                {
                    // 供货价有修改则记录日志
                    if (product.getWholesalePrice() != oldProduct.getWholesalePrice())
                    {
                        productBaseService.insertWholesalePriceUpdatelog(product.getId(), oldProduct.getWholesalePrice(), product.getWholesalePrice());
                    }

                    //结算方式变动
                    if (oldProduct.getSubmitType() != product.getSubmitType()) {
                        Map<String, Object> logInfoMap = new HashMap<>();
                        logInfoMap.put("objectId", oldProduct.getId());
                        logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_BASE_PRODUCT_SUbMIT.ordinal());
                        logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                        logInfoMap.put("oldType", ProductBaseEnum.SUbMIT_TYPE.getDescByCode(oldProduct.getSubmitType()));
                        logInfoMap.put("newType", ProductBaseEnum.SUbMIT_TYPE.getDescByCode(product.getSubmitType()));
                        if (product.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.SUPPLY_PRICE.getCode()) {
                            logInfoMap.put("newWholesalePrice", product.getWholesalePrice());
                        } else if (product.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.DISCOUNTED_POINT.getCode()) {
                            logInfoMap.put("newDeduction", product.getDeduction());
                            logInfoMap.put("newProposalPrice", product.getProposalPrice());
                        } else if (product.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.SELF_PURCHASE_PRICE.getCode()) {
                            logInfoMap.put("newSelfPurchasePrice", product.getSelfPurchasePrice());
                        }
                        logService.logger(logInfoMap);
                    } else {
                        Map<String, Object> logInfoMap = new HashMap<>();
                        logInfoMap.put("objectId", oldProduct.getId());
                        logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_BASE_PRODUCT_SUbMIT.ordinal());
                        logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                        if (product.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.SUPPLY_PRICE.getCode()) {
                            if (product.getWholesalePrice() != oldProduct.getWholesalePrice()) {
                                logInfoMap.put("newWholesalePrice", product.getWholesalePrice());
                                logInfoMap.put("oldWholesalePrice", oldProduct.getWholesalePrice());
                                logService.logger(logInfoMap);
                            }
                        } else if (product.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.DISCOUNTED_POINT.getCode()) {
                            if (product.getDeduction() != oldProduct.getDeduction()
                                    || product.getProposalPrice() != oldProduct.getProposalPrice()) {
                                if (product.getDeduction() != oldProduct.getDeduction()) {
                                    logInfoMap.put("newDeduction", product.getDeduction());
                                    logInfoMap.put("oldDeduction", oldProduct.getDeduction());
                                }
                                if (product.getProposalPrice() != oldProduct.getProposalPrice()) {
                                    logInfoMap.put("newProposalPrice", product.getProposalPrice());
                                    logInfoMap.put("oldProposalPrice", oldProduct.getProposalPrice());
                                }
                                logService.logger(logInfoMap);
                            }
                        } else if (product.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.SELF_PURCHASE_PRICE.getCode()) {
                            if (product.getSelfPurchasePrice() != oldProduct.getSelfPurchasePrice()) {
                                logInfoMap.put("newSelfPurchasePrice", product.getSelfPurchasePrice());
                                logInfoMap.put("oldSelfPurchasePrice", oldProduct.getSelfPurchasePrice());
                                logService.logger(logInfoMap);
                            }
                        }
                    }

                    // 建议市场价和 销售价有变动
                    if (new BigDecimal(oldProduct.getProposalMarketPrice()).compareTo(new BigDecimal(product.getProposalMarketPrice())) != 0
                            || new BigDecimal(oldProduct.getProposalSalesPrice()).compareTo(new BigDecimal(product.getProposalSalesPrice())) != 0)
                    {
                        Map<String, Object> logInfoMap = new HashMap<String, Object>();
                        logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_BASE_PRODUCT_PRICE.ordinal());
                        logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                        logInfoMap.put("objectId", oldProduct.getId());
                        if (new BigDecimal(oldProduct.getProposalMarketPrice()).compareTo(new BigDecimal(product.getProposalMarketPrice())) != 0) {
                            logInfoMap.put("oldProposalMarketPrice", oldProduct.getProposalMarketPrice());
                            logInfoMap.put("newProposalMarketPrice", product.getProposalMarketPrice());
                        }
                        if (new BigDecimal(oldProduct.getProposalSalesPrice()).compareTo(new BigDecimal(product.getProposalSalesPrice())) != 0) {
                            logInfoMap.put("oldProposalSalesPrice", oldProduct.getProposalSalesPrice());
                            logInfoMap.put("newProposalSalesPrice", product.getProposalSalesPrice());
                        }
                        logService.logger(logInfoMap);
                    }

                    if (!oldProduct.getCode().equals(product.getCode()))
                    {
                        Map<String, Object> logInfoMap = new HashMap<String, Object>();
                        logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_CODE.ordinal());
                        logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                        logInfoMap.put("objectId", oldProduct.getId());
                        logInfoMap.put("oldCode", oldProduct.getCode());
                        logInfoMap.put("newCode", product.getCode());
                        logService.logger(logInfoMap);
                    }
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
                
            }
            // 同步到商家后台
            try
            {
                System.out.println("\r\n\r\n 同步基本商品到商家后台 \r\n\r\n");
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("productBaseId", product.getId() + ""));
                HttpRequestUtil.reqPost(YggAdminProperties.getInstance().getPropertie("sync_url") + "sync/refresh", nvps);
            }
            catch (Exception ex)
            {
            
            }
            resultMap.put("status", 1);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败，" + e.getMessage());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 复制商品
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/copyProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-复制基本商品")
    public ResultEntity copyProduct(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) String code)
    {
        try
        {
            return productBaseService.copyProduct(id, code);
        }
        catch (Exception e)
        {
            log.error(String.format("基本商品复制失败，product_base_id=%d，code=%s", id, code), e);
            return ResultEntity.getFailResult("复制失败");
        }
    }
    
    @RequestMapping(value = "/jsonProductBaseCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductBaseCode(HttpServletRequest request, @RequestParam(value = "id", required = false, defaultValue = "-1") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "0") int isAvailable)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        List<ProductBaseEntity> baseList = productBaseService.queryAllProductBase(para);
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        Map<String, String> mapAll = new HashMap<String, String>();
        if (isAvailable == -1)
        {
            mapAll.put("code", "");
            mapAll.put("text", "--请选择--");
            codeList.add(mapAll);
        }
        for (ProductBaseEntity entity : baseList)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", entity.getId() + "");
            map.put("text", entity.getName());
            if (entity.getId() == id)
            {
                map.put("selected", "true");
            }
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/getProductBaseInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getProductBaseInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        
        para.put("id", id);
        
        ProductBaseEntity entity = productBaseService.queryProductBaseById(id);
        Map<String, Object> mapAll = new HashMap<String, Object>();
        if (entity == null)
        {
            mapAll.put("status", "0");
        }
        else
        {
//            BrandEntity be = brandService.findBrandById(entity.getBrandId());
            mapAll.put("returnDistributionProportionType", '1');
            mapAll.put("name", entity.getName());
            mapAll.put("status", "1");
            mapAll.put("prouctBaseId", entity.getId() + "");
            mapAll.put("sellerId", entity.getSellerId() + "");
            SellerEntity seller = sellerService.findSellerById(entity.getSellerId());
            mapAll.put("sellerName", seller == null ? "" : seller.getSellerName());
            mapAll.put("realSellerName", seller == null ? "" : seller.getRealSellerName());
            mapAll.put("sendAddress", seller == null ? "" : seller.getSendAddress());
            mapAll.put("freightType", seller == null ? "" : seller.getFreightType() == 1 ? "包邮" : seller.getFreightType() == 2 ? "满"
                + String.format("%.2f", seller.getFreightMoney()) + "包邮" : seller.getFreightType() == 3 ? "全部不包邮" : seller.getFreightOther());
            SellerEnum.SellerTypeEnum sellerType = SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(seller == null ? 1 : seller.getSellerType());
            mapAll.put("sellerType", seller == null ? "" : sellerType.getDesc()
                + (sellerType.getIsNeedIdCardImage() == 1 ? "(需要身份证照片)" : sellerType.getIsNeedIdCardNumber() == 1 ? "(身份证号码)" : ""));
            mapAll.put("sendTimeRemark", seller == null ? "" : SellerEnum.SellerSendTimeTypeEnum.getDescByCode(seller.getSendTimeType()));
            
            List<CategoryEntity> categoryList = categoryService.findCategoryByProductBaseId(id);
            mapAll.put("categoryList", categoryList);
            BrandEntity brand = brandService.findBrandById(entity.getBrandId());
            mapAll.put("brandId", entity.getBrandId() + "");
            mapAll.put("brandName", brand == null ? "" : brand.getName());
            mapAll.put("gegeImageId", entity.getGegeImageId() + "");
            mapAll.put("gegeSay", entity.getGegeSay() + "");
            mapAll.put("code", entity.getCode() + "");
            mapAll.put("barcode", entity.getBarcode() + "");
            mapAll.put("name", entity.getName() + "");
            mapAll.put("submitType", entity.getSubmitType());
            if(entity.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.SUPPLY_PRICE.getCode())
            {
                mapAll.put("submitContent", String.format("%.2f", entity.getWholesalePrice()));
            }else if(entity.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.DISCOUNTED_POINT.getCode()){
                mapAll.put("submitContent", (int)(entity.getDeduction()) + "%");
            }else if(entity.getSubmitType() == ProductBaseEnum.SUbMIT_TYPE.SELF_PURCHASE_PRICE.getCode()){
                mapAll.put("submitContent", String.format("%.2f", entity.getSelfPurchasePrice()));
            }
            mapAll.put("proposalPrice", String.format("%.2f", entity.getProposalPrice()));
            mapAll.put("totalStock", String.format("%d", entity.getTotalStock()));
            mapAll.put("saleStock", String.format("%d", entity.getSaleStock()));
            mapAll.put("netVolume", entity.getNetVolume());
            mapAll.put("placeOfOrigin", entity.getPlaceOfOrigin());
            mapAll.put("manufacturerDate", entity.getManufacturerDate());
            mapAll.put("storageMethod", entity.getStorageMethod());
            mapAll.put("durabilityPeriod", entity.getDurabilityPeriod());
            mapAll.put("peopleFor", entity.getPeopleFor());
            mapAll.put("foodMethod", entity.getFoodMethod());
            mapAll.put("useMethod", entity.getUseMethod());
            mapAll.put("tip", entity.getTip());
            mapAll.put("image1", entity.getImage1());
            mapAll.put("image2", entity.getImage2());
            mapAll.put("image3", entity.getImage3());
            mapAll.put("image4", entity.getImage4());
            mapAll.put("image5", entity.getImage5());
            mapAll.put("mediumImage", entity.getMediumImage());
            mapAll.put("smallImage", entity.getSmallImage());
            mapAll.put("remark", entity.getRemark());
            mapAll.put("gegeImageId", entity.getGegeImageId());
            GegeImageEntity imageEntity = geGeImageService.findGegeImageById(entity.getGegeImageId(), "product");
            mapAll.put("gegeImageUrl", imageEntity == null ? "" : imageEntity.getImage());
            // 商品信息 -- 详情页
            List<ProductBaseMobileDetailEntity> mobileDetails = productBaseService.findProductBaseMobileDetailsByProductBaseId(id);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            if (mobileDetails.size() > 0)
            {
                for (int i = 0; i < mobileDetails.size(); i++)
                {
                    Map<String, String> tmp = new HashMap<String, String>();
                    ProductBaseMobileDetailEntity currDetail = mobileDetails.get(i);
                    tmp.put("id", currDetail.getId() + "");
                    tmp.put("pic", currDetail.getContent());
                    list.add(tmp);
                }
            }
            mapAll.put("mobileDetails", list);
            
        }
        return JSON.toJSONString(mapAll);
    }
    
    /**
     * 设置商品可用状态
     * 
     * @param request
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/forAvailable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-设置商品可用状态")
    public String forAvailable(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        List<Integer> idList = new ArrayList<Integer>();
        if (ids.indexOf(",") > 0)
        {
            String[] arr = ids.split(",");
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
        }
        else
        {
            idList.add(Integer.valueOf(ids));
        }
        para.put("code", code);
        para.put("idList", idList);
        // 结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int resultStatus = productBaseService.forAvailable(para);
        if (resultStatus > 0)
        {
            resultMap.put("status", 1);
            // 商品状态改为可、停用记录日志
            try
            {
                Map<String, Object> logInfoMap = new HashMap<String, Object>();
                logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_STATUS.ordinal());
                logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                logInfoMap.put("objectId", ids);
                logInfoMap.put("newStatus", code == 1 ? "可用" : "停用");
                logService.logger(logInfoMap);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/toAdjustStock")
    public ModelAndView toAdjustStock(@RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productBase/adjustStock");
        
        ProductBaseEntity productBase = productBaseService.queryProductBaseById(id);
        if (productBase == null)
        {
            mv.setViewName("error/404");
            return mv;
        }
        mv.addObject("base", productBase);
        
        SellerEntity seller = sellerService.findSellerById(productBase.getSellerId());
        mv.addObject("seller", seller);
        mv.addObject("sellerType", seller == null ? "" : SellerEnum.SellerTypeEnum.getDescByCode(seller.getSellerType()));
        
        // 特卖商品信息
        List<Map<String, Object>> productInfoList = productBaseService.querySaleProductInfoByBaseId(id);
        mv.addObject("productInfoList", productInfoList);
        
        return mv;
    }
    
    @RequestMapping(value = "/addTotalStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-调基本商品总库存")
    public String addTotalStock(@RequestParam(value = "stock", required = true) String stock, @RequestParam(value = "baseId", required = true) int baseId)
        throws Exception
    {
        try
        {
            if (stock.equals("") || !StringUtils.isNumeric(stock.startsWith("-") ? stock.substring(1) : stock))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "数量必填且必须为数值");
                return JSON.toJSONString(map);
            }
            
            int addStockNum = Integer.valueOf(stock);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", baseId);
            map.put("stockNum", addStockNum);
            if (addStockNum < 0)
            {
                map.put("jian", 1);
                map.put("rStockNum", -addStockNum);
            }
            else
            {
                map.put("jian", 0);
            }
            int resultStatus = productBaseService.addTotalStock(map);
            if (resultStatus == 1)
            {
                map.put("status", 1);
                // 商品库存有修改时记录日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_STOCK.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                    logInfoMap.put("objectId", baseId);
                    logInfoMap.put("addStockNum", addStockNum);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
                
            }
            else
            {
                map.put("status", 0);
                map.put("msg", "修改失败，请确保减少后的库存数量大于锁定数量且大于0");
            }
            
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 调库存
     * @param productId
     * @param baseId
     * @param stock
     * @param type:商品类型:1特卖商品;2商城商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/adjustStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-调商品库存")
    public String adjustStock(//
        @RequestParam(value = "productId", required = true) int productId,//
        @RequestParam(value = "baseId", required = true) int baseId,//
        @RequestParam(value = "stock", required = true) int stock,//
        @RequestParam(value = "type", required = true) int type)
        throws Exception
    {
        try
        {
            return productBaseService.adjustStock(baseId, productId, type, stock);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return JSON.toJSONString(ResultEntity.getFailResult("库存调整失败"));
        }
    }
    
    /**
     * 自动生成商品编号
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/autoCreateCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String autoCreateCode()
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int maxProductId = productBaseService.findMaxProductId();
            String code = "GGJ" + (int)((Math.random() * 9 + 1) * 10000) + maxProductId;
            result.put("status", 1);
            result.put("code", code);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            result.put("status", 0);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 检查商品编码是否唯一，规则，同一个商家的商品编码必须唯一（一开始数据就重复较多，没法做唯一约束）
     * @return
     */
    @RequestMapping(value = "/checkCodeAndBarCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity checkCodeAndBarCode(@RequestParam(value = "sellerId", required = true) int sellerId, @RequestParam(value = "code", required = true) String code,
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId)
    {
        try
        {
            List<Integer> ids = productBaseService.checkCodeAndBarCode(sellerId, code);
            ids.remove(Integer.valueOf(productBaseId));
            if (ids.isEmpty())
            {
                return ResultEntity.getSuccessResult();
            }
            else
            {
                return ResultEntity.getFailResult("商品编码重复");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return ResultEntity.getFailResult("服务器忙");
        }
    }
    
    /**
     * 
     * @param productBaseId，基本商品Id
     * @param
     * @return
     */
    @RequestMapping(value = "/findProductInfoByBaseId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findProductInfoByBaseId(@RequestParam(value = "baseId", required = true) int productBaseId)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            ProductBaseEntity productBase = productBaseService.queryProductBaseById(productBaseId);
            if (productBase == null)
            {
                result.put("status", 0);
                result.put("msg", "基本商品不存在");
            }
            else
            {
                result.put("status", 1);
                result.put("baseId", productBase.getId() + "");
                result.put("baseName", productBase.getName());
                result.put("barcode", productBase.getBarcode());
                result.put("code", productBase.getCode());
                
                SellerEntity seller = sellerService.findSellerById(productBase.getSellerId());
                result.put("sellerName", seller == null ? "" : seller.getRealSellerName());
                result.put("sendType",
                    seller == null ? ""
                        : SellerEnum.SellerTypeEnum.getDescByCode(seller.getSellerType())
                            + (seller.getIsNeedIdCardImage() == 1 ? "(身份证照片)" : seller.getIsNeedIdCardNumber() == 1 ? "(仅身份证号)" : ""));
                result.put("sendAddress", seller.getSendAddress());
                result.put("warehouse", seller.getWarehouse());
                result.put("availableStock", productBase.getAvailableStock() + "");
                result.put("allottedStock", productBaseService.findAllottedStockById(productBaseId) + "");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", "服务器忙，请稍后再试");
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 
     * @param request
     * @param page
     * @param rows
     * @param type：商品类型；1特卖商品，2商城商品
     * @param baseId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "type", required = false, defaultValue = "-1") int type,
        @RequestParam(value = "baseId", required = false, defaultValue = "0") int baseId,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("id", baseId);
            para.put("isAvailable", isAvailable);
            if (type != -1)
            {
                para.put("type", type);
            }
            
            resultMap = productBaseService.findJsonProductInfoBybaseId(para);
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 刷新库存
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refreshStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String refreshStock(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        ProductBaseEntity product = productBaseService.queryProductBaseById(id);
        if (product != null)
        {
            resultMap.put("status", 1);
            resultMap.put("stock", product.getAvailableStock());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/copyImageFromProductBase", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-复制基本商品图片")
    public String copyImageFromProductBase(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            ProductBaseEntity product = productBaseService.queryProductBaseById(id);
            List<ProductBaseMobileDetailEntity> mobileDetails = productBaseService.findProductBaseMobileDetailsByProductBaseId(id);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            resultMap.put("status", 1);
            if (product != null)
            {
                resultMap.put("image1", product.getImage1() == null ? "" : product.getImage1());
                resultMap.put("image2", product.getImage2() == null ? "" : product.getImage2());
                resultMap.put("image3", product.getImage3() == null ? "" : product.getImage3());
                resultMap.put("image4", product.getImage4() == null ? "" : product.getImage4());
                resultMap.put("image5", product.getImage5() == null ? "" : product.getImage5());
                
                resultMap.put("mediumImage", product.getMediumImage() == null ? "" : product.getMediumImage());
                resultMap.put("smallImage", product.getSmallImage() == null ? "" : product.getSmallImage());
                if (mobileDetails.size() > 0)
                {
                    for (int i = 0; i < mobileDetails.size(); i++)
                    {
                        Map<String, String> tmp = new HashMap<String, String>();
                        ProductBaseMobileDetailEntity currDetail = mobileDetails.get(i);
                        tmp.put("id", currDetail.getId() + "");
                        tmp.put("pic", currDetail.getContent());
                        list.add(tmp);
                    }
                }
            }
            else
            {
                resultMap.put("image1", "");
                resultMap.put("image2", "");
                resultMap.put("image3", "");
                resultMap.put("image4", "");
                resultMap.put("image5", "");
                resultMap.put("mediumImage", "");
                resultMap.put("smallImage", "");
            }
            resultMap.put("mobileDetails", list);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 批量修改供货价管理
     * @return
     */
    @RequestMapping("/batchUpdateProductCostPrice")
    public ModelAndView batchUpdateProductCostPrice()
    {
        return new ModelAndView("productBase/batchUpdateProductCostPrice");
    }
    
    /**
     * 下载批量修改供货价模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadBatchUpdateProductCostPrice")
    public void downloadBatchUpdateProductCostPrice(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "批量修改供货价导入模板";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"基本商品ID", "商品结算类型", "供货价", "扣点", "建议售价", "自营采购价"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 模拟or保存 批量修改供货价
     * @param request
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importBatchUpdateProductCostPrice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-批量修改供货价")
    public String importBatchUpdateProductCostPrice(HttpServletRequest request, //
        @RequestParam("importFile") MultipartFile file, //
        @RequestParam(value = "type", required = true) int type// 1 : 模拟 ； 2 : 确认
    )
        throws Exception
    {
        try
        {
            int returnStatus = 1;
            String returnMsg = "";
            int successNum = 0;
            int failNum = 0;
            String dataKey = "importBatchUpdateProductCostPrice" + System.currentTimeMillis() + "";
            
            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                if (type == 1)
                {
                    // 模拟导入
                    List<Map<String, Object>> simulationList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String productBaseId = String.valueOf(it.get("cell0") == null ? "" : it.get("cell0")).trim();
                        String submitType = String.valueOf(it.get("cell1") == null ? "" : it.get("cell1")).trim();
                        String wholesalePrice = String.valueOf(it.get("cell2") == null ? "" : it.get("cell2")).trim();
                        String deduction = String.valueOf(it.get("cell3") == null ? "" : it.get("cell3")).trim();
                        String proposalPrice = String.valueOf(it.get("cell4") == null ? "" : it.get("cell4")).trim();
                        String selfPurchasePrice = String.valueOf(it.get("cell5") == null ? "" : it.get("cell5")).trim();
                        int status = productBaseService.checkBatchUpdateProductCostPrice(productBaseId, submitType, wholesalePrice, deduction, proposalPrice, selfPurchasePrice,
                            simulationList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, simulationList);
                }
                else if (type == 2)
                {
                    // 确认导入
                    List<Map<String, Object>> confirmList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String productBaseId = String.valueOf(it.get("cell0") == null ? "" : it.get("cell0")).trim();
                        String submitType = String.valueOf(it.get("cell1") == null ? "" : it.get("cell1")).trim();
                        String wholesalePrice = String.valueOf(it.get("cell2") == null ? "" : it.get("cell2")).trim();
                        String deduction = String.valueOf(it.get("cell3") == null ? "" : it.get("cell3")).trim();
                        String proposalPrice = String.valueOf(it.get("cell4") == null ? "" : it.get("cell4")).trim();
                        String selfPurchasePrice = String.valueOf(it.get("cell5") == null ? "" : it.get("cell5")).trim();
                        int status =
                            productBaseService.saveBatchUpdateProductCostPrice(productBaseId, submitType, wholesalePrice, deduction, proposalPrice, selfPurchasePrice, confirmList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, confirmList);
                }
            }
            else
            {
                returnStatus = 0;
                returnMsg = "文件有误";
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", returnStatus);
            result.put("isRight", (failNum == 0 && type == 1) ? 1 : 0);
            result.put("msg", returnMsg);
            result.put("okNum", successNum);
            result.put("failNum", failNum);
            result.put("dataKey", dataKey);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("模拟or保存 批量修改供货价 -- 失败！！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "系统错误");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 异步获取模拟批量修改供货价结果
     * 
     * @param request
     * @param page
     * @param rows
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonBatchUpdateProductCostPriceResult", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBatchUpdateProductCostPriceResult(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "100") int rows, //
        @RequestParam(value = "dataKey", required = true) String dataKey)
            throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int total = 0;
        List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            if (page == 0)
            {
                page = 1;
            }
            int start = rows * (page - 1);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
            for (int i = start; (i < dataList.size() && i < (start + rows)); i++)
            {
                dataRows.add(dataList.get(i));
            }
            total = dataList.size();
        }
        result.put("total", total);
        result.put("rows", dataRows);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/downloadBatchUpdateProductCostPriceResult")
    public void downloadBatchUpdateProductCostPriceResult(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "dataKey", required = true) String dataKey)
            throws Exception
    {
        List<Map<String, Object>> dataList = null;
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
        }
        dataList = dataList == null ? new ArrayList<Map<String, Object>>() : dataList;
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "模拟结果导出";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"导入状态", "说明", "基本商品ID", "商品结算类型", "供货价", "扣点", "建议售价", "自营采购价"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> curr = dataList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(curr.get("status") + "");
                row.createCell(1).setCellValue(curr.get("msg") + "");
                row.createCell(2).setCellValue(curr.get("productBaseId") + "");
                row.createCell(3).setCellValue(curr.get("submitType") + "");
                row.createCell(4).setCellValue(curr.get("wholesalePrice") + "");
                row.createCell(5).setCellValue(curr.get("deduction") + "");
                row.createCell(6).setCellValue(curr.get("proposalPrice") + "");
                row.createCell(7).setCellValue(curr.get("selfPurchasePrice") + "");
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 商品正品保证列表
     * 
     * @return
     */
    @RequestMapping("/qualityList")
    public ModelAndView qualityList()
    {
        ModelAndView mv = new ModelAndView("productBase/qualityList");
        return mv;
    }
    
    /**
     * 异步加载商品正品保证信息
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonQualityPromiseInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonQualityPromiseInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            
            resultMap = productBaseService.jsonQualityPromiseInfo(para);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增/修改商品正品保证信息
     * 
     * @param id
     * @param type
     * @param image
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateQualityPromise", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-新增/修改商品正品保证信息")
    public String saveOrUpdateQualityPromise(@RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, //
        @RequestParam(value = "image", required = false, defaultValue = "") String image, //
        @RequestParam(value = "customPageId", required = false, defaultValue = "0") int customPageId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("type", type);
            para.put("image", image);
            para.put("customPageId", customPageId);
            
            PageCustomEntity pce = pageCustomService.findPageCustomById(customPageId);
            if (pce == null || pce.getIsAvailable() == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id=" + customPageId + "的自定义页面不存在或不可用");
                return JSON.toJSONString(resultMap);
            }
            para.put("url", pce.getPcUrl());
            int result = productBaseService.saveOrUpdateQualityPromise(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else if (result == 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "图片尺寸只能为333x333");
            }
            else if (result == 3)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", ProductEnum.QUALITY_PROMISE_TYPE.getDescByCode(type) + "已近存在");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getCause(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。如果问题一直存在，请联系管理员");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/classifyProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-基本商品批量分类")
    public String classifyProduct(@RequestParam(value = "ids", required = true) String baseIds, @RequestParam(value = "categoryIds", required = true) String categoryIds)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Set<CategoryEntity> categoryList = new HashSet<CategoryEntity>();
            if (!"".equals(categoryIds))
            {
                String[] categorysArr = categoryIds.split(";");
                for (String str : categorysArr)
                {
                    String[] idArr = str.split(",");
                    CategoryEntity category = new CategoryEntity();
                    category.setCategoryFirstId(Integer.valueOf(idArr[0]));
                    category.setCategorySecondId(Integer.valueOf(idArr[1]));
                    category.setCategoryThirdId(Integer.valueOf(idArr[2]));
                    categoryList.add(category);
                }
            }
            
            List<String> baseIdList = null;
            if (!"".equals(baseIds))
            {
                baseIdList = Arrays.asList(baseIds.split(","));
            }
            if (baseIdList == null || baseIdList.size() == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要分类的商品");
                return JSON.toJSONString(resultMap);
            }
            Set<String> baseIdSet = new HashSet<String>(baseIdList);
            List<CategoryEntity> addCategoryList = new ArrayList<CategoryEntity>();
            for (String id : baseIdSet)
            {
                for (CategoryEntity ce : categoryList)
                {
                    CategoryEntity cet = new CategoryEntity();
                    cet.setCategoryFirstId(ce.getCategoryFirstId());
                    cet.setCategorySecondId(ce.getCategorySecondId());
                    cet.setCategoryThirdId(ce.getCategoryThirdId());
                    cet.setProductBaseId(Integer.valueOf(id));
                    addCategoryList.add(cet);
                }
            }
            
            // 结果
            int resultStatus = productBaseService.classifyProduct(addCategoryList);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "修改失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请稍后再试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/batchAdjustStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "基本商品管理-批量调库存")
    public String batchAdjustStock(//
        @RequestParam(value = "ids", required = true) String ids, //
        @RequestParam(value = "stock", required = true) int stock, //
        @RequestParam(value = "type", required = true) int type)
            throws Exception
    {
        Map<String, Object> resutMap = new HashMap<String, Object>();
        try
        {
            StringBuffer sb = new StringBuffer();
            String[] bidAndpidArr = ids.split(";");
            for (String str : bidAndpidArr)
            {
                int productBaseId = Integer.parseInt(str.split(":")[0]);
                int productId = Integer.parseInt(str.split(":")[1]);
                String jsonStr = productBaseService.adjustStock(productBaseId, productId, type, stock);
                int status = JSON.parseObject(jsonStr).getIntValue("status");
                if (status == CommonConstant.COMMON_YES)
                {
                    sb.append("Id=").append(productId).append("的商品调整成功").append(";");
                }
                else
                {
                    sb.append("Id=" + productId).append("的商品调整失败，原因库存不足").append(";");
                }
            }
            resutMap.put("msg", sb.toString());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resutMap.put("msg", "保存失败");
        }
        
        return JSON.toJSONString(resutMap);
    }
    
    @RequestMapping("/expireProductList")
    public ModelAndView expireProductList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productBase/expireProductList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonExpireProductBaseInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonExpireProductBaseInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "productId", required = false, defaultValue = "-1") int productId,
        @RequestParam(value = "productName", required = false, defaultValue = "") String name)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (productId != -1)
            {
                para.put("id", productId);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            resultMap = productBaseService.findAllExpireProduct(para);
        }
        catch (Exception e)
        {
            log.error("异步加载过期基本商品列表出错了", e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/exportResult")
    @ControllerLog(description = "基本商品管理-导出商品")
    public void exportResult(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "productId", required = false, defaultValue = "-1") int productId, @RequestParam(value = "code", required = false, defaultValue = "") String code,
        @RequestParam(value = "barCode", required = false, defaultValue = "") String barCode, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String name,
        @RequestParam(value = "firstCategory", required = false, defaultValue = "-1") int firstCategory,
        @RequestParam(value = "secondCategory", required = false, defaultValue = "-1") int secondCategory,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (productId != -1)
        {
            para.put("productId", productId);
        }
        if (code.indexOf("%") > -1)
        {
            // 默认一个%
            code = code.substring(0, code.indexOf("%")) + "\\" + code.substring(code.indexOf("%"), code.length());
        }
        if (!"".equals(code))
        {
            para.put("likecode", "%" + code + "%");
            // para.put("code", code);
        }
        // if (!"".equals(code))
        // {
        // para.put("code", code);
        // }
        // if (!"".equals(code))
        // {
        // para.put("likecode", "%" + code + "%");
        // // para.put("code", code);
        // }
        if (!"".equals(barCode))
        {
            para.put("barCode", barCode);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        
        if (firstCategory != -1)
        {
            if (firstCategory == 0)
            {
                para.put("noCategory", 1);
            }
            else
            {
                para.put("firstCategory", firstCategory);
            }
        }
        if (secondCategory != -1)
        {
            if (secondCategory == 0)
            {
                para.put("noCategory", 1);
            }
            else
            {
                para.put("secondCategory", secondCategory);
            }
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (brandId != 0)
        {
            para.put("brandId", brandId);
        }
        if (!"".equals(remark))
        {
            para.put("remark", "%" + remark + "%");
        }
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            ResultEntity info = productBaseService.ajaxPageDataProductBase(para);
            
            if (info == null)
            {
                log.error("导出商品查询结果为空");
                return;
            }
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)info.getRows();
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "基本商品";
            if (isAvailable == 0)
            {
                codedFileName = "停用基本商品";
            }
            else if (isAvailable == 1)
            {
                codedFileName = "可用基本商品";
            }
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"商品ID", "状态", "商品条码", "商品编码", "名称", "备注", "类别", "商家", "品牌", "发货类型", "发货地", "结算方式", "结算内容", "邮费结算", "未分配库存", "特卖库存", "分销库存", "商城库存"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            
            if (resultList != null)
            {
                int index = 1;
                for (Map<String, Object> currMap : resultList)
                {
                    int cellIndex = 0;
                    Row r = sheet.createRow(index++);
                    r.createCell(cellIndex++).setCellValue(currMap.get("productId") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("isAvailable") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("barCode") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("productCode") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("productName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("categoryName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sellerName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("brandName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sendType") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sendAddress") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("submitType") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("submitContent") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("freightType") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("availableStock") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("saleStock") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("distributionStock") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("mallStock") + "");
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error("导出基本商品出错", e);
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 刷新已分配库存
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refreshAllottedStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String refreshAllottedStock(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int sum = productBaseService.findAllottedStockById(id);
        resultMap.put("status", 1);
        resultMap.put("stock", sum);
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("findProductBaseRelationInfoByProductBaseId")
    @ResponseBody
    public Object findProductBaseById(int id)
    {
        try
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            List<Integer> list = new ArrayList<Integer>();
            list.add(id);
            result.put("data", productBaseService.findProductBaseRelationInfoByProductBaseId(list).get(0));
            return result;
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    @RequestMapping(value = "/jsonWholeSalePriceHistory", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultEntity jsonWholeSalePriceHistory(/*
                                                   * @RequestParam(value = "page", required = false, defaultValue = "1")
                                                   * int page,
                                                   * 
                                                   * @RequestParam(value = "rows", required = false, defaultValue =
                                                   * "30") int pageSize,
                                                   */
        @RequestParam(value = "proudctBaseId", required = false, defaultValue = "0") int proudctBaseId)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            /*
             * if (page == 0) { page = 1; } para.put("start", pageSize * (page - 1)); para.put("max", pageSize);
             */
            para.put("proudctBaseId", proudctBaseId);
            return productBaseService.jsonWholeSalePriceHistory(para);
        }
        catch (Exception e)
        {
            log.error("异步加载基本商品供货价历史列表出错了", e);
            return ResultEntity.getFailResultList();
        }
    }
    
    /**
     * 根据售价计算提示信息
     * 
     * @param request
     * @param id
     * @param salesPrice 售价
     * @param bsCommision 佣金 此时为0
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getInfoBySalesPrice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getInfoBySalesPrice(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
        @RequestParam(value = "salesPrice", required = true) float salesPrice)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        
        para.put("id", id);
        
        ProductBaseEntity entity = productBaseService.queryProductBaseById(id);
        Map<String, Object> mapAll = new HashMap<String, Object>();
        if (entity == null)
        {
            mapAll.put("status", "0");
        }
        else
        {
            mapAll.put("status", "1");
            float cost = 0;
            if (entity.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
            {
                cost = entity.getWholesalePrice();
            }
            else if (entity.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())// 扣点结算，salesPrice*(1-deduction/100)
            {
                cost = (100 - entity.getDeduction()) * entity.getProposalPrice() / 100;
            }
            else if (entity.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())// 自营采购价，selfPurchasePrice
            {
                cost = entity.getSelfPurchasePrice();
            }
            String bsCommision = MathUtil.round(salesPrice * 0.2, 2);
            mapAll.put("bsCommision", bsCommision);
            // 1.比例
            String bil = "20";
            // 2.对应盈亏：售价-成本价-总佣金
            double dyk = salesPrice - cost - (salesPrice * 0.2);
            String yk = MathUtil.round(dyk, 2);
            // 3.对应盈亏比例：（售价-成本价-总佣金）/ 成本价 * 100%
            String ykb = MathUtil.round((dyk / cost) * 100, 2);
            mapAll.put("bil", bil);
            mapAll.put("yk", yk);
            mapAll.put("ykb", ykb);
        }
        return JSON.toJSONString(mapAll);
    }
    
    /**
     * 根据售价和佣金计算提示信息
     * 
     * @param request
     * @param id
     * @param salesPrice 售价
     * @param bsCommision 佣金
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getInfoByBsCommision", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getInfoByBsCommision(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
        @RequestParam(value = "salesPrice", required = true) float salesPrice, @RequestParam(value = "bsCommision", required = true) float bsCommision)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        
        para.put("id", id);
        
        ProductBaseEntity entity = productBaseService.queryProductBaseById(id);
        Map<String, Object> mapAll = new HashMap<String, Object>();
        if (entity == null)
        {
            mapAll.put("status", "0");
        }
        else
        {
            mapAll.put("status", "1");
            float cost = 0;
            if (entity.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
            {
                cost = entity.getWholesalePrice();
            }
            else if (entity.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())// 扣点结算，salesPrice*(1-deduction/100)
            {
                cost = (100 - entity.getDeduction()) * entity.getProposalPrice() / 100;
            }
            else if (entity.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())// 自营采购价，selfPurchasePrice
            {
                cost = entity.getSelfPurchasePrice();
            }
            // 1.比例
            String bil = MathUtil.round((bsCommision / salesPrice) * 100, 2);
            // 2.对应盈亏：售价-成本价-总佣金
            double dyk = salesPrice - cost - bsCommision;
            String yk = MathUtil.round(dyk, 2);
            // 3.对应盈亏比例：（售价-成本价-总佣金）/ 成本价 * 100%
            String ykb = MathUtil.round((dyk / cost) * 100, 2);
            mapAll.put("bil", bil);
            mapAll.put("yk", yk);
            mapAll.put("ykb", ykb);
        }
        return JSON.toJSONString(mapAll);
    }

    @RequestMapping(value = "/previewPicture", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object previewPicture(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        try
        {
            return productBaseService.previewPicture(id);
        }
        catch (Exception e)
        {
            log.error("基本商品预览图片出错，id=" + id, e);
            return ResultEntity.getFailResult("预览出错，请稍后");
        }
    }

    @RequestMapping("/historySalesVolume/{id}")
    public ModelAndView historySalesVolume(@PathVariable("id") int id, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
    {
        ModelAndView mv = new ModelAndView("productBase/historySalesVolume");
        try
        {
            Map<String, Object> info = productBaseService.findHistorySalesVolumeById(id, startTime, endTime);
            mv.addObject("labels", JSON.toJSONString(info.get("labels")));
            mv.addObject("data", JSON.toJSONString(info.get("data")));
            mv.addObject("totalSales", info.get("totalSales"));
            mv.addObject("productName", info.get("productName"));
            mv.addObject("begin", info.get("begin"));
            mv.addObject("end", info.get("end"));
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
}
