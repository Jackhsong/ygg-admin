package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.ygg.admin.service.PromotionActivityService;

/**
 * 促销活动管理后台
 */
@Controller
@RequestMapping("/promotion")
public class PromotionActivityController
{
    private static Logger log = Logger.getLogger(PromotionActivityController.class);
    
    @Resource
    private PromotionActivityService promotionActivityService;
    
    @Resource
    private ProductService productService;
    
    /**
     * 新情景专场
     * 
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("promotion/list");
        return mv;
    }
    
    /**
     * 异步加载新情景专场信息
     * 
     * @param page
     * @param rows
     * @param title
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonPromotionInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPromotionInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "title", required = false, defaultValue = "") String title, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
        throws Exception
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
        Map<String, Object> result = promotionActivityService.findSpecialActivityNewByPara(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 异步保存活动信息
     * 
     * @param id
     * @param title
     * @param image
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdatePromotion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "新精品活动管理-新增/修改精品活动")
    public String saveOrUpdateActivitySimplify(@RequestParam(value = "editId", required = false, defaultValue = "0") int id,
        @RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "image", required = false, defaultValue = "") String image,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            para.put("title", title);
            para.put("image", image);
            para.put("isAvailable", isAvailable);
            int status = 0;
            if (id == 0)
            {
                status = promotionActivityService.saveSpecialActivityNew(para);
            }
            else
            {
                status = promotionActivityService.updateSpecialActivityNew(para);
            }
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
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 管理楼层商品
     * 
     * @return
     */
    @RequestMapping("/floorProduct/{specialActivityNewId}")
    public ModelAndView floorProduct(@PathVariable("specialActivityNewId") int specialActivityNewId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("promotion/floorProduct");
        mv.addObject("activityId", specialActivityNewId + "");
        Map<String, Object> info = promotionActivityService.findSpecialActivityNewById(specialActivityNewId);
        if (info != null)
        {
            mv.addObject("activity", info);
        }
        return mv;
    }
    
    @RequestMapping(value = "/updatePromotionActivityProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "新情景专场-修改情景专场商品排序值")
    public String updatePromotionActivityProductSequence(@RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "sequence", required = true) int sequence//
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = promotionActivityService.updateSpecialActivityNewProduct(para);
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
    
    @RequestMapping(value = "/jsonFloorProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonFloorProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, //
        @RequestParam(value = "specialActivityNewId", required = true) int specialActivityNewId //
    )
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
        para.put("specialActivityNewId", specialActivityNewId);
        Map<String, Object> result = promotionActivityService.findSpecialActivityNewProductByPara(para);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/saveOrUpdatePromotionActivityProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "新情景专场-新增/修改情景专场商品")
    public String saveOrUpdatePromotionActivityProduct(@RequestParam(value = "editId", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "activityId", required = false, defaultValue = "0") int activityId, //
        @RequestParam(value = "title", required = false, defaultValue = "") String title, //
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "productId", required = false, defaultValue = "") int productId, //
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            ProductEntity pe = productService.findProductById(productId);
            if (pe == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "Id=" + productId + "的商品不存在");
                return JSON.toJSONString(resultMap);
            }
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            para.put("specialActivityNewId", activityId);
            para.put("title", title);
            para.put("keyword", keyword);
            para.put("type", type);
            para.put("desc", desc);
            para.put("productId", productId);
            para.put("isDisplay", isDisplay);
            int status = 0;
            if (id == 0)
            {
                status = promotionActivityService.saveSpecialActivityNewProduct(para);
            }
            else
            {
                status = promotionActivityService.updateSpecialActivityNewProduct(para);
            }
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
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 管理更多商品
     * 
     * @return
     */
    @RequestMapping("/moreProduct/{specialActivityNewId}")
    public ModelAndView moreProduct(@PathVariable("specialActivityNewId") int specialActivityNewId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("promotion/moreProduct");
        mv.addObject("activityId", specialActivityNewId + "");
        Map<String, Object> info = promotionActivityService.findSpecialActivityNewById(specialActivityNewId);
        if (info != null)
        {
            mv.addObject("activity", info);
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonPromotionActivityCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPromotionActivityCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> resultList = promotionActivityService.findSpecialActivityNewByPara();
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
                int lotteryId = Integer.valueOf(tmp.get("id") + "").intValue();
                if (id == lotteryId)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("code", "0");
            mapAll.put("text", "暂无数据");
            codeList.add(mapAll);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/batchAddPromotionActivityProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "新情景专场-批量添加情景专场商品")
    public String batchAddPromotionActivityProduct(//
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
            
            return promotionActivityService.batchAddPromotionActivityProduct(activityId, type, isDisplay, productIdList);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙，请稍后再试~~~");
        }
        return JSON.toJSONString(resultMap);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "新情景-删除商品")
    public String delete(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
            throws Exception
    {
        Map resultMap = new HashMap();
        int resultStatus = promotionActivityService.deleteSpecialActivityNewProductById(id);
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        return JSON.toJSONString(resultMap);
    }
}
