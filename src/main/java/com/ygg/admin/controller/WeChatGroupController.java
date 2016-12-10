package com.ygg.admin.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.CategoryEntity;
import com.ygg.admin.entity.DetailPicAndText;
import com.ygg.admin.entity.FreightTemplateEntity;
import com.ygg.admin.entity.GegeImageEntity;
import com.ygg.admin.entity.MwebGroupImageDetailEntity;
import com.ygg.admin.entity.MwebGroupProductEntity;
import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.BrandService;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.FreightService;
import com.ygg.admin.service.MwebGroupImageDetailService;
import com.ygg.admin.service.MwebGroupProductCountService;
import com.ygg.admin.service.MwebGroupProductInfoService;
import com.ygg.admin.service.MwebGroupProductService;
import com.ygg.admin.service.ProductBaseService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SellerService;

@Controller
@RequestMapping("/wechatGroup")
public class WeChatGroupController
{
    Logger log = Logger.getLogger(WeChatGroupController.class);
    
    @Resource
    private MwebGroupProductService mwebGroupProductService;
    
    @Resource
    private MwebGroupImageDetailService mwebGroupImageDetailService;
    
    @Resource
    private MwebGroupProductCountService mwebGroupProductCountService;
    
    @Resource
    private MwebGroupProductInfoService mwebGroupProductInfoService;
    
    @Resource
    private SellerService sellerService;
    
    @Resource
    private BrandService brandService;
    
    @Resource
    private FreightService freightService;
    
    // @Resource
    // private PageCustomService pageCustomService;
    
    @Resource
    private ProductService productService;
    
    @Resource
    private ProductBaseService productBaseService;
    
    @Resource
    private CategoryService categoryService;
    
    // @Resource
    // private GegeImageService geGeImageService;
    
    // @Resource
    // private SystemLogService logService;
    
    // @Resource
    // private ProductBaseService productBaseService;
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("listType", isAvailable == 1 ? "可用商品" : "停用商品");
        mv.addObject("isAvailable", isAvailable);
        try
        {
            mv.setViewName("wechatGroup/list");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping("/listTeam")
    public ModelAndView listTeam(HttpServletRequest request, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("wechatGroup/teamManager");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping("/jsonProductInfo")
    @ResponseBody
    public Object jsonProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "isAvailable", required = true, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId,
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "-1") int isOffShelves,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin,
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd, @RequestParam(value = "code", required = false, defaultValue = "") String code)
            throws Exception
    {
        
        JSONObject jsonObject = new JSONObject();
        
        if (page == 0)
        {
            page = 1;
        }
        jsonObject.put("start", rows * (page - 1));
        jsonObject.put("max", rows);
        
        if (StringUtils.isNotEmpty(name))
        {
            jsonObject.put("name", name);
        }
        if (productId != 0)
        {
            jsonObject.put("productId", productId);
        }
        if (id != 0)
        {
            jsonObject.put("id", id);
        }
        if (sellerId != 0)
        {
            jsonObject.put("sellerId", sellerId);
        }
        if (productBaseId != 0)
        {
            jsonObject.put("productBaseId", productBaseId);
        }
        if (StringUtils.isNotEmpty(startTimeBegin))
        {
            jsonObject.put("startTimeBegin", startTimeBegin);
        }
        if (StringUtils.isNotEmpty(startTimeEnd))
        {
            jsonObject.put("startTimeEnd", startTimeEnd);
        }
        if (StringUtils.isNotEmpty(endTimeBegin))
        {
            jsonObject.put("endTimeBegin", endTimeBegin);
        }
        if (StringUtils.isNotEmpty(endTimeEnd))
        {
            jsonObject.put("endTimeEnd", endTimeEnd);
        }
        if (isOffShelves != -1)
        {
            jsonObject.put("isOffShelves", isOffShelves);
        }
        if (isAvailable != -1)
        {
            jsonObject.put("isAvailable", isAvailable);
        }
        if (StringUtils.isNotEmpty(code))
        {
            jsonObject.put("code", code);
        }
        
        return mwebGroupProductService.findProductAndStock2(jsonObject);
    }
    
