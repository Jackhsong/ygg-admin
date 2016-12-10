package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SaleWindowEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SaleWindowDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.CategoryFirstEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.entity.SaleWindowEntity;
import com.ygg.admin.service.CategoryService;
import com.ygg.admin.service.SaleWindowServcie;
import com.ygg.admin.util.CommonUtil;

@Service("saleWindowServcie")
public class SaleWindowServcieImpl implements SaleWindowServcie
{
    
    Logger log = Logger.getLogger(SaleWindowServcieImpl.class);
    
    @Resource(name = "saleWindowDao")
    private SaleWindowDao saleWindowDao = null;
    
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
    
    /** 商品分类服务 */
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    
    @Override
    public int save(SaleWindowEntity saleWindow)
        throws Exception
    {
        return saleWindowDao.save(saleWindow);
    }
    
    @Override
    public int update(SaleWindowEntity saleWindow)
        throws Exception
    {
        return saleWindowDao.update(saleWindow);
    }
    
    @Override
    public int countSaleWindow(Map<String, Object> para)
        throws Exception
    {
        int count = saleWindowDao.countSaleWindow(para);
        int saleTimeType = DateTime.now().getHourOfDay() < 10 || DateTime.now().getHourOfDay() >= 20 ? 2 : 1;
        if (saleTimeType == 1)
        {
            para.put("endTime", CommonUtil.getNowSaleDateNight());
            count = count + saleWindowDao.countSaleWindow(para);
        }
        return count;
    }
    
    @Override
    public List<SaleWindowEntity> findAllSaleWindow(Map<String, Object> para)
        throws Exception
    {
        List<SaleWindowEntity> list = saleWindowDao.findAllSaleWindow(para);
        int saleTimeType = DateTime.now().getHourOfDay() < 10 || DateTime.now().getHourOfDay() >= 20 ? 2 : 1;
        if (saleTimeType == 1)
        {
            para.put("endTime", CommonUtil.getNowSaleDateNight());
            list.addAll(saleWindowDao.findAllSaleWindow(para));
        }
        return list;
    }
    
    @Override
    public SaleWindowEntity findSaleWindowById(int id)
        throws Exception
    {
        return saleWindowDao.findSaleWindowById(id);
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
        return saleWindowDao.updateDisplayCode(para);
    }
    
