package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.ygg.admin.service.ActivitySimplifyService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.StringUtils;

/**
 * 精品特卖活动
 */
@Controller
@RequestMapping("/simplify")
public class ActivitySimplifyController
{
    private static Logger logger = Logger.getLogger(ActivitySimplifyController.class);
    
    @Resource
    private ActivitySimplifyService activitySimplifyService;
    
    @Resource
    private ProductService productService;

    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("activitySimplify/activitySimplifyList");
        return mv;
    }
    
    /**
     * 精品活动列表
     * @param page
     * @param rows
     * @param title
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonActivitySimplifyInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonActivitySimplifyInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "title", required = false, defaultValue = "") String title,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (!"".equals(title))
            {
                para.put("title", "%" + title + "%");
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = activitySimplifyService.jsonActivitySimplifyInfo(para);
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
     * 新增/修改精品活动
     * @param id
     * @param title
     * @param desc
     * @param image
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateActivitySimplify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "精品活动管理-新增/修改精品活动")
    public String saveOrUpdateActivitySimplify(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "image", required = false, defaultValue = "") String image, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("title", title);
            para.put("desc", desc);
            para.put("image", image);
            para.put("isAvailable", isAvailable);
            int status = activitySimplifyService.saveOrUpdateActivitySimplify(para);
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
     * 修改精品活动展现状态
     * @param id
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateActivitySimplifyAvailableStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "精品活动管理-修改精品活动展现状态")
    public String updateActivitySimplifyAvailableStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int isAvailable)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isAvailable", isAvailable);
            para.put("idList", Arrays.asList(id.split(",")));
            int status = activitySimplifyService.updateActivitySimplifyAvailableStatus(para);
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
     * 版块管理
     * @param activityId
     * @return
     */
    @RequestMapping("/manageLayout/{activityId}")
    public ModelAndView manageLayout(@PathVariable("activityId") int activityId)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("activitySimplify/simplifyActivityLayoutList");
            
            Map<String, Object> resultMap = activitySimplifyService.findActivitySimplifyById(activityId);
            if (resultMap == null || resultMap.size() == 0)
            {
                mv.setViewName("error/404");
            }
            mv.addObject("activity", resultMap);
            mv.addObject("activityId", activityId + "");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 版块管理 json
     * @param page
     * @param rows
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/jsonActivitySimplifyProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonActivitySimplifyProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "activityId", required = true) int activityId)
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
            para.put("activityId", activityId);
            resultMap = activitySimplifyService.jsonActivitySimplifyLayoutProductInfo(para);
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
     * 修改精品活动商品展现状态
     * @param id
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateActivitySimplifyProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "精品活动管理-修改精品活动商品展现状态")
    public String updateActivitySimplifyProductDisplayStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isDisplay", code);
            para.put("idList", Arrays.asList(id.split(",")));
            int status = activitySimplifyService.updateActivitySimplifyProductDisplayStatus(para);
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
     * 修改精品活动商品排序值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateActivitySimplifyProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "精品活动管理-修改精品活动商品排序值")
    public String updateActivitySimplifyProductSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = activitySimplifyService.updateActivitySimplifyProductSequence(para);
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
     * 新增/修改精品活动商品
     * @param id
     * @param activityId
     * @param title
     * @param desc
     * @param productId
     * @param image
     * @param isDisplay
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateActivitySimplifyProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "精品活动管理-新增/修改精品活动商品")
    public String saveOrUpdateActivitySimplifyProduct(@RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "activityId", required = false, defaultValue = "0") int activityId,//
        @RequestParam(value = "title", required = false, defaultValue = "") String title,// 
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc,//
        @RequestParam(value = "productId", required = false, defaultValue = "") int productId,//
        @RequestParam(value = "image", required = false, defaultValue = "") String image, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            ProductEntity pe = productService.findProductById(productId);
            if (pe == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id=" + productId + "的商品不存在");
                return JSON.toJSONString(resultMap);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("activityId", activityId);
            para.put("title", title);
            para.put("desc", desc);
            if (StringUtils.isEmpty(image))
            {
                image = pe.getImage1();
            }
            para.put("image", image);
            para.put("productId", productId);
            para.put("isDisplay", isDisplay);
            int status = activitySimplifyService.saveOrUpdateActivitySimplifyProduct(para);
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

    @RequestMapping(value = "/jsonActivitySimplifyCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonActivitySimplifyCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> resultList = activitySimplifyService.findAllActivitySimplify(para);
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> tmp : resultList)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", tmp.get("id") + "");
                map.put("text", tmp.get("title") + "");
                int activity = Integer.valueOf(tmp.get("id") + "").intValue();
                if (id == activity)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            logger.error(e.getMessage(), e);
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("code", "0");
            mapAll.put("text", "暂无数据");
            codeList.add(mapAll);
        }
        return JSON.toJSONString(codeList);
    }
}
