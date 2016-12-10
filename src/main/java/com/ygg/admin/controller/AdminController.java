package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController
{
    private Logger log = Logger.getLogger(AdminController.class);
    
    @Resource(name = "shiroAdminService")
    private AdminService adminService;
    
    /**
    * 管理员列表
    *
    * @return
    */
    @RequestMapping("/list")
    public ModelAndView list()
        throws Exception
    {
        ModelAndView mv = new ModelAndView("admin/list");
        Map<String, Object> result = adminService.findAllRole(new HashMap<String, Object>());
        mv.addObject("roles", (List<Map<String, Object>>)result.get("rows"));
        return mv;
    }
    
    /**
     * 新增后台用户
     */
    @RequestMapping(value = "/addUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addUser(@RequestParam(value = "roles", required = true) String roles, @RequestParam(value = "name", required = true) String name,//
        @RequestParam(value = "realname", required = true) String realname,//
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,//
        @RequestParam(value = "pwd", required = true) String pwd//
    )
        throws Exception
    {
        try
        {
            List<Integer> roleIdList = new ArrayList<>();
            if (roles.indexOf(",") > 0)
            {
                String[] arr = roles.split(",");
                for (String cur : arr)
                {
                    roleIdList.add(Integer.valueOf(cur));
                }
            }
            Map<String, Object> result = adminService.createUser(name, realname, mobileNumber, pwd, roleIdList);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            log.error("新增角色失败", e);
            result.put("status", 0);
            result.put("msg", "新增失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 编辑用户
     */
    @RequestMapping(value = "/editUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String editUser(@RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "roles", required = true) String roles,//
        @RequestParam(value = "name", required = true) String name,//
        @RequestParam(value = "realname", required = true) String realname, //
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber //
    )
        throws Exception
    {
        try
        {
            List<Integer> roleIdList = new ArrayList<>();
            if (roles.indexOf(",") > 0)
            {
                String[] arr = roles.split(",");
                for (String cur : arr)
                {
                    roleIdList.add(Integer.valueOf(cur));
                }
            }
            if (roleIdList.size() == 0)
            {
                Map<String, Object> result = new HashMap<>();
                result.put("status", 0);
                result.put("msg", "请选择至少一个角色");
                return JSON.toJSONString(result);
            }
            Map<String, Object> result = adminService.updateUser(id, name, realname, mobileNumber, roleIdList);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            log.error("修改失败", e);
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /** 异步获取管理员信息 */
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String userInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "name", required = false, defaultValue = "") String name)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(name))
        {
            para.put("username", name);
        }
        Map re = adminService.findUser(para);
        return JSON.toJSONString(re);
    }
    
    /**
     * 角色列表
     *
     * @return
     */
    @RequestMapping("/roleList")
    public ModelAndView roleList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView("admin/roleList");
        List<Map<String, Object>> permissionList = adminService.findAllPermission(new HashMap<String, Object>(), 0);
        mv.addObject("permissionList", permissionList);
        return mv;
    }
    
    /**
     * 新增 or 更新 角色
     *
     * @throws Exception
     */
    @RequestMapping(value = "/updateRole", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addRole(@RequestParam(value = "role", required = true) String role, //
        @RequestParam(value = "resource", required = true) String resource, //
        @RequestParam(value = "desc", required = true) String desc,//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        try
        {
            if ("".equals(role) || "".equals(desc))
            {
                Map<String, Object> result = new HashMap<>();
                result.put("status", 0);
                result.put("msg", "请填写完整信息！");
                return JSON.toJSONString(result);
            }
            List<Integer> permissionIdList = new ArrayList<>();
            if (resource.indexOf(",") > 0)
            {
                String[] arr = resource.split(",");
                for (String cur : arr)
                {
                    permissionIdList.add(Integer.valueOf(cur));
                }
            }
            if (permissionIdList.size() == 0)
            {
                Map<String, Object> result = new HashMap<>();
                result.put("status", 0);
                result.put("msg", "请选择权限再保存！");
                return JSON.toJSONString(result);
            }
            Map<String, Object> result = adminService.insertRole(id, role, desc, permissionIdList);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            log.error("新增角色失败", e);
            result.put("status", 0);
            result.put("msg", "新增失败！");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 异步获取角色信息
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/roleInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String roleInfo()
        throws Exception
    {
        try
        {
            Map<String, Object> result = adminService.findAllRole(new HashMap<String, Object>());
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("rows", new ArrayList());
            result.put("total", 0);
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 插入权限
     *
     * @param permission  eg : AccountController,list,用户列表;AccountController,export,用户列表导出
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addPermission", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addPermission(@RequestParam(value = "permission", required = true) String permission)
        throws Exception
    {
        try
        {
            List<Map<String, String>> permissions = new ArrayList<>();
            for (String s : permission.split(";"))
            {
                String[] info = s.trim().split(",");
                if (info.length == 3)
                {
                    Map<String, String> cuPer = new HashMap<>();
                    cuPer.put("category", info[0].trim());
                    cuPer.put("permission", info[0].trim() + "_" + info[1].trim());
                    cuPer.put("description", info[2].trim());
                    permissions.add(cuPer);
                }
            }
            int status = adminService.insertPermission(permissions);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status > 0 ? 1 : 0);
            result.put("msg", status > 0 ? "ok" : "fail");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            log.error("新增角色失败", e);
            result.put("status", 0);
            result.put("msg", "新增失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 修改角色信息
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editRoleForm", produces = "application/json;charset=UTF-8")
    public ModelAndView editRoleForm(@RequestParam(value = "roleId", required = false, defaultValue = "0") int roleId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("admin/editRoleForm");
        Map<String, Object> info = adminService.findRoleById(roleId);
        if (info == null)
        {
            return mv;
        }
        mv.addObject("id", roleId + "");
        mv.addObject("role", info.get("role") + "");
        mv.addObject("description", info.get("description") + "");
        List<Map<String, Object>> permissionList = adminService.findAllPermission(new HashMap<String, Object>(), roleId);
        mv.addObject("permissionList", permissionList);
        return mv;
    }
    
    @RequestMapping(value = "/refreshPermission", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String refreshPermission()
        throws Exception
    {
        try
        {
            return adminService.refreshPermission("com.ygg.admin.controller");
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            log.error("刷新权限失败", e);
            result.put("status", 0);
            result.put("msg", "失败");
            return JSON.toJSONString(result);
        }
    }
}
