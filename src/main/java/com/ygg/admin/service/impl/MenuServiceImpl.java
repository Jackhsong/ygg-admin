package com.ygg.admin.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.code.ControllerMappingEnum;
import com.ygg.admin.service.AdminService;
import com.ygg.admin.util.CacheConstant;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.MenuDao;
import com.ygg.admin.dao.UserDao;
import com.ygg.admin.service.MenuService;

@Service("menuService")
public class MenuServiceImpl implements MenuService
{
    
    @Resource
    private MenuDao menuDao;
    
    @Resource
    private UserDao userDao;

    @Resource(name = "shiroAdminService")
    private AdminService adminService;
    
    @Override
    public List<Map<String, Object>> listMenu(Integer pid)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("pid", pid);
        return menuDao.findAllMenuByPara(para);
    }
    
    @Override
    public int createOrUpdate(Map<String, Object> para)
        throws Exception
    {
        String pid = para.get("pid") + "";
        int status = 0;
        if ("0".equals(para.get("id")))
        {
            //新增
            status = menuDao.createMenu(para);
            if (status == 1 && "0".equals(pid))
            {
                String permission = para.get("id") + "";
                String text = para.get("text") + "";
                //增加一级目录，加入权限表
                Map<String, Object> permissionPara = new HashMap<String, Object>();
                permissionPara.put("permission", permission);
                permissionPara.put("description", text);
                userDao.insertPermission(permissionPara);
            }
        }
        else
        {
            //更新
            status = menuDao.updateMenu(para);
        }
        return status;
    }
    
    @Override
    public List<Map<String, Object>> loadTree(List<Integer> menuIdList, List<Integer> stateList)
        throws Exception
    {
        Map<String, Object> menuPara = new HashMap<String, Object>();
        menuPara.put("idList", menuIdList);
        List<Map<String, Object>> menuList = menuDao.findAllMenuByPara(menuPara);
        List<Map<String, Object>> menuInfoList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> currMap : menuList)
        {
            // before {"id":4,"pid":0,"sequence":9,"text":"商品管理","url":""}
            // end id:1,text:'',children:[],state:'open',attributes:[url:'']
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", currMap.get("id"));
            map.put("text", currMap.get("text"));
            String url = currMap.get("url") + "";
            if ("".equals(url))
            {
                // 包含子目录
                int id = Integer.valueOf(currMap.get("id") + "");
                if (stateList.size() == 0)
                {
                    if (menuIdList.contains(3))// 含有特卖管理
                    {
                        stateList.add(3);
                    }
                    else
                    {
                        stateList.add(2);
                    }
                    stateList.add(4);
                }
                if (stateList.contains(id))
                {
                    map.put("state", "open");
                }
                else
                {
                    map.put("state", "closed");
                }
                Integer pId = Integer.valueOf(currMap.get("id") + "");
                // TODO 获得children
                List<Map<String, Object>> children = getMenuChildren(pId);
                map.put("children", children);
            }
            // 自定义属性放这个map
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("url", url);
            map.put("attributes", attributes);
            menuInfoList.add(map);
        }
        return menuInfoList;
    }
    
    private List<Map<String, Object>> getMenuChildren(Integer pId)
        throws Exception
    {
        Map<String, Object> menuPara = new HashMap<String, Object>();
        menuPara.put("pid", pId);
        List<Map<String, Object>> menuList = menuDao.findAllMenuByPara(menuPara);
        List<Map<String, Object>> menuInfoList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> m : menuList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", m.get("id"));
            map.put("text", m.get("text"));
            String url = m.get("url") + "";
            if ("".equals(url))
            {
                // 包含子目录
                map.put("state", "open");
                Integer _pId = Integer.valueOf(m.get("id") + "");
                // TODO 获得children
                List<Map<String, Object>> children = getMenuChildren(_pId);
                map.put("children", children);
            }
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("url", url);
            map.put("attributes", attributes);
            menuInfoList.add(map);
        }
        return menuInfoList;
    }
    
    @Override
    public int delete(int id)
        throws Exception
    {
        return menuDao.delete(id);
    }

    @Override
    public List<Map<String, Object>> findMenuByUsername(String username, List<Integer> stateList)
        throws Exception
    {
        List<Map<String, Object>> menuList = new ArrayList<>();
        Set<String> permissionSet = adminService.findPermissions(username);
        Map<String,Object> para = new HashMap<>();
        para.put("pid", 0);
        List<Map<String, Object>> firstMenuList = menuDao.findAllMenuByPara(para);
        for (Map<String, Object> firstMenu : firstMenuList)
        {
            int id = Integer.valueOf(firstMenu.get("id") + "");
            List<Map<String, Object>> children = loadChild(id, permissionSet);
            if (children.size() > 0)
            {
                Map<String,Object> currMenu = new HashMap<>();
                currMenu.put("children", children);
                currMenu.put("id", id + "");
                currMenu.put("text", firstMenu.get("text"));
                if (stateList.contains(id))
                {
                    currMenu.put("state", "open");
                }
                else
                {
                    currMenu.put("state", "closed");
                }
                // 自定义属性放这个map
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("url", firstMenu.get("url") + "");
                currMenu.put("attributes", attributes);
                menuList.add(currMenu);
            }
        }
        return menuList;
    }

    private List<Map<String, Object>> loadChild(int pid, Set<String> permissionSet)
        throws Exception
    {
        List<Map<String, Object>> menuInfoList = new ArrayList<>();
        Map<String,Object> para = new HashMap<>();
        para.put("pid", pid);
        List<Map<String, Object>> currMenuList = menuDao.findAllMenuByPara(para);
        for (Map<String, Object> it : currMenuList)
        {
            String url = it.get("url") + "";  // /banner/list
            Map<String,Object> childMenu = null;
            if ("null".equals(url) || "".equals(url))
            {
                // 是目录
                Integer _id = Integer.valueOf(it.get("id") + "");
                List<Map<String, Object>> nextChildren = loadChild(_id, permissionSet);
                if (nextChildren.size() > 0)
                {
                    childMenu = new HashMap<>();
                    childMenu.put("state", "open");
                    childMenu.put("children", nextChildren);
                }
            }
            else
            {
                // 判断是否有权限展示
//                if (permissionSet.contains(getUrlPermission(url)))
//                {
                    childMenu = new HashMap<>();
//                }
            }

            if (childMenu != null)
            {
                childMenu.put("id", it.get("id"));
                childMenu.put("text", it.get("text"));
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("url", url);
                childMenu.put("attributes", attributes);
                menuInfoList.add(childMenu);
            }
        }
        return menuInfoList;
    }

    private String getUrlPermission(String url)
    {
        String[] arr = url.split("/"); // /banner/list?a=1
        if (arr.length >= 2)
        {
            String controllerName = ControllerMappingEnum.getControllerNameByName(arr[1]);
            if (controllerName != null)
            {
                String method = arr[2];
                if (method.indexOf("?") > -1)
                {
                    method = method.substring(0, method.indexOf("?"));
                }
                String permission = controllerName + "_" + method;
                return permission.trim();
            }
        }
        return "";
    }

}
