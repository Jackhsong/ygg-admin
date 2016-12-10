package com.ygg.admin.dao.impl.mybatis;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupProductCountDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupProductCountEntity;

@Repository("mwebGroupProductCountDao")
public class MwebGroupProductCountDaoImpl extends BaseDaoImpl implements MwebGroupProductCountDao
{
    
    @Override
    public int addMwebGroupProductCount(MwebGroupProductCountEntity mwebGroupProductCountEntity)
    {
        
        return this.getSqlSession().insert("ProductCountMapper.addMwebGroupProductCount", mwebGroupProductCountEntity);
    }
    
    @Override
    public MwebGroupProductCountEntity getGroupProductCount(MwebGroupProductCountEntity mwebGroupProductCountEntity)
    {
        
        return this.getSqlSessionRead().selectOne("ProductCountMapper.getGroupProductCount", mwebGroupProductCountEntity);
    }
    
    @Override
    public MwebGroupProductCountEntity getGroupProductCount_forUpdate(MwebGroupProductCountEntity mwebGroupProductCountEntity)
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().selectOne("ProductCountMapper.getGroupProductCount_forUpdate", mwebGroupProductCountEntity);
    }
    
    @Override
    public int updateGroupProductCount(JSONObject jsonObject)
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().update("ProductCountMapper.updateGroupProductCount", jsonObject);
    }
    
}
