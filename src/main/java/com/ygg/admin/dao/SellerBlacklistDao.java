package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

/**
 * 商家黑名单商品 dao
 */
public interface SellerBlacklistDao
{
    List<Map<String, Object>> findSellerBlackInfo(int type, int sellerId, int isAvailable, int page, int rows)
            throws Exception;

    int countSellerBlackInfo(int type, int sellerId, int isAvailable)
            throws Exception;

    int saveSellerBlackInfo(Map<String, Object> para)
            throws Exception;

    int updateSellerBlackInfo(Map<String, Object> para)
            throws Exception;

    int deleteSellerBlackInfo(int id)
            throws Exception;
}
