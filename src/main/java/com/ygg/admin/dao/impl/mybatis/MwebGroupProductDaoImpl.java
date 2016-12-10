package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.MwebGroupProductDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupProductEntity;
import com.ygg.admin.entity.ProductEntity;

@Repository("mwebGroupProductDao")
public class MwebGroupProductDaoImpl extends BaseDaoImpl implements MwebGroupProductDao
{
    
    @Override
    public List<JSONObject> findProductAndStock(Map<String, Object> parameter)
    {
        
        List<JSONObject> list = getSqlSessionRead().selectList("MwebGroupProductMapper.findProductAndStock", parameter);
        return list == null ? new ArrayList<JSONObject>() : list;
    }
    
    @Override
    public int countProductAndStock(Map<String, Object> parameter)
    {
        
        return getSqlSessionRead().selectOne("MwebGroupProductMapper.countProductAndStock", parameter);
    }
    
    @Override
    public int addProduct(MwebGroupProductEntity mwebGroupProductEntity)
    {
        
        return getSqlSession().insert("MwebGroupProductMapper.addProduct", mwebGroupProductEntity);
    }
    
    @Override
    public List<MwebGroupProductEntity> findProduct(MwebGroupProductEntity mwebGroupProductEntity)
    {
        // TODO Auto-generated method stub
        return getSqlSessionRead().selectList("MwebGroupProductMapper.findProduct", mwebGroupProductEntity);
    }
    
    @Override
    public int updateProduct(MwebGroupProductEntity mwebGroupProductEntity)
    {
        // TODO Auto-generated method stub
        return getSqlSession().update("MwebGroupProductMapper.updateProduct", mwebGroupProductEntity);
    }
    
    @Override
    public int forSale(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupProductMapper.updateIsOffShelves", para);
    }
    
    @Override
    public int addProductEntity(ProductEntity productEntity)
    {
        // TODO Auto-generated method stub
        return getSqlSession().insert("MwebGroupProductMapper.addProductEntity", productEntity);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        
        return this.getSqlSession().update("MwebGroupProductMapper.updateOrder", para);
    }
    
    @Override
    public int updateRelationProductByPara(Map<String, Object> map)
        throws Exception
    {
        
        return this.getSqlSession().update("MwebGroupProductMapper.updateRelationProductByPara", map);
    }
    
    @Override
    public List<JSONObject> findProductAndStockForTeam(Map<String, Object> parameter)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupProductMapper.findProductAndStockForTeam", parameter);
    }
    
    @Override
    public int countProductAndStockForTeam(Map<String, Object> parameter)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("MwebGroupProductMapper.countProductAndStockForTeam", parameter);
    }
    
    @Override
    public int updateProductForTeam(Map<String, Object> parameter)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupProductMapper.updateProductForTeam", parameter);
    }
    
    @Override
    public MwebGroupProductEntity getProduct(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("MwebGroupProductMapper.getProduct", id);
    }
    
    @Override
    public int updateProductEntity(ProductEntity productEntity)
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().update("MwebGroupProductMapper.updateProductEntity", productEntity);
    }
    
    @Override
    public MwebGroupProductEntity getProductByProductId(int id)
        throws Exception
    {
        // TODO Auto-generated method stub
        return this.getSqlSession().selectOne("MwebGroupProductMapper.getProductByProductId", id);
    }
    
}
