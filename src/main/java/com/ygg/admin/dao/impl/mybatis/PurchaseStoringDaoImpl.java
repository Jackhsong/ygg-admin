package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.PurchaseStoringDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("purchaseStoringDao")
public class PurchaseStoringDaoImpl extends BaseDaoImpl implements PurchaseStoringDao
{
    
    @Override
    public List<Map<String, Object>> findPurchaseStoringByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseStoringMapper.findPurchaseStoringByParam", param);
    }
    
    @Override
    public List<Map<String, Object>> findPurchaseStoringByIds(List<Object> list)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseStoringMapper.findPurchaseStoringByIds", list);
    }
    
    @Override
    public int updatePurchaseStoringByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().update("PurchaseStoringMapper.updatePurchaseStoringByParam", param);
    }
    
    @Override
    public List<Map<String, Object>> findUnpushOrder()
        throws Exception
    {
        return getSqlSession().selectList("PurchaseStoringMapper.findUnpushOrder");
    }
    
    @Override
    public List<Map<String, Object>> findProviderProduct(List<Object> list)
        throws Exception
    {
        return getSqlSession().selectList("PurchaseStoringMapper.findProviderProduct", list);
    }
    
}
