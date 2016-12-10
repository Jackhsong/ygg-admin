package com.ygg.admin.util;

import java.util.List;
import java.util.Map;

/**
 * 个人总结的常用的utils
 * 
 * @author zhangyb
 *
 */
public class FavoriteUtil
{
    
    /**
     * 判断参数是否有效
     * 
     * @param o
     * @return
     * @throws Exception
     */
    public static Boolean isUseful(Object o)
        throws Exception
    {
        if (o == null)
        {
            return false;
        }
        if (o instanceof Byte)
        {
            Byte b = (Byte)o;
            if (b.byteValue() == 0)
            {
                return false;
            }
        }
        else if (o instanceof Short)
        {
            Short s = (Short)o;
            if (s.shortValue() == 0)
            {
                return false;
            }
        }
        else if (o instanceof Integer)
        {
            Integer b = (Integer)o;
            if (b.intValue() == 0)
            {
                return false;
            }
        }
        else if (o instanceof Long)
        {
            Long b = (Long)o;
            if (b.longValue() == 0)
            {
                return false;
            }
        }
        else if (o instanceof Float)
        {
            Float b = (Float)o;
            if (b.floatValue() == 0)
            {
                return false;
            }
        }
        else if (o instanceof Double)
        {
            Double b = (Double)o;
            if (b.doubleValue() == 0)
            {
                return false;
            }
        }
        else if (o instanceof Character)
        {
            Character c = (Character)o;
            if ('\0' == c)
            {
                return false;
            }
        }
        else if (o instanceof String)
        {
            String b = (String)o;
            if ("".equals(b))
            {
                return false;
            }
        }
        else if (o instanceof Map)
        {
            Map m = (Map)o;
            if (m.keySet().size() == 0)
            {
                return false;
            }
        }
        else if (o instanceof List)
        {
            List l = (List)o;
            if (l.size() == 0)
            {
                return false;
            }
        }
        return true;
    }
    
}
