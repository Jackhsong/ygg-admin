
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
package com.ygg.admin.dao.brandrecommend.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.brandrecommend.BrandRecommendDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

/**
  * 首页品牌推荐Dao
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: BrandRecommendDaoImpl.java 12325 2016-05-19 06:07:54Z zhangyibo $   
  * @since 2.0
  */
@Repository("brandRecommendDao")
public class BrandRecommendDaoImpl extends BaseDaoImpl implements BrandRecommendDao
{
    @Override
    public List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("BrandRecommendMapper.findListByParam", param);
    }
    
    @Override
    public int getCountByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectOne("BrandRecommendMapper.getCountByParam", param);
    }
    
    @Override
    public int save(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("BrandRecommendMapper.save", param);
    }
    
    @Override
    public int update(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("BrandRecommendMapper.update", param);
    }
    
    @Override
    public int delete(int id)
        throws Exception
    {
        return getSqlSession().delete("BrandRecommendMapper.delete", id);
    }
}
