package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SpecialActivityModelDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("specialActivityModelDao")
public class SpecialActivityModelDaoImpl extends BaseDaoImpl implements SpecialActivityModelDao
{

    @Override
    public List<Map<String, Object>> findListByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("SpecialActivityModelMapper.findListByParam", param);
    }

    @Override
    public int countByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SpecialActivityModelMapper.countByParam", param);
    }

    @Override
    public int updateByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("SpecialActivityModelMapper.updateByParam", param);
    }

    @Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("SpecialActivityModelMapper.save", param);
    }

    @Override
    public Map<String, Object> findById(String id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return getSqlSessionRead().selectOne("SpecialActivityModelMapper.findListByParam", param);
    }
    
}
