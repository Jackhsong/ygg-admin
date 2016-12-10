package com.ygg.admin.code;

public class BrandEnum
{
    public enum RETURN_PROPORTION_TYPE
    {
        
        /** 1 返25% */
        PROPORTION_25(1, "返25%"),
        
        /** 2 返100% */
        PROPORTION_100(2, "返100%");
        
        private final int code;
        
        private final String desc;
        
        private RETURN_PROPORTION_TYPE(int code, String desc)
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
        
        public static RETURN_PROPORTION_TYPE getEnumByCode(int code)
        {
            for (RETURN_PROPORTION_TYPE e : RETURN_PROPORTION_TYPE.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    public enum ACTIVITY_TYPE
    {
        ACTIVITIESCOMMON(1, "组合"),
        
        ACTIVITIESCUSTOM(2, "自定义活动"),
        
        PAGE(3, "原生自定义活动");
        
        private final int code;
        
        private final String desc;
        
        private ACTIVITY_TYPE(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
        public int getCode()
        {
            return code;
        }
    }
    
}
