package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.entity.OrderEntity;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.code.Kd8LogisticsStateEnum;
import com.ygg.admin.code.KdCompanyEnum;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.RefundDao;
import com.ygg.admin.entity.OrderLogistics;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.OutCallServcie;
import com.ygg.admin.service.SmsService;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.kd100.JacksonHelper;
import com.ygg.admin.util.kd100.pojo.NoticeRequest;
import com.ygg.admin.util.kd100.pojo.Result;
import com.ygg.admin.util.kd100.pojo.ResultItem;

@Service("outCallServcie")
public class OutCallServiceImpl implements OutCallServcie
{
    private Logger log = Logger.getLogger(OutCallServcie.class);
    
    private CacheServiceIF cache = CacheManager.getClient();
    
    @Resource
    private RefundDao refundDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private SmsService smsService;
    
    public boolean kd100CallBack(String param, String refundId)
        throws Exception
    {
        try
        {
            log.info("快递100回调处理...退款退货ID：" + refundId);
            log.info(param);
            NoticeRequest nReq = JacksonHelper.fromJSON(param, NoticeRequest.class);
            // 处理快递100回调结果
            String status = nReq.getStatus();// 监控状态 :
            // polling:监控中，shutdown:结束，abort:中止，updateall：重新推送
            String message = nReq.getMessage();// 监控状态相关消息
            Map<String, Object> failMap = new HashMap<String, Object>();
            failMap.put("refundId", refundId);
            failMap.put("trackInfo", status);
            refundDao.updateRefundLogistics(failMap);
            // 持久化数据
            Result result = nReq.getLastResult();
            String state = result.getState();// 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态，其中4-7需要另外开通才有效
            List<ResultItem> resultItemList = result.getData();
            String ischeck = result.getIscheck();
            
            // 1. 更新order_product_refund_logistics。物流状态,0：在途中，1：已揽收，2：疑难，3：已签收',
            Map<String, Object> map = new HashMap<String, Object>();
            failMap.put("refundId", refundId);
            map.put("status", state);
            refundDao.updateRefundLogistics(map);
            
            // 2. 插入logistics_detail
            if (resultItemList.size() > 0)
            {
                Map<String, Object> refundLogistics = refundDao.findRefundLogisticsByRefundId(Integer.valueOf(refundId));
                for (ResultItem item : resultItemList)
                {
                    Map<String, Object> detail = new HashMap<String, Object>();
                    detail.put("logisticsChannel", refundLogistics.get("channel"));
                    detail.put("logisticsNumber", refundLogistics.get("number"));
                    detail.put("operateTime", item.getFtime());
                    detail.put("content", item.getContext());
                    detail.put("createTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                    int existsNum = orderDao.existsLogisticsDetail(detail);
                    if (existsNum == 0)
                    {
                        // 不存在，插入
                        orderDao.saveLogisticsDetail(detail);
                    }
                    else
                    {
                        log.debug("退款退货快递回调 ，已经存在物流信息信息，跳过插入");
                    }
                }
            }
            log.info("退款退货快递回调  ，快递100回调处理结束");
            return true;
        }
        catch (Exception e)
        {
            log.error("退款退货快递回调  ，快递100回调处理失败", e);
            return false;
        }
        
    }
    
    @Override
    public boolean kd8CallBack(Map<String, String> param)
        throws Exception
    {
        String channel = param.get("companyname").trim();
        String companyName = KdCompanyEnum.getCompanyNameByKd8Code(channel);
        if (companyName == null)
        {
            log.error("【快递吧】回调处理...未知物流公司编码：" + channel);
            return false;
        }
        String number = param.get("outid").trim();
        int status = Integer.valueOf(param.get("status") == null ? "0" : param.get("status")).intValue();
        String tracklist = param.get("tracklist");
        
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("channel", companyName);
        searchMap.put("number", number);
        //订单物流信息
        List<OrderLogistics> olList = orderDao.findAllOrderLogisticsBychannelAndNumber(searchMap);
        
        //更新订单物流信息
        for (OrderLogistics ol : olList)
        {
            log.info("【快递吧】快递回调处理...订单ID：" + ol.getOrderId());
            //快递吧的物流状态跟快递100的物流状态编码不一致，转换后以快递100的状态码为准
            int logisticsState = 0;
            if (status == Kd8LogisticsStateEnum.NOT_RECEIVED.getValue())//快递吧未签收，转为快递100在途
            {
                logisticsState = CommonEnum.LogisticsStateEnum.ON_WAY.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.RECEIVED.getValue())//快递吧已签收，转为快递100已签收
            {
                logisticsState = CommonEnum.LogisticsStateEnum.RECEIVED.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.DELEGATE_RECEIVED.getValue())//快递吧代签收，转为快递100为已签收
            {
                logisticsState = CommonEnum.LogisticsStateEnum.RECEIVED.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.NO_RECORD.getValue())//快递吧无记录，转为快递100在途
            {
                logisticsState = CommonEnum.LogisticsStateEnum.ON_WAY.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.NO_RECORD_MORE_2DAYS.getValue())//快递吧2天以上无记录，转为快递100在途
            {
                logisticsState = CommonEnum.LogisticsStateEnum.ON_WAY.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.NOT_RECEIVED_MORE_6DAYS.getValue())//快递吧6天以上未签收，转为快递100在途
            {
                logisticsState = CommonEnum.LogisticsStateEnum.IN_TROUBLE.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.IN_TROUBLE.getValue())//快递吧疑难件，转为快递100疑难件
            {
                logisticsState = CommonEnum.LogisticsStateEnum.IN_TROUBLE.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.NO_NEWS_MORE_3DAYS.getValue())//快递吧3天没有新记录，转为快递100在途
            {
                logisticsState = CommonEnum.LogisticsStateEnum.IN_TROUBLE.ordinal();
            }
            else if (status == Kd8LogisticsStateEnum.ON_SEND.getValue())//快递吧派送中，转为快递100同城派送中
            {
                logisticsState = CommonEnum.LogisticsStateEnum.CITY_WIDE_ON_WAY.ordinal();
            }
            else
            {
                log.error("【快递吧】回调返回未知状态码：" + status + ",channel=" + companyName + ";number=" + number);
                return false;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", ol.getOrderId());
            map.put("status", logisticsState);
            map.put("isSubscribed", 1);
            orderDao.updateOrderLogistics(map);
            log.info("【快递吧】更新订单Id=" + ol.getOrderId() + "的物流状态为：" + CommonEnum.LogisticsStateEnum.getDescription(logisticsState));
            
            if (logisticsState == CommonEnum.LogisticsStateEnum.RECEIVED.ordinal())
            {
                // 订单状态为已签收时插入待发送提醒短信表
                try
                {
                    OrderEntity oe = orderDao.findOrderById(Integer.valueOf(ol.getOrderId()));
                    if (oe.getType() != OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                    {
                        if (orderDao.findOrderSignRecordIdByOrderId(ol.getOrderId()) == -1)
                        {
                            if (orderDao.addOrderSignRecordIdByOrderId(ol.getOrderId()) != 1)
                            {
                                log.info("订单状态为已签收时插入待发送提醒短信表失败！");
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    log.error("订单状态为已签收时插入待发送提醒短信表失败！", e);
                }
            }
        }
        
        //插入物流详细信息
        JSONObject jsonObject = JSON.parseObject(tracklist);
        if (jsonObject != null)
        {
            String statusStr = jsonObject.getString("Status");
            log.info("【快递吧】快递物流信息：物流公司：" + companyName + ";物流单号：" + number + ";物流状态：" + status + ";状态说明：" + statusStr);
            JSONArray jsonArray = jsonObject.getJSONArray("TrackList");
            if (jsonArray != null)
            {
                for (int i = 0; i < jsonArray.size(); i++)
                {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String operateTime = json.getString("TrackDate");
                    String content = json.getString("TrackStatus");
                    String ShortStatus = json.getString("ShortStatus");
                    log.debug("【快递吧】物流明细：物流公司：" + companyName + ";物流单号：" + number + ",ShortStatus：" + ShortStatus + ";TrackStatus：" + content + ";TrackDate：" + operateTime);
                    Map<String, Object> detail = new HashMap<String, Object>();
                    detail.put("logisticsChannel", companyName);
                    detail.put("logisticsNumber", number);
                    detail.put("operateTime", operateTime);
                    detail.put("content", content);
                    detail.put("createTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                    int existsNum = orderDao.existsLogisticsDetail(detail);
                    if (existsNum == 0)
                    {
                        // 不存在，插入
                        orderDao.saveLogisticsDetail(detail);
                    }
                    else
                    {
                        log.debug("已经存在物流信息信息，跳过插入");
                    }
                }
            }
        }
        log.info("【快递吧】快递回调处理结束");
        return true;
    }
}
