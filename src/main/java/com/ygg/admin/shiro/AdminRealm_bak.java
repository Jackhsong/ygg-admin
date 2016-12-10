package com.ygg.admin.shiro;

import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.CacheConstant;
import com.ygg.admin.util.SpringBeanUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class AdminRealm_bak extends AuthorizingRealm
{
    Logger log = Logger.getLogger(AdminRealm_bak.class);
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    private UserService userService = null;
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
    {
        try
        {
            if (userService == null)
            {
                userService = (UserService)SpringBeanUtil.getBean("userService");
            }
            // System.out.println("---------------------------doGetAuthorizationInfo---------------------");
            String username = (String)principals.getPrimaryPrincipal();
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            //            System.out.println("roles:" + userService.findRoles(username));
            authorizationInfo.setRoles(userService.findRoles(username));
            authorizationInfo.setStringPermissions(userService.findPermissions(username));
            // System.out.println("pers:" +
            // userService.findPermissions(username));
            return authorizationInfo;
        }
        catch (Exception e)
        {
            log.error("登陆获取权限失败", e);
            return null;
        }
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException
    {
        try
        {
            if (userService == null)
            {
                userService = (UserService)SpringBeanUtil.getBean("userService");
                // System.out.println(userService);
            }
            // System.out.println("---------------------------doGetAuthenticationInfo---------------------");
            String username = (String)token.getPrincipal(); // 得到用户名
            String password = new String((char[])token.getCredentials()); // 得到密码
            User user = userService.findByUsername(username);
            SimpleAuthenticationInfo currentUser = null;
            if (user == null)
            {
                return null;
            }
            if (user.getLocked() == 1)
            {
                cache.set(CacheConstant.ADMIN_ADMINREALM_LOCKED_USER + user.getUsername(), 1, 60);
                return null;
            }
            if (!password.equals(user.getPassword()))
            {
                //密码错误
                cache.set(CacheConstant.ADMIN_ADMINREALM_ERROR_PASSWORD_USER + user.getUsername(), user.getId(), 60);
                return null;
            }
            currentUser = new SimpleAuthenticationInfo(username, password, getName());
            return currentUser;
        }
        catch (Exception e)
        {
            log.error("登陆失败", e);
            return null;
        }
    }
}