    @Override
    public List<Map<String, Object>> packageSaleWindowList(List<SaleWindowEntity> saleWindowList, int type, int running)
        throws Exception
    {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (SaleWindowEntity saleWindowEntity : saleWindowList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", saleWindowEntity.getId());
            map.put("desc", saleWindowEntity.getDesc());
            CategoryFirstEntity categoryFirstEntity = categoryService.findCategoryFirstById(saleWindowEntity.getCategoryFirstId());
            if (categoryFirstEntity != null)
            {
                map.put("categoryFirstName", categoryFirstEntity.getName());
            }
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
            if (type == 1)
            {
                map.put("order", (saleWindowEntity.getNowOrder() == 0) ? "无" : saleWindowEntity.getNowOrder());
            }
            else
            {
                map.put("order", (saleWindowEntity.getLaterOrder() == 0) ? "无" : saleWindowEntity.getLaterOrder());
            }
            map.put("nowOrder", (saleWindowEntity.getNowOrder() == 0) ? "无" : saleWindowEntity.getNowOrder());
            map.put("laterOrder", (saleWindowEntity.getLaterOrder() == 0) ? "无" : saleWindowEntity.getLaterOrder());
            map.put("isDisplay", (saleWindowEntity.getIsDisplay() == 1) ? "展现" : "不展现");
            map.put("isDisplayCode", saleWindowEntity.getIsDisplay());
            // 特卖状态
            String timePostfix = " 100000";
            if (saleWindowEntity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
            {
                timePostfix = " 200000";
            }
            String startTime = saleWindowEntity.getStartTime() + timePostfix;// 20150302
            String endTime = saleWindowEntity.getEndTime() + timePostfix;// 20150331
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
            
            DateTime startTime_dateTime = new DateTime(sdf.parse(startTime));
            
            DateTime endTime_dateTime = new DateTime(sdf.parse(endTime)).plusDays(1);
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
            String saleTimeTypeStr = "";
            if (saleWindowEntity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getCode())
            {
                saleTimeTypeStr = SaleWindowEnum.SALE_TIME_TYPE.SALE_10.getDesc();
            }
            else if (saleWindowEntity.getSaleTimeType() == SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getCode())
            {
                saleTimeTypeStr = SaleWindowEnum.SALE_TIME_TYPE.SALE_20.getDesc();
            }
            map.put("saleTimeTypeStr", saleTimeTypeStr);
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
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        int type = (int)para.get("type"); // 1:修改now_order ； 2:修改later_order
        int order = (int)para.get("order");
        int result = 0;
        if (type == 1)
        {
            para.put("nowOrder", order);
            result = saleWindowDao.updateNowOrder(para);
        }
        else if (type == 2)
        {
            para.put("laterOrder", order);
            result = saleWindowDao.updateLaterOrder(para);
        }
        return result;
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
    public int hideSaleWindow(Map<String, Object> para)
        throws Exception
    {
        return saleWindowDao.hideSaleWindow(para);
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
    public int resetTomorrowOrder()
        throws Exception
    {
        int status = 1;
        // 记录开始时间
        long startAt = System.currentTimeMillis();
        String name = "手动更新特卖排序值";
        log.info(name + "开始执行~~~");
        try
        {
            // 执行具体业务逻辑
            int hour = DateTime.now().getHourOfDay();
            boolean beforeTen = (hour < 10) ? true : false;
            if (beforeTen)
            {
                doExecuteBeforeTen();
            }
            else
            {
                doExecuteAfterTen();
            }
            // 记录结束时间
            long endAt = System.currentTimeMillis();
            log.info(name + "执行结束。耗时：" + (endAt - startAt) + "毫秒");
        }
        catch (Exception e)
        {
            log.error(name + "执行失败", e);
            status = 0;
        }
        return status;
    }
    
    private void doExecuteBeforeTen()
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("sourceType", 1);
        para.put("start", 0);
        para.put("max", 1000);
        
        // 得到 今日10点 开始的特卖列表
        int nowIntValue = Integer.valueOf(DateTime.now().toString("yyyyMMdd"));
        para.put("startTime", nowIntValue);
        para.put("saleTimeType", 1);
        List<SaleWindowEntity> tomorrow10start = saleWindowDao.findAllSaleWindow(para);
        
        // 得到 今日20点 开始的特卖列表
        para.put("saleTimeType", 2);
        List<SaleWindowEntity> tomorrow20start = saleWindowDao.findAllSaleWindow(para);
        
        // 得到 已经开始并且明天仍然进行的特卖
        para.remove("saleTimeType");
        para.remove("startTime");
        para.put("status", 2);
        int compareToStart = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        int compareToEnd = Integer.valueOf(DateTime.now().plusDays(-1).toString("yyyyMMdd")).intValue();
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        List<SaleWindowEntity> stillRunning = saleWindowDao.findAllSaleWindow(para);
        
        Collections.sort(tomorrow10start, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        Collections.sort(tomorrow20start, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        Collections.sort(stillRunning, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        int index = updateSaleOrder(2, 1, stillRunning);
        index = updateSaleOrder(2, index, tomorrow10start);
        updateSaleOrder(2, index, tomorrow20start);
    }
    
    private void doExecuteAfterTen()
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("sourceType", 1);
        para.put("start", 0);
        para.put("max", 1000);
        
        // 得到 明日10点 开始的特卖列表
        int nowIntValue = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd"));
        para.put("startTime", nowIntValue);
        para.put("saleTimeType", 1);
        List<SaleWindowEntity> tomorrow10start = saleWindowDao.findAllSaleWindow(para);
        
        // 得到 明日20点 开始的特卖列表
        para.put("saleTimeType", 2);
        List<SaleWindowEntity> tomorrow20start = saleWindowDao.findAllSaleWindow(para);
        
        // 得到 已经开始并且明天仍然进行的特卖
        para.remove("saleTimeType");
        para.remove("startTime");
        para.put("status", 2);
        int compareToStart = Integer.valueOf(DateTime.now().plusDays(1).toString("yyyyMMdd")).intValue();
        int compareToEnd = Integer.valueOf(DateTime.now().toString("yyyyMMdd")).intValue();
        para.put("compareToStart", compareToStart);
        para.put("compareToEnd", compareToEnd);
        List<SaleWindowEntity> stillRunning = saleWindowDao.findAllSaleWindow(para);
        
        Collections.sort(tomorrow10start, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        Collections.sort(tomorrow20start, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        Collections.sort(stillRunning, new Comparator<SaleWindowEntity>()
        {
            @Override
            public int compare(SaleWindowEntity entity1, SaleWindowEntity entity2)
            {
                return entity1.getNowOrder() - entity2.getNowOrder();
            }
        });
        int index = updateSaleOrder(2, 1, stillRunning);
        index = updateSaleOrder(2, index, tomorrow10start);
        updateSaleOrder(2, index, tomorrow20start);
    }
    
    /**
     * 更新order值
     *
     * @param type ： 1，更新今日排序值；2，更新明日排序值。
     * @param index ： 具体排序值
     * @param saleList
     * @return
     * @throws Exception
     */
    private int updateSaleOrder(int type, int index, List<SaleWindowEntity> saleList)
        throws Exception
    {
        for (SaleWindowEntity entity : saleList)
        {
            Map<String, Object> upPara = new HashMap<>();
            upPara.put("id", entity.getId());
            if (type == 1)
            {
                // 更新nowOrder
                upPara.put("nowOrder", index++);
                saleWindowDao.updateNowOrder(upPara);
            }
            else
            {
                // 更新LaterOrder，得判断是否需要更新明日排序值
                int nowIntValue = Integer.valueOf(DateTime.now().toString("yyyyMMdd"));
                if (entity.getEndTime() >= nowIntValue)
                {
                    upPara.put("laterOrder", index++);
                    saleWindowDao.updateLaterOrder(upPara);
                }
                else
                    log.info("跳过更新later...");
            }
        }
        return index;
    }
    
    @Override
    public String jsonSaleWindows(int page, int rows, int saleStatus, String saleName, int categoryFirstId, int productId, String productName, int brandId, int sellerId, int type,
        int isDisplay, String startTime)
        throws Exception
    
    {
        Map<String, Object> params = null;
        try
        {
            params = buildQueryParams(page, rows, saleStatus, saleName, categoryFirstId, productId, productName, brandId, sellerId, type, isDisplay, startTime);
        }
        catch (JumpExceptionWithNothingToFind e)
        {
            return JSON.toJSONString(ResultEntity.getFailResultList());
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", packageData(saleWindowDao.findSaleWindowListByPara(params), 0));
        resultMap.put("total", saleWindowDao.countSaleWindowByPara(params));
        return JSON.toJSONString(resultMap);
    }
    
    private class JumpExceptionWithNothingToFind extends Exception
    {
        
        /**
         * 
         */
        private static final long serialVersionUID = 6356239258489142013L;
        
    }
    
    /**
     * 寻找单品和特卖的id列表
     * 
     * @param productId
     * @param productName
     * @param brandId
     * @param sellerId
     * @param type
     * @return
     * @throws Exception
     */
    private List<Integer> findSaleWindowIds(Map<String, Object> saleWindowIdsQueryParams, int type)
        throws Exception
    {
        if (type == -1)
        {
            Set<Integer> saleWindowIds = new HashSet<Integer>();
            saleWindowIds.addAll(saleWindowDao.findSaleWindowSingleIdsByPara(saleWindowIdsQueryParams));
            saleWindowIds.addAll(saleWindowDao.findSaleWindowGroupIdsByPara(saleWindowIdsQueryParams));
            return new ArrayList<Integer>(saleWindowIds);
        }
        if (type == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
        {
            return saleWindowDao.findSaleWindowSingleIdsByPara(saleWindowIdsQueryParams);
        }
        if (type == SaleWindowEnum.SALE_TYPE.ACTIVITIES_COMMON.getCode())
        {
            return saleWindowDao.findSaleWindowGroupIdsByPara(saleWindowIdsQueryParams);
        }
        return null;
    }
    
    /**
     * 需要关联商品表查询的部分条件
     * 
     * @param productId
     * @param productName
     * @param brandId
     * @param sellerId
     * @return
     */
    private Map<String, Object> buildSaleWindowIdsQueryParams(int productId, String productName, int brandId, int sellerId)
    {
        Map<String, Object> saleWindowIdsQueryParams = new HashMap<>();
        if (productId != 0)
        {
            saleWindowIdsQueryParams.put("productId", productId);
        }
        if (!productName.isEmpty())
        {
            saleWindowIdsQueryParams.put("productName", "%" + productName + "%");
        }
        if (sellerId != -1)
        {
            saleWindowIdsQueryParams.put("sellerId", sellerId);
        }
        if (brandId != -1)
        {
            saleWindowIdsQueryParams.put("brandId", brandId);
        }
        return saleWindowIdsQueryParams;
        
    }
    
    private Map<String, Object> buildQueryParams(int page, int rows, int saleStatus, String saleName, int categoryFirstId, int productId, String productName, int brandId,
        int sellerId, int type, int isDisplay, String startTime)
        throws JumpExceptionWithNothingToFind, Exception
    {
        Map<String, Object> queryPrams = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        queryPrams.put("start", rows * (page - 1));
        queryPrams.put("max", rows);
        queryPrams.put("saleStatus", saleStatus);
        Map<String, Object> saleWindowIdsQueryParams = buildSaleWindowIdsQueryParams(productId, productName, brandId, sellerId);
        if (!saleWindowIdsQueryParams.isEmpty())
        {
            List<Integer> ids = findSaleWindowIds(saleWindowIdsQueryParams, type);
            if (CollectionUtils.isEmpty(ids))
            {
                throw new JumpExceptionWithNothingToFind();
            }
            queryPrams.put("ids", ids);
        }
        
        if (!saleName.isEmpty())
        {
            queryPrams.put("name", "%" + saleName + "%");
        }
        if (isDisplay != -1)
        {
            queryPrams.put("isDisplay", isDisplay);
        }
        if (type != -1)
        {
            queryPrams.put("type", type);
        }
        if (!startTime.isEmpty())
        {
            queryPrams.put("startTime", startTime);
        }
        if (categoryFirstId != -1)
        {
            queryPrams.put("categoryFirstId", categoryFirstId);
        }
        
        boolean beforeTen = DateTime.now().getHourOfDay() < 10;
        int currentTime = 0;
        if (beforeTen)
        {
            currentTime = Integer.parseInt(DateTime.now().minusDays(1).toString("yyyyMMdd"));
        }
        else
        {
            currentTime = Integer.parseInt(DateTime.now().toString("yyyyMMdd"));
        }
        queryPrams.put("currentTime", currentTime);
        
        // 查询首页特卖
        queryPrams.put("sourceType", 1);
        return queryPrams;
    }
    
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> packageData(List<SaleWindowEntity> sws, int type)
        throws Exception
    {
        List<Map<String, Object>> datas = new ArrayList<>();
        List<Integer> sweIds = new ArrayList<>();
        List<Integer> sweIdsType1 = new ArrayList<>();
        List<Integer> sweIdsType2 = new ArrayList<>();
        List<Integer> sweIdsType3 = new ArrayList<>();
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
            else if (sw.getType() == SaleWindowEnum.SALE_TYPE.WEB_CUSTOM_ACTIVITY.getCode())
            {
                sweIdsType3.add(sw.getId());
            }
        }
        
        // 查询一级分类名称
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
        
        // 查询特卖(单品、组合)商家名称
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
        
        // 查询特卖单品库存
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
        
        // 查询特卖单品基本商品Id
        List<Map<String, Object>> productBases = new ArrayList<>();
        if (!sweIdsType1.isEmpty())
        {
            productBases.addAll(saleWindowDao.findProductBasesBySingleswids(sweIdsType1));
        }
        Map<String, ProductSnippet> productBaseMap = new HashMap<>();
        for (Map<String, Object> base : productBases)
        {
            productBaseMap.put(base.get("id").toString(), new ProductSnippet(Integer.valueOf(base.get("baseId").toString()), Float.valueOf(base.get("bsCommision").toString()),
                Float.valueOf(base.get("salesPrice").toString())));
        }
        
        // 查询自定义活动特卖url
        List<Map<String, Object>> webCustomActivityUrls = new ArrayList<>();
        if (!sweIdsType3.isEmpty())
        {
            webCustomActivityUrls.addAll(saleWindowDao.findCustomActivityShareUrlBySwids(sweIdsType3));
        }
        Map<String, String> webCustomActivityUrlMap = new HashMap<>();
        for (Map<String, Object> url : webCustomActivityUrls)
        {
            webCustomActivityUrlMap.put(url.get("id").toString(), url.get("shareUrl").toString());
        }
        
        for (SaleWindowEntity swe : sws)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.putAll(new BeanMap(swe));
            map.put("categoryFirstName", categoryMap.get(swe.getId() + "") == null ? "" : categoryMap.get(swe.getId() + ""));
            map.put("relaRealSellerName", sellerNameMap.get(swe.getId() + "") == null ? "" : sellerNameMap.get(swe.getId() + ""));
            map.put("stock", stockMap.get(swe.getId() + "") == null ? "0" : stockMap.get(swe.getId() + ""));
            ProductSnippet producSnippt = productBaseMap.get(swe.getId() + "");
            map.put("baseId", producSnippt == null ? "-1" : String.valueOf(producSnippt.getBaseId()));
            if (swe.getType() == SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode())
            {
                map.put("commision", producSnippt == null ? "" : producSnippt.caculateCommisionPercent());
            }
            
            if (type == 1)
            {
                map.put("order", (swe.getNowOrder() == 0) ? "无" : swe.getNowOrder());
            }
            else if (type == 2)
            {
                map.put("order", (swe.getLaterOrder() == 0) ? "无" : swe.getLaterOrder());
            }
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
                map.put("url", webCustomActivityUrlMap.get(swe.getId() + "") == null ? "#" : webCustomActivityUrlMap.get(swe.getId() + ""));
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
    
    private class ProductSnippet
    {
        public ProductSnippet(int baseId, float bsCommision, float salePrice)
        {
            this.baseId = baseId;
            this.bsCommision = bsCommision;
            this.salePrice = salePrice;
        }
        
        public String caculateCommisionPercent()
        {
            NumberFormat nt = NumberFormat.getPercentInstance();
            // 设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(2);
            return nt.format(salePrice > 0 ? bsCommision / salePrice : 0);
        }
        
        private int baseId;
        
        private float bsCommision;
        
        private float salePrice;
        
        public int getBaseId()
        {
            return baseId;
        }
        
        public void setBaseId(int baseId)
        {
            this.baseId = baseId;
        }
        
        public float getBsCommision()
        {
            return bsCommision;
        }
        
        public void setBsCommision(float bsCommision)
        {
            this.bsCommision = bsCommision;
        }
        
        public float getSalePrice()
        {
            return salePrice;
        }
        
        public void setSalePrice(float salePrice)
        {
            this.salePrice = salePrice;
        }
        
    }
    
    @Override
    public String checkSaleWindowRelation(int id, int productId)
        throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("type", SaleWindowEnum.SALE_TYPE.SINGLE_PRODUCT.getCode());
        params.put("displayId", productId);
        SaleWindowEntity swe = saleWindowDao.findSaleWindowByPara(params);
        if (swe == null || swe.getId() == id)
        {
            return JSON.toJSONString(ResultEntity.getSuccessResult());
        }
        if (CommonUtil.getSaleWindowStatus(swe) == SaleWindowEnum.SALE_WINDOW_STATUS.TO_START.getCode()
            || CommonUtil.getSaleWindowStatus(swe) == SaleWindowEnum.SALE_WINDOW_STATUS.IN_PROGRESS.getCode())
        {
            return JSON.toJSONString(ResultEntity.getFailResult("Id=" + productId + "的商品已经被其他正在进行或即将开始的特卖关联"));
        }
        return JSON.toJSONString(ResultEntity.getSuccessResult());
    }
}
