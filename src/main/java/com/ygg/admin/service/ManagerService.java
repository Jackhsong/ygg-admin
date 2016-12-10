package com.ygg.admin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ygg.admin.entity.ManagerEntity;

public interface ManagerService
{
    
    /** 登陆 */
    Map<String, Object> login(String name, String pwd)
        throws Exception;
    
    /** 注册 */
    int register(String name, String pwd)
        throws Exception;
    
    /** 根据ID查询Manager */
    ManagerEntity findManagerById(int id)
        throws Exception;
    
    Map findAllManagerByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改用户密码
     * 
     * @param accountId
     * @param pwd
     * @return
     * @throws Exception
     */
    int updatePWD(int id, String pwd)
        throws Exception;
    
    /**
     * 获取当前登陆的用户
     * 
     * @param request
     * @return
     * @throws Exception
     */
    ManagerEntity getCurrentUser(HttpServletRequest request)
        throws Exception;
    
}
