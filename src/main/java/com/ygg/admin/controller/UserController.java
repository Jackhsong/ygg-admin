package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController
{
    private Logger log = Logger.getLogger(UserController.class);
    
    @Resource
    UserService userService;
    
    /**
     * 新增管理员
     * 
     * @return
     */
    @RequestMapping("/add")
    public ModelAndView list()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/add");
        return mv;
    }
    
    /**
     * 新增角色
     * 
     * @param request
     * @return
     */
    @RequestMapping("/addRoleForm")
    public ModelAndView addRoleForm(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/addRoleForm");
        List<Map<String, Object>> permissions = userService.findPermissionForAdd();
        mv.addObject("permissions", permissions);
        return mv;
    }
    
    /**
     * 新增 角色
     * 
     * @param request
     * @param role
     * @param id
     * @param resource
     * @param desc
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addRole", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-新增角色")
    public String addRole(HttpServletRequest request, @RequestParam(value = "role", required = true) String role,
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, @RequestParam(value = "resource", required = true) String resource,
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc)
        throws Exception
    {
        try
        {
            List<Integer> permissionIdList = new ArrayList<Integer>();
            if (resource.indexOf(",") > 0)
            {
                String[] arr = resource.split(",");
                for (String cur : arr)
                {
                    permissionIdList.add(Integer.valueOf(cur));
                }
            }
            Map<String, Object> result = userService.createOrUpdateRole(id, role, permissionIdList, desc);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            log.error("新增角色失败", e);
            result.put("status", 0);
            result.put("msg", "新增失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/roleInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String roleInfo(HttpServletRequest request)
        throws Exception
    {
        try
        {
            Map<String, Object> result = userService.findRolesForShow();
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList());
            result.put("total", 0);
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 插入权限
     * 
     * @param request
     * @param permission
     * @param description
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addPermission", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-新增权限")
    public String addPermission(HttpServletRequest request, @RequestParam(value = "permission", required = true) String permission,
        @RequestParam(value = "description", required = true) String description)
        throws Exception
    {
        try
        {
            userService.insertPermission(permission, description);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("msg", "ok");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            log.error("新增角色失败", e);
            result.put("status", 0);
            result.put("msg", "新增失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 管理员
     * 
     * @param request
     * @return
     */
    @RequestMapping("/userList")
    public ModelAndView userList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/user");
        Map<String, Object> roles = userService.findRolesForShow();
        List<Map<String, Object>> reList = (List<Map<String, Object>>)roles.get("rows");
        mv.addObject("roles", reList);
        return mv;
    }
    
    /** 管理员list */
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String userInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "name", required = false, defaultValue = "") String name)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
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
        Map re = userService.listUser(para);
        return JSON.toJSONString(re);
    }
    
    /**
     * 新增用户
     * 
     * @param request
     * @param roles：角色
     * @param name：用户名
     * @param realname：用户名
     * @param mobileNumber：手机号
     * @param pwd
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-新增用户")
    public String addUser(HttpServletRequest request, @RequestParam(value = "roles", required = true) String roles, @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "realname", required = true) String realname, @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,
        @RequestParam(value = "pwd", required = true) String pwd)
        throws Exception
    {
        try
        {
            List<Integer> roleIdList = new ArrayList<Integer>();
            if (roles.indexOf(",") > 0)
            {
                String[] arr = roles.split(",");
                for (String cur : arr)
                {
                    roleIdList.add(Integer.valueOf(cur));
                }
            }
            Map<String, Object> result = userService.createUser(name, realname, mobileNumber, pwd, roleIdList);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            log.error("新增角色失败", e);
            result.put("status", 0);
            result.put("msg", "新增失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 修改用户
     * 
     * @param request
     * @param id
     * @param roles
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editUser", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-修改用户")
    public String editUser(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, @RequestParam(value = "roles", required = true) String roles,// 
        @RequestParam(value = "name", required = true) String name,// 
        @RequestParam(value = "realname", required = true) String realname, @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber)
        throws Exception
    {
        try
        {
            List<Integer> roleIdList = new ArrayList<Integer>();
            if (roles.indexOf(",") > 0)
            {
                String[] arr = roles.split(",");
                for (String cur : arr)
                {
                    roleIdList.add(Integer.valueOf(cur));
                }
            }
            Map<String, Object> result = userService.updateUser(id, name, realname, mobileNumber, roleIdList);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            log.error("修改失败", e);
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 修改密码
     * 
     * @param request
     * @param pwd
     * @param _pwd
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "updatePWD", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-修改密码")
    public String updatePWD(HttpServletRequest request, @RequestParam(value = "pwd", required = false, defaultValue = "") String pwd,
        @RequestParam(value = "pwd1", required = false, defaultValue = "") String _pwd, @RequestParam(value = "editId", required = false, defaultValue = "0") int id)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!pwd.equals(_pwd))
        {
            result.put("status", 0);
            result.put("msg", "两次输入的密码不一致");
            return JSON.toJSONString(result);
        }
        // if (!StringUtils.conformPWD(pwd))
        // {
        // result.put("status", 0);
        // result.put("msg", "密码只允许6-12位的数字和26个英文字符的组合");
        // return JSON.toJSONString(result);
        // }
        try
        {
            Map<String, Object> map = userService.changePassword(id, pwd);
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "updateLocked", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-更新锁定状态")
    public String updateLocked(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,//
        @RequestParam(value = "locked", required = true) int locked)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            int status = userService.updateUserLocked(id, locked);
            result.put("status", status);
            result.put("msg", status == 1 ? "修改成功" : "修改失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 批量锁定/解锁账户
     * @param request
     * @param ids
     * @param locked：1锁定，0解锁
     * @return
     */
    @RequestMapping(value = "batchUpdateLockStatus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "管理员管理-批量更新锁定状态")
    public String batchUpdateLockStatus(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids,
        @RequestParam(value = "locked", required = true) int locked)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            String[] arr = ids.split(",");
            List<Integer> idList = new ArrayList<Integer>();
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
            para.put("locked", locked);
            para.put("idList", idList);
            int status = userService.batchUpdateLockStatus(para);
            result.put("status", status);
            result.put("msg", status == 1 ? "操作成功" : "操作失败");
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            result.put("status", 0);
            result.put("msg", "操作失败");
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "findAllUserCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findAllUserCode() throws Exception
    {
        List<Map<String, Object>> userCode = userService.findAllUserCode();
        return JSON.toJSONString(userCode);
    }
}
