package com.ygg.admin.service.yw.brand.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.yw.brand.YwBrandCategoryDao;
import com.ygg.admin.entity.yw.YwBrandCategoryEntity;
import com.ygg.admin.service.qqbsbrand.QqbsBrandCategoryService;
import com.ygg.admin.service.yw.brand.YwBrandCategoryService;

@Repository
public class YwBrandCategoryServiceImpl implements YwBrandCategoryService
{

    @Resource
    private YwBrandCategoryDao ywBrandCategoryDao;
    
    @Override
    public int addBrandCategory(YwBrandCategoryEntity category)
            throws Exception
    {
        return ywBrandCategoryDao.addBrandCategory(category);
    }

    @Override
    public int updateBrandCategory(YwBrandCategoryEntity category) 
            throws Exception
    {
        return ywBrandCategoryDao.updateBrandCategory(category);
    }

    @Override
    public Map<String,Object> findBrandCategoryInfo(Map<String,Object> para)
            throws Exception
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<YwBrandCategoryEntity> categoryList= ywBrandCategoryDao.findBrandCategoryInfo(para);
        int total = ywBrandCategoryDao.countBrandCategoryInfo();
        resultMap.put("rows", categoryList);
        resultMap.put("total",total);
        return resultMap;
    }
    
}
