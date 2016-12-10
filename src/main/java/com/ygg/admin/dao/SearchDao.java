package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyb on 2015/8/24 0024.
 */
public interface SearchDao
{
    /**
     * 查找搜索词
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> findAllSearchHotKeyword(Map<String,Object> para)
        throws Exception;

    /**
     * 统计搜索词总数
     * @param para
     * @return
     * @throws Exception
     */
    int countAllSearchHotKeyword(Map<String,Object> para)
        throws Exception;

    /**
     * 删除搜索词
     * @param id
     * @return
     * @throws Exception
     */
    int deleteSearchHotKeyword(int id)
        throws Exception;

    /**
     * 更新搜索词
     * @param para
     * @return
     * @throws Exception
     */
    int updateSearchHotKeyword(Map<String,Object> para)
        throws Exception;

    /**
     * 保存热搜词
     * @param para
     * @return
     */
    int saveSearchHotKeyword(Map<String,Object> para);

    /**
     * 查找用户搜索记录
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> findAllSearchRecord(Map<String,Object> para)
        throws Exception;

    /**
     * 用户搜索记录总数
     * @param para
     * @return
     * @throws Exception
     */
    int countAllSearchRecord(Map<String,Object> para)
        throws Exception;
}
