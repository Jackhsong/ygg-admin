package com.ygg.admin.view;

public class ClientDetailView
{
    /**成交金额*/
    private String totalPrice;
    
    /**成交金额占比*/
    private String totalPricePercent;
    
    /**成交人数*/
    private String totalPersonCount;
    
    /**成交人数占比*/
    private String totalPersonCountPercent;
    
    /**客单价*/
    private String divPersonPrice;
    
    /**成交订单数*/
    private String totalOrderCount;
    
    /**笔单价*/
    private String divOrderPrice;
    
    /**类型：1: IOS，2：IOS马甲，3：Android,4：Web*/
    private int type;
    
    /**版本号*/
    private String version;
    
    /**类型版本号*/
    private String typeVersion;
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getTotalOrderCount()
    {
        return totalOrderCount;
    }
    
    public void setTotalOrderCount(String totalOrderCount)
    {
        this.totalOrderCount = totalOrderCount;
    }
    
    public String getTotalPersonCount()
    {
        return totalPersonCount;
    }
    
    public void setTotalPersonCount(String totalPersonCount)
    {
        this.totalPersonCount = totalPersonCount;
    }
    
    public String getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public String getTotalPricePercent()
    {
        return totalPricePercent;
    }
    
    public void setTotalPricePercent(String totalPricePercent)
    {
        this.totalPricePercent = totalPricePercent;
    }
    
    public String getTotalPersonCountPercent()
    {
        return totalPersonCountPercent;
    }
    
    public void setTotalPersonCountPercent(String totalPersonCountPercent)
    {
        this.totalPersonCountPercent = totalPersonCountPercent;
    }
    
    public String getTypeVersion()
    {
        if (type == 1)
        {
            typeVersion = "IOS" + version;
        }
        else if (type == 2)
        {
            typeVersion = "IOS马甲" + version;
        }
        else if (type == 3)
        {
            typeVersion = "Android" + version;
        }
        else if (type == 4)
        {
            typeVersion = "web";
        }
        return typeVersion;
    }
    
    public void setTypeVersion(String typeVersion)
    {
        this.typeVersion = typeVersion;
    }
    
    public String getDivPersonPrice()
    {
        return divPersonPrice;
    }
    
    public void setDivPersonPrice(String divPersonPrice)
    {
        this.divPersonPrice = divPersonPrice;
    }
    
    public String getDivOrderPrice()
    {
        return divOrderPrice;
    }
    
    public void setDivOrderPrice(String divOrderPrice)
    {
        this.divOrderPrice = divOrderPrice;
    }
    
}
