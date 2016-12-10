package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.PageCustomDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.PageCustomEntity;
import com.ygg.admin.entity.RelationProductAndPageCustom;

@Repository("pageCustomDao")
public class PageCustomDaoImpl extends BaseDaoImpl implements PageCustomDao
{
    
    @Override
    public List<PageCustomEntity> findAllPageCustomForProduct()
        throws Exception
    {
        List<PageCustomEntity> reList = getSqlSession().selectList("PageCustomMapper.findAllPageCustomForProduct");
        return reList == null ? new ArrayList<PageCustomEntity>() : reList;
    }
    
    @Override
    public int saveRelationProductAndPageCustom(RelationProductAndPageCustom para)
        throws Exception
    {
        return getSqlSession().insert("PageCustomMapper.saveRelationProductAndPageCustom", para);
    }
    
    @Override
    public int updateRelationProductAndPageCustom(RelationProductAndPageCustom para)
        throws Exception
    {
        return getSqlSession().update("PageCustomMapper.updateRelationProductAndPageCustomById", para);
    }
    
    @Override
    public int deleteRelationProductAndPageCustomById(int id)
        throws Exception
    {
        return getSqlSession().delete("PageCustomMapper.deleteRelationProductAndPageCustomById", id);
    }
    
    @Override
    public List<PageCustomEntity> findAllPageCustomByPara(Map<String, Object> para)
        throws Exception
    {
        List<PageCustomEntity> reList = getSqlSession().selectList("PageCustomMapper.findAllPageCustomByPara", para);
        return reList == null ? new ArrayList<PageCustomEntity>() : reList;
    }
    
    @Override
    public PageCustomEntity findPageCustomByid(int id)
        throws Exception
    {
        return getSqlSession().selectOne("PageCustomMapper.findPageCustomById", id);
    }
    
    @Override
    public List<RelationProductAndPageCustom> findRelationProductAndPageCustom(Map<String, Object> para)
        throws Exception
    {
        List<RelationProductAndPageCustom> reList = getSqlSession().selectList("PageCustomMapper.findRelationProductAndPageCustom", para);
        return reList == null ? new ArrayList<RelationProductAndPageCustom>() : reList;
    }
    
    @Override
    public int save(PageCustomEntity entity)
        throws Exception
    {
        return this.getSqlSession().insert("PageCustomMapper.save", entity);
    }
    
    @Override
    public int update(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("PageCustomMapper.update", para);
    }
    
    @Override
    public int countPageCustom(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("PageCustomMapper.countPageCustom", para);
    }
    
}
