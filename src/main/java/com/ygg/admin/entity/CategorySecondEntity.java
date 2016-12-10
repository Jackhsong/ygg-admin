package com.ygg.admin.entity;

public class CategorySecondEntity
{
    private int id;
    
    private int categoryFirstId;
    
    private String name = "";
    
    private int sequence;
    
    /**子类目排序规则，1按子类目商品销量排序，2按子类目sequence排序*/
    private int orderType;
    
    private String createTime;
    
    private int isAvailable;
    
    private int isDisplay;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
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
    
    public int getSequence()
    {
        return sequence;
    }
    
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getCategoryFirstId()
    {
        return categoryFirstId;
    }
    
    public void setCategoryFirstId(int categoryFirstId)
    {
        this.categoryFirstId = categoryFirstId;
    }
    
    public int getOrderType()
    {
        return orderType;
    }
    
    public void setOrderType(int orderType)
    {
        this.orderType = orderType;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
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
