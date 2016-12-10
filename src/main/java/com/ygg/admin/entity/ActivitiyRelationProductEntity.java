package com.ygg.admin.entity;

public class ActivitiyRelationProductEntity
{
    
    private int id;
    
    /**活动类型；1：网页自定义活动-其他活动，2：网页自定义活动-情景专场，3：原生自定义活动*/
    private byte type;
    
    /**活动id*/
    private int typeId;
    
    /**商品id*/
    private int productId;
    
    // 商品编码
    private String code;
    
    // 商品名称
    private String productName;
    
    // 商品短名称
    private String productShortName;
    
    // 商品备注
    private String remark;
    
    // 商品库存
    private int stock;
    
    // 商品销量
    private int sell;
    
    // 原价
    private float marketPrice;
    
    // 现价
    private float salesPrice;
    
    // 商品是否下架
    private int isOffShelves;
    
    // 品牌名字
    private String brandName;
    
    // 品牌ID
    private int brandId;
    
    // 商家名字
    private String sellerName;
    
    // 商家ID
    private int sellerId;
    
    // 发货地
    private String sendAddress;
    
    // 分仓
    private String warehouse;
    
    // 基本商品Id
    private int productBaseId;
    
    //商品类型，1特卖商品，2商城商品
    private int productType;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public int getTypeId()
    {
        return typeId;
    }
    
    public void setTypeId(int typeId)
    {
        this.typeId = typeId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
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
    
    public String getProductShortName()
    {
        return productShortName;
    }
    
    public void setProductShortName(String productShortName)
    {
        this.productShortName = productShortName;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public int getStock()
    {
        return stock;
    }
    
    public void setStock(int stock)
    {
        this.stock = stock;
    }
    
    public int getSell()
    {
        return sell;
    }
    
    public void setSell(int sell)
    {
        this.sell = sell;
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
    
    public int getIsOffShelves()
    {
        return isOffShelves;
    }
    
    public void setIsOffShelves(int isOffShelves)
    {
        this.isOffShelves = isOffShelves;
    }
    
    public String getBrandName()
    {
        return brandName;
    }
    
    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }
    
    public int getBrandId()
    {
        return brandId;
    }
    
    public void setBrandId(int brandId)
    {
        this.brandId = brandId;
    }
    
    public String getSellerName()
    {
        return sellerName;
    }
    
    public void setSellerName(String sellerName)
    {
        this.sellerName = sellerName;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
    }
    
    public String getSendAddress()
    {
        return sendAddress;
    }
    
    public void setSendAddress(String sendAddress)
    {
        this.sendAddress = sendAddress;
    }
    
    public String getWarehouse()
    {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getProductType()
    {
        return productType;
    }
    
    public void setProductType(int productType)
    {
        this.productType = productType;
    }
    
}
