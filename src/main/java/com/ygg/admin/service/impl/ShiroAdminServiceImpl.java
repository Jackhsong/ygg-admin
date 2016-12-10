package com.ygg.admin.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.dao.AdminDao;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.AdminService;
import com.ygg.admin.util.CacheConstant;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.PermissionUtil;

/**
 * 基于Shiro实现的权限管理
 */
@Service("shiroAdminService")
public class ShiroAdminServiceImpl implements AdminService
{
    private Logger log = Logger.getLogger(ShiroAdminServiceImpl.class);
    
    @Resource
    private AdminDao adminDao;
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    @Override
    public int insertPermission(List<Map<String, String>> permissions)
        throws Exception
    {
        return adminDao.insertPermission(permissions);
    }
    
    @Override
    public List<Map<String, Object>> findAllPermission(Map<String, Object> para, int roleId)
        throws Exception
    {
        List<Integer> permissionIds = new ArrayList<>();
        if (roleId > 0)
        {
            permissionIds = adminDao.findPermissionIdByRoleId(roleId);
        }
        
        List<Map<String, Object>> permissionList = adminDao.findPermissionByPara(para);
        
        // 按category分组
        SortedMap<String, List<Map<String, Object>>> permissionMapByGroup = new TreeMap<>();
        for (Map<String, Object> it : permissionList)
        {
            String description = it.get("category") + "";
            List<Map<String, Object>> cuList = permissionMapByGroup.get(description);
            if (cuList == null)
            {
                cuList = new ArrayList<>();
                permissionMapByGroup.put(description, cuList);
            }
            cuList.add(it);
        }
        
        List<Map<String, Object>> resultRows = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> e : permissionMapByGroup.entrySet())
        {
            String category = e.getKey();
            List<Map<String, Object>> value = e.getValue();
            Map<String, Object> row = new HashMap<>();
            row.put("category", category);
            List<Map<String, Object>> ps = new ArrayList<>();
            for (Map<String, Object> it : value)
            {
                Map<String, Object> itR = new HashMap<>();
                itR.put("description", (it.get("description") + ""));
                itR.put("permission", (it.get("permission") + ""));
                itR.put("id", it.get("id") + "");
                if (permissionIds.contains(Integer.valueOf(it.get("id") + "")))
                {
                    itR.put("selected", 1);
                }
                else
                {
                    itR.put("selected", 0);
                }
                ps.add(itR);
            }
            row.put("pes", ps);
            resultRows.add(row);
        }
        return resultRows;
    }
    
    @Override
    public Map<String, Object> insertRole(int id, String role, String desc, List<Integer> permissionIdList)
        throws Exception
    {
        int status = 0;
        
        if (id > 0)
        {
            // 修改
            adminDao.deleteRelationRolePermissionByRoleId(id);
            adminDao.updateRole(id, role, desc);
            // 插入角色与权限关系
            List<Map<String, Integer>> list = new ArrayList<>();
            for (Integer it : permissionIdList)
            {
                Map<String, Integer> cm = new HashMap<>();
                cm.put("realmPermissionId", it);
                cm.put("realmRoleId", id);
                list.add(cm);
            }
            status = adminDao.insertRelationRolePermission(list);
        }
        else
        {
            // 新增
            int returnId = adminDao.insertRole(role, desc);
            if (returnId > 0)
            {
                // 插入角色与权限关系
                List<Map<String, Integer>> list = new ArrayList<>();
                for (Integer it : permissionIdList)
                {
                    Map<String, Integer> cm = new HashMap<>();
                    cm.put("realmPermissionId", it);
                    cm.put("realmRoleId", returnId);
                    list.add(cm);
                }
                status = adminDao.insertRelationRolePermission(list);
            }
        }
        if (status > 0)
        {
            // 刷新用户权限缓存版本
            String cacheVersion =
                cache.get(CacheConstant.ADMIN_PLATFORM_USER_PERMISSION_VSERSION) == null ? "0" : cache.get(CacheConstant.ADMIN_PLATFORM_USER_PERMISSION_VSERSION) + "";
            int v = Integer.valueOf(cacheVersion);
            cache.set(CacheConstant.ADMIN_PLATFORM_USER_PERMISSION_VSERSION, (v + 1) + "", 12 * 60 * 60);
            log.info("成功刷新用户权限缓存版本-" + (v + 1));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status", status > 0 ? 1 : 0);
        result.put("msg", status > 0 ? "保存成功！" : "保存失败！");
        return result;
    }
    
    @Override
    public Map<String, Object> findAllRole(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> roleList = adminDao.findRoleByPara(para);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", roleList);
        result.put("total", roleList.size());
        return result;
    }
    
    @Override
    public Map<String, Object> findRoleById(int id)
        throws Exception
    {
        return adminDao.findRoleById(id);
    }
    
    @Override
    public Map<String, Object> findUser(Map<String, Object> para)
        throws Exception
    {
        List<User> users = adminDao.findUserByPara(para);
        List<Map<String, Object>> userList = new ArrayList<>();
        int total = 0;
        if (users.size() > 0)
        {
            Map<String, Object> cm = null;
            List<Integer> clist = null;
            for (User user : users)
            {
                cm = new HashMap<>();
                cm.put("id", user.getId());
                cm.put("username", user.getUsername());
                cm.put("realname", user.getRealname());
                cm.put("mobileNumber", user.getMobileNumber());
                cm.put("locked", user.getLocked());
                cm.put("lockStatus", user.getLocked() == 1 ? "已锁定" : "-");
                // 查询权限信息
                Set<String> roles = adminDao.findRolesByUsername(user.getUsername());
                StringBuilder sb = new StringBuilder();
                for (String string : roles)
                {
                    sb.append(string + ";");
                }
                cm.put("roles", sb.toString());
                
                clist = adminDao.findAllRoleIdByUserId(user.getId());
                cm.put("rIds", clist);
                userList.add(cm);
            }
            total = adminDao.countUserByPara(para);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("rows", userList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map<String, Object> updateUser(int id, String username, String realname, String mobileNumber, List<Integer> roleIdList)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> userPara = new HashMap<>();
        userPara.put("username", username);
        userPara.put("realname", realname);
        userPara.put("mobileNumber", mobileNumber);
        userPara.put("id", id);
        int status = adminDao.updateUser(userPara);
        if (status != 1)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return map;
        }
        // 加入用户角色
        adminDao.deleteAllRoleByUserId(id);
        Map<String, Object> rolePermissionPara = new HashMap<>();
        for (Integer rId : roleIdList)
        {
            rolePermissionPara.put("roleId", rId);
            rolePermissionPara.put("userId", id);
            adminDao.insertUserRole(rolePermissionPara);
        }
        // 加入用户权限
        //        adminDao.deleteRelationUserPermissionByUserId(id);
        //        Set<Integer> permissionIdList = new HashSet<>();
        //        for (Integer rId : roleIdList)
        //        {
        //            permissionIdList.addAll(adminDao.findPermissionIdByRoleId(rId));
        //        }
        //        List<Map<String, Object>> insetList = new ArrayList<>();
        //        for (Integer pId : permissionIdList)
        //        {
        //            Map<String,Object> cuInsertMap = new HashMap<>();
        //            cuInsertMap.put("realmPermissionId", pId);
        //            cuInsertMap.put("userId", id);
        //            insetList.add(cuInsertMap);
        //        }
        //        if (insetList.size() > 0)
        //        {
        //            adminDao.insertRelationUserPermission(insetList);
        //        }
        result.put("status", 1);
        return result;
    }
    
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
        user = adminDao.createUser(user);
        if (user == null)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return map;
        }
        // 加入角色
        Map<String, Object> rolePermissionPara = new HashMap<>();
        for (Integer rId : roleIdList)
        {
            rolePermissionPara.put("roleId", rId);
            rolePermissionPara.put("userId", user.getId());
            adminDao.insertUserRole(rolePermissionPara);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status", 1);
        return result;
    }
    
    @Override
    public Set<String> findRoles(String username)
        throws Exception
    {
        return adminDao.findRolesByUsername(username);
    }
    
    @Override
    public Set<String> findPermissions(String username)
        throws Exception
    {
        //        try
        //        {
        //            String cacheVersion = cache.get(CacheConstant.ADMIN_PLATFORM_USER_PERMISSION_VSERSION) == null ? "0" : cache.get(CacheConstant.ADMIN_PLATFORM_USER_PERMISSION_VSERSION) + "";
        //            Set<String> result = null;
        //            String key = CacheConstant.ADMIN_PLATFORM_USER_PERMISSION + cacheVersion + "_" + username;
        //            Object pers = cache.get(key);
        //            if (pers == null)
        //            {
        //                result = adminDao.findPermissions(username);
        //                cache.set(key, result, 6 * 60 *60);
        //            }
        //            else
        //            {
        //                result = (Set<String>)pers;
        //                log.info("从缓存获取权限：" + result);
        //            }
        //            return result;
        //        }
        //        catch (Exception e)
        //        {
        //            log.error("获取权限失败！", e);
        return adminDao.findPermissions(username);
        //        }
    }
    
    @Override
    public String refreshPermission(String packageName)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("刷新权限 - 开始");
        Set<Class<?>> controllers = PermissionUtil.getClasses("com.ygg.admin.controller");
        for (Class<?> clazz : controllers)
        {
            String controllerName = clazz.getSimpleName();
            if (PermissionUtil.isControllerMappingContains(controllerName))
            {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods)
                {
                    if (method.isAnnotationPresent(RequestMapping.class))
                    {
                        RequestMapping rm = method.getAnnotation(RequestMapping.class);
                        String[] values = rm.value();
                        String mapping = Arrays.toString(values).replaceAll("\\[", "").replaceAll("\\]", "").replaceFirst("/", "");
                        Map<String, Object> permission = new HashMap<>();
                        permission.put("category", controllerName);
                        permission.put("permission", controllerName + "_" + mapping.split("/")[0]);
                        permission.put("description", mapping.split("/")[0]);
                        try
                        {
                            if (adminDao.addPermission(permission) > 0)
                            {
                                log.info(String.format("新增权限%s", permission.toString()));
                            }
                        }
                        catch (Exception e)
                        {
                            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_permission"))
                            {
                                log.debug("权限已经存在：" + permission.toString());
                            }
                            else
                            {
                                throw new ServletException(e);
                            }
                        }
                    }
                }
            }
            else
            {
                log.info(controllerName + "不需要权限，跳过。。。");
            }
        }
        log.info("刷新权限 - 结束");
        resultMap.put("status", 1);
        resultMap.put("msg", "刷新权限成功");
        return JSON.toJSONString(resultMap);
    }
}
