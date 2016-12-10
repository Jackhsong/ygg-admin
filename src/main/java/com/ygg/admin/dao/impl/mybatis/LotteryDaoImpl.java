package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.LotteryDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("lotteryDao")
public class LotteryDaoImpl extends BaseDaoImpl implements LotteryDao
{
    
    @Override
    public List<Map<String, Object>> findAllLottery(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> resultList = this.getSqlSessionRead().selectList("LotteryMapper.findAllLottery", para);
        return resultList == null ? new ArrayList<Map<String, Object>>() : resultList;
    }
    
    @Override
    public List<Map<String, Object>> findAllGiftActivity(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> resultList = this.getSqlSessionRead().selectList("LotteryMapper.findAllGiftActivity", para);
        return resultList == null ? new ArrayList<Map<String, Object>>() : resultList;
    }
    
}
