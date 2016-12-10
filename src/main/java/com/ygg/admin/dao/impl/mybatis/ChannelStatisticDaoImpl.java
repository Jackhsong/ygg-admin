package com.ygg.admin.dao.impl.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ChannelStatisticDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.exception.DaoException;

@Repository
public class ChannelStatisticDaoImpl extends BaseDaoImpl implements ChannelStatisticDao
{

    @Override
    public List<Map<String, Object>> jsonChannelStatisticInfo(Map<String, Object> para) throws DaoException
    {
        return getSqlSessionRead().selectList("ChannelStatisticMapper.getChannelStatisticInfo",para);
    }

    @Override
    public int countChannelStatisticInfo(Map<String, Object> para) throws DaoException
    {
        return getSqlSessionRead().selectOne("ChannelStatisticMapper.countChannelStatisticInfo",para);
    }

    @Override
    public List<Map<String, Object>> jsonChannelProStatisticInfo(Map<String, Object> para) throws DaoException
    {
        
        return getSqlSessionRead().selectList("ChannelStatisticMapper.getChannelProStatisticInfo",para);
    }

    @Override
    public int countChannelProStatisticInfo(Map<String, Object> para) throws DaoException
    {
        return getSqlSessionRead().selectOne("ChannelStatisticMapper.countChannelProStatisticInfo",para);
    }

    @Override
    public List<Map<String, Object>> getProductCodeListByTotalPrice(
            Map<String, Object> para) throws DaoException
    {
        return getSqlSessionRead().selectList("ChannelStatisticMapper.getProductCodeListByTotalPrice",para);
    }

    @Override
    public List<Map<String, Object>> getProductCodeListByTotalNum(
            Map<String, Object> para) throws DaoException
    {
        return getSqlSessionRead().selectList("ChannelStatisticMapper.getProductCodeListByTotalNum",para);
    }
    
}
