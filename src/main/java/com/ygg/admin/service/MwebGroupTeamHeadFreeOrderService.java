package com.ygg.admin.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface MwebGroupTeamHeadFreeOrderService
{
    public Map<String, Object> findTeamHeadFreeOrder(Map<String, Object> para);
    
    public int saveOrUpdateTeamHeadFreeOrder(JSONObject j)
        throws Exception;
        
    public int updateIsOpenGive(Map<String, Object> para)
        throws Exception;
}
