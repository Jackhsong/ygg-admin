
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
  * @version $Id: QqbsAccountUpgradeLog.java 11707 2016-05-12 11:56:13Z zhanglide $   
  * @since 2.0
  */
public class QqbsAccountUpgradeLog extends BaseEntity
{
    
     /**    */
    private static final long serialVersionUID = 8329473987583472013L;
    /**用户ID*/
    private int accountId;
     /**申请身份：0经理 1总监*/
    private int flag;
    
     /** 姓名*/
    private String name;
    
     /**身份证号*/
    private String cardId;
    
     /**昵称*/
    private String nickname;
    
     /**手机号*/
    private String number;
    
    /**审批状态 0：待审批1：审批通过 2：审批不通过*/
    private int status;
    /**生效时间*/
    private String forceTime;
    
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
     *@return  the flag
     */
    public int getFlag()
    {
        return flag;
    }
    
    /** 
     * @param flag the flag to set
     */
    public void setFlag(int flag)
    {
        this.flag = flag;
    }
    
    /**  
     *@return  the name
     */
    public String getName()
    {
        return name;
    }
    
    /** 
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**  
     *@return  the cardId
     */
    public String getCardId()
    {
        return cardId;
    }
    
    /** 
     * @param cardId the cardId to set
     */
    public void setCardId(String cardId)
    {
        this.cardId = cardId;
    }
    
    /**  
     *@return  the nickname
     */
    public String getNickname()
    {
        return nickname;
    }
    
    /** 
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
    
    /**  
     *@return  the number
     */
    public String getNumber()
    {
        return number;
    }
    
    /** 
     * @param number the number to set
     */
    public void setNumber(String number)
    {
        this.number = number;
    }
    
    /**  
     *@return  the status
     */
    public int getStatus()
    {
        return status;
    }
    
    /** 
     * @param status the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    /**  
     *@return  the forceTime
     */
    public String getForceTime()
    {
        return forceTime;
    }
    
    /** 
     * @param forceTime the forceTime to set
     */
    public void setForceTime(String forceTime)
    {
        this.forceTime = forceTime;
    }
}
