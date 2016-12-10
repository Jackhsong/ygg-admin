package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.ygg.admin.service.ChannelService;

/**
 * 第三方渠道管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/channel")
public class ChannelController
{
    private static Logger logger = Logger.getLogger(ChannelController.class);
    
    @Resource
    private ChannelService channelService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("channel/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonChannelInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonChannelInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "name", required = false, defaultValue = "") String name)
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
            if (id != 0)
            {
                para.put("id", id);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            return channelService.jsonChannelInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载渠道列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道管理-编辑渠道")
    public String saveOrUpdate(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "name", required = false, defaultValue = "") String name)
    {
        try
        {
            if (id == 0)
            {
                return channelService.saveChannel(name);
            }
            else
            {
                return channelService.updateChannel(id, name);
            }
        }
        catch (Exception e)
        {
            logger.error("编辑渠道出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/deleteChannel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道管理-删除渠道")
    public String deleteChannel(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        try
        {
            return channelService.deleteChannel(id);
        }
        catch (Exception e)
        {
            logger.error("删除渠道出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/jsonChannelCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonChannelCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> reList = channelService.findAllChannelByPara(para);
            for (Map<String, Object> it : reList)
            {
                Map<String, String> mp = new HashMap<String, String>();
                mp.put("code", it.get("id").toString());
                mp.put("text", it.get("channelName").toString());
                if (id == Integer.parseInt(it.get("id") + ""))
                {
                    mp.put("selected", "true");
                }
                resultList.add(mp);
            }
        }
        catch (Exception e)
        {
            logger.error("删除渠道出错", e);
        }
        return JSON.toJSONString(resultList);
    }
}
