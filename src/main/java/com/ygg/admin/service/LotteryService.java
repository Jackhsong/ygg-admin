package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface LotteryService
{
    
    List<Map<String, Object>> findAllLottery(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAllGiftActivity(Map<String, Object> para)
        throws Exception;
    
}
