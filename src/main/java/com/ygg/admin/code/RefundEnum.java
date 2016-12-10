package com.ygg.admin.code;

import com.ygg.admin.config.YggAdminProperties;

/**
 * 退款相关枚举
 *
 * @author xiongl
 * @create 2016-04-27 9:48
 */
public class RefundEnum
{
    /**
     * 退款接收账户类型
     */
    public enum REFUND_PAY_TYPE
    {
        
        /** 1  新建账户 */
        CREATE_NEW_ACCOUNT_CARD(1, "新建账户"),
        
        /** 2  原路返回 */
        RETURN_BACK(2, "原路返回");
        
        private final int code;
        
        private final String desc;
        
        private REFUND_PAY_TYPE(int code, String desc)
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
        
        public static REFUND_PAY_TYPE getEnumByCode(int code)
        {
            for (REFUND_PAY_TYPE e : REFUND_PAY_TYPE.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    public enum REFUND_PROPORTION_TYPE
    {
        /** 未发货订单取消 */
        UN_SEND_ORDER_CANCEL(0, "未发货订单取消"),
        
        /** 已发货订单退款 */
        ALREADY_SEND_ORDER_REFUND(1, "已发货订单退款"),
        
        /** 订单退差价 */
        ORDER_FOR_MONEY(2, "订单退差价"),
        
        /** 订单退运费 */
        ORDER_FOR_POSTAGE(3, "订单退运费");
        
        private final int code;
        
        private final String desc;
        
        REFUND_PROPORTION_TYPE(int code, String desc)
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
            for (REFUND_PROPORTION_TYPE e : REFUND_PROPORTION_TYPE.values())
            {
                if (code == e.code)
                {
                    return e.desc;
                }
            }
            return "";
        }
        
        public static REFUND_PROPORTION_TYPE getEnumByCode(int code)
        {
            for (REFUND_PROPORTION_TYPE e : REFUND_PROPORTION_TYPE.values())
            {
                if (e.code == code)
                    return e;
            }
            return null;
        }
    }
    
    /**
     * 以下信息均为生产换进配置信息，如需测试，请换成测试帐号
     * 退款账户类型
     */
    public static enum REFUND_ACCOUNT
    {
        GGJ_APP1("1249412201", "wx01d40e85e9b1fe8b", "d261cc29c722ab025e4d6251e5c3e436"),
        
        GGJ_WAP1("1266647901", "wx2fbe8b0e1e95cb16", "d261cc29c722ab025e4d6251e5c3e427"),
        
        HQBS_WAP("1304400701", "wx50718ec381121bd5", "8934e7d15453e97507ef794cf7b0519d");
        
        private final String mchid;
        
        private final String appid;
        
        private final String key;
        
        private final String cert;
        
        public String getMchid()
        {
            return mchid;
        }
        
        public String getAppid()
        {
            return appid;
        }
        
        public String getKey()
        {
            return key;
        }
        
        public String getCert()
        {
            return cert;
        }
        
        private REFUND_ACCOUNT(String mchid, String appid, String key)
        {
            this.mchid = mchid;
            this.appid = appid;
            this.key = key;
            this.cert = YggAdminProperties.getInstance().getPropertie("weixin_refund_cert_path") + mchid + ".p12";
        }
        
        public static REFUND_ACCOUNT getEnum(String mchid)
        {
            for (REFUND_ACCOUNT e : REFUND_ACCOUNT.values())
            {
                if (e.mchid.equals(mchid))
                {
                    return e;
                }
            }
            return null;
        }
    }
}
