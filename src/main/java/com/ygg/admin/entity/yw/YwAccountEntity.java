
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.yw;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsAccountEntity.java 10162 2016-04-13 05:54:23Z zhanglide $   
  * @since 2.0
  */
public class YwAccountEntity extends BaseEntity {
	
	 /**    */
	private static final long serialVersionUID = 5534106697904922185L;

	private int accountId;
    
    private String openId;
    
    private String unionId;
    
    private int fatherAccountId;
    
    private String nickName;
    
    private String image;
    
    private String subscribe;
	
	/**  
	 *@return  the accountId
	 */
	public int getAccountId() {
		return accountId;
	}
	
	/** 
	 * @param accountId the accountId to set
	 */
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	/**  
	 *@return  the openId
	 */
	public String getOpenId() {
		return openId;
	}
	
	/** 
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	/**  
	 *@return  the unionId
	 */
	public String getUnionId() {
		return unionId;
	}
	
	/** 
	 * @param unionId the unionId to set
	 */
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	
	/**  
	 *@return  the fatherAccountId
	 */
	public int getFatherAccountId() {
		return fatherAccountId;
	}
	
	/** 
	 * @param fatherAccountId the fatherAccountId to set
	 */
	public void setFatherAccountId(int fatherAccountId) {
		this.fatherAccountId = fatherAccountId;
	}
	
	/**  
	 *@return  the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	
	/** 
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	 *@return  the subscribe
	 */
	public String getSubscribe() {
		return subscribe;
	}
	
	/** 
	 * @param subscribe the subscribe to set
	 */
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
}
