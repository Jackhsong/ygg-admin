package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SaleWindowEntity;

@Repository("saleWindowDao")
public class SaleWindowDaoImpl extends BaseDaoImpl implements SaleWindowDao
{
    
    @Override
    public int save(SaleWindowEntity saleWindow)
        throws Exception
    {
        return this.getSqlSession().insert("SaleWindowMapper.save", saleWindow);
    }
    
    @Override
    public int update(SaleWindowEntity saleWindow)
        throws Exception
    {
        return this.getSqlSession().update("SaleWindowMapper.update", saleWindow);
    }
    
    @Override
    public int countSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("SaleWindowMapper.countSaleWindow", para);
    }
    
    @Override
    public List<SaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectList("SaleWindowMapper.findAllSaleWindow", para);
    }
    
    @Override
    public SaleWindowEntity findSaleWindowById(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("SaleWindowMapper.findSaleWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SaleWindowMapper.updateDisplayCode", para);
    }
    
    @Override
    public int updateLaterOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SaleWindowMapper.updateLaterOrder", para);
    }
    
    @Override
    public int updateNowOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SaleWindowMapper.updateNowOrder", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SaleWindowMapper.findAllSaleWindowForSellerPeriod", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int hideSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SaleWindowMapper.hideSaleWindow", para);
    }
    
    @Override
    public int countAllSaleWindowForSellerPeriod(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SaleWindowMapper.countAllSaleWindowForSellerPeriod", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllTomorrowSaleWindow(int startTime)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("SaleWindowMapper.findAllTomorrowSaleWindow", startTime);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> findAllCurrentSaleWindow(int currentTime)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("SaleWindowMapper.findAllCurrentSaleWindow", currentTime);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Integer> findSaleWindowGroupIdsByPara(Map<String, Object> parameter)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findSaleWindowGroupIdsByPara", parameter);
    }
    
    @Override
    public List<Integer> findSaleWindowSingleIdsByPara(Map<String, Object> parameter)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findSaleWindowSingleIdsByPara", parameter);
    }
    
    @Override
    public List<SaleWindowEntity> findAllSaleWindowByPara(Map<String, Object> params)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findAllSaleWindowByPara", params);
    }
    
    /**
     * 根据特卖Id批量查询一级分类名称
     */
    @Override
    public List<Map<String, Object>> findCategoryFirstNamesBySwids(List<Integer> ids)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findCategoryFirstNamesBySwids", ids);
    }
    
    @Override
    public List<Map<String, Object>> findSellerNameBySingleSwids(List<Integer> ids)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findSellerNameBySingleSwids", ids);
    }
    
    @Override
    public List<Map<String, Object>> findSellerNameByGroupSwids(List<Integer> ids)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findSellerNameByGroupSwids", ids);
    }
    
    @Override
    public List<Map<String, Object>> findStockBySingleSwids(List<Integer> ids)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findStockBySingleSwids", ids);
    }
    
    @Override
    public int countSaleWindowByPara(Map<String, Object> params)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SaleWindowMapper.countSaleWindowByPara", params);
    }
    
    @Override
    public List<SaleWindowEntity> findSaleWindowListByPara(Map<String, Object> params)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findSaleWindowListByPara", params);
    }
    
    @Override
    public List<Map<String, Object>> findProductBasesBySingleswids(List<Integer> ids)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findProductBasesBySingleswids", ids);
    }
    
    @Override
    public List<Map<String, Object>> findCustomActivityShareUrlBySwids(List<Integer> ids)
        throws Exception
    {
        return getSqlSessionRead().selectList("SaleWindowMapper.findCustomActivityShareUrlBySwids", ids);
    }
    
    @Override
    public SaleWindowEntity findSaleWindowByPara(Map<String, Object> params)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SaleWindowMapper.findSaleWindowByPara", params);
    }
}
