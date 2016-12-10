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

import com.alibaba.fastjson.JSON;
import com.ygg.admin.service.LotteryService;

@Controller
@RequestMapping("/lotteryActivity")
public class LotteryActivityController
{
    private Logger logger = Logger.getLogger(LotteryActivityController.class);
    
    @Resource
    private LotteryService lotteryService;
    
    //    
    //    /**
    //     * 抽奖活动列表
    //     * @param request
    //     * @return
    //     * @throws Exception
    //     */
    //    @RequestMapping("/list")
    //    public ModelAndView list(HttpServletRequest request)
    //        throws Exception
    //    {
    //        ModelAndView mv = new ModelAndView();
    //        mv.setViewName("lotteryActivity/list");
    //        return mv;
    //    }
    //    
    //    @RequestMapping(value = "/jsonDataForIndex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String jsonDataForIndex(HttpServletRequest request)
    //        throws Exception
    //    {
    //        System.out.println("jsonDataForIndex");
    //        return "";
    //    }
    //    
    //    @RequestMapping(value = "/saveOrUpdate", produces = "application/json;charset=UTF-8")
    //    @ResponseBody
    //    public String saveOrUpdate(HttpServletRequest request, @RequestParam(value = "number", required = true) String number,
    //        @RequestParam(value = "orderId", required = true) int id, @RequestParam(value = "mark", required = true) String mark,
    //        @RequestParam(value = "oldStatus", required = true) String oldStatus, @RequestParam(value = "status", required = true) int status)
    //        throws Exception
    //    {
    //        Map<String, Object> map = new HashMap<String, Object>();
    //        map.put("id", id);
    //        map.put("number", number);
    //        map.put("mark", mark);
    //        map.put("status", status);
    //        try
    //        {
    //            int resultStatus = orderService.updateOrderStatus(id, number, oldStatus, status, mark);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            if (resultStatus != 1)
    //            {
    //                result.put("status", 0);
    //                result.put("msg", "修改失败");
    //                return JSON.toJSONString(result);
    //            }
    //            result.put("status", 1);
    //            result.put("msg", "修改成功");
    //            
    //            return JSON.toJSONString(result);
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("修改失败", e);
    //            Map<String, Object> result = new HashMap<String, Object>();
    //            result.put("status", 0);
    //            result.put("msg", "修改失败");
    //            return JSON.toJSONString(result);
    //        }
    //        
    //    }
    
    @RequestMapping(value = "/jsonLotteryCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLotteryCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> resultList = lotteryService.findAllLottery(para);
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
                map.put("text", tmp.get("name") + "");
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
    
    @RequestMapping(value = "/jsonGiftActivityCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGiftActivityCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> resultList = lotteryService.findAllGiftActivity(para);
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
                map.put("text", tmp.get("name") + "");
                int giftActivity = Integer.valueOf(tmp.get("id") + "").intValue();
                if (id == giftActivity)
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
}
