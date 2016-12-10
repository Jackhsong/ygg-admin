package com.ygg.admin.entity;

public class MwebGroupProductInfoEntity
{
    private int id;
    
    private byte type;
    
    // 商品id
    private int mwebGroupProductId;
    
    // 人数上限
    private int teamNumberLimit;
    
    // 当前参团人数
    private int currentNumber;
    
    // 组团状态：1：开团成功（进行中），2：组团成功，3：组团失败
    private byte status;
    
    // 团队购买结束时间
    private String teamEndTime;
    
    // 前端显示 剩余团购秒数
    private int validTime;
    
    // 创建时间
    private String createTime;
    
    // 更新时间
    private String updateTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public int getMwebGroupProductId()
    {
        return mwebGroupProductId;
    }
    
    public void setMwebGroupProductId(int mwebGroupProductId)
    {
        this.mwebGroupProductId = mwebGroupProductId;
    }
    
    public int getTeamNumberLimit()
    {
        return teamNumberLimit;
    }
    
    public void setTeamNumberLimit(int teamNumberLimit)
    {
        this.teamNumberLimit = teamNumberLimit;
    }
    
    public int getCurrentNumber()
    {
        return currentNumber;
    }
    
    public void setCurrentNumber(int currentNumber)
    {
        this.currentNumber = currentNumber;
    }
    
    public byte getStatus()
    {
        return status;
    }
    
    public void setStatus(byte status)
    {
        this.status = status;
    }
    
    public String getTeamEndTime()
    {
        return teamEndTime;
    }
    
    public void setTeamEndTime(String teamEndTime)
    {
        this.teamEndTime = teamEndTime;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public int getValidTime()
    {
        return validTime;
    }
    
    public void setValidTime(int validTime)
    {
        this.validTime = validTime;
    }
}
