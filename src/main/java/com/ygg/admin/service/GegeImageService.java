package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.GegeImageEntity;

public interface GegeImageService
{
    
    Map<String, Object> jsonImageInfo(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdate(GegeImageEntity image, String type)
        throws Exception;
    
    GegeImageEntity findGegeImageById(int id, String type)
        throws Exception;
    
    boolean checkIsUse(int id, String type)
        throws Exception;
    
    int delete(Map<String, Object> para)
        throws Exception;
    
    int batchDelete(Map<String, Object> para)
        throws Exception;
    
    boolean checkIsExist(GegeImageEntity image, String type)
        throws Exception;
    
    List<GegeImageEntity> findAllGegeImage(String type)
        throws Exception;
    
}
