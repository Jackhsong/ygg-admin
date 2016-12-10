
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.sdk.wenxin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.MD5Util;

/**
  * 
  	*微信支付服务器签名支付请求请求类
	*api说明：
	*init(app_id, app_secret, partner_key, app_key);
	*初始化函数，默认给一些参数赋值，如cmdno,date等。
	*setKey(key_)'设置商户密钥
	*getLasterrCode(),获取最后错误号
	*GetToken();获取Token
	*getTokenReal();Token过期后实时获取Token
	*createMd5Sign(signParams);生成Md5签名
	*genPackage(packageParams);获取package包
	*createSHA1Sign(signParams);创建签名SHA1
	*sendPrepay(packageParams);提交预支付
	*getDebugInfo(),获取debug信息
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: RequestHandler.java 10162 2016-04-13 05:54:23Z zhanglide $   
  * @since 2.0
  */
public class RequestHandler {
	
	Logger logger = Logger.getLogger(RequestHandler.class);
    
    /** Token获取网关地址地址 */
    private String tokenUrl;
    
    /** 预支付网关url地址 */
    private String gateUrl;
    
    /** 查询支付通知网关URL */
    private String notifyUrl;
    
    /** 商户参数 */
    private String appid;
    
    private String appkey;
    
    private String partnerkey;
    
    private String appsecret;
    
    private String key;
    
    /** 请求的参数 */
    private SortedMap<String, String> parameters;
    
    /** Token */
    private String Token;
    
    private String charset;
    
    /** debug信息 */
    private String debugInfo;
    
    private String last_errcode;
    
    private HttpServletRequest request;
    
    private HttpServletResponse response;
    
    /**
     * 初始构造函数。
     *
     * @return
     */
    public RequestHandler(HttpServletRequest request, HttpServletResponse response)
    {
        this.last_errcode = "0";
        this.request = request;
        this.response = response;
        this.charset = "UTF-8";
        this.parameters = new TreeMap<String, String>();
        // 验证notify支付订单网关
        notifyUrl = null; // "https://gw.tenpay.com/gateway/simpleverifynotifyid.xml";
        
    }
    
    /**
     * 初始化函数。
     */
    public void init(String app_id, String app_secret, String app_key, String partner_key)
    {
        this.last_errcode = "0";
        this.Token = "token_";
        this.debugInfo = "";
        this.appkey = app_key;
        this.appid = app_id;
        this.partnerkey = partner_key;
        this.appsecret = app_secret;
    }
    
    public void init()
    {
    }
    
    public SortedMap<String, String> getParameters()
    {
        return parameters;
    }
    
    /**
     * 获取最后错误号
     */
    public String getLasterrCode()
    {
        return last_errcode;
    }
    
    /**
     * 获取入口地址,不包含参数值
     */
    public String getGateUrl()
    {
        return gateUrl;
    }
    
    /**
     * 获取参数值
     *
     * @param parameter 参数名称
     * @return String
     */
    public String getParameter(String parameter)
    {
        String s = (String)this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }
    
    // 设置密钥
    
    public void setKey(String key)
    {
        this.key = key;
    }
    
    // 设置微信密钥
    public void setAppKey(String key)
    {
        this.appkey = key;
    }
    
    // 特殊字符处理
    public String UrlEncode(String src)
        throws UnsupportedEncodingException
    {
        return URLEncoder.encode(src, this.charset).replace("+", "%20");
    }
    
    // 获取package的签名包
    public String genPackage(SortedMap<String, String> packageParams)
        throws UnsupportedEncodingException
    {
        String sign = createSign(packageParams);
        
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            sb.append(k + "=" + UrlEncode(v) + "&");
        }
        
        // 去掉最后一个&
        String packageValue = sb.append("sign=" + sign).toString();
        // System.out.println("packageValue=" + packageValue);
        return packageValue;
    }
    
    public String getParams(SortedMap<String, String> packageParams)
    {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = packageParams.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
            
        }
        String re = sb.toString();
        re = re.substring(0, re.length() - 1);
        return re;
    }
    
    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public String createSign(SortedMap<String, String> packageParams)
    {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = packageParams.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + this.getKey());
        String sign = MD5Util.MD5Encode(sb.toString(), this.charset).toUpperCase();
        return sign;
    }
    
    
    // 输出XML
    public String parseXML()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (null != v && !"".equals(v) && !"appkey".equals(k))
            {
                
                sb.append("<" + k + ">" + getParameter(k) + "</" + k + ">\n");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }
    
    /**
     * 设置debug信息
     */
    protected void setDebugInfo(String debugInfo)
    {
        this.debugInfo = debugInfo;
    }
    
    public void setPartnerkey(String partnerkey)
    {
        this.partnerkey = partnerkey;
    }
    
    public String getDebugInfo()
    {
        return debugInfo;
    }
    
    public String getKey()
    {
        return key;
    }
    
    public String getTokenUrl()
    {
        return tokenUrl;
    }
    
    public void setTokenUrl(String tokenUrl)
    {
        this.tokenUrl = tokenUrl;
    }
    
    
    /**
     * 设置参数值
     * 
     * @param parameter 参数名称
     * @param parameterValue 参数值
     */
    
    public void setParameter(String parameter, String parameterValue)
    {
        String v = "";
        if (null != parameterValue)
        {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }
    
    
    public String getRequestURL(){
        // this.createSign();
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        String enc = "UTF-8"; // TenpayUtil.getCharacterEncoding(this.request, this.response);
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k))
            {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            }
            else
            {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        String result = sb.toString();
        return result; // new String(result.getBytes(), "ISO8859-1");
    }
    
	
	
}
