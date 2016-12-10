package com.ygg.admin.dao.yw.banner.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.yw.banner.YwBannerDao;
import com.ygg.admin.entity.BannerWindowEntity;

@Repository("ywBannerDao")
public class YwBannerDaoImpl extends BaseDaoImpl implements YwBannerDao
{
    
    @Override
    public int save(BannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().insert("YwBannerMapper.save", window);
    }
    
    @Override
    public int update(BannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().update("YwBannerMapper.updateObject", window);
    }
    
    @Override
    public int countBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwBannerMapper.countBannerWindow", para);
    }
    
    @Override
    public List<BannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("YwBannerMapper.findBannerWindow", para);
    }
    
    @Override
    public BannerWindowEntity findBannerWindowById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwBannerMapper.findBannerWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("YwBannerMapper.updateDisplayCode", para);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("YwBannerMapper.updateOrder", para);
    }
}
