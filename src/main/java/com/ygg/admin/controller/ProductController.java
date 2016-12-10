package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.entity.*;
import com.ygg.admin.service.*;
import com.ygg.admin.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

@Controller
@RequestMapping("/product")
public class ProductController
{
    Logger log = Logger.getLogger(ProductController.class);

    @Resource
    SellerService sellerService;

    @Resource(name = "activitiesCommonService")
    private ActivitiesCommonService activitiesCommonService;
    
    @Resource
    BrandService brandService;
    
    @Resource
    FreightService freightService;
    
    @Resource
    PageCustomService pageCustomService;
    
    @Resource
    ProductService productService;
    
    @Resource
    CategoryService categoryService;
    
    @Resource
    GegeImageService geGeImageService;
    
    @Resource
    private SystemLogService logService;
    
    @Resource
    private ProductBaseService productBaseService;
    
    /**
     * 商品列表
     * @param request
     * @param isAvailable：是否可用，1可用，0不可用
     * @param productType：商品类型，1特卖商品，2商城商品
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "productType", required = false, defaultValue = "1") int productType)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.addObject("listType", isAvailable == 1 ? "可用商品" : "停用商品");
            mv.addObject("isAvailable", isAvailable);
            mv.addObject("productType", productType);
            if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                mv.setViewName("product/list");
            }
            else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
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
                mv.setViewName("mallProduct/list");
            }
            else
            {
                mv.setViewName("error/404");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 异步获得所有商品信息
     * @param request
     * @param page
     * @param rows
     * @param productType:商品类型，1特卖商品，2商城商品
     * @param code：商品编码
     * @param name：商品名称
     * @param desc：格格说
     * @param searchId：商品Id
     * @param isAvailable：是否可用，1可用，0不可用
     * @param isOffShelves：是否下架：1下架，0上架
     * @param startTimeBegin：特卖商品开始时间起
     * @param startTimeEnd：特卖商品开始时间止
     * @param endTimeBegin：特卖商品结束时间起
     * @param endTimeEnd：特卖商品结束时间起
     * @param sellerType：商家发货类型
     * @param sellerId：商家Id
     * @param brandId：品牌Id
     * @param baseId:基本商品Id
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "productType", required = true) int productType,
        @RequestParam(value = "code", required = false, defaultValue = "") String code, @RequestParam(value = "name", required = false, defaultValue = "") String name,
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, @RequestParam(value = "searchId", required = false, defaultValue = "") String searchId,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "-1") int isOffShelves,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin,
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd,
        @RequestParam(value = "sendType", required = false, defaultValue = "-1") int sellerType,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,
        @RequestParam(value = "baseId", required = false, defaultValue = "0") int baseId,
        @RequestParam(value = "lowerPrice", required = false, defaultValue = "0") double lowerPrice,
        @RequestParam(value = "higherPrice", required = false, defaultValue = "0") double higherPrice,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "productUseScopeId", required = false, defaultValue = "-1") int productUseScopeId

    )
        throws Exception
    {
        
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("productType", productType);
        if (code.indexOf("%") > -1)
        {
            //默认一个%
            code = code.substring(0, code.indexOf("%")) + "\\" + code.substring(code.indexOf("%"), code.length());
        }
        if (!"".equals(code))
        {
            para.put("likecode", "%" + code + "%");
            //            para.put("code", code);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(desc))
        {
            para.put("desc", "%" + desc + "%");
        }
        if (!"".equals(searchId))
        {
            para.put("id", searchId);
        }
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (isOffShelves != -1)
        {
            para.put("isOffShelves", isOffShelves);
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("startTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("startTimeEnd", startTimeEnd);
        }
        if (!"".equals(endTimeBegin))
        {
            para.put("endTimeBegin", endTimeBegin);
        }
        if (!"".equals(endTimeEnd))
        {
            para.put("endTimeEnd", endTimeEnd);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (sellerType != -1)
        {
            para.put("sellerType", SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(sellerType).getCode());
            // 发货类型为：香港（身份证照片）暂时不用，查询是不用区分香港（仅身份证号）和香港（身份证照片）
            /*
             * if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()) { para.put("isNeedIdcardImage", 1);
             * para.put("isNeedIdcardNumber", 1); }
             */
        }
        if (brandId != 0)
        {
            para.put("brandId", brandId);
        }
        if (baseId != 0)
        {
            para.put("baseId", baseId);
        }
        if (lowerPrice > 0)
        {
            para.put("lowerPrice", lowerPrice);
        }
        if (higherPrice > 0)
        {
            para.put("higherPrice", higherPrice);
        }
        if (!"".equals(remark))
        {
            para.put("remark", "%" + remark + "%");
        }
        if( productUseScopeId >= 1 ) {
            para.put("productUseScopeId", productUseScopeId);
        }
        Map<String, Object> info = null;
        if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
        {
            info = productService.jsonProductForManage(para);
        }
        else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
        {
            info = productService.jsonMallProductForManage(para);
        }
        return JSON.toJSONString(info);
    }
    
    /**
     * 导出 结果 excel
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportResult")
    @ControllerLog(description = "商品管理-导出商品")
    public void exportResult(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "productType", required = true) int productType, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, //
        @RequestParam(value = "title", required = false, defaultValue = "") String name, //
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "searchId", required = false, defaultValue = "") String searchId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable, //
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "-1") int isOffShelves, //
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin, //
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd, //
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin, //
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd, //
        @RequestParam(value = "sendType", required = false, defaultValue = "-1") int sellerType, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId, //
        @RequestParam(value = "baseId", required = false, defaultValue = "0") int baseId, //
        @RequestParam(value = "lowerPrice", required = false, defaultValue = "0") double lowerPrice, //
        @RequestParam(value = "higherPrice", required = false, defaultValue = "0") double higherPrice, //
        @RequestParam(value = "searchCategoryFirstId", required = false, defaultValue = "0") int categoryFirstId, //
        @RequestParam(value = "searchCategorySecondId", required = false, defaultValue = "0") int categorySecondId, //
        @RequestParam(value = "searchCategoryThirdId", required = false, defaultValue = "0") int categoryThirdId, //
        @RequestParam(value = "searchIsShowInMall", required = false, defaultValue = "-1") int isShowInMall,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "productUseScopeId", required = false, defaultValue = "-1") int productUseScopeId
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("productType", productType);
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(desc))
        {
            para.put("desc", "%" + desc + "%");
        }
        if (!"".equals(searchId))
        {
            para.put("id", searchId);
        }
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (isOffShelves != -1)
        {
            para.put("isOffShelves", isOffShelves);
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("startTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("startTimeEnd", startTimeEnd);
        }
        if (!"".equals(endTimeBegin))
        {
            para.put("endTimeBegin", endTimeBegin);
        }
        if (!"".equals(endTimeEnd))
        {
            para.put("endTimeEnd", endTimeEnd);
        }
        if (sellerType != -1)
        {
            para.put("sellerType", sellerType);
        }
        if (sellerType != -1)
        {
            para.put("sellerType", SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(sellerType).getCode());
            // 发货类型为：香港（身份证照片）暂时不用，查询是不用区分香港（仅身份证号）和香港（身份证照片）
            /*
             * if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()) { para.put("isNeedIdcardImage", 1);
             * para.put("isNeedIdcardNumber", 1); }
             */
        }
        if (brandId != 0)
        {
            para.put("brandId", brandId);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (baseId != 0)
        {
            para.put("baseId", baseId);
        }
        if (lowerPrice > 0)
        {
            para.put("lowerPrice", lowerPrice);
        }
        if (higherPrice > 0)
        {
            para.put("higherPrice", higherPrice);
        }
        if (categoryFirstId != 0)
        {
            para.put("categoryFirstId", categoryFirstId);
        }
        if (categorySecondId != 0)
        {
            para.put("categorySecondId", categorySecondId);
        }
        if (categoryThirdId != 0)
        {
            para.put("categoryThirdId", categoryThirdId);
        }
        if (isShowInMall != -1)
        {
            para.put("isShowInMall", isShowInMall);
        }
        if (!"".equals(remark))
        {
            para.put("remark", "%" + remark + "%");
        }
        if( productUseScopeId >= 1 ) {
            para.put("productUseScopeId", productUseScopeId);
        }

        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> info = null;
            
            if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == productType)
            {
                info = productService.jsonProductForManage(para);
            }
            else if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == productType)
            {
                info = productService.jsonMallProductForManage(para);
            }
            if (info == null)
            {
                log.error("导出商品查询结果为空");
                return;
            }
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)info.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "停用商品";
            if (isAvailable == 1)
            {
                codedFileName = "可用商品";
            }
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"商品ID", "使用状态", "商品状态", "编码", "名称", "短名称", "商家发货名称", "备注", "累计销量", "剩余库存", "锁定库存", "售价", "结算内容", "运费模板", "商家", "品牌", "发货地", "国家", "分类","商品使用渠道"};
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
                    r.createCell(cellIndex++).setCellValue(currMap.get("editId") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("isAvailable") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("isOffShelves") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("code") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("name") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("shortName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sellerProductName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sell") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("stock") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("lockNum") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("salesPrice") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("submitContent") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("ftName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sellerName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("brandName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sendAddress") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("countryName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("categoryName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("scopeName") + "");
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error("导出商品出错", e);
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
     * 转发到新增商品页面
     * @param request
     * @param productType：商品类型，1特卖商品，2商城商品
     * @return
     */
    @RequestMapping("/add/{productType}")
    public ModelAndView add(HttpServletRequest request, @PathVariable("productType") int productType)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            if (productType == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                mv.setViewName("product/update");
            }
            else if (productType == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                mv.setViewName("mallProduct/update");
            }
            else
            {
                mv.setViewName("error/500");
                return mv;
            }
            mv.addObject("toAction", "save");
            // 商家列表
            List<SellerEntity> sEntities = sellerService.findSellerIsAvailable();
            mv.addObject("sellerList", sEntities);
            
            // 品牌
            List<BrandEntity> bEntities = brandService.findBrandIsAvailable();
            mv.addObject("brandList", bEntities);
            
            // 运费模板
            List<FreightTemplateEntity> templateList = freightService.findFreightTemplateIsAvailable();
            mv.addObject("templateList", templateList);
            
            // 自定义页面
            List<PageCustomEntity> pList = pageCustomService.findAllPageCustomForProduct();
            mv.addObject("pageCustomList", pList);
            
            // 查询session中是否存在product信息 保存出错时...
            ProductEntity product = new ProductEntity();
            mv.addObject("product", product);
            
            // 格格头像
            List<GegeImageEntity> imageList = geGeImageService.findAllGegeImage("product");
            mv.addObject("gegeImageList", imageList);
            
            GegeImageEntity geGeImageEntity = new GegeImageEntity();
            geGeImageEntity.setImage("http://www.cilu.com.cn/climg/LOGO/logo_01.jpg");
            mv.addObject("geGeImageEntity", geGeImageEntity);
            
            // 基本商品信息
            ProductBaseEntity productBase = new ProductBaseEntity();
            mv.addObject("productBase", productBase);
            
            SellerEntity productRelationSeller = new SellerEntity();
            mv.addObject("productRelationSeller", productRelationSeller);
            
            List<CategoryEntity> categoryList = categoryService.findCategoryByProductBaseId(product.getProductBaseId());
            mv.addObject("categoryList", categoryList);
            // 商品 返分销毛利百分比类型
            mv.addObject("returnDistributionProportionType", 1);
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
            log.error(e.getMessage(), e);
        }
        return mv;
    }
    
    /**
     * 异步刷新商品库存信息
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refreshStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String refreshStock(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> rMap = productService.findProductAndCountInfoByProductId(id);
        String rId = rMap.get("id") + "";
        if (!"".equals(rId))
        {
            rMap.put("status", 1);
        }
        else
        {
            rMap.put("status", 0);
        }
        return JSON.toJSONString(rMap);
    }
    
    /**
     * 复制商品
     * @param id：商品Id,表示从此Id复制商品
     * @param productType：商品类型，1特卖商品，2商城商品，表示要复制的商品类型
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/copyProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-复制商品")
    public String copyProduct(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "productType", required = true) int productType)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int status = productService.copyProduct(id, productType, 1, null);
            if (status > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "复制成功");
            }
            else if (status == -2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id=" + id + "的商品不存在");
            }
            else if (status == -3)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id=" + id + "的特卖商品不存在");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "复制失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "复制发生异常，请刷新后重试。");
        }
        return JSON.toJSONString(resultMap);
        
    }
    
    /**
     * 批量将fromType的类型商品复制成toType类型的商品
     * 
     * @param ids：商品Id拼接的字符串
     * @param fromType：商品类型：1特卖商品，2商城商品
     * @param toType：商品类型：1特卖商品，2商城商品
     * @return
     */
    @RequestMapping(value = "/copyProductFromOtherType", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-复制商品")
    public String copyProductFromOtherType(@RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "fromType", required = true) int fromType,
        @RequestParam(value = "toType", required = true) int toType)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入基本商品Id");
            }
            else if (ids.split(",").length > 50)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "每次最多只能复制50个商品");
            }
            else
            {
                resultMap = productService.copyProductFromOtherType(ids, fromType, toType);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "复制发生异常，请刷新后重试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 增加销量
     * 
     * @param request
     * @param id
     * @param stock
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addSellNum", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-修改商品销量")
    public String addSellNum(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sellNum", required = false, defaultValue = "") String sellNumStr)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (sellNumStr.equals("") || !StringUtils.isNumeric(sellNumStr.startsWith("-") ? sellNumStr.substring(1) : sellNumStr))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "数量必填且必须为数值");
                return JSON.toJSONString(map);
            }
            
            int sellNum = Integer.valueOf(sellNumStr);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("sellNum", sellNum);
            if (sellNum < 0)
            {
                map.put("jian", 1);
                map.put("rSellNum", -sellNum);
            }
            else
            {
                map.put("jian", 0);
            }
            int resultStatus = productService.addProductSellNum(map);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
                // 商品销量修改时记录日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_SELL_COUNT.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                    logInfoMap.put("objectId", id);
                    logInfoMap.put("sellNum", sellNum);
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
                resultMap.put("msg", "总销售数量不足，无法减少" + (-sellNum));
            }
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
        
    }
    
    /**
     * 跳转到id对应的商品修改页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit/{productType}/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("productType") int productType, @PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == productType)
            {
                mv.setViewName("product/update");
            }
            else if (ProductEnum.PRODUCT_TYPE.MALL.getCode() == productType)
            {
                mv.setViewName("mallProduct/update");
            }
            // 左岸城堡
            else if (3 == productType)
            {
                mv.setViewName("product/update");
            }
            else
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.addObject("toAction", "update");
            
            // 商品信息
            ProductEntity product = productService.findProductById(id, productType);
            if (product == null)
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.addObject("returnDistributionProportionType", product.getReturnDistributionProportionType());
            mv.addObject("product", product);
            mv.addObject("productId", id);
            // 价格
            mv.addObject("marketPrice", product.getMarketPrice() + "");
            mv.addObject("salesPrice", product.getSalesPrice() + "");
            mv.addObject("partnerDistributionPrice", product.getPartnerDistributionPrice() + "");

            ProductBaseEntity productBase = productBaseService.queryProductBaseById(product.getProductBaseId());
            float cost = 0;
            if (productBase != null)
            {
                StringBuilder submitType = new StringBuilder();
                if (productBase.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
                {
                    submitType.append("供货价").append(String.format("%.2f", productBase.getWholesalePrice()));
                    cost = productBase.getWholesalePrice();
                }
                else if (productBase.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
                {
                    submitType.append("扣点").append(productBase.getDeduction()).append("%  ").append("建议价").append(productBase.getProposalPrice());
                    cost = (100 - productBase.getDeduction()) * productBase.getProposalPrice() / 100;
                }
                else if (productBase.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())
                {
                    submitType.append("自营采购价").append(productBase.getSelfPurchasePrice());
                    cost = productBase.getSelfPurchasePrice();
                }
                mv.addObject("submitType", submitType.toString());
                mv.addObject("productBase", productBase);
            }
            if (productType != 3)
            {
                float bsCommision = Float.valueOf(product.getBsCommision()).floatValue();
                float salesPrice = product.getSalesPrice();
                // 1.比例
                String bil = MathUtil.round((bsCommision / salesPrice) * 100, 2);
                // 2.对应盈亏：售价-成本价-总佣金
                double dyk = salesPrice - cost - bsCommision;
                String yk = MathUtil.round(dyk, 2);
                // 3.对应盈亏比例：（售价-成本价-总佣金）/ 成本价 * 100%
                
                if (cost == 0)
                {
                    mv.addObject("ykb", "0");
                }
                else
                {
                    mv.addObject("ykb", MathUtil.round((dyk / cost) * 100, 2));
                }
                mv.addObject("bil", bil);
                mv.addObject("yk", yk);
                // productCommon信息
                ProductCommonEntity productCommon = productService.findProductCommonByProductId(id);// 可能为null
                mv.addObject("productCommon", productCommon);
                
                // 商品信息 -关联- 商家信息
                SellerEntity productRelationSeller = sellerService.findSellerById(product.getSellerId());
                mv.addObject("productRelationSeller", productRelationSeller);
                mv.addObject("sellerType", SellerEnum.SellerTypeEnum.getDescByCode(productRelationSeller.getSellerType()));
                mv.addObject("freightType",
                    productRelationSeller.getFreightType() == 1 ? "包邮"
                        : productRelationSeller.getFreightType() == 2 ? "满" + String.format("%.2f", productRelationSeller.getFreightMoney()) + "包邮"
                            : productRelationSeller.getFreightType() == 3 ? "全部不包邮" : productRelationSeller.getFreightOther());
                productRelationSeller.setSendTimeRemark(productRelationSeller.getSendTimeType() == 4 ? productRelationSeller.getSendTimeRemark()
                    : productRelationSeller.getSendTimeType() == 0 ? "" : SellerEnum.SellerSendTimeTypeEnum.getDescByCode(productRelationSeller.getSendTimeType()));
                    
                BrandEntity brand = brandService.findBrandById(product.getBrandId());
                mv.addObject("brandName", brand == null ? "" : brand.getName());
                
                // 商品信息 -关联- 库存数量
                ProductCountEntity productCountEntity = productService.findProductCountByProductId(id);
                mv.addObject("stock", (productCountEntity == null) ? 0 : productCountEntity.getStock() + "");
                mv.addObject("productCount", (productCountEntity == null) ? "" : productCountEntity.getStock() + "");
                mv.addObject("restriction", (productCountEntity == null) ? "" : productCountEntity.getRestriction() + "");
                mv.addObject("sellAmount", (productCountEntity == null) ? 0 : productCountEntity.getSell());
            }
            else
            {
                
                // productCommon信息
                ProductCommonEntity productCommon = productService.findProductCommonByProductId(id);// 可能为null
                mv.addObject("productCommon", productCommon);
                SellerEntity productRelationSeller = sellerService.findSellerById(product.getSellerId());
                mv.addObject("productRelationSeller", productRelationSeller);
                mv.addObject("sellerType", SellerEnum.SellerTypeEnum.getDescByCode(productRelationSeller.getSellerType()));
                mv.addObject("freightType", "包邮");
                BrandEntity brand = brandService.findBrandById(product.getBrandId());
                mv.addObject("brandName", brand == null ? "" : brand.getName());
            }
            
            // 商品信息 -关联- 自定义页面
            Map<String, Object> relationPageMap = productService.findProductAndPageCustomInfo(id);
            mv.addObject("relationPageMap", relationPageMap);
            
            // 商品信息 -- 详情页
            Map<String, Object> mobileDetailMap = productService.findProductMobileDetailInfo(id);
            mv.addObject("mobileDetailMap", mobileDetailMap);
            
            // 运费模板
            List<FreightTemplateEntity> templateList = freightService.findFreightTemplateIsAvailable();
            mv.addObject("templateList", templateList);
            
            // 格格头像
            List<GegeImageEntity> imageList = geGeImageService.findAllGegeImage("product");
            mv.addObject("gegeImageList", imageList);
            
            GegeImageEntity geGeImageEntity = geGeImageService.findGegeImageById(product.getGegeImageId(), "product");
            if (geGeImageEntity == null)
            {
                geGeImageEntity = new GegeImageEntity();
            }
            if (StringUtils.isEmpty(geGeImageEntity.getImage()))
            {
                geGeImageEntity.setImage("http://m.gegejia.com:80/ygg/pages/images/userimg/gege.png");
            }
            mv.addObject("geGeImageEntity", geGeImageEntity);
            
            // 自定义页面
            List<PageCustomEntity> pList = pageCustomService.findAllPageCustomForProduct();
            // TODO 假如该商品关联的自定义页面已经过期，这里要加上。
            mv.addObject("pageCustomList", pList);
            
            List<CategoryEntity> categoryList = categoryService.findCategoryByProductBaseId(product.getProductBaseId());
            mv.addObject("categoryList", categoryList);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        
        return mv;
    }
    
    /**
     * 异步调用，返回ID对应的商家的详细信息
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajaxSellerInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxSellerInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> map = sellerService.findSellerInfoById(id);
        if ((byte)map.get("isAvailable") == (byte)0)
        {
            map.put("status", 2);
        }
        else
        {
            map.put("status", 1);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 异步调用，返回ID对应的品牌可用状态
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajaxBrandInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxBrandInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        BrandEntity brand = brandService.findBrandById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (brand.getIsAvailable() == 0)
        {
            map.put("msg", brand.getName() + "品牌已经停用");
            map.put("status", 2);
        }
        else
        {
            map.put("status", 1);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 异步调用，返回ID对应的自定义页面的可用状态
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajaxPageCustomInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxPageCustomInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        PageCustomEntity pageCustom = pageCustomService.findPageCustomById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (pageCustom.getIsAvailable() == (byte)0)
        {
            map.put("msg", "所选自定义页面已经停用");
            map.put("status", 2);
        }
        else
        {
            map.put("status", 1);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 新增商品
     * @param product
     * @param productCommon：通用商品信息
     * @param productCount：商品库存信息
     * @param detailPicAndText：详情页信息
     * @param freight：邮费方式，1：免邮，2：选择运费模板ID
     * @param freightTemplate：运费模板
     * @param pageCustomName1：自定义页面名称1
     * @param pageCustomId1：自定义页面Id1
     * @param pageCustomName2：自定义页面名称2
     * @param pageCustomId2：自定义页面Id2
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-新增商品")
    public String save(ProductEntity product, ProductCommonEntity productCommon, ProductCountEntity productCount, DetailPicAndText detailPicAndText,
        @RequestParam(value = "freight", required = true) int freight, @RequestParam(value = "freightTemplate", required = false, defaultValue = "0") int freightTemplate,
        @RequestParam(value = "pageCustomName1", required = false, defaultValue = "") String pageCustomName1,
        @RequestParam(value = "pageCustomId1", required = false, defaultValue = "0") int pageCustomId1,
        @RequestParam(value = "pageCustomName2", required = false, defaultValue = "") String pageCustomName2,
        @RequestParam(value = "pageCustomId2", required = false, defaultValue = "0") int pageCustomId2)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            
            // 设置运费模板
            int defaultFreightTemplate = CommonConstant.defaultFreightTemplate;
            if ((freight != 1) && (freightTemplate != 0))
            {
                defaultFreightTemplate = freightTemplate;
            }
            product.setFreightTemplateId(defaultFreightTemplate);
            
            // 特卖商品，设置特卖开始和结束时间
            if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                if ("".equals(product.getStartTime()))
                {
                    product.setStartTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                }
                if ("".equals(product.getEndTime()))
                {
                    product.setEndTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                }
                if (product.getActivitiesStatus() == ProductEnum.PRODUCT_ACTIVITY_STATUS.GROUP_BUY.getCode())
                {
                    Date startTime = CommonUtil.string2Date(product.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                    if (startTime.compareTo(new Date()) < 1)
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", "商品特卖开始时间不在当前时间之后，添加到团购商品失败");
                        log.info("新增特卖商品--商品特卖开始时间不在当前时间之后，添加到团购商品失败！！！");
                        return JSON.toJSONString(resultMap);
                    }
                }
            }
            else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                product.setStartTime("0000-00-00 00:00:00");
                product.setEndTime("0000-00-00 00:00:00");
                
            }
            
            // 商品停用，则设为下架状态
            if (product.getIsAvailable() == 0)
            {
                product.setIsOffShelves((byte)1);
            }
            // 商品信息
            map.put("product", product);
            
            // 商品常用信息 商品图片后缀更换
            productCommon.setSmallImage(product.getImage1());
            if (productCommon.getSmallImage().indexOf(ImageUtil.getPrefix()) > 0)
            {
                String image = productCommon.getSmallImage().substring(0, productCommon.getSmallImage().indexOf(ImageUtil.getPrefix()));
                productCommon.setSmallImage(image);
            }
            productCommon.setMediumImage(product.getImage1());
            if (productCommon.getMediumImage().indexOf(ImageUtil.getPrefix()) > 0)
            {
                String image = productCommon.getSmallImage().substring(0, productCommon.getMediumImage().indexOf(ImageUtil.getPrefix()));
                productCommon.setMediumImage(image);
            }
            map.put("productCommon", productCommon);
            // 商品数量表
            map.put("productCount", productCount);
            
            // 自定义页面关联
            RelationProductAndPageCustom relation1 = null;
            RelationProductAndPageCustom relation2 = null;
            if (!"".equals(pageCustomName1) && (pageCustomId1 != 0))
            {
                relation1 = new RelationProductAndPageCustom();
                relation1.setMarks(pageCustomName1);
                relation1.setPageCustomId(pageCustomId1);
                map.put("relation1", relation1);
            }
            if (!"".equals(pageCustomName2) && (pageCustomId2 != 0))
            {
                relation2 = new RelationProductAndPageCustom();
                relation2.setMarks(pageCustomName2);
                relation2.setPageCustomId(pageCustomId2);
                map.put("relation2", relation2);
            }
            
            map.put("detailPicAndText", detailPicAndText);
            int result = productService.saveProduct(map);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后重试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 异步保存商品信息
     * @param request
     * @param product
     * @param productCommon
     * @param productCount
     * @param detailPicAndText
     * @param productCommonId：若没有，新建
     * @param relationProAndPageId1
     * @param relationProAndPageId2
     * @param freight：邮费方式，1：免邮，2：选择运费模板ID
     * @param freightTemplate：运费模板ID，若==0，免邮
     * @param pageCustomName1
     * @param pageCustomId1：没有则不加
     * @param pageCustomName2
     * @param pageCustomId2：没有则不加
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-编辑商品")
    public String update(HttpServletRequest request, ProductEntity product, ProductCommonEntity productCommon, ProductCountEntity productCount, DetailPicAndText detailPicAndText,
        @RequestParam(value = "productCommonId", required = false, defaultValue = "0") int productCommonId,
        @RequestParam(value = "relationProAndPageId1", required = false, defaultValue = "0") int relationProAndPageId1,
        @RequestParam(value = "relationProAndPageId2", required = false, defaultValue = "0") int relationProAndPageId2,
        @RequestParam(value = "freight", required = true) int freight, @RequestParam(value = "freightTemplate", required = false, defaultValue = "0") int freightTemplate,
        @RequestParam(value = "pageCustomName1", required = false, defaultValue = "") String pageCustomName1,
        @RequestParam(value = "pageCustomId1", required = false, defaultValue = "0") int pageCustomId1,
        @RequestParam(value = "pageCustomName2", required = false, defaultValue = "") String pageCustomName2,
        @RequestParam(value = "pageCustomId2", required = false, defaultValue = "0") int pageCustomId2)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            
            Map<String, Object> map = new HashMap<String, Object>();
            // 设置运费模板
            int defaultFreightTemplate = CommonConstant.defaultFreightTemplate;
            if ((freight != 1) && (freightTemplate != 0))
            {
                defaultFreightTemplate = freightTemplate;
            }
            product.setFreightTemplateId(defaultFreightTemplate);
            
            if (!(product.getSalesPrice() * 0.6f <= product.getPartnerDistributionPrice() && product.getPartnerDistributionPrice() <= product.getSalesPrice()))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
                log.info("修改商品--分销价不满足条件，修改失败！！！！（分销价：" + product.getPartnerDistributionPrice() + ",售价：" + product.getSalesPrice() + "）");
                return JSON.toJSONString(resultMap);
            }
            
            // 特卖商品设置特卖时间
            if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == product.getType())
            {
                if ("".equals(product.getStartTime()))
                {
                    product.setStartTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                }
                if ("".equals(product.getEndTime()))
                {
                    product.setEndTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                }
                if (product.getActivitiesStatus() == ProductEnum.PRODUCT_ACTIVITY_STATUS.GROUP_BUY.getCode())
                {
                    Date startTime = CommonUtil.string2Date(product.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                    if (startTime.compareTo(new Date()) < 1)
                    {
                        resultMap.put("status", 0);
                        resultMap.put("msg", "商品特卖开始时间不在当前时间之后，添加到团购商品失败");
                        log.info("修改特卖商品--商品特卖开始时间不在当前时间之后，添加到团购商品失败！！！");
                        return JSON.toJSONString(resultMap);
                    }
                }
            }
            // 商城商品设置时间
            else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                product.setStartTime("0000-00-00 00:00:00");
                product.setEndTime("0000-00-00 00:00:00");
                
            }
            // 商品停用，则设为下架状态
            if (product.getIsAvailable() == 0)
            {
                product.setIsOffShelves((byte)1);
            }
            // 商品信息
            map.put("product", product);
            // 商品常用信息
            productCommon.setId(productCommonId);
            // 商品图片后缀更换
            productCommon.setSmallImage(product.getImage1());
            if (productCommon.getSmallImage().indexOf(ImageUtil.getPrefix()) > 0)
            {
                String image = productCommon.getSmallImage().substring(0, productCommon.getSmallImage().indexOf(ImageUtil.getPrefix()));
                productCommon.setSmallImage(image);
            }
            map.put("productCommon", productCommon);
            // 商品数量表
            map.put("productCount", productCount);
            // 自定义页面关联
            RelationProductAndPageCustom relation1 = new RelationProductAndPageCustom();
            relation1.setMarks(pageCustomName1);
            relation1.setPageCustomId(pageCustomId1);
            relation1.setId(relationProAndPageId1);
            map.put("relation1", relation1);
            RelationProductAndPageCustom relation2 = new RelationProductAndPageCustom();
            relation2.setMarks(pageCustomName2);
            relation2.setPageCustomId(pageCustomId2);
            relation2.setId(relationProAndPageId2);
            map.put("relation2", relation2);
            // mobileDetail
            map.put("detailPicAndText", detailPicAndText);
            
            ProductEntity pe = productService.findProductById(product.getId());
            int result = productService.updateProduct(map);
            
            if (result == 1)
            {
                resultMap.put("status", 1);
                // 记录日志
                if (pe != null)
                {
                    try
                    {
                        Map<String, Object> logInfoMap = new HashMap<String, Object>();
                        logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                        logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                        if (pe.getSellerId() != product.getSellerId())// 商品商家名称修改时记录日志
                        {
                            SellerEntity oldSeller = sellerService.findSellerById(pe.getSellerId());
                            SellerEntity newSeller = sellerService.findSellerById(product.getSellerId());
                            logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_SELLER_NAME.ordinal());
                            logInfoMap.put("objectId", product.getId());
                            logInfoMap.put("oldSeller", oldSeller == null ? "" : oldSeller.getSellerName());
                            logInfoMap.put("newSeller", newSeller == null ? "" : newSeller.getSellerName());
                            logService.logger(logInfoMap);
                        }
                        if (pe.getIsAvailable() != product.getIsAvailable())// 商品状态改为可用或停用记录日志
                        {
                            logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_STATUS.ordinal());
                            logInfoMap.put("objectId", product.getId());
                            logInfoMap.put("newStatus", product.getIsAvailable() == 1 ? "可用" : "停用");
                            logService.logger(logInfoMap);
                        }
                        // 特卖商品记录特卖时间是否修改
                        if (ProductEnum.PRODUCT_TYPE.SALE.getCode() == product.getType())
                        {
                            if (!pe.getStartTime().equals(product.getStartTime()) || !pe.getEndTime().equals(product.getEndTime()))// 商品销售时间修改记录日志
                            {
                                String oldTime =
                                    pe.getStartTime().substring(0, pe.getStartTime().lastIndexOf(".")) + "~" + pe.getEndTime().substring(0, pe.getEndTime().lastIndexOf("."));
                                String newTime = product.getStartTime() + "~" + product.getEndTime();
                                logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_TIME.ordinal());
                                logInfoMap.put("objectId", product.getId());
                                logInfoMap.put("oldTime", oldTime);
                                logInfoMap.put("newTime", newTime);
                                logService.logger(logInfoMap);
                            }
                        }
                        // 记录价格相关的修改
                        /** 销售价 */
                        if (pe.getSalesPrice() != product.getSalesPrice())
                        {
                            Map<String, Object> logInfo = new HashMap<String, Object>();
                            logInfo.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                            logInfo.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_PRICE.ordinal());
                            logInfo.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                            logInfo.put("objectId", product.getId());
                            logInfo.put("oldSalesPrice", pe.getSalesPrice());
                            logInfo.put("newSalesPrice", product.getSalesPrice());
                            logService.logger(logInfo);
                        }
                        /** 合伙人分销价 */
                        if (pe.getPartnerDistributionPrice() != product.getPartnerDistributionPrice())
                        {
                            Map<String, Object> logInfo = new HashMap<String, Object>();
                            logInfo.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                            logInfo.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_PRICE.ordinal());
                            logInfo.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                            logInfo.put("objectId", product.getId());
                            logInfo.put("oldPartnerDistributionPrice", pe.getPartnerDistributionPrice());
                            logInfo.put("newPartnerDistributionPrice", product.getPartnerDistributionPrice());
                            logService.logger(logInfo);
                        }
                        /** 行动派分销佣金 */
                        if (!pe.getBsCommision().equalsIgnoreCase(product.getBsCommision()))
                        {
                            Map<String, Object> logInfo = new HashMap<String, Object>();
                            logInfo.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                            logInfo.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_PRICE.ordinal());
                            logInfo.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                            logInfo.put("objectId", product.getId());
                            logInfo.put("oldBsCommision", pe.getBsCommision());
                            logInfo.put("newBsCommision", product.getBsCommision());
                            logService.logger(logInfo);
                        }
                    }
                    catch (Exception e)
                    {
                        log.error(e.getMessage(), e);
                    }
                    
                }
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
            resultMap.put("msg", "服务器发生异常，请刷新后再试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 上下架商品
     * 
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/forSale", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-商品上下架")
    public String forSale(@RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code,
        @RequestParam(value = "relationIds", required = false, defaultValue = "") String relationIds // 活动商品关联表Id
    )
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
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
            if (code == 1 && StringUtils.isNotEmpty(relationIds))
            { // 下架不展示
                List<String> relationIdList = Splitter.on(",").splitToList(relationIds);
                para.put("idList", relationIdList);
                para.put("isDisplay", 0);
                activitiesCommonService.updateProductDisplayStatus(para);
            }
            para.put("code", code);
            para.put("idList", idList);
            // 结果
            int resultStatus = productService.forSale(para);
            if (resultStatus > 0)
            {
                resultMap.put("status", 1);
                // 商品上、下架记录日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_SALE_STATUS.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                    logInfoMap.put("objectId", ids);
                    logInfoMap.put("newStatus", code == 0 ? "上架" : "下架");
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
                resultMap.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请刷新后再试.");
        }
        return JSON.toJSONString(resultMap);
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
    @ControllerLog(description = "商品管理-设置商品可用状态")
    public String forAvailable(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
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
            int resultStatus = productService.forAvailable(para);
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
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请稍后再试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /** 修改商品 */
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-修改商品信息")
    public String updateProduct(@RequestParam(value = "salesPrice", required = false, defaultValue = "-1") double salesPrice,
        @RequestParam(value = "marketPrice", required = false, defaultValue = "-1") double marketPrice,
        @RequestParam(value = "shortName", required = false, defaultValue = "") String shortName, @RequestParam(value = "name", required = false, defaultValue = "") String name,
        @RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            if (!"".equals(shortName))
            {
                para.put("shortName", shortName);
            }
            if (!"".equals(name))
            {
                para.put("name", name);
            }
            ProductEntity product = productService.findProductById(id);
            
            // 如果同时修改市场价和售价，则判断输入的售价是否小于市场价
            if (marketPrice >= 0 && salesPrice >= 0)
            {
                if (new BigDecimal(marketPrice).compareTo(new BigDecimal(salesPrice)) <= 0)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "售价必须小于市场价");
                    return JSON.toJSONString(resultMap);
                }
                if (!(salesPrice * 0.6f <= product.getPartnerDistributionPrice() && product.getPartnerDistributionPrice() <= salesPrice))
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "修改后分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
                    return JSON.toJSONString(resultMap);
                }
                para.put("marketPrice", marketPrice);
                para.put("salesPrice", salesPrice);
            }
            int result = productService.updateProductPara(para, true);
            
            if (result == 1)
            {
                resultMap.put("status", 1);
                // 商品价格有修改时记录日志
                logService.loggerUpdate("修改商品信息", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal(), CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal(),
                    CommonEnum.OperationTypeEnum.MODIFY_PRODUCT.ordinal(), product, para);
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
    
    /** 批量修改特卖商品价格 */
    @RequestMapping(value = "/batchUpdateProductPrice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-批量修改特卖商品价格")
    public ResultEntity batchUpdateProductPrice(@RequestParam(value = "salesPrice", required = false, defaultValue = "-1") double salesPrice,
        @RequestParam(value = "marketPrice", required = false, defaultValue = "-1") double marketPrice, @RequestParam(value = "ids", required = true) String ids)
    {
        try
        {
            Map<String, Object> para = new HashMap<>();
            List<String> idSplitter = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(ids);
            List<Integer> idList = new ArrayList<>(idSplitter.size());
            for (String id : idSplitter)
            {
                idList.add(Integer.valueOf(id));
            }
            para.put("ids", idList);
            if (marketPrice > 0)
                para.put("marketPrice", marketPrice);
            if (salesPrice > 0)
                para.put("salesPrice", salesPrice);
                
            List<ProductEntity> products = productService.batchFindProductByIds(idList);
            
            // 如果同时修改市场价和售价，则判断输入的售价是否小于市场价
            int productBaseId = products.get(0).getProductBaseId();
            for (ProductEntity product : products)
            {
                checkArgument(product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode(), "只能批量修改特卖商品 商品id：" + product.getId());
                checkArgument(product.getProductBaseId() == productBaseId, "选中的特卖商品应都对应一个基本商品 base id ：" + productBaseId);
                double marketPrice_ = marketPrice > 0 ? marketPrice : product.getMarketPrice();
                double salesPrice_ = salesPrice > 0 ? salesPrice : product.getSalesPrice();
                checkArgument(new BigDecimal(marketPrice_).compareTo(new BigDecimal(salesPrice_)) > 0, "售价必须小于市场价");
                if (salesPrice > 0)
                    checkArgument(salesPrice * 0.6f <= product.getPartnerDistributionPrice() && product.getPartnerDistributionPrice() <= salesPrice,
                        "修改后分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存 分销价：" + product.getPartnerDistributionPrice());
            }
            
            if (productService.updateProductPara(para, true) == 1)
            {
                // 商品价格有修改时记录日志
                try
                {
                    for (ProductEntity product : products)
                    {
                        logService.loggerUpdate("修改商品价格", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal(), CommonEnum.BusinessTypeEnum.PRODUCT_MANAGEMENT.ordinal(),
                            CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_PRICE.ordinal(), product, para);
                    }
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
            else
            {
                return ResultEntity.getFailResult("修改失败");
            }
        }
        catch (IllegalArgumentException ie)
        {
            log.error(ie.getMessage(), ie);
            return ResultEntity.getFailResult(ie.getMessage());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return ResultEntity.getFailResult("服务器发生异常，请稍后再试。");
        }
        return ResultEntity.getSuccessResult();
    }
    
    /**
     * 批量修改商品温馨提示
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/batchEditTip", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-批量修改商品温馨提示")
    public String batchEditTip(HttpServletRequest request, @RequestParam(value = "tip", required = false, defaultValue = "") String tip,
        @RequestParam(value = "ids", required = true) String ids)
            throws Exception
    {
        if ("".equals(tip))
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "温馨提示为空");
            return JSON.toJSONString(resultMap);
        }
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
        para.put("tip", tip);
        para.put("idList", idList);
        try
        {
            int result = productService.batchUpdateProductTip(idList, tip);
            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (result > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "成功保存" + result + "条");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error("批量修改商品数据失败！", e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败，温馨提示字数超过范围了哦，检查检查是哪个，把他踢掉，重来下。");
            return JSON.toJSONString(resultMap);
        }
        
    }
    
    /**
     * 获得商品信息
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/findProductInfoById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findProductInfoById(HttpServletRequest request, int id, @RequestParam(value = "type", required = false, defaultValue = "0") int type)
        throws Exception
    {
        ProductEntity p = null;
        if (type == 0)
        {
            p = productService.findProductById(id);
        }
        else
        {
            p = productService.findProductById(id, type);
        }
        if (p == null)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "无此商品信息");
            return JSON.toJSONString(result);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        result.put("msg", p.getName());
        result.put("image1", p.getImage1());
        result.put("shortName", p.getShortName());
        result.put("name", p.getName());
        return JSON.toJSONString(result);
    }
    
    /**
     * 开启/关闭商品库存不足邮件提醒
     * @param request
     * @param ids
     * @param code：1开启，0关闭
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/changeEmailRemind", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-开启/关闭商品库存不足邮件提醒")
    public String changeEmailRemind(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> idList = new ArrayList<Integer>();
            String[] arr = ids.split(",");
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
            para.put("code", code);
            para.put("idList", idList);
            // 结果
            int resultStatus = productService.changeEmailRemind(para);
            if (resultStatus > 0)
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
    
    /**
     * 格格福利商品列表
     * @return
     * @throws Exception
     */
    @RequestMapping("/gegeWelfare")
    public ModelAndView gegeWelfare()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("product/gegeWelfare");
        return mv;
    }
    
    /**
     * 异步获取格格福利商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonGegeWelfare", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGegeWelfare(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(productName))
        {
            para.put("productName", "%" + productName + "%");
        }
        
        if (productId != 0)
        {
            para.put("productId", productId);
        }
        
        Map<String, Object> info = productService.findGegeWelfareInfo(para);
        return JSON.toJSONString(info);
    }
    
    /**
     * 保存格格福利商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveGegeWelfare", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-新增格格福利商品")
    public String saveGegeWelfare(HttpServletRequest request, //
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "salesPrice", required = false, defaultValue = "0") double salesPrice, // 格格价
        @RequestParam(value = "limitPrice", required = false, defaultValue = "0") double limitPrice, // 最低消费
        @RequestParam(value = "limitNum", required = false, defaultValue = "0") int limitNum, // 限购件数
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品id
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "brandIds", required = false, defaultValue = "") String brandIds, // 品牌
        @RequestParam(value = "payTimeBegin", required = false, defaultValue = "0000-00-00 00:00:00") String payTimeBegin, // 付款开始时间
        @RequestParam(value = "payTimeEnd", required = false, defaultValue = "0000-00-00 00:00:00") String payTimeEnd)
            throws Exception
    {
        try
        {
            Map<String, Object> info = productService.saveOrUpdateGegeWelfareProduct(id, productId, salesPrice, limitPrice, limitNum, remark, brandIds, payTimeBegin, payTimeEnd);
            return JSON.toJSONString(info);
        }
        catch (Exception e)
        {
            log.error("保存格格福利商品失败！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 批量删除格格福利商品
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteGegeWelfare", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-删除格格福利商品")
    public String deleteGegeWelfare(HttpServletRequest request, //
        @RequestParam(value = "productIds", required = false, defaultValue = "") String productIds)
            throws Exception
    {
        try
        {
            String[] arr = productIds.split(",");
            List<Integer> productIdList = new ArrayList<Integer>();
            for (String it : arr)
            {
                productIdList.add(Integer.parseInt(it));
            }
            Map<String, Object> info = productService.deleteGegeWelfareProduct(productIdList);
            return JSON.toJSONString(info);
        }
        catch (Exception e)
        {
            log.error("删除格格福利商品失败！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 
     * @param request
     * @param page
     * @param rows
     * @param productType:商品类型，1特卖商品，2商城商品
     * @param code：商品编码
     * @param name：商品名称
     * @param desc：格格说
     * @param searchId：商品Id
     * @param isAvailable：是否可用，1可用，0不可用
     * @param isOffShelves：是否下架：1下架，0上架
     * @param startTimeBegin：特卖商品开始时间起
     * @param startTimeEnd：特卖商品开始时间止
     * @param endTimeBegin：特卖商品结束时间起
     * @param endTimeEnd：特卖商品结束时间起
     * @param sellerType：商家发货类型
     * @param sellerId：商家Id
     * @param brandId：品牌Id
     * @param baseId：基本商品Id
     * @param categoryFirstId：一级分类Id
     * @param categorySecondId：二级分类Id
     * @param categoryThirdId：三级分类Id
     * @return
     */
    @RequestMapping(value = "/jsonMallProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonMallProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "productType", required = true) int productType, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "searchId", required = false, defaultValue = "") String searchId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable, //
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "-1") int isOffShelves, //
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin, //
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd, //
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin, //
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd, //
        @RequestParam(value = "sendType", required = false, defaultValue = "-1") int sellerType, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId,//
        @RequestParam(value = "baseId", required = false, defaultValue = "0") int baseId,//
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId,//
        @RequestParam(value = "categorySecondId", required = false, defaultValue = "0") int categorySecondId,//
        @RequestParam(value = "categoryThirdId", required = false, defaultValue = "0") int categoryThirdId,//
        @RequestParam(value = "isShowInMall", required = false, defaultValue = "-1") int isShowInMall,//
        @RequestParam(value = "lowerPrice", required = false, defaultValue = "0") double lowerPrice,//
        @RequestParam(value = "higherPrice", required = false, defaultValue = "0") double higherPrice,
        @RequestParam(value = "productUseScopeId", required = false, defaultValue = "-1") int productUseScopeId
    )
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
            para.put("productType", productType);
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
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (!"".equals(desc))
            {
                para.put("desc", "%" + desc + "%");
            }
            if (!"".equals(searchId))
            {
                para.put("id", searchId);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (isOffShelves != -1)
            {
                para.put("isOffShelves", isOffShelves);
            }
            if (!"".equals(startTimeBegin))
            {
                para.put("startTimeBegin", startTimeBegin);
            }
            if (!"".equals(startTimeEnd))
            {
                para.put("startTimeEnd", startTimeEnd);
            }
            if (!"".equals(endTimeBegin))
            {
                para.put("endTimeBegin", endTimeBegin);
            }
            if (!"".equals(endTimeEnd))
            {
                para.put("endTimeEnd", endTimeEnd);
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            if (sellerType != -1)
            {
                para.put("sellerType", SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(sellerType).getCode());
                // 发货类型为：香港（身份证照片）暂时不用，查询是不用区分香港（仅身份证号）和香港（身份证照片）
                /*
                 * if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()) { para.put("isNeedIdcardImage",
                 * 1); para.put("isNeedIdcardNumber", 1); }
                 */
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            if (baseId != 0)
            {
                para.put("baseId", baseId);
            }
            if (categoryFirstId != 0)
            {
                para.put("categoryFirstId", categoryFirstId);
            }
            if (categorySecondId != 0)
            {
                para.put("categorySecondId", categorySecondId);
            }
            if (categoryThirdId != 0)
            {
                para.put("categoryThirdId", categoryThirdId);
            }
            if (isShowInMall != -1)
            {
                para.put("isShowInMall", isShowInMall);
            }
            if (lowerPrice > 0)
            {
                para.put("lowerPrice", lowerPrice);
            }
            if (higherPrice > 0)
            {
                para.put("higherPrice", higherPrice);
            }
            if( productUseScopeId >= 1 ) {
                para.put("productUseScopeId", productUseScopeId);
            }
            resultMap = productService.jsonMallProductForManage(para);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            log.error("异步加载商城商品列表出错了");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 放入或从商城中移除
     * 
     * @param ids
     * @param isShow
     * @return
     */
    @RequestMapping(value = "/updateShowInMall", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-放入或从商城中移除")
    public String updateShowInMall(@RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "isShow", required = true) int isShow)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isShow", isShow);
            para.put("type", 2);
            para.put("idList", Arrays.asList(ids.split(",")));
            // 结果
            int resultStatus = productService.updateShowInMall(para);
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
    
    @RequestMapping(value = "/classifyProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-商品分类")
    public String classifyProduct(@RequestParam(value = "baseIds", required = true) String baseIds, @RequestParam(value = "categoryIds", required = true) String categoryIds)
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
            int resultStatus = productService.classifyProduct(addCategoryList);
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
    
    /**
     * 导出商品图片压缩包
     * 
     * @param response
     * @param productId
     * @throws Exception
     */
    @RequestMapping(value = "/exportProductImage")
    @ControllerLog(description = "商品管理-导出商品图片压缩包")
    public void exportProductImage(HttpServletResponse response, @RequestParam(value = "productId", required = false, defaultValue = "") int productId)
        throws Exception
    {
        OutputStream servletOutPutStream = null;
        try
        {
            String dowName = "商品ID：" + productId;
            String result = productService.exportProductImage(productId);
            String zipName = result + ".zip";
            ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
            zca.compressExe(result);
            FileInputStream downFile = new FileInputStream(zipName);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
            servletOutPutStream = response.getOutputStream();
            // 设置缓冲区为1024个字节，即1KB
            byte bytes[] = new byte[1024];
            int len = 0;
            // 读取数据。返回值为读入缓冲区的字节总数,如果到达文件末尾，则返回-1
            while ((len = downFile.read(bytes)) != -1)
            {
                // 将指定 byte数组中从下标 0 开始的 len个字节写入此文件输出流,(即读了多少就写入多少)
                servletOutPutStream.write(bytes, 0, len);
            }
            servletOutPutStream.close();
            downFile.close();
            FileUtil.deleteFile(result);
            FileUtil.deleteFile(zipName);
        }
        catch (Exception e)
        {
            String errorStr = "<script>alert('数据异常，检查商品ID是否正确');window.history.back();</script>";
            if (e.getMessage().indexOf("系统找不到指定的文件") > -1)
            {
                errorStr = "<script>alert('无数据');window.history.back();</script>";
            }
            else
            {
                log.error(e.getMessage(), e);
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Content-disposition", "");
            if (servletOutPutStream == null)
            {
                servletOutPutStream = response.getOutputStream();
            }
            servletOutPutStream.write(errorStr.getBytes());
            servletOutPutStream.close();
            return;
        }
        
    }
    
    /**
     * 下载图片页面
     * 
     * @return
     */
    @RequestMapping(value = "/downProductImage")
    public ModelAndView downProductImage()
    {
        return new ModelAndView("product/downProductImage");
    }
    
    /**
     * 库存列表页面
     * 
     * @return
     */
    @RequestMapping(value = "/stock")
    public ModelAndView stockList()
    {
        return new ModelAndView("product/stockList");
    }
    
    /**
     * 异步加载库存告急商品列表
     * 
     * @param page
     * @param rows
     * @param productId：商品Id
     * @param productName：商品名称
     * @return
     */
    @RequestMapping(value = "/jsonProductStockInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductStockInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName)
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
            if (productId != 0)
            {
                para.put("id", productId);
                
            }
            if (!"".equals(productName))
            {
                para.put("name", "%" + productName + "%");
            }
            resultMap = productService.jsonProductStockInfo(para);
        }
        catch (Exception e)
        {
            log.error("异步加载库存告急商品列表出错了", e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/exportStockList")
    @ControllerLog(description = "商品管理-导出商品库存信息")
    public void exportStockList(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName)
            throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (productId != 0)
            {
                para.put("id", productId);
                
            }
            if (!"".equals(productName))
            {
                para.put("name", "%" + productName + "%");
            }
            List<Map<String, String>> rows = productService.exportStockList(para);
            String[] title = {"商品类型", "商品ID", "长名称", "备注", "特卖开始时间", "特卖结束时间", "剩余库存", "锁定库存", "可用库存", "售价", "商家", "发货地"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, String> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("typeStr") == null ? "" : it.get("typeStr") + "");
                r.createCell(cellIndex++).setCellValue(it.get("id") == null ? "" : it.get("id") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productName") == null ? "" : it.get("productName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("remark") == null ? "" : it.get("remark") + "");
                r.createCell(cellIndex++).setCellValue(it.get("startTime") == null ? "" : it.get("startTime") + "");
                r.createCell(cellIndex++).setCellValue(it.get("endTime") == null ? "" : it.get("endTime") + "");
                r.createCell(cellIndex++).setCellValue(it.get("stock") == null ? "" : it.get("stock") + "");
                r.createCell(cellIndex++).setCellValue(it.get("lock") == null ? "" : it.get("lock") + "");
                r.createCell(cellIndex++).setCellValue(it.get("availableStock") == null ? "" : it.get("availableStock") + "");
                r.createCell(cellIndex++).setCellValue(it.get("salesPrice") == null ? "" : it.get("salesPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sellerName") == null ? "" : it.get("sellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sendAddress") == null ? "" : it.get("sendAddress") + "");
            }
            String fileName = "库存告急商品";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
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
    
    @RequestMapping(value = "/checkIsInMall", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkIsInMall(@RequestParam(value = "productId", required = true) int productId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            boolean isExist = productService.checkProductIsInMall(productId);
            if (isExist)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "未放入商城或没有三级分类不能排序");
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
    
    @RequestMapping(value = "/updateProductRemark", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateProductRemark(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
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
            return productService.updateProductRemark(ids, remark);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常，请稍后再试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新人商品专区列表
     * @return
     */
    @RequestMapping("/newbieList")
    public ModelAndView newbieList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("product/newbieList");
        return mv;
    }
    
    /**
     * 异步获取新人商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProductNewbie", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object jsonProductNewbie(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "productName", required = false, defaultValue = "") String productName)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(productName))
        {
            para.put("productName", "%" + productName + "%");
        }
        return productService.findProductNewbieInfo(para);
    }
    
    /**
     * 保存新人商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrUpdateProductNewbie", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-新增新人专享商品")
    public String saveOrUpdateProductNewbie(@RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "salesPrice", required = false, defaultValue = "0") double salesPrice,//格格价
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,// 商品id
        @RequestParam(value = "isDisplay", required = false, defaultValue = "") int isDisplay)
        throws Exception
    {
        try
        {
            return productService.saveOrUpdateProductNewbie(id, productId, salesPrice, isDisplay);
        }
        catch (Exception e)
        {
            log.error("保存新人专享商品失败！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 批量删除新人专享商品
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteProductNewbie", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-删除新人专享商品")
    public String deleteProductNewbie(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
        throws Exception
    {
        try
        {
            return productService.deleteProductNewbie(ids);
        }
        catch (Exception e)
        {
            log.error("删除新人专享商品失败！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateProductNewbieSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-更新新人专享商品排序值")
    public String updateProductNewbieSequence(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "sequence", required = false, defaultValue = "0") int sequence)
        throws Exception
    {
        try
        {
            return productService.updateProductNewbieSequence(id, sequence);
        }
        catch (Exception e)
        {
            log.error("更新新人专享商品排序值失败！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateProductNewbieDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-更新新人专享商品展现状态")
    public String updateProductNewbieDisplayStatus(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay)
        throws Exception
    {
        try
        {
            return productService.updateProductNewbieDisplayStatus(id, isDisplay);
        }
        catch (Exception e)
        {
            log.error("更新新人专享商品展现状态失败！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }

    @RequestMapping(value = "/updateProductActivityWholesalePrice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-修改商品活动供货价")
    public Object updateProductActivityWholesalePrice(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "startTime", required = false, defaultValue = "0") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "0") String endTime,
        @RequestParam(value = "wholesalePrice", required = false, defaultValue = "0") float wholesalePrice)
    {
        try
        {
            return productService.updateProductActivityWholesalePrice(id, startTime, endTime, wholesalePrice);
        }
        catch (Exception e)
        {
            log.error(String.format("修改商品供货价失败,id=%d,startTime=%s,endTime=%s,wholesalePrice=%f", id, startTime, endTime, wholesalePrice), e);
            return ResultEntity.getFailResult("修改失败");
        }
    }
}
