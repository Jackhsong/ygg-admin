package com.ygg.admin.entity;

import java.io.Serializable;

import org.elasticsearch.common.joda.time.DateTime;

public class BaseEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    protected int id;
    
    protected int createUser;
    
    protected String createTime;
    
    protected int updateUser;
    
    protected String updateTime;
    
    protected BaseEntity()
    {
        id = 0;
        createUser = 0;
        createTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        updateUser = 0;
        updateTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(int createUser)
    {
        this.createUser = createUser;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public int getUpdateUser()
    {
        return updateUser;
    }
    
    public void setUpdateUser(int updateUser)
    {
        this.updateUser = updateUser;
    }
    
    public String getUpdateTime()
    {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }
    
}
