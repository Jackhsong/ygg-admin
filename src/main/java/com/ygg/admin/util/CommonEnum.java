package com.ygg.admin.util;

public class CommonEnum
{
    
    /**
     * 快递跟踪状态。
     *
     * 依据快递100API制定初版。 状态 4～7 需要与快递100签订增值服务才能开通。
     * 
     * @author Administrator
     * 
     */
    public static enum LogisticsStateEnum
    {
        
        /** 0 - 在途中 */
        ON_WAY("在途中"),
        
        /** 1 - 已揽收 */
        POSTED("已揽收"),
        
        /** 2 - 疑难 */
        IN_TROUBLE("问题件"),
        
        /** 3 - 已签收 */
        RECEIVED("已签收"),
        
        /** 4 - 退签 */
        REJECTED("退签"),
        
        /** 5 - 同城派送中 */
        CITY_WIDE_ON_WAY("同城派送中"),
        
        /** 6 - 退回 */
        RETURNED("退回"),
        
        /** 7 - 转单 */
        SEND_AGAIN("转单");
        
        /** 描述 */
        final String description;
        
        private LogisticsStateEnum(String description)
        {
            this.description = description;
        }
        
        public static String getDescription(int ordinal)
        {
            for (LogisticsStateEnum e : LogisticsStateEnum.values())
            {
                if (e.ordinal() == ordinal)
                {
                    return e.description;
                }
            }
            return "";
        }
    }
    
    public static enum RefundStatusEnum
    {
        /** 0 - 没有用 ，只做占位符 */
        NORMAL("占位符"),
        /** 1 */
        APPLY("申请中"),
        /** 2 */
        WAIT_RETURN_OF_GOODS("待退货"),
        /** 3 */
        WAIT_RETURN_OF_MONEY("待退款"),
        /** 4 */
        SUCCESS("退款成功"),
        /** 5 */
        CLOSE("退款关闭"),
        /** 6 */
        CANCEL("退款取消");
        
        final String description;
        
        private RefundStatusEnum(String description)
        {
            this.description = description;
        }
        
