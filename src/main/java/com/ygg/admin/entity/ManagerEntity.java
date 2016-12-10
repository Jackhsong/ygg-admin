package com.ygg.admin.entity;

/**
 * 后台管理员
 * 
 * @author zhangyb
 *
 */
public class ManagerEntity
{
    
    /** ID */
    private int id;
    
    /** 名称 */
    private String name;
    
    /** 密码 MD5(name+pwd) */
    private String pwd;
    
    /** 是否可用 */
    private int enabled;
    
    /** 创建时间 */
    private String createTime;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPwd()
    {
        return pwd;
    }
    
    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }
    
    public int getEnabled()
    {
        return enabled;
    }
    
    public void setEnabled(int enabled)
    {
        this.enabled = enabled;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    @Override
    public String toString()
    {
        return id + ":" + name;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof ManagerEntity)
        {
            ManagerEntity other = (ManagerEntity)obj;
            return this.id == other.id;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return id;
    }
}
