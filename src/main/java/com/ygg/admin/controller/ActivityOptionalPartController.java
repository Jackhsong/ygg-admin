package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ActivityEnum;
import com.ygg.admin.entity.ActivitiesOptionalPartEntity;
import com.ygg.admin.service.ActivityOptionalPartService;

/**
 * N元任选活动
 */

@Controller
@RequestMapping("/activityOptionalPart")
public class ActivityOptionalPartController
{
    private Logger log = Logger.getLogger(ActivityOptionalPartController.class);
    
    @Resource
    private ActivityOptionalPartService activityOptionalPartService;
    
    /**
     * N元任选活动列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("activityOptionalPart/list");
        mv.addObject("type", "1");
        mv.addObject("status", "1,2");
        return mv;
    }
    
    @RequestMapping(value = "/jsonActivityOptionalPartInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonActivityOptionalPartInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "status", required = false, defaultValue = "") String status)
    {
        try
        {
            Map<String, Object> result = activityOptionalPartService.findAllActivityOptionalPart(status, page, rows);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> result = new HashMap<>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<>());
            return JSON.toJSONString(result);
        }
    }
    
    @ControllerLog(description = "促销管理-新增或修改N元任选活动")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdate(ActivitiesOptionalPartEntity activity, HttpServletRequest request)
    {
        try
        {
            String groupSaleId = StringUtils.isEmpty(request.getParameter("groupSaleId")) ? "0" : request.getParameter("groupSaleId");
            String customSaleId = StringUtils.isEmpty(request.getParameter("customSaleId")) ? "0" : request.getParameter("customSaleId");
            if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.GROUP.getCode())
            {
                activity.setTypeId(Integer.parseInt(groupSaleId));
            }
            else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.CUSTOM_ACTIVITY.getCode())
            {
                activity.setTypeId(Integer.parseInt(customSaleId));
            }
            if (activity.getId() == 0)
            {
                return JSON.toJSONString(activityOptionalPartService.saveActivityOptionalPart(activity));
            }
            else
            {
                return JSON.toJSONString(activityOptionalPartService.updateActivityOptionalPart(activity));
            }
        }
        catch (Exception e)
        {
            log.error("编辑N元任选活动出错", e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @ControllerLog(description = "促销管理-修改N元任选活动显示状态")
    @RequestMapping(value = "/updateDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDisplayStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int isAvailable)
    {
        
        try
        {
            return JSON.toJSONString(activityOptionalPartService.updateDisplayStatus(id, isAvailable));
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "操作失败");
            log.error(e.getMessage(), e);
            return JSON.toJSONString(result);
        }
    }
    
}
