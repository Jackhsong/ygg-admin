package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ygg.admin.entity.Role;
import com.ygg.admin.entity.User;

/**
 * 管理员DAO
 * 
 * @author zhangyb
 *
 */
public interface UserDao
{
    /**
     * 创建新的管理员
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public User createUser(User user)
        throws Exception;
    
    /**
     * 根据username查询user
     * 
     * @param username
     * @return
     * @throws Exception
     */
    public User findUserByUsername(String username)
        throws Exception;
    
    public List<User> findUserByPara(Map<String, Object> para)
        throws Exception;
    
    public User findUserById(int id)
        throws Exception;
    
    public int countUserByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改管理员密码
     */
    public int updateUser(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入角色信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int insertRole(Role para)
        throws Exception;
    
    /**
     * 更新角色信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int updateRole(Role para)
        throws Exception;
    
    /**
     * 删除角色信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public int deleteRole(int id)
        throws Exception;
    
    /**
     * 插入权限信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int insertPermission(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除权限信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public int deletePermission(int id)
        throws Exception;
    
    /**
     * 插入用户角色关系
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int insertUserRole(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据userId删除角色
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public int deleteAllRoleByUserId(int userId)
        throws Exception;
    
    /**
     * 根据userId查询用户角色Id
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public List<Integer> findAllRoleIdByUserId(int userId)
        throws Exception;
    
    /**
     * 删除用户角色关系
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public int deleteUserRole(int id)
        throws Exception;
    
    /**
     * 插入角色权限关系
     * 
     * @param para
     * @return
     * @throws Exception
     */
    public int insertRolePermission(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据roleId删除角色权限关系
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public int deleteAllPermissionByRole(int roleId)
        throws Exception;
    
    /**
     * 根据roleId查询对应permissionId
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<Integer> findAllPermissionIdByRole(int roleId)
        throws Exception;
    
    /**
     * 删除角色权限关系
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public int deleteRolePermission(int id)
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
    public List<Map<String, Object>> findRolesForShow(Map<String, Object> para)
        throws Exception;
    
    public int countRolesForShow()
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
    
    /**
     * 根据roleId 查询所有权限
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public List<Integer> findPermissionByRoleId(int id)
        throws Exception;
    
    public int batchUpdateLockStatus(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findUserByIds(List<Object> ids)
        throws Exception;
    
    public List<User> findUserByRole(String role)
        throws Exception;
    
    List<Map<String, Object>> findAllUserCode()
        throws Exception;
}
