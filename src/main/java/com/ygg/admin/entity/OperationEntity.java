package com.ygg.admin.entity;

public class OperationEntity
{
    private int id;
    
    private String dateCreated;
    
    private String orderId;
    
    private String autoMemo;
    
    private String mark;
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getDateCreated()
    {
        return dateCreated;
    }
    
    public void setDateCreated(String dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    
    public String getAutoMemo()
    {
        return autoMemo;
    }
    
    public void setAutoMemo(String autoMemo)
    {
        this.autoMemo = autoMemo;
    }
    
    public String getMark()
    {
        return mark;
    }
    
    public void setMark(String mark)
    {
        this.mark = mark;
    }
    
}
