package com.ygg.admin.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

public class GetPermissionStrUtil
{

    public static void main(String[] args)
            throws Exception
    {
        List<String> classNameList = new ArrayList<String>()
        {
            {
                 add("com.ygg.admin.controller.SellerBlacklistController");
            }
        };

        for (String className : classNameList)
        {
            String controllerName = className.substring(className.lastIndexOf(".") + 1);
            Class clazz = Class.forName(className);
            if (clazz != null)
            {
                Method[] methods = clazz.getDeclaredMethods();
//                System.out.println("控制器" + controllerName + "共有" + methods.length + "个方法");

                StringBuilder sb = new StringBuilder();
                for (Method method : methods)
                {
                    if (method.isAnnotationPresent(RequestMapping.class))
                    {
                        RequestMapping ann = method.getAnnotation(RequestMapping.class);
                        String[] values = ann.value();
                        String mapping = Arrays.toString(values).replaceAll("\\[", "").replaceAll("\\]", "").replaceFirst("/", "");
                        sb.append(controllerName + "," + mapping.split("/")[0] + "," + mapping.split("/")[0] + ";");
                    }
                }
                System.out.println(sb.toString());
            }
        }
    }
}
