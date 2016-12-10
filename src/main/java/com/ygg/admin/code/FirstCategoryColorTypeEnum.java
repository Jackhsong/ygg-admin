package com.ygg.admin.code;

public enum FirstCategoryColorTypeEnum
{
    COLOR_OF_LIGHT_YELLOW("浅黄色", "0xffa200"),
    
    COLOR_OF_ORANGE("橘黄色", "0xff6d01"),
    
    COLOR_OF_RED("红色", "0xfb3c32"),
    
    COLOR_OF_DEEP_RED("大红色", "0xef391b"),
    
    COLOR_OF_BROWN("咖啡色", "0x9a3f3f"),
    
    COLOR_OF_DEEP_BROWN("深咖啡色", "0x6b2121"),
    
    COLOR_OF_PURPLE("紫色", "0xa275b3"),
    
    COLOR_OF_YELLOW_GREEN("黄色绿色", "0x96ae35"),
    
    COLOR_OF_LAWNGREEN("草绿色", "0x709300"),
    
    COLOR_OF_LIGHT_BLUE("浅蓝色", "0x289fea"),
    
    COLOR_OF_BLUE_GREEN("蓝绿色", "0x23aac6");
    
    private String name;
    
    private String value;
    
    private FirstCategoryColorTypeEnum(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public String getName()
    {
        return name;
    }
    
    public static String getName(String value)
    {
        for (FirstCategoryColorTypeEnum color : FirstCategoryColorTypeEnum.values())
        {
            if (value.equals(color.getValue()))
            {
                return color.getName();
            }
        }
        return null;
    }
}