        public String getDescription()
        {
            return description;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (RefundStatusEnum e : RefundStatusEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }
            }
            return "";
        }
    }

    /**
     * app订单来源
     * 
     * @author Administrator
     * 
     */
    public static enum OrderAppChannelEnum
    {
        
        /** 0 */
        IOS("IOS"),
        
        /** 1 */
        ANDROID_OTHER("Android-其他"),
        
        /** 2 */
        ANDROID_360("Android-360"),
        
        /** 3 */
        ANDROID_MI("Android-小米"),
        
        /** 4 */
        ANDROID_91("Android-91"),
        
        /** 5 */
        ANDROID_BAIDU("Android-百度"),
        
        /** 6 */
        ANDROID_WAN_DOU_JIA("Android-豌豆荚"),
        
        /** 7 */
        ANDROID_YING_YONG_BAO("Android-应用宝"),
        
        /** 8 */
        ANDROID_TAOBAO("Android-淘宝"),
        
        /** 9 */
        ANDROID_VIVO("Android-vivo"),
        
        /** 10 */
        ANDROID_INDEX("Android-官网"),
        
        /** 11 */
        ANDROID_MOBILE("移动网页"),
        
        /** 12 */
        ANDROID_SOUGOU("Android-搜狗"),
        
        /** 13 */
        ANDROID_ANZHI("Android-安智"),
        
        /** 14 */
        ANDROID_SUNINGYIGOU("Android-苏宁易购"),
        
        /** 15 */
        ANDROID_HUAWEI("Android-华为"),
        
        /** 16 */
        ANDROID_GUANGDIANTONG_YUANSHENG("Android-广点通-原生"),
        
        /** 17 */
        ANDROID_GUANGDIANTONG_FEED("Android-广点通-FEED流"),
        
        /** 18 */
        ANDROID_JINRITOUTIAO("Android-今日头条"),
        
        /** 19 */
        IOS_VEST1("历史副IOS已弃用"),
        
        /** 20 */
        IOS_VEST2("IOS女神版"),
        
        /** 21武汉光点通 */
        ANDROID_GUANGDIANTONG_WUHANG("Android-广点通-WUHANG"),
        
        /** 22微信拼团 */
        GEGETUAN_WECHAT("左岸城堡-微信"),
        
        /** 23魅族 */
        MEIZU("Meizu-魅族"),
        
        /** 24 */
        GEGETUAN_APP("左岸城堡-APP"),
        
        /** 25 */
        IOS_slave("iOS【1】"),
        
        /** 26 */
        GEGETUAN_APP_IOS("左岸城堡-APPIOS"),
        
        /** 27 */
        GEGETUAN_APP_ANDROID("左岸城堡-APP安卓"),
        
        /** 28 */
        QUAN_QIU_BU_SHOU("左岸城堡"),
        
        /** 29 */
        YAN_WANG("燕网"),
        
        /** 30 */
        SAMSUNG_ANDROID("三星安卓"),
        
        /** 31 */
        LENOVO_ANDROID("联想安卓"),
        
        /** 32 */
        GiONEE_ANDROID("金立安卓"),
        
        /** 33 */
        LETV_ANDROID("乐视安卓"),
        
        /** 34 */
        AWL_ANDROID("锤子安卓"),
        
        /** 35 */
        OPPO_ANDROID("OPPO安卓"),
        
        /** 36 */
        JIFENG_ANDROID("机锋安卓"),
        
        /** 37 */
        MUMAYI_ANDROID("木蚂蚁安卓"),
        
        /** 38 */
        YINYONGHUI_ANDROID("应用汇安卓"),
        
        /** 39 */
        YOUYI_ANDROID("优亿市场安卓"),
        
        /** 40 */
        IOS3_ANDROID("IOS专业版"),
        
        
        //只属于左岸城堡IOS
        /** 201 */
        IOS_GEGE_YOUXUAN(201,"格格优选"),
        /** 202 */
        IOS_ZUIMEI_CHIHUO(202,"最美吃货"),
        /** 203 */
        IOS_GEGE_HAIGOU(203,"格格海购"),
        /** 204 */
        IOS_MEISHI_TUANGOU(204,"美食团购"),
        /** 205 */
        IOS_MEISHI_CAIPU(205,"美食菜谱");
        
        
        
        final String description;
        int code;//添加新编码
        
        private OrderAppChannelEnum(String description)
        {
            this.description = description;
        }
        
        private OrderAppChannelEnum(int code,String description)
        {
            this.code = code;
            this.description = description;
        }
        
        public String getDescription()
        {
            return description;
        }
        
        public int getCode()
        {
            return code;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (OrderAppChannelEnum e : OrderAppChannelEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }else if(ordinal == e.getCode()){
                    return e.description;
                }
            }
            return "";
        }
    }
    
    /**
     * 短信类型 1：注册验证码；2：忘记密码；3：自定义短信；4：发货短信
     * 
     * @author zhangyb
     * 
     */
    public static enum SmsTypeEnum
    {
        /** 0 - 没有用 ，只做占位符 */
        NORMAL("占位符"),
        
        /** 1 */
        REGISTER("注册验证码"),
        
        /** 2 */
        FORGET_PWD("忘记密码"),
        
        /** 3 */
        CUSTOM("自定义短信"),
        
        /** 4 */
        SENDGOODS("发货短信"),
        
        /** 5 */
        OVERSEASREMIND("海外购节假日提醒短信"),
        
        /** 6 */
        SMSMONEYREMIND("短信余额提醒"),
        
        /** 7 先报关后又物流的商家下单时发送短息 */
        BAOGUANFIRSTWULIULATER("下单提醒短信"),
        
        /** 8 签收提醒短信 */
        RECEIVE_GOODS("签收提醒短信"),
        
        /** 9 提醒商家发货短信 */
        REMIND_SELLER_SENDGOODS("提醒商家发货短信");
        
        final String description;
        
        private SmsTypeEnum(String description)
        {
            this.description = description;
        }
        
        public String getDescription()
        {
            return description;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (SmsTypeEnum e : SmsTypeEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }
            }
            return "";
        }
    }
    
    public static enum BankTypeEnum
    {
        /** 0 - 只占茅坑不拉屎 */
        NORMAL("占位符"),
        
        /** 1 */
        BANK_ICBC("中国工商银行"),
        
        /** 2 */
        BANK_ABCHINA("中国农业银行"),
        
        /** 3 */
        BANK_CHINA("中国银行"),
        
        /** 4 */
        BANK_CCB("中国建设银行"),
        
        /** 5 */
        BANK_PSBC("中国邮政储蓄银行"),
        
        /** 6 */
        BANK_COMM("交通银行"),
        
        /** 7 */
        BANK_MERCHANTS("招商银行"),
        
        /** 8 */
        BANK_CEB("中国光大银行"),
        
        /** 9 */
        BANK_CITIC("中信银行"),

        /** 10 */
        ZHI_FU_BAO("支付宝");
        
        final String description;
        
        private BankTypeEnum(String description)
        {
            this.description = description;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (BankTypeEnum e : BankTypeEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }
            }
            return "";
        }
    }
    
    public static enum BusinessTypeEnum
    {
        /** 0 - 占位符 */
        NORMAL("占位符"),
        
        /** 1 */
        ADMINISTRATOR_MANAGEMENT("管理员管理"),
        
        /** 2 */
        USER_MANAGEMENT("用户管理"),
        
        /** 3 */
        ORDER_MANAGEMENT("订单管理"),
        
        /** 4 */
        SELL_MANAGEMENT("特卖管理"),
        
        /** 5 */
        PRODUCT_MANAGEMENT("商品管理"),
        
        /** 6 */
        CATEGORY_MANAGEMENT("分类管理"),
        
        /** 7 */
        SELLER_MANAGEMENT("商家管理"),
        
        /** 8 */
        SALE_MANAGEMENT("促销管理"),
        
        /** 9 */
        BRAND_MANAGEMENT("品牌管理"),
        
        /** 10 */
        SALESERVICE_MANAGEMENT("售后管理"),
        
        /** 11 */
        DATA_COLLECTION("数据统计"),
        
        /** 12 */
        SMS_MANAGEMENT("短信管理"),
        
        /** 13 */
        FENXIAO_MANAGEMENT("分销管理"),
        
        /** 14 */
        SYSTEM_TOOL("系统工具"),
        
        FINACE_MANAGEMENT("财务管理");
        
        final String description;
        
        private BusinessTypeEnum(String description)
        {
            this.description = description;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (BusinessTypeEnum e : BusinessTypeEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }
            }
            return "";
        }
    }

    /**  使用origin获取值 禁止 中间插入新的  */
    public static enum OperationTypeEnum
    {
        /** 0 - 占位符 */
        NORMAL("占位符"),
        
        /** 1 */
        MODIFY_ORDER_PRICE("订单改价"),
        
        /** 2 */
        MODIFY_ORDER_STATUS("订单改状态"),
        
        /** 3 */
        HAND_CREATE_ORDER("手动创建订单"),
        
        /** 4 */
        MODIFY_PRODUCT_STOCK("调整库存"),
        
        /** 5 */
        MODIFY_PRODUCT_PRICE("商品改价"),
        
        /** 6 */
        MODIFY_SELL_COUNT("修改销量"),
        
        /** 7 */
        MODIFY_SELLER_NAME("修改商品商家"),
        
        MODIFY_PRODUCT_STATUS("修改商品状态"),
        
        MODIFY_PRODUCT_TIME("修改商品销售时间"),
        
        MODIFY_PRODUCT_SALE_STATUS("商品上下架"),
        
        MODIFY_SELL_TIME("修改特卖时间"),
        
        MODIFY_SELL_PRODUCT("修改特卖关联商品"),
        
        MODIFY_SELL_PRODUCT_TIME("修改特卖商品销售时间"),
        
        MODIFY_BANNER_PRODUCT("修改banner关联商品"),
        
        MODIFY_USER_INTEGRAL("调整用户积分"),
        
        SEND_COUPON("优惠券发放"),
        
        SEND_COUPON_CODE("优惠码生成"),
        
        CREATE_COUPON_DETAIL("新增优惠券类型"),
        
        CHANGE_COUPON_DETAIL_STATUS("更改优惠券状态"),
        
        PARTNER_AUDIT_STATUS("合伙人审核"),
        
        CREATE_PARTNER("创建合伙人"),
        
        CANCEL_PARTNER_STATUS("取消合伙人资格"),
        
        RESET_PARTNER_STATUS("恢复合伙人资格"),
        
        MODIFY_PRODUCT_CODE("修改商品编码"),
        
        CREATE_INVITED_RELATION("手动创建邀请关系"),
        
        MODIFY_ORDER_REMARK("订单改商家备注"),
        
        MODIFY_ORDER_REMARK2("订单改客服备注"),
        
        ADD_GEGEWELFARE_PRODUCT("新增格格福利商品"),
        
        MODIFY_GEGEWELFARE_PRODUCT("修改格格福利商品"),
        
        EXPORT_ORDER("导出订单"),
        
        MODIFY_ORDER_ADDRESS("手动修改订单收货地址"),

        MODIFY_BASE_PRODUCT_PRICE("基本商品改价"),

        MODIFY_BASE_PRODUCT_SUbMIT("修改基本商品结算"),

        MODIFY_PRODUCT("修改商品"),

        HAND_CREATE_REFUND_ORDER("新增退款退货订单"),

        DELETE_GEGEWELFARE_PRODUCT("删除格格福利商品");
        
        final String description;
        
        private OperationTypeEnum(String description)
        {
            this.description = description;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (OperationTypeEnum e : OperationTypeEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }
            }
            return "";
        }
    }
    
    public static enum LogLevelEnum
    {
        /** 0 - 只占茅坑不拉屎 */
        NORMAL("占位符"),
        
        /** 1 */
        LEVEL_ONE("订单操作日志"),
        
        /** 2 */
        LEVEL_TWO("运维操作日志"),
        
        /** 3 */
        LEVEL_THREE("用户操作日志"),
        
        /** 4 */
        LEVEL_FOUR("促销操作日志");
        
        final String description;
        
        private LogLevelEnum(String description)
        {
            this.description = description;
        }
        
        /**
         * 根据排序值查询描述
         * 
         * @param ordinal
         * @return
         */
        public static String getDescriptionByOrdinal(int ordinal)
        {
            for (LogLevelEnum e : LogLevelEnum.values())
            {
                if (ordinal == e.ordinal())
                {
                    return e.description;
                }
            }
            return "";
        }
    }
    
    public static enum PRODUCT_TYPE
    {
        
        ORDINARY_GOODS(0), // 普通商品
        LARGE_GROUP(1); // 系统自动团
        
        private int value;
        
        private PRODUCT_TYPE(int value)
        {
            this.value = value;
        }
        
        public int getValue()
        {
            return this.value;
        }
    }
    
    /**
     * 团队状态
     */
    public static enum TEAM_STATUS
    {
        TRANSACTION(1), // 开团成功（进行中）
        SUCCESS(2), // 组团成功
        FAIL(3); // 组团失败
        private int value;
        
        private TEAM_STATUS(int iValue)
        {
            this.value = iValue;
        }
        
        public int getValue()
        {
            return this.value;
        }
    }
}
