package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.ManagerDao;
import com.ygg.admin.entity.ManagerEntity;
import com.ygg.admin.service.ManagerService;
import com.ygg.admin.util.CommonUtil;

@Service("managerService")
public class ManagerServiceImpl implements ManagerService
{
    
    @Resource
    private ManagerDao managerDao;
    
    @Override
    public ManagerEntity getCurrentUser(HttpServletRequest request)
        throws Exception
    {
        String currentUserId = (String)request.getSession().getAttribute("currentUserId");
        if (currentUserId == null)
        {
            return null;
        }
        ManagerEntity manager = findManagerById(Integer.valueOf(currentUserId).intValue());
        return manager;
    }
    
    @Override
    public Map<String, Object> login(String name, String pwd)
        throws Exception
    {
        ManagerEntity manager = managerDao.findManagerByName(name);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        if (manager == null)
        {
            map.put("msg", "用户名错误");
            return map;
        }
        String md5Pwd = CommonUtil.strToMD5(pwd + name);
        if (manager.getPwd().equals(md5Pwd))
        {
            map.put("status", 1);
            map.put("md5pwd", md5Pwd);
            map.put("userId", manager.getId());
        }
        else
        {
            map.put("msg", "密码错误");
        }
        return map;
    }
    
    @Override
    public int register(String name, String pwd)
        throws Exception
    {
        ManagerEntity manager = new ManagerEntity();
        manager.setName(name);
        String md5Pwd = CommonUtil.strToMD5(pwd + name);
        manager.setPwd(md5Pwd);
        int result = managerDao.insertManager(manager);
        return result;
    }
    
    @Override
    public ManagerEntity findManagerById(int id)
        throws Exception
    {
        ManagerEntity manager = managerDao.findManagerById(id);
        return manager;
    }
    
    @Override
    public Map findAllManagerByPara(Map<String, Object> para)
        throws Exception
    {
        para.put("enabled", 1);
        List<ManagerEntity> reList = managerDao.findAllManagerByPara(para);
        int num = 0;
        if (reList.size() > 0)
        {
            for (ManagerEntity managerEntity : reList)
            {
                managerEntity.setPwd("");
            }
            num = managerDao.countManagerByPara(para);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", reList);
        map.put("total", num);
        return map;
    }
    
    @Override
    public int updatePWD(int id, String pwd)
        throws Exception
    {
        ManagerEntity ma = managerDao.findManagerById(id);
        String md5Pwd = CommonUtil.strToMD5(pwd + ma.getName());
        return managerDao.updatePWDByID(md5Pwd, id);
    }
    
}
