package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.CustomerProblemService;

@Controller
@RequestMapping("/customerProblem")
public class CustomerProblemController
{
    private static Logger logger = Logger.getLogger(ChannelController.class);
    
    @Resource
    private CustomerProblemService customerProblemService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        return new ModelAndView("customerProblem/list");
    }
    
    @RequestMapping(value = "/jsonCustomerProblemInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCustomerProblemInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
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
            return customerProblemService.jsonCustomerProblemInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载客服问题列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "客服问题管理-编辑客服问题")
    public String saveOrUpdate(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "question", required = false, defaultValue = "") String question,// 
        @RequestParam(value = "answer", required = false, defaultValue = "") String answer,//
        @RequestParam(value = "sequence", required = false, defaultValue = "0") int sequence, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        try
        {
            if (id == 0)
            {
                return customerProblemService.saveCustomerProblem(question, answer, sequence, isDisplay);
            }
            else
            {
                return customerProblemService.updateCustomerProblem(id, question, answer, sequence, isDisplay);
            }
        }
        catch (Exception e)
        {
            logger.error("编辑客服问题", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateCustomerProblemStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "客服问题管理-修改展现状态")
    public String updateCustomerProblemStatus(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay)
    {
        try
        {
            return customerProblemService.updateCustomerProblemStatus(id, isDisplay);
        }
        catch (Exception e)
        {
            logger.error("修改客服问题展现状态出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
}
