package com.ygg.admin.entity;

public class OrderLogistics extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int orderId;
    
    private String channel;
    
    private String number;
    
    private float money;
    
    private byte status;
    
    private String createTime;
    
    /**
     * 是否订阅成功
     */
    private byte isSubscribed;
    
    /**
     * 发起订阅的次数
     */
    private byte subscribeCount;
    
    /**
     * 是否为假单
     */
    private byte isFake;
    
    /**
     * 订单跟踪状态
     */
    private String trackInfo;
    
    /**快递服务商类型，1：快递100，2：快递吧*/
    private int serviceType;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }
    
    public String getChannel()
    {
        return channel;
    }
    
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public float getMoney()
    {
        return money;
    }
    
    public void setMoney(float money)
    {
        this.money = money;
    }
    
    public byte getStatus()
    {
        return status;
    }
    
    public void setStatus(byte status)
    {
        this.status = status;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public byte getIsSubscribed()
    {
        return isSubscribed;
    }
    
    public void setIsSubscribed(byte isSubscribed)
    {
        this.isSubscribed = isSubscribed;
    }
    
    public byte getSubscribeCount()
    {
        return subscribeCount;
    }
    
    public void setSubscribeCount(byte subscribeCount)
    {
        this.subscribeCount = subscribeCount;
    }
    
    public byte getIsFake()
    {
        return isFake;
    }
    
    public void setIsFake(byte isFake)
    {
        this.isFake = isFake;
    }
    
    public String getTrackInfo()
    {
        return trackInfo;
    }
    
    public void setTrackInfo(String trackInfo)
    {
        this.trackInfo = trackInfo;
    }
    
    public int getServiceType()
    {
        return serviceType;
    }
    
    public void setServiceType(int serviceType)
    {
        this.serviceType = serviceType;
    }
    
}
