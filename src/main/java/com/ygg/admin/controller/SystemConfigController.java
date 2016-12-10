package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.SystemConfigService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.CacheConstant;

/**
 * 系统配置管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/sysConfig")
public class SystemConfigController
{
    private CacheServiceIF cache = CacheManager.getClient();
    
    private Logger logger = Logger.getLogger(SystemConfigController.class);
    
    @Resource
    private SystemConfigService systemConfigService;
    
    @Resource
    private UserService userService;
    
    @RequestMapping("whiteIp")
    public ModelAndView ipList()
    {
        ModelAndView mv = new ModelAndView("common/whiteIpList");
        return mv;
    }
    
    @RequestMapping("whiteURL")
    public ModelAndView URLlist()
    {
        ModelAndView mv = new ModelAndView("common/whiteURLlist");
        return mv;
    }
    
    @RequestMapping(value = "/jsonWhiteIpInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonWhiteIpInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "ip", required = false, defaultValue = "") String ip,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(ip))
            {
                para.put("ip", ip);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = systemConfigService.jsonWhiteIpInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/jsonWhiteURLInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonWhiteURLInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "url", required = false, defaultValue = "") String url,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(url))
            {
                para.put("ip", url);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = systemConfigService.jsonWhiteURLInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增/保存ip白名单
     * @param id：id=0，表示新增
     * @param ip：ip
     * @param remark：备注
     * @param isAvailable：1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateWhiteIp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "系统管理-新增/编辑IP")
    public String saveOrUpdateWhiteIp(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "ip", required = false, defaultValue = "") String ip, @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("ip", ip);
            para.put("remark", remark);
            para.put("isAvailable", isAvailable);
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            if (user == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "无操作权限");
                return JSON.toJSONString(resultMap);
            }
            para.put("createUser", user.getId());
            para.put("updateUser", user.getId());
            int result = systemConfigService.saveOrUpdateWhiteIp(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
                // IP有更新时，清空缓存
                cache.delete(CacheConstant.ADMIN_USER_LOGIN_WHITE_IP_LIST);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 保存/更新url白名单
     * @param id：id=0，表示新增
     * @param url
     * @param remark
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateWhiteURL", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "系统管理-新增/编辑URL")
    public String saveOrUpdateWhiteURL(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "url", required = false, defaultValue = "") String url, @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("url", url);
            para.put("remark", remark);
            para.put("isAvailable", isAvailable);
            
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            if (user == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "无操作权限");
                return JSON.toJSONString(resultMap);
            }
            para.put("createUser", user.getId());
            para.put("updateUser", user.getId());
            int result = systemConfigService.saveOrUpdateWhiteURL(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
                // URL有更新时，清空缓存
                cache.delete(CacheConstant.ADMIN_USER_LOGIN_WHITE_URL_LIST);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新ip可用状态
     * @param id
     * @param isAvailable：1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/updateWhiteIpStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "系统管理-设置IP可用状态")
    public String updateWhiteIpStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> idList = new ArrayList<Integer>();
            String[] idArr = id.split(",");
            for (String idStr : idArr)
            {
                idList.add(Integer.valueOf(idStr));
            }
            
            para.put("idList", idList);
            para.put("isAvailable", isAvailable);
            int result = systemConfigService.updateWhiteIpStatus(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
                // IP有更新时，清空缓存
                cache.delete(CacheConstant.ADMIN_USER_LOGIN_WHITE_IP_LIST);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "更新失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新url可用状态
     * @param id
     * @param isAvailable：1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/updateWhiteURLStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "系统管理-设置URL可用状态")
    public String updateWhiteURLStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> idList = new ArrayList<Integer>();
            String[] idArr = id.split(",");
            for (String idStr : idArr)
            {
                idList.add(Integer.valueOf(idStr));
            }
            
            para.put("idList", idList);
            para.put("isAvailable", isAvailable);
            int result = systemConfigService.updateWhiteURLStatus(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
                // URL有更新时，清空缓存
                cache.delete(CacheConstant.ADMIN_USER_LOGIN_WHITE_URL_LIST);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "更新失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
}
