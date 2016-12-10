package com.ygg.admin.entity;

public class RefundEntity
{
    
    private int id;
    
    /** 废弃  账号卡号id */
    private int accountCardId;
    
    /** 账号id */
    private int accountId;
    
    /** 订单id */
    private int orderId;
    
    /** 订单产品id */
    private int orderProductId;
    
    /** 数量 */
    private int count;
    
    /** 类型；1：仅退款，2：退款并退货 */
    private int type;
    
    /** 状态；1：申请中，2：待退货，3：待退款，4：退款成功，5：退款关闭，6：退款取消 */
    private int status;
    
    /** 退款说明 */
    private String explain = "";
    
    /** 申请退款金额 */
    private double applyMoney;
    
    /** 实际退款金额 */
    private double realMoney;
    
    /** 退款图片1URL */
    private String image1 = "";
    
    /** 退款图片2URL */
    private String image2 = "";
    
    /** 退款图片3URL */
    private String image3 = "";
    
    /** 创建时间 */
    private String createTime = "";
    
    /** 审核时间 */
    private String checkTime = "";
    
    /** 更新时间 */
    private String updateTime = "";
    
    /** 实际返还积分 */
    private int returnAccountPoint = 0;
    
    /** 实际扣除积分 */
    private int removeAccountPoint = 0;
    
    /**卡号类型；1：银行，2：支付宝*/
    private int cardType;
    
    /**银行类型；1：中国工商银行，2：中国农业银行，3：中国银行，4：中国建设银行，5：中国邮政储蓄银行，6：交通银行，7：招商银行，8：中国光大银行，9：中信银行*/
    private int bankType;
    
    /**卡号信息*/
    private String cardNumber = "";
    
    /**卡号姓名*/
    private String cardName = "";
    
    /** 退款退货承担方，1：商家，2：我方 */
    private int responsibilitySide;
    
    /** 退款退货承担方承担金额 */
    private double responsibilityMoney;
    
    /** 是否已结算；0：否，1：是 */
    private int isSettlement;
    
    /** 退款接收账户类型，1：新建账户；2：原路返回 */
    private int refundPayType;
    
    private int isCancelOrder;

    /** 财务打款账户 */
    private int financialAffairsCardId;
    
    private int refundReasonId;

    /**
     * 订单退款来源，1用户主动申请，2客服手动创建
     */
    private  int sourceType;

    public int getRefundReasonId()
    {
        return refundReasonId;
    }

    public void setRefundReasonId(int refundReasonId)
    {
        this.refundReasonId = refundReasonId;
    }

    public int getFinancialAffairsCardId()
    {
        return financialAffairsCardId;
    }

    public void setFinancialAffairsCardId(int financialAffairsCardId)
    {
        this.financialAffairsCardId = financialAffairsCardId;
    }

    public int getIsCancelOrder()
    {
        return isCancelOrder;
    }
    
    public void setIsCancelOrder(int isCancelOrder)
    {
        this.isCancelOrder = isCancelOrder;
    }

    public int getRefundPayType()
    {
        return refundPayType;
    }
    
    public void setRefundPayType(int refundPayType)
    {
        this.refundPayType = refundPayType;
    }
    
    public int getIsSettlement()
    {
        return isSettlement;
    }
    
    public void setIsSettlement(int isSettlement)
    {
        this.isSettlement = isSettlement;
    }
    
    public int getResponsibilitySide()
    {
        return responsibilitySide;
    }
    
    public void setResponsibilitySide(int responsibilitySide)
    {
        this.responsibilitySide = responsibilitySide;
    }
    
    public double getResponsibilityMoney()
    {
        return responsibilityMoney;
    }
    
    public void setResponsibilityMoney(double responsibilityMoney)
    {
        this.responsibilityMoney = responsibilityMoney;
    }
    
    public int getReturnAccountPoint()
    {
        return returnAccountPoint;
    }
    
    public void setReturnAccountPoint(int returnAccountPoint)
    {
        this.returnAccountPoint = returnAccountPoint;
    }
    
    public int getRemoveAccountPoint()
    {
        return removeAccountPoint;
    }
    
    public void setRemoveAccountPoint(int removeAccountPoint)
    {
        this.removeAccountPoint = removeAccountPoint;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getAccountCardId()
    {
        return accountCardId;
    }
    
    public void setAccountCardId(int accountCardId)
    {
        this.accountCardId = accountCardId;
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
    
    public int getOrderProductId()
    {
        return orderProductId;
    }
    
    public void setOrderProductId(int orderProductId)
    {
        this.orderProductId = orderProductId;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public String getExplain()
    {
        return explain;
    }
    
    public void setExplain(String explain)
    {
        this.explain = explain;
    }
    
    public double getApplyMoney()
    {
        return applyMoney;
    }
    
    public void setApplyMoney(double applyMoney)
    {
        this.applyMoney = applyMoney;
    }
    
    public double getRealMoney()
    {
        return realMoney;
    }
    
    public void setRealMoney(double realMoney)
    {
        this.realMoney = realMoney;
    }
    
    public String getImage1()
    {
        return image1;
    }
    
    public void setImage1(String image1)
    {
        this.image1 = image1;
    }
    
    public String getImage2()
    {
        return image2;
    }
    
    public void setImage2(String image2)
    {
        this.image2 = image2;
    }
    
    public String getImage3()
    {
        return image3;
    }
    
    public void setImage3(String image3)
    {
        this.image3 = image3;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getCheckTime()
    {
        return checkTime;
    }
    
    public void setCheckTime(String checkTime)
    {
        this.checkTime = checkTime;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public int getCardType()
    {
        return cardType;
    }
    
    public void setCardType(int cardType)
    {
        this.cardType = cardType;
    }
    
    public int getBankType()
    {
        return bankType;
    }
    
    public void setBankType(int bankType)
    {
        this.bankType = bankType;
    }
    
    public String getCardNumber()
    {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }
    
    public String getCardName()
    {
        return cardName;
    }
    
    public void setCardName(String cardName)
    {
        this.cardName = cardName;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
}
