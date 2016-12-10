package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.dao.ChannelDao;
import com.ygg.admin.service.ChannelService;

@Repository
public class ChannelServiceImpl implements ChannelService
{
    @Resource
    private ChannelDao channelDao;
    
    @Override
    public String jsonChannelInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", channelDao.findAllChannel(para));
        resultMap.put("total", channelDao.countChannel(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String saveChannel(String name)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (channelDao.saveChannel(name) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_channel_name"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("渠道%s已经存在", name));
                return JSON.toJSONString(resultMap);
            }
            else
            {
                throw new Exception(e);
            }
        }
        
    }
    
    @Override
    public String updateChannel(int id, String name)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("name", name);
        try
        {
            if (channelDao.updateChannel(para) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_channel_name"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("渠道%s已经存在", name));
                return JSON.toJSONString(resultMap);
            }
            else
            {
                throw new Exception(e);
            }
        }
        
    }
    
    @Override
    public String deleteChannel(int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (channelDao.deleteChannel(id) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public List<Map<String, Object>> findAllChannelByPara(Map<String, Object> para)
        throws Exception
    {
        return channelDao.findAllChannel(para);
    }
    
}
