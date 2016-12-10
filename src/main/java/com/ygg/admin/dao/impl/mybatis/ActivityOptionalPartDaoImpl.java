package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.ActivityOptionalPartDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ActivitiesOptionalPartEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ActivityOptionalPartDaoImpl extends BaseDaoImpl implements ActivityOptionalPartDao
{
    
    @Override
    public List<ActivitiesOptionalPartEntity> findAllActivityOptionalPart(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ActivityOptionalPartMapper.findAllActivityOptionalPart", para);
    }
    
    @Override
    public int countActivityOptionalPart(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ActivityOptionalPartMapper.countAllActivityOptionalPart", para);
    }
    
    @Override
    public int saveActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception
    {
        return getSqlSession().insert("ActivityOptionalPartMapper.saveActivityOptionalPart", activity);
    }
    
    @Override
    public int updateActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception
    {
        return getSqlSession().update("ActivityOptionalPartMapper.updateActivityOptionalPart", activity);
    }
    
    @Override
    public ActivitiesOptionalPartEntity findActivityOptionalPartById(int id)
        throws Exception
    {
        Map<String,Object> para = new HashMap<>();
        para.put("id" ,id);
        List<ActivitiesOptionalPartEntity> result = findAllActivityOptionalPart(para);
        return result.size() > 0 ? result.get(0) : null;
    }
}
