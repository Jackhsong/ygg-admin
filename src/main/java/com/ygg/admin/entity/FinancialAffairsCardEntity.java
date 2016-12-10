/*
 * Copyright (c) 2014-2016. 浙江格家网络技术有限公司
 */

package com.ygg.admin.entity;

/**
 * 财务打款卡号信息
 *
 * @author xiongl
 * @create 2016-04-26 17:25
 */

public class FinancialAffairsCardEntity
{
    private int id;
    
    /**
     * 卡号类型；1：银行，2：支付宝
     */
    private int type;
    
    /**
     * 银行类型；1：中国工商银行，2：中国农业银行，3：中国银行，4：中国建设银行，5：中国邮政储蓄银行，6：交通银行，7：招商银行，8：中国光大银行，9：中信银行
     */
    private int bankType;
    
    /**
     * 卡号
     */
    private String cardNumber;
    
    /**
     * 姓名
     */
    private String cardName;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
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

    public String getCardInfo()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(type == 1 ? "银行" : type == 2 ? "支付宝" : "微信").append("-").append(cardName).append(cardNumber);
        return sb.toString();
    }
}
