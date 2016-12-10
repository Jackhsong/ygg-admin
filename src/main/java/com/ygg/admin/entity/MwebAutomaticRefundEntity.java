package com.ygg.admin.entity;

public class MwebAutomaticRefundEntity
{
    private int id;
    
    private int orderId;
    
    private String number;
    
    private byte orderChannel;
    
    private int accountId;
    
    private String orderTime;
    
    private String payTime;
    
    private String payTid;
    
    private String payMark;
    
    private byte payChannel;
    
    private float realPrice;
    
    private float totalPrice;
    
    private float refundPrice;
    
    private String paymentAccount;
    
    private String createTime;
    
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
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public byte getOrderChannel()
    {
        return orderChannel;
    }
    
    public void setOrderChannel(byte orderChannel)
    {
        this.orderChannel = orderChannel;
    }
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public String getOrderTime()
    {
        return orderTime;
    }
    
    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
    }
    
    public String getPayTime()
    {
        return payTime;
    }
    
    public void setPayTime(String payTime)
    {
        this.payTime = payTime;
    }
    
    public String getPayTid()
    {
        return payTid;
    }
    
    public void setPayTid(String payTid)
    {
        this.payTid = payTid;
    }
    
    public String getPayMark()
    {
        return payMark;
    }
    
    public void setPayMark(String payMark)
    {
        this.payMark = payMark;
    }
    
    public byte getPayChannel()
    {
        return payChannel;
    }
    
    public void setPayChannel(byte payChannel)
    {
        this.payChannel = payChannel;
    }
    
    public float getRealPrice()
    {
        return realPrice;
    }
    
    public void setRealPrice(float realPrice)
    {
        this.realPrice = realPrice;
    }
    
    public float getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public float getRefundPrice()
    {
        return refundPrice;
    }
    
    public void setRefundPrice(float refundPrice)
    {
        this.refundPrice = refundPrice;
    }
    
    public String getPaymentAccount()
    {
        return paymentAccount;
    }
    
    public void setPaymentAccount(String paymentAccount)
    {
        this.paymentAccount = paymentAccount;
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
