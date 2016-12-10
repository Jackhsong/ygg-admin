package com.ygg.admin.service;

import java.util.Map;

/**
 * 商家黑名单 服务
 */
public interface SellerBlacklistService
{
    Map<String, Object> findSellerBlackInfo(int type, int sellerId, int isAvailable, int page, int rows)
        throws Exception;
        
    int deleteSellerBlackInfo(int id)
        throws Exception;

    Map<String, Object> saveOrUpdateSellerBlackInfo(Map<String, Object> param)
        throws Exception;
        
}
