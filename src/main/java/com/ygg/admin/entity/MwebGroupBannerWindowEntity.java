package com.ygg.admin.entity;

public class MwebGroupBannerWindowEntity extends BaseEntity
{
    
    private static final long serialVersionUID = 3822588381201487751L;

    private int id;
    
    private int displayId;
    
    private String desc;
    
    private String image;
    
    private String startTime;
    
    private String endTime;
    
    private byte isDisplay;
    
    private short order;
    
    private String createTime;
    
    private String updateTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getDisplayId()
    {
        return displayId;
    }
    
    public void setDisplayId(int displayId)
    {
        this.displayId = displayId;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
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
    
    public byte getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(byte isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    public short getOrder()
    {
        return order;
    }
    
    public void setOrder(short order)
    {
        this.order = order;
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
    
    @Override
    public String toString()
    {
        return id + ":" + desc;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof MwebGroupBannerWindowEntity)
        {
            MwebGroupBannerWindowEntity other = (MwebGroupBannerWindowEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
    
}
