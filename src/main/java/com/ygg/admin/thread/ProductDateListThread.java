package com.ygg.admin.thread;

import java.text.NumberFormat;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.WechatGroupDataDao;

public class ProductDateListThread implements Runnable
{
    private String startTime;
    
    private String endTime;
    
    private int mwebGroupProductId;
    
    private String name;
    
    private int createGroupCount = 0;
    
    private int createOrderCount = 0;
    
    private double createRealPrice = 0;
    
    private int successGroupCount = 0;
    
    private double successRealPrice = 0;
    
    private int successOrderCount = 0;
    
    private String successRate = "0.00%";
    
    public String getStartTime()
    {
        return startTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public int getMwebGroupProductId()
    {
        return mwebGroupProductId;
    }
    
    public int getCreateGroupCount()
    {
        return createGroupCount;
    }
    
    public int getCreateOrderCount()
    {
        return createOrderCount;
    }
    
    public double getCreateRealPrice()
    {
        return createRealPrice;
    }
    
    public int getSuccessGroupCount()
    {
        return successGroupCount;
    }
    
    public double getSuccessRealPrice()
    {
        return successRealPrice;
    }
    
    public int getSuccessOrderCount()
    {
        return successOrderCount;
    }
    
    public String getSuccessRate()
    {
        return successRate;
    }
    
    public String getName()
    {
        return name;
    }
    
    public ProductDateListThread(String startTime, String endTime, int mwebGroupProductId, int createGroupCount, String name)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.mwebGroupProductId = mwebGroupProductId;
        this.createGroupCount = createGroupCount;
        this.name = name;
    }
    
    @Override
    public void run()
    {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        WechatGroupDataDao wechatGroupDataDao = (WechatGroupDataDao)wac.getBean("wechatGroupDataDao");
        
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        JSONObject parameter = new JSONObject();
        
        StringBuffer sb = new StringBuffer("SELECT * FROM ");
        // 创建金额
        sb.append(
            "(SELECT IFNULL(sum(o.real_price), 0) AS createRealPrice FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
                + mwebGroupProductId + " AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startTime
                + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) a,");
        // 创建订单数
        sb.append(
            "(SELECT count(o.id) as createOrderCount FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
                + mwebGroupProductId + " AND o.is_group = 1 AND o.pay_time !='0000-00-00 00:00:00' AND DATE_FORMAT(o.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startTime
                + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) b,");
        // 成功拼团数
        sb.append("(SELECT count(i.id) AS successGroupCount FROM mweb_group_product_info i,mweb_group_product p WHERE i.mweb_group_product_id = p.id AND p.id = "
            + mwebGroupProductId + " AND i.`status` = 2 AND DATE_FORMAT(i.create_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('" + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime
            + "', '%Y-%m-%d')) c,");
        // 成功金额
        sb.append(
            "(SELECT IFNULL(sum(o.real_price), 0) AS successRealPrice FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
                + mwebGroupProductId + " AND o.is_group = 1 AND (o.`status`=2 or o.`status`=3 or o.`status`=4) AND DATE_FORMAT(o.pay_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
                + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) d,");
        // 成功订单数
        sb.append(
            "(SELECT count(o.id) as successOrderCount FROM mweb_group_product p,order_product op,`order` o WHERE p.product_id = op.product_id AND op.order_id = o.id AND o.type = 2 AND p.id = "
                + mwebGroupProductId + " AND o.is_group = 1 AND (o.`status`=2 or o.`status`=3 or o.`status`=4) AND DATE_FORMAT(o.pay_time, '%Y-%m-%d') BETWEEN DATE_FORMAT('"
                + startTime + "', '%Y-%m-%d') AND DATE_FORMAT('" + endTime + "', '%Y-%m-%d')) e");
        parameter.put("sql", sb.toString());
        List<JSONObject> list = wechatGroupDataDao.customSql(parameter);
        JSONObject jtemp = list.get(0);
        createOrderCount = jtemp.getIntValue("createOrderCount");
        createRealPrice = jtemp.getDoubleValue("createRealPrice");
        successGroupCount = jtemp.getIntValue("successGroupCount");
        successRealPrice = jtemp.getDoubleValue("successRealPrice");
        successOrderCount = jtemp.getIntValue("successOrderCount");
        
        // 拼团成功率
        // 成功率
        if (createGroupCount > 0)
        {
            
            successRate = numberFormat.format((float)successGroupCount / (float)createGroupCount * 100) + "%";
            
        }
        
    }
    
}
