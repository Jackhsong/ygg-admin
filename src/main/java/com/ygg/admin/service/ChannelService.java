package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface ChannelService
{
    
    String jsonChannelInfo(Map<String, Object> para)
        throws Exception;
    
    String saveChannel(String name)
        throws Exception;
    
    String updateChannel(int id, String name)
        throws Exception;
    
    String deleteChannel(int id)
        throws Exception;
    
    List<Map<String, Object>> findAllChannelByPara(Map<String, Object> para)
        throws Exception;
    
}
