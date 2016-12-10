package com.ygg.admin.service;

import java.util.Map;

public interface SellerTipService
{
    /**
     * 更新或保存
     * @param param
     * @return
     * @throws Exception
     */
    int saveOrUpdate(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据ID查询
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findById(int id)
        throws Exception;
    
    /**
     * 查询列表
     * @param param
     * @return
     * @throws Exception
     */
    Map<String, Object> findListInfo(Map<String, Object> param)
        throws Exception;
}
