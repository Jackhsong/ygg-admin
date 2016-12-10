package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SystemConfigDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("systemConfigDao")
public class SystemConfigDaoImpl extends BaseDaoImpl implements SystemConfigDao
{
    
    @Override
    public List<Map<String, Object>> findWhiteIpInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionAdmin().selectList("SystemConfigMapper.findWhiteIpInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countWhiteIpInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().selectOne("SystemConfigMapper.countWhiteIpInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findWhiteURLInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionAdmin().selectList("SystemConfigMapper.findWhiteURLInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countWhiteURLInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().selectOne("SystemConfigMapper.countWhiteURLInfo", para);
    }
    
    @Override
    public int addWhiteIp(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().insert("SystemConfigMapper.insertWhiteIp", para);
    }
    
    @Override
    public int updateWhiteIp(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().update("SystemConfigMapper.updateWhiteIp", para);
    }
    
    @Override
    public int addWhiteURL(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().insert("SystemConfigMapper.insertWhiteURL", para);
    }
    
    @Override
    public int updateWhiteURL(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().update("SystemConfigMapper.updateWhiteURL", para);
    }
    
    @Override
    public int updateWhiteURLStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().update("SystemConfigMapper.updateWhiteURLStatus", para);
    }
    
    @Override
    public int updateWhiteIpStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().update("SystemConfigMapper.updateWhiteIpStatus", para);
    }
    
}
