package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.CustomLayoutStyleTypeEnum;
import com.ygg.admin.code.PageModelTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.entity.PageModelBannerEntity;
import com.ygg.admin.entity.PageModelCustomLayoutEntity;
import com.ygg.admin.entity.PageModelEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.PageService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.StringUtils;

/**
 * 自定义拼装页面 控制器
 */
@Controller
@RequestMapping("/page")
public class PageController
{
    private Logger log = Logger.getLogger(PageController.class);
    
    @Resource
    private PageService pageService;
    
    @Resource
    private ProductService productService;
    
    /** 自定义拼装页面列表 */
    @RequestMapping("/list")
    public ModelAndView list()
        throws Exception
    {
        ModelAndView mv = new ModelAndView("page/list");
        return mv;
    }
    
    /** 异步 获取列表信息 */
    @RequestMapping(value = "/ajaxPageData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxPageData(@RequestParam(value = "page", required = false, defaultValue = "1") int page, // 页码
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, // 每页行数
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        Map<String, Object> result = pageService.findAllPageByPara(para);
        return JSON.toJSONString(result);
    }
    
    /** 更新 or 保存数据 */
    @RequestMapping(value = "/updatePage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑页面")
    public String updatePage(@RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 页面名称
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, // 页面描述
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable //
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            if (!"".equals(name))
            {
                para.put("name", name);
            }
            if (!"".equals(remark))
            {
                para.put("remark", remark);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            int status = pageService.insertOrUpdatePage(para);
            result.put("status", status);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 自定义拼装页面管理 */
    @RequestMapping("/pageManage/{pageId}")
    public ModelAndView pageManage(@PathVariable("pageId") int pageId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("page/pageManage");
        Map<String, Object> pageInfo = pageService.findPageById(pageId);
        mv.addObject("pageId", pageId + "");
        mv.addObject("pageName", pageInfo.get("name") + "");
        mv.addObject("pageRemark", pageInfo.get("remark") + "");
        return mv;
    }
    
    /** 异步 获取页面模块信息 */
    @RequestMapping(value = "/ajaxPageModel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxPageModel(@RequestParam(value = "pageId", required = true) int pageId // 页面ID
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("pageId", pageId);
        Map<String, Object> result = pageService.findPageModelByPara(para);
        return JSON.toJSONString(result);
    }
    
    /** 更新 or 保存数据 */
    @RequestMapping(value = "/updatePageModel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑模块")
    public String updatePageModel(@RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 模块名称
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, // 模块描述
        @RequestParam(value = "pageId", required = true) int pageId, // 页面ID
        @RequestParam(value = "type", required = false, defaultValue = "0") int type, // 模块类型
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay, // 模块展现状态
        @RequestParam(value = "sequence", required = false, defaultValue = "-1") int sequence // 模块展现状态
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            para.put("pageId", pageId);
            para.put("name", name);
            
            if (!"".equals(remark))
            {
                para.put("remark", remark);
            }
            if (type != 0)
            {
                para.put("type", type);
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            if (sequence != -1)
            {
                para.put("sequence", sequence);
            }
            int status = pageService.insertOrUpdatePageModel(para);
            result.put("status", status);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 模块内容管理 */
    @RequestMapping("/modelManage/{modelId}")
    public ModelAndView modelManage(@PathVariable("modelId") int modelId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        PageModelEntity model = pageService.findPageModelById(modelId);
        // 判断设置viewname
        if (model.getType() == PageModelTypeEnum.CUSTOM.ordinal())
        {
            mv.setViewName("page/modelCustom");
        }
        else if (model.getType() == PageModelTypeEnum.TWO_PRODUCT_LIST.ordinal())
        {
            mv.setViewName("page/twoProductList");
        }
        else if (model.getType() == PageModelTypeEnum.ROLL_PRODUCT.ordinal())
        {
            mv.setViewName("page/rollProductList");
        }
        else if (model.getType() == PageModelTypeEnum.FULL_BANNER.ordinal())
        {
            mv.setViewName("page/fullBanner");
        }
        // 不作model 的null 值判断。 出现null 直接跑页面500
        mv.addObject("modelId", model.getId() + "");
        mv.addObject("modelName", model.getName());
        mv.addObject("modelRemark", model.getName());
        mv.addObject("modelType", PageModelTypeEnum.getDescriptionByOrdinal(model.getType()));
        return mv;
    }
    
    /** 异步 获取 模块 数据 */
    @RequestMapping(value = "/ajaxPageModelData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxPageModelData(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "modelId", required = false, defaultValue = "0") int modelId, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay, //
        @RequestParam(value = "productType", required = false, defaultValue = "-1") int productType, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, //
        @RequestParam(value = "bannerIsDisplay", required = false, defaultValue = "-1") int bannerIsDisplay, //
        @RequestParam(value = "bannerStatus", required = false, defaultValue = "-1") int bannerStatus, // 1 即将开始，2 进行中，3
                                                                                                       // 已结束
        @RequestParam(value = "bannerRemark", required = false, defaultValue = "") String bannerRemark //
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            PageModelEntity pme = pageService.findPageModelById(modelId);
            if (pme.getType() == PageModelTypeEnum.CUSTOM.ordinal())
            {
                result = pageService.findModelCustomLayoutInfo(modelId);
            }
            else if (pme.getType() == PageModelTypeEnum.FULL_BANNER.ordinal())
            {
                Map<String, Object> para = new HashMap<>();
                if (page == 0)
                {
                    page = 1;
                }
                para.put("start", rows * (page - 1));
                para.put("max", rows);
                para.put("pageModelId", modelId);
                if (bannerIsDisplay != -1)
                {
                    para.put("isDisplay", bannerIsDisplay);
                }
                if (!"".equals(bannerRemark))
                {
                    para.put("desc", "%" + bannerRemark + "%");
                }
                if (bannerStatus != -1)
                {
                    para.put("nowTime", DateTimeUtil.now());
                    para.put("bannerStatus", bannerStatus);
                    if (bannerStatus == 1)
                    {
                        // 即将开始 start_time > now
                    }
                    else if (bannerStatus == 3)
                    {
                        // 已结束 end_time < now
                    }
                    else
                    {
                        // 进行中 start_time < now and end_time > now
                    }
                }
                result = pageService.findAllPageModelBanner(para);
            }
            else if (pme.getType() == PageModelTypeEnum.ROLL_PRODUCT.ordinal())
            {
                Map<String, Object> para = new HashMap<>();
                if (page == 0)
                {
                    page = 1;
                }
                para.put("start", rows * (page - 1));
                para.put("max", rows);
                para.put("pageModelId", modelId);
                if (isDisplay != -1)
                {
                    para.put("isDisplay", isDisplay);
                }
                if (!"".equals(productName))
                {
                    para.put("productName", "%" + productName + "%");
                }
                if (productType != -1)
                {
                    para.put("productType", productType);
                }
                if (productId != 0)
                {
                    para.put("productId", productId);
                }
                result = pageService.findModelRollProductInfo(para);
            }
            else if (pme.getType() == PageModelTypeEnum.TWO_PRODUCT_LIST.ordinal())
            {
                Map<String, Object> para = new HashMap<>();
                if (page == 0)
                {
                    page = 1;
                }
                para.put("start", rows * (page - 1));
                para.put("max", rows);
                para.put("pageModelId", modelId);
                if (isDisplay != -1)
                {
                    para.put("isDisplay", isDisplay);
                }
                if (!"".equals(productName))
                {
                    para.put("productName", "%" + productName + "%");
                }
                if (productType != -1)
                {
                    para.put("productType", productType);
                }
                if (productId != 0)
                {
                    para.put("productId", productId);
                }
                result = pageService.findModelColumnProductInfo(para);
            }
        }
        catch (Exception e)
        {
            log.error("异步获取模块自定义布局数据失败！", e);
            result.put("total", 0);
            result.put("rows", new ArrayList<>());
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 转发到添加布局页面
     * 
     * @return
     */
    @RequestMapping("/toAdd/{modelId}")
    public ModelAndView toAdd(@PathVariable(value = "modelId") int modelId)
    {
        ModelAndView mv = new ModelAndView("page/updateModelCustom");
        mv.addObject("customLayout", new HashMap<>());
        mv.addObject("modelId", modelId + "");
        return mv;
    }
    
    /**
     * 转发到修改布局页面
     * 
     * @return
     */
    @RequestMapping("/toEdit/{modelId}/{layoutId}")
    public ModelAndView toEdit(@PathVariable(value = "modelId") int modelId, @PathVariable(value = "layoutId") int layoutId)
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> para = new HashMap<>();
        try
        {
            para.put("id", layoutId);
            para.put("modelId", modelId);
            PageModelCustomLayoutEntity customLayout = pageService.findCustomLayoutInfoById(layoutId);
            mv.setViewName("page/updateModelCustom");
            mv.addObject("customLayout", customLayout);
            mv.addObject("modelId", modelId + "");
            mv.addObject("oneDisplayId", customLayout.getOneDisplayId());
            mv.addObject("twoDisplayId", customLayout.getTwoDisplayId());
            mv.addObject("threeDisplayId", customLayout.getThreeDisplayId());
            mv.addObject("fourDisplayId", customLayout.getFourDisplayId());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 自定义拼装页面-编辑自定义模块内容
     * 
     * @param id
     * @param displayType：展示类型；1：单张，1：两张
     * @param oneRemark：第一张备注
     * @param oneImage：第一张图片URL
     * @param oneType：第一张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品，5：签到，6：邀请小伙伴
     * @param oneProductId：商品Id
     * @param oneGroupSale：组合Id
     * @param oneCustomSale：自定义特卖Id
     * @param isDisplay：是否可见；0：否，1：是
     * @return
     */
    @RequestMapping(value = "/updatePageModelCustomLayout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑自定义模块内容")
    public String updatePageModelCustomLayout(@RequestParam(value = "pageModelId", required = true) int pageModelId, // 模块ID
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "displayType", required = false, defaultValue = "1") int displayType, //
        @RequestParam(value = "oneRemark", required = false, defaultValue = "") String oneRemark, //
        @RequestParam(value = "oneImage", required = false, defaultValue = "") String oneImage, //
        @RequestParam(value = "oneType", required = false, defaultValue = "1") int oneType, //
        @RequestParam(value = "oneProductId", required = false, defaultValue = "0") int oneProductId, //
        @RequestParam(value = "oneGroupSale", required = false, defaultValue = "0") int oneGroupSale, //
        @RequestParam(value = "oneCustomSale", required = false, defaultValue = "0") int oneCustomSale, //
        @RequestParam(value = "twoRemark", required = false, defaultValue = "") String twoRemark, //
        @RequestParam(value = "twoImage", required = false, defaultValue = "") String twoImage, //
        @RequestParam(value = "twoType", required = false, defaultValue = "1") int twoType, //
        @RequestParam(value = "twoProductId", required = false, defaultValue = "0") int twoProductId, //
        @RequestParam(value = "twoGroupSale", required = false, defaultValue = "0") int twoGroupSale, //
        @RequestParam(value = "twoCustomSale", required = false, defaultValue = "0") int twoCustomSale, //
        @RequestParam(value = "threeRemark", required = false, defaultValue = "") String threeRemark, //
        @RequestParam(value = "threeImage", required = false, defaultValue = "") String threeImage, //
        @RequestParam(value = "threeType", required = false, defaultValue = "1") int threeType, //
        @RequestParam(value = "threeProductId", required = false, defaultValue = "0") int threeProductId, //
        @RequestParam(value = "threeGroupSale", required = false, defaultValue = "0") int threeGroupSale, //
        @RequestParam(value = "threeCustomSale", required = false, defaultValue = "0") int threeCustomSale, //
        @RequestParam(value = "fourRemark", required = false, defaultValue = "") String fourRemark, //
        @RequestParam(value = "fourImage", required = false, defaultValue = "") String fourImage, //
        @RequestParam(value = "fourType", required = false, defaultValue = "1") int fourType, //
        @RequestParam(value = "fourProductId", required = false, defaultValue = "0") int fourProductId, //
        @RequestParam(value = "fourGroupSale", required = false, defaultValue = "0") int fourGroupSale, //
        @RequestParam(value = "fourCustomSale", required = false, defaultValue = "0") int fourCustomSale, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay //
    )
    {
        Map<String, Object> para = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try
        {
            PageModelCustomLayoutEntity customLayout = new PageModelCustomLayoutEntity();
            customLayout.setId(id);
            customLayout.setPageModelId(pageModelId);
            customLayout.setDisplayType(displayType);
            customLayout.setOneRemark(oneRemark);
            customLayout.setOneImage(oneImage);
            if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
            {
                ProductEntity product = productService.findProductById(oneProductId);
                if (product == null)
                {
                    result.put("status", 0);
                    result.put("msg", "Id=" + oneProductId + "的商品不存在");
                    return JSON.toJSONString(result);
                }
                // 特卖商品，关联的类型=1
                if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    customLayout.setOneType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal());
                    
                }
                // 商城商品，关联的类型=4
                else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    customLayout.setOneType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal());
                }
                customLayout.setOneDisplayId(oneProductId);
            }
            else if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
            {
                customLayout.setOneType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal());
                customLayout.setOneDisplayId(oneGroupSale);
            }
            else if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
            {
                customLayout.setOneType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal());
                customLayout.setOneDisplayId(oneCustomSale);
            }
            else if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                || oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || oneType == CustomLayoutRelationTypeEnum.PICTURE.ordinal())
            {
                customLayout.setOneType(oneType);
                customLayout.setOneDisplayId(0);
            }
            
