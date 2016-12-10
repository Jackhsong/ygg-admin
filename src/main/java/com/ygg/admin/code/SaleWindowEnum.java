package com.ygg.admin.code;

public class SaleWindowEnum
{
    /**
     * 特卖类型
     * @author Administrator
     *
     */
    public static enum SALE_TYPE
    {
        SINGLE_PRODUCT(1, "单品"),
        
        ACTIVITIES_COMMON(2, "通用专场"),
        
        WEB_CUSTOM_ACTIVITY(3, "网页自定义活动"),
        
        APP_CUSTOM_ACTIVITY(4, "原生自定义活动");
        
        private final int code;
        
        private final String desc;
        
        private SALE_TYPE(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public static String getDescByCode(int code)
        {
            for (SALE_TYPE e : SALE_TYPE.values())
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
     * 特卖时间类型
     * @author Administrator
     *
     */
    public static enum SALE_TIME_TYPE
    {
        
        /** 1 10点场 */
        SALE_10(1, "早10点"),
        
        /** 2 20点场 */
        SALE_20(2, "晚8点");
        
        private final int code;
        
        private final String desc;
        
        private SALE_TIME_TYPE(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
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
            for (SALE_TIME_TYPE e : SALE_TIME_TYPE.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
    }
    
    public static enum SALE_WINDOW_STATUS
    {
        TO_START(1, "即将开始"),
        
        IN_PROGRESS(2, "进行中"),
        
        FINISHED(3, "已结束");
        
        private final int code;
        
        private final String desc;
        
        private SALE_WINDOW_STATUS(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
    }
}
