package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.OrderProductRefundReasonDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xiongl
 * @create 2016-05-18 15:42
 */
@Repository
public class OrderProductRefundReasonDaoImpl extends BaseDaoImpl implements OrderProductRefundReasonDao
{
    @Override
    public int countOrderProductRefundReason(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("OrderProductRefundReasonMapper.countOrderProductRefundReason", param);
    }
    
    @Override
    public List<Map<String, Object>> findOrderProductRefundReasonList(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("OrderProductRefundReasonMapper.findOrderProductRefundReasonList", param);
    }
    
    @Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("OrderProductRefundReasonMapper.save", param);
    }
    
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("OrderProductRefundReasonMapper.update", param);
    }

    @Override
    public int delete(int id) throws Exception {
        return getSqlSession().delete("OrderProductRefundReasonMapper.delete", id);
    }
}
