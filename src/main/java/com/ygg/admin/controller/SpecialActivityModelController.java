package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.dao.SpecialActivityModelLayoutProductDao;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SpecialActivityModelLayoutProductService;
import com.ygg.admin.service.SpecialActivityModelLayoutService;
import com.ygg.admin.service.SpecialActivityModelService;
import com.ygg.admin.util.CgiUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 情景模版
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("specialActivityModel")
public class SpecialActivityModelController
{
    
    Logger logger = Logger.getLogger(SpecialActivityModelController.class);
    
    @Resource
    private SpecialActivityModelService specialActivityModelService;
    
    @Resource
    private SpecialActivityModelLayoutService specialActivityModelLayoutService;

    @Resource
    private SpecialActivityModelLayoutProductService specialActivityModelLayoutProductService;

    @Resource
    private ProductService productService;

    @Resource
    private SpecialActivityModelLayoutProductDao specialActivityModelLayoutProductDao;
    
    /**
     * 跳情景模版列表
     * 
     * @return
     */
    @RequestMapping("toList")
    public ModelAndView toList()
    {
        ModelAndView mv = new ModelAndView("specialActivityModel/list");
        return mv;
    }
    
    /**
     * 跳情景模版板块列表
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("toLayoutList/{id}")
    public ModelAndView toListLayout(@PathVariable String id)
        throws Exception
    {
        if (StringUtils.isBlank(id))
            throw new RuntimeException("出错了，请联开发人员");
        ModelAndView mv = new ModelAndView("specialActivityModel/layoutList");
        mv.addObject("activity", specialActivityModelService.findById(id));
        return mv;
    }
    
    /**
     * 跳情景模版商品列表
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("toLayoutProductList/{id}")
    public ModelAndView toLayoutProductList(@PathVariable String id)
        throws Exception
    {
        if (StringUtils.isBlank(id))
            throw new RuntimeException("出错了，请联开发人员");
        ModelAndView mv = new ModelAndView("specialActivityModel/layoutProductList");
        mv.addObject("layout", specialActivityModelLayoutService.findById(id));
        mv.addObject("layoutId", id);
        return mv;
    }
    
    /**
     * 情景模版列表
     * 
     * @param title
     * @param isAvailable
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(String title, String isAvailable, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("title", title);
            param.put("isAvailable", StringUtils.equals(isAvailable, "-1") ? null : isAvailable);
            param.put("start", (page - 1) * rows);
            param.put("size", rows);
            return specialActivityModelService.findListByParam(param);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 新增或更新情景模版
     * 
     * @param title
     * @param id
     * @param image
     * @param isAvailable
     * @return
     */
    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public Object saveOrUpdate(String title, String id, String image, String isAvailable)
    {
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("title", title);
            param.put("id", id);
            param.put("image", image);
            param.put("isAvailable", isAvailable);
            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", specialActivityModelService.saveOrUpdate(param));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 情景模版板块列表
     * 
     * @param activityId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("layoutList")
    @ResponseBody
    public Object layoutList(String activityId, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay)
    {
        try
        {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("start", (page - 1) * rows);
            param.put("size", rows);
            param.put("activityId", activityId);
            if(isDisplay != -1) {
                param.put("isDisplay", isDisplay);
            }
            return specialActivityModelLayoutService.findListByParam(param);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 新增或更新情景模版板块
     * 
     * @param title
     * @param id
     * @param activityId
     * @param image
     * @param isDisplay
     * @param sequence
     * @return
     */
    @RequestMapping("saveOrUpdateLayout")
    @ResponseBody
    public Object saveOrUpdateLayout(String title, String id, String activityId, String image, String isDisplay, String sequence)
    {
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("title", title);
            param.put("id", id);
            param.put("image", image);
            param.put("isDisplay", isDisplay);
            param.put("activityId", activityId);
            param.put("sequence", sequence);
            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", specialActivityModelLayoutService.saveOrUpdate(param));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 情景模版商品列表
     * 
     * @param activityId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("layoutProductList")
    @ResponseBody
    public Object layoutProductList(String layoutId, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay
    )
    {
        try
        {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("start", (page - 1) * rows);
            param.put("size", rows);
            param.put("layoutId", layoutId);
            if(isDisplay != -1) {
                param.put("isDisplay", isDisplay);
            }
            return specialActivityModelLayoutProductService.findListByParam(param);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 新增或更新情景模板商品
     * 
     * @param title
     * @param id
     * @param activityId
     * @param image
     * @param isDisplay
     * @param sequence
     * @return
     */
    @RequestMapping("saveOrUpdateLayoutProduct")
    @ResponseBody
    public Object saveOrUpdateLayoutProduct(String layoutId, String id, String productId, String desc, String isDisplay, String sequence)
    {
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("layoutId", layoutId);
            param.put("id", id);
            param.put("desc", desc);
            param.put("productId", productId);
            param.put("isDisplay", isDisplay);
            param.put("sequence", sequence);

            if(StringUtils.isNotEmpty(productId)){
                ProductEntity product = productService.findProductById(Integer.valueOf(productId));
                Preconditions.checkNotNull(product, "id：" + productId + "商品不存在！");
                Map<String, Object> qPara = new HashMap<>();
                qPara.put("layoutId", layoutId);
                qPara.put("productId", productId);
                if(StringUtils.isEmpty(id)) {
                    List<Map<String, Object>> layoutProducts = specialActivityModelLayoutProductDao.findListByParam(qPara);
                    Preconditions.checkArgument(CollectionUtils.isEmpty(layoutProducts), "id为" + productId + "的商品已经存在于布局");
                }
            }

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", specialActivityModelLayoutProductService.saveOrUpdate(param));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    @RequestMapping(value = "/jsonSpecialActivityModelCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpecialActivityModelCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> list = specialActivityModelService.findAllSpecialActivityModel(para);
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

    @RequestMapping("/quickAddLayoutProduct")
    @ResponseBody
    @ControllerLog(description = "情景模版板块管理-快速添加商品")
    public ResultEntity quickAddLayoutProduct(
            @RequestParam(value = "layoutId", required = true) int layoutId,
            HttpServletRequest req
    ){
        try {
            List<Integer> idsList = CgiUtil.getSplitToIntegerList(req, "ids", "", ",");
            Set<Integer> idsSet = Sets.newLinkedHashSet(idsList);
            specialActivityModelLayoutProductService.saveByQuickAdd(idsSet, layoutId);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            logger.error("情景特卖快速添加左右布局的商品失败", e);
            return ResultEntity.getFailResult(e.getMessage());
        }
    }
}
