package com.ygg.admin.dao.impl.mybatis.base;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

import com.ygg.admin.dao.BaseDao;

public class BaseDaoImpl /* extends SqlSessionDaoSupport */implements BaseDao
{
    
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    private SqlSessionTemplate sqlSessionTemplateRead;

    @Resource
    private SqlSessionTemplate sqlSessionTemplateAdmin;
    
    protected SqlSessionTemplate getSqlSession()
    {
        return sqlSessionTemplate;
    }

    protected SqlSessionTemplate getSqlSessionRead()
    {
        return sqlSessionTemplateRead;
    }

    protected SqlSessionTemplate getSqlSessionAdmin()
    {
        return sqlSessionTemplateAdmin;
    }
    
}
