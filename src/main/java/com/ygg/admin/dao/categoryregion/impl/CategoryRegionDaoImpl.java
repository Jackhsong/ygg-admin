
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
package com.ygg.admin.dao.categoryregion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.categoryregion.CategoryRegionDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.categoryregion.Page2ModelCustomLayoutEntity;
import com.ygg.admin.entity.categoryregion.Page2ModelCustomRegionEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryRegionDaoImpl.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
@Repository("categoryRegionDao")
public class CategoryRegionDaoImpl extends BaseDaoImpl implements CategoryRegionDao {
	@Override
    public List<Map<String, Object>> findAllCustomRegion(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CategoryRegionMapper.findAllCustomRegion", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryRegionMapper.countCustomRegion", para);
    }
    
    @Override
    public int saveCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryRegionMapper.saveCustomRegion", para);
    }
    
    @Override
    public int updateCustomRegion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryRegionMapper.updateCustomRegion", para);
    }
    
    @Override
    public int updateCustomRegionAvailableStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryRegionMapper.updateCustomRegionAvailableStatus", para);
    }
    
    @Override
    public int updateCustomRegionDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryRegionMapper.updateCustomRegionDisplayStatus", para);
    }
    
    @Override
    public int findMaxCustomRegionSequence()
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CategoryRegionMapper.findMaxCustomRegionSequence");
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int countCustomLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryRegionMapper.countCustomLayout", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllCustomLayout(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("CategoryRegionMapper.findAllCustomLayout", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int updateCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryRegionMapper.updateCustomLayoutDisplayStatus", para);
    }
    
    @Override
    public int updateCustomLayoutSequence(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("CategoryRegionMapper.updateCustomLayoutSequence", para);
    }
    
    @Override
    public int addCustomLayout(Page2ModelCustomLayoutEntity customLayout)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryRegionMapper.addCustomLayout", customLayout);
    }
    
    @Override
    public int updateCustomLayout(Page2ModelCustomLayoutEntity customLayout)
        throws Exception
    {
        return this.getSqlSession().update("CategoryRegionMapper.updateCustomLayout", customLayout);
    }
    
    @Override
    public int getCustonLayoutMaxSequence(int regionId)
        throws Exception
    {
        Integer sequence = this.getSqlSessionRead().selectOne("CategoryRegionMapper.getCustonLayoutMaxSequence", regionId);
        return sequence == null ? 1 : sequence.intValue() + 1;
    }
    
    @Override
    public int insertRelationCustomRegionLayout(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("CategoryRegionMapper.insertRelationCustomRegionLayout", para);
    }
    
    @Override
    public int deleteCustomLayout(int id)
        throws Exception
    {
        return this.getSqlSession().delete("CategoryRegionMapper.deleteCustomLayout", id);
    }
    
    @Override
    public Page2ModelCustomLayoutEntity findCustomLayoutById(int customLayoutId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryRegionMapper.findCustomLayoutById", customLayoutId);
    }
    
    @Override
    public Page2ModelCustomRegionEntity findCustomRegionById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("CategoryRegionMapper.findCustomRegionById", id);
    }
    
    @Override
    public List<Integer> findCustomLayoutIdByCustomRegionId(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("CategoryRegionMapper.findCustomLayoutIdByCustomRegionId", id);
    }
}
