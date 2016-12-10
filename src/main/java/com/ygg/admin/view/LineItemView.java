package com.ygg.admin.view;

public class LineItemView
{
    private String type = "";
    
    private String productId;
    
    private String code = "";
    
    private String name = "";
    
    /** 数量 */
    private int count;
    
    /** 单价 */
    private String salePrice = "";
    
    /** 总价 = 数量 x 单价 */
    private String salePriceMulCount = "";
    
    /** 分摊 邮费  暂不用 */
    private String postage = "";
    
    /** 分摊 积分优惠金额 */
    private String accountPointPrice = "";
    
    /** 分摊 优惠券优惠金额 */
    private String couponPrice = "";

    /** 分摊 左岸城堡
金额 */
    private String totalWithdrawCash = "";

    /** 分摊 满减金额 */
    private String activitiesPrice = "";

    /** 分摊 N元金额 */
    private String activitiesOptionalPartPrice = "";
    
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
    
    private int refundId;
    
    private String refundType = "";
    
    private int refundCount;

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

    public String getTotalWithdrawCash()
    {
        return totalWithdrawCash;
    }
    
    public void setTotalWithdrawCash(String totalWithdrawCash)
    {
        this.totalWithdrawCash = totalWithdrawCash;
    }

    public String getActivitiesPrice()
    {
        return activitiesPrice;
    }

    public void setActivitiesPrice(String activitiesPrice)
    {
        this.activitiesPrice = activitiesPrice;
    }

    public String getSettlementComfirmDate()
    {
        return settlementComfirmDate;
    }

    public void setSettlementComfirmDate(String settlementComfirmDate)
    {
        this.settlementComfirmDate = settlementComfirmDate;
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

    public String getTotalSinglePayPrice()
    {
        return totalSinglePayPrice;
    }
    
    public void setTotalSinglePayPrice(String totalSinglePayPrice)
    {
        this.totalSinglePayPrice = totalSinglePayPrice;
    }
    
    public String getTotalCost()
    {
        return totalCost;
    }
    
    public void setTotalCost(String totalCost)
    {
        this.totalCost = totalCost;
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
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
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
    
    public String getPostage()
    {
        return postage;
    }
    
    public void setPostage(String postage)
    {
        this.postage = postage;
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
    
    public String getCost()
    {
        return cost;
    }
    
    public void setCost(String cost)
    {
        this.cost = cost;
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
    
    public int getRefundId()
    {
        return refundId;
    }
    
    public void setRefundId(int refundId)
    {
        this.refundId = refundId;
    }
    
    public String getRefundType()
    {
        return refundType;
    }
    
    public void setRefundType(String refundType)
    {
        this.refundType = refundType;
    }
    
    public int getRefundCount()
    {
        return refundCount;
    }
    
    public void setRefundCount(int refundCount)
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

    public String getActivitiesOptionalPartPrice()
    {
        return activitiesOptionalPartPrice;
    }
    
    public void setActivitiesOptionalPartPrice(String activitiesOptionalPartPrice)
    {
        this.activitiesOptionalPartPrice = activitiesOptionalPartPrice;
    }
}
