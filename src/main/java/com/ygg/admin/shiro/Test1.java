package com.ygg.admin.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class Test1
{
    @Test
    public void testIsPermitted()
    {
        Subject s = login("classpath:shiro-realm.ini", "lh", "654321");
        System.out.println(s);
    }
    
    public Subject login(String source, String name, String pwd)
    {
        // 1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(source);
        
        // 2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        
        // 3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd.toCharArray());
        // 4、登录，即身份验证
        try
        {
            subject.login(token);
            return subject;
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
}
