package com.ygg.admin.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ActivityEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ProductInfoForGroupSale;
import com.ygg.admin.entity.RelationActivitiesCommonAndProduct;
import com.ygg.admin.service.ActivitiesCommonService;
import com.ygg.admin.util.ProductUtil;

@Service("activitiesCommonService")
public class ActivitiesCommonServiceImpl implements ActivitiesCommonService
{
    
    @Resource(name = "activitiesCommonDao")
    private ActivitiesCommonDao activitiesCommonDao = null;
    
    @Override
    public String jsonAcCommonInfo(Map<String, Object> para)
        throws Exception
    {
        List<ActivitiesCommonEntity> reList = activitiesCommonDao.findAllAcCommonByPara(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (reList.size() > 0)
        {
            for (ActivitiesCommonEntity curr : reList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.getId());
                String pName = curr.getName();
                String nameUrl =  pName ;
                map.put("name", nameUrl);
                map.put("desc", curr.getDesc());
                map.put("gegesay", curr.getGegesay());
                map.put("weixin", curr.getWxShareTitle());
                map.put("isAvailable", (curr.getIsAvailable() == (byte)1) ? "可用" : "停用");
                map.put("type", curr.getType());
                map.put("typeName", ActivityEnum.ACTIVITIES_COMMON_TYPE.getDescByCode(curr.getType()));
                resultList.add(map);
            }
            total = activitiesCommonDao.countAllAcCommonByPara(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public int save(ActivitiesCommonEntity entity)
        throws Exception
    {
        return activitiesCommonDao.save(entity);
    }
    
    @Override
    public int update(ActivitiesCommonEntity entity)
        throws Exception
    {
        return activitiesCommonDao.update(entity);
    }
    
    @Override
    public ActivitiesCommonEntity findAcCommonById(int id)
        throws Exception
    {
        return activitiesCommonDao.findAcCommonById(id);
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return activitiesCommonDao.updateOrder(para);
    }
    
    @Override
    public String jsonGroupSaleProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<ProductInfoForGroupSale> reList = activitiesCommonDao.findProductInfoForGroupSaleByActivitiesCommonId(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (reList.size() > 0)
        {
            for (ProductInfoForGroupSale curr : reList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.getRelationId());
                map.put("productId", curr.getProductId());
                map.put("order", curr.getOrder());
                map.put("code", curr.getCode());
                map.put("name", curr.getName());
                map.put("shortName", curr.getShortName());
                map.put("remark", curr.getRemark());
                map.put("isOffShelves", curr.getIsOffShelves() == 1 ? "下架" : "上架");
                map.put("sell", curr.getSell() + "");
                map.put("stock", curr.getStock() + "");
                map.put("marketPrice", curr.getMarketPrice());
                map.put("salesPrice", curr.getSalesPrice());
                map.put("sellerName", curr.getSellerName());
                map.put("sendAddress", curr.getSendAddress());
                map.put("baseId", curr.getProductBaseId());
                map.put("type", curr.getType());
                map.put("isDisplay", curr.getIsDisplay());
                map.put("display", curr.getIsDisplay() == 1 ? "展现" : "不展现");
                resultList.add(map);
            }
            total = activitiesCommonDao.countProductNumByActivitiesCommonId((int)para.get("id"));
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String jsonProductListForAdd(Map<String, Object> para)
        throws Exception
    {
        // 查询商品数据，不包括该特卖已经添加的商品和不可用的商品
        List<Integer> idList = activitiesCommonDao.findAllProductIdByActivitiesCommonId((int)para.get("cid"));
        if (idList != null && idList.size() > 0)
        {
            para.put("idList", idList);
        }
        List<ProductInfoForGroupSale> reList = activitiesCommonDao.findProductInfoForGroupSale(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (reList.size() > 0)
        {
            for (ProductInfoForGroupSale curr : reList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", curr.getProductId());
                map.put("showId", curr.getProductId());
                map.put("order", curr.getOrder());
                map.put("code", curr.getCode());
                map.put("name", curr.getName());
                map.put("stock", curr.getStock());
                map.put("marketPrice", curr.getMarketPrice());
                map.put("salesPrice", curr.getSalesPrice());
                map.put("sellerName", curr.getSellerName());
                map.put("brandName", curr.getBrandName());
                map.put("sendAddress", curr.getSendAddress());
                map.put("remark", curr.getRemark());
                map.put("warehouse", curr.getWarehouse());
                resultList.add(map);
            }
            total = activitiesCommonDao.countProductInfoForGroupSale(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public int deleteRelationActivitiesCommonAndProduct(int id)
        throws Exception
    {
        return activitiesCommonDao.deleteRelationActivitiesCommonAndProduct(id);
    }
    
    @Override
    public int deleteRelationActivitiesCommonAndProductList(List<Integer> ids)
        throws Exception
    {
        try
        {
            for (Integer id : ids)
            {
                activitiesCommonDao.deleteRelationActivitiesCommonAndProduct(id);
            }
            return 1;
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    @Override
    public int addGroupSaleProduct(Map<String, Object> para)
        throws Exception
    {
        List<Integer> idList_new = (List<Integer>)para.get("idList_new");
        int activitiesCommonId = (int)para.get("activitiesCommonId");
        int maxOrder = 0;
        for (Integer currId : idList_new)
        {
            List<Integer> existsIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(activitiesCommonId);
            if (!existsIdList.contains(currId))
            {
                RelationActivitiesCommonAndProduct racp = new RelationActivitiesCommonAndProduct();
                racp.setActivitiesCommonId(activitiesCommonId);
                racp.setProductId(currId);
                // order
                int num = activitiesCommonDao.countProductNumByActivitiesCommonId(activitiesCommonId);
                if (num > 1)
                {
                    maxOrder = activitiesCommonDao.findMaxOrderByActivitiesCommonId(activitiesCommonId);
                }
                maxOrder++;
                racp.setOrder(maxOrder);
                activitiesCommonDao.saveRelationActivitiesCommonAndProduct(racp);
            }
        }
        return 1;
    }
    
    @Override
    public List<ActivitiesCommonEntity> findAllAcCommonByPara(Map<String, Object> para)
        throws Exception
    {
        return activitiesCommonDao.findAllAcCommonByPara(para);
    }
    
    @Override
    public List<ProductInfoForGroupSale> findProductInfoForGroupSaleByActivitiesCommonId(Map<String, Object> para)
        throws Exception
    {
        return activitiesCommonDao.findProductInfoForGroupSaleByActivitiesCommonId(para);
    }
    
    @Override
    public int countProductNumByActivitiesCommonId(int id)
        throws Exception
    {
        return activitiesCommonDao.countProductNumByActivitiesCommonId(id);
    }
    
    @Override
    public int updateProductDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return activitiesCommonDao.updateProductDisplayStatus(para);
    }
    
    @Override
    public String batchUpdateGroupProductTime(String groupIds, String startTime, String endTime)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String> groupIdList = Arrays.asList(groupIds.split(","));
        Map<String, Object> para = new HashMap<String, Object>();
        for (String groupId : groupIdList)
        {
            if (StringUtils.isEmpty(groupId.trim()))
            {
                result.put("status", 0);
                result.put("msg", String.format("组合Id中含有空白字符串", groupId));
                return JSON.toJSONString(result);
            }
            ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(Integer.parseInt(groupId.trim()));
            if (ac == null)
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%s的组合不存在", groupId));
                return JSON.toJSONString(result);
            }
            if (ac.getType() == 2)
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%s的组合为商城组合，不能修改时间", groupId));
                return JSON.toJSONString(result);
            }
            para.put("groupId", groupId);
            para.put("startTime", startTime);
            para.put("endTime", endTime);
            activitiesCommonDao.batchUpdateGroupProductTime(para);
        }
        result.put("status", 1);
        result.put("msg", "修改成功");
        return JSON.toJSONString(result);
    }
    
    @Override
    public List<Map<String, Object>> findProductsByActivityCommonId(Map<String, Object> params)
    {
        List<Map<String, Object>> result = activitiesCommonDao.findProductsByActivityCommonId(params);
        for (Map<String, Object> map : result)
        {
            float salesPrice = Float.valueOf(map.get("salesPrice").toString());
            float commision = Float.valueOf(map.get("commision").toString());
            NumberFormat nt = NumberFormat.getPercentInstance();
            // 设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(2);
            map.put("commisionPercent", nt.format(salesPrice > 0 ? commision / salesPrice : 0));
        }
        return result;
    }
    
    @Override
    public int countProductsByActivityCommonId(int activityCommonId)
    {
        return activitiesCommonDao.countProductsByActivityCommonId(activityCommonId);
    }
}
