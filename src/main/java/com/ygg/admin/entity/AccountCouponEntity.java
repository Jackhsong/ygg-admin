package com.ygg.admin.entity;

public class AccountCouponEntity
{
    /**账号优惠券id*/
    private int id;
    
    /**账号id*/
    private int accountId;
    
    /**优惠券来源类型；1：优惠券发放，2：优惠码兑换，3：抽奖所得，4：签到奖励，5：小伙伴推荐奖励，6：分享礼包，7:玩游戏领取，8：推广渠道领取，9：任意门领取，10：订单取消返还*/
    private int sourceType;
    
    /**优惠券Id*/
    private int couponId;
    
    /**优惠码Id,sourceType=2时使用*/
    private int couponCodeId;
    
    /**优惠券详情Id*/
    private int couponDetailId;
    
    /**有效期起*/
    private String startTime;
    
    /**有效期止*/
    private String endTime;
    
    /**备注*/
    private String remark;
    
    /**是否使用；0：否，1：是*/
    private int isUsed;
    
    /**创建时间*/
    private String createTime;
    
    /**更新时间*/
    private String updateTime;
    
    /**优惠金额，随机优惠券该值才有效*/
    private int reducePrice;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public int getSourceType()
    {
        return sourceType;
    }
    
    public void setSourceType(int sourceType)
    {
        this.sourceType = sourceType;
    }
    
    public int getCouponId()
    {
        return couponId;
    }
    
    public void setCouponId(int couponId)
    {
        this.couponId = couponId;
    }
    
    public int getCouponCodeId()
    {
        return couponCodeId;
    }
    
    public void setCouponCodeId(int couponCodeId)
    {
        this.couponCodeId = couponCodeId;
    }
    
    public int getCouponDetailId()
    {
        return couponDetailId;
    }
    
    public void setCouponDetailId(int couponDetailId)
    {
        this.couponDetailId = couponDetailId;
    }
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public int getIsUsed()
    {
        return isUsed;
    }
    
    public void setIsUsed(int isUsed)
    {
        this.isUsed = isUsed;
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
    
    public int getReducePrice()
    {
        return reducePrice;
    }
    
    public void setReducePrice(int reducePrice)
    {
        this.reducePrice = reducePrice;
    }
    
}
