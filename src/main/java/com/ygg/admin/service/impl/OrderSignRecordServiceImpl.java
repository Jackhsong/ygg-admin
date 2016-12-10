package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.SmsServiceTypeEnum;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.OrderSignRecordDao;
import com.ygg.admin.service.OrderSignRecordService;
import com.ygg.admin.service.SmsService;
import com.ygg.admin.util.CommonEnum;

/**
 * Created by zhangyb on 2015/9/22 0022.
 */
@Service("orderSignRecordService")
public class OrderSignRecordServiceImpl implements OrderSignRecordService
{
    
    private Logger log = Logger.getLogger(OrderSignRecordServiceImpl.class);
    
    /**左岸城堡普通商品*/
    private static String GEGEJIA_CONTENT_1 = "亲，您购买的%s宝贝已签收，如有破损或错发漏发等情况，请及时拍照并联系左岸城堡在线客服哦～";
    
    /**左岸城堡水果生鲜商品*/
    private static String GEGEJIA_CONTENT_2 = "亲，您购买的%s生鲜食品已签收，请立刻冷藏或者冷冻～ 如有破损或错发漏发等情况，请及时拍照并联系左岸城堡在线客服哦～";
    
    /**左岸城堡普通商品*/
    private static String GEGETUAN_CONTENT_1 = "亲，您购买的%s已签收，如有破损或错发漏发等情况，请及时拍照并联系左岸城堡微信在线客服或拨打客服电话4001-603-602哦～";
    
    /**左岸城堡水果生鲜商品*/
    private static String GEGETUAN_CONTENT_2 = "亲，您购买的%s已签收，因水果的保质期较短，若您收到的水果有任何质量问题，请于24小时内拍照并联系左岸城堡微信在线客服或拨打客服电话4001-603-602哦～";

    /**左岸城堡普通商品*/
    private static String HUANQIUBUSHOU_CONTENT_1 = "亲，您购买的%s已签收，如有破损或错发漏发等情况，请及时拍照并联系左岸城堡在线客服哦～";

    /**左岸城堡
水果生鲜商品*/
    private static String HUANQIUBUSHOU_CONTENT_2 = "亲，您购买的%s已签收，请立刻冷藏或者冷冻哦～ 如有破损或错发漏发等情况，请及时拍照并联系左岸城堡在线客服哦～";

    @Resource
    private OrderSignRecordDao orderSignRecordDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private SmsService smsService;
    
    @Override
    public int sendMessage()
        throws Exception
    {
        
        List<Integer> orderIdList = orderSignRecordDao.findOrderSignRecord();
        List<Integer> orderIdList_success = new ArrayList<>();
        if (orderIdList.size() > 0)
        {
            for (Integer orderId : orderIdList)
            {
                List<Map<String, Object>> listMap = orderDao.findProductNameAndTypeByOrderId(orderId + "");
                int type = 1;
                int orderType = 0;
                String productName = "";
                String content = null;
                for (Map<String, Object> it : listMap)
                {
                    orderType = Integer.parseInt(it.get("orderType") + "");
                    if (Integer.valueOf(it.get("type") + "").intValue() == 1)
                    {
                        continue;
                    }
                    // 订单下有多个商品且普通商品和生鲜商品同时存在，只发生鲜类商品提醒
                    else if (Integer.valueOf(it.get("type") + "").intValue() == 2)
                    {
                        type = 2;
                        productName = it.get("productName") + "";
                        break;
                    }
                }
                // 只有普通商品，只发普通商品提醒
                if (type == 1 && "".equals(productName) && listMap.size() > 0)
                {
                    productName = listMap.get(0).get("productName") + "";
                }
                if (!"".equals(productName))
                {
                    if (type == 1)
                    {
                        if (listMap.size() > 1)
                        {
                            productName += "等";
                        }
                        if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
                        {
                            content = String.format(GEGEJIA_CONTENT_1, productName);
                        }
                        else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode() || orderType == OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode())
                        {
                            content = String.format(GEGETUAN_CONTENT_1, productName);
                        }
                        else if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                        {
                            content = String.format(HUANQIUBUSHOU_CONTENT_1, productName);
                        }
                    }
                    else if (type == 2)
                    {
                        if (listMap.size() > 1)
                        {
                            productName += "等";
                        }
                        if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
                        {
                            content = String.format(GEGEJIA_CONTENT_2, productName);
                        }
                        else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode() || orderType == OrderEnum.ORDER_TYPE.GEGETUAN_QQG.getCode())
                        {
                            content = String.format(GEGETUAN_CONTENT_2, productName);
                        }
                        else if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                        {
                            content = String.format(HUANQIUBUSHOU_CONTENT_2, productName);
                        }
                    }
                    String mobile = orderDao.findReceiveMobileNumberByOrderId(orderId + "");
                    if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(content))
                    {
                        try
                        {
                            Map<String, Object> para = new HashMap<>();
                            para.put("phoneNumber", mobile);
                            para.put("type", CommonEnum.SmsTypeEnum.RECEIVE_GOODS.ordinal());
                            para.put("content", content);
                            para.put("serviceType", SmsServiceTypeEnum.MENGWANG.getValue());
                            int status = -1;
                            if (orderType == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
                            {
                                //左岸城堡订单
                                status = smsService.sendMontnets(para);
                            }
                            else if (orderType == OrderEnum.ORDER_TYPE.GEGETUAN.getCode())
                            {
                                //左岸城堡订单
                                status = smsService.sendMontnetsTuan(para);
                            }
                            else if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                            {
                                //左岸城堡订单
                                status = smsService.sendMontnetsGlobal(para);
                            }
                            if (status == 1)
                            {
                                log.info("发送签收短信成功，订单ID=" + orderId + ",手机号=" + mobile);
                            }
                            else
                            {
                                log.info("发送签收短信失败！！！订单ID=" + orderId + ",手机号=" + mobile);
                            }
                            orderIdList_success.add(orderId);
                        }
                        catch (Exception e)
                        {
                            log.error("快递签收发送提醒短信出错", e);
                        }
                    }
                }
                else
                {
                    log.info(String.format("发送签收短信失败，orderId=%d,orderType=%d,baseProductType=%d,productName=%s", orderId, orderType, type, productName));
                }
            }
            
            // 更新已发送短息的订单状态
            if (orderIdList_success.size() > 0)
            {
                if (orderSignRecordDao.updateOrderSignRecord(orderIdList_success) > 0)
                {
                    log.info("成功发送了" + orderIdList_success.size() + "个签收订单");
                }
            }
        }
        return 1;
    }
}
