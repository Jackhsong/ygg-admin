package com.ygg.admin.dao.yw.sale.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.yw.sale.YwSaleDao;
import com.ygg.admin.entity.yw.YwSaleEntity;

@Repository("ywSaleDao")
public class YwSaleDaoImpl extends BaseDaoImpl implements YwSaleDao
{
    
    @Override
    public int save(YwSaleEntity ywSale)
        throws Exception
    {
        return this.getSqlSession().insert("YwSaleMapper.save", ywSale);
    }
    
    @Override
    public int update(YwSaleEntity ywSale)
        throws Exception
    {
        return this.getSqlSession().update("YwSaleMapper.update", ywSale);
    }
    
    @Override
    public int countSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.countSaleWindow", para);
    }
    
    @Override
    public List<YwSaleEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("YwSaleMapper.findAllSaleWindow", para);
    }
    
    @Override
    public YwSaleEntity findSaleWindowById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.findSaleWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("YwSaleMapper.updateDisplayCode", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("YwSaleMapper.findAllSaleWindowForSellerPeriod", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int hideSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("YwSaleMapper.hideSaleWindow", para);
    }
    
    @Override
    public int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.countAllSaleWindowForSellerPeriod", para);
    }
    
    @Override
    public int countSingleSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.countSingleSaleWindow", para);
    }
    
    @Override
    public List<YwSaleEntity> findAllSingleSaleWindow(Map<String, Object> para)
        throws Exception
    {
        List<YwSaleEntity> reList = this.getSqlSessionRead().selectList("YwSaleMapper.findAllSingleSaleWindow", para);
        return reList == null ? new ArrayList<YwSaleEntity>() : reList;
    }
    
    @Override
    public int countGroupSaleWinodw(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.countGroupSaleWinodw", para);
    }
    
    @Override
    public List<YwSaleEntity> findAllGroupSaleWinodw(Map<String, Object> para)
        throws Exception
    {
        List<YwSaleEntity> reList = this.getSqlSessionRead().selectList("YwSaleMapper.findAllGroupSaleWinodw", para);
        return reList == null ? new ArrayList<YwSaleEntity>() : reList;
    }
    
    public List<YwSaleEntity> findAllByParam(Map<String, Object> para)
        throws Exception
    {
        List<YwSaleEntity> reList = this.getSqlSessionRead().selectList("YwSaleMapper.findAllByParam", para);
        return reList == null ? new ArrayList<YwSaleEntity>() : reList;
    }
    
    public int countAllByParam(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.countAllByParam", para);
    }

    @Override
    public List<YwSaleEntity> findAllSaleWindowByParam(Map<String, Object> para)
    {
        return this.getSqlSessionRead().selectList("YwSaleMapper.findAllSaleWindowByParam", para);
    }

    @Override
    public int countAllSaleWindowByParam(Map<String, Object> para)
    {
        return this.getSqlSessionRead().selectOne("YwSaleMapper.countAllSaleWindowByParam", para);
    }

    @Override
    public int updateOrder(int id, int order)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("order", order);
        return this.getSqlSession().update("YwSaleMapper.updateOrder", para);
    }
}
