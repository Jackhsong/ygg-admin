package com.ygg.admin.code;

/**
 * 自定义布局展示类型枚举
 * @author xiongl
 *
 */
public enum CustomLayoutStyleTypeEnum
{
    /** 0 占位符 */
    NORMAL("占位符"),
    
    /** 1 单张 */
    LAYOUT_STYLE_OF_SINGLE("单张"),
    
    /** 2 两张 */
    LAYOUT_STYLE_OF_DOUBLE("两张"),

    /** 3 四张 */
    LAYOUT_STYLE_OF_FOUR("四张");
    
    private String description;
    
    private CustomLayoutStyleTypeEnum(String description)
    {
        this.description = description;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public static String getDescriptionByOrdinal(int ordinal)
    {
        for (CustomLayoutStyleTypeEnum e : CustomLayoutStyleTypeEnum.values())
        {
            if (ordinal == e.ordinal())
            {
                return e.description;
            }
        }
        return "";
    }
}
