package com.ygg.admin.util;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("deprecation")
public class ESBuilderUtil
{
    // ES游标存活时间：30秒
    public static final long SCROLL_KEEP_ALIVE_TIME = 30000;
    public static final int DEFAULT_SCROLL_RESULTSET_SIZE = 2000;
    // 结果集大小。默认ES只会返回10条
     //public static final int DEFAULT_RESULTSET_SIZE = Integer.MAX_VALUE;
    /** 500万条 */
    public static final int DEFAULT_RESULTSET_SIZE = 5000000;
    
    // 数值匹配模式
    public static final int NUMBER_TYPE = 1;
    // 时间匹配模式
    public static final int TIME_TYPE = 2;
    // 生日匹配模式
    public static final int BIRTHDAY_TYPE = 3;
    
    // 包含还是排除
    public static final String INCLUDE_PREFIX = "incldue";
    // 开始
    public static final String START_PREFIX = "start";
    // 结束
    public static final String END_PREFIX = "end";
    
    /**
     * 找出启用的字段
     * @param param
     * @param keys
     */
    public static boolean isUse(HashMap<String, String> param, String useKey) {
        if(StringUtils.equals("on", param.get(useKey)))
            return true;
        return false;
    }
    
    /**
     * 取数字的值
     * @param param
     * @param key
     * @return
     */
    public static Number getNumberValue(HashMap<String, String> param, String key) {
        String value = param.get(key);
        value = StringUtils.defaultIfEmpty(value, "0");
        return NumberUtils.createNumber(value);
    }
    
    /**
     * 取时间的值
     * @param param
     * @param key
     * @return
     */
    public static long getTimestampValue(HashMap<String, String> param, String key, int offert) {
        String value = param.get(key);
        if(StringUtils.isBlank(value))
            return System.currentTimeMillis() / 1000;
        
        Calendar ins = Calendar.getInstance();
        ins.set(Calendar.YEAR, Integer.valueOf(StringUtils.substring(value, 0, 4)));
        ins.set(Calendar.MONTH, Integer.valueOf(StringUtils.substring(value, 5, 7)) - 1);
        ins.set(Calendar.DAY_OF_MONTH, Integer.valueOf(StringUtils.substring(value, 8, 10)) + offert);
        ins.set(Calendar.HOUR_OF_DAY, 0);
        ins.set(Calendar.MINUTE, 0);
        ins.set(Calendar.SECOND, 0);
        
        return ins.getTime().getTime() / 1000;
    }
    
    /**
     * 取数字的值
     * @param param
     * @param key
     * @return
     */
    public static Number getBrithdayValue(HashMap<String, String> param, String key) {
        String value = param.get(key);
        if(StringUtils.isBlank(value))
            return 0;
        
        String birthday = StringUtils.substring(value, 0, 2) + StringUtils.substring(value, 3, 5);
        birthday = StringUtils.indexOf(birthday, "0") == 0 ? StringUtils.substring(birthday, 1) : birthday;
        return Integer.valueOf(birthday);
    }
}
