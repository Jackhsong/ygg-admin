package com.ygg.admin.entity;

public class MwebGroupProductCountEntity
{
    private int id;
    
    // 商品id
    private int mwebGroupProductId;
    
    // 卖出数量
    private int sell;
    
    // 库存
    private int stock;
    
    // 锁定数量
    private int lock;
    
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
    
    public int getSell()
    {
        return sell;
    }
    
    public void setSell(int sell)
    {
        this.sell = sell;
    }
    
    public int getStock()
    {
        return stock;
    }
    
    public void setStock(int stock)
    {
        this.stock = stock;
    }
    
    public int getLock()
    {
        return lock;
    }
    
    public void setLock(int lock)
    {
        this.lock = lock;
    }
}
