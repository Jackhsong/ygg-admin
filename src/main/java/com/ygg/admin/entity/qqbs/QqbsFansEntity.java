
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
  * @version $Id: QqbsFansEntity.java 10347 2016-04-16 07:27:17Z zhanglide $   
  * @since 2.0
  */
public class QqbsFansEntity extends BaseEntity
{
    
     /**    */
    private static final long serialVersionUID = 5262948633672025692L;

    /**帐号ID    */
    private int accountId;
      
     /**粉丝账号Id*/
    private int fansAccountId;
      
     /**粉丝等级:1 一级粉丝 2 二级粉丝 3 三级粉丝   */
    private int level;
     /**粉丝用户头像*/
    private String fansImage;
     /** 粉丝昵称 */
    private String fansNickname;
    
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
     *@return  the fansAccountId
     */
    public int getFansAccountId()
    {
        return fansAccountId;
    }
    
    /** 
     * @param fansAccountId the fansAccountId to set
     */
    public void setFansAccountId(int fansAccountId)
    {
        this.fansAccountId = fansAccountId;
    }
    
    /**  
     *@return  the level
     */
    public int getLevel()
    {
        return level;
    }
    
    /** 
     * @param level the level to set
     */
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    /**  
     *@return  the fansImage
     */
    public String getFansImage()
    {
        return fansImage;
    }
    
    /** 
     * @param fansImage the fansImage to set
     */
    public void setFansImage(String fansImage)
    {
        this.fansImage = fansImage;
    }
    
    /**  
     *@return  the fansNickname
     */
    public String getFansNickname()
    {
        return fansNickname;
    }
    
    /** 
     * @param fansNickname the fansNickname to set
     */
    public void setFansNickname(String fansNickname)
    {
        this.fansNickname = fansNickname;
    }
}
