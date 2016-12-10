package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MwebGroupBannerWindowDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupBannerWindowEntity;

@Repository("mwebGroupBannerWindowDao")
public class MwebGroupBannerWindowDaoImpl extends BaseDaoImpl implements MwebGroupBannerWindowDao
{
    
    @Override
    public int save(MwebGroupBannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().insert("MwebGroupBannerWindowMapper.save", window);
    }
    
    @Override
    public int update(MwebGroupBannerWindowEntity window)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupBannerWindowMapper.updateObject", window);
    }
    
    @Override
    public int countBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("MwebGroupBannerWindowMapper.countBannerWindow", para);
    }
    
    @Override
    public List<MwebGroupBannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("MwebGroupBannerWindowMapper.findBannerWindow", para);
    }
    
    @Override
    public MwebGroupBannerWindowEntity findBannerWindowById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("MwebGroupBannerWindowMapper.findBannerWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupBannerWindowMapper.updateDisplayCode", para);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupBannerWindowMapper.updateOrder", para);
    }
}
