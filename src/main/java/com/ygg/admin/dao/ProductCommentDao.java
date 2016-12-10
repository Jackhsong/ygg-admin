package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.OrderProductCommentEntity;

public interface ProductCommentDao
{
    
    List<Map<String, Object>> findAllProductComment(Map<String, Object> para)
        throws Exception;
    
    int countProductComment(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllProductBaseComment(Map<String, Object> para)
        throws Exception;
    
    int countProductBaseComment(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllProductBaseCommentLevel(int level)
        throws Exception;
    
    List<Map<String, Object>> findAllProductCommentDetail(Map<String, Object> para)
        throws Exception;
    
    int countProductCommentDetail(Map<String, Object> para)
        throws Exception;
    
    OrderProductCommentEntity findProductCommentById(int id)
        throws Exception;
    
    int updateProductCommentDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int replayProductComment(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllProductBaseTotalComment()
        throws Exception;

    /**
     * 处理反馈
     * @param para
     * @return
     * @throws Exception
     */
    int updateDealContent(Map<String, Object> para)
        throws Exception;
    
    int updateProductCommentDisplayTextStatus(Map<String, Object> para)
            throws Exception;
    
    List<OrderProductCommentEntity> findProCommentsByIds(Map<String, Object> para)
            throws Exception;

    /**
     * 根据条件查找基本商品评论数量
     * @param param
     * @return
     * @throws Exception
     */
    int findProductBaseCommentByParam(Map<String, Object> param)
        throws Exception;
    
}
