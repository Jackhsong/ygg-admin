package com.ygg.admin.util;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@SuppressWarnings("deprecation")
public class ESQueryBuilderUtil extends ESBuilderUtil
{
    public static QueryBuilder defaultBuilder(HashMap<String, String> param, String useKey, String field, int type) {
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
           return numberRangeQueryBuilder(param, field, includeKey, startKey, endKey);
       if(TIME_TYPE == type)
           return timeRangeQueryBuilder(param, field, includeKey, startKey, endKey);
       if(BIRTHDAY_TYPE == type)
           return BirthdayRangeQueryBuilder(param, field, includeKey, startKey, endKey);
       
       return QueryBuilders.matchAllQuery();
    }
    
    /**
     * 满足conditions中的条件。相当于SQL中的<code>in(xxx,xxx,xxx)</code>函数
     * @param param
     * @param useKey 在页面上form表单中的属性名
     * @param field 在ES索引中的字段名称
     * @param conditions 条件
     * @return
     */
    public static QueryBuilder inQueryBuilder(HashMap<String, String> param, String useKey, String field, String[] conditions) {
        if(! isUse(param, useKey)) 
            return null;
        if(conditions == null || conditions.length == 0)
            return null;
        // 查询字段
        String column = StringUtils.substring(useKey, 3);
        // 包含、排除key
        String includeKey = INCLUDE_PREFIX + column;
        
        if(StringUtils.equals("0", param.get(includeKey))) {
            return QueryBuilders.boolQuery().mustNot(QueryBuilders.inQuery(field, conditions));
        } else {
            return QueryBuilders.boolQuery().must(QueryBuilders.inQuery(field, conditions));
        }
    }
    
    /**
     * 时间范围过滤条件
     * @param param
     * @param column 查询的字段
     * @param includeKey 
     * @return
     */
    public static QueryBuilder BirthdayRangeQueryBuilder(HashMap<String, String> param, String column, String includeKey, String startKey, String endKey) {
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
    public static QueryBuilder timeRangeQueryBuilder(HashMap<String, String> param, String column, String includeKey, String startKey, String endKey) {
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
    public static QueryBuilder numberRangeQueryBuilder(HashMap<String, String> param, String field, String includeKey, String startKey, String endKey) {
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
    public static QueryBuilder parseExcludeBirthdayBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        QueryBuilder rangeFilterBuilder = QueryBuilders.rangeQuery(field)
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
    public static QueryBuilder parseIncludeBirthdayBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        QueryBuilder rangeFilterBuilder = QueryBuilders.rangeQuery(field)
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
    public static QueryBuilder parseExcludeTimeBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        QueryBuilder rangeFilterBuilder =  QueryBuilders.rangeQuery(field)
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
    public static QueryBuilder parseIncludeTimeBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        QueryBuilder rangeFilterBuilder = QueryBuilders.rangeQuery(field)
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
    public static QueryBuilder parseExcludeNumberBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        QueryBuilder rangeFilterBuilder = QueryBuilders.rangeQuery(field)
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
    public static QueryBuilder parseIncludeNumberBuilder(HashMap<String, String> param, String field, String startKey, String endKey) {
        QueryBuilder rangeFilterBuilder = QueryBuilders.rangeQuery(field)
            .from(getNumberValue(param, startKey))
            .to(getNumberValue(param, endKey));
        return rangeFilterBuilder;
    }
    
}
