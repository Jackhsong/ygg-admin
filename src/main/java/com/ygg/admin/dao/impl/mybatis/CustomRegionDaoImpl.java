package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CustomRegionDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomLayoutEntity;
import com.ygg.admin.entity.CustomRegionEntity;

@Repository("customRegionDao")
public class CustomRegionDaoImpl extends BaseDaoImpl implements CustomRegionDao
{
    
    @Override
    public List<Map<String, Object>> findAllCustomRegion(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CustomRegionMapper.findAllCustomRegion", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegionMapper.countCustomRegion", para);
    }
    
    @Override
    public int saveCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CustomRegionMapper.saveCustomRegion", para);
    }
    
    @Override
    public int updateCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegionMapper.updateCustomRegion", para);
    }
    
    @Override
    public int updateCustomRegionAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegionMapper.updateCustomRegionAvailableStatus", para);
    }
    
    @Override
    public int updateCustomRegionDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegionMapper.updateCustomRegionDisplayStatus", para);
    }
    
    @Override
    public int findMaxCustomRegionSequence()
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CustomRegionMapper.findMaxCustomRegionSequence");
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int countCustomLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegionMapper.countCustomLayout", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCustomLayout(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CustomRegionMapper.findAllCustomLayout", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegionMapper.updateCustomLayoutDisplayStatus", para);
    }
    
    @Override
    public int updateCustomLayoutSequence(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegionMapper.updateCustomLayoutSequence", para);
    }
    
    @Override
    public int addCustomLayout(CustomLayoutEntity customLayout)
        throws Exception
    {
        return this.getSqlSession().insert("CustomRegionMapper.addCustomLayout", customLayout);
    }
    
    @Override
    public int updateCustomLayout(CustomLayoutEntity customLayout)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegionMapper.updateCustomLayout", customLayout);
    }
    
    @Override
    public int getCustonLayoutMaxSequence(int regionId)
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CustomRegionMapper.getCustonLayoutMaxSequence", regionId);
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int insertRelationCustomRegionLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CustomRegionMapper.insertRelationCustomRegionLayout", para);
    }
    
    @Override
    public int deleteCustomLayout(int id)
        throws Exception
    {
        return this.getSqlSession().delete("CustomRegionMapper.deleteCustomLayout", id);
    }
    
    @Override
    public CustomLayoutEntity findCustomLayoutById(int customLayoutId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegionMapper.findCustomLayoutById", customLayoutId);
    }
    
    @Override
    public CustomRegionEntity findCustomRegionById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegionMapper.findCustomRegionById", id);
    }
    
    @Override
    public List<Integer> findCustomLayoutIdByCustomRegionId(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("CustomRegionMapper.findCustomLayoutIdByCustomRegionId", id);
    }
}