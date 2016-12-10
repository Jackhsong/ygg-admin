package com.ygg.admin.service;

import java.util.Map;

public interface MwebGroupGameActivityService
{
    
    Map<String, Object> findAllGames(Map<String, Object> para)
        throws Exception;
    
    int saveOrUpdateGame(Map<String, Object> para)
        throws Exception;
    
    int updateGameAvailable(Map<String, Object> para)
        throws Exception;
    
    int updateGameSendMsg(Map<String, Object> para)
        throws Exception;
    
    int deleteGame(int id)
        throws Exception;
    
    int editMsgContent(Map<String, Object> para)
        throws Exception;
    
}
