package com.ygg.admin.dao.yw.brand.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.QqbsBrandCategoryDao;
import com.ygg.admin.dao.yw.brand.YwBrandCategoryDao;
import com.ygg.admin.entity.yw.YwBrandCategoryEntity;

@Repository
public class YwBrandCategoryDaoImpl extends BaseDaoImpl implements YwBrandCategoryDao
{

    @Override
    public int addBrandCategory(YwBrandCategoryEntity category)
            throws Exception
    {
        return this.getSqlSession().insert("YwBrandCategoryMapper.addBrandCategory",category);
    }

    @Override
    public int updateBrandCategory(
            YwBrandCategoryEntity category) throws Exception
    {
        return this.getSqlSession().update("YwBrandCategoryMapper.updateBrandCategory",category);
    }

    @Override
    public List<YwBrandCategoryEntity> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception
    {
        return this.getSqlSessionRead().selectList("YwBrandCategoryMapper.findBrandCategoryInfo"); 
    }

    @Override
    public int countBrandCategoryInfo() throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwBrandCategoryMapper.countBrandCategoryInfo");
    }
    
}
