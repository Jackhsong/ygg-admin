package com.ygg.admin.service;

import java.util.Map;

import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.OrderQuestionTemplateEntity;

public interface OrderQuestionService
{
    /**
     * 根据订单Id查询订单问题
     * @param orderId
     * @return
     * @throws Exception
     */
    String findOrderQuestionInfoStr(int orderId)
        throws Exception;
    
    /**
     * 订单问题模版
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonQuestionTemplateInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增问题模板
     * @param id 
     * @param name
     * @param limitHour :时限
     * @return
     * @throws Exception
     */
    int saveQuestionTemplate(int id, String name, String limitHour)
        throws Exception;
    
    /**
     * 更新问题模板可用状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderQuestionTemplateStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增订单问题
     * @param para
     * @return
     * @throws Exception
     */
    int addOrderQuestion(OrderQuestionEntity oqe, String[] imageArray)
        throws Exception;
    
    /**
     * 查看问题详细记录
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderQuestionDetailInfo(int id)
        throws Exception;
    
    /**
     * 更新问题进度
     * @param para
     * @param imageArray
     * @return
     * @throws Exception
     */
    int updateOrderQuestionProgress(Map<String, Object> para, String[] imageArray)
        throws Exception;
    
    Map<String, Object> jsonQuestionListInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据订单问题模板Id查询模版
     * @param templateId
     * @return
     * @throws Exception
     */
    OrderQuestionTemplateEntity findOrderQuestionTemplateById(int templateId)
        throws Exception;
    
    /**
     * 根据订单问题模板名称查询模版
     * @param templateId
     * @return
     * @throws Exception
     */
    OrderQuestionTemplateEntity findOrderQuestionTemplateByName(String name)
        throws Exception;
    
    String updateOrderQuestionMark(int id, int isMark)
        throws Exception;
    
    String updateOrderQuestionPushStatus(int id)
        throws Exception;
}
