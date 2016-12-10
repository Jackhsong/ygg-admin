package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.entity.CustomFunctionEntity;
import com.ygg.admin.service.CustomFunctionService;
import com.ygg.admin.util.CommonUtil;

/**
 * 
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/customFunction")
public class CustomFunctionController
{
    private static Logger logger = Logger.getLogger(CustomFunctionController.class);
    
    @Resource
    private CustomFunctionService customFunctionService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("customFunction/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonCustomFunctionInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCustomFunctionInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,//
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            Map<String, Object> result = customFunctionService.jsonCustomFunctionInfo(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            logger.error("异步加载自定定义功能列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 编辑
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdate(HttpServletRequest request)
    {
        try
        {
            int status = 0;
            CustomFunctionEntity function = new CustomFunctionEntity();
            CommonUtil.wrapParamter2Entity(function, request);
            if (function.getId() == 0)
            {
                status = customFunctionService.saveCustomFunction(function);
            }
            else
            {
                status = customFunctionService.updateCustomFunction(function);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", status > 0 ? 1 : 0);
            resultMap.put("msg", "保存" + (status > 0 ? "成功" : "失败"));
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            logger.error("编辑首页自定义功能出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 更新自定义板块是否可用
     * @param id
     * @param code：1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/updateDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDisplayStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isDisplay", code);
            List<Integer> idList = new ArrayList<Integer>();
            String[] idStrArr = id.split(",");
            for (String idStr : idStrArr)
            {
                idList.add(Integer.valueOf(idStr.trim()).intValue());
            }
            para.put("idList", idList);
            int status = customFunctionService.updateDisplayStatus(para);
            if (status > 0)
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
}
