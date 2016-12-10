package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ActivityManjianDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ActivitiyRelationProductEntity;
import com.ygg.admin.entity.ActivityManjianEntity;

@Repository
public class ActivityManjianDaoImpl extends BaseDaoImpl implements ActivityManjianDao
{
    
    @Override
    public List<ActivityManjianEntity> findAllActivityManjian(Map<String, Object> para)
        throws Exception
    {
        List<ActivityManjianEntity> reList = getSqlSessionRead().selectList("ActivityManjianMapper.findAllActivityManjian", para);
        return reList == null ? new ArrayList<ActivityManjianEntity>() : reList;
    }
    
    @Override
    public int countActivityManjian(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ActivityManjianMapper.countActivityManjian", para);
    }
    
    @Override
    public List<Integer> findProductIdByTypeAndTypeId(Map<String, Object> para)
        throws Exception
    {
        List<Integer> reList = getSqlSessionRead().selectList("ActivityManjianMapper.findProductIdByTypeAndTypeId", para);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int saveActivityManjian(ActivityManjianEntity activity)
        throws Exception
    {
        return getSqlSession().insert("ActivityManjianMapper.saveActivityManjian", activity);
    }
    
    @Override
    public int updateActivityManjian(ActivityManjianEntity activity)
        throws Exception
    {
        return getSqlSession().update("ActivityManjianMapper.updateActivityManjian", activity);
    }
    
    @Override
    public ActivityManjianEntity findActivityManjianById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ActivityManjianMapper.findActivityManjianById", id);
    }
    
    @Override
    public List<ActivityManjianEntity> findActivityManjianByMap(Map<String, Object> para)
        throws Exception
    {
        List<ActivityManjianEntity> reList = getSqlSessionRead().selectList("ActivityManjianMapper.findActivityManjianByMap", para);
        return reList == null ? new ArrayList<ActivityManjianEntity>() : reList;
    }
    
    @Override
    public int updateDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ActivityManjianMapper.updateDisplayStatus", para);
    }
    
    @Override
    public int countActivitiyRelationProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ActivityManjianMapper.countActivitiyRelationProduct", para);
    }
    
    @Override
    public List<ActivitiyRelationProductEntity> findActivitiyRelationProduct(Map<String, Object> para)
        throws Exception
    {
        List<ActivitiyRelationProductEntity> reList = getSqlSessionRead().selectList("ActivityManjianMapper.findActivitiyRelationProduct", para);
        return reList == null ? new ArrayList<ActivitiyRelationProductEntity>() : reList;
    }
    
    @Override
    public int countProductForAddToActivityManjian(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ActivityManjianMapper.countProductForAddToActivityManjian", para);
    }
    
    @Override
    public List<ActivitiyRelationProductEntity> findProductForAddToActivityManjian(Map<String, Object> para)
        throws Exception
    {
        List<ActivitiyRelationProductEntity> reList = getSqlSessionRead().selectList("ActivityManjianMapper.findProductForAddToActivityManjian", para);
        return reList == null ? new ArrayList<ActivitiyRelationProductEntity>() : reList;
    }
    
    @Override
    public int addProductForActivityManjian(List<ActivitiyRelationProductEntity> productList)
        throws Exception
    {
        return getSqlSession().insert("ActivityManjianMapper.addProductForActivityManjian", productList);
    }
    
    @Override
    public int deleteActivityManjianProduct(List<String> idList)
        throws Exception
    {
        return getSqlSession().delete("ActivityManjianMapper.deleteActivityManjianProduct", idList);
    }
    
}
