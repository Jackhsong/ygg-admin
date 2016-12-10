package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.GlobalSaleWindowDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.GlobalSaleWindowEntity;

@Repository("globalSaleWindowDao")
public class GlobalSaleWindowDaoImpl extends BaseDaoImpl implements GlobalSaleWindowDao
{
    
    @Override
    public int save(GlobalSaleWindowEntity qqbsSaleWindow)
        throws Exception
    {
        return this.getSqlSession().insert("GlobalSaleWindowMapper.save", qqbsSaleWindow);
    }
    
    @Override
    public int update(GlobalSaleWindowEntity qqbsSaleWindow)
        throws Exception
    {
        return this.getSqlSession().update("GlobalSaleWindowMapper.update", qqbsSaleWindow);
    }
    
    @Override
    public int countSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("GlobalSaleWindowMapper.countSaleWindow", para);
    }
    
    @Override
    public List<GlobalSaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectList("GlobalSaleWindowMapper.findAllSaleWindow", para);
    }
    
    @Override
    public GlobalSaleWindowEntity findSaleWindowById(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("GlobalSaleWindowMapper.findSaleWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("GlobalSaleWindowMapper.updateDisplayCode", para);
    }
    
    
    @Override
    public List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("GlobalSaleWindowMapper.findAllSaleWindowForSellerPeriod", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int hideSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("GlobalSaleWindowMapper.hideSaleWindow", para);
    }
    
    @Override
    public int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("GlobalSaleWindowMapper.countAllSaleWindowForSellerPeriod", para);
    }
    
    @Override
    public int countSingleSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("GlobalSaleWindowMapper.countSingleSaleWindow", para);
    }
    
    @Override
    public List<GlobalSaleWindowEntity> findAllSingleSaleWindow(Map<String, Object> para)
        throws Exception
    {
        List<GlobalSaleWindowEntity> reList = this.getSqlSessionRead().selectList("GlobalSaleWindowMapper.findAllSingleSaleWindow", para);
        return reList == null ? new ArrayList<GlobalSaleWindowEntity>() : reList;
    }
    
    @Override
    public int countGroupSaleWinodw(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("GlobalSaleWindowMapper.countGroupSaleWinodw", para);
    }
    
    @Override
    public List<GlobalSaleWindowEntity> findAllGroupSaleWinodw(Map<String, Object> para)
        throws Exception
    {
        List<GlobalSaleWindowEntity> reList = this.getSqlSessionRead().selectList("GlobalSaleWindowMapper.findAllGroupSaleWinodw", para);
        return reList == null ? new ArrayList<GlobalSaleWindowEntity>() : reList;
    }

}
