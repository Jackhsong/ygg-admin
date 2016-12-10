package com.ygg.admin.code;

/**
 * 页面模块类型枚举
 *
 */
public enum PageModelTypeEnum
{
    NORMAL(""),

    FULL_BANNER("通栏Banner图片"),

    ROLL_PRODUCT("滚动商品"),

    CUSTOM("自定义板块"),

    TWO_PRODUCT_LIST("两栏商品");

    final String description;

    PageModelTypeEnum(String description)
    {
        this.description = description;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    /**
     * 根据排序值查询描述
     * 
     * @param ordinal
     * @return
     */
    public static String getDescriptionByOrdinal(int ordinal)
    {
        for (PageModelTypeEnum e : PageModelTypeEnum.values())
        {
            if (ordinal == e.ordinal())
            {
                return e.description;
            }
        }
        return "";
    }
}
