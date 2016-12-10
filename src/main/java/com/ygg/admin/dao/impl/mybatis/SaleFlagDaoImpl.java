package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SaleFlagDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("saleFlagDao")
public class SaleFlagDaoImpl extends BaseDaoImpl implements SaleFlagDao
{
    
    @Override
    public List<Map<String, Object>> findAllSaleFlagInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("SaleFlagMapper.findAllSaleFlagInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countSaleFlagInfoInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SaleFlagMapper.countSaleFlagInfoInfo", para);
    }
    
    @Override
    public int countFlagIdFromSaleWindow(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SaleFlagMapper.countFlagIdFromSaleWindow", id);
    }
    
    @Override
    public int updateFlag(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SaleFlagMapper.updateFlag", para);
    }
    
    @Override
    public int saveFlag(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SaleFlagMapper.saveFlag", para);
    }
    
    @Override
    public String findFlagNameById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SaleFlagMapper.findFlagNameById", id);
    }
}
