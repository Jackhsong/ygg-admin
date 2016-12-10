package com.ygg.admin.dao.qqbs.reward;

import java.util.List;
import java.util.Map;


public interface QqbsNewGuyRewardThrDao
{
    
    /**
     * 统计粉丝销售额
     * @param accountId
     * @return
     * @throws Exception
     */
    public double getFansOrderPrice(int accountId) throws Exception;
    
    /**
     * 统计所有粉丝数量、一级粉丝数量 
     * @param accountId
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getFansNum(int accountId) throws Exception;

}
