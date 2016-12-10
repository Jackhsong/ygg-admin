package com.ygg.admin.code;

/**
 * @author lorabit
 * @since 16-5-18
 */
public class FortuneWheelPrizeEnum {

    public static enum TYPE
    {
        XIEXIE(0, "谢谢惠顾"),

        FIXED_JIFEN(1, "固定积分"),

        RANDOM_JIFEN(2, "随机积分"),

        COUPON(3, "优惠券");

        private final int code;

        private final String desc;

        private TYPE(int code, String desc)
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
            for (TYPE e : TYPE.values())
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
