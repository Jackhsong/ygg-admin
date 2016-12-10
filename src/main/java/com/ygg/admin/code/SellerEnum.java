package com.ygg.admin.code;

/**
 * 商家相关枚举
 * @author xiongl
 *
 */
public class SellerEnum
{
    /**
     * 商家发货时效说明
        1:当天15点前订单当天24点前打包并提供物流单号，24小时内有物流信息,
        2:单笔订单24小时发货,
        3:单笔订单48小时发货,
        4:单笔订单72小时发货
     * @author xiongl
     *
     */
    public static enum SellerSendTimeTypeEnum
    {
        IN_15_HOUR(1, "当天15点前订单当天24点前打包并提供物流单号，24小时内有物流信息"),
        
        IN_24_HOUR(2, "单笔订单24小时发货"),
        
        IN_48_HOUR(3, "单笔订单48小时发货"),
        
        IN_72_HOUR(4, "单笔订单72小时发货");
        
        private int code;
        
        private String desc;
        
        private SellerSendTimeTypeEnum(int code, String desc)
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
            for (SellerSendTimeTypeEnum e : SellerSendTimeTypeEnum.values())
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
     * 商家周末发货类型枚举
     * @author xiongl
     *
     */
    public static enum WeekendSendTypeEnum
    {
        NOT_SEND_ON_WEEKEND(1, "周六、周日不发货"),
        
        SEND_ON_SATURDAY(2, "周日不发货"),
        
        SEND_ON_SUNDAY(3, "周六不发货"),
        
        SEND_ON_WEEKEND(4, "周六、周日正常发货");
        
        private int code;
        
        private String desc;
        
        private WeekendSendTypeEnum(int code, String desc)
        {
            this.desc = desc;
            
            this.code = code;
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
            for (WeekendSendTypeEnum e : WeekendSendTypeEnum.values())
            {
                if (code == e.code)
                {
                    return e.getDesc();
                }
            }
            return "";
        }
    }
    
    /**
     * 商家发货依据枚举
     * @author xiongl
     *
     */
    public static enum SellerSendCodeTypeEnum
    {
        PRODUCTCODE(1, "商品编码"),
        
        PRODUCTBARCODE(2, "商品条码"),
        
        PRODUCTNAME(3, "商品名称"),
        
        OTHER(4, "其他");
        
        final int code;
        
        final String desc;
        
        private SellerSendCodeTypeEnum(int code, String desc)
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
            for (SellerSendCodeTypeEnum e : SellerSendCodeTypeEnum.values())
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
     * 商家发货类型枚举
     * @author xiongl
     *
     */
    public static enum SellerTypeEnum
    {
        /** 国内 */
        CHINA("国内", 1, 0, 0),
        
        /** 保税区 */
        BAOSHIQU("保税区", 2, 1, 0),
        
        /** 香港 */
        HONG_KONG("香港", 3, 1, 0),
        
        /** 美国 */
        USA("美国", 4, 1, 1),
        
        /** 日本 */
        JAPAN("日本", 5, 1, 1),
        
        /** 澳大利亚 */
        AUSTRALIA("澳大利亚", 6, 1, 1),
        
        /** 德国 */
        GERMANY("德国", 7, 1, 1),
        
        /** 荷兰 */
        HOLLAND("荷兰", 8, 1, 1),
        
        /** 台湾 */
        TAI_WAN("台湾", 9, 1, 1),
        
        /** 香港 需要身份证 ,暂时不用 */
        // HONG_KONG_1("香港", 3, 1, 1);
        
        /** 加拿大 */
        CANADA("加拿大", 10, 1, 1),
        
        /** 新西兰 */
        NEW_ZEALAND("新西兰", 11, 1, 1),
        
        KOREA("韩国", 12, 1, 1);
        
        final String desc;
        
        final int code;
        
        final int isNeedIdCardNumber;
        
        final int isNeedIdCardImage;
        
        private SellerTypeEnum(String desc, int code, int isNeedIdCardNumber, int isNeedIdCardImage)
        {
            this.desc = desc;
            this.code = code;
            this.isNeedIdCardNumber = isNeedIdCardNumber;
            this.isNeedIdCardImage = isNeedIdCardImage;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public int getIsNeedIdCardImage()
        {
            return isNeedIdCardImage;
        }
        
        public int getIsNeedIdCardNumber()
        {
            return isNeedIdCardNumber;
        }
        
        public static String getDescByCode(int code)
        {
            for (SellerTypeEnum e : SellerTypeEnum.values())
            {
                if (code == e.code)
                {
                    String suffix = "";
                    if (e.isNeedIdCardImage == 1)
                    {
                        suffix = "（身份证照片）";
                    }
                    else if (e.isNeedIdCardNumber == 1)
                    {
                        suffix = "（仅身份证号）";
                    }
                    return e.desc + suffix;
                }
            }
            return "";
        }
        
        /**
         * 根据排序值查询枚举
         * 
         * @param code
         * @return
         */
        public static SellerTypeEnum getSellerTypeEnumByCode(int code)
        {
            for (SellerTypeEnum e : SellerTypeEnum.values())
            {
                if (code == e.code)
                {
                    return e;
                }
            }
            return null;
        }
    }
    
    public static enum BondedNumberTypeEnum
    {
        
        BONDED_TYPE_1(1, "先有物流单号后报关"),
        
        BONDED_TYPE_2(2, "先报关后有物流单号");
        
        private int code;
        
        private String desc;
        
        private BondedNumberTypeEnum(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public void setCode(int code)
        {
            this.code = code;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
        public void setDesc(String desc)
        {
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (BondedNumberTypeEnum e : BondedNumberTypeEnum.values())
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
     * 商家黑名单
     */
    public static enum SellerBlackTypeEnum
    {
        
        POSTAGE(1, "邮费黑名单"),
        
        ACTIVITY(2, "活动及优惠券黑名单");
        
        private int code;
        
        private String desc;
        
        private SellerBlackTypeEnum(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public int getCode()
        {
            return code;
        }
        
        public void setCode(int code)
        {
            this.code = code;
        }
        
        public String getDesc()
        {
            return desc;
        }
        
        public void setDesc(String desc)
        {
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (SellerBlackTypeEnum e : SellerBlackTypeEnum.values())
            {
                if (e.code == code)
                {
                    return e.desc;
                }
            }
            return "";
        }
    }
    
    public static enum DELIVER_AREA_SUPPORT_TYPE
    {
        SUPPORT(1, "支持地区"),
        
        UNSUPPORT(2, "不支持地区");
        
        private final int code;
        
        private final String desc;
        
        private DELIVER_AREA_SUPPORT_TYPE(int code, String desc)
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

    public static enum DEPOSIT_STATUS
    {
        NOT_SIGN(0, "未签协议"),
        NONPAYMENT(1, "已签未缴纳"),
        PAYMENT(2, "已缴纳");

        private int code;
        private String desc;
        DEPOSIT_STATUS(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static String getByCode(int code) {
            for(DEPOSIT_STATUS status : DEPOSIT_STATUS.values()) {
                if(status.code == code ) {
                    return status.desc;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
