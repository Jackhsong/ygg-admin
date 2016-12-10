package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.OperationDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OperationEntity;

@Repository("operationDao")
public class OperationDaoImpl extends BaseDaoImpl implements OperationDao
{
    
    @Override
    public int save(OperationEntity op)
        throws Exception
    {
        return getSqlSession().insert("OperationMapper.save", op);
    }
    
    @Override
    public List<OperationEntity> findAllOperationByPara(Map<String, Object> para)
        throws Exception
    {
        List<OperationEntity> reList = getSqlSession().selectList("OperationMapper.findAllOperationByPara", para);
        return reList == null ? new ArrayList<OperationEntity>() : reList;
    }
    
    @Override
    public int countOperationByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("OperationMapper.countOperationByPara", para);
    }
    
}
