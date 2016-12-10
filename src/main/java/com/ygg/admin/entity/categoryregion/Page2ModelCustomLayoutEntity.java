
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.categoryregion;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: Page2ModelCustomLayoutEntity.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
public class Page2ModelCustomLayoutEntity extends BaseEntity {
	
	 /**    */
	private int page2ModelCustomRegionId;
	/**展示类型；1：单张，1：两张*/
    private int displayType;
    
    /**第一张图片url*/
    private String oneImage;
    
    /**第一张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴*/
    private int oneType;
    
    /**第一张类型对应的id*/
    private int oneDisplayId;
    
    /**第一张备注*/
    private String oneRemark;
    
    /**第一张图片宽度*/
    private int oneWidth;
    
    /**第一张图片高度*/
    private int oneHeight;
    
    /**第二张图片url*/
    private String twoImage;
    
    /**第二张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴*/
    private int twoType;
    
    /**第二张类型对应的id*/
    private int twoDisplayId;
    
    /**第二张备注*/
    private String twoRemark;
    
    /**第二张图片宽度*/
    private int twoWidth;
    
    /**第二张图片高度*/
    private int twoHeight;

    /**第三张图片url*/
    private String threeImage;

    /**第三张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴*/
    private int threeType;

    /**第三张类型对应的id*/
    private int threeDisplayId;

    /**第三张备注*/
    private String threeRemark;

    /**第三张图片宽度*/
    private int threeWidth;

    /**第三张图片高度*/
    private int threeHeight;

    /**第四张图片url*/
    private String fourImage;

    /**第四张类型；1：特卖商品，2：通用专场，3：自定义专场，4：商城商品, 5：签到，6：邀请小伙伴*/
    private int fourType;

    /**第四张类型对应的id*/
    private int fourDisplayId;

    /**第四张备注*/
    private String fourRemark;

    /**第四张图片宽度*/
    private int fourWidth;

    /**第四张图片高度*/
    private int fourHeight;
    
