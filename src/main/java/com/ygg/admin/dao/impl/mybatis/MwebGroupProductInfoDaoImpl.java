package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupProductInfoDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupProductInfoEntity;

@Repository("mwebGroupProductInfoDao")
public class MwebGroupProductInfoDaoImpl extends BaseDaoImpl implements MwebGroupProductInfoDao
{
    
    @Override
    public int addGroupProductInfo(MwebGroupProductInfoEntity mwebGroupProductInfoEntity)
    {
        
        return getSqlSession().insert("MwebGroupProductInfoMapper.addGroupProductInfo", mwebGroupProductInfoEntity);
    }
    
    @Override
    public List<JSONObject> findAutoTeamList(Map<String, Object> parameter)
    {
        List<JSONObject> list = getSqlSessionRead().selectList("MwebGroupProductInfoMapper.findAutoTeamList", parameter);
        return list == null ? new ArrayList<JSONObject>() : list;
    }
    
    @Override
    public JSONObject getAutoTeamConfig(int id)
    {
        // TODO Auto-generated method stub
        return getSqlSession().selectOne("MwebGroupProductInfoMapper.getAutoTeamConfigById", id);
    }
    
    @Override
    public int updateAutoTeamConfig(Map<String, Object> parameter)
    {
        // TODO Auto-generated method stub
        return getSqlSession().update("MwebGroupProductInfoMapper.updateAutoTeamConfig", parameter);
    }
    
    @Override
    public int addAutoTeamConfig(Map<String, Object> parameter)
    {
        
        return getSqlSession().insert("MwebGroupProductInfoMapper.addAutoTeamConfig", parameter);
    }
    
    @Override
    public MwebGroupProductInfoEntity getGroupProductInfoById(int id)
    {
        // TODO Auto-generated method stub
        return getSqlSession().selectOne("MwebGroupProductInfoMapper.getGroupProductInfoById", id);
    }
    
    @Override
    public int countAutoJoinTeamAccount(int mwebGroupProductInfoId)
    {
        // TODO Auto-generated method stub
        return getSqlSession().selectOne("MwebGroupProductInfoMapper.countAutoJoinTeamAccount", mwebGroupProductInfoId);
    }
    
    @Override
    public List<MwebGroupProductInfoEntity> findAutoGroupProductInfoByMwebGroupProductId(int mwebGroupProductId)
    {
        // TODO Auto-generated method stub
        return getSqlSession().selectList("MwebGroupProductInfoMapper.findAutoGroupProductInfoByMwebGroupProductId", mwebGroupProductId);
    }
    
    @Override
    public int updateGroupProductInfo(MwebGroupProductInfoEntity mwebGroupProductInfoEntity)
    {
        
        return getSqlSession().update("MwebGroupProductInfoMapper.updateGroupProductInfo", mwebGroupProductInfoEntity);
    }
    
    @Override
    public JSONObject findAutoTeamListByMwebGroupProductInfoId(int mwebGroupProductInfoId)
    {
        // TODO Auto-generated method stub
        return getSqlSession().selectOne("MwebGroupProductInfoMapper.findAutoTeamListByMwebGroupProductInfoId", mwebGroupProductInfoId);
    }
    
}
