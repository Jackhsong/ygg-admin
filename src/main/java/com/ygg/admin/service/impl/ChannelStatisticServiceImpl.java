package com.ygg.admin.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ChannelProductManageDao;
import com.ygg.admin.dao.ChannelStatisticDao;
import com.ygg.admin.exception.DaoException;
import com.ygg.admin.service.ChannelStatisticService;
/**
 * 渠道统计和商品统计公用Service
 * @author Qiu,Yibo
 *
 */
@Repository
public class ChannelStatisticServiceImpl implements ChannelStatisticService
{

    @Resource
    private ChannelProductManageDao channelProductManageDao;
    
    @Resource
    private ChannelStatisticDao channelStatisticDao;
    
    @Override
    public List<String> getAllChannelNameAndId(int type) throws DaoException
    {
        //按照ID大小排序
        List<Map<String,Object>> resultList = channelProductManageDao.getAllChannelNameAndId();
        List<String> channelNameList = new ArrayList<String>();
        for(Map<String,Object> map :resultList){
            String channelName =  map.get("channelName") + 
                    (type>=0?"":"金额") + "%" + String.valueOf(map.get("channelId"));
            channelNameList.add(channelName);
        }
        return channelNameList;
    }

    @Override
    public Map<String, Object> jsonChannelStatisticInfo(Map<String, Object> para)
            throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = channelStatisticDao.jsonChannelStatisticInfo(para);
        //orderTime为key，List<Row>为value,orderTime时间排序
        Map<String,List<Map<String,Object>>> mapList = new LinkedHashMap<String,List<Map<String,Object>>>();
        for(Map<String,Object> rowMap:infoList){
            String orderTime = String.valueOf(rowMap.get("orderTime"));
            if(mapList.containsKey(orderTime)){
                mapList.get(orderTime).add(rowMap);
            }else{
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                list.add(rowMap);
                mapList.put(orderTime, list);
            }
        }
        
        //List<Map<orderTime,value> Map<ChannelId1,value1> Map<ChannelId1,value1>....Map<totalPrice,totalPrice>>
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for(Map.Entry<String,List<Map<String,Object>>> entry:mapList.entrySet()){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("orderTime", entry.getKey());
            List<Map<String,Object>> listhasSameOrderTime =  entry.getValue();
            double doubleTotal = 0;
            for(Map<String,Object> m:listhasSameOrderTime){
                String channel = "channelId"+m.get("channelId");
                String price = String.valueOf(new DecimalFormat("0.00").format(m.get("totalPrice")));
                //同一个渠道有多个订单
                if(map.containsKey(channel)){
                    double p = Double.valueOf(String.valueOf(map.get(channel)));
                    map.put(channel, String.valueOf(new DecimalFormat("0.00").format(p+Double.valueOf(price))));
                }else{
                    map.put(channel,price);
                }
                doubleTotal+=Double.valueOf(price);
            }
            map.put("totalPrice", new DecimalFormat("0.00").format(doubleTotal));
            resultList.add(map);
        }
        
        resultMap.put("rows", resultList);
        int total = channelStatisticDao.countChannelStatisticInfo(para);
        resultMap.put("total",total);
        return resultMap;
    }

    
    public Map<String, Object> jsonChannelProStatisticInfo(Map<String, Object> para) throws Exception
    {
        int type = (int)para.get("type");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> productCodeAndTotalPriceOrNumList = 
                type==0?channelStatisticDao.getProductCodeListByTotalPrice(para):
                                 channelStatisticDao.getProductCodeListByTotalNum(para);
                
        List<String> productCodePriceOrNumList = new ArrayList<String>();
        for(Map<String, Object> m : productCodeAndTotalPriceOrNumList){
            productCodePriceOrNumList.add(String.valueOf(m.get("productCode")));
        }
        
        if(CollectionUtils.isNotEmpty(productCodePriceOrNumList)){
            para.put("productCodeList", productCodePriceOrNumList);
        }
        List<Map<String, Object>> infoList = channelStatisticDao.jsonChannelProStatisticInfo(para);
        //productCode为key，List<Row>为value,productCode时间排序
        Map<String,List<Map<String,Object>>> mapList = new LinkedHashMap<String,List<Map<String,Object>>>();
        for(Map<String,Object> rowMap:infoList){
            String productCode = String.valueOf(rowMap.get("productCode"));
            if(mapList.containsKey(productCode)){
                mapList.get(productCode).add(rowMap);
            }else{
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                list.add(rowMap);
                mapList.put(productCode, list);
            }
        }
        
        //List<Map<orderTime,value> Map<ChannelId1,value1> Map<ChannelId1,value1>....Map<totalPrice,totalPrice>>
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for(Map.Entry<String,List<Map<String,Object>>> entry:mapList.entrySet()){
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("productCode", entry.getKey());
            List<Map<String,Object>> listhasSameProductCode =  entry.getValue();
            double doubleTotal = 0;
            for(Map<String,Object> m:listhasSameProductCode){
                String channel = "channelId"+m.get("channelId");
                String productPriceorCount = String.valueOf(new DecimalFormat("0.00").format(
                                                        type==0?m.get("productPrice"):m.get("productCount")));
                map.put("productName", m.get("productName"));   
                map.put(channel,productPriceorCount);
                doubleTotal+=Double.valueOf(productPriceorCount);
            }
            map.put("totalPrice", new DecimalFormat("0.00").format(doubleTotal));
            resultList.add(map);
        }
        
        //按照productCodeList中的productCode重新排序resultList
//        Collections.sort(resultList,new Comparator<Map<String, Object>>(){
//            @Override
//            public int compare(Map<String, Object> o1, Map<String, Object> o2)
//            {
//                return 0;
//            }
//        });
        
        //TODO refact
        for(int i=0;i<productCodePriceOrNumList.size();i++){
            String productCode = productCodePriceOrNumList.get(i);
            for(int j=0;j<resultList.size();j++){
                String pc = String.valueOf(resultList.get(j).get("productCode"));
                if(productCode.equals(pc)){
                    Collections.swap(resultList, i, j);
                }
            }
        }
        
        resultMap.put("rows", resultList);
        int total = channelStatisticDao.countChannelProStatisticInfo(para);
        resultMap.put("total",total);
        return resultMap;
    }
}
