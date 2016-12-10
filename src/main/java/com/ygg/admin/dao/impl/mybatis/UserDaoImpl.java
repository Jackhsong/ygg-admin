package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.UserDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.Role;
import com.ygg.admin.entity.User;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl implements UserDao
{
    
    @Override
    public User createUser(User user)
        throws Exception
    {
        int status = getSqlSessionAdmin().insert("UserMapper.createUser", user);
        if (status != 1)
        {
            return null;
        }
        return user;
    }
    
    @Override
    public int updateUser(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().update("UserMapper.updateUser", para);
    }
    
    @Override
    public int insertRole(Role para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("UserMapper.insertRole", para);
    }
    
    @Override
    public int deleteAllRoleByUserId(int userId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("UserMapper.deleteAllRoleByUserId", userId);
    }
    
    @Override
    public int updateRole(Role para)
        throws Exception
    {
        return getSqlSessionAdmin().update("UserMapper.updateRole", para);
    }
    
    @Override
    public int deleteRole(int id)
        throws Exception
    {
        return getSqlSessionAdmin().delete("UserMapper.deleteRole", id);
    }
    
    @Override
    public int insertPermission(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("UserMapper.insertPermission", para);
    }
    
    @Override
    public int deletePermission(int id)
        throws Exception
    {
        return getSqlSessionAdmin().delete("UserMapper.deletePermission", id);
    }
    
    @Override
    public int insertUserRole(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("UserMapper.insertUserRole", para);
    }
    
    @Override
    public List<Integer> findAllRoleIdByUserId(int userId)
        throws Exception
    {
        List<Integer> reList = getSqlSessionAdmin().selectList("UserMapper.findAllRoleIdByUserId", userId);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int deleteUserRole(int id)
        throws Exception
    {
        return getSqlSessionAdmin().delete("UserMapper.deleteUserRole", id);
    }
    
    @Override
    public int insertRolePermission(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("UserMapper.insertRolePermission", para);
    }
    
    @Override
    public List<Integer> findAllPermissionIdByRole(int roleId)
        throws Exception
    {
        List<Integer> reList = getSqlSessionAdmin().selectList("UserMapper.findAllPermissionIdByRole", roleId);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int deleteAllPermissionByRole(int roleId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("UserMapper.deleteAllPermissionByRole", roleId);
    }
    
    @Override
    public int deleteRolePermission(int id)
        throws Exception
    {
        return getSqlSessionAdmin().delete("UserMapper.deleteRolePermission", id);
    }
    
    @Override
    public Set<String> findRoles(String username)
        throws Exception
    {
        List<String> reList = getSqlSessionAdmin().selectList("UserMapper.findRolesByUsername", username);
        reList = reList == null ? new ArrayList<String>() : reList;
        Set<String> setResult = new HashSet<String>();
        for (String role : reList)
        {
            setResult.add(role);
        }
        return setResult;
    }
    
    @Override
    public List<Map<String, Object>> findRolesForShow(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("UserMapper.findRolesForShow", para);
        reList = reList == null ? new ArrayList<Map<String, Object>>() : reList;
        return reList;
    }
    
    @Override
    public int countRolesForShow()
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("UserMapper.countRolesForShow");
    }
    
    @Override
    public Set<String> findPermissions(String username)
        throws Exception
    {
        List<String> reList = getSqlSessionAdmin().selectList("UserMapper.findPermissionByUsername", username);
        Set<String> setResult = new HashSet<String>();
        for (String permission : reList)
        {
            setResult.add(permission);
        }
        return setResult;
    }
    
    @Override
    public User findUserByUsername(String username)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("start", 0);
        map.put("max", 1);
        List<User> users = findUserByPara(map);
        if (users.size() > 0)
        {
            return users.get(0);
        }
        return null;
    }
    
    @Override
    public List<User> findUserByPara(Map<String, Object> para)
        throws Exception
    {
        List<User> reList = getSqlSessionAdmin().selectList("UserMapper.findUserByPara", para);
        return reList == null ? new ArrayList<User>() : reList;
    }
    
    @Override
    public User findUserById(int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        List<User> reList = findUserByPara(map);
        if (reList.size() < 1)
        {
            return null;
        }
        return reList.get(0);
    }
    
    @Override
    public int countUserByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("UserMapper.countUserByPara", para);
    }
    
    @Override
    public List<Map<String, Object>> findPermissionForAdd()
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("UserMapper.findPermissionForAdd");
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Integer> findPermissionByRoleId(int id)
        throws Exception
    {
        List<Integer> reList = getSqlSessionAdmin().selectList("UserMapper.findPermissionByRoleId", id);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int batchUpdateLockStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().update("UserMapper.batchUpdateLockStatus", para);
    }
    
    @Override
    public List<Map<String, Object>> findUserByIds(List<Object> ids)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("UserMapper.findUserByIds", ids);
    }
    
    @Override
    public List<User> findUserByRole(String role)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("UserMapper.findUserByRole", role);
    }
    
    @Override
    public List<Map<String, Object>> findAllUserCode()
        throws Exception
    {
        return getSqlSessionAdmin().selectList("UserMapper.findAllUserCode");
    }
}
