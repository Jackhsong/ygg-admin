package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.LogisticsTimeoutDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.LogisticsTimeoutEntity;
import com.ygg.admin.entity.OrderDetailInfoForSeller;

@Repository
public class LogisticsTimeoutDaoImpl extends BaseDaoImpl implements LogisticsTimeoutDao
{
    
    @Override
    public List<Map<String, Object>> findAllLogisticsTimeoutOrders(Map<String, Object> para)
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findAllLogisticsTimeoutOrders", para);
    }
    
    @Override
    public int countLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.countLogisticsTimeoutOrders", para);
    }
    
    @Override
    public List<Map<String, Object>> findLogisticsTimeoutOrders(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findLogisticsTimeoutOrders", para);
    }
    
    @Override
    public String findLogisticsTimeoutReasonById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.findLogisticsTimeoutReasonById", id);
    }
    
    @Override
    public int countLogisticsTimeoutOrders2(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.countLogisticsTimeoutOrders2", para);
    }
    
    @Override
    public List<Map<String, Object>> findLogisticsTimeoutOrders2(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findLogisticsTimeoutOrders2", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllLogisticsTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findAllLogisticsTimeoutReason", para);
    }
    
    @Override
    public int countLogisticsTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.countLogisticsTimeoutReason", para);
    }
    
    @Override
    public int insertTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("LogisticsTimeoutMapper.insertTimeoutReason", para);
    }
    
    @Override
    public int updateTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("LogisticsTimeoutMapper.updateTimeoutReason", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllComplainOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findAllComplainOrder", para);
    }
    
    @Override
    public int countComplainOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.countComplainOrder", para);
    }
    
    @Override
    public Map<String, Object> findLogisticsTimeoutByOid(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.findLogisticsTimeoutByOid", orderId);
    }
    
    @Override
    public List<Map<String, Object>> findLogisticsTimeoutComplainListByOrderId(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findLogisticsTimeoutComplainListByOrderId", orderId);
    }
    
    @Override
    public List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("LogisticsTimeoutMapper.findAllTimeoutReason", para);
    }
    
    @Override
    public Map<String, Object> findRecentLogisticsTimeoutComplainByOrderId(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.findRecentLogisticsTimeoutComplainByOrderId", orderId);
    }
    
    @Override
    public int updateLogisticsTimeoutComplain(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("LogisticsTimeoutMapper.updateLogisticsTimeoutComplain", para);
    }
    
    @Override
    public int updateLogisticsTimeout(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("LogisticsTimeoutMapper.updateLogisticsTimeout", para);
    }
    
    @Override
    public int insertLogisticsTimeoutComplain(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("LogisticsTimeoutMapper.insertLogisticsTimeoutComplain", para);
    }
    
    @Override
    public int addTimeoutOrderQcDeal(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("LogisticsTimeoutMapper.addTimeoutOrderQcDeal", para);
    }
    
    @Override
    public LogisticsTimeoutEntity findLogisticsTimeoutByOrderId(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.findLogisticsTimeoutByOrderId", orderId);
    }
    
    @Override
    public List<Map<String, Object>> findOrderLogisticsTimeoutQcListByOid(int orderId)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findOrderLogisticsTimeoutQcListByOid", orderId);
    }
    
    @Override
    public List<Map<String, Object>> findOrderLogisticsTimeoutQcListByOids(List<Integer> orderIdList)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findOrderLogisticsTimeoutQcListByOids", orderIdList);
    }
    
    @Override
    public int batchAddTimeoutOrderQcDeal(List<Map<String, Object>> params)
        throws Exception
    {
        return getSqlSession().insert("LogisticsTimeoutMapper.batchAddTimeoutOrderQcDeal", params);
    }
    
    @Override
    public int countTimeoutOrderDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("LogisticsTimeoutMapper.countTimeoutOrderDetail", para);
    }
    
    @Override
    public List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("LogisticsTimeoutMapper.findAllTimeoutOrderDetail", para);
    }
}
