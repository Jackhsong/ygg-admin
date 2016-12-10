package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderProductCommentEntity;

public interface ProductCommentService
{
    
    Map<String, Object> jsonProductCommentInfo(Map<String, Object> para)
        throws Exception;
    
    OrderProductCommentEntity findProductCommentById(int id)
        throws Exception;
    
    /**
     * 加载基本商品评论列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonProductBaseCommentInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 加载基本商品详细评论列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonProductCommentDetailList(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据参数查找商品评论信息
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findProductCommentByPara(int id)
        throws Exception;
    
    /**
     * 更新评论展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductCommentDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 回复商品评论
     * @param para
     * @return
     * @throws Exception
     */
    int replayProductComment(Map<String, Object> para)
        throws Exception;

    /**
     * 处理反馈
     * @param id
     * @param dealContent
     * @return
     * @throws Exception
     */
    int updateDealContent(int id, String dealContent)
        throws Exception;
    
    /**
     * 更新只展现文本或展现文本和图片
     * @param para
     * @return
     * @throws Exception
     */
    int updateProductCommentDisplayTextStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找是否存在评论 且 为差评时，左岸城堡回复不能为空
     * @param para
     * @return
     * @throws Exception
     */
    List<OrderProductCommentEntity> findProCommentsByIds(Map<String, Object> para)
            throws Exception;

    /**
     * 根据基本商品id和评价等级获取评论率
     * @param productId
     * @param commentLevel
     * @return
     * @throws Exception
     */
    String getProductBaseCommentRateById(int productId, int commentLevel)
        throws Exception;
}
