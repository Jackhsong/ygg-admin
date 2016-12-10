package com.ygg.admin.code;

public enum FirstCategoryWindowRelationTypeEnum
{
    TYPE_OF_SALE_PRODUCT(1, "特卖单品"),
    
    TYPE_OF_GROUP_SALE(2, "组合特卖"),
    
    TYPE_OF_CUSTOM_ACTIVITY(3, "自定义活动"),
    
    TYPE_OF_MALL_PRODUCT(4, "商城单品");
    
    private int code;
    
    private String value;
    
    private FirstCategoryWindowRelationTypeEnum(int code, String value)
    {
        this.code = code;
        this.value = value;
    }
    
    public int getCode()
    {
        return code;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public static String getValue(int code)
    {
        for (FirstCategoryWindowRelationTypeEnum e : FirstCategoryWindowRelationTypeEnum.values())
        {
            if (e.code == code)
            {
                return e.value;
            }
        }
        return null;
    }
}
