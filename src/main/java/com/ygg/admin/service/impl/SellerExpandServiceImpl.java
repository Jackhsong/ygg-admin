package com.ygg.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.SellerExpandDao;
import com.ygg.admin.entity.SellerExpandEntity;
import com.ygg.admin.service.SellerExpandService;

@Service("sellerExpandService")
public class SellerExpandServiceImpl implements SellerExpandService
{
    @Resource
    private SellerExpandDao sellerExpandDao;
    
    @Override
    public SellerExpandEntity findSellerExpandBysid(int sellerId)
        throws Exception
    {
        return sellerExpandDao.findSellerExpandBysid(sellerId);
    }

    @Override
    public int updateEcount(int sellerId)
        throws Exception
    {
        return sellerExpandDao.updateEcount(sellerId);
    }
    
    
    
}