    /**是否展现，1是，0否*/
    private int isDisplay;

	
	/**  
	 *@return  the page2ModelCustomRegionId
	 */
	public int getPage2ModelCustomRegionId() {
		return page2ModelCustomRegionId;
	}

	
	/** 
	 * @param page2ModelCustomRegionId the page2ModelCustomRegionId to set
	 */
	public void setPage2ModelCustomRegionId(int page2ModelCustomRegionId) {
		this.page2ModelCustomRegionId = page2ModelCustomRegionId;
	}

	
	/**  
	 *@return  the displayType
	 */
	public int getDisplayType() {
		return displayType;
	}

	
	/** 
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	
	/**  
	 *@return  the oneImage
	 */
	public String getOneImage() {
		return oneImage;
	}

	
	/** 
	 * @param oneImage the oneImage to set
	 */
	public void setOneImage(String oneImage) {
		this.oneImage = oneImage;
	}

	
	/**  
	 *@return  the oneType
	 */
	public int getOneType() {
		return oneType;
	}

	
	/** 
	 * @param oneType the oneType to set
	 */
	public void setOneType(int oneType) {
		this.oneType = oneType;
	}

	
	/**  
	 *@return  the oneDisplayId
	 */
	public int getOneDisplayId() {
		return oneDisplayId;
	}

	
	/** 
	 * @param oneDisplayId the oneDisplayId to set
	 */
	public void setOneDisplayId(int oneDisplayId) {
		this.oneDisplayId = oneDisplayId;
	}

	
	/**  
	 *@return  the oneRemark
	 */
	public String getOneRemark() {
		return oneRemark;
	}

	
	/** 
	 * @param oneRemark the oneRemark to set
	 */
	public void setOneRemark(String oneRemark) {
		this.oneRemark = oneRemark;
	}

	
	/**  
	 *@return  the oneWidth
	 */
	public int getOneWidth() {
		return oneWidth;
	}

	
	/** 
	 * @param oneWidth the oneWidth to set
	 */
	public void setOneWidth(int oneWidth) {
		this.oneWidth = oneWidth;
	}

	
	/**  
	 *@return  the oneHeight
	 */
	public int getOneHeight() {
		return oneHeight;
	}

	
	/** 
	 * @param oneHeight the oneHeight to set
	 */
	public void setOneHeight(int oneHeight) {
		this.oneHeight = oneHeight;
	}

	
	/**  
	 *@return  the twoImage
	 */
	public String getTwoImage() {
		return twoImage;
	}

	
	/** 
	 * @param twoImage the twoImage to set
	 */
	public void setTwoImage(String twoImage) {
		this.twoImage = twoImage;
	}

	
	/**  
	 *@return  the twoType
	 */
	public int getTwoType() {
		return twoType;
	}

	
	/** 
	 * @param twoType the twoType to set
	 */
	public void setTwoType(int twoType) {
		this.twoType = twoType;
	}

	
	/**  
	 *@return  the twoDisplayId
	 */
	public int getTwoDisplayId() {
		return twoDisplayId;
	}

	
	/** 
	 * @param twoDisplayId the twoDisplayId to set
	 */
	public void setTwoDisplayId(int twoDisplayId) {
		this.twoDisplayId = twoDisplayId;
	}

	
	/**  
	 *@return  the twoRemark
	 */
	public String getTwoRemark() {
		return twoRemark;
	}

	
	/** 
	 * @param twoRemark the twoRemark to set
	 */
	public void setTwoRemark(String twoRemark) {
		this.twoRemark = twoRemark;
	}

	
	/**  
	 *@return  the twoWidth
	 */
	public int getTwoWidth() {
		return twoWidth;
	}

	
	/** 
	 * @param twoWidth the twoWidth to set
	 */
	public void setTwoWidth(int twoWidth) {
		this.twoWidth = twoWidth;
	}

	
	/**  
	 *@return  the twoHeight
	 */
	public int getTwoHeight() {
		return twoHeight;
	}

	
	/** 
	 * @param twoHeight the twoHeight to set
	 */
	public void setTwoHeight(int twoHeight) {
		this.twoHeight = twoHeight;
	}

	
	/**  
	 *@return  the threeImage
	 */
	public String getThreeImage() {
		return threeImage;
	}

	
	/** 
	 * @param threeImage the threeImage to set
	 */
	public void setThreeImage(String threeImage) {
		this.threeImage = threeImage;
	}

	
	/**  
	 *@return  the threeType
	 */
	public int getThreeType() {
		return threeType;
	}

	
	/** 
	 * @param threeType the threeType to set
	 */
	public void setThreeType(int threeType) {
		this.threeType = threeType;
	}

	
	/**  
	 *@return  the threeDisplayId
	 */
	public int getThreeDisplayId() {
		return threeDisplayId;
	}

	
	/** 
	 * @param threeDisplayId the threeDisplayId to set
	 */
	public void setThreeDisplayId(int threeDisplayId) {
		this.threeDisplayId = threeDisplayId;
	}

	
	/**  
	 *@return  the threeRemark
	 */
	public String getThreeRemark() {
		return threeRemark;
	}

	
	/** 
	 * @param threeRemark the threeRemark to set
	 */
	public void setThreeRemark(String threeRemark) {
		this.threeRemark = threeRemark;
	}

	
	/**  
	 *@return  the threeWidth
	 */
	public int getThreeWidth() {
		return threeWidth;
	}

	
	/** 
	 * @param threeWidth the threeWidth to set
	 */
	public void setThreeWidth(int threeWidth) {
		this.threeWidth = threeWidth;
	}

	
	/**  
	 *@return  the threeHeight
	 */
	public int getThreeHeight() {
		return threeHeight;
	}

	
	/** 
	 * @param threeHeight the threeHeight to set
	 */
	public void setThreeHeight(int threeHeight) {
		this.threeHeight = threeHeight;
	}

	
	/**  
	 *@return  the fourImage
	 */
	public String getFourImage() {
		return fourImage;
	}

	
	/** 
	 * @param fourImage the fourImage to set
	 */
	public void setFourImage(String fourImage) {
		this.fourImage = fourImage;
	}

	
	/**  
	 *@return  the fourType
	 */
	public int getFourType() {
		return fourType;
	}

	
	/** 
	 * @param fourType the fourType to set
	 */
	public void setFourType(int fourType) {
		this.fourType = fourType;
	}

	
	/**  
	 *@return  the fourDisplayId
	 */
	public int getFourDisplayId() {
		return fourDisplayId;
	}

	
	/** 
	 * @param fourDisplayId the fourDisplayId to set
	 */
	public void setFourDisplayId(int fourDisplayId) {
		this.fourDisplayId = fourDisplayId;
	}

	
	/**  
	 *@return  the fourRemark
	 */
	public String getFourRemark() {
		return fourRemark;
	}

	
	/** 
	 * @param fourRemark the fourRemark to set
	 */
	public void setFourRemark(String fourRemark) {
		this.fourRemark = fourRemark;
	}

	
	/**  
	 *@return  the fourWidth
	 */
	public int getFourWidth() {
		return fourWidth;
	}

	
	/** 
	 * @param fourWidth the fourWidth to set
	 */
	public void setFourWidth(int fourWidth) {
		this.fourWidth = fourWidth;
	}

	
	/**  
	 *@return  the fourHeight
	 */
	public int getFourHeight() {
		return fourHeight;
	}

	
	/** 
	 * @param fourHeight the fourHeight to set
	 */
	public void setFourHeight(int fourHeight) {
		this.fourHeight = fourHeight;
	}

	
	/**  
	 *@return  the isDisplay
	 */
	public int getIsDisplay() {
		return isDisplay;
	}

	
	/** 
	 * @param isDisplay the isDisplay to set
	 */
	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}
}
