package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SaleWindowEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    /**原生自定义页面2模块id*/
    private int page2ModelId;
    
    /**添加来源类型；1：首页特卖，2：原生自定义页面2-特卖模块*/
    private int sourceType;
    
    private int displayId;
    
    private byte type;
    
    private String name;
    
    private String desc;
    
    private String image;
    
    private String newImage;
    
    private String newImage17;
    
    private int startTime;
    
    private int endTime;
    
    private byte isDisplay;
    
    private short nowOrder;
    
    private short laterOrder;
    
    private String createTime;
    
    private String updateTime;
    
    /** 特卖国旗Id*/
    private int saleFlagId;
    
    /**是否展示国旗；0：否，1：是*/
    private int isDisplayFlag;
    
    /** 促销场次类型；1：10点场，2：20点场 */
    private int saleTimeType = 1;
    
    /**所属分类一级分类ID  */
    private int categoryFirstId;
    
    /**品类馆排序*/
    private int categoryOrder;
    
    /**品类馆锁定序位*/
    private int categoryLockIndex;
    
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
    
    public int getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(int startTime)
    {
        this.startTime = startTime;
    }
    
    public int getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(int endTime)
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
    
    public short getNowOrder()
    {
        return nowOrder;
    }
    
    public void setNowOrder(short nowOrder)
    {
        this.nowOrder = nowOrder;
    }
    
    public short getLaterOrder()
    {
        return laterOrder;
    }
    
    public void setLaterOrder(short laterOrder)
    {
        this.laterOrder = laterOrder;
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
    
    public String getNewImage()
    {
        return newImage;
    }
    
    public void setNewImage(String newImage)
    {
        this.newImage = newImage;
    }
    
    public String getNewImage17()
    {
        return newImage17;
    }
    
    public void setNewImage17(String newImage17)
    {
        this.newImage17 = newImage17;
    }
    
    @Override
    public String toString()
    {
        return id + "" + name;
    }
    
    /**  
     *@return  the categoryFirstId
     */
    public int getCategoryFirstId()
    {
        return categoryFirstId;
    }
    
    /** 
     * @param categoryFirstId the categoryFirstId to set
     */
    public void setCategoryFirstId(int categoryFirstId)
    {
        this.categoryFirstId = categoryFirstId;
    }
    
    /**  
     *@return  the page2ModelId
     */
    public int getPage2ModelId()
    {
        return page2ModelId;
    }
    
    /** 
     * @param page2ModelId the page2ModelId to set
     */
    public void setPage2ModelId(int page2ModelId)
    {
        this.page2ModelId = page2ModelId;
    }
    
    /**  
     *@return  the sourceType
     */
    public int getSourceType()
    {
        return sourceType;
    }
    
    /** 
     * @param sourceType the sourceType to set
     */
    public void setSourceType(int sourceType)
    {
        this.sourceType = sourceType;
    }
    
    public int getCategoryOrder()
    {
        return categoryOrder;
    }
    
    public void setCategoryOrder(int categoryOrder)
    {
        this.categoryOrder = categoryOrder;
    }
    
    public int getCategoryLockIndex()
    {
        return categoryLockIndex;
    }
    
    public void setCategoryLockIndex(int categoryLockIndex)
    {
        this.categoryLockIndex = categoryLockIndex;
    }
    
}
