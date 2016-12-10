package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.SpreadChannelDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.SpreadChannelEntity;

@Repository("spreadChannelDao")
public class SpreadChannelDaoImpl extends BaseDaoImpl implements SpreadChannelDao
{
    
    @Override
    public List<Map<String, Object>> findAllSpreadChannels(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SpreadChannelMapper.findAllSpreadChannels", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countSpreadChannels(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("SpreadChannelMapper.countSpreadChannels", para);
    }
    
    @Override
    public List<Map<String, Object>> findPrizeByChannelId(int channelId)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSessionRead().selectList("SpreadChannelMapper.findPrizeByChannelId", channelId);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int addSpreadChannel(SpreadChannelEntity channel)
        throws Exception
    {
        return this.getSqlSession().insert("SpreadChannelMapper.addSpreadChannel", channel);
    }
    
    @Override
    public int addSpreadChannelPrize(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("SpreadChannelMapper.addSpreadChannelPrize", para);
    }
    
    @Override
    public int updateSpreadChannel(SpreadChannelEntity channel)
        throws Exception
    {
        
        return this.getSqlSession().update("SpreadChannelMapper.updateSpreadChannel", channel);
    }
    
    @Override
    public int deleteSpreadChannelPrizeById(int channelId)
        throws Exception
    {
        return this.getSqlSession().delete("SpreadChannelMapper.deleteSpreadChannelPrizeById", channelId);
    }
    
    @Override
    public int deleteSpreadChannel(int id)
        throws Exception
    {
        return this.getSqlSession().delete("SpreadChannelMapper.deleteSpreadChannel", id);
    }
    
    @Override
    public int updateChannelStatus(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SpreadChannelMapper.updateChannelStatus", para);
    }
    
    @Override
    public int updateSpreadChannelURL(SpreadChannelEntity channel)
    {
        return this.getSqlSession().update("SpreadChannelMapper.updateSpreadChannelURL", channel);
    }
}
