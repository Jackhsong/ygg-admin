package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.GroupBuyDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.GroupProductCodeEntity;

@Repository("groupBuyDao")
public class GroupBuyDaoImpl extends BaseDaoImpl implements GroupBuyDao
{
    
    @Override
    public List<GroupProductCodeEntity> findAllGroupProductCodeByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("GroupBuyMapper.findAllGroupProductCodeByPara", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllGourpProductNums(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("GroupBuyMapper.findAllGourpProductNums", para);
    }
    
    @Override
    public int countAllGourpProductNums(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("GroupBuyMapper.countAllGourpProductNums", para);
    }
    
    @Override
    public List<Map<String, Object>> findGroupTotalPeople(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("GroupBuyMapper.findGroupTotalPeople", para);
    }
    
}
