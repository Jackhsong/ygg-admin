package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.PromotionActivityDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("promotionActivityDao")
public class PromotionActivityDaoImpl extends BaseDaoImpl implements PromotionActivityDao
{
    @Override
    public List<Map<String, Object>> findSpecialActivityNewByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("PromotionActivityMapper.findSpecialActivityNewByPara", para);
    }
    
    @Override
    public int saveSpecialActivityNew(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("PromotionActivityMapper.saveSpecialActivityNew", para);
    }
    
    @Override
    public int updateSpecialActivityNew(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PromotionActivityMapper.updateSpecialActivityNew", para);
    }
    
    @Override
    public List<Map<String, Object>> findSpecialActivityNewProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("PromotionActivityMapper.findSpecialActivityNewProductByPara", para);
    }
    
    @Override
    public int updateSpecialActivityNewProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PromotionActivityMapper.updateSpecialActivityNewProduct", para);
    }
    
    @Override
    public int saveSpecialActivityNewProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("PromotionActivityMapper.saveSpecialActivityNewProduct", para);
    }
    
    @Override
    public int countSpecialActivityNewByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("PromotionActivityMapper.countSpecialActivityNewByPara", para);
    }
    
    @Override
    public int countSpecialActivityNewProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("PromotionActivityMapper.countSpecialActivityNewProductByPara", para);
    }
    
    @Override
    public Map<String, Object> findSpecialActivityNewByid(int id)
        throws Exception
    {
        return getSqlSession().selectOne("PromotionActivityMapper.findSpecialActivityNewByid", id);
    }
    
    @Override
    public List<Integer> findSpecialActivityNewProductByid(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("PromotionActivityMapper.findSpecialActivityNewProductByid", id);
    }

    @Override
    public int deleteSpecialActivityNewProductById(int id)
        throws Exception
    {
        return getSqlSession().delete("PromotionActivityMapper.deleteSpecialActivityNewProductById", id);
    }
}
