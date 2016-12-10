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
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SpecialGroupActivityService;

/**
 * 组合情景特卖活动
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/specialGroup")
public class SpecialGroupActivityController
{
    private static Logger logger = Logger.getLogger(SpecialGroupActivityController.class);
    
    @Resource
    private SpecialGroupActivityService specialGroupActivityService;
    
    @Resource
    private ProductService productService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("specialGroup/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonSpecialGroupInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpecialGroupInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "title", required = false, defaultValue = "") String title, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (!"".equals(title))
            {
                para.put("title", "%" + title + "%");
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            return specialGroupActivityService.findSpecialGroupActivityByPara(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载组合情景特卖出错：" + e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("rows", new ArrayList<>());
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdateSpecialGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "组合情景活动管理-编辑组合情景")
    public String saveOrUpdateSpecialGroup(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (id == 0)
            {
                return specialGroupActivityService.saveSpecialGroup(title, isAvailable);
            }
            else
            {
                return specialGroupActivityService.updateSpecialGroup(id, title, isAvailable);
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
    
    @RequestMapping("/floorProduct/{specialGroupActivityId}")
    public ModelAndView floorProduct(@PathVariable("specialGroupActivityId") int specialGroupActivityId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("specialGroup/floorProduct");
        mv.addObject("activityId", specialGroupActivityId + "");
        mv.addObject("type", 1);
        Map<String, Object> activity = specialGroupActivityService.findSpecialGroupActivityById(specialGroupActivityId);
        if (activity != null)
        {
            mv.addObject("activity", activity);
        }
        return mv;
    }
    
    @RequestMapping("/moreProduct/{specialGroupActivityId}")
    public ModelAndView moreProduct(@PathVariable("specialGroupActivityId") int specialGroupActivityId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("specialGroup/moreProduct");
        mv.addObject("activityId", specialGroupActivityId + "");
        mv.addObject("type", 2);
        Map<String, Object> activity = specialGroupActivityService.findSpecialGroupActivityById(specialGroupActivityId);
        if (activity != null)
        {
            mv.addObject("activity", activity);
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonFloorProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonFloorProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, //
        @RequestParam(value = "activityId", required = true) int activityId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("type", type);
        para.put("activityId", activityId);
        Map<String, Object> result = specialGroupActivityService.findSpecialGroupActivityProductByPara(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 
     * @param id：id
     * @param type：类型，1楼层，2更多
     * @param activityId：活动Id
     * @param layoutType：布局类型，1一张一张，2一行两张
     * @param oneType：第一张关联类型，1：商品，2：组合；3：自定义活动；4：点击不跳转
     * @param oneRemark：第一张备注
     * @param oneImageUrl：第一张图片url
     * @param oneDisplayId：第一张关联对象Id
     * @param twoType：第二张关联类型，1：商品，2：组合；3：自定义活动；4：点击不跳转
     * @param twoRemark：第二张备注
     * @param twoImageUrl：第二张图片url
     * @param twoDisplayId：第二张关联对象Id
     * @param productId：商品Id
     * @param isDisplay：是否展现，1是，0否
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editSpecialGroupActivityProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "情景模版-编辑情景模版商品")
    public String editSpecialGroupActivityProduct(
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, //
        @RequestParam(value = "activityId", required = false, defaultValue = "0") int activityId, //
        @RequestParam(value = "layoutType", required = false, defaultValue = "1") int layoutType, //
        @RequestParam(value = "oneType", required = false, defaultValue = "1") int oneType, //
        @RequestParam(value = "oneRemark", required = false, defaultValue = "") String oneRemark, //
        @RequestParam(value = "oneImageUrl", required = false, defaultValue = "") String oneImageUrl,
        @RequestParam(value = "oneDisplayId", required = false, defaultValue = "0") int oneDisplayId,
        @RequestParam(value = "twoType", required = false, defaultValue = "1") int twoType, //
        @RequestParam(value = "twoRemark", required = false, defaultValue = "") String twoRemark, //
        @RequestParam(value = "twoImageUrl", required = false, defaultValue = "") String twoImageUrl,
        @RequestParam(value = "twoDisplayId", required = false, defaultValue = "0") int twoDisplayId,
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
        throws Exception
    {
        return specialGroupActivityService.editSpecialGroupActivityProduct(id, activityId, type, layoutType, oneType, oneRemark, oneImageUrl, oneDisplayId, twoType, twoRemark, twoImageUrl, twoDisplayId, productId, isDisplay);
    }
    
    @RequestMapping(value = "/updateSpecialGroupActivityProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "情景模版-编辑商品排序值")
    public String updateSpecialGroupActivityProductSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            return specialGroupActivityService.updateSpecialGroupActivityProductSequence(id, sequence);
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/deleteSpecialGroupActivityProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "情景模版-删除商品")
    public String deleteSpecialGroupActivityProduct(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            return specialGroupActivityService.deleteSpecialGroupActivityProduct(id);
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/updateSpecialGroupActivityProductDisplay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "情景模版-编辑商品展现状态")
    public String updateSpecialGroupActivityProductDisplay(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            return specialGroupActivityService.updateSpecialGroupActivityProductDisplay(id, isDisplay);
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/batchAddSpecialGroupActivityProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "情景模版-批量添加情景模版商品")
    public String batchAddSpecialGroupActivityProduct(//
        @RequestParam(value = "activityId", required = false, defaultValue = "0") int activityId, //
        @RequestParam(value = "productIds", required = false, defaultValue = "") String productIds, //
        @RequestParam(value = "type", required = false, defaultValue = "2") int type, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            List<Integer> productIdList = new ArrayList<Integer>();
            for (String productId : productIds.split(","))
            {
                ProductEntity pe = productService.findProductById(Integer.valueOf(productId.trim()));
                if (pe != null)
                {
                    productIdList.add(Integer.parseInt(productId.trim()));
                }
            }
            
            return specialGroupActivityService.batchAddSpecialGroupActivityProduct(activityId, type, isDisplay, productIdList);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙，请稍后再试~~~");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/jsonSpecialGroupCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpecialGroupCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> list = specialGroupActivityService.findAllSpecialGroupActivity(para);
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> mp : list)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", mp.get("id").toString());
                map.put("text", mp.get("title").toString());
                if (Integer.parseInt(mp.get("id").toString()) == id)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("code", "0");
            mapAll.put("text", "暂无数据");
            codeList.add(mapAll);
        }
        return JSON.toJSONString(codeList);
    }
}
