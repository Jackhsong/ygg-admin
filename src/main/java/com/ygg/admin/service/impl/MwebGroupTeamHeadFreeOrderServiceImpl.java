package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupTeamHeadFreeOrderDao;
import com.ygg.admin.service.MwebGroupTeamHeadFreeOrderService;

@Service("mwebGroupTeamHeadFreeOrderService")
public class MwebGroupTeamHeadFreeOrderServiceImpl implements MwebGroupTeamHeadFreeOrderService
{
    @Resource
    private MwebGroupTeamHeadFreeOrderDao mwebGroupTeamHeadFreeOrderDao;
    
    @Override
    public Map<String, Object> findTeamHeadFreeOrder(Map<String, Object> para)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<JSONObject> infoList = mwebGroupTeamHeadFreeOrderDao.findTeamHeadFreeOrder(para);
        int total = 0;
        total = mwebGroupTeamHeadFreeOrderDao.countTeamHeadFreeOrder();
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateTeamHeadFreeOrder(JSONObject j)
        throws Exception
    {
        if (j.getInteger("id") == 0)
        {
            return mwebGroupTeamHeadFreeOrderDao.addTeamHeadFreeOrder(j);
        }
        else
        {
            return mwebGroupTeamHeadFreeOrderDao.updateTeamHeadFreeOrder(j);
        }
        
    }
    
    @Override
    public int updateIsOpenGive(Map<String, Object> para)
        throws Exception
    {
        // TODO Auto-generated method stub
        return mwebGroupTeamHeadFreeOrderDao.updateIsOpenGive(para);
    }
    
}
