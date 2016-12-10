package com.ygg.admin.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.WechatGroupDataDao;
import com.ygg.admin.service.WechatGroupDataService;
import com.ygg.admin.thread.MonthListThread;
import com.ygg.admin.thread.ProductDateListThread;

@Service("wechatGroupDataService")
public class WechatGroupDataServiceImpl implements WechatGroupDataService
{
    private Logger log = Logger.getLogger(WechatGroupDataServiceImpl.class);
    
    @Resource
    private WechatGroupDataDao wechatGroupDataDao;
    
    /*
     * @Override public JSONObject monthList(String selectDate) { JSONObject jsonObject = new JSONObject(); JSONArray
     * array = new JSONArray(); // 创建一个数值格式化对象 NumberFormat numberFormat = NumberFormat.getInstance(); // 设置精确到小数点后2位
     * numberFormat.setMaximumFractionDigits(2); String sql =
     * "SELECT DATE_FORMAT(i.create_time, '%Y-%m-%d') as createTime,count(i.id) as createGroupCount FROM mweb_group_product_info i WHERE DATE_FORMAT(i.create_time, '%Y-%m') = DATE_FORMAT('"
     * + selectDate + "', '%Y-%m') GROUP BY DATE_FORMAT(i.create_time, '%Y-%m-%d') order by i.create_time desc";
     * JSONObject parameter = new JSONObject(); parameter.put("sql", sql); List<JSONObject> groupByDay =
     * wechatGroupDataDao.customSql(parameter);
     * 
     * int totalCreateGroupCount = 0; int totalSuccessGroupCount = 0; int totalCreateOrderCount = 0; int
     * totalSuccessOrderCount = 0; int totalAloneOrderCount = 0; double totalCreateRealPrice = 0; double
     * totalSuccessRealPrice = 0; double totalAloneRealPrice = 0; String totalSuccessRate = "0.00%";
     * 
     * for (Iterator<JSONObject> it = groupByDay.iterator(); it.hasNext();) { JSONObject jdata = new JSONObject();
     * JSONObject j = it.next(); int createGroupCount = j.getIntValue("createGroupCount"); String createTime =
     * j.getString("createTime"); totalCreateGroupCount += createGroupCount; jdata.put("createTime", createTime);
     * jdata.put("createGroupCount", createGroupCount);
     * 
     * StringBuffer sb = new StringBuffer("SELECT * FROM "); // 创建金额与创建订单数 sb.append(
     * "(SELECT count(o.id) as createOrderCount,IFNULL(sum(o.real_price), 0) AS createRealPrice FROM `order` o WHERE o.type=2 AND o.is_group=1 AND o.pay_time !='0000-00-00 00:00:00' "
     * ); sb.append("AND DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('" + createTime + "', '%Y-%m-%d')) a,");
     * 
     * // 成功拼团数 sb.append(
     * "(SELECT count(i.id) AS successGroupCount FROM mweb_group_product_info i WHERE i.`status` = 2 AND DATE_FORMAT(i.create_time, '%Y-%m-%d') = DATE_FORMAT('"
     * + createTime + "', '%Y-%m-%d')) b,"); // 成功金额 sb.append(
     * "(SELECT IFNULL(sum(o.real_price), 0) AS successRealPrice FROM `order` o WHERE o.type=2 AND o.is_group = 1 AND (o.`status` = 2 OR o.`status` = 3 OR o.`status` = 4) AND  DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('"
     * + createTime + "', '%Y-%m-%d')) c,"); // 成功订单数 sb.append(
     * "(SELECT count(o.id) as successOrderCount FROM `order` o WHERE o.type=2 AND o.is_group = 1 AND ( o.`status` = 2 OR o.`status` = 3 OR o.`status` = 4) AND DATE_FORMAT(o.pay_time, '%Y-%m-%d') = DATE_FORMAT('"
     * + createTime + "', '%Y-%m-%d')) d");
     * 
     * // 单独购买 // sb.append( // "(SELECT count(o.id) as aloneOrderCount,IFNULL(sum(o.real_price), 0) AS aloneRealPrice
     * FROM `order` o // WHERE o.type=2 AND o.is_group = 0 AND ( o.`status` = 2 OR o.`status` = 3 OR o.`status` = 4) AND
     * // DATE_FORMAT(o.pay_time, '%Y-%m-%d') = DATE_FORMAT('" // + createTime + "', '%Y-%m-%d')) e");
     * 
     * parameter.put("sql", sb.toString()); List<JSONObject> list = wechatGroupDataDao.customSql(parameter); JSONObject
     * jtemp = list.get(0); int createOrderCount = jtemp.getIntValue("createOrderCount"); double createRealPrice =
     * jtemp.getDoubleValue("createRealPrice"); int successGroupCount = jtemp.getIntValue("successGroupCount"); double
     * successRealPrice = jtemp.getDoubleValue("successRealPrice"); int successOrderCount =
     * jtemp.getIntValue("successOrderCount"); // int aloneOrderCount = jtemp.getIntValue("aloneOrderCount"); // double
     * aloneRealPrice = jtemp.getDoubleValue("aloneRealPrice"); jdata.put("createOrderCount", createOrderCount + "");
     * jdata.put("createRealPrice", numberFormat.format(createRealPrice)); jdata.put("successGroupCount",
     * successGroupCount + ""); jdata.put("successRealPrice", numberFormat.format(successRealPrice));
     * jdata.put("successOrderCount", successOrderCount + ""); // jdata.put("aloneOrderCount", aloneOrderCount + ""); //
     * jdata.put("aloneRealPrice", numberFormat.format(aloneRealPrice));
     * 
     * totalSuccessGroupCount += successGroupCount; totalCreateOrderCount += createOrderCount; totalSuccessOrderCount +=
     * successOrderCount; // totalAloneOrderCount += aloneOrderCount; totalCreateRealPrice += createRealPrice;
     * totalSuccessRealPrice += successRealPrice; // totalAloneRealPrice += aloneRealPrice;
     * 
     * // 成功率 if (createGroupCount > 0) { int successCount = jdata.getIntValue("successCount"); String successRate =
     * numberFormat.format((float)successGroupCount / (float)createGroupCount * 100) + "%"; jdata.put("successRate",
     * successRate);
     * 
     * } else { jdata.put("successRate", "0.00%"); }
     * 
     * array.add(jdata); } // 总成功率 if (totalCreateGroupCount > 0) { String result =
     * numberFormat.format((float)totalSuccessGroupCount / (float)totalCreateGroupCount * 100) + "%"; totalSuccessRate =
     * result; }
     * 
     * jsonObject.put("list", array); jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
     * jsonObject.put("totalSuccessGroupCount", totalSuccessGroupCount + ""); jsonObject.put("totalCreateOrderCount",
     * totalCreateOrderCount + ""); jsonObject.put("totalSuccessOrderCount", totalSuccessOrderCount + ""); //
     * jsonObject.put("totalAloneOrderCount", totalAloneOrderCount + ""); jsonObject.put("totalCreateRealPrice",
     * totalCreateRealPrice); jsonObject.put("totalSuccessRealPrice", totalSuccessRealPrice); //
     * jsonObject.put("totalAloneRealPrice", totalAloneRealPrice); jsonObject.put("totalSuccessRate", totalSuccessRate);
     * return jsonObject; }
     */
    
