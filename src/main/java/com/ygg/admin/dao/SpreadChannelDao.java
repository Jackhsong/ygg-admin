package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.SpreadChannelEntity;

public interface SpreadChannelDao
{
    
    List<Map<String, Object>> findAllSpreadChannels(Map<String, Object> para)
        throws Exception;
    
    int countSpreadChannels(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findPrizeByChannelId(int channelId)
        throws Exception;
    
    int addSpreadChannel(SpreadChannelEntity channel)
        throws Exception;
    
    int addSpreadChannelPrize(Map<String, Object> para)
        throws Exception;
    
    int updateSpreadChannel(SpreadChannelEntity channel)
        throws Exception;
    
    int deleteSpreadChannelPrizeById(int id)
        throws Exception;
    
    int deleteSpreadChannel(int id)
        throws Exception;
    
    int updateChannelStatus(Map<String, Object> para)
        throws Exception;
    
    int updateSpreadChannelURL(SpreadChannelEntity channel);
    
}
