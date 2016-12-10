package com.ygg.admin.entity;

public class CustomCenterEntity
{
    private int id;
    
    /**展示活动类型；1：一个活动，2：两个活动，3：三个活动，4：四个活动*/
    private Byte displayType;
    
    private String oneImage = "";
    
    /**第一张类型；1：特卖商品，2：通用专场，3：网页自定义页面，4：商城商品，5：原生自定义页面，6：积分商城*/
    private Byte oneType;
    
    private int oneDisplayId;
    
    private String oneRemark = "";
    
    private String oneTitle = "";
    
    private String oneTitleColor = "";
    
    private int oneWidth;
    
    private int oneHeight;
    
    private String twoImage = "";
    
    /**第二张类型；1：特卖商品，2：通用专场，3：网页自定义页面，4：商城商品，5：原生自定义页面，6：积分商城*/
    private Byte twoType;
    
    private int twoDisplayId;
    
    private String twoRemark = "";
    
    private String twoTitle = "";
    
    private String twoTitleColor = "";
    
    private int twoWidth;
    
    private int twoHeight;
    
    private String threeImage = "";
    
    /**第三张类型；1：特卖商品，2：通用专场，3：网页自定义页面，4：商城商品，5：原生自定义页面，6：积分商城*/
    private Byte threeType;
    
    private int threeDisplayId;
    
    private String threeRemark = "";
    
    private String threeTitle = "";
    
    private String threeTitleColor = "";
    
    private int threeWidth;
    
    private int threeHeight;
    
    private String fourImage = "";
    
    /**第四张类型；1：特卖商品，2：通用专场，3：网页自定义页面，4：商城商品，5：原生自定义页面，6：积分商城*/
    private Byte fourType;
    
    private int fourDisplayId;
    
    private String fourRemark = "";
    
    private String fourTitle = "";
    
    private String fourTitleColor = "";
    
    private int fourWidth;
    
    private int fourHeight;
    
    private int isDisplay;
    
    private String remark = "";
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public Byte getDisplayType()
    {
        return displayType;
    }
    
    public void setDisplayType(Byte displayType)
    {
        this.displayType = displayType;
    }
    
    public String getOneImage()
    {
        return oneImage;
    }
    
    public void setOneImage(String oneImage)
    {
        this.oneImage = oneImage;
    }
    
    public Byte getOneType()
    {
        return oneType;
    }
    
    public void setOneType(Byte oneType)
    {
        this.oneType = oneType;
    }
    
    public int getOneDisplayId()
    {
        return oneDisplayId;
    }
    
    public void setOneDisplayId(int oneDisplayId)
    {
        this.oneDisplayId = oneDisplayId;
    }
    
    public String getOneRemark()
    {
        return oneRemark;
    }
    
    public void setOneRemark(String oneRemark)
    {
        this.oneRemark = oneRemark;
    }
    
    public String getOneTitle()
    {
        return oneTitle;
    }
    
    public void setOneTitle(String oneTitle)
    {
        this.oneTitle = oneTitle;
    }
    
    public String getOneTitleColor()
    {
        return oneTitleColor;
    }
    
    public void setOneTitleColor(String oneTitleColor)
    {
        this.oneTitleColor = oneTitleColor;
    }
    
    public int getOneWidth()
    {
        return oneWidth;
    }
    
    public void setOneWidth(int oneWidth)
    {
        this.oneWidth = oneWidth;
    }
    
    public int getOneHeight()
    {
        return oneHeight;
    }
    
    public void setOneHeight(int oneHeight)
    {
        this.oneHeight = oneHeight;
    }
    
    public String getTwoImage()
    {
        return twoImage;
    }
    
    public void setTwoImage(String twoImage)
    {
        this.twoImage = twoImage;
    }
    
    public Byte getTwoType()
    {
        return twoType;
    }
    
    public void setTwoType(Byte twoType)
    {
        this.twoType = twoType;
    }
    
    public int getTwoDisplayId()
    {
        return twoDisplayId;
    }
    
