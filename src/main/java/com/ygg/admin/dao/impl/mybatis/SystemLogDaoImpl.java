package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SystemLogDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("logDao")
public class SystemLogDaoImpl extends BaseDaoImpl implements SystemLogDao
{
    @Override
    public int countSystemLog(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("SystemLogMapper.countSystemLog", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSystemLogList(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionAdmin().selectList("SystemLogMapper.findAllSystemLogList", para);
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    public int logger(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("SystemLogMapper.logger", para);
    }
    
    @Override
    public int log(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("SystemLogMapper.log", para);
    }
    
    @Override
    public int countLog(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("SystemLogMapper.countLog", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllLogList(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionAdmin().selectList("SystemLogMapper.findAllLogList", para);
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
}
