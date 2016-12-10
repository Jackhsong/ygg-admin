package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.ActivitySimplifyDao;
import com.ygg.admin.service.ActivitySimplifyService;

@Service("activitySimplifyService")
public class ActivitySimplifyServiceImpl implements ActivitySimplifyService
{
    @Resource
    private ActivitySimplifyDao activitySimplifyDao;
    
    @Override
    public Map<String, Object> jsonActivitySimplifyInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = activitySimplifyDao.findAllActivitySimplify(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isAvailable = Integer.valueOf(map.get("isAvailable") + "").intValue();
                
                map.put("availableDesc", isAvailable == 1 ? "可用" : "停用");
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
                map.put("url", "http://m.gegejia.com/ygg/activity/simplify/web/" + map.get("id"));
            }
            total = activitySimplifyDao.countActivitySimplify(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateActivitySimplify(Map<String, Object> para)
        throws Exception
    {
        int id = Integer.valueOf(para.get("id") + "").intValue();
        if (id == 0)
        {
            return activitySimplifyDao.saveActivitySimplify(para);
        }
        else
        {
            return activitySimplifyDao.updateActivitySimplify(para);
        }
    }
    
    @Override
    public int updateActivitySimplifyAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return activitySimplifyDao.updateActivitySimplifyAvailableStatus(para);
    }
    
    @Override
    public Map<String, Object> findActivitySimplifyById(int activityId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", activityId);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = activitySimplifyDao.findAllActivitySimplify(para);
        if (infoList == null || infoList.size() == 0)
            return null;
        return infoList.get(0);
    }
    
    @Override
    public Map<String, Object> jsonActivitySimplifyLayoutProductInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = activitySimplifyDao.findActivitySimplifyProduct(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int isDisplay = Integer.valueOf(map.get("isDisplay") + "").intValue();
                map.put("displayDesc", isDisplay == 1 ? "展现" : "不展现");
                map.put("imageURL", "<a href='" + map.get("image") + "' target='_blank'>查看图片</a>");
            }
            total = activitySimplifyDao.countActivitySimplifyProduct(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateActivitySimplifyProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return activitySimplifyDao.updateActivitySimplifyProductDisplayStatus(para);
    }
    
    @Override
    public int updateActivitySimplifyProductSequence(Map<String, Object> para)
        throws Exception
    {
        return activitySimplifyDao.updateActivitySimplifyProduct(para);
    }
    
    @Override
    public int saveOrUpdateActivitySimplifyProduct(Map<String, Object> para)
        throws Exception
    {
        int id = Integer.valueOf(para.get("id") + "").intValue();
        if (id == 0)
        {
            return activitySimplifyDao.saveActivitySimplifyProduct(para);
        }
        else
        {
            return activitySimplifyDao.updateActivitySimplifyProduct(para);
        }
        
    }
    
    @Override
    public List<Map<String, Object>> findAllActivitySimplify(Map<String, Object> para)
        throws Exception
    {
        return activitySimplifyDao.findAllActivitySimplify(para);
    }
}
