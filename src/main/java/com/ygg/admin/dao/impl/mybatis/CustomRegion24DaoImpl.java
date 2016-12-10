package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.CustomRegion24Dao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomLayoutEntity;
import com.ygg.admin.entity.CustomRegionEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("customRegion24Dao")
public class CustomRegion24DaoImpl extends BaseDaoImpl implements CustomRegion24Dao
{
    
    @Override
    public List<Map<String, Object>> findAllCustomRegion(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CustomRegion24Mapper.findAllCustomRegion", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegion24Mapper.countCustomRegion", para);
    }
    
    @Override
    public int saveCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CustomRegion24Mapper.saveCustomRegion", para);
    }
    
    @Override
    public int updateCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegion24Mapper.updateCustomRegion", para);
    }
    
    @Override
    public int updateCustomRegionAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegion24Mapper.updateCustomRegionAvailableStatus", para);
    }
    
    @Override
    public int updateCustomRegionDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegion24Mapper.updateCustomRegionDisplayStatus", para);
    }
    
    @Override
    public int findMaxCustomRegionSequence()
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CustomRegion24Mapper.findMaxCustomRegionSequence");
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int countCustomLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegion24Mapper.countCustomLayout", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCustomLayout(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CustomRegion24Mapper.findAllCustomLayout", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegion24Mapper.updateCustomLayoutDisplayStatus", para);
    }
    
    @Override
    public int updateCustomLayoutSequence(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegion24Mapper.updateCustomLayoutSequence", para);
    }
    
    @Override
    public int addCustomLayout(CustomLayoutEntity customLayout)
        throws Exception
    {
        return this.getSqlSession().insert("CustomRegion24Mapper.addCustomLayout", customLayout);
    }
    
    @Override
    public int updateCustomLayout(CustomLayoutEntity customLayout)
        throws Exception
    {
        return this.getSqlSession().update("CustomRegion24Mapper.updateCustomLayout", customLayout);
    }
    
    @Override
    public int getCustonLayoutMaxSequence(int regionId)
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CustomRegion24Mapper.getCustonLayoutMaxSequence", regionId);
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int insertRelationCustomRegionLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CustomRegion24Mapper.insertRelationCustomRegionLayout", para);
    }
    
    @Override
    public int deleteCustomLayout(int id)
        throws Exception
    {
        return this.getSqlSession().delete("CustomRegion24Mapper.deleteCustomLayout", id);
    }
    
    @Override
    public CustomLayoutEntity findCustomLayoutById(int customLayoutId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegion24Mapper.findCustomLayoutById", customLayoutId);
    }
    
    @Override
    public CustomRegionEntity findCustomRegionById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomRegion24Mapper.findCustomRegionById", id);
    }
    
    @Override
    public List<Integer> findCustomLayoutIdByCustomRegionId(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("CustomRegion24Mapper.findCustomLayoutIdByCustomRegionId", id);
    }
}
