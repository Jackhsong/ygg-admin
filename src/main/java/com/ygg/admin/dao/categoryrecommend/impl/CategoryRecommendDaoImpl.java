
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
package com.ygg.admin.dao.categoryrecommend.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.categoryrecommend.CategoryRecommendDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryRecommendDaoImpl.java 8541 2016-03-09 02:00:26Z zhanglide $   
  * @since 2.0
  */
@Repository("categoryRecommendDao")
public class CategoryRecommendDaoImpl extends BaseDaoImpl implements CategoryRecommendDao {
	@Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception{
        return getSqlSessionRead().selectList("CategoryRecommendMapper.findListByParam", param);
    }
	@Override
    public int getCountByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CategoryRecommendMapper.getCountByParam", param);
    } 
	 
	@Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("CategoryRecommendMapper.save", param);
    }
    
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("CategoryRecommendMapper.update", param);
    } 
    @Override
    public int delete(int id) throws Exception{
        return getSqlSession().delete("CategoryRecommendMapper.delete", id);
    }
}
