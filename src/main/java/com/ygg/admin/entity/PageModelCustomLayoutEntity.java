package com.ygg.admin.entity;

public class PageModelCustomLayoutEntity
{
    private int id;
    
    /** 页面模块ID */
    private int pageModelId;
    
    /** 排序值 */
    private int sequence;
    
    /** 展示类型；1：单张，2：两张，3：四张 */
    private int displayType;
    
    /** 第一张图片url */
    private String oneImage;
    
    /** 第一张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴 */
    private int oneType;
    
    /** 第一张类型对应的id */
    private int oneDisplayId;
    
    /** 第一张备注 */
    private String oneRemark;
    
    /** 第一张图片宽度 */
    private int oneWidth;
    
    /** 第一张图片高度 */
    private int oneHeight;
    
    /** 第二张图片url */
    private String twoImage;
    
    /** 第二张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴 */
    private int twoType;
    
    /** 第二张类型对应的id */
    private int twoDisplayId;
    
    /** 第二张备注 */
    private String twoRemark;
    
    /** 第二张图片宽度 */
    private int twoWidth;
    
    /** 第二张图片高度 */
    private int twoHeight;
    
    /** 第三张图片url */
    private String threeImage;
    
    /** 第三张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴 */
    private int threeType;
    
    /** 第三张类型对应的id */
    private int threeDisplayId;
    
    /** 第三张备注 */
    private String threeRemark;
    
    /** 第三张图片宽度 */
    private int threeWidth;
    
    /** 第三张图片高度 */
    private int threeHeight;
    
    /** 第四张图片url */
    private String fourImage;
    
    /** 第四张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴 */
    private int fourType;
    
    /** 第四张类型对应的id */
    private int fourDisplayId;
    
    /** 第四张备注 */
    private String fourRemark;
    
    /** 第四张图片宽度 */
    private int fourWidth;
    
    /** 第四张图片高度 */
    private int fourHeight;
    
    /** 是否展现，1是，0否 */
    private int isDisplay;
    
    /** 创建时间 */
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getDisplayType()
    {
        return displayType;
    }
    
    public void setDisplayType(int displayType)
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
    
    public int getOneType()
    {
        return oneType;
    }
    
    public void setOneType(int oneType)
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
    
    public int getTwoType()
    {
        return twoType;
    }
    
    public void setTwoType(int twoType)
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
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getIsDisplay()
    {
        return isDisplay;
    }
    
    public void setIsDisplay(int isDisplay)
    {
        this.isDisplay = isDisplay;
    }
    
    public String getThreeImage()
    {
        return threeImage;
    }
    
    public void setThreeImage(String threeImage)
    {
        this.threeImage = threeImage;
    }
    
    public int getThreeType()
    {
        return threeType;
    }
    
    public void setThreeType(int threeType)
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
    
    public int getFourType()
    {
        return fourType;
    }
    
    public void setFourType(int fourType)
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
    
    public int getPageModelId()
    {
        return pageModelId;
    }
    
    public void setPageModelId(int pageModelId)
    {
        this.pageModelId = pageModelId;
    }
    
    public int getSequence()
    {
        return sequence;
    }
    
    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }
}
