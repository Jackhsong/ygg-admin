package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.RefundReasonDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("refundReasonDao")
public class RefundReasonDaoImpl extends BaseDaoImpl implements RefundReasonDao
{

    @Override
    public int updateRefundReason(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("RefundReasonMapper.updateRefundReason", param);
    }

    @Override
    public List<Map<String, Object>> findRefundReasonList(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("RefundReasonMapper.findRefundReasonList", param);
    }

    @Override
    public int countRefundReason()
        throws Exception
    {
        return getSqlSessionRead().selectOne("RefundReasonMapper.countRefundReason");
    }

    @Override
    public int saveRefundReason(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("RefundReasonMapper.saveRefundReason", param);
    }
    
    @Override
    public Map<String, Object> findRefundReasonById(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("RefundReasonMapper.findRefundReasonList", param);
    }
}
