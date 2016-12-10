package com.ygg.admin.dao.qqbs.reward.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.reward.QqbsNewGuyRewardThrDao;

@Repository
public class QqbsNewGuyRewardThrDaoImpl extends BaseDaoImpl implements QqbsNewGuyRewardThrDao
{

    @Override
    public double getFansOrderPrice(int accountId) throws Exception
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("accountId", accountId);
        return this.getSqlSessionRead().selectOne("QqbsRewardThrMapper.getFansOrderPrice",map);
    }

    @Override
    public List<Map<String,Object>> getFansNum(int accountId) throws Exception
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("accountId", accountId);
        return this.getSqlSessionRead().selectList("QqbsRewardThrMapper.getFansNum",map);

    }
}
