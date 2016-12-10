package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QueryImageDao
{
    
    List<String> findActivitiesCommonImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findBannerWindowImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findBrandImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findGegeImageActivitiesImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findGegeImageProductImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findOrderProductImage(Map<String, Object> para)
        throws Exception;
    
    Set<String> findOrderProductRefundImage(Map<String, Object> para)
        throws Exception;
    
    Set<String> findProductImage(Map<String, Object> para)
        throws Exception;
    
    Set<String> findProductCommonImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findProductMobileDetailImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findSaleTagImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findSaleWindowImage(Map<String, Object> para)
        throws Exception;
    
    Set<String> findProductBaseImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findProductBaseMobileDetailImage(Map<String, Object> para)
        throws Exception;
    
    List<String> findAccountImage(Map<String, Object> para)
        throws Exception;
    
}
