package com.ygg.admin.util;

import org.apache.http.impl.client.DefaultHttpClient;

import com.ygg.admin.sdk.montnets.common.MethodTypeEnum;
import com.ygg.admin.sdk.montnets.httpget.CHttpGet;
import com.ygg.admin.sdk.montnets.httppost.CHttpPost;
import com.ygg.admin.sdk.montnets.httpsoap.CHttpSoap;
import com.ygg.admin.sdk.montnets.httpsoap.Sms;
import com.ygg.admin.sdk.montnets.httpsoap.SmsLocator;
import com.ygg.admin.sdk.montnets.httpsoap.SmsSoap;

/**
 * 梦网短信 
 * @author xiongl
 *
 */
@SuppressWarnings("deprecation")
public class MontnetsGGJCRMUtil extends MontnetsUtil
{
    private static final String USERNAME = "JS1673";
    private static final String PASSWORD = "236523";
    private static final String IP = "61.145.229.28";
    private static final String PORT = "7902";
    
    
    public void init(Integer methodType, Integer envType)
        throws Exception
    {
        if (methodType == null || methodType == MethodTypeEnum.GET.getValue())
        {
            sms = new CHttpGet();
        }
        else if (methodType == MethodTypeEnum.POST.getValue())
        {
            sms = new CHttpPost();
        }
        else if (methodType == MethodTypeEnum.SOAP.getValue())
        {
            sms = new CHttpSoap();
        }
        else
        {
            throw new Exception("【梦网短信】初始化参数methodType错误，初始化失败！！！");
        }
        
        username = USERNAME;
        password = PASSWORD;
        host = "http://" + IP + ":" + PORT;
        
        httpClient = new DefaultHttpClient();
        Sms service = new SmsLocator(host);
        SmsSoap client = service.getSmsSoap();
        call = client.createCall();
    }
    
}
