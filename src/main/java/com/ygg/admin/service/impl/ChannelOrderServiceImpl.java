package com.ygg.admin.service.impl;

import com.google.common.base.Preconditions;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.WareHouseEnum;
import com.ygg.admin.dao.ChannelDao;
import com.ygg.admin.dao.ChannelOrderDao;
import com.ygg.admin.service.ChannelOrderService;
import com.ygg.admin.view.channel.ChannelOrderExcelView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-13
 */
@Service
public class ChannelOrderServiceImpl implements ChannelOrderService
{
    
    @Resource
    TransactionTemplate transactionTemplate;
    
    @Resource
    ChannelOrderDao channelOrderDao;
    
    @Resource
    ChannelDao channelDao;
    
    @Override
    public Map<String, Object> jsonChannelOrderInfo(Map<String, Object> param)
    {
        List<Map<String, Object>> infos = channelOrderDao.jsonChannelOrderInfo(param);
        for(Map<String,Object> info : infos ){
            if(info.get("warehouseType") != null){
                info.put("warehouseName", WareHouseEnum.getValueById((Integer)info.get("warehouseType")));
            }
            if(info.get("orderPayTime") == null ){
                info.put("orderPayTime", "");
            }else {
                info.put("orderPayTime",(info.get("orderPayTime").toString()));
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", infos);
        resultMap.put("total", channelOrderDao.countJsonChannelOrderInfo(param));
        return resultMap;
    }
    
    @Override
    public int importChannelOrders(List<ChannelOrderExcelView> orders)
        throws Exception
    {
        for (final ChannelOrderExcelView order : orders)
        {
            // 根据订单编号和渠道 判断订单是否已存在
            Map<String, Object> info = channelOrderDao.getChannelOrderInfoByNumberAndChannelId(order);
            if ( info != null && info.get("id") != null )
            {
                order.setOrder_id((long)info.get("id"));
                order.setAddress_id((long)info.get("addressId"));
                long productId;
                //根据订单id和商品id 判断订单下该商品是否有记录
                if ((productId = channelOrderDao.getChannelOrderProductIdByParam(order)) > 0)
                {
                    order.setProduct_id(productId);
                    channelOrderDao.updateChannelOrder(order);
                    channelOrderDao.updateChannelOrderReceiverAddress(order);
                    channelOrderDao.updateChannelOrderProduct(order);
                }
                else
                {
                    Preconditions.checkArgument(channelOrderDao.insertChannelOrderProduct(order) > 0, "插入ChannelProduct失败" + order);
                }
            }
            else
            {
                Preconditions.checkArgument(channelOrderDao.insertChannelOrderReceiveAddress(order) > 0, "插入ChannelOrderReceiveAddress失败" + order);
                Preconditions.checkArgument(channelOrderDao.insertChannelOrder(order) > 0, "插入ChannelOrder失败" + order);
                Preconditions.checkArgument(channelOrderDao.insertChannelOrderProduct(order) > 0, "插入ChannelProduct失败" + order);
            }
        }
        return orders.size();
    }

    @Override
    public List<ChannelOrderExcelView> getChannelOrderAllInfo(Map<String, Object> param) {
        List<ChannelOrderExcelView> orders =  channelOrderDao.getChannelOrderAllInfo(param);
        for(ChannelOrderExcelView order : orders){
            order.setIsFreezeString(getCheckStatus(order.getIs_freeeze()));
            order.setPayCompany(OrderEnum.PAY_CHANNEL.getDescByCode(order.getPay_channel()));
            order.setOrderStatus(OrderEnum.ORDER_STATUS.getDescByCode(order.getStatus()));
            if("0000-00-00 00:00:00".equals(order.getDeliver_time())){
                order.setDeliver_time("");
            }
            if("0000-00-00 00:00:00".equals(order.getOrder_pay_time())) {
                order.setOrder_pay_time("");
            }
            if("0000-00-00 00:00:00".equals(order.getOrder_create_time())) {
                order.setOrder_create_time("");
            }
        }
        return orders;
    }

    public String getCheckStatus(int status){
        if(status == 1){
            return "Y";
        }
        return "N";
    }

}
