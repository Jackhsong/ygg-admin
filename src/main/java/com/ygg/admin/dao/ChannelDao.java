package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface ChannelDao
{
    
    List<Map<String, Object>> findAllChannel(Map<String, Object> para)
        throws Exception;
    
    int countChannel(Map<String, Object> para)
        throws Exception;
    
    int saveChannel(String name)
        throws Exception;
    
    int updateChannel(Map<String, Object> para)
        throws Exception;
    
    int deleteChannel(int id)
        throws Exception;
    
    int findChannelIdByName(String channelName)
        throws Exception;
    
}
