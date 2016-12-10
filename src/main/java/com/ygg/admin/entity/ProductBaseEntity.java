package com.ygg.admin.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 基本商品
 * 
 * @author xiongl
 *
 */
public class ProductBaseEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = -693875051542995228L;
    
    /** 基本商品Id */
    private int id;
    
    /** 品牌Id */
    private int brandId;
    
    /** 商家Id */
    private int sellerId;
    
    /** 格格头像Id */
    private int gegeImageId;
    
    /** 格格说 */
    private String gegeSay = "";
    
    /** 商品编码 */
    private String code = "";
    
    /** 商品条形码 */
    private String barcode = "";
    
    /** 商品名称 */
    private String name = "";
    
    /** 结算类型 */
    private int submitType = 1;
    
    /** 供货价 submitType=1时使用 */
    private float wholesalePrice;
    
    /** 折扣点 submitType=2时使用 */
    private float deduction;
    
    /** 建议价 submitType=2时使用 */
    private float proposalPrice;
    
    /** 自营采购价 submitType=3时使用 */
    private float selfPurchasePrice;
    
    /** 总库存 */
    private int totalStock;
    
    /** 特卖库存 */
    private int saleStock;
    
    /** 分销库存 */
    private int distributionStock;
    
    /** 商城库存 */
    private int mallStock;
    
    /** 可用库存 */
    private int availableStock;
    
    /** 净含量 */
    private String netVolume = "";
    
    /** 产地 */
    private String placeOfOrigin = "";
    
    /** 生产日期 */
    private String manufacturerDate = "";
    
    /** 储藏方法 */
    private String storageMethod = "";
    
    /** 保质期 */
    private String durabilityPeriod = "";
    
    /** 适用人群 */
    private String peopleFor = "";
    
    /** 食用方法 */
    private String foodMethod = "";
    
    /** 使用方法 */
    private String useMethod = "";
    
    /** 温馨提示 */
    private String tip = "";
    
    /** 商品图片1访问URL */
    private String image1 = "";
    
    /** 品牌图片2访问URL */
    private String image2 = "";
    
    /** 品牌图片3访问URL */
    private String image3 = "";
    
    /** 品牌图片4访问URL */
    private String image4 = "";
    
    /** 品牌图片5访问URL */
    private String image5 = "";
    
    /** 中型图片访问URL（组合特卖图） */
    private String mediumImage = "";
    
    /** 小型图片访问URL（购物车图） */
    private String smallImage = "";
    
    /** 是否可用 */
    private int isAvailable;
    
    /** 备注 */
    private String remark = "";
    
    /** 创建时间 */
    private String createTime = "";
    
    /** 修改时间 */
    private String updateTime = "";
    
    /**商品类型：1：普通商品；2：生鲜商品*/
    private int type = 1;
    
    /**特卖国旗Id*/
    private int saleFlagId;
    
    /**商品正品保证展示,1：进口商品；2：国产商品*/
    private int qualityPromiseType = 1;
    
    /**配送地区描述*/
    private String deliverAreaDesc = "";
    
    /**配送地区类型，1：支持地区；2：不支持地区*/
    private int deliverAreaType = 1;
    
    /**是否自动调库存*/
    private int isAutomaticAdjustStock = 1;
    
    /**过期时间*/
    private String expireDate;
    
    /**卖点*/
    private String sellingPoint = "";
    
    /**建议特卖价*/
    private float proposalSalesPrice;
    
    /**建议市场价*/
    private float proposalMarketPrice;
    
    /**商家配送地区模版Id*/
    private int sellerDeliverAreaTemplateId;

    /** 左岸城堡
供货价 */
    private float hqbsWholesalePrice;

    public float getHqbsWholesalePrice()
    {
        return hqbsWholesalePrice;
    }
    
    public void setHqbsWholesalePrice(float hqbsWholesalePrice)
    {
        this.hqbsWholesalePrice = hqbsWholesalePrice;
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
    
    public String getGegeSay()
    {
        return gegeSay;
    }
    
    public void setGegeSay(String gegeSay)
    {
        this.gegeSay = gegeSay;
    }
    
    public float getWholesalePrice()
    {
        return wholesalePrice;
    }
    
    public void setWholesalePrice(float wholesalePrice)
    {
        this.wholesalePrice = wholesalePrice;
    }
    
    public float getDeduction()
    {
        return deduction;
    }
    
    public void setDeduction(float deduction)
    {
        this.deduction = deduction;
    }
    
    public float getProposalPrice()
    {
        return proposalPrice;
    }
    
    public void setProposalPrice(float proposalPrice)
    {
        this.proposalPrice = proposalPrice;
    }
    
    public int getTotalStock()
    {
        return totalStock;
    }
    
    public void setTotalStock(int totalStock)
    {
        this.totalStock = totalStock;
    }
    
    public int getSaleStock()
    {
        return saleStock;
    }
    
    public void setSaleStock(int saleStock)
    {
        this.saleStock = saleStock;
    }
    
    public int getDistributionStock()
    {
        return distributionStock;
    }
    
    public void setDistributionStock(int distributionStock)
    {
        this.distributionStock = distributionStock;
    }
    
    public int getMallStock()
    {
        return mallStock;
    }
    
    public void setMallStock(int mallStock)
    {
        this.mallStock = mallStock;
    }
    
    public int getAvailableStock()
    {
        return availableStock;
    }
    
    public void setAvailableStock(int availableStock)
    {
        this.availableStock = availableStock;
    }
    
    public String getMediumImage()
    {
        return mediumImage;
    }
    
    public void setMediumImage(String mediumImage)
    {
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
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    public int getSubmitType()
    {
        return submitType;
    }
    
    public void setSubmitType(int submitType)
    {
        this.submitType = submitType;
    }
    
    public float getSelfPurchasePrice()
    {
        return selfPurchasePrice;
    }
    
    public void setSelfPurchasePrice(float selfPurchasePrice)
    {
        this.selfPurchasePrice = selfPurchasePrice;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public int getSaleFlagId()
    {
        return saleFlagId;
    }
    
    public void setSaleFlagId(int saleFlagId)
    {
        this.saleFlagId = saleFlagId;
    }
    
    public int getQualityPromiseType()
    {
        return qualityPromiseType;
    }
    
    public void setQualityPromiseType(int qualityPromiseType)
    {
        this.qualityPromiseType = qualityPromiseType;
    }
    
    public String getDeliverAreaDesc()
    {
        return deliverAreaDesc;
    }
    
    public void setDeliverAreaDesc(String deliverAreaDesc)
    {
        this.deliverAreaDesc = deliverAreaDesc;
    }
    
    public int getDeliverAreaType()
    {
        return deliverAreaType;
    }
    
    public void setDeliverAreaType(int deliverAreaType)
    {
        this.deliverAreaType = deliverAreaType;
    }
    
    public String getExpireDate()
    {
        return expireDate;
    }
    
    public void setExpireDate(String expireDate)
    {
        this.expireDate = expireDate;
    }
    
    public int getIsAutomaticAdjustStock()
    {
        return isAutomaticAdjustStock;
    }
    
    public void setIsAutomaticAdjustStock(int isAutomaticAdjustStock)
    {
        this.isAutomaticAdjustStock = isAutomaticAdjustStock;
    }
    
    public String getSellingPoint()
    {
        return sellingPoint;
    }
    
    public void setSellingPoint(String sellingPoint)
    {
        this.sellingPoint = sellingPoint;
    }
    
    public float getProposalSalesPrice()
    {
        return proposalSalesPrice;
    }
    
    public void setProposalSalesPrice(float proposalSalesPrice)
    {
        this.proposalSalesPrice = proposalSalesPrice;
    }
    
    public float getProposalMarketPrice()
    {
        return proposalMarketPrice;
    }
    
    public void setProposalMarketPrice(float proposalMarketPrice)
    {
        this.proposalMarketPrice = proposalMarketPrice;
    }
    
    public int getSellerDeliverAreaTemplateId()
    {
        return sellerDeliverAreaTemplateId;
    }
    
    public void setSellerDeliverAreaTemplateId(int sellerDeliverAreaTemplateId)
    {
        this.sellerDeliverAreaTemplateId = sellerDeliverAreaTemplateId;
    }
    
}
