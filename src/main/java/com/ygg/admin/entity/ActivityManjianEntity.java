package com.ygg.admin.entity;

public class ActivityManjianEntity
{
    private int id;
    
    private String name;
    
    /**满减类型；1：全场，2：通用专场，3：网页自定义活动，4：原生自定义活动*/
    private Byte type;
    
    private int typeId;
    
    private String startTime;
    
    private String endTime;
    
    /**梯度类型；1：一档，2：二档，3：三档，4：四档*/
    private Byte gradientType;
    
    /**第一档阀值*/
    private Integer oneThreshold;
    
    /**第一档减免金额*/
    private Integer oneReduce;
    
    /**第二档阀值*/
    private Integer twoThreshold;
    
    /**第二档减免金额*/
    private Integer twoReduce;
    
    /**第三档阀值*/
    private Integer threeThreshold;
    
    /**第三档减免金额*/
    private Integer threeReduce;
    
    /**第四档阀值*/
    private Integer fourThreshold;
    
    /**第四档减免金额*/
    private Integer fourReduce;
    
    private String remark = "";
    
    private int isAvailable;
    
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
    
    public Byte getType()
    {
        return type;
    }
    
    public void setType(Byte type)
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
    
    public Byte getGradientType()
    {
        return gradientType;
    }
    
    public void setGradientType(Byte gradientType)
    {
        this.gradientType = gradientType;
    }
    
    public Integer getOneThreshold()
    {
        return oneThreshold;
    }
    
    public void setOneThreshold(Integer oneThreshold)
    {
        this.oneThreshold = oneThreshold;
    }
    
    public Integer getOneReduce()
    {
        return oneReduce;
    }
    
    public void setOneReduce(Integer oneReduce)
    {
        this.oneReduce = oneReduce;
    }
    
    public Integer getTwoThreshold()
    {
        return twoThreshold;
    }
    
    public void setTwoThreshold(Integer twoThreshold)
    {
        this.twoThreshold = twoThreshold;
    }
    
    public Integer getTwoReduce()
    {
        return twoReduce;
    }
    
    public void setTwoReduce(Integer twoReduce)
    {
        this.twoReduce = twoReduce;
    }
    
    public Integer getThreeThreshold()
    {
        return threeThreshold;
    }
    
    public void setThreeThreshold(Integer threeThreshold)
    {
        this.threeThreshold = threeThreshold;
    }
    
    public Integer getThreeReduce()
    {
        return threeReduce;
    }
    
    public void setThreeReduce(Integer threeReduce)
    {
        this.threeReduce = threeReduce;
    }
    
    public Integer getFourThreshold()
    {
        return fourThreshold;
    }
    
    public void setFourThreshold(Integer fourThreshold)
    {
        this.fourThreshold = fourThreshold;
    }
    
    public Integer getFourReduce()
    {
        return fourReduce;
    }
    
    public void setFourReduce(Integer fourReduce)
    {
        this.fourReduce = fourReduce;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public int getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(int isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
}
