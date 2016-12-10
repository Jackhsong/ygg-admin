package com.ygg.admin.service;

import java.util.Map;

public interface RefundReasonService
{
    
    /**
     * 保存退款原因
     * 
     * @param param
     * @return
     * @throws Exception
     */
    int saveRefundReason(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新退款原因
     * 
     * @param param
     * @return
     * @throws Exception
     */
    int updateRefundReason(Map<String, Object> param)
        throws Exception;
    
    /**
     * 查询退款原因列表
     * 
     * @param param
     * @return
     * @throws Exception
     */
    Map<String, Object> findRefundReasonList(Map<String, Object> param)
        throws Exception;
    
    /**
     * 根据ID查询退款原因
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findRefundReasonById(int id)
        throws Exception;
}
