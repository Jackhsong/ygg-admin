package com.ygg.admin.util;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;

public class ESFilterBuilderUtil extends ESBuilderUtil
{
    /**
     * 基本匹配模式。请求参数格式：是否启用查询条件 ----- 包含、排除 ----- 开始值 -- 结束值
     * @param param
     * @param useKey 在页面上form表单中的属性名
     * @param field 在ES索引中的字段名称
     * @param type 1数字类型，2时间类型
     */
    public static FilterBuilder defaultBuilder(HashMap<String, String> param, String useKey, String field, int type) {
        // 启用的过滤条件
        if(! isUse(param, useKey)) 
            return null;
        // 查询字段
        String column = StringUtils.substring(useKey, 3);
        // 包含、排除key
        String includeKey = INCLUDE_PREFIX + column;
        String startKey = START_PREFIX + column;
        String endKey = END_PREFIX + column;
        
        if(NUMBER_TYPE == type)
            return numberRangeFilterBuilder(param, field, includeKey, startKey, endKey);
        if(TIME_TYPE == type)
            return timeRangeFilterBuilder(param, field, includeKey, startKey, endKey);
        if(BIRTHDAY_TYPE == type)
            return BirthdayRangeFilterBuilder(param, field, includeKey, startKey, endKey);
        
        return FilterBuilders.matchAllFilter();
    }
    
    /**
     * 满足conditions中的条件。相当于SQL中的<code>in(xxx,xxx,xxx)</code>函数
     * @param param
     * @param useKey 在页面上form表单中的属性名
     * @param field 在ES索引中的字段名称
     * @param conditions 条件
     * @return
     */
    public static FilterBuilder inFilterBuilder(HashMap<String, String> param, String useKey, String field, String[] conditions) {
        if(! isUse(param, useKey)) 
            return null;
        if(conditions == null || conditions.length == 0)
            return null;
        // 查询字段
        String column = StringUtils.substring(useKey, 3);
        // 包含、排除key
        String includeKey = INCLUDE_PREFIX + column;
        
        if(StringUtils.equals("0", param.get(includeKey))) {
            return FilterBuilders.boolFilter().mustNot(FilterBuilders.inFilter(field, conditions));
        } else {
            return FilterBuilders.inFilter(field, conditions);
        }
    }
    
    /**
     * 时间范围过滤条件
     * @param param
     * @param column 查询的字段
     * @param includeKey 
     * @return
     */
    public static RangeFilterBuilder BirthdayRangeFilterBuilder(HashMap<String, String> param, String column, String includeKey, String startKey, String endKey) {
        // 排除：0
        if(StringUtils.equals("0", param.get(includeKey))) 
            return parseExcludeBirthdayBuilder(param, column, startKey, endKey);
        
        // 包含：1，默认就是包含
        return parseIncludeBirthdayBuilder(param, column, startKey, endKey);
    }
    
    /**
     * 时间范围过滤条件
     * @param param
     * @param column 查询的字段
     * @param includeKey 
     * @return
     */
    public static RangeFilterBuilder timeRangeFilterBuilder(HashMap<String, String> param, String column, String includeKey, String startKey, String endKey) {
        // 排除：0
        if(StringUtils.equals("0", param.get(includeKey))) 
            return parseExcludeTimeBuilder(param, column, startKey, endKey);
        
        // 包含：1，默认就是包含
        return parseIncludeTimeBuilder(param, column, startKey, endKey);
    }
    
    /**
     * 数值范围过滤条件
     * @param param
     * @param field 查询的字段
     * @param includeKey 
     * @return
     */
    public static RangeFilterBuilder numberRangeFilterBuilder(HashMap<String, String> param, String field, String includeKey, String startKey, String endKey) {
        // 排除：0
        if(StringUtils.equals("0", param.get(includeKey))) 
            return parseExcludeNumberBuilder(param, field, startKey, endKey);
        
        // 包含：1，默认就是包含
        return parseIncludeNumberBuilder(param, field, startKey, endKey);
    }
    
    /**
     * 解析<strong> 生日 </strong>排除的过滤条件
     * @param param
     * @param field
     * @return
     */
    public static RangeFilterBuilder parseExcludeBirthdayBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(field)
            .lt(getBrithdayValue(param, startKey))
            .gt(getBrithdayValue(param, endKey));
        return rangeFilterBuilder;
    }
    
    /**
     * 解析<strong> 生日 </strong>包含的过滤条件
     * @param param
     * @param field
     * @return
     */
    public static RangeFilterBuilder parseIncludeBirthdayBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(field)
            .from(getBrithdayValue(param, startKey))
            .to(getBrithdayValue(param, endKey));
        return rangeFilterBuilder;
    }
    
    /**
     * 解析<strong> 时间 </strong>排除的过滤条件
     * @param param
     * @param field
     * @return
     */
    public static RangeFilterBuilder parseExcludeTimeBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(field)
            .lt(getTimestampValue(param, startKey, 0))
            .gt(getTimestampValue(param, endKey, 1));
        return rangeFilterBuilder;
    }
    
    /**
     * 解析<strong> 时间 </strong>包含的过滤条件
     * @param param
     * @param field
     * @return
     */
    public static RangeFilterBuilder parseIncludeTimeBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(field)
            .from(getTimestampValue(param, startKey, 0))
            .to(getTimestampValue(param, endKey, 1));
        return rangeFilterBuilder;
    }
    
    /**
     * 解析<strong> 数字 </strong>排除的过滤条件
     * @param param
     * @param field
     * @return
     */
    public static RangeFilterBuilder parseExcludeNumberBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(field)
            .lt(getNumberValue(param, startKey))
            .gt(getNumberValue(param, endKey));
        return rangeFilterBuilder;
    }
    
    /**
     * 解析<strong> 数字 </strong>包含的过滤条件
     * @param param
     * @param field
     * @return
     */
    public static RangeFilterBuilder parseIncludeNumberBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        RangeFilterBuilder rangeFilterBuilder = FilterBuilders.rangeFilter(field)
            .from(getNumberValue(param, startKey))
            .to(getNumberValue(param, endKey));
        return rangeFilterBuilder;
    }
    
}
