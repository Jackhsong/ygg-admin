package com.ygg.admin.sdk.alipay.util;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.sdk.alipay.config.AlipayConfig;
import com.ygg.admin.sdk.alipay.sign.MD5;
import com.ygg.admin.sdk.alipay.util.httpClient.HttpProtocolHandler;
import com.ygg.admin.sdk.alipay.util.httpClient.HttpRequest;
import com.ygg.admin.sdk.alipay.util.httpClient.HttpResponse;
import com.ygg.admin.sdk.alipay.util.httpClient.HttpResultType;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import java.util.Map;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipaySubmit
{
    private  static Logger logger = Logger.getLogger(AlipaySubmit.class);
    
    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @return 提交表单HTML文本
     */
    public static String executeRequest(Map<String, String> sParaTemp, String strParaFileName, String strFilePath, String gatewayUrl)
            throws Exception
    {
        //待请求参数数组
        Map<String, String> sPara = globalBuildRequestPara(sParaTemp);

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        // 设置编码集
        request.setCharset(AlipayConfig.GLOBAL_INPUT_CHARSET);

        logger.info("支付宝立即退款请求参数："+JSONObject.toJSONString(generatNameValuePair(sPara)));
        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(gatewayUrl + "_input_charset=" + AlipayConfig.GLOBAL_INPUT_CHARSET);
        HttpResponse response = httpProtocolHandler.execute(request, strParaFileName, strFilePath);
        if (response == null)
        {
            return null;
        }
        String strResult = response.getStringResult();
        return strResult;
    }
    
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> globalBuildRequestPara(Map<String, String> sParaTemp)
    {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = globalBuildRequestMysign(sPara);
        
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.GLOBAL_SIGN_TYPE);
        return sPara;
    }
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    public static String globalBuildRequestMysign(Map<String, String> sPara)
    {
        String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if (AlipayConfig.GLOBAL_SIGN_TYPE.equals("MD5"))
        {
            mysign = MD5.sign(prestr, AlipayConfig.GLOBAL_KEY, AlipayConfig.GLOBAL_INPUT_CHARSET);
        }
        return mysign;
    }

    /**
     * MAP类型数组转换成NameValuePair类型
     *
     * @param properties MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties)
    {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet())
        {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
    
}
