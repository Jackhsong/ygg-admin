package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface SellerAdminOrderService
{
    
    List<Map<String, Object>> findSendTimeoutOrderInfo(List<Integer> sellerIdList, String selectDate)
        throws Exception;
    
    Map<String, Object> jsonSellerQuestionListInfo(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findOrderQuestionDetailInfo(int id)
        throws Exception;
    
    /**
     * 商家订单问题反馈
     * @param questionId
     * @param content
     * @return
     * @throws Exception
     */
    String updateSellerOrderQuestion(int questionId, String content, String sellerName)
        throws Exception;
    
    List<Map<String, Object>> findLogisticsTimeoutOrderInfo(List<Integer> sellerIdList, String selectDate)
        throws Exception;
    
    Map<String, Object> findSendTimeoutComplainOrder(Map<String, Object> para)
        throws Exception;
    
}
