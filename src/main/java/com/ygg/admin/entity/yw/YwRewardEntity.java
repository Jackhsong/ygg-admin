
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
  * @version $Id: QqbsRewardEntity.java 10130 2016-04-12 10:32:37Z zhanglide $   
  * @since 2.0
  */
public class YwRewardEntity extends BaseEntity {
	
	 /**    */
	private static final long serialVersionUID = -3320803222370543433L;
	/**帐号ID    */
	private int accountId;
	 /**总奖励*/
	private float totalReward;
	  
	 /**已提现    */
	private float alreadyCash;
	  
	 /** 可提现   */
	private float withdrawCash;

	
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
	 *@return  the totalReward
	 */
	public float getTotalReward() {
		return totalReward;
	}

	
	/** 
	 * @param totalReward the totalReward to set
	 */
	public void setTotalReward(float totalReward) {
		this.totalReward = totalReward;
	}

	
	/**  
	 *@return  the alreadyCash
	 */
	public float getAlreadyCash() {
		return alreadyCash;
	}

	
	/** 
	 * @param alreadyCash the alreadyCash to set
	 */
	public void setAlreadyCash(float alreadyCash) {
		this.alreadyCash = alreadyCash;
	}

	
	/**  
	 *@return  the withdrawCash
	 */
	public float getWithdrawCash() {
		return withdrawCash;
	}

	
	/** 
	 * @param withdrawCash the withdrawCash to set
	 */
	public void setWithdrawCash(float withdrawCash) {
		this.withdrawCash = withdrawCash;
	}
}
