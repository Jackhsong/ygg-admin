package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.GroupProductCodeEntity;

public interface GroupBuyDao
{
    /**
     * 根据para查询 团购商品口令
     * @param para
     * @return
     * @throws Exception
     */
    List<GroupProductCodeEntity> findAllGroupProductCodeByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询商品团购次数
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllGourpProductNums(Map<String, Object> para)
        throws Exception;
    
    int countAllGourpProductNums(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计团购人数
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findGroupTotalPeople(Map<String, Object> para)
        throws Exception;
}
