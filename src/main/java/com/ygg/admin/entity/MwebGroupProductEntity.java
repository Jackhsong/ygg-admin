package com.ygg.admin.entity;

public class MwebGroupProductEntity
{
    // 团购商品id
    private int id;
    
    private byte type;
    
    private int productId;
    
    // 基本商品id
    private int productBaseId;
    
    // 品牌id
    private int brandId;
    
    // 商家id
    private int sellerId;
    
    // 商品编码
    private String code;
    
    // 商品条形码
    private String barCode;
    
    // 开始时间
    private String startTime;
    
    // 结束时间
    private String endTime;
    
    private String startTeamTime;
    
    // 提供给商家的发货名称
    private String sellerProductName;
    
    // 名称
    private String name;
    
    // 短名称
    private String shortName;
    
    // 卖点
    private String sellingPoint;
    
    // 描述，格格说
    private String desc;
    
    // 市场价
    private float marketPrice;
    
    // 单独购买价
    private float singlePrice;
    
    // 参团价
    private float teamPrice;
    
    // 参团人数上限
    private int teamNumberLimit;
    
    private byte teamCount;
    
    // 开团有效时间（单位：小时）
    private int teamValidHour;
    
    // 商品主图
    private String productImage;
    
    // 商品图片1访问URL
    private String image1;
    
    //
    private String image2;
    
    //
    private String image3;
    
    //
    private String image4;
    
    //
    private String image5;
    
    // 净含量
    private String netVolume;
    
    // 产地
    private String placeOfOrigin;
    
    // 生产日期
    private String manufacturerDate;
    
    // 储藏方法
    private String storageMethod;
    
    // 保质期
    private String durabilityPeriod;
    
    // 适用人群
    private String peopleFor;
    
    // 食用方法
    private String foodMethod;
    
    // 使用方法
    private String useMethod;
    
    // 温馨提示
    private String tip;
    
    // 备注
    private String remark;
    
    // 是否下架；0：否，1：是
    private byte isOffShelves = -1;
    
    // 是否可用；0：否，1：是
    private byte isAvailable = -1;
    
    // 配送地区描述
    private String deliverAreaDesc;
    
    // 配送地区类型，1：支持地区；2：不支持地区
    private String deliverAreaType;
    
    // 创建时间
    private String createTime;
    
    // 更新时间
    private String updateTime;
    
    private int order;
    
    private float teamSupplyPrice;
    
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
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public int getBrandId()
    {
        return brandId;
    }
    
    public void setBrandId(int brandId)
    {
        this.brandId = brandId;
    }
    
    public int getSellerId()
    {
        return sellerId;
    }
    
    public void setSellerId(int sellerId)
    {
        this.sellerId = sellerId;
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
    
    public String getSellingPoint()
    {
        return sellingPoint;
    }
    
    public void setSellingPoint(String sellingPoint)
    {
        this.sellingPoint = sellingPoint;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public float getMarketPrice()
    {
        return marketPrice;
    }
    
    public void setMarketPrice(float marketPrice)
    {
        this.marketPrice = marketPrice;
    }
    
    public float getSinglePrice()
    {
        return singlePrice;
    }
    
    public void setSinglePrice(float singlePrice)
    {
        this.singlePrice = singlePrice;
    }
    
    public float getTeamPrice()
    {
        return teamPrice;
    }
    
    public void setTeamPrice(float teamPrice)
    {
        this.teamPrice = teamPrice;
    }
    
    public int getTeamNumberLimit()
    {
        return teamNumberLimit;
    }
    
    public void setTeamNumberLimit(int teamNumberLimit)
    {
        this.teamNumberLimit = teamNumberLimit;
    }
    
    public int getTeamValidHour()
    {
        return teamValidHour;
    }
    
    public void setTeamValidHour(int teamValidHour)
    {
        this.teamValidHour = teamValidHour;
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
    
    public String getImage4()
    {
        return image4;
    }
    
    public void setImage4(String image4)
    {
        this.image4 = image4;
    }
    
    public String getImage5()
    {
        return image5;
    }
    
    public void setImage5(String image5)
    {
        this.image5 = image5;
    }
    
    public int getIsOffShelves()
    {
        return isOffShelves;
    }
    
    public void setIsOffShelves(byte isOffShelves)
    {
        this.isOffShelves = isOffShelves;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getBarCode()
    {
        return barCode;
    }
    
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
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
    
    public String getSellerProductName()
    {
        return sellerProductName;
    }
    
    public void setSellerProductName(String sellerProductName)
    {
        this.sellerProductName = sellerProductName;
    }
    
    public String getNetVolume()
    {
        return netVolume;
    }
    
    public void setNetVolume(String netVolume)
    {
        this.netVolume = netVolume;
    }
    
    public String getPlaceOfOrigin()
    {
        return placeOfOrigin;
    }
    
    public void setPlaceOfOrigin(String placeOfOrigin)
    {
        this.placeOfOrigin = placeOfOrigin;
    }
    
    public String getManufacturerDate()
    {
        return manufacturerDate;
    }
    
    public void setManufacturerDate(String manufacturerDate)
    {
        this.manufacturerDate = manufacturerDate;
    }
    
    public String getStorageMethod()
    {
        return storageMethod;
    }
    
    public void setStorageMethod(String storageMethod)
    {
        this.storageMethod = storageMethod;
    }
    
    public String getDurabilityPeriod()
    {
        return durabilityPeriod;
    }
    
    public void setDurabilityPeriod(String durabilityPeriod)
    {
        this.durabilityPeriod = durabilityPeriod;
    }
    
    public String getPeopleFor()
    {
        return peopleFor;
    }
    
    public void setPeopleFor(String peopleFor)
    {
        this.peopleFor = peopleFor;
    }
    
    public String getFoodMethod()
    {
        return foodMethod;
    }
    
    public void setFoodMethod(String foodMethod)
    {
        this.foodMethod = foodMethod;
    }
    
    public String getUseMethod()
    {
        return useMethod;
    }
    
    public void setUseMethod(String useMethod)
    {
        this.useMethod = useMethod;
    }
    
    public String getTip()
    {
        return tip;
    }
    
    public void setTip(String tip)
    {
        this.tip = tip;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getDeliverAreaDesc()
    {
        return deliverAreaDesc;
    }
    
    public void setDeliverAreaDesc(String deliverAreaDesc)
    {
        this.deliverAreaDesc = deliverAreaDesc;
    }
    
    public String getDeliverAreaType()
    {
        return deliverAreaType;
    }
    
    public void setDeliverAreaType(String deliverAreaType)
    {
        this.deliverAreaType = deliverAreaType;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public String getProductImage()
    {
        return productImage;
    }
    
    public void setProductImage(String productImage)
    {
        this.productImage = productImage;
    }
    
    public int getOrder()
    {
        return order;
    }
    
    public void setOrder(int order)
    {
        this.order = order;
    }
    
    public float getTeamSupplyPrice()
    {
        return teamSupplyPrice;
    }
    
    public void setTeamSupplyPrice(float teamSupplyPrice)
    {
        this.teamSupplyPrice = teamSupplyPrice;
    }
    
    public String getStartTeamTime()
    {
        return startTeamTime;
    }
    
    public void setStartTeamTime(String startTeamTime)
    {
        this.startTeamTime = startTeamTime;
    }
    
    public byte getTeamCount()
    {
        return teamCount;
    }
    
    public void setTeamCount(byte teamCount)
    {
        this.teamCount = teamCount;
    }
    
}
