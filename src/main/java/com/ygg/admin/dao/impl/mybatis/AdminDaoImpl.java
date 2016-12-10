package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.AdminDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.User;

@Repository("adminDao")
public class AdminDaoImpl extends BaseDaoImpl implements AdminDao
{
    
    @Override
    public int insertPermission(String permission, String description)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("permission", permission);
        para.put("description", description);
        return getSqlSessionAdmin().insert("AdminMapper.insertPermission", para);
    }
    
    @Override
    public int insertPermission(List list)
        throws Exception
    {
        return getSqlSessionAdmin().insert("AdminMapper.batchInsertPermission", list);
    }
    
    @Override
    public List<Map<String, Object>> findPermissionByPermission(String permission)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("permission", permission);
        return findPermissionByPara(para);
    }
    
    @Override
    public List<Map<String, Object>> findPermissionByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("AdminMapper.findPermissionByPara", para);
    }
    
    @Override
    public int insertRole(String role, String description)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("role", role);
        para.put("description", description);
        para.put("id", 0);
        if (getSqlSessionAdmin().insert("AdminMapper.insertRole", para) == 1)
        {
            return Integer.valueOf(para.get("id") + "");
        }
        return 0;
    }
    
    @Override
    public int updateRole(int id, String role, String description)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("role", role);
        para.put("description", description);
        para.put("id", id);
        return getSqlSessionAdmin().update("AdminMapper.updateRole", para);
    }
    
    @Override
    public int insertRelationRolePermission(List list)
        throws Exception
    {
        return getSqlSessionAdmin().insert("AdminMapper.insertRelationRolePermission", list);
    }
    
    @Override
    public int deleteRelationRolePermissionByRoleId(int roleId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("AdminMapper.deleteRelationRolePermissionByRoleId", roleId);
    }
    
    @Override
    public int insertRelationUserPermission(List list)
        throws Exception
    {
        return getSqlSessionAdmin().insert("AdminMapper.insertRelationUserPermission", list);
    }
    
    @Override
    public int deleteRelationUserPermissionByUserId(int userId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("AdminMapper.deleteRelationUserPermissionByUserId", userId);
    }
    
    @Override
    public List<Integer> findPermissionIdByRoleId(int roleId)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("AdminMapper.findPermissionIdByRoleId", roleId);
    }
    
    @Override
    public List<Map<String, Object>> findRoleByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("AdminMapper.findRoleByPara", para);
    }
    
    @Override
    public Map<String, Object> findRoleById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        List<Map<String, Object>> list = findRoleByPara(para);
        if (list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }
    
    @Override
    public List<User> findUserByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("AdminMapper.findUserByPara", para);
    }
    
    @Override
    public int countUserByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("AdminMapper.countUserByPara", para);
    }
    
    @Override
    public Set<String> findRolesByUsername(String username)
        throws Exception
    {
        List<String> reList = getSqlSessionAdmin().selectList("AdminMapper.findRolesByUsername", username);
        Set<String> setResult = new HashSet<>();
        for (String role : reList)
        {
            setResult.add(role);
        }
        return setResult;
    }
    
    @Override
    public List<Integer> findAllRoleIdByUserId(int userId)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("AdminMapper.findAllRoleIdByUserId", userId);
    }
    
    @Override
    public User createUser(User user)
        throws Exception
    {
        if (getSqlSessionAdmin().insert("AdminMapper.createUser", user) == 1)
        {
            return user;
        }
        return null;
    }
    
    @Override
    public int updateUser(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().update("AdminMapper.updateUser", para);
    }
    
    @Override
    public int insertUserRole(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("AdminMapper.insertUserRole", para);
    }
    
    @Override
    public int deleteAllRoleByUserId(int userId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("AdminMapper.deleteAllRoleByUserId", userId);
    }
    
    @Override
    public Set<String> findPermissions(String username)
        throws Exception
    {
        List<String> reList = getSqlSessionAdmin().selectList("AdminMapper.findPermissionByUsername", username);
        Set<String> setResult = new HashSet<>();
        for (String p : reList)
        {
            setResult.add(p);
        }
        return setResult;
    }
    
    @Override
    public List<Integer> findUserIdByRoleId(int roleId)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("AdminMapper.findUserIdByRoleId", roleId);
    }
    
    @Override
    public int updatePermissionDesc(List<Map<String, String>> permissions)
        throws Exception
    {
        return getSqlSessionAdmin().update("AdminMapper.updatePermissionDesc", permissions);
    }
    
    @Override
    public int addPermission(Map<String, Object> permission)
        throws Exception
    {
        return getSqlSessionAdmin().insert("AdminMapper.insertPermission", permission);
    }
}
