package com.ygg.admin.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil
{
    private static final String SELLER_AUTH_KEY = "6FE97759AA27A0C9";
    
    /**
     * 最简单http post请求
     * @param url
     * @param nvps
     * @return
     */
    public static String reqPost(String url, List<NameValuePair> nvps) {
        return reqPost(url, nvps, false);
    }
    
    /**
     * 商家后台使用
     * @param url
     * @param nvps
     * @param useKey 是否验证
     * @return
     */
    public static String reqPost(String url, List<NameValuePair> nvps, boolean useKey) {
        CloseableHttpResponse response = null;
        try {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            // 这个参数可以忽略不计，这是给商家后台验证的，应为有多个地方调用都需要使用改参数，所以写死在这里
            // 如果其他地方需要验证，可以做调整
            if(useKey)
                nvps.add(new BasicNameValuePair("key", SELLER_AUTH_KEY));
            httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httppost);

            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String body = EntityUtils.toString(entity, "UTF-8");
                    if(body.contains("\"status\":1")) {
                        EntityUtils.consume(entity);
                        return body;
                    } else {
                        throw new RuntimeException("同步数据失败");
                    }
                } else {
                    throw new RuntimeException("同步数据失败");
                }
            } else {
                throw new RuntimeException("同步数据失败");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
