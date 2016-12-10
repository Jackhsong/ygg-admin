package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.TimeoutOrderDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OrderDetailInfoForSeller;

@Repository
public class TimeoutOrderDaoImpl extends BaseDaoImpl implements TimeoutOrderDao
{
    
    @Override
    public List<Map<String, Object>> findTimeOutOrderInfoByOidList(List<Integer> orderIdList)
        throws Exception
    {
        return getSqlSession().selectList("TimeOutOrderMapper.findTimeOutOrderInfoByOidList", orderIdList);
    }
    
    @Override
    public int insertOrderTimeoutComplain(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("TimeOutOrderMapper.insertOrderTimeoutComplain", para);
    }
    
    @Override
    public int insertOrderTimeoutComplainResult(int orderId)
        throws Exception
    {
        return getSqlSession().insert("TimeOutOrderMapper.insertOrderTimeoutComplainResult", orderId);
    }
    
    @Override
    public int updateOrderTimeoutComplainResult(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TimeOutOrderMapper.updateOrderTimeoutComplainResult", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllComplainOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("TimeOutOrderMapper.findAllComplainOrder", para);
    }
    
    @Override
    public int countComplainOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("TimeOutOrderMapper.countComplainOrder", para);
    }
    
    @Override
    public String findOrderTimeoutReasonName(int id)
        throws Exception
    {
        String name = getSqlSession().selectOne("TimeOutOrderMapper.findOrderTimeoutReasonName", id);
        return name == null ? "" : name;
    }
    
    @Override
    public List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("TimeOutOrderMapper.findAllTimeoutReason", para);
    }
    
    @Override
    public int countTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("TimeOutOrderMapper.countTimeoutReason", para);
    }
    
    @Override
    public Map<String, Object> findRecentOrderTimeoutComplainByOrderId(int orderId)
        throws Exception
    {
        return getSqlSession().selectOne("TimeOutOrderMapper.findRecentOrderTimeoutComplainByOrderId", orderId);
    }
    
    @Override
    public int updateOrderTimeoutComplain(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TimeOutOrderMapper.updateOrderTimeoutComplain", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderTimeoutComplainListByOrderId(int orderId)
        throws Exception
    {
        return getSqlSession().selectList("TimeOutOrderMapper.findOrderTimeoutComplainListByOrderId", orderId);
    }
    
    @Override
    public Map<String, Object> findOrderTimeoutComplainResultByOId(int orderId)
        throws Exception
    {
        return getSqlSession().selectOne("TimeOutOrderMapper.findOrderTimeoutComplainResultByOId", orderId);
    }
    
    @Override
    public int insertTimeoutReason(String name)
        throws Exception
    {
        return getSqlSession().insert("TimeOutOrderMapper.insertTimeoutReason", name);
    }
    
    @Override
    public int updateTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("TimeOutOrderMapper.updateTimeoutReason", para);
    }
    
    @Override
    public int addTimeoutOrderQcDeal(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("TimeOutOrderMapper.addTimeoutOrderQcDeal", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderTimeoutQcDealListByOid(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectList("TimeOutOrderMapper.findOrderTimeoutQcDealListByOid", orderId);
    }
    
    @Override
    public List<Map<String, Object>> findOrderTimeoutQcDealListByOids(List<Integer> orderIdList)
        throws Exception
    {
        return getSqlSessionRead().selectList("TimeOutOrderMapper.findOrderTimeoutQcDealListByOids", orderIdList);
    }
    
    @Override
    public int batchAddTimeoutOrderQcDeal(List<Map<String, Object>> params)
        throws Exception
    {
        return getSqlSession().insert("TimeOutOrderMapper.batchAddTimeoutOrderQcDeal", params);
    }
    
    @Override
    public List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("TimeOutOrderMapper.findAllTimeoutOrderDetail", para);
    }
    
    @Override
    public int countTimeoutOrderDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("TimeOutOrderMapper.countTimeoutOrderDetail", para);
    }
}