    public void setTwoDisplayId(int twoDisplayId)
    {
        this.twoDisplayId = twoDisplayId;
    }
    
    public String getTwoRemark()
    {
        return twoRemark;
    }
    
    public void setTwoRemark(String twoRemark)
    {
        this.twoRemark = twoRemark;
    }
    
    public String getTwoTitle()
    {
        return twoTitle;
    }
    
    public void setTwoTitle(String twoTitle)
    {
        this.twoTitle = twoTitle;
    }
    
    public String getTwoTitleColor()
    {
        return twoTitleColor;
    }
    
    public void setTwoTitleColor(String twoTitleColor)
    {
        this.twoTitleColor = twoTitleColor;
    }
    
    public int getTwoWidth()
    {
        return twoWidth;
    }
    
    public void setTwoWidth(int twoWidth)
    {
        this.twoWidth = twoWidth;
    }
    
    public int getTwoHeight()
    {
        return twoHeight;
    }
    
    public void setTwoHeight(int twoHeight)
    {
        this.twoHeight = twoHeight;
    }
    
    public String getThreeImage()
    {
        return threeImage;
    }
    
    public void setThreeImage(String threeImage)
    {
        this.threeImage = threeImage;
    }
    
    public Byte getThreeType()
    {
        return threeType;
    }
    
    public void setThreeType(Byte threeType)
    {
        this.threeType = threeType;
    }
    
    public int getThreeDisplayId()
    {
        return threeDisplayId;
    }
    
    public void setThreeDisplayId(int threeDisplayId)
    {
        this.threeDisplayId = threeDisplayId;
    }
    
    public String getThreeRemark()
    {
        return threeRemark;
    }
    
    public void setThreeRemark(String threeRemark)
    {
        this.threeRemark = threeRemark;
    }
    
    public String getThreeTitle()
    {
        return threeTitle;
    }
    
    public void setThreeTitle(String threeTitle)
    {
        this.threeTitle = threeTitle;
    }
    
    public String getThreeTitleColor()
    {
        return threeTitleColor;
    }
    
    public void setThreeTitleColor(String threeTitleColor)
    {
        this.threeTitleColor = threeTitleColor;
    }
    
    public int getThreeWidth()
    {
        return threeWidth;
    }
    
    public void setThreeWidth(int threeWidth)
    {
        this.threeWidth = threeWidth;
    }
    
    public int getThreeHeight()
    {
        return threeHeight;
    }
    
    public void setThreeHeight(int threeHeight)
    {
        this.threeHeight = threeHeight;
    }
    
    public String getFourImage()
    {
        return fourImage;
    }
    
    public void setFourImage(String fourImage)
    {
        this.fourImage = fourImage;
    }
    
    public Byte getFourType()
    {
        return fourType;
    }
    
    public void setFourType(Byte fourType)
    {
        this.fourType = fourType;
    }
    
    public int getFourDisplayId()
    {
        return fourDisplayId;
    }
    
    public void setFourDisplayId(int fourDisplayId)
    {
        this.fourDisplayId = fourDisplayId;
    }
    
    public String getFourRemark()
    {
        return fourRemark;
    }
    
    public void setFourRemark(String fourRemark)
    {
        this.fourRemark = fourRemark;
    }
    
    public String getFourTitle()
    {
        return fourTitle;
    }
    
    public void setFourTitle(String fourTitle)
    {
        this.fourTitle = fourTitle;
    }
    
    public String getFourTitleColor()
    {
        return fourTitleColor;
    }
    
    public void setFourTitleColor(String fourTitleColor)
    {
        this.fourTitleColor = fourTitleColor;
    }
    
    public int getFourWidth()
    {
        return fourWidth;
    }
    
    public void setFourWidth(int fourWidth)
    {
        this.fourWidth = fourWidth;
    }
    
    public int getFourHeight()
    {
        return fourHeight;
    }
    
    public void setFourHeight(int fourHeight)
    {
        this.fourHeight = fourHeight;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
}
