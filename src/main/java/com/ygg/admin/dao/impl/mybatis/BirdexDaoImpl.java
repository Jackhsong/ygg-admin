package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.BirdexDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository
public class BirdexDaoImpl extends BaseDaoImpl implements BirdexDao
{
    
    @Override
    public List<Map<String, Object>> findAllBirdexProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("BirdexMapper.findAllBirdexProduct", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countBirdexProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("BirdexMapper.countBirdexProduct", para);
    }
    
    @Override
    public int saveBirdexProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("BirdexMapper.saveBirdexProduct", para);
    }
    
    @Override
    public int updateBirdexProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("BirdexMapper.updateBirdexProduct", para);
    }
    
    @Override
    public int deleteBirdexProduct(int id)
        throws Exception
    {
        return getSqlSession().delete("BirdexMapper.deleteBirdexProduct", id);
    }
    
    @Override
    public int countBirdexStock(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("BirdexMapper.countBirdexStock", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllBirdexStock(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("BirdexMapper.findAllBirdexStock", para);
    }
    
    @Override
    public int countBirdexCancelOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("BirdexMapper.countBirdexCancelOrder", para);
    }
    
    @Override
    public List<Map<String, Object>> findBirdexCancelOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("BirdexMapper.findBirdexCancelOrder", para);
    }
    
    @Override
    public int updateBirdexOrderConfirmPushStatus(int orderId)
        throws Exception
    {
        return getSqlSession().update("BirdexMapper.updateBirdexOrderConfirmPushStatus", orderId);
    }
}
