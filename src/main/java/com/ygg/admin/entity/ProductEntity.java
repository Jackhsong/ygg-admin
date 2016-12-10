package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ProductEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    /**商品类型：1：特卖商品，2：商城商品，格格福利商品*/
    private int type;
    
    /**品牌Id*/
    private int brandId;
    
    /**运费模板Id*/
    private int freightTemplateId;
    
    /**商家Id*/
    private int sellerId;
    
    /**条形码*/
    private String barcode = "";
    
    /**商品编码*/
    private String code = "";
    
    /**商品名称*/
    private String name = "";
    
    /**商家发货名称*/
    private String sellerProductName = "";
    
    /**商品段名称*/
    private String shortName = "";
    
    /**备注*/
    private String remark = "";
    
    /**格格说*/
    private String desc = "";
    
    /**市场价*/
    private float marketPrice;
    
    /**售价*/
    private float salesPrice;
    
    /**合伙人分销价*/
    private float partnerDistributionPrice;
    
    /**图片访问URL*/
    private String image1 = "";
    
    /**图片访问URL*/
    private String image2 = "";
    
    /**图片访问URL*/
    private String image3 = "";
    
    /**图片访问URL*/
    private String image4 = "";
    
    /**图片访问URL*/
    private String image5 = "";
    
    /**净含量*/
    private String netVolume = "";
    
    /**产地*/
    private String placeOfOrigin = "";
    
    /**储存方法*/
    private String storageMethod = "";
    
    /**生产日期*/
    private String manufacturerDate = "";
    
    /**保质期*/
    private String durabilityPeriod = "";
    
    /**适用人群*/
    private String peopleFor = "";
    
    /**食用方法*/
    private String foodMethod = "";
    
    /**使用方法*/
    private String useMethod = "";
    
    /**温馨提示*/
    private String tip = "";
    
    /**pc详情内容*/
    private String pcDetail = "";
    
    /**是否可用；1可用，0不可用*/
    private Byte isAvailable;
    
    /**是否下架；1是，0否*/
    private Byte isOffShelves;
    
    /**创建时间*/
    private String createTime = "";
    
    /**更新时间*/
    private String updateTime = "";
    
    /**type=2时为空*/
    private String startTime = "";
    
    /**type=2时为空*/
    private String endTime = "";
    
    /**格格头像Id*/
    private int gegeImageId;
    
    /**卖点*/
    private String sellingPoint = "";
    
    /**基本商品Id*/
    private int productBaseId;
    
    /** 返分销毛利百分比类型 */
    private int returnDistributionProportionType;
    
    /** 活动状态；1：没有活动，2：团购活动，3：格格福利*/
    private int activitiesStatus = 1;
    
    /**是否放入商城；0：否，1：是，type=1时该字值为0*/
    private int isShowInMall;
    
    /**是否自动调库存*/
    private int isAutomaticAdjustStock = 1;
    
     /**行动派分销佣金  */
    private String bsCommision ="0";

    private int productUseScopeId = 0;

    /**活动供货价 */
    private float activityWholesalePrice;

    /** 活动供货价开始时间 */
    private String activityWholesalePriceStartTime;

    /**活动供货价结束时间 */
    private String activityWholesalePriceEndTime;
    
    public int getReturnDistributionProportionType()
    {
        return returnDistributionProportionType;
    }
    
    public void setReturnDistributionProportionType(int returnDistributionProportionType)
    {
        this.returnDistributionProportionType = returnDistributionProportionType;
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
    
    public int getId()
    {
        return id;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getBrandId()
    {
        return brandId;
    }
    
    public void setBrandId(int brandId)
    {
        this.brandId = brandId;
    }
    
    public String getBarcode()
    {
        return barcode;
    }
    
    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public int getFreightTemplateId()
    {
        return freightTemplateId;
    }
    
    public void setFreightTemplateId(int freightTemplateId)
    {
        this.freightTemplateId = freightTemplateId;
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
    
    public String getSellerProductName()
    {
        return sellerProductName;
    }
    
    public void setSellerProductName(String sellerProductName)
    {
        this.sellerProductName = sellerProductName;
    }
    
    public String getShortName()
    {
        return shortName;
    }
    
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
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
    
    public float getSalesPrice()
    {
        return salesPrice;
    }
    
    public void setSalesPrice(float salesPrice)
    {
        this.salesPrice = salesPrice;
    }
    
    public String getImage1()
    {
        return image1;
    }
    
    public void setImage1(String image1)
    {
        if (image1 != null && !image1.equals("") && !image1.startsWith("http://"))
            image1 = "http://" + image1;
        this.image1 = image1;
    }
    
    public String getImage2()
    {
        return image2;
    }
    
    public void setImage2(String image2)
    {
        if (image2 != null && !image2.equals("") && !image2.startsWith("http://"))
            image2 = "http://" + image2;
        this.image2 = image2;
    }
    
    public String getImage3()
    {
        return image3;
    }
    
    public void setImage3(String image3)
    {
        if (image3 != null && !image3.equals("") && !image3.startsWith("http://"))
            image3 = "http://" + image3;
        this.image3 = image3;
    }
    
    public String getImage4()
    {
        return image4;
    }
    
    public void setImage4(String image4)
    {
        if (image4 != null && !image4.equals("") && !image4.startsWith("http://"))
            image4 = "http://" + image4;
        this.image4 = image4;
    }
    
    public String getImage5()
    {
        return image5;
    }
    
    public void setImage5(String image5)
    {
        if (image5 != null && !image5.equals("") && !image5.startsWith("http://"))
            image5 = "http://" + image5;
        this.image5 = image5;
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
    
    public String getStorageMethod()
    {
        return storageMethod;
    }
    
    public void setStorageMethod(String storageMethod)
    {
        this.storageMethod = storageMethod;
    }
    
    public String getManufacturerDate()
    {
        return manufacturerDate;
    }
    
    public void setManufacturerDate(String manufacturerDate)
    {
        this.manufacturerDate = manufacturerDate;
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
    
    public String getPcDetail()
    {
        return pcDetail;
    }
    
    public void setPcDetail(String pcDetail)
    {
        this.pcDetail = pcDetail;
    }
    
    public Byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(Byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public Byte getIsOffShelves()
    {
        return isOffShelves;
    }
    
    public void setIsOffShelves(Byte isOffShelves)
    {
        this.isOffShelves = isOffShelves;
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
    
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    
    public int getGegeImageId()
    {
        return gegeImageId;
    }
    
    public void setGegeImageId(int gegeImageId)
    {
        this.gegeImageId = gegeImageId;
    }
    
    public String getSellingPoint()
    {
        return sellingPoint;
    }
    
    public void setSellingPoint(String sellingPoint)
    {
        this.sellingPoint = sellingPoint;
    }
    
    public int getProductBaseId()
    {
        return productBaseId;
    }
    
    public void setProductBaseId(int productBaseId)
    {
        this.productBaseId = productBaseId;
    }
    
    public float getPartnerDistributionPrice()
    {
        return partnerDistributionPrice;
    }
    
    public void setPartnerDistributionPrice(float partnerDistributionPrice)
    {
        this.partnerDistributionPrice = partnerDistributionPrice;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getActivitiesStatus()
    {
        return activitiesStatus;
    }
    
    public void setActivitiesStatus(int activitiesStatus)
    {
        this.activitiesStatus = activitiesStatus;
    }
    
    public int getIsShowInMall()
    {
        return isShowInMall;
    }
    
    public void setIsShowInMall(int isShowInMall)
    {
        this.isShowInMall = isShowInMall;
    }
    
    public int getIsAutomaticAdjustStock()
    {
        return isAutomaticAdjustStock;
    }
    
    public void setIsAutomaticAdjustStock(int isAutomaticAdjustStock)
    {
        this.isAutomaticAdjustStock = isAutomaticAdjustStock;
    }

	
	/**  
	 *@return  the bsCommision
	 */
	public String getBsCommision() {
		return bsCommision;
	}

	
	/** 
	 * @param bsCommision the bsCommision to set
	 */
	public void setBsCommision(String bsCommision) {
		this.bsCommision = bsCommision;
	}

    public int getProductUseScopeId() {
        return productUseScopeId;
    }

    public void setProductUseScopeId(int productUseScopeId) {
        this.productUseScopeId = productUseScopeId;
    }

    public float getActivityWholesalePrice() {
        return activityWholesalePrice;
    }

    public void setActivityWholesalePrice(float activityWholesalePrice) {
        this.activityWholesalePrice = activityWholesalePrice;
    }

    public String getActivityWholesalePriceStartTime() {
        return activityWholesalePriceStartTime;
    }

    public void setActivityWholesalePriceStartTime(String activityWholesalePriceStartTime) {
        this.activityWholesalePriceStartTime = activityWholesalePriceStartTime;
    }

    public String getActivityWholesalePriceEndTime() {
        return activityWholesalePriceEndTime;
    }

    public void setActivityWholesalePriceEndTime(String activityWholesalePriceEndTime) {
        this.activityWholesalePriceEndTime = activityWholesalePriceEndTime;
    }
}
