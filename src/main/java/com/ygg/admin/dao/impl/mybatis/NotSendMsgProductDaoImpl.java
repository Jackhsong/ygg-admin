package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.NotSendMsgProductDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("notSendMsgDao")
public class NotSendMsgProductDaoImpl extends BaseDaoImpl implements NotSendMsgProductDao
{
    
    @Override
    public List<Map<String, Object>> queryAllProductId()
        throws Exception
    {
        return getSqlSessionAdmin().selectList("NotSendMsgProductMapper.queryAllProductId");
    }
    
    @Override
    public int add(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("NotSendMsgProductMapper.add", para);
    }
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().delete("NotSendMsgProductMapper.delete", para);
    }
    
    @Override
    public boolean checkIsExist(String productId)
        throws Exception
    {
        int count = getSqlSessionAdmin().selectOne("NotSendMsgProductMapper.checkIsExist", productId);
        return count > 0;
    }
    
    @Override
    public List<Integer> findProductById(List<Integer> orderForProductIdList)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("NotSendMsgProductMapper.findProductById", orderForProductIdList);
    }
    
}
