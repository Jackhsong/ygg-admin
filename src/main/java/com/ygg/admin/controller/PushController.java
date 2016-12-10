package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.service.PushService;

/**
 * 推送管理
 * 
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("push")
public class PushController
{
    
    @Resource
    private PushService pushService;
    
    @RequestMapping("toCreatePush")
    public ModelAndView toCreatePush() {
        ModelAndView mv = new ModelAndView("crm/createPush");
        return mv;
    }
    
    
    /**
     * 给APP推送信息
     * 
     * @param param
     * @return
     */
    @RequestMapping("submitMessage")
    @ResponseBody
    public Object push(Map<String, String> param, String[] platfrom) {
        try
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("data", pushService.push(param, platfrom));
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
}
