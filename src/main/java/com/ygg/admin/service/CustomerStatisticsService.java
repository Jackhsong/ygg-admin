package com.ygg.admin.service;

import java.util.Map;

public interface CustomerStatisticsService
{
    
    /**
     * 根据日期统计退款率
     * 
     * @param date
     * @return
     * @throws Exception
     */
    Map<String, Object> refundListOfDay(String date)
        throws Exception;
    
    /**
     * 根据商家统计退款率
     * 
     * @param startTime
     * @param endTime
     * @param sellerId
     * @return
     * @throws Exception
     */
    Map<String, Object> refundListOfSeller(String startTime, String endTime, int sellerId, int page, int rows, int isExport)
        throws Exception;
}
