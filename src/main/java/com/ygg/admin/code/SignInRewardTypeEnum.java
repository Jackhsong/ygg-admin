package com.ygg.admin.code;

/**
 * 签到奖励类型
 * @author xiongl
 *
 */
public enum SignInRewardTypeEnum
{
    /** 0 占位符 */
    NORMAL("占位符"),
    
    /** 1 积分 */
    SIGNIN_REWARD_TYPE_SCORE("积分"),
    
    /** 2 优惠券 */
    SIGNIN_REWARD_TYPE_COUPON("优惠券");
    
    private String description;
    
    private SignInRewardTypeEnum(String description)
    {
        this.description = description;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public static String getDescriptionByOrdinal(int ordinal)
    {
        for (SignInRewardTypeEnum e : SignInRewardTypeEnum.values())
        {
            if (ordinal == e.ordinal())
            {
                return e.description;
            }
        }
        return "";
    }
}
