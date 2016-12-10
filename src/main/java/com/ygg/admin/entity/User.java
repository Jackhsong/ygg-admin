package com.ygg.admin.entity;

import java.io.Serializable;

/**
 * 管理员
 */
public class User implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private String username;// 用户名
    
    private String realname;// 真实姓名
    
    private String password;// 密码
    
    private String mobileNumber;//手机号
    
    private String smsCode;//短信验证码
    
    private byte locked;// 锁定状态；0：未锁定；1：一锁定
    
    public User()
    {
    }
    
    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    public Integer getId()
    {
        return id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getRealname()
    {
        return realname;
    }
    
    public void setRealname(String realname)
    {
        this.realname = realname;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public byte getLocked()
    {
        return locked;
    }
    
    public void setLocked(byte locked)
    {
        this.locked = locked;
    }
    
    public String getMobileNumber()
    {
        return mobileNumber;
    }
    
    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }
    
    public String getSmsCode()
    {
        return smsCode;
    }
    
    public void setSmsCode(String smsCode)
    {
        this.smsCode = smsCode;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        User user = (User)o;
        
        if (id != null ? !id.equals(user.id) : user.id != null)
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
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password + '\'' + ", locked=" + locked + '}';
    }
}
