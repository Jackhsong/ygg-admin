package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ygg.admin.service.PurchaseStoringService;

@Controller("purchaseStoring")
public class PurchaseStoringController
{
    
    @Resource
    private PurchaseStoringService purchaseStoringService;
    
    @RequestMapping("updateUnallocationStoring/{providerProductId}/{number}")
    @ResponseBody
    public Object updateUnallocationStoring(@PathVariable int providerProductId, @PathVariable int number)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("providerProductId", providerProductId);
        param.put("number", number);
        purchaseStoringService.updatePurchaseStoring(param);
        return null;
    }
    
}
