package com.ygg.admin.util;

import org.joda.time.DateTime;

public class CacheConstant
{
    public static String PREFIX = "admin_prod_";
    
    /**
     * 海外购周末短信提醒发送成功缓存
     */
    public static final String ADMIN_SENDOVERSEASMSJOB_SUCCESSNUMBERLIST = PREFIX + "SendOverSeaSMSJob_successNumberList";
    
    /**
     * 海外购周末短信提醒发送失败缓存
     */
    public static final String ADMIN_SENDOVERSEASMSJOB_FAIRNUMBERLIST = PREFIX + "SendOverSeaSMSJob_fairNumberList";
    
    /**
     * 客服积分操作来源临时记录
     */
    public static final String ADMIN_ACCOUNTSERVICE_UPDATEACCOUNTINTEGRAL = PREFIX + "AccountServiceImpl_updateAccountIntegral_source";
    
    /**
     * 后台登陆用户锁定KEY
     */
    public static final String ADMIN_ADMINREALM_LOCKED_USER = PREFIX + "Adminrealm_doGetAuthenticationInfo_locked_user";
    
    /**
     * 后台登陆用户密码错缓存KEY
     */
    public static final String ADMIN_ADMINREALM_ERROR_PASSWORD_USER = PREFIX + "Adminrealm_doGetAuthenticationInfo_error_password_user";
    
    /**
     * 后台用户外网登陆缓存key
     */
    public static final String ADMIN_USER_LOGIN_FROM_OUTER = PREFIX + "LoginInterceptor_postHandle_user_login_from_outer";
    
    /**
     * 后台用户外网登陆短信验证缓存key
     */
    public static final String ADMIN_USER_LOGIN_FROM_OUTER_SMSCODE = PREFIX + "LoginInterceptor_postHandle_user_login_from_outer_smscode";
    
    /**
     * 缓存IP白名单
     */
    public static final String ADMIN_USER_LOGIN_WHITE_IP_LIST = PREFIX + "LoginInterceptor_preHandle_user_login_white_ip_list";
    
    /**
     * 缓存URL白名单
     */
    public static final String ADMIN_USER_LOGIN_WHITE_URL_LIST = PREFIX + "LoginInterceptor_preHandle_user_login_white_url_list";
    
    /**
     * 外网登陆发送短信次数缓存
     */
    public static final String ADMIN_USER_LOGIN_SEND_SMS_COUNT = PREFIX + "LoginInterceptor_preHandle_user_login_send_sms_count_" + DateTime.now().toString("yyyyMMdd") + "_";
    
    /**
     * 后台用户外网登陆缓存时间，6小时
     */
    public static final int ADMIN_USER_LOGIN_FROM_OUTER_CACHE_TIME_HOUR_OF_6 = 6 * 60 * 60;
    
    /**
     * 后台用户外网登陆短信验证有效时间，10分钟
     */
    public static final int ADMIN_USER_LOGIN_FROM_OUTER_SMS_DEADLINE_CACHE_TIME_MINUTE_OF_10 = 10 * 60;
    
    /**
     * IP/URL白名单缓存30天
     */
    public static final int ADMIN_USER_LOGIN_FROM_OUTER_WHITE_LIST_CACHE_TIME_DAY_OF_30 = 30 * 24 * 60 * 60;
    
    public static final int ADMIN_USER_LOGIN_SEND_SMS_COUNT_CACHE_TIME_DAY_OF_1 = 24 * 60 * 60;
    
    public static final int ADMIN_USER_LOGIN_SEND_SMS_COUNT_CACHE_TIME_DAY_OF_3 = 3 * 24 * 60 * 60;
    
    /**
     * 缓存一小时
     */
    public static final int CACHE_HOUR_1 = 60 * 60;
    
    /**
     * 已签收订单号
     */
    public static final String ADMIN_RECEIVE_ORDER_ID_LIST = PREFIX + "OrderServiceImpl_kd100CallBack_received_order_ids";
    
    /**
     * 用户权限缓存版本
     */
    public static final String ADMIN_PLATFORM_USER_PERMISSION_VSERSION = PREFIX + "ShiroAdminServiceImpl_findPermissions_permissionSet_VERSION_";
    
    /**
     * 用户权限缓存
     */
    public static final String ADMIN_PLATFORM_USER_PERMISSION = PREFIX + "ShiroAdminServiceImpl_findPermissions_permissionSet_";
    
    /**
     * 特殊请求，用户访问请求次数记录
     */
    public static final String ADMIN_RESTRICT_ACCESS_USER_NUM = PREFIX + "RestrictAccessInterceptor_user_num";
    
    /**
     * 特殊请求，用户最近访问请求时间
     */
    public static final String ADMIN_RESTRICT_ACCESS_USER_LAST_TIME = PREFIX + "RestrictAccessInterceptor_user_last_time";
    
}
