package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class GlobalSaleWindowEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int displayId;
    
    private byte type;
    
    private String name;
    
    private String desc;
    
    private String image;
    
    private String startTime;
    
    private String endTime;
    
    private byte isDisplay;
    
    private short order;
    
    private String createTime;
    
    private String updateTime;
    
    /** 特卖国旗Id*/
    private int saleFlagId;
    
    /**是否展示国旗；0：否，1：是*/
    private int isDisplayFlag;

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
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
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
    
    public int getSaleFlagId()
    {
        return saleFlagId;
    }
    
    public void setSaleFlagId(int saleFlagId)
    {
        this.saleFlagId = saleFlagId;
    }
    
    public int getIsDisplayFlag()
    {
        return isDisplayFlag;
    }
    
    public void setIsDisplayFlag(int isDisplayFlag)
    {
        this.isDisplayFlag = isDisplayFlag;
    }
    
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public String toString()
    {
        return id + "" + name;
    }
    
}
