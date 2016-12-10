package com.ygg.admin.entity;

import java.util.List;
import java.util.Map;

public class OrderListDetailView
{
    /** 订单类型  */
    private String orderType = "正常订单";

    private int type;
    
    /** 订单ID */
    private int id;
    
    private long number;

    /** 合并订单号 */
    private String hbNumber = "";
    
    /** 订单状态 */
    private int status;
    
    /** 订单结算状态 */
    private int isSettlement;
    
    private String settlementTime;

    /** 订单是否需要结算 */
    private int isNeedSettlement;
    
    private int payChannel;
    
    private String createTime = "";
    
    private String payTime = "";
    
    private String sendTime = "";
    
    private int freightMoney;
    
    /** 订单总价 */
    private double totalPrice;
    
    /** 订单实付金额 */
    private double realPrice;
    
    /** 使用积分 */
    private int accountPoint;
    
    /** 优惠券优惠金额 */
    private double couponPrice;

    /** N元任选优惠金额 */
    private double activitiesOptionalPartPrice;

    /** 客服调价金额 全部为正值，代表减去多少money */
    private double adjustPrice;

    /** 满减金额 */
    private double activitiesPrice;

    /** 左岸城堡
总奖励金额 */
    private double totalWithdrawCash;
    
    private String remark = "";
    
    private String remark2 = "";
    
    private String realSellerName = "";
    
    private String sendAddress = "";
    
    private String fullName = "";
    
    private String idCard = "";
    
    private String province = "";
    
    private String city = "";
    
    private String district = "";
    
    private String detailAddress = "";
    
    private String mobileNumber = "";
    
    private String logisticsChannel = "";
    
    private String logisticsNumber = "";

    private int appChannel = 0;
    
    /** 用户名 */
    private String accountName = "";
    
    /** 用户类型 */
    private int accountType;
    
    /**  结算邮费    */
    private double orderSettlementFreightMoney;
    
    /** 邮费是否结算  0：否，1：是 */
    private int orderSettlementPostageIsSettlement;
    
    private String postageConfirmDate;
    
    private String penaltyTime;

    private int isTimeout;
    
    //***************只有findOrderManualInfoDetail才会一起查询出下列值*******************
    
    private int orderManualProductId;
    
    private double salesPrice;
    
    private int productId;
    
    private int productCount;
    
    private int sellerId;
    
    private double cost;

    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }

    public String getRemark2()
    {
        return remark2;
    }

    public void setRemark2(String remark2)
    {
        this.remark2 = remark2;
    }

    public double getTotalWithdrawCash()
    {
        return totalWithdrawCash;
    }
    
    public void setTotalWithdrawCash(double totalWithdrawCash)
    {
        this.totalWithdrawCash = totalWithdrawCash;
    }

    public int getAppChannel()
    {
        return appChannel;
    }

    public void setAppChannel(int appChannel)
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

    public double getCost()
    {
        return cost;
    }
    
    public void setCost(double cost)
    {
        this.cost = cost;
    }
    
    public String getPostageConfirmDate()
    {
        return postageConfirmDate != null ? postageConfirmDate : "";
    }
    
    public void setPostageConfirmDate(String postageComfirmDate)
    {
        this.postageConfirmDate = postageComfirmDate;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    private List<Map<String, Object>> productList;
    
    public int getOrderManualProductId()
    {
        return orderManualProductId;
    }
    
    public void setOrderManualProductId(int orderManualProductId)
    {
        this.orderManualProductId = orderManualProductId;
    }
    
    public List<Map<String, Object>> getProductList()
    {
        return productList;
    }
    
    public void setProductList(List<Map<String, Object>> productList)
    {
        this.productList = productList;
    }
    
    public double getSalesPrice()
    {
        return salesPrice;
    }
    
    public void setSalesPrice(double salesPrice)
    {
        this.salesPrice = salesPrice;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getProductCount()
    {
        return productCount;
    }
    
    public void setProductCount(int productCount)
    {
        this.productCount = productCount;
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
    
    public long getNumber()
    {
        return number;
    }

    public String getHbNumber()
    {
        return hbNumber;
    }

    public void setHbNumber(String hbNumber)
    {
        this.hbNumber = hbNumber;
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
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public String getSettlementTime()
    {
        return settlementTime != null ? settlementTime : "";
    }
    
    public void setSettlementTime(String settlementTime)
    {
        this.settlementTime = settlementTime;
    }
    
    public int getPayChannel()
    {
        return payChannel;
    }
    
    public void setPayChannel(int payChannel)
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
    
    public int getFreightMoney()
    {
        return freightMoney;
    }
    
    public void setFreightMoney(int freightMoney)
    {
        this.freightMoney = freightMoney;
    }
    
    public double getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public double getRealPrice()
    {
        return realPrice;
    }
    
    public void setRealPrice(double realPrice)
    {
        this.realPrice = realPrice;
    }
    
    public int getAccountPoint()
    {
        return accountPoint;
    }
    
    public void setAccountPoint(int accountPoint)
    {
        this.accountPoint = accountPoint;
    }
    
    public double getCouponPrice()
    {
        return couponPrice;
    }
    
    public void setCouponPrice(double couponPrice)
    {
        this.couponPrice = couponPrice;
    }
    
    public double getAdjustPrice()
    {
        return adjustPrice;
    }
    
    public void setAdjustPrice(double adjustPrice)
    {
        this.adjustPrice = adjustPrice;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getRealSellerName()
    {
        return realSellerName;
    }
    
    public void setRealSellerName(String realSellerName)
    {
        this.realSellerName = realSellerName;
    }
    
    public String getSendAddress()
    {
        return sendAddress;
    }
    
    public void setSendAddress(String sendAddress)
    {
        this.sendAddress = sendAddress;
    }
    
    public String getFullName()
    {
        return fullName;
    }
    
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
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
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public int getAccountType()
    {
        return accountType;
    }
    
    public void setAccountType(int accountType)
    {
        this.accountType = accountType;
    }
    
    public double getOrderSettlementFreightMoney()
    {
        return orderSettlementFreightMoney;
    }
    
    public void setOrderSettlementFreightMoney(double orderSettlementFreightMoney)
    {
        this.orderSettlementFreightMoney = orderSettlementFreightMoney;
    }
    
    public int getOrderSettlementPostageIsSettlement()
    {
        return orderSettlementPostageIsSettlement;
    }
    
    public void setOrderSettlementPostageIsSettlement(int orderSettlementPostageIsSettlement)
    {
        this.orderSettlementPostageIsSettlement = orderSettlementPostageIsSettlement;
    }

    public double getActivitiesPrice()
    {
        return activitiesPrice;
    }

    public void setActivitiesPrice(double activitiesPrice)
    {
        this.activitiesPrice = activitiesPrice;
    }

    public String getPenaltyTime()
    {
        return penaltyTime;
    }
    
    public void setPenaltyTime(String penaltyTime)
    {
        this.penaltyTime = penaltyTime;
    }

    public int getIsTimeout()
    {
        return isTimeout;
    }
    
    public void setIsTimeout(int isTimeout)
    {
        this.isTimeout = isTimeout;
    }

    public double getActivitiesOptionalPartPrice()
    {
        return activitiesOptionalPartPrice;
    }
    
    public void setActivitiesOptionalPartPrice(double activitiesOptionalPartPrice)
    {
        this.activitiesOptionalPartPrice = activitiesOptionalPartPrice;
    }
}
