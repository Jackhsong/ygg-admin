package com.ygg.admin.sdk.montnets.common;

public enum MethodTypeEnum
{
    GET("GET请求", 1),
    
    POST("POST请求", 2),
    
    SOAP("SOAP请求", 3);
    
    private String name;
    
    private int value;
    
    private MethodTypeEnum(String name, int value)
    {
        this.name = name;
        this.value = value;
    }
    
    public int getValue()
    {
        return this.value;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public static String getName(int value)
    {
        for (MethodTypeEnum e : MethodTypeEnum.values())
        {
            if (e.value == value)
            {
                return e.name;
            }
        }
        return "";
    }
}
