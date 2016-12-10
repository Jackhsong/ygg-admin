package com.ygg.admin.code;

public class ProductEnum
{
    public enum PRODUCT_TYPE
    {
        
        /** 1 特卖商品 */
        SALE(1, "特卖商品"),
        
        /** 2 商城商品 */
        MALL(2, "商城商品"),
        
        TEAM(3, "左岸城堡商品");
        
        private final int code;
        
        private final String desc;
        
        private PRODUCT_TYPE(int code, String desc)
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
        
        public static String getDescByCode(int code)
        {
            for (PRODUCT_TYPE e : PRODUCT_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    /**
     * 商品活动状态
     * 
     * @author zhangyb
     *         
     */
    public enum PRODUCT_ACTIVITY_STATUS
    {
        
        /** 1 正常 */
        NORMAL(1, "正常"),
        
        /** 2 团购 */
        GROUP_BUY(2, "团购"),
        
        /** 3 格格福利 */
        GEGE_WELFARE(3, "格格福利"),
        
        NEWBIE(4, "新人专享");
        
        private final int code;
        
        private final String desc;
        
        private PRODUCT_ACTIVITY_STATUS(int code, String desc)
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
        
        /**
         * 根据Code得到枚举值
         * 
         * @param code
         * @return
         */
        public static PRODUCT_ACTIVITY_STATUS getEnumByCode(int code)
        {
            for (PRODUCT_ACTIVITY_STATUS e : PRODUCT_ACTIVITY_STATUS.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    public enum PRODUCT_COMMENT_TYPE
    {
        /** 1 差评 */
        BAD(1, "差评"),
        
        /** 2 中评 */
        MIDDLE(2, "中评"),
        
        /** 3 好评 */
        GOOD(3, "好评");
        
        private final int code;
        
        private final String desc;
        
        private PRODUCT_COMMENT_TYPE(int code, String desc)
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
            for (PRODUCT_COMMENT_TYPE e : PRODUCT_COMMENT_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    public enum PRODUCT_SUBMIT_TYPE
    {
        /** 1 正常结算 */
        MONEY_SUBMIT(1, "正常结算"),
        
        /** 2 扣点结算 */
        PERCENT_SUBMIT(2, "扣点结算"),
        
        /** 3 自营采购价 */
        SELF_PURCHASE_PRICE(3, "自营采购价");
        
        private final int code;
        
        private final String desc;
        
        private PRODUCT_SUBMIT_TYPE(int code, String desc)
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
            for (PRODUCT_SUBMIT_TYPE e : PRODUCT_SUBMIT_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    public enum QUALITY_PROMISE_TYPE
    {
        IMPORT(1, "进口商品"),
        
        HOMEMADE(2, "国产商品");
        
        private final int code;
        
        private final String desc;
        
        private QUALITY_PROMISE_TYPE(int code, String desc)
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
            for (QUALITY_PROMISE_TYPE e : QUALITY_PROMISE_TYPE.values())
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
