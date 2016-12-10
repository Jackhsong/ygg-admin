package com.ygg.admin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

@SuppressWarnings("deprecation")
public class CommonHttpClient
{
    private static final Logger log = Logger.getLogger(CommonHttpClient.class);
    
    /**
     * 左岸城堡
提现/退款
     * 
     * @param url ：请求url
     * @param mchid:weixin_mchid
     * @param certPath:证书路径
     * @param xmlData：请求数据
     * @return
     * @throws Exception
     */
    public static JSONObject sendXmlRefundHTTP(String url, String mchid, String certPath, String xmlData)
        throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // InputStream instream =
        // CommonHttpClient.class.getClassLoader().getResourceAsStream(YggAdminProperties.getInstance().getPropertie("CERT"));
        FileInputStream instream = new FileInputStream(new File(certPath));
        try
        {
            keyStore.load(instream, mchid.toCharArray());
            
        }
        finally
        {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
        
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf =
            new SSLConnectionSocketFactory(sslcontext, new String[] {"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try
        {
            
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(xmlData, "UTF-8"));
            httpPost.addHeader("Content-Type", "text/xml");
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try
            {
                
                int status = response.getStatusLine().getStatusCode();
                if (status == 200)
                {
                    HttpEntity entity = response.getEntity();
                    return parseXml(entity.getContent());
                }
            }
            finally
            {
                response.close();
            }
        }
        finally
        {
            httpclient.close();
        }
        
        return null;
    }
    
    public static JSONObject parseXml(InputStream inputStream)
        throws Exception
    {
        JSONObject jsonObject = new JSONObject();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        @SuppressWarnings("unchecked")
        List<Element> elementList = root.elements();
        
        // 遍历所有子节点
        for (Element e : elementList)
        {
            jsonObject.put(e.getName(), e.getText());
        }
        
        // 释放资源
        inputStream.close();
        inputStream = null;
        
        return jsonObject;
    }
    
    public static Map<String, Object> commonHTTP(String method, String url, Map<String, Object> parameters)
    {
        HttpClient httpClient = HttpClients.createDefault();
        try
        {
            
            if ("get".equals(method.toLowerCase()))
            {
                HttpGet httpGet = new HttpGet();
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                
                for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry<String, Object> o = (Map.Entry<String, Object>)it.next();
                    BasicNameValuePair pair = new BasicNameValuePair(o.getKey(), o.getValue().toString()); //
                    nvps.add(pair);
                }
                
                httpGet.setURI(URI.create(url + "?" + URLEncodedUtils.format(nvps, HTTP.UTF_8)));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status == 200)
                {
                    HttpEntity entity = httpResponse.getEntity();
                    
                    JSONReader jsonReader = new JSONReader(new InputStreamReader(entity.getContent(), "utf-8"));
                    JSONObject j = JSON.parseObject(jsonReader.readString());
                    
                    // System.out.println(j.toJSONString());
                    // String response = parserResponse(entity);
                    // System.out.println(response);
                    return j;
                }
            }
            else if ("post".equals(method.toLowerCase()))
            {
                HttpPost httpPost = new HttpPost(url);
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Iterator<Map.Entry<String, Object>> it = parameters.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry<String, Object> o = (Map.Entry<String, Object>)it.next();
                    BasicNameValuePair pair = new BasicNameValuePair(o.getKey(), o.getValue().toString());
                    nvps.add(pair);
                }
                
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                
                HttpResponse httpResponse = httpClient.execute(httpPost);
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status == 200)
                {
                    HttpEntity entity = httpResponse.getEntity();
                    // String response = parserResponse(entity);
                    // System.out.println(response);
                    
                    JSONReader jsonReader = new JSONReader(new InputStreamReader(entity.getContent()));
                    JSONObject j = JSON.parseObject(jsonReader.readString());
                    System.out.println(j.toJSONString());
                    return j;
                }
                
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // 关闭连接 ,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return null;
        
    }
    
    /**
     * 发起RESTFul-POST请求获取返回结果
     * 
     * @param req_url 请求地址
     * @return
     */
    public static String sendRESTFulPost(String req_url, String body, Map<String, String> headers)
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            // 需要请求的restful地址
            URL url = new URL(req_url);
            
            // 忽略ssl
            SslUtils.ignoreSsl();
            
            // 打开restful链接
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            // 提交模式
            conn.setRequestMethod("POST");// POST GET PUT DELETE
            
            // 设置访问提交模式，表单提交
            conn.setRequestProperty("Content-Type", "application/json");
            for (String key : headers.keySet())
            {
                conn.setRequestProperty(key, headers.get(key));
            }
            
            conn.setConnectTimeout(10000);// 连接超时 单位毫秒
            conn.setReadTimeout(2000);// 读取超时 单位毫秒
            
            conn.setDoOutput(true);// 是否输入参数
            conn.connect();
            
            byte[] bypes = body.getBytes();
            conn.getOutputStream().write(bypes);// 输入参数
            
            // 将返回的输入流转换成字符串
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            String str = null;
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            conn.disconnect();
        }
        catch (Exception e)
        {
            log.error(e);
            e.printStackTrace();
        }
        return buffer.toString();
    }
    
}
