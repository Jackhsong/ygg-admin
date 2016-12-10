package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomActivityEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("customActivitiesDao")
public class CustomActivitiesDaoImpl extends BaseDaoImpl implements CustomActivitiesDao
{
    
    @Override
    public List<Map<String, Object>> findCustomActivitiesInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CustomActivitiesMapper.findCustomActivitiesInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }

    @Override
    public Map<String, Object> findCustomActivitiesById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CustomActivitiesMapper.findCustomActivitiesInfo", para);
        if (result == null || result.size() == 0)
            return null;
        return result.get(0);
    }
    
    @Override
    public int countCustomActivitiesInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CustomActivitiesMapper.countCustomActivitiesInfo", para);
    }
    
    @Override
    public int updateCustomActivitiesStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomActivitiesMapper.updateCustomActivitiesStatus", para);
    }
    
    @Override
    public int add(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CustomActivitiesMapper.add", para);
    }
    
    @Override
    public int update(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CustomActivitiesMapper.update", para);
    }
    
    @Override
    public CustomActivityEntity findCustomActivitiesId(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomActivitiesMapper.findCustomActivitiesId", id);
    }
}
