package com.ygg.admin.entity;

public class AccountSuccessOrderRecordEntity
{
    private static final long serialVersionUID = 1L;

    private int id;

    private int accountId;

    private int orderId;

    private float realPrice;

    private float totalRealPrice;

    private int operateType;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getAccountId()
    {
        return accountId;
    }

    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public float getRealPrice()
    {
        return realPrice;
    }

    public void setRealPrice(float realPrice)
    {
        this.realPrice = realPrice;
    }

    public float getTotalRealPrice()
    {
        return totalRealPrice;
    }

    public void setTotalRealPrice(float totalRealPrice)
    {
        this.totalRealPrice = totalRealPrice;
    }

    public int getOperateType()
    {
        return operateType;
    }

    public void setOperateType(int operateType)
    {
        this.operateType = operateType;
    }
    
}
