package com.ygg.admin.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.KdCompanyEnum;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.service.RefundService;
import com.ygg.admin.servlet.Kd100Service;

/**
 * 快递100回调接口
 * 
 * @author zhangyb
 *         
 */
@Controller
@RequestMapping("/kd100")
public class Kd100CallbackController
{
    
    Logger log = Logger.getLogger(Kd100CallbackController.class);
    
    @Resource(name = "orderDao")
    private OrderDao orderDao;
    
    @Resource
    private Kd100Service kd100Service;
    
    @Resource
    private RefundService refundService;
    
    private boolean sendOrder(Map<String, Object> para)
        throws Exception
    {
        
        Map<String, String> sendMap = new HashMap<String, String>();
        sendMap.put("id", para.get("orderId") + "");
        sendMap.put("company", KdCompanyEnum.getKd100CodeByCompanyName(para.get("channel") + ""));
        sendMap.put("number", para.get("number") + "");
        boolean isSuccess = kd100Service.send(sendMap);
        return isSuccess;
    }
    
    @RequestMapping(value = "/sendTest", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String sendTest(HttpServletRequest request)
        throws Exception
    {


//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>()
//        {
//            {
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221523");
//                        put("channel", "汇通快运");
//                        put("number", "70187604057794");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220914");
//                        put("channel", "汇通快运");
//                        put("number", "70187601057795");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220731");
//                        put("channel", "汇通快运");
//                        put("number", "70187609057796");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223979");
//                        put("channel", "汇通快运");
//                        put("number", "50136322208756");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223836");
//                        put("channel", "汇通快运");
//                        put("number", "50136327208955");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223789");
//                        put("channel", "汇通快运");
//                        put("number", "50136322208254");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223704");
//                        put("channel", "汇通快运");
//                        put("number", "50136323208453");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223511");
//                        put("channel", "汇通快运");
//                        put("number", "50136325208652");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223478");
//                        put("channel", "汇通快运");
//                        put("number", "50136326208851");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223460");
//                        put("channel", "汇通快运");
//                        put("number", "50136327208050");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223390");
//                        put("channel", "汇通快运");
//                        put("number", "50136324208249");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223332");
//                        put("channel", "汇通快运");
//                        put("number", "50136322208448");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "222169");
//                        put("channel", "汇通快运");
//                        put("number", "210960147788");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "222071");
//                        put("channel", "汇通快运");
//                        put("number", "210960147789");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221977");
//                        put("channel", "汇通快运");
//                        put("number", "210960147790");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221367");
//                        put("channel", "汇通快运");
//                        put("number", "210960147791");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221266");
//                        put("channel", "汇通快运");
//                        put("number", "210960147792");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223733");
//                        put("channel", "汇通快运");
//                        put("number", "210960147799");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223783");
//                        put("channel", "汇通快运");
//                        put("number", "70340907052692");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223450");
//                        put("channel", "汇通快运");
//                        put("number", "70340909052686");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223172");
//                        put("channel", "汇通快运");
//                        put("number", "70340901052690");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "223036");
//                        put("channel", "汇通快运");
//                        put("number", "70340903052694");
//                    }
//                });
//
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "222950");
//                        put("channel", "汇通快运");
//                        put("number", "70340903052689");
//                    }
//                });
//
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "222138");
//                        put("channel", "汇通快运");
//                        put("number", "210971429728");
//                    }
//                });
//
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "222102");
//                        put("channel", "汇通快运");
//                        put("number", "210971429727");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221515");
//                        put("channel", "汇通快运");
//                        put("number", "210971429732");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221490");
//                        put("channel", "汇通快运");
//                        put("number", "210971429731");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221172");
//                        put("channel", "汇通快运");
//                        put("number", "70340905052688");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "221036");
//                        put("channel", "汇通快运");
//                        put("number", "70340901052685");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220878");
//                        put("channel", "汇通快运");
//                        put("number", "210971429730");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220873");
//                        put("channel", "汇通快运");
//                        put("number", "70340905052693");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220866");
//                        put("channel", "汇通快运");
//                        put("number", "210971429729");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220667");
//                        put("channel", "汇通快运");
//                        put("number", "70340909052691");
//                    }
//                });
//                add(new HashMap<String, Object>()
//                {
//                    {
//                        put("orderId", "220415");
//                        put("channel", "汇通快运");
//                        put("number", "70340907052687");
//
//                    }
//                });
//            }
//        };
//
//        for (Map<String, Object> map : list)
//        {
//            boolean status = sendOrder(map);
////            Map<String, Object> map1 = new HashMap<String, Object>();
//            System.out.println("status:" + status + "map" + map);
////            map.put("status", status);
////            return JSON.toJSONString(map1);
//        }
        return "ok";
    }
    
}
