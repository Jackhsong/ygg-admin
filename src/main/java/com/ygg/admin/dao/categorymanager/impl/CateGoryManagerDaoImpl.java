
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
package com.ygg.admin.dao.categorymanager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.categorymanager.CateGoryManagerDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.categorymanager.Page2ModelEntity;

/**
  * 品类馆管理Dao
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CateGoryManagerDaoImpl.java 8678 2016-03-11 01:42:35Z zhanglide $   
  * @since 2.0
  */
@Repository("cateGoryManagerDao")
public class CateGoryManagerDaoImpl extends BaseDaoImpl implements CateGoryManagerDao {

	
	/**
	 * @param para
	 * @return
	 * @throws Exception
	 * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#findAllPageByPara(java.util.Map)
	 */
	@Override
    public List<Map<String, Object>> findAllPageByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CategoryManagerMapper.findAllPageByPara", para);
    }
	
	/**
	 * @param para
	 * @return
	 * @throws Exception
	 * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#countAllPageByPara(java.util.Map)
	 */
	@Override
    public int countAllPageByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CategoryManagerMapper.countAllPageByPara", para);
    }
	 
	/**
	 * @param id
	 * @return
	 * @throws Exception
	 * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#findPageById(int)
	 */
	@Override
    public Map<String, Object> findPageById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        List<Map<String, Object>> resultList = findAllPageByPara(para);
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    
    
    /**
     * @param para
     * @return
     * @throws Exception
     * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#insertPage(java.util.Map)
     */
    @Override
    public int insertPage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CategoryManagerMapper.insertPage", para);
    }
    
    
    /**
     * @param para
     * @return
     * @throws Exception
     * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#updatePageById(java.util.Map)
     */
    @Override
    public int updatePageById(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CategoryManagerMapper.updatePageById", para);
    }
    
    
    /**
     * @param para
     * @return
     * @throws Exception
     * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#insertPageModel(java.util.Map)
     */
    @Override
    public int insertPageModel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CategoryManagerMapper.insertPageModel", para);
    }
    
    /**
     * @param para
     * @return
     * @throws Exception
     * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#findAllPageModelByPara(java.util.Map)
     */
    @Override
    public List<Page2ModelEntity> findAllPageModelByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CategoryManagerMapper.findAllPageModelByPara", para);
    }
    
    /**
     * @param para
     * @return
     * @throws Exception
     * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#updatePageModelById(java.util.Map)
     */
    @Override
    public int updatePageModelById(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CategoryManagerMapper.updatePageModelById", para);
    }
    
    
    /**
     * @param id
     * @return
     * @throws Exception
     * @see com.ygg.admin.dao.categorymanager.CateGoryManagerDao#findPageModelById(int)
     */
    public Page2ModelEntity findPageModelById(int id)
        throws Exception{
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        List<Page2ModelEntity> resultList = getSqlSessionRead().selectList("CategoryManagerMapper.findAllPageModelById", para);
        return resultList.isEmpty() ? null : resultList.get(0);
    }
    
}
