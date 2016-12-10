package com.ygg.admin.service;

import java.util.Map;

/**
 * @author xiongl
 * @create 2016-05-18 15:17
 */
public interface OrderProductRefundReasonService
{
    Object findOrderProductRefundReasonList(Map<String, Object> param)
        throws Exception;
        
    Object save(String reason, int isDisplay)
        throws Exception;
        
    Object upadte(int id, String reason, int isDisplay)
        throws Exception;

    Object delete(int id)
        throws Exception;
}

