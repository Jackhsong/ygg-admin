package com.ygg.admin.entity;

public class MemberLevelProductEntity
{
    private int id;
    
    private int memberLevelId;
    
    private int productId;
    
    private int level;
    
    private float point;
    
    private int sequence;
    
    private int limitNum;
    
    private int isSupportCashBuy;
    
    private int isDisplay;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getMemberLevelId()
    {
        return memberLevelId;
    }
    
    public void setMemberLevelId(int memberLevelId)
    {
        this.memberLevelId = memberLevelId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    public float getPoint()
    {
        return point;
    }
    
    public void setPoint(float point)
    {
        this.point = point;
    }
    
    public int getSequence()
    {
        return sequence;
    }
    
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    
    public int getLimitNum()
    {
        return limitNum;
    }
    
    public void setLimitNum(int limitNum)
    {
        this.limitNum = limitNum;
    }
    
    public int getIsSupportCashBuy()
    {
        return isSupportCashBuy;
    }
    
    public void setIsSupportCashBuy(int isSupportCashBuy)
    {
        this.isSupportCashBuy = isSupportCashBuy;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
}
