/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.service.categorysale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.categorysale.CategorySaleDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.entity.SaleWindowEntity;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.categorysale.CategorySaleService;
import com.ygg.admin.util.CommonUtil;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategorySaleServiceImpl.java 10216 2016-04-14 01:54:28Z xiongliang $   
  * @since 2.0
  */
@Service("categorySaleService")
public class CategorySaleServiceImpl implements CategorySaleService
{
    Logger log = Logger.getLogger(CategorySaleServiceImpl.class);
    
    /**    */
    @Resource(name = "categorySaleDao")
    private CategorySaleDao categorySaleDao;
    
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
    
    /**商品分类服务    */
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    
    @Resource(name = "saleWindowDao")
    private SaleWindowDao saleWindowDao;
    
    @Override
    public int save(SaleWindowEntity saleWindow)
        throws Exception
    {
        return categorySaleDao.save(saleWindow);
    }
    
    @Override
    public int update(SaleWindowEntity saleWindow)
        throws Exception
    {
        return categorySaleDao.update(saleWindow);
    }
    
    @Override
    public SaleWindowEntity findSaleWindowById(int id)
        throws Exception
    {
        return categorySaleDao.findSaleWindowById(id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return categorySaleDao.updateDisplayCode(para);
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
            // TODO 获取自定义特卖的数据 
            result = true;
        }
        else if (type == 4)
        {
            // TODO 自定义页面
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
    public String findCategorySaleWindow(int start, int max, int saleStatus, String saleName, int page2ModelId, int categoryFirstId, int productId, String productName,
        int brandId, int sellerId, int type, int isDisplay, String startTime)
        throws Exception
    
    {
        Map<String, Object> resultMap = new HashMap<>();
        
        Map<String, Object> params = new HashMap<>();
        params.put("saleStatus", saleStatus);
        params.put("page2ModelId", page2ModelId);
        params.put("categoryFirstId", categoryFirstId);
        
        //需要关联商品表查询的部分条件
        Map<String, Object> productPara = new HashMap<>();
        if (productId != 0)
        {
            productPara.put("productId", productId);
        }
        if (!productName.isEmpty())
        {
            productPara.put("productName", "%" + productName + "%");
        }
        if (sellerId != -1)
        {
            productPara.put("sellerId", sellerId);
        }
        if (brandId != -1)
        {
            productPara.put("brandId", brandId);
        }
        
        if (!productPara.isEmpty())
        {
            if (type == -1)
            {
                Set<Integer> saleWindowIds = new HashSet<Integer>();
                saleWindowIds.addAll(saleWindowDao.findSaleWindowSingleIdsByPara(productPara));
                saleWindowIds.addAll(saleWindowDao.findSaleWindowGroupIdsByPara(productPara));
                if (saleWindowIds.isEmpty())
                {
                    return JSON.toJSONString(ResultEntity.getFailResultList());
                }
                params.put("ids", saleWindowIds);
            }
            else if (type == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
            {
                List<Integer> ids = saleWindowDao.findSaleWindowSingleIdsByPara(productPara);
                if (ids.isEmpty())
                {
                    return JSON.toJSONString(ResultEntity.getFailResultList());
                }
                params.put("ids", ids);
            }
            else if (type == SaleWindowEnum.SALE_TYPE.ACTIVITIES_COMMON.getCode())
            {
                List<Integer> ids = saleWindowDao.findSaleWindowGroupIdsByPara(productPara);
                if (ids.isEmpty())
                {
                    return JSON.toJSONString(ResultEntity.getFailResultList());
                }
                params.put("ids", ids);
            }
            else if (type == SaleWindowEnum.SALE_TYPE.WEB_CUSTOM_ACTIVITY.getCode())
            {
                return JSON.toJSONString(ResultEntity.getFailResultList());
            }
            else if (type == SaleWindowEnum.SALE_TYPE.APP_CUSTOM_ACTIVITY.getCode())
            {
                return JSON.toJSONString(ResultEntity.getFailResultList());
            }
        }
        
        if (!saleName.isEmpty())
        {
            params.put("name", "%" + saleName + "%");
        }
        if (isDisplay != -1)
        {
            params.put("isDisplay", isDisplay);
        }
        if (type != -1)
        {
            params.put("type", type);
        }
        if (!startTime.isEmpty())
        {
            params.put("startTime", startTime);
        }
        
        boolean beforeTen = DateTime.now().getHourOfDay() >= 10 ? false : true;
        int currentTime = 0;
        if (beforeTen)
        {
            currentTime = Integer.parseInt(DateTime.now().minusDays(1).toString("yyyyMMdd"));
        }
        else
        {
            currentTime = Integer.parseInt(DateTime.now().toString("yyyyMMdd"));
        }
        params.put("currentTime", currentTime);
        
        //查询首页特卖
        params.put("sourceType", 1);
        List<SaleWindowEntity> sws1 = saleWindowDao.findSaleWindowListByPara(params);
        int total1 = saleWindowDao.countSaleWindowByPara(params);
        
        //查询品类馆特卖
        params.put("sourceType", 2);
        List<SaleWindowEntity> sws2 = categorySaleDao.findCategorySaleWindowByPara(params);
        int total2 = categorySaleDao.countCategorySaleWindowByPara(params);
        
        List<SaleWindowEntity> rows = new LinkedList<>();
        rows.addAll(sws1);
        rows.addAll(sws2);
        sort(rows);
        resultMap.put("rows", packageData(rows.subList(start, max > rows.size() ? rows.size() : max)));
        resultMap.put("total", total1 + total2);
        return JSON.toJSONString(resultMap);
    }
    
    private void sort(List<SaleWindowEntity> rows)
    {
        int totalSize = rows.size();
        Collections.sort(rows, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity o1, SaleWindowEntity o2)
            {
                return o2.getCategoryOrder() - o1.getCategoryOrder();
            }
        });
        
        List<SaleWindowEntity> lockedList = new ArrayList<SaleWindowEntity>();
        Iterator<SaleWindowEntity> iterator = rows.iterator();
        while (iterator.hasNext())
        {
            SaleWindowEntity swe = iterator.next();
            if (swe.getCategoryLockIndex() > 0)
            {
                lockedList.add(swe);
                iterator.remove();
            }
        }
        
        Collections.sort(lockedList, new Comparator<SaleWindowEntity>()
        {
            
            @Override
            public int compare(SaleWindowEntity o1, SaleWindowEntity o2)
            {
                return o1.getCategoryLockIndex() - o2.getCategoryLockIndex();
            }
            
        });
        for (SaleWindowEntity swe : lockedList)
        {
            if (swe.getCategoryLockIndex() > totalSize)
            {
                rows.add(totalSize - 1, swe);
            }
            else
            {
                rows.add(swe.getCategoryLockIndex() - 1, swe);
            }
        }
    }
    
    @Override
    public String updateCategoryOrderAndIndex(int id, int categoryOrder, int categoryLockIndex)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        
        if (categoryLockIndex < 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "锁定序位不能为负数");
            return JSON.toJSONString(resultMap);
        }
        
