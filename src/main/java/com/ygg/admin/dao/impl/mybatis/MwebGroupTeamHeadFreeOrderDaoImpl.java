package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupTeamHeadFreeOrderDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("mwebGroupTeamHeadFreeOrderDao")
public class MwebGroupTeamHeadFreeOrderDaoImpl extends BaseDaoImpl implements MwebGroupTeamHeadFreeOrderDao
{
    
    @Override
    public List<JSONObject> findTeamHeadFreeOrder(Map<String, Object> para)
    {
        
        return this.getSqlSessionRead().selectList("MwebGroupTeamHeadFreeOrderMapper.findTeamHeadFreeOrder", para);
        
    }
    
    @Override
    public int countTeamHeadFreeOrder()
    {
        return this.getSqlSessionRead().selectOne("MwebGroupTeamHeadFreeOrderMapper.countTeamHeadFreeOrder");
    }
    
    @Override
    public int addTeamHeadFreeOrder(JSONObject j)
        throws Exception
    {
        
        return this.getSqlSession().insert("MwebGroupTeamHeadFreeOrderMapper.addTeamHeadFreeOrder", j);
    }
    
    @Override
    public int updateIsOpenGive(Map<String, Object> para)
        throws Exception
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().update("MwebGroupTeamHeadFreeOrderMapper.updateIsOpenGive", para);
    }
    
    @Override
    public int updateTeamHeadFreeOrder(JSONObject j)
        throws Exception
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().update("MwebGroupTeamHeadFreeOrderMapper.updateTeamHeadFreeOrder", j);
    }
    
}
