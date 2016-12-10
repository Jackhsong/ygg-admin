package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.service.IndexSettingService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cache")
public class CacheController
{
    Logger log = Logger.getLogger(CacheController.class);

    @Resource
    private IndexSettingService indexSettingService;

    /**
     * 缓存列表
     * 
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("cache/list");
        return mv;
    }
    

    /**
     * 清楚搜索缓存
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/clearSearchCache", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String clearSearchCache()
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            int status = indexSettingService.updateSearchIndex();
            result.put("status",status);
            result.put("msg",status == 1 ? "成功" : "失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("清除缓存失败！",e);
            result.put("status", 0);
            result.put("msg", "失败");
            return JSON.toJSONString(result);
        }
    }
    
}
