package com.ygg.admin.service.qqbs.reward.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.qqbs.reward.QqbsAllRewardThrDao;
import com.ygg.admin.entity.qqbs.QqbsRewardThrEntity;
import com.ygg.admin.service.qqbs.reward.QqbsAllRewardThrService;

@Repository
public class QqbsAllRewardThrServiceImpl implements QqbsAllRewardThrService
{
    
    @Resource
    private QqbsAllRewardThrDao qqbsAllRewardThrDao;

    @Override
    public Map<String, Object> getAllRewardThr(int accountId)
            throws Exception
    {
        List<QqbsRewardThrEntity> rewardList = qqbsAllRewardThrDao.getAllRewardThr(accountId);
        Map<String, Object>  resultMap = new HashMap<String,Object>();
        resultMap.put("rows", rewardList);
        resultMap.put("total", rewardList.size());
        return resultMap;
    }
    
}
