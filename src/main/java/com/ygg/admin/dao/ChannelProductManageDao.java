package com.ygg.admin.dao;

import com.ygg.admin.entity.ChannelProductEntity;
import com.ygg.admin.exception.DaoException;

import java.util.List;
import java.util.Map;

public interface ChannelProductManageDao
{
    int addChannelProduct(ChannelProductEntity product) throws DaoException;
    
    int updateChannelProduct(ChannelProductEntity product) throws DaoException;
    
    Map<String, Object> getProductName(Map<String, Object> para) throws DaoException;
    
    List<Map<String, Object>> jsonChannelProInfo(Map<String, Object> para) throws DaoException;
    
    int countChannelProInfo(Map<String, Object> para) throws DaoException;
    
    List<Map<String,Object>> getAllChannelNameAndId() throws DaoException;
    
    int batchAddChannelProduct(List<ChannelProductEntity> productList) throws DaoException;

    /**
     * 查询第三方订单商品
     */
    List<Map<String,Object>> findChannelProductByPara(Map<String, Object> para);
    
}
