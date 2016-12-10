package com.ygg.admin.entity.qqbs;

public class QqbsRewardThrEntity
{
    /**账号ID*/
    private int accountId;
    
    /**账号昵称*/
    private String nickName;
    
    /**交易日期*/
    private String tradeDate;
    
    /**日交易总价*/
    private String realPrice;
    

    public int getAccountId()
    {
        return accountId;
    }

    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getTradeDate()
    {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate)
    {
        this.tradeDate = tradeDate;
    }

    public String getRealPrice()
    {
        return realPrice;
    }

    public void setRealPrice(String realPrice)
    {
        this.realPrice = realPrice;
    }
    
}
