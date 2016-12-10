package com.ygg.admin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil
{
    /**
     * 获取cookie
     * 
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name)
    {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie c : cookies)
        {
            if (name == c.getName())
                return c;
        }
        return null;
    }
    
    /**
     * 删除可疑cookie
     * 
     * @param request
     * @param response
     */
    public static void delCookies(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies)
        {
            
            if (c.getName().startsWith("_a") || c.getName().startsWith("_j") || c.getName().startsWith("sgsa_") || c.getName().startsWith("__q")
                || c.getName().startsWith("_q"))
            {
                
                Cookie tmp = new Cookie(c.getName(), null);
                tmp.setMaxAge(0);
                tmp.setPath("/");
                tmp.setDomain(".lizi.com");
                response.addCookie(tmp);
            }
        }
    }
    
    /**
     * 根据名称获取cookie值
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name)
    {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie c : cookies)
        {
            if (name.equals(c.getName()))
                return c.getValue();
        }
        return null;
    }
    
    /**
     * 设置cookie
     * 
     * @param response
     * @param name
     * @param value
     * @param maxAge
     * @return
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String domain, String path)
    {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath(path);
        response.addCookie(cookie);
    }
}
