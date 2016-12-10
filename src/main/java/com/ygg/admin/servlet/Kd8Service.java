package com.ygg.admin.servlet;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.joda.time.DateTime;

import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.kd100.HttpRequest;
import com.ygg.admin.util.kd8.Kd8Util;

/**
 * 快递吧
 * @author xiongl
 *
 */
public class Kd8Service
{
    private static Logger logger = Logger.getLogger(Kd8Service.class);
    
    public JSONObject send(Map<String, String> para)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            String company = para.get("company");
            if (StringUtils.isEmpty(company))
            {
                throw new Exception("物流公司名称channel为空！！！");
            }
            String number = para.get("number");
            if (StringUtils.isEmpty(number))
            {
                throw new Exception("物流单号number为空！！！");
            }
            String sendTime = para.get("sendTime");
            if (StringUtils.isEmpty(sendTime))
            {
                sendTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
            }
            String outids = company + "," + number + "," + sendTime;
            String sign = Kd8Util.createSign(outids);
            Map<String, String> data = new HashMap<String, String>();
            data.put("data", outids);
            data.put("account", CommonConstant.KD8_ACCOUNT);
            data.put("sign", sign);
            String result = HttpRequest.postData(CommonConstant.KD8_SUBSCRIBE, data, "UTF-8");

            if (StringUtils.isEmpty(result))
            {
                jsonObject.put("code", CommonConstant.COMMON_YES);
                jsonObject.put("message", "快递吧订阅失败，网络异常");
                logger.error("【快递吧】快递订阅失败，订单ID：" + para.get("id") + "；错误消息：网络异常");
            }
            else
            {
                jsonObject.put("code", result.split(";")[0]);
                jsonObject.put("message", result.split(";")[1]);
            }
        }
        catch (Exception e)
        {
            logger.error("【快递吧】向快递吧发起订阅，抛出错误，订单ID：" + para.get("id"), e);
            jsonObject.put("code", CommonConstant.COMMON_YES);
            jsonObject.put("message", e.getMessage());
        }
        return jsonObject;
    }
    
    /*    public static void main(String[] args)
        {
            Map<String, String> para = new HashMap<String, String>();
            para.put("channel", "申通快递");
            para.put("number", "229393251290");
            new Kd8Service().send(para);
        }*/
}
