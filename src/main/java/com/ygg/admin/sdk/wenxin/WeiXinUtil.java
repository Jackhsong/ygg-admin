package com.ygg.admin.sdk.wenxin;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonHttpClient;

/**
 * @author wuhy
 * @date 创建时间：2016年5月17日 下午3:14:29
 */
public class WeiXinUtil
{
    
    private static final JsonParser parser = new JsonParser();
    
    private static final Logger logger = Logger.getLogger(WeiXinUtil.class);
    
    private static String getAccessToken()
    {
        return CommonHttpClient.commonHTTP("get", CommonConstant.QQBS_WEIXIN_TOKEN_URL, new HashMap<String, Object>()).get("accessToken").toString();
    }
    
    public static String buildPersistentQRCode(int accountId)
    {
        String weixinAPIUrl = CommonConstant.QQBS_WEIXIN_CREATE_QRCODE_URL + "?access_token=" + getAccessToken();
        String persistentQRCodeBody = buildPersistentQRCodeBody(accountId);
        JsonObject callPersistentQRCodeResult = parser.parse(CommonHttpClient.sendRESTFulPost(weixinAPIUrl, persistentQRCodeBody, new HashMap<String, String>())).getAsJsonObject();
        JsonElement ticketJson = callPersistentQRCodeResult.get("ticket");
        if (ticketJson.isJsonNull())
        {
            logger.error("生成二维码失败");
            throw new ServiceException("生成二维码失败");
        }
        
        return CommonConstant.QQBS_WEIXIN_SHOW_QRCODE_URL + "?ticket=" + ticketJson.getAsString();
        
    }
    
    private static String buildPersistentQRCodeBody(int accountId)
    {
        return "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + accountId + "\"}}}";
    }
    
}
