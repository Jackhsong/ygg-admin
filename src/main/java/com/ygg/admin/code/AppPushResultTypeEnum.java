package com.ygg.admin.code;

/**
 * 推送返回status
 * 
 * @author zhangyb
 *
 */
public enum AppPushResultTypeEnum
{
    /** 0 目标用户列表为空 */
    TargetListIsNullOrSizeIs0("目标用户列表为空"),
    
    /** 1 群推消息次数超限 */
    PushMsgToListOrAppTimesOverLimit("群推消息次数超限"),
    
    /** 2 推送总数超限 */
    PushTotalNumOverLimit("推送总数超限"),
    
    /** 3 接口流量超限  */
    flow_exceeded("接口流量超限"),
    
    /** 4 未查找到用户，列表内无有效用户 */
    TokenMD5NoUsers("未查找到用户，列表内无有效用户"),
    
    /** 5 cid绑定的appId和sdk上传的appId不一致 */
    AppidError("cid绑定的appId和sdk上传的appId不一致"),
    
    /** 6 未找到消息公共体 */
    NullMsgCommon("未找到消息公共体"),
    
    /** 7 任务已经被CANCEL */
    TaskIdHasBeanCanceled("任务已经被CANCEL"),
    
    /** 8 未知错误 */
    OtherError("未知错误");
    
    private String description;
    
    private AppPushResultTypeEnum(String description)
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
    
    public static String getErrorDescByName(String name)
    {
        for (AppPushResultTypeEnum e : AppPushResultTypeEnum.values())
        {
            if (e.name().equals(name))
            {
                return e.getDescription();
            }
        }
        return "";
    }
}
