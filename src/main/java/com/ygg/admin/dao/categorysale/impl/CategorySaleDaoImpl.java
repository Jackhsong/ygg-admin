/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡
APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.dao.categorysale.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.categorysale.CategorySaleDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SaleWindowEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategorySaleDaoImpl.java 9402 2016-03-30 01:55:10Z xiongliang $   
  * @since 2.0
  */
@Repository("categorySaleDao")
public class CategorySaleDaoImpl extends BaseDaoImpl implements CategorySaleDao
{
    @Override
    public int save(SaleWindowEntity saleWindow)
        throws Exception
    {
        return this.getSqlSession().insert("CategorySaleMapper.save", saleWindow);
    }
    
    @Override
    public int update(SaleWindowEntity saleWindow)
        throws Exception
    {
        return this.getSqlSession().update("CategorySaleMapper.update", saleWindow);
    }
    
    @Override
    public SaleWindowEntity findSaleWindowById(int id)
        throws Exception
    {
        return this.getSqlSession().selectOne("CategorySaleMapper.findSaleWindowById", id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategorySaleMapper.updateDisplayCode", para);
    }
    
    @Override
    public int countCategorySaleWindowByPara(Map<String, Object> params)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CategorySaleMapper.countCategorySaleWindowByPara", params);
    }
    
    @Override
    public List<SaleWindowEntity> findCategorySaleWindowByPara(Map<String, Object> params)
        throws Exception
    {
        return getSqlSessionRead().selectList("CategorySaleMapper.findCategorySaleWindowByPara", params);
    }
    
    @Override
    public int deleteCategorySale(int id)
        throws Exception
    {
        return getSqlSession().delete("CategorySaleMapper.deleteCategorySale", id);
    }
    
    @Override
    public List<SaleWindowEntity> findCategorySaleWindowGreatLockIndex(int id, int categoryLockIndex, int page2ModelId)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("categoryLockIndex", categoryLockIndex);
        param.put("page2ModelId", page2ModelId);
        return getSqlSessionRead().selectList("CategorySaleMapper.findCategorySaleWindowGreatLockIndex", param);
    }
    
    @Override
    public int updateCategoryLockIndex(List<SaleWindowEntity> swes)
        throws Exception
    {
        return getSqlSession().update("CategorySaleMapper.updateCategoryLockIndex", swes);
    }
}
