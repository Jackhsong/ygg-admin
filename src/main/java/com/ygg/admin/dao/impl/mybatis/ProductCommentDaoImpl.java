package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ProductCommentDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OrderProductCommentEntity;

@Repository("productCommentDao")
public class ProductCommentDaoImpl extends BaseDaoImpl implements ProductCommentDao
{
    
    @Override
    public List<Map<String, Object>> findAllProductComment(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("ProductCommentMapper.findAllProductComment", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countProductComment(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductCommentMapper.countProductComment", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllProductBaseComment(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ProductCommentMapper.findAllProductBaseComment", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countProductBaseComment(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductCommentMapper.countProductBaseComment", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllProductBaseCommentLevel(int level)
        throws Exception
    {
        List<Map<String, Object>> list = this.getSqlSessionRead().selectList("ProductCommentMapper.findAllProductBaseCommentLevel", level);
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public int countProductCommentDetail(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductCommentMapper.countProductCommentDetail", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllProductCommentDetail(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> list = this.getSqlSessionRead().selectList("ProductCommentMapper.findAllProductCommentDetail", para);
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public OrderProductCommentEntity findProductCommentById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductCommentMapper.findProductCommentById", id);
    }
    
    @Override
    public int updateProductCommentDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductCommentMapper.updateProductCommentDisplayStatus", para);
    }
    
    @Override
    public int replayProductComment(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductCommentMapper.replayProductComment", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllProductBaseTotalComment()
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ProductCommentMapper.findAllProductBaseTotalComment");
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }

    @Override
    public int updateDealContent(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductCommentMapper.updateDealContent", para);
    }

    @Override
    public int updateProductCommentDisplayTextStatus(Map<String, Object> para)
            throws Exception
    {
        return this.getSqlSession().update("ProductCommentMapper.updateProductCommentDisplayTextStatus", para);
    }

    @Override
    public List<OrderProductCommentEntity> findProCommentsByIds(
            Map<String, Object> para) throws Exception
    {
        return this.getSqlSessionRead().selectList("ProductCommentMapper.findProCommentsByIds",para);
    }

    @Override
    public int findProductBaseCommentByParam(Map<String, Object> param) throws Exception {
        return getSqlSessionRead().selectOne("ProductCommentMapper.findProductBaseCommentByParam", param);
    }
}
