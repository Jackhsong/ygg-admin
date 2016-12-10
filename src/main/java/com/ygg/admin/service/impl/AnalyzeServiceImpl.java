package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.ActivitySimplifyDao;
import com.ygg.admin.dao.AnalyzeDao;
import com.ygg.admin.dao.BannerWindowDao;
import com.ygg.admin.dao.CategoryDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.SpecialActivityDao;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.BannerWindowEntity;
import com.ygg.admin.entity.SaleWindowEntity;
import com.ygg.admin.entity.UserStatisticView;
import com.ygg.admin.service.AnalyzeService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.MathUtil;
import com.ygg.admin.view.ClientBuyView;
import com.ygg.admin.view.ClientDetailView;
import com.ygg.admin.view.UserBehaviorView;

@Service("analyzeService")
public class AnalyzeServiceImpl implements AnalyzeService
{
    DecimalFormat df = new DecimalFormat("0.00");
    
    @Resource
    private SellerDao sellerDao = null;
    
    @Resource
    private ProductDao productDao = null;
    
    @Resource
    private OrderDao orderDao = null;
    
    @Resource
    private BannerWindowDao bannerWindowDao = null;
    
    @Resource
    private SaleWindowDao saleWindowDao = null;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao = null;
    
    @Resource
    private AccountDao accountDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private SpecialActivityDao specialActivityDao;
    
    @Resource
    private ActivitySimplifyDao activitySimplifyDao;
    
    @Resource
    private AnalyzeDao analyzeDao;
    
    @Resource
    private CategoryDao categoryDao;
    
    @Override
    public Map<String, Object> sellerDataCustom(Map<String, Object> para)
        throws Exception
    {
        
        List<Map<String, Object>> reList = sellerDao.findAllSellerSaleInfo(para);
        
        // key为sellerID value为改seller下的订单
        Map<String, List<Map<String, Object>>> groupBySellerId = new HashMap<>();
        for (Map<String, Object> currMap : reList)
        {
            String sellerId = currMap.get("sId") + "";
            List<Map<String, Object>> sellerIdList = groupBySellerId.get(sellerId);
            if (sellerIdList == null)
            {
                sellerIdList = new ArrayList<>();
                groupBySellerId.put(sellerId, sellerIdList);
            }
            sellerIdList.add(currMap);
        }
        // 总计，，根据sellerID 分组 sellerId+totalNum : xxx ; sellerId+totalPrice : xxx
        Map<String, Object> sellerCountInfoMap = new HashMap<>();
        for (Entry<String, List<Map<String, Object>>> entry : groupBySellerId.entrySet())
        {
            String sellerId = entry.getKey();
            String numKey = sellerId + "totalNum";
            String priceKey = sellerId + "totalPrice";
            int totalNum = 0;
            double totalPrice = 0;
            List<Map<String, Object>> va = entry.getValue();
            for (Map<String, Object> currMap : va)
            {
                totalNum += 1;
                Float currTotalPrice = Float.parseFloat(currMap.get("totalPrice") == null ? "0.0" : currMap.get("totalPrice") + "");
                totalPrice += currTotalPrice;
            }
            sellerCountInfoMap.put(numKey, totalNum);
            sellerCountInfoMap.put(priceKey, new DecimalFormat("0.00").format(totalPrice));
        }
        
        // rows sellerid:1 tp1:100,tp2:200 .... ,num1:30,num2:50 ....
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Entry<String, List<Map<String, Object>>> entry : groupBySellerId.entrySet())
        {
            Map<String, Object> currRow = new HashMap<>();
            String sellerId = entry.getKey();
            String sellerName = null;
            String sendAddress = null;
            
            List<Map<String, Object>> va = entry.getValue();
            for (Map<String, Object> currMap : va)
            {
                if (sellerName == null)
                {
                    sellerName = (String)currMap.get("sellerName");
                    sendAddress = (String)currMap.get("sendAddress");
                }
                
                // 作为中间变量使用
                Timestamp payTime = null;
                DateTime payTimeDateTime = null;
                int day = 0;
                
                // 具体内容
                
                payTime = (Timestamp)currMap.get("payTime");
                payTimeDateTime = new DateTime(payTime.getTime());
                
                // 按天 划分 放入每天的 总价格 & 总数量
                day = payTimeDateTime.getDayOfMonth();
                String tpKey = "tp" + day;
                String numKey = "num" + day;
                Float currTotalPrice = Float.parseFloat(currMap.get("totalPrice") == null ? "0.0" : currMap.get("totalPrice") + "");
                
                // 判断currRow 中是否已经存在改key所对应的值
                
                Double totalPrice = currRow.get(tpKey) != null ? Double.valueOf(currRow.get(tpKey) + "") : null;
                if (totalPrice == null)
                {
                    // currRow.put(tpKey, currTotalPrice);
                    currRow.put(tpKey, new DecimalFormat("0.00").format(currTotalPrice));
                    currRow.put(numKey, 1);
                }
                else
                {
                    totalPrice += currTotalPrice;
                    Integer num = Integer.parseInt(currRow.get(numKey) == null ? "0" : currRow.get(numKey) + "");
                    num += 1;
                    currRow.put(tpKey, new DecimalFormat("0.00").format(totalPrice));
                    currRow.put(numKey, num);
                }
                
            }
            currRow.put("sellerId", sellerId);
            currRow.put("sellerName", sellerName);
            currRow.put("sendAddress", sendAddress);
            currRow.put("totalNum", sellerCountInfoMap.get(sellerId + "totalNum"));
            currRow.put("totalPrice", sellerCountInfoMap.get(sellerId + "totalPrice"));
            rows.add(currRow);
        }
        
        // 统计每日数据
        Map<String, Object> lastRow = new HashMap<>();
        for (int i = 1; i <= 31; i++)
        {
            String tpKey = "tp" + i;
            String numKey = "num" + i;
            int totalNum = 0;
            double totalPrice = 0;
            for (Map<String, Object> map : rows)
            {
                Object currNum = map.get(numKey);
                if (currNum != null)
                {
                    totalNum += Integer.valueOf(map.get(numKey) + "");
                }
                Object currPrice = map.get(tpKey);
                if (currPrice != null)
                {
                    totalPrice += Double.valueOf(map.get(tpKey) + "");
                }
            }
            lastRow.put(tpKey, new DecimalFormat("0.00").format(totalPrice));
            lastRow.put(numKey, totalNum);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("lastRow", lastRow);
        return result;
    }
    
    @Override
    public Map<String, Object> productDataCustom(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> productSaleInfoList = productDao.findProductSalesRecord(para);
        // 将productSaleInfoList按 商品id 分组
        Map<String, List<Map<String, Object>>> groupByProductId = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> currMap : productSaleInfoList)
        {
            String productId = currMap.get("productId") + "";
            List<Map<String, Object>> productIdList = (List<Map<String, Object>>)groupByProductId.get(productId);
            if (productIdList == null)
            {
                productIdList = new ArrayList<Map<String, Object>>();
                groupByProductId.put(productId, productIdList);
            }
            productIdList.add(currMap);
        }
        
        // rows productId:1 tp1:100,tp2:200 .... ,num1:30,num2:50 ....
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        for (Entry<String, List<Map<String, Object>>> entry : groupByProductId.entrySet())
        {
            List<Map<String, Object>> va = (List<Map<String, Object>>)entry.getValue();
            String productId = entry.getKey();
            Map<String, Object> currRow = new HashMap<String, Object>();
            
            String productBaseId = null;
            if (va == null || va.size() == 0)
            {
                productBaseId = "0";
            }
            else
            {
                productBaseId = va.get(0).get("productBaseId") == null ? "0" : va.get(0).get("productBaseId") + "";
            }
            String categoryName = categoryDao.findCategoryFirstNameBybid(productBaseId);
            String sellerName = null;
            String sendAddress = null;
            String shortName = null;
            String code = null;
            String productType = null;
            String brandName = null;
            
            // 对所有lineItem遍历 计算
            for (Map<String, Object> currMap : va)
            {
                if (sellerName == null)
                {
                    sellerName = currMap.get("sellerName") + "";
                    sendAddress = currMap.get("sendAddress") + "";
                    shortName = currMap.get("shortName") + "";
                    code = currMap.get("code") + "";
                    productType = ProductEnum.PRODUCT_TYPE.getDescByCode(Integer.parseInt(currMap.get("type") + ""));
                    brandName = currMap.get("brandName") + "";
                }
                
                // 作为中间变量使用
                Timestamp payTime = null;
                DateTime payTimeDateTime = null;
                int day = 0;
                // 具体内容
                payTime = (Timestamp)currMap.get("payTime");
                payTimeDateTime = new DateTime(payTime.getTime());
                // 按天 划分 放入每天的 总价格 & 总数量
                day = payTimeDateTime.getDayOfMonth();
                String tpKey = "tp" + day;
                String numKey = "num" + day;
                Float currSalesPrice = Float.parseFloat(currMap.get("salesPrice") == null ? "0.0" : currMap.get("salesPrice") + "");
                Integer currProductCount = Integer.parseInt(currMap.get("productCount") == null ? "0" : currMap.get("productCount") + "");
                // 判断currRow 中是否已经存在改key所对应的值
                Double todayTotalPrice = currRow.get(tpKey) != null ? Double.valueOf(currRow.get(tpKey) + "") : null;
                if (todayTotalPrice == null)
                {
                    // 第一次put
                    double lineItemPrice = currSalesPrice * currProductCount;
                    currRow.put(tpKey, new DecimalFormat("0.00").format(lineItemPrice));
                    currRow.put(numKey, currProductCount);
                }
                else
                {
                    double lineItemPrice = currSalesPrice * currProductCount;
                    todayTotalPrice += lineItemPrice;
                    Integer num = Integer.parseInt(currRow.get(numKey) == null ? "0" : currRow.get(numKey) + "");
                    num += currProductCount;
                    currRow.put(tpKey, new DecimalFormat("0.00").format(todayTotalPrice));
                    currRow.put(numKey, num);
                }
            }
            currRow.put("productId", productId);
            currRow.put("productBaseId", productBaseId);
            currRow.put("categoryName", categoryName);
            currRow.put("sellerName", sellerName);
            currRow.put("sendAddress", sendAddress);
            currRow.put("shortName", shortName);
            currRow.put("code", code);
            currRow.put("productType", productType);
            currRow.put("brandName", brandName);
            rows.add(currRow);
        }
        
        // 总计每一行
        for (Map<String, Object> currMap : rows)
        {
            double totalPrice = 0;
            int totalNum = 0;
            for (int i = 1; i <= 31; i++)
            {
                Object cuP = currMap.get("tp" + i);
                Object cuN = currMap.get("num" + i);
                if (cuP != null)
                {
                    totalPrice += Double.valueOf(currMap.get("tp" + i) + "").doubleValue();
                }
                if (cuN != null)
                {
                    totalNum += Integer.valueOf(currMap.get("num" + i) + "").intValue();
                }
            }
            currMap.put("totalPrice", new DecimalFormat("0.00").format(totalPrice));
            currMap.put("totalNum", totalNum);
        }
        
        // 统计每日数据
        Map<String, Object> lastRow = new HashMap<String, Object>();
        for (int i = 1; i <= 31; i++)
        {
            String tpKey = "tp" + i;
            String numKey = "num" + i;
            int totalNum = 0;
            double totalPrice = 0;
            for (Map<String, Object> map : rows)
            {
                Object currNum = map.get(numKey);
                if (currNum != null)
                {
                    totalNum += Integer.valueOf(map.get(numKey) + "");
                }
                Object currPrice = map.get(tpKey);
                if (currPrice != null)
                {
                    totalPrice += Double.valueOf(map.get(tpKey) + "");
                }
            }
            lastRow.put(tpKey, new DecimalFormat("0.00").format(totalPrice));
            lastRow.put(numKey, totalNum);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", rows);
        result.put("lastRow", lastRow);
        return result;
    }
    
