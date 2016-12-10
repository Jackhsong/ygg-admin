package com.ygg.admin.dao;

import com.ygg.admin.view.channel.ChannelOrderExcelView;

import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-13
 */
public interface ChannelOrderDao
{
    /**
     *  根据渠道和订单号 获取第三方订单
     */
    Map<String, Object> getChannelOrderInfoByNumberAndChannelId(ChannelOrderExcelView param);

    /**
     * 获取 第三方订单商品id
     */
    long getChannelOrderProductIdByParam(ChannelOrderExcelView param);

    /**
     *  插入第三方订单
     */
    long insertChannelOrder(ChannelOrderExcelView order);

    /**
     *  插入第三方订单产品
     */
    long insertChannelOrderProduct(ChannelOrderExcelView product);

    /**
     *  插入第三方订单收货地址信息
     */
    long insertChannelOrderReceiveAddress(ChannelOrderExcelView logistic);

    /**
     * 前台展示 获取第三方订单信息
     */
    List<Map<String, Object>> jsonChannelOrderInfo(Map<String, Object> para);

    /**
     * 导出第三方订单
     */
    List<ChannelOrderExcelView> getChannelOrderAllInfo(Map<String, Object> para);

    /**
     *  前台展示 获取第三方订单数量
     */
    int countJsonChannelOrderInfo(Map<String, Object> para);

    int updateChannelOrder(ChannelOrderExcelView order);

    int updateChannelOrderReceiverAddress(ChannelOrderExcelView logistic);

    int updateChannelOrderProduct(ChannelOrderExcelView product);
    
}
