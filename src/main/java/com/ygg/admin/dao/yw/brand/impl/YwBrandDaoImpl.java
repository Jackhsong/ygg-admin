package com.ygg.admin.dao.yw.brand.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.yw.brand.YwBrandDao;
import com.ygg.admin.entity.yw.YwBrandEntity;


@Repository
public class YwBrandDaoImpl extends BaseDaoImpl implements YwBrandDao
{

    @Override
    public int addBrand(YwBrandEntity brand) throws Exception
    {
        return this.getSqlSession().insert("YwBrandMapper.addBrand",brand);
    }

    @Override
    public int updateBrand(YwBrandEntity brand) throws Exception
    {
        return this.getSqlSession().update("YwBrandMapper.updateBrand",brand);
    }

    @Override
    public List<YwBrandEntity> findBrandInfo(Map<String,Object> para)
            throws Exception
    {
        return this.getSqlSessionRead().selectList("YwBrandMapper.findBrandInfo",para);
    }

    @Override
    public int countBrandInfo(Map<String, Object> para) throws Exception
    {
        return this.getSqlSessionRead().selectOne("YwBrandMapper.countBrandInfo",para);
    }

    @Override
    public int updateBrandDisplay(YwBrandEntity brand) throws Exception
    {
        return this.getSqlSession().update("YwBrandMapper.updateBrandDisplay",brand);
    }

    @Override
    public String getBrandCategoryName(int categoryId) throws Exception
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("categoryId", categoryId);
        return this.getSqlSessionRead().selectOne("YwBrandMapper.getBrandCateName",map);
    }
    
}
