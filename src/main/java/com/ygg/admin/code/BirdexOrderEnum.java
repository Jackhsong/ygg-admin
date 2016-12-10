package com.ygg.admin.code;

/**
 * @author Administrator
 *  笨鸟订单相关枚举
 */
public class BirdexOrderEnum
{
    /**
     * 笨鸟订单修改状态
     */
    public enum CHANGE_STATUS
    {
        PROCESSING(1, "处理中"),
        
        SUCCESS(2, "成功"),
        
        FAIL(3, "失败");
        private int code;
        
        private String desc;
        
        private CHANGE_STATUS(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (CHANGE_STATUS e : CHANGE_STATUS.values())
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
     * 笨鸟订单修改类型
     */
    public enum CHANGE_TYPE
    {
        MODIFY_ADDRESS(1, "修改收货地址"),
        
        MODIFY_IDCARD(2, "修改身份证"),
        
        CANCEL_ORDER(3, "取消订单");
        
        private int code;
        
        private String desc;
        
        private CHANGE_TYPE(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (CHANGE_TYPE e : CHANGE_TYPE.values())
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
     * @author Administrator
     * 笨鸟订单物流状态
     */
    public enum LOGISTICS_STATUS
    {
        IN_AUDIT(0, "审核中"),
        
        NOT_PASS_AUDIT(1, "审核未通过"),
        
        WAITING_CHECKOUT(2, "等待出库"),
        
        CHECKOUT(3, "已出库"),
        
        CUSTOMS_CLEARANCE(4, "海关清关中"),
        
        CUSTOMS_SUCCESS(5, "清关成功"),
        
        DISPATCH(6, "国内快递派送中"),
        
        SIGN(7, "派送成功"),
        
        ERROR(8, "异常原因派送失败");
        
        private int code;
        
        private String desc;
        
        private LOGISTICS_STATUS(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (LOGISTICS_STATUS e : LOGISTICS_STATUS.values())
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
     * 笨鸟订单推送状态
     */
    public enum PUSH_STATUS
    {
        PROCESSING(1, "推送中"),
        
        SUCCESS(2, "推送成功"),
        
        FAIL(3, "推送失败");
        private int code;
        
        private String desc;
        
        private PUSH_STATUS(int code, String desc)
        {
            this.code = code;
            this.desc = desc;
        }
        
        public static String getDescByCode(int code)
        {
            for (PUSH_STATUS e : PUSH_STATUS.values())
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
