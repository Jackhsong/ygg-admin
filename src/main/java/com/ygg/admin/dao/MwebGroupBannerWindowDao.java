package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MwebGroupBannerWindowEntity;

public interface MwebGroupBannerWindowDao
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
    
    int updateOrder(Map<String, Object> para)
        throws Exception;
}
