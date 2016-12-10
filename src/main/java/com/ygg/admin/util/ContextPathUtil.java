package com.ygg.admin.util;

import javax.servlet.http.HttpServletRequest;

public class ContextPathUtil
{
    
    public static String yggcontextPath = "";
    
    public static String getBasePath(HttpServletRequest request)
    {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path; // +"/";
        // return basePath ;
        if (yggcontextPath == null || yggcontextPath.equals(""))
            return basePath; // http://localhost:8080/ygg
        return yggcontextPath;
    }
    
    public static String getContextPath1(HttpServletRequest request)
    {
        String path = request.getContextPath();
        return path + "/"; // /ygg/
    }
    
    public static String getCookiePath(HttpServletRequest request)
    {
        return "/";
    }
}
