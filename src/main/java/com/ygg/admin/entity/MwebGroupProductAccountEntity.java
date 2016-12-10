package com.ygg.admin.entity;

public class MwebGroupProductAccountEntity
{
    private int id;
    
    // 商品id
    private int mwebGroupProductId;
    
    // 团购商品信息id
    private int mwebGroupProductInfoId;
    
    // 团购账号id
    private int mwebGroupAccountId;
    
    // 人员类型：1：团长，2：成员',
    private byte type;
    
    // 组团状态：1：开团成功（进行中），2：组团成功，3：组团失败
    private byte status;
    
    // 创建时间
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getMwebGroupProductInfoId()
    {
        return mwebGroupProductInfoId;
    }
    
    public int getMwebGroupProductId()
    {
        return mwebGroupProductId;
    }
    
    public void setMwebGroupProductId(int mwebGroupProductId)
    {
        this.mwebGroupProductId = mwebGroupProductId;
    }
    
    public void setMwebGroupProductInfoId(int mwebGroupProductInfoId)
    {
        this.mwebGroupProductInfoId = mwebGroupProductInfoId;
    }
    
    public int getMwebGroupAccountId()
    {
        return mwebGroupAccountId;
    }
    
    public void setMwebGroupAccountId(int mwebGroupAccountId)
    {
        this.mwebGroupAccountId = mwebGroupAccountId;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(byte status)
    {
        this.status = status;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
}
