package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ChannelDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.util.CommonConstant;

@Repository
public class ChannelDaoImpl extends BaseDaoImpl implements ChannelDao
{
    
    @Override
    public List<Map<String, Object>> findAllChannel(Map<String, Object> para)
        throws Exception
    {
        
        return getSqlSessionRead().selectList("ChannelMapper.findAllChannel", para);
    }
    
    @Override
    public int countChannel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ChannelMapper.countChannel", para);
    }
    
    @Override
    public int saveChannel(String name)
        throws Exception
    {
        return getSqlSession().insert("ChannelMapper.saveChannel", name);
    }
    
    @Override
    public int updateChannel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ChannelMapper.updateChannel", para);
    }
    
    @Override
    public int deleteChannel(int id)
        throws Exception
    {
        return getSqlSession().delete("ChannelMapper.deleteChannel", id);
    }
    
    @Override
    public int findChannelIdByName(String channelName)
        throws Exception
    {
        Integer channelId = getSqlSessionRead().selectOne("ChannelMapper.findChannelIdByName", channelName);
        return channelId == null ? CommonConstant.COMMON_NO : channelId.intValue();
    }
}
