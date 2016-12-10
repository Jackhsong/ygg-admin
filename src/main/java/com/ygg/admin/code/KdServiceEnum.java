package com.ygg.admin.code;

/**
 * 快递服务商类型
 * @author Administrator
 *
 */
public enum KdServiceEnum
{
    NONE("都不支持", 0),
    
    KD100("快递100", 1),
    
    KD8("快递吧", 2),
    
    BOTH("都支持", 3);
    
    private String name;
    
    private int value;
    
    private KdServiceEnum(String name, int value)
    {
        this.name = name;
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public String getName()
    {
        return name;
    }
    
    public static String getName(int value)
    {
        for (KdServiceEnum e : KdServiceEnum.values())
        {
            if (e.value == value)
            {
                return e.name;
            }
        }
        return "";
    }
}
