package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CustomerProblemDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository
public class CustomerProblemDaoImpl extends BaseDaoImpl implements CustomerProblemDao
{
    
    @Override
    public List<Map<String, Object>> findAllCustomerProblem(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CustomerProblemMapper.findAllCustomerProblem", para);
    }
    
    @Override
    public int countCustomerProblem(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomerProblemMapper.countCustomerProblem", para);
    }
    
    @Override
    public int saveCustomerProblem(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CustomerProblemMapper.saveCustomerProblem", para);
    }
    
    @Override
    public int updateCustomerProblem(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CustomerProblemMapper.updateCustomerProblem", para);
    }
    
}
