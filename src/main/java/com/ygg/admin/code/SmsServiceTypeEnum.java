package com.ygg.admin.code;

/**
 * 短信服务商类型
 * @author xiongl
 *
 */
public enum SmsServiceTypeEnum
{
    YIMEI("亿美短信", 1),
    
    MENGWANG("梦网短信", 2);
    
    private String name;
    
    private int value;
    
    private SmsServiceTypeEnum(String name, int value)
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
        for (SmsServiceTypeEnum e : SmsServiceTypeEnum.values())
        {
            if (e.value == value)
            {
                return e.getName();
            }
        }
        return "";
    }
}
