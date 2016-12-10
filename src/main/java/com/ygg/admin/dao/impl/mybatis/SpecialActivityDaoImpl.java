package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.SpecialActivityDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SpecialActivityEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("specialActivityDao")
public class SpecialActivityDaoImpl extends BaseDaoImpl implements SpecialActivityDao
{
    
    @Override
    public List<Map<String, Object>> findAllSpecialActivity(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SpecialActivityMapper.findAllSpecialActivity", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countSpecialActivity(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SpecialActivityMapper.countSpecialActivity", para);
    }
    
    @Override
    public int saveSpecialActivity(SpecialActivityEntity sae)
        throws Exception
    {
        return this.getSqlSession().insert("SpecialActivityMapper.saveSpecialActivity", sae);
    }
    
    @Override
    public int updateSpecialActivity(SpecialActivityEntity sae)
        throws Exception
    {
        return this.getSqlSession().update("SpecialActivityMapper.updateSpecialActivity", sae);
    }
    
    @Override
    public int updateSpecialActivityAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SpecialActivityMapper.updateSpecialActivityAvailableStatus", para);
    }
    
    @Override
    public int countSpecialActivityLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SpecialActivityMapper.countSpecialActivityLayout", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSpecialActivityLayout(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SpecialActivityMapper.findAllSpecialActivityLayout", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int saveSpecialActivityLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("SpecialActivityMapper.saveSpecialActivityLayout", para);
    }
    
    @Override
    public int updateSpecialActivityLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SpecialActivityMapper.updateSpecialActivityLayout", para);
    }
    
    @Override
    public int findMaxSpecialActivityLayoutSequenceByActivityId(int activityId)
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("SpecialActivityMapper.findMaxSpecialActivityLayoutSequenceByActivityId", activityId);
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int updateSpecialActivityLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SpecialActivityMapper.updateSpecialActivityLayoutDisplayStatus", para);
    }
    
    @Override
    public int updateSpecialActivityLayoutSequence(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SpecialActivityMapper.updateSpecialActivityLayoutSequence", para);
    }
    
    @Override
    public int countSpecialActivityLayouProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SpecialActivityMapper.countSpecialActivityLayouProduct", para);
    }
    
    @Override
    public List<Map<String, Object>> findSpecialActivityLayouProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SpecialActivityMapper.findSpecialActivityLayouProduct", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateSpecialActivityLayoutProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SpecialActivityMapper.updateSpecialActivityLayoutProduct", para);
    }
    
    @Override
    public Map<String, Object> findSpecialActivityLayoutProduct(int layoutProductId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SpecialActivityMapper.findSpecialActivityLayoutProduct", layoutProductId);
    }
    
    @Override
    public int insertSpecialActivityLayoutProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("SpecialActivityMapper.insertSpecialActivityLayoutProduct", para);
    }
    
    @Override
    public int findMaxSpecialActivityLayoutProductSequenceByActivityId(int layoutId)
        throws Exception
    {
        Integer sequence = this.getSqlSession().selectOne("SpecialActivityMapper.findMaxSpecialActivityLayoutProductSequenceByActivityId", layoutId);
        return sequence == null ? 1 : sequence.intValue() + 1;
    }

    @Override public List<Map<String, Object>> findSpecialActivityLayouProductBySpecialActivityId(int SpecialActivityId)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("SpecialActivityMapper.findSpecialActivityLayouProductBySpecialActivityId",SpecialActivityId);
    }
}
