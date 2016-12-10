package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.service.CustomActivitiesService;

@Service("customActivitiesService")
public class CustomActivitiesServiceImpl implements CustomActivitiesService
{
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Override
    public Map<String, Object> jsonCustomActivitiesInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = customActivitiesDao.findCustomActivitiesInfo(para);
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            int type = Integer.valueOf(map.get("typeCode") + "").intValue();
            map.put("type", CustomEnum.CUSTOM_ACTIVITY_RELATION.getDescrByCode(type));
            map.put("isAvailable", ((int)map.get("isAvailableCode") == 1) ? "可用" : "停用");// 使用状态
        }
        total = customActivitiesDao.countCustomActivitiesInfo(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateCustomActivitiesStatus(Map<String, Object> para)
        throws Exception
    {
        return customActivitiesDao.updateCustomActivitiesStatus(para);
    }
    
    @Override
    public Map<String, Object> findCustomActivitiesById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = customActivitiesDao.findCustomActivitiesInfo(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> para)
        throws Exception
    {
        int id = Integer.valueOf(para.get("id") + "").intValue();
        if (id == 0)
        {
            return customActivitiesDao.add(para);
        }
        else
        {
            return customActivitiesDao.update(para);
        }
    }
    
    @Override
    public List<Map<String, Object>> findAllCustomActivities(Map<String, Object> para)
        throws Exception
    {
        return customActivitiesDao.findCustomActivitiesInfo(para);
    }
    
}
