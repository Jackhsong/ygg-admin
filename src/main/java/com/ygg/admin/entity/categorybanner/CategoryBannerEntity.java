
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.categorybanner;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryBannerEntity.java 8535 2016-03-08 12:45:02Z zhanglide $   
  * @since 2.0
  */
public class CategoryBannerEntity extends BaseEntity {
	
	private int page2ModelId;
	
	private int type;
	
	
	private int displayId;
    
    
    
    private String desc;
    
    private String image;
    
    private String startTime;
    
    private String endTime;
    
    private byte isDisplay;
    
    private int sequence;

	
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
	 *@return  the desc
	 */
	public String getDesc() {
		return desc;
	}

	
	/** 
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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
	 *@return  the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	
	/** 
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	
	/**  
	 *@return  the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	
	/** 
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	/**  
	 *@return  the isDisplay
	 */
	public byte getIsDisplay() {
		return isDisplay;
	}

	
	/** 
	 * @param isDisplay the isDisplay to set
	 */
	public void setIsDisplay(byte isDisplay) {
		this.isDisplay = isDisplay;
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
}
