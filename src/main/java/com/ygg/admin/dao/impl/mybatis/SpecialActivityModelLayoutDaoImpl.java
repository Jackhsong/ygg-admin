package com.ygg.admin.dao.impl.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SpecialActivityModelLayoutDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("specialActivityModelLayoutDao")
public class SpecialActivityModelLayoutDaoImpl extends BaseDaoImpl implements SpecialActivityModelLayoutDao
{

    @Override
    public List<Map<String, Object>> findListByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("SpecialActivityModelLayoutMapper.findListByParam", param);
    }

    @Override
    public int countByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SpecialActivityModelLayoutMapper.countByParam", param);
    }

    @Override
    public int updateByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("SpecialActivityModelLayoutMapper.updateByParam", param);
    }

    @Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("SpecialActivityModelLayoutMapper.save", param);
    }

    @Override
    public Map<String, Object> findById(String id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return getSqlSessionRead().selectOne("SpecialActivityModelLayoutMapper.findListByParam", param);
    }
    
}
