package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CustomGGRecommendEntity;

public interface CustomGGRecommendDao
{
    
    /**
     * 查询推荐列表
     * @param param
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findRecommendListInfo(Map<String, Object> param)
        throws Exception;
    
    /**
     * 统计条数
     * @param param
     * @return
     * @throws Exception
     */
    int countRecommendByParam(Map<String, Object> param)
        throws Exception;
    
    /**
     * 新增推荐
     * @param param
     * @return
     * @throws Exception
     */
    int saveRecommend(Map<String, Object> param)
        throws Exception;
    
    /**
     * 更新推荐
     * @param param
     * @return
     * @throws Exception
     */
    int updateRecommend(Map<String, Object> param)
        throws Exception;
    
    CustomGGRecommendEntity findCustomGGRecommendById(int id)
        throws Exception;
}
