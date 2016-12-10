package com.ygg.admin.service.yw.banner;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.BannerWindowEntity;

public interface YwBannerService
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
    
    List<Map<String, Object>> packageBannerWindowList(List<BannerWindowEntity> bList)
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
    
    boolean checkIsExist(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> checkProductTime(Map<String, Object> para)
        throws Exception;
}
