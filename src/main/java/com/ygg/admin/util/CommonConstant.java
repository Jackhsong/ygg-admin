package com.ygg.admin.util;

import com.ygg.admin.config.YggAdminProperties;

public class CommonConstant
{
    /** 后台链接 */
    public static final String ADMIN_URL = "http://121.40.73.86:3335/yggManager/";
    
    /** 快递100 : 订阅指定的快递单的URL */
    public static final String KD100_SUBSCRIBE = "http://www.kuaidi100.com/poll";
    
    /** 快递100 : 推送消息是计算签名用 salt的前缀，完整的salt = 该前缀 + 订单ID */
    public static final String KD100_SALTPREFIX = "*^nG-8&*&0BN86b3fd5f_3)*(&*(4";
    
    /** 快递100 : 推送服务授权密匙(Key) */
    public static final String KD100_KEY = "BffCiwXx774";
    
    /** 快递吧：订阅地址 */
    public static final String KD8_SUBSCRIBE = "http://i.kd8.cc/postids.aspx";
    
    /** 快递吧：测试订阅地址 */
    // public static final String KD8_SUBSCRIBE = "http://api.kd8.cc/posttest.aspx";
    
    /** 快递吧：帐号 */
    public static final String KD8_ACCOUNT = "gegejia";
    
    /** 快递吧：测试帐号 */
    // public static final String KD8_ACCOUNT = "test";
    
    /** 快递吧：授权码 */
    public static final String KD8_SECRET = "b2ade5508a7cff62acbd1ZZ777c8f35d74ed79";
    
    /** 快递吧：测试授权码 */
    // public static final String KD8_SECRET = "kd8cctestsecret";
    
    /** 默认包邮模板ID */
    public static final int defaultFreightTemplate = 1;
    
    /** 平台使用字符编码 */
    public static final String CHARACTER_ENCODING = "UTF-8";
    
    /**
     * 平台机器唯一标识
     */
    public static final String PLATFORM_IDENTITY_CODE = "1";
    
    /**
     * 每日特卖上新事件，单位：二十四计时制
     */
    public static final int SALE_REFRESH_HOUR = 10;
    
    /**
     * 平台签名密钥
     */
    public static final String SIGN_KEY = "yangegegegeyan";
    
    /**
     * 左岸城堡短信内容前缀
     */
    public static final String SMS_PREFIX = "【左岸城堡】";
    
    /** 个推 appid */
    public static String GETUI_APPID = "vxUFsZ2uhe7Bw8DqgOvqRA";
    
    /** 个推 appkey */
    public static String GETUI_APPKEY = "ps31GCEY3n6mt3KBIP1LC";
    
    /** 个推 master */
    public static String GETUI_MASTER = "x6iLoqrZB6865AalOWY5UA";
    
    /** 个推 推送os域名 */
    public static String GETUI_IP = "http://sdk.open.api.igexin.com/apiex.htm";
    
    /** 使用阿里云图片还是又拍云图片 ali or youpai */
    public static final String imageUseAliOrYoupai = "youpai";
    
    /** 又拍云图片前缀 */
    public static final String youpai_prefix = "!";
    
    /** 阿里云图片前缀 */
    public static final String ali_prefix = "@!";
    
    /** excel 限制导出数量 */
    public static final int workbook_num_3w = 30000;
    
    /** excel 限制导出数量 */
    public static final int workbook_num_4w = 40000;
    
    /** excel 限制导出数量 */
    public static final int workbook_num_5w = 50000;
    
    /** excel 限制导出数量 */
    public static final int workbook_num_6w = 60000;
    
    /** excel 限制导出数量 */
    public static final int workbook_num_10w = 100000;
    
    /** 单个workbook 最大记录数 */
    public static final int workbook_max_nums_3w = 30000;
    
    /** 单个workbook 最大记录数 主要用于财务导出 */
    public static final int workbook_max_nums_1w = 10000;
    
    /** 单个workbook 最大记录数 主要用于财务导出 */
    public static final int workbook_max_nums_5k = 5000;
    
    /** 单个workbook 最大记录数 主要用于财务导出 */
    public static final int workbook_max_nums_8k = 8000;
    
    public static final int COMMON_YES = 1;
    
    public static final int COMMON_NO = 0;
    
    /** app订单号后缀 */
    public static final String ORDER_SUFFIX_APP = "1";
    
    /** wap订单后缀 */
    public static final String ORDER_SUFFIX_WAP = "2";
    
    /** 左岸城堡订单后缀 */
    public static final String ORDER_SUFFIX_GROUP = "3";
    
    /**
     * 每日晚场特卖上新事件，单位：二十四计时制
     */
    public static final int SALE_REFRESH_HOUR_NIGHT = 20;
    
    /**
     * 特卖商品wap链接，%d替换成对应的特卖商品Id
     */
    public static final String SALE_PRODUCT_WAP_URL = "http://m.gegejia.com/item-%d.htm";
    
    /**
     * 商城商品wap链接，%d替换成对应的商城商品Id
     */
    public static final String MALL_PRODUCT_WAP_URL = "http://m.gegejia.com/mitem-%d.htm";
    
    /**
     * 商品组合wap链接，%d替换成对应的商品组合Id
     */
    public static final String SALE_WINDOW_WAP_URL = "http://m.gegejia.com/sale-%d.htm";
    
    /**
     * account.channel=999->历史版本
     */
    public static final String HISTORY_VERSION = "历史版本";
    
    /**
     * 微信退款url
     */
    public static final String WECHAT_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    
    /**
     * 订单发货超时每单罚款金额
     */
    public static final int ORDER_SEND_TIME_OUT_AMERCE = 10;
    
    /**
     * 订单物流超时每单罚款金额
     */
    public static final int ORDER_LOGISTICS_TIME_OUT_AMERCE = 50;
    
    public static final String QQBS_WEIXIN_TOKEN_URL =
        YggAdminProperties.getInstance().getPropertie("qqbs_weixin_token_url") == null ? "http://127.0.0.1:9080/ygg-hqbs/wechatToken" : YggAdminProperties.getInstance()
            .getPropertie("qqbs_weixin_token_url");
    
    public static final String QQBS_WEIXIN_CREATE_QRCODE_URL =
        YggAdminProperties.getInstance().getPropertie("qqbs_weixin_create_qrcode_url") == null ? "https://api.weixin.qq.com/cgi-bin/qrcode/create"
            : YggAdminProperties.getInstance().getPropertie("qqbs_weixin_create_qrcode_url");
    
    public static final String QQBS_WEIXIN_SHOW_QRCODE_URL =
        YggAdminProperties.getInstance().getPropertie("qqbs_weixin_show_qrcode_url") == null ? "https://mp.weixin.qq.com/cgi-bin/showqrcode" : YggAdminProperties.getInstance()
            .getPropertie("qqbs_weixin_show_qrcode_url");
}
