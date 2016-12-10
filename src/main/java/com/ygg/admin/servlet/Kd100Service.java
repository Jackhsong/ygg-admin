package com.ygg.admin.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.kd100.HttpRequest;
import com.ygg.admin.util.kd100.JacksonHelper;
import com.ygg.admin.util.kd100.pojo.TaskRequest;
import com.ygg.admin.util.kd100.pojo.TaskResponse;

/**
 * 快递100相关服务
 * 
 * @author Administrator
 *
 */
public class Kd100Service
{
    
    Logger logger = Logger.getLogger(Kd100Service.class);
    
    @Resource(name = "orderDao")
    private OrderDao orderDao;
    
    @Resource
    private RestTemplate restTemplate;
    
    /**
     * 向快递100发起订阅
     * 
     * @param para
     * @return
     */
    public boolean send(Map<String, String> para)
    {
        TaskRequest req = new TaskRequest();
        req.setCompany(para.get("company"));
        req.setFrom(para.get("from"));
        req.setTo(para.get("to"));
        req.setNumber(para.get("number"));
        req.getParameters().put("callbackurl", YggAdminProperties.getInstance().getPropertie("kd100_callback") + "?orderId=" + para.get("id"));
        req.setKey(CommonConstant.KD100_KEY);
        HashMap<String, String> p = new HashMap<String, String>();
        p.put("schema", "json");
        p.put("param", JacksonHelper.toJSON(req));
        try
        {
            String ret = HttpRequest.postData(CommonConstant.KD100_SUBSCRIBE, p, "UTF-8");
            TaskResponse resp = JacksonHelper.fromJSON(ret, TaskResponse.class);
            if (resp.getResult() == true)
            {
                logger.info("订阅成功，返回内容：" + resp.getReturnCode() + ":" + resp.getMessage() + "，订单ID：" + para.get("id"));
                return true;
            }
            else
            {
                logger.warn("向快递100发起订阅失败，返回内容：" + resp.getReturnCode() + ":" + resp.getMessage() + "，订单ID：" + para.get("id"));
                if ("501".equals(resp.getReturnCode()))
                {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("orderId", para.get("id"));
                    map.put("trackInfo", "polling");
                    orderDao.updateOrderLogistics(map);
                }
                return false;
            }
        }
        catch (Exception e)
        {
            logger.error("向快递100发起订阅，抛出错误，订单ID：" + para.get("id"), e);
            return false;
        }
    }
    
    /**
     * 退款退货 物流 向快递100发起订阅
     * 
     * @param para
     * @return
     */
    public boolean refundSend(Map<String, String> para)
    {
        TaskRequest req = new TaskRequest();
        req.setCompany(para.get("company"));
        req.setFrom(para.get("from"));
        req.setTo(para.get("to"));
        req.setNumber(para.get("number"));
        req.getParameters().put("callbackurl", YggAdminProperties.getInstance().getPropertie("kd100_refund_callback") + "?refundId=" + para.get("id"));
        req.setKey(CommonConstant.KD100_KEY);
        
        HashMap<String, String> p = new HashMap<String, String>();
        p.put("schema", "json");
        p.put("param", JacksonHelper.toJSON(req));
        try
        {
            String ret = HttpRequest.postData(CommonConstant.KD100_SUBSCRIBE, p, "UTF-8");
            TaskResponse resp = JacksonHelper.fromJSON(ret, TaskResponse.class);
            if (resp.getResult() == true)
            {
                return true;
            }
            else
            {
                logger.warn("退款退货 物流 向快递100发起订阅失败，返回内容：" + resp.getReturnCode() + ":" + resp.getMessage() + "，退款退货ID：" + para.get("id"));
                return false;
            }
        }
        catch (Exception e)
        {
            logger.error("退款退货 物流 向快递100发起订阅，抛出错误，退款退货ID：" + para.get("id"), e);
            return false;
        }
    }
    
    // public int send(Map<String, Object> para){
    // logger.info("开始向快递100发起订阅，订单ID："+para.get("id"));
    // MultiValueMap<String, Object> reqMsg = new LinkedMultiValueMap<String,
    // Object>();
    // //MultiValueMap reqMsg = new LinkedMultiValueMap();
    // reqMsg.add("company", para.get("company")); //快递公司编码（小写）
    // reqMsg.add("number", para.get("number")); //快递单号
    // reqMsg.add("from", ""); //出发城市
    // reqMsg.add("to", ""); //目的城市
    // reqMsg.add("key", CommonConstant.KD100_KEY); // 推送服务授权密匙
    // Map<String, Object> parameters = new HashMap<String, Object>();
    // parameters.put("callbackurl", CommonConstant.KD100_CALLBACK + "?orderId="
    // + para.get("id"));
    // parameters.put("salt", CommonConstant.KD100_SALTPREFIX + para.get("id"));
    // parameters.put("resultv2", "0");
    //
    // reqMsg.add("parameters", JSON.toJSONString(parameters));
    //
    // String result =
    // restTemplate.postForObject(CommonConstant.KD100_SUBSCRIBE, reqMsg,
    // String.class);
    // // String result =
    // restTemplate.postForObject("http://localhost:8080/ygg/order/simulate",
    // reqMsg, String.class);
    // // Map resultMap = (HashMap)JSON.parse(result);
    // logger.info("向快递100发起订阅得到返回结果:");
    // logger.info(result);
    // return 0;
    // }
}
