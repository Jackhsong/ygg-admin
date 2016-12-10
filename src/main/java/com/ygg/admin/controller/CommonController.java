package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.service.MenuService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.StringUtils;

@Controller
@RequestMapping("/common")
public class CommonController
{
    @Resource
    private MenuService menuService;
    
    @Resource
    private UserService userService;

    /**
     * 获取菜单数据  新  启用
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/menu", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String menu(HttpServletRequest request)
        throws Exception
    {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser == null)
        {
            // 未登陆
            return "";
        }
        String nodes = request.getParameter("nodes");
        List<Integer> stateList = new ArrayList<>();
        if (nodes != null && !nodes.equals("0"))
        {
            String[] msArr = nodes.split("-");
            for (String string : msArr)
            {
                stateList.add(Integer.valueOf(string));
            }
        }
        String username = currentUser.getPrincipal() + "";
        List<Map<String, Object>> menuList = menuService.findMenuByUsername(username, stateList);
        return JSON.toJSONString(menuList);
    }

    /**
     * 获取菜单数据   弃用
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/menuInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String userInfo(HttpServletRequest request)
        throws Exception
    {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser == null)
        {
            // 未登陆
            return "";
        }
        String username = currentUser.getPrincipal() + "";
        Set<String> menuIdSet = userService.findPermissions(username);
        List<Integer> menuIdList = new ArrayList<Integer>();
        for (String string : menuIdSet)
        {
            menuIdList.add(Integer.valueOf(string));
        }
        String nodes = request.getParameter("nodes");
        List<Integer> stateList = new ArrayList<Integer>();
        if (nodes != null && !nodes.equals("0"))
        {
            String[] msArr = nodes.split("-");
            for (String string : msArr)
            {
                stateList.add(Integer.valueOf(string));
            }
        }
        List<Map<String, Object>> menuList = menuService.loadTree(menuIdList, stateList);
        return JSON.toJSONString(menuList);
    }
    
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        String ua = request.getHeader("User-Agent");
        if (StringUtils.isMobile(ua))
        {
            mv.setViewName("user/mobileIndex");
        }
        else
        {
            mv.setViewName("user/index");
        }
        return mv;
    }
}
