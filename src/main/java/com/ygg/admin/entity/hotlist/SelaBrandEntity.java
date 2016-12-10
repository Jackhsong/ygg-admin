
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.hotlist;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: SelaBrandEntity.java 9237 2016-03-25 03:10:00Z zhanglide $   
  * @since 2.0
  */
public class SelaBrandEntity extends BaseEntity {
	
	 /** 品牌id   */
	private int brandId;
	
	 /** 品牌名称   */
	private String name;
	
	 /**品牌图片访问URL    */
	private String image;
	
	 /** 品牌所属国   */
	private int stateId;
	
	 /** 品牌类目   */
	private int categoryFirstId;
	
	 /**排序值，从大到小排序    */
	private int sequence;
	/**是否展现；0：否，1：是    */
    private int isDisplay;
     /**品牌头图*/
    private String headImage;
    
	/**  
	 *@return  the brandId
	 */
	public int getBrandId() {
		return brandId;
	}

	
	/** 
	 * @param brandId the brandId to set
	 */
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	
	/**  
	 *@return  the name
	 */
	public String getName() {
		return name;
	}

	
	/** 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**  
	 *@return  the image
	 */
	public String getImage() {
		return image;
	}

	
	/** 
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
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
	 *@return  the sequence
	 */
	public int getSequence() {
		return sequence;
	}

	
	/** 
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
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


	
	/**  
	 *@return  the headImage
	 */
	public String getHeadImage() {
		return headImage;
	}


	
	/** 
	 * @param headImage the headImage to set
	 */
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
}
