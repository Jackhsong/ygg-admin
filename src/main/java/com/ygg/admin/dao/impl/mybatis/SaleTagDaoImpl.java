package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SaleTagDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SaleTagEntity;

@Repository("saleTagDao")
public class SaleTagDaoImpl extends BaseDaoImpl implements SaleTagDao
{
    
    @Override
    public SaleTagEntity findSaleTagById(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("SaleTagMapper.findSaleTagById", id);
    }
    
    @Override
    public int save(SaleTagEntity entity)
        throws Exception
    {
        return this.getSqlSession().insert("SaleTagMapper.save", entity);
    }
    
    @Override
    public int update(SaleTagEntity entity)
        throws Exception
    {
        return this.getSqlSession().update("SaleTagMapper.update", entity);
    }
    
    @Override
    public List<SaleTagEntity> findAllSaleTag(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectList("SaleTagMapper.findAllSaleTag", para);
    }
    
    @Override
    public int countSaleTag(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("SaleTagMapper.countSaleTag", para);
    }
    
    @Override
    public int saveRelation(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("SaleTagMapper.saveRelation", para);
    }
    
    @Override
    public int deleteAllRelationBySaleWindowId(int id)
        throws Exception
    {
        return this.getSqlSession().delete("SaleTagMapper.deleteAllRelationBySaleWindowId", id);
    }
    
    @Override
    public List<Integer> findTagIdsBySaleWindowId(int id)
        throws Exception
    {
        return this.getSqlSession().selectList("SaleTagMapper.selectTagIdsBySaleWindowId", id);
    }
    
}
