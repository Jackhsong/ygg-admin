package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.GameActivityDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.GameEntity;

@Repository("gameActivityDao")
public class GameActivityDaoImpl extends BaseDaoImpl implements GameActivityDao
{
    
    @Override
    public int countGames(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("GameMapper.countGames", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllGames(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("GameMapper.findAllGames", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int addGame(GameEntity game)
        throws Exception
    {
        
        return this.getSqlSession().insert("GameMapper.addGame", game);
    }
    
    @Override
    public int addGamePrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("GameMapper.addGamePrize", para);
    }
    
    @Override
    public int updateGame(GameEntity game)
        throws Exception
    {
        return this.getSqlSession().update("GameMapper.updateGame", game);
    }
    
    @Override
    public int updateGamePrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("GameMapper.updateGamePrize", para);
    }
    
    @Override
    public int updateGameStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("GameMapper.updateGameStatus", para);
    }
    
    @Override
    public int setGameURL(GameEntity game)
        throws Exception
    {
        return this.getSqlSession().update("GameMapper.setGameURL", game);
    }
    
    @Override
    public int deleteGame(int id)
        throws Exception
    {
        return this.getSqlSession().delete("GameMapper.deleteGame", id);
    }
    
    @Override
    public int deleteGamePrize(int gameId)
        throws Exception
    {
        return this.getSqlSession().delete("GameMapper.deleteGamePrize", gameId);
    }
    
}
