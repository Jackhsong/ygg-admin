package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.BannerWindowDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.BannerWindowEntity;

@Repository("bannerWindowDao")
public class BannerWindowDaoImpl extends BaseDaoImpl implements BannerWindowDao
{
    
    @Override
    public int save(BannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().insert("BannerWindowMapper.save", window);
    }
    
    @Override
    public int update(BannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().update("BannerWindowMapper.updateObject", window);
    }
    
    @Override
    public int countBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("BannerWindowMapper.countBannerWindow", para);
    }
    
    @Override
    public List<BannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectList("BannerWindowMapper.findBannerWindow", para);
    }
    
    @Override
    public BannerWindowEntity findBannerWindowById(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("BannerWindowMapper.findBannerWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("BannerWindowMapper.updateDisplayCode", para);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("BannerWindowMapper.updateOrder", para);
    }
    
}
