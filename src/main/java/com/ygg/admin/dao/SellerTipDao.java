package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SellerTipDao
{
    /**
     * 保存商家提示信息
     * @param param
     * @return
     * @throws Exception
     */
    int save(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新商家提示信息
     * @param param
     * @return
     * @throws Exception
     */
    int update(Map<String, Object> param)
        throws Exception;
    
    /**
     * 统计条数
     * @param param
     * @return
     * @throws Exception
     */
    int countList(Map<String, Object> param)
        throws Exception;
    
    /**
     * 查询列表
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findListInfo(Map<String, Object> param)
        throws Exception;
    
    /**
     * 查询单条
     * @param int
     * @return
     * @throws Exception
     */
    Map<String, Object> findById(int id)
        throws Exception;
    
    /**
     * 判断标题是否重复
     * @param param
     * @return
     * @throws Exception
     */
    int checkTitle(Map<String, Object> param)
        throws Exception;
    
    /**
     * 判断展示状态是否重复
     * @param param
     * @return
     * @throws Exception
     */
    int checkStatus(Map<String, Object> param)
        throws Exception;
}
