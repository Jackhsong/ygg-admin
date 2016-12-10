package com.ygg.admin.entity;

public class CategoryThirdEntity
{
    
    private int id;
    
    private int firstCategoryId;
    
    private int secondCategoryId;
    
    private String name;
    
    private int sequence;
    
    private int isHot;
    
    private int isHighlight;
    
    private int isAvailable;
    
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getFirstCategoryId()
    {
        return firstCategoryId;
    }
    
    public void setFirstCategoryId(int firstCategoryId)
    {
        this.firstCategoryId = firstCategoryId;
    }
    
    public int getSecondCategoryId()
    {
        return secondCategoryId;
    }
    
    public void setSecondCategoryId(int secondCategoryId)
    {
        this.secondCategoryId = secondCategoryId;
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
    
    public int getIsHot()
    {
        return isHot;
    }
    
    public void setIsHot(int isHot)
    {
        this.isHot = isHot;
    }
    
    public int getIsHighlight()
    {
        return isHighlight;
    }
    
    public void setIsHighlight(int isHighlight)
    {
        this.isHighlight = isHighlight;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
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
