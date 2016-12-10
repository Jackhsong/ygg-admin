package com.ygg.admin.service.yw.data.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.CategoryDao;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.service.yw.data.YwAnalyzeDataService;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.MathUtil;


@Repository("ywAnalyzerService")
public class YwAnalyzeDataServiceImpl implements YwAnalyzeDataService
{

    @Resource
    private OrderDao orderDao = null;
    
    @Resource
    private ProductDao productDao = null;
    
    @Resource
    private CategoryDao categoryDao;
    
    @Override
    public Map<String, Object> monthAnalyze(Map<String, Object> para)
            throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        String selectDate = para.get("selectDate") + "";
        List<Map<String, Object>> result = orderDao.findOrderSalesRecordForYWMonthAnalyze(para);
        // 将数据按月天分组 key为天
        Map<String, List<Map<String, Object>>> groupByDayMap = new HashMap<String, List<Map<String, Object>>>();
        Timestamp payTime = null;
        DateTime payTimeDateTime = null;
        int day = 0;
        for (Map<String, Object> currMap : result)
        {
            payTime = (Timestamp) currMap.get("createTime");
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
                Long accountId = (Long) currMap.get("accountId");
                int status = Integer.parseInt(currMap.get("status") + "");
                if (status == OrderEnum.ORDER_STATUS.REVIEW.getCode()
                        || status == OrderEnum.ORDER_STATUS.SENDGOODS.getCode()
                        || status == OrderEnum.ORDER_STATUS.SUCCESS.getCode()
                        || status == OrderEnum.ORDER_STATUS.USER_CANCEL.getCode())
                {
                    payOrderCount++;
                    totalPrice += currTotalPrice;
                    persons.add(accountId);
                    totalPersons.add(accountId);
                }
            }
            int totalOrderCount = dayList.size();// 创建订单总数
            int totalPersonCount = persons.size();
            // 开始计算 (笔单价 = 订单金额 / 订单数量) (客单价 = 订单金额 / 成交人数)
            double divOrderCountPrice = division( totalPrice,payOrderCount);
            double divPersonCountPrice =  division( totalPrice,totalPersonCount);
            double divPayOrderCount = payOrderCount * 1.0d / totalOrderCount
                    * 100.0d;
            ForSortRow row = new ForSortRow();
            row.setTotalOrderCount(totalOrderCount + "");
            row.setPayOrderCount(payOrderCount + "");
            row.setTotalPersonCount(totalPersonCount + "");
            row.setTotalPrice(new DecimalFormat("0.00").format(totalPrice));
            row.setDivOrderCountPrice(new DecimalFormat("0.00")
                    .format(divOrderCountPrice));
            row.setDivPersonCountPrice(new DecimalFormat("0.00")
                    .format(divPersonCountPrice));
            row.setDivPayOrderCount(new DecimalFormat("0.00")
                    .format(divPayOrderCount) + "%");
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
            totalOrderCount += Integer
                    .parseInt(forSortRow.getTotalOrderCount());
            payOrderCount += Integer.parseInt(forSortRow.getPayOrderCount());
            totalPrice += Double.parseDouble(forSortRow.getTotalPrice());
            // totalPersonCount +=
            // Integer.parseInt(forSortRow.getTotalPersonCount());
        }
        resultMap.put("rows", newrows);

        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("totalPrice", MathUtil.round(totalPrice, 2));
        lastRow.put("totalOrderCount", totalOrderCount + "");
        lastRow.put("payOrderCount", payOrderCount + "");
        lastRow.put("totalPersonCount", totalPersonCount + "");
        lastRow.put(
                "divOrderCountPrice",
                payOrderCount == 0 ? "0" : MathUtil.round(totalPrice
                        / payOrderCount, 2));
        lastRow.put(
                "divPayOrderCount",
                totalOrderCount == 0 ? "0" : MathUtil.round(payOrderCount
                        * 1.0d / totalOrderCount * 100.0d, 2)
                        + "%");
        lastRow.put("divPersonCountPrice", totalPersonCount == 0 ? "0"
                : MathUtil.round(totalPrice / totalPersonCount, 2));
        resultMap.put("lastRow", lastRow);
        return resultMap;
    }
    
    
    @Override
    public List<Map<String, Object>> todaySaleTop(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = orderDao.findYWAllTodaySaleRelOrderProduct(para);
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
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> saleLineData(DateTime date) throws Exception
    {

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("payTimeBegin", date.toString("yyyy-MM-dd 00:00:00"));
        para.put("payTimeEnd", date.plusDays(1).toString("yyyy-MM-dd 00:00:00"));
        List<Map<String, Object>> data1 = orderDao.findYWSaleDataByDate(para);
        Map<String, Object> re1 = adjustData(data1, DateTime.now().getHourOfDay() + 1);
        List<Double> nowSaleData = (List<Double>)re1.get("data");
        double nowTotal = Double.parseDouble(re1.get("total") == null ? "0" : re1.get("total") + "");
        para.put("payTimeBegin", date.plusDays(-1).toString("yyyy-MM-dd 00:00:00"));
        para.put("payTimeEnd", date.toString("yyyy-MM-dd 00:00:00"));
        List<Map<String, Object>> data2 = orderDao.findYWSaleDataByDate(para);
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
    
    private Map<String, Object> adjustData(List<Map<String, Object>> data, int lastHour)
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
    
    //去除NAN情况
    private double division(double firstPrice,int count){
        double price = firstPrice / count;
        if("NAN".equalsIgnoreCase(String.valueOf(price))){
            return 0.00;
        }
        return price;
    }
    
    
    @Override
    public Map<String, Object> productDataCustom(Map<String, Object> para)
            throws Exception
    {
        List<Map<String, Object>> productSaleInfoList = productDao.findYWProductSalesRecord(para);
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
    
    
    
}
