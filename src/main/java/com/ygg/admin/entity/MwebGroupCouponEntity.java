package com.ygg.admin.entity;

public class MwebGroupCouponEntity
{
    /** 优惠券id */
    private int id;
    
    /** 优惠券详情id */
    private int couponDetailId;
    
    /** 优惠券总数 */
    private int total;
    
    /** 优惠券批次类型；1：单个，2：批量 */
    private int type;
    
    /** 优惠券有效期起始时间 */
    private String startTime;
    
    /** 优惠券有效期结束时间 */
    private String endTime;
    
    /** 备注 */
    private String remark;
    
    /** 创建时间 */
    private String createTime;
    
    /** 更新时间 */
    private String updateTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getCouponDetailId()
    {
        return couponDetailId;
    }
    
    public void setCouponDetailId(int couponDetailId)
    {
        this.couponDetailId = couponDetailId;
    }
    
    public int getTotal()
    {
        return total;
    }
    
    public void setTotal(int total)
    {
        this.total = total;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
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
}
