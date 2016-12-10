package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.BannerWindowEntity;

public interface QqbsBannerWindowDao
{
    int save(BannerWindowEntity window)
        throws Exception;
    
    int update(BannerWindowEntity window)
        throws Exception;
    
    int countBannerWindow(Map<String, Object> para)
        throws Exception;
    
    List<BannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception;
    
    BannerWindowEntity findBannerWindowById(int id)
        throws Exception;
    
    int updateDisplayCode(Map<String, Object> para)
        throws Exception;
    
    int updateOrder(Map<String, Object> para)
        throws Exception;
}
