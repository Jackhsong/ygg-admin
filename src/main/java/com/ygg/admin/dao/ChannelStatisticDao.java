package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.exception.DaoException;

public interface ChannelStatisticDao
{
    
    List<Map<String, Object>> getProductCodeListByTotalPrice(Map<String, Object> para) throws DaoException;
    
    List<Map<String, Object>> getProductCodeListByTotalNum(Map<String, Object> para) throws DaoException;
    
    List<Map<String, Object>> jsonChannelStatisticInfo(Map<String, Object> para) throws DaoException;
    
    int countChannelStatisticInfo(Map<String, Object> para) throws DaoException;
    
    List<Map<String, Object>> jsonChannelProStatisticInfo(Map<String, Object> para) throws DaoException;
    
    int countChannelProStatisticInfo(Map<String, Object> para) throws DaoException;

}
