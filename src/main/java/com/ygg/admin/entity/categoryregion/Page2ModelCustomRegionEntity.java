
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
  * @version $Id: Page2ModelCustomRegionEntity.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
public class Page2ModelCustomRegionEntity extends BaseEntity {
	
	/**原生自定义页面2模块id*/
	private int page2ModelId;
	
	private String title;
    
    private String remark;
    
    private int isDisplay;
    
    private int isAvailable;
    
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
	 *@return  the remark
	 */
	public String getRemark() {
		return remark;
	}

	
	/** 
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	 *@return  the isAvailable
	 */
	public int getIsAvailable() {
		return isAvailable;
	}

	
	/** 
	 * @param isAvailable the isAvailable to set
	 */
	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
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
