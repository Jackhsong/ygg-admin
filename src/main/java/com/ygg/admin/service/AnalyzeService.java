package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.ygg.admin.view.UserBehaviorView;

/**
 * 统计service
 * 
 * @author zhangyb
 *
 */
public interface AnalyzeService
{
    
    /**
     * 根据para统计商家信息 自定义 table
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> sellerDataCustom(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para统计 商品 信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> productDataCustom(Map<String, Object> para)
        throws Exception;
    
    /**
     * 左岸城堡月度订单数据统计
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> monthAnalyze(Map<String, Object> para)
        throws Exception;

    /**
     * 左岸城堡月度订单数据统计
     *
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> platformMonthAnalyze(Map<String, Object> para)
            throws Exception;
    
    /**
     * 今日销售数据分析
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> todayAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 详细信息 今日在售组合特卖信息售卖情况
     * 
     * @param type 1：banner；2：特卖
     * @param id 对应id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> todayAnalyzeDetail(int type, int id)
        throws Exception;
    
    /**
     * 月度注册情况分析
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> registAnalyze(String selectDate, String channel)
        throws Exception;
    
    /**
     * 左岸城堡新用户订单数据统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Object> newAccountBuyingAnalyze(Map<String, Object> para)
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
     * 客户端成交统计
     * @param para
     * @return
     */
    Map<String, Object> clientBuyAnalyze(String selectDate)
        throws Exception;
    
    /**
     * 客户端成交明细
     * @param selectDate
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> clientBuyDetailAnalyze(String selectDate)
        throws Exception;
    
    /**
     * 用户成交行为统计
     * @param para
     * @return
     */
    List<UserBehaviorView> userBehaviorAnalyze(Map<String, String> para)
        throws Exception;
    
    /**
     * 用户成交行为统计详细
     * @param para
     * @return
     * @throws Exception
     */
    List<UserBehaviorView> userBehaviorDetailAnalyze(Map<String, String> para)
        throws Exception;
    
    /**
     * 各版本实付金额统计
     * @param selectDate
     * @return
     * @throws Exception
     */
    Map<String, Object> appVersionBuyAnalyze(String selectDate)
        throws Exception;
    
    /**
     * 每日订单发货时效统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> eachDayOrderSendTimeAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 每日发货后有物流信息时效统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> eachDayOrderLogisticAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 商家发货时效统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> sellerSendTimeAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 商家发货后有物流信息时效统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> sellerOrderLogisticAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 全国省份成交统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, String>> provinceTurnOverAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 全国城市成交统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, String>> cityTurnOverAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 商品(特卖/商城)成交统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, String>> productTurnOverAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 商品分类成交统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, String>> productCategoryTurnOverAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 一级分类成交统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> firstCategoryTurnoverAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 客户平均提篮量统计
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, String>> customerAverageAnalyze(Map<String, Object> para)
        throws Exception;
    
    /**
     * 用户首次购买行为统计分析
     * @param para
     * @return
     * @throws Exception
     */
    List<UserBehaviorView> userFirstBehaviorAnalyze(Map<String, String> para)
        throws Exception;
    
    /**
     * 每日订单发货时效统计
     * @param selectDate
     * @param payTimeBegin
     * @param payTimeEnd 
     * @return
     */
    List<Map<String, Object>> orderSendTimeAnalyzeByMonth(String selectDate, String payTimeBegin, String payTimeEnd)
        throws Exception;
    
    /***
     * 渠道用户注册与成交统计
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> getUserStatisticByChannel(Map<String,Object> para) 
        throws Exception;

    /***
     * 渠道用户注册与成交统计明细
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> viewStatisticDetail(Map<String, Object> para)
            throws Exception;

}
