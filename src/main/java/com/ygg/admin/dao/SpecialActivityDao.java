package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SpecialActivityEntity;

public interface SpecialActivityDao
{
    /**
     * 特卖活动列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllSpecialActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计特卖活动
     * @param para
     * @return
     * @throws Exception
     */
    int countSpecialActivity(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增特卖活动
     * @param sae
     * @return
     * @throws Exception
     */
    int saveSpecialActivity(SpecialActivityEntity sae)
        throws Exception;
    
    /**
     * 更新特卖活动
     * @param sae
     * @return
     * @throws Exception
     */
    int updateSpecialActivity(SpecialActivityEntity sae)
        throws Exception;
    
    /**
     * 更新特卖活动可用状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityAvailableStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据特卖活动Id查找特卖活动板块
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllSpecialActivityLayout(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计特卖活动板块
     * @param para
     * @return
     * @throws Exception
     */
    int countSpecialActivityLayout(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增特卖活动板块
     * @param customLayout
     * @return
     * @throws Exception
     */
    int saveSpecialActivityLayout(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改特卖活动板块
     * @return
     * @throws Exception
     */
    int updateSpecialActivityLayout(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找特卖活动板块排序最大值
     * @param saId：特卖活动Id
     * @return
     * @throws Exception
     */
    int findMaxSpecialActivityLayoutSequenceByActivityId(int saId)
        throws Exception;
    
    /**
     * 修改特卖活动板块排序值
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityLayoutSequence(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改特卖活动板块站下状态
     * @param para
     * @return
     * @throws Exception
     */
    int updateSpecialActivityLayoutDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找板块商品
     * @param para
     * @return
     */
    List<Map<String, Object>> findSpecialActivityLayouProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计板块商品
     * @param layoutId
     * @return
     * @throws Exception
     */
    int countSpecialActivityLayouProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改板块商品
     * @param para
     * @return
     */
    int updateSpecialActivityLayoutProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找办款商品布局
     * @param layoutProductId
     * @return
     * @throws Exception
     */
    Map<String, Object> findSpecialActivityLayoutProduct(int layoutProductId)
        throws Exception;
    
    /**
     * 新增板块商品
     * @param para
     * @return
     */
    int insertSpecialActivityLayoutProduct(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找板块商品序号
     * @param layoutId
     * @return
     * @throws Exception
     */
    int findMaxSpecialActivityLayoutProductSequenceByActivityId(int layoutId)
        throws Exception;

    /**
     * 根据 SpecialActivityId 查询旗下所有商品
     * @param SpecialActivityId
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> findSpecialActivityLayouProductBySpecialActivityId(int SpecialActivityId)
        throws Exception;
}
