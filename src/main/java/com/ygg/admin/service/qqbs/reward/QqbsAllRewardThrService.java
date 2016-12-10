package com.ygg.admin.service.qqbs.reward;

import java.util.Map;

public interface QqbsAllRewardThrService
{
    /**
     * 按照account_id,获取该用户所有奖励
     * @param accountId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getAllRewardThr(int accountId) throws Exception;
}
