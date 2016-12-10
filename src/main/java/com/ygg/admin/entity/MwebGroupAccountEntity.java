package com.ygg.admin.entity;

public class MwebGroupAccountEntity
{
    private int id;
    
    private int baseAccountId;
    
    private String openId;
    
    private String appOpenId;
    
    private String unionId;
    
    private String wechatNickName;
    
    private String wechatImage;
    
    // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    private String subscribe;
    
    private String createTime;
    
    private String updateTime;
    
    // '账号类型；1：左岸城堡用户，2：app用户 3 app手机
    private byte type;
    
    public int getId()
    {
        return id;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getOpenId()
    {
        return openId;
    }
    
    public void setOpenId(String openId)
    {
        this.openId = openId;
    }
    
    public String getUnionId()
    {
        return unionId;
    }
    
    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }
    
    public String getWechatNickName()
    {
        return wechatNickName;
    }
    
    public void setWechatNickName(String wechatNickName)
    {
        this.wechatNickName = wechatNickName;
    }
    
    public String getWechatImage()
    {
        return wechatImage;
    }
    
    public void setWechatImage(String wechatImage)
    {
        this.wechatImage = wechatImage;
    }
    
    public String getSubscribe()
    {
        return subscribe;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getBaseAccountId()
    {
        return baseAccountId;
    }
    
    public void setBaseAccountId(int baseAccountId)
    {
        this.baseAccountId = baseAccountId;
    }
    
    public void setSubscribe(String subscribe)
    {
        this.subscribe = subscribe;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public String getAppOpenId()
    {
        return appOpenId;
    }
    
    public void setAppOpenId(String appOpenId)
    {
        this.appOpenId = appOpenId;
    }
}
