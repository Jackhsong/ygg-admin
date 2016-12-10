package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.OrderManualDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OrderManualEntity;

@Repository("orderManualDao")
public class OrderManualDaoImpl extends BaseDaoImpl implements OrderManualDao
{
    
    @Override
    public int insertOrderManual(OrderManualEntity entity)
        throws Exception
    {
        return getSqlSessionAdmin().insert("OrderManualMapper.saveOrderManual", entity);
    }
    
    @Override
    public int insertOrderManualProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("OrderManualMapper.saveOrderManualProduct", para);
    }
    
    @Override
    public List<OrderManualEntity> findAllOrderManual(Map<String, Object> para)
        throws Exception
    {
        List<OrderManualEntity> reList = getSqlSessionAdmin().selectList("OrderManualMapper.findAllOrderManual", para);
        return reList == null ? new ArrayList<OrderManualEntity>() : reList;
    }
    
    @Override
    public int countOrderManual(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("OrderManualMapper.countOrderManual", para);
    }
    
    @Override
    public int updateOrderManual(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().update("OrderManualMapper.updateOrderManual", para);
    }
    
    @Override
    public OrderManualEntity findOrderManualById(int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        List<OrderManualEntity> reList = findAllOrderManual(map);
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
    public List<Map<String, Object>> findAllProductInfoByOrderManualId(int manualId)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("OrderManualMapper.findAllProductInfoByOrderManualId", manualId);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findAllOrderManualAndProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("OrderManualMapper.findAllOrderManualAndProduct", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int findOrderManualIdByNumber(long number)
        throws Exception
    {
        Integer id = getSqlSessionAdmin().selectOne("OrderManualMapper.findOrderManualIdByNumber", number);
        return id == null ? -1 : id;
    }
    
    @Override
    public Map<String, Object> findOrderManualSettlementByNumber(long number)
        throws Exception
    {
        int orderManualId = findOrderManualIdByNumber(number);
        if (orderManualId == -1)
        {
            return null;
        }
        return getSqlSessionAdmin().selectOne("OrderManualMapper.findOrderManualSettlementByOrderManualId", orderManualId);
    }
    
    @Override
    public int insertOrderManualSettlement(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("OrderManualMapper.insertOrderManualSettlement", para);
    }
    
    @Override
    public int deleteOrderManualSettlement(int orderManualId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("OrderManualMapper.deleteOrderManualSettlement", orderManualId);
    }
    
    @Override
    public int updateOrderProductCost(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("OrderManualMapper.updateOrderProductCost", para);
    }
    
    @Override
    public int addOverseasManualProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("OrderManualMapper.addOverseasManualProduct", para);
    }
    
    @Override
    public List<Map<String, Object>> findOverseasManualProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("OrderManualMapper.findOverseasManualProduct", para);
    }
    
    @Override
    public int countOverseasManualProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("OrderManualMapper.countOverseasManualProduct", para);
    }
    
}
