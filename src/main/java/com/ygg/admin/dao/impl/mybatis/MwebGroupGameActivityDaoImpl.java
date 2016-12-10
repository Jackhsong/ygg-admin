package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MwebGroupGameActivityDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupGameEntity;

@Repository("mwebGroupGameActivityDao")
public class MwebGroupGameActivityDaoImpl extends BaseDaoImpl implements MwebGroupGameActivityDao
{
    
    @Override
    public int countGames(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("MwebGroupGameMapper.countGames", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllGames(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("MwebGroupGameMapper.findAllGames", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int addGame(MwebGroupGameEntity game)
        throws Exception
    {
        
        return this.getSqlSession().insert("MwebGroupGameMapper.addGame", game);
    }
    
    @Override
    public int addGamePrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("MwebGroupGameMapper.addGamePrize", para);
    }
    
    @Override
    public int updateGame(MwebGroupGameEntity game)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupGameMapper.updateGame", game);
    }
    
    @Override
    public int updateGamePrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupGameMapper.updateGamePrize", para);
    }
    
    @Override
    public int updateGameStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupGameMapper.updateGameStatus", para);
    }
    
    @Override
    public int setGameURL(MwebGroupGameEntity game)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupGameMapper.setGameURL", game);
    }
    
    @Override
    public int deleteGame(int id)
        throws Exception
    {
        return this.getSqlSession().delete("MwebGroupGameMapper.deleteGame", id);
    }
    
    @Override
    public int deleteGamePrize(int gameId)
        throws Exception
    {
        return this.getSqlSession().delete("MwebGroupGameMapper.deleteGamePrize", gameId);
    }
    
}
