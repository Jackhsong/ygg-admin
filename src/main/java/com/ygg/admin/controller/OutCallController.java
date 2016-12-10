package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ygg.admin.entity.RefundEntity;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.util.CommonEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.KdCompanyEnum;
import com.ygg.admin.service.OutCallServcie;
import com.ygg.admin.service.RefundService;
import com.ygg.admin.util.kd8.Kd8Util;

@Controller
@RequestMapping("/outCall")
public class OutCallController
{
    private Logger log = Logger.getLogger(OutCallController.class);
    
    @Resource
    private RefundService refundService;
    
    @Resource
    private OutCallServcie outCallServcie;

    @Resource(name = "orderService")
    private OrderService orderService = null;
    
    /**
     * 外部物流信息订阅
     * 
     * @param request
     * @param refundId 退款退货ID
     * @param channel 物流公司编码（之前快递100所定义的物流公司编码，全部为英文，如：yutong）
     * @param number 物流单号
     * @param money 运费
     * @param type 类型： 1：退款退货物流信息订阅
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/subscribeLogistics", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String subscribeLogistics(HttpServletRequest request, @RequestParam(value = "refundId", required = true) int refundId,
        @RequestParam(value = "channel", required = true) String channel, @RequestParam(value = "number", required = true) String number,
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, @RequestParam(value = "money", required = false, defaultValue = "0") int money)
        throws Exception
    {
        try
        {
            Map<String, Object> result = null;
            if (type == 1)
            {
                String compnayName = KdCompanyEnum.getCompanyNameByKd100Code(channel);
                result = refundService.saveRefundLogisticsInfo(refundId, number, compnayName, null);
            }
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("外部物流信息订阅 失败！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("errorCode", 0);
            result.put("msg", "服务器出错");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 快递100订阅回调接口
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/kd100Callback", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String kd100Callback(HttpServletRequest request)
        throws Exception
    {
        String param = request.getParameter("param");
        String refundId = request.getParameter("refundId");
        String orderId = request.getParameter("orderId");
        if (refundId != null)
        {
            boolean isOk = outCallServcie.kd100CallBack(param, refundId);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (isOk)
            {
                resultMap.put("result", true);
                resultMap.put("returnCode", 200);
                resultMap.put("message", "成功");
            }
            else
            {
                resultMap.put("result", false);
                resultMap.put("returnCode", 500);
                resultMap.put("message", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        else
        {
            boolean isOk = orderService.kd100CallBack(param, orderId);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (isOk)
            {
                resultMap.put("result", true);
                resultMap.put("returnCode", 200);
                resultMap.put("message", "成功");
            }
            else
            {
                resultMap.put("result", false);
                resultMap.put("returnCode", 500);
                resultMap.put("message", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }

    }
    
    /**
     * 对外提供物流公司信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logisticsChannelInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String logisticsChannelInfo(HttpServletRequest request)
        throws Exception
    {
        String[] arr =
            new String[] {"guotongkuaidi", "huiqiangkuaidi", "shunfeng", "yuantong", "tiantian", "zhaijisong", "jiajiwuliu", "shentong", "yunda", "lianbangkuaidi",
                "huitongkuaidi", "zhongtong", "ems"};
        List<String> arrToList = Arrays.asList(arr);
        List<Map<String, Object>> channelInfo = new ArrayList<Map<String, Object>>();
        for (KdCompanyEnum el : KdCompanyEnum.values())
        {
            String kd100Code = el.getKd100Code();
            String kd8Code = el.getKd8Code();
            if (arrToList.contains(kd8Code) || arrToList.contains(kd100Code))
            {
                Map<String, Object> currMap = new HashMap<String, Object>();
                currMap.put("name", el.getCompanyName());
                currMap.put("value", StringUtils.isEmpty(kd8Code) ? (StringUtils.isEmpty(kd100Code) ? "" : kd100Code) : kd8Code);
                channelInfo.add(currMap);
            }
        }
        return JSON.toJSONString(channelInfo);
    }
    
    /**
     * 快递吧回调地址
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/kd8Callback", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public int kd8Callback(HttpServletRequest request)
    {
        int result = -1;
        try
        {
            Map<String, String> param = new HashMap<String, String>();
            param.put("companyname", request.getParameter("companyname"));
            param.put("outid", request.getParameter("outid"));
            param.put("status", request.getParameter("status"));
            param.put("tracklist", request.getParameter("tracklist"));
            
            boolean isValidate = Kd8Util.validateSign(request);
            if (!isValidate)
            {
                log.error("【快递吧】回调签名验证失败！！！");
                return -8;
            }
            if (outCallServcie.kd8CallBack(param))
            {
                result = 0;
            }
        }
        catch (Exception e)
        {
            log.error("【快递吧】快递回调出错！！！", e);
            result = -9;
        }
        return result;
    }

    /**
     * 退款取消，wap调用
     *
     * @param refundId 退款退货ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancelRefund", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String cancelRefund(@RequestParam(value = "refundId", required = true) int refundId)
        throws Exception
    {
        try
        {
            RefundEntity refund = refundService.findRefundInfo(refundId);
            if (refund == null || (refund.getStatus() != CommonEnum.RefundStatusEnum.CANCEL.ordinal() && refund.getStatus() != CommonEnum.RefundStatusEnum.APPLY.ordinal()))
            {
                Map<String, Object> result = new HashMap<>();
                result.put("status", 0);
                result.put("errorCode", 0);
                result.put("msg", "数据错误");
                return JSON.toJSONString(result);
            }
            if (refund.getStatus() == CommonEnum.RefundStatusEnum.CANCEL.ordinal())
            {
                Map<String, Object> result = new HashMap<>();
                result.put("status", 1);
                return JSON.toJSONString(result);
            }
            Map<String, Object> result = refundService.updateRefund(refund.getType(), 3, (byte)1, null, refundId, "用户自行取消退款申请", null, 0, false, true, null, false, 0);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("外部调用退款取消 失败！", e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("errorCode", 0);
            result.put("msg", "服务器出错");
            return JSON.toJSONString(result);
        }
    }
    
}