    /**
     * 转发到新增商品页面
     * 
     * @param request
     * @return
     */
    @RequestMapping("/add/{type}")
    public ModelAndView add(@PathVariable("type") String type, HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        
        if (type.equals("0"))
        {
            mv.setViewName("wechatGroup/add");
        }
        else if (type.equals("1"))
        {
            mv.setViewName("wechatGroup/addAutoTeam");
        }
        try
        {
            
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
            // List<PageCustomEntity> pList = pageCustomService.findAllPageCustomForProduct();
            // mv.addObject("pageCustomList", pList);
            
            // 查询session中是否存在product信息 保存出错时...
            // ProductEntity product = new ProductEntity();
            // mv.addObject("product", product);
            
            // 格格头像
            // List<GegeImageEntity> imageList = geGeImageService.findAllGegeImage("product");
            // mv.addObject("gegeImageList", imageList);
            
            GegeImageEntity geGeImageEntity = new GegeImageEntity();
            geGeImageEntity.setImage("http://m.gegejia.com:80/ygg/pages/images/userimg/gege.png");
            mv.addObject("geGeImageEntity", geGeImageEntity);
            
            // 基本商品信息
            ProductBaseEntity productBase = new ProductBaseEntity();
            mv.addObject("productBase", productBase);
            
            SellerEntity productRelationSeller = new SellerEntity();
            mv.addObject("productRelationSeller", productRelationSeller);
            
            // List<CategoryEntity> categoryList =
            // categoryService.findCategoryByProductBaseId(product.getProductBaseId());
            // mv.addObject("categoryList", categoryList);
            // // 商品 返分销毛利百分比类型
            // mv.addObject("returnDistributionProportionType", 1);
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
            log.error(e.getMessage(), e);
        }
        return mv;
    }
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2015年11月9日 下午7:54:47
     * @描述:
     *      <p>
     *      (添加拼团商品)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年11月9日 下午7:54:47
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param mwebGroupProductEntity
     * @param stock
     * @param detailPicAndText
     * @return
     * @returnType String
     * @version V1.0
     */
    @RequestMapping(value = "/save/{operationType}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String save(@PathVariable("operationType") String operationType, MwebGroupProductEntity mwebGroupProductEntity,
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId,
        @RequestParam(value = "mwebGroupProductId", required = false, defaultValue = "0") int mwebGroupProductId,
        @RequestParam(value = "stock", required = false, defaultValue = "0") int stock, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        DetailPicAndText detailPicAndText, @RequestParam(value = "isOffShelves", required = false, defaultValue = "1") byte isOffShelves)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("msg", "系统出错");
        if (isAvailable == -1)
        {
            mwebGroupProductEntity.setIsAvailable(new Byte("1"));
        }
        else
        {
            mwebGroupProductEntity.setIsAvailable(new Byte(isAvailable + ""));
        }
        
        try
        {
            mwebGroupProductEntity.setIsOffShelves(isOffShelves);
            if ("update".equals(operationType))
            {
                mwebGroupProductEntity.setId(mwebGroupProductId);
                jsonObject = mwebGroupProductService.updateProduct(mwebGroupProductEntity);
                if (jsonObject.getIntValue("status") == 1)
                {
                    detailPicAndText(mwebGroupProductId, detailPicAndText);
                    jsonObject.put("status", 1);
                }
            }
            else if ("add".equals(operationType))
            {
                
                if (mwebGroupProductService.addProduct(mwebGroupProductEntity, stock, detailPicAndText, productBaseId) == 1)
                {
                    jsonObject.put("status", 1);
                }
                
            }
            
        }
        catch (Exception e)
        {
            
            log.error(e.getMessage(), e);
            return jsonObject.toJSONString();
        }
        
