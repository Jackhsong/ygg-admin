package com.ygg.admin.entity;

public class ActivitiesOptionalPartEntity
{
    private Integer id;
    
    private String name;
    
    /** 满减类型；1：全场，2：通用专场，3：网页自定义活动，4：原生自定义活动 */
    private Integer type;
    
    private Integer typeId;
    
    private String startTime;
    
    private String endTime;
    
    private Integer price;
    
    private Integer count;
    
    private Integer isAvailable;
    
    private String createTime;
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Integer getType()
    {
        return type;
    }
    
    public void setType(Integer type)
    {
        this.type = type;
    }
    
    public Integer getTypeId()
    {
        return typeId;
    }
    
    public void setTypeId(Integer typeId)
    {
        this.typeId = typeId;
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
    
    public Integer getPrice()
    {
        return price;
    }
    
    public void setPrice(Integer price)
    {
        this.price = price;
    }
    
    public Integer getCount()
    {
        return count;
    }
    
    public void setCount(Integer count)
    {
        this.count = count;
    }
    
    public Integer getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(Integer isAvailable)
    {
        this.isAvailable = isAvailable;
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
