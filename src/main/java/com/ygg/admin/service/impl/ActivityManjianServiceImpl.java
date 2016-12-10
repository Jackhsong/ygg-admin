package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ActivityEnum;
import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.ActivityManjianDao;
import com.ygg.admin.dao.ActivitySimplifyDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PageDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.PromotionActivityDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ActivitiyRelationProductEntity;
import com.ygg.admin.entity.ActivityManjianEntity;
import com.ygg.admin.entity.CustomActivityEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.ActivityManjianService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ProductUtil;

@Service
public class ActivityManjianServiceImpl implements ActivityManjianService
{
    @Resource
    private ActivityManjianDao activityManjianDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private PageDao pageDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ActivitySimplifyDao activitySimplifyDao;
    
    @Resource
    private PromotionActivityDao promotionActivityDao;
    
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> jsonActivityManjianInfo(Map<String, Object> para)
        throws Exception
    {
        int status = 0;
        String statusStr = para.get("status").toString();
        if (StringUtils.isNotEmpty(statusStr))
        {
            String[] statusArr = statusStr.split(",");
            for (String str : statusArr)
            {
                status += Integer.parseInt(str);
            }
        }
        para.put("status", status);
        
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        int count = 0;
        DateTime nowTime = DateTime.now();
        para.put("nowTime", nowTime.toString("yyyy-MM-dd HH:mm:ss"));
        List<ActivityManjianEntity> reList = activityManjianDao.findAllActivityManjian(para);
        for (ActivityManjianEntity cc : reList)
        {
            DateTime startTime = new DateTime(DateTimeUtil.string2Date(cc.getStartTime(), "yyyy-MM-dd HH:mm:ss").getTime());
            DateTime endTime = new DateTime(DateTimeUtil.string2Date(cc.getEndTime(), "yyyy-MM-dd HH:mm:ss").getTime());
            Map<String, Object> it = new HashMap<String, Object>();
            it.putAll(new BeanMap(cc));
            it.put("availableDesc", cc.getIsAvailable() == 1 ? "可用" : "停用");
            it.put("gradientTypeDesc", ActivityEnum.MANJIAN_GRADIENT_TYPE.getDescByCode(cc.getGradientType()));
            it.put("relationTypeDesc", ActivityEnum.MANJIAN_RELATION_TYPE.getDescByCode(cc.getType()));
            it.put("typeId", cc.getTypeId());
            if (nowTime.isBefore(startTime))
            {
                it.put("timeStatus", "即将开始");
            }
            else if (nowTime.isAfter(endTime))
            {
                it.put("timeStatus", "已结束");
            }
            else
            {
                it.put("timeStatus", "进行中");
            }
            dataList.add(it);
        }
        count = activityManjianDao.countActivityManjian(para);
        result.put("total", count);
        result.put("rows", dataList);
        return result;
    }
    
