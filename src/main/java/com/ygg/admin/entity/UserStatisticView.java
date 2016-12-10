package com.ygg.admin.entity;

import java.util.Comparator;


public class UserStatisticView
{
    /**业务线*/
    private int type;
    
    /**业务线名称*/
    private String typeName;
    
    /** 平台*/
    private String platform;
    
    /** 渠道 */
    private int appChannel;
    
    /**渠道名称*/
    private String appChannelName;
    
    private int userCount;
    
    /**注册用户数*/
    private int registUserCount;
    
    /**成交用户数*/
    private int orderUserCount;
    
    /**累计成交额*/
    private float totalPrice;
    
    /**For 0.00*/
    private String totalPriceDisplay;
    
    /**排序*/
    private int sort;
    

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getPlatform()
    {
        return platform;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public int getAppChannel()
    {
        return appChannel;
    }

    public void setAppChannel(int appChannel)
    {
        this.appChannel = appChannel;
    }

    public int getUserCount()
    {
        return userCount;
    }

    public void setUserCount(int userCount)
    {
        this.userCount = userCount;
    }

    public float getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public String getAppChannelName()
    {
        return appChannelName;
    }

    public void setAppChannelName(String appChannelName)
    {
        this.appChannelName = appChannelName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public String getTotalPriceDisplay()
    {
        return totalPriceDisplay;
    }

    public void setTotalPriceDisplay(String totalPriceDisplay)
    {
        this.totalPriceDisplay = totalPriceDisplay;
    }

    public int getRegistUserCount()
    {
        return registUserCount;
    }

    public void setRegistUserCount(int registUserCount)
    {
        this.registUserCount = registUserCount;
    }

    public int getOrderUserCount()
    {
        return orderUserCount;
    }

    public void setOrderUserCount(int orderUserCount)
    {
        this.orderUserCount = orderUserCount;
    }

    
}
