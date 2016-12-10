package com.ygg.admin.dao.qqbs.reward;

import java.util.List;

import com.ygg.admin.entity.qqbs.QqbsRewardThrEntity;

public interface QqbsAllRewardThrDao
{
    
    /**
     * 按照account_id,获取该用户所有奖励
     * @param accountId
     * @return
     * @throws Exception
     */
    public List<QqbsRewardThrEntity> getAllRewardThr(int accountId) throws Exception;

}
