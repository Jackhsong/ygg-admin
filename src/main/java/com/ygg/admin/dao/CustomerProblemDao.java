package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface CustomerProblemDao
{
    
    List<Map<String, Object>> findAllCustomerProblem(Map<String, Object> para)
        throws Exception;
    
    int countCustomerProblem(Map<String, Object> para)
        throws Exception;
    
    int saveCustomerProblem(Map<String, Object> para)
        throws Exception;
    
    int updateCustomerProblem(Map<String, Object> para)
        throws Exception;
    
}
