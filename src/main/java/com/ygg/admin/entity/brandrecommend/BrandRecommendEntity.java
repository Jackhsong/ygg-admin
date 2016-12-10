
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.brandrecommend;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: BrandRecommendEntity.java 8478 2016-03-07 01:57:30Z zhanglide $   
  * @since 2.0
  */
public class BrandRecommendEntity extends BaseEntity {
	
	/**品牌标题    */
	private String title;
	
	 /**品牌图片url */
	private String image;
	
	 /**图片宽度，固定750*/
	private int width;
	
	 /**图片高度，不限    */
	private int height;
	
	 /**类型；2：通用专场，3：网页自定义活动，7：原生自定义活动    */
	private int type;
	
	 /**类型对应的id*/
	private int displayId;
	
	 /**排序值，从大到小排序    */
	private int sequence;
	
	 /**是否展现；0：否，1：是    */
	private int isDisplay;

	
	/**  
	 *@return  the title
	 */
	public String getTitle() {
		return title;
	}

	
	/** 
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 *@return  the width
	 */
	public int getWidth() {
		return width;
	}

	
	/** 
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	
	/**  
	 *@return  the height
	 */
	public int getHeight() {
		return height;
	}

	
	/** 
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	
	/**  
	 *@return  the type
	 */
	public int getType() {
		return type;
	}

	
	/** 
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	
	/**  
	 *@return  the displayId
	 */
	public int getDisplayId() {
		return displayId;
	}

	
	/** 
	 * @param displayId the displayId to set
	 */
	public void setDisplayId(int displayId) {
		this.displayId = displayId;
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
}
