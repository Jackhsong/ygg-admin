package com.ygg.admin.code;

/**
 * 优惠券相关枚举
 * @author Administrator
 *
 */
public class CouponEnum
{
    /**
     * 优惠码类型
     * 
     * @author zhangyb
     *
     */
    public enum CouponCodeType
    {
        /** 1 不同账号同一优惠码一次使用 */
        ONE_CODE_ONE_USE(1, "不同账号同一优惠码一次使用", "单次"),
        
        /** 2 不同账号同一优惠码无限次使用 */
        ONE_CODE_MANY_USE(2, "不同账号同一优惠码无限次使用", "无限次");
        
        private final int code;
        
        private final String longDesc;
        
        private final String shortDesc;
        
        private CouponCodeType(int code, String longDesc, String shortDesc)
        {
            this.code = code;
            this.longDesc = longDesc;
            this.shortDesc = shortDesc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public String getLongDesc()
        {
            return longDesc;
        }
        
        public String getShortDesc()
        {
            return shortDesc;
        }
        
        public static CouponCodeType getEnumByCode(int code)
        {
            for (CouponCodeType e : CouponCodeType.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 优惠码类型
     * 
     * @author zhangyb
     *
     */
    public enum CouponCodeChangeType
    {
        /** 1 兑换单张优惠券 */
        SINGLE(1, "兑换单张优惠券"),
        
        /** 2 兑换礼包 */
        LIBAO(2, "兑换礼包");
        
        private final int code;
        
        private final String desc;
        
        private CouponCodeChangeType(int code, String desc)
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
        
        public static CouponCodeChangeType getEnumByCode(int code)
        {
            for (CouponCodeChangeType e : CouponCodeChangeType.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 优惠券来源类型
     * 
     * @author zhangyb
     *         
     */
    public enum CouponAccountSourceType
    {
        /** 1 优惠券发放 */
        COUPON_SEND(1, "优惠券发放"),
        
        /** 2 优惠码兑换 */
        COUPON_CODE_CHANGE(2, "优惠码兑换"),
        
        /** 3 抽奖所得 */
        LOTTERY(3, "抽奖所得"),
        
        /** 4 签到奖励 */
        LOGIN(4, "签到奖励"),
        
        /** 5 小伙伴推荐奖励 */
        RECOMMEND(5, "小伙伴推荐奖励"),
        
        /** 6 分享礼包 */
        GIFT(6, "分享礼包"),
        
        /** 7 玩游戏领取 */
        GAME(7, "玩游戏领取"),
        
        /** 8 通过推广渠道领取 */
        SPREAD_CHANNEL(8, "推广渠道领取"),
        
        /** 9 通过任意门领取 */
        ANY_DOOR(9, "任意门领取"),
        
        /** 10 订单取消返还 */
        ORDER_CANCEL_RETURN(10, "订单取消返还"),
        
        /** 11 红包领取 */
        RED_PACKET(11, "红包领取"),
        
        /** 12 美食迎新会 */
        DELICIOUS_FOOD_PARTY(12, "美食迎新会"),
        
        /** 14 圣诞砸金蛋 */
        CHRISTMAS(14, "圣诞砸金蛋"),
        
        /** 15 跨年领券 */
        NEW_YEAR(15, "跨年领券"),
        
        /** 2016新年红包领券 */
        NEW_YEAR_2016(16, "2016新年红包领券"),
        
        /** 2016新年红包雨领券 */
        RED_PACKET_RAIN(17, " 2016新年红包雨领券"),
        
        /**2016新年100元红包*/
        NEW_YEAR_2016_RED_PACKET(18, "2016新年100元红包"),
        
        /**新年幸运签*/
        NEW_YEAR_LUCKY_LOT(19, "新年幸运签"),
        
        VALENTINES_DAY(20, "情人节活动"),
        
        LANTERN_FESTIVAL(21, "元宵猜灯谜"),
        
        HUA_DENG(22, "元宵挑花灯"),
        
        GODDESS_FESTIVAL(23, "2016女神节"),
        
        ANNIVERSARY_JIGSAW(24, "周年庆拼图");
        
        private final int code;
        
        private final String desc;
        
        private CouponAccountSourceType(int code, String desc)
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
         * 根据序值得到枚举值
         * 
         * @param ordinal
         * @return
         */
        public static CouponAccountSourceType getEnumByCode(int code)
        {
            for (CouponAccountSourceType e : CouponAccountSourceType.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 优惠券使用范围
     *
     */
    public static enum SCOPE_TYPE
    {
        
        /** 1 */
        ALL_PRODUCT(1, "全场通用"),
        
        /** 2 */
        ACTIVITIES_COMMON(2, "通用专场商品"),
        
        /** 3 */
        SPECIFIC_PRODUCT(3, "指定商品"),
        
        /** 4 */
        SECOND_CATEGORY_PRODUCT(4, "二级类目商品"),
        
        /** 5 */
        SELLER_APPOINT_PRODUCT(5, "卖家商品"),
        
        /** 6 */
        SELLER_SENDTYPE_PRODUCT(6, "卖家发货类型商品");
        
        final int code;
        
        final String desc;
        
        private SCOPE_TYPE(int code, String desc)
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
            for (SCOPE_TYPE e : SCOPE_TYPE.values())
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
