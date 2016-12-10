package com.ygg.admin.code;

/**
 * @author lorabit
 * @since 16-4-21
 */
public class ProductBaseEnum
{
    public enum SUbMIT_TYPE
    {
        SUPPLY_PRICE(1, "供货价"), DISCOUNTED_POINT(2, "折扣点"), SELF_PURCHASE_PRICE(3, "自营采购价");
        private SUbMIT_TYPE(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        private int code;
        
        private String desc;
        
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
            for (SUbMIT_TYPE e : SUbMIT_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
}
