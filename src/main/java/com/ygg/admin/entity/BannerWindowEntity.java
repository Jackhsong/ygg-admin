package com.ygg.admin.entity;

public class BannerWindowEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int displayId;
    
    private byte type;
    
    private String desc;
    
    private String image;
    
    private String startTime;
    
    private String endTime;
    
    private byte isDisplay;
    
    private short order;
    
    private String createTime;
    
    private String updateTime;
    
    /**网页链接*/
    private String url;
    
    /** 促销场次类型；1：10点场，2：20点场 */
    private int saleTimeType = 1;
    
    public int getSaleTimeType()
    {
        return saleTimeType;
    }
    
    public void setSaleTimeType(int saleTimeType)
    {
        this.saleTimeType = saleTimeType;
    }
    
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
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
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
    
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
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
        if (obj instanceof BannerWindowEntity)
        {
            BannerWindowEntity other = (BannerWindowEntity)obj;
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
