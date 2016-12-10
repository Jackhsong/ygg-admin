package com.ygg.admin.service;

import com.ygg.admin.view.channel.ChannelOrderExcelView;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-13
 */
public interface ChannelOrderService
{
    /**
     * 前台展示 获取第三方订单
     */
    Map<String, Object> jsonChannelOrderInfo(Map<String, Object> param);

    /**
     * 导入第三方订单
     */
    int importChannelOrders(List<ChannelOrderExcelView> orders) throws Exception;

    /**
     * 导出第三方订单
     */
    List<ChannelOrderExcelView> getChannelOrderAllInfo(Map<String, Object> param);


}
