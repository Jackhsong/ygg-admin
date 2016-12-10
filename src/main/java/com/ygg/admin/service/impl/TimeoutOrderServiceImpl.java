package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.TimeoutOrderDao;
import com.ygg.admin.dao.UserDao;
import com.ygg.admin.entity.OrderDetailInfoForSeller;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.CommonService;
import com.ygg.admin.service.TimeoutOrderService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.MathUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TimeoutOrderServiceImpl implements TimeoutOrderService
{
    private static Logger logger = Logger.getLogger(TimeoutOrderServiceImpl.class);
    
    @Resource(name = "orderDao")
    private OrderDao orderDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private TimeoutOrderDao timeoutOrderDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private CommonService commonService;
    
    @Override
    public String jsonComplainOrderInfo(Map<String, Object> para)
        throws Exception
    {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> info = timeoutOrderDao.findAllComplainOrder(para);
        packageOrderInfo(info);
        resultMap.put("rows", info);
        resultMap.put("total", timeoutOrderDao.countComplainOrder(para));
        return JSON.toJSONString(resultMap);
    }
    
    private void packageOrderInfo(List<Map<String, Object>> info)
        throws Exception
    {
        List<Integer> orderIdList = new ArrayList<>();
        List<Integer> receiveAddressIdList = new ArrayList<>();
        Set<Integer> sellerIdList = new HashSet<>();
        Set<Integer> sourceChannelIdList = new HashSet<>();
        
        for (Map<String, Object> it : info)
        {
            int id = Integer.valueOf(it.get("id") + "");
            int sourceChannelId = Integer.valueOf(it.get("sourceChannelId") + "");
            int receiveAddressId = Integer.valueOf(it.get("receiveAddressId") + "");
            int sellerId = Integer.valueOf(it.get("sellerId") + "");
            orderIdList.add(id);
            receiveAddressIdList.add(receiveAddressId);
            sellerIdList.add(sellerId);
            sourceChannelIdList.add(sourceChannelId);
        }
        
        // 查询收货人信息
        Map<String, Map<String, Object>> receiveInfoMap = new HashMap<>();
        if (receiveAddressIdList.size() > 0)
        {
            List<Map<String, Object>> list = orderDao.findReceiveInfoByIdList(receiveAddressIdList);
            for (Map<String, Object> it : list)
            {
                String id = it.get("id") + "";
                receiveInfoMap.put(id, it);
            }
        }
        
        // 查询商家信息
        Map<String, Map<String, Object>> sellerInfoMap = new HashMap<>();
        if (sellerIdList.size() > 0)
        {
            Map<String, Object> _sp = new HashMap<>();
            _sp.put("idList", CommonUtil.setToList(sellerIdList));
            List<Map<String, Object>> sellerList = sellerDao.findSellerInfoBySellerIdList(_sp);
            for (Map<String, Object> it : sellerList)
            {
                String id = it.get("id") + "";
                sellerInfoMap.put(id, it);
            }
        }
        
        // 查询order_source_channel
        Map<String, Map<String, Object>> sourceChannelInfoMap = new HashMap<>();
        if (sourceChannelIdList.size() > 0)
        {
            List<Map<String, Object>> list = orderDao.findSourceChannelInfoByIdList(CommonUtil.setToList(sourceChannelIdList));
            for (Map<String, Object> it : list)
            {
                String id = it.get("id") + "";
                sourceChannelInfoMap.put(id, it);
            }
        }
        
        // 查询发货信息
        Map<String, Map<String, Object>> logisticsInfoMap = new HashMap<>();
        if (orderIdList.size() > 0)
        {
            List<Map<String, Object>> list = orderDao.findLogisticsInfoByIdList(orderIdList);
            for (Map<String, Object> it : list)
            {
                String id = it.get("orderId") + "";
                logisticsInfoMap.put(id, it);
            }
        }
        
        //List<Map<String, Object>> rows = new ArrayList<>();
        for (Map<String, Object> it : info)
        {
            int status = Integer.valueOf(it.get("status") + "");
            String number = it.get("number") + "";
            it.put("expireTime", it.get("expireTime") == null ? "" : it.get("expireTime").toString());
            it.put("createTime", it.get("createTime").toString());
            it.put("payTime", it.get("payTime") == null ? "" : it.get("payTime").toString());
            it.put("sendTime", it.get("sendTime") == null ? "" : it.get("sendTime").toString());
            it.put("payChannel", OrderEnum.PAY_CHANNEL.getDescByCode(Integer.valueOf(it.get("payChannel") + "")));
            it.put("oStatusDescripton", OrderEnum.ORDER_STATUS.getDescByCode(status));
            it.put("totalPrice", MathUtil.round(it.get("totalPrice") + "", 2));
            it.put("realPrice", MathUtil.round(it.get("realPrice") + "", 2));
            // 尾1:app,2:wap
            if (number.endsWith("1"))
            {
                int appChannel = Integer.valueOf(it.get("appChannel") == null ? "0" : it.get("appChannel") + "");
                String appVersion = it.get("appVersion") + "";
                appVersion = "".equals(appVersion) ? "" : "(" + appVersion + ")";
                it.put("orderChannel", CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel) + appVersion);
                
            }
            else if (number.endsWith("2"))
            {
                it.put("orderChannel", "网页");
            }
            else if (number.endsWith("3"))
            {
                it.put("orderChannel", "左岸城堡");
                int appChannel = Integer.valueOf(it.get("appChannel") == null ? "0" : it.get("appChannel") + "");
                it.put("orderChannel", CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel));
            }
            
            int isSettlement = Integer.valueOf(it.get("isSettlement") + "");
            it.put("isSettlement", isSettlement == 1 ? "已结算" : "未结算");
            
            // 需要查询收货人信息
            String receiveAddressId = it.get("receiveAddressId") + "";
            Map<String, Object> r = receiveInfoMap.get(receiveAddressId);
            it.put("raFullName", r != null ? r.get("fullName") : "");
            it.put("raMobileNumber", r != null ? r.get("mobileNumber") : "");
            
            // 需要查询商家信息
            String sellerId = it.get("sellerId") + "";
            Map<String, Object> s = sellerInfoMap.get(sellerId);
            it.put("sSellerName", s != null ? s.get("realSellerName") : "");
            it.put("sSendAddress", s != null ? s.get("sendAddress") : "");
            it.put("sendTimeType", s == null ? "" : SellerEnum.SellerSendTimeTypeEnum.getDescByCode((int)s.get("sendTimeType")) + "("
                + SellerEnum.WeekendSendTypeEnum.getDescByCode((int)s.get("sendWeekendType")) + ")");
            
            // 需要查询order_source_channel
            String sourceId = it.get("sourceChannelId") + "";
            Map<String, Object> s1 = sourceChannelInfoMap.get(sourceId);
            it.put("orderSource", s1 != null ? s1.get("name") : "");
            
            // 查询发货信息
            String orderId = it.get("id") + "";
            Map<String, Object> o = logisticsInfoMap.get(orderId);
            it.put("ologChannel", o != null ? o.get("channel") : "");
            it.put("ologNumber", o != null ? o.get("number") : "");
            it.put("ologMoney", o != null ? o.get("money") : "" + "");
            
            //订单超时信息
            String reason = timeoutOrderDao.findOrderTimeoutReasonName(Integer.parseInt(it.get("reasonId").toString()));
            it.put("timeoutReason", reason);
            Map<String, Object> complainMap = timeoutOrderDao.findRecentOrderTimeoutComplainByOrderId(Integer.parseInt(orderId));
            it.put("complainId", complainMap == null ? 0 : complainMap.get("id") == null ? "" : complainMap.get("id"));
            it.put("complainReason", complainMap == null ? 0 : complainMap.get("reason") == null ? "" : complainMap.get("reason"));
            /*if (status == OrderEnum.OrderStatusEnum.REVIEW.getCode() || status == OrderEnum.OrderStatusEnum.SENDGOODS.getCode()
                || status == OrderEnum.OrderStatusEnum.SUCCESS.getCode() || status == OrderEnum.OrderStatusEnum.GROUPING.getCode())
            {
                if (expireTime != null)
                {
                    if (sendTime == null)
                    {
                        if (new DateTime(expireTime.getTime()).isBeforeNow())
                        {
                            //超时待发货
                            it.put("isTimeout", 1);
                        }
                        else
                        {
                            //正常待发货
                            it.put("isTimeout", 0);
                        }
                    }
                    else
                    {
                        if (new DateTime(sendTime.getTime()).isAfter(new DateTime(expireTime.getTime())))
                        {
                            //超时发货
                            it.put("isTimeout", 1);
                        }
                        else
                        {
                            //按时发货
                            it.put("isTimeout", 0);
                        }
                    }
                }
                else
                {
                    it.put("isTimeout", 0);
                }
                
            }
            else
            {
                it.put("isTimeout", 0);
            }*/
            //rows.add(it);
        }
        /*info.clear();
        return rows;*/
    }
    
    @Override
    public String sendTimeOutComplain(int orderId, String reason)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("orderId", orderId);
        para.put("reason", reason);
        if (timeoutOrderDao.insertOrderTimeoutComplain(para) > 0)
        {
            int status = 0;
            try
            {
                status = timeoutOrderDao.insertOrderTimeoutComplainResult(orderId);
            }
            catch (Exception e)
            {
                if (e.getMessage().contains("uniq_order_id") && e.getMessage().contains("Duplicate"))
                {
                    para.put("status", OrderEnum.TIMEOUT_COMPLAIN_RESULT.PROCESSING.getCode());
                    status = timeoutOrderDao.updateOrderTimeoutComplainResult(para);
                }
                else
                {
                    throw new Exception(e);
                }
            }
            if (status == CommonConstant.COMMON_YES)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "申诉成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "申诉失败");
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "申诉失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return timeoutOrderDao.findAllTimeoutReason(para);
    }
    
    @Override
    public String dealComplain(int orderId, int complainId, int timeoutReasonId, int dealResult, String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        
        String username = SecurityUtils.getSubject().getPrincipal() + "";
        User user = userDao.findUserByUsername(username);
        Map<String, Object> para = new HashMap<>();
        para.put("id", complainId);
        para.put("status", dealResult);
        para.put("remark", remark);
        para.put("dealUser", user == null ? 0 : user.getId());
        if (timeoutOrderDao.updateOrderTimeoutComplain(para) > 0)
        {
            para.clear();
            para.put("orderId", orderId);
            para.put("status", dealResult);
            para.put("timeoutReasonId", timeoutReasonId);
            if (timeoutOrderDao.updateOrderTimeoutComplainResult(para) > 0)
            {
                if (dealResult == OrderEnum.TIMEOUT_COMPLAIN_RESULT.SUCCESS.getCode())
                {
                    para.clear();
                    para.put("id", orderId);
                    para.put("isTimeout", CommonConstant.COMMON_NO);
                    para.put("expireTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
                    orderDao.updateOrder(para);
                    logger.info("订单申诉处理成功：" + para.toString());
                }
                resultMap.put("status", 1);
                resultMap.put("msg", "处理成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "处理失败");
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public List<Map<String, Object>> findAllOrderSummaryInfo(String startTime, String endTime, int orderType)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("startTimeBegin", startTime);
        para.put("startTimeEnd", endTime);
        para.put("orderType", orderType);

        List<Map<String, Object>> orderInfo = orderDao.findAllOrderInfoForList(para);

        Map<String, List<Map<String, Object>>> groupBySellerMap = new HashMap<>();
        for (Map<String, Object> it : orderInfo)
        {
            String sellerId = it.get("sellerId") + "";
            List<Map<String, Object>> sellerList = groupBySellerMap.get(sellerId);
            if (sellerList == null)
            {
                sellerList = new ArrayList<>();
                groupBySellerMap.put(sellerId, sellerList);
            }
            sellerList.add(it);
        }

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupBySellerMap.entrySet())
        {
            String sellerId = entry.getKey();
            List<Map<String, Object>> sellerOrderList = entry.getValue();
            int totalAmount = 0;
            Set<Integer> totalIdList = new HashSet<>();
            int timeoutSendAmount = 0;//超时发货
            Set<Integer> timeoutSendIdList = new HashSet<>();
            int timeoutNoSendAmount = 0;//超时待发货
            Set<Integer> timeoutNotSendIdList = new HashSet<>();
            int ontimeAmount = 0;//准时发货
            Set<Integer> ontimeIdList = new HashSet<>();
            int waitSendAmount = 0;//正常待发货订单
            Set<Integer> waitSendIdList = new HashSet<>();
            for (Map<String, Object> item : sellerOrderList)
            {
                int orderId = Integer.parseInt(item.get("id").toString());
                int status = Integer.parseInt(item.get("status").toString());
                int isTimeout = Integer.parseInt(item.get("isTimeout").toString());
                Timestamp sendTime = item.get("sendTime") == null ? null : (Timestamp)item.get("sendTime");
                if (status == OrderEnum.ORDER_STATUS.REVIEW.getCode() || status == OrderEnum.ORDER_STATUS.SENDGOODS.getCode()
                    || status == OrderEnum.ORDER_STATUS.SUCCESS.getCode() || status == OrderEnum.ORDER_STATUS.GROUPING.getCode())
                {
                    if (sendTime == null)
                    {
                        if (isTimeout == CommonConstant.COMMON_YES)
                        {
                            //超时待发货
                            timeoutNoSendAmount++;
                            timeoutNotSendIdList.add(orderId);
                        }
                        else
                        {
                            //正常待发货
                            waitSendAmount++;
                            waitSendIdList.add(orderId);
                        }
                    }
                    else
                    {
                        if (isTimeout == CommonConstant.COMMON_YES)
                        {
                            //超时发货
                            timeoutSendAmount++;
                            timeoutSendIdList.add(orderId);
                        }
                        else
                        {
                            //正常发货
                            ontimeAmount++;
                            ontimeIdList.add(orderId);
                        }
                    }
                    totalAmount++;
                    totalIdList.add(orderId);
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("sellerId", sellerId);
            SellerEntity seller = sellerDao.findSellerById(Integer.parseInt(sellerId));
            map.put("sellerName", seller == null ? "" : seller.getRealSellerName());
            map.put("sendTimeType", seller == null ? "" : SellerEnum.SellerSendTimeTypeEnum.getDescByCode(seller.getSendTimeType()) + "("
                + SellerEnum.WeekendSendTypeEnum.getDescByCode(seller.getIsSendWeekend()) + ")");
            map.put("totalAmount", totalAmount);
            map.put("totalIdList", CommonUtil.list2String(new ArrayList<Integer>(totalIdList), ","));
            map.put("ontimeAmount", ontimeAmount);
            map.put("ontimeIdList", CommonUtil.list2String(new ArrayList<Integer>(ontimeIdList), ","));
            map.put("timeoutSendAmount", timeoutSendAmount);
            map.put("timeoutSendIdList", CommonUtil.list2String(new ArrayList<Integer>(timeoutSendIdList), ","));
            map.put("timeoutNoSendAmount", timeoutNoSendAmount);
            map.put("timeoutNotSendIdList", CommonUtil.list2String(new ArrayList<Integer>(timeoutNotSendIdList), ","));
            map.put("waitSendAmount", waitSendAmount);
            map.put("waitSendIdList", CommonUtil.list2String(new ArrayList<Integer>(waitSendIdList), ","));
            map.put("amerceAmount", (timeoutNoSendAmount + timeoutSendAmount) * CommonConstant.ORDER_SEND_TIME_OUT_AMERCE);
            map.put("sendOntimePercent", totalAmount > 0 ? MathUtil.round(ontimeAmount * 1.0d / totalAmount * 100, 2) + "%" : 0);
            map.put("bestSendOntimePercent", totalAmount > 0 ? MathUtil.round((ontimeAmount + waitSendAmount) * 1.0d / totalAmount * 100, 2) + "%" : 0);
            map.put("sendProgressPercent", totalAmount > 0 ? MathUtil.round((ontimeAmount + timeoutSendAmount) * 1.0d / totalAmount * 100, 2) + "%" : 0);
            resultList.add(map);
        }

        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {

            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int amerceAmount1 = Integer.parseInt(o1.get("amerceAmount").toString());
                int amerceAmount2 = Integer.parseInt(o2.get("amerceAmount").toString());
                int totalAmount1 = Integer.parseInt(o1.get("totalAmount").toString());
                int totalAmount2 = Integer.parseInt(o2.get("totalAmount").toString());
                if (amerceAmount2 > amerceAmount1)
                {
                    return 1;
                }
                else if (amerceAmount2 < amerceAmount1)
                {
                    return -1;
                }
                else
                {
                    return totalAmount2 - totalAmount1;
                }
            }
        });

        return resultList;
    }

    @Override
    public List<Map<String, Object>> findComplainDetailsByOrderId(int orderId)
        throws Exception
    {
        List<Map<String, Object>> reList = timeoutOrderDao.findOrderTimeoutComplainListByOrderId(orderId);
        for (Map<String, Object> it : reList)
        {
            it.put("complainTime", ((Timestamp)it.get("createTime")).toString());
            it.put("dealTime", ((Timestamp)it.get("updateTime")).toString());
            
            User user = userDao.findUserById(Integer.parseInt(it.get("dealUser").toString()));
            it.put("dealUser", user == null ? "系统管理员" : user.getRealname());
            it.put("result", OrderEnum.TIMEOUT_COMPLAIN_RESULT.getNameByCode(Integer.parseInt(it.get("status").toString())));
        }
        return reList;
    }
    
    @Override
    public Map<String, Object> findOrderTimeoutComplainResultByOId(int orderId)
        throws Exception
    {
        return timeoutOrderDao.findOrderTimeoutComplainResultByOId(orderId);
    }
    
    @Override
    public String findOrderTimeoutReasonNameById(int id)
        throws Exception
    {
        return timeoutOrderDao.findOrderTimeoutReasonName(id);
    }
    
    @Override
    public Map<String, Object> jsonTimeoutReasonInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", timeoutOrderDao.findAllTimeoutReason(para));
        resultMap.put("total", timeoutOrderDao.countTimeoutReason(para));
        return resultMap;
    }
    
    @Override
    public String saveTimeoutReason(int id, String name)
        throws Exception
    {
        int result = 0;
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (id == 0)
            {
                result = timeoutOrderDao.insertTimeoutReason(name);
            }
            else
            {
                Map<String, Object> para = new HashMap<>();
                para.put("id", id);
                para.put("name", name);
                result = timeoutOrderDao.updateTimeoutReason(para);
            }
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("uniq_name") && e.getMessage().contains("Duplicate"))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", String.format("超时原因【%s】已经存在", name));
            }
            else
            {
                throw new Exception(e);
            }
        }
        if (result > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateTimeoutReasonStatus(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (timeoutOrderDao.updateTimeoutReason(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String addTimeoutOrderQcDeal(int orderId, int reasonId, String result, String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(remark))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入处理说明");
            return JSON.toJSONString(resultMap);
        }
        if (StringUtils.isEmpty(result))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入处理结果");
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<>();
        para.put("orderId", orderId);
        para.put("reasonId", reasonId);
        para.put("dealRemark", remark);
        para.put("dealResult", result);
        para.put("dealUser", commonService.getCurrentRealName());
        if (timeoutOrderDao.addTimeoutOrderQcDeal(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "处理成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String addBatchTimeoutOrderQcDeal(String orderIds, int reasonId, String result, String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(remark))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入处理说明");
            return JSON.toJSONString(resultMap);
        }
        if (StringUtils.isEmpty(result))
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入处理结果");
            return JSON.toJSONString(resultMap);
        }
        List<Map<String, Object>> params = new ArrayList<>();
        List<String> orderIdList = Arrays.asList(orderIds.split(","));
        for (String orderId : orderIdList)
        {
            Map<String, Object> para = new HashMap<>();
            para.put("orderId", orderId);
            para.put("reasonId", reasonId);
            para.put("dealRemark", remark);
            para.put("dealResult", result);
            para.put("dealUser", commonService.getCurrentRealName());
            params.add(para);
        }
        if (timeoutOrderDao.batchAddTimeoutOrderQcDeal(params) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "处理成功");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "处理成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public List<OrderDetailInfoForSeller> findAllTimeoutOrderDetail(Map<String, Object> para)
        throws Exception
    {
        Map<String, OrderDetailInfoForSeller> detailMap = new HashMap<>();
        List<OrderDetailInfoForSeller> sourcesDatas = timeoutOrderDao.findAllTimeoutOrderDetail(para);
        //去重
        for (OrderDetailInfoForSeller detail : sourcesDatas)
        {
            detailMap.put(detail.getoNumber(), detail);
        }
        return new ArrayList<OrderDetailInfoForSeller>(detailMap.values());
    }
    
    @Override
    public int getExportOrderNums(Map<String, Object> para)
        throws Exception
    {
        return timeoutOrderDao.countTimeoutOrderDetail(para);
    }
}
