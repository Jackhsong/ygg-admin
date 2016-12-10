
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.categoryclassification;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryClassificationEntity.java 8535 2016-03-08 12:45:02Z zhanglide $   
  * @since 2.0
  */
public class CategoryClassificationEntity extends BaseEntity {

	
	 /**原生自定义页面2模块id*/
	private int page2ModelId;
	/**分类名称*/
	private String name;
	/**类型；1：商城分类，2：通用专场（组合特卖），3：自定义活动，4：原生自定义 */
	private int type;
	/**对应跳转id，当type=2、3、4时，有效*/
	private int displayId;
	 /**一级分类id，当type=1时，有效*/
	private int firstCategoryId;
	 /**二级分类id，当type=1时，有效    */
	private int secondCategoryId;
	 /**三级分类Id，当type=1时，有效    */
	private int thirdCategoryId;
	 /**品牌图片url */
	private String image;
	 /**图片宽度，固定750*/
	private int width;
	 /**图片高度，不限    */
	private int height;
	 /**排序值，从大到小排序    */
	private int sequence;
	 /**是否展现；0：否，1：是    */
	private int isDisplay;
	
	/**  
	 *@return  the page2ModelId
	 */
	public int getPage2ModelId() {
		return page2ModelId;
	}
	
	/** 
	 * @param page2ModelId the page2ModelId to set
	 */
	public void setPage2ModelId(int page2ModelId) {
		this.page2ModelId = page2ModelId;
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
	 *@return  the firstCategoryId
	 */
	public int getFirstCategoryId() {
		return firstCategoryId;
	}
	
	/** 
	 * @param firstCategoryId the firstCategoryId to set
	 */
	public void setFirstCategoryId(int firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}
	
	/**  
	 *@return  the secondCategoryId
	 */
	public int getSecondCategoryId() {
		return secondCategoryId;
	}
	
	/** 
	 * @param secondCategoryId the secondCategoryId to set
	 */
	public void setSecondCategoryId(int secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}
	
	
	/**  
	 *@return  the thirdCategoryId
	 */
	public int getThirdCategoryId() {
		return thirdCategoryId;
	}

	
	/** 
	 * @param thirdCategoryId the thirdCategoryId to set
	 */
	public void setThirdCategoryId(int thirdCategoryId) {
		this.thirdCategoryId = thirdCategoryId;
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
