package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SignInDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("signInDao")
public class SignInDaoImpl extends BaseDaoImpl implements SignInDao
{
    @Override
    public Integer getBeginYearMonth()
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SignInMapper.getBeginYearMonth");
    }
    
    @Override
    public List<Map<String, Object>> findAllSignSetting(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SignInMapper.findAllSignSetting", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countSignSetting(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SignInMapper.countSignSetting", para);
    }
    
    @Override
    public int findCurrentDayByYearMonth(int yearMonth)
        throws Exception
    {
        Integer day = this.getSqlSessionRead().selectOne("SignInMapper.findCurrentDayByYearMonth", yearMonth);
        return day == null ? 1 : day.intValue() + 1;
    }
    
    @Override
    public int insert(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("SignInMapper.insert", para);
    }
    
    @Override
    public int update(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SignInMapper.update", para);
    }
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().delete("SignInMapper.delete", para);
    }
    
    @Override
    public List<Map<String, Object>> findAccountSignCount(int currentYearMonth)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SignInMapper.findAccountSignCount", currentYearMonth);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countSigninProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SignInMapper.countSigninProduct", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSigninProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SignInMapper.findAllSigninProduct", para);
    }
    
    @Override
    public int addSigninProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("SignInMapper.addSigninProduct", para);
    }
    
    @Override
    public int deleteSigninProduct(List<String> idList)
        throws Exception
    {
        return getSqlSession().delete("SignInMapper.deleteSigninProduct", idList);
    }
    
    @Override
    public int updateSigninProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SignInMapper.updateSigninProductDisplayStatus", para);
    }
    
    @Override
    public int updateSigninProductSequence(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SignInMapper.updateSigninProductSequence", para);
    }
    
}
