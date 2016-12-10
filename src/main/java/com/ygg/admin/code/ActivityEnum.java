package com.ygg.admin.code;

/**
 * 活动相关枚举类
 * @author Administrator
 *
 */
public class ActivityEnum
{
    public static enum SPECIAL_ACTIVITY_GROUP_TYPE
    {
        PRODUCT(1, "商品"),
        
        GROUP(2, "组合"),
        
        CUSTOM_ACTIVITY(3, "自定义活动"),
        
        NONE(4, "点击不跳转");
        
        private int value;
        
        private String desc;
        
        private SPECIAL_ACTIVITY_GROUP_TYPE(int value, String desc)
        {
            this.value = value;
            this.desc = desc;
        }
        
        public int getValue()
        {
            return value;
        }
        
        public static String getDescByValue(int value)
        {
            for (SPECIAL_ACTIVITY_GROUP_TYPE e : SPECIAL_ACTIVITY_GROUP_TYPE.values())
            {
                if (e.value == value)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    /**
     * 满减活动梯度枚举
     * @author Administrator
     *
     */
    public enum MANJIAN_GRADIENT_TYPE
    {
        ONE((byte)1, "一档"),
        
        TWO((byte)2, "二档"),
        
        THREE((byte)3, "三档"),
        
        FOUR((byte)4, "四档");
        
        private byte code;
        
        private String desc;
        
        private MANJIAN_GRADIENT_TYPE(byte code, String desc)
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
            for (MANJIAN_GRADIENT_TYPE e : MANJIAN_GRADIENT_TYPE.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    /**
     * @author Administrator
     *
     * 满减活动关联类型枚举
     */
    public enum MANJIAN_RELATION_TYPE
    {
        ALL((byte)1, "全场"),
        
        GROUP((byte)2, "组合专场"),
        
        CUSTOM_ACTIVITY((byte)3, "自定义活动"),
        
        CUSTOM_PAGE((byte)4, "自定义页面");
        
        private byte code;
        
        private String desc;
        
        MANJIAN_RELATION_TYPE(byte code, String desc)
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
            for (MANJIAN_RELATION_TYPE e : MANJIAN_RELATION_TYPE.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    /**
     * @author Administrator
     * N元仍选活动类型
     */
    public enum OPTION_PART_TYPE
    {
        ALL((byte)1, "全场"),
        
        GROUP((byte)2, "组合专场"),
        
        CUSTOM_ACTIVITY((byte)3, "自定义活动"),
        
        CUSTOM_PAGE((byte)4, "自定义页面");
        
        private byte code;
        
        private String desc;
        
        OPTION_PART_TYPE(byte code, String desc)
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
            for (OPTION_PART_TYPE e : OPTION_PART_TYPE.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    /**
     * 通用专场组合类型枚举
     */
    public enum ACTIVITIES_COMMON_TYPE
    {
        SALE(1, "特卖商品组合"),
        
        MALL(2, "商城商品组合");
        
        final private int code;
        
        final private String desc;
        
        private ACTIVITIES_COMMON_TYPE(int code, String desc)
        {
            this.desc = desc;
            this.code = code;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (ACTIVITIES_COMMON_TYPE e : ACTIVITIES_COMMON_TYPE.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
}
