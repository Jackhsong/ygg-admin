package com.ygg.admin.entity;

public class OverseasDelayRemindDateEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = -676141458196711808L;
    
    private int id;
    
    private String day;
    
    private int startHour;
    
    private int endHour;
    
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getDay()
    {
        return day;
    }
    
    public void setDay(String day)
    {
        this.day = day;
    }
    
    public int getStartHour()
    {
        return startHour;
    }
    
    public void setStartHour(int startHour)
    {
        this.startHour = startHour;
    }
    
    public int getEndHour()
    {
        return endHour;
    }
    
    public void setEndHour(int endHour)
    {
        this.endHour = endHour;
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
