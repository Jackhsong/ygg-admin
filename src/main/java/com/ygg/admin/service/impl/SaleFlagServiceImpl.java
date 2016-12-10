package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.SaleFlagDao;
import com.ygg.admin.service.SaleFlagService;

@Service("saleFlagService")
public class SaleFlagServiceImpl implements SaleFlagService
{
    @Resource
    private SaleFlagDao saleFlagDao;
    
    @Override
    public Map<String, Object> jsonSaleFlagInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = saleFlagDao.findAllSaleFlagInfo(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("isAvailable", ((int)map.get("isAvailableCode")) == 1 ? "可用" : "不可用");
                map.put("image", "<img alt='' src='" + map.get("imageURL") + "' style='max-width:100px'/>");
            }
            total = saleFlagDao.countSaleFlagInfoInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public boolean checkFlagIsInUse(int id)
        throws Exception
    {
        int count = saleFlagDao.countFlagIdFromSaleWindow(id);
        return count > 0;
    }
    
    @Override
    public int updateFlag(Map<String, Object> para)
        throws Exception
    {
        return saleFlagDao.updateFlag(para);
    }
    
    @Override
    public int saveOrUpdateFlag(Map<String, Object> para)
        throws Exception
    {
        int flagId = (int)para.get("id");
        if (flagId == 0)
        {
            return saleFlagDao.saveFlag(para);
        }
        else
        {
            return saleFlagDao.updateFlag(para);
        }
    }
    
    @Override
    public List<Map<String, Object>> jsonSaleFlagCode(Map<String, Object> para)
        throws Exception
    {
        return saleFlagDao.findAllSaleFlagInfo(para);
    }
    
    @Override
    public String findFlagNameById(int id)
        throws Exception
    {
        return saleFlagDao.findFlagNameById(id);
    }
}
