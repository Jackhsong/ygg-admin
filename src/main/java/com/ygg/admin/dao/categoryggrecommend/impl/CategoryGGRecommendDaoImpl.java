
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
package com.ygg.admin.dao.categoryggrecommend.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.categoryggrecommend.CategoryGGRecommendDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CustomGGRecommendEntity;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryGGRecommendDaoImpl.java 8560 2016-03-09 07:44:57Z zhanglide $   
  * @since 2.0
  */
@Repository("categoryGGRecommendDao")
public class CategoryGGRecommendDaoImpl extends BaseDaoImpl implements CategoryGGRecommendDao {
	@Override
    public List<Map<String, Object>> findRecommendListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("CategoryGGRecommendMapper.findRecommendByParam", param);
    }
    
    @Override
    public int countRecommendByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CategoryGGRecommendMapper.countRecommendByParam", param);
    }
    
    @Override
    public int saveRecommend(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("CategoryGGRecommendMapper.saveRecommend", param);
    }
    
    @Override
    public int updateRecommend(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("CategoryGGRecommendMapper.updateRecommend", param);
    }
    
    @Override
    public CustomGGRecommendEntity findCustomGGRecommendById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CategoryGGRecommendMapper.findCustomGGRecommendById", id);
    }
}
