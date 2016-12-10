package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ProductBlacklistDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("productBlacklistDao")
public class ProductBlacklistDaoImpl extends BaseDaoImpl implements ProductBlacklistDao
{
    
    @Override
    public List<Map<String, Object>> findAllProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductBlacklistMapper.findAllProduct", para);
    }
    
    @Override
    public int countAllProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductBlacklistMapper.countAllProduct", para);
    }
    
    @Override
    public int add(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("ProductBlacklistMapper.add", para);
    }
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().delete("ProductBlacklistMapper.delete", para);
    }
    
    @Override
    public boolean exist(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = findAllProduct(para);
        if (reList != null && reList.size() > 0)
        {
            return true;
        }
        return false;
    }
    
    @Override
    public List<Map<String, Object>> findReduceOrderId()
        throws Exception
    {
        return getSqlSession().selectList("ProductBlacklistMapper.findReduceOrderId");
    }
    
}
