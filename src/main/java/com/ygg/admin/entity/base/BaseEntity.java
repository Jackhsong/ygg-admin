/**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.entity.base;

import java.io.Serializable;

/**
 * 抽象基础Entity
 * 
 * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
 * @version $Id: BaseEntity.java 12176 2016-05-18 02:54:05Z wuhuyun $
 * @since 2.0
 */
public abstract class BaseEntity extends AbstractPageableEntity implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5783992467338133574L;
    
    /** id */
    protected int id;
    
    /** 创建时间 */
    protected String createTime;
    
    /** 更新时间 */
    protected String updateTime;
    
    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * @return the createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }
    
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    /**
     * @return the updateTime
     */
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
}
