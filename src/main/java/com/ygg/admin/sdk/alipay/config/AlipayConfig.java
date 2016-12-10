package com.ygg.admin.sdk.alipay.config;


/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

import com.ygg.admin.config.YggAdminProperties;

public class AlipayConfig
{
    
    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String partner = YggAdminProperties.getInstance().getPropertie("ali_partner");
    
    // 交易安全检验码，由数字和字母组成的32位字符串
    // 如果签名方式设置为“MD5”时，请设置该参数
    public static String key = "2wznbo1ghm8sxa1ux5w0xyvoi3hasfzq";
    
    // 商户的私钥 pkcs8编码过的
    // 如果签名方式设置为“0001”时，请设置该参数
    // public static String private_key =
    // "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ4or2q98qfMC6eytZQPePQiVizbZy9YAbRaLX10uLS8lFhYSIadOQbHnHyOE2DlpIiy0Qrw15PEPyEcE98fjh39dRin72WLVxi1rGEd3C82SnB/pZ/3Sf/QECX0HDX1l2xqpHURgosYNgiqvg6VxwiVN5kDLf9KAAR932+smrl5AgMBAAECgYASqqyhhY+5PDz36wDBW94julW/b5nLHbD9z1LlJrySIFvF3Y2Zzb6oTTDsBnzFB/WWaLyBdRGMkDH/P2gdAoVsVy4LJaH6oYC0XPNv5w/j213gdDhVV9M2L/jI3PgS6XXXEQYysebLs60D8tpUfeqPEDdSamglLkYV+qUaOAnffQJBANIP52KwNOb8qzrqH4Z5qDRePetvjslz+3FP4Lb4Kty5RnvWuYyK3Lg5nn4IRmdujr6N2BUKMJWI3aEx0bsQHUsCQQDAvwnDozLG/nUaY5KPvxm3CNTyPq/Fdwe9aE5QvGO+ltdd3boxN7Pac6PdqrdknzQUNaF/agS0SoeUD+rjRh3LAkBmki945/OdCqmNxf6IymTQ1WfEy59cTQ7tQITIsxsy1iFNnQbTUob+SdxTTDUckkaPU3G8rFIW4lLeQBZaEj9LAkBPIyDDLpJ+maNC/ncChhfsyreD7rEEhmarIdRl2bOqYQx0AcGcy54Qp0I0lQ9PRP6A/bQdOE5OJ/uakx6ByhwZAkAjL8ckXtaxJoV5M8lCKzv5ekjs44m9BiKo+u2m3dN05Jf8+3Mic4StkSk5VQH2egrw1ydNxMrwfCc0eUGEPSYZ";
    public static String private_key =
        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALL/w0lvwnBNk5t6ppG8LLze/Pnym24SvnQjxmWdCPaIVlZMBCGVXwlkymPXnplKWqCUKXisVxBXh3kpIpOycjrMGHBEFbHXjC9ffKqjdDgRyg5/liEsF+5FeBgdXcS2kkSMKCdRxeInwGZ+4iCa5CGvykXgZq/DOJRmOsqGEjrRAgMBAAECgYBBARDygluiXQnV1NzY3/V/QZutSaWYe8Yedz99d2Hh5fE0tbZqW8x/pNK7H3rBQHdZCXVmFXAoxyCzp3SfHpbMl65rOi0rTIjwMF4vS5/sG/cBi2QVO5rvTOn4khEWlkFl2pRgAs+VT7QOGFTSup99UbsCcXctOFHAXr3TyCt4gQJBAOTZlr+iiT8PLxD8ePhjs9wiJGPjFzX+V4l8NJxYQN2Z6uSOy17Wbf7fOBYjjCe3L/zOM8xWKbLI4GpKeJA01TUCQQDIPCactjgQ/9VEc0pCWX10soaFpyhBIlWui+DLAc7N9LEgC7E70w5InhXsdM0/x5POFkVm/+4axgEUe4XIyE6tAkEAv1fe32P69Sqw47b1Plm+rLQvUQUzBwYeEuoy4vY3ZhGngGqUSDtpxMzGjFw0d5CHIw0V6iewvbePS8/wdkJ2cQJAVSBtcfZuA5wgOQeirG6LTY+QbUXRcU99icVh/ix4lUrP+sW9xadGzUyYGXzWvaTFi7ogK1fnDG7diq2xBNlTyQJBAK9jgcrJE+XXxBdnshOO599rbv7XB+xGZj/hDgFR73zpVOVPNLVCk5AX59sONVvTExtUTBHIiYCA1+oUslnli9c=";
    
    // 支付宝的公钥
    // 如果签名方式设置为“0001”时，请设置该参数
    public static String ali_public_key =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTlnhc1aOMZbQRN6TIXZ+olihmV3LmSyDYoPljH8/8xdgR3GEH8F/L0oAmoQXi6zNsP10GqHOX5UvwFppF62rcEDMsYtHPIIj762/oemmw/ppvsA/5GNakU8bQb+LogQK/kggRJtgceAhnsU+vRJbryG4qNCISjO7VNk+NvytzewIDAQAB";
    
    // MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTlnhc1aOMZbQRN6TIXZ+olihmV3LmSyDYoPlj
    // H8/8xdgR3GEH8F/L0oAmoQXi6zNsP10GqHOX5UvwFppF62rcEDMsYtHPIIj762/oemmw/ppvsA/5
    // GNakU8bQb+LogQK/kggRJtgceAhnsU+vRJbryG4qNCISjO7VNk+NvytzewIDAQAB
    // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    
    // 调试用，创建TXT日志文件夹路径
    public static String log_path = "D:\\";
    
    // 字符编码格式 目前支持 utf-8
    public static String INPUT_CHARSET = "UTF-8";
    
    // 签名方式，选择项：0001(RSA)、MD5
    public static String SIGN_TYPE = "0001";
    // 无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA

    /***********************************国际支付宝信息***********************************************************/

    // 国际支付宝 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static final String GLOBAL_PARTNER = "2088021646713176";


    // 分账账号
    public static final String GLOBAL_SPLIT_PARTNER = "2088911825995616";

    // 如果签名方式设置为MD5时，请设置该参数
    public static final String GLOBAL_KEY = "zluqafeljw4d4zxru78h5d5rw32z1xi4";

    // 签名方式 不需修改
    public static final String GLOBAL_SIGN_TYPE = "MD5";

    public static final String GLOBAL_INPUT_CHARSET = "UTF-8";

    public static final String GLOBAL_GATE_URL = "https://mapi.alipay.com/gateway.do?";
}
