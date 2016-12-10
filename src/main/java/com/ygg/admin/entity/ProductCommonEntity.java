package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProductCommonEntity extends BaseEntity implements Comparable<ProductCommonEntity>
{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private int productId;
    
    private String mediumImage;
    
    private String smallImage;
    
    private String name;
    
    private String shortName;
    
    private float marketPrice;
    
    private float salesPrice;
    
    private int sellCount;
    
    private String sellingPoint = "";
    
    /**合伙人分销价*/
    private float partnerDistributionPrice;
    
    /**商品类型：1特卖商品；2商城商品*/
    private int type;
    
    public float getPartnerDistributionPrice()
    {
        return partnerDistributionPrice;
    }
    
    public void setPartnerDistributionPrice(float partnerDistributionPrice)
    {
        this.partnerDistributionPrice = partnerDistributionPrice;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public String getMediumImage()
    {
        return mediumImage;
    }
    
    public void setMediumImage(String mediumImage)
    {
        if (mediumImage != null && !mediumImage.equals("") && !mediumImage.startsWith("http://"))
            mediumImage = "http://" + mediumImage;
        this.mediumImage = mediumImage;
    }
    
    public String getSmallImage()
    {
        return smallImage;
    }
    
    public void setSmallImage(String smallImage)
    {
        this.smallImage = smallImage;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getShortName()
    {
        return shortName;
    }
    
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }
    
    public float getMarketPrice()
    {
        return marketPrice;
    }
    
    public void setMarketPrice(float marketPrice)
    {
        this.marketPrice = marketPrice;
    }
    
    public float getSalesPrice()
    {
        return salesPrice;
    }
    
    public void setSalesPrice(float salesPrice)
    {
        this.salesPrice = salesPrice;
    }
    
    public int getSellCount()
    {
        return sellCount;
    }
    
    public void setSellCount(int sellCount)
    {
        this.sellCount = sellCount;
    }
    
    public String getSellingPoint()
    {
        return sellingPoint;
    }
    
    public void setSellingPoint(String sellingPoint)
    {
        this.sellingPoint = sellingPoint;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    @Override
    public int compareTo(ProductCommonEntity pce)
    {
        if (this.salesPrice != pce.getSalesPrice())
        {
            return (this.salesPrice - pce.getSalesPrice()) > 0 ? 1 : -1;
        }
        else
        {
            return this.productId - pce.getProductId();
        }
    }
}
