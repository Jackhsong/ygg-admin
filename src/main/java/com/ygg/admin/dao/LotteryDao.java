package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

public interface LotteryDao
{
    
    List<Map<String, Object>> findAllLottery(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllGiftActivity(Map<String, Object> para)
        throws Exception;
    
}
