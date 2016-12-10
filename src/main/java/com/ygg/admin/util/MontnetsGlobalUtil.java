package com.ygg.admin.util;

import org.apache.http.impl.client.DefaultHttpClient;

import com.ygg.admin.sdk.montnets.common.EnvironmentTypeEnum;
import com.ygg.admin.sdk.montnets.common.MethodTypeEnum;
import com.ygg.admin.sdk.montnets.common.StaticValueGlobal;
import com.ygg.admin.sdk.montnets.httpget.CHttpGet;
import com.ygg.admin.sdk.montnets.httppost.CHttpPost;
import com.ygg.admin.sdk.montnets.httpsoap.CHttpSoap;
import com.ygg.admin.sdk.montnets.httpsoap.Sms;
import com.ygg.admin.sdk.montnets.httpsoap.SmsLocator;
import com.ygg.admin.sdk.montnets.httpsoap.SmsSoap;

/**
 * @author lorabit
 * @since 16-4-7
 */
@SuppressWarnings("deprecation")
public class MontnetsGlobalUtil extends MontnetsUtil
{
    
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
            throw new Exception("【梦网左岸城堡短信】初始化参数methodType错误，初始化失败！！！");
        }
        if (envType == null || envType == EnvironmentTypeEnum.PROD.getValue())
        {
            username = StaticValueGlobal.username;
            password = StaticValueGlobal.password;
            host = "http://" + StaticValueGlobal.ip + ":" + StaticValueGlobal.port;
        }
        else if (envType == EnvironmentTypeEnum.SERVICE.getValue())
        {
            /*
             * username = StaticValueTuan.service_username; password = StaticValueTuan.service_password; host =
             * "http://" + StaticValueTuan.service_ip + ":" + StaticValueTuan.service_port;
             */
        }
        else
        {
            throw new Exception("【梦网左岸城堡短信】初始化参数envType错误，初始化失败！！！");
        }
        
        httpClient = new DefaultHttpClient();
        Sms service = new SmsLocator(host);
        SmsSoap client = service.getSmsSoap();
        call = client.createCall();
    }
    
    
}
