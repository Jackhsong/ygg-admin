package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SpecialGroupActivityDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository
public class SpecialGroupActivityDaoImpl extends BaseDaoImpl implements SpecialGroupActivityDao
{
    
    @Override
    public List<Map<String, Object>> findAllSpecialGroupActivity(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SpecialGroupActivityMapper.findAllSpecialGroupActivity", para);
    }
    
    @Override
    public int countSpecialGroupActivity(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SpecialGroupActivityMapper.countSpecialGroupActivity", para);
    }
    
    @Override
    public int saveSpecialGroup(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SpecialGroupActivityMapper.saveSpecialGroup", para);
    }
    
    @Override
    public int updateSpecialGroup(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SpecialGroupActivityMapper.updateSpecialGroup", para);
    }
    
    @Override
    public List<Map<String, Object>> findSpecialGroupActivityProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SpecialGroupActivityMapper.findSpecialGroupActivityProductByPara", para);
    }
    
    @Override
    public int countSpecialGroupActivityProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SpecialGroupActivityMapper.countSpecialGroupActivityProduct", para);
    }
    
    @Override
    public int insertSpecialGroupActivityProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SpecialGroupActivityMapper.insertSpecialGroupActivityProduct", para);
    }
    
    @Override
    public int updateSpecialGroupActivityProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SpecialGroupActivityMapper.updateSpecialGroupActivityProduct", para);
    }
    
    @Override
    public int deleteSpecialGroupActivityProduct(int id)
        throws Exception
    {
        return getSqlSession().delete("SpecialGroupActivityMapper.deleteSpecialGroupActivityProduct", id);
    }
}
