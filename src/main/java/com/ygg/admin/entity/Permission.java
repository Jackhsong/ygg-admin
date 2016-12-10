package com.ygg.admin.entity;

import java.io.Serializable;

/**
 * 权限
 */
public class Permission implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String permission; // 权限标识 程序中判断使用,如"user:create"
    
    private String description; // 权限描述,UI界面显示使用
    
    private byte isAvailable;// 是否可用,如果不可用将不会添加给用户
    
    public Permission()
    {
    }
    
    public Permission(String permission, String description, byte isAvailable)
    {
        this.permission = permission;
        this.description = description;
        this.isAvailable = isAvailable;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    public void setPermission(String permission)
    {
        this.permission = permission;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public byte getIsAvailable()
    {
        return isAvailable;
    }
    
    public void setIsAvailable(byte isAvailable)
    {
        this.isAvailable = isAvailable;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        Permission role = (Permission)o;
        
        if (id != null ? !id.equals(role.id) : role.id != null)
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString()
    {
        return "Role{" + "id=" + id + ", permission='" + permission + '\'' + ", description='" + description + '\'' + ", isAvailable=" + isAvailable + '}';
    }
}