            if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal() || displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
            {
                customLayout.setTwoRemark(twoRemark);
                customLayout.setTwoImage(twoImage);
                if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    ProductEntity product = productService.findProductById(twoProductId);
                    if (product == null)
                    {
                        result.put("status", 0);
                        result.put("msg", "Id=" + twoProductId + "的商品不存在");
                        return JSON.toJSONString(result);
                    }
                    // 特卖商品，关联的类型=1
                    if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                    {
                        customLayout.setTwoType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal());
                        
                    }
                    // 商城商品，关联的类型=4
                    else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        customLayout.setTwoType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal());
                    }
                    customLayout.setTwoDisplayId(twoProductId);
                }
                else if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    customLayout.setTwoType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal());
                    customLayout.setTwoDisplayId(twoGroupSale);
                }
                else if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    customLayout.setTwoType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal());
                    customLayout.setTwoDisplayId(twoCustomSale);
                }
                else if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                    || twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || twoType == CustomLayoutRelationTypeEnum.PICTURE.ordinal())
                {
                    customLayout.setTwoType(twoType);
                    customLayout.setTwoDisplayId(0);
                }
            }
            else
            {
                
                customLayout.setTwoType(0);
                customLayout.setTwoDisplayId(0);
                customLayout.setTwoRemark("");
                customLayout.setTwoImage("");
            }
            
            if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_FOUR.ordinal())
            {
                customLayout.setThreeRemark(threeRemark);
                customLayout.setThreeImage(threeImage);
                if (threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    ProductEntity product = productService.findProductById(threeProductId);
                    if (product == null)
                    {
                        result.put("status", 0);
                        result.put("msg", "Id=" + threeProductId + "的商品不存在");
                        return JSON.toJSONString(result);
                    }
                    // 特卖商品，关联的类型=1
                    if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                    {
                        customLayout.setThreeType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal());
                        
                    }
                    // 商城商品，关联的类型=4
                    else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        customLayout.setThreeType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal());
                    }
                    customLayout.setThreeDisplayId(threeProductId);
                }
                else if (threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    customLayout.setThreeType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal());
                    customLayout.setThreeDisplayId(threeGroupSale);
                }
                else if (threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    customLayout.setThreeType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal());
                    customLayout.setThreeDisplayId(threeCustomSale);
                }
                else if (threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                    || threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || threeType == CustomLayoutRelationTypeEnum.PICTURE.ordinal())
                {
                    customLayout.setThreeType(threeType);
                    customLayout.setThreeDisplayId(0);
                }
                
                customLayout.setFourRemark(fourRemark);
                customLayout.setFourImage(fourImage);
                if (fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                {
                    ProductEntity product = productService.findProductById(fourProductId);
                    if (product == null)
                    {
                        result.put("status", 0);
                        result.put("msg", "Id=" + fourProductId + "的商品不存在");
                        return JSON.toJSONString(result);
                    }
                    // 特卖商品，关联的类型=1
                    if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                    {
                        customLayout.setFourType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal());
                        
                    }
                    // 商城商品，关联的类型=4
                    else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                    {
                        customLayout.setFourType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal());
                    }
                    customLayout.setFourDisplayId(fourProductId);
                }
                else if (fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                {
                    customLayout.setFourType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal());
                    customLayout.setFourDisplayId(fourGroupSale);
                }
                else if (fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
                {
                    customLayout.setFourType(CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal());
                    customLayout.setFourDisplayId(fourCustomSale);
                }
                else if (fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                    || fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || fourType == CustomLayoutRelationTypeEnum.PICTURE.ordinal())
                {
                    customLayout.setFourType(fourType);
                    customLayout.setFourDisplayId(0);
                }
            }
            else
            {
                customLayout.setThreeType(0);
                customLayout.setThreeDisplayId(0);
                customLayout.setThreeRemark("");
                customLayout.setThreeImage("");
                customLayout.setFourType(0);
                customLayout.setFourDisplayId(0);
                customLayout.setFourRemark("");
                customLayout.setFourImage("");
            }
            
            customLayout.setIsDisplay(isDisplay);
            para.put("customLayout", customLayout);
            int status = pageService.insertOrUpdatePageModelCustomLayout(customLayout);
            if (status == 1)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            log.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /** 更新编辑自定义模块-修改排序值或展现状态 */
    @RequestMapping(value = "/updatePageModelCustomLayoutSimpleData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑自定义模块-修改排序值或展现状态")
    public String updatePageModelCustomLayoutSimpleData(@RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay, // 展现状态
        @RequestParam(value = "sequence", required = false, defaultValue = "-1") int sequence // 排序值
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            if (sequence != -1)
            {
                para.put("sequence", sequence);
            }
            int status = pageService.updatePageModelCustomLayoutSimpleData(para);
            result.put("status", status);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("自定义拼装页面-编辑自定义模块-修改排序值或展现状态失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 滚动商品 更新修改排序值或展现状态 */
    @RequestMapping(value = "/updatePageModelRelationRollProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑滚动商品模块-修改排序值或展现状态")
    public String updatePageModelRelationRollProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay, // 模块展现状态
        @RequestParam(value = "sequence", required = false, defaultValue = "-1") int sequence // 模块展现状态
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            if ("".equals(ids))
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            List<Integer> idList = new ArrayList<>();
            if (ids.indexOf(",") > -1)
            {
                for (String id : ids.split(","))
                {
                    if (StringUtils.isNumeric(id))
                    {
                        idList.add(Integer.valueOf(id));
                    }
                }
            }
            else
            {
                idList.add(Integer.valueOf(ids));
            }
            
            Map<String, Object> para = new HashMap<>();
            para.put("idList", idList);
            
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            if (sequence != -1)
            {
                para.put("sequence", sequence);
            }
            int status = pageService.updatePageModelRelationRollProduct(para);
            result.put("status", status >= 1 ? 1 : 0);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 删除 模块滚动商品 */
    @RequestMapping(value = "/deletePageModelRelationRollProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑滚动商品模块-删除商品")
    public String deletePageModelRelationRollProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            if ("".equals(ids))
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            List<Integer> idList = new ArrayList<>();
            if (ids.indexOf(",") > -1)
            {
                for (String id : ids.split(","))
                {
                    if (StringUtils.isNumeric(id))
                    {
                        idList.add(Integer.valueOf(id));
                    }
                }
            }
            else
            {
                idList.add(Integer.valueOf(ids));
            }
            int status = pageService.deletePageModelRelationRollProduct(idList);
            result.put("status", status >= 1 ? 1 : 0);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 自定义拼装页面-编辑滚动商品模块-添加商品 */
    @RequestMapping(value = "/addPageModelRelationRollProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑滚动商品模块-添加商品")
    public String addPageModelRelationRollProduct(@RequestParam(value = "modelId", required = true) String modelId, //
        @RequestParam(value = "ids", required = true) String ids //
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            if ("".equals(ids))
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            List<Map<String, Object>> list = new ArrayList<>();
            if (ids.indexOf(",") > -1)
            {
                for (String id : ids.split(","))
                {
                    if (StringUtils.isNumeric(id))
                    {
                        Map<String, Object> map = new HashMap<>();
                        map.put("pageModelId", modelId);
                        map.put("productId", id);
                        list.add(map);
                    }
                }
            }
            else
            {
                Map<String, Object> map = new HashMap<>();
                map.put("pageModelId", modelId);
                map.put("productId", ids);
                list.add(map);
            }
            
            if (list.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            
            int status = pageService.insertBatchPageModelRelationRollProduct(list, Integer.valueOf(modelId));
            String msg = "";
            if (status == -2)
            {
                msg = "商品ID不准确";
            }
            else if (status > 0)
            {
                msg = "保存成功";
            }
            else
            {
                msg = "保存失败";
            }
            result.put("status", status >= 1 ? 1 : 0);
            result.put("msg", msg);
        }
        catch (Exception e)
        {
            log.error("自定义拼装页面-编辑自定义模块-修改排序值或展现状态失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 两栏商品 更新修改排序值或展现状态 */
    @RequestMapping(value = "/updatePageModelRelationColumnProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑两栏商品模块-修改排序值或展现状态")
    public String updatePageModelRelationColumnProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay, // 模块展现状态
        @RequestParam(value = "sequence", required = false, defaultValue = "-1") int sequence // 模块展现状态
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            if ("".equals(ids))
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            List<Integer> idList = new ArrayList<>();
            if (ids.indexOf(",") > -1)
            {
                for (String id : ids.split(","))
                {
                    if (StringUtils.isNumeric(id))
                    {
                        idList.add(Integer.valueOf(id));
                    }
                }
            }
            else
            {
                idList.add(Integer.valueOf(ids));
            }
            
            Map<String, Object> para = new HashMap<>();
            para.put("idList", idList);
            
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            if (sequence != -1)
            {
                para.put("sequence", sequence);
            }
            int status = pageService.updatePageModelRelationColumnProduct(para);
            result.put("status", status >= 1 ? 1 : 0);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 删除 模块两栏商品 */
    @RequestMapping(value = "/deletePageModelRelationColumnProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑两栏商品模块-删除商品")
    public String deletePageModelRelationColumnProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            if ("".equals(ids))
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            List<Integer> idList = new ArrayList<>();
            if (ids.indexOf(",") > -1)
            {
                for (String id : ids.split(","))
                {
                    if (StringUtils.isNumeric(id))
                    {
                        idList.add(Integer.valueOf(id));
                    }
                }
            }
            else
            {
                idList.add(Integer.valueOf(ids));
            }
            int status = pageService.deletePageModelRelationColumnProduct(idList);
            result.put("status", status >= 1 ? 1 : 0);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 自定义拼装页面-编辑两栏商品模块-添加商品 */
    @RequestMapping(value = "/addPageModelRelationColumnProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑两栏商品模块-添加商品")
    public String addPageModelRelationColumnProduct(@RequestParam(value = "modelId", required = true) String modelId, //
        @RequestParam(value = "ids", required = true) String ids //
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            if ("".equals(ids))
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            List<Map<String, Object>> list = new ArrayList<>();
            if (ids.indexOf(",") > -1)
            {
                for (String id : ids.split(","))
                {
                    if (StringUtils.isNumeric(id))
                    {
                        Map<String, Object> map = new HashMap<>();
                        map.put("pageModelId", modelId);
                        map.put("productId", id);
                        list.add(map);
                    }
                }
            }
            else
            {
                Map<String, Object> map = new HashMap<>();
                map.put("pageModelId", modelId);
                map.put("productId", ids);
                list.add(map);
            }
            
            if (list.isEmpty())
            {
                result.put("status", 0);
                result.put("msg", "数据为空。保存失败！");
                return JSON.toJSONString(result);
            }
            
            int status = pageService.insertBatchPageModelRelationColumnProduct(list, Integer.valueOf(modelId));
            String msg = "";
            if (status == -2)
            {
                msg = "商品ID不准确";
            }
            else if (status > 0)
            {
                msg = "保存成功";
            }
            else
            {
                msg = "保存失败";
            }
            result.put("status", status >= 1 ? 1 : 0);
            result.put("msg", msg);
        }
        catch (Exception e)
        {
            log.error("自定义拼装页面-编辑自定义模块-修改排序值或展现状态失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 通栏banner添加页面
     *
     * @return
     */
    @RequestMapping("/toAddBanner/{modelId}")
    public ModelAndView toAddBanner(@PathVariable(value = "modelId") int modelId)
    {
        ModelAndView mv = new ModelAndView("page/updateBanner");
        mv.addObject("bannerWindow", new PageModelBannerEntity());
        mv.addObject("modelId", modelId + "");
        return mv;
    }
    
    /**
     * 通栏banner修改页面
     *
     * @return
     */
    @RequestMapping("/toEditBanner/{modelId}/{id}")
    public ModelAndView toEditBanner(@PathVariable(value = "modelId") int modelId, @PathVariable(value = "id") int id)
    {
        ModelAndView mv = new ModelAndView("page/updateBanner");
        try
        {
            PageModelBannerEntity pmbe = pageService.findPageModelBannerById(id);
            mv.addObject("bannerWindow", pmbe);
            mv.addObject("modelId", modelId + "");
            mv.addObject("startTime", DateTimeUtil.timestampStringToWebString(pmbe.getStartTime()));
            mv.addObject("endTime", DateTimeUtil.timestampStringToWebString(pmbe.getEndTime()));
        }
        catch (Exception e)
        {
            log.error("通栏banner修改页面失败！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping("/saveBanner")
    @ControllerLog(description = "自定义页面-banner模块-新增/修改banner")
    public ModelAndView saveBanner(
        @RequestParam(value = "editId", required = false, defaultValue = "0") int editId, //
        @RequestParam(value = "modelId", required = false, defaultValue = "0") int modelId, //
        @RequestParam(value = "type", required = true) byte type, // 1单品；2组合特卖；3自定义活动;4自定义页面（暂时不用）
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "image", required = true) String image, //
        @RequestParam(value = "code", required = false, defaultValue = "0") int code, // 商品ID
        @RequestParam(value = "groupSale", required = false, defaultValue = "0") int groupSale,
        @RequestParam(value = "customSale", required = false, defaultValue = "0") int customSale, @RequestParam(value = "startTime", required = true) String startTime, //
        @RequestParam(value = "endTime", required = true) String endTime, //
        @RequestParam(value = "isDisplay", required = true) byte isDisplay//
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        PageModelBannerEntity pmbe = new PageModelBannerEntity();
        pmbe.setId(editId);
        pmbe.setPageModelId(modelId);
        pmbe.setType(type);
        pmbe.setDesc(desc);
        pmbe.setImage(image);
        pmbe.setIsDisplay(isDisplay);
        if (type == 1)
        {
            pmbe.setDisplayId(code);
        }
        else if (type == 2)
        {
            pmbe.setDisplayId(groupSale);
        }
        else if (type == 3)
        {
            pmbe.setDisplayId(customSale);
        }
        else if (type == 6)
        {
            pmbe.setDisplayId(0);
        }
        pmbe.setStartTime(startTime);
        pmbe.setEndTime(endTime);
        pageService.insertOrUpdatePageModelBanner(pmbe);
        mv.setViewName("redirect:/page/modelManage/" + modelId);
        return mv;
    }
    
    /** 自定义页面-banner模块展现状态 */
    @RequestMapping(value = "/updateBanner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义页面-banner模块展现状态")
    public String updateBanner(@RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "isDisplay", required = true) int isDisplay // 展现状态
    )
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            para.put("isDisplay", isDisplay);
            int status = pageService.updateSimplePageModelBanner(para);
            result.put("status", status);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }
        catch (Exception e)
        {
            log.error("自定义页面-banner模块展现状态失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/ajaxAppCustomPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxAppCustomPage(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("isAvailable", isAvailable);
            Map<String, Object> pages = pageService.findAllPageByPara(para);
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> tmp : (List<Map<String, Object>>)pages.get("rows"))
            {
                Map<String, String> map = new HashMap<>();
                map.put("code", tmp.get("id") + "");
                map.put("text", tmp.get("name") + "");
                int cId = Integer.valueOf(tmp.get("id") + "").intValue();
                if (id == cId)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            log.error("异步获取自定义页面信息失败", e);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 可用商品列表，供两栏、滚动商品模块添加，过滤重复商品
     *
     * @param page
     * @param rows
     * @param id modelID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajaxProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code,//
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "status", required = false, defaultValue = "0") int status,//
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type,//
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,//
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "0") int isOffShelves)
        throws Exception
    {
        Map resultMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (status == 0)
        {
            resultMap.put("rows", resultList);
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
        
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (brandId != 0)
        {
            para.put("brandId", brandId);
        }
        if (sellerId != 0)
        {
            para.put("sellerId", sellerId);
        }
        if (productId != 0)
        {
            para.put("productId", productId);
        }
        if (!"".equals(remark))
        {
            para.put("remark", "%" + remark + "%");
        }
        para.put("cid", id);
        para.put("type", type);
        para.put("isAvailable", isAvailable);
        para.put("isOffShelves", isOffShelves);
        
        Map<String, Object> result = pageService.findProductListForAdd(para);
        return JSON.toJSONString(result);
    }
    
}
