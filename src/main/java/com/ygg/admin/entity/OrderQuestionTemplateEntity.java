package com.ygg.admin.entity;

public class OrderQuestionTemplateEntity
{
    private int id;
    
    private String name;
    
    private float limitHour;
    
    private int isAvailable;
    
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
    
    public float getLimitHour()
    {
        return limitHour;
    }
    
    public void setLimitHour(float limitHour)
    {
        this.limitHour = limitHour;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
}
