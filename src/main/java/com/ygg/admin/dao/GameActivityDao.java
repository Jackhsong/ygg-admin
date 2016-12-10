package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.GameEntity;

public interface GameActivityDao
{
    List<Map<String, Object>> findAllGames(Map<String, Object> para)
        throws Exception;
    
    int countGames(Map<String, Object> para)
        throws Exception;
    
    int addGame(GameEntity game)
        throws Exception;
    
    int updateGame(GameEntity game)
        throws Exception;
    
    int addGamePrize(Map<String, Object> para)
        throws Exception;
    
    int updateGamePrize(Map<String, Object> para)
        throws Exception;
    
    int updateGameStatus(Map<String, Object> para)
        throws Exception;
    
    int setGameURL(GameEntity game)
        throws Exception;
    
    int deleteGame(int id)
        throws Exception;
    
    int deleteGamePrize(int gameId)
        throws Exception;
    
}
