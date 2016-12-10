package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ActivitySimplifyDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("activitySimplifyDao")
public class ActivitySimplifyDaoImpl extends BaseDaoImpl implements ActivitySimplifyDao
{
    
    @Override
    public List<Map<String, Object>> findAllActivitySimplify(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ActivitySimplifyMapper.findAllActivitySimplify", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countActivitySimplify(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ActivitySimplifyMapper.countActivitySimplify", para);
    }
    
    @Override
    public int saveActivitySimplify(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("ActivitySimplifyMapper.saveActivitySimplify", para);
    }
    
    @Override
    public int updateActivitySimplify(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ActivitySimplifyMapper.updateActivitySimplify", para);
    }
    
    @Override
    public int updateActivitySimplifyAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ActivitySimplifyMapper.updateActivitySimplifyAvailableStatus", para);
    }
    
    @Override
    public int countActivitySimplifyProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ActivitySimplifyMapper.countActivitySimplifyProduct", para);
    }
    
    @Override
    public List<Map<String, Object>> findActivitySimplifyProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ActivitySimplifyMapper.findActivitySimplifyProduct", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int updateActivitySimplifyProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ActivitySimplifyMapper.updateActivitySimplifyProductDisplayStatus", para);
    }
    
    @Override
    public int saveActivitySimplifyProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("ActivitySimplifyMapper.saveActivitySimplifyProduct", para);
    }
    
    @Override
    public int updateActivitySimplifyProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ActivitySimplifyMapper.updateActivitySimplifyProduct", para);
    }

    @Override public List<Integer> findActivitySimplifyProductIdBySimplifyActivityId(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("ActivitySimplifyMapper.findActivitySimplifyProductIdBySimplifyActivityId",id);
    }
}
