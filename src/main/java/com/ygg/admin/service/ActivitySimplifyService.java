package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface ActivitySimplifyService
{
    /**
     * 精品活动列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonActivitySimplifyInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改精品活动
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdateActivitySimplify(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplifyAvailableStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id查找精品活动
     * @param activityId
     * @return
     */
    Map<String, Object> findActivitySimplifyById(int activityId)
        throws Exception;
    
    /**
     * 精品活动布局列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonActivitySimplifyLayoutProductInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动商品展现状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplifyProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动商品排序值
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplifyProductSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增/修改精品活动商品
     * @param para
     * @return
     * @throws Exception
     */
    int saveOrUpdateActivitySimplifyProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找所有精品活动
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllActivitySimplify(Map<String, Object> para)
        throws Exception;
    
}
