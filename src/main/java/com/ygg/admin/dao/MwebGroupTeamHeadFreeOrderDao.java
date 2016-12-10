package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface MwebGroupTeamHeadFreeOrderDao
{
    public List<JSONObject> findTeamHeadFreeOrder(Map<String, Object> para);
    
    public int countTeamHeadFreeOrder();
    
    public int addTeamHeadFreeOrder(JSONObject j)
        throws Exception;
        
    public int updateIsOpenGive(Map<String, Object> para)
        throws Exception;
        
    public int updateTeamHeadFreeOrder(JSONObject j)
        throws Exception;
}
