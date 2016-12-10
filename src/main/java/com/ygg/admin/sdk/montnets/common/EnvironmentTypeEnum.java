package com.ygg.admin.sdk.montnets.common;

/**
 * 帐号类型枚举
 * @author xiongl
 *
 */
public enum EnvironmentTypeEnum
{
    
    PROD("生产帐号", 1),
    
    SERVICE("服务帐号", 2);
    
    private String name;
    
    private int value;
    
    private EnvironmentTypeEnum(String name, int value)
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
        for (EnvironmentTypeEnum e : EnvironmentTypeEnum.values())
        {
            if (e.value == value)
            {
                return e.name;
            }
        }
        return "";
    }
}
