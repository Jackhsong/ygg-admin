package com.ygg.admin.entity;

public class RefundProportionEntity
{
    private int id;
    
    private int refundId;
    
    private double sellerMoney;
    
    private double gegejiaMoney;
    
    private double sellerPostageMoney;
    
    private double gegejiaPostageMoney;
    
    private double sellerDifferenceMoney;
    
    private double gegejiaDifferenceMoney;
    
    /** 类型，0：未发货订单取消；1：已发货订单退款；2：订单退差价；3：订单退运费 */
    private int type;
    
    private String createTime;
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getRefundId()
    {
        return refundId;
    }
    
    public void setRefundId(int refundId)
    {
        this.refundId = refundId;
    }
    
    public double getSellerMoney()
    {
        return sellerMoney;
    }
    
    public void setSellerMoney(double sellerMoney)
    {
        this.sellerMoney = sellerMoney;
    }
    
    public double getGegejiaMoney()
    {
        return gegejiaMoney;
    }
    
    public void setGegejiaMoney(double gegejiaMoney)
    {
        this.gegejiaMoney = gegejiaMoney;
    }
    
    public double getSellerPostageMoney()
    {
        return sellerPostageMoney;
    }
    
    public void setSellerPostageMoney(double sellerPostageMoney)
    {
        this.sellerPostageMoney = sellerPostageMoney;
    }
    
    public double getGegejiaPostageMoney()
    {
        return gegejiaPostageMoney;
    }
    
    public void setGegejiaPostageMoney(double gegejiaPostageMoney)
    {
        this.gegejiaPostageMoney = gegejiaPostageMoney;
    }
    
    public double getSellerDifferenceMoney()
    {
        return sellerDifferenceMoney;
    }
    
    public void setSellerDifferenceMoney(double sellerDifferenceMoney)
    {
        this.sellerDifferenceMoney = sellerDifferenceMoney;
    }
    
    public double getGegejiaDifferenceMoney()
    {
        return gegejiaDifferenceMoney;
    }
    
    public void setGegejiaDifferenceMoney(double gegejiaDifferenceMoney)
    {
        this.gegejiaDifferenceMoney = gegejiaDifferenceMoney;
    }
    
}
