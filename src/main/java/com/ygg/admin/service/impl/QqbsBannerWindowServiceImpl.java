package com.ygg.admin.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.QqbsBannerWindowDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.BannerWindowEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.QqbsBannerWindowService;

@Service("qqbsBannerWindowService")
public class QqbsBannerWindowServiceImpl implements QqbsBannerWindowService
{
    
    @Resource
    private ProductDao productDao;
    
    @Resource(name = "qqbsBannerWindowDao")
    private QqbsBannerWindowDao qqbsBannerWindowDao = null;
    
    @Resource(name = "activitiesCommonDao")
    private ActivitiesCommonDao activitiesCommonDao = null;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private PageDao pageDao;
    
    @Override
    public int save(BannerWindowEntity window)
        throws Exception
    {
        if (window.getType() == 1)
        {
            ProductEntity product = productDao.findProductByID(window.getDisplayId(), null);
            if (product != null && product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                window.setType((byte)4);
            }
        }
        return qqbsBannerWindowDao.save(window);
    }
    
    @Override
    public int update(BannerWindowEntity window)
        throws Exception
    {
        if (window.getType() == 1)
        {
            ProductEntity product = productDao.findProductByID(window.getDisplayId(), null);
            if (product != null && product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                window.setType((byte)4);
            }
        }
        return qqbsBannerWindowDao.update(window);
    }
    
    @Override
    public int countBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return qqbsBannerWindowDao.countBannerWindow(para);
    }
    
    @Override
    public List<BannerWindowEntity> findAllBannerWindow(Map<String, Object> para)
        throws Exception
    {
        return qqbsBannerWindowDao.findAllBannerWindow(para);
    }
    
    @Override
    public BannerWindowEntity findBannerWindowById(int id)
        throws Exception
    {
        return qqbsBannerWindowDao.findBannerWindowById(id);
    }
    
    @Override
    public int updateDisplayCode(Map<String, Object> para)
        throws Exception
    {
        return qqbsBannerWindowDao.updateDisplayCode(para);
    }
    
    @Override
    public List<Map<String, Object>> packageBannerWindowList(List<BannerWindowEntity> bList)
        throws Exception
    {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (BannerWindowEntity bEntity : bList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", bEntity.getId());
            int displayId = bEntity.getDisplayId();
            map.put("displayId", displayId);
            String displayName = "";
            String displayRemark = "";
            if (bEntity.getType() == 1)
            {
                ProductEntity product = productDao.findProductByID(displayId, null);
                displayName = product.getShortName();
                displayRemark = product.getRemark();
            }
            else if (bEntity.getType() == 2)
            {
                ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(displayId);
                displayName = ac.getName();
                displayRemark = ac.getDesc();
            }
            else if (bEntity.getType() == 3)
            {
//                Map<String, Object> customActivitiesMap = customActivitiesDao.findCustomActivitiesById(displayId);
//                if (customActivitiesMap != null)
//                {
//                    int type = Integer.valueOf(customActivitiesMap.get("typeCode") + "").intValue();
//                    displayName = CustomEnum.CUSTOM_ACTIVITY_RELATION.getDescrByCode(type);
//                    displayRemark = customActivitiesMap.get("remark") + "";
//                }
                displayName = bEntity.getUrl();
            }
            map.put("displayName", displayName);
            map.put("displayRemark", displayRemark);
            map.put("order", (bEntity.getOrder() == 0) ? "无" : bEntity.getOrder());
            map.put("isDisplay", (bEntity.getIsDisplay() == 1) ? "展现" : "不展现");
            map.put("isDisplayCode", bEntity.getIsDisplay());
            // 特卖状态
            String startTime = bEntity.getStartTime();
            String endTime = bEntity.getEndTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            DateTime startTime_dateTime = new DateTime(sdf.parse(startTime));
            DateTime endTime_dateTime = new DateTime(sdf.parse(endTime));
            
            // 未开始
            if (startTime_dateTime.isAfterNow())
            {
                map.put("bannerStatus", "等待开始");
            }
            // 进行中
            if (startTime_dateTime.isBeforeNow() && endTime_dateTime.isAfterNow())
            {
                map.put("bannerStatus", "进行中");
            }
            // 已结束
            if (endTime_dateTime.isBeforeNow())
            {
                map.put("bannerStatus", "已结束");
            }
            map.put("type", (bEntity.getType() == 1 ? "单品"
                            : (bEntity.getType() == 2 ? "组合"
                              :(bEntity.getType() == 3 ? "网页":"自定义页面"))));
            // 库存数量
            map.put("desc", bEntity.getDesc());
            map.put("startTime", startTime_dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", endTime_dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            StringBuilder imageSB = new StringBuilder("");
            imageSB.append("<img alt='' src='").append(bEntity.getImage()).append("' style='max-height:40px;'/>");
            map.put("bannerImage", imageSB.toString());
            map.put("image", bEntity.getImage());
            resultList.add(map);
        }
        return resultList;
    }
    
    @Override
    public int updateOrder(Map<String, Object> para)
        throws Exception
    {
        return qqbsBannerWindowDao.updateOrder(para);
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
        else if (type == 5)
        {
            Map<String, Object> pageMap = pageDao.findPageById(subjectId);
            return pageMap != null;
        }
        else if (type == 6)
        {
            return true;
        }
        else
        {
            return false;
        }
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
        else if (type == 5)
        {
            // TODO 自定义页面
            result = true;
        }
        else if (type == 6)
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
    
}
