package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.OverseasDelayDateDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OverseasDelayRemindDateEntity;

@Repository("overseasDelayDateDao")
public class OverseasDelayDateDaoImpl extends BaseDaoImpl implements OverseasDelayDateDao
{
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        
        return getSqlSessionAdmin().delete("DelayRemindDateMapper.delete", para);
    }
    
    @Override
    public List<Map<String, Object>> findOverseasDelayDateInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("DelayRemindDateMapper.findOverseasDelayDateInfo", para);
    }
    
    @Override
    public int countOverseasDelayDateInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("DelayRemindDateMapper.countOverseasDelayDateInfo");
    }
    
    @Override
    public int deleteByYear(String year)
        throws Exception
    {
        
        return getSqlSessionAdmin().delete("DelayRemindDateMapper.deleteByYear", year);
    }
    
    @Override
    public OverseasDelayRemindDateEntity findDelayDateByDay(String day)
        throws Exception
    {
        
        return getSqlSessionAdmin().selectOne("DelayRemindDateMapper.findDelayDateByDay", day);
    }
    
    @Override
    public int isExist(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("DelayRemindDateMapper.isExist", para);
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> para)
        throws Exception
    {
        int id = (int)para.get("id");
        if (id == 0)
        {// 新增
            return getSqlSessionAdmin().insert("DelayRemindDateMapper.save", para);
        }
        else
        {// 更新
            return getSqlSessionAdmin().update("DelayRemindDateMapper.update", para);
        }
    }
}
