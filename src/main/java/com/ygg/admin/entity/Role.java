package com.ygg.admin.entity;

import java.io.Serializable;

/**
 * 角色
 */
public class Role implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private String role; // 角色标识 程序中判断使用,如"admin"
    
    private String description; // 角色描述,UI界面显示使用
    
    private byte isAvailable;// 是否可用,如果不可用将不会添加给用户
    
    public Role()
    {
    }
    
    public Role(String role, String description, byte isAvailable)
    {
        this.role = role;
        this.description = description;
        this.isAvailable = isAvailable;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getRole()
    {
        return role;
    }
    
    public void setRole(String role)
    {
        this.role = role;
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
        
        Role role = (Role)o;
        
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
        return "Role{" + "id=" + id + ", role='" + role + '\'' + ", description='" + description + '\'' + ", isAvailable=" + isAvailable + '}';
    }
    
}
