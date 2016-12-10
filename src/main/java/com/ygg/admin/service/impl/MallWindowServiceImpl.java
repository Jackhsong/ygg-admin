package com.ygg.admin.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.MallWindowDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.entity.MallPageEntity;
import com.ygg.admin.entity.MallWindowEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.MallWindowService;
import com.ygg.admin.util.ImageUtil;
import com.ygg.admin.util.ProductUtil;

@Service("mallWindowService")
public class MallWindowServiceImpl implements MallWindowService
{
    @Resource
    private MallWindowDao mallWindowDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource(name = "saleWindowDao")
    private SaleWindowDao saleWindowDao = null;
    
    @Resource(name = "activitiesCommonDao")
    private ActivitiesCommonDao activitiesCommonDao = null;
    
    @Override
    public Map<String, Object> findAllMallWindow(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mallWindowDao.findJsonMallWindowInfo(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("isDiaplay", ((int)map.get("isDisplayCode")) == 1 ? "展现" : "不展现");
                map.put("image", "<img alt='' src='" + map.get("imageURL") + "' style='max-width:100px'/>");
            }
            total = mallWindowDao.countMallWindowInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateMallWindowByPara(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.updateMallWindowByPara(para);
    }
    
    @Override
    public int saveOrUpdate(MallWindowEntity mallWindow)
        throws Exception
    {
        mallWindow.setImage(mallWindow.getImage().indexOf(ImageUtil.getPrefix()) > 0 ? mallWindow.getImage()
            : (mallWindow.getImage() + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.mallicon.ordinal())));
        if (mallWindow.getId() != 0)
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", mallWindow.getId());
            para.put("mallPageId", mallWindow.getMallPageId());
            para.put("name", mallWindow.getName());
            para.put("image", mallWindow.getImage());
            para.put("isDisplay", mallWindow.getIsDisplay());
            para.put("remark", mallWindow.getRemark());
            return mallWindowDao.updateMallWindowByPara(para);
        }
        else
        {
            int sequence = mallWindowDao.getMallWindowMaxSequence();
            mallWindow.setSequence(sequence);
            return mallWindowDao.saveMallWindow(mallWindow);
        }
    }
    
    @Override
    public List<Map<String, Object>> findAllMallPage(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.findAllMallPage(para);
    }
    
    @Override
    public MallWindowEntity findMallWindowById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<MallWindowEntity> resultList = mallWindowDao.findMallWindowByPara(para);
        if (resultList != null && resultList.size() > 0)
        {
            return resultList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public Map<String, Object> findAllMallPageJsonInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mallWindowDao.findAllMallPage(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("isAvailable", ((int)map.get("isAvailableCode")) == 1 ? "可用" : "不可用");
            }
            total = mallWindowDao.countMallPageInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateMallPage(MallPageEntity mallPage)
        throws Exception
    {
        if (mallPage.getId() == 0)
        {
            return mallWindowDao.saveMallPage(mallPage);
        }
        else
        {
            return mallWindowDao.updateMallPage(mallPage);
        }
    }
    
    @Override
    public MallPageEntity findAllMallPageById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<Map<String, Object>> infoList = mallWindowDao.findAllMallPage(para);
        if (infoList != null && infoList.size() > 0)
        {
            MallPageEntity mallPage = new MallPageEntity();
            mallPage.setId(id);
            mallPage.setName(infoList.get(0).get("name") + "");
            mallPage.setRemark(infoList.get(0).get("remark") + "");
            mallPage.setIsAvailable(Integer.valueOf(infoList.get(0).get("isAvailableCode") + "").intValue());
            return mallPage;
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public Map<String, Object> findAllMallPageFloorJsonInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mallWindowDao.findAllMallPageFloor(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("isDisplay", "1".equals(map.get("isDisplayCode") + "") ? "展现" : "不展现");
            }
            total = mallWindowDao.countMallPageFloor(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdatePageFloor(Map<String, Object> para)
        throws Exception
    {
        String id = para.get("id") + "";
        if ("0".equals(id))
        {
            int sequence = mallWindowDao.getPageFloorMaxSequence(para);
            para.put("sequence", sequence);
            return mallWindowDao.savePageFloor(para);
        }
        else
        {
            return mallWindowDao.updatePageFloor(para);
        }
    }
    
    @Override
    public int updatePageFloorByPara(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.updatePageFloor(para);
    }
    
    @Override
    public Map<String, Object> jsonMallFloorProductInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mallWindowDao.findJsonMallFloorProductInfo(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("nameUrl", ProductUtil.getViewURL(Integer.parseInt(map.get("productId").toString())));
                map.put("isOffShelves", (int)map.get("isOffShelves") == 1 ? "下架" : "上架");
            }
            total = mallWindowDao.countMallFloorProductInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public List<Map<String, Object>> findMallPageFloorCode(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.findAllMallPageFloor(para);
    }
    
    @Override
    public int updateFloorProductSequence(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.updateFloorProductSequence(para);
    }
    
    @Override
    public int deleteRelationMallPageFloorAndProduct(int id)
        throws Exception
    {
        return mallWindowDao.deleteRelationMallPageFloorAndProduct(id);
    }
    
    @Override
    public int deleteRelationMallPageFloorAndProductList(List<Integer> idList)
        throws Exception
    {
        for (Integer id : idList)
        {
            mallWindowDao.deleteRelationMallPageFloorAndProduct(id);
        }
        return 1;
    }
    
    @Override
    public Map<String, Object> jsonProductListForAdd(Map<String, Object> para)
        throws Exception
    {
        // 查询商品数据，不包括该主题馆页面已经添加的商品和不可用的商品
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> idList = mallWindowDao.findAllProductIdByMallPageId((int)para.get("mallPageId"));
        if (idList != null && idList.size() > 0)
        {
            para.put("idList", idList);
        }
        List<Map<String, Object>> infoList = mallWindowDao.findProductInfoForAdd(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("showId", map.get("productId"));
                map.put("typeStr", (int)map.get("type") == 1 ? "特卖商品" : "商城商品");
            }
            total = mallWindowDao.countProductInfoForAdd(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int addProductForPageFloor(Map<String, Object> para)
        throws Exception
    {
        List<Integer> idList_new = (List<Integer>)para.get("idList_new");
        int floorId = (int)para.get("floorId");
        int maxSequence = mallWindowDao.findMaxSequenceRelationMallPageFloorAndProduct(floorId);
        for (Integer currId : idList_new)
        {
            // 过滤楼层下已经存在的商品
            List<Integer> existsIdList = mallWindowDao.findAllProductIdByPageFloorId(floorId);
            if (!existsIdList.contains(currId))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("productId", currId);
                map.put("mallPageFloorId", floorId);
                map.put("sequence", maxSequence);
                mallWindowDao.saveRelationMallPageFloorAndProduct(map);
                maxSequence++;
            }
        }
        return 1;
    }
    
    @Override
    public Map<String, Object> findAllCurrentHotProductList(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mallWindowDao.findAllCurrentHotProductInfo(para);
        
        // 计算销量导致页面加载速度太慢，这里暂时注释掉计算销量代码，勿删！！！
        
        // 统计特卖商品的销量：特卖商品查询下单时间在特卖开始和结束之间的订单
        //        Map<Integer, Integer> saleProductAmountMap = new HashMap<Integer, Integer>();
        //        Set<Integer> saleProductId = new HashSet<Integer>();
        //        List<Map<String, Object>> saleProductInfoList = mallWindowDao.findSaleProductInfo();
        //        for (Map<String, Object> map : saleProductInfoList)
        //        {
        //            DateTime startTime = DateTime.parse(map.get("startTime") + "", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        //            DateTime endTime = DateTime.parse(map.get("endTime") + "", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        //            int hour = Hours.hoursBetween(startTime, endTime).getHours();
        //            int productId = Integer.valueOf(map.get("productId") + "").intValue();
        //            // 特卖商品如果开始时间和结束时间相差在120h内的，则统计这段时间内的销量；如果相差>120h的，则统计五天之前到当前时间的销量
        //            if (hour <= 120)
        //            {
        //                Integer amount = saleProductAmountMap.get(productId);
        //                if (amount == null)
        //                {
        //                    saleProductAmountMap.put(productId, Integer.valueOf(map.get("amount") + "").intValue());
        //                }
        //                else
        //                {
        //                    amount += Integer.valueOf(map.get("amount") + "").intValue();
        //                    saleProductAmountMap.put(productId, amount);
        //                }
        //            }
        //            else
        //            {
        //                saleProductId.add(productId);
        //            }
        //            
        //        }
        //        //统计销售时间大于120小时的特卖商品销量
        //        Map<Integer, Integer> saleProductAmountIn5DaysMap = new HashMap<Integer, Integer>();
        //        if (saleProductId.size() > 0)
        //        {
        //            para.put("startTime", DateTime.now().minusDays(5).toString("yyyy-MM-dd HH:mm:ss"));
        //            para.put("endTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        //            para.put("productIdList", new ArrayList<Integer>(saleProductId));
        //            List<Map<String, Object>> saleProductInfoListIn5Days = mallWindowDao.findSaleProductInfoIn5Days(para);
        //            for (Map<String, Object> map : saleProductInfoListIn5Days)
        //            {
        //                //特卖超过120小时商品销量统计
        //                int productId = Integer.valueOf(map.get("productId") + "").intValue();
        //                Integer amount = saleProductAmountIn5DaysMap.get(productId);
        //                if (amount == null)
        //                {
        //                    saleProductAmountIn5DaysMap.put(productId, Integer.valueOf(map.get("amount") + "").intValue());
        //                }
        //                else
        //                {
        //                    amount += Integer.valueOf(map.get("amount") + "").intValue();
        //                    saleProductAmountIn5DaysMap.put(productId, amount);
        //                }
        //                
        //            }
        //        }
        //        saleProductAmountMap.putAll(saleProductAmountIn5DaysMap);
        //        
        //        //统计商城商品的销量：商城商品查询五天之内的订单
        //        Map<Integer, Integer> mallProductAmountMap = new HashMap<Integer, Integer>();
        //        int hour = DateTime.now().getHourOfDay();
        //        int n = 5;
        //        if (hour >= 10)
        //        {
        //            n = 4;
        //        }
        //        String beginTime = DateTime.now().minus(n).withHourOfDay(10).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toString("yyyy-MM-dd HH:mm:ss.SSS");
        //        List<Map<String, Object>> mallProductInfoList = mallWindowDao.findMallProductInfo(beginTime);
        //        for (Map<String, Object> map : mallProductInfoList)
        //        {
        //            int productId = Integer.valueOf(map.get("productId") + "").intValue();
        //            Integer amount = mallProductAmountMap.get(productId);
        //            if (amount == null)
        //            {
        //                mallProductAmountMap.put(productId, Integer.valueOf(map.get("amount") + "").intValue());
        //            }
        //            else
        //            {
        //                amount += Integer.valueOf(map.get("amount") + "").intValue();
        //                mallProductAmountMap.put(productId, amount);
        //            }
        //            
        //        }
        
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int typeCode = Integer.valueOf(map.get("typeCode") + "").intValue();
                int productId = Integer.valueOf(map.get("productId") + "").intValue();
                int stock = Integer.valueOf(map.get("stock") + "").intValue();
                if (typeCode == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    DateTime endTime = DateTime.parse(map.get("endTime") + "", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                    map.put("productTime", (Timestamp)map.get("startTime") + " ~ " + (Timestamp)map.get("endTime"));
                    //                    map.put("sellAmount", saleProductAmountMap.get(productId) == null ? 0 : saleProductAmountMap.get(productId).intValue());
                    map.put("isEnd", endTime.isBeforeNow() ? "是" : "否");
                }
                else if (typeCode == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    map.put("productTime", "-");
                    //                    map.put("sellAmount", mallProductAmountMap.get(productId) == null ? 0 : saleProductAmountMap.get(productId).intValue());
                    map.put("isEnd", "-");
                }
                map.put("nameUrl", ProductUtil.getViewURL(productId));
                map.put("type", (int)map.get("typeCode") == 1 ? "特卖商品" : "商城商品");
                map.put("isDisplay", (int)map.get("isDisplayCode") == 1 ? "展现" : "不展现");
                map.put("isZeroStock", stock == 0 ? "无" : "有");
            }
            total = mallWindowDao.countCurrentHotProductInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updateHotProduct(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.updateHotProduct(para);
    }
    
    @Override
    public int deleteHotProduct(Map<String, Object> result)
        throws Exception
    {
        mallWindowDao.deleteHotProduct(result);
        return 1;
    }
    
    @Override
    public Map<String, Object> jsonHotProductListForAdd(Map<String, Object> para)
        throws Exception
    {
        // 查询商品数据，不包括已经添加到今日最热的商品和不可用的商品
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Integer> idList = mallWindowDao.findAllHotProductId();
        if (idList != null && idList.size() > 0)
        {
            para.put("idList", idList);
        }
        List<Map<String, Object>> infoList = mallWindowDao.findProductInfoForAdd(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("showId", map.get("productId"));
                map.put("typeStr", (int)map.get("type") == 1 ? "特卖商品" : "商城商品");
            }
            total = mallWindowDao.countProductInfoForAdd(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int addProductForHotProduct(Map<String, Object> para)
        throws Exception
    {
        List<String> idList_new = (List<String>)para.get("idList_new");
        for (String IdAndType : idList_new)
        {
            String[] arr = IdAndType.split(":");
            Map<String, Object> map = new HashMap<String, Object>();
            int count = mallWindowDao.countProductHotId(Integer.valueOf(arr[0]));
            if (count == 0)
            {
                //添加的商品默认为自动设为不展现
                map.put("productId", arr[0]);
                map.put("type", arr[1]);
                map.put("sequence", 0);
                map.put("isDisplay", 0);
                map.put("updateDisplayByTimer", 1);
                mallWindowDao.saveProductHot(map);
            }
        }
        return 1;
    }
    
    @Override
    public Map<String, Object> quickAddProduct(List<Integer> idList)
        throws Exception
    {
        String msg = "";
        String msg1 = "";
        String msg2 = "";
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (idList == null || idList.size() == 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入要添加的商品Id");
            return resultMap;
            
        }
        for (Integer productId : idList)
        {
            int count = mallWindowDao.countProductHotId(productId);
            if (count > 0)
            {
                msg1 = msg1 + productId + ",";
            }
            else
            {
                Map<String, Object> para = new HashMap<String, Object>();
                ProductEntity product = productDao.findProductByID(productId, null);
                if (product == null)
                {
                    msg2 = msg2 + productId + ",";
                }
                else
                {
                    //添加的商品默认为自动设为不展现
                    para.put("productId", productId);
                    para.put("type", product.getType());
                    para.put("sequence", 0);
                    para.put("isDisplay", 0);
                    para.put("updateDisplayByTimer", 1);
                    mallWindowDao.saveProductHot(para);
                }
                
            }
        }
        if (!"".equals(msg1))
        {
            msg = "Id=[" + msg1.substring(0, msg1.lastIndexOf(",")) + "]的商品已经存在今日最热商品列表中，无需添加<br/>";
        }
        if (!"".equals(msg2))
        {
            msg += "Id=[" + msg2.substring(0, msg2.lastIndexOf(",")) + "]的商品不存在，无法添加";
        }
        resultMap.put("status", 1);
        if (!"".equals(msg))
        {
            resultMap.put("msg", msg);
        }
        else
        {
            resultMap.put("msg", "添加成功");
        }
        return resultMap;
    }
    
    @Override
    public Map<String, Object> findAllCurrentHotDisplayProductList(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mallWindowDao.findAllCurrentHotDisplayProductList(para);
        
        // 计算导致页面加载速度慢，暂时注释计算销量的代码
        // 特卖商品查询下单时间在特卖开始和结束之间的订单
        //        Map<Integer, Integer> saleProductAmountMap = new HashMap<Integer, Integer>();
        //        Set<Integer> saleProductId = new HashSet<Integer>();
        //        List<Map<String, Object>> saleProductInfoList = mallWindowDao.findSaleProductInfo();
        //        for (Map<String, Object> map : saleProductInfoList)
        //        {
        //            DateTime nowTime = DateTime.now();
        //            DateTime startTime = DateTime.parse(map.get("startTime") + "", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        //            DateTime endTime = DateTime.parse(map.get("endTime") + "", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        //            if (endTime.isAfter(nowTime) && startTime.isBefore(nowTime))
        //            {
        //                int hour = Hours.hoursBetween(startTime, nowTime).getHours();
        //                int productId = Integer.valueOf(map.get("productId") + "").intValue();
        //                // 特卖商品如果开始时间和结束时间相差在120h内的，则统计这段时间内的销量；如果相差>120h的，则统计五天之前到当前时间的销量
        //                if (hour <= 120 && hour >= 0)
        //                {
        //                    Integer amount = saleProductAmountMap.get(productId);
        //                    if (amount == null)
        //                    {
        //                        saleProductAmountMap.put(productId, Integer.valueOf(map.get("amount") + "").intValue());
        //                    }
        //                    else
        //                    {
        //                        amount += Integer.valueOf(map.get("amount") + "").intValue();
        //                        saleProductAmountMap.put(productId, amount);
        //                    }
        //                }
        //                else if (hour > 120)
        //                {
        //                    saleProductId.add(productId);
        //                }
        //            }
        //            
        //        }
        //        //统计销售时间大于120小时的特卖商品销量
        //        Map<Integer, Integer> saleProductAmountIn5DaysMap = new HashMap<Integer, Integer>();
        //        if (saleProductId.size() > 0)
        //        {
        //            para.put("startTime", DateTime.now().minusDays(5).toString("yyyy-MM-dd HH:mm:ss"));
        //            para.put("endTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        //            para.put("productIdList", new ArrayList<Integer>(saleProductId));
        //            List<Map<String, Object>> saleProductInfoListIn5Days = mallWindowDao.findSaleProductInfoIn5Days(para);
        //            for (Map<String, Object> map : saleProductInfoListIn5Days)
        //            {
        //                //特卖超过120小时商品销量统计
        //                int productId = Integer.valueOf(map.get("productId") + "").intValue();
        //                Integer amount = saleProductAmountIn5DaysMap.get(productId);
        //                if (amount == null)
        //                {
        //                    saleProductAmountIn5DaysMap.put(productId, Integer.valueOf(map.get("amount") + "").intValue());
        //                }
        //                else
        //                {
        //                    amount += Integer.valueOf(map.get("amount") + "").intValue();
        //                    saleProductAmountIn5DaysMap.put(productId, amount);
        //                }
        //                
        //            }
        //        }
        //        saleProductAmountMap.putAll(saleProductAmountIn5DaysMap);
        //        
        //        //商城商品查询五天之内的订单
        //        Map<Integer, Integer> mallProductAmountMap = new HashMap<Integer, Integer>();
        //        int hour = DateTime.now().getHourOfDay();
        //        int n = 5;
        //        if (hour >= 10)
        //        {
        //            n = 4;
        //        }
        //        String beginTime = DateTime.now().minus(n).withHourOfDay(10).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toString("yyyy-MM-dd HH:mm:ss.SSS");
        //        List<Map<String, Object>> mallProductInfoList = mallWindowDao.findMallProductInfo(beginTime);
        //        for (Map<String, Object> map : mallProductInfoList)
        //        {
        //            int productId = Integer.valueOf(map.get("productId") + "").intValue();
        //            Integer amount = mallProductAmountMap.get(productId);
        //            if (amount == null)
        //            {
        //                mallProductAmountMap.put(productId, Integer.valueOf(map.get("amount") + "").intValue());
        //            }
        //            else
        //            {
        //                amount += Integer.valueOf(map.get("amount") + "").intValue();
        //                mallProductAmountMap.put(productId, amount);
        //            }
        //            
        //        }
        
        int total = 0;
        
        for (Map<String, Object> map : infoList)
        {
            int productId = Integer.valueOf(map.get("productId") + "").intValue();
            
            // 商品类型，type=1表示特卖商品，type=2表示商城商品
            int type = Integer.valueOf(map.get("typeCode") + "").intValue();
            
            // 总销量，type=1时，从saleProductAmountMap中取；type=2时，从mallProductAmountMap中取
            int sellAmount = 0;
            if (type == 1)
            {
                //特卖商品开始时间
                String startTime = map.get("startTime") + "";
                
                //销量，特卖商品从saleProductAmountMap中取
                //sellAmount = saleProductAmountMap.get(productId) == null ? 0 : saleProductAmountMap.get(productId).intValue();
                
                //销售时长折扣，特卖商品根据特卖开始时间取不同的区间
                int saleTime = Hours.hoursBetween(DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")), DateTime.now()).getHours();
                if (saleTime <= 120)
                {
                    map.put("saleTime", saleTime >= 0 ? saleTime : 0);
                }
                else
                {
                    map.put("saleTime", 120);
                }
                
            }
            else if (type == 2)
            {
                //销量，商城商品从mallProductAmountMap中取
                //sellAmount = mallProductAmountMap.get(productId) == null ? 0 : mallProductAmountMap.get(productId).intValue();
                
                map.put("saleTime", "-");
            }
            
            // 按销售天数打折之后的权重  = 总权重*销售时长折扣，
            // map.put("sellAmount", sellAmount);
            // map.put("saleMoney", new BigDecimal(sellAmount).multiply(new BigDecimal(map.get("salesPrice") + "")));
            map.put("type", (int)map.get("typeCode") == 1 ? "特卖商品" : "商城商品");
        }
        total = mallWindowDao.countCurrentHotDisplayProduct(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    private int getFactor(String time, Map<String, Object> factorMap)
    {
        Object result = null;
        DateTime now = DateTime.now();
        DateTime beginTime = DateTime.parse(time, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        int hours = Hours.hoursBetween(beginTime, now).getHours();
        if (hours < 24)
        {
            result = factorMap.get("factor1");
        }
        else if (hours < 48)
        {
            result = factorMap.get("factor2");
        }
        else if (hours < 72)
        {
            result = factorMap.get("factor3");
        }
        else if (hours < 96)
        {
            result = factorMap.get("factor4");
        }
        else
        {
            result = factorMap.get("factor5");
        }
        return result == null ? 0 : Integer.valueOf(result + "").intValue();
    }
    
    @Override
    public int updateHotProductCustomFactor(Map<String, Object> para)
        throws Exception
    {
        /*Map<String, Object> map = mallWindowDao.findHotProductInfoById(para);
        
        // 销售时长权重列表
        Map<String, Object> saleTimeFactorMap = mallWindowDao.findProductHotTimeFactor();
        int type = Integer.valueOf(map.get("type") + "").intValue();
        int totalFactor = Integer.valueOf(map.get("totalFactor") + "").intValue();
        int saleTimeFactor = 0;
        if (type == 1)
        {
            String time = map.get("startTime") + "";
            saleTimeFactor = getFactor(time, saleTimeFactorMap);
        }
        else
        {
            saleTimeFactor = Integer.valueOf(saleTimeFactorMap.get("factor0") == null ? "0" : saleTimeFactorMap.get("factor0") + "").intValue();
        }
        int sequence = new BigDecimal(totalFactor).multiply(new BigDecimal(saleTimeFactor / 100.00f)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        int customFactor = Integer.valueOf(para.get("customFactor") + "").intValue();
        sequence += customFactor;
        if (sequence < 0)
        {
            sequence = 0;
        }
        para.put("sequence", sequence);*/
        return mallWindowDao.updateHotProductCustomFactor(para);
    }
    
    @Override
    public Map<String, Object> jsonSaleTimeDiscount()
        throws Exception
    {
        Map<String, Object> saleTimeFactorMap = mallWindowDao.findProductHotTimeFactor();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < saleTimeFactorMap.size(); i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            if (i == 0)
            {
                map.put("type", "商城商品");
                map.put("time", "-");
            }
            else if (i == 1)
            {
                map.put("type", "特卖商品");
                map.put("time", "[0,24)");
            }
            else if (i == 2)
            {
                map.put("type", "特卖商品");
                map.put("time", "[24,48)");
            }
            else if (i == 3)
            {
                map.put("type", "特卖商品");
                map.put("time", "[48,72)");
            }
            else if (i == 4)
            {
                map.put("type", "特卖商品");
                map.put("time", "[72,96)");
            }
            else if (i == 5)
            {
                map.put("type", "特卖商品");
                map.put("time", "[96,120)");
            }
            map.put("factor", saleTimeFactorMap.get("factor" + i));
            map.put("index", i);
            infoList.add(map);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", 6);
        return resultMap;
    }
    
    @Override
    public int updateSaleTimeFactor(Map<String, Object> para)
        throws Exception
    {
        return mallWindowDao.updateSaleTimeFactor(para);
    }
    
    @Override
    public int updateHotProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        mallWindowDao.updateHotProductDisplayStatus(para);
        return 1;
    }
    
    @Override
    public int addSaleWindowToHotProduct(String selectedDate)
        throws Exception
    {
        int startTime = Integer.valueOf(DateTime.parse(selectedDate, DateTimeFormat.forPattern("yyyy-MM-dd")).toString("yyyyMMdd"));
        Set<Integer> productIdSet = new HashSet<Integer>();
        List<Map<String, Object>> reList = saleWindowDao.findAllTomorrowSaleWindow(startTime);
        for (Map<String, Object> map : reList)
        {
            int type = Integer.valueOf(map.get("type") + "").intValue();
            int displayId = Integer.valueOf(map.get("displayId") + "").intValue();
            if (type == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
            {
                productIdSet.add(displayId);
            }
            else if (type == SaleWindowEnum.SALE_TYPE.ACTIVITIES_COMMON.getCode())
            {
                List<Integer> productIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(displayId);
                productIdSet.addAll(new HashSet<Integer>(productIdList));
            }
        }
        for (Integer productId : productIdSet)
        {
            int count = mallWindowDao.countProductHotId(productId);
            if (count == 0)
            {
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("productId", productId);
                para.put("type", ProductEnum.PRODUCT_TYPE.SALE.getCode());
                para.put("sequence", 0);
                para.put("isDisplay", 0);
                para.put("updateDisplayByTimer", 1);
                mallWindowDao.saveProductHot(para);
            }
        }
        return productIdSet.size();
    }
}
