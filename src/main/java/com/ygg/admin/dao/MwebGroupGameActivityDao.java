package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MwebGroupGameEntity;

public interface MwebGroupGameActivityDao
{
    List<Map<String, Object>> findAllGames(Map<String, Object> para)
        throws Exception;
        
    int countGames(Map<String, Object> para)
        throws Exception;
        
    int addGame(MwebGroupGameEntity game)
        throws Exception;
        
    int updateGame(MwebGroupGameEntity game)
        throws Exception;
        
    int addGamePrize(Map<String, Object> para)
        throws Exception;
        
    int updateGamePrize(Map<String, Object> para)
        throws Exception;
        
    int updateGameStatus(Map<String, Object> para)
        throws Exception;
        
    int setGameURL(MwebGroupGameEntity game)
        throws Exception;
        
    int deleteGame(int id)
        throws Exception;
        
    int deleteGamePrize(int gameId)
        throws Exception;
        
}