    @Override
    public String saveActivityManjian(ActivityManjianEntity activity)
        throws Exception
    {
        String result = validateParams(activity);
        if (JSON.parseObject(result).getIntValue("status") == CommonConstant.COMMON_YES)
        {
            int status = activityManjianDao.saveActivityManjian(activity);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (status > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        else
        {
            return result;
        }
    }
    
    @Override
    public String updateActivityManjian(ActivityManjianEntity activity)
        throws Exception
    {
        String result = validateParams(activity);
        if (JSON.parseObject(result).getIntValue("status") == CommonConstant.COMMON_YES)
        {
            int status = activityManjianDao.updateActivityManjian(activity);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (status > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        else
        {
            return result;
        }
    }
    
    @Override
    public String updateDisplayStatus(int id, int isAvailable)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        if (isAvailable == CommonConstant.COMMON_YES)
        {
            ActivityManjianEntity am = activityManjianDao.findActivityManjianById(id);
            if (am == null)
            {
                para.put("status", 0);
                para.put("msg", "保存失败");
                return JSON.toJSONString(para);
            }
            para.put("id", id);
            para.put("startTime", am.getStartTime());
            para.put("endTime", am.getEndTime());
            if (am.getType() != ActivityEnum.MANJIAN_RELATION_TYPE.ALL.getCode())
            {
                para.put("type", ActivityEnum.MANJIAN_RELATION_TYPE.ALL.getCode());
            }
            para.put("isAvailable", CommonConstant.COMMON_YES);
            List<ActivityManjianEntity> amList = activityManjianDao.findActivityManjianByMap(para);
            if (!amList.isEmpty())
            {
                para.clear();
                para.put("status", 0);
                para.put("msg", "当前满减活动与可用其他满减活动时间重叠，保存失败");
                return JSON.toJSONString(para);
            }
        }
        para.clear();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        int status = activityManjianDao.updateDisplayStatus(para);
        para.clear();
        para.put("status", status);
        para.put("msg", status > 0 ? "保存成功" : "保存失败");
        return JSON.toJSONString(para);
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String jsonProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<ActivitiyRelationProductEntity> reList = activityManjianDao.findActivitiyRelationProduct(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (reList.size() > 0)
        {
            for (ActivitiyRelationProductEntity arp : reList)
            {
                Map<String, Object> beanMap = new BeanMap(arp);
                Map<String, Object> map = new HashMap<String, Object>();
                map.putAll(beanMap);
                map.put("isOffShelvesDesc", arp.getIsOffShelves() == 1 ? "下架" : "上架");
                map.put("productTypeDesc", ProductEnum.PRODUCT_TYPE.getDescByCode(arp.getProductType()));
                map.put("nameUrl", ProductUtil.getViewURL(arp.getProductId()));
                resultList.add(map);
            }
            total = activityManjianDao.countActivitiyRelationProduct(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String jsonProductListForAdd(Map<String, Object> para)
        throws Exception
    {
        List<Integer> productIdList = activityManjianDao.findProductIdByTypeAndTypeId(para);
        if (productIdList != null && productIdList.size() > 0)
        {
            para.put("idList", productIdList);
        }
        List<ActivitiyRelationProductEntity> reList = activityManjianDao.findProductForAddToActivityManjian(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (reList.size() > 0)
        {
            for (ActivitiyRelationProductEntity curr : reList)
            {
                Map<String, Object> map = new BeanMap(curr);
                map.put("id", curr.getProductId());
                resultList.add(map);
            }
            total = activityManjianDao.countProductForAddToActivityManjian(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String addProductForActivityManjian(int type, int typeId, String productIds)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (typeId == 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要添加商品的活动");
            return JSON.toJSONString(resultMap);
        }
        if (productIds.isEmpty())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入商品Id");
            return JSON.toJSONString(resultMap);
        }
        Set<Integer> productIdSet = new HashSet<Integer>();
        List<String> productIdList = Arrays.asList(productIds.split(","));
        for (String productId : productIdList)
        {
            productIdSet.add(Integer.parseInt(productId.trim()));
        }
        
        //已经存在的则不再添加
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("type", type);
        para.put("typeId", typeId);
        List<Integer> existsProductIdList = activityManjianDao.findProductIdByTypeAndTypeId(para);
        if (existsProductIdList != null && existsProductIdList.size() > 0)
        {
            productIdSet.removeAll(existsProductIdList);
        }
        
        List<ActivitiyRelationProductEntity> productList = new ArrayList<ActivitiyRelationProductEntity>();
        for (Integer productId : productIdSet)
        {
            ActivitiyRelationProductEntity arp = new ActivitiyRelationProductEntity();
            arp.setType((byte)type);
            arp.setTypeId(typeId);
            arp.setProductId(productId);
            productList.add(arp);
        }
        if (!productList.isEmpty())
        {
            activityManjianDao.addProductForActivityManjian(productList);
        }
        resultMap.put("status", 1);
        resultMap.put("msg", "添加成功");
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public int deleteActivityManjianProduct(List<String> idList)
        throws Exception
    {
        return activityManjianDao.deleteActivityManjianProduct(idList);
    }
    
    @Override
    public int updateProductTime(String productIds, String startTime, String endTime)
        throws Exception
    {
        int result = 0;
        List<String> productIdList = Arrays.asList(productIds.split(","));
        for (String productId : productIdList)
        {
            ProductEntity product = productDao.findProductByID(Integer.parseInt(productId.trim()), null);
            if (product != null && product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                product.setStartTime(startTime);
                product.setEndTime(endTime);
                result += productDao.updateProductTime(product);
            }
        }
        return result;
    }
    
    @Override
    public List<ActivitiyRelationProductEntity> findActivitiyRelationProduct(Map<String, Object> para)
        throws Exception
    {
        return activityManjianDao.findActivitiyRelationProduct(para);
    }
    
    private String validateParams(ActivityManjianEntity activity)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isEmpty(activity.getName()))
        {
            result.put("status", 0);
            result.put("msg", "活动名称不能为空");
            return JSON.toJSONString(result);
        }
        if (StringUtils.isEmpty(activity.getStartTime()) || StringUtils.isEmpty(activity.getEndTime()))
        {
            result.put("status", 0);
            result.put("msg", "活动开始时间和结束时间不能为空");
            return JSON.toJSONString(result);
        }
        
        Date startTime = DateTimeUtil.string2Date(activity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateTimeUtil.string2Date(activity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        if (startTime.after(endTime))
        {
            result.put("status", 0);
            result.put("msg", "活动开始时间不能晚于结束时间");
            return JSON.toJSONString(result);
        }
        
        if (activity.getOneThreshold() == null || activity.getOneReduce() == null)
        {
            result.put("status", 0);
            result.put("msg", "梯度1满减金额必填且只能为整数");
            return JSON.toJSONString(result);
        }
        if (activity.getOneReduce() > activity.getOneThreshold())
        {
            result.put("status", 0);
            result.put("msg", "梯度1满额必须大于或等于减额");
            return JSON.toJSONString(result);
        }
        if (activity.getTwoThreshold() != null)
        {
            if (activity.getOneThreshold() == 0 || activity.getOneReduce() == 0)
            {
                result.put("status", 0);
                result.put("msg", "不允许跨梯度");
                return JSON.toJSONString(result);
            }
            else if (activity.getTwoThreshold() <= activity.getOneThreshold())
            {
                result.put("status", 0);
                result.put("msg", "梯度2满减金额必须大于梯度1满减金额");
                return JSON.toJSONString(result);
            }
            if (activity.getTwoReduce() == null)
            {
                result.put("status", 0);
                result.put("msg", "梯度2满减信息不完整");
                return JSON.toJSONString(result);
            }
            else if (activity.getTwoReduce() > activity.getTwoThreshold())
            {
                result.put("status", 0);
                result.put("msg", "梯度2满额必须大于或等于减额");
                return JSON.toJSONString(result);
            }
        }
        else
        {
            activity.setTwoThreshold(0);
            activity.setTwoReduce(0);
        }
        if (activity.getThreeThreshold() != null)
        {
            if (activity.getTwoThreshold() == 0 || activity.getTwoReduce() == 0)
            {
                result.put("status", 0);
                result.put("msg", "不允许跨梯度");
                return JSON.toJSONString(result);
            }
            else if (activity.getThreeThreshold() <= activity.getTwoThreshold())
            {
                result.put("status", 0);
                result.put("msg", "梯度3满减金额必须大于梯度2满减金额");
                return JSON.toJSONString(result);
            }
            if (activity.getThreeReduce() == null)
            {
                result.put("status", 0);
                result.put("msg", "梯度3满减信息不完整");
                return JSON.toJSONString(result);
            }
            else if (activity.getThreeReduce() > activity.getThreeThreshold())
            {
                result.put("status", 0);
                result.put("msg", "梯度3满额必须大于或等于减额");
                return JSON.toJSONString(result);
            }
        }
        else
        {
            activity.setThreeThreshold(0);
            activity.setThreeReduce(0);
        }
        if (activity.getFourThreshold() != null)
        {
            if (activity.getThreeThreshold() == 0 || activity.getThreeReduce() == 0)
            {
                result.put("status", 0);
                result.put("msg", "不允许跨梯度");
                return JSON.toJSONString(result);
            }
            else if (activity.getFourThreshold() <= activity.getThreeThreshold())
            {
                result.put("status", 0);
                result.put("msg", "梯度4满减金额必须大于梯度3满减金额");
                return JSON.toJSONString(result);
            }
            if (activity.getFourReduce() == null)
            {
                result.put("status", 0);
                result.put("msg", "梯度4满减信息不完整");
                return JSON.toJSONString(result);
            }
            else if (activity.getFourReduce() > activity.getFourThreshold())
            {
                result.put("status", 0);
                result.put("msg", "梯度4满额必须大于或等于减额");
                return JSON.toJSONString(result);
            }
        }
        else
        {
            activity.setFourThreshold(0);
            activity.setFourReduce(0);
        }
        
        if (activity.getFourThreshold() > 0)
        {
            activity.setGradientType(ActivityEnum.MANJIAN_GRADIENT_TYPE.FOUR.getCode());
        }
        else
        {
            if (activity.getThreeThreshold() > 0)
            {
                activity.setGradientType(ActivityEnum.MANJIAN_GRADIENT_TYPE.THREE.getCode());
            }
            else
            {
                if (activity.getTwoThreshold() > 0)
                {
                    activity.setGradientType(ActivityEnum.MANJIAN_GRADIENT_TYPE.TWO.getCode());
                }
                else
                {
                    activity.setGradientType(ActivityEnum.MANJIAN_GRADIENT_TYPE.ONE.getCode());
                }
            }
        }
        if (activity.getType() == null)
        {
            result.put("status", 0);
            result.put("msg", "请选择活动关联类型");
            return JSON.toJSONString(result);
        }
        else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.ALL.getCode())
        {
            activity.setTypeId(0);
        }
        else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.GROUP.getCode())
        {
            ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(activity.getTypeId());
            if (ac == null)
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的组合特卖不存在", activity.getTypeId()));
                return JSON.toJSONString(result);
            }
            else
            {
                List<Integer> productIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(activity.getTypeId());
                if (productIdList == null || productIdList.isEmpty())
                {
                    result.put("status", 0);
                    result.put("msg", String.format("Id=%d的组合特卖没有关联商品，请先关联商品", activity.getTypeId()));
                    return JSON.toJSONString(result);
                }
            }
        }
        else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.CUSTOM_ACTIVITY.getCode())
        {
            CustomActivityEntity ca = customActivitiesDao.findCustomActivitiesId(activity.getTypeId());
            if (ca == null)
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的自定义活动不存在", activity.getTypeId()));
                return JSON.toJSONString(result);
            }
            else
            {
                Map<String, Object> para = new HashMap<String, Object>();
                if (ca.getType() == (byte)CustomEnum.CUSTOM_ACTIVITY_RELATION.OTHER_ACTIVITY.getCode())
                {
                    para.put("type", 1);
                    para.put("typeId", activity.getTypeId());
                    List<Integer> productIdList = activityManjianDao.findProductIdByTypeAndTypeId(para);
                    if (productIdList == null || productIdList.isEmpty())
                    {
                        result.put("status", 0);
                        result.put("msg", String.format("Id=%d的自定义活动没有关联商品，请先关联商品", activity.getTypeId()));
                        return JSON.toJSONString(result);
                    }
                }
                else if (ca.getType() == (byte)CustomEnum.CUSTOM_ACTIVITY_RELATION.SALE_ACTIVITY.getCode())
                {
                    para.put("type", 2);
                    para.put("typeId", activity.getTypeId());
                    List<Integer> productIdList = activityManjianDao.findProductIdByTypeAndTypeId(para);
                    if (productIdList == null || productIdList.isEmpty())
                    {
                        result.put("status", 0);
                        result.put("msg", String.format("Id=%d的自定义活动没有关联商品，请先关联商品", activity.getTypeId()));
                        return JSON.toJSONString(result);
                    }
                }
                else if (ca.getType() == (byte)CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode())
                {
                    
                    List<Integer> productIdList = activitySimplifyDao.findActivitySimplifyProductIdBySimplifyActivityId(ca.getTypeId());
                    if (productIdList == null || productIdList.isEmpty())
                    {
                        result.put("status", 0);
                        result.put("msg", String.format("Id=%d的精品特卖活动没有商品", activity.getTypeId()));
                        return JSON.toJSONString(result);
                    }
                }
                else if (ca.getType() == (byte)CustomEnum.CUSTOM_ACTIVITY_RELATION.NEW_SIMPLIFY_ACTIVITY.getCode())
                {
                    List<Integer> productIdList = promotionActivityDao.findSpecialActivityNewProductByid(ca.getTypeId());
                    if (productIdList == null || productIdList.isEmpty())
                    {
                        result.put("status", 0);
                        result.put("msg", String.format("Id=%d的新情景活动没有商品", activity.getTypeId()));
                        return JSON.toJSONString(result);
                    }
                }
                else
                {
                    result.put("status", 0);
                    result.put("msg", "只能关联情景特卖自定义活动或其他自定义活动");
                    return JSON.toJSONString(result);
                }
            }
        }
        else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.CUSTOM_PAGE.getCode())
        {
            Map<String, Object> page = pageDao.findPageById(activity.getTypeId());
            if (page == null)
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的原生自定义页面不存在", activity.getTypeId()));
                return JSON.toJSONString(result);
            }
            else
            {
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("type", 3);
                para.put("typeId", activity.getTypeId());
                List<Integer> productIdList = activityManjianDao.findProductIdByTypeAndTypeId(para);
                if (productIdList == null || productIdList.isEmpty())
                {
                    result.put("status", 0);
                    result.put("msg", String.format("Id=%d的原生自定义页面没有关联商品，请先关联商品", activity.getTypeId()));
                    return JSON.toJSONString(result);
                }
            }
        }
        else
        {
            result.put("status", 0);
            result.put("msg", "不支持的活动类型");
            return JSON.toJSONString(result);
        }
        result.put("status", 1);
        return JSON.toJSONString(result);
    }
}
