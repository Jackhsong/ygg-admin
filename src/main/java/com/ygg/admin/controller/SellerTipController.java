package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.SellerTipService;

@Controller
@RequestMapping("sellerTip")
public class SellerTipController
{
    
    @Resource
    private SellerTipService sellerTipService;
    
    private Logger logger = Logger.getLogger(SellerTipController.class);
    
    private static final String regEx = "[\\u4e00-\\u9fa5]";
    
    private boolean checkMsg(String msg)
    {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(msg);
        return m.find();
    }
    
    @RequestMapping("toList")
    public ModelAndView toList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sellerTip/list");
        return mv;
    }
    
    @RequestMapping("findListInfo")
    @ResponseBody
    public Object findListInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
        throws Exception
    {
        page = page == 0 ? 1 : page;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        return sellerTipService.findListInfo(param);
    }
    
    @RequestMapping("saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "商家后台公告管理-新增修改公告")
    public Object saveOrUpObject(String id, String title, String content, String status, String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", id == "" ? null : id);
            param.put("title", title);
            param.put("content", content);
            param.put("status", status);
            param.put("remark", remark);
            resultMap.put("status", 1);
            resultMap.put("data", sellerTipService.saveOrUpdate(param));
            return resultMap;
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            logger.error(msg, ex);
            resultMap.put("status", 0);
            if (checkMsg(msg))
            {
                resultMap.put("msg", msg);
            }
            else
            {
                resultMap.put("msg", "服务器发生异常，请刷新后重试.");
            }
            return resultMap;
        }
    }
    
    @RequestMapping("/findById/{id}")
    @ResponseBody
    public Object findById(@PathVariable int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", 1);
        resultMap.put("data", sellerTipService.findById(id));
        return resultMap;
    }
}
