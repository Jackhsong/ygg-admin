package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ygg.admin.entity.User;

/**
 * 管理员DAO
 * 
 * @author zhangyb
 *
 */
public interface AdminDao
{
    int insertPermission(String permission, String description)
        throws Exception;
    
    int insertPermission(List list)
        throws Exception;
    
    List<Map<String, Object>> findPermissionByPermission(String permission)
        throws Exception;
    
    List<Map<String, Object>> findPermissionByPara(Map<String, Object> para)
        throws Exception;
    
    int insertRole(String role, String description)
        throws Exception;
    
    int updateRole(int id, String role, String description)
        throws Exception;
    
    int insertRelationRolePermission(List list)
        throws Exception;
    
    int deleteRelationRolePermissionByRoleId(int roleId)
        throws Exception;
    
    int insertRelationUserPermission(List list)
        throws Exception;
    
    int deleteRelationUserPermissionByUserId(int userId)
        throws Exception;
    
    List<Integer> findPermissionIdByRoleId(int roleId)
        throws Exception;
    
    List<Map<String, Object>> findRoleByPara(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findRoleById(int id)
        throws Exception;
    
    List<User> findUserByPara(Map<String, Object> para)
        throws Exception;
    
    int countUserByPara(Map<String, Object> para)
        throws Exception;
    
    Set<String> findRolesByUsername(String username)
        throws Exception;
    
    List<Integer> findAllRoleIdByUserId(int userId)
        throws Exception;
    
    User createUser(User user)
        throws Exception;
    
    int updateUser(Map<String, Object> para)
        throws Exception;
    
    int insertUserRole(Map<String, Object> para)
        throws Exception;
    
    int deleteAllRoleByUserId(int userId)
        throws Exception;
    
    Set<String> findPermissions(String username)
        throws Exception;
    
    List<Integer> findUserIdByRoleId(int roleId)
        throws Exception;
    
    int updatePermissionDesc(List<Map<String, String>> permissions)
        throws Exception;
    
    int addPermission(Map<String, Object> permission)
        throws Exception;
}
