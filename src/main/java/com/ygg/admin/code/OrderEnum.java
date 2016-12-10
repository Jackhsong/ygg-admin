package com.ygg.admin.code;

/**
 * 订单相关枚举
 * @author xiongl
 *
 */
public class OrderEnum
{
    /**
     * 订单申诉结果枚举
     * @author Administrator
     *
     */
    public static enum TIMEOUT_COMPLAIN_RESULT
    {
        PROCESSING(1, "处理中"),
        
        SUCCESS(2, "申诉成功"),
        
        FAILED(3, "申诉失败");
        
        private int code;
        
        private String name;
        
        private TIMEOUT_COMPLAIN_RESULT(int code, String name)
        {
            this.code = code;
            this.name = name;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public static String getNameByCode(int code)
        {
            for (TIMEOUT_COMPLAIN_RESULT e : TIMEOUT_COMPLAIN_RESULT.values())
            {
                if (e.code == code)
                {
                    return e.name;
                }
            }
            return "";
        }
    }
    
    public static enum ORDER_CHECK_STATUS
    {
        WAIT_FOR_CHECK(1, "待审核"),
        
        CHECK_PASS(2, "审核通过"),
        
        CHECK_UN_PASS(3, "审核不通过");
        
        private int code;
        
        private String desc;
        
        private ORDER_CHECK_STATUS(int code, String desc)
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
            for (ORDER_CHECK_STATUS e : ORDER_CHECK_STATUS.values())
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
     * 物流超时申诉处理结果枚举
     *
     */
    public static enum LogisticsTimeoutComplainResultEnum
    {
        NOT_SUBMIT(0, "未申诉"),
        
        PROCESSING(1, "处理中"),
        
        SUCCESS(2, "申诉成功"),
        
        FAILED(3, "申诉失败");
        
        private int code;
        
        private String name;
        
        private LogisticsTimeoutComplainResultEnum(int code, String name)
        {
            this.code = code;
            this.name = name;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public static String getNameByCode(int code)
        {
            for (LogisticsTimeoutComplainResultEnum e : LogisticsTimeoutComplainResultEnum.values())
            {
                if (e.code == code)
                {
                    return e.name;
                }
            }
            return "";
        }
    }
    
    /**
     * 
     * 订单状态枚举
     *
     */
    public static enum ORDER_STATUS
    {
        
        /** 1 */
        CREATE(1, "未付款"),
        /** 2 */
        REVIEW(2, "待发货"),
        /** 3 */
        SENDGOODS(3, "已发货"),
        /** 4 */
        SUCCESS(4, "交易成功"),
        /** 5 */
        USER_CANCEL(5, "用户取消"),
        /** 6 */
        TIMEOUT(6, "超时取消"),
        /** 7 */
        GROUPING(7, "团购进行中"),
        /** 8 */
        JOINTEAMREFUND(8, "已支付参团失败");
        
        private int code;
        
        private String desc;
        
        private ORDER_STATUS(int code, String desc)
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
            for (ORDER_STATUS e : ORDER_STATUS.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static ORDER_STATUS getOrderStatusEnum(int code)
        {
            for (ORDER_STATUS e : ORDER_STATUS.values())
            {
                if (e.code == code)
                {
                    return e;
                }
            }
            return null;
        }

        public static ORDER_STATUS getOrderStatusEnum(String desc)
        {
            for (ORDER_STATUS e : ORDER_STATUS.values())
            {
                if (e.desc.equalsIgnoreCase(desc))
                {
                    return e;
                }
            }
            return null;
        }

        public static String getDescString() {
            StringBuilder sb = new StringBuilder();
            for (ORDER_STATUS e : ORDER_STATUS.values()) {
                sb.append(e.desc).append(",");
            }
            String s = sb.toString();
            return s.substring(0, s.length() - 1);
        }
    }
    
    /**
     * 订单类型枚举
     */
    public static enum ORDER_TYPE
    {
        GEGEJIA(1, "左岸城堡"),
        
        GEGETUAN(2, "左岸城堡"),

        GEGETUAN_QQG(3, "左岸城堡-全球购"),

        HUANQIUBUSHOU(4,"左岸城堡"),

        YANWANG(5,"燕网");
        
        final int code;
        
        final String desc;
        
        ORDER_TYPE(int code, String desc)
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
            for (ORDER_TYPE e : ORDER_TYPE.values())
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
     * 订单冻结状态
     *
     */
    public enum ORDER_FREEZE_STATUS
    {
        
        FREEZE(1, "冻结未处理"),
        
        UN_FREEZE(2, "已解冻"),
        
        FREEZE_FOREVER(3, "永久冻结");
        
        final int code;
        
        final String desc;
        
        ORDER_FREEZE_STATUS(int code, String desc)
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
            for (ORDER_FREEZE_STATUS e : ORDER_FREEZE_STATUS.values())
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
         * @return
         */
        public static ORDER_FREEZE_STATUS getEnumByCode(int code)
        {
            for (ORDER_FREEZE_STATUS e : ORDER_FREEZE_STATUS.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 订单支付渠道枚举
     *
     */
    public enum PAY_CHANNEL
    {
        
        UNION(1, "银联"),
        
        ALIPAY(2, "支付宝"),
        
        WEIXIN(3, "微信");
        
        final int code;
        
        final String desc;
        
        PAY_CHANNEL(int code, String desc)
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
            for (PAY_CHANNEL e : PAY_CHANNEL.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static PAY_CHANNEL getEnumByCode(int code)
        {
            for (PAY_CHANNEL e : PAY_CHANNEL.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }

        public static PAY_CHANNEL getEnumByDesc(String desc)
        {
            for (PAY_CHANNEL e : PAY_CHANNEL.values())
            {
                if (e.desc.equalsIgnoreCase(desc))
                    return e;
            }
            return null;
        }

    }
    
    /**
     * 快递跟踪状态。
     *
     * 依据快递100API制定初版。 状态 4～7 需要与快递100签订增值服务才能开通。
     * 
     * @author Administrator
     * 
     */
    public static enum LOGISTICS_STATE
    {
        
        /** 0 - 在途中 */
        ON_WAY(0, "在途中"),
        
        /** 1 - 已揽收 */
        POSTED(1, "已揽收"),
        
        /** 2 - 疑难 */
        IN_TROUBLE(2, "问题件"),
        
        /** 3 - 已签收 */
        RECEIVED(3, "已签收"),
        
        /** 4 - 退签 */
        REJECTED(4, "退签"),
        
        /** 5 - 同城派送中 */
        CITY_WIDE_ON_WAY(5, "同城派送中"),
        
        /** 6 - 退回 */
        RETURNED(6, "退回"),
        
        /** 7 - 转单 */
        SEND_AGAIN(8, "转单");
        
        final int code;
        
        final String desc;
        
        private LOGISTICS_STATE(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (LOGISTICS_STATE e : LOGISTICS_STATE.values())
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
