package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.GateActivityDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.GateEntity;

@Repository("gateActivityDao")
public class GateActivityDaoImpl extends BaseDaoImpl implements GateActivityDao
{
    
    @Override
    public int countGates(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("GateMapper.countGates", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllGates(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("GateMapper.findAllGates", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int addGate(GateEntity gate)
        throws Exception
    {
        
        return this.getSqlSession().insert("GateMapper.addGate", gate);
    }
    
    @Override
    public int addGatePrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("GateMapper.addGatePrize", para);
    }
    
    @Override
    public int updateGate(GateEntity gate)
        throws Exception
    {
        return this.getSqlSession().update("GateMapper.updateGate", gate);
    }
    
    @Override
    public int updateGatePrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("GateMapper.updateGatePrize", para);
    }
    
    @Override
    public int updateGateStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("GateMapper.updateGateStatus", para);
    }
    
    @Override
    public int deleteGate(int id)
        throws Exception
    {
        return this.getSqlSession().delete("GateMapper.deleteGate", id);
    }
    
    @Override
    public int deleteGatePrize(int gateId)
        throws Exception
    {
        return this.getSqlSession().delete("GateMapper.deleteGatePrize", gateId);
    }
    
}
