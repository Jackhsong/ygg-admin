package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.PurchaseStoringDao;
import com.ygg.admin.service.PurchaseStoringService;

@Service("purchaseStoringService")
public class PurchaseStoringServiceImpl implements PurchaseStoringService
{
    
    @Resource
    private PurchaseStoringDao purchaseStoringDao;
    
    @Override
    public List<Map<String, Object>> findPurchaseStoringByParam(Map<String, Object> param)
        throws Exception
    {
        return purchaseStoringDao.findPurchaseStoringByParam(param);
    }
    
    @Override
    public List<Map<String, Object>> findPurchaseStoringByIds(List<Object> list)
        throws Exception
    {
        return purchaseStoringDao.findPurchaseStoringByIds(list);
    }
    
    @Override
    public int purchaseOrder(List<Map<String, Object>> infos)
        throws Exception
    {
        if (infos == null || infos.isEmpty())
            return 0;
        int result = 0;
        for (Map<String, Object> param : infos)
        {
            Object providerProductId = param.get("providerProductId");
            Object purchaseQuantity = param.get("purchaseQuantity");
            param.clear();
            param.put("providerProductId", providerProductId);// 采购商品ID
            param.put("gegeWatingStoring", purchaseQuantity);// 等待入库数
            result += updatePurchaseStoring(param);
        }
        return result;
    }
    
    @Override
    public int updatePurchaseStoring(Map<String, Object> param)
        throws Exception
    {
        return purchaseStoringDao.updatePurchaseStoringByParam(param);
    }
    
    @Override
    public int usedProviderProduct(List<Map<String, Object>> infos)
        throws Exception
    {
        if (infos == null || infos.isEmpty())
            throw new RuntimeException("更新渠道库存时没有找到参数。");
        for (Iterator<Map<String, Object>> iterator = infos.iterator(); iterator.hasNext();)
        {
            Map<String, Object> param = iterator.next();
            updatePurchaseStoring(param);
        }
        return 1;
    }
    
    @Override
    public int usedUnallocationStoring(Map<String, Object> info)
        throws Exception
    {
        return updatePurchaseStoring(info);
    }
    
    @Override
    public Map<String, Object> statisticsUnpush()
        throws Exception
    {
        // 未推送订单
        List<Map<String, Object>> unpushOrder = purchaseStoringDao.findUnpushOrder();
        if (unpushOrder == null || unpushOrder.isEmpty())
            return new HashMap<String, Object>();
        
        List<Object> productIdList = new ArrayList<Object>();
        for (Map<String, Object> unpush : unpushOrder)
        {
            productIdList.add(unpush.get("productId"));
        }
        
        // 商品信息与采购商品之间的关联关系
        List<Map<String, Object>> providerProductInfo = purchaseStoringDao.findProviderProduct(productIdList);
        if (providerProductInfo == null || providerProductInfo.isEmpty())
            return new HashMap<String, Object>();
        
        Map<String, Object> result = new HashMap<String, Object>();
        for (Map<String, Object> item : providerProductInfo)
        {
            String productId = String.valueOf(item.get("id"));
            int groupCount = Integer.valueOf(String.valueOf(item.get("groupCount")));
            String providerProductId = String.valueOf(item.get("providerProductId"));
            for (Map<String, Object> unpush : unpushOrder)
            {
                if (StringUtils.equals(productId, String.valueOf(unpush.get("productId"))))
                {
                    result.put(providerProductId, groupCount * Integer.valueOf(String.valueOf(unpush.get("productCount"))));
                }
            }
        }
        return result;
    }
    
}
