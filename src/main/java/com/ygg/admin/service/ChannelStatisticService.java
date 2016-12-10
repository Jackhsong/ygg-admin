package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.exception.DaoException;

public interface ChannelStatisticService
{
    /**
     * 获取商品列表
     */
    List<String> getAllChannelNameAndId(int type) throws DaoException;
    
    /**
     * 获取渠道统计
     */
    Map<String, Object> jsonChannelStatisticInfo(Map<String, Object> para)
            throws Exception;
    
    /**
     * 获取金额统计
     */
    Map<String, Object> jsonChannelProStatisticInfo(Map<String, Object> para)
            throws Exception;
}
