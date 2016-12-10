package com.ygg.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.OrderDetailInfoForSeller;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.CommonService;
import com.ygg.admin.service.LogisticsTimeoutService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.MathUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class LogisticsTimeoutServiceImpl implements LogisticsTimeoutService
{
    @Resource(name = "orderDao")
    private OrderDao orderDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private AccountDao accountDao;
    
    @Resource
    private ReceiveAddressDao receiveAddressDao;
    
    @Resource
    private LogisticsTimeoutDao logisticsTimeoutDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private CommonService commonService;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private TimeoutOrderDao timeoutOrderDao;
    
    @Override
    public List<Map<String, Object>> findAllLogisticsTimeoutOrders(String startTime, String endTime, int orderType)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sendTimeBegin", startTime);
        para.put("sendTimeEnd", endTime);
        para.put("orderType", orderType);
        
        List<Map<String, Object>> orderInfo = logisticsTimeoutDao.findAllLogisticsTimeoutOrders(para);
        Map<String, List<Map<String, Object>>> groupBySellerMap = new HashMap<String, List<Map<String, Object>>>();
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
            
            int totalAmount = sellerOrderList.size();
            Set<Integer> totalIdList = new HashSet<>();
            
            int ontimeAmount = 0;// 时效内物流更新
            Set<Integer> ontimeIdList = new HashSet<>();
            
            int timeoutUpateAmount = 0;// 超时物流更新
            Set<Integer> timeoutUpateIdList = new HashSet<>();
            
            int timeoutNoUpdateAmount = 0;// 超时物流未更新
            Set<Integer> timeoutNoUpdateIdList = new HashSet<>();
            
            int notUpdateAmount = 0;// 总体未更新
            Set<Integer> notUpdateIdList = new HashSet<>();
            for (Map<String, Object> item : sellerOrderList)
            {
                int orderId = Integer.parseInt(item.get("orderId").toString());
                int isTimeout = Integer.parseInt(item.get("isTimeout").toString());
                Timestamp logisticsTime = item.get("logisticsTime") == null ? null : (Timestamp)item.get("logisticsTime");
                
                if (isTimeout == CommonConstant.COMMON_YES)
                {
                    if (logisticsTime == null)
                    {
                        // 超时未更新
                        timeoutNoUpdateAmount++;
                        timeoutNoUpdateIdList.add(orderId);
                        
                        notUpdateAmount++;
                        notUpdateIdList.add(orderId);
                    }
                    else
                    {
                        // 超时更新
                        timeoutUpateAmount++;
                        timeoutUpateIdList.add(orderId);
                    }
                }
                else
                {
                    if (logisticsTime == null)
                    {
                        // 时校内未更新
                        notUpdateAmount++;
                        notUpdateIdList.add(orderId);
                    }
                    else
                    {
                        // 时效内更新
                        ontimeAmount++;
                        ontimeIdList.add(orderId);
                    }
                }
                
                totalIdList.add(orderId);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("sellerId", sellerId);
            SellerEntity seller = sellerDao.findSellerById(Integer.parseInt(sellerId));
            map.put("sellerName", seller == null ? "" : seller.getRealSellerName());
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
    public Map<String, Object> jsonLogisticsOrders(Map<String, Object> para)
        throws Exception
    {
        // 若有订单来源，先查询来源ID
        String orderSource = para.get("orderSource") == null ? null : para.get("orderSource") + "";
        if (orderSource != null)
        {
            Map<String, Object> sourcePara = new HashMap<>();
            sourcePara.put("name", orderSource);
            List<Map<String, Object>> reList = orderDao.findAllOrderChannel(sourcePara);
            if (reList.size() > 0)
            {
                para.put("orderSourceId", reList.get(0).get("id"));
            }
            else
            {
                Map<String, Object> _result = new HashMap<>();
                _result.put("total", 0);
                _result.put("rows", new ArrayList<>());
                return _result;
            }
        }
        
        // 用户名
        String accountName = para.get("accountName") == null ? null : para.get("accountName") + "";
        if (accountName != null)
        {
            List<Integer> accountIdList = accountDao.findIdListByName(accountName);
            if (accountIdList.size() > 0)
            {
                para.put("accountIdList", accountIdList);
            }
            else
            {
                Map<String, Object> _result = new HashMap<>();
                _result.put("total", 0);
                _result.put("rows", new ArrayList<>());
                return _result;
            }
        }
        
        // 收货信息
        String mobileNumber = para.get("mobileNumber") == null ? null : para.get("mobileNumber") + "";
        String fullName = para.get("fullName") == null ? null : para.get("fullName") + "";
        if (mobileNumber != null || fullName != null)
        {
            Map<String, Object> spara = new HashMap<>();
            if (fullName != null)
            {
                spara.put("fullName", fullName);
            }
            if (mobileNumber != null)
            {
                spara.put("mobileNumber", mobileNumber);
            }
            List<Integer> receiveAddressIdList = orderDao.findOrderReceiveAddressIdListByFullNameAndPhone(para);
            if (receiveAddressIdList.size() > 0)
            {
                para.put("receiveAddressIdList", receiveAddressIdList);
            }
            else
            {
                Map<String, Object> _result = new HashMap<>();
                _result.put("total", 0);
                _result.put("rows", new ArrayList<>());
                return _result;
            }
        }
        
        // 物流信息
        String logisticsNumber = para.get("logisticsNumber") == null ? null : para.get("logisticsNumber") + "";
        if (logisticsNumber != null)
        {
            List<Integer> orderIdList = orderDao.findOrderIdListByOrderLogisticsNumber(logisticsNumber);
            if (orderIdList.size() > 0)
            {
                para.put("orderIdList", orderIdList);
            }
            else
            {
                Map<String, Object> _result = new HashMap<>();
                _result.put("total", 0);
                _result.put("rows", new ArrayList<>());
                return _result;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        int total = 0;
        int sqlType = Integer.valueOf(para.get("sqlType") == null ? "1" : para.get("sqlType") + "");
        if (sqlType == 1)
        {
            total = logisticsTimeoutDao.countLogisticsTimeoutOrders(para);
            List<Map<String, Object>> info = logisticsTimeoutDao.findLogisticsTimeoutOrders(para);
            rows = packageOrderInfo(info);
        }
        else
        {
            if (sqlType == 12)
            {
                String productId = para.get("productId") == null ? null : para.get("productId") + "";
                List<Integer> productIdList = new ArrayList<>();
                productIdList.add(Integer.valueOf(productId));
                para.put("productIdList", productIdList);
            }
            else
            {
                // 包含商品信息
                String productId = para.get("productId") == null ? null : para.get("productId") + "";
                if (productId == null)
                {
                    Map<String, Object> _cpara = new HashMap<>();
                    String productName = para.get("productName") == null ? null : para.get("productName") + "";
                    String productRemark = para.get("productRemark") == null ? null : para.get("productRemark") + "";
                    String code = para.get("code") == null ? null : para.get("code").toString().trim();
                    if (productName != null)
                    {
                        _cpara.put("productName", productName);
                    }
                    if (productRemark != null)
                    {
                        _cpara.put("productRemark", productRemark);
                    }
                    if (StringUtils.isNotEmpty(code))
                    {
                        _cpara.put("code", code);
                    }
                    List<Integer> productIdList = productDao.findProductIdByNameAndRemark(_cpara);
                    if (productIdList.size() > 0)
                    {
                        para.put("productIdList", productIdList);
                    }
                    else
                    {
                        Map<String, Object> _result = new HashMap<>();
                        _result.put("total", 0);
                        _result.put("rows", new ArrayList<>());
                        return _result;
                    }
                }
                else
                {
                    List<Integer> productIdList = new ArrayList<>();
                    productIdList.add(Integer.valueOf(productId));
                    para.put("productIdList", productIdList);
                }
            }
            
            // 查询数据库
            total = logisticsTimeoutDao.countLogisticsTimeoutOrders2(para);
            List<Map<String, Object>> info = logisticsTimeoutDao.findLogisticsTimeoutOrders2(para);
            rows = packageOrderInfo(info);
            info.clear();
        }
        
        result.put("total", total);
        result.put("rows", rows);
        return result;
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
            int id = Integer.valueOf(it.get("orderId") + "");
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
        
        // 查询订单发货超时QC处理记录
        Map<String, Map<String, Object>> sendTimeoutQcDealMap = new HashMap<>();
        if (orderIdList.size() > 0)
        {
            List<Map<String, Object>> list = timeoutOrderDao.findOrderTimeoutQcDealListByOids(orderIdList);
            for (Map<String, Object> it : list)
            {
                String id = it.get("orderId") + "";
                sendTimeoutQcDealMap.put(id, it);
            }
        }
        
        // 查询订单物流时效超时QC处理记录
        Map<String, Map<String, Object>> logisticsTimeoutQcDealMap = new HashMap<>();
        if (orderIdList.size() > 0)
        {
            List<Map<String, Object>> list = logisticsTimeoutDao.findOrderLogisticsTimeoutQcListByOids(orderIdList);
            for (Map<String, Object> it : list)
            {
                String id = it.get("orderId") + "";
                logisticsTimeoutQcDealMap.put(id, it);
            }
        }
        
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map<String, Object> it : info)
        {
            int status = Integer.valueOf(it.get("status") + "");
            String number = it.get("number") + "";
            it.put("createTime", it.get("createTime").toString());
            it.put("expireTime", it.get("expireTime") == null ? "" : it.get("expireTime").toString());
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
            String orderId = it.get("orderId") + "";
            Map<String, Object> o = logisticsInfoMap.get(orderId);
            it.put("ologChannel", o != null ? o.get("channel") : "");
            it.put("ologNumber", o != null ? o.get("number") : "");
            it.put("ologMoney", o != null ? o.get("money") : "" + "");
            
            // 订单超时信息
            int reasonId = Integer.parseInt(it.get("reasonId").toString());
            int complainStatus = Integer.parseInt(it.get("complainStatus").toString());
            String timeOutreason = logisticsTimeoutDao.findLogisticsTimeoutReasonById(reasonId);
            it.put("timeOutreason", timeOutreason == null ? "" : timeOutreason);
            it.put("timeOutResult", OrderEnum.LogisticsTimeoutComplainResultEnum.getNameByCode(complainStatus));
            Map<String, Object> complainMap = logisticsTimeoutDao.findRecentLogisticsTimeoutComplainByOrderId(Integer.parseInt(orderId));
            it.put("complainId", complainMap == null ? 0 : complainMap.get("id") == null ? "" : complainMap.get("id"));
            it.put("complainReason", complainMap == null ? 0 : complainMap.get("reason") == null ? "" : complainMap.get("reason"));
            
            // 订单发货超时QC处理记录
            Map<String, Object> sendTimeQcDealMap = sendTimeoutQcDealMap.get(orderId);
            if (sendTimeQcDealMap == null)
            {
                it.put("isSendTimeoutDeal", 0);
            }
            else
            {
                it.put("isSendTimeoutDeal", 1);
            }
            
            // 订单物流时效超时QC处理记录
            Map<String, Object> logisticsTimeQcDealMap = logisticsTimeoutQcDealMap.get(orderId);
            if (logisticsTimeQcDealMap == null)
            {
                it.put("isLogisticsTimeoutDeal", 0);
            }
            else
            {
                it.put("isLogisticsTimeoutDeal", 1);
            }
            
            rows.add(it);
        }
        info.clear();
        return rows;
    }
    
    @Override
    public Map<String, Object> jsonLogisticsTimeoutReasonInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", logisticsTimeoutDao.findAllLogisticsTimeoutReason(para));
        resultMap.put("total", logisticsTimeoutDao.countLogisticsTimeoutReason(para));
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
            Map<String, Object> para = new HashMap<>();
            if (id == 0)
            {
                para.put("name", name);
                para.put("createUser", commonService.getCurrentUserId());
                result = logisticsTimeoutDao.insertTimeoutReason(para);
            }
            else
            {
                para.put("id", id);
                para.put("name", name);
                result = logisticsTimeoutDao.updateTimeoutReason(para);
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
    public String updateLogisticsTimeoutReasonStatus(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (logisticsTimeoutDao.updateTimeoutReason(para) > 0)
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
    public String jsonComplainOrderInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", packageOrderInfo(logisticsTimeoutDao.findLogisticsTimeoutOrders(para)));
        resultMap.put("total", logisticsTimeoutDao.countLogisticsTimeoutOrders(para));
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public Map<String, Object> findLogisticsTimeoutByOid(int orderId)
        throws Exception
    {
        return logisticsTimeoutDao.findLogisticsTimeoutByOid(orderId);
    }
    
    @Override
    public List<Map<String, Object>> findComplainDetailsByOrderId(int orderId)
        throws Exception
    {
        List<Map<String, Object>> reList = logisticsTimeoutDao.findLogisticsTimeoutComplainListByOrderId(orderId);
        for (Map<String, Object> it : reList)
        {
            it.put("complainTime", ((Timestamp)it.get("createTime")).toString());
            it.put("dealTime", ((Timestamp)it.get("updateTime")).toString());
            
            User user = userDao.findUserById(Integer.parseInt(it.get("dealUser").toString()));
            it.put("dealUser", user == null ? "系统管理员" : user.getRealname());
            it.put("result", OrderEnum.LogisticsTimeoutComplainResultEnum.getNameByCode(Integer.parseInt(it.get("status").toString())));
        }
        return reList;
    }
    
    @Override
    public String findLogisticsTimeoutReasonById(int reasonId)
        throws Exception
    {
        return logisticsTimeoutDao.findLogisticsTimeoutReasonById(reasonId);
    }
    
    @Override
    public List<Map<String, Object>> findAllTimeoutReason(Map<String, Object> para)
        throws Exception
    {
        return logisticsTimeoutDao.findAllTimeoutReason(para);
    }
    
    @Override
    public String dealComplain(int orderId, int complainId, int timeoutReasonId, int dealResult, String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", complainId);
        para.put("status", dealResult);
        para.put("remark", remark);
        para.put("dealUser", commonService.getCurrentUserId());
        if (logisticsTimeoutDao.updateLogisticsTimeoutComplain(para) > 0)
        {
            para.clear();
            para.put("orderId", orderId);
            para.put("status", dealResult);
            para.put("timeoutReasonId", timeoutReasonId);
            para.put("dealUser", commonService.getCurrentUserId());
            if (dealResult == OrderEnum.LogisticsTimeoutComplainResultEnum.SUCCESS.getCode())
            {
                para.put("isTimeout", CommonConstant.COMMON_NO);
            }
            
            if (logisticsTimeoutDao.updateLogisticsTimeout(para) > 0)
            {
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
    public String logisticsTimeOutComplain(int orderId, String reason)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("orderId", orderId);
        para.put("reason", reason);
        if (logisticsTimeoutDao.insertLogisticsTimeoutComplain(para) > 0)
        {
            para.put("status", OrderEnum.LogisticsTimeoutComplainResultEnum.PROCESSING.getCode());
            if (logisticsTimeoutDao.updateLogisticsTimeout(para) > 0)
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
        if (logisticsTimeoutDao.addTimeoutOrderQcDeal(para) > 0)
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
        if (logisticsTimeoutDao.batchAddTimeoutOrderQcDeal(params) > 0)
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
        List<OrderDetailInfoForSeller> sourcesDatas = logisticsTimeoutDao.findAllTimeoutOrderDetail(para);
        // 去重
        for (OrderDetailInfoForSeller detail : sourcesDatas)
        {
            detailMap.put(detail.getoNumber(), detail);
        }
        return new ArrayList<OrderDetailInfoForSeller>(detailMap.values());
    }
    
    @Override
    public int updateLogisticsTimeout(Map<String, Object> para)
        throws Exception
    {
        return logisticsTimeoutDao.updateLogisticsTimeout(para);
    }
    
    @Override
    public int getExportOrderNums(Map<String, Object> para)
        throws Exception
    {
        return logisticsTimeoutDao.countTimeoutOrderDetail(para);
    }
}
