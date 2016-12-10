package com.ygg.admin.util;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.ygg.admin.entity.PushEntity;

public class PushBase
{
    /**
     * 根据分组推送
     * 
     * @param list 根据CID推送
     * @throws Exception 
     */
    public static void pushGroup(String appId, String appKey, String masterSecret, List<Target> targetList, PushEntity entity) throws Exception {
        IGtPush push = new IGtPush(appKey, masterSecret);
        ListMessage message = new ListMessage();
        message.setData(getTransmissionTemplate(appId, appKey, entity));
        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(Integer.valueOf(entity.getExpireTime()) * 1000 * 3600);
        message.setPushNetWorkType(0);
        
        String contentId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(contentId, targetList);
        
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }
    
    /**
     * 根据应用推送信息
     * 
     * @param appId
     * @param appKey
     * @param masterSecret
     * @param entity
     * @throws Exception 
     */
    public static void pushAppMessage(String appId, String appKey, String masterSecret, PushEntity entity) throws Exception {
        IGtPush push = new IGtPush(appKey, masterSecret);

        AppMessage message = new AppMessage();
        message.setData(getTransmissionTemplate(appId, appKey, entity));

        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(Integer.valueOf(entity.getExpireTime()) * 1000 * 3600);
        List<String> appIdList = new ArrayList<String>();
        appIdList.add(appId);
        message.setAppIdList(appIdList);
        IPushResult ret = push.pushMessageToApp(message, entity.getDesc());
        
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }
    
    @SuppressWarnings("deprecation")
    public static TransmissionTemplate getTransmissionTemplate(String appId, String appKey, PushEntity entity)
        throws Exception
    {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);
        // Android透传
        template.setTransmissionContent(getContentType(Integer.valueOf(entity.getContentType())) + "&" + entity.getAndroidTitle() + "&" + entity.getAndroidContent() + "&" + entity.getProductId());
        // ios透传
        template.setPushInfo("", 1, entity.getAndroidContent(), "", getContentType(Integer.valueOf(entity.getContentType())) + "&1&2&" + entity.getProductId(), "", "", "");
        return template;
    }
    
    private static final int INDEX = 1; // 首页
    private static final int CUSTOM = 2; // 自定义
    private static final int SINGTON = 3; //特卖单品
    private static final int COMMON = 4;// 特卖组合
    
    /**
     * 推送内容类型转换为APP监听类型。
     * 
     * @param type
     * @return
     */
    private static int getContentType(int type) {
        switch (type)
        {
            case INDEX:
                return 3;
            case CUSTOM:
                return 12;
            case SINGTON:
                return 8;
            case COMMON:
                return 10;
            default:
                return 0;
        }
    }
    
}
