package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.CustomLayoutStyleTypeEnum;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.entity.SpecialActivityEntity;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.SpecialActivityService;
import com.ygg.admin.util.CgiUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 特卖活动相关
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/special")
public class SpecialActivityController
{
    private static Logger logger = Logger.getLogger(SpecialActivityController.class);
    
    @Resource
    private SpecialActivityService specialActivityService;
    
    @Resource
    private ProductService productService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("specialActivity/specialActivityList");
        return mv;
    }
    
    /**
     * 
     * @param page
     * @param rows
     * @param title：活动标题
     * @param desc：活动描述
     * @param isAvailable：是否可用，1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/jsonSpecialActivityInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpecialActivityInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "title", required = false, defaultValue = "") String title,
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (!"".equals(desc))
            {
                para.put("desc", "%" + desc + "%");
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = specialActivityService.jsonSpecialActivityInfo(para);
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
     * 保存/更新特卖活动
     * @param id
     * @param title
     * @param remark
     * @param isDisplay
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateSpecialActivity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-新增/编辑特卖活动")
    public String saveOrUpdateSpecialActivity(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "title", required = false, defaultValue = "") String title, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "image", required = false, defaultValue = "") String image, @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            SpecialActivityEntity sae = new SpecialActivityEntity();
            sae.setId(id);
            sae.setTitle(title);
            sae.setDesc(desc);
            sae.setImage(image);
            sae.setIsAvailable(isAvailable);
            int status = specialActivityService.saveOrUpdateSpecialActivity(sae);
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
     * 更新特卖活动可用状态
     * @param id
     * @param code：1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/updateSpecialActivityAvailableStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-设置特卖活动可用状态")
    public String updateSpecialActivityAvailableStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("code", code);
            para.put("idList", Arrays.asList(id.split(",")));
            int status = specialActivityService.updateSpecialActivityAvailableStatus(para);
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
     * 特卖活动板块View
     * @param activityId：特卖活动Id
     * @return
     */
    @RequestMapping("/manageLayout/{activityId}")
    public ModelAndView manageLayout(@PathVariable("activityId") int activityId)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("specialActivity/specialActivityLayoutList");
            
            Map<String, Object> resultMap = specialActivityService.findSpecialActivityById(activityId);
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
     * 特卖活动板块列表
     * @param page
     * @param rows
     * @param activityId：特卖活动Id
     * @return
     */
    @RequestMapping(value = "/jsonActivityLayoutInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonActivityLayoutInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
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
            resultMap = specialActivityService.jsonActivityLayoutInfo(para);
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
     * 新增/修改特卖活动板块
     * @param id
     * @param title
     * @param desc
     * @param image
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateSpecialActivityLayout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-新增/修改特卖活动板块")
    public String saveOrUpdateSpecialActivityLayout(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "saId", required = false, defaultValue = "0") int saId, @RequestParam(value = "shortTitle", required = false, defaultValue = "") String shortTitle,
        @RequestParam(value = "longTitle", required = false, defaultValue = "") String longTitle, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("saId", saId);
            para.put("shortTitle", shortTitle);
            para.put("longTitle", longTitle);
            para.put("desc", desc);
            para.put("isDisplay", isDisplay);
            int status = specialActivityService.saveOrUpdateSpecialActivityLayout(para);
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
     * 修改特卖活动板块排序排序值
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateSpecialActivityLayoutSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-修改特卖活动板块排序值")
    public String updateSpecialActivityLayoutSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = specialActivityService.updateSpecialActivityLayoutSequence(para);
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
     * 修改特卖活动版块展现状态
     * @param layoutId
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateSpecialActivityLayoutDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-修改特卖活动版块展现状态")
    public String updateSpecialActivityLayoutDisplayStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("code", code);
            para.put("idList", Arrays.asList(id.split(",")));
            int status = specialActivityService.updateSpecialActivityLayoutDisplayStatus(para);
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
    
    @RequestMapping("/manageProduct/{id}")
    public ModelAndView manageProduct(@PathVariable("id") int layoutId)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("specialActivity/specialActivityLayoutProduct");
            
            Map<String, Object> resultMap = specialActivityService.findSpecialActivityLayout(layoutId);
            if (resultMap == null || resultMap.size() == 0)
            {
                mv.setViewName("error/404");
            }
            mv.addObject("layout", resultMap);
            mv.addObject("layoutId", layoutId + "");
            mv.addObject("saId", resultMap.get("saId") + "");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 板块商品布局
     * @param page
     * @param rows
     * @param layoutId
     * @return
     */
    @RequestMapping(value = "/jsonSpecialActivityLayoutProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpecialActivityLayoutProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "layoutId", required = true) int layoutId)
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
            para.put("layoutId", layoutId);
            resultMap = specialActivityService.jsonSpecialActivityLayoutProductInfo(para);
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
     * 修改布局排序
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateSpecialActivityLayoutProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-修改特卖活动布局商品排序值")
    public String updateSpecialActivityLayoutProductSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = specialActivityService.updateSpecialActivityLayoutProductSequence(para);
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
     * 
     * @param id
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateSpecialActivityLayoutProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-修改特卖活动布局商品展现状态")
    public String updateSpecialActivityLayoutProductDisplayStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isDisplay", code);
            para.put("id", id);
            int status = specialActivityService.updateSpecialActivityLayoutProductDisplayStatus(para);
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
    @RequestMapping("/toAdd/{layoutId}")
    public ModelAndView toAdd(@PathVariable(value = "layoutId") int layoutId)
    {
        ModelAndView mv = new ModelAndView("specialActivity/update");
        mv.addObject("layoutId", layoutId + "");
        mv.addObject("layoutProduct", new HashMap<String, Object>());
        return mv;
    }
    
    /**
     * 转发到修页面
     * @param regionId
     * @param layoutId
     * @return
     */
    @RequestMapping("/toEdit/{layoutId}/{layoutProductId}")
    public ModelAndView toEdit(@PathVariable(value = "layoutId") int layoutId, @PathVariable(value = "layoutProductId") int layoutProductId)
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            para.put("id", layoutProductId);
            para.put("layoutId", layoutId);
            Map<String, Object> layoutProduct = specialActivityService.findSpecialActivityLayoutProduct(layoutProductId);
            if (layoutProduct == null)
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.setViewName("specialActivity/update");
            mv.addObject("layoutProduct", layoutProduct);
            mv.addObject("layoutId", layoutId + "");
            mv.addObject("oneDisplayId", layoutProduct.get("oneDisplayId"));
            mv.addObject("twoDisplayId", layoutProduct.get("twoDisplayId"));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 新增/修改商品布局
     * @param layoutId：板块布局Id
     * @param id：商品布局Id
     * @param displayType：展现类型，1单张，2两张
     * @param oneDesc：左侧描述
     * @param oneImage：左侧图片
     * @param oneType：左侧关联类型，1商品，2组合
     * @param oneProductId：左侧商品Id
     * @param oneGroupSale：左侧组合Id
     * @param twoDesc：右侧描述
     * @param twoImage：右侧图片
     * @param twoType：右侧关联类型，1商品，2组合
     * @param twoProductId：右侧商品Id
     * @param twoGroupSale：右侧组合Id
     * @param isDisplay：是否展现，1展现，0不展现
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateSpecialActivityLayoutProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-修改特卖活动布局商品")
    public String saveOrUpdateSpecialActivityLayoutProduct(
        @RequestParam(value = "layoutId", required = true) int layoutId,
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "displayType", required = false, defaultValue = "1") int displayType,//
        @RequestParam(value = "oneDesc", required = false, defaultValue = "") String oneDesc,//
        @RequestParam(value = "oneImage", required = false, defaultValue = "") String oneImage, //
        @RequestParam(value = "oneType", required = false, defaultValue = "1") int oneType,//
        @RequestParam(value = "oneProductId", required = false, defaultValue = "0") int oneProductId,//
        @RequestParam(value = "oneGroupSale", required = false, defaultValue = "0") int oneGroupSale,//
        @RequestParam(value = "twoDesc", required = false, defaultValue = "") String twoDesc,//
        @RequestParam(value = "twoImage", required = false, defaultValue = "") String twoImage, //
        @RequestParam(value = "twoType", required = false, defaultValue = "1") int twoType,//
        @RequestParam(value = "twoProductId", required = false, defaultValue = "0") int twoProductId,//
        @RequestParam(value = "twoGroupSale", required = false, defaultValue = "0") int twoGroupSale,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("layoutId", layoutId);
            para.put("id", id);
            para.put("displayType", displayType);
            para.put("isDisplay", isDisplay);
            para.put("oneDesc", oneDesc);
            para.put("oneImage", oneImage);
            para.put("oneType", oneType);
            if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
            {
                ProductEntity product = productService.findProductById(oneProductId);
                if (product == null)
                {
                    result.put("status", 0);
                    result.put("msg", "Id=" + oneProductId + "的商品不存在");
                    return JSON.toJSONString(result);
                }
                para.put("oneDisplayId", oneProductId);
            }
            else if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
            {
                para.put("oneDisplayId", oneGroupSale);
            }
            if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())
            {
                if (twoProductId == 0)
                {
                    para.put("twoDesc", "");
                    para.put("twoImage", "");
                    para.put("twoType", 0);
                    para.put("twoDisplayId", 0);
                }
                else
                {
                    para.put("twoDesc", twoDesc);
                    para.put("twoImage", twoImage);
                    para.put("twoType", twoType);
                    if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
                    {
                        ProductEntity product = productService.findProductById(twoProductId);
                        if (product == null)
                        {
                            result.put("status", 0);
                            result.put("msg", "Id=" + twoProductId + "的商品不存在");
                            return JSON.toJSONString(result);
                        }
                        para.put("twoDisplayId", twoProductId);
                    }
                    else if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
                    {
                        para.put("twoDisplayId", twoGroupSale);
                    }
                }
            }
            else
            {
                para.put("twoDesc", "");
                para.put("twoImage", "");
                para.put("twoType", 0);
                para.put("twoDisplayId", 0);
                
            }
            int status = specialActivityService.saveOrUpdateSpecialActivityLayoutProduct(para);
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
    
    @RequestMapping(value = "/jsonSpecialActivityCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpecialActivityCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> resultList = specialActivityService.findSpecialActivity();
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
            logger.error(e.getMessage(), e);
            logger.error(e.getMessage(), e);
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("code", "0");
            mapAll.put("text", "暂无数据");
            codeList.add(mapAll);
        }
        return JSON.toJSONString(codeList);
    }

//    @RequestMapping("/quickAddLayoutProduct")
//    @ResponseBody
//    @ControllerLog(description = "特卖活动管理-情景特卖快速添加左右布局的商品")
//    public ResultEntity quickAddLayoutProduct(
//            @RequestParam(value = "layoutId", required = true) int layoutId,
//            HttpServletRequest req
//    ){
//        try {
//            List<Integer> idsList = CgiUtil.getSplitToIntegerList(req,"ids","",",");
//            List<List<Integer>> idsPartition = Lists.partition(idsList, 2);
//            specialActivityService.saveByQuickAdd(idsPartition, layoutId);
//            return ResultEntity.getSuccessResult();
//        } catch (Exception e) {
//            logger.error("情景特卖快速添加左右布局的商品失败", e);
//            return ResultEntity.getFailResult(e.getMessage());
//        }
//    }

}
