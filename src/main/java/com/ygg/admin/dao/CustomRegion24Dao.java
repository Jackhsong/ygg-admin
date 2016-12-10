package com.ygg.admin.dao;

import com.ygg.admin.entity.CustomLayoutEntity;
import com.ygg.admin.entity.CustomRegionEntity;

import java.util.List;
import java.util.Map;

public interface CustomRegion24Dao
{
    
    List<Map<String, Object>> findAllCustomRegion(Map<String, Object> para)
        throws Exception;
    
    int countCustomRegion(Map<String, Object> para)
        throws Exception;
    
    int saveCustomRegion(Map<String, Object> para)
        throws Exception;
    
    int updateCustomRegion(Map<String, Object> para)
        throws Exception;
    
    int updateCustomRegionAvailableStatus(Map<String, Object> para)
        throws Exception;
    
    int updateCustomRegionDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int findMaxCustomRegionSequence()
        throws Exception;
    
    List<Map<String, Object>> findAllCustomLayout(Map<String, Object> para)
        throws Exception;
    
    int countCustomLayout(Map<String, Object> para)
        throws Exception;
    
    int updateCustomLayoutDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int updateCustomLayoutSequence(Map<String, Object> para)
        throws Exception;
    
    int addCustomLayout(CustomLayoutEntity customLayout)
        throws Exception;
    
    int updateCustomLayout(CustomLayoutEntity customLayout)
        throws Exception;
    
    int getCustonLayoutMaxSequence(int regionId)
        throws Exception;
    
    int insertRelationCustomRegionLayout(Map<String, Object> para)
        throws Exception;
    
    int deleteCustomLayout(int id)
        throws Exception;
    
    CustomLayoutEntity findCustomLayoutById(int customLayoutId)
        throws Exception;
    
    CustomRegionEntity findCustomRegionById(int id)
        throws Exception;
    
    List<Integer> findCustomLayoutIdByCustomRegionId(int id)
        throws Exception;
    
}
