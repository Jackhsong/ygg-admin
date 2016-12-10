package com.ygg.admin.excel;

public class Row
{
    /** 订单类型 */
    private String orderType = "正常订单";

    /** 订单编号 */
    private String number;

    /** 合并订单号 */
    private String hbNumber = "";

    /** 订单状态 */
    private String status = "";

    /** 订单结算状态 */
    private String settlement = "";

    /**订单结算时间*/
    private String settlementTime;

    /** 订单付款渠道 */
    private String payChannel = "";

    private String createTime = "";

    private String payTime = "";

    private String sendTime = "";

    /** 订单总价 */
    private String totalPrice = "";

    /** 订单实付金额 */
    private String realPrice = "";

    private String freightMoney;

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

    private String accountName = "";

    /******************************************************************************** 运费信息 */

    private String postage = "";

    /** 运费结算状态 */
    private String postageSettlementStatus = "";

    private String postageConfirmDate = "";


    /***********************************************商品信息***************************************************************/

    private String type = "";

    private String productId;

    private String code = "";

    private String productName = "";

    /** 数量 */
    private String count;

    /** 单价 */
    private String salePrice = "";

    /** 总价 = 数量 x 单价 */
    private String salePriceMulCount = "";


    /** 分摊 积分优惠金额 */
    private String accountPointPrice = "";

    /** 分摊 优惠券优惠金额 */
    private String couponPrice = "";

    /** 分摊 客服调价金额 全部为正值，代表减去多少money */
    private String adjustPrice = "";

    /** 单位实付金额   (订单实付-邮费 / (psaleprice1 + psaleprice2) * psaleprice1) /count  */
    private String singlePayPrice = "";

    /** 总实付金额 */
    private String totalSinglePayPrice = "";

    /** 单位供货价 */
    private String cost = "";

    /** 总供货价 */
    private String totalCost = "";

    /** 单位实付毛利 */
    private String singleGross = "";

    /** 总实付毛利 = 单位实付毛利 x 数量 */
    private String totalGross = "";

    /******************************************************************************** 退款退货信息 */

    private String refundId;

    private String refundType = "";

    private String refundCount;

    /** 退款金额 */
    private String refundPrice = "";

    /** 商家承担金额 */
    private String sellerRefundPrice = "";

    /** 左岸城堡承担金额 */
    private String gegeRefundPrice = "";

    private String refundStatus = "";

    /** 是否已打款 */
    private String moneyStatus = "";

    /** 是否已收货 */
    private String receiveGoodsStatus = "";

    /** 退款退货结算状态 */
    private String refundSettlementStatus = "";

    /** 退款退货结算时间 */
    private String settlementComfirmDate = "";

    /** 承担方 */
    private String responsibilityPosition = "";

    /** 承担金额 */
    private String responsibilityMoney = "";

    public String getOrderType()
    {
        return orderType;
    }

    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getHbNumber()
    {
        return hbNumber;
    }

    public void setHbNumber(String hbNumber)
    {
        this.hbNumber = hbNumber;
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

    public String getReceiveAddress()
    {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress)
    {
        this.receiveAddress = receiveAddress;
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

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
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

    public String getPostageConfirmDate()
    {
        return postageConfirmDate;
    }

    public void setPostageConfirmDate(String postageConfirmDate)
    {
        this.postageConfirmDate = postageConfirmDate;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getFreightMoney()
    {
        return freightMoney;
    }

    public void setFreightMoney(String freightMoney)
    {
        this.freightMoney = freightMoney;
    }

    public String getCount()
    {
        return count;
    }

    public void setCount(String count)
    {
        this.count = count;
    }

    public String getSalePrice()
    {
        return salePrice;
    }

    public void setSalePrice(String salePrice)
    {
        this.salePrice = salePrice;
    }

    public String getSalePriceMulCount()
    {
        return salePriceMulCount;
    }

    public void setSalePriceMulCount(String salePriceMulCount)
    {
        this.salePriceMulCount = salePriceMulCount;
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

    public String getSinglePayPrice()
    {
        return singlePayPrice;
    }

    public void setSinglePayPrice(String singlePayPrice)
    {
        this.singlePayPrice = singlePayPrice;
    }

    public String getTotalSinglePayPrice()
    {
        return totalSinglePayPrice;
    }

    public void setTotalSinglePayPrice(String totalSinglePayPrice)
    {
        this.totalSinglePayPrice = totalSinglePayPrice;
    }

    public String getCost()
    {
        return cost;
    }

    public void setCost(String cost)
    {
        this.cost = cost;
    }

    public String getTotalCost()
    {
        return totalCost;
    }

    public void setTotalCost(String totalCost)
    {
        this.totalCost = totalCost;
    }

    public String getSingleGross()
    {
        return singleGross;
    }

    public void setSingleGross(String singleGross)
    {
        this.singleGross = singleGross;
    }

    public String getTotalGross()
    {
        return totalGross;
    }

    public void setTotalGross(String totalGross)
    {
        this.totalGross = totalGross;
    }



    public String getRefundType()
    {
        return refundType;
    }

    public void setRefundType(String refundType)
    {
        this.refundType = refundType;
    }

    public String getRefundId()
    {
        return refundId;
    }

    public void setRefundId(String refundId)
    {
        this.refundId = refundId;
    }

    public String getRefundCount()
    {
        return refundCount;
    }

    public void setRefundCount(String refundCount)
    {
        this.refundCount = refundCount;
    }

    public String getRefundPrice()
    {
        return refundPrice;
    }

    public void setRefundPrice(String refundPrice)
    {
        this.refundPrice = refundPrice;
    }

    public String getSellerRefundPrice()
    {
        return sellerRefundPrice;
    }

    public void setSellerRefundPrice(String sellerRefundPrice)
    {
        this.sellerRefundPrice = sellerRefundPrice;
    }

    public String getGegeRefundPrice()
    {
        return gegeRefundPrice;
    }

    public void setGegeRefundPrice(String gegeRefundPrice)
    {
        this.gegeRefundPrice = gegeRefundPrice;
    }

    public String getRefundStatus()
    {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus)
    {
        this.refundStatus = refundStatus;
    }

    public String getMoneyStatus()
    {
        return moneyStatus;
    }

    public void setMoneyStatus(String moneyStatus)
    {
        this.moneyStatus = moneyStatus;
    }

    public String getReceiveGoodsStatus()
    {
        return receiveGoodsStatus;
    }

    public void setReceiveGoodsStatus(String receiveGoodsStatus)
    {
        this.receiveGoodsStatus = receiveGoodsStatus;
    }

    public String getRefundSettlementStatus()
    {
        return refundSettlementStatus;
    }

    public void setRefundSettlementStatus(String refundSettlementStatus)
    {
        this.refundSettlementStatus = refundSettlementStatus;
    }

    public String getSettlementComfirmDate()
    {
        return settlementComfirmDate;
    }

    public void setSettlementComfirmDate(String settlementComfirmDate)
    {
        this.settlementComfirmDate = settlementComfirmDate;
    }

    public String getResponsibilityPosition()
    {
        return responsibilityPosition;
    }

    public void setResponsibilityPosition(String responsibilityPosition)
    {
        this.responsibilityPosition = responsibilityPosition;
    }

    public String getResponsibilityMoney()
    {
        return responsibilityMoney;
    }

    public void setResponsibilityMoney(String responsibilityMoney)
    {
        this.responsibilityMoney = responsibilityMoney;
    }
}
