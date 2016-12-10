package com.ygg.admin.util;

import com.ygg.admin.entity.SellerEntity;

import javax.servlet.http.HttpSession;

/**
 * 当前商家后台用户 session util
 */
public class SessionUtil
{
    /** 当前登陆商家ID */
    public static final String CURRENT_SELLER_SESSION_KEY = "yggAdmin_current_seller_id_key";

    /** 商家登陆信息 cookie 保留时长 */
    public static final int SELLER_INFO_COOKIES_KEEP_TIME = 60 * 60 * 24 * 30;
    
    /** 商家登陆 */
    public static void addSellerAdminUserToSession(HttpSession session, SellerEntity se)
    {
        if (null == se || se.getId() <= 0)
        {
            return;
        }
        session.setAttribute(CURRENT_SELLER_SESSION_KEY, se);
    }

    /** 获取当前登陆商家 */
    public static SellerEntity getCurrentSellerAdminUser(HttpSession session)
    {
        if (session == null)
        {
            return null;
        }
        return session.getAttribute(CURRENT_SELLER_SESSION_KEY) == null ? null : (SellerEntity)session.getAttribute(CURRENT_SELLER_SESSION_KEY);
    }
}
