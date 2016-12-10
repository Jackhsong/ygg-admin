package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 *
 * @author xiongl
 * @create 2016-05-18 15:38
 */
public interface OrderProductRefundReasonDao
{
    
    int countOrderProductRefundReason(Map<String, Object> param)
        throws Exception;
        
    List<Map<String, Object>> findOrderProductRefundReasonList(Map<String, Object> param)
        throws Exception;
        
    int save(Map<String, Object> param)
        throws Exception;
        
    int update(Map<String, Object> param)
        throws Exception;

    int delete(int id)
        throws Exception;
}
