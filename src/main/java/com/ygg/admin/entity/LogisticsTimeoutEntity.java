package com.ygg.admin.entity;

public class LogisticsTimeoutEntity
{
    private int id;
    
    private int sellerId;
    
    private int orderId;
    
    private String sendTime;
    
    private String expireTime;
    
    /**物流更新时间*/
    private String logisticsTime;
    
    private String logisticsNumber;
    
    private String logisticsCompany;
    
    private int isTimeout;
    
    private int complainStatus;
    
    private int logisticsTimeoutReasonId;
    
    private int dealUser;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public int getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }
    
    public String getSendTime()
    {
        return sendTime;
    }
    
    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    
    public String getExpireTime()
    {
        return expireTime;
    }
    
    public void setExpireTime(String expireTime)
    {
        this.expireTime = expireTime;
    }
    
    public String getLogisticsTime()
    {
        return logisticsTime;
    }
    
    public void setLogisticsTime(String logisticsTime)
    {
        this.logisticsTime = logisticsTime;
    }
    
    public String getLogisticsNumber()
    {
        return logisticsNumber;
    }
    
    public void setLogisticsNumber(String logisticsNumber)
    {
        this.logisticsNumber = logisticsNumber;
    }
    
    public String getLogisticsCompany()
    {
        return logisticsCompany;
    }
    
    public void setLogisticsCompany(String logisticsCompany)
    {
        this.logisticsCompany = logisticsCompany;
    }
    
    public int getIsTimeout()
    {
        return isTimeout;
    }
    
    public void setIsTimeout(int isTimeout)
    {
        this.isTimeout = isTimeout;
    }
    
    public int getComplainStatus()
    {
        return complainStatus;
    }
    
    public void setComplainStatus(int complainStatus)
    {
        this.complainStatus = complainStatus;
    }
    
    public int getLogisticsTimeoutReasonId()
    {
        return logisticsTimeoutReasonId;
    }
    
    public void setLogisticsTimeoutReasonId(int logisticsTimeoutReasonId)
    {
        this.logisticsTimeoutReasonId = logisticsTimeoutReasonId;
    }
    
    public int getDealUser()
    {
        return dealUser;
    }
    
    public void setDealUser(int dealUser)
    {
        this.dealUser = dealUser;
    }
    
}
