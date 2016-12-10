package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.CrmDao;
import com.ygg.admin.entity.CrmDetailEntity;
import com.ygg.admin.sdk.montnets.common.EnvironmentTypeEnum;
import com.ygg.admin.service.CrmService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ESBuilderUtil;
import com.ygg.admin.util.ESClient;
import com.ygg.admin.util.ESFilterBuilderUtil;
import com.ygg.admin.util.ESQueryBuilderUtil;
import com.ygg.admin.util.MontnetsGGJCRMUtil;

@Service("crmService")
@SuppressWarnings("deprecation")
public class CrmServiceImpl implements CrmService
{
    
    // 索引名称
    private static final String INDEX = "crm";
    // 索引类型
    private static final String ACCOUNT_TYPE = "account";
    private static final String ORDER_TYPE = "order";
    private static final String COUPON_TYPE = "coupon";
    
    private static final Map<String, String> ACCOUNTID_MOBILENUMBER = new HashMap<String, String>(3000000);
    
    @Resource
    private CrmDao crmDao;
    
    @Override
    public Map<String, Object> filterByParam(HashMap<String, String> param, String[] level, String[] commentLevel,
        String[] province, String[] saleFlag, String[] brand) throws Exception
    {
        long s1 = System.currentTimeMillis();
        Set<String> set1 = null;
        BoolQueryBuilder accountQueryBuilder = accountQueryBuilders(param, level, province);
        if(accountQueryBuilder != null) {
            // 这里只取id，也就是accountid
            SearchResponse response = execute(param, INDEX, ACCOUNT_TYPE, accountQueryBuilder, "id");
            set1 = getIds(response);
            
            // 查询了。但是没有满足条件数据
            if(set1 == null || set1.size() < 1)
                return createEmpty();
        }
        
        // accountResult结果集拿到   优惠劵过滤器   中作为条件
        Set<String> set2 = null;
        BoolQueryBuilder couponQueryBuilder = couponQueryBuilders(param);
        // 说明需要按照这个条件查询
        if(couponQueryBuilder != null) {
            // 这里只取accountid
            SearchResponse response = execute(param, INDEX, COUPON_TYPE, couponQueryBuilder, "accountId");
            set2 = getValueFromResponse(response, "accountId");
            
            // 查询了。但是没有满足条件数据
            if(set2 == null || set2.size() < 1)
                return createEmpty();
        }
        
        // 求交集
        Set<String> result = intersectionOperation(set1, set2);
        
        // couponFilterBuilder结果集拿到  订单过滤器   中作为条件
        Set<String> set3 = null;
        BoolQueryBuilder orderQueryBuilder = orderQuery(param, commentLevel, saleFlag, brand);
        if(orderQueryBuilder != null) {
            // 这里只取accountId
            SearchResponse response = execute(param, INDEX, ORDER_TYPE, orderQueryBuilder, "accountId");
            set3 = getValueFromResponse(response, "accountId");
            // 查询了。但是没有满足条件数据
            if(set3 == null || set3.size() < 1)
                return createEmpty();
        }
        
        // 如果此时result是null，说明没有使用上面2个过滤条件
        if(result == null || result.isEmpty()) 
            result = set3;
        else 
            result = intersectionOperation(result, set3);
        
        // accountIds结果集拿到     指定时间内订单内分组统计   作为条件
        result = intersectionOperation(result, getLastConsumerExecuteResult(param, INDEX, ORDER_TYPE));
        result = intersectionOperation(result, getTotalOrderInTimeExecuteResult(param, INDEX, ORDER_TYPE));
        result = intersectionOperation(result, getTotalCustomMoneyInTimeExecuteResult(param, INDEX, ORDER_TYPE));
        
        System.out.println("过滤的时间：" + (System.currentTimeMillis() - s1));
        
        
        // 查询了。但是没有满足条件数据
        if(result == null || result.size() < 1)
            return createEmpty();
        
        long s = System.currentTimeMillis();
        Map<String, Object> t = getPhoneNumber(result);
        System.out.println("取电话号码的时间：" + (System.currentTimeMillis() - s));
        return t;
    }
    
