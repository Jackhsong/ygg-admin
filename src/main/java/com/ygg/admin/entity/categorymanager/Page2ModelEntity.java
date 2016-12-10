
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.categorymanager;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: Page2ModelEntity.java 8535 2016-03-08 12:45:02Z zhanglide $   
  * @since 2.0
  */
public class Page2ModelEntity extends BaseEntity {
	
    private int pageId;
    
    private int type;
    
    private int isDisplay;
    
    private int sequence;

	
	/**  
	 *@return  the pageId
	 */
	public int getPageId() {
		return pageId;
	}

	
	/** 
	 * @param pageId the pageId to set
	 */
	public void setPageId(int pageId) {
		this.pageId = pageId;
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
