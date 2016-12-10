package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.GlobalSaleWindowDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.GlobalSaleWindowEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.GlobalSaleWindowServcie;

@Service("globalSaleWindowServcie")
public class GlobalSaleWindowServcieImpl implements GlobalSaleWindowServcie
{
    
    Logger log = Logger.getLogger(GlobalSaleWindowServcieImpl.class);
    
    @Resource(name = "globalSaleWindowDao")
    private GlobalSaleWindowDao globalSaleWindowDao = null;
    
    @Resource(name = "productDao")
    private ProductDao productDao = null;
    
    @Resource(name = "activitiesCommonDao")
    private ActivitiesCommonDao activitiesCommonDao = null;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private PageDao pageDao;
    
    @Override
    public int save(GlobalSaleWindowEntity saleWindow)
        throws Exception
    {
        return globalSaleWindowDao.save(saleWindow);
    }
    
    @Override
    public int update(GlobalSaleWindowEntity saleWindow)
        throws Exception
    {
        return globalSaleWindowDao.update(saleWindow);
    }
    
    @Override
    public int countSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return globalSaleWindowDao.countSaleWindow(para);
    }
    
    @Override
    public List<GlobalSaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return globalSaleWindowDao.findAllSaleWindow(para);
    }
    
    @Override
    public GlobalSaleWindowEntity findSaleWindowById(int id)
        throws Exception
    {
        return globalSaleWindowDao.findSaleWindowById(id);
    }
    
    @Override
    public int countStock(int type, int displayId)
        throws Exception
    {
        List<Integer> ids = new ArrayList<Integer>();
        if (type == 1)
        {// 单品
            ids.add(displayId);
        }
        else if (type == 2)
        {// 组合
            ids = activitiesCommonDao.findAllProductIdByActivitiesCommonId(displayId);
        }
        else if (type == 3)
        {// 自定义
            return 0;
        }
        else
        {
            return 0;
        }
        if (ids.size() < 1)
        {
            log.warn("ids为空,type:" + type + "displayId:" + displayId);
            return 0;
        }
        return productDao.countStockByProductIds(ids);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return globalSaleWindowDao.updateDisplayCode(para);
    }
    
    @Override
    public List<Map<String, Object>> packageSaleWindowList(List<GlobalSaleWindowEntity> saleWindowList, int type, int running)
        throws Exception
    {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (GlobalSaleWindowEntity saleWindowEntity : saleWindowList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", saleWindowEntity.getId());
            map.put("desc", saleWindowEntity.getDesc());
            StringBuffer descToWeb = new StringBuffer();
            String relaRealSellerName = "";// 单品或者组合 所关联的商家名称
            if (saleWindowEntity.getType() == 1)
            {
                // 1 获取访问链接
                descToWeb.append("http://m.gegejia.com/item-").append(saleWindowEntity.getDisplayId()).append(".htm");
                
                // 2 获得商家名称
                List<Integer> idList = new ArrayList<Integer>();
                idList.add(saleWindowEntity.getDisplayId());
                List<String> sellerNameList = productDao.findRealSellerNameByProductIdList(idList);
                if (sellerNameList.size() > 0)
                {
                    relaRealSellerName = sellerNameList.get(0);
                }
            }
            else if (saleWindowEntity.getType() == 2)
            {
                // 1 获取访问链接
                descToWeb.append("http://m.gegejia.com/sale-").append(saleWindowEntity.getDisplayId()).append(".htm");
                // 2 获得商家名称
                List<Integer> idList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(saleWindowEntity.getDisplayId());
                if (idList.size() > 0)
                {
                    List<String> sellerNameList = productDao.findRealSellerNameByProductIdList(idList);
                    for (String name : sellerNameList)
                    {
                        relaRealSellerName += (name + ";");
                    }
                }
            }
            else if (saleWindowEntity.getType() == 3)
            {
                Map<String, Object> customMap = customActivitiesDao.findCustomActivitiesById(saleWindowEntity.getDisplayId());
                if (customMap == null)
                {
                    descToWeb.append("#");
                }
                else
                {
                    descToWeb.append(customMap.get("shareURL"));
                }
                relaRealSellerName = "-";
            }
            String show = "<a target='_blank' href='" + descToWeb.toString() + "'>" + "查看" + "</a>";
            map.put("descToWeb", show);
            map.put("relaRealSellerName", relaRealSellerName);
            map.put("displayId", saleWindowEntity.getDisplayId());
            map.put("order", (saleWindowEntity.getOrder() == 0) ? "无" : saleWindowEntity.getOrder());
            map.put("isDisplay", (saleWindowEntity.getIsDisplay() == 1) ? "展现" : "不展现");
            map.put("isDisplayCode", saleWindowEntity.getIsDisplay());
            // 特卖状态
            String timePostfix = " 100000";
            String startTime = saleWindowEntity.getStartTime() + timePostfix;// 20150302
            String endTime = saleWindowEntity.getEndTime() + timePostfix;// 20150331
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTime startTime_dateTime = new DateTime(sdf.parse(startTime));
            DateTime endTime_dateTime = new DateTime(sdf.parse(endTime));
            // 未开始
            if (startTime_dateTime.isAfterNow())
            {
                map.put("saleStatus", "即将开始");
            }
            else if (startTime_dateTime.isBeforeNow() && endTime_dateTime.isAfterNow())
            {
                map.put("saleStatus", "进行中");
            }
            else
            {
                map.put("saleStatus", "已结束");
            }
            if (saleWindowEntity.getType() == 1)
            {
                map.put("type", "单品");
            }
            else if (saleWindowEntity.getType() == 2)
            {
                map.put("type", "组合");
            }
            else if (saleWindowEntity.getType() == 3)
            {
                map.put("type", "自定义活动");
            }
            else if (saleWindowEntity.getType() == 4)
            {
                map.put("type", "原生自定义页面");
            }
            if (saleWindowEntity.getType() == 1)
            {
                map.put("baseId", productDao.findProductByID(saleWindowEntity.getDisplayId(), ProductEnum.PRODUCT_TYPE.SALE.getCode()) == null ? -1
                    : productDao.findProductByID(saleWindowEntity.getDisplayId(), ProductEnum.PRODUCT_TYPE.SALE.getCode()).getProductBaseId());
            }
            else
            {
                map.put("baseId", -1);
            }
            map.put("typeCode", saleWindowEntity.getType());
            map.put("name", saleWindowEntity.getName());
            // 库存数量
            map.put("stock", countStock(saleWindowEntity.getType(), saleWindowEntity.getDisplayId()));
            map.put("startTime", startTime_dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", endTime_dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            resultList.add(map);
        }
        return resultList;
    }
    
    @Override
    public Map<String, Object> checkProductTime(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String productStr = "";
        resultMap.put("status", 0);
        int type = (int)para.get("type");// 1单品；2组合特卖；3自定义特卖
        int subjectId = (int)para.get("subjectId");
        String startTime = para.get("startTime") == null ? null : para.get("startTime") + ".0";
        String endTime = para.get("endTime") == null ? null : para.get("endTime") + ".0";
        boolean result = true;
        if (type == 1)
        {
            // 检查单品是否设置了时间
            ProductEntity product = productDao.findProductSaleTimeById(subjectId);
            if (product.getStartTime() == null || product.getEndTime() == null)
            {
                result = false;
            }
            if (startTime != null && endTime != null)
            {
                if (!product.getStartTime().equals(startTime) || !product.getEndTime().equals(endTime))
                {
                    result = false;
                }
            }
            BigDecimal partnerDistributionPrice = new BigDecimal(product.getPartnerDistributionPrice());
            BigDecimal salePrice = new BigDecimal(product.getSalesPrice());
            if (partnerDistributionPrice.compareTo(salePrice.multiply(new BigDecimal(0.6))) < 0 || partnerDistributionPrice.compareTo(salePrice) > 0)
            {
                productStr = productStr + product.getId();
            }
        }
        else if (type == 2)
        {
            List<Integer> productIds = activitiesCommonDao.findAllProductIdByActivitiesCommonId(subjectId);
            for (int i = 0; i < productIds.size(); i++)
            {
                int productId = productIds.get(i);
                ProductEntity product = productDao.findProductSaleTimeById(productId);
                if (product.getStartTime() == null || product.getEndTime() == null)
                {
                    result = false;
                    break;
                }
                if (startTime != null && endTime != null)
                {
                    if (!product.getStartTime().equals(startTime) || !product.getEndTime().equals(endTime))
                    {
                        result = false;
                        break;
                    }
                }
                BigDecimal partnerDistributionPrice = new BigDecimal(product.getPartnerDistributionPrice());
                BigDecimal salePrice = new BigDecimal(product.getSalesPrice());
                if (partnerDistributionPrice.compareTo(salePrice.multiply(new BigDecimal(0.6))) < 0 || partnerDistributionPrice.compareTo(salePrice) > 0)
                {
                    if ("".equals(productStr))
                    {
                        productStr = productStr + productId;
                    }
                    else
                    {
                        productStr = productStr + "," + productId;
                    }
                }
            }
        }
        else if (type == 3)
        {
            result = true;
        }
        else if (type == 4)
        {
            result = true;
        }
        else
        {
            result = false;
        }
        if (!result)
        {
            resultMap.put("msg", "还有关联商品未设置时间或者时间与特卖时间不一致，请先修改它们的时间");
            return resultMap;
        }
        else
        {
            if (!"".equals(productStr))
            {
                resultMap.put("msg", "Id为[" + productStr + "]的特卖商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
                return resultMap;
            }
            resultMap.put("status", 1);
            return resultMap;
        }
    }
    
    @Override
    public int hideSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return globalSaleWindowDao.hideSaleWindow(para);
    }
    
    @Override
    public boolean checkIsExist(Map<String, Object> para)
        throws Exception
    {
        int type = (int)para.get("type");// 1单品；2组合特卖；3自定义特卖
        int subjectId = (int)para.get("subjectId");
        if (type == 1)
        {
            ProductEntity product = productDao.findProductByID(subjectId, ProductEnum.PRODUCT_TYPE.SALE.getCode());
            return product != null;
        }
        else if (type == 2)
        {
            ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(subjectId);
            return ac != null;
        }
        else if (type == 3)
        {
            Map<String, Object> customActivitiesMap = customActivitiesDao.findCustomActivitiesById(subjectId);
            return customActivitiesMap != null;
        }
        else if (type == 4)
        {
            Map<String, Object> pageMap = pageDao.findPageById(subjectId);
            return pageMap != null;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public Map<String, Object> findAllSaleWindowNew(Map<String, Object> para)
        throws Exception
    {
        int type = (int)para.get("type");
        int total = 0;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        
        //所有特卖位为单品
        if (type == 1)
        {
            List<GlobalSaleWindowEntity> singleSaleWindowList = globalSaleWindowDao.findAllSingleSaleWindow(para);
            resultList = packageSaleWindowList(singleSaleWindowList, 1, 0);
            total = globalSaleWindowDao.countSingleSaleWindow(para);
        }
        //特卖位为组合
        else if (type == 2)
        {
            List<GlobalSaleWindowEntity> groupSaleWindowList = globalSaleWindowDao.findAllGroupSaleWinodw(para);
            resultList = packageSaleWindowList(groupSaleWindowList, 1, 0);
            total = globalSaleWindowDao.countGroupSaleWinodw(para);
        }
        //特卖位为自定义活动
        else if (type == 3 || type == 4)
        {
            List<GlobalSaleWindowEntity> customSaleWindowList = globalSaleWindowDao.findAllSaleWindow(para);
            resultList = packageSaleWindowList(customSaleWindowList, 1, 0);
            total = globalSaleWindowDao.countSaleWindow(para);
        }
        else
        {
            para.remove("type");
            int start = Integer.valueOf(para.get("start") + "").intValue();
            int max = Integer.valueOf(para.get("max") + "").intValue();
            int singleSaleTotal = globalSaleWindowDao.countSingleSaleWindow(para);
            int groupSaleTotal = 0;
            
            List<GlobalSaleWindowEntity> singleSaleWindowList = globalSaleWindowDao.findAllSingleSaleWindow(para);
            resultList = packageSaleWindowList(singleSaleWindowList, 1, 0);
            
            boolean isNeedFindOtherType = false;
            if (resultList.size() == 0)
            {
                start = start - singleSaleTotal;
                para.put("start", start);
                isNeedFindOtherType = true;
            }
            else if (resultList.size() < max)
            {
                max = max - (singleSaleTotal % max);
                para.put("start", 0);
                para.put("max", max);
                isNeedFindOtherType = true;
            }
            if (isNeedFindOtherType)
            {
                List<GlobalSaleWindowEntity> groupSaleWindowList = globalSaleWindowDao.findAllGroupSaleWinodw(para);
                List<Map<String, Object>> groupSaleList = packageSaleWindowList(groupSaleWindowList, 1, 0);
                resultList.addAll(groupSaleList);
            }
            groupSaleTotal = globalSaleWindowDao.countGroupSaleWinodw(para);
            total = singleSaleTotal + groupSaleTotal;
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }
    
}
