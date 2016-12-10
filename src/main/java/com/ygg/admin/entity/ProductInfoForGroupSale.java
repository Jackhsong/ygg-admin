package com.ygg.admin.entity;

public class ProductInfoForGroupSale extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    // 通用专场与商品关联id
    private int relationId;
    
    // 商品id
    private int productId;
    
    // 通用专场id
    private int activitiesCommonId;
    
    // 排序值
    private int order;
    
    // 商品编码
    private String code;
    
    // 商品名称
    private String name;
    
    // 商品短名称
    private String shortName;
    
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
    
    // 开售时间
    private String startTime;
    
    // 结束时间
    private String endTime;
    
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
    private int type;
    
    //商品是否展现
    private int isDisplay;
    
    public String getWarehouse()
    {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse)
    {
        this.warehouse = warehouse;
    }
    
    public int getSell()
    {
        return sell;
    }
    
    public void setSell(int sell)
    {
        this.sell = sell;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public int getIsOffShelves()
    {
        return isOffShelves;
    }
    
    public void setIsOffShelves(int isOffShelves)
    {
        this.isOffShelves = isOffShelves;
    }
    
    public String getShortName()
    {
        return shortName;
    }
    
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public int getRelationId()
    {
        return relationId;
    }
    
    public void setRelationId(int relationId)
    {
        this.relationId = relationId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getActivitiesCommonId()
    {
        return activitiesCommonId;
    }
    
    public void setActivitiesCommonId(int activitiesCommonId)
    {
        this.activitiesCommonId = activitiesCommonId;
    }
    
    public int getOrder()
    {
        return order;
    }
    
    public void setOrder(int order)
    {
        this.order = order;
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
    
    public int getStock()
    {
        return stock;
    }
    
    public void setStock(int stock)
    {
        this.stock = stock;
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
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
}
