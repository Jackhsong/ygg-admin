package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CustomCenterDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomCenterEntity;

@Repository
public class CustomCenterDaoImpl extends BaseDaoImpl implements CustomCenterDao
{
    
    @Override
    public List<CustomCenterEntity> findAllCustomCenter(Map<String, Object> para)
        throws Exception
    {
        List<CustomCenterEntity> reList = getSqlSessionRead().selectList("CustomCenterMapper.findAllCustomCenter", para);
        return reList == null ? new ArrayList<CustomCenterEntity>() : reList;
    }
    
    @Override
    public int countCustomCenter(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomCenterMapper.countCustomCenter", para);
    }
    
    @Override
    public int saveCustomCenter(CustomCenterEntity center)
        throws Exception
    {
        return getSqlSession().insert("CustomCenterMapper.saveCustomCenter", center);
    }
    
    @Override
    public int updateCustomCenter(CustomCenterEntity center)
        throws Exception
    {
        return getSqlSession().update("CustomCenterMapper.updateCustomCenter", center);
    }
    
    @Override
    public int updateDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CustomCenterMapper.updateDisplayStatus", para);
    }
    
    @Override
    public CustomCenterEntity findCustomCenterById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomCenterMapper.findCustomCenterById", id);
    }
    
}