        return jsonObject.toJSONString();
        
    }
    
    private void detailPicAndText(int mwebGroupProductId, DetailPicAndText detailPicAndText)
        throws MalformedURLException, IOException
    {
        
        List list = new ArrayList<String>();
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_1()))
        {
            list.add(detailPicAndText.getDetail_pic_1());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_2()))
        {
            list.add(detailPicAndText.getDetail_pic_2());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_3()))
        {
            list.add(detailPicAndText.getDetail_pic_3());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_4()))
        {
            list.add(detailPicAndText.getDetail_pic_4());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_5()))
        {
            list.add(detailPicAndText.getDetail_pic_5());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_6()))
        {
            list.add(detailPicAndText.getDetail_pic_6());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_7()))
        {
            list.add(detailPicAndText.getDetail_pic_7());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_8()))
        {
            list.add(detailPicAndText.getDetail_pic_8());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_9()))
        {
            list.add(detailPicAndText.getDetail_pic_9());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_10()))
        {
            list.add(detailPicAndText.getDetail_pic_10());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_11()))
        {
            list.add(detailPicAndText.getDetail_pic_11());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_12()))
        {
            list.add(detailPicAndText.getDetail_pic_12());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_13()))
        {
            list.add(detailPicAndText.getDetail_pic_13());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_14()))
        {
            list.add(detailPicAndText.getDetail_pic_14());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_15()))
        {
            list.add(detailPicAndText.getDetail_pic_15());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_16()))
        {
            list.add(detailPicAndText.getDetail_pic_16());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_17()))
        {
            list.add(detailPicAndText.getDetail_pic_17());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_18()))
        {
            list.add(detailPicAndText.getDetail_pic_18());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_19()))
        {
            list.add(detailPicAndText.getDetail_pic_19());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_20()))
        {
            list.add(detailPicAndText.getDetail_pic_20());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_21()))
        {
            list.add(detailPicAndText.getDetail_pic_21());
        }
        if (StringUtils.isNotEmpty(detailPicAndText.getDetail_pic_22()))
        {
            list.add(detailPicAndText.getDetail_pic_22());
        }
        
        int i = 0;
        List list_ids = new ArrayList<Integer>();
        for (Iterator<String> it = list.iterator(); it.hasNext();)
        {
            String url = it.next();
            InputStream is = new URL(url).openStream();
            BufferedImage sourceImg = ImageIO.read(is);
            
            int height = sourceImg.getHeight();
            int width = sourceImg.getWidth();
            
            MwebGroupImageDetailEntity m = new MwebGroupImageDetailEntity();
            m.setMwebGroupProductId(mwebGroupProductId);
            m.setUrl(url);
            m.setOrder(new Byte(i + ""));
            m.setHeight(height);
            m.setWidth(width);
            mwebGroupImageDetailService.addMwebGroupImageDetail(m);
            list_ids.add(m.getId());
            i++;
        }
        
        if (list_ids.size() > 0)
        {
            mwebGroupImageDetailService.deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId(mwebGroupProductId, list_ids);
        }
        
    }
    
    /**
     * 跳转到id对应的商品修改页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit/{id}/{isAvailable}/{type}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") int id, @PathVariable("isAvailable") int isAvailable, @PathVariable("type") int type)
    {
        ModelAndView mv = new ModelAndView();
        if (type == 0)
        {
            mv.setViewName("wechatGroup/update");
        }
        else
        {
            mv.setViewName("wechatGroup/updateAutoTeam");
        }
        
        try
        {
            mv.addObject("mwebGroupProductId", id);
            JSONObject jsonObject = mwebGroupProductService.getProduct(id);
            if (jsonObject.getInteger("status") == 0)
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.addAllObjects(jsonObject);
            mv.addObject("isAvailable", isAvailable);
            MwebGroupProductEntity mwebGroupProductEntity = (MwebGroupProductEntity)jsonObject.get("mwebGroupProductEntity");
            int sellerId = mwebGroupProductEntity.getSellerId();
            int brandId = mwebGroupProductEntity.getBrandId();
            int productBaseId = mwebGroupProductEntity.getProductBaseId();
            int stock = jsonObject.getIntValue("stock");
            
            List<CategoryEntity> categoryList = categoryService.findCategoryByProductBaseId(mwebGroupProductEntity.getProductBaseId());
            mv.addObject("categoryList", categoryList);
           
            
            ProductBaseEntity productBase = productBaseService.queryProductBaseById(productBaseId);
            if (productBase != null)
            {
                StringBuilder submitType = new StringBuilder();
                if (productBase.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
                {
                    submitType.append("供货价").append(String.format("%.2f", productBase.getWholesalePrice()));
                }
                else if (productBase.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
                {
                    submitType.append("扣点").append(productBase.getDeduction()).append("%").append("建议价").append(productBase.getProposalPrice());
                }
                else if (productBase.getSubmitType() == ProductEnum.PRODUCT_SUBMIT_TYPE.SELF_PURCHASE_PRICE.getCode())
                {
                    submitType.append("自营采购价").append(productBase.getSelfPurchasePrice());
                }
                mv.addObject("submitType", submitType.toString());
                mv.addObject("productBase", productBase);
            }
            
            // 商品信息 -关联- 商家信息
            SellerEntity productRelationSeller = sellerService.findSellerById(sellerId);
            mv.addObject("productRelationSeller", productRelationSeller);
            mv.addObject("sellerType", SellerEnum.SellerTypeEnum.getDescByCode(productRelationSeller.getSellerType()));
            mv.addObject("freightType",
                productRelationSeller.getFreightType() == 1 ? "包邮"
                    : productRelationSeller.getFreightType() == 2 ? "满" + String.format("%.2f", productRelationSeller.getFreightMoney()) + "包邮"
                        : productRelationSeller.getFreightType() == 3 ? "全部不包邮" : productRelationSeller.getFreightOther());
            productRelationSeller.setSendTimeRemark(productRelationSeller.getSendTimeType() == 4 ? productRelationSeller.getSendTimeRemark()
                : productRelationSeller.getSendTimeType() == 0 ? "" : SellerEnum.SellerSendTimeTypeEnum.getDescByCode(productRelationSeller.getSendTimeType()));
                
            BrandEntity brand = brandService.findBrandById(brandId);
            mv.addObject("brandName", brand == null ? "" : brand.getName());
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        
        return mv;
    }
    
    @RequestMapping("/getGroupProductCount")
    @ResponseBody
    public String getGroupProductCount(HttpServletRequest request, @RequestParam(value = "mwebGroupProductId", required = true, defaultValue = "0") int mwebGroupProductId)
    {
        
        JSONArray jsonArray = new JSONArray();
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mwebGroupProductId);
            jsonArray = mwebGroupProductService.findProductAndStock(jsonObject);
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return jsonArray.toJSONString();
        }
        return jsonArray.toJSONString();
    }
    
    @RequestMapping("/updateGroupProductCount")
    @ResponseBody
    public String updateGroupProductCount(HttpServletRequest request, @RequestParam(value = "mwebGroupProductId", required = true, defaultValue = "0") int mwebGroupProductId,
        @RequestParam(value = "addStock", required = true) int addStock)
    {
        
        JSONObject jsonObject = new JSONObject();
        try
        {
            
            jsonObject = mwebGroupProductCountService.updateGroupProductCount(mwebGroupProductId, addStock);
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return jsonObject.toJSONString();
        }
        return jsonObject.toJSONString();
    }
    
    /**
     * 上下架商品
     * 
     * @param request
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/forSale", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-商品上下架")
    public String forSale(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code)
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
            int resultStatus = mwebGroupProductService.forSale(para);
            if (resultStatus > 0)
            {
                resultMap.put("status", 1);
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
            resultMap.put("msg", "服务器发生异常，请刷新后再试。");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/updateOrder")
    @ResponseBody
    public String updateOrder(HttpServletRequest request, @RequestParam(value = "mwebGroupProductId", required = true, defaultValue = "0") int mwebGroupProductId,
        @RequestParam(value = "order", required = true) int order)
    {
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("msg", "系统出错");
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", mwebGroupProductId);
            para.put("order", order);
            int i = mwebGroupProductService.updateOrder(para);
            jsonObject.put("status", 1);
            jsonObject.put("msg", "成功");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return jsonObject.toJSONString();
        }
        return jsonObject.toJSONString();
    }
    
    /**
     * 导出拼团商品管理
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "导出拼团商品管理")
    public void exportProductInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "isAvailable", required = true, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "productBaseId", required = false, defaultValue = "0") int productBaseId,
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "-1") int isOffShelves,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "endTimeBegin", required = false, defaultValue = "") String endTimeBegin,
        @RequestParam(value = "endTimeEnd", required = false, defaultValue = "") String endTimeEnd, @RequestParam(value = "code", required = false, defaultValue = "") String code)
            throws Exception
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        JSONObject jsonObject = new JSONObject();
        if (page == 0)
        {
            page = 1;
        }
        // jsonObject.put("start", rows * (page - 1));
        // jsonObject.put("max", rows);
        
        if (StringUtils.isNotEmpty(name))
        {
            jsonObject.put("name", name);
        }
        if (id != 0)
        {
            jsonObject.put("id", id);
        }
        if (sellerId != 0)
        {
            jsonObject.put("sellerId", sellerId);
        }
        if (productBaseId != 0)
        {
            jsonObject.put("productBaseId", productBaseId);
        }
        if (StringUtils.isNotEmpty(startTimeBegin))
        {
            jsonObject.put("startTimeBegin", startTimeBegin);
        }
        if (StringUtils.isNotEmpty(startTimeEnd))
        {
            jsonObject.put("startTimeEnd", startTimeEnd);
        }
        if (StringUtils.isNotEmpty(endTimeBegin))
        {
            jsonObject.put("endTimeBegin", endTimeBegin);
        }
        if (StringUtils.isNotEmpty(endTimeEnd))
        {
            jsonObject.put("endTimeEnd", endTimeEnd);
        }
        if (isOffShelves != -1)
        {
            jsonObject.put("isOffShelves", isOffShelves);
        }
        if (isAvailable != -1)
        {
            jsonObject.put("isAvailable", isAvailable);
        }
        if (StringUtils.isNotEmpty(code))
        {
            jsonObject.put("code", code);
        }
        try
        {
            JSONArray jsonArray = mwebGroupProductService.findProductAndStock(jsonObject);
            
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("左岸城堡拼团商品管理");
            String[] str = {"商品ID", "使用状态", "商品状态", "编码", "长名称", "短名称", "团购价", "已成团销量", "剩余库存", "锁定库存", "排序值", "商家", "发货地", "开始时间", "结束时间", "备注"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JSONObject j = (JSONObject)jsonArray.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(j.getString("id"));
                r.createCell(1).setCellValue(j.getString("isAvailable"));
                r.createCell(2).setCellValue(j.getString("isOffShelves"));
                r.createCell(3).setCellValue(j.getString("code"));
                r.createCell(4).setCellValue(j.getString("name"));
                r.createCell(5).setCellValue(j.getString("shortName"));
                r.createCell(6).setCellValue(j.getString("teamPrice"));
                r.createCell(7).setCellValue(j.getString("sell"));
                r.createCell(8).setCellValue(j.getString("stock"));
                r.createCell(9).setCellValue(j.getString("lock"));
                r.createCell(10).setCellValue(j.getString("order"));
                r.createCell(11).setCellValue(j.getString("realSellerName"));
                r.createCell(12).setCellValue(j.getString("sendAddress"));
                r.createCell(13).setCellValue(j.getString("startTime"));
                r.createCell(14).setCellValue(j.getString("endTime"));
                r.createCell(15).setCellValue(j.getString("remark"));
            }
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "左岸城堡拼团商品管理";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
     * 更新商品信息
     * 
     * 
     * @param mwebGroupProductEntity
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateProductForTeam")
    @ResponseBody
    public Object jsonProductInfoForTeam(@RequestParam Map<String, Object> param)
        throws Exception
    {
        int resultStatus = mwebGroupProductService.updateProductForTeam(param);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        else
        {
            resultMap.put("status", 1);
        }
        if (param.get("editId") != null && StringUtils.isNotBlank(param.get("editId").toString()))
        {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/wechatGroup/listTeam");
            return mv;
        }
        return resultMap;
    }
    
    @RequestMapping("/jsonProductInfoForTeam")
    @ResponseBody
    public Object jsonProductInfoForTeam(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "status", required = false, defaultValue = "0") int status, String name, String startTime, String endTime)
            throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        page = page == 0 ? 1 : page;
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("status", status);
        param.put("name", name == "" ? null : name);
        param.put("productId", productId == 0 ? null : productId);
        param.put("startTime", startTime == "" ? null : startTime);
        param.put("endTime", endTime == "" ? null : endTime);
        JSONObject res = mwebGroupProductService.findProductAndStockForTeam(param);
        return res;
    }
    
    @RequestMapping("/jsonProductInfoForTeamById")
    @ResponseBody
    public Object jsonProductInfoForTeamById(@RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "id", required = false, defaultValue = "0") int id)
            throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("productId", productId == 0 ? null : productId);
        param.put("id", id == 0 ? null : id);
        JSONObject res = mwebGroupProductService.findProductAndStockForTeamById(param);
        param.clear();
        if (res.getString("name") != null)
        {
            param.put("status", 1);
            param.put("data", res);
        }
        else
        {
            param.put("status", 0);
            param.put("msg", "对应的商品不存在");
        }
        return param;
    }
    
    @RequestMapping("/editForTeam/{productId}")
    public ModelAndView editForTeam(HttpServletRequest request, @PathVariable("productId") int productId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("productId", productId);
        JSONObject product = mwebGroupProductService.findProductAndStockForTeamById(param);
        mv.addObject("product", product);
        mv.setViewName("wechatGroup/teamForm");
        return mv;
    }
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年4月9日 下午2:53:00
     * @描述:
     *      <p>
     *      (自动开团列表)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年4月9日 下午2:53:00
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param request
     * @return
     * @returnType ModelAndView
     * @version V1.0
     */
    @RequestMapping("/autoTeamList")
    public ModelAndView autoTeamList(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        
        try
        {
            mv.setViewName("wechatGroup/autoTeamList");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping("/jsonAutoTeamList")
    @ResponseBody
    public Object jsonAutoTeamList(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
            throws Exception
            
    {
        JSONObject jsonObject = new JSONObject();
        
        if (page == 0)
        {
            page = 1;
        }
        jsonObject.put("start", rows * (page - 1));
        jsonObject.put("max", rows);
        
        return mwebGroupProductInfoService.findAutoTeamList(jsonObject);
    }
    
    @RequestMapping("/getAutoTeamConfig/{id}")
    @ResponseBody
    public Object getAutoTeamConfig(@PathVariable("id") int id, HttpServletRequest request)
        throws Exception
        
    {
        
        return mwebGroupProductInfoService.getAutoTeamConfig(id);
    }
    
    @RequestMapping("/updateAutoTeamConfig")
    @ResponseBody
    public Object updateAutoTeamConfig(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "randomStartSecond", required = true) int randomStartSecond,
        @RequestParam(value = "randomEndSecond", required = true) int randomEndSecond,
        @RequestParam(value = "autoJoinTeamNumberLimit", required = true) int autoJoinTeamNumberLimit, HttpServletRequest request)
            throws Exception
            
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("randomStartSecond", randomStartSecond);
        jsonObject.put("randomEndSecond", randomEndSecond);
        jsonObject.put("autoJoinTeamNumberLimit", autoJoinTeamNumberLimit);
        jsonObject = mwebGroupProductInfoService.updateAutoTeamConfig(jsonObject);
        
        return jsonObject;
    }
    
    @RequestMapping("/setupAutoJoinTeam")
    @ResponseBody
    public Object setupAutoJoinTeam(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isOpenAutoJoinTeam", required = true) int isOpenAutoJoinTeam,
        HttpServletRequest request)
            throws Exception
            
    {
        
        JSONObject jsonObject = mwebGroupProductInfoService.setupAutoJoinTeam(id, isOpenAutoJoinTeam);
        
        return jsonObject;
    }
    
}