    @Override
    public JSONObject monthList(String selectDate)
    {
        List<MonthListThread> list = new ArrayList<MonthListThread>();
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String sql =
            "SELECT DATE_FORMAT(i.create_time, '%Y-%m-%d') as createTime,count(i.id) as createGroupCount FROM mweb_group_product_info i WHERE DATE_FORMAT(i.create_time, '%Y-%m') = DATE_FORMAT('"
                + selectDate + "', '%Y-%m') GROUP BY DATE_FORMAT(i.create_time, '%Y-%m-%d') order by i.create_time desc";
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sql);
        List<JSONObject> groupByDay = wechatGroupDataDao.customSql(parameter);
        
        int totalCreateGroupCount = 0;
        int totalSuccessGroupCount = 0;
        int totalCreateOrderCount = 0;
        int totalSuccessOrderCount = 0;
        int totalAloneOrderCount = 0;
        double totalCreateRealPrice = 0;
        double totalSuccessRealPrice = 0;
        double totalAloneRealPrice = 0;
        String totalSuccessRate = "0.00%";
        ExecutorService executorService = null;
        int listSize = groupByDay.size();
        long a = System.currentTimeMillis();
        if (listSize > 0)
        {
            if (listSize > 5)
            {
                executorService = Executors.newFixedThreadPool(5);
            }
            else
            {
                executorService = Executors.newFixedThreadPool(listSize);
            }
            
            for (Iterator<JSONObject> it = groupByDay.iterator(); it.hasNext();)
            {
                JSONObject jdata = new JSONObject();
                JSONObject j = it.next();
                int createGroupCount = j.getIntValue("createGroupCount");
                String createTime = j.getString("createTime");
                totalCreateGroupCount += createGroupCount;
                MonthListThread m = new MonthListThread(createTime, createGroupCount);
                executorService.execute(m);
                list.add(m);
            }
            executorService.shutdown();
        }
        
