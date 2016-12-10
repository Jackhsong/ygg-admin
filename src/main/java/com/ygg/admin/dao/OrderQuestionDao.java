package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.OrderQuestionProgressEntity;
import com.ygg.admin.entity.OrderQuestionTemplateEntity;

public interface OrderQuestionDao
{
    /**
     * 根据订单Id查询订单问题列表
     * @param orderId
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findOrderQuestionListByOrderId(int orderId)
        throws Exception;
    
    /**
     * 查询订单问题模板
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllQuestionTemplate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计所有订单问题模板
     * @param para
     * @return
     * @throws Exception
     */
    int countQuestionTemplate(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增问题模板
     * @param para
     * @return
     * @throws Exception
     */
    int saveQuestionTemplate(Map<String, Object> para)
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
     * @param question
     * @return
     * @throws Exception
     */
    int addOrderQuestion(OrderQuestionEntity question)
        throws Exception;
    
    /**
     * 新增订单问题图片
     * @param imageList
     * @return
     * @throws Exception
     */
    int insertOrderQuestionImage(List<Map<String, Object>> imageList)
        throws Exception;
    
    /**
     * 根据Id查找订单问题
     * @param id
     * @return
     * @throws Exception
     */
    OrderQuestionEntity findOrderQuestionById(int id)
        throws Exception;
    
    int countDealingOrderQuestionByOrderId(int orderId)
        throws Exception;
    
    List<String> findOrderQuestionImageListqid(int id)
        throws Exception;
    
    List<Map<String, Object>> findCustomerProgressListByqid(int id)
        throws Exception;
    
    List<Map<String, Object>> findSellerProgressListByqid(int id)
        throws Exception;
    
    /**
     * 更新顾客问题进度
     * @param oqpe
     * @return
     * @throws Exception
     */
    int insertCustomerQuestionProgress(OrderQuestionProgressEntity oqpe)
        throws Exception;
    
    /**
     * 插入顾客处理问题图片
     * @param imageList
     * @return
     * @throws Exception
     */
    int insertCustomerQuestionProgressImage(List<Map<String, Object>> imageList)
        throws Exception;
    
    /**
     * 更新商家对接进度
     * @param oqpe
     * @return
     * @throws Exception
     */
    int insertSellerQuestionProgress(OrderQuestionProgressEntity oqpe)
        throws Exception;
    
    /**
     * 插入商家处理问题图片
     * @param imageList
     * @return
     * @throws Exception
     */
    int insertSellerQuestionProgressImage(List<Map<String, Object>> imageList)
        throws Exception;
    
    /**
     * 更新问题状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrderQuestionStatus(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllOrderAndQuestionInfo(Map<String, Object> para)
        throws Exception;
    
    int countOrderAndQuestionInfo(Map<String, Object> para)
        throws Exception;
    
    List<OrderQuestionEntity> findAllOrderQuestion()
        throws Exception;
    
    OrderQuestionTemplateEntity findOrderQuestionTemplateByPara(Map<String, Object> para)
        throws Exception;
    
    int updateQuestionTemplate(Map<String, Object> para)
        throws Exception;
    
    List<String> findCustomerProgressImagesBycid(int cid)
        throws Exception;
    
    List<String> findSellerProgressImagesBysid(int sid)
        throws Exception;
    
    List<Map<String, Object>> findOrderQuestionDescListByOrderId(List<Integer> orderIdList)
        throws Exception;
    
    List<Map<String, Object>> findSellerFeedbackDetailListByqid(int id)
        throws Exception;
    
    int saveOrderQuestionSellerFeedback(Map<String, Object> para)
        throws Exception;
}
