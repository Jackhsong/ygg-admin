package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.CustomNavigationService;

@Controller
@RequestMapping("/customNavigation")
public class CustomNavigationController
{
    
    Logger logger = Logger.getLogger(CustomNavigationController.class);
    
    @Resource
    private CustomNavigationService customNavigationService;
    
    /**
     * 首页导航入口
     * 
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("customNavigation/list");
        return mv;
    }
    
    /**
     * 查询自定义首页导航列表
     * 
     * @param id
     *            导航ID
     * @param name
     *            导航名称
     * @param isDisplay
     *            是否展示
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public Object info(@RequestParam(value = "id", defaultValue = "0", required = false) int id, @RequestParam(value = "name", defaultValue = "", required = false) String name,
        @RequestParam(value = "isDisplay", defaultValue = "1", required = false) int isDisplay, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return customNavigationService.findNavigationList(id, name, isDisplay, page, rows);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询单条
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @ResponseBody
    public Object findById(int id)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", customNavigationService.findById(id));
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 根据条件更新导航
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping("/updateByParam")
    @ResponseBody
    public Object updateByParam(@RequestParam(value = "id", defaultValue = "0", required = true) int id,
        @RequestParam(value = "sequence", defaultValue = "-1", required = false) int sequence,
        @RequestParam(value = "isDisplay", defaultValue = "-1", required = false) int isDisplay)
    {
        try
        {
            customNavigationService.updateByParam(id, sequence, isDisplay);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 保存或修改首页导航信息
     * 
     * @param customNavigationId
     * @param name
     * @param remark
     * @param customNavigationType
     * @param activitiesCommonId
     * @param activitiesCustomId
     * @param pageId
     * @param isDisplay
     * @return
     */
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "保存或修改首页导航信息")
    public Object saveOrUpdate(int customNavigationId, String name, String remark, int customNavigationType,
        @RequestParam(defaultValue = "0", required = false) int activitiesCommonId, @RequestParam(defaultValue = "0", required = false) int activitiesCustomId,
        @RequestParam(defaultValue = "0", required = false) int pageId, int isDisplay)
    {
        try
        {
            int displayId = 0;
            if (customNavigationType == 1)
            {
                displayId = activitiesCommonId;
            }
            else if (customNavigationType == 2)
            {
                displayId = activitiesCustomId;
            }
            else if (customNavigationType == 3)
            {
                displayId = pageId;
            }
            if (customNavigationId < 1)
            {
                customNavigationService.save(name, remark, customNavigationType, displayId, isDisplay);
            }
            else
            {
                customNavigationService.update(customNavigationId, name, remark, customNavigationType, displayId, isDisplay);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 修改限制地区
     *
     * @param id 首页导航ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editAreaForm", produces = "application/json;charset=UTF-8")
    public ModelAndView editAreaForm(@RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("customNavigation/editAreaForm");
        Map<String, Object> result = customNavigationService.findNavigationSupportAreaInfo(id);
        mv.addObject("id", id + "");
        mv.addObject("supportAreaType", Integer.valueOf(result.get("supportAreaType") + ""));
        mv.addObject("pList", (List<Map<String, Object>>)result.get("pList"));
        return mv;
    }
    
    @RequestMapping(value = "/editArea", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String editArea(@RequestParam(value = "provinceStr", required = true) String provinceStr, //
        @RequestParam(value = "supportAreaType", required = true) int supportAreaType, //
        @RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        try
        {
            List<Integer> provinceIdList = new ArrayList<>();
            if (provinceStr.indexOf(",") > 0)
            {
                String[] arr = provinceStr.split(",");
                for (String cur : arr)
                {
                    provinceIdList.add(Integer.valueOf(cur));
                }
            }
            int status = customNavigationService.updateNavAreaInfo(id, provinceIdList, supportAreaType);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status > 0 ? "1" : 0);
            result.put("msg", status > 0 ? "保存成功" : "保存失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            logger.error("修改导航展示地区失败", e);
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
}
