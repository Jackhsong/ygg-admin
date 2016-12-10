package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface RefundReasonDao
{

    int saveRefundReason(Map<String, Object> param) throws Exception;
    
    int updateRefundReason(Map<String, Object> param) throws Exception;
    
    List<Map<String, Object>> findRefundReasonList(Map<String, Object> param) throws Exception;
    
    int countRefundReason() throws Exception;
    
    Map<String, Object> findRefundReasonById(Map<String, Object> param) throws Exception;
}
