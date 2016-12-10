package com.ygg.admin.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.RefundDao;
import com.ygg.admin.service.CustomerStatisticsService;

@Service("customerStatisticsService")
public class CustomerStatisticsServiceImpl implements CustomerStatisticsService
{
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private RefundDao refundDao;
    
    @Override
    public Map<String, Object> refundListOfDay(String date)
        throws Exception
    {
        String year = StringUtils.substringBefore(date, "-");
        String month = StringUtils.substringAfter(date, "-");
        
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("startTime", date + "-01 00:00:00");
        param.put("endTime", year + "-" + (Integer.valueOf(month) + 1) + "-01 00:00:00");
        List<Map<String, Object>> refundOrderInfoList = orderDao.findRefundForEveryday(param);
        List<Map<String, Object>> refundInfoList = refundDao.findRefundForEveryday(param);
        param.put("refundType", 1);
        List<Map<String, Object>> customerRefundType1InfoList = orderDao.findCustomerRefundForEveryday(param);
        param.put("refundType", 2);
        List<Map<String, Object>> customerRefundType2InfoList = orderDao.findCustomerRefundForEveryday(param);
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (refundOrderInfoList != null && refundOrderInfoList.size() > 0)
        {
            for (Map<String, Object> refundOrderInfo : refundOrderInfoList)
            {
                Map<String, Object> item = new HashMap<String, Object>();
                // 退款订单数据（status 2,3,4）
                String itemDate = refundOrderInfo.get("date").toString();
                item.put("day", itemDate);
                item.put("count", refundOrderInfo.get("count"));
                item.put("realMoney", refundOrderInfo.get("realPrice"));
                // 退款中的数据（status 1,2,3,4）
                appendRefundInfoForEveryDay(item, refundInfoList, itemDate);
                // 用户主动申请退款type 类型1：仅退款
                appendCustomerRefundMoneyForEveryDay(item, customerRefundType1InfoList, 1);
                // 用户主动申请退款type 类型2：退款并退货
                appendCustomerRefundMoneyForEveryDay(item, customerRefundType2InfoList, 2);
                
                resultList.add(item);
            }
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", resultList.size());
        result.put("rows", resultList);
        return result;
    }
    
    private void appendRefundInfoForEveryDay(Map<String, Object> item, List<Map<String, Object>> refundInfoList, String itemDate)
    {
        if (refundInfoList != null && refundInfoList.size() > 0)
        {
            for (Map<String, Object> refundInfo : refundInfoList)
            {
                if(StringUtils.equals(itemDate, refundInfo.get("date").toString())) {
                    if(StringUtils.equals("1", refundInfo.get("type").toString()))
                    {
                        item.put("realMoney1", refundInfo.get("realMoney"));
                    }
                    if(StringUtils.equals("2", refundInfo.get("type").toString()))
                    {
                        item.put("realMoney2", refundInfo.get("realMoney"));
                    }
                }
            }
        }
        else
        {
            item.put("realMoney1", 0);
            item.put("realMoney2", 0);
        }
    }
    
    /**
     * 用户主动退款
     * @param item
     * @param customerrefundInfoList
     * @param type 1：仅退款，2：退款并退货
     */
    private void appendCustomerRefundMoneyForEveryDay(Map<String, Object> item, List<Map<String, Object>> customerrefundInfoList, int type) {
        if(customerrefundInfoList == null || customerrefundInfoList.size() < 1)
            return ;
        for (Map<String, Object> map : customerrefundInfoList)
        {
            if(StringUtils.equals(item.get("day").toString(), map.get("date").toString()))
            {
                item.put("count", Integer.valueOf(map.get("count").toString()) + Integer.valueOf(item.get("count").toString()));
                
                BigDecimal rm = new BigDecimal(item.get("realMoney").toString());
                item.put("realMoney", rm.add(new BigDecimal(map.get("realPrice").toString())));
                
                BigDecimal rm1 = new BigDecimal(item.get("realMoney" + type).toString());
                item.put("realMoney" + type, rm1.add(new BigDecimal(map.get("realMoney").toString())));
                break;
            }
        }
    }
    
    @Override
    public Map<String, Object> refundListOfSeller(String startTime, String endTime, int sellerId, int page, int rows, int isExport)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        if(isExport == 0) {
            param.put("start", rows * (page - 1));
            param.put("size", rows);
        }
        // 获取查询条件
        // 商家ID列表
        List<Object> sellerIdList = getSellerIdList(sellerId, startTime, endTime, page, rows);
        
        param.put("sellerIdList", sellerIdList);
        
        // 统计仅退款
        param.put("type", 1);
        List<Map<String, Object>> refundInfoList1 = refundDao.findRefundForSeller(param);
        List<Map<String, Object>> customerRefundInfoList1 = orderDao.findCustomerRefundForSeller(param);
        
        // 统计退款并退货
        param.put("type", 2);
        List<Map<String, Object>> refundInfoList2 = refundDao.findRefundForSeller(param);
        List<Map<String, Object>> customerRefundInfoList2 = orderDao.findCustomerRefundForSeller(param);
        
        // 订单信息、商家信息
        List<Map<String, Object>> refundOrderInfoList = orderDao.findRefundForSeller(param);
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Object o : sellerIdList)
        {
            Map<String, Object> item = new HashMap<String, Object>();
            String itemSellerId = o.toString();
            item.put("sellerId", itemSellerId);
            // 退款订单中数据type=1
            getElement(refundInfoList1, itemSellerId, "1", item);
            // 退款订单中数据type=2
            getElement(refundInfoList2, itemSellerId, "2", item);
            // 订单中的数据
            appendElement(item, itemSellerId, refundOrderInfoList);
            // 用户主动退款中的数据type=1
            appendCustomerRefundMoneyForSeller(item, customerRefundInfoList1, 1);
            // 用户主动退款中的数据type=2
            appendCustomerRefundMoneyForSeller(item, customerRefundInfoList2, 2);
            resultList.add(item);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", refundDao.countRefundSellerIdForSeller(param));
        result.put("rows", resultList);
        return result;
    }
    
