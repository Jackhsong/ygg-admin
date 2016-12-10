package com.ygg.admin.code;

/**
 * 用户反馈类型枚举
 * @author Administrator
 * 1：购物流程，2：物流问题，3：售后服务，4：积分/优惠券，5：新品建议，6：其他建议
 */
public enum ProposeTypeEnum
{
    PROPOSE_TYPE_ALL(0, "全部"),
    
    PROPOSE_TYPE_SHOPPINGFLOW(1, "购物流程"),
    
    PROPOSE_TYPE_LOGISTICS(2, "物流问题"),
    
    PROPOSE_TYPE_AFTERSERVICE(3, "售后服务"),
    
    PROPOSE_TYPE_COUPON_INFO(4, "积分/优惠券"),
    
    PROPOSE_TYPE_NEW_PRODUCT(5, "新品建议"),
    
    PROPOSE_TYPE_OTHER(6, "其他建议");
    
    private int code;
    
    private String desc;
    
    private ProposeTypeEnum(int code, String desc)
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
    
    public static String getDesc(int code)
    {
        for (ProposeTypeEnum e : ProposeTypeEnum.values())
        {
            if (e.getCode() == code)
            {
                return e.desc;
            }
        }
        return null;
    }
}
