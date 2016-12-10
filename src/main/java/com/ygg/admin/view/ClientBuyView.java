package com.ygg.admin.view;

public class ClientBuyView
{
    private int accountId;
    
    private String orderNumber;
    
    private int appChannel;
    
    private String appVersion;
    
    private float totalPrice;
    
    private float realPrice;
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public String getOrderNumber()
    {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber)
    {
        this.orderNumber = orderNumber;
    }
    
    public int getAppChannel()
    {
        return appChannel;
    }
    
    public void setAppChannel(int appChannel)
    {
        this.appChannel = appChannel;
    }
    
    public String getAppVersion()
    {
        return appVersion;
    }
    
    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }
    
    public float getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public float getRealPrice()
    {
        return realPrice;
    }
    
    public void setRealPrice(float realPrice)
    {
        this.realPrice = realPrice;
    }
    
}
