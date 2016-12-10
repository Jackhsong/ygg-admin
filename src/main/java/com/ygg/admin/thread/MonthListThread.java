package com.ygg.admin.thread;

import java.text.NumberFormat;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.dao.WechatGroupDataDao;

/**
 * 
 * @ClassName: MonthListThread
 * @Description: TODO(左岸城堡月度统计线程)
 * @author zero
 * @date 2016年1月12日 下午6:58:14
 *       
 */
public class MonthListThread implements Runnable
{
    
    private String selectDate;
    
    private int createGroupCount = 0;
    
    private int createOrderCount = 0;
    
    private double createRealPrice = 0;
    
    private int successGroupCount = 0;
    
    private double successRealPrice = 0;
    
    private int successOrderCount = 0;
    
    private String successRate = "0.00%";
    
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
    
    public String getSelectDate()
    {
        return selectDate;
    }
    
    public int getCreateGroupCount()
    {
        return createGroupCount;
    }
    
    public MonthListThread(String selectDate, int createGroupCount)
    {
        this.selectDate = selectDate;
        this.createGroupCount = createGroupCount;
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
        // 创建金额与创建订单数
        sb.append(
            "(SELECT count(o.id) as createOrderCount,IFNULL(sum(o.real_price), 0) AS createRealPrice FROM `order` o WHERE o.type=2 AND o.is_group=1 AND o.pay_time !='0000-00-00 00:00:00' ");
        sb.append("AND DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('" + this.selectDate + "', '%Y-%m-%d')) a,");
        
        // 成功拼团数
        sb.append("(SELECT count(i.id) AS successGroupCount FROM mweb_group_product_info i WHERE i.`status` = 2 AND DATE_FORMAT(i.create_time, '%Y-%m-%d') = DATE_FORMAT('"
            + this.selectDate + "', '%Y-%m-%d')) b,");
        // 成功金额
        sb.append(
            "(SELECT IFNULL(sum(o.real_price), 0) AS successRealPrice FROM `order` o WHERE o.type=2 AND o.is_group = 1 AND (o.`status` = 2 OR o.`status` = 3 OR o.`status` = 4) AND  DATE_FORMAT(o.create_time, '%Y-%m-%d') = DATE_FORMAT('"
                + this.selectDate + "', '%Y-%m-%d')) c,");
        // 成功订单数
        sb.append(
            "(SELECT count(o.id) as successOrderCount FROM `order` o WHERE o.type=2 AND o.is_group = 1 AND ( o.`status` = 2 OR o.`status` = 3 OR o.`status` = 4) AND DATE_FORMAT(o.pay_time, '%Y-%m-%d') = DATE_FORMAT('"
                + this.selectDate + "', '%Y-%m-%d')) d");
                
        JSONObject jdata = new JSONObject();
        parameter.put("sql", sb.toString());
        List<JSONObject> list = wechatGroupDataDao.customSql(parameter);
        JSONObject jtemp = list.get(0);
        createOrderCount = jtemp.getIntValue("createOrderCount");
        createRealPrice = jtemp.getDoubleValue("createRealPrice");
        successGroupCount = jtemp.getIntValue("successGroupCount");
        successRealPrice = jtemp.getDoubleValue("successRealPrice");
        successOrderCount = jtemp.getIntValue("successOrderCount");
        
        // 成功率
        if (createGroupCount > 0)
        {
            
            successRate = numberFormat.format((float)successGroupCount / (float)createGroupCount * 100) + "%";
            
        }
        
    }
    
}
