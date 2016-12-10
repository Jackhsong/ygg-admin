package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.GegeImageEntity;

public interface GegeImageDao
{
    
    List<Map<String, Object>> findImageInfo(Map<String, Object> para)
        throws Exception;
    
    int countImageInfo(Map<String, Object> para)
        throws Exception;
    
    int save(GegeImageEntity image, String type)
        throws Exception;
    
    int update(GegeImageEntity image, String type)
        throws Exception;
    
    GegeImageEntity findGegeImageById(int id, String type)
        throws Exception;
    
    boolean checkInUse(int id, String type)
        throws Exception;
    
    int batchDelete(Map<String, Object> para)
        throws Exception;
    
    boolean checkIsExist(GegeImageEntity image, String type)
        throws Exception;
    
    List<GegeImageEntity> findAllGegeImage(String type)
        throws Exception;
    
}
