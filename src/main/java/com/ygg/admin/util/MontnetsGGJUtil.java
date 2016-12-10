package com.ygg.admin.util;

import org.apache.http.impl.client.DefaultHttpClient;

import com.ygg.admin.sdk.montnets.common.EnvironmentTypeEnum;
import com.ygg.admin.sdk.montnets.common.MethodTypeEnum;
import com.ygg.admin.sdk.montnets.common.StaticValue;
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
public class MontnetsGGJUtil extends MontnetsUtil
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
            throw new Exception("【梦网短信】初始化参数methodType错误，初始化失败！！！");
        }
        if (envType == null || envType == EnvironmentTypeEnum.PROD.getValue())
        {
            username = StaticValue.username;
            password = StaticValue.password;
            host = "http://" + StaticValue.ip + ":" + StaticValue.port;
        }
        else if (envType == EnvironmentTypeEnum.SERVICE.getValue())
        {
            username = StaticValue.service_username;
            password = StaticValue.service_password;
            host = "http://" + StaticValue.service_ip + ":" + StaticValue.service_port;
        }
        else
        {
            throw new Exception("【梦网短信】初始化参数envType错误，初始化失败！！！");
        }
        
        httpClient = new DefaultHttpClient();
        Sms service = new SmsLocator(host);
        SmsSoap client = service.getSmsSoap();
        call = client.createCall();
    }
    
    
    
    public static void main(String[] args)
        throws Exception
    {
        //getBalance(EnvironmentTypeEnum.PROD.getValue());
        //getBalance(EnvironmentTypeEnum.SERVICE.getValue());
        
        //18664573290
        //sendSms("15968835661", "这是一条测试短信");
        
        /*List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("phone", "15968835661");
        map1.put("content", "test2");
        list.add(map1);
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("phone", "13738043225");
        map2.put("content", "test2");
        list.add(map2);
        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("phone", "18664573290");
        map3.put("content", "这是一条测试短信");
        list.add(map3);*/
        //sendMultContentSms(list);
        //sendSms(phones, "这是一条测试短信", EnvironmentTypeEnum.PROD.getValue());
        //sendSms(phones, "这是一条测试短信", EnvironmentTypeEnum.SERVICE.getValue());
        //        System.out.println(getBalance(EnvironmentTypeEnum.PROD.getValue()));
        //getBalance(EnvironmentTypeEnum.SERVICE.getValue());
        //        List<Map<String, String>> reportMap1 = getReport(EnvironmentTypeEnum.PROD.getValue());
        //        if (reportMap1 != null)
        //        {
        //            for (Map<String, String> report : reportMap1)
        //            {
        //                logger.info(report.toString());
        //            }
        //        }
//        List<Map<String, String>> reportMap2 = getReport(EnvironmentTypeEnum.SERVICE.getValue());
//        if (reportMap2 != null)
//        {
//            for (Map<String, String> report : reportMap2)
//            {
//                logger.info(report.toString());
//            }
//        }
        
    }
}
