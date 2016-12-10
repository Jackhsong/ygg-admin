package com.ygg.admin.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

public class SpringBeanUtil
{
    
    private static WebApplicationContext ctx = null; //
    
    public static void initServletContext(ServletContext sc)
    {
        ctx = WebApplicationContextUtils.getWebApplicationContext(sc);
    }

    public static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
    
    /**
     * 在其它类中可通过 如来得到spring中的bean CacheService cs = SpringBeanUtil.getBean(CacheService.class) ;
     * 
     * @param claz
     * @return
     */
    public static <T> T getBean(Class<T> claz)
    {
        if (ctx == null)
            return null;
        return ctx.getBean(claz);
    }
    
    public static Object getBean(String beanName)
    {
        if (ctx == null)
            return null;
        return ctx.getBean(beanName);
    }
    
    public static <T> T getBean(String beanName, Class<T> claz)
    {
        if (ctx == null)
            return null;
        return ctx.getBean(beanName, claz);
    }
    
}