        if (listSize > 0)
        {
            while (true)
            {
                /**
                 * 通过不断运行ExecutorService.isTerminated()方法检测全部的线程是否都已经运行结束
                 */
                if (executorService.isTerminated())
                {
                    log.info("左岸城堡月度统计，所有线程任务执行完毕 时间差=" + String.valueOf(System.currentTimeMillis() - a));
                    
                    for (Iterator<MonthListThread> it = list.iterator(); it.hasNext();)
                    {
                        JSONObject jdata = new JSONObject();
                        MonthListThread m = it.next();
                        
                        jdata.put("createTime", m.getSelectDate());
                        jdata.put("createGroupCount", m.getCreateGroupCount() + "");
                        jdata.put("createOrderCount", m.getCreateOrderCount() + "");
                        jdata.put("createRealPrice", numberFormat.format(m.getCreateRealPrice()));
                        jdata.put("successGroupCount", m.getSuccessGroupCount() + "");
                        jdata.put("successRealPrice", numberFormat.format(m.getSuccessRealPrice()));
                        jdata.put("successOrderCount", m.getSuccessOrderCount() + "");
                        jdata.put("successRate", m.getSuccessRate());
                        totalSuccessGroupCount += m.getSuccessGroupCount();
                        totalCreateOrderCount += m.getCreateOrderCount();
                        totalSuccessOrderCount += m.getSuccessOrderCount();
                        
                        totalCreateRealPrice += m.getCreateRealPrice();
                        totalSuccessRealPrice += m.getSuccessRealPrice();
                        array.add(jdata);
                    }
                    
                    break;
                }
                
            }
        }
        // 总成功率
        if (totalCreateGroupCount > 0)
        {
            String result = numberFormat.format((float)totalSuccessGroupCount / (float)totalCreateGroupCount * 100) + "%";
            totalSuccessRate = result;
        }
        
        jsonObject.put("list", array);
        jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
        jsonObject.put("totalSuccessGroupCount", totalSuccessGroupCount + "");
        jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
        jsonObject.put("totalSuccessOrderCount", totalSuccessOrderCount + "");
        
        jsonObject.put("totalCreateRealPrice", totalCreateRealPrice);
        jsonObject.put("totalSuccessRealPrice", totalSuccessRealPrice);
        
