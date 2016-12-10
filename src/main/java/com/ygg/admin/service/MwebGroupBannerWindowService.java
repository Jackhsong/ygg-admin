package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MwebGroupBannerWindowEntity;


public interface MwebGroupBannerWindowService
{
    int save(MwebGroupBannerWindowEntity window)
        throws Exception;
    
    int update(MwebGroupBannerWindowEntity window)
        throws Exception;
    
    int countBannerWindow(Map<String, Object> para)
        throws Exception;
    
    List<MwebGroupBannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception;
    
    MwebGroupBannerWindowEntity findBannerWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> packageBannerWindowList(List<MwebGroupBannerWindowEntity> bList)
        throws Exception;
    
    /**
     * 更新排序值
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateOrder(Map<String, Object> para)
        throws Exception;
    
}
