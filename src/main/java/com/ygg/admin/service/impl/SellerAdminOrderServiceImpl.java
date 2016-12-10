package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.OrderQuestionEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.SellerAdminOrderService;
import com.ygg.admin.util.*;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class SellerAdminOrderServiceImpl implements SellerAdminOrderService
{
    @Resource
    private SellerAdminOrderDao sellerAdminOrderDao;
    
    @Resource
    private OrderQuestionDao orderQuestionDao;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private TimeoutOrderDao timeoutOrderDao;
    
    @Override
    public List<Map<String, Object>> findSendTimeoutOrderInfo(List<Integer> sellerIdList, String selectDate)
        throws Exception
    {
        DateTime select = DateTime.parse(selectDate, DateTimeFormat.forPattern("yyyy-MM"));
        String beginDate = select.withDayOfMonth(1).withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        String endDate = select.plusMonths(1).withDayOfMonth(1).withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerIdList", sellerIdList);
        para.put("beginDate", beginDate);
        para.put("endDate", endDate);
        // 审核通过的订单
        para.put("checkStatusList", Lists.newArrayList(OrderEnum.ORDER_CHECK_STATUS.CHECK_PASS.getCode()));
        List<Map<String, Object>> reList = sellerAdminOrderDao.findSellerOrderList(para);
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> mp : reList)
        {
            Timestamp payTime = (Timestamp)mp.get("payTime");
            String key = new DateTime(payTime.getTime()).getDayOfMonth() + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(mp);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            String day = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            int totalAmount = dayList.size();
            Set<Integer> totalIdList = new HashSet<>();
            int timeoutSendAmount = 0;//超时发货
            Set<Integer> timeoutSendIdList = new HashSet<>();
            int timeoutNoSendAmount = 0;//超时待发货
            Set<Integer> timeoutNotSendIdList = new HashSet<>();
            int ontimeAmount = 0;//准时发货
            Set<Integer> ontimeIdList = new HashSet<>();
            int waitSendAmount = 0;//正常待发货订单
            Set<Integer> waitSendIdList = new HashSet<>();
            for (Map<String, Object> item : dayList)
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
                }
                
                totalIdList.add(orderId);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("date", String.format("%s-%02d", selectDate, Integer.parseInt(day)));
            map.put("day", day);
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
            resultList.add(map);
        }
        
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int day1 = Integer.parseInt(o1.get("day").toString());
                int day2 = Integer.parseInt(o2.get("day").toString());
                return day2 - day1;
            }
        });
        return resultList;
    }
    
    @Override
    public Map<String, Object> jsonSellerQuestionListInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = sellerAdminOrderDao.findSellerOrderQuestionInfo(para);
        int total = 0;
        DateTime nowTime = DateTime.now();
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                DateTime endTime = new DateTime(DateTimeUtil.string2Date(map.get("sellerFeedbackEndTime").toString()).getTime());
                int leftMinute = Minutes.minutesBetween(nowTime, endTime).getMinutes();
                map.put("leftTime", leftMinute <= 0 ? "已超时" : "剩" + MathUtil.round(leftMinute * 1.0 / 60, 1) + "小时");
                map.put("oStatusDescripton", OrderEnum.ORDER_STATUS.getDescByCode(Integer.parseInt(map.get("oStatus") + "")));
                map.put("payTime", map.get("payTime") == null ? "" : ((Timestamp)map.get("payTime")).toString());
                map.put("sendTime", map.get("sendTime") == null ? "" : ((Timestamp)map.get("sendTime")).toString());
                map.put("createTime", map.get("createTime") == null ? "" : ((Timestamp)map.get("createTime")).toString());
            }
            total = sellerAdminOrderDao.countSellerOrderQuestionInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> findOrderQuestionDetailInfo(int id)
        throws Exception
    {
        Map<String, Object> resultMap = null;
        OrderQuestionEntity question = orderQuestionDao.findOrderQuestionById(id);
        if (question == null)
            return resultMap;
        
        resultMap = new HashMap<String, Object>();
        String receiveMobile = orderDao.findReceiveMobileNumberByOrderId(question.getOrderId() + "");
        int count = orderQuestionDao.countDealingOrderQuestionByOrderId(question.getOrderId());
        User createUser = userDao.findUserById(question.getCreateUser());
        DateTime ceateTime = new DateTime(DateTimeUtil.string2Date(question.getCreateTime()).getTime());
        resultMap.put("isPush", question.getIsPush());
        resultMap.put("id", question.getId() + "");
        resultMap.put("orderId", question.getOrderId() + "");
        resultMap.put("count", count + "");
        resultMap.put("customerStatus", question.getCustomerStatus() == 1 ? "进行中" : "已完结");
        resultMap.put("sellerStatus", question.getSellerStatus() == 1 ? "进行中" : "已完结");
        resultMap.put("createTime", question.getCreateTime());
        resultMap.put("createUser", createUser == null ? "未知" : createUser.getRealname());
        resultMap.put("questionType", question.getTemplateName());
        resultMap.put("receiveMobile", StringUtils.isEmpty(receiveMobile) ? "" : receiveMobile);
        if (question.getTimeLimitType() != 3)
        {
            resultMap.put("timeLimit", question.getTimeLimitType() + "小时");
        }
        else
        {
            resultMap.put("timeLimit", question.getCustomTime());
        }
        List<String> imageList = orderQuestionDao.findOrderQuestionImageListqid(id);
        resultMap.put("imageList", imageList);
        resultMap.put("questionDesc", question.getQuestionDesc());
        
        //顾客问题处理进度
        List<Map<String, Object>> cProgressList = orderQuestionDao.findCustomerProgressListByqid(id);
        for (Map<String, Object> it : cProgressList)
        {
            DateTime cpCreateTime = DateTimeUtil.string2DateTime(((Timestamp)it.get("createTime")).toString(), "yyyy-MM-dd HH:mm:ss.SSS");
            int cid = Integer.parseInt(it.get("id") + "");
            int cpCreateUserId = Integer.parseInt(it.get("createUser") + "");
            int timeBetween = Minutes.minutesBetween(ceateTime, cpCreateTime).getMinutes();
            int status = Integer.parseInt(it.get("status") + "");
            User cpCreateUser = userDao.findUserById(cpCreateUserId);
            String operation = (cpCreateUser == null ? "匿名用户" : cpCreateUser.getRealname()) + MathUtil.round(timeBetween * 1.0 / 60, 1) + "小时后" + (status == 1 ? "更新问题" : "完结问题");
            it.put("createTime", ((Timestamp)it.get("createTime")).toString());
            it.put("operation", operation);
            List<String> cimageList = orderQuestionDao.findCustomerProgressImagesBycid(cid);
            it.put("images", cimageList);
        }
        resultMap.put("cProgressList", cProgressList);
        
        //商家问题处理进度
        List<Map<String, Object>> sProgressList = orderQuestionDao.findSellerProgressListByqid(id);
        for (Map<String, Object> it : sProgressList)
        {
            DateTime spCreateTime = DateTimeUtil.string2DateTime(((Timestamp)it.get("createTime")).toString(), "yyyy-MM-dd HH:mm:ss.SSS");
            int sid = Integer.parseInt(it.get("id") + "");
            int spCreateUserId = Integer.parseInt(it.get("createUser") + "");
            int timeBetween = Minutes.minutesBetween(ceateTime, spCreateTime).getMinutes();
            int status = Integer.parseInt(it.get("status") + "");
            User spCreateUser = userDao.findUserById(spCreateUserId);
            String operation =
                (spCreateUser == null ? "匿名用户" : spCreateUser.getRealname()) + MathUtil.round(timeBetween * 1.0 / 60, 1) + "小时后" + (status == 1 ? "更新对接情况" : "完结对接情况");
            it.put("createTime", ((Timestamp)it.get("createTime")).toString());
            it.put("operation", operation);
            List<String> cimageList = orderQuestionDao.findSellerProgressImagesBysid(sid);
            it.put("images", cimageList);
        }
        resultMap.put("sProgressList", sProgressList);
        
        //商家反馈记录
        if (question.getIsPush() == CommonConstant.COMMON_YES)
        {
            List<Map<String, Object>> feedBackList = orderQuestionDao.findSellerFeedbackDetailListByqid(id);
            for (Map<String, Object> it : feedBackList)
            {
                it.put("createTime", ((Timestamp)it.get("createTime")).toString());
            }
            resultMap.put("feedBackList", feedBackList);
        }
        return resultMap;
    }
    
    @Override
    public String updateSellerOrderQuestion(int questionId, String content, String sellerName)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("questionId", questionId);
        para.put("content", content);
        if (orderQuestionDao.saveOrderQuestionSellerFeedback(para) > 0)
        {
            StringBuffer sb = new StringBuffer();
            sb.append(DateTime.now().toString("yyyy-MM-dd HH:mm:ss")).append("&nbsp;").append(sellerName).append("&nbsp;").append(content).append("<br/>");
            para.put("feedback", sb.toString());
            if (orderQuestionDao.updateOrderQuestionStatus(para) > 0)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public List<Map<String, Object>> findLogisticsTimeoutOrderInfo(List<Integer> sellerIdList, String selectDate)
        throws Exception
    {
        DateTime select = DateTime.parse(selectDate, DateTimeFormat.forPattern("yyyy-MM"));
        String beginDate = select.withDayOfMonth(1).withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        String endDate = select.plusMonths(1).withDayOfMonth(1).withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerIdList", sellerIdList);
        para.put("sendTimeBegin", beginDate);
        para.put("sendTimeEnd", endDate);
        List<Map<String, Object>> reList = sellerAdminOrderDao.findSellerLogisticsOrderList(para);
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> mp : reList)
        {
            Timestamp sendTime = (Timestamp)mp.get("sendTime");
            String key = new DateTime(sendTime.getTime()).getDayOfMonth() + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(mp);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            String day = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            
            int totalAmount = dayList.size();
            Set<Integer> totalIdList = new HashSet<>();
            
            int ontimeAmount = 0;//时效内物流更新
            Set<Integer> ontimeIdList = new HashSet<>();
            
            int timeoutUpateAmount = 0;//超时物流更新
            Set<Integer> timeoutUpateIdList = new HashSet<>();
            
            int timeoutNoUpdateAmount = 0;//超时物流未更新
            Set<Integer> timeoutNoUpdateIdList = new HashSet<>();
            
            int notUpdateAmount = 0;//总体未更新
            Set<Integer> notUpdateIdList = new HashSet<>();
            for (Map<String, Object> item : dayList)
            {
                int orderId = Integer.parseInt(item.get("orderId").toString());
                int isTimeout = Integer.parseInt(item.get("isTimeout").toString());
                Timestamp logisticsTime = item.get("logisticsTime") == null ? null : (Timestamp)item.get("logisticsTime");
                
                if (isTimeout == CommonConstant.COMMON_YES)
                {
                    if (logisticsTime == null)
                    {
                        //超时未更新
                        timeoutNoUpdateAmount++;
                        timeoutNoUpdateIdList.add(orderId);
                        
                        notUpdateAmount++;
                        notUpdateIdList.add(orderId);
                    }
                    else
                    {
                        //超时更新
                        timeoutUpateAmount++;
                        timeoutUpateIdList.add(orderId);
                    }
                }
                else
                {
                    if (logisticsTime == null)
                    {
                        //时校内未更新
                        notUpdateAmount++;
                        notUpdateIdList.add(orderId);
                    }
                    else
                    {
                        //时效内更新
                        ontimeAmount++;
                        ontimeIdList.add(orderId);
                    }
                }
                
                totalIdList.add(orderId);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("date", String.format("%s-%02d", selectDate, Integer.parseInt(day)));
            map.put("day", day);
            map.put("totalAmount", totalAmount);
            map.put("totalIdList", CommonUtil.list2String(new ArrayList<Integer>(totalIdList), ","));
            map.put("ontimeAmount", ontimeAmount);
            map.put("ontimeIdList", CommonUtil.list2String(new ArrayList<Integer>(ontimeIdList), ","));
            map.put("timeoutUpateAmount", timeoutUpateAmount);
            map.put("timeoutUpateIdList", CommonUtil.list2String(new ArrayList<Integer>(timeoutUpateIdList), ","));
            map.put("timeoutNoUpdateAmount", timeoutNoUpdateAmount);
            map.put("timeoutNoUpdateIdList", CommonUtil.list2String(new ArrayList<Integer>(timeoutNoUpdateIdList), ","));
            map.put("notUpdateAmount", notUpdateAmount);
            map.put("notUpdateIdList", CommonUtil.list2String(new ArrayList<Integer>(notUpdateIdList), ","));
            map.put("amerceAmount", (timeoutUpateAmount + timeoutNoUpdateAmount) * CommonConstant.ORDER_LOGISTICS_TIME_OUT_AMERCE);
            resultList.add(map);
        }
        
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int day1 = Integer.parseInt(o1.get("day").toString());
                int day2 = Integer.parseInt(o2.get("day").toString());
                return day2 - day1;
            }
        });
        return resultList;
    }
    
    @Override
    public Map<String, Object> findSendTimeoutComplainOrder(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", packageOrderInfo(sellerAdminOrderDao.findSendTimeoutComplainOrder(para)));
        resultMap.put("total", sellerAdminOrderDao.countSendTimeoutComplainOrder(para));
        return resultMap;
    }
    
    private List<Map<String, Object>> packageOrderInfo(List<Map<String, Object>> info)
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
        
        //查询订单审核信息
        /*Map<String, Map<String, Object>> orderCheckInfoMap = new HashMap<>();
        if (orderIdList.size() > 0 && checkOrder == 1)
        {
            List<Map<String, Object>> list = orderDao.findOrderCheckListByOrderList(orderIdList);
            for (Map<String, Object> it : list)
            {
                String id = it.get("orderId") + "";
                orderCheckInfoMap.put(id, it);
            }
        }*/
        
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map<String, Object> it : info)
        {
            int status = Integer.valueOf(it.get("status") + "");
            String number = it.get("number") + "";
            it.put("createTime", it.get("createTime").toString());
            it.put("payTime", it.get("payTime") == null ? "" : it.get("payTime").toString());
            it.put("sendTime", it.get("sendTime") == null ? "" : it.get("sendTime").toString());
            it.put("payChannel", OrderEnum.PAY_CHANNEL.getDescByCode(Integer.valueOf(it.get("payChannel") + "")));
            it.put("oStatusDescripton", OrderEnum.ORDER_STATUS.getDescByCode(status));
            it.put("totalPrice", MathUtil.round(it.get("totalPrice") + "", 2));
            it.put("realPrice", MathUtil.round(it.get("realPrice") + "", 2));
            // 尾1:app,2:wap
            int appChannel = Integer.valueOf(it.get("appChannel") == null ? "0" : it.get("appChannel") + "");
            int orderType = Integer.valueOf(it.get("orderType") == null ? "1" : it.get("orderType") + "");
            if (number.endsWith(CommonConstant.ORDER_SUFFIX_APP))
            {
                String appVersion = it.get("appVersion") + "";
                appVersion = "".equals(appVersion) ? "" : "(" + appVersion + ")";
                it.put("orderChannel", CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel) + appVersion);
                
            }
            else if (number.endsWith(CommonConstant.ORDER_SUFFIX_WAP))
            {
                if (orderType == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                {
                    it.put("orderChannel", "左岸城堡");
                }
                else
                {
                    it.put("orderChannel", "网页");
                }
            }
            else if (number.endsWith(CommonConstant.ORDER_SUFFIX_GROUP))
            {
                it.put("orderChannel", "左岸城堡");
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
            
            int isFreeze = Integer.valueOf(it.get("isFreeze") + "");
            if (isFreeze == 1)
            {
                it.put("refundStatus", "退款冻结");
            }
            else if (isFreeze == 2)
            {
                it.put("refundStatus", "");
            }
            else
            {
                it.put("refundStatus", "");
            }
            
            //订单超时信息
            it.put("timeOutResult", OrderEnum.TIMEOUT_COMPLAIN_RESULT.getNameByCode(Integer.parseInt(it.get("complainStatus").toString())));
            it.put("timeOutReason", timeoutOrderDao.findOrderTimeoutReasonName(Integer.parseInt(it.get("reasonId") + "")));
            
            // 订单审核信息
            /*if (checkOrder == 1)
            {
                Map<String, Object> map = orderCheckInfoMap.get(orderId);
                String checkTime = map == null ? "" : (map.get("checkTime") == null ? "" : DateTimeUtil.timestampObjectToWebString(map.get("checkTime")));
                String checkStatusDesc = OrderEnum.OrderCheckStatusEnum.getDescByCode(Integer.valueOf(it.get("checkStatus") + ""));
                it.put("checkTime", checkTime);
                it.put("checkStatusDesc", checkStatusDesc);
            }*/
            
            rows.add(it);
        }
        info.clear();
        return rows;
    }
}
