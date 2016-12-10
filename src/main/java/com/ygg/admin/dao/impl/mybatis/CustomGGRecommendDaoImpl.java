package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CustomGGRecommendDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomGGRecommendEntity;

@Repository("customGGRecommendDao")
public class CustomGGRecommendDaoImpl extends BaseDaoImpl implements CustomGGRecommendDao
{
    
    @Override
    public List<Map<String, Object>> findRecommendListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("CustomGGRecommendMapper.findRecommendByParam", param);
    }
    
    @Override
    public int countRecommendByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomGGRecommendMapper.countRecommendByParam", param);
    }
    
    @Override
    public int saveRecommend(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("CustomGGRecommendMapper.saveRecommend", param);
    }
    
    @Override
    public int updateRecommend(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("CustomGGRecommendMapper.updateRecommend", param);
    }
    
    @Override
    public CustomGGRecommendEntity findCustomGGRecommendById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomGGRecommendMapper.findCustomGGRecommendById", id);
    }
}
