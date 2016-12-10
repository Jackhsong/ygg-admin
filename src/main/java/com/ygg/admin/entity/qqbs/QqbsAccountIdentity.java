
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
  * @version $Id: QqbsAccountIdentity.java 11707 2016-05-12 11:56:13Z zhanglide $   
  * @since 2.0
  */
public class QqbsAccountIdentity extends BaseEntity
{
   
     /**    */
    private static final long serialVersionUID = -8130906106931794418L;
    
     /**    */
    private int accountId;
    /**经理：0否，1是*/
   private int manager;
    /**成为经理时间*/
   private String managerTime;
    /**总监：0否，1是*/
   private int director;
    /**成为总监时间 */
   private String directorTime;
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
     *@return  the manager
     */
    public int getManager()
    {
        return manager;
    }
    
    /** 
     * @param manager the manager to set
     */
    public void setManager(int manager)
    {
        this.manager = manager;
    }
    
    /**  
     *@return  the managerTime
     */
    public String getManagerTime()
    {
        return managerTime;
    }
    
    /** 
     * @param managerTime the managerTime to set
     */
    public void setManagerTime(String managerTime)
    {
        this.managerTime = managerTime;
    }
    
    /**  
     *@return  the director
     */
    public int getDirector()
    {
        return director;
    }
    
    /** 
     * @param director the director to set
     */
    public void setDirector(int director)
    {
        this.director = director;
    }
    
    /**  
     *@return  the directorTime
     */
    public String getDirectorTime()
    {
        return directorTime;
    }
    
    /** 
     * @param directorTime the directorTime to set
     */
    public void setDirectorTime(String directorTime)
    {
        this.directorTime = directorTime;
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
