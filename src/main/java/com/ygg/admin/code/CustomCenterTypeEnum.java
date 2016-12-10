package com.ygg.admin.code;

public enum CustomCenterTypeEnum
{
    SINGLE_SALE_PRODUCT((byte)1, "单品"),
    
    GROUP((byte)2, "组合"),
    
    CUSTOM_ACTIVITY((byte)3, "自定义活动"),
    
    SINGLE_MALL_PRODUCT((byte)4, "商城商品"),
    
    CUSTOM_PAGE((byte)5, "自定义拼装页面");
    
    private byte code;
    
    private String desc;
    
    private CustomCenterTypeEnum(byte code, String desc)
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
        for (CustomCenterTypeEnum e : CustomCenterTypeEnum.values())
        {
            if (e.code == code)
            {
                return e.desc;
            }
        }
        return "";
    }
}
