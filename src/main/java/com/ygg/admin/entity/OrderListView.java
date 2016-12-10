package com.ygg.admin.entity;

public class OrderListView
{
    /****************************************************************************** 订单信息 */
    /** 订单类型  */
    private String orderType = "正常订单";
    
    /** 订单ID */
    private int id;
    
    private long number;
    
    /** 订单状态 */
    private int status;
    
    private String statusStr = "";

    private int isNeedSettlement = 1;

    /** 订单结算状态 */
    private int isSettlement;
    
    private String isSettlementStr = "";
    
    private String createTime = "";
    
    private String payTime = "";
    
    private String sendTime = "";
    
    /** 订单总价 */
    private String totalPrice = "";
    
    /** 订单实付金额 */
    private String realPrice = "";
    
    /** 积分优惠金额 */
    private String accountPointPrice = "";
    
    /** 优惠券优惠金额 */
    private String couponPrice = "";
    
    /** 客服调价金额 全部为正值，代表减去多少money */
    private String adjustPrice = "";
    
    private String sellerName = "";
    
    private String sendAddress = "";
    
    //******************  运费结算状态  *************** 
    
    /**   邮费是否结算  0：否，1：是 */
    private int postageIsSettlement;
    
    private String postageIsSettlementStr = "";
    
    private String settlementFreightMoney = "";
    
    //*****************   退款结算状态 *******
    
    private String containsRefund = "";

    public int getIsNeedSettlement()
    {
        return isNeedSettlement;
    }

    public void setIsNeedSettlement(int isNeedSettlement)
    {
        this.isNeedSettlement = isNeedSettlement;
    }

    public String getOrderType()
    {
        return orderType;
    }
    
    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }
    
    public String getSettlementFreightMoney()
    {
        return settlementFreightMoney;
    }
    
    public void setSettlementFreightMoney(String settlementFreightMoney)
    {
        this.settlementFreightMoney = settlementFreightMoney;
    }
    
    public int getPostageIsSettlement()
    {
        return postageIsSettlement;
    }
    
    public void setPostageIsSettlement(int postageIsSettlement)
    {
        this.postageIsSettlement = postageIsSettlement;
    }
    
    public String getPostageIsSettlementStr()
    {
        return postageIsSettlementStr;
    }
    
    public void setPostageIsSettlementStr(String postageIsSettlementStr)
    {
        this.postageIsSettlementStr = postageIsSettlementStr;
    }
    
    public String getContainsRefund()
    {
        return containsRefund;
    }
    
    public void setContainsRefund(String containsRefund)
    {
        this.containsRefund = containsRefund;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public long getNumber()
    {
        return number;
    }
    
    public void setNumber(long number)
    {
        this.number = number;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public String getStatusStr()
    {
        return statusStr;
    }
    
    public void setStatusStr(String statusStr)
    {
        this.statusStr = statusStr;
    }
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public String getIsSettlementStr()
    {
        return isSettlementStr;
    }
    
    public void setIsSettlementStr(String isSettlementStr)
    {
        this.isSettlementStr = isSettlementStr;
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
    
    public String getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public String getRealPrice()
    {
        return realPrice;
    }
    
    public void setRealPrice(String realPrice)
    {
        this.realPrice = realPrice;
    }
    
    public String getAccountPointPrice()
    {
        return accountPointPrice;
    }
    
    public void setAccountPointPrice(String accountPointPrice)
    {
        this.accountPointPrice = accountPointPrice;
    }
    
    public String getCouponPrice()
    {
        return couponPrice;
    }
    
    public void setCouponPrice(String couponPrice)
    {
        this.couponPrice = couponPrice;
    }
    
    public String getAdjustPrice()
    {
        return adjustPrice;
    }
    
    public void setAdjustPrice(String adjustPrice)
    {
        this.adjustPrice = adjustPrice;
    }
    
    public String getSellerName()
    {
        return sellerName;
    }
    
    public void setSellerName(String sellerName)
    {
        this.sellerName = sellerName;
    }
    
    public String getSendAddress()
    {
        return sendAddress;
    }
    
    public void setSendAddress(String sendAddress)
    {
        this.sendAddress = sendAddress;
    }
    
}
