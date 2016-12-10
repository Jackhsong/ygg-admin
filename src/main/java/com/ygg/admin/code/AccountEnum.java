package com.ygg.admin.code;

/**
 * 用户相关枚举
 *
 */
public class AccountEnum
{
    
    /**
     * 帐号类型枚举
     *
     */
    public static enum ACCOUNT_TYPE
    {
        MOBILE(1, "手机用户"),
        
        QQ(2, "QQ用户"),
        
        WEIXIN(3, "微信用户"),
        
        SINA(4, "新浪用户"),
        
        ALIPAY(5, "支付宝用户"),
        
        GGT_WEIXIN(6, "左岸城堡微信用户"),
        
        GGT_APP(7, "左岸城堡app用户"),
        
        QUAN_QIU_BU_SHOU(8, "左岸城堡"),
        
        YAN_WANG(9, "燕网");
        
        private final int code;
        
        private final String desc;
        
        private ACCOUNT_TYPE(int code, String desc)
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
            for (ACCOUNT_TYPE e : ACCOUNT_TYPE.values())
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
     * 返积分类型；1：首次下单，2：重复下单
     *
     */
    public enum RECOMMEND_RETURNPOINT_TYPE
    {
        
        FIRST_SHOPPING(1, "首次下单"),
        
        REPEAT_SHOPPING(2, "重复下单"),
        
        PERPETUAL_SHOPPING(3, "合伙人永久收益");
        
        final int code;
        
        final String desc;
        
        RECOMMEND_RETURNPOINT_TYPE(int code, String desc)
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
         * 根据排序值查询描述
         * 
         * @param code
         * @return
         */
        public static String getDescByCode(int code)
        {
            for (RECOMMEND_RETURNPOINT_TYPE e : RECOMMEND_RETURNPOINT_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        /**
         * 根据排序值获取枚举类型
         * 
         * @param code
         * @return
         */
        public static RECOMMEND_RETURNPOINT_TYPE getEnumByCode(int code)
        {
            for (RECOMMEND_RETURNPOINT_TYPE e : RECOMMEND_RETURNPOINT_TYPE.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 积分变动类型枚举
     *
     */
    public static enum INTEGRAL_OPERATION_TYPE
    {
        /** 1 购物返积分 */
        SHOPPING_GIFT(1, "购物返积分"),
        
        /** 2 系统调整 */
        ADMIN_OPERATION(2, "系统调整"),
        
        /** 3 购物抵现 */
        SHOPPING_USED(3, "购物抵现"),
        
        /** 4 订单退款返还 */
        order_refund(4, "订单退款返还"),
        
        /** 5 兑换购物券 */
        COUPON_USED(5, "兑换购物券"),
        
        /** 6 积分提现 */
        POINT_EXCHANGE(6, "积分提现"),
        
        /** 7 小伙伴首次下单 */
        FRIENDS_FIRST_SHOPPING(7, "小伙伴首次下单"),
        
        /** 8 小伙伴重复下单 */
        FRIENDS_REPEAT_SHOPPING(8, "小伙伴重复下单"),
        
        /** 9 签到奖励 */
        SIGNIN_REWARD(9, "签到奖励"),
        
        /** 10 评价送积分 */
        COMMENT_PRODUCT_RETURN(10, "评价送积分"),
        
        /** 11 合伙人永久收益 */
        BUDDY_PERPETUAL_ORDER(11, "合伙人永久收益"),
        
        /** 12 购物抵现取消 */
        SHOPPING_USED_CANCEL(12, "购物抵现取消"),
        
        /** 13 购物抵现取消 */
        EXCHANGE_PRODUCT_USED(13, "积分换购商品"),
        
        ACTIVITIES_REWARD(14, "活动送积分");
        
        final int code;
        
        final String desc;
        
        private INTEGRAL_OPERATION_TYPE(int code, String desc)
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
            for (INTEGRAL_OPERATION_TYPE e : INTEGRAL_OPERATION_TYPE.values())
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
     * 用户等级
     *
     */
    public static enum LEVEL
    {
        V0(0, 0, "V0会员"),
        
        V1(1, 500, "V1会员"),
        
        V2(2, 2000, "V2会员"),
        
        V3(3, 10000, "V3会员"),
        
        V4(4, 50000, "V3会员"),
        
        V5(5, 100000, "V3会员"),
        
        V6(6, 200000, "V3会员"),
        
        V7(7, 500000, "V3会员");
        
        private final int code;
        
        /** 最低消费 元 */
        private final int limitMoney;
        
        private final String description;
        
        LEVEL(int code, int limitMoney, String description)
        {
            this.code = code;
            this.limitMoney = limitMoney;
            this.description = description;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public int getLimitMoney()
        {
            return limitMoney;
        }
        
        public String getDescription()
        {
            return description;
        }
    }
    
    /**
     * 帐号类型枚举
     *
     */
    public static enum ACCOUNT_CHANNEL_TYPE
    {
        MOBILE(1, "左岸城堡"),
        
        QQ(2, "左岸城堡"),
        
        WEIXIN(3, "左岸城堡"),
        
        SINA(4, "左岸城堡"),
        
        ALIPAY(5, "左岸城堡"),
        
        GGT_WEIXIN(6, "左岸城堡"),
        
        GGT_APP(7, "左岸城堡"),
        
        QUAN_QIU_BU_SHOU(8, "左岸城堡");
        
        private final int code;
        
        private final String desc;
        
        private ACCOUNT_CHANNEL_TYPE(int code, String desc)
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
            for (ACCOUNT_CHANNEL_TYPE e : ACCOUNT_CHANNEL_TYPE.values())
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
     * 左岸城堡用户类型
     */
    public static enum MWEB_USER_TYPE
    {
        WECHAT(1, "微信用户"), // 微信
        WECHAT_APP(2, "app微信用户"), // app微信
        MOBILE_NUMBER_APP(3, "app手机用户"); // app手号
        
        private final int code;
        
        private final String desc;
        
        private MWEB_USER_TYPE(int code, String desc)
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
            for (MWEB_USER_TYPE e : MWEB_USER_TYPE.values())
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
