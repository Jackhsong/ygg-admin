package com.ygg.admin.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.uitls.ApnsUtils;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.ygg.admin.code.AppPushContentTypeEnum;
import com.ygg.admin.code.AppPushInfoTypeEnum;
import com.ygg.admin.code.AppPushResultTypeEnum;
import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.service.AppPushService;
import com.ygg.admin.util.CommonConstant;

@Service("appPushService")
public class AppPushServiceImpl implements AppPushService
{
    Logger log = Logger.getLogger(AppPushServiceImpl.class);
    
    @Resource
    private AccountDao accountDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Override
    public Map<String, Object> pushMessage(List<String> accountIdList, Set<String> pushIdList, byte pushType, String pushUrl, String pushNumber, String pushTitle,
        String pushContent, String pushProductId, String pushWindowId)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        //如果是订单详情页，将pushNumber替换为相应订单ID
        if ((pushType == AppPushInfoTypeEnum.ORDER_DETAIL.ordinal()))
        {
            OrderEntity order = orderDao.findOrderByNumber(pushNumber);
            if (order == null)
            {
                result.put("status", 0);
                result.put("msg", "订单编号不存在");
                return result;
            }
            pushNumber = order.getId() + "";
        }
        //建立连接，开始鉴权
        IGtPush push = new IGtPush(CommonConstant.GETUI_IP, CommonConstant.GETUI_APPKEY, CommonConstant.GETUI_MASTER);
        push.connect();
        try
        {
            AppPushInfoTypeEnum e = AppPushInfoTypeEnum.getEnumByOrdinal(pushType);
            String androidContent = e.getAppType() + "&" + pushContent + "&" + pushTitle;
            String iosContent = e.getAppType() + "&0&" + pushContent;
            if (e.getAppType() == AppPushContentTypeEnum.CUSTOM_PAGE.ordinal())
            {
                androidContent += "&" + pushUrl;
                iosContent += "&" + pushUrl;
            }
            else if (e.getAppType() == AppPushContentTypeEnum.ORDER_DETAIL.ordinal())
            {
                androidContent += "&" + pushNumber;
                iosContent += "&" + pushNumber;
            }
            else if (e.getAppType() == AppPushContentTypeEnum.SALE_PRODUCT_SELLING.ordinal() || e.getAppType() == AppPushContentTypeEnum.MALL_PRODUCT_SELLING.ordinal())
            {
                androidContent += "&" + pushProductId;
                iosContent += "&" + pushProductId;
            }
            else if (e.getAppType() == AppPushContentTypeEnum.SALE_WINDOW_SELLING.ordinal() || e.getAppType() == AppPushContentTypeEnum.MALL_WINDOW_SELLING.ordinal())
            {
                androidContent += "&" + pushWindowId;
                iosContent += "&" + pushWindowId;
            }
            
            //ios最大长度验证
            int length = ApnsUtils.validatePayloadLength("", "", pushTitle, "", "", String.valueOf(1), "", iosContent, null);
            if (length > 256)
            {
                
                result.put("status", 0);
                result.put("msg", "信息总长度过长，超出约" + ((length - 256) / 3) + "个汉字 或者 " + (length - 256) + "个英文字符数字");
                return result;
            }
            //模板
            TransmissionTemplate template = new TransmissionTemplate();
            template.setAppId(CommonConstant.GETUI_APPID);
            template.setAppkey(CommonConstant.GETUI_APPKEY);
            template.setTransmissionType(2);//透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动。
            template.setTransmissionContent(androidContent);//透传内容，不支持转义字符
            /*
             *  template.setPushInfo("", 1, "通知栏内容", "default", "透传消息", "", "", "");
             */
            template.setPushInfo("", 1, pushTitle, "", iosContent, "", "", "");
            Map<String, Object> pushResponse = null;
            if (pushIdList.size() > 1)
            {
                ListMessage message = new ListMessage();
                message.setData(template);
                //设置消息离线，并设置离线时间
                message.setOffline(true);
                //离线有效时间，单位为毫秒，可选
                message.setOfflineExpireTime(1 * 1000 * 3600);
                message.setPushNetWorkType(0);
                
                //配置推送目标
                List<Target> targets = new ArrayList<Target>();
                for (String it : pushIdList)
                {
                    Target target = new Target();
                    target.setAppId(CommonConstant.GETUI_APPID);
                    target.setClientId(it);
                    targets.add(target);
                }
                //获取taskID
                String taskId = push.getContentId(message);
                //使用taskID对目标进行推送
                IPushResult pushResult = push.pushMessageToList(taskId, targets);
                pushResponse = pushResult.getResponse();
            }
            else
            {
                SingleMessage message = new SingleMessage();
                message.setOffline(true);
                message.setOfflineExpireTime(1 * 1000 * 3600);
                message.setData(template);
                
                Target target = new Target();
                target.setAppId(CommonConstant.GETUI_APPID);
                for (String it : pushIdList)
                {
                    target.setClientId(it);
                }
                IPushResult pushResult = push.pushMessageToSingle(message, target);
                pushResponse = pushResult.getResponse();
            }
            String resultStr = (String)pushResponse.get("result");
            if ("ok".equals(resultStr))
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", AppPushResultTypeEnum.getErrorDescByName(resultStr));
            }
        }
        catch (Exception e)
        {
            log.error("推送出错！", e);
            result.put("status", 0);
            result.put("msg", "系统异常");
        }
        finally
        {
            try
            {
                push.close();
            }
            catch (IOException e)
            {
                log.error("close IGtPush 出错！", e);
            }
        }
        
        return result;
    }
}
