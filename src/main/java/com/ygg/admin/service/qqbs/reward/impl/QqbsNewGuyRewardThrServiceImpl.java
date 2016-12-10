package com.ygg.admin.service.qqbs.reward.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.qqbs.reward.QqbsNewGuyRewardThrDao;
import com.ygg.admin.service.qqbs.reward.QqbsNewGuyRewardThrService;

@Repository
public class QqbsNewGuyRewardThrServiceImpl implements QqbsNewGuyRewardThrService
{
    
    @Resource
    private QqbsNewGuyRewardThrDao qqbsNewGuyRewardThrDao;

    @Override
    public Map<String, Object> getNewGuyRewardThr(int accountId)
            throws Exception
    {
        Map<String, Object>  resultMap = new HashMap<String,Object>();
        Map<String, Object>  map = new HashMap<String,Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String nickName = null,levelAllNum = null,levelOneNum = null;
        List<Map<String,Object>> rewardList = qqbsNewGuyRewardThrDao.getFansNum(accountId);
        if(CollectionUtils.isNotEmpty(rewardList)){
            nickName = String.valueOf(rewardList.get(0).get("nickName"));
            levelAllNum = String.valueOf(rewardList.get(0).get("levelAllNum"));
            levelOneNum = String.valueOf(rewardList.get(0).get("levelOneNum"));
        }
        double totalPrice = qqbsNewGuyRewardThrDao.getFansOrderPrice(accountId);
        if(!StringUtils.contains("null", nickName)){
            map.put("accountId",accountId);
            map.put("nickName", nickName);
            map.put("levelAllNum", levelAllNum);
            map.put("levelOneNum", levelOneNum);
            map.put("totalPrice", new DecimalFormat("0.00").format(totalPrice));
            list.add(map);
        }
        resultMap.put("rows", list);
        return resultMap;
    }
    

}
