package com.ygg.admin.service.qqbsbrand.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.qqbs.QqbsBrandCategoryDao;
import com.ygg.admin.entity.qqbs.QqbsBrandCategoryEntity;
import com.ygg.admin.service.qqbsbrand.QqbsBrandCategoryService;

@Repository
public class QqbsBrandCategoryServiceImpl implements QqbsBrandCategoryService
{

    @Resource
    private QqbsBrandCategoryDao qqbsBrandCategoryDao;
    
    @Override
    public int addBrandCategory(QqbsBrandCategoryEntity category)
            throws Exception
    {
        return qqbsBrandCategoryDao.addBrandCategory(category);
    }

    @Override
    public int updateBrandCategory(QqbsBrandCategoryEntity category) 
            throws Exception
    {
        return qqbsBrandCategoryDao.updateBrandCategory(category);
    }

    @Override
    public Map<String,Object> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<QqbsBrandCategoryEntity> categoryList= qqbsBrandCategoryDao.findBrandCategoryInfo(para);
        int total = qqbsBrandCategoryDao.countBrandCategoryInfo();
        resultMap.put("rows", categoryList);
        resultMap.put("total",total);
        return resultMap;
    }
    
}
