package com.ygg.admin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ProductInfoForGroupSale;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.entity.SaleTagEntity;
import com.ygg.admin.entity.SaleWindowEntity;
import com.ygg.admin.service.ActivitiesCommonService;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SaleTagService;
import com.ygg.admin.service.SaleWindowServcie;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.time.SaleWindowUpdateServiceJob;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.ImageUtil;

/**
 * 首页特卖管理
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/indexSale")
public class SaleWindowController
{
    
    @Resource(name = "activitiesCommonService")
    private ActivitiesCommonService activitiesCommonService;
    
    @Resource(name = "productService")
    private ProductService productService;
    
    @Resource(name = "saleWindowServcie")
    private SaleWindowServcie saleWindowServcie;
    
    @Resource(name = "saleTagService")
    private SaleTagService saleTagService;
    
    @Resource
    private SaleWindowUpdateServiceJob saleWindowUpdateServiceJob;
    
    @Resource
    private SystemLogService logService;
    
    private Logger log = Logger.getLogger(SaleWindowController.class);
    
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    
    public SaleWindowUpdateServiceJob getSaleWindowUpdateService()
    {
        return saleWindowUpdateServiceJob;
    }
    
    public void setSaleWindowUpdateServiceJob(SaleWindowUpdateServiceJob saleWindowUpdateServiceJob)
    {
        this.saleWindowUpdateServiceJob = saleWindowUpdateServiceJob;
    }
    
    /**
     * 转发到特卖添加页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/addSale")
    public ModelAndView addSale(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SaleWindowEntity saleWindow = new SaleWindowEntity();
        mv.addObject("saleWindow", saleWindow);
        
        // 获取标签数据
        Map<String, Object> tagPara = new HashMap<String, Object>();
        tagPara.put("available", "1");
        tagPara.put("start", 0);
        tagPara.put("max", 100);
        
        mv.addObject("flagId", "0");
        // 一级分类
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        List<CategoryFirstEntity> catetgoryFirstList = categoryService.findAllCategoryFirst(para);
        mv.addObject("catetgoryFirstList", catetgoryFirstList);
        mv.setViewName("sale/saleForm");
        return mv;
    }
    
    /**
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SaleWindowEntity saleWindow = saleWindowServcie.findSaleWindowById(id);
        if (saleWindow == null)
        {
            mv.setViewName("forward:/error/404");
            return mv;
        }
        mv.addObject("saleWindow", saleWindow);
        // 转换时间
        String timePostfix = saleWindow.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode() ? "100000" : "200000";
        String startTime = saleWindow.getStartTime() + timePostfix;// 20150302
        String endTime = saleWindow.getEndTime() + timePostfix;// 20150302
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        mv.addObject("startTime", new DateTime(sdf.parse(startTime)).toString("yyyy-MM-dd HH:mm:ss"));
        mv.addObject("endTime", new DateTime(sdf.parse(endTime)).plusDays(1).toString("yyyy-MM-dd HH:mm:ss"));
        
        // 组合特卖ID
        mv.addObject("id", saleWindow.getDisplayId());
        mv.addObject("flagId", saleWindow.getSaleFlagId() + "");
        mv.addObject("categoryFirstId", saleWindow.getCategoryFirstId());
        // 一级分类
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        List<CategoryFirstEntity> catetgoryFirstList = categoryService.findAllCategoryFirst(para);
        mv.addObject("catetgoryFirstList", catetgoryFirstList);
        // 获取特卖与标签相关数据
        List<Integer> tagIds = saleTagService.findTagIdsBySaleWindowId(id);
        mv.addObject("tagIds", tagIds);
        // 获取标签相关数据
        Map<String, Object> tagPara = new HashMap<String, Object>();
        // tagPara.put("available", 1);
        tagPara.put("start", 0);
        tagPara.put("max", 50);
        List<SaleTagEntity> saleTagList = saleTagService.findAllSaleTag(tagPara);
        List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
        for (SaleTagEntity entity : saleTagList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", entity.getId());
            map.put("name", entity.getName());
            if (tagIds.contains(entity.getId()))
            {
                map.put("contain", 1);
            }
            else
            {
                map.put("contain", 0);
            }
            tagList.add(map);
        }
        mv.addObject("tagList", tagList);
        
        mv.setViewName("sale/saleForm");
        return mv;
    }
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sale/indexSaleManage");
        return mv;
    }
    
    /**
     *
     * @param page
     * @param rows
     * @param type：特卖位类型：1单品，2组合，3自定义特卖，4原生自定义页面
     * @param saleStatus：特卖状态：1等待开始，2进行中，3已结束
     * @param name：特卖名称
     * @param isDisplay：展现状态，1展现，0不展现
     * @param searchStartTime：开售时间，开售时间
     * @param productId：商品Id
     * @param productName：商品名称
     * @return
     */
    @RequestMapping(value = "/jsonSaleWindowInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSaleWindowInfo(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "type", required = false, defaultValue = "-1") int type, //
        @RequestParam(value = "saleStatus", required = false, defaultValue = "-1") int saleStatus, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay, //
        @RequestParam(value = "startTime", required = false, defaultValue = "") String searchStartTime, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "brandId", required = false, defaultValue = "-1") int brandId, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,
        @RequestParam(value = "categoryFirstId", defaultValue = "-1", required = false) int categoryFirstId)
    {
        try
        {
            return saleWindowServcie.jsonSaleWindows(page, rows, saleStatus, name, categoryFirstId, productId, productName, brandId, sellerId, type, isDisplay, searchStartTime);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping("/listToday/{day}")
    public ModelAndView listToday(HttpServletRequest request, @PathVariable("day") int day)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        if (day == 2)
        {
            mv.addObject("showButtom", "1");
        }
        mv.addObject("timeStr", DateTime.now().plusDays(day - 1).toString("yyyy-MM-dd 10:00:00") + " - " + DateTime.now().plusDays(day).toString("yyyy-MM-dd 10:00:00"));
        mv.addObject("day", day);
        if (day == 1)
        {
            mv.addObject("orderType", 1);
        }
        else if (day == 2)
        {
            mv.addObject("orderType", 2);
        }
        else
        {
            mv.addObject("orderType", 1);
        }
        mv.setViewName("sale/todayIndexSaleManage");
        return mv;
    }
    
    @RequestMapping(value = "/todayJsonSaleWindowInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String todayJsonSaleWindowInfo(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "day", required = true) int day, // 查询增加的天数，1代表查询今天，2代表查询明天
        @RequestParam(value = "type", required = true) int type, // 1:将售,2:在售
        @RequestParam(value = "orderType", required = true) int orderType, // 1：nowOrder,2：laterOrder
        @RequestParam(value = "saleName", required = false, defaultValue = "") String saleName// 特卖名称
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
        if (type == 1)
        {
            para.put("status", 1); // 将售
        }
        else
        {
            para.put("status", 2); // 在售
        }
        if (!"".equals(saleName))
        {
            para.put("saleName", "%" + saleName + "%");
        }
        // 查询时间
        DateTime searchTime = DateTime.now().plusDays(day - 1);
        
        int saleStatus = (int)para.get("status");
        boolean beforeTen = DateTime.now().getHourOfDay() >= 10 ? false : true;
        int compareToStart = 0;
        int maxToStart = 0;
        int compareToEnd = 0;
        if (beforeTen)
        {
            if (saleStatus == 1)
            {
                // 等待开始 20150202 - 20150202 20150202 2015 02 02 9:00 start_time
                // >= now --> start_time > (now - 1)
                compareToStart = Integer.valueOf(searchTime.plusDays(-1).toString("yyyyMMdd")).intValue();
                maxToStart = Integer.valueOf(searchTime.plusDays(1).toString("yyyyMMdd")).intValue();
                para.put("maxToStart", maxToStart);
            }
            else if (saleStatus == 2)
            {
                // 进行中 20150205 - 20150205 20150206 2015 02 06 9:00 start_time <
                // now AND (now - 1 -1) < end_time
                compareToStart = Integer.valueOf(searchTime.toString("yyyyMMdd")).intValue();
                compareToEnd = Integer.valueOf(searchTime.plusDays(-2).toString("yyyyMMdd")).intValue();
            }
        }
        else
        {
            if (saleStatus == 1)
            {
                // 等待开始 20150202 - 20150202 20150202 2015 02 02 10:12
                compareToStart = Integer.valueOf(searchTime.toString("yyyyMMdd")).intValue();
                maxToStart = Integer.valueOf(searchTime.plusDays(2).toString("yyyyMMdd")).intValue();
                para.put("maxToStart", maxToStart);
            }
            else if (saleStatus == 2)
            {
                // 进行中
                compareToStart = Integer.valueOf(searchTime.plusDays(1).toString("yyyyMMdd")).intValue();
                compareToEnd = Integer.valueOf(searchTime.plusDays(-1).toString("yyyyMMdd")).intValue();
            }
        }
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        if (day == 1)
        {
            para.put("nowOrder", 1);
        }
        else
        {
            para.put("laterOrder", 1);
        }
        para.put("sourceType", 1);
        List<SaleWindowEntity> saleWindowList = saleWindowServcie.findAllSaleWindow(para);
        Map resultMap = new HashMap();
        List<Map<String, Object>> resultList = saleWindowServcie.packageSaleWindowList(saleWindowList, orderType, type);
        resultMap.put("rows", resultList);
        int total = saleWindowServcie.countSaleWindow(para);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 查询商品 特卖类型管理的商品
     * 
     * @param request
     * @param page
     * @param rows
     * @param type
     * @param subjectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProductGroup")
    @ResponseBody
    public Map<String, Object> jsonProductGroup(HttpServletRequest request, @RequestParam(value = "activityCommonId", required = true) int activityCommonId,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "rows", required = false, defaultValue = "30") int rows)
        throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        params.put("start", rows * (page - 1));
        params.put("max", rows);
        params.put("activityCommonId", activityCommonId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", activitiesCommonService.findProductsByActivityCommonId(params));
        resultMap.put("total", activitiesCommonService.countProductsByActivityCommonId(activityCommonId));
        return resultMap;
    }
    
    /**
     * 查询商品 特卖类型管理的商品
     * 
     * @param request
     * @param page
     * @param rows
     * @param type
     * @param subjectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "type", required = true) int type, // 1单品；2组合特卖；3自定义特卖
        @RequestParam(value = "subjectId", required = false, defaultValue = "0") int subjectId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("id", subjectId);
        
        Map resultMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        
        if (type == 1 && subjectId != 0)
        {
            ProductEntity product = productService.findProductById(subjectId);
            if (product != null)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", product.getId());
                map.put("showId", product.getId());
                map.put("code", product.getCode());
                map.put("name", product.getName());
                map.put("startTime", product.getStartTime());
                map.put("endTime", product.getEndTime());
                resultList.add(map);
                resultMap.put("rows", resultList);
                resultMap.put("total", 1);
            }
            else
            {
                resultMap.put("rows", resultList);
                resultMap.put("total", 0);
            }
        }
        else if (type == 2 && subjectId != 0)
        {
            List<ProductInfoForGroupSale> pList = activitiesCommonService.findProductInfoForGroupSaleByActivitiesCommonId(para);
            for (ProductInfoForGroupSale productInfoForGroupSale : pList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("showId", productInfoForGroupSale.getProductId());
                map.put("id", productInfoForGroupSale.getProductId());
                map.put("code", productInfoForGroupSale.getCode());
                map.put("name", productInfoForGroupSale.getName());
                map.put("startTime", productInfoForGroupSale.getStartTime());
                map.put("endTime", productInfoForGroupSale.getEndTime());
                resultList.add(map);
            }
            resultMap.put("rows", resultList);
            int total = activitiesCommonService.countProductNumByActivitiesCommonId(subjectId);
            resultMap.put("total", total);
        }
        else if (type == 3 && subjectId != 0)
        {// 自定义特卖
        
        }
        else if (type == 4 && subjectId != 0)
        {// 自定义页面
        
        }
        else
        {
            
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖位管理-修改特卖位排序值")
    public String updateOrder(HttpServletRequest request, @RequestParam(value = "order", required = true) int order, @RequestParam(value = "id", required = true) int id,
        @RequestParam(value = "type", required = true) int type // 1:修改now_order
                                                                // ；
                                                                // 2:修改later_order
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("order", order);
        para.put("id", id);
        para.put("type", type);
        int resultStatus = saleWindowServcie.updateOrder(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 重置明日排序值
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/resetTomorrowOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖位管理-重置明日排序值")
    public String resetTomorrowOrder(HttpServletRequest request)
        throws Exception
    {
        
        // saleWindowUpdateServiceJob.manWork();
        int status = saleWindowServcie.resetTomorrowOrder();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", status);
        resultMap.put("msg", status == 1 ? "更新成功" : "更新失败");
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 修改商品时间
     * 
     * @param request
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateProductTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖位管理-修改商品时间")
    public String updateProductTime(HttpServletRequest request, @RequestParam(value = "productIds", required = true) String productIds,
        @RequestParam(value = "startTime", required = true) String startTime, @RequestParam(value = "endTime", required = true) String endTime,
        @RequestParam(value = "type", required = false) int type, @RequestParam(value = "saleId", required = false, defaultValue = "0") int saleId)
        throws Exception
    {
        
        List<Integer> idList = new ArrayList<>();
        if (productIds.indexOf(",") > 0)
        {
            String[] arr = productIds.split(",");
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
        }
        else
        {
            idList.add(Integer.valueOf(productIds));
        }
        for (Integer currId : idList)
        {
            ProductEntity pEntity = new ProductEntity();
            pEntity.setId(currId);
            pEntity.setStartTime(startTime);
            pEntity.setEndTime(endTime);
            ProductEntity pe = productService.findProductById(currId);
            
            productService.updateProductTime(pEntity);
            // 记录日志
            try
            {
                Map<String, Object> logInfoMap = new HashMap<>();
                logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SELL_MANAGEMENT.ordinal());
                if (type == 1)// banner修改商品销售时间
                {
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_PRODUCT_TIME.ordinal());
                }
                if (type == 2)// 特卖修改商品时间
                {
                    logInfoMap.put("saleId", saleId);
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_SELL_PRODUCT_TIME.ordinal());
                }
                logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                String newTime = startTime + "~" + endTime;
                String oldTime = pe.getStartTime().substring(0, pe.getStartTime().lastIndexOf(".")) + "~" + pe.getEndTime().substring(0, pe.getEndTime().lastIndexOf("."));
                logInfoMap.put("objectId", currId);
                logInfoMap.put("oldTime", oldTime);
                logInfoMap.put("newTime", newTime);
                logService.logger(logInfoMap);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", 1);
        resultMap.put("msg", "修改成功");
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增特卖
     * 
     * @param request
     * @param editId
     * @param name
     * @param desc
     * @param image
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ControllerLog(description = "特卖位管理-新增特卖位")
    public ModelAndView save(HttpServletRequest request,
        @RequestParam(value = "editId", required = false, defaultValue = "0") int editId,
        @RequestParam(value = "type", required = true) byte type, // 1单品；2组合特卖；3自定义特卖
        @RequestParam(value = "name", required = true) String name, //
        @RequestParam(value = "saleTimeType", required = true) int saleTimeType, // 1：早场，2：晚场
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "image", required = false, defaultValue = "") String image,//
        @RequestParam(value = "newImage", required = false) String newImage, //
        @RequestParam(value = "newImage17", required = false, defaultValue = "") String newImage17, // app1.7以后版本需要
        @RequestParam(value = "code", required = false, defaultValue = "0") int code, // 商品ID
        @RequestParam(value = "groupSale", required = false, defaultValue = "0") int groupSale,//
        @RequestParam(value = "customSale", required = false, defaultValue = "0") int customSale, //
        @RequestParam(value = "customPage", required = false, defaultValue = "0") int customPage, //
        @RequestParam(value = "startTime", required = true) String startTime,//
        @RequestParam(value = "endTime", required = true) String endTime, //
        @RequestParam(value = "isDisplay", required = true) byte isDisplay,//
        @RequestParam(value = "saleFlagId", required = false, defaultValue = "0") int saleFlagId,//
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "0") int categoryFirstId,
        @RequestParam(value = "isDisplayFlag", required = false, defaultValue = "1") int isDisplayFlag)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        // image = newImage;
        newImage = newImage17;
        image = newImage17;
        if (image.indexOf(ImageUtil.getPrefix()) > 0)
        {
            image = image.substring(0, image.indexOf(ImageUtil.getPrefix()));
        }
        if (newImage.indexOf(ImageUtil.getPrefix()) > 0)
        {
            newImage = newImage.substring(0, newImage.indexOf(ImageUtil.getPrefix()));
        }
        if (newImage17.indexOf(ImageUtil.getPrefix()) > 0)
        {
            newImage17 = newImage17.substring(0, newImage17.indexOf(ImageUtil.getPrefix()));
        }
        SaleWindowEntity saleWindow = new SaleWindowEntity();
        saleWindow.setType(type);
        saleWindow.setName(name);
        saleWindow.setDesc(desc);
        saleWindow.setImage((image.indexOf(ImageUtil.getPrefix()) > 0) ? image : (image + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v1sell.ordinal())));
        saleWindow.setNewImage((newImage.indexOf(ImageUtil.getPrefix()) > 0) ? newImage : (newImage + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.newsell.ordinal())));
        saleWindow.setNewImage17((newImage17.indexOf(ImageUtil.getPrefix()) > 0) ? newImage17
            : (newImage17 + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v17newsell.ordinal())));
        saleWindow.setIsDisplay(isDisplay);
        saleWindow.setSaleFlagId(saleFlagId);
        saleWindow.setIsDisplayFlag(isDisplayFlag);
        saleWindow.setSaleTimeType(saleTimeType);
        saleWindow.setCategoryFirstId(categoryFirstId);
        if (type == 1)
        {
            saleWindow.setDisplayId(code);
        }
        else if (type == 2)
        {
            saleWindow.setDisplayId(groupSale);
        }
        else if (type == 3)
        {
            saleWindow.setDisplayId(customSale);
        }
        else if (type == 4)
        {
            saleWindow.setDisplayId(customPage);
        }
        // 开始、结束时间 2014-03-11 10:00:00 ->20140311
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime_date = sdf.parse(startTime);
        Date endTime_date = sdf.parse(endTime);
        DateTime startTime_datetime = new DateTime(startTime_date);
        DateTime endTime_datetime = new DateTime(endTime_date);
        saleWindow.setStartTime(Integer.valueOf(startTime_datetime.toString("yyyyMMdd")));
        saleWindow.setEndTime(Integer.valueOf(endTime_datetime.plusDays(-1).toString("yyyyMMdd")));
        
        if (saleWindow.getType() == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
        {
            String jsonStr = saleWindowServcie.checkSaleWindowRelation(editId, saleWindow.getDisplayId());
            int status = JSON.parseObject(jsonStr).getIntValue("status");
            if (status == CommonConstant.COMMON_NO)
            {
                mv.addObject("wrongMessage", JSON.parseObject(jsonStr).getString("message"));
                if (editId == 0)
                {
                    mv.setViewName("redirect:/indexSale/addSale");
                }
                else
                {
                    mv.setViewName("redirect:/indexSale/edit/" + editId);
                }
                return mv;
            }
        }
        
        if (editId != 0)
        {
            // update
            saleWindow.setId(editId);
            SaleWindowEntity sw = saleWindowServcie.findSaleWindowById(editId);
            saleWindowServcie.update(saleWindow);
            if (sw != null)
            {
                try
                {
                    String timePostfixOld = saleWindow.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode() ? "100000" : "200000";
                    String timePostfixNew = sw.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode() ? "100000" : "200000";
                    Map<String, Object> logInfoMap = new HashMap<>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SELL_MANAGEMENT.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                    logInfoMap.put("objectId", editId);
                    if (sw.getStartTime() != saleWindow.getStartTime() || sw.getEndTime() != saleWindow.getEndTime() || timePostfixNew != timePostfixOld)// 特卖时间修改时记录日志
                    {
                        SimpleDateFormat formt = new SimpleDateFormat("yyyyMMddHHmmss");
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_SELL_TIME.ordinal());
                        String newTime = startTime + "~" + endTime;
                        String oldTime =
                            new DateTime(formt.parse(sw.getStartTime() + timePostfixOld)).toString("yyyy-MM-dd HH:mm:ss") + "~"
                                + new DateTime(formt.parse(sw.getEndTime() + timePostfixOld)).toString("yyyy-MM-dd HH:mm:ss");
                        logInfoMap.put("oldTime", oldTime);
                        logInfoMap.put("newTime", newTime);
                        logService.logger(logInfoMap);
                    }
                    if (!(sw.getDisplayId() == saleWindow.getDisplayId() && sw.getType() == saleWindow.getType()))// 特卖关联商品Id修改时记录日志
                    {
                        logInfoMap.put("old", sw);
                        logInfoMap.put("new", saleWindow);
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_SELL_PRODUCT.ordinal());
                        logService.logger(logInfoMap);
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
            // insert 新增默认排序值在最前面
            saleWindow.setNowOrder((short)1000);
            saleWindow.setLaterOrder((short)1000);
            saleWindowServcie.save(saleWindow);
        }
        
        mv.setViewName("redirect:/indexSale/list");
        return mv;
    }
    
    @RequestMapping(value = "/updateDisplayCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖位管理-修改特卖位展现状态")
    public String updateDisplayCode(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isDisplay", code);
        int resultStatus = saleWindowServcie.updateDisplayCode(para);
        Map resultMap = new HashMap();
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        else
        {
            resultMap.put("status", 1);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 检查商品的特卖时间 并且检查商品的分销供货价是否大于或等于售价*60%并且小于等于售价
     * 
     * @param request
     * @param type
     * @param subjectId
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkProductTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkProductTime(HttpServletRequest request,
        @RequestParam(value = "type", required = true) int type, // 1单品；2组合特卖；3自定义特卖
        @RequestParam(value = "subjectId", required = true) int subjectId, @RequestParam(value = "startTime", required = false) String startTime,
        @RequestParam(value = "endTime", required = false) String endTime)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        para.put("type", type);
        para.put("subjectId", subjectId);
        boolean isExist = saleWindowServcie.checkIsExist(para);
        if (!isExist)
        {
            resultMap.put("status", 0);
            String tip = "单品";
            switch (type)
            {
                case 1:
                    tip = "单品";
                    break;
                case 2:
                    tip = "组合特卖";
                    break;
                case 3:
                    tip = "自定义特卖";
                    break;
                case 4:
                    tip = "原生自定义页面";
                    break;
                default:
                    break;
            }
            resultMap.put("msg", "id=" + subjectId + "的" + tip + "不存在");
            return JSON.toJSONString(resultMap);
        }
        if (startTime != null)
        {
            para.put("startTime", startTime);
        }
        if (endTime != null)
        {
            para.put("endTime", endTime);
        }
        resultMap = saleWindowServcie.checkProductTime(para);
        return JSON.toJSONString(resultMap);
        
    }
    
    @RequestMapping(value = "/checkNameAndDescLength", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkNameAndDescLength(@RequestParam(value = "name", required = true, defaultValue = "") String name,
        @RequestParam(value = "desc", required = true, defaultValue = "") String desc)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        float count = 0.0f;
        if (!"".equals(name))
        {
            name = name.replaceAll(" ", "");
            for (int i = 0; i < name.length(); i++)
            {
                char ch = name.charAt(i);
                if (ch >= 'a' && ch <= 'z')
                {
                    count += 0.5;
                }
                else if (ch >= '0' && ch <= '9')
                {
                    count += 0.5;
                }
                else
                {
                    count += 1;
                }
            }
        }
        if (!"".equals(desc))
        {
            desc = desc.replaceAll(" ", "");
            for (int i = 0; i < desc.length(); i++)
            {
                char ch = desc.charAt(i);
                if (ch >= 'a' && ch <= 'z')
                {
                    count += 0.5;
                }
                else if (ch >= '0' && ch <= '9')
                {
                    count += 0.5;
                }
                else
                {
                    count += 1;
                }
            }
        }
        if ((int)count >= 22)
        {
            map.put("status", 0);
            map.put("msg", "名称+描述不得超过22个字符");
        }
        else
        {
            map.put("status", 1);
        }
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = "/checkSaleWindowRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object checkSaleWindowRelation(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId)
    {
        try
        {
            return saleWindowServcie.checkSaleWindowRelation(id, productId);
        }
        catch (Exception e)
        {
            return ResultEntity.getFailResult("服务器忙，请稍后");
        }
    }
}
