package com.ygg.admin.service;

import com.ygg.admin.entity.SellerExpandEntity;

public interface SellerExpandService
{
    
    SellerExpandEntity findSellerExpandBysid(int id)
        throws Exception;
    
    int updateEcount(int sellerId) throws Exception;
}
