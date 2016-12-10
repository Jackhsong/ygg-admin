package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.CommonEnum;

@Controller
@RequestMapping("/log")
public class SystemLogController
{
    @Resource
    private SystemLogService logService;
    
    @RequestMapping("/list")
    public ModelAndView list()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("common/systemLogList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonSystemLogInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "日志管理-访问日志列表")
    public String jsonSystemLogInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "username", required = false, defaultValue = "") String username,
        @RequestParam(value = "businessType", required = false, defaultValue = "0") int businessType,
        @RequestParam(value = "operationType", required = false, defaultValue = "0") int operationType,
        @RequestParam(value = "content", required = false, defaultValue = "") String content, @RequestParam(value = "level", required = false, defaultValue = "0") int level,
        @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(username))
        {
            para.put("username", "%" + username + "%");
        }
        if (businessType != 0)
        {
            para.put("businessType", businessType);
        }
        if (operationType != 0)
        {
            para.put("operationType", operationType);
        }
        if (!"".equals(content))
        {
            para.put("content", "%" + content + "%");
        }
        if (level != 0)
        {
            para.put("level", level);
        }
        if (!"".equals(createTimeBegin))
        {
            para.put("createTimeBegin", createTimeBegin);
        }
        if (!"".equals(createTimeEnd))
        {
            para.put("createTimeEnd", createTimeEnd);
        }
        Map<String, Object> map = logService.jsonSystemLogInfo(para);
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = "/jsonBusinessTypeCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBusinessTypeCode()
        throws Exception
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        for (CommonEnum.BusinessTypeEnum type : CommonEnum.BusinessTypeEnum.values())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", type.ordinal());
            map.put("text", type.ordinal() == 0 ? "全部" : type.getDescriptionByOrdinal(type.ordinal()));
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/jsonOperationTypeCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOperationTypeCode()
        throws Exception
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        for (CommonEnum.OperationTypeEnum type : CommonEnum.OperationTypeEnum.values())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", type.ordinal());
            map.put("text", type.ordinal() == 0 ? "全部" : type.getDescriptionByOrdinal(type.ordinal()));
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/jsonLevelCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLevelCode()
        throws Exception
    {
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        for (CommonEnum.LogLevelEnum type : CommonEnum.LogLevelEnum.values())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", type.ordinal());
            map.put("text", type.ordinal() == 0 ? "全部" : type.getDescriptionByOrdinal(type.ordinal()));
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping("/logList")
    public ModelAndView systemLogList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("common/logList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonLogInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLogInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "username", required = false, defaultValue = "") String username,//
        @RequestParam(value = "operation", required = false, defaultValue = "") String operation,//
        @RequestParam(value = "content", required = false, defaultValue = "") String content,//
        @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,//
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(username))
        {
            para.put("username", "%" + username + "%");
        }
        if (!"".equals(operation))
        {
            para.put("operation", "%" + operation + "%");
        }
        if (!"".equals(content))
        {
            para.put("content", "%" + content + "%");
        }
        if (!"".equals(createTimeBegin))
        {
            para.put("createTimeBegin", createTimeBegin);
        }
        if (!"".equals(createTimeEnd))
        {
            para.put("createTimeEnd", createTimeEnd);
        }
        Map<String, Object> map = logService.jsonLogInfo(para);
        return JSON.toJSONString(map);
    }
}