        SaleWindowEntity swe = categorySaleDao.findSaleWindowById(id);
        if (swe == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
            return JSON.toJSONString(resultMap);
        }
        
        boolean updateLockIndex = false;
        swe.setCategoryOrder(categoryOrder);
        
        //只有品类馆中正在进行的特卖才能修改锁定排序
        if (swe.getCategoryLockIndex() != categoryLockIndex)
        {
            if (swe.getSourceType() == 2 && CommonUtil.getSaleWindowStatus(swe) == SaleWindowEnum.SALE_WINDOW_STATUS.IN_PROGRESS.getCode())
            {
                swe.setCategoryLockIndex(categoryLockIndex);
                updateLockIndex = true;
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "只有品类馆中正在进行的特卖才能修改锁定排序");
                return JSON.toJSONString(resultMap);
            }
        }
        
        if (categorySaleDao.update(swe) > 0)
        {
            if (updateLockIndex)
            {
                List<SaleWindowEntity> swes = categorySaleDao.findCategorySaleWindowGreatLockIndex(id, categoryLockIndex, swe.getPage2ModelId());
                reindex(swes, categoryLockIndex);
                if (!swes.isEmpty())
                {
                    categorySaleDao.updateCategoryLockIndex(swes);
                }
            }
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    private void reindex(List<SaleWindowEntity> swes, int lockIndex)
    {
        if (swes.isEmpty())
        {
            return;
        }
        for (int i = 0; i < swes.size(); i++)
        {
            int index = i + lockIndex;
            SaleWindowEntity swe = swes.get(i);
            if (swe.getCategoryLockIndex() == index)
            {
                swe.setCategoryLockIndex(index + 1);
            }
        }
    }
    
    @Override
    public String deleteCategorySale(int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        if (categorySaleDao.deleteCategorySale(id) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "删除成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> packageData(List<SaleWindowEntity> sws)
        throws Exception
    {
        List<Map<String, Object>> datas = new ArrayList<>();
        List<Integer> sweIds = new ArrayList<>();
        List<Integer> sweIdsType1 = new ArrayList<>();
        List<Integer> sweIdsType2 = new ArrayList<>();
        for (SaleWindowEntity sw : sws)
        {
            sweIds.add(sw.getId());
            if (sw.getType() == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
            {
                sweIdsType1.add(sw.getId());
            }
            else if (sw.getType() == SaleWindowEnum.SALE_TYPE.ACTIVITIES_COMMON.getCode())
            {
                sweIdsType2.add(sw.getId());
            }
        }
        
        //查询一级分类名称
        List<Map<String, Object>> categorys = new ArrayList<>();
        if (!sweIds.isEmpty())
        {
            categorys.addAll(saleWindowDao.findCategoryFirstNamesBySwids(sweIds));
        }
        Map<String, String> categoryMap = new HashMap<>();
        for (Map<String, Object> category : categorys)
        {
            categoryMap.put(category.get("id").toString(), category.get("categoryName") == null ? "" : category.get("categoryName").toString());
        }
        
        //查询特卖(单品、组合)商家名称
        List<Map<String, Object>> sellerNames = new ArrayList<>();
        if (!sweIdsType1.isEmpty())
        {
            sellerNames.addAll(saleWindowDao.findSellerNameBySingleSwids(sweIdsType1));
        }
        if (!sweIdsType2.isEmpty())
        {
            sellerNames.addAll(saleWindowDao.findSellerNameByGroupSwids(sweIdsType2));
        }
        Map<String, String> sellerNameMap = new HashMap<>();
        for (Map<String, Object> seller : sellerNames)
        {
            sellerNameMap.put(seller.get("id").toString(), seller.get("sellerName") == null ? "" : seller.get("sellerName").toString());
        }
        
        //查询特卖单品库存
        List<Map<String, Object>> stocks = new ArrayList<>();
        if (!sweIdsType1.isEmpty())
        {
            stocks.addAll(saleWindowDao.findStockBySingleSwids(sweIdsType1));
        }
        Map<String, String> stockMap = new HashMap<>();
        for (Map<String, Object> stock : stocks)
        {
            stockMap.put(stock.get("id").toString(), stock.get("stock") == null ? "0" : stock.get("stock").toString());
        }
        
        for (SaleWindowEntity swe : sws)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.putAll(new BeanMap(swe));
            map.put("categoryFirstName", categoryMap.get(swe.getId() + "") == null ? "" : categoryMap.get(swe.getId() + ""));
            map.put("relaRealSellerName", sellerNameMap.get(swe.getId() + "") == null ? "" : sellerNameMap.get(swe.getId() + ""));
            map.put("stock", stockMap.get(swe.getId() + "") == null ? "0" : stockMap.get(swe.getId() + ""));
            map.put("nowOrder", swe.getNowOrder() == 0 ? "无" : swe.getNowOrder());
            map.put("laterOrder", swe.getLaterOrder() == 0 ? "无" : swe.getLaterOrder());
            map.put("saleType", SaleWindowEnum.SALE_TYPE.getDescByCode(swe.getType()));
            map.put("saleTimeType", SaleWindowEnum.SALE_TIME_TYPE.getDescByCode(swe.getSaleTimeType()));
            
            if (swe.getType() == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
            {
                map.put("url", "http://m.gegejia.com/item-" + swe.getDisplayId() + ".htm");
            }
            else if (swe.getType() == SaleWindowEnum.SALE_TYPE.ACTIVITIES_COMMON.getCode())
            {
                map.put("url", "http://m.gegejia.com/sale-" + swe.getDisplayId() + ".htm");
            }
            else if (swe.getType() == SaleWindowEnum.SALE_TYPE.WEB_CUSTOM_ACTIVITY.getCode())
            {
                map.put("url", "#");
            }
            else if (swe.getType() == SaleWindowEnum.SALE_TYPE.APP_CUSTOM_ACTIVITY.getCode())
            {
                map.put("url", "#");
            }
            
            String suffix = "";
            if (swe.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
            {
                suffix = "100000";
            }
            else if (swe.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
            {
                suffix = "200000";
            }
            
            DateTime startTime = DateTime.parse(swe.getStartTime() + suffix, DateTimeFormat.forPattern("yyyyMMddHHmmss"));
            DateTime endTime = DateTime.parse(swe.getEndTime() + suffix, DateTimeFormat.forPattern("yyyyMMddHHmmss")).plusDays(1);
            
            if (startTime.isAfterNow())
            {
                map.put("saleStatus", "即将开始");
            }
            else if (startTime.isBeforeNow() && endTime.isAfterNow())
            {
                map.put("saleStatus", "进行中");
            }
            else
            {
                map.put("saleStatus", "已结束");
            }
            
            map.put("startTime", startTime.toString("yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", endTime.toString("yyyy-MM-dd HH:mm:ss"));
            
            datas.add(map);
        }
        return datas;
    }
}
