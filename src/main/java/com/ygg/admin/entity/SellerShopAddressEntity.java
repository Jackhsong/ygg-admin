package com.ygg.admin.entity;

public class SellerShopAddressEntity
{
    private int shopId;
    
    private int sellerId;
    
    private String shopURL;
    
    public int getShopId()
    {
        return shopId;
    }
    
    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public String getShopURL()
    {
        return shopURL;
    }
    
    public void setShopURL(String shopURL)
    {
        this.shopURL = shopURL;
    }
    
}
