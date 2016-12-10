package com.ygg.admin.dao.qqbs.reward.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.dao.qqbs.reward.QqbsAllRewardThrDao;
import com.ygg.admin.entity.qqbs.QqbsRewardThrEntity;

@Repository
public class QqbsAllRewardThrDaoImpl extends BaseDaoImpl implements QqbsAllRewardThrDao
{
    @Override
    public List<QqbsRewardThrEntity> getAllRewardThr(int accountId) throws Exception
    {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("accountId", accountId);
        return this.getSqlSessionRead().selectList("QqbsRewardThrMapper.getAllRewardThr",map);
    }
}
