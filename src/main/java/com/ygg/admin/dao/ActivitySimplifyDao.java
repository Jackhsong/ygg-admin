package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface ActivitySimplifyDao
{
    /**
     * 查找所有精品活动
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllActivitySimplify(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计所有精品活动
     * @param para
     * @return
     * @throws Exception
     */
    int countActivitySimplify(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增精品活动
     * @param para
     * @return
     * @throws Exception
     */
    int saveActivitySimplify(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplify(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplifyAvailableStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找精品活动商品
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findActivitySimplifyProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计精品活动商品
     * @param para
     * @return
     * @throws Exception
     */
    int countActivitySimplifyProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动商品
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplifyProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增精品活动商品
     * @param para
     * @return
     * @throws Exception
     */
    int saveActivitySimplifyProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改精品活动商品
     * @param para
     * @return
     * @throws Exception
     */
    int updateActivitySimplifyProduct(Map<String, Object> para)
        throws Exception;

    /**
     * 根据精品活动ID查询旗下所有商品
     * @param id
     * @return
     * @throws Exception
     */
    List<Integer> findActivitySimplifyProductIdBySimplifyActivityId(int id)
        throws Exception;

}
