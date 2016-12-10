package com.ygg.admin.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.ygg.admin.service.ShiroService;
import com.ygg.admin.util.SpringBeanUtil;

@Service("shiroService")
public class ShiroServiceImpl implements ShiroService
{
    // private static String source = "classpath:shiro.ini";
    
    @Override
    public Subject login(String name, String pwd)
    {
        // 1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        // Factory<org.apache.shiro.mgt.SecurityManager> factory = new
        // IniSecurityManagerFactory(source);
        
        // 2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = (SecurityManager)SpringBeanUtil.getBean("securityManager");
        SecurityUtils.setSecurityManager(securityManager);
        
        // 3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd.toCharArray());
        // 记住我
        token.setRememberMe(true);
        // 4、登录，即身份验证
        try
        {
            subject.login(token);
            return subject;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
}
