package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface SignInService
{
    
    Map<String, Object> findAllSignSetting(Map<String, Object> para)
        throws Exception;
    
    int beginYearMonth()
        throws Exception;
    
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
    
    Map<String, Integer> copyFromLastMonth(Map<String, Object> para)
        throws Exception;
    
    Map<String, Object> findAllSigninProduct(Map<String, Object> para)
        throws Exception;
    
    String addSigninProduct(int productId, int sequence)
        throws Exception;
    
    String deleteSigninProduct(List<String> asList)
        throws Exception;
    
    String updateSigninProductDisplayStatus(List<String> asList, int isDisplay)
        throws Exception;
    
    String updateSigninProductSequence(int id, int sequence)
        throws Exception;
    
}
