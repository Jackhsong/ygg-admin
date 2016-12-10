package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.Role;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.CommonUtil;

@Service("userService")
public class UserServiceImpl implements UserService
{
    @Resource
    private UserDao userDao;
    
    @Override
    public Map<String, Object> createUser(String username, String realname, String mobileNumber, String password, List<Integer> roleIdList)
        throws Exception
    {
        // 新增user
        User user = new User();
        password = CommonUtil.strToMD5(password + username);
        user.setPassword(password);
        user.setUsername(username);
        user.setRealname(realname);
        user.setMobileNumber(mobileNumber);
        user.setLocked((byte)0);
        user = userDao.createUser(user);
        if (user == null)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return map;
        }
        // 加入角色
        Map<String, Object> rolePermissionPara = new HashMap<String, Object>();
        for (Integer rId : roleIdList)
        {
            rolePermissionPara.put("roleId", rId);
            rolePermissionPara.put("userId", user.getId());
            userDao.insertUserRole(rolePermissionPara);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", 1);
        return result;
    }
    
    @Override
    public Map<String, Object> updateUser(int id, String username, String realname, String mobileNumber, List<Integer> roleIdList)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> userPara = new HashMap<String, Object>();
        userPara.put("username", username);
        userPara.put("realname", realname);
        userPara.put("mobileNumber", mobileNumber);
        userPara.put("id", id);
        int status = userDao.updateUser(userPara);
        if (status != 1)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return map;
        }
        // 加入角色
        userDao.deleteAllRoleByUserId(id);
        Map<String, Object> rolePermissionPara = new HashMap<String, Object>();
        for (Integer rId : roleIdList)
        {
            rolePermissionPara.put("roleId", rId);
            rolePermissionPara.put("userId", id);
            userDao.insertUserRole(rolePermissionPara);
        }
        result.put("status", 1);
        return result;
    }
    
    @Override
    public Map<String, Object> changePassword(int userId, String newPassword)
        throws Exception
    {
        User user = userDao.findUserById(userId);
        if (user == null)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("status", "用户不存在");
            return map;
        }
        newPassword = CommonUtil.strToMD5(newPassword + user.getUsername());
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", userId);
        para.put("password", newPassword);
        int status = userDao.updateUser(para);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        return map;
    }
    
    @Override
    public Map<String, Object> createOrUpdateRole(int id, String role, List<Integer> permissionIdList, String desc)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String msg = "";
        Role rolePojo = new Role();
        rolePojo.setDescription(desc);
        rolePojo.setRole(role);
        int roleStatus = 0;
        if (id == 0)// 1 插入role
        {
            // 查询是否存在相同的role
            Map<String, Object> rolePara = new HashMap<String, Object>();
            rolePara.put("role", role);
            List<Map<String, Object>> reList = userDao.findRolesForShow(rolePara);
            if (reList.size() > 0)
            {
                roleStatus = -1;
                msg = "已经存在该角色";
            }
            else
            {
                roleStatus = userDao.insertRole(rolePojo);
            }
        }
        else
        {
            rolePojo.setId(id);
            roleStatus = userDao.updateRole(rolePojo);
        }
        
        if (roleStatus == 1)
        {
            // 插入 role_permission
            if (id != 0)
            {
                userDao.deleteAllPermissionByRole(id);
            }
            Map<String, Object> rolePermissionPara = new HashMap<String, Object>();
            for (Integer pId : permissionIdList)
            {
                rolePermissionPara.put("roleId", rolePojo.getId());
                rolePermissionPara.put("permissionId", pId);
                userDao.insertRolePermission(rolePermissionPara);
            }
        }
        if (roleStatus == 0)
        {
            msg = "保存失败";
        }
        result.put("status", roleStatus);
        result.put("msg", msg);
        return result;
    }
    
    @Override
    public int insertPermission(String permission, String description)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("permission", permission);
        para.put("description", description);
        return userDao.insertPermission(para);
    }
    
    @Override
    public void correlationRoles(Long userId, Long... roleIds)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public User findByUsername(String username)
        throws Exception
    {
        return userDao.findUserByUsername(username);
    }
    
    @Override
    public Map<String, Object> listUser(Map<String, Object> para)
        throws Exception
    {
        List<User> users = userDao.findUserByPara(para);
        List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (users.size() > 0)
        {
            Map<String, Object> cm = null;
            List<Integer> clist = null;
            for (User user : users)
            {
                cm = new HashMap<String, Object>();
                cm.put("id", user.getId());
                cm.put("username", user.getUsername());
                cm.put("realname", user.getRealname());
                cm.put("mobileNumber", user.getMobileNumber());
                cm.put("locked", user.getLocked());
                cm.put("lockStatus", user.getLocked() == 1 ? "已锁定" : "-");
                // 查询权限信息
                Set<String> roles = userDao.findRoles(user.getUsername());
                StringBuilder sb = new StringBuilder();
                for (String string : roles)
                {
                    sb.append(string + ";");
                }
                cm.put("roles", sb.toString());
                
                clist = userDao.findAllRoleIdByUserId(user.getId());
                cm.put("rIds", clist);
                userList.add(cm);
            }
            total = userDao.countUserByPara(para);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", userList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Set<String> findRoles(String username)
        throws Exception
    {
        return userDao.findRoles(username);
    }
    
    @Override
    public Map<String, Object> findRolesForShow()
        throws Exception
    {
        List<Map<String, Object>> reList = userDao.findRolesForShow(new HashMap<String, Object>());
        int total = 0;
        if (reList.size() > 0)
        {
            total = userDao.countRolesForShow();
            List<Integer> clist = null;
            for (Map<String, Object> cMap : reList)
            {
                int roleId = Integer.valueOf(cMap.get("id") + "").intValue();
                clist = userDao.findPermissionByRoleId(roleId);
                cMap.put("pIds", clist);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", reList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Set<String> findPermissions(String username)
        throws Exception
    {
        return userDao.findPermissions(username);
    }
    
    @Override
    public List<Map<String, Object>> findPermissionForAdd()
        throws Exception
    {
        return userDao.findPermissionForAdd();
    }
    
    @Override
    public int updateUserLocked(int id, int lockedStatus)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("locked", lockedStatus);
        return userDao.updateUser(para);
    }
    
    @Override
    public int updateUser(User user)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("smsCode", user.getSmsCode());
        para.put("id", user.getId());
        return userDao.updateUser(para);
    }
    
    @Override
    public int batchUpdateLockStatus(Map<String, Object> para)
        throws Exception
    {
        userDao.batchUpdateLockStatus(para);
        return 1;
    }
    
    @Override
    public List<User> findAllUser()
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("locked", 0);
        return userDao.findUserByPara(para);
    }
    
    @Override
    public List<Map<String, Object>> findUserByIds(List<Object> ids)
        throws Exception
    {
        return userDao.findUserByIds(ids);
    }
    
    @Override
    public List<User> findUserByRole(String role)
        throws Exception
    {
        return userDao.findUserByRole(role);
    }
    
    @Override
    public List<Map<String, Object>> findAllUserCode()
        throws Exception
    {
        return userDao.findAllUserCode();
    }
    
    @Override
    public User findUserById(int id)
        throws Exception
    {
        return userDao.findUserById(id);
    }
}
