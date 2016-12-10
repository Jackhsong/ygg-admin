package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface SignInDao
{
    
    List<Map<String, Object>> findAllSignSetting(Map<String, Object> para)
        throws Exception;
    
    int countSignSetting(Map<String, Object> para)
        throws Exception;
    
    Integer getBeginYearMonth()
        throws Exception;
    
    int findCurrentDayByYearMonth(int yearMonth)
        throws Exception;
    
    int insert(Map<String, Object> para)
        throws Exception;
    
    int update(Map<String, Object> para)
        throws Exception;
    
    int delete(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAccountSignCount(int currentYearMonth)
        throws Exception;
    
    List<Map<String, Object>> findAllSigninProduct(Map<String, Object> para)
        throws Exception;
    
    int countSigninProduct(Map<String, Object> para)
        throws Exception;
    
    int addSigninProduct(Map<String, Object> para)
        throws Exception;
    
    int deleteSigninProduct(List<String> idList)
        throws Exception;
    
    int updateSigninProductDisplayStatus(Map<String, Object> para)
        throws Exception;
    
    int updateSigninProductSequence(Map<String, Object> para)
        throws Exception;
    
}