    @Override
    public Map<String, Object> monthAnalyze(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        String selectDate = para.get("selectDate") + "";
        List<Map<String, Object>> result = orderDao.findOrderSalesRecordForMonthAnalyze(para);
        // 将数据按月天分组 key为天
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        Timestamp payTime = null;
        DateTime payTimeDateTime = null;
        int day = 0;
        for (Map<String, Object> currMap : result)
        {
            payTime = (Timestamp)currMap.get("createTime");
            payTimeDateTime = new DateTime(payTime.getTime());
            day = payTimeDateTime.getDayOfMonth();
            String key = "" + day;// 1
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(currMap);
        }
        Set<Long> totalPersons = new HashSet<Long>();
        List<ForSortRow> rows = new ArrayList<ForSortRow>();
        // 遍历map key为天，，，value为改天下对应的所有已付款订单信息
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            String currKey = entry.getKey();// 1
            List<Map<String, Object>> dayList = entry.getValue();
            double totalPrice = 0;
            int payOrderCount = 0;
            Set<Long> persons = new HashSet<Long>();
            for (Map<String, Object> currMap : dayList)
            {
                Float currTotalPrice = Float.parseFloat(currMap.get("totalPrice") == null ? "0.0" : currMap.get("totalPrice") + "");
                Long accountId = (Long)currMap.get("accountId");
                int status = Integer.parseInt(currMap.get("status") + "");
                if (status == OrderEnum.ORDER_STATUS.REVIEW.getCode() || status == OrderEnum.ORDER_STATUS.SENDGOODS.getCode() || status == OrderEnum.ORDER_STATUS.SUCCESS.getCode()
                    || status == OrderEnum.ORDER_STATUS.USER_CANCEL.getCode())
                {
                    payOrderCount++;
                    totalPrice += currTotalPrice;
                    persons.add(accountId);
                    totalPersons.add(accountId);
                }
            }
            int totalOrderCount = dayList.size();//创建订单总数
            int totalPersonCount = persons.size();
            // 开始计算 (笔单价 = 订单金额 / 订单数量) (客单价 = 订单金额 / 成交人数)
            double divOrderCountPrice = totalPrice / payOrderCount;
            double divPersonCountPrice = totalPrice / totalPersonCount;
            double divPayOrderCount = payOrderCount * 1.0d / totalOrderCount * 100.0d;
            ForSortRow row = new ForSortRow();
            row.setTotalOrderCount(totalOrderCount + "");
            row.setPayOrderCount(payOrderCount + "");
            row.setTotalPersonCount(totalPersonCount + "");
            row.setTotalPrice(new DecimalFormat("0.00").format(totalPrice));
            row.setDivOrderCountPrice(new DecimalFormat("0.00").format(divOrderCountPrice));
            row.setDivPersonCountPrice(new DecimalFormat("0.00").format(divPersonCountPrice));
            row.setDivPayOrderCount(new DecimalFormat("0.00").format(divPayOrderCount) + "%");
            row.setDateStr(selectDate + "-" + currKey);
            row.setDate(Integer.valueOf(currKey));
            rows.add(row);
        }
        Collections.sort(rows, new Comparator<ForSortRow>()
        {
            
            @Override
            public int compare(ForSortRow o1, ForSortRow o2)
            {
                return o1.getDate() - o2.getDate();
            }
            
        });
        List<Object> newrows = new ArrayList<Object>();
        int totalPersonCount = totalPersons.size();
        int totalOrderCount = 0;
        int payOrderCount = 0;
        double totalPrice = 0.0;
        for (ForSortRow forSortRow : rows)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", forSortRow.getDate());
            map.put("dateStr", forSortRow.getDateStr());
            map.put("divOrderCountPrice", forSortRow.getDivOrderCountPrice());
            map.put("divPersonCountPrice", forSortRow.getDivPersonCountPrice());
            map.put("divPayOrderCount", forSortRow.getDivPayOrderCount());
            map.put("totalOrderCount", forSortRow.getTotalOrderCount());
            map.put("payOrderCount", forSortRow.getPayOrderCount());
            map.put("totalPersonCount", forSortRow.getTotalPersonCount());
            map.put("totalPrice", forSortRow.getTotalPrice());
            newrows.add(map);
            totalOrderCount += Integer.parseInt(forSortRow.getTotalOrderCount());
            payOrderCount += Integer.parseInt(forSortRow.getPayOrderCount());
            totalPrice += Double.parseDouble(forSortRow.getTotalPrice());
            //            totalPersonCount += Integer.parseInt(forSortRow.getTotalPersonCount());
        }
        resultMap.put("rows", newrows);
        
        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("totalPrice", MathUtil.round(totalPrice, 2));
        lastRow.put("totalOrderCount", totalOrderCount + "");
        lastRow.put("payOrderCount", payOrderCount + "");
        lastRow.put("totalPersonCount", totalPersonCount + "");
        lastRow.put("divOrderCountPrice", payOrderCount == 0 ? "0" : MathUtil.round(totalPrice / payOrderCount, 2));
        lastRow.put("divPayOrderCount", totalOrderCount == 0 ? "0" : MathUtil.round(payOrderCount * 1.0d / totalOrderCount * 100.0d, 2) + "%");
        lastRow.put("divPersonCountPrice", totalPersonCount == 0 ? "0" : MathUtil.round(totalPrice / totalPersonCount, 2));
        resultMap.put("lastRow", lastRow);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> platformMonthAnalyze(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        String selectDate = para.get("selectDate") + "";
        List<Map<String, Object>> result = orderDao.findOrderSalesRecordForPlatformMonthAnalyze(para);
        // 将数据按月天分组 key为天
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<>();
        DateTime payTimeDateTime;
        int day;
        for (Map<String, Object> currMap : result)
        {
            payTimeDateTime = new DateTime(((Timestamp)currMap.get("createTime")).getTime());
            day = payTimeDateTime.getDayOfMonth();
            String key = "" + day;// 1
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(currMap);
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        // 遍历map key为天，，，value为该天订单信息
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            String currKey = entry.getKey();// 1
            List<Map<String, Object>> orderList = entry.getValue();
            double totalPrice = 0;
            int payOrderCount = 0;
            double ggjTotalPrice = 0;
            int ggjPayOrderCount = 0;
            double ggtTotalPrice = 0;
            int ggtPayOrderCount = 0;
            double qqbsTotalPrice = 0;
            int qqbsPayOrderCount = 0;
            for (Map<String, Object> currMap : orderList)
            {
                Float currTotalPrice = Float.parseFloat(currMap.get("totalPrice") == null ? "0.0" : currMap.get("totalPrice") + "");
                int status = Integer.parseInt(currMap.get("status") + "");
                int type = Integer.parseInt(currMap.get("type") + "");
                int channel = Integer.parseInt(currMap.get("channel") + "");
                if (status == OrderEnum.ORDER_STATUS.REVIEW.getCode() || status == OrderEnum.ORDER_STATUS.SENDGOODS.getCode() || status == OrderEnum.ORDER_STATUS.SUCCESS.getCode()
                    || status == OrderEnum.ORDER_STATUS.USER_CANCEL.getCode())
                {
                    payOrderCount++;
                    totalPrice += currTotalPrice;
                    if (type == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                    {
                        qqbsTotalPrice += currTotalPrice;
                        qqbsPayOrderCount++;
                    }
                    else if (type == OrderEnum.ORDER_TYPE.GEGEJIA.getCode())
                    {
                        ggjTotalPrice += currTotalPrice;
                        ggjPayOrderCount++;
                    }
                    else if (type == OrderEnum.ORDER_TYPE.GEGETUAN.getCode())
                    {
                        ggtTotalPrice += currTotalPrice;
                        ggtPayOrderCount++;
                    }
                }
            }
            Map<String, Object> row = new HashMap();
            row.put("payOrderCount", payOrderCount + "");
            row.put("totalPrice", new DecimalFormat("0.00").format(totalPrice));
            row.put("ggjPayOrderCount", ggjPayOrderCount + "");
            row.put("ggjTotalPrice", new DecimalFormat("0.00").format(ggjTotalPrice));
            row.put("ggtPayOrderCount", ggtPayOrderCount + "");
            row.put("ggtTotalPrice", new DecimalFormat("0.00").format(ggtTotalPrice));
            row.put("qqbsPayOrderCount", qqbsPayOrderCount + "");
            row.put("qqbsTotalPrice", new DecimalFormat("0.00").format(qqbsTotalPrice));
            row.put("dateStr", selectDate + "-" + currKey);
            row.put("date", Integer.valueOf(currKey));
            rows.add(row);
        }
        Collections.sort(rows, new Comparator<Map>()
        {
            
            @Override
            public int compare(Map o1, Map o2)
            {
                return (Integer)o1.get("date") - (Integer)o2.get("date");
            }
        });
        Map<String, Object> lastRow = new HashMap();
        double totalPrice = 0;
        int payOrderCount = 0;
        double ggjTotalPrice = 0;
        int ggjPayOrderCount = 0;
        double ggtTotalPrice = 0;
        int ggtPayOrderCount = 0;
        double qqbsTotalPrice = 0;
        int qqbsPayOrderCount = 0;
        for (Map<String, Object> row : rows)
        {
            totalPrice += Double.parseDouble((String)row.get("totalPrice"));
            payOrderCount += Integer.parseInt((String)row.get("payOrderCount"));
            ggjTotalPrice += Double.parseDouble((String)row.get("ggjTotalPrice"));
            ggjPayOrderCount += Integer.parseInt((String)row.get("ggjPayOrderCount"));
            ggtTotalPrice += Double.parseDouble((String)row.get("ggtTotalPrice"));
            ggtPayOrderCount += Integer.parseInt((String)row.get("ggtPayOrderCount"));
            qqbsTotalPrice += Double.parseDouble((String)row.get("qqbsTotalPrice"));
            qqbsPayOrderCount += Integer.parseInt((String)row.get("qqbsPayOrderCount"));
        }
        lastRow.put("payOrderCount", payOrderCount + "");
        lastRow.put("totalPrice", new DecimalFormat("0.00").format(totalPrice));
        lastRow.put("ggjPayOrderCount", ggjPayOrderCount + "");
        lastRow.put("ggjTotalPrice", new DecimalFormat("0.00").format(ggjTotalPrice));
        lastRow.put("ggtPayOrderCount", ggtPayOrderCount + "");
        lastRow.put("ggtTotalPrice", new DecimalFormat("0.00").format(ggtTotalPrice));
        lastRow.put("qqbsPayOrderCount", qqbsPayOrderCount + "");
        lastRow.put("qqbsTotalPrice", new DecimalFormat("0.00").format(qqbsTotalPrice));
        lastRow.put("dateStr", "总计");
        rows.add(rows.size(), lastRow);
        resultMap.put("rows", rows);
        return resultMap;
    }
    
    class ForSortRow
    {
        private int date;
        
        private String totalOrderCount;
        
        private String payOrderCount;
        
        private String totalPersonCount;
        
        private String totalPrice;
        
        private String divOrderCountPrice;
        
        private String divPersonCountPrice;
        
        private String divPayOrderCount;
        
        private String dateStr;
        
        // 以下  新用户成交统计字段
        private String newPersonCount;//首次成交用户人数
        
        private String newPersonCountDivtotalPersonCount;//首次成交用户人数占比
        
        private String newPersonTotalPrice;//首次成交用户金额
        
        private String newPersonTotalPriceDivtotalPrice;//首次成交用户金额占比
        
        private String oldPersonCount;//老用户成交人数
        
        private String oldPersonCountDivtotalPersonCount;//老用户成交人数占比
        
        private String oldPersonTotalPrice;//老用户成交金额
        
        private String oldPersonTotalPriceDivtotalPrice;//老用户成交金额占比
        
        private String newOrderCount;//首次成交订单数
        
        private String oldOrderCount;//老用户成交订单数
        
        private String totalProductCount;//成交商品数
        
        private String newProductCount;//首次成交商品数
        
        private String oldProductCount;//老用户成交商品数
        
        private String ggjPayOrderCount;
        
        private String ggjTotalPrice;
        
        private String ggtPayOrderCount;
        
        private String ggtTotalPrice;
        
        private String qqbsPayOrderCount;
        
        private String qqbsTotalPrice;
        
        public String getNewPersonCount()
        {
            return newPersonCount;
        }
        
        public void setNewPersonCount(String newPersonCount)
        {
            this.newPersonCount = newPersonCount;
        }
        
        public String getNewPersonCountDivtotalPersonCount()
        {
            //1.0000   0.9520
            Double x = Double.parseDouble(newPersonCountDivtotalPersonCount) * 10000;
            return MathUtil.round(MathUtil.div(x, 100), 2) + "%";
        }
        
        public void setNewPersonCountDivtotalPersonCount(String newPersonCountDivtotalPersonCount)
        {
            this.newPersonCountDivtotalPersonCount = newPersonCountDivtotalPersonCount;
        }
        
        public String getNewPersonTotalPrice()
        {
            return newPersonTotalPrice;
        }
        
        public void setNewPersonTotalPrice(String newPersonTotalPrice)
        {
            this.newPersonTotalPrice = newPersonTotalPrice;
        }
        
        public String getNewPersonTotalPriceDivtotalPrice()
        {
            //1.0000   0.9520
            Double x = Double.parseDouble(newPersonTotalPriceDivtotalPrice) * 10000;
            return MathUtil.round(MathUtil.div(x, 100), 2) + "%";
        }
        
        public void setNewPersonTotalPriceDivtotalPrice(String newPersonTotalPriceDivtotalPrice)
        {
            this.newPersonTotalPriceDivtotalPrice = newPersonTotalPriceDivtotalPrice;
        }
        
        public String getOldPersonCount()
        {
            return oldPersonCount;
        }
        
        public void setOldPersonCount(String oldPersonCount)
        {
            this.oldPersonCount = oldPersonCount;
        }
        
        public String getPayOrderCount()
        {
            return payOrderCount;
        }
        
        public void setPayOrderCount(String payOrderCount)
        {
            this.payOrderCount = payOrderCount;
        }
        
        public String getDivPayOrderCount()
        {
            return divPayOrderCount;
        }
        
        public void setDivPayOrderCount(String divPayOrderCount)
        {
            this.divPayOrderCount = divPayOrderCount;
        }
        
        public String getOldPersonCountDivtotalPersonCount()
        {
            Double x = Double.parseDouble(oldPersonCountDivtotalPersonCount) * 10000;
            return MathUtil.round(MathUtil.div(x, 100), 2) + "%";
        }
        
        public void setOldPersonCountDivtotalPersonCount(String oldPersonCountDivtotalPersonCount)
        {
            this.oldPersonCountDivtotalPersonCount = oldPersonCountDivtotalPersonCount;
        }
        
        public String getOldPersonTotalPrice()
        {
            return oldPersonTotalPrice;
        }
        
        public void setOldPersonTotalPrice(String oldPersonTotalPrice)
        {
            this.oldPersonTotalPrice = oldPersonTotalPrice;
        }
        
        public String getOldPersonTotalPriceDivtotalPrice()
        {
            Double x = Double.parseDouble(oldPersonTotalPriceDivtotalPrice) * 10000;
            return MathUtil.round(MathUtil.div(x, 100), 2) + "%";
        }
        
        public void setOldPersonTotalPriceDivtotalPrice(String oldPersonTotalPriceDivtotalPrice)
        {
            this.oldPersonTotalPriceDivtotalPrice = oldPersonTotalPriceDivtotalPrice;
        }
        
        public String getTotalOrderCount()
        {
            return totalOrderCount;
        }
        
        public void setTotalOrderCount(String totalOrderCount)
        {
            this.totalOrderCount = totalOrderCount;
        }
        
        public String getTotalPersonCount()
        {
            return totalPersonCount;
        }
        
        public void setTotalPersonCount(String totalPersonCount)
        {
            this.totalPersonCount = totalPersonCount;
        }
        
        public int getDate()
        {
            return date;
        }
        
        public void setDate(int date)
        {
            this.date = date;
        }
        
        public String getTotalPrice()
        {
            return totalPrice;
        }
        
        public void setTotalPrice(String totalPrice)
        {
            this.totalPrice = totalPrice;
        }
        
        public String getDivOrderCountPrice()
        {
            return divOrderCountPrice;
        }
        
        public void setDivOrderCountPrice(String divOrderCountPrice)
        {
            this.divOrderCountPrice = divOrderCountPrice;
        }
        
        public String getDivPersonCountPrice()
        {
            return divPersonCountPrice;
        }
        
        public void setDivPersonCountPrice(String divPersonCountPrice)
        {
            this.divPersonCountPrice = divPersonCountPrice;
        }
        
        public String getDateStr()
        {
            return dateStr;
        }
        
        public void setDateStr(String dateStr)
        {
            this.dateStr = dateStr;
        }
        
        public String getNewOrderCount()
        {
            return newOrderCount;
        }
        
        public void setNewOrderCount(String newOrderCount)
        {
            this.newOrderCount = newOrderCount;
        }
        
        public String getOldOrderCount()
        {
            return oldOrderCount;
        }
        
        public void setOldOrderCount(String oldOrderCount)
        {
            this.oldOrderCount = oldOrderCount;
        }
        
        public String getTotalProductCount()
        {
            return totalProductCount;
        }
        
        public void setTotalProductCount(String totalProductCount)
        {
            this.totalProductCount = totalProductCount;
        }
        
        public String getNewProductCount()
        {
            return newProductCount;
        }
        
        public void setNewProductCount(String newProductCount)
        {
            this.newProductCount = newProductCount;
        }
        
        public String getOldProductCount()
        {
            return oldProductCount;
        }
        
        public void setOldProductCount(String oldProductCount)
        {
            this.oldProductCount = oldProductCount;
        }
        
        public String getGgjPayOrderCount()
        {
            return ggjPayOrderCount;
        }
        
        public void setGgjPayOrderCount(String ggjPayOrderCount)
        {
            this.ggjPayOrderCount = ggjPayOrderCount;
        }
        
        public String getGgjTotalPrice()
        {
            return ggjTotalPrice;
        }
        
        public void setGgjTotalPrice(String ggjTotalPrice)
        {
            this.ggjTotalPrice = ggjTotalPrice;
        }
        
        public String getGgtPayOrderCount()
        {
            return ggtPayOrderCount;
        }
        
        public void setGgtPayOrderCount(String ggtPayOrderCount)
        {
            this.ggtPayOrderCount = ggtPayOrderCount;
        }
        
        public String getGgtTotalPrice()
        {
            return ggtTotalPrice;
        }
        
        public void setGgtTotalPrice(String ggtTotalPrice)
        {
            this.ggtTotalPrice = ggtTotalPrice;
        }
        
        public String getQqbsPayOrderCount()
        {
            return qqbsPayOrderCount;
        }
        
        public void setQqbsPayOrderCount(String qqbsPayOrderCount)
        {
            this.qqbsPayOrderCount = qqbsPayOrderCount;
        }
        
        public String getQqbsTotalPrice()
        {
            return qqbsTotalPrice;
        }
        
        public void setQqbsTotalPrice(String qqbsTotalPrice)
        {
            this.qqbsTotalPrice = qqbsTotalPrice;
        }
        
        @Override
        public String toString()
        {
            return dateStr;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
                return false;
            if (this == obj)
                return true;
            if (obj instanceof ForSortRow)
            {
                ForSortRow other = (ForSortRow)obj;
                return this.date == other.date;
            }
            return false;
        }
        
    }
    
    class PlatformDayOrderInfo
    {
        String totalPrice;
        
        int payOrderCount = 0;
        
        String ggjTotalPrice;
        
        int ggjPayOrderCount = 0;
        
        String ggtTotalPrice;
        
        int ggtPayOrderCount = 0;
        
        double qqbsTotalPrice = 0;
        
        int qqbsPayOrderCount = 0;
        
        String dateStr;
        
        public Integer date;
    }
    
    @Override
    public List<Map<String, Object>> todayAnalyze(Map<String, Object> para)
        throws Exception
    {
        int searchType = Integer.parseInt(para.get("searchType") + "");
        List<Map<String, Object>> saleProductList = new ArrayList<>();
        Set<Integer> productIdList = new HashSet<>();
        Date minDateTime = new Date();
        // 特卖位
        // 查询时间
        Map<String, Object> saleWinPara = new HashMap<>();
        DateTime searchTime = DateTime.now();
        saleWinPara.put("status", 2); // 在售
        boolean beforeTen = DateTime.now().getHourOfDay() >= 10 ? false : true;
        int compareToStart = 0;
        int compareToEnd = 0;
        if (beforeTen)
        {
            compareToStart = Integer.valueOf(searchTime.toString("yyyyMMdd")).intValue();//20150929
            compareToEnd = Integer.valueOf(searchTime.plusDays(-2).toString("yyyyMMdd")).intValue();//20150929-->20150927
        }
        else
        {
            compareToStart = Integer.valueOf(searchTime.plusDays(1).toString("yyyyMMdd")).intValue();//20150930-->20151001
            compareToEnd = Integer.valueOf(searchTime.plusDays(-1).toString("yyyyMMdd")).intValue();//20150930-->20150929
        }
        saleWinPara.put("compareToStart", compareToStart);
        saleWinPara.put("compareToEnd", compareToEnd);
        saleWinPara.put("nowOrder", 1);
        List<SaleWindowEntity> saleWindowList = saleWindowDao.findAllSaleWindow(saleWinPara);
        for (SaleWindowEntity entity : saleWindowList)
        {
            if (entity.getIsDisplay() == 1)
            {
                // 展现
                Map<String, Object> map = new HashMap<>();
                map.put("type", entity.getType());
                if (entity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
                {
                    map.put("startTime", DateTimeUtil.string2Date(entity.getStartTime() + " 10:00:00", "yyyyMMdd HH:mm:ss"));
                    DateTime endTimeDT = DateTimeUtil.string2DateTime(entity.getEndTime() + " 10:00:00", "yyyyMMdd HH:mm:ss");
                    map.put("endTime", endTimeDT.plusDays(1).toDate());
                }
                else
                {
                    map.put("startTime", DateTimeUtil.string2Date(entity.getStartTime() + " 20:00:00", "yyyyMMdd HH:mm:ss"));
                    DateTime endTimeDT = DateTimeUtil.string2DateTime(entity.getEndTime() + " 20:00:00", "yyyyMMdd HH:mm:ss");
                    map.put("endTime", endTimeDT.plusDays(1).toDate());
                }
                List<Integer> idList = new ArrayList<>();
                if (entity.getType() == 1)
                {
                    // 单品
                    productIdList.add(entity.getDisplayId());
                    idList.add(entity.getDisplayId());
                    map.put("source", "特卖-单品");
                    map.put("name", "");
                }
                else if (entity.getType() == 2)
                {
                    // 通用专场
                    idList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(entity.getDisplayId());
                    productIdList.addAll(idList);
                    map.put("source", "特卖-专场");
                    map.put("name", entity.getName());
                    
                    // 访问具体url
                    String detailUrl = "/analyze/todaySaleDetail?type=2&id=" + entity.getId();
                    map.put("detailUrl", detailUrl);
                }
                else if (entity.getType() == 3)
                {
                    // 自定义专场
                    Map<String, Object> customActivity = customActivitiesDao.findCustomActivitiesById(entity.getDisplayId());
                    int typeCode = Integer.valueOf(customActivity.get("typeCode") == null ? "0" : customActivity.get("typeCode") + "");
                    int typeId = Integer.valueOf(customActivity.get("typeId") == null ? "0" : customActivity.get("typeId") + "");
                    if (CustomEnum.CUSTOM_ACTIVITY_RELATION.SALE_ACTIVITY.getCode() == typeCode)
                    {
                        //情景活动
                        List<Map<String, Object>> layoutProductData = specialActivityDao.findSpecialActivityLayouProductBySpecialActivityId(typeId);
                        for (Map<String, Object> it : layoutProductData)
                        {
                            int displayType = Integer.valueOf(it.get("displayType") == null ? "0" : it.get("displayType") + "");
                            int oneType = Integer.valueOf(it.get("oneType") == null ? "0" : it.get("oneType") + "");
                            int oneDisplayId = Integer.valueOf(it.get("oneDisplayId") == null ? "0" : it.get("oneDisplayId") + "");
                            int twoType = Integer.valueOf(it.get("twoType") == null ? "0" : it.get("twoType") + "");
                            int twoDisplayId = Integer.valueOf(it.get("twoDisplayId") == null ? "0" : it.get("twoDisplayId") + "");
                            if (displayType == 1)
                            {
                                if (oneType == 1)
                                {
                                    productIdList.add(oneDisplayId);
                                    idList.add(oneDisplayId);
                                }
                            }
                            else if (displayType == 2)
                            {
                                if (oneType == 1)
                                {
                                    productIdList.add(oneDisplayId);
                                    idList.add(oneDisplayId);
                                }
                                if (twoType == 1)
                                {
                                    productIdList.add(twoDisplayId);
                                    idList.add(twoDisplayId);
                                }
                            }
                        }
                        map.put("source", "特卖-情景");
                        map.put("name", entity.getName());
                        
                        // 访问具体url
                        String detailUrl = "/analyze/todaySaleDetail?type=2&id=" + entity.getId();
                        map.put("detailUrl", detailUrl);
                    }
                    else if (CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode() == typeCode)
                    {
                        //精品活动
                        idList = activitySimplifyDao.findActivitySimplifyProductIdBySimplifyActivityId(entity.getDisplayId());
                        productIdList.addAll(idList);
                        map.put("source", "特卖-精品");
                        map.put("name", entity.getName());
                        
                        // 访问具体url
                        String detailUrl = "/analyze/todaySaleDetail?type=2&id=" + entity.getId();
                        map.put("detailUrl", detailUrl);
                    }
                    else
                    {
                        continue;
                    }
                }
                if (searchType == 1)
                {
                    if (beforeTen)
                    {
                        minDateTime =
                            DateTimeUtil.string2Date(DateTime.now().withHourOfDay(10).withMinuteOfHour(0).withSecondOfMinute(0).minusDays(1).toString("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
                    }
                    else
                    {
                        minDateTime =
                            DateTimeUtil.string2Date(DateTime.now().withHourOfDay(10).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
                    }
                }
                else if (searchType == 2)
                {
                    int dateInt = entity.getStartTime();
                    Date nowD = DateTimeUtil.string2Date(dateInt + " 10:00:00", "yyyyMMdd HH:mm:ss");
                    if (nowD.before(minDateTime))
                    {
                        minDateTime = nowD;
                    }
                }
                map.put("idList", idList);
                map.put("saleTotalCount", "0");
                map.put("saleToalPrice", "0");
                saleProductList.add(map);
            }
        }
        // 今日所有线上特卖（包括单品和组合）的销售数量、金额、库存等情况；统计时间根据单品 或 组合的开始结束时间来计算
        if (productIdList.size() > 0)
        {
            Map<String, Object> orderProductPara = new HashMap<>();
            orderProductPara.put("minTime", DateTimeUtil.dateToString(minDateTime));
            
            orderProductPara.put("idList", productIdList);
            List<Map<String, Object>> reList = orderDao.findAllTodaySaleRelOrderProduct(orderProductPara);// oId,productId,salesPrice,productCount,payTime
            if (reList.size() > 0)
            {
                // 计算
                for (Map<String, Object> currMap : saleProductList)
                {
                    // source,type,idList,startTime,endTime
                    List<Integer> idList = (List<Integer>)currMap.get("idList");
                    Date startTime = (Date)currMap.get("startTime");
                    Date endTime = (Date)currMap.get("endTime");
                    
                    // 计算值
                    int saleTotalCount = 0;
                    double saleToalPrice = 0;
                    for (Map<String, Object> sMap : reList)
                    {
                        int _productId = Integer.valueOf(sMap.get("productId") + "");
                        if (idList.contains(_productId))
                        {
                            // 订单付款时间
                            String s = sMap.get("payTime") + "";
                            Date _date = DateTimeUtil.string2Date(s);
                            if (_date.after(startTime) && _date.before(endTime))
                            {
                                int currCount = Integer.parseInt(sMap.get("productCount") == null ? "0" : sMap.get("productCount") + "");
                                double currPrice = Double.parseDouble(sMap.get("salesPrice") == null ? "0.0" : sMap.get("salesPrice") + "");
                                saleTotalCount += currCount;
                                saleToalPrice += (currCount * currPrice);
                            }
                        }
                    }
                    currMap.put("saleTotalCount", saleTotalCount);
                    currMap.put("saleToalPrice", new DecimalFormat("0.00").format(saleToalPrice));
                }
            }
            // 计算 库存 & 特卖名称
            List<Map<String, Object>> stockList = orderDao.findProductStockAndNameByProductIdList(orderProductPara);
            for (Map<String, Object> currMap : saleProductList)
            {
                // 库存
                int totalStock = 0;
                String name = "";
                List<Integer> idList = (List<Integer>)currMap.get("idList");
                byte type = (byte)currMap.get("type");
                for (Map<String, Object> map2 : stockList)
                {
                    int _productId = Integer.valueOf(map2.get("productId") + "");
                    if (idList.contains(_productId))
                    {
                        int count = Integer.valueOf(map2.get("stock") + "");
                        totalStock += count;
                        
                        // type == 1 的情况下 idList.size() 必定 == 1
                        if (type == 1)
                        {
                            // 单品
                            name = map2.get("name") + "";
                        }
                    }
                }
                currMap.put("totalStock", totalStock);
                if (!"".equals(name))
                {
                    currMap.put("name", name);
                }
            }
            //按首页顺序排序
            
        }
        return saleProductList;
    }
    
    @Override
    public List<Map<String, Object>> todayAnalyzeDetail(int type, int id)
        throws Exception
    {
        Map<String, Object> commonMap = new HashMap<String, Object>();
        List<Integer> productIdList = new ArrayList<Integer>();
        Date minDateTime = new Date();
        if (type == 1)
        {
            // bannerWindow
            Map<String, Object> bannerPara = new HashMap<String, Object>();
            bannerPara.put("id", id);
            bannerPara.put("isDisplay", 1);
            bannerPara.put("status", 2);// 进行中
            List<BannerWindowEntity> bannerWindowList = bannerWindowDao.findAllBannerWindow(bannerPara);
            if (bannerWindowList.size() > 0)
            {
                BannerWindowEntity entity = bannerWindowList.get(0);
                productIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(entity.getDisplayId());
                commonMap.put("source", "banner专场-" + entity.getDesc());
                minDateTime = DateTimeUtil.string2Date(entity.getStartTime());
            }
        }
        else if (type == 2)
        {
            // saleWindow
            Map<String, Object> saleWinPara = new HashMap<String, Object>();
            DateTime searchTime = DateTime.now();
            saleWinPara.put("status", 2); // 在售
            saleWinPara.put("id", id); // 在售
            boolean beforeTen = DateTime.now().getHourOfDay() >= 10 ? false : true;
            int compareToStart = 0;
            int compareToEnd = 0;
            if (beforeTen)
            {
                compareToStart = Integer.valueOf(searchTime.toString("yyyyMMdd")).intValue();
                compareToEnd = Integer.valueOf(searchTime.plusDays(-2).toString("yyyyMMdd")).intValue();
            }
            else
            {
                compareToStart = Integer.valueOf(searchTime.plusDays(1).toString("yyyyMMdd")).intValue();
                compareToEnd = Integer.valueOf(searchTime.plusDays(-1).toString("yyyyMMdd")).intValue();
            }
            saleWinPara.put("compareToStart", compareToStart);
            saleWinPara.put("compareToEnd", compareToEnd);
            List<SaleWindowEntity> saleWindowList = saleWindowDao.findAllSaleWindow(saleWinPara);
            if (saleWindowList.size() > 0)
            {
                SaleWindowEntity entity = saleWindowList.get(0);
                if (entity.getType() == 2)
                {
                    productIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(entity.getDisplayId());
                    commonMap.put("source", "专场特卖-" + entity.getName());
                    minDateTime = DateTimeUtil.string2Date(entity.getStartTime() + " 10:00:00", "yyyyMMdd HH:mm:ss");
                }
                else if (entity.getType() == 3)
                {
                    // 自定义专场
                    Map<String, Object> customActivity = customActivitiesDao.findCustomActivitiesById(entity.getDisplayId());
                    int typeCode = Integer.valueOf(customActivity.get("typeCode") == null ? "0" : customActivity.get("typeCode") + "");
                    int typeId = Integer.valueOf(customActivity.get("typeId") == null ? "0" : customActivity.get("typeId") + "");
                    if (CustomEnum.CUSTOM_ACTIVITY_RELATION.SALE_ACTIVITY.getCode() == typeCode)
                    {
                        //情景活动
                        List<Map<String, Object>> layoutProductData = specialActivityDao.findSpecialActivityLayouProductBySpecialActivityId(typeId);
                        for (Map<String, Object> it : layoutProductData)
                        {
                            int displayType = Integer.valueOf(it.get("displayType") == null ? "0" : it.get("displayType") + "");
                            int oneType = Integer.valueOf(it.get("oneType") == null ? "0" : it.get("oneType") + "");
                            int oneDisplayId = Integer.valueOf(it.get("oneDisplayId") == null ? "0" : it.get("oneDisplayId") + "");
                            int twoType = Integer.valueOf(it.get("twoType") == null ? "0" : it.get("twoType") + "");
                            int twoDisplayId = Integer.valueOf(it.get("twoDisplayId") == null ? "0" : it.get("twoDisplayId") + "");
                            if (displayType == 1)
                            {
                                if (oneType == 1)
                                {
                                    productIdList.add(oneDisplayId);
                                }
                            }
                            else if (displayType == 2)
                            {
                                if (oneType == 1)
                                {
                                    productIdList.add(oneDisplayId);
                                }
                                if (twoType == 1)
                                {
                                    productIdList.add(twoDisplayId);
                                }
                            }
                        }
                        commonMap.put("source", "自定义特卖-" + entity.getName());
                        minDateTime = DateTimeUtil.string2Date(entity.getStartTime() + " 10:00:00", "yyyyMMdd HH:mm:ss");
                    }
                    else if (CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode() == typeCode)
                    {
                        productIdList = activitySimplifyDao.findActivitySimplifyProductIdBySimplifyActivityId(typeId);
                        commonMap.put("source", "自定义特卖-" + entity.getName());
                        minDateTime = DateTimeUtil.string2Date(entity.getStartTime() + " 10:00:00", "yyyyMMdd HH:mm:ss");
                    }
                    else
                    {
                    }
                }
            }
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        if (productIdList.size() > 0)
        {
            Map<String, Object> orderProductPara = new HashMap<>();
            orderProductPara.put("minTime", DateTimeUtil.dateToString(minDateTime));
            orderProductPara.put("idList", productIdList);
            List<Map<String, Object>> reList = orderDao.findAllTodaySaleRelOrderProduct(orderProductPara);// oId,productId,salesPrice,productCount,payTime
            if (reList.size() > 0)
            {
                for (Integer pid : productIdList)
                {
                    Map<String, Object> row = new HashMap<>();
                    // 计算值
                    int saleTotalCount = 0;
                    double saleToalPrice = 0;
                    for (Map<String, Object> sMap : reList)
                    {
                        int _productId = Integer.valueOf(sMap.get("productId") + "");
                        if (pid == _productId)
                        {
                            int currCount = Integer.parseInt(sMap.get("productCount") == null ? "0" : sMap.get("productCount") + "");
                            float currPrice = Float.parseFloat(sMap.get("salesPrice") == null ? "0.0" : sMap.get("salesPrice") + "");
                            saleTotalCount += currCount;
                            saleToalPrice += (currCount * currPrice);
                        }
                    }
                    row.put("saleTotalCount", saleTotalCount);
                    row.put("saleToalPrice", new DecimalFormat("0.00").format(saleToalPrice));
                    row.put("id", pid);
                    row.put("source", commonMap.get("source"));
                    rows.add(row);
                }
            }
            
            // 计算 库存 & 特卖名称
            List<Map<String, Object>> stockList = orderDao.findProductStockAndNameByProductIdList(orderProductPara);
            for (Map<String, Object> row : rows)
            {
                int productId = Integer.parseInt(row.get("id") == null ? "0" : row.get("id") + "");
                for (Map<String, Object> map2 : stockList)
                {
                    int _productId = Integer.valueOf(map2.get("productId") + "");
                    if (_productId == productId)
                    {
                        int count = Integer.valueOf(map2.get("stock") + "");
                        row.put("totalStock", count);
                        row.put("name", map2.get("name") + "");
                    }
                }
            }
        }
        return rows;
    }
    
    @Override
    public Map<String, Object> registAnalyze(String selectDate, String channel)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        DateTime curr_turns_today = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));//2015-06-01 00:00:00
        DateTime curr_turns_tomorrow = curr_turns_today.plusDays(1);//2015-06-02 00:00:00
        DateTime nextMonth = curr_turns_today.plusMonths(1);////2015-07-01 00:00:00
        int _total = 0;
        int _mobile = 0;
        int _qq = 0;
        int _weixin = 0;
        int _weibo = 0;
        int _alipay = 0;
        while (curr_turns_today.isBefore(DateTime.now().getMillis()) && !curr_turns_tomorrow.isAfter(nextMonth.getMillis()))
        {
            Map<String, Object> row = new HashMap<String, Object>();
            String date = curr_turns_today.toString("yyyy-MM-dd");
            row.put("dateStr", date);
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("createTimeBegin", curr_turns_today.toString("yyyy-MM-dd HH:mm:ss"));
            searchPara.put("createTimeEnd", curr_turns_tomorrow.toString("yyyy-MM-dd HH:mm:ss"));
            if (channel != null && !"".equals(channel))
            {
                searchPara.put("channel", channel);
            }
            List<AccountEntity> acs = accountDao.findAllAccountByPara(searchPara);
            int total = 0;
            int mobile = 0;
            int qq = 0;
            int weixin = 0;
            int weibo = 0;
            int alipay = 0;
            for (AccountEntity it : acs)
            {
                if (it.getType() == AccountEnum.ACCOUNT_TYPE.GGT_WEIXIN.getCode() || it.getType() == AccountEnum.ACCOUNT_TYPE.GGT_APP.getCode()
                    || it.getType() == AccountEnum.ACCOUNT_TYPE.QUAN_QIU_BU_SHOU.getCode())
                {
                    //不统计左岸城堡（微信、app）和左岸城堡

                    continue;
                }
                if ((int)it.getType() == AccountEnum.ACCOUNT_TYPE.ALIPAY.getCode())
                {
                    _alipay++;
                    alipay++;
                }
                else if ((int)it.getType() == AccountEnum.ACCOUNT_TYPE.MOBILE.getCode())
                {
                    _mobile++;
                    mobile++;
                }
                else if ((int)it.getType() == AccountEnum.ACCOUNT_TYPE.QQ.getCode())
                {
                    _qq++;
                    qq++;
                }
                else if ((int)it.getType() == AccountEnum.ACCOUNT_TYPE.SINA.getCode())
                {
                    _weibo++;
                    weibo++;
                }
                else if ((int)it.getType() == AccountEnum.ACCOUNT_TYPE.WEIXIN.getCode())
                {
                    _weixin++;
                    weixin++;
                }
                _total++;
                total++;
            }
            row.put("total", total + "");
            row.put("mobile", mobile + "");
            row.put("qq", qq + "");
            row.put("weixin", weixin + "");
            row.put("weibo", weibo + "");
            row.put("alipay", alipay + "");
            rows.add(row);
            curr_turns_today = curr_turns_today.plusDays(1);
            curr_turns_tomorrow = curr_turns_tomorrow.plusDays(1);
        }
        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("total", _total + "");
        lastRow.put("mobile", _mobile + "");
        lastRow.put("qq", _qq + "");
        lastRow.put("weixin", _weixin + "");
        lastRow.put("weibo", _weibo + "");
        lastRow.put("alipay", _alipay + "");
        result.put("lastRow", lastRow);
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public List<Object> newAccountBuyingAnalyze(Map<String, Object> para)
        throws Exception
    {
        String selectDate = para.get("selectDate") + "";
        String payTimeBegin = para.get("payTimeBegin") + "";
        
        //订单记录
        List<Map<String, Object>> result = orderDao.findOrderSalesRecord(para);
        
        //订单商品
        List<Map<String, Object>> productCountList = orderDao.findOrderProductCount(para);
        Map<String, Integer> orderIdAndCountMap = new HashMap<String, Integer>();
        for (Map<String, Object> mp : productCountList)
        {
            String orderId = mp.get("orderId") + "";
            int count = Integer.parseInt(mp.get("productCount") == null ? "0" : mp.get("productCount") + "");
            if (orderIdAndCountMap.get(orderId) == null)
            {
                orderIdAndCountMap.put(orderId, count);
            }
            else
            {
                orderIdAndCountMap.put(orderId, count + orderIdAndCountMap.get(orderId));
            }
        }
        
        // 将数据按月天分组 key为天
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        Timestamp payTime = null;
        DateTime payTimeDateTime = null;
        int day = 0;
        for (Map<String, Object> currMap : result)
        {
            payTime = (Timestamp)currMap.get("payTime");
            payTimeDateTime = new DateTime(payTime.getTime());
            day = payTimeDateTime.getDayOfMonth();
            String key = "" + day;// 1
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(currMap);
        }
        
        int sumTotalPersonCount = 0;
        int sumNewPersonCount = 0;
        double sumOldPersonTotalPrice = 0d;
        double sumNewPersonTotalPrice = 0d;
        double sumPersonTotalPrice = 0d;
        int sumTotalOrderCount = 0;
        int sumNewOrderCount = 0;
        int sumTotalProductCount = 0;
        int sumNewProductCount = 0;
        Set<Integer> personSets = new HashSet<Integer>();
        List<ForSortRow> rows = new ArrayList<ForSortRow>();
        // 遍历map key为天，，，value为改天下对应的所有已付款订单信息
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            String currKey = entry.getKey();// 1
            List<Map<String, Object>> dayList = entry.getValue();
            Set<Integer> persons = new HashSet<Integer>();
            for (Map<String, Object> currMap : dayList)
            {
                Integer accountId = Integer.valueOf(currMap.get("accountId") + "");
                persons.add(accountId);
            }
            personSets.addAll(persons);
            //统计新用户数量
            String searchDateStr = selectDate + "-" + currKey + " 00:00:00"; //2015-06-1
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("payTimeEnd", DateTimeUtil.string2DateTime(searchDateStr).plusDays(-1).toDate());
            searchPara.put("accountIdList", persons);
            List<Integer> oldAccounts = orderDao.findOrderAccountIdByPara(searchPara);
            
            double oldPersonTotalPrice = 0d;//老用户订单总销量
            double personTotalPrice = 0d;
            int totalOrderCount = dayList == null ? 0 : dayList.size();//成交订单总数
            int newOrderCount = 0;
            int totalProductCount = 0;
            int newProductCount = 0;
            for (Map<String, Object> currMap : dayList)
            {
                Float currTotalPrice = Float.parseFloat(currMap.get("totalPrice") == null ? "0.0" : currMap.get("totalPrice") + "");
                Integer accountId = Integer.valueOf(currMap.get("accountId") + "");
                String orderId = currMap.get("id") + "";
                int productCount = orderIdAndCountMap.get(orderId) == null ? 0 : orderIdAndCountMap.get(orderId).intValue();
                if (oldAccounts.contains(accountId))
                {
                    oldPersonTotalPrice += currTotalPrice;
                }
                else
                {
                    newOrderCount++;
                    newProductCount += productCount;
                }
                totalProductCount += productCount;
                personTotalPrice += currTotalPrice;
            }
            
            int totalPersonCount = persons.size();
            int newPersonCount = persons.size() - oldAccounts.size();
            double newPersonTotalPrice = personTotalPrice - oldPersonTotalPrice;
            //            System.out.println(currKey + " " + oldAccounts + " " + persons);
            ForSortRow row = new ForSortRow();
            row.setTotalPersonCount(totalPersonCount + "");//成交人数
            row.setNewPersonCount(newPersonCount + "");//首次成交用户人数
            row.setOldPersonCount((totalPersonCount - newPersonCount) + "");//老用户成交人数
            row.setNewPersonCountDivtotalPersonCount(MathUtil.round(MathUtil.div(newPersonCount + "", totalPersonCount + ""), 4));//首次成交用户人数占比
            row.setOldPersonCountDivtotalPersonCount(MathUtil.round(MathUtil.div((totalPersonCount - newPersonCount) + "", totalPersonCount + ""), 4));
            row.setTotalPrice(new DecimalFormat("0.00").format(personTotalPrice));
            row.setNewPersonTotalPrice(new DecimalFormat("0.00").format(newPersonTotalPrice));
            row.setOldPersonTotalPrice(new DecimalFormat("0.00").format(oldPersonTotalPrice));
            row.setNewPersonTotalPriceDivtotalPrice(MathUtil.round(MathUtil.div(newPersonTotalPrice + "", personTotalPrice + ""), 4));
            row.setOldPersonTotalPriceDivtotalPrice(MathUtil.round(MathUtil.div(oldPersonTotalPrice + "", personTotalPrice + ""), 4));
            row.setDateStr(selectDate + "-" + currKey);
            row.setDate(Integer.valueOf(currKey));
            row.setTotalOrderCount(totalOrderCount + "");
            row.setNewOrderCount(newOrderCount + "");
            row.setTotalProductCount(totalProductCount + "");
            row.setNewProductCount(newProductCount + "");
            rows.add(row);
            
            sumOldPersonTotalPrice += oldPersonTotalPrice;
            sumPersonTotalPrice += personTotalPrice;
            sumTotalOrderCount += totalOrderCount;
            sumNewOrderCount += newOrderCount;
            sumTotalProductCount += totalProductCount;
            sumNewProductCount += newProductCount;
        }
        
        Collections.sort(rows, new Comparator<ForSortRow>()
        {
            
            @Override
            public int compare(ForSortRow o1, ForSortRow o2)
            {
                return o1.getDate() - o2.getDate();
            }
            
        });
        List<Object> newrows = new ArrayList<Object>();
        
        for (ForSortRow forSortRow : rows)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", forSortRow.getDate());
            map.put("dateStr", forSortRow.getDateStr());
            map.put("totalPersonCount", forSortRow.getTotalPersonCount());
            map.put("newPersonCount", forSortRow.getNewPersonCount());
            map.put("oldPersonCount", forSortRow.getOldPersonCount());
            map.put("newPersonCountDivtotalPersonCount", forSortRow.getNewPersonCountDivtotalPersonCount());
            map.put("totalPrice", forSortRow.getTotalPrice());
            map.put("newPersonTotalPrice", forSortRow.getNewPersonTotalPrice());
            map.put("oldPersonTotalPrice", forSortRow.getOldPersonTotalPrice());
            map.put("newPersonTotalPriceDivtotalPrice", forSortRow.getNewPersonTotalPriceDivtotalPrice());
            map.put("totalOrderCount", forSortRow.getTotalOrderCount());
            map.put("newOrderCount", forSortRow.getNewOrderCount());
            map.put("totalProductCount", forSortRow.getTotalProductCount());
            map.put("newProductCount", forSortRow.getNewProductCount());
            newrows.add(map);
        }
        sumTotalPersonCount = personSets.size();
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put("payTimeEnd", payTimeBegin);
        if (!personSets.isEmpty())
        {
            mp.put("accountIdList", new ArrayList<Integer>(personSets));
        }
        List<Integer> allOldAccounts = orderDao.findOrderAccountIdByPara(mp);
        Set<Integer> oldPersonSet = new HashSet<Integer>(allOldAccounts);
        
        personSets.removeAll(oldPersonSet);
        sumNewPersonCount = personSets.size();
        sumNewPersonTotalPrice = sumPersonTotalPrice - sumOldPersonTotalPrice;
        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("dateStr", "总计");
        lastRow.put("totalPersonCount", sumTotalPersonCount + "");
        lastRow.put("newPersonCount", sumNewPersonCount + "");
        lastRow.put("oldPersonCount", (sumTotalPersonCount - sumNewPersonCount) + "");
        lastRow.put("newPersonCountDivtotalPersonCount", MathUtil.round(MathUtil.div(sumNewPersonCount * 100 + "", sumTotalPersonCount + ""), 2) + "%");
        lastRow.put("totalPrice", new DecimalFormat("0.00").format(sumPersonTotalPrice));
        lastRow.put("newPersonTotalPrice", new DecimalFormat("0.00").format(sumNewPersonTotalPrice));
        lastRow.put("oldPersonTotalPrice", new DecimalFormat("0.00").format(sumOldPersonTotalPrice));
        lastRow.put("newPersonTotalPriceDivtotalPrice", MathUtil.round(MathUtil.div(sumNewPersonTotalPrice * 100 + "", sumPersonTotalPrice + ""), 2) + "%");
        lastRow.put("totalOrderCount", sumTotalOrderCount + "");
        lastRow.put("newOrderCount", sumNewOrderCount + "");
        lastRow.put("totalProductCount", sumTotalProductCount + "");
        lastRow.put("newProductCount", sumNewProductCount + "");
        newrows.add(lastRow);
        return newrows;
    }
    
    @Override
    public List<Map<String, Object>> todaySaleTop(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = orderDao.findAllTodaySaleRelOrderProduct(para);
        Map<String, List<Map<String, Object>>> groupByProductId = new HashMap<String, List<Map<String, Object>>>();
        Map<Double, Map<String, Object>> sortMapByCount = new TreeMap<Double, Map<String, Object>>();
        for (Map<String, Object> it : reList)
        {
            String productId = it.get("productId") + "";
            List<Map<String, Object>> culist = groupByProductId.get(productId);
            if (culist == null)
            {
                culist = new ArrayList<>();
                groupByProductId.put(productId, culist);
            }
            culist.add(it);
        }
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByProductId.entrySet())
        {
            List<Map<String, Object>> currSale = entry.getValue();
            double totalSalePrice = 0;
            Long totalCount = Long.parseLong("0");
            String name = "";
            String stock = "";
            String productId = "";
            String type = "";
            String brandName = "";
            String code = "";
            for (Map<String, Object> it : currSale)
            {
                if ("".equals(name))
                {
                    name = it.get("name") + "";
                    stock = it.get("stock") + "";
                    productId = it.get("productId") + "";
                    type = ProductEnum.PRODUCT_TYPE.getDescByCode(Integer.parseInt(it.get("type") + ""));
                    brandName = it.get("brandName") + "";
                    code = it.get("code") + "";
                }
                double salesPrice = Double.parseDouble(it.get("salesPrice") == null ? "0" : it.get("salesPrice") + "");
                Long productCount = Long.parseLong(it.get("productCount") + "");
                totalCount += productCount;
                totalSalePrice += (salesPrice * productCount);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("totalSalePrice", MathUtil.round(totalSalePrice, 2));
            map.put("name", name);
            map.put("productId", productId);
            map.put("type", type);
            map.put("stock", stock);
            map.put("totalCount", totalCount.toString());
            map.put("brandName", brandName);
            map.put("code", code);
            
            if (sortMapByCount.get(totalSalePrice) == null)
            {
                sortMapByCount.put(totalSalePrice, map);
            }
            else
            {
                while (true)
                {
                    totalSalePrice = totalSalePrice + 0.001;
                    if (sortMapByCount.get(totalSalePrice) == null)
                    {
                        sortMapByCount.put(totalSalePrice, map);
                        break;
                    }
                }
            }
        }
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        int index = 1;
        for (Map.Entry<Double, Map<String, Object>> entry : sortMapByCount.entrySet())
        {
            Map<String, Object> curValue = entry.getValue();
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("name", curValue.get("name"));
            row.put("type", curValue.get("type"));
            row.put("productId", curValue.get("productId"));
            row.put("index", index++);
            row.put("stock", curValue.get("stock"));
            row.put("totalCount", curValue.get("totalCount"));
            row.put("totalSalePrice", curValue.get("totalSalePrice"));
            row.put("brandName", curValue.get("brandName"));
            row.put("code", curValue.get("code"));
            rows.add(row);
        }
        List<Map<String, Object>> newRows = new ArrayList<Map<String, Object>>();
        for (int i = rows.size() - 1; i >= 0; i--)
        {
            newRows.add(rows.get(i));
        }
        return newRows;
    }
    
    @Override
    public Map<String, Object> clientBuyAnalyze(String selectDate)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        DateTime curr_turns_today = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));//2015-06-01 00:00:00
        DateTime curr_turns_tomorrow = curr_turns_today.plusDays(1);//2015-06-02 00:00:00
        DateTime nextMonth = curr_turns_today.plusMonths(1);////2015-07-01 00:00:00
        float sumTotal = 0.0f;
        float sumIos = 0.0f;
        float sumAndroid = 0.0f;
        float sumWeb = 0.0f;
        while (curr_turns_today.isBefore(DateTime.now().getMillis()) && !curr_turns_tomorrow.isAfter(nextMonth.getMillis()))
        {
            Map<String, Object> row = new HashMap<String, Object>();
            String date = curr_turns_today.toString("yyyy-MM-dd");
            row.put("dateStr", date);
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("payTimeBegin", curr_turns_today.toString("yyyy-MM-dd HH:mm:ss"));
            searchPara.put("payTimeEnd", curr_turns_tomorrow.toString("yyyy-MM-dd HH:mm:ss"));
            List<ClientBuyView> infoList = orderDao.clientBuyAnalyze(searchPara);
            float total = 0.0f;
            float ios = 0.0f;
            float android = 0.0f;
            float web = 0.0f;
            for (ClientBuyView it : infoList)
            {
                if ((it.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS.ordinal() || it.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS_VEST1.ordinal() || it.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS_VEST2.ordinal())
                    && it.getOrderNumber().endsWith("1"))
                {
                    ios += it.getRealPrice();
                    sumIos += it.getRealPrice();
                }
                else if ((it.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS.ordinal() && it.getOrderNumber().endsWith("2"))
                    || (it.getAppChannel() == CommonEnum.OrderAppChannelEnum.ANDROID_MOBILE.ordinal()))
                {
                    web += it.getRealPrice();
                    sumWeb += it.getRealPrice();
                }
                else
                {
                    android += it.getRealPrice();
                    sumAndroid += it.getRealPrice();
                }
                total += it.getRealPrice();
                sumTotal += it.getRealPrice();
            }
            row.put("total", MathUtil.round(total + "", 2));
            row.put("ios", MathUtil.round(ios + "", 2));
            row.put("iosPercent", total > 0.0f ? new BigDecimal(ios / total * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
            row.put("android", MathUtil.round(android + "", 2));
            row.put("androidPercent", total > 0.0f ? new BigDecimal(android / total * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
            row.put("web", MathUtil.round(web + "", 2));
            row.put("webPercent", total > 0.0f ? new BigDecimal(web / total * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
            rows.add(row);
            curr_turns_today = curr_turns_today.plusDays(1);
            curr_turns_tomorrow = curr_turns_tomorrow.plusDays(1);
        }
        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("total", MathUtil.round(sumTotal + "", 2));
        lastRow.put("ios", MathUtil.round(sumIos + "", 2));
        lastRow.put("iosPercent", sumTotal > 0.0f ? new BigDecimal(sumIos / sumTotal * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
        lastRow.put("android", MathUtil.round(sumAndroid + "", 2));
        lastRow.put("androidPercent", sumTotal > 0.0f ? new BigDecimal(sumAndroid / sumTotal * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
        lastRow.put("web", MathUtil.round(sumWeb + "", 2));
        lastRow.put("webPercent", sumTotal > 0.0f ? new BigDecimal(sumWeb / sumTotal * 100).setScale(2, BigDecimal.ROUND_HALF_UP) + "%" : "0%");
        result.put("lastRow", lastRow);
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public List<Map<String, Object>> clientBuyDetailAnalyze(String selectDate)
        throws Exception
    {
        DateTime payTimeBegin = DateTime.parse(selectDate + " 00:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime payTimeEnd = DateTime.parse(selectDate + " 23:59:59", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("payTimeBegin", payTimeBegin.toString("yyyy-MM-dd HH:mm:ss"));
        para.put("payTimeEnd", payTimeEnd.toString("yyyy-MM-dd HH:mm:ss"));
        List<ClientBuyView> infoList = orderDao.clientBuyAnalyze(para);
        Map<String, List<ClientBuyView>> infoMap = new HashMap<String, List<ClientBuyView>>();
        Set<Integer> persons = new HashSet<Integer>();
        double totalPrice = 0.00d;
        for (ClientBuyView cbv : infoList)
        {
            totalPrice += cbv.getRealPrice();
            persons.add(cbv.getAccountId());
            String key = getTypeVersion(cbv);
            List<ClientBuyView> detailList = infoMap.get(key);
            if (detailList == null)
            {
                detailList = new ArrayList<ClientBuyView>();
                detailList.add(cbv);
                infoMap.put(key, detailList);
            }
            else
            {
                detailList.add(cbv);
            }
        }
        List<ClientDetailView> cdvList = new ArrayList<ClientDetailView>();
        for (Map.Entry<String, List<ClientBuyView>> entry : infoMap.entrySet())
        {
            double price = 0.0d;
            ClientDetailView cdv = new ClientDetailView();
            String typeVersion = entry.getKey();
            cdv.setType(Integer.valueOf(typeVersion.split("-")[0]));
            cdv.setVersion(typeVersion.split("-").length > 1 ? typeVersion.split("-")[1] : "");
            Set<Integer> accountIdSet = new HashSet<Integer>();
            List<ClientBuyView> cbvList = entry.getValue();
            for (ClientBuyView cbv : cbvList)
            {
                accountIdSet.add(cbv.getAccountId());
                price += cbv.getRealPrice();
            }
            cdv.setTotalPrice(new DecimalFormat("0.00").format(price));
            cdv.setTotalPricePercent(new DecimalFormat("0.00").format(price / totalPrice * 100) + "%");
            cdv.setTotalPersonCount(accountIdSet.size() + "");
            cdv.setTotalPersonCountPercent(new DecimalFormat("0.00").format(accountIdSet.size() * 1.0 / persons.size() * 100) + "%");
            cdv.setDivPersonPrice(new DecimalFormat("0.00").format(price / accountIdSet.size()));
            cdv.setTotalOrderCount(cbvList.size() + "");
            cdv.setDivOrderPrice(new DecimalFormat("0.00").format(price / cbvList.size()));
            cdvList.add(cdv);
        }
        
        Collections.sort(cdvList, new Comparator<ClientDetailView>()
        {
            
            @Override
            public int compare(ClientDetailView o1, ClientDetailView o2)
            {
                if (o1.getType() == o2.getType())
                {
                    return -o1.getVersion().compareTo(o2.getVersion());
                }
                else
                {
                    return o1.getType() - o2.getType();
                }
            }
            
        });
        List<Map<String, Object>> resultRows = new ArrayList<Map<String, Object>>();
        for (ClientDetailView cdv : cdvList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("typeVersion", cdv.getTypeVersion());
            map.put("totalPrice", cdv.getTotalPrice());
            map.put("totalPricePercent", cdv.getTotalPricePercent());
            map.put("totalPersonCount", cdv.getTotalPersonCount());
            map.put("totalPersonCountPercent", cdv.getTotalPersonCountPercent());
            map.put("divPersonPrice", cdv.getDivPersonPrice());
            map.put("totalOrderCount", cdv.getTotalOrderCount());
            map.put("divOrderPrice", cdv.getDivOrderPrice());
            resultRows.add(map);
        }
        return resultRows;
    }
    
    private String getTypeVersion(ClientBuyView cbv)
    {
        if (cbv.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS.ordinal() && cbv.getOrderNumber().endsWith("1"))
        {
            return "1-" + cbv.getAppVersion();
        }
        else if ((cbv.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS_VEST1.ordinal() || cbv.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS_VEST2.ordinal())
            && cbv.getOrderNumber().endsWith("1"))
        {
            return "2-" + cbv.getAppVersion();
        }
        else if ((cbv.getAppChannel() == CommonEnum.OrderAppChannelEnum.IOS.ordinal() && cbv.getOrderNumber().endsWith("2"))
            || (cbv.getAppChannel() == CommonEnum.OrderAppChannelEnum.ANDROID_MOBILE.ordinal()))
        {
            return "4-";
        }
        else
        {
            return "3-" + cbv.getAppVersion();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<UserBehaviorView> userBehaviorAnalyze(Map<String, String> para)
        throws Exception
    {
        String prevStartTimeStr = para.get("prevStartTime");
        String prevEndTimeStr = para.get("prevEndTime");
        String nextStartTimeStr = para.get("nextStartTime");
        String nextEndTimeStr = para.get("nextEndTime");
        String prevStartTime =
            DateTime.parse(prevStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        String prevEndTime =
            DateTime.parse(prevEndTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        String nextStartTime =
            DateTime.parse(nextStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        String nextEndTime =
            DateTime.parse(nextEndTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> searchPara = new HashMap<String, Object>();
        searchPara.put("oStartTime", prevStartTime);
        searchPara.put("oEndTime", prevEndTime);
        searchPara.put("aStartTime", prevStartTime);
        searchPara.put("aEndTime", prevEndTime);
        List<Map<String, Object>> prevsInfo = orderDao.userBehaviorAnalyze(searchPara);
        int prevZeroBuyCount = orderDao.countZeroBuyUser(searchPara);
        double totalPrice = 0.0d;
        int totalCount = prevZeroBuyCount;
        Map<Integer, Map<String, Object>> prevsMap = new HashMap<Integer, Map<String, Object>>();
        for (Map<String, Object> map : prevsInfo)
        {
            int times = Integer.valueOf(map.get("times") + "").intValue();
            int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
            double price = Double.valueOf(map.get("totalPrice") + "").intValue();
            Map<String, Object> itMap = prevsMap.get(times);
            if (itMap == null)
            {
                itMap = new HashMap<String, Object>();
                Set<Integer> accountIdSet = new HashSet<Integer>();
                accountIdSet.add(accountId);
                itMap.put("accountIdSet", accountIdSet);
                itMap.put("sumPrice1", price);
                prevsMap.put(times, itMap);
            }
            else
            {
                Set<Integer> accountIdSet = (Set<Integer>)itMap.get("accountIdSet");
                accountIdSet.add(accountId);
                double sumPrice = Double.valueOf(itMap.get("sumPrice1") + "");
                itMap.put("sumPrice1", sumPrice + price);
            }
            totalPrice += price;
            totalCount++;
        }
        
        searchPara.put("oStartTime", nextStartTime);
        searchPara.put("oEndTime", nextEndTime);
        searchPara.put("aStartTime", prevStartTime);
        searchPara.put("aEndTime", prevEndTime);
        List<Map<String, Object>> nextInfo = orderDao.userBehaviorAnalyze(searchPara);
        for (Map<String, Object> it : prevsMap.values())
        {
            Set<Integer> accountIdSet = (Set<Integer>)it.get("accountIdSet");
            Set<Integer> _accountIdSet = new HashSet<Integer>();
            double sumPrice = 0.0d;
            for (Map<String, Object> map : nextInfo)
            {
                int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
                double price = Double.valueOf(map.get("totalPrice") + "").intValue();
                if (accountIdSet.contains(accountId))
                {
                    sumPrice += price;
                    _accountIdSet.add(accountId);
                }
            }
            it.put("count1", accountIdSet.size());
            it.put("count2", _accountIdSet.size());
            it.put("sumPrice2", sumPrice);
        }
        
        List<UserBehaviorView> ubvList = new ArrayList<UserBehaviorView>();
        for (Map.Entry<Integer, Map<String, Object>> it : prevsMap.entrySet())
        {
            int times = it.getKey();
            Map<String, Object> valueMap = it.getValue();
            int userCount = Integer.valueOf(valueMap.get("count1") + "").intValue();
            double totalAmount = Double.valueOf(valueMap.get("sumPrice1") + "").doubleValue();
            int nextUserCount = Integer.valueOf(valueMap.get("count2") + "").intValue();
            double nextTotalAmount = Double.valueOf(valueMap.get("sumPrice2") + "").doubleValue();
            
            UserBehaviorView ubv = new UserBehaviorView();
            ubv.setTimes(times);
            ubv.setUserCount(userCount + "");
            ubv.setUserCountPercent(totalCount == 0 ? "0%" : new DecimalFormat("0.00").format(1.0 * userCount / totalCount * 100) + "%");
            ubv.setTotalAmount(new DecimalFormat("0.00").format(totalAmount));
            ubv.setTotalAmountPercent(totalPrice == 0.0d ? "0%" : new DecimalFormat("0.00").format(totalAmount / totalPrice * 100) + "%");
            ubv.setAveragePrice(userCount == 0 ? "0" : new DecimalFormat("0.00").format(totalAmount / userCount));
            ubv.setNextUserCount(nextUserCount + "");
            ubv.setNextTotalAmount(new DecimalFormat("0.00").format(nextTotalAmount));
            ubv.setNextStartTime(nextStartTimeStr);
            ubv.setNextEndTime(nextEndTimeStr);
            ubv.setPrevStartTime(prevStartTimeStr);
            ubv.setPrevEndTime(prevEndTimeStr);
            ubvList.add(ubv);
        }
        
        UserBehaviorView ubv = new UserBehaviorView();
        ubv.setTimes(0);
        ubv.setUserCount(prevZeroBuyCount + "");
        ubv.setUserCountPercent(totalCount == 0 ? "0%" : new DecimalFormat("0.00").format(1.0 * prevZeroBuyCount / totalCount * 100) + "%");
        ubv.setTotalAmount(new DecimalFormat("0.00").format(0));
        ubv.setTotalAmountPercent("0.00%");
        ubv.setAveragePrice("0");
        ubv.setNextStartTime(nextStartTimeStr);
        ubv.setNextEndTime(nextEndTimeStr);
        ubv.setPrevStartTime(prevStartTimeStr);
        ubv.setPrevEndTime(prevEndTimeStr);
        
        para.put("prevStartTime", prevStartTime);
        para.put("prevEndTime", prevEndTime);
        para.put("nextStartTime", nextStartTime);
        para.put("nextEndTime", nextEndTime);
        Map<String, Object> nextMap = orderDao.countNextBuyUser(para);
        ubv.setNextUserCount(nextMap.get("nextUserCount") == null ? "0" : nextMap.get("nextUserCount") + "");
        ubv.setNextTotalAmount(new DecimalFormat("0.00").format(nextMap.get("nextTotalAmount") == null ? 0 : Double.valueOf(nextMap.get("nextTotalAmount") + "")));
        ubvList.add(ubv);
        Collections.sort(ubvList, new Comparator<UserBehaviorView>()
        {
            @Override
            public int compare(UserBehaviorView o1, UserBehaviorView o2)
            {
                return o1.getTimes() - o2.getTimes();
            }
        });
        
        return ubvList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<UserBehaviorView> userBehaviorDetailAnalyze(Map<String, String> para)
        throws Exception
    {
        String prevStartTimeStr = para.get("prevStartTime");
        String prevEndTimeStr = para.get("prevEndTime");
        String nextStartTimeStr = para.get("nextStartTime");
        String nextEndTimeStr = para.get("nextEndTime");
        int timesPara = Integer.valueOf(para.get("times") + "").intValue();
        String prevStartTime =
            DateTime.parse(prevStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        String prevEndTime =
            DateTime.parse(prevEndTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        String nextStartTime =
            DateTime.parse(nextStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        String nextEndTime =
            DateTime.parse(nextEndTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> searchPara = new HashMap<String, Object>();
        searchPara.put("startTime", prevStartTime);
        searchPara.put("endTime", prevEndTime);
        Set<Integer> accountIdSet = new HashSet<Integer>();
        if (timesPara == 0)
        {
            //0次购买用户id
            List<Integer> accountIdList = orderDao.findZeroBuyAccount(searchPara);
            accountIdSet = new HashSet<Integer>(accountIdList);
        }
        else
        {
            //统计上个月购买timesPara次的用户Id
            searchPara.put("oStartTime", prevStartTime);
            searchPara.put("oEndTime", prevEndTime);
            searchPara.put("aStartTime", prevStartTime);
            searchPara.put("aEndTime", prevEndTime);
            List<Map<String, Object>> prevsInfo = orderDao.userBehaviorAnalyze(searchPara);
            for (Map<String, Object> map : prevsInfo)
            {
                int times = Integer.valueOf(map.get("times") + "").intValue();
                int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
                if (timesPara == times)
                {
                    accountIdSet.add(accountId);
                }
            }
        }
        
        searchPara.put("oStartTime", nextStartTime);
        searchPara.put("oEndTime", nextEndTime);
        searchPara.put("aStartTime", prevStartTime);
        searchPara.put("aEndTime", prevEndTime);
        searchPara.put("idList", new ArrayList<Integer>(accountIdSet));
        List<Map<String, Object>> nextInfo = orderDao.userBehaviorAnalyze(searchPara);
        Map<Integer, Map<String, Object>> nextInfoMap = new HashMap<Integer, Map<String, Object>>();
        for (Map<String, Object> map : nextInfo)
        {
            int times = Integer.valueOf(map.get("times") + "").intValue();
            int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
            double price = Double.valueOf(map.get("totalPrice") + "").intValue();
            Map<String, Object> itMap = nextInfoMap.get(times);
            if (itMap == null)
            {
                itMap = new HashMap<String, Object>();
                Set<Integer> accountSet = new HashSet<Integer>();
                accountSet.add(accountId);
                itMap.put("accountSet", accountSet);
                itMap.put("sumPrice", price);
                nextInfoMap.put(times, itMap);
            }
            else
            {
                Set<Integer> accountSet = (Set<Integer>)itMap.get("accountSet");
                accountSet.add(accountId);
                double sumPrice = Double.valueOf(itMap.get("sumPrice") + "");
                itMap.put("sumPrice", sumPrice + price);
            }
        }
        
        List<UserBehaviorView> ubvList = new ArrayList<UserBehaviorView>();
        for (Map.Entry<Integer, Map<String, Object>> it : nextInfoMap.entrySet())
        {
            int times = it.getKey();
            Map<String, Object> valueMap = it.getValue();
            double totalAmount = Double.valueOf(valueMap.get("sumPrice") + "").doubleValue();
            Set<Integer> accountSet = (Set<Integer>)valueMap.get("accountSet");
            
            UserBehaviorView ubv = new UserBehaviorView();
            ubv.setTimes(times);
            ubv.setUserCount(accountSet.size() + "");
            ubv.setTotalAmount(new DecimalFormat("0.00").format(totalAmount));
            if (accountSet.size() == 0)
            {
                ubv.setAveragePrice(times == 0 ? "0.00" : new DecimalFormat("0.00").format(totalAmount / times));
            }
            else
            {
                ubv.setAveragePrice(times == 0 ? new DecimalFormat("0.00").format(totalAmount / accountSet.size()) : new DecimalFormat("0.00").format(totalAmount / times
                    / accountSet.size()));
            }
            
            ubvList.add(ubv);
        }
        Collections.sort(ubvList, new Comparator<UserBehaviorView>()
        {
            @Override
            public int compare(UserBehaviorView o1, UserBehaviorView o2)
            {
                return o1.getTimes() - o2.getTimes();
            }
        });
        return ubvList;
    }
    
    @Override
    public Map<String, Object> saleLineData(DateTime date)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("payTimeBegin", date.toString("yyyy-MM-dd 00:00:00"));
        para.put("payTimeEnd", date.plusDays(1).toString("yyyy-MM-dd 00:00:00"));
        List<Map<String, Object>> data1 = orderDao.findSaleDataByDate(para);
        Map<String, Object> re1 = adjustData(data1, DateTime.now().getHourOfDay() + 1);
        List<Double> nowSaleData = (List<Double>)re1.get("data");
        double nowTotal = Double.parseDouble(re1.get("total") == null ? "0" : re1.get("total") + "");
        para.put("payTimeBegin", date.plusDays(-1).toString("yyyy-MM-dd 00:00:00"));
        para.put("payTimeEnd", date.toString("yyyy-MM-dd 00:00:00"));
        List<Map<String, Object>> data2 = orderDao.findSaleDataByDate(para);
        Map<String, Object> re2 = adjustData(data2, 24);
        List<Double> yesterdaySaleData = (List<Double>)re2.get("data");
        double yesterdayTotal = Double.parseDouble(re2.get("total") == null ? "0" : re2.get("total") + "");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("nowSaleData", nowSaleData);
        result.put("nowTotal", nowTotal);
        result.put("yesterdaySaleData", yesterdaySaleData);
        result.put("yesterdayTotal", yesterdayTotal);
        return result;
    }
    
    Map<String, Object> adjustData(List<Map<String, Object>> data, int lastHour)
    {
        TreeMap<Integer, Double> tm = new TreeMap<Integer, Double>();
        double total = 0;
        for (Map<String, Object> it : data)
        {
            DateTime dt = DateTimeUtil.string2DateTime(it.get("payTime") + "", DateTimeUtil.TIMESTAMP_FORMAT);
            Double realPrice = Double.parseDouble(it.get("realPrice") == null ? "0" : it.get("realPrice") + "");
            int hour_1 = dt.getHourOfDay() + 1;
            Double d = tm.get(hour_1);
            if (d == null)
            {
                d = Double.valueOf(0);
            }
            d += realPrice;
            tm.put(hour_1, Double.valueOf(MathUtil.round(d, 2)));
            // 总计
            total += realPrice;
        }
        List<Double> da = new ArrayList<Double>();
        for (int i = 1; i <= lastHour; i++)
        {
            if (tm.get(i) == null)
            {
                tm.put(i, Double.valueOf(0));
            }
        }
        for (Map.Entry<Integer, Double> e : tm.entrySet())
        {
            da.add(e.getValue());
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", da);
        result.put("total", Double.valueOf(MathUtil.round(total, 2)));
        return result;
    }
    
    @Override
    public Map<String, Object> appVersionBuyAnalyze(String selectDate)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        DateTime curr_turns_today = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));//2015-06-01 00:00:00
        DateTime curr_turns_tomorrow = curr_turns_today.plusDays(1);//2015-06-02 00:00:00
        DateTime nextMonth = curr_turns_today.plusMonths(1);////2015-07-01 00:00:00
        float tIOS18 = 0.0f;
        float tIOS17 = 0.0f;
        float tIOS16 = 0.0f;
        float tIOS15 = 0.0f;
        float tIOS14 = 0.0f;
        float tIOS马甲17 = 0.0f;
        float tIOS马甲15 = 0.0f;
        float tAndroid18 = 0.0f;
        float tAndroid17 = 0.0f;
        float tAndroid16 = 0.0f;
        float tAndroid15 = 0.0f;
        float tAndroid14 = 0.0f;
        float tweb = 0.0f;
        while (curr_turns_today.isBefore(DateTime.now().getMillis()) && !curr_turns_tomorrow.isAfter(nextMonth.getMillis()))
        {
            Map<String, Object> row = new HashMap<String, Object>();
            String date = curr_turns_today.toString("yyyy-MM-dd");
            row.put("dateStr", date);
            Map<String, Object> searchPara = new HashMap<String, Object>();
            searchPara.put("payTimeBegin", curr_turns_today.toString("yyyy-MM-dd HH:mm:ss"));
            searchPara.put("payTimeEnd", curr_turns_tomorrow.toString("yyyy-MM-dd HH:mm:ss"));
            List<ClientBuyView> infoList = orderDao.clientBuyAnalyze(searchPara);
            Map<String, List<ClientBuyView>> infoMap = new HashMap<String, List<ClientBuyView>>();
            for (ClientBuyView cbv : infoList)
            {
                String key = getTypeVersion(cbv);
                List<ClientBuyView> detailList = infoMap.get(key);
                if (detailList == null)
                {
                    detailList = new ArrayList<ClientBuyView>();
                    detailList.add(cbv);
                    infoMap.put(key, detailList);
                }
                else
                {
                    detailList.add(cbv);
                }
            }
            
            List<ClientDetailView> cdvList = new ArrayList<ClientDetailView>();
            for (Map.Entry<String, List<ClientBuyView>> entry : infoMap.entrySet())
            {
                double price = 0.0d;
                ClientDetailView cdv = new ClientDetailView();
                String typeVersion = entry.getKey();
                cdv.setType(Integer.valueOf(typeVersion.split("-")[0]));
                cdv.setVersion(typeVersion.split("-").length > 1 ? typeVersion.split("-")[1] : "");
                List<ClientBuyView> cbvList = entry.getValue();
                for (ClientBuyView cbv : cbvList)
                {
                    price += cbv.getRealPrice();
                }
                cdv.setTotalPrice(new DecimalFormat("0.00").format(price));
                cdvList.add(cdv);
            }
            Collections.sort(cdvList, new Comparator<ClientDetailView>()
            {
                
                @Override
                public int compare(ClientDetailView o1, ClientDetailView o2)
                {
                    if (o1.getType() == o2.getType())
                    {
                        return -o1.getVersion().compareTo(o2.getVersion());
                    }
                    else
                    {
                        return o1.getType() - o2.getType();
                    }
                }
                
            });
            Map<String, Object> rowMap = new HashMap<String, Object>();
            for (ClientDetailView cdv : cdvList)
            {
                rowMap.put(cdv.getTypeVersion(), cdv.getTotalPrice());
                if ("IOS1.8".equals(cdv.getTypeVersion()))
                {
                    tIOS18 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("IOS1.7".equals(cdv.getTypeVersion()))
                {
                    tIOS17 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("IOS1.6".equals(cdv.getTypeVersion()))
                {
                    tIOS16 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("IOS1.5".equals(cdv.getTypeVersion()))
                {
                    tIOS15 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("IOS1.4".equals(cdv.getTypeVersion()))
                {
                    tIOS14 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("IOS马甲1.7".equals(cdv.getTypeVersion()))
                {
                    tIOS马甲17 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("IOS马甲1.5".equals(cdv.getTypeVersion()))
                {
                    tIOS马甲15 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("Android1.8".equals(cdv.getTypeVersion()))
                {
                    tAndroid18 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("Android1.7".equals(cdv.getTypeVersion()))
                {
                    tAndroid17 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("Android1.6".equals(cdv.getTypeVersion()))
                {
                    tAndroid16 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("Android1.5".equals(cdv.getTypeVersion()))
                {
                    tAndroid15 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("Android1.4".equals(cdv.getTypeVersion()))
                {
                    tAndroid14 += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
                if ("web".equals(cdv.getTypeVersion()))
                {
                    tweb += Float.valueOf(cdv.getTotalPrice()).floatValue();
                }
            }
            row.put("IOS18", rowMap.get("IOS1.8") == null ? "0.00" : rowMap.get("IOS1.8"));
            row.put("IOS17", rowMap.get("IOS1.7") == null ? "0.00" : rowMap.get("IOS1.7"));
            row.put("IOS16", rowMap.get("IOS1.6") == null ? "0.00" : rowMap.get("IOS1.6"));
            row.put("IOS15", rowMap.get("IOS1.5") == null ? "0.00" : rowMap.get("IOS1.5"));
            row.put("IOS14", rowMap.get("IOS1.4") == null ? "0.00" : rowMap.get("IOS1.4"));
            row.put("IOS马甲17", rowMap.get("IOS马甲1.7") == null ? "0.00" : rowMap.get("IOS马甲1.7"));
            row.put("IOS马甲15", rowMap.get("IOS马甲1.5") == null ? "0.00" : rowMap.get("IOS马甲1.5"));
            row.put("Android18", rowMap.get("Android1.8") == null ? "0.00" : rowMap.get("Android1.8"));
            row.put("Android17", rowMap.get("Android1.7") == null ? "0.00" : rowMap.get("Android1.7"));
            row.put("Android16", rowMap.get("Android1.6") == null ? "0.00" : rowMap.get("Android1.6"));
            row.put("Android15", rowMap.get("Android1.5") == null ? "0.00" : rowMap.get("Android1.5"));
            row.put("Android14", rowMap.get("Android1.4") == null ? "0.00" : rowMap.get("Android1.4"));
            row.put("web", rowMap.get("web") == null ? "0.00" : rowMap.get("web"));
            rows.add(row);
            curr_turns_today = curr_turns_today.plusDays(1);
            curr_turns_tomorrow = curr_turns_tomorrow.plusDays(1);
        }
        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("tIOS18", MathUtil.round(tIOS18 + "", 2));
        lastRow.put("tIOS17", MathUtil.round(tIOS17 + "", 2));
        lastRow.put("tIOS16", MathUtil.round(tIOS16 + "", 2));
        lastRow.put("tIOS15", MathUtil.round(tIOS15 + "", 2));
        lastRow.put("tIOS14", MathUtil.round(tIOS14 + "", 2));
        lastRow.put("tIOS马甲17", MathUtil.round(tIOS马甲17 + "", 2));
        lastRow.put("tIOS马甲15", MathUtil.round(tIOS马甲15 + "", 2));
        lastRow.put("tAndroid18", MathUtil.round(tAndroid18 + "", 2));
        lastRow.put("tAndroid17", MathUtil.round(tAndroid17 + "", 2));
        lastRow.put("tAndroid16", MathUtil.round(tAndroid16 + "", 2));
        lastRow.put("tAndroid15", MathUtil.round(tAndroid15 + "", 2));
        lastRow.put("tAndroid14", MathUtil.round(tAndroid14 + "", 2));
        lastRow.put("tweb", MathUtil.round(tweb + "", 2));
        result.put("lastRow", lastRow);
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public List<Map<String, Object>> eachDayOrderSendTimeAnalyze(Map<String, Object> para)
        throws Exception
    {
        String selectDate = para.get("selectDate").toString();
        List<Map<String, Object>> orderInfoList = orderDao.findOrderPayRecordForSendTimeAnalyze(para);
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        Timestamp payTime = null;
        for (Map<String, Object> it : orderInfoList)
        {
            payTime = (Timestamp)it.get("payTime");
            int day = new DateTime(payTime.getTime()).getDayOfMonth();
            String key = day + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(it);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            int totalAmount = 0;//付款订单总量
            int dayOfHalfAmount = 0;//0.5天发货订单
            int dayOf1Amount = 0;//1天发货订单总量
            int dayOf15Amount = 0;//1.5天发货订单总量
            int dayOf2Amount = 0;//2天发货订单总量
            int dayOf3Amount = 0;//3天发货订单总量
            int dayOf4Amount = 0;//4天发货订单总量
            int dayOf5Amount = 0;//5天发货订单总量
            int dayOf6Amount = 0;//6天发货订单总量
            int dayOf7Amount = 0;//7天发货订单总量
            int dayOf8Amount = 0;//8天发货订单总量
            int notSendAmount = 0;//未发货订单数量
            String day = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            totalAmount = dayList.size();
            for (Map<String, Object> it : dayList)
            {
                String sendTime = it.get("sendTime") == null ? "" : it.get("sendTime") + "";
                if (StringUtils.isEmpty(sendTime) || "null".equals(sendTime))
                {
                    notSendAmount++;
                }
                else
                {
                    DateTime payDateTime = new DateTime((Timestamp)it.get("payTime"));
                    DateTime sendDateTime = new DateTime((Timestamp)it.get("sendTime"));
                    int hour = Hours.hoursBetween(payDateTime, sendDateTime).getHours();
                    if (hour < 12)
                    {
                        dayOfHalfAmount++;
                    }
                    else if (hour < 24)
                    {
                        dayOf1Amount++;
                    }
                    else if (hour < 36)
                    {
                        dayOf15Amount++;
                    }
                    else if (hour < 48)
                    {
                        dayOf2Amount++;
                    }
                    else if (hour < 72)
                    {
                        dayOf3Amount++;
                    }
                    else if (hour < 96)
                    {
                        dayOf4Amount++;
                    }
                    else if (hour < 120)
                    {
                        dayOf5Amount++;
                    }
                    else if (hour < 144)
                    {
                        dayOf6Amount++;
                    }
                    else if (hour < 168)
                    {
                        dayOf7Amount++;
                    }
                    else
                    {
                        dayOf8Amount++;
                    }
                }
            }
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("day", day);
            reMap.put("date", selectDate + "-" + day);
            reMap.put("totalAmount", totalAmount + "");
            reMap.put("dayOfHalfAmount", dayOfHalfAmount + "");
            reMap.put("dayOf1Amount", dayOf1Amount + "");
            reMap.put("dayOf15Amount", dayOf15Amount + "");
            reMap.put("dayOf2Amount", dayOf2Amount + "");
            reMap.put("dayOf3Amount", dayOf3Amount + "");
            reMap.put("dayOf4Amount", dayOf4Amount + "");
            reMap.put("dayOf5Amount", dayOf5Amount + "");
            reMap.put("dayOf6Amount", dayOf6Amount + "");
            reMap.put("dayOf7Amount", dayOf7Amount + "");
            reMap.put("dayOf8Amount", dayOf8Amount + "");
            reMap.put("notSendAmount", notSendAmount + "");
            resultList.add(reMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int day1 = Integer.valueOf(o1.get("day") + "");
                int day2 = Integer.valueOf(o2.get("day") + "");
                return day1 - day2;
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, Object>> eachDayOrderLogisticAnalyze(Map<String, Object> para)
        throws Exception
    {
        String selectDate = para.get("selectDate").toString();
        List<Map<String, Object>> orderInfoList = orderDao.findOrderPayRecordForLogisticAnalyze(para);
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        Timestamp sendTime = null;
        for (Map<String, Object> it : orderInfoList)
        {
            sendTime = (Timestamp)it.get("sendTime");
            int day = new DateTime(sendTime.getTime()).getDayOfMonth();
            String key = day + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(it);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            int totalAmount = 0;//发货订单总量
            int dayOfHalfAmount = 0;//0.5天有物流信息订单
            int dayOf1Amount = 0;//1天有物流信息订单总量
            int dayOf2Amount = 0;//2天有物流信息订单总量
            int dayOf3Amount = 0;//3天有物流信息订单总量
            int dayOf4Amount = 0;//4天有物流信息订单总量
            int dayOf5Amount = 0;//5天有物流信息订单总量
            int noLogisticsAmount = 0;//未发货订单数量
            String day = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            totalAmount = dayList.size();
            for (Map<String, Object> it : dayList)
            {
                DateTime sendDateTime = new DateTime((Timestamp)it.get("sendTime"));
                String channel = it.get("channel").toString();
                String number = it.get("number").toString();
                //TODO 待优化,这个查询要放到循环外面
                DateTime logisticsTime = orderDao.finOrderLogisticsTimeByChannelAndNumber(channel, number);
                if (logisticsTime == null)
                {
                    noLogisticsAmount++;
                }
                else
                {
                    int hour = Hours.hoursBetween(sendDateTime, logisticsTime).getHours();
                    if (hour < 12)
                    {
                        dayOfHalfAmount++;
                    }
                    else if (hour < 24)
                    {
                        dayOf1Amount++;
                    }
                    else if (hour < 48)
                    {
                        dayOf2Amount++;
                    }
                    else if (hour < 72)
                    {
                        dayOf3Amount++;
                    }
                    else if (hour < 96)
                    {
                        dayOf4Amount++;
                    }
                    else
                    {
                        dayOf5Amount++;
                    }
                }
                
            }
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("day", day);
            reMap.put("date", selectDate + "-" + (Integer.parseInt(day) > 10 ? day : "0" + day));
            reMap.put("totalAmount", totalAmount + "");
            reMap.put("dayOfHalfAmount", dayOfHalfAmount + "");
            reMap.put("dayOf1Amount", dayOf1Amount + "");
            reMap.put("dayOf2Amount", dayOf2Amount + "");
            reMap.put("dayOf3Amount", dayOf3Amount + "");
            reMap.put("dayOf4Amount", dayOf4Amount + "");
            reMap.put("dayOf5Amount", dayOf5Amount + "");
            reMap.put("noLogisticsAmount", noLogisticsAmount + "");
            resultList.add(reMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int day1 = Integer.valueOf(o1.get("day") + "");
                int day2 = Integer.valueOf(o2.get("day") + "");
                return day1 - day2;
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, Object>> sellerSendTimeAnalyze(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> orderInfoList = orderDao.findOrderPayRecordForSendTimeAnalyze(para);
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> it : orderInfoList)
        {
            String sellerId = it.get("sellerId") == null ? "0" : it.get("sellerId") + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(sellerId);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayMap.put(sellerId, dayList);
            }
            dayList.add(it);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            int totalAmount = 0;//付款订单总量
            int dayOfHalfAmount = 0;//0.5天发货订单
            int dayOf1Amount = 0;//1天发货订单总量
            int dayOf15Amount = 0;//1.5天发货订单总量
            int dayOf2Amount = 0;//2天发货订单总量
            int dayOf3Amount = 0;//3天发货订单总量
            int dayOf4Amount = 0;//4天发货订单总量
            int dayOf5Amount = 0;//5天发货订单总量
            int dayOf6Amount = 0;//6天发货订单总量
            int dayOf7Amount = 0;//7天发货订单总量
            int dayOf8Amount = 0;//8天发货订单总量
            int notSendAmount = 0;//未发货订单数量
            String sellerId = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            totalAmount = dayList.size();
            for (Map<String, Object> it : dayList)
            {
                String sendTime = it.get("sendTime") == null ? "" : it.get("sendTime") + "";
                if (StringUtils.isEmpty(sendTime))
                {
                    notSendAmount++;
                }
                else
                {
                    DateTime payDateTime = new DateTime((Timestamp)it.get("payTime"));
                    DateTime sendDateTime = new DateTime((Timestamp)it.get("sendTime"));
                    int hour = Hours.hoursBetween(payDateTime, sendDateTime).getHours();
                    if (hour < 12)
                    {
                        dayOfHalfAmount++;
                    }
                    else if (hour < 24)
                    {
                        dayOf1Amount++;
                    }
                    else if (hour < 36)
                    {
                        dayOf15Amount++;
                    }
                    else if (hour < 48)
                    {
                        dayOf2Amount++;
                    }
                    else if (hour < 72)
                    {
                        dayOf3Amount++;
                    }
                    else if (hour < 96)
                    {
                        dayOf4Amount++;
                    }
                    else if (hour < 120)
                    {
                        dayOf5Amount++;
                    }
                    else if (hour < 144)
                    {
                        dayOf6Amount++;
                    }
                    else if (hour < 168)
                    {
                        dayOf7Amount++;
                    }
                    else
                    {
                        dayOf8Amount++;
                    }
                }
            }
            Map<String, Object> reMap = new HashMap<>();
            Map<String, Object> sellerMap = dayList.get(0);
            reMap.put("sellerId", sellerId);
            reMap.put("sellerName", sellerMap.get("sellerName") + "");
            reMap.put("sendAddress", sellerMap.get("sendAddress") + "");
            reMap.put("warehouse", StringUtils.isEmpty(sellerMap.get("warehouse") + "") ? "无" : sellerMap.get("warehouse") + "");
            if (Integer.valueOf(sellerMap.get("sellerType") + "") == 2 || Integer.valueOf(sellerMap.get("sellerType") + "") == 3)
            {
                if (Integer.valueOf(sellerMap.get("bondedNumberType") + "") == 1)
                {
                    reMap.put("bondedNumberType", "后报关");
                }
                else
                {
                    reMap.put("bondedNumberType", "先报关");
                }
            }
            else
            {
                reMap.put("bondedNumberType", "无");
            }
            reMap.put("totalAmount", totalAmount + "");
            reMap.put("dayOfHalfAmount", dayOfHalfAmount + "");
            reMap.put("dayOf1Amount", dayOf1Amount + "");
            reMap.put("dayOf15Amount", dayOf15Amount + "");
            reMap.put("dayOf2Amount", dayOf2Amount + "");
            reMap.put("dayOf3Amount", dayOf3Amount + "");
            reMap.put("dayOf4Amount", dayOf4Amount + "");
            reMap.put("dayOf5Amount", dayOf5Amount + "");
            reMap.put("dayOf6Amount", dayOf6Amount + "");
            reMap.put("dayOf7Amount", dayOf7Amount + "");
            reMap.put("dayOf8Amount", dayOf8Amount + "");
            reMap.put("notSendAmount", notSendAmount + "");
            resultList.add(reMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int day1 = Integer.valueOf(o1.get("totalAmount") + "");
                int day2 = Integer.valueOf(o2.get("totalAmount") + "");
                return day2 - day1;
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, Object>> sellerOrderLogisticAnalyze(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> orderInfoList = orderDao.findOrderPayRecordForLogisticAnalyze(para);
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> it : orderInfoList)
        {
            String sellerId = it.get("sellerId") + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(sellerId);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayMap.put(sellerId, dayList);
            }
            dayList.add(it);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            int totalAmount = 0;//发货订单总量
            int dayOfHalfAmount = 0;//0.5天有物流信息订单
            int dayOf1Amount = 0;//1天有物流信息订单总量
            int dayOf2Amount = 0;//2天有物流信息订单总量
            int dayOf3Amount = 0;//3天有物流信息订单总量
            int dayOf4Amount = 0;//4天有物流信息订单总量
            int dayOf5Amount = 0;//5天有物流信息订单总量
            int noLogisticsAmount = 0;//未发货订单数量
            String sellerId = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            totalAmount = dayList.size();
            for (Map<String, Object> it : dayList)
            {
                DateTime sendDateTime = new DateTime((Timestamp)it.get("sendTime"));
                String channel = it.get("channel").toString();
                String number = it.get("number").toString();
                //TODO 待优化,这个查询要放到循环外面
                DateTime logisticsTime = orderDao.finOrderLogisticsTimeByChannelAndNumber(channel, number);
                if (logisticsTime == null)
                {
                    noLogisticsAmount++;
                }
                else
                {
                    int hour = Hours.hoursBetween(sendDateTime, logisticsTime).getHours();
                    if (hour < 12)
                    {
                        dayOfHalfAmount++;
                    }
                    else if (hour < 24)
                    {
                        dayOf1Amount++;
                    }
                    else if (hour < 48)
                    {
                        dayOf2Amount++;
                    }
                    else if (hour < 72)
                    {
                        dayOf3Amount++;
                    }
                    else if (hour < 96)
                    {
                        dayOf4Amount++;
                    }
                    else
                    {
                        dayOf5Amount++;
                    }
                }
                
            }
            Map<String, Object> reMap = new HashMap<>();
            Map<String, Object> sellerMap = dayList.get(0);
            reMap.put("sellerId", sellerId);
            reMap.put("sellerName", sellerMap.get("sellerName") + "");
            reMap.put("sendAddress", sellerMap.get("sendAddress") + "");
            reMap.put("warehouse", StringUtils.isEmpty(sellerMap.get("warehouse") + "") ? "无" : sellerMap.get("warehouse") + "");
            if (Integer.valueOf(sellerMap.get("sellerType") + "") == 2 || Integer.valueOf(sellerMap.get("sellerType") + "") == 3)
            {
                if (Integer.valueOf(sellerMap.get("bondedNumberType") + "") == 1)
                {
                    reMap.put("bondedNumberType", "后报关");
                }
                else
                {
                    reMap.put("bondedNumberType", "先报关");
                }
            }
            else
            {
                reMap.put("bondedNumberType", "无");
            }
            reMap.put("totalAmount", totalAmount + "");
            reMap.put("dayOfHalfAmount", dayOfHalfAmount + "");
            reMap.put("dayOf1Amount", dayOf1Amount + "");
            reMap.put("dayOf2Amount", dayOf2Amount + "");
            reMap.put("dayOf3Amount", dayOf3Amount + "");
            reMap.put("dayOf4Amount", dayOf4Amount + "");
            reMap.put("dayOf5Amount", dayOf5Amount + "");
            reMap.put("noLogisticsAmount", noLogisticsAmount + "");
            resultList.add(reMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int day1 = Integer.valueOf(o1.get("totalAmount") + "");
                int day2 = Integer.valueOf(o2.get("totalAmount") + "");
                return day2 - day1;
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, String>> provinceTurnOverAnalyze(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> orderInfoList = analyzeDao.findProvinceAndCityTurnOver(para);
        //key为provinceName
        Map<String, List<Map<String, Object>>> groupByProvinceMap = new HashMap<String, List<Map<String, Object>>>();
        Set<String> accountIdSet = new HashSet<String>();
        double totalPrice = 0.0;
        for (Map<String, Object> it : orderInfoList)
        {
            String provinceName = it.get("provinceName") + "";
            String accountId = it.get("accountId") == null ? "0" : it.get("accountId") + "";
            double realPrice = Double.parseDouble(it.get("realPrice") == null ? "0.0" : it.get("realPrice") + "");
            List<Map<String, Object>> provinceList = groupByProvinceMap.get(provinceName);
            if (provinceList == null)
            {
                provinceList = new ArrayList<Map<String, Object>>();
                groupByProvinceMap.put(provinceName, provinceList);
            }
            provinceList.add(it);
            accountIdSet.add(accountId);
            totalPrice += realPrice;
        }
        
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByProvinceMap.entrySet())
        {
            double totalPrice_0 = 0.0;
            String provinceName = entry.getKey();
            Set<String> actIdSet = new HashSet<String>();
            List<Map<String, Object>> provinceList = entry.getValue();
            for (Map<String, Object> it : provinceList)
            {
                String accountId = it.get("accountId") == null ? "0" : it.get("accountId") + "";
                actIdSet.add(accountId);
                double realPrice = Double.parseDouble(it.get("realPrice") == null ? "0.0" : it.get("realPrice") + "");
                totalPrice_0 += realPrice;
            }
            Map<String, String> reMap = new HashMap<String, String>();
            reMap.put("provinceName", provinceName);
            reMap.put("totalPerson", actIdSet.size() + "");
            reMap.put("totalPersonPercent", accountIdSet.size() == 0 ? "0" : MathUtil.round(actIdSet.size() * 1.0 / accountIdSet.size() * 100, 2) + "%");
            reMap.put("totalPrice", MathUtil.round(totalPrice_0, 2));
            reMap.put("totalPricePercent", totalPrice == 0.0 ? "0.0" : MathUtil.round(totalPrice_0 / totalPrice * 100, 2) + "%");
            resultList.add(reMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, String>>()
        {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2)
            {
                Double d1 = Double.valueOf(o1.get("totalPrice"));
                Double d2 = Double.valueOf(o2.get("totalPrice"));
                return d2.compareTo(d1);
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, String>> cityTurnOverAnalyze(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> orderInfoList = analyzeDao.findProvinceAndCityTurnOver(para);
        //key为cityName
        Map<String, List<Map<String, Object>>> groupByCityMap = new HashMap<String, List<Map<String, Object>>>();
        Set<String> accountIdSet = new HashSet<String>();
        double totalPrice = 0.0;
        for (Map<String, Object> it : orderInfoList)
        {
            String cityName = it.get("cityName") + "";
            String accountId = it.get("accountId") == null ? "0" : it.get("accountId") + "";
            double realPrice = Double.parseDouble(it.get("realPrice") == null ? "0.0" : it.get("realPrice") + "");
            List<Map<String, Object>> cityList = groupByCityMap.get(cityName);
            if (cityList == null)
            {
                cityList = new ArrayList<Map<String, Object>>();
                groupByCityMap.put(cityName, cityList);
            }
            cityList.add(it);
            accountIdSet.add(accountId);
            totalPrice += realPrice;
        }
        
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByCityMap.entrySet())
        {
            double totalPrice_0 = 0.0;
            String cityName = entry.getKey();
            Set<String> actIdSet = new HashSet<String>();
            List<Map<String, Object>> cityList = entry.getValue();
            for (Map<String, Object> it : cityList)
            {
                String accountId = it.get("accountId") == null ? "0" : it.get("accountId") + "";
                actIdSet.add(accountId);
                double realPrice = Double.parseDouble(it.get("realPrice") == null ? "0.0" : it.get("realPrice") + "");
                totalPrice_0 += realPrice;
            }
            Map<String, String> reMap = new HashMap<String, String>();
            reMap.put("cityName", cityName);
            reMap.put("provinceName", cityList.get(0).get("provinceName") + "");
            reMap.put("totalPerson", actIdSet.size() + "");
            reMap.put("totalPersonPercent", accountIdSet.size() == 0 ? "0" : MathUtil.round(actIdSet.size() * 1.0 / accountIdSet.size() * 100, 2) + "%");
            reMap.put("totalPrice", MathUtil.round(totalPrice_0, 2));
            reMap.put("totalPricePercent", totalPrice == 0.0 ? "0.0" : MathUtil.round(totalPrice_0 / totalPrice * 100, 2) + "%");
            resultList.add(reMap);
        }
        Collections.sort(resultList, new Comparator<Map<String, String>>()
        {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2)
            {
                Double d1 = Double.valueOf(o1.get("totalPrice"));
                Double d2 = Double.valueOf(o2.get("totalPrice"));
                return d2.compareTo(d1);
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, String>> productTurnOverAnalyze(Map<String, Object> para)
        throws Exception
    {
        String selectDate = para.get("selectDate").toString();
        List<Map<String, Object>> orderInfoList = analyzeDao.findProductTurnOverAnalyze(para);
        //key为天数
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        Timestamp payTime = null;
        for (Map<String, Object> it : orderInfoList)
        {
            payTime = (Timestamp)it.get("payTime");
            int day = new DateTime(payTime.getTime()).getDayOfMonth();
            String key = day + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(it);
        }
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            int totalCount = 0;
            int saleProductCount = 0;
            int mallProductCount = 0;
            double totalPrice = 0.0;
            double saleProductTotalPrice = 0.0;
            double mallProductTotalPrice = 0.0;
            String day = entry.getKey();
            List<Map<String, Object>> dayList = entry.getValue();
            for (Map<String, Object> it : dayList)
            {
                int type = Integer.parseInt(it.get("type") + "");
                double salesPrice = Double.parseDouble(it.get("salesPrice") == null ? "0.0" : it.get("salesPrice") + "");
                int productCount = Integer.parseInt(it.get("productCount") == null ? "0" : it.get("productCount") + "");
                if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    saleProductCount += productCount;
                    saleProductTotalPrice += (salesPrice * productCount);
                }
                else if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    mallProductCount += productCount;
                    mallProductTotalPrice += (salesPrice * productCount);
                }
                totalCount += productCount;
                totalPrice += (salesPrice * productCount);
            }
            Map<String, String> reMap = new HashMap<>();
            reMap.put("day", day);
            reMap.put("date", selectDate + "-" + day);
            reMap.put("totalProductCount", totalCount + "");
            reMap.put("totalPrice", MathUtil.round(totalPrice, 2));
            reMap.put("saleProductCount", saleProductCount + "");
            reMap.put("saleProductTotalPrice", MathUtil.round(saleProductTotalPrice, 2));
            reMap.put("saleProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(saleProductTotalPrice / totalPrice * 100, 2) + "%");
            reMap.put("mallProductCount", mallProductCount + "");
            reMap.put("mallProductRealPrice", MathUtil.round(mallProductTotalPrice, 2));
            reMap.put("mallProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(mallProductTotalPrice / totalPrice * 100, 2) + "%");
            resultList.add(reMap);
        }
        
        Collections.sort(resultList, new Comparator<Map<String, String>>()
        {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2)
            {
                int day1 = Integer.valueOf(o1.get("day") + "");
                int day2 = Integer.valueOf(o2.get("day") + "");
                return day1 - day2;
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, String>> productCategoryTurnOverAnalyze(Map<String, Object> para)
        throws Exception
    {
        //具有完整分类的商品信息
        List<Map<String, Object>> fullCategoryProductInfo = analyzeDao.findAllFullCategoryProduct(para);
        //只有二级分类的商品信息
        List<Map<String, Object>> secondCategoryProductInfo = analyzeDao.finSecondCategoryProduct(para);
        //没有分类的商品信息
        List<Map<String, Object>> noCategoryProductInfo = analyzeDao.findNoCategoryProduct(para);
        
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        
        //按三级分类名称分组
        Map<String, List<Map<String, Object>>> groupByThirdCNameMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> it : fullCategoryProductInfo)
        {
            String thirdCName = it.get("categorythirdName") + "";
            List<Map<String, Object>> categoryMap = groupByThirdCNameMap.get(thirdCName);
            if (categoryMap == null)
            {
                categoryMap = new ArrayList<Map<String, Object>>();
                groupByThirdCNameMap.put(thirdCName, categoryMap);
            }
            categoryMap.add(it);
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByThirdCNameMap.entrySet())
        {
            int totalCount = 0;
            int saleProductCount = 0;
            int mallProductCount = 0;
            double totalPrice = 0.0;
            double saleProductTotalPrice = 0.0;
            double mallProductTotalPrice = 0.0;
            Set<Integer> productIdSet1 = new HashSet<Integer>();
            Set<Integer> productIdSet2 = new HashSet<Integer>();
            List<Map<String, Object>> dayList = entry.getValue();
            for (Map<String, Object> it : dayList)
            {
                int type = Integer.parseInt(it.get("type") + "");
                int productId = Integer.parseInt(it.get("productId") + "");
                double salesPrice = Double.parseDouble(it.get("salesPrice") == null ? "0.0" : it.get("salesPrice") + "");
                int productCount = Integer.parseInt(it.get("productCount") == null ? "0" : it.get("productCount") + "");
                if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    saleProductCount += productCount;
                    saleProductTotalPrice += (salesPrice * productCount);
                    productIdSet1.add(productId);
                }
                else if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    mallProductCount += productCount;
                    mallProductTotalPrice += (salesPrice * productCount);
                    productIdSet2.add(productId);
                }
                totalCount += productCount;
                totalPrice += (salesPrice * productCount);
            }
            Map<String, String> reMap = new HashMap<>();
            reMap.put("categoryFirstName", dayList.get(0).get("categoryFirstName") + "");
            reMap.put("categorySecondName", dayList.get(0).get("categorySecondName") + "");
            reMap.put("categorythirdName", entry.getKey());
            reMap.put("totalCount", totalCount + "");
            reMap.put("totalCountSKU", (productIdSet1.size() + productIdSet2.size()) + "");
            reMap.put("totalPrice", MathUtil.round(totalPrice, 2));
            reMap.put("saleProductCount", saleProductCount + "");
            reMap.put("saleProductCountSKU", productIdSet1.size() + "");
            reMap.put("saleProductTotalPrice", MathUtil.round(saleProductTotalPrice, 2));
            reMap.put("saleProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(saleProductTotalPrice / totalPrice * 100, 2) + "%");
            reMap.put("mallProductCount", mallProductCount + "");
            reMap.put("mallProductCountSKU", productIdSet2.size() + "");
            reMap.put("mallProductRealPrice", MathUtil.round(mallProductTotalPrice, 2));
            reMap.put("mallProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(mallProductTotalPrice / totalPrice * 100, 2) + "%");
            resultList.add(reMap);
        }
        
        //按二级分类明名称分组(有可能出现二级类目名称和三级类目名称相等，所以分开)
        Map<String, List<Map<String, Object>>> groupBySecondCNameMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> it : secondCategoryProductInfo)
        {
            String secondCName = it.get("categorySecondName") + "";
            List<Map<String, Object>> categoryMap = groupBySecondCNameMap.get(secondCName);
            if (categoryMap == null)
            {
                categoryMap = new ArrayList<Map<String, Object>>();
                groupBySecondCNameMap.put(secondCName, categoryMap);
            }
            it.put("categorythirdName", "空");
            categoryMap.add(it);
        }
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupBySecondCNameMap.entrySet())
        {
            int totalCount = 0;
            int saleProductCount = 0;
            int mallProductCount = 0;
            double totalPrice = 0.0;
            double saleProductTotalPrice = 0.0;
            double mallProductTotalPrice = 0.0;
            Set<Integer> productIdSet1 = new HashSet<Integer>();
            Set<Integer> productIdSet2 = new HashSet<Integer>();
            List<Map<String, Object>> dayList = entry.getValue();
            for (Map<String, Object> it : dayList)
            {
                int type = Integer.parseInt(it.get("type") + "");
                double salesPrice = Double.parseDouble(it.get("salesPrice") == null ? "0.0" : it.get("salesPrice") + "");
                int productId = Integer.parseInt(it.get("productId") + "");
                int productCount = Integer.parseInt(it.get("productCount") == null ? "0" : it.get("productCount") + "");
                if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    saleProductCount += productCount;
                    saleProductTotalPrice += (salesPrice * productCount);
                    productIdSet1.add(productId);
                }
                else if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    mallProductCount += productCount;
                    mallProductTotalPrice += (salesPrice * productCount);
                    productIdSet2.add(productId);
                }
                totalCount += productCount;
                totalPrice += (salesPrice * productCount);
            }
            Map<String, String> reMap = new HashMap<>();
            reMap.put("categoryFirstName", dayList.get(0).get("categoryFirstName") + "");
            reMap.put("categorySecondName", entry.getKey());
            reMap.put("categorythirdName", dayList.get(0).get("categorythirdName") + "");
            reMap.put("totalCount", totalCount + "");
            reMap.put("totalCountSKU", (productIdSet1.size() + productIdSet2.size()) + "");
            reMap.put("totalPrice", MathUtil.round(totalPrice, 2));
            reMap.put("saleProductCount", saleProductCount + "");
            reMap.put("saleProductCountSKU", productIdSet1.size() + "");
            reMap.put("saleProductTotalPrice", MathUtil.round(saleProductTotalPrice, 2));
            reMap.put("saleProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(saleProductTotalPrice / totalPrice * 100, 2) + "%");
            reMap.put("mallProductCount", mallProductCount + "");
            reMap.put("mallProductCountSKU", productIdSet2.size() + "");
            reMap.put("mallProductRealPrice", MathUtil.round(mallProductTotalPrice, 2));
            reMap.put("mallProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(mallProductTotalPrice / totalPrice * 100, 2) + "%");
            resultList.add(reMap);
        }
        
        //统计没有分类的商品
        int totalCount = 0;
        int saleProductCount = 0;
        int mallProductCount = 0;
        double totalPrice = 0.0;
        double saleProductTotalPrice = 0.0;
        double mallProductTotalPrice = 0.0;
        Set<Integer> productIdSet1 = new HashSet<Integer>();
        Set<Integer> productIdSet2 = new HashSet<Integer>();
        for (Map<String, Object> it : noCategoryProductInfo)
        {
            int type = Integer.parseInt(it.get("type") + "");
            int productId = Integer.parseInt(it.get("productId") + "");
            double salesPrice = Double.parseDouble(it.get("salesPrice") == null ? "0.0" : it.get("salesPrice") + "");
            int productCount = Integer.parseInt(it.get("productCount") == null ? "0" : it.get("productCount") + "");
            if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                saleProductCount += productCount;
                saleProductTotalPrice += (salesPrice * productCount);
                productIdSet1.add(productId);
            }
            else if (type == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                mallProductCount += productCount;
                mallProductTotalPrice += (salesPrice * productCount);
                productIdSet2.add(productId);
            }
            totalCount += productCount;
            totalPrice += (salesPrice * productCount);
        }
        Map<String, String> reMap = new HashMap<>();
        reMap.put("categoryFirstName", "空");
        reMap.put("categorySecondName", "空");
        reMap.put("categorythirdName", "空");
        reMap.put("totalCount", totalCount + "");
        reMap.put("totalCountSKU", (productIdSet1.size() + productIdSet2.size()) + "");
        reMap.put("totalPrice", MathUtil.round(totalPrice, 2));
        reMap.put("saleProductCount", saleProductCount + "");
        reMap.put("saleProductCountSKU", productIdSet1.size() + "");
        reMap.put("saleProductTotalPrice", MathUtil.round(saleProductTotalPrice, 2));
        reMap.put("saleProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(saleProductTotalPrice / totalPrice * 100, 2) + "%");
        reMap.put("mallProductCount", mallProductCount + "");
        reMap.put("mallProductCountSKU", productIdSet2.size() + "");
        reMap.put("mallProductRealPrice", MathUtil.round(mallProductTotalPrice, 2));
        reMap.put("mallProductTotalPricePrecent", totalPrice == 0.0 ? "0" : MathUtil.round(mallProductTotalPrice / totalPrice * 100, 2) + "%");
        resultList.add(reMap);
        
        Collections.sort(resultList, new Comparator<Map<String, String>>()
        {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2)
            {
                Double d1 = Double.valueOf(o1.get("totalPrice"));
                Double d2 = Double.valueOf(o2.get("totalPrice"));
                return d2.compareTo(d1);
            }
        });
        return resultList;
    }
    
    @Override
    public List<Map<String, Object>> firstCategoryTurnoverAnalyze(Map<String, Object> para)
        throws Exception
    {
        //具有完整分类的商品信息
        List<Map<String, Object>> fullCategoryProductInfo = analyzeDao.findAllFirstCategoryProduct(para);
        
        //没有分类的商品信息
        List<Map<String, Object>> noCategoryProductInfo = analyzeDao.findNoCategoryProduct(para);
        
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        
        Map<String, List<Map<String, Object>>> groupByFirstCNameMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> it : fullCategoryProductInfo)
        {
            String firstCName = it.get("categoryFirstName") + "";
            List<Map<String, Object>> categoryMap = groupByFirstCNameMap.get(firstCName);
            if (categoryMap == null)
            {
                categoryMap = new ArrayList<Map<String, Object>>();
                groupByFirstCNameMap.put(firstCName, categoryMap);
            }
            categoryMap.add(it);
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByFirstCNameMap.entrySet())
        {
            Set<Integer> orderIdSet = new HashSet<Integer>();
            List<Map<String, Object>> list = entry.getValue();
            for (Map<String, Object> it : list)
            {
                int orderId = Integer.parseInt(it.get("orderId") == null ? "0" : it.get("orderId") + "");
                orderIdSet.add(orderId);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("categoryName", entry.getKey());
            map.put("totalCount", orderIdSet.size() + "");
            resultList.add(map);
        }
        
        Collections.sort(resultList, new Comparator<Map<String, Object>>()
        {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2)
            {
                int t1 = Integer.parseInt(o1.get("totalCount") + "");
                int t2 = Integer.parseInt(o2.get("totalCount") + "");
                return t2 - t1;
            }
        });
        
        Set<Integer> orderIdSet = new HashSet<Integer>();
        for (Map<String, Object> it : noCategoryProductInfo)
        {
            int orderId = Integer.parseInt(it.get("orderId") == null ? "0" : it.get("orderId") + "");
            orderIdSet.add(orderId);
        }
        if (orderIdSet.size() > 0)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("categoryName", "无分类");
            map.put("totalCount", orderIdSet.size() + "");
            resultList.add(map);
        }
        return resultList;
    }
    
    @Override
    public List<Map<String, String>> customerAverageAnalyze(Map<String, Object> para)
        throws Exception
    {
        String selectDate = para.get("selectDate").toString();
        List<Map<String, Object>> infoList = analyzeDao.findProductTurnOverAnalyze(para);
        Map<String, List<Map<String, Object>>> groupByDayAndName = new HashMap<String, List<Map<String, Object>>>();
        
        Timestamp payTime = null;
        for (Map<String, Object> it : infoList)
        {
            payTime = (Timestamp)it.get("payTime");
            int day = new DateTime(payTime.getTime()).getDayOfMonth();
            String name = it.get("accountName") + "";
            String key = day + "#" + name;
            List<Map<String, Object>> dayList = groupByDayAndName.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<Map<String, Object>>();
                groupByDayAndName.put(key, dayList);
            }
            dayList.add(it);
        }
        
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayAndName.entrySet())
        {
            int totalCount = 0;
            Set<Integer> productIdSet = new HashSet<Integer>();
            String day = entry.getKey().split("#")[0];
            String name = entry.getKey().split("#")[1];
            List<Map<String, Object>> dayList = entry.getValue();
            for (Map<String, Object> it : dayList)
            {
                int productId = Integer.parseInt(it.get("productId") + "");
                int productCount = Integer.parseInt(it.get("productCount") == null ? "0" : it.get("productCount") + "");
                productIdSet.add(productId);
                totalCount += productCount;
                
            }
            Map<String, String> reMap = new HashMap<>();
            reMap.put("day", day);
            reMap.put("date", selectDate + "-" + day);
            reMap.put("name", name);
            reMap.put("productCount", productIdSet.size() + "");
            reMap.put("totalCount", totalCount + "");
            resultList.add(reMap);
        }
        
        Collections.sort(resultList, new Comparator<Map<String, String>>()
        {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2)
            {
                int day1 = Integer.valueOf(o1.get("day") + "");
                int day2 = Integer.valueOf(o2.get("day") + "");
                return day1 - day2;
            }
        });
        return resultList;
    }
    
    @Override
    public List<UserBehaviorView> userFirstBehaviorAnalyze(Map<String, String> para)
        throws Exception
    {
        String prevStartTimeStr = para.get("prevStartTime");
        String prevEndTimeStr = para.get("prevEndTime");
        String nextStartTimeStr = para.get("nextStartTime");
        String nextEndTimeStr = para.get("nextEndTime");
        String prevStartTime =
            DateTime.parse(prevStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        String prevEndTime =
            DateTime.parse(prevEndTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        String nextStartTime =
            DateTime.parse(nextStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
        String nextEndTime =
            DateTime.parse(nextEndTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> searchPara = new HashMap<String, Object>();
        searchPara.put("oStartTime", prevStartTime);
        searchPara.put("oEndTime", prevEndTime);
        //        searchPara.put("aStartTime", prevStartTime);
        //        searchPara.put("aEndTime", prevEndTime);
        
        //String month = DateTime.parse(prevStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd")).toString("yyyyMM");
        List<Integer> oldAccountIds = orderDao.findOldBuyAccountIds(prevStartTime);
        if (oldAccountIds != null && oldAccountIds.size() > 0)
        {
            searchPara.put("idList", oldAccountIds);
        }
        
        List<Map<String, Object>> prevsInfo = orderDao.userFirstBehaviorAnalyze(searchPara);
        double totalPrice = 0.0d;
        int totalCount = 0;
        Map<Integer, Map<String, Object>> prevsMap = new HashMap<Integer, Map<String, Object>>();
        for (Map<String, Object> map : prevsInfo)
        {
            int times = Integer.valueOf(map.get("times") + "").intValue();
            int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
            double price = Double.valueOf(map.get("totalPrice") + "").intValue();
            Map<String, Object> itMap = prevsMap.get(times);
            if (itMap == null)
            {
                itMap = new HashMap<String, Object>();
                Set<Integer> accountIdSet = new HashSet<Integer>();
                accountIdSet.add(accountId);
                itMap.put("accountIdSet", accountIdSet);
                itMap.put("sumPrice1", price);
                prevsMap.put(times, itMap);
            }
            else
            {
                Set<Integer> accountIdSet = (Set<Integer>)itMap.get("accountIdSet");
                accountIdSet.add(accountId);
                double sumPrice = Double.valueOf(itMap.get("sumPrice1") + "");
                itMap.put("sumPrice1", sumPrice + price);
            }
            totalPrice += price;
            totalCount++;
        }
        
        searchPara.put("oStartTime", nextStartTime);
        searchPara.put("oEndTime", nextEndTime);
        List<Map<String, Object>> nextInfo = orderDao.userFirstBehaviorAnalyze(searchPara);
        for (Map<String, Object> it : prevsMap.values())
        {
            Set<Integer> accountIdSet = (Set<Integer>)it.get("accountIdSet");
            Set<Integer> _accountIdSet = new HashSet<Integer>();
            double sumPrice = 0.0d;
            for (Map<String, Object> map : nextInfo)
            {
                int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
                double price = Double.valueOf(map.get("totalPrice") + "").intValue();
                if (accountIdSet.contains(accountId))
                {
                    sumPrice += price;
                    _accountIdSet.add(accountId);
                }
            }
            it.put("count1", accountIdSet.size());
            it.put("count2", _accountIdSet.size());
            it.put("sumPrice2", sumPrice);
        }
        
        List<UserBehaviorView> ubvList = new ArrayList<UserBehaviorView>();
        for (Map.Entry<Integer, Map<String, Object>> it : prevsMap.entrySet())
        {
            int times = it.getKey();
            Map<String, Object> valueMap = it.getValue();
            int userCount = Integer.valueOf(valueMap.get("count1") + "").intValue();
            double totalAmount = Double.valueOf(valueMap.get("sumPrice1") + "").doubleValue();
            int nextUserCount = Integer.valueOf(valueMap.get("count2") + "").intValue();
            double nextTotalAmount = Double.valueOf(valueMap.get("sumPrice2") + "").doubleValue();
            
            UserBehaviorView ubv = new UserBehaviorView();
            ubv.setTimes(times);
            ubv.setUserCount(userCount + "");
            ubv.setUserCountPercent(totalCount == 0 ? "0%" : new DecimalFormat("0.00").format(1.0 * userCount / totalCount * 100) + "%");
            ubv.setTotalAmount(new DecimalFormat("0.00").format(totalAmount));
            ubv.setTotalAmountPercent(totalPrice == 0.0d ? "0%" : new DecimalFormat("0.00").format(totalAmount / totalPrice * 100) + "%");
            ubv.setAveragePrice(userCount == 0 ? "0" : new DecimalFormat("0.00").format(totalAmount / userCount));
            ubv.setNextUserCount(nextUserCount + "");
            ubv.setNextTotalAmount(new DecimalFormat("0.00").format(nextTotalAmount));
            ubv.setNextStartTime(nextStartTimeStr);
            ubv.setNextEndTime(nextEndTimeStr);
            ubv.setPrevStartTime(prevStartTimeStr);
            ubv.setPrevEndTime(prevEndTimeStr);
            ubvList.add(ubv);
        }
        
        /*        UserBehaviorView ubv = new UserBehaviorView();
                ubv.setTimes(0);
                ubv.setUserCount(prevZeroBuyCount + "");
                ubv.setUserCountPercent(totalCount == 0 ? "0%" : new DecimalFormat("0.00").format(1.0 * prevZeroBuyCount / totalCount * 100) + "%");
                ubv.setTotalAmount(new DecimalFormat("0.00").format(0));
                ubv.setTotalAmountPercent("0.00%");
                ubv.setAveragePrice("0");
                ubv.setNextStartTime(nextStartTimeStr);
                ubv.setNextEndTime(nextEndTimeStr);
                ubv.setPrevStartTime(prevStartTimeStr);
                ubv.setPrevEndTime(prevEndTimeStr);
                
                Map<String, Object> seach = new HashMap<String, Object>();
                seach.put("idList", oldAccountIds);
                seach.put("prevStartTime", prevStartTime);
                seach.put("prevEndTime", prevEndTime);
                seach.put("nextStartTime", nextStartTime);
                seach.put("nextEndTime", nextEndTime);
                Map<String, Object> nextMap = orderDao.countFirstNextBuyUser(seach);
                ubv.setNextUserCount(nextMap.get("nextUserCount") == null ? "0" : nextMap.get("nextUserCount") + "");
                ubv.setNextTotalAmount(new DecimalFormat("0.00").format(nextMap.get("nextTotalAmount") == null ? 0 : Double.valueOf(nextMap.get("nextTotalAmount") + "")));
                ubvList.add(ubv);*/
        Collections.sort(ubvList, new Comparator<UserBehaviorView>()
        {
            @Override
            public int compare(UserBehaviorView o1, UserBehaviorView o2)
            {
                return o1.getTimes() - o2.getTimes();
            }
        });
        
        return ubvList;
    }
    
    @Override
    public List<Map<String, Object>> orderSendTimeAnalyzeByMonth(String selectDate, String payTimeBegin, String payTimeEnd)
        throws Exception
    {
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("startTimeBegin", payTimeBegin);
        para.put("startTimeEnd", payTimeEnd);
        para.put("orderType", 1);
        List<Map<String, Object>> orderInfo = orderDao.findAllOrderInfoForList(para);
        
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> it : orderInfo)
        {
            Timestamp payTime = (Timestamp)it.get("payTime");
            String key = new DateTime(payTime.getTime()).getDayOfMonth() + "";
            List<Map<String, Object>> dayList = groupByDayMap.get(key);
            if (dayList == null)
            {
                dayList = new ArrayList<>();
                groupByDayMap.put(key, dayList);
            }
            dayList.add(it);
        }
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : groupByDayMap.entrySet())
        {
            int day = Integer.valueOf(entry.getKey());
            List<Map<String, Object>> dayOrderList = entry.getValue();
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
            for (Map<String, Object> item : dayOrderList)
            {
                int orderId = Integer.parseInt(item.get("id").toString());
                int status = Integer.parseInt(item.get("status").toString());
                int isTimeout = Integer.parseInt(item.get("isTimeout").toString());
                int appChannel = Integer.parseInt(item.get("appChannel").toString());
                Timestamp sendTime = item.get("sendTime") == null ? null : (Timestamp)item.get("sendTime");
                if (status == OrderEnum.ORDER_STATUS.REVIEW.getCode() || status == OrderEnum.ORDER_STATUS.SENDGOODS.getCode() || status == OrderEnum.ORDER_STATUS.SUCCESS.getCode())
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
            map.put("day", day);
            map.put("date", selectDate + "-" + (day > 9 ? day : ("0" + day)));
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
            map.put("amerceAmount", (timeoutNoSendAmount + timeoutSendAmount) * 10);
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
                int day1 = Integer.parseInt(o1.get("day").toString());
                int day2 = Integer.parseInt(o2.get("day").toString());
                return day1 - day2;
            }
        });
        
        return resultList;
        
    }

    
    @Override
    public List<Map<String, Object>> viewStatisticDetail(Map<String, Object> para) throws Exception
    {
        List<Map<String,Object>> statisticDetailInfo = accountDao.viewStatisticDetail(para);
        Map<String,Object> timeAndPriceMap = new LinkedHashMap<String,Object>();
        for(Map<String,Object> map:statisticDetailInfo){
            String creatTime = String.valueOf(map.get("createTime"));
            String key = StringUtils.split(creatTime," ")[0];
            double price = (double)map.get("totalPrice");
            if(timeAndPriceMap.containsKey(key)){
                double value = (double)timeAndPriceMap.get(key);
                value = value + price;
                timeAndPriceMap.put(key, value);
            }else{
                timeAndPriceMap.put(key,price);
            }
        }
        statisticDetailInfo.removeAll(statisticDetailInfo);
        for(Map.Entry<String, Object> map:timeAndPriceMap.entrySet()){
            Map<String,Object> m = new HashMap<String,Object>();
            String price = String.valueOf(new DecimalFormat("0.00").format(map.getValue()));
            m.put("createTime",map.getKey());
            m.put("totalPrice",price);
            statisticDetailInfo.add(m);
        }
        return statisticDetailInfo;
    }
    
    
    @Override
    public Map<String, Object> getUserStatisticByChannel(Map<String, Object> para) throws Exception
    {
        List<UserStatisticView> registUserStatisticInfo = accountDao.findRegistUserStatisticByChannel(para);
        List<UserStatisticView> orderUserStatisticInfo = accountDao.findOrderUserStatisticByChannel(para);
        
        //左岸城堡注册用户List
        List<UserStatisticView> registGGJInfoList = new ArrayList<UserStatisticView>();
        //左岸城堡注册用户List
        List<UserStatisticView> registGGTInfoList = new ArrayList<UserStatisticView>();
        //左岸城堡注册用户List
        List<UserStatisticView> registQQBSInfoList = new ArrayList<UserStatisticView>();
        
        //左岸城堡订单用户List
        List<UserStatisticView> orderGGJInfoList = new ArrayList<UserStatisticView>();
        //左岸城堡订单用户List
        List<UserStatisticView> orderGGTInfoList = new ArrayList<UserStatisticView>();
        //左岸城堡订单用户List
        List<UserStatisticView> orderQQBSInfoList = new ArrayList<UserStatisticView>();
        
        setInfoListByType(registUserStatisticInfo,registGGJInfoList,registGGTInfoList,registQQBSInfoList);
        setInfoListByType(orderUserStatisticInfo,orderGGJInfoList,orderGGTInfoList,orderQQBSInfoList);
        
        //注册用户为true，订单用户为false
        reStatisticByChannel(true,1,registGGJInfoList);
        reStatisticByChannel(true,6,registGGTInfoList);
        reStatisticByChannel(true,8,registQQBSInfoList);
        reStatisticByChannel(false,1,orderGGJInfoList);
        reStatisticByChannel(false,6,orderGGTInfoList);
        reStatisticByChannel(false,8,orderQQBSInfoList);
                 
        setOrderInfoToRegist(registGGJInfoList,orderGGJInfoList);
        setOrderInfoToRegist(registGGTInfoList,orderGGTInfoList);
        setOrderInfoToRegist(registQQBSInfoList,orderQQBSInfoList);
        
        Comparator<UserStatisticView> comparator = new Comparator<UserStatisticView>(){
            @Override
            public int compare(UserStatisticView o1, UserStatisticView o2)
            {
                return o1.getSort()>o2.getSort()?1:o1.getSort()==o2.getSort()?0:-1;
         }};
        
        Collections.sort(registGGJInfoList,comparator);
        Collections.sort(registGGTInfoList,comparator);
        Collections.sort(registQQBSInfoList,comparator);
        
        registUserStatisticInfo.removeAll(registUserStatisticInfo);
        registUserStatisticInfo.addAll(registGGJInfoList);
        registUserStatisticInfo.addAll(registGGTInfoList);
        registUserStatisticInfo.addAll(registQQBSInfoList);
        
        //左岸城堡硬编码 other(999)-> MWEB
        //以后有更改直接去除该代码
        for(UserStatisticView usv:registQQBSInfoList){
            usv.setPlatform("MWEB");
        }
        
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("rows", registUserStatisticInfo);
        
        return resultMap;
    }
    
    private void setOrderInfoToRegist(List<UserStatisticView> registInfoList,List<UserStatisticView> orderInfoList){
        for(UserStatisticView registUsv:registInfoList){
            int registChannel = registUsv.getAppChannel();
            for(UserStatisticView orderUsv:orderInfoList){
                int orderChannel = orderUsv.getAppChannel();
                if(registChannel==orderChannel){
                    registUsv.setOrderUserCount(orderUsv.getOrderUserCount());
                    registUsv.setTotalPriceDisplay(orderUsv.getTotalPriceDisplay());
                }
            }
        }
    }
    
    /***
     * 按照类型划分List
     * @param userStatisticInfo
     * @param list1
     * @param list2
     * @param list3
     */
    private void setInfoListByType(List<UserStatisticView> userStatisticInfo,List<UserStatisticView> list1,
                                                    List<UserStatisticView> list2,List<UserStatisticView> list3){
        for (UserStatisticView usv : userStatisticInfo)
        {
            int type = usv.getType();
            switch(type){
            //1,2,3,4,5左岸城堡
            case 1:case 2: case 3: case 4: case 5:
                list1.add(usv);
            break;
            //6,7 左岸城堡
            case 6:case 7:
                list2.add(usv);
            break;
            //8 左岸城堡

            case 8:
                list3.add(usv);
            break;
            }
        }
    }
    
    /**
     * 统计不同type的但有相同channel的和
     * @param type
     * @param infoList
     */
    private void reStatisticByChannel(boolean isRoO,int type,List<UserStatisticView> infoList){
       Map<Integer,String> channelPriceMap = new LinkedHashMap<Integer,String>();
       for(UserStatisticView usv : infoList){
            int appChannel = usv.getAppChannel();
            double price = usv.getTotalPrice();
            int userCount = isRoO ?usv.getRegistUserCount():usv.getOrderUserCount();
            if(!channelPriceMap.containsKey(appChannel)){
                channelPriceMap.put(appChannel, price+"%"+userCount);
            }else{
                String[] priceUserCount =StringUtils.split(channelPriceMap.get(appChannel),"%");
                price+= Double.valueOf(priceUserCount[0]);
                userCount+= Integer.valueOf(priceUserCount[1]);
                channelPriceMap.put(appChannel, price+"%"+userCount);
            }
       }
       infoList.removeAll(infoList);
       for(Map.Entry<Integer, String> map:channelPriceMap.entrySet()){
           UserStatisticView usv = new UserStatisticView();
           int appChannel  = map.getKey();
           String[] priceUserCount =StringUtils.split(channelPriceMap.get(appChannel),"%");
           String totalPrice = new DecimalFormat("0.00").format(Double.valueOf(priceUserCount[0]));
           
           usv.setType(type);
           usv.setAppChannel(appChannel);
           usv.setTypeName(AccountEnum.ACCOUNT_CHANNEL_TYPE.getDescByCode(type));
           String appChannelName = 999 ==appChannel?CommonConstant.HISTORY_VERSION:
               CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel);
           usv.setAppChannelName(appChannelName);
           usv.setTotalPriceDisplay(totalPrice);
           int userCount = Integer.valueOf(priceUserCount[1]);
           if(isRoO){
               usv.setRegistUserCount(userCount);
           }else{
               usv.setOrderUserCount(userCount);
           }
           
           if ((appChannel == CommonEnum.OrderAppChannelEnum.IOS.ordinal() || appChannel == CommonEnum.OrderAppChannelEnum.IOS_VEST1.ordinal() || 
                   appChannel == CommonEnum.OrderAppChannelEnum.IOS_VEST2.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.IOS_slave.ordinal()||
                           appChannel == CommonEnum.OrderAppChannelEnum.IOS3_ANDROID.ordinal() || appChannel == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_IOS.ordinal()||
                           appChannel == CommonEnum.OrderAppChannelEnum.IOS_GEGE_YOUXUAN.getCode()|| appChannel == CommonEnum.OrderAppChannelEnum.IOS_ZUIMEI_CHIHUO.getCode()||
                           appChannel == CommonEnum.OrderAppChannelEnum.IOS_GEGE_HAIGOU.getCode() || appChannel == CommonEnum.OrderAppChannelEnum.IOS_MEISHI_TUANGOU.getCode()||
                           appChannel == CommonEnum.OrderAppChannelEnum.IOS_MEISHI_CAIPU.getCode())){
               usv.setPlatform("IOS");  
               usv.setSort(1); 
           }else if(appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_360.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_MI.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_91.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_BAIDU.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_WAN_DOU_JIA.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_YING_YONG_BAO.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_TAOBAO.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_VIVO.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_INDEX.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_SOUGOU.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_ANZHI.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_SUNINGYIGOU.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_HUAWEI.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_JINRITOUTIAO.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.MEIZU.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.SAMSUNG_ANDROID.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.LENOVO_ANDROID.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.GiONEE_ANDROID.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.LETV_ANDROID.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.AWL_ANDROID.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.OPPO_ANDROID.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.JIFENG_ANDROID.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.MUMAYI_ANDROID.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.YINYONGHUI_ANDROID.ordinal() ||
                   appChannel == CommonEnum.OrderAppChannelEnum.YOUYI_ANDROID.ordinal()||appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_OTHER.ordinal()||
                   appChannel == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_ANDROID.ordinal()){
               usv.setPlatform("Android");
               usv.setSort(2);
           }else if(appChannel == CommonEnum.OrderAppChannelEnum.ANDROID_MOBILE.ordinal()){//添加网页类型
               usv.setPlatform("MWEB");
               usv.setSort(3);
           }else if(999 == appChannel){//999
               usv.setPlatform("Other");
               usv.setSort(4);
           }else{
               usv.setPlatform("MWEB");
               usv.setSort(3);
           }
           infoList.add(usv);
       }
    }
    
}
