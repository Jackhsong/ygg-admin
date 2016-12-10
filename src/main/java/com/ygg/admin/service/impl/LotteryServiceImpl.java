package com.ygg.admin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.LotteryDao;
import com.ygg.admin.service.LotteryService;

@Service("lotteryService")
public class LotteryServiceImpl implements LotteryService
{
    @Resource
    private LotteryDao lotteryDao;
    
    @Override
    public List<Map<String, Object>> findAllLottery(Map<String, Object> para)
        throws Exception
    {
        return lotteryDao.findAllLottery(para);
    }
    
    @Override
    public List<Map<String, Object>> findAllGiftActivity(Map<String, Object> para)
        throws Exception
    {
        return lotteryDao.findAllGiftActivity(para);
    }
    
}
