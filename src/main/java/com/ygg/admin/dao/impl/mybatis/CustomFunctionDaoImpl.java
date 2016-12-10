package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CustomFunctionDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomFunctionEntity;

@Repository
public class CustomFunctionDaoImpl extends BaseDaoImpl implements CustomFunctionDao
{
    
    @Override
    public List<CustomFunctionEntity> findAllCustomFunction(Map<String, Object> para)
        throws Exception
    {
        List<CustomFunctionEntity> reList = getSqlSessionRead().selectList("CustomFunctionMapper.findAllCustomFunction", para);
        return reList == null ? new ArrayList<CustomFunctionEntity>() : reList;
    }
    
    @Override
    public int countCustomFunction(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomFunctionMapper.countCustomFunction", para);
    }
    
    @Override
    public int saveCustomFunction(CustomFunctionEntity function)
        throws Exception
    {
        return getSqlSession().insert("CustomFunctionMapper.saveCustomFunction", function);
    }
    
    @Override
    public int updateCustomFunction(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CustomFunctionMapper.updateCustomFunction", para);
    }
    
    @Override
    public int updateDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CustomFunctionMapper.updateDisplayStatus", para);
    }
    
    @Override
    public CustomFunctionEntity findCustomFunctionById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomFunctionMapper.findCustomFunctionById", id);
    }
    
}
