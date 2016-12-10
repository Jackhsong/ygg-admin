package com.ygg.admin.util;

public class CommonConstant_BAK
{
    /** 后台链接 */
    public static final String ADMIN_URL = "http://121.40.73.86:3335/yggManager/";
    
    /** 快递100 : 订阅指定的快递单的URL */
    public static final String KD100_SUBSCRIBE = "http://www.kuaidi100.com/poll";
    
    /** 快递100 : 向yangege推送快递单状态变化的回调URL */
    public static final String KD100_CALLBACK = "http://121.40.73.86:3335/yggManager/order/kd100Callback";
    
    /** 快递100 : 向yangege推送快递单状态变化的回调URL2 仅供退款退货物流信息回调 */
    public static final String KD100_REFUND_CALLBACK = "http://121.40.73.86:3335/yggManager/outCall/kd100Callback";
    
    /** 快递100 : 推送消息是计算签名用 salt的前缀，完整的salt = 该前缀 + 订单ID */
    public static final String KD100_SALTPREFIX = "*^nG-8&*&0BN86b3fd5f_3)*(&*(4";
    
    /** 快递100 : 推送服务授权密匙(Key) */
    public static final String KD100_KEY = "BffCiwXx774";
    
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
    
    /**
     * 订单下载目录E:\\orderDownload
     */
    //    public static final String OrderZipDownloadDir = "E:\\orderDownload";
    
    //    public static final String OrderZipDownloadDir = "/alidata/server/adminExportFile/orderDownload";
    
    //图片上传目录 prod
    //    public static final String imageUploadDir = "/alidata/server/adminUploadFile/images";
    
    //图片上传目录 dev
    //    public static final String imageUploadDir = "E:\\adminUploadFile";
    
    /** 平台缓存配置 开发 */
    // public static String defaultCacheConfig = "memcache_ygg_dev";
    
    /** 平台缓存配置 外网测试 */
    //    public static String defaultCacheConfig = "memcache_ygg_test";
    
    /** 平台缓存配置 正式环境 */
    //    public static String defaultCacheConfig = "memcache_ygg_prod";
    
    /**
     * 修改订单状态调用接口 test版本这注释掉
     */
    //    public static String updateStatusUrl = "http://m.gegejia.com/ygg/order/modifynopayorderstatus?orderIds=";
    
    //    public static String updateStatusUrl = "null";
    
    /** 个推 appid */
    public static String GETUI_APPID = "vxUFsZ2uhe7Bw8DqgOvqRA";
    
    /** 个推 appkey */
    public static String GETUI_APPKEY = "ps31GCEY3n6mt3KBIP1LC";
    
    /** 个推 master */
    public static String GETUI_MASTER = "x6iLoqrZB6865AalOWY5UA";
    
    /** 个推 推送os域名 */
    public static String GETUI_IP = "http://sdk.open.api.igexin.com/apiex.htm";
    
    /** 使用阿里云图片还是又拍云图片   ali   or  youpai */
    public static final String imageUseAliOrYoupai = "youpai";
    
    /** 又拍云图片前缀 */
    public static final String youpai_prefix = "!";
    
    /** 阿里云图片前缀 */
    public static final String ali_prefix = "@!";
    
    /**
     * 系统账号66236
     */
    //    public static final String adminAccountId = "66236";
}
