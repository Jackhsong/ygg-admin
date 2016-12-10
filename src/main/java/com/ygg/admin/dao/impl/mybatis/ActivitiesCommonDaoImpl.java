package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ProductInfoForGroupSale;
import com.ygg.admin.entity.RelationActivitiesCommonAndProduct;
import com.ygg.admin.exception.DaoException;

@Repository("activitiesCommonDao")
public class ActivitiesCommonDaoImpl extends BaseDaoImpl implements ActivitiesCommonDao
{
    
    @Override
    public List<ActivitiesCommonEntity> findAllAcCommonByPara(Map<String, Object> para)
        throws Exception
    {
        List<ActivitiesCommonEntity> reList = getSqlSession().selectList("ActivitiesCommonMapper.findActivitiesCommon", para);
        return reList == null ? new ArrayList<ActivitiesCommonEntity>() : reList;
    }
    
    @Override
    public ActivitiesCommonEntity findAcCommonById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<ActivitiesCommonEntity> reList = findAllAcCommonByPara(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int countAllAcCommonByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("ActivitiesCommonMapper.countActivitiesCommon", para);
    }
    
    @Override
    public int save(ActivitiesCommonEntity entity)
        throws Exception
    {
        return getSqlSession().insert("ActivitiesCommonMapper.save", entity);
    }
    
    @Override
    public int update(ActivitiesCommonEntity entity)
        throws Exception
    {
        return getSqlSession().update("ActivitiesCommonMapper.update", entity);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ActivitiesCommonMapper.updateOrder", para);
    }
    
    @Override
    public List<ProductInfoForGroupSale> findProductInfoForGroupSaleByActivitiesCommonId(Map<String, Object> para)
        throws Exception
    {
        List<ProductInfoForGroupSale> reList = getSqlSession().selectList("ProductMapper.findProductInfoForGroupSaleById", para);
        return reList == null ? new ArrayList<ProductInfoForGroupSale>() : reList;
    }
    
    @Override
    public int countProductNumByActivitiesCommonId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("ActivitiesCommonMapper.countProductNumByActivitiesCommonId", id);
    }
    
    @Override
    public List<Integer> findAllProductIdByActivitiesCommonId(int id)
        throws DaoException
    {
        List<Integer> reList = getSqlSession().selectList("ActivitiesCommonMapper.findAllProductIdByActivitiesCommonId", id);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public List<ProductInfoForGroupSale> findProductInfoForGroupSale(Map<String, Object> para)
        throws Exception
    {
        List<ProductInfoForGroupSale> reList = getSqlSession().selectList("ProductMapper.findAllProductInfoForGroupSale", para);
        return reList == null ? new ArrayList<ProductInfoForGroupSale>() : reList;
    }
    
    @Override
    public int countProductInfoForGroupSale(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("ProductMapper.countProductInfoForGroupSale", para);
    }
    
    @Override
    public int deleteRelationActivitiesCommonAndProduct(int id)
        throws Exception
    {
        return this.getSqlSession().delete("ActivitiesCommonMapper.deleteRelationActivitiesCommonAndProduct", id);
    }
    
    @Override
    public int findMaxOrderByActivitiesCommonId(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("ActivitiesCommonMapper.findMaxOrderByActivitiesCommonId", id);
    }
    
    @Override
    public int saveRelationActivitiesCommonAndProduct(RelationActivitiesCommonAndProduct para)
        throws Exception
    {
        return this.getSqlSession().insert("ActivitiesCommonMapper.saveRelationActivitiesCommonAndProduct", para);
    }
    
    @Override
    public int updateProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ActivitiesCommonMapper.updateProductDisplayStatus", para);
    }
    
    @Override
    public int batchUpdateGroupProductTime(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ActivitiesCommonMapper.batchUpdateGroupProductTime", para);
    }
    
    @Override
    public ActivitiesCommonEntity findActivitiesCommonById(int id)
        throws Exception
    {
        return getSqlSession().selectOne("ActivitiesCommonMapper.findActivitiesCommonById", id);
    }
    
    @Override
    public List<Integer> findProductIdByActivitiesCommonId(int id)
        throws Exception
    {
        return getSqlSession().selectList("ActivitiesCommonMapper.findProductIdByActivitiesCommonId", id);
    }
    
    @Override
    public int countProductsByActivityCommonId(int activityCommonId)
    {
        return getSqlSession().selectOne("ActivitiesCommonMapper.countProductsByActivityCommonId", activityCommonId);
        
    }
    
    @Override
    public List<Map<String, Object>> findProductsByActivityCommonId(Map<String, Object> params)
    {
        return getSqlSession().selectList("ActivitiesCommonMapper.findProductsByActivityCommonId", params);
        
    }
}
