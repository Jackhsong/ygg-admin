package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.QqbsBannerWindowDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.BannerWindowEntity;

@Repository("qqbsBannerWindowDao")
public class QqbsBannerWindowDaoImpl extends BaseDaoImpl implements QqbsBannerWindowDao
{
    
    @Override
    public int save(BannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().insert("QqbsBannerWindowMapper.save", window);
    }
    
    @Override
    public int update(BannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().update("QqbsBannerWindowMapper.updateObject", window);
    }
    
    @Override
    public int countBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("QqbsBannerWindowMapper.countBannerWindow", para);
    }
    
    @Override
    public List<BannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectList("QqbsBannerWindowMapper.findBannerWindow", para);
    }
    
    @Override
    public BannerWindowEntity findBannerWindowById(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("QqbsBannerWindowMapper.findBannerWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("QqbsBannerWindowMapper.updateDisplayCode", para);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("QqbsBannerWindowMapper.updateOrder", para);
    }
}
