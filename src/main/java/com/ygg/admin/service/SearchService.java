package com.ygg.admin.service;

import java.util.Map;

/**
 * Created by zhangyb on 2015/8/24 0024.
 */
public interface SearchService
{
    /**
     * 查找搜索词
     * @param para
     * @return
     * @throws Exception
     */
    Map<String,Object> findAllSearchHotKeyword(Map<String,Object> para)
        throws Exception;

    /**
     * 保存热搜词
     * @param keyword
     * @param sequence
     * @return
     * @throws Exception
     */
    Map<String,Object> saveSearchHotKeyword(int id,String keyword,int sequence)
        throws Exception;

    /**
     * 删除热搜词
     * @param id
     * @return
     * @throws Exception
     */
    int deleteSearchHotKeyword(int id)
        throws Exception;

    /**
     * 查询搜索记录
     * @param para
     * @return
     * @throws Exception
     */
    Map<String,Object> findAllSearchRecord(Map<String,Object> para)
        throws Exception;

    int countAllSearchRecord(Map<String,Object> para)
        throws Exception;

    int refreshSearchIndex()
        throws Exception;
}
