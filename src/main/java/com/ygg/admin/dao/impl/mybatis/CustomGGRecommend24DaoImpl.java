package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.CustomGGRecommend24Dao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomGGRecommendEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("customGGRecommend24Dao")
public class CustomGGRecommend24DaoImpl extends BaseDaoImpl implements CustomGGRecommend24Dao
{
    
    @Override
    public List<Map<String, Object>> findRecommendListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("CustomGGRecommend24Mapper.findRecommendByParam", param);
    }
    
    @Override
    public int countRecommendByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomGGRecommend24Mapper.countRecommendByParam", param);
    }
    
    @Override
    public int saveRecommend(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("CustomGGRecommend24Mapper.saveRecommend", param);
    }
    
    @Override
    public int updateRecommend(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("CustomGGRecommend24Mapper.updateRecommend", param);
    }
    
    @Override
    public CustomGGRecommendEntity findCustomGGRecommendById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CustomGGRecommend24Mapper.findCustomGGRecommendById", id);
    }
}
