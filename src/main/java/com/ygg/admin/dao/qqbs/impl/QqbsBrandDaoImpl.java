package com.ygg.admin.dao.qqbs.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.QqbsBrandDao;
import com.ygg.admin.entity.qqbs.QqbsBrandEntity;


@Repository
public class QqbsBrandDaoImpl extends BaseDaoImpl implements QqbsBrandDao
{

    @Override
    public int addBrand(QqbsBrandEntity brand) throws Exception
    {
        return this.getSqlSession().insert("QqbsBrandMapper.addBrand",brand);
    }

    @Override
    public int updateBrand(QqbsBrandEntity brand) throws Exception
    {
        return this.getSqlSession().update("QqbsBrandMapper.updateBrand",brand);
    }

    @Override
    public List<QqbsBrandEntity> findBrandInfo(Map<String,Object> para)
            throws Exception
    {
        return this.getSqlSessionRead().selectList("QqbsBrandMapper.findBrandInfo",para);
    }

    @Override
    public int countBrandInfo(Map<String, Object> para) throws Exception
    {
        return this.getSqlSessionRead().selectOne("QqbsBrandMapper.countBrandInfo",para);
    }

    @Override
    public int updateBrandDisplay(QqbsBrandEntity brand) throws Exception
    {
        return this.getSqlSession().update("QqbsBrandMapper.updateBrandDisplay",brand);
    }

    @Override
    public String getBrandCategoryName(int categoryId) throws Exception
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("categoryId", categoryId);
        return this.getSqlSessionRead().selectOne("QqbsBrandMapper.getBrandCateName",map);
    }
    
}
