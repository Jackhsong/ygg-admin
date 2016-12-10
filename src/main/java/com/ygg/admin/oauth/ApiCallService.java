package com.ygg.admin.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.sdk.alipay.config.AlipayConfig;
import com.ygg.admin.sdk.alipay.util.AlipaySubmit;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonHttpClient;
import com.ygg.admin.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * api调用的服务类
 * 
 */
public class ApiCallService
{
    private static final Logger logger = LoggerFactory.getLogger(ApiCallService.class);


    /**
     * 微信退款
     * @param refund
     * @param key
     * @param cert
     * @return
     * @throws Exception
     */
    public static JSONObject weChatRefund(WeiChatRefund refund, String key, String cert)
            throws Exception
    {
        refund.createSign(key);
        logger.info("微信立即退款请求参数："+ JSON.toJSONString(refund));
        JSONObject j = CommonHttpClient.sendXmlRefundHTTP(CommonConstant.WECHAT_REFUND_URL, refund.getMch_id(), cert, CommonUtil.objectToXml(refund));
        return j;
    }

    public static JSONObject globalAlipayRefund(GlobalAlipayRefund refund)throws Exception
    {
        Map<String, String> parameters = refund.getParamters();
        String resultStr = AlipaySubmit.executeRequest(parameters, "", "", AlipayConfig.GLOBAL_GATE_URL);
        JSONObject j = CommonUtil.parseXml(resultStr);
        return j;
    }
    
    public static void main(String[] args)
        throws Exception
        
    {
        /*JSONObject j = weChatRefund("FD84804E93924F99BD7D5F106AB01C", "4004242001201604184966244485", "991150", 1, 1);
        System.out.println(j.toJSONString());*/
        
        /*
         * UserInfo UserInfo = getUserInfo(
         * "DC1vcLRrPGGvvuvUi5HPx0-MTzKZ5FDrL6lfsjNW4aSoukXczOaaaVf-Bz53W9knFCJJbUZ1CnSQY0GucDyF5WaF0myJEnQV95dlJneeNG0",
         * "oOOq-s9enyRnhKuImh7ftkzpsHwI"); if (UserInfo != null) { System.out.println(UserInfo.getNickname()); }
         */

        GlobalAlipayRefund refund = new GlobalAlipayRefund("56039","C96BFC3F79D2440CAE6D2AFF760370","0.1");
        refund.setProduct_code("NEW_WAP_OVERSEAS_SELLER");
        refund.setSplit_fund_info("[{'transOut':'" + AlipayConfig.GLOBAL_SPLIT_PARTNER + "','amount':'" + "0.1" + "','currency':'CNY','desc':'国际支付宝订单退款'}]");
        System.out.println(globalAlipayRefund(refund));
    }
    
}
