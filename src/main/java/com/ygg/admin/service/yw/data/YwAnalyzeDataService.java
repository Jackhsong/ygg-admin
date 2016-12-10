package com.ygg.admin.service.yw.data;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public interface YwAnalyzeDataService
{
    /**
     * 燕网月度订单数据统计
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> monthAnalyze(Map<String, Object> para)
        throws Exception;
    
    
    /**
     * 燕网今日销售top
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> todaySaleTop(Map<String, Object> para)
        throws Exception;
    
    
    /**
     * 燕网获得销售折线图数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> saleLineData(DateTime date)
        throws Exception;

    /**
     * 根据para统计燕网商品信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> productDataCustom(Map<String, Object> para)
            throws Exception;
}
