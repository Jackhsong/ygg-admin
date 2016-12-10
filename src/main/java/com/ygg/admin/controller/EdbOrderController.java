package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/edbOrder")
public class EdbOrderController
{
    Logger log = Logger.getLogger(EdbOrderController.class);

    @Resource(name = "orderService")
    private OrderService orderService;

    /**
     * E店宝未确认订单列表
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/listUnconfirmed")
    public ModelAndView listUnconfirmed()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("edbOrder/listUnconfirmed");
        return mv;
    }
    
    /**
     * 异步获取未确认E店宝订单信息
     */
    @RequestMapping(value = "/jsonUnconfirmedEdbOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonUnconfirmedEdbOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, //
        @RequestParam(value = "number", required = false, defaultValue = "") String number, // 订单编号
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId, // 商家ID
        @RequestParam(value = "freezeStatus", required = false, defaultValue = "-1") int freezeStatus
    )
        throws Exception
    {
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(number))
            {
                para.put("number", number);
            }
            if (sellerId != 0)
            {
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                para.put("sellerIdList", sellerIdList);
            }
            if (!"".equals(startTime))
            {
                para.put("startTimeBegin", startTime);
            }
            else
            {
                para.put("startTimeBegin", "2016-01-07 10:00:00");
            }
            if (!"".equals(endTime))
            {
                para.put("startTimeEnd", endTime);
            }
            if (freezeStatus != -1)
            {
                if (freezeStatus == 0)
                {
                    para.put("unFreeze", 1);
                }
                else
                {
                    para.put("freeze", 1);
                }
            }
            return JSON.toJSONString(orderService.findEdbOrderUnconfirmed(para));
        }
        catch (Exception e)
        {
            log.error("异步获取未确认E店宝订单信息失败!", e);
            Map<String,Object> result = new HashMap<>();
            result.put("rows", new ArrayList<>());
            result.put("total", 0);
            return JSON.toJSONString(result);
        }
    }

    /**
     * 单个 or 批量 确认E店宝订单
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "E店宝订单管理-确认E店宝订单")
    public String confirm(@RequestParam(value = "ids", required = true) String ids,
        @RequestParam(value = "isPush", required = true) int isPush // 推送状态 1:推送；0:不推送
    )
        throws Exception
    {
        try
        {
            List<Integer> idList = new ArrayList<>();
            if (ids.indexOf(",") > 0)
            {
                String[] arr = ids.split(",");
                for (String cur : arr)
                {
                    idList.add(Integer.valueOf(cur));
                }
            }
            else
            {
                idList.add(Integer.valueOf(ids));
            }
            Map<String, Object> result = orderService.confirmEdbOrder(idList, isPush);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("客服确认 E店宝订单推送失败！！！", e);
            Map<String, Object> map = new HashMap<>();
            map.put("status", 0);
            map.put("msg", "确认失败");
            return JSON.toJSONString(map);
        }
    }

    /**
     * E店宝已确认订单列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/listConfirmed")
    public ModelAndView listConfirmed()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Map<String,Object> result = orderService.getConfirmedCountInfo();
        mv.addObject("errorNums", result.get("errorNums"));
        mv.setViewName("edbOrder/listConfirmed");
        return mv;
    }

    @RequestMapping(value = "/getConfirmedCountInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getConfirmedCountInfo()
        throws Exception
    {
        try
        {
            return JSON.toJSONString(orderService.getConfirmedCountInfo());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String,Object> result = new HashMap<>();
            result.put("waitNums", 0);
            result.put("successNums", 0);
            result.put("errorNums", 0);
            return JSON.toJSONString(result);
        }
    }

    /**
     * 异步获取已确认E店宝订单信息
     */
    @RequestMapping(value = "/jsonConfirmedEdbOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonConfirmedEdbOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "number", required = false, defaultValue = "") String number, // 订单编号
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId, // 商家ID
        @RequestParam(value = "pushStatus", required = false, defaultValue = "0") int pushStatus, // 推送状态
        @RequestParam(value = "receiveStatus", required = false, defaultValue = "-1") int receiveStatus, // 发货信息获取状态
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus // 订单状态
    )
        throws Exception
    {
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(number))
            {
                para.put("number", number);
            }
            if (sellerId != 0)
            {
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                para.put("sellerIdList", sellerIdList);
            }
            para.put("pushStatus", pushStatus);
            if (orderStatus > 0)
            {
                para.put("status", orderStatus);
            }
            if (receiveStatus != -1)
            {
                para.put("receiveStatus", receiveStatus);
            }
            return JSON.toJSONString(orderService.findEdbOrderConfirmed(para));
        }
        catch (Exception e)
        {
            log.error("获取已确认E店宝订单失败！", e);
            Map<String,Object> result = new HashMap<>();
            result.put("rows", new ArrayList<>());
            result.put("total", 0);
            return JSON.toJSONString(result);
        }
    }
    
}