    /**
     * 对结果取交集
     * @param arg1
     * @param arg2
     * @return
     */
    private Set<String> intersectionOperation(Set<String> arg1, Set<String> arg2) {
        if(arg1 == null || arg1.size() < 1)
            return arg2;
        
        if(arg2 == null || arg2.size() < 1)
            return arg1;
        
        arg1.retainAll(arg2);
        return arg1;
    }
    
    public void cacheMobileNumber() {
        System.out.println("重做缓存");
        ACCOUNTID_MOBILENUMBER.clear();
        Client client = ESClient.getInstance().getClient();
        SearchResponse response = client
            .prepareSearch(INDEX)
            .setTypes(ACCOUNT_TYPE)
            .setSearchType(SearchType.SCAN)
            .setScroll(new TimeValue(ESBuilderUtil.SCROLL_KEEP_ALIVE_TIME))
            .setSize(ESBuilderUtil.DEFAULT_SCROLL_RESULTSET_SIZE)
            .addFields("id", "mobileNumber")
            .setQuery(QueryBuilders.matchAllQuery())
            .execute().actionGet();
        
        while (true) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                if(searchHit.getFields() == null) {
                    System.out.println("");
                }
                
                // 非法值
                String id = searchHit.getId();
                if(StringUtils.equals(id, "_sum"))
                    continue;
                
                String mobileNumber = searchHit.getFields().get("mobileNumber").getValue();
                
                if(! StringUtils.isBlank(id))
                    ACCOUNTID_MOBILENUMBER.put(id, mobileNumber);
            }
            response = client.prepareSearchScroll(response.getScrollId())
                    .setScroll(new TimeValue(ESBuilderUtil.SCROLL_KEEP_ALIVE_TIME)).execute().actionGet();
            if (response.getHits().getHits().length == 0) {
                break;
            }
        }
        System.out.println("重做缓存结束");
    }
    
    private Map<String, Object> getPhoneNumber(Set<String> accountIds) {
        
        if(ACCOUNTID_MOBILENUMBER.isEmpty())
            cacheMobileNumber();
        
        if(accountIds == null || accountIds.size() < 1)
            return createEmpty();
        
        int phoneCount = 0;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(accountIds.size());
        for (String key : accountIds) {
           
            String moblieNumber = ACCOUNTID_MOBILENUMBER.get(key);
            if(StringUtils.isNotBlank(moblieNumber))
                phoneCount ++;
            
            Map<String, String> m = new HashMap<String, String>();
            m.put(key, moblieNumber);
            list.add(m);
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        int accountCount = accountIds.size();
        result.put("accountCount", accountCount);
        result.put("phoneCount", phoneCount);
        result.put("data", list);
        return result;
    }
    
    
    private Map<String, Object> createEmpty() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("accountCount", 0);
        result.put("data", "");
        result.put("phoneCount", 0);
        return result;
    }
    
    /**
     * 在指定时间内订单内分组统计。并执行查询
     * 
     * @param param
     * @param index
     * @param type
     * @param ids
     * @param builder 分组的条件
     * @return
     */
    private SearchResponse getIntimeOrderGroupByBuilderResponse(HashMap<String, String> param, String index, String type, BoolQueryBuilder queryBuilder, AbstractAggregationBuilder builder) {

        BoolQueryBuilder query = addQueryBuilder(queryBuilder, getProductLine(param));
        
        System.out.println(ESClient.getInstance().getClient()
            .prepareSearch(index)
            .setTypes(type)
            //.setSize(0)
            .setQuery(query)
            .addAggregation(builder));
        
        SearchResponse sr = ESClient.getInstance().getClient()
            .prepareSearch(index)
            .setTypes(type)
            //.setSize(0)
            //.setSearchType(SearchType.SCAN)
            //.setScroll(new TimeValue(ESFilterBuilderUtil.SCROLL_KEEP_ALIVE_TIME))
            .setQuery(query)
            .addAggregation(builder)
            .execute().actionGet();
        
        return sr;
    }
    
    /**
     * 最近一次消费。
     * 
     * @param param
     * @param index
     * @param type
     * @param ids
     * @return
     */
    private Set<String> getLastConsumerExecuteResult(HashMap<String, String> param, String index, String type) {
        // 不启用该条件查询
        if(! ESQueryBuilderUtil.isUse(param, "useLastConsumer"))
            return null;
        
        // 最近一次消费，说明在这段时间中有订单
        //BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
        //    .must(ESQueryBuilderUtil.defaultBuilder(param, "useLastConsumer", "consumeTime", ESFilterBuilderUtil.TIME_TYPE));
        
        // 对accountId分组
        TermsBuilder builder = AggregationBuilders.terms("1").field("accountId")
                                                            .size(ESBuilderUtil.DEFAULT_RESULTSET_SIZE)
                                                            .subAggregation(AggregationBuilders.max("2").field("consumeTime"));
        // 订单在一段时间内分组统计
        SearchResponse response = getIntimeOrderGroupByBuilderResponse(param, index, type, null, builder);
        
        long min = ESBuilderUtil.getTimestampValue(param, "startLastConsumer", 0);
        long max = ESBuilderUtil.getTimestampValue(param, "endLastConsumer", 1);
        
        int include = Integer.valueOf(param.get("includeLastConsumer").toString());
        
        
        
        if(response == null)
            return null;
        
        Set<String> set = new HashSet<String>();
        // accountId分组后的结果
        Terms byAccountId = response.getAggregations().get("1");
        for (Bucket accountBucket : byAccountId.getBuckets()) {
            // accountId、orderId分组后的结果
            InternalMax maxTime = accountBucket.getAggregations().get("2");
            long v = Double.valueOf(maxTime.getValue()).longValue();
            //包含
            if(include == 1) {
                if(v > min && v < max)
                    // 满足条件的accountId
                    set.add(accountBucket.getKey());
            } else {
                //排除
                if(v < min || v > max)
                    // 满足条件的accountId
                    set.add(accountBucket.getKey());
            }
        }
            
        return set;
    }
    
    /**
     * 时段内消费金额
     * @param param
     * @param index
     * @param type
     * @param ids
     * @return
     */
    private Set<String> getTotalCustomMoneyInTimeExecuteResult(HashMap<String, String> param, String index, String type) {
        // 不启用该条件查询
        if(! ESQueryBuilderUtil.isUse(param, "useTotalCustomMoneyInTime"))
            return null;
        
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
            .must(ESQueryBuilderUtil.defaultBuilder(param, "useTotalCustomMoneyInTime", "consumeTime", ESFilterBuilderUtil.TIME_TYPE));
        
        // 根据accountId,orderId分组
        TermsBuilder builder = AggregationBuilders.terms("1").field("accountId")
                                                                .size(ESBuilderUtil.DEFAULT_RESULTSET_SIZE)
                                                                .subAggregation(AggregationBuilders.terms("2").field("orderId")
                                                                // 分组后，对money求和
                                                                .subAggregation(AggregationBuilders.sum("3").field("money")));
        
        // 订单在一段时间内分组统计
        SearchResponse response = getIntimeOrderGroupByBuilderResponse(param, index, type, queryBuilder, builder);
        
        Double min = Double.valueOf(param.get("startTotalCountCustomMoneyInTime").toString());
        Double max = Double.valueOf(param.get("endTotalCountCustomMoneyInTime").toString());

        int include = Integer.valueOf(param.get("includeTotalOrderInTime").toString());
        
        Terms byAccountId = response.getAggregations().get("1");
        Set<String> set = new HashSet<String>();
        for (Bucket accountBucket : byAccountId.getBuckets()) {
            // accountId、orderId分组后的结果
            LongTerms byOrderId = accountBucket.getAggregations().get("2");
            for (Bucket orderBucket : byOrderId.getBuckets())
            {
                Sum m3 = orderBucket.getAggregations().get("3");
                Double money = m3.getValue();
                //包含
                if(include == 1) {
                    if(money.compareTo(min) >= 0 && money.compareTo(max) <= 0)
                        // 满足条件的accountId
                        set.add(accountBucket.getKey());
                } else {
                  //排除
                    if(money.compareTo(min) < 0 || money.compareTo(max) > 0)
                        // 满足条件的accountId
                        set.add(accountBucket.getKey());
                }
            }
        }
            
        return set;
    }
    
    /**
     * 时段内付款订单数
     * @param param
     * @param index
     * @param type
     * @param ids
     * @return
     */
    private Set<String> getTotalOrderInTimeExecuteResult(HashMap<String, String> param, String index, String type) {
        // 不启用该条件查询
        if(! ESQueryBuilderUtil.isUse(param, "useTotalOrderInTime"))
            return null;
        
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
            .must(ESQueryBuilderUtil.defaultBuilder(param, "useTotalOrderInTime", "consumeTime", ESFilterBuilderUtil.TIME_TYPE));
        
        // 根据accountId,orderId分组
        TermsBuilder builder = AggregationBuilders.terms("1").field("accountId")
                                                            .size(ESBuilderUtil.DEFAULT_RESULTSET_SIZE)
                                                            .subAggregation(AggregationBuilders.terms("2").field("orderId"));
        
        // 订单在一段时间内分组统计
        SearchResponse sr = getIntimeOrderGroupByBuilderResponse(param, index, type, queryBuilder, builder);
        
        long min = Long.valueOf(param.get("startTotalCountOrderInTime").toString());
        long max = Long.valueOf(param.get("endTotalCountOrderInTime").toString());
        
        int include = Integer.valueOf(param.get("includeTotalOrderInTime").toString());
        
        // accountId分组后的结果
        Terms byAccountId = sr.getAggregations().get("1");
        Set<String> set = new HashSet<String>(byAccountId.getBuckets().size());
        Set<String> temp = new HashSet<String>();
        for (Bucket accountBucket : byAccountId.getBuckets()) {
            // accountId、orderId分组后的结果
            LongTerms byOrderId = accountBucket.getAggregations().get("2");
            for (Bucket orderBucket : byOrderId.getBuckets()) {
                // 对accountId，orderId分组后。
                // 统计在accountId 这个Bucket中，有多少个不同的orderId，一个orderId对应一笔订单，
                temp.add(orderBucket.getKey());
            }
            //包含
            if(include == 1) {
                if(temp.size() >= min && temp.size() <= max) {
                    // 满足条件的accountId
                    set.add(accountBucket.getKey());
                }
            } else {
              //排除
                if(temp.size() < min || temp.size() > max) {
                    // 满足条件的accountId
                    set.add(accountBucket.getKey());
                }
            }
            // 清空所有的orderId
            temp.clear();
        }
       // System.out.println(set);
        return set;
    }
    
    /**
     * 执行简单的普通的过滤器
     * 
     * @param index
     * @param type
     * @param queryBuilder
     * @param field
     * @return
     */
    private SearchResponse execute(HashMap<String, String> param, String index, String type, QueryBuilder queryBuilder, String... field) {
        
        if (queryBuilder == null)
            return null;
        
        BoolQueryBuilder query = addQueryBuilder(queryBuilder, getProductLine(param));
        System.out.println(query);
        return ESClient.getInstance().getClient()
            .prepareSearch(index)
            .setTypes(type)
            .setSearchType(SearchType.SCAN)
            .setScroll(new TimeValue(ESBuilderUtil.SCROLL_KEEP_ALIVE_TIME))
            .addFields(field)
            .setQuery(query)
            //.setExplain(true) // 打分，不需要
            .setSize(ESBuilderUtil.DEFAULT_SCROLL_RESULTSET_SIZE)
            .execute()
            .actionGet();
    }
    
    /**
     * 读取大量的返回结果
     * @param response
     * @return
     */
    private Set<String> getIds(SearchResponse response) {
        if(response == null)
            return null;
        long s = System.currentTimeMillis();
        Set<String> ids = new HashSet<String>((int)response.getHits().getTotalHits());
        while (true) {
            for (SearchHit searchHit : response.getHits().getHits()) {
                String id = searchHit.getId();
                if(! StringUtils.isBlank(id))
                    ids.add(id);
            }
            response = ESClient.getInstance().getClient().prepareSearchScroll(response.getScrollId())
                    .setScroll(new TimeValue(ESBuilderUtil.SCROLL_KEEP_ALIVE_TIME)).execute().actionGet();
            if (response.getHits().getHits().length == 0) {
                break;
            }
        }
        System.out.println("取iD时间：" + (System.currentTimeMillis() - s));
        return ids;
    }
    
    private Set<String> getValueFromResponse(SearchResponse response, String key) {
        if(response == null)
            return null;
        Set<String> values = new HashSet<String>((int)response.getHits().getTotalHits());
        while (true) {
            for (SearchHit searchHit : response.getHits()) {
                String value = String.valueOf(searchHit.getFields().get(key).getValue());
                if(! StringUtils.isBlank(value))
                    values.add(value);
            }
            response = ESClient.getInstance().getClient().prepareSearchScroll(response.getScrollId())
                    .setScroll(new TimeValue(ESBuilderUtil.SCROLL_KEEP_ALIVE_TIME)).execute().actionGet();
            if (response.getHits().getHits().length == 0) {
                break;
            }
        }
        return values;
    }
    
    private QueryBuilder getProductLine(HashMap<String, String> param) {
        QueryBuilder productLineQueryBuilder = null;
        if(StringUtils.equals(param.get("type"), "1"))
            productLineQueryBuilder = ESQueryBuilderUtil.inQueryBuilder(param, "useType", "type", new String[] {"6", "7"});//左岸城堡
        else if(StringUtils.equals(param.get("type"), "2"))
            productLineQueryBuilder = ESQueryBuilderUtil.inQueryBuilder(param, "useType", "type", new String[] {"8"});//左岸城堡

        else
            productLineQueryBuilder = ESQueryBuilderUtil.inQueryBuilder(param, "useType", "type", new String[] {"1","2","3","4","5"});//左岸城堡
        return productLineQueryBuilder;
    }
    
    /**
     * 组装用户类型的ES过滤条件
     * @param keys
     * @return 
     */
    private BoolQueryBuilder accountQueryBuilders(HashMap<String, String> param, String[] levels, String[] province) {
        BoolQueryBuilder boolQueryBuilder = this.addQueryBuilder(
            ESQueryBuilderUtil.defaultBuilder(param, "useCreateTime", "createTime", ESBuilderUtil.TIME_TYPE),//注册时间
            ESQueryBuilderUtil.defaultBuilder(param, "useBirthday", "birthday", ESBuilderUtil.BIRTHDAY_TYPE),//生日
            ESQueryBuilderUtil.defaultBuilder(param, "useAge", "age", ESBuilderUtil.NUMBER_TYPE),//年龄
            ESQueryBuilderUtil.defaultBuilder(param, "usePoint", "point", ESBuilderUtil.NUMBER_TYPE),//积分
            ESQueryBuilderUtil.defaultBuilder(param, "useCustomTransaction", "customerTransaction", ESBuilderUtil.NUMBER_TYPE),//客单价
            ESQueryBuilderUtil.defaultBuilder(param, "useTotalCustomMoney", "totalMoney", ESBuilderUtil.NUMBER_TYPE),//消费总金额
            ESQueryBuilderUtil.defaultBuilder(param, "useTotalOrder", "orderCount", ESBuilderUtil.NUMBER_TYPE),//总订单数
            ESQueryBuilderUtil.inQueryBuilder(param, "useLevel", "level", levels),//会员等级
            ESQueryBuilderUtil.inQueryBuilder(param, "useProvince", "province", province)//省份
        );
        return boolQueryBuilder;
    }
    
    /**
     * 创建过滤器链表
     * @param filterBuilders
     * @return
     */
    private BoolQueryBuilder addQueryBuilder(QueryBuilder ...queryBuilders) {
        BoolQueryBuilder bqb = null;
        for (QueryBuilder queryBuilder : queryBuilders)
        {
            if(bqb != null && queryBuilder != null) {
                bqb.must(queryBuilder);
                continue;
            } else {
                if(queryBuilder != null)
                    bqb = QueryBuilders.boolQuery().must(queryBuilder);
            }
        }
        return bqb;
    }
    
    /**
     * 组装优惠劵类型的ES过滤条件
     * @param param
     */
    private BoolQueryBuilder couponQueryBuilders(HashMap<String, String> param) {
        if(! ESBuilderUtil.isUse(param, "useCoupon")) 
            return null;
        
        BoolQueryBuilder bqb = null;
        
        // 页面提交的参数
        // 全部优惠劵：0
        // 优惠劵id：1
        if(StringUtils.equals("0", param.get("couponType"))) {
            bqb = addQueryBuilder(
                QueryBuilders.rangeQuery("startTime").from(ESBuilderUtil.getTimestampValue(param, "startCoupon", 0)).to(ESBuilderUtil.getTimestampValue(param, "endCoupon", 1))
            );
        } else {
            bqb = addQueryBuilder(
                QueryBuilders.inQuery("couponId", StringUtils.split(param.get("coupon"), ","))
            );
        }
        
        return bqb;
    }
    
    
    
    /**
     * 组装订单类型的ES过滤条件
     * @param keys
     */
    private BoolQueryBuilder orderQuery(HashMap<String, String> param, String[] commentLevel, String[] saleFlag, String[] brand) {
        
        BoolQueryBuilder boolQueryBuilder = this.addQueryBuilder(
            ESQueryBuilderUtil.inQueryBuilder(param, "useBrand", "brandId", brand),// 商品品牌
            ESQueryBuilderUtil.inQueryBuilder(param, "useSaleFlag", "flagId", saleFlag),// 商品国家
            ESQueryBuilderUtil.inQueryBuilder(param, "useProductId", "productId", StringUtils.split(param.get("productId"), ",")),// 商品id
            ESQueryBuilderUtil.inQueryBuilder(param, "useCategory", "categoryFirstId", StringUtils.split(param.get("categoryFirstId"), ",")),// 商品一级分类
            ESQueryBuilderUtil.inQueryBuilder(param, "useCategory", "categorySecondId", StringUtils.split(param.get("categorySecondId"), ",")),// 商品二级分类
            ESQueryBuilderUtil.inQueryBuilder(param, "useCategory", "categoryThirdId", StringUtils.split(param.get("categoryThirdId"), ",")),// 商品三级分类
            ESQueryBuilderUtil.inQueryBuilder(param, "useOrderComment", "level", commentLevel),//评论的等级
            ESQueryBuilderUtil.defaultBuilder(param, "useOrderComment", "commentTime", ESBuilderUtil.TIME_TYPE),//评论的时间段
            ESQueryBuilderUtil.defaultBuilder(param, "useConsumer", "consumeTime", ESBuilderUtil.TIME_TYPE)//消费时间
        );
        
        // 说明以上的这些条件都没有被启用
        if(boolQueryBuilder == null)
            return null;
        
        return boolQueryBuilder;
    }

    @Override
    public int saveFilterResult(Map<String, String> param)
    {
        // 这3个不是查询条件
        String title = param.remove("title");
        String phoneCount = param.remove("phoneCount");
        String accountCount = param.remove("accountCount");
        String data = param.remove("data");
        String conds = JSON.toJSONString(param);
        
        Map<String, Object> groupInfo = new HashMap<String, Object>();
        groupInfo.put("id", 0);
        groupInfo.put("title", title);
        groupInfo.put("phoneCount", phoneCount);
        groupInfo.put("accountCount", accountCount);
        groupInfo.put("conds", conds);
        groupInfo.put("type", param.get("type"));
        
        int result = crmDao.saveAccountGroup(groupInfo);
        if(result < 1)
            throw new RuntimeException("保存筛选结果失败");
        
        
        List<CrmDetailEntity> detailList = new ArrayList<CrmDetailEntity>(100);
        JSONArray detailArray = JSON.parseArray(data);
        for (Object json : detailArray)
        {
            String s = json.toString();
            if(s.length() < 3)
                continue;
            detailList.add(new CrmDetailEntity(s));
        }
        
        
        Map<String, Object> detailInfo = new HashMap<String, Object>();
        detailInfo.put("groupId", groupInfo.get("id"));
        detailInfo.put("list", detailList);
        result = crmDao.saveAccountDetail(detailInfo);
        if(result < 1)
            throw new RuntimeException("保存分组明细失败");
        
        return 1;
    }

    @Override
    public Map<String, Object> findGroupList(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", crmDao.findGroupList(param));
        result.put("total", crmDao.countGroupList(param));
        return result;
    }

    @Override
    public int deleteGroupInfo(int groupId)
        throws Exception
    {
        int result = crmDao.deleteGroupById(groupId);
        if(result < 1)
            throw new RuntimeException("删除用户分组失败");
        result = crmDao.deleteGroupDetailByGroupId(groupId);
        if(result < 1)
            throw new RuntimeException("删除用户分组明细失败");
        return result;
    }

    @Override
    public Map<String, Object> findGroupDetailByGroupId(Map<String, Object> param)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> groupDetailList = crmDao.findGroupDetailList(param);
        if(groupDetailList != null && groupDetailList.size() > 0) {
            List<Object> list = new ArrayList<Object>(groupDetailList.size());
            for (Map<String, Object> groupDetail : groupDetailList)
            {
                list.add(groupDetail.get("accountId"));
            }
            // 合并数据
            List<Map<String, Object>> accountList = crmDao.findGroupDetailFromAccount(list);
            f:for (Map<String, Object> groupDetail : groupDetailList)
            {
                for (Map<String, Object> accountInfo : accountList)
                {
                    if(StringUtils.equals(groupDetail.get("accountId").toString(), accountInfo.get("accountId").toString())) {
                        groupDetail.put("createTime", accountInfo.get("createTime"));
                        groupDetail.put("type", accountInfo.get("type"));
                        groupDetail.put("nickname", accountInfo.get("nickname"));
                        groupDetail.put("name", accountInfo.get("name"));
                        continue f;
                    }
                }
            }
        }
        result.put("rows", groupDetailList);
        result.put("total", crmDao.countGroupDetailList(param));
        return result;
    }

    @Override
    public Object saveSms(String sendType, String phone, String content, int groupId, int contentType, int filterType, String filterDay, String longUrl, String shortUrl, int linkInfoId, String sendTime)
    {
        // 仅有于发送短信测试
        if(StringUtils.equals(sendType, "0")) {
            return sendMassage(phone, content, contentType);
        } else {
            // 仅保存任务，不发送短信
            return crmDao.saveSendMessageTask(groupId, linkInfoId, content, contentType, 
                shortUrl, longUrl, filterType, filterDay, StringUtils.isBlank(sendTime) ? DateTimeUtil.now() : sendTime);
        }
    }
    
    /**
     * 定时任务，每分钟跑一次
     * @return
     */
    public void sendMessageTask() {
        try {
            // 非生产环境，不跑发送短信任务。
            if(StringUtils.equals("0", YggAdminProperties.getInstance().getPropertie("timer_switch")))
                return;
            
//            List<Map<String, Object>> taskList = crmDao.findSendMessageTask();
//            if(taskList != null && !taskList.isEmpty()) {
//                 有发送短信任务
//                new SendMessageThread(taskList).run();
//            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    class SendMessageThread implements Runnable {
        
        private List<Map<String, Object>> taskList;
        
        public SendMessageThread(List<Map<String, Object>> taskList) {
            this.taskList = taskList;
        }

        @Override
        public void run()
        {
            for (Map<String, Object> task : taskList)
            {
                try {
                    // 群发短信
                    int messageId = sendMassage((String)task.get("content"), Integer.valueOf(task.get("groupId").toString()), 
                        Integer.valueOf(task.get("contentType").toString()), Integer.valueOf(task.get("filterType").toString()), task.get("filterDay").toString());
                    
                    int result = crmDao.saveLinkInfo(Integer.valueOf(task.get("linkInfoId").toString()), (String)task.get("longUrl"), (String)task.get("shortUrl"), messageId);
                    
                    if(result < 1)
                        throw new RuntimeException("保存短信营销链接信息失败");
                    result = crmDao.updateMessageTask(Integer.valueOf(task.get("id").toString()));
                    if(result < 1)
                        throw new RuntimeException("保存短信营销链接信息失败");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 测试发送时使用
     * 
     * @param phone
     * @param content
     * @param contentType
     * @return
     */
    private Object sendMassage(String phone, String content, int contentType) {
        if (!CommonUtil.isMobile(phone))
            throw new RuntimeException("手机号码不符合格式");
        send(phone, content, contentType);
        return 1;
    }
    
    /** 左岸城堡 */
    private static final int GGJ = 0;
    /** 左岸城堡 */
    //private static final int GGT = 1;
    /** 左岸城堡
 */
    //private static final int QQBS = 2;
    
    private void send(String phone, String content, int contentSign) {
        if(contentSign == GGJ)
            new MontnetsGGJCRMUtil().sendSms(phone, content);
        //if(contentSign == GGT)
        //    new MontnetsTuanUtil().sendSms(phone, content);
        //if(contentSign == QQBS)
        //    new MontnetsGlobalUtil().sendSms(phone, content);
    }
    
    
    /** 不过滤  */
    private static final int DAY_0 = 0;
    /** 最近24小时  */
    private static final int DAY_1 = 1;
    /** 最近5天  */
    private static final int DAY_5 = 2;
    /** 最近7天  */
    private static final int DAY_7 = 3;
    /** 自定义的  */
    private static final int DAY_OTHER = 4;
    
    private int getDay(int filterSign, String filterDay) {
        switch (filterSign) {
            case DAY_0:
                return 0;
            case DAY_1:
                return 1;
            case DAY_5:
                return 5;
            case DAY_7:
                return 7;
            case DAY_OTHER:
                return Integer.valueOf(filterDay);
            default:
                return 0;
        }
    }
    
    /**
     * 群发短信
     * @param content
     * @param groupId
     * @param contentType
     * @param filterType
     * @return
     */
    private int sendMassage(String content, int groupId, int contentType, int filterType, String filterDay) {
        // 根据用户分组id，查询满足过滤策略的手机号码
        List<String> phones = crmDao.findSendMessagePhone(groupId, getDay(filterType, filterDay));
        // 发送
        if(phones == null || phones.size() < 1)
            throw new RuntimeException("没有找到需要发送短信的电话号码");
        
        List<String> newPhones = new ArrayList<String>(phones.size());
        for (String phone : phones)
        {
            if (CommonUtil.isMobile(phone))
                newPhones.add(phone);
        }
        
        if(newPhones == null || newPhones.size() < 1)
            throw new RuntimeException("没有找到需要发送短信的电话号码");
        
        
        send(newPhones, content, contentType);
        // 保存发送短信记录
        int messageId = crmDao.saveSendMessage(groupId, content, filterType, filterDay, contentType, newPhones.size());
        // 保存发送短信记录明细
        crmDao.saveMessageDetail(messageId, newPhones);
        return messageId;
    }
    
    /**
     * 发送短信
     * @param phones
     * @param content
     * @param contentSign
     */
    private void send(List<String> phones, String content, int contentSign) {
        if(contentSign == GGJ)
            new MontnetsGGJCRMUtil().sendSms(phones, content, EnvironmentTypeEnum.SERVICE.getValue());
        //if(contentSign == GGT)
        //    MontnetsTuanUtil.sendSms(phones, content);
        //if(contentSign == QQBS)
        //    MontnetsGlobalUtil.sendSms(phones, content);
    }

    @Override
    public Map<String, String> generateShortUrl(String url, String groupId) throws Exception
    {
        String callbackUrl = YggAdminProperties.getInstance().getPropertie("domain_name");
        int maxId = crmDao.getMaxId4ShortUrl();
        // 长连接：
        // http://static.gegejia.com/custom/openApp.html?    跳转链接地址
        // url=gegejia://resource/saleProduct/28923/code     APP展示的地址
        // callbackUrl/crm/statistics/id                     回调统计点击次数地址
        String longUrl = url +"&cbUrl=" + callbackUrl + "crm/statistics/" + (maxId + 1);
        // 生成的短链接
        String shortUrl = CommonUtil.generateTCNShortUrl(longUrl);
        
        Map<String, String> res = new HashMap<String, String>();
        res.put("shortUrl", shortUrl);
        res.put("longUrl", longUrl);
        res.put("id", String.valueOf(maxId + 1));
        return res;
    }

    @Override
    public void updateStatistics(String remoteIpAddr, int id)
    {
        crmDao.updateLinkInfo(id);
        crmDao.saveLinkDetail(remoteIpAddr, id);
    }

    @Override
    public Map<String, Object> smsList(Map<String, Object> param)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", crmDao.findSmsList(param));
        result.put("total", crmDao.countSmsList(param));
        return result;
    }

    @Override
    public Map<String, Object> statisticsResult(int id)
    {
        return crmDao.statisticsResult(id);
    }
    
}
