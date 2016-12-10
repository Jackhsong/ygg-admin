package com.ygg.admin.code;

public enum SignInRewardStyleTypeEnum
{
    /** 0 占位符 */
    NORMAL("占位符"),
    
    /** 1 普通 */
    SIGNIN_REWARD_STYLE_NORMAL("普通"),
    
    /** 2 高亮 */
    SIGNIN_REWARD_STYLE_HIGHLIGHT("高亮"),
    
    /** 3 月底大奖 */
    SIGNIN_REWARD_STYLE_PRIZE("月底大奖");
    
    private String description;
    
    private SignInRewardStyleTypeEnum(String description)
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
        for (SignInRewardStyleTypeEnum e : SignInRewardStyleTypeEnum.values())
        {
            if (ordinal == e.ordinal())
            {
                return e.description;
            }
        }
        return "";
    }
}
