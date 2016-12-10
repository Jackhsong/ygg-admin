/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡
APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.controller.categoryregion;

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
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.controller.CustomRegionController;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.categoryregion.Page2ModelCustomLayoutEntity;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.categorymanager.CateGoryManagerService;
import com.ygg.admin.service.categoryregion.CategoryRegionService;

/**
  * 品类馆自定义板块
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryRegionController.java 9206 2016-03-24 09:13:16Z xiongliang $   
  * @since 2.0
  */
@Controller
@RequestMapping("/categoryRegion")
public class CategoryRegionController
{
    private static Logger logger = Logger.getLogger(CustomRegionController.class);
    
    @Resource
    private ProductService productService;
    
    /**品类馆管理服务接口*/
    @Resource(name = "cateGoryManagerService")
    private CateGoryManagerService cateGoryManagerService;
    
    /**    */
    @Resource(name = "categoryRegionService")
    private CategoryRegionService categoryRegionService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("categoryregion/customRegionList");
        return mv;
    }
    
    /** 自定义拼装页面管理 */
    @RequestMapping("/listCategoryRegion/{modelId}/{pageId}")
    public ModelAndView pageManage(@PathVariable("modelId") int modelId, @PathVariable("pageId") int pageId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("categoryregion/customRegionList");
        
        Map<String, Object> pageInfo = cateGoryManagerService.findPageById(pageId);
        mv.addObject("pageId", pageId + "");
        mv.addObject("pageName", pageInfo.get("name") + "");
        mv.addObject("modelId", modelId + "");
        return mv;
    }
    
    /**
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonCustomRegionInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCustomRegionInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") String isAvailable,
        @RequestParam(value = "page2ModelId", defaultValue = "-1", required = false) int page2ModelId)
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
            if (!isAvailable.contains(","))
            {
                para.put("isAvailable", isAvailable);
            }
            para.put("page2ModelId", page2ModelId);
            resultMap = categoryRegionService.jsonCustomRegionInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 保存/更新自定义板块
     * @param id
     * @param title
     * @param remark
     * @param isDisplay
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateCustonRegion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-新增/编辑自定义板块")
    public String saveOrUpdateCustonRegion(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "modelId", defaultValue = "-1", required = false) int modelId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("title", title);
            para.put("remark", remark);
            para.put("isDisplay", isDisplay);
            para.put("isAvailable", isAvailable);
            para.put("modelId", modelId);
            int status = categoryRegionService.saveOrUpdateCustonRegion(para);
            if (status == 1)
            {
                resultMap.put("status", status);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新自定义板块排序值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateCustomRegionSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-更新自定义板块排序值")
    public String updateCustomRegionSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = categoryRegionService.updateCustomRegionSequence(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 更新自定义板块展现状态
     * @param id
     * @param code：1展现，0不展现
     * @return
     */
    @RequestMapping(value = "/updateCustomRegionDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-更新自定义板块展现状态")
    public String updateCustomRegionDisplayStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("code", code);
            List<Integer> idList = new ArrayList<Integer>();
            String[] idStrArr = id.split(",");
            for (String idStr : idStrArr)
            {
                idList.add(Integer.valueOf(idStr).intValue());
            }
            para.put("idList", idList);
            int status = categoryRegionService.updateCustomRegionDisplayStatus(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 更新自定义板块是否可用
     * @param id
     * @param code：1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/updateCustomRegionAvailableStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-更新自定义板块可用状态")
    public String updateCustomRegionAvailableStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("code", code);
            List<Integer> idList = new ArrayList<Integer>();
            String[] idStrArr = id.split(",");
            for (String idStr : idStrArr)
            {
                idList.add(Integer.valueOf(idStr).intValue());
            }
            para.put("idList", idList);
            int status = categoryRegionService.updateCustomRegionAvailableStatus(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 板块布局管理
     * @param regionId：板块Id
     * @return
     */
    @RequestMapping("/manageLayout/{regionId}/{modelId}/{pageId}")
    public ModelAndView manageLayout(@PathVariable("regionId") int regionId, @PathVariable("modelId") int modelId, @PathVariable("pageId") int pageId)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("categoryregion/customLayoutList");
            Map<String, Object> resultMap = categoryRegionService.findCustomRegionById(regionId);
            if (resultMap == null || resultMap.size() == 0)
            {
                mv.setViewName("error/404");
            }
            mv.addObject("customRegion", resultMap);
            mv.addObject("regionId", regionId + "");
            mv.addObject("modelId", modelId + "");
            mv.addObject("pageId", pageId + "");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonCustomLayoutInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCustomLayoutInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "regionId", required = true) int regionId)
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
            para.put("regionId", regionId);
            resultMap = categoryRegionService.jsonCustomLayoutInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新布局展现状态
     * @param layoutId：布局Id
     * @param code：展现状态，1展现，0不展现
     * @return
     */
    @RequestMapping(value = "/updateCustomLayoutDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-更新布局展现状态")
    public String updateCustomLayoutDisplayStatus(@RequestParam(value = "layoutId", required = true) String layoutId, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("code", code);
            List<Integer> idList = new ArrayList<Integer>();
            String[] idStrArr = layoutId.split(",");
            for (String idStr : idStrArr)
            {
                idList.add(Integer.valueOf(idStr).intValue());
            }
            para.put("idList", idList);
            int status = categoryRegionService.updateCustomLayoutDisplayStatus(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 更新布局排序值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateCustomLayoutSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-更新布局排序值")
    public String updateCustomLayoutSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = categoryRegionService.updateCustomLayoutSequence(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 转发到添加布局页面
     * @return
     */
    @RequestMapping("/toAdd/{regionId}/{modelId}/{pageId}")
    public ModelAndView toAdd(@PathVariable(value = "regionId") int regionId, @PathVariable("modelId") int modelId, @PathVariable("pageId") int pageId)
    {
        ModelAndView mv = new ModelAndView("categoryregion/update");
        mv.addObject("regionId", regionId + "");
        mv.addObject("modelId", modelId + "");
        mv.addObject("pageId", pageId + "");
        mv.addObject("customLayout", new HashMap<String, Object>());
        return mv;
    }
    
    /**
     * 
     * @param id
     * @param displayType：展示类型；1：单张，1：两张
     * @param oneRemark：第一张备注
     * @param oneImage：第一张图片URL
     * @param oneType：第一张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品，5：签到，6：邀请小伙伴
     * @param oneProductId：商品Id
     * @param oneGroupSale：组合Id
     * @param oneCustomSale：自定义特卖Id
     * @param twoRemark：第二张备注
     * @param twoImage：第二张图片URL
     * @param twoType：第二张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品，5：签到，6：邀请小伙伴
     * @param twoProductId：商品Id
     * @param twoGroupSale：组合Id
     * @param twoCustomSale：自定义Id
     * @param isDisplay：是否可见；0：否，1：是
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateCustomLayout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-新增/编辑布局")
    public String saveOrUpdateCustomLayout(@RequestParam(value = "regionId", required = true) int regionId,
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, @RequestParam(value = "displayType", required = false, defaultValue = "1") int displayType,
        @RequestParam(value = "oneRemark", required = false, defaultValue = "") String oneRemark,
        @RequestParam(value = "oneImage", required = false, defaultValue = "") String oneImage, @RequestParam(value = "oneType", required = false, defaultValue = "1") int oneType,
        @RequestParam(value = "oneProductId", required = false, defaultValue = "0") int oneProductId,
        @RequestParam(value = "oneGroupSale", required = false, defaultValue = "0") int oneGroupSale,
        @RequestParam(value = "oneCustomSale", required = false, defaultValue = "0") int oneCustomSale,
        @RequestParam(value = "oneAppCustomPage", required = false, defaultValue = "0") int oneAppCustomPage,
        @RequestParam(value = "twoRemark", required = false, defaultValue = "") String twoRemark,
        @RequestParam(value = "twoImage", required = false, defaultValue = "") String twoImage, @RequestParam(value = "twoType", required = false, defaultValue = "1") int twoType,
        @RequestParam(value = "twoProductId", required = false, defaultValue = "0") int twoProductId,
        @RequestParam(value = "twoGroupSale", required = false, defaultValue = "0") int twoGroupSale,
        @RequestParam(value = "twoCustomSale", required = false, defaultValue = "0") int twoCustomSale,
        @RequestParam(value = "twoAppCustomPage", required = false, defaultValue = "0") int twoAppCustomPage,
        @RequestParam(value = "threeRemark", required = false, defaultValue = "") String threeRemark,
        @RequestParam(value = "threeImage", required = false, defaultValue = "") String threeImage,
        @RequestParam(value = "threeType", required = false, defaultValue = "1") int threeType,
        @RequestParam(value = "threeProductId", required = false, defaultValue = "0") int threeProductId,
        @RequestParam(value = "threeGroupSale", required = false, defaultValue = "0") int threeGroupSale,
        @RequestParam(value = "threeCustomSale", required = false, defaultValue = "0") int threeCustomSale,
        @RequestParam(value = "threeAppCustomPage", required = false, defaultValue = "0") int threeAppCustomPage,
        @RequestParam(value = "fourRemark", required = false, defaultValue = "") String fourRemark,
        @RequestParam(value = "fourImage", required = false, defaultValue = "") String fourImage,
        @RequestParam(value = "fourType", required = false, defaultValue = "1") int fourType,
        @RequestParam(value = "fourProductId", required = false, defaultValue = "0") int fourProductId,
        @RequestParam(value = "fourGroupSale", required = false, defaultValue = "0") int fourGroupSale,
        @RequestParam(value = "fourCustomSale", required = false, defaultValue = "0") int fourCustomSale,
        @RequestParam(value = "fourAppCustomPage", required = false, defaultValue = "0") int fourAppCustomPage,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay)
    {
        Map<String, Object> para = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try
        {
            para.put("regionId", regionId);
            Page2ModelCustomLayoutEntity customLayout = new Page2ModelCustomLayoutEntity();
            customLayout.setPage2ModelCustomRegionId(regionId);
            customLayout.setId(id);
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
            else if (oneType == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
            {
                customLayout.setOneType(CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal());
                customLayout.setOneDisplayId(oneAppCustomPage);
            }
            else if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                || oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || oneType == CustomLayoutRelationTypeEnum.PICTURE.ordinal()
                || oneType == CustomLayoutRelationTypeEnum.INTEGRAL_MALL.ordinal())
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
                else if (twoType == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    customLayout.setTwoType(CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal());
                    customLayout.setTwoDisplayId(twoAppCustomPage);
                }
                else if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                    || twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || twoType == CustomLayoutRelationTypeEnum.PICTURE.ordinal()
                    || oneType == CustomLayoutRelationTypeEnum.INTEGRAL_MALL.ordinal())
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
                else if (threeType == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    customLayout.setThreeType(CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal());
                    customLayout.setThreeDisplayId(threeAppCustomPage);
                }
                else if (threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                    || threeType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || threeType == CustomLayoutRelationTypeEnum.PICTURE.ordinal()
                    || oneType == CustomLayoutRelationTypeEnum.INTEGRAL_MALL.ordinal())
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
                else if (fourType == CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal())
                {
                    customLayout.setFourType(CustomLayoutRelationTypeEnum.APP_CUSTOM_PAGE.ordinal());
                    customLayout.setFourDisplayId(fourAppCustomPage);
                }
                else if (fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_INVITEFRIEND.ordinal()
                    || fourType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SIGNIN.ordinal() || fourType == CustomLayoutRelationTypeEnum.PICTURE.ordinal()
                    || oneType == CustomLayoutRelationTypeEnum.INTEGRAL_MALL.ordinal())
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
            int status = categoryRegionService.saveOrUpdateCustomLayout(para);
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
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    @RequestMapping("/toEdit/{regionId}/{layoutId}/{modelId}/{pageId}")
    public ModelAndView toEdit(@PathVariable(value = "regionId") int regionId, @PathVariable(value = "layoutId") int layoutId, @PathVariable("modelId") int modelId,
        @PathVariable("pageId") int pageId)
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            para.put("id", layoutId);
            para.put("regionId", regionId);
            Map<String, Object> customLayout = categoryRegionService.findCustomLayoutBydId(para);
            if (customLayout == null)
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.setViewName("categoryregion/update");
            mv.addObject("customLayout", customLayout);
            mv.addObject("regionId", regionId + "");
            mv.addObject("modelId", modelId + "");
            mv.addObject("pageId", pageId + "");
            mv.addObject("oneDisplayId", customLayout.get("oneDisplayId"));
            mv.addObject("twoDisplayId", customLayout.get("twoDisplayId"));
            mv.addObject("threeDisplayId", customLayout.get("threeDisplayId"));
            mv.addObject("fourDisplayId", customLayout.get("fourDisplayId"));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/deleteCustomLayout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义板块管理-删除板块")
    public String deleteCustomLayout(@RequestParam(value = "id", required = true) int id)
    {
        try
        {
            return categoryRegionService.deleteCustomLayout(id);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
            return JSON.toJSONString(result);
        }
    }
}
