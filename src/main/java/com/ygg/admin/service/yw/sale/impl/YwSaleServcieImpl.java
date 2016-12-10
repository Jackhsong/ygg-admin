package com.ygg.admin.service.yw.sale.impl;

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
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.code.UrlEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.yw.sale.YwSaleDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.yw.YwSaleEntity;
import com.ygg.admin.service.yw.sale.YwSaleServcie;

@Service("ywSaleServcie")
public class YwSaleServcieImpl implements YwSaleServcie
{
    
    Logger log = Logger.getLogger(YwSaleServcieImpl.class);
    
    @Resource(name = "ywSaleDao")
    private YwSaleDao ywSaleDao = null;
    
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
    public int save(YwSaleEntity saleWindow)
        throws Exception
    {
        return ywSaleDao.save(saleWindow);
    }
    
    @Override
    public int update(YwSaleEntity saleWindow)
        throws Exception
    {
        return ywSaleDao.update(saleWindow);
    }
    
    @Override
    public int countSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return ywSaleDao.countSaleWindow(para);
    }
    
    @Override
    public List<YwSaleEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return ywSaleDao.findAllSaleWindow(para);
    }
    
    @Override
    public YwSaleEntity findSaleWindowById(int id)
        throws Exception
    {
        return ywSaleDao.findSaleWindowById(id);
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
        return ywSaleDao.updateDisplayCode(para);
    }
    
    @Override
    public List<Map<String, Object>> packageSaleWindowList(List<YwSaleEntity> ywSaleList, int type, int running)
        throws Exception
    {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (YwSaleEntity ywSaleEntity : ywSaleList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", ywSaleEntity.getId());
            map.put("desc", ywSaleEntity.getDesc());
            StringBuffer descToWeb = new StringBuffer();
            //新增行动派链接
            StringBuffer urlForYw = new StringBuffer();
            String relaRealSellerName = "";// 单品或者组合 所关联的商家名称
            int disPlayId = ywSaleEntity.getDisplayId();
            if (ywSaleEntity.getType() == 1)
            {
                // 1 获取访问链接
                descToWeb.append(UrlEnum.SinglePro.URL).append(disPlayId).append(".htm");
                //左岸城堡单品链接
                urlForYw.append(UrlEnum.SingleProYw.URL).append(disPlayId);
                // 2 获得商家名称
                List<Integer> idList = new ArrayList<Integer>();
                idList.add(disPlayId);
                List<String> sellerNameList = productDao.findRealSellerNameByProductIdList(idList);
                if (sellerNameList.size() > 0)
                {
                    relaRealSellerName = sellerNameList.get(0);
                }
            }
            else if (ywSaleEntity.getType() == 2)
            {
                // 1 获取访问链接
                descToWeb.append(UrlEnum.GroupPro.URL).append(disPlayId).append(".htm");
                //左岸城堡组合链接
                urlForYw.append(UrlEnum.GroupProYw.URL).append(disPlayId);
                // 2 获得商家名称
                List<Integer> idList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(disPlayId);
                if (idList.size() > 0)
                {
                    List<String> sellerNameList = productDao.findRealSellerNameByProductIdList(idList);
                    for (String name : sellerNameList)
                    {
                        relaRealSellerName += (name + ";");
                    }
                }
            }
            else if (ywSaleEntity.getType() == 3)
            {
                Map<String, Object> customMap = customActivitiesDao.findCustomActivitiesById(disPlayId);
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
            String qqbsUrl = "<a target='_blank' href='" + urlForYw.toString() + "'>" + "燕网链接" + "</a>";
            map.put("descToWeb", show);
            map.put("urlForQqbs", qqbsUrl);
            map.put("relaRealSellerName", relaRealSellerName);
            map.put("displayId", disPlayId);
            map.put("order", (ywSaleEntity.getOrder() == 0) ? "无" : ywSaleEntity.getOrder());
            map.put("isDisplay", (ywSaleEntity.getIsDisplay() == 1) ? "展现" : "不展现");
            map.put("isDisplayCode", ywSaleEntity.getIsDisplay());
            // 特卖状态
            String timePostfix = " 100000";
            if (ywSaleEntity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
            {
                timePostfix = " 200000";
            }
            String startTime = ywSaleEntity.getStartTime() + timePostfix;// 20150302
            String endTime = ywSaleEntity.getEndTime() + timePostfix;// 20150331
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
            if (ywSaleEntity.getType() == 1)
            {
                map.put("type", "单品");
            }
            else if (ywSaleEntity.getType() == 2)
            {
                map.put("type", "组合");
            }
            else if (ywSaleEntity.getType() == 3)
            {
                map.put("type", "自定义活动");
            }
            else if (ywSaleEntity.getType() == 4)
            {
                map.put("type", "原生自定义页面");
            }
            if (ywSaleEntity.getType() == 1)
            {
                map.put("baseId", productDao.findProductByID(ywSaleEntity.getDisplayId(), ProductEnum.PRODUCT_TYPE.SALE.getCode()) == null ? -1
                    : productDao.findProductByID(ywSaleEntity.getDisplayId(), ProductEnum.PRODUCT_TYPE.SALE.getCode()).getProductBaseId());
            }
            else
            {
                map.put("baseId", -1);
            }
            String saleTimeTypeStr = "";
            if (ywSaleEntity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
            {
                saleTimeTypeStr = SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getDesc();
            }
            else if (ywSaleEntity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
            {
                saleTimeTypeStr = SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getDesc();
            }
            map.put("saleTimeTypeStr", saleTimeTypeStr);
            map.put("typeCode", ywSaleEntity.getType());
            map.put("name", ywSaleEntity.getName());
            // 库存数量
            map.put("stock", countStock(ywSaleEntity.getType(), ywSaleEntity.getDisplayId()));
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
        if (!"".equals(productStr))
        {
            resultMap.put("msg", "Id为[" + productStr + "]的特卖商品分销供货价必须大于或等于售价的60%并且小于或等于售价，否则无法保存");
            return resultMap;
        }
        resultMap.put("status", 1);
        return resultMap;
    }
    
    @Override
    public int hideSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return ywSaleDao.hideSaleWindow(para);
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
            List<YwSaleEntity> singleSaleWindowList = ywSaleDao.findAllSingleSaleWindow(para);
            resultList = packageSaleWindowList(singleSaleWindowList, 1, 0);
            total = ywSaleDao.countSingleSaleWindow(para);
        }
        //特卖位为组合
        else if (type == 2)
        {
            List<YwSaleEntity> groupSaleWindowList = ywSaleDao.findAllGroupSaleWinodw(para);
            resultList = packageSaleWindowList(groupSaleWindowList, 1, 0);
            total = ywSaleDao.countGroupSaleWinodw(para);
        }
        //特卖位为自定义活动
        else if (type == 3 || type == 4)
        {
            List<YwSaleEntity> customSaleWindowList = ywSaleDao.findAllSaleWindow(para);
            resultList = packageSaleWindowList(customSaleWindowList, 1, 0);
            total = ywSaleDao.countSaleWindow(para);
        }
        else if(para.containsKey("brandId") || para.containsKey("productName") || para.containsKey("sellerId")
            || para.containsKey("pId") || para.containsKey("productId"))
        {
            System.out.println(para);
            para.remove("type");
            int start = Integer.valueOf(para.get("start") + "").intValue();
            int max = Integer.valueOf(para.get("max") + "").intValue();
            int singleSaleTotal = ywSaleDao.countSingleSaleWindow(para);
            int groupSaleTotal = 0;
            
            List<YwSaleEntity> singleSaleWindowList = ywSaleDao.findAllSingleSaleWindow(para);
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
                List<YwSaleEntity> groupSaleWindowList = ywSaleDao.findAllGroupSaleWinodw(para);
                List<Map<String, Object>> groupSaleList = packageSaleWindowList(groupSaleWindowList, 1, 0);
                resultList.addAll(groupSaleList);
            }
            groupSaleTotal = ywSaleDao.countGroupSaleWinodw(para);
            total = singleSaleTotal + groupSaleTotal;
        } else {
            List<YwSaleEntity> saleWindowList = ywSaleDao.findAllSaleWindowByParam(para);
            resultList = packageSaleWindowList(saleWindowList, 1, 0);
            total = ywSaleDao.countAllSaleWindowByParam(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return resultMap;
    }

    @Override
    public int updateOrder(int id, int order)
    {
        return ywSaleDao.updateOrder(id, order);
    }
    
}
