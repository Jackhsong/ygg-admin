package com.ygg.admin.dao.impl.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupImageDetailDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupImageDetailEntity;

@Repository("mwebGroupImageDetailDao")
public class MwebGroupImageDetailDaoImpl extends BaseDaoImpl implements MwebGroupImageDetailDao
{
    
    @Override
    public List<MwebGroupImageDetailEntity> findMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity)
    {
        
        return this.getSqlSessionRead().selectList("MwebGroupImageDetailMapper.findMwebGroupImageDetail", mwebGroupImageDetailEntity);
    }
    
    @Override
    public int addMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity)
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().insert("MwebGroupImageDetailMapper.addMwebGroupImageDetail", mwebGroupImageDetailEntity);
    }
    
    @Override
    public int updateMwebGroupImageDetail(MwebGroupImageDetailEntity mwebGroupImageDetailEntity)
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().update("MwebGroupImageDetailMapper.updateMwebGroupImageDetail", mwebGroupImageDetailEntity);
    }
    
    @Override
    public MwebGroupImageDetailEntity getMwebGroupImageDetailById(int id)
    {
        // TODO Auto-generated method stub
        return this.getSqlSessionRead().selectOne("MwebGroupImageDetailMapper.getMwebGroupImageDetailById", id);
    }
    
    @Override
    public int deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId(JSONObject j)
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().delete("MwebGroupImageDetailMapper.deleteMwebGroupImageDetailByNotInIdsAndMwebGroupProductId", j);
    }
    
}
