package com.ygg.admin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.service.QqbsOrderService;



@Controller
@RequestMapping("qqbsOrder")
public class QqbsOrderController
{
    
    @Resource
    private QqbsOrderService qqbsOrderService;
    
    @RequestMapping("toList")
    public ModelAndView toList() throws Exception {
        ModelAndView mv = new ModelAndView("qqbsSale/order");
        return mv;
    }
    
    @RequestMapping("orderList")
    @ResponseBody
    public Object orderList(String orderNumber, String orderStatus, String accountId,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, 
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows) throws Exception {
        
        page = page == 0 ? 1 : page;
        return JSON.toJSONStringWithDateFormat(qqbsOrderService.findOrderList(orderNumber, orderStatus, accountId, page, rows), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
    }
    
}
