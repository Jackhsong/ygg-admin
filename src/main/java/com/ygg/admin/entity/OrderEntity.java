package com.ygg.admin.entity;

public class OrderEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String number;
    
    private int accountId;
    
    private int receiveAddressId;
    
    private float totalPrice;
    
    /**商家备注*/
    private String remark;
    
    /**客服备注*/
    private String remark2;
    
    private byte status;
    
    private String createTime;
    
    private String payTime;
    
    private String sendTime;
    
    private String receiveTime;
    
    private short freightMoney;
    
    private int sellerId;
    
    private int payChannel;
    
    private int isDelete;
    
    private int updateTime;
    
    private int operationStatus;
    
    private int orderSellerId;
    
    private int accountCouponId;
    
    private float couponPrice;
    
    private float realPrice;
    
    private float adjustPrice;
    
    private int accountPoint;
    
    private String appVersion;
    
    private String appChannel;
    
    /** 是否结算：1:是；0:否 */
    private int isSettlement;
    
    /** 是否冻结：1:是；0:否 */
    private int isFreeze;
    
    /** 邮费是否已经结算 */
    private int postageIsSettlement;
    
    private int isNeedSettlement;
    
    private int isMemberOrder;
    
    /** 1：左岸城堡订单，2：左岸城堡订单 */
    private int type;
    
    // 订单参与的满减金额，减去的金额
    private float activitiesPrice;
    
    //发货是否超时
    private int isTimeOut;
    
    // N元任选优惠金额
    private float activitiesOptionalPartPrice;

    //同一批次订单号
    private String sameBatchNumber;

    public float getActivitiesOptionalPartPrice()
    {
        return activitiesOptionalPartPrice;
    }
    
    public void setActivitiesOptionalPartPrice(float activitiesOptionalPartPrice)
    {
        this.activitiesOptionalPartPrice = activitiesOptionalPartPrice;
    }

    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public float getActivitiesPrice()
    {
        return activitiesPrice;
    }
    
    public void setActivitiesPrice(float activitiesPrice)
    {
        this.activitiesPrice = activitiesPrice;
    }
    
    public String getAppChannel()
    {
        return appChannel;
    }
    
    public void setAppChannel(String appChannel)
    {
        this.appChannel = appChannel;
    }
    
    public int getIsNeedSettlement()
    {
        return isNeedSettlement;
    }
    
    public void setIsNeedSettlement(int isNeedSettlement)
    {
        this.isNeedSettlement = isNeedSettlement;
    }
    
    public String getAppVersion()
    {
        return appVersion;
    }
    
    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }
    
    public int getPostageIsSettlement()
    {
        return postageIsSettlement;
    }
    
    public void setPostageIsSettlement(int postageIsSettlement)
    {
        this.postageIsSettlement = postageIsSettlement;
    }
    
    public int getIsFreeze()
    {
        return isFreeze;
    }
    
    public void setIsFreeze(int isFreeze)
    {
        this.isFreeze = isFreeze;
    }
    
    public int getOrderSellerId()
    {
        return orderSellerId;
    }
    
    public void setOrderSellerId(int orderSellerId)
    {
        this.orderSellerId = orderSellerId;
    }
    
    public int getAccountCouponId()
    {
        return accountCouponId;
    }
    
    public void setAccountCouponId(int accountCouponId)
    {
        this.accountCouponId = accountCouponId;
    }
    
    public float getCouponPrice()
    {
        return couponPrice;
    }
    
    public void setCouponPrice(float couponPrice)
    {
        this.couponPrice = couponPrice;
    }
    
    public float getRealPrice()
    {
        return realPrice;
    }
    
    public void setRealPrice(float realPrice)
    {
        this.realPrice = realPrice;
    }
    
    public float getAdjustPrice()
    {
        return adjustPrice;
    }
    
    public void setAdjustPrice(float adjustPrice)
    {
        this.adjustPrice = adjustPrice;
    }
    
    public int getAccountPoint()
    {
        return accountPoint;
    }
    
    public void setAccountPoint(int accountPoint)
    {
        this.accountPoint = accountPoint;
    }
    
    public int getOperationStatus()
    {
        return operationStatus;
    }
    
    public void setOperationStatus(int operationStatus)
    {
        this.operationStatus = operationStatus;
    }
    
    public short getFreightMoney()
    {
        return freightMoney;
    }
    
    public void setFreightMoney(short freightMoney)
    {
        this.freightMoney = freightMoney;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public int getAccountId()
    {
        return accountId;
    }
    
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    public int getReceiveAddressId()
    {
        return receiveAddressId;
    }
    
    public void setReceiveAddressId(int receiveAddressId)
    {
        this.receiveAddressId = receiveAddressId;
    }
    
    public float getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(float totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getRemark2()
    {
        return remark2;
    }
    
    public void setRemark2(String remark2)
    {
        this.remark2 = remark2;
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
    
    public String getPayTime()
    {
        return payTime;
    }
    
    public void setPayTime(String payTime)
    {
        this.payTime = payTime;
    }
    
    public String getSendTime()
    {
        return sendTime;
    }
    
    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    
    public String getReceiveTime()
    {
        return receiveTime;
    }
    
    public void setReceiveTime(String receiveTime)
    {
        this.receiveTime = receiveTime;
    }
    
    public int getPayChannel()
    {
        return payChannel;
    }
    
    public void setPayChannel(int payChannel)
    {
        this.payChannel = payChannel;
    }
    
    public int getIsDelete()
    {
        return isDelete;
    }
    
    public void setIsDelete(int isDelete)
    {
        this.isDelete = isDelete;
    }
    
    public void setUpdateTime(int updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public int getIsMemberOrder()
    {
        return isMemberOrder;
    }
    
    public void setIsMemberOrder(int isMemberOrder)
    {
        this.isMemberOrder = isMemberOrder;
    }
    
    public int getIsTimeOut()
    {
        return isTimeOut;
    }
    
    public void setIsTimeOut(int isTimeOut)
    {
        this.isTimeOut = isTimeOut;
    }

    public String getSameBatchNumber()
    {
        return sameBatchNumber;
    }
    
    public void setSameBatchNumber(String sameBatchNumber)
    {
        this.sameBatchNumber = sameBatchNumber;
    }

    @Override
    public String toString()
    {
        return id + "";
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof OrderEntity)
        {
            OrderEntity other = (OrderEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
}
