package com.ygg.admin.entity;

public class AccountEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name;
    
    private String pwd;
    
    private byte type;
    
    private String nickname;
    
    private String mobileNumber;
    
    private int availablePoint;
    
    private String createTime;
    
    private String updateTime;
    
    /////////////账户合伙人新增字段///////////////
    /**邀请码*/
    private String recommendedCode;
    
    /**自己推荐人数*/
    private int recommendedCount;
    
    /**自己推荐下单人数*/
    private int recommendedOrderCount;
    
    /**小伙伴推荐人数*/
    private int subRecommendedCount;
    
    /**累计获得的推荐积分*/
    private int totalRecommendedPoint;
    
    /**是否填写过推荐账户；0：否，1：是*/
    private int isRecommended;
    
    /**合伙人状态；1：不是合伙人，2：是合伙人，3：合伙人被禁用*/
    private int partnerStatus;
    
    /**申请合伙人状态；1:未申请，2：申请中，3：申请未通过，4：申请通过*/
    private int applyPartnerStatus;
    
    /**是否有下单；0：否，1：是*/
    private int isHasOrder;

    private int level;

    private float totalSuccessPrice;

    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public float getTotalSuccessPrice()
    {
        return totalSuccessPrice;
    }

    public void setTotalSuccessPrice(float totalSuccessPrice)
    {
        this.totalSuccessPrice = totalSuccessPrice;
    }

    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPwd()
    {
        return pwd;
    }
    
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
    
    public byte getType()
    {
        return type;
    }
    
    public void setType(byte type)
    {
        this.type = type;
    }
    
    public String getNickname()
    {
        return nickname;
    }
    
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
    
    public String getMobileNumber()
    {
        return mobileNumber;
    }
    
    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }
    
    public int getAvailablePoint()
    {
        return availablePoint;
    }
    
    public void setAvailablePoint(int availablePoint)
    {
        this.availablePoint = availablePoint;
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
    
    public String getRecommendedCode()
    {
        return recommendedCode;
    }
    
    public void setRecommendedCode(String recommendedCode)
    {
        this.recommendedCode = recommendedCode;
    }
    
    public int getRecommendedCount()
    {
        return recommendedCount;
    }
    
    public void setRecommendedCount(int recommendedCount)
    {
        this.recommendedCount = recommendedCount;
    }
    
    public int getRecommendedOrderCount()
    {
        return recommendedOrderCount;
    }
    
    public void setRecommendedOrderCount(int recommendedOrderCount)
    {
        this.recommendedOrderCount = recommendedOrderCount;
    }
    
    public int getSubRecommendedCount()
    {
        return subRecommendedCount;
    }
    
    public void setSubRecommendedCount(int subRecommendedCount)
    {
        this.subRecommendedCount = subRecommendedCount;
    }
    
    public int getTotalRecommendedPoint()
    {
        return totalRecommendedPoint;
    }
    
    public void setTotalRecommendedPoint(int totalRecommendedPoint)
    {
        this.totalRecommendedPoint = totalRecommendedPoint;
    }
    
    public int getIsRecommended()
    {
        return isRecommended;
    }
    
    public void setIsRecommended(int isRecommended)
    {
        this.isRecommended = isRecommended;
    }
    
    public int getPartnerStatus()
    {
        return partnerStatus;
    }
    
    public void setPartnerStatus(int partnerStatus)
    {
        this.partnerStatus = partnerStatus;
    }
    
    public int getApplyPartnerStatus()
    {
        return applyPartnerStatus;
    }
    
    public void setApplyPartnerStatus(int applyPartnerStatus)
    {
        this.applyPartnerStatus = applyPartnerStatus;
    }
    
    public int getIsHasOrder()
    {
        return isHasOrder;
    }
    
    public void setIsHasOrder(int isHasOrder)
    {
        this.isHasOrder = isHasOrder;
    }
    
}