    private List<Object> getSellerIdList(int sellerId, String startTime, String endTime, int page, int rows) throws Exception
    {
        List<Object> sellerIdList = new ArrayList<Object>();
        if(sellerId != 0)
        {
            // sellerId存在，
            // 指定查询商家信息
            sellerIdList.add(sellerId);
        }
        else 
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("startTime", startTime);
            param.put("endTime", endTime);
            // sellerId不存在
            // 统计满足条件的sellerId
            List<Map<String, Object>> tempList = refundDao.findRefundSellerIdForSeller(param);
            List<Map<String, Object>> tempCustomRefundList = orderDao.findCustomerRefundForSeller(param);
            
            if(tempList != null && tempList.size() > 0)
            {
                RefundBeanFactory factory = new RefundBeanFactory();
                List<RefundBean> refundBeanList = factory.createBean(tempList);
                factory.updateBean(refundBeanList, tempCustomRefundList);
                Collections.sort(refundBeanList);
                
                // rows 等于0，导出所有的数据
                int count = rows == 0 ? refundBeanList.size() : rows;
                // 满足条件的记录小于count时，count值改为满足条件的记录数
                count = count > refundBeanList.size() ? refundBeanList.size() : count;
                int index = page == 0 ? 0 : (rows * (page - 1));
                for (int i = 0; i < count; i++)
                {
                    sellerIdList.add(refundBeanList.get(index + i).getSellerId());
                }
            }
            else 
            {
                sellerIdList.add(0);
            }
        }
        return sellerIdList;
    }
    
    class RefundBeanFactory {
        
        List<RefundBean> createBean(List<Map<String, Object>> resourceList)
        {
            List<RefundBean> list = new ArrayList<RefundBean>();
            for (Map<String, Object> map : resourceList)
            {
                RefundBean bean = new RefundBean(map.get("sellerId").toString(), map.get("realPrice").toString(), map.get("realMoney").toString());
                list.add(bean);
            }
            return list;
        }
        
        void updateBean(List<RefundBean> target, List<Map<String, Object>> resourceList)
        {
            for (RefundBean bean : target)
            {
                bean.updateRealMoney(resourceList);
                bean.updateRealPrice(resourceList);
                bean.updateRate();
            }
        }
    }
    
    class RefundBean implements Comparable<RefundBean>, Serializable {
        
        private static final long serialVersionUID = 8418245662690374573L;
        private String sellerId;
        private BigDecimal realPrice;
        private BigDecimal realMoney;
        private Double rate;
        
        RefundBean(String sellerId, String realPrice, String realMoney) {
            this.sellerId = sellerId;
            this.realPrice = new BigDecimal(realPrice);
            this.realMoney = new BigDecimal(realMoney);
        }
        
        public void updateRate()
        {
            this.rate = this.realPrice.doubleValue() == 0 ? 0 : this.realMoney.divide(this.realPrice, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        
        public void updateRealPrice(List<Map<String, Object>> resourceList)
        {
            for (Map<String, Object> map : resourceList)
            {
                if(StringUtils.equals(this.sellerId, map.get("sellerId").toString()))
                {
                    this.realPrice = this.realPrice.add(new BigDecimal(map.get("realPrice").toString()));
                    break;
                }
            }
        }
        
        public void updateRealMoney(List<Map<String, Object>> resourceList)
        {
            for (Map<String, Object> map : resourceList)
            {
                if(StringUtils.equals(this.sellerId, map.get("sellerId").toString()))
                {
                    this.realMoney = this.realMoney.add(new BigDecimal(map.get("realMoney").toString()));
                    break;
                }
            }
        }
        
        @Override
        public int compareTo(RefundBean o)
        {
            int c = this.rate.compareTo(o.getRate());
            return c * -1;
        }

        public String getSellerId()
        {
            return sellerId;
        }

        public void setSellerId(String sellerId)
        {
            this.sellerId = sellerId;
        }

        public BigDecimal getRealPrice()
        {
            return realPrice;
        }

        public void setRealPrice(BigDecimal realPrice)
        {
            this.realPrice = realPrice;
        }

        public BigDecimal getRealMoney()
        {
            return realMoney;
        }

        public void setRealMoney(BigDecimal realMoney)
        {
            this.realMoney = realMoney;
        }

        public Double getRate()
        {
            return rate;
        }

        public void setRate(Double rate)
        {
            this.rate = rate;
        }

        @Override
        public String toString()
        {
            return "sellerId:" + this.sellerId + " rate:" + this.rate;
        }
    }
    
    private void appendCustomerRefundMoneyForSeller(Map<String, Object> item, List<Map<String, Object>> customerrefundInfoList, int type) {
        if(customerrefundInfoList == null || customerrefundInfoList.size() < 1)
            return ;
        for (Map<String, Object> map : customerrefundInfoList)
        {
            if(StringUtils.equals(item.get("sellerId").toString(), map.get("sellerId").toString()))
            {
                Object r = item.get("realMoney") == null ? "0" : item.get("realMoney");
                BigDecimal rm = new BigDecimal(r.toString());
                item.put("realMoney", rm.add(new BigDecimal(map.get("realPrice").toString())));
                
                BigDecimal rm1 = new BigDecimal(item.get("realMoney" + type).toString());
                item.put("realMoney" + type, rm1.add(new BigDecimal(map.get("realMoney").toString())));
                break;
            }
        }
    }
    
    private void appendElement(Map<String, Object> item, String sellerId, List<Map<String, Object>> refundOrderInfoList)
    {
        for (Map<String, Object> refundOrderInfo : refundOrderInfoList)
        {
            if(StringUtils.equals(sellerId, refundOrderInfo.get("sellerId").toString()))
            {
                item.put("sellerName", refundOrderInfo.get("sellerName"));
                item.put("count", refundOrderInfo.get("count"));
                item.put("realMoney", refundOrderInfo.get("realPrice"));
            }
        }
    }
    
    private void getElement(List<Map<String, Object>> refundInfoList, String sellerId, String type, Map<String, Object> item)
    {
        String key = "realMoney" + type;
        Object o = null;
        for (Map<String, Object> refundInfo : refundInfoList)
        {
            if(StringUtils.equals(type, refundInfo.get("type").toString()) && StringUtils.equals(sellerId, refundInfo.get("sellerId").toString()))
            {
                o = refundInfo.get("realMoney");
            }
        }
        item.put(key, o == null ? 0 : o);
    }
    
}
