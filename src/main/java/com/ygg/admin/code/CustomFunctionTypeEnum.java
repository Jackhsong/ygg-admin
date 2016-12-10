package com.ygg.admin.code;

public enum CustomFunctionTypeEnum
{
    
    GATE(1, "任意门"),
    
    SIGNIN(2, "签到"),
    
    TOP(3, "热卖榜");
    
    private int code;
    
    private String desc;
    
    private CustomFunctionTypeEnum(int code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }
    
    public static String getDescByCode(int code)
    {
        for (CustomFunctionTypeEnum e : CustomFunctionTypeEnum.values())
        {
            if (e.code == code)
            {
                return e.desc;
            }
        }
        return "";
    }
}
