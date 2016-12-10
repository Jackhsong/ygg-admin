package com.ygg.admin.entity;

public class MwebGroupImageDetailEntity
{
    private int id;
    
    private int mwebGroupProductId;
    
    private String url;
    
    private int height;
    
    private int width;
    
    private byte order;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getMwebGroupProductId()
    {
        return mwebGroupProductId;
    }
    
    public void setMwebGroupProductId(int mwebGroupProductId)
    {
        this.mwebGroupProductId = mwebGroupProductId;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public byte getOrder()
    {
        return order;
    }
    
    public void setOrder(byte order)
    {
        this.order = order;
    }
}
