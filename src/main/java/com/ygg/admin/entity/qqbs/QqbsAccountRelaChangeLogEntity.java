
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.qqbs;

import com.ygg.admin.entity.base.BaseEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsAccountRelaChangeLogEntity.java 10347 2016-04-16 07:27:17Z zhanglide $   
  * @since 2.0
  */
public class QqbsAccountRelaChangeLogEntity extends BaseEntity
{
    
     /**    */
    private static final long serialVersionUID = 8812740968155679132L;
    private int accountId;
    private int tuiAccountId;
    private String operator;
    private String remark;
    
    /**  
     *@return  the accountId
     */
    public int getAccountId()
    {
        return accountId;
    }
    
    /** 
     * @param accountId the accountId to set
     */
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }
    
    /**  
     *@return  the tuiAccountId
     */
    public int getTuiAccountId()
    {
        return tuiAccountId;
    }
    
    /** 
     * @param tuiAccountId the tuiAccountId to set
     */
    public void setTuiAccountId(int tuiAccountId)
    {
        this.tuiAccountId = tuiAccountId;
    }
    
    /**  
     *@return  the operator
     */
    public String getOperator()
    {
        return operator;
    }
    
    /** 
     * @param operator the operator to set
     */
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    /**  
     *@return  the remark
     */
    public String getRemark()
    {
        return remark;
    }
    
    /** 
     * @param remark the remark to set
     */
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
}
