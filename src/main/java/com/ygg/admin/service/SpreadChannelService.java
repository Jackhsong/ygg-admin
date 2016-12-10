package com.ygg.admin.service;

import java.util.Map;

public interface SpreadChannelService
{
    
    Map<String, Object> findAllSpreadChannels(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
    
    int deleteSpreadChannel(int id)
        throws Exception;
    
    int updateChannelAvailable(Map<String, Object> para)
        throws Exception;
    
    int updateChannelSendMsg(Map<String, Object> para)
        throws Exception;
    
    int editMsgContent(Map<String, Object> para)
        throws Exception;
    
}
