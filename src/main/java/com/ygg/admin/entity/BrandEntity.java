package com.ygg.admin.entity;

import java.util.List;

public class  BrandEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    private String name;

    private String enName;

    private String adImage;

    private String image;

    private byte isAvailable;
    
    private String createTime;

    private int returnDistributionProportionType = 1;

    private int activityType;

    private int activityDisplayId;

    private int isShowActivity;

     /**品牌类目*/
    private int categoryFirstId;

    /**多个品牌类目*/
    private List<Integer> categoryFirstIdList;

     /**品牌国家    */
    private int stateId;
     /** 品牌热点   */
    private String hotSpots;
    
     /** 品牌介绍   */
    private String introduce;
    
    public int getActivityType()
    {
        return activityType;
    }

    public void setActivityType(int activityType)
    {
        this.activityType = activityType;
    }

    public int getActivityDisplayId()
    {
        return activityDisplayId;
    }

    public void setActivityDisplayId(int activityDisplayId)
    {
        this.activityDisplayId = activityDisplayId;
    }

    public int getIsShowActivity()
    {
        return isShowActivity;
    }

    public void setIsShowActivity(int isShowActivity)
    {
        this.isShowActivity = isShowActivity;
    }

    public int getReturnDistributionProportionType()
    {
        return returnDistributionProportionType;
    }

    public void setReturnDistributionProportionType(int returnDistributionProportionType)
    {
        this.returnDistributionProportionType = returnDistributionProportionType;
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
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    
    public byte getIsAvailable()
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
    
    @Override
    public String toString()
    {
        return id + ":" + name;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof BrandEntity)
        {
            BrandEntity other = (BrandEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }

	
	/**  
	 *@return  the categoryFirstId
	 */
	public int getCategoryFirstId() {
		return categoryFirstId;
	}

	
	/** 
	 * @param categoryFirstId the categoryFirstId to set
	 */
	public void setCategoryFirstId(int categoryFirstId) {
		this.categoryFirstId = categoryFirstId;
	}

	
	/**  
	 *@return  the stateId
	 */
	public int getStateId() {
		return stateId;
	}

	
	/** 
	 * @param stateId the stateId to set
	 */
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	
	/**  
	 *@return  the hotSpots
	 */
	public String getHotSpots() {
		return hotSpots;
	}

	
	/** 
	 * @param hotSpots the hotSpots to set
	 */
	public void setHotSpots(String hotSpots) {
		this.hotSpots = hotSpots;
	}

	
	/**  
	 *@return  the introduce
	 */
	public String getIntroduce() {
		return introduce;
	}

	
	/** 
	 * @param introduce the introduce to set
	 */
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public List<Integer> getCategoryFirstIdList() {
        return categoryFirstIdList;
    }

    public void setCategoryFirstIdList(List<Integer> categoryFirstIdList) {
        this.categoryFirstIdList = categoryFirstIdList;
    }

}
