package com.ygg.admin.service.impl;

import com.ygg.admin.cache.CacheManager;
import com.ygg.admin.cache.CacheServiceIF;
import com.ygg.admin.code.*;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.ProductCountEntity;
import com.ygg.admin.service.SearchService;
import com.ygg.admin.util.CacheConstant;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ProductUtil;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zhangyb on 2015/8/24 0024.
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService
{

    private Logger log = Logger.getLogger(SearchServiceImpl.class);

    private CacheServiceIF cache = CacheManager.getClient();

    @Resource
    private SearchDao searchDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private SaleWindowDao saleWindowDao;

    @Resource
    private ActivitiesCommonDao activitiesCommonDao;

    @Resource
    private CustomActivitiesDao customActivitiesDao;

    @Resource
    private SpecialActivityDao specialActivityDao;

    @Resource
    private ActivitySimplifyDao activitySimplifyDao;

    @Resource
    private CategoryDao categoryDao;

    private Client client = null;

    private String index = "index_yangege";

    private String type = "type_product";

    private static List<String> specialProductIdList = Arrays.asList("18158", "20057", "21139");

    @Override
    public Map<String, Object> findAllSearchHotKeyword(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> rows = searchDao.findAllSearchHotKeyword(para);
        int total = 0;
        if (!rows.isEmpty()){
            total = searchDao.countAllSearchHotKeyword(para);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("rows",rows);
        result.put("total",total);
        return result;
    }

    @Override
    public Map<String, Object> saveSearchHotKeyword(int id,String keyword, int sequence)
        throws Exception
    {
        Map<String,Object> para = new HashMap<>();
        para.put("keyword",keyword);
        para.put("sequence",sequence);
        int status = 0 ;
        if(id == 0){
            status = searchDao.saveSearchHotKeyword(para);
        }else{
            para.put("id",id);
            status = searchDao.updateSearchHotKeyword(para);
        }
        Map<String,Object> result  = new HashMap<>();
        result.put("status",status);
        result.put("msg",status == 1?"保存成功":"保存失败");
        return result;
    }

    @Override
    public int deleteSearchHotKeyword(int id)
        throws Exception
    {
        return searchDao.deleteSearchHotKeyword(id);
    }

    @Override public Map<String, Object> findAllSearchRecord(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> rows = searchDao.findAllSearchRecord(para);
        int total = 0;
        if (!rows.isEmpty()){
            total = searchDao.countAllSearchRecord(para);
            for (Map<String, Object> row : rows)
            {
                row.put("createTime", DateTimeUtil.timestampStringToWebString(row.get("createTime")+""));
                row.put("accountId","0".equals(row.get("accountId")+"")?"":row.get("accountId")+"");
                row.put("accountName",row.get("accountName") == null?"":row.get("accountName")+"");
            }
        }
        Map<String,Object> result = new HashMap<>();
        result.put("rows",rows);
        result.put("total",total);
        return result;
    }

    @Override
    public int countAllSearchRecord(Map<String, Object> para)
        throws Exception
    {
        return searchDao.countAllSearchRecord(para);
    }

    @Override
    public int refreshSearchIndex()
        throws Exception
    {
        int status = 1;
        int currentTime = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        //查找所有当前在售特卖
        List<Map<String, Object>> _saleInfo = saleWindowDao.findAllCurrentSaleWindow(currentTime);
        List<Map<String, Object>> saleInfo = new ArrayList<>();
        for (Map<String, Object> it : _saleInfo)
        {
            int _currentTime = Integer.valueOf(it.get("startTime") + "");
            if (_currentTime == currentTime)
            {
                int saleTimeType = Integer.valueOf(it.get("saleTimeType") + "");
                int hour = DateTime.now().getHourOfDay();
                if (hour > 10)
                {
                    if (hour > 20)
                    {
                        saleInfo.add(it);
                    }
                    else
                    {
                        // 10点场加入。
                        if (saleTimeType == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
                        {
                            saleInfo.add(it);
                        }
                    }
                }
            }
            else
            {
                saleInfo.add(it);
            }
        }

        Set<Integer> productIdSet = new HashSet<>();
        for (Map<String, Object> map : saleInfo)
        {
            int displayId = Integer.valueOf(map.get("displayId") == null ? "0" : map.get("displayId") + "").intValue();
            int type = Integer.valueOf(map.get("type") == null ? "0" : map.get("type") + "").intValue();
            if (type == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())//特卖单品
            {
                productIdSet.add(displayId);
            }
            else if (type == SaleWindowEnum.SALE_TYPE.ACTIVITIES_COMMON.getCode())//特卖组合
            {
                List<Integer> productIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(displayId);
                productIdSet.addAll(productIdList);
            }
            else if (type == SaleWindowEnum.SALE_TYPE.WEB_CUSTOM_ACTIVITY.getCode())//自定义特卖
            {
                Map<String, Object> cae = customActivitiesDao.findCustomActivitiesById(displayId);
                int typeCode = Integer.valueOf(cae.get("typeCode") == null ? "0" : cae.get("typeCode") + "").intValue();
                int typeId = Integer.valueOf(cae.get("typeId") == null ? "0" : cae.get("typeId") + "").intValue();
                if (typeCode == CustomEnum.CUSTOM_ACTIVITY_RELATION.SALE_ACTIVITY.getCode())//情景特卖活动
                {
                    List<Map<String, Object>> layoutProductData = specialActivityDao.findSpecialActivityLayouProductBySpecialActivityId(typeId);
                    for (Map<String, Object> it : layoutProductData)
                    {
                        int displayType = Integer.valueOf(it.get("displayType") == null ? "0" : it.get("displayType") + "");
                        int oneType = Integer.valueOf(it.get("oneType") == null ? "0" : it.get("oneType") + "");
                        int oneDisplayId = Integer.valueOf(it.get("oneDisplayId") == null ? "0" : it.get("oneDisplayId") + "");
                        int twoType = Integer.valueOf(it.get("twoType") == null ? "0" : it.get("twoType") + "");
                        int twoDisplayId = Integer.valueOf(it.get("twoDisplayId") == null ? "0" : it.get("twoDisplayId") + "");
                        if (oneType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())//关联特卖单品
                        {
                            productIdSet.add(oneDisplayId);
                        }
                        if (displayType == CustomLayoutStyleTypeEnum.LAYOUT_STYLE_OF_DOUBLE.ordinal())//左右布局
                        {
                            if (twoType == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())//关联特卖单品
                            {
                                productIdSet.add(twoDisplayId);
                            }
                        }
                    }
                }
                else if (typeCode == CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode())//精品特卖活动
                {
                    List<Integer> productIdList = activitySimplifyDao.findActivitySimplifyProductIdBySimplifyActivityId(typeId);
                    productIdSet.addAll(productIdList);
                }
            }
        }

        // 加入其它活动关联的商品IDS
        List<Integer> otherList = ProductUtil.findAllDisplayAndAvailableProductId();
        if (!otherList.isEmpty())
        {
            productIdSet.addAll(otherList);
        }

        Map<String, Object> para = new HashMap<>();
        para.put("idList", new ArrayList<>(productIdSet));

        List<Map<String, Object>> _pinfos = productDao.findAllProductInfoForElasticsearchIndex(para);
        List<Map<String, Object>> pinfos = new ArrayList<>();

        // 过滤，同一基本商品ID关联的特卖or商城只展现一个价格最低的。
        Map<String,List<Map<String, Object>>> _tempMapByKeyProductBaseId = new HashMap<>();
        for (Map<String, Object> pinfo : _pinfos)
        {
            String productBaseId = pinfo.get("productBaseId") + "";
            List<Map<String, Object>> _list = _tempMapByKeyProductBaseId.get(productBaseId);
            if (_list == null)
            {
                _list = new ArrayList<>();
                _tempMapByKeyProductBaseId.put(productBaseId, _list);
            }
            _list.add(pinfo);
        }
        for (Map.Entry<String,List<Map<String, Object>>> e : _tempMapByKeyProductBaseId.entrySet())
        {
            List<Map<String, Object>> value = e.getValue();
            if (value.size() == 1)
            {
                pinfos.add(value.get(0));
            }
            else
            {
                // 选取其中一个价格最低的商品
                double _currSalesPrice = Double.MAX_VALUE;
                Map<String,Object> _currTempMap = null;
                List<Map<String,Object>> _sprcialList = new ArrayList<>();// 特殊商品，加入到索引
                for (Map<String, Object> it : value)
                {
                    double salesPrice = Double.valueOf(it.get("salesPrice") + "");
                    int type = Integer.valueOf(it.get("type") + "");
                    int id = Integer.valueOf(it.get("id") + "");
                    if (salesPrice < _currSalesPrice)
                    {
                        _currTempMap = it;
                        _currSalesPrice = salesPrice;
                    }
                    else if (salesPrice == _currSalesPrice)
                    {
                        int _type = Integer.valueOf(_currTempMap.get("type") + "");
                        int _id = Integer.valueOf(_currTempMap.get("id") + "");

                        // 价格一致 优先库存，其次特卖
                        ProductCountEntity pe = productDao.findProductCountByProductId(id);
                        ProductCountEntity _pe = productDao.findProductCountByProductId(_id);
                        if (pe.getStock() > 0 && _pe.getStock() == 0)
                        {
                            _currTempMap = it;
                            _currSalesPrice = salesPrice;
                        }
                        else if (type == 1 && _type == 2 && pe.getStock() > 0 && (pe.getStock() > pe.getLock()))
                        {
                            _currTempMap = it;
                            _currSalesPrice = salesPrice;
                        }
                        else if (type == 1 && _type == 2)
                        {
                            _currTempMap = it;
                            _currSalesPrice = salesPrice;
                        }
                    }

                    if (specialProductIdList.contains(id))
                    {
                        _sprcialList.add(it);
                    }
                }

                if (_currTempMap != null)
                {
                    pinfos.add(_currTempMap);
                }
                // 特殊商品加入索引
                if (_sprcialList.size() > 0)
                {
                    for (Map<String, Object> _sprcialMap : _sprcialList)
                    {
                        if (!(_currTempMap.get("id").equals(_sprcialMap.get("id"))))
                        {
                            pinfos.add(_sprcialMap);
                        }
                    }
                }
//                System.out.println("_currTempMap: " + _currTempMap);
//                System.out.println("value: " + value);
            }
        }

//        log.info("更新elasticsearch，商品信息：" + pinfos);
        if (!pinfos.isEmpty())
        {
            List<Integer> cacheProductIdList = new ArrayList<>();// 待放入缓存商品ID列表

            // 查询分类信息
            Set<Integer> pbIds = new HashSet<>();
            for (Map<String, Object> map : pinfos)
            {
                String pbid = map.get("productBaseId") + "";
                pbIds.add(Integer.valueOf(pbid));

                cacheProductIdList.add(Integer.valueOf(map.get("id") + ""));
            }
            Map<String,Object> cpara = new HashMap<>();
            cpara.put("idList", new ArrayList<>(pbIds));
            List<Map<String, Object>> cinfos = productDao.findAllProductCategoryInfoByProductBaseIds(cpara);
            Set<Integer> secondCIds = new HashSet<>();
            Set<Integer> thirdCIds = new HashSet<>();
            for (Map<String, Object> it : cinfos)
            {
                Integer cid = Integer.valueOf(it.get("categorySecondId") == null ? "0" : it.get("categorySecondId") + "");
                Integer tid = Integer.valueOf(it.get("categoryThirdId") == null ? "0" : it.get("categoryThirdId") + "");
                secondCIds.add(cid);
                if (tid > 0)
                {
                    thirdCIds.add(tid);
                }
            }
            //查询分类名称
            Map<String,String> secondMap = new HashMap<>(); // 二级分类map
            Map<String,String> thirdMap = new HashMap<>(); // 三级分类map
            if (secondCIds.size() > 0)
            {
                List<Map<String, Object>> secondList = categoryDao.findCategorySecondInfoByIds(new ArrayList<>(secondCIds));
                for (Map<String, Object> map : secondList)
                {
                    String id = map.get("id") + "";
                    String name = map.get("name") + "";
                    secondMap.put(id, name);
                }
            }

            if (thirdCIds.size() > 0)
            {
                List<Map<String, Object>> thirdList = categoryDao.findCategoryThirdInfoByIds(new ArrayList<>(thirdCIds));
                for (Map<String, Object> map : thirdList)
                {
                    String id = map.get("id") + "";
                    String name = map.get("name") + "";
                    thirdMap.put(id, name);
                }
            }

            Map<String,Map<String,Object>> productCatagoryInfoMap = new HashMap<>();
            for (Map<String, Object> it : cinfos)
            {
                String sid = it.get("categorySecondId") + "";
                String tid = it.get("categoryThirdId") + "";
                String pbid = it.get("productBaseId") + "";

                Map<String,Object> category = productCatagoryInfoMap.get(pbid);
                if (category == null)
                {
                    category = new HashMap<>();
                    productCatagoryInfoMap.put(pbid, category);
                }
                String secondCategory = category.get("secondCategory") == null ? "" : category.get("secondCategory") + "";
                String thirdCategory = category.get("thirdCategory") == null ? "" : category.get("thirdCategory") + "";

                String currSecCateName = secondMap.get(sid) == null ? "" : secondMap.get(sid);
                String currThiCateName = thirdMap.get(tid) == null ? "" : thirdMap.get(tid);
                if (!"".equals(currSecCateName))
                {
                    secondCategory = secondCategory + "x" + currSecCateName + "x";
                    category.put("secondCategory", secondCategory);
                }
                if (!"".equals(currThiCateName))
                {
                    thirdCategory = thirdCategory + "x" + currThiCateName + "x";
                    category.put("thirdCategory", thirdCategory);
                }
                productCatagoryInfoMap.put(pbid, category);
            }

            // 封装商品分类信息
            for (Map<String, Object> map : pinfos)
            {
                String pbid = map.get("productBaseId") + "";
                Map<String,Object> category = productCatagoryInfoMap.get(pbid);
                if (category == null )
                {
                    category = new HashMap<>();
                }
                String secondCategory = category.get("secondCategory") == null ? "" : category.get("secondCategory") + "";
                String thirdCategory = category.get("thirdCategory") == null ? "" : category.get("thirdCategory") + "";
                map.put("secondCategory", secondCategory);
                map.put("thirdCategory", thirdCategory);
            }
//            System.out.println("pont:"+pinfos);
            if (client == null)
            {
                String elasticsearch_host = YggAdminProperties.getInstance().getPropertie("elasticsearch_host");
                String elasticsearch_port = YggAdminProperties.getInstance().getPropertie("elasticsearch_port");
                String elasticsearch_cluster_name = YggAdminProperties.getInstance().getPropertie("elasticsearch_cluster_name");

                log.info("elasticsearch_host:" + elasticsearch_host + ",elasticsearch_port:" + elasticsearch_port + ",elasticsearch_cluster_name:" + elasticsearch_cluster_name);

                Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", elasticsearch_cluster_name).build();
                client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(elasticsearch_host, Integer.valueOf(elasticsearch_port)));
            }
            // 检查索引是否存在
            if (client.admin().indices().prepareExists(index).execute().actionGet().isExists())
            {
                // 删除索引
                DeleteIndexResponse deleteIndexResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
            }
            // 创建索引
            CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(index).execute().actionGet();

            // 批量索引文件
            BulkRequestBuilder builder = client.prepareBulk();
            for (Map<String, Object> map : pinfos)
            {
                String id = map.get("id") + "";
                IndexRequest request = client.prepareIndex(index, type).setId(id).setSource(map).setOpType(IndexRequest.OpType.INDEX).request();
                builder.add(request);
            }

            BulkResponse bResponse = builder.execute().actionGet();
            if (bResponse.hasFailures())
            {
                log.warn("增加索引失败！" + bResponse.buildFailureMessage());
                status = 0;
            }
            else
            {
                log.info("增加索引成功");
                cache.set("common_all_display_productid", cacheProductIdList, 24 * 60 * 60);
                System.out.println("common_all_display_productid: " + cacheProductIdList);
            }
        }
        return status;
    }
}
