package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SellerAdminOrderDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository
public class SellerAdminOrderDaoImpl extends BaseDaoImpl implements SellerAdminOrderDao
{
    
    @Override
    public List<Map<String, Object>> findSellerOrderList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerAdminOrderMapper.findSellerOrderList", para);
    }
    
    @Override
    public List<Map<String, Object>> findSellerOrderQuestionInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerAdminOrderMapper.findSellerOrderQuestionInfo", para);
    }
    
    @Override
    public int countSellerOrderQuestionInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerAdminOrderMapper.countSellerOrderQuestionInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findSellerLogisticsOrderList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerAdminOrderMapper.findSellerLogisticsOrderList", para);
    }
    
    @Override
    public List<Map<String, Object>> findSendTimeoutComplainOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerAdminOrderMapper.findSendTimeoutComplainOrder", para);
    }
    
    @Override
    public int countSendTimeoutComplainOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerAdminOrderMapper.countSendTimeoutComplainOrder", para);
    }
}
