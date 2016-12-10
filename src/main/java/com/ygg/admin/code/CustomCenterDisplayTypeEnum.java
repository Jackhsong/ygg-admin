package com.ygg.admin.code;

public enum CustomCenterDisplayTypeEnum
{
    ONE((byte)1, "一行1张"),
    
    TWO((byte)2, "一行2张"),
    
    THREE((byte)3, "一行3张"),
    
    FOUR((byte)4, "一行4张");
    
    private byte code;
    
    private String desc;
    
    private CustomCenterDisplayTypeEnum(byte code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }
    
    public byte getCode()
    {
        return code;
    }
    
    public static String getDescByCode(byte code)
    {
        for (CustomCenterDisplayTypeEnum e : CustomCenterDisplayTypeEnum.values())
        {
            if (e.code == code)
            {
                return e.desc;
            }
        }
        return "";
    }
}
