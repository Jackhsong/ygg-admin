package com.ygg.admin.service;

import com.alibaba.fastjson.JSONObject;

public interface WechatGroupDataService
{
    /**
     * 
     * @创建人: zero
     * @创建时间: 2015年12月8日 下午2:03:44
     * @描述:
     *      <p>
     *      (团购月度统计)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年12月8日 下午2:03:44
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @return
     * @returnType JSONObject
     * @version V1.0
     */
    public JSONObject monthList(String selectDate);
    
    public JSONObject monthList2(String selectDate);
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2015年12月8日 下午8:28:43
     * @描述:
     *      <p>
     *      (团购商品统计)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年12月8日 下午8:28:43
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param startTime
     * @param endTime
     * @return
     * @returnType JSONObject
     * @version V1.0
     */
    public JSONObject productDateList(String startTime, String endTime);
    
    public JSONObject productDateList2(String startTime, String endTime);
    
    public JSONObject todaySaleTop(String day);
    
    public JSONObject todaySaleTop2(String day);
    
    /**
     * 获得折线图数据
     * 
     * @创建人: zero
     * @创建时间: 2015年12月31日 下午4:51:15
     * @描述:
     *      <p>
     *      (这里用一句话描述这个方法的作用)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年12月31日 下午4:51:15
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param day
     * @return
     * @returnType JSONObject
     * @version V1.0
     */
    public JSONObject saleLineData(String day);
}
