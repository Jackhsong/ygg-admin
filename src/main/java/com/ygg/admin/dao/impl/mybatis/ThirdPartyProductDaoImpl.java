package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ThirdPartyProductDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ThirdPartyProductEntity;

@Repository
public class ThirdPartyProductDaoImpl extends BaseDaoImpl implements ThirdPartyProductDao
{
    
    @Override
    public List<Map<String, Object>> findAllThirdPartyProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ThirdPartyProductMapper.findAllThirdPartyProduct", para);
    }
    
    @Override
    public int countThirdPartyProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ThirdPartyProductMapper.countThirdPartyProduct", para);
    }
    
    @Override
    public int saveThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception
    {
        return getSqlSession().insert("ThirdPartyProductMapper.saveThirdPartyProduct", product);
    }
    
    @Override
    public int updateThirdPartyProduct(ThirdPartyProductEntity product)
        throws Exception
    {
        return getSqlSession().update("ThirdPartyProductMapper.updateThirdPartyProduct", product);
    }
    
    @Override
    public int batchSaveThirdPartyProduct(List<ThirdPartyProductEntity> productList)
        throws Exception
    {
        return getSqlSession().insert("ThirdPartyProductMapper.batchSaveThirdPartyProduct", productList);
    }
    
    @Override
    public int updateThirdPartyProductStatus(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("ThirdPartyProductMapper.updateThirdPartyProductStatus", param);
    }
    
    @Override
    public int updateThirdPartyProductSales(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ThirdPartyProductMapper.updateThirdPartyProductSales", para);
    }
    
    @Override
    public int updateThirdPartyProductStock(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ThirdPartyProductMapper.updateThirdPartyProductStock", para);
    }
}
