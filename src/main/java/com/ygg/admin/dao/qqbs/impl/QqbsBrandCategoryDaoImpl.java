package com.ygg.admin.dao.qqbs.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.QqbsBrandCategoryDao;
import com.ygg.admin.entity.qqbs.QqbsBrandCategoryEntity;

@Repository
public class QqbsBrandCategoryDaoImpl extends BaseDaoImpl implements QqbsBrandCategoryDao
{

    @Override
    public int addBrandCategory(QqbsBrandCategoryEntity category)
            throws Exception
    {
        return this.getSqlSession().insert("QqbsBrandCategoryMapper.addBrandCategory",category);
    }

    @Override
    public int updateBrandCategory(
            QqbsBrandCategoryEntity category) throws Exception
    {
        return this.getSqlSession().update("QqbsBrandCategoryMapper.updateBrandCategory",category);
    }

    @Override
    public List<QqbsBrandCategoryEntity> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception
    {
        return this.getSqlSessionRead().selectList("QqbsBrandCategoryMapper.findBrandCategoryInfo"); 
    }

    @Override
    public int countBrandCategoryInfo() throws Exception
    {
        return this.getSqlSessionRead().selectOne("QqbsBrandCategoryMapper.countBrandCategoryInfo");
    }
    
}
