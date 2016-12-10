 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

    
public interface QqbsAnalyzeService {
    /**
     * 左岸城堡
月度订单数据统计
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> monthAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 今日销售top
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> todaySaleTop(Map<String, Object> para)
        throws Exception;
    
     
    /**
     * 获得销售折线图数据
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> saleLineData(DateTime date)
        throws Exception;
    
    /**
     * 根据para统计左岸城堡
商品信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> productDataCustom(Map<String, Object> para)
        throws Exception;
}
