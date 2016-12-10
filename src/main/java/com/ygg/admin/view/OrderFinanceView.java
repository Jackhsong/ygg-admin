package com.ygg.admin.view;

import java.util.List;

public class OrderFinanceView
{
    /****************************************************************************** 订单信息 */
    /** 订单类型 */
    private String orderType = "正常订单";

    /** 订单渠道 */
    private String orderChannel = "";
    
    /** 订单ID */
    private int id;
    
    private String number;

    /** 合并订单号 */
    private String hbNumber = "";
    
    /** 订单状态 */
    private String status = "";
    
    /** 订单结算状态 */
    private String settlement = "";
    
    /**订单结算时间*/
    private String settlementTime = "";
    
    /** 订单付款渠道 */
    private String payChannel = "";
    
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

    /** 满减优惠金额 */
    private String activitiesPrice = "";
    
    /** 客服调价金额 全部为正值，代表减去多少money */
    private String adjustPrice = "";
    
    private int freightMoney;
    
    /** 客服备注 */
    private String buyerRemark = "";
    
    /** 卖家备注 */
    private String sellerRemark = "";
    
    /******************************************************************************* 收货人信息 */
    private String receiveFullName = "";
    
    private String idCard = "";
    
    private String receiveAddress = "";
    
    private String province = "";
    
    private String city = "";
    
    private String district = "";
    
    private String detailAddress = "";
    
    private String mobileNumber = "";
    
    /******************************************************************************** 商家信息 */
    private String sellerName = "";
    
    private String sendAddress = "";
    
    /******************************************************************************** 物流信息 */
    private String logisticsChannel = "";
    
    private String logisticsNumber = "";
    
    /******************************************************************************** 购买用户信息 */
    /** 账号类型；1：手机用户，2：QQ用户，3：微信用户，4：新浪用户，5：支付宝用户 */
    private String userType = "";
    
    private String name = "";
    
    /******************************************************************************** 运费信息 */
    
    private String postage = "";
    
    /** 运费结算状态 */
    private String postageSettlementStatus = "";
    
    private String postageConfirmDate = "";

    /******************************************************************************** 超时罚款结算信息 */

    /** 是否超时 */
    private String isTimeOut = "否";

    /** 罚款金额 */
    private String penaltyMoney = "";

    /** 是否已罚款 */
    private String isPenalty = "";

    /** 罚款时间 */
    private String penaltyTime = "";

    /******************************************************************************** 订单商品信息 */
    List<LineItemView> lineItems;

    public String getActivitiesPrice()
    {
        return activitiesPrice;
    }

    public void setActivitiesPrice(String activitiesPrice)
    {
        this.activitiesPrice = activitiesPrice;
    }

    public String getPostageConfirmDate()
    {
        return postageConfirmDate;
    }
    
    public void setPostageConfirmDate(String postageConfirmDate)
    {
        this.postageConfirmDate = postageConfirmDate;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    public int getFreightMoney()
    {
        return freightMoney;
    }
    
    public void setFreightMoney(int freightMoney)
    {
        this.freightMoney = freightMoney;
    }
    
    public String getReceiveAddress()
    {
        return receiveAddress;
    }
    
    public void setReceiveAddress(String receiveAddress)
    {
        this.receiveAddress = receiveAddress;
    }
    
    public String getOrderType()
    {
        return orderType;
    }
    
    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getSettlement()
    {
        return settlement;
    }
    
    public void setSettlement(String settlement)
    {
        this.settlement = settlement;
    }
    
    public String getSettlementTime()
    {
        return settlementTime;
    }
    
    public void setSettlementTime(String settlementTime)
    {
        this.settlementTime = settlementTime;
    }
    
    public String getPayChannel()
    {
        return payChannel;
    }
    
    public void setPayChannel(String payChannel)
    {
        this.payChannel = payChannel;
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
    
    public String getBuyerRemark()
    {
        return buyerRemark;
    }
    
    public void setBuyerRemark(String buyerRemark)
    {
        this.buyerRemark = buyerRemark;
    }
    
    public String getSellerRemark()
    {
        return sellerRemark;
    }
    
    public void setSellerRemark(String sellerRemark)
    {
        this.sellerRemark = sellerRemark;
    }
    
    public String getReceiveFullName()
    {
        return receiveFullName;
    }
    
    public void setReceiveFullName(String receiveFullName)
    {
        this.receiveFullName = receiveFullName;
    }
    
    public String getIdCard()
    {
        return idCard;
    }
    
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }
    
    public String getProvince()
    {
        return province;
    }
    
    public void setProvince(String province)
    {
        this.province = province;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getDistrict()
    {
        return district;
    }
    
    public void setDistrict(String district)
    {
        this.district = district;
    }
    
    public String getDetailAddress()
    {
        return detailAddress;
    }
    
    public void setDetailAddress(String detailAddress)
    {
        this.detailAddress = detailAddress;
    }
    
    public String getMobileNumber()
    {
        return mobileNumber;
    }
    
    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
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
    
    public String getLogisticsChannel()
    {
        return logisticsChannel;
    }
    
    public void setLogisticsChannel(String logisticsChannel)
    {
        this.logisticsChannel = logisticsChannel;
    }
    
    public String getLogisticsNumber()
    {
        return logisticsNumber;
    }
    
    public void setLogisticsNumber(String logisticsNumber)
    {
        this.logisticsNumber = logisticsNumber;
    }
    
    public String getUserType()
    {
        return userType;
    }
    
    public void setUserType(String userType)
    {
        this.userType = userType;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPostage()
    {
        return postage;
    }
    
    public void setPostage(String postage)
    {
        this.postage = postage;
    }
    
    public String getPostageSettlementStatus()
    {
        return postageSettlementStatus;
    }
    
    public void setPostageSettlementStatus(String postageSettlementStatus)
    {
        this.postageSettlementStatus = postageSettlementStatus;
    }
    
    public List<LineItemView> getLineItems()
    {
        return lineItems;
    }
    
    public void setLineItems(List<LineItemView> lineItems)
    {
        this.lineItems = lineItems;
    }

    public String getHbNumber()
    {
        return hbNumber;
    }

    public void setHbNumber(String hbNumber)
    {
        this.hbNumber = hbNumber;
    }

    public String getOrderChannel()
    {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel)
    {
        this.orderChannel = orderChannel;
    }

    public String getIsTimeOut()
    {
        return isTimeOut;
    }
    
    public void setIsTimeOut(String isTimeOut)
    {
        this.isTimeOut = isTimeOut;
    }
    
    public String getPenaltyMoney()
    {
        return penaltyMoney;
    }
    
    public void setPenaltyMoney(String penaltyMoney)
    {
        this.penaltyMoney = penaltyMoney;
    }
    
    public String getIsPenalty()
    {
        return isPenalty;
    }
    
    public void setIsPenalty(String isPenalty)
    {
        this.isPenalty = isPenalty;
    }
    
    public String getPenaltyTime()
    {
        return penaltyTime;
    }
    
    public void setPenaltyTime(String penaltyTime)
    {
        this.penaltyTime = penaltyTime;
    }
}
