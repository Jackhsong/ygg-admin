package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.SearchDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyb on 2015/8/24
 */
@Repository("searchDao")
public class SearchDaoImpl extends BaseDaoImpl implements SearchDao
{
    @Override
    public List<Map<String, Object>> findAllSearchHotKeyword(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SearchMapper.findAllSearchHotKeyword",para);
    }

    @Override public int countAllSearchHotKeyword(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SearchMapper.countAllSearchHotKeyword",para);
    }

    @Override
    public int deleteSearchHotKeyword(int id)
        throws Exception
    {
        return getSqlSession().delete("SearchMapper.deleteSearchHotKeyword",id);
    }

    @Override
    public int updateSearchHotKeyword(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SearchMapper.updateSearchHotKeyword",para);
    }

    @Override public int saveSearchHotKeyword(Map<String, Object> para)
    {
        return getSqlSession().insert("SearchMapper.saveSearchHotKeyword",para);
    }

    @Override public List<Map<String, Object>> findAllSearchRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("SearchMapper.findAllSearchRecord", para);
    }

    @Override public int countAllSearchRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SearchMapper.countAllSearchRecord", para);
    }
}