        jsonObject.put("totalSuccessRate", totalSuccessRate);
        return jsonObject;
    }
    
    @Override
    public JSONObject monthList2(String selectDate)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        
        int totalCreateGroupCount = 0;
        int totalSuccessGroupCount = 0;
        int totalCreateOrderCount = 0;
        int totalSuccessOrderCount = 0;
        double totalCreateRealPrice = 0;
        double totalSuccessRealPrice = 0;
        String totalSuccessRate = "0.00%";
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        StringBuffer sb =
            new StringBuffer("SELECT  a.createTime,a.createGroupCount,a.successGroupCount,b.createOrderCount,b.createRealPrice,c.successOrderCount,c.successRealPrice,");
        sb.append("IF(a.createGroupCount>0,concat(round((a.successGroupCount/a.createGroupCount*100),2),'%'),'0.00%') as successRate from ");
        // 创团和成功拼团数
        sb.append(
            "(SELECT DATE_FORMAT(i.create_time, '%Y-%m-%d') as createTime,count(1) as createGroupCount,count(case when i.`status`=2 then i.id end ) as successGroupCount FROM mweb_group_product_info i GROUP BY DATE_FORMAT(i.create_time, '%Y-%m-%d') order by i.create_time desc) a,");
        // 创建金额与创建订单数
        sb.append(
            "(SELECT DATE_FORMAT(o.create_time, '%Y-%m-%d') as createTime,count(o.id) as  createOrderCount,IFNULL(sum(o.real_price), 0) AS createRealPrice from `order` o where o.type=2 AND o.is_group=1 AND o.pay_time !='0000-00-00 00:00:00'  GROUP BY DATE_FORMAT(o.create_time, '%Y-%m-%d') order by o.create_time desc) b,");
        // 成功金额和成功订单数
        sb.append(
            "(SELECT DATE_FORMAT(o.pay_time, '%Y-%m-%d') as payTime,count(case when o.`status` in(2,3,4) then o.id end ) as successOrderCount,IFNULL(sum(case when o.`status` in(2,3,4) then o.real_price end ), 0) AS successRealPrice  from `order` o  where o.type=2 AND o.is_group=1 AND o.pay_time !='0000-00-00 00:00:00'  GROUP BY DATE_FORMAT(o.pay_time, '%Y-%m-%d') order by o.create_time desc) c ");
        sb.append("where a.createTime=b.createTime and b.createTime =c.payTime and  DATE_FORMAT(a.createTime, '%Y-%m')='" + selectDate + "'");
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sb.toString());
        List<JSONObject> list = wechatGroupDataDao.customSql(parameter);
        for (Iterator<JSONObject> it = list.iterator(); it.hasNext();)
        {
            JSONObject jdata = new JSONObject();
            JSONObject j = it.next();
            String createTime = j.getString("createTime");
            int createGroupCount = j.getInteger("createGroupCount");
            int successGroupCount = j.getInteger("successGroupCount");
            int createOrderCount = j.getInteger("createOrderCount");
            float createRealPrice = j.getFloat("createRealPrice");
            int successOrderCount = j.getInteger("successOrderCount");
            float successRealPrice = j.getFloat("successRealPrice");
            String successRate = j.getString("successRate");
            
            jdata.put("createTime", createTime);
            jdata.put("createGroupCount", createGroupCount + "");
            jdata.put("successGroupCount", successGroupCount + "");
            jdata.put("createOrderCount", createOrderCount + "");
            jdata.put("createRealPrice", numberFormat.format(createRealPrice));
            jdata.put("successOrderCount", successOrderCount + "");
            jdata.put("successRealPrice", numberFormat.format(successRealPrice));
            jdata.put("successRate", successRate);
            
            totalCreateGroupCount += createGroupCount;
            totalSuccessGroupCount += successGroupCount;
            totalCreateOrderCount += createOrderCount;
            totalSuccessOrderCount += successOrderCount;
            totalCreateRealPrice += createRealPrice;
            totalSuccessRealPrice += successRealPrice;
            array.add(jdata);
        }
        
        // 总成功率
        if (totalCreateGroupCount > 0)
        {
            String result = numberFormat.format((float)totalSuccessGroupCount / (float)totalCreateGroupCount * 100) + "%";
            totalSuccessRate = result;
        }
        
        jsonObject.put("list", array);
        jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
        jsonObject.put("totalSuccessGroupCount", totalSuccessGroupCount + "");
        jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
        jsonObject.put("totalSuccessOrderCount", totalSuccessOrderCount + "");
        
        jsonObject.put("totalCreateRealPrice", numberFormat.format(totalCreateRealPrice));
        jsonObject.put("totalSuccessRealPrice", numberFormat.format(totalSuccessRealPrice));
        
        jsonObject.put("totalSuccessRate", totalSuccessRate);
        return jsonObject;
    }
    
    /*
     * @Override public JSONObject productDateList(String startTime, String endTime) { // 创建一个数值格式化对象 NumberFormat
     * numberFormat = NumberFormat.getInstance(); // 设置精确到小数点后2位 numberFormat.setMaximumFractionDigits(2); JSONObject
     * jsonObject = new JSONObject(); JSONArray array = new JSONArray(); int totalCreateGroupCount = 0; int
     * totalSuccessGroupCount = 0; int totalCreateOrderCount = 0; int totalSuccessOrderCount = 0; // int
     * totalAloneOrderCount = 0; double totalCreateRealPrice = 0; double totalSuccessRealPrice = 0; // double
     * totalAloneRealPrice = 0; String totalSuccessRate = "0.00%"; String sql =
     * "SELECT count(i.id) AS createGroupCount,i.mweb_group_product_id,mp.`name` FROM mweb_group_product_info i, mweb_group_product mp WHERE i.mweb_group_product_id=mp.id AND DATE_FORMAT(i.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
     * + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime +
     * "', '%Y-%m-%d') GROUP BY i.mweb_group_product_id ORDER BY createGroupCount DESC"; JSONObject parameter = new
     * JSONObject(); parameter.put("sql", sql); List<JSONObject> groupByDay = wechatGroupDataDao.customSql(parameter);
     * for (Iterator<JSONObject> it = groupByDay.iterator(); it.hasNext();) { JSONObject jdata = new JSONObject();
     * JSONObject j = it.next(); int mwebGroupProductId = j.getInteger("mweb_group_product_id"); String name =
     * j.getString("name"); int createGroupCount = j.getIntValue("createGroupCount");
     * 
     * totalCreateGroupCount += createGroupCount; jdata.put("mwebGroupProductId", mwebGroupProductId); jdata.put("name",
     * name); jdata.put("createGroupCount", createGroupCount);
     * 
     * StringBuffer sb = new StringBuffer("SELECT * FROM "); // 创建金额 sb.append(
     * "(SELECT IFNULL(sum(o.real_price), 0) AS createRealPrice FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
     * + mwebGroupProductId +
     * " AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
     * + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) a,"); // 创建订单数 sb.append(
     * "(SELECT count(o.id) as createOrderCount FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
     * + mwebGroupProductId +
     * " AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
     * + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) b,"); // 成功拼团数 sb.append(
     * "(SELECT count(i.id) AS successGroupCount FROM mweb_group_product_info i,mweb_group_product p WHERE i.mweb_group_product_id = p.id AND p.id = "
     * + mwebGroupProductId + " AND i.`status` = 2 AND DATE_FORMAT(i.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('" +
     * startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) c,"); // 成功金额 sb.append(
     * "(SELECT IFNULL(sum(o.real_price), 0) AS successRealPrice FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
     * + mwebGroupProductId +
     * " AND o.is_group = 1 AND (o.`status`=2 or o.`status`=3 or o.`status`=4) AND DATE_FORMAT(o.pay_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
     * + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) d,"); // 成功订单数 sb.append(
     * "(SELECT count(o.id) as successOrderCount FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
     * + mwebGroupProductId +
     * " AND o.is_group = 1 AND (o.`status`=2 or o.`status`=3 or o.`status`=4) AND DATE_FORMAT(o.pay_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
     * + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) e"); parameter.put("sql",
     * sb.toString()); List<JSONObject> list = wechatGroupDataDao.customSql(parameter); JSONObject jtemp = list.get(0);
     * int createOrderCount = jtemp.getIntValue("createOrderCount"); double createRealPrice =
     * jtemp.getDoubleValue("createRealPrice"); int successGroupCount = jtemp.getIntValue("successGroupCount"); double
     * successRealPrice = jtemp.getDoubleValue("successRealPrice"); int successOrderCount =
     * jtemp.getIntValue("successOrderCount");
     * 
     * jdata.put("createOrderCount", createOrderCount + ""); jdata.put("createRealPrice",
     * numberFormat.format(createRealPrice)); jdata.put("successGroupCount", successGroupCount + "");
     * jdata.put("successRealPrice", numberFormat.format(successRealPrice)); jdata.put("successOrderCount",
     * successOrderCount + "");
     * 
     * totalSuccessGroupCount += successGroupCount; totalCreateOrderCount += createOrderCount; totalSuccessOrderCount +=
     * successOrderCount;
     * 
     * totalCreateRealPrice += createRealPrice; totalSuccessRealPrice += successRealPrice;
     * 
     * // 拼团成功率 // 成功率 if (createGroupCount > 0) { int successCount = jdata.getIntValue("successCount"); String
     * successRate = numberFormat.format((float)successGroupCount / (float)createGroupCount * 100) + "%";
     * jdata.put("successRate", successRate);
     * 
     * } else { jdata.put("successRate", "0.00%"); }
     * 
     * array.add(jdata); } // 总成功率 // 总成功率 if (totalCreateGroupCount > 0) { String result =
     * numberFormat.format((float)totalSuccessGroupCount / (float)totalCreateGroupCount * 100) + "%"; totalSuccessRate =
     * result; }
     * 
     * jsonObject.put("list", array); jsonObject.put("totalProductCount", groupByDay.size() + "");
     * jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + ""); jsonObject.put("totalSuccessGroupCount",
     * totalSuccessGroupCount + ""); jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
     * jsonObject.put("totalSuccessOrderCount", totalSuccessOrderCount + "");
     * 
     * jsonObject.put("totalCreateRealPrice", totalCreateRealPrice); jsonObject.put("totalSuccessRealPrice",
     * totalSuccessRealPrice);
     * 
     * jsonObject.put("totalSuccessRate", totalSuccessRate);
     * 
     * return jsonObject; }
     */
    
    @Override
    public JSONObject productDateList(String startTime, String endTime)
    {
        List<ProductDateListThread> list = new ArrayList<ProductDateListThread>();
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        int totalCreateGroupCount = 0;
        int totalSuccessGroupCount = 0;
        int totalCreateOrderCount = 0;
        int totalSuccessOrderCount = 0;
        // int totalAloneOrderCount = 0;
        double totalCreateRealPrice = 0;
        double totalSuccessRealPrice = 0;
        // double totalAloneRealPrice = 0;
        String totalSuccessRate = "0.00%";
        String sql =
            "SELECT count(i.id) AS createGroupCount,i.mweb_group_product_id,mp.`name` FROM mweb_group_product_info i, mweb_group_product mp WHERE i.mweb_group_product_id=mp.id AND DATE_FORMAT(i.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
                + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d') GROUP BY i.mweb_group_product_id ORDER BY createGroupCount DESC";
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sql);
        List<JSONObject> groupByDay = wechatGroupDataDao.customSql(parameter);
        
        ExecutorService executorService = null;
        int listSize = groupByDay.size();
        long a = System.currentTimeMillis();
        if (listSize > 0)
        {
            if (listSize > 5)
            {
                executorService = Executors.newFixedThreadPool(5);
            }
            else
            {
                executorService = Executors.newFixedThreadPool(listSize);
            }
            
            for (Iterator<JSONObject> it = groupByDay.iterator(); it.hasNext();)
            {
                JSONObject jdata = new JSONObject();
                JSONObject j = it.next();
                int mwebGroupProductId = j.getInteger("mweb_group_product_id");
                String name = j.getString("name");
                int createGroupCount = j.getIntValue("createGroupCount");
                
                totalCreateGroupCount += createGroupCount;
                
                ProductDateListThread p = new ProductDateListThread(startTime, endTime, mwebGroupProductId, createGroupCount, name);
                executorService.execute(p);
                list.add(p);
            }
            executorService.shutdown();
        }
        if (listSize > 0)
        {
            while (true)
            {
                /**
                 * 通过不断运行ExecutorService.isTerminated()方法检测全部的线程是否都已经运行结束
                 */
                if (executorService.isTerminated())
                {
                    log.info("左岸城堡商品统计，所有线程任务执行完毕 时间差=" + String.valueOf(System.currentTimeMillis() - a));
                    
                    for (Iterator<ProductDateListThread> it = list.iterator(); it.hasNext();)
                    {
                        JSONObject jdata = new JSONObject();
                        ProductDateListThread p = it.next();
                        
                        jdata.put("createGroupCount", p.getCreateGroupCount() + "");
                        jdata.put("mwebGroupProductId", p.getMwebGroupProductId() + "");
                        jdata.put("name", p.getName());
                        
                        jdata.put("createOrderCount", p.getCreateOrderCount() + "");
                        jdata.put("createRealPrice", numberFormat.format(p.getCreateRealPrice()));
                        jdata.put("successGroupCount", p.getSuccessGroupCount() + "");
                        jdata.put("successRealPrice", numberFormat.format(p.getSuccessRealPrice()));
                        jdata.put("successOrderCount", p.getSuccessOrderCount() + "");
                        jdata.put("successRate", p.getSuccessRate());
                        totalSuccessGroupCount += p.getSuccessGroupCount();
                        totalCreateOrderCount += p.getCreateOrderCount();
                        totalSuccessOrderCount += p.getSuccessOrderCount();
                        
                        totalCreateRealPrice += p.getCreateRealPrice();
                        totalSuccessRealPrice += p.getSuccessRealPrice();
                        array.add(jdata);
                    }
                    
                    break;
                }
                
            }
        }
        // 总成功率
        if (totalCreateGroupCount > 0)
        {
            String result = numberFormat.format((float)totalSuccessGroupCount / (float)totalCreateGroupCount * 100) + "%";
            totalSuccessRate = result;
        }
        
        jsonObject.put("list", array);
        jsonObject.put("totalProductCount", groupByDay.size() + "");
        jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
        jsonObject.put("totalSuccessGroupCount", totalSuccessGroupCount + "");
        jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
        jsonObject.put("totalSuccessOrderCount", totalSuccessOrderCount + "");
        
        jsonObject.put("totalCreateRealPrice", totalCreateRealPrice);
        jsonObject.put("totalSuccessRealPrice", totalSuccessRealPrice);
        
        jsonObject.put("totalSuccessRate", totalSuccessRate);
        
        return jsonObject;
    }
    
    @Override
    public JSONObject productDateList2(String startTime, String endTime)
    {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        int totalCreateGroupCount = 0;
        int totalSuccessGroupCount = 0;
        int totalCreateOrderCount = 0;
        int totalSuccessOrderCount = 0;
        
        double totalCreateRealPrice = 0;
        double totalSuccessRealPrice = 0;
        
        String totalSuccessRate = "0.00%";
        
        String s1 = "(SELECT ";
        String s2 =
            " FROM mweb_group_product_info i,mweb_group_product_info_relation_order r,`order` o WHERE i.id = r.mweb_group_product_info_id AND r.order_id = o.id and o.type = 2 AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' and i.mweb_group_product_id=p.mwebGroupProductId AND DATE_FORMAT(o.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
                + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) ";
        // 订单数
        String c_sql = "count(o.id) as  createOrderCount";
        // 成功订单数
        String c_sq2 = "count(case when o.`status` in(2,3,4) and i.`status`=2 then o.id end ) as successOrderCount";
        // 创建金额
        String c_sq3 = "IFNULL(sum(o.real_price), 0) AS createRealPrice";
        // 成功创建金额
        String c_sq4 = "IFNULL(sum(case when o.`status` in(2,3,4) and i.`status`=2 then o.real_price end ), 0) AS successRealPrice";
        
        StringBuffer sb = new StringBuffer(
            "SELECT p.mwebGroupProductId,p.`name`, count(p.mwebGroupProductId) as createGroupCount,count(case when i.`status`=2 then i.id end ) as successGroupCount,");
        sb.append(
            "IF(count(case when i.`status`=2 then i.id end )>0,concat(round((count(case when i.`status`=2 then i.id end )/count(p.mwebGroupProductId) *100),2),'%'),'0.00%') as successRate,");
        sb.append(s1 + c_sql + s2 + "as createOrderCount,");
        sb.append(s1 + c_sq2 + s2 + "as successOrderCount,");
        sb.append(s1 + c_sq3 + s2 + "as createRealPrice,");
        sb.append(s1 + c_sq4 + s2 + "as successRealPrice ");
        
        sb.append(" FROM (SELECT p.id AS mwebGroupProductId,p.`name` FROM mweb_group_product p) p,");
        sb.append("(SELECT i.id,i.`status`,i.mweb_group_product_id,DATE_FORMAT(i.create_time, '%Y-%m-%d') as createTime FROM mweb_group_product_info i ) i");
        sb.append(" where p.mwebGroupProductId=i.mweb_group_product_id and DATE_FORMAT(i.createTime, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startTime
            + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d') GROUP BY p.mwebGroupProductId ORDER BY count(p.mwebGroupProductId) desc");
            
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sb.toString());
        List<JSONObject> list = wechatGroupDataDao.customSql(parameter);
        
        for (Iterator<JSONObject> it = list.iterator(); it.hasNext();)
        {
            JSONObject jdata = new JSONObject();
            JSONObject j = it.next();
            String mwebGroupProductId = j.getString("mwebGroupProductId");
            String name = j.getString("name");
            int createGroupCount = j.getInteger("createGroupCount");
            int successGroupCount = j.getInteger("successGroupCount");
            int createOrderCount = j.getInteger("createOrderCount");
            float createRealPrice = j.getFloat("createRealPrice");
            int successOrderCount = j.getInteger("successOrderCount");
            float successRealPrice = j.getFloat("successRealPrice");
            String successRate = j.getString("successRate");
            
            jdata.put("mwebGroupProductId", mwebGroupProductId);
            jdata.put("name", name);
            jdata.put("createGroupCount", createGroupCount + "");
            jdata.put("successGroupCount", successGroupCount + "");
            jdata.put("createOrderCount", createOrderCount + "");
            jdata.put("createRealPrice", numberFormat.format(createRealPrice));
            jdata.put("successOrderCount", successOrderCount + "");
            jdata.put("successRealPrice", numberFormat.format(successRealPrice));
            jdata.put("successRate", successRate);
            
            totalCreateGroupCount += createGroupCount;
            totalSuccessGroupCount += successGroupCount;
            totalCreateOrderCount += createOrderCount;
            totalSuccessOrderCount += successOrderCount;
            totalCreateRealPrice += createRealPrice;
            totalSuccessRealPrice += successRealPrice;
            array.add(jdata);
            
        }
        jsonObject.put("list", array);
        jsonObject.put("totalProductCount", list.size() + "");
        jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
        jsonObject.put("totalSuccessGroupCount", totalSuccessGroupCount + "");
        jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
        jsonObject.put("totalSuccessOrderCount", totalSuccessOrderCount + "");
        
        jsonObject.put("totalCreateRealPrice", numberFormat.format(totalCreateRealPrice));
        jsonObject.put("totalSuccessRealPrice", numberFormat.format(totalSuccessRealPrice));
        
        jsonObject.put("totalSuccessRate", totalSuccessRate);
        return jsonObject;
    }
    
    @Override
    public JSONObject todaySaleTop(String day)
    {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        int totalCreateGroupCount = 0;
        
        int totalCreateOrderCount = 0;
        
        double totalCreateRealPrice = 0;
        
        String sql =
            "SELECT count(i.id) AS createGroupCount,i.mweb_group_product_id,mp.`name` FROM mweb_group_product_info i, mweb_group_product mp WHERE i.mweb_group_product_id=mp.id AND DATE_FORMAT(i.create_time, '%Y-%m-%d') = DATE_FORMAT('"
                + day + "', '%Y-%m-%d') GROUP BY i.mweb_group_product_id ORDER BY createGroupCount DESC";
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sql);
        List<JSONObject> groupByDay = wechatGroupDataDao.customSql(parameter);
        for (Iterator<JSONObject> it = groupByDay.iterator(); it.hasNext();)
        {
            JSONObject jdata = new JSONObject();
            JSONObject j = it.next();
            int mwebGroupProductId = j.getInteger("mweb_group_product_id");
            String name = j.getString("name");
            int createGroupCount = j.getIntValue("createGroupCount");
            
            totalCreateGroupCount += createGroupCount;
            jdata.put("mwebGroupProductId", mwebGroupProductId);
            jdata.put("name", name);
            jdata.put("createGroupCount", createGroupCount);
            
            StringBuffer sb = new StringBuffer("SELECT * FROM ");
            // 创建金额
            sb.append(
                "(SELECT IFNULL(sum(o.real_price), 0) AS createRealPrice FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
                    + mwebGroupProductId + " AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('" + day
                    + "', '%Y-%m-%d')) a,");
            // 创建订单数
            sb.append(
                "(SELECT count(o.id) as createOrderCount FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
                    + mwebGroupProductId + " AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('" + day
                    + "', '%Y-%m-%d') ) b");
                    
            parameter.put("sql", sb.toString());
            List<JSONObject> list = wechatGroupDataDao.customSql(parameter);
            JSONObject jtemp = list.get(0);
            int createOrderCount = jtemp.getIntValue("createOrderCount");
            double createRealPrice = jtemp.getDoubleValue("createRealPrice");
            
            jdata.put("createOrderCount", createOrderCount + "");
            jdata.put("createRealPrice", numberFormat.format(createRealPrice));
            
            totalCreateOrderCount += createOrderCount;
            
            totalCreateRealPrice += createRealPrice;
            
            array.add(jdata);
        }
        
        jsonObject.put("list", array);
        jsonObject.put("totalProductCount", groupByDay.size() + "");
        jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
        
        jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
        
        jsonObject.put("totalCreateRealPrice", totalCreateRealPrice);
        
        return jsonObject;
    }
    
    @Override
    public JSONObject todaySaleTop2(String day)
    {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        int totalCreateGroupCount = 0;
        
        int totalCreateOrderCount = 0;
        
        double totalCreateRealPrice = 0;
        
        String s1 = "(SELECT ";
        String s2 =
            " FROM mweb_group_product_info i,mweb_group_product_info_relation_order r,`order` o WHERE i.id = r.mweb_group_product_info_id AND r.order_id = o.id and o.type = 2 AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' and i.mweb_group_product_id=p.mwebGroupProductId AND DATE_FORMAT(o.create_time, '%Y-%m-%d')=DATE_FORMAT('"
                + day + "', '%Y-%m-%d')) ";
        // 订单数
        String c_sql = "count(o.id) as  createOrderCount";
        // 成功订单数
        String c_sq2 = "count(case when o.`status` in(2,3,4) and i.`status`=2 then o.id end ) as successOrderCount";
        // 创建金额
        String c_sq3 = "IFNULL(sum(o.real_price), 0) AS createRealPrice";
        // 成功创建金额
        String c_sq4 = "IFNULL(sum(case when o.`status` in(2,3,4) and i.`status`=2 then o.real_price end ), 0) AS successRealPrice";
        
        StringBuffer sb = new StringBuffer("SELECT p.mwebGroupProductId,p.`name`, count(p.mwebGroupProductId) as createGroupCount,");
        // sb.append(
        // "IF(count(case when i.`status`=2 then i.id end )>0,concat(round((count(case when i.`status`=2 then i.id end
        // )/count(p.mwebGroupProductId) *100),2),'%'),'0.00%') as successRate,");
        sb.append(s1 + c_sql + s2 + "as createOrderCount,");
        // sb.append(s1 + c_sq2 + s2 + "as successOrderCount,");
        sb.append(s1 + c_sq3 + s2 + "as createRealPrice ");
        // sb.append(s1 + c_sq4 + s2 + "as successRealPrice ");
        
        sb.append(" FROM (SELECT p.id AS mwebGroupProductId,p.`name` FROM mweb_group_product p) p,");
        sb.append("(SELECT i.id,i.`status`,i.mweb_group_product_id,DATE_FORMAT(i.create_time, '%Y-%m-%d') as createTime FROM mweb_group_product_info i ) i");
        sb.append(" where p.mwebGroupProductId=i.mweb_group_product_id and DATE_FORMAT(i.createTime, '%Y-%m-%d') = DATE_FORMAT('" + day
            + "', '%Y-%m-%d') GROUP BY p.mwebGroupProductId ORDER BY count(p.mwebGroupProductId) desc");
            
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sb.toString());
        List<JSONObject> list = wechatGroupDataDao.customSql(parameter);
        
        for (Iterator<JSONObject> it = list.iterator(); it.hasNext();)
        {
            JSONObject jdata = new JSONObject();
            JSONObject j = it.next();
            String mwebGroupProductId = j.getString("mwebGroupProductId");
            String name = j.getString("name");
            int createGroupCount = j.getInteger("createGroupCount");
            // int successGroupCount = j.getInteger("successGroupCount");
            int createOrderCount = j.getInteger("createOrderCount");
            float createRealPrice = j.getFloat("createRealPrice");
            // int successOrderCount = j.getInteger("successOrderCount");
            // float successRealPrice = j.getFloat("successRealPrice");
            // String successRate = j.getString("successRate");
            
            jdata.put("mwebGroupProductId", mwebGroupProductId);
            jdata.put("name", name);
            jdata.put("createGroupCount", createGroupCount + "");
            // jdata.put("successGroupCount", successGroupCount + "");
            jdata.put("createOrderCount", createOrderCount + "");
            jdata.put("createRealPrice", numberFormat.format(createRealPrice));
            // jdata.put("successOrderCount", successOrderCount + "");
            // jdata.put("successRealPrice", numberFormat.format(successRealPrice));
            // jdata.put("successRate", successRate);
            
            totalCreateGroupCount += createGroupCount;
            // totalSuccessGroupCount += successGroupCount;
            totalCreateOrderCount += createOrderCount;
            // totalSuccessOrderCount += successOrderCount;
            totalCreateRealPrice += createRealPrice;
            // totalSuccessRealPrice += successRealPrice;
            array.add(jdata);
            
        }
        
        jsonObject.put("list", array);
        jsonObject.put("totalProductCount", array.size() + "");
        jsonObject.put("totalCreateGroupCount", totalCreateGroupCount + "");
        
        jsonObject.put("totalCreateOrderCount", totalCreateOrderCount + "");
        
        jsonObject.put("totalCreateRealPrice", totalCreateRealPrice);
        
        return jsonObject;
    }
    
    @Override
    public JSONObject saleLineData(String day)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        String sql =
            "SELECT DATE_FORMAT(o.create_time, '%H') as h, sum(o.real_price) AS s FROM `order` o WHERE o.type = 2 AND o.is_group = 1 AND o.pay_time != '0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('"
                + day + "', '%Y-%m-%d') GROUP  BY DATE_FORMAT(o.create_time, '%H')";
        JSONObject parameter = new JSONObject();
        parameter.put("sql", sql);
        List<JSONObject> groupByDay = wechatGroupDataDao.customSql(parameter);
        JSONObject jc = new JSONObject();
        for (Iterator<JSONObject> it = groupByDay.iterator(); it.hasNext();)
        {
            JSONObject j = it.next();
            String h = j.getString("h");
            float s = j.getFloat("s");
            jc.put(h, s);
        }
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        float s = 0;
        for (int i = 0; i <= 24; i++)
        {
            if (i < 10)
            {
                if (jc.containsKey("0" + i))
                {
                    array.add(jc.getString("0" + i));
                    s += jc.getFloat("0" + i);
                }
                else
                {
                    array.add("0");
                }
            }
            else
            {
                if (jc.containsKey(String.valueOf(i)))
                {
                    array.add(jc.getString(String.valueOf(i)));
                    s += jc.getFloat(String.valueOf(i));
                }
                else
                {
                    array.add("0");
                }
            }
            
        }
        jsonObject.put("array", array);
        jsonObject.put("s", numberFormat.format(s));
        return jsonObject;
    }
    
}
