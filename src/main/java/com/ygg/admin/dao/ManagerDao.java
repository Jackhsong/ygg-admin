package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.ManagerEntity;

public interface ManagerDao
{
    
    /**
     * 插入manager
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    int insertManager(ManagerEntity entity)
        throws Exception;
    
    /**
     * 根据ID查询ManagerEntity信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    ManagerEntity findManagerById(int id)
        throws Exception;
    
    /**
     * 根据username查询ManagerEntity信息
     * 
     * @param username
     * @return
     * @throws Exception
     */
    ManagerEntity findManagerByName(String username)
        throws Exception;
    
    /**
     * 修改用户密码
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updatePWDByID(String pwd, int id)
        throws Exception;
    
    List<ManagerEntity> findAllManagerByPara(Map<String, Object> para)
        throws Exception;
    
    int countManagerByPara(Map<String, Object> para)
        throws Exception;
}
