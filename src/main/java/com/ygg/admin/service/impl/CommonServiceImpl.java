package com.ygg.admin.service.impl;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService
{
    @Resource
    private UserDao userDao;
    
    @Override
    public int getCurrentUserId()
        throws Exception
    {
        String username = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(username);
        return user == null ? 0 : user.getId();
    }
    
    @Override
    public String getCurrentUserName()
        throws Exception
    {
        return SecurityUtils.getSubject().getPrincipal() + "";
    }
    
    @Override
    public String getCurrentRealName()
        throws Exception
    {
        String username = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(username);
        return user == null ? "" : user.getRealname();
    }
}
