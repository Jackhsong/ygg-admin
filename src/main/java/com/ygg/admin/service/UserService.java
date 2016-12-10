package com.ygg.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ygg.admin.entity.User;

public interface UserService
{
    /**
     * 创建账户
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public Map<String, Object> createUser(String username, String realname, String mobileNumber, String password, List<Integer> roleIdList)
        throws Exception;
    
    public Map<String, Object> updateUser(int id, String username, String realname, String mobileNumber, List<Integer> roleIdList)
        throws Exception;
    
    public int updateUserLocked(int id, int lockedStatus)
        throws Exception;
    
    public Map<String, Object> listUser(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改密码
     * 
     * @param userId
     * @param newPassword
     * @throws Exception
     */
    public Map<String, Object> changePassword(int userId, String newPassword)
        throws Exception;
    
    /**
     * 创建 or 更新 角色
     * 
     * @param id
     * @param role
     * @param menuIdList
     * @param desc
     * @return
     * @throws Exception
     */
    public Map<String, Object> createOrUpdateRole(int id, String role, List<Integer> permissionIdList, String desc)
        throws Exception;
    
    /**
     * 插入权限
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int insertPermission(String permission, String description)
        throws Exception;
    
    /**
     * 添加用户-角色关系
     * 
     * @param userId
     * @param roleIds
     * @throws Exception
     */
    public void correlationRoles(Long userId, Long... roleIds)
        throws Exception;
    
    /**
     * 移除用户-角色关系
     * 
     * @param userId
     * @param roleIds
     * @throws Exception
     */
    public void uncorrelationRoles(Long userId, Long... roleIds)
        throws Exception;
    
    /**
     * 根据用户名查找用户
     * 
     * @param username
     * @return
     * @throws Exception
     */
    public User findByUsername(String username)
        throws Exception;
    
    /**
     * 根据用户名查找其角色
     * 
     * @param username
     * @return
     * @throws Exception
     */
    public Set<String> findRoles(String username)
        throws Exception;
    
    /**
     * 查询所有可用角色
     * 
     * @return
     * @throws Exception
     */
    public Map<String, Object> findRolesForShow()
        throws Exception;
    
    /**
     * 根据用户名查找其权限
     * 
     * @param username
     * @return
     * @throws Exception
     */
    public Set<String> findPermissions(String username)
        throws Exception;
    
    /**
     * 查找系统所有权限
     * 
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findPermissionForAdd()
        throws Exception;
    
    public int updateUser(User user)
        throws Exception;
    
    public int batchUpdateLockStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有用户
     * @return
     * @throws Exception
     */
    public List<User> findAllUser()
        throws Exception;
    
    List<Map<String, Object>> findUserByIds(List<Object> ids)
        throws Exception;
    
    /**
     * 根据角色查找用户
     * @param string
     * @return
     * @throws Exception
     */
    public List<User> findUserByRole(String role)
        throws Exception;
    
    List<Map<String, Object>> findAllUserCode()
        throws Exception;
    
    public User findUserById(int auditUserId)
        throws Exception;
}
