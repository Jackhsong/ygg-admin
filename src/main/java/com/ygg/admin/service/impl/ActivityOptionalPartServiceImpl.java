package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.ActivityEnum;
import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.dao.ActivitiesCommonDao;
import com.ygg.admin.dao.ActivityManjianDao;
import com.ygg.admin.dao.ActivityOptionalPartDao;
import com.ygg.admin.dao.ActivitySimplifyDao;
import com.ygg.admin.dao.CustomActivitiesDao;
import com.ygg.admin.dao.PromotionActivityDao;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.ActivitiesOptionalPartEntity;
import com.ygg.admin.entity.CustomActivityEntity;
import com.ygg.admin.service.ActivityOptionalPartService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;

/**
 * N元任选 service
 */
@Service("activityOptionalPartService")
public class ActivityOptionalPartServiceImpl implements ActivityOptionalPartService
{
    private Logger log = Logger.getLogger(ActivityOptionalPartServiceImpl.class);
    
    @Resource
    private ActivityOptionalPartDao activityOptionalPartDao;
    
    @Resource
    private ActivitiesCommonDao activitiesCommonDao;
    
    @Resource
    private CustomActivitiesDao customActivitiesDao;
    
    @Resource
    private ActivityManjianDao activityManjianDao;
    
    @Resource
    private ActivitySimplifyDao activitySimplifyDao;
    
    @Resource
    private PromotionActivityDao promotionActivityDao;
    
    @Override
    public Map<String, Object> findAllActivityOptionalPart(String status, int page, int rows)
        throws Exception
    {
        // 1：即将开始，2：进行中，3：已结束
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if ("".equals(status))
        {
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("rows", new ArrayList<>());
            return result;
        }
        else if (status.indexOf(",") == -1)
        {
            // 只选择了一种状态
            para.put("status", status);
        }
        else
        {
            int statusCode = 1;
            String[] arr = status.split(",");
            for (String curr : arr)
            {
                statusCode += Integer.valueOf(curr);
            }
            para.put("status", statusCode);
        }
        List<ActivitiesOptionalPartEntity> aopes = activityOptionalPartDao.findAllActivityOptionalPart(para);
        List<Map<String, Object>> list = new ArrayList<>();
        int total = 0;
        if (aopes.size() > 0)
        {
            total = activityOptionalPartDao.countActivityOptionalPart(para);
            for (ActivitiesOptionalPartEntity aope : aopes)
            {
                Map<String, Object> map = new HashMap<>();
                map.put("id", aope.getId());
                map.put("availableDesc", aope.getIsAvailable() == 1 ? "可用" : "停用");
                map.put("isAvailable", aope.getIsAvailable());
                Date startDate = CommonUtil.string2Date(aope.getStartTime(), "yyyy-MM-dd HH:mm:ss");
                Date endDate = CommonUtil.string2Date(aope.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                if (startDate.after(now))
                {
                    map.put("timeStatus", "即将开始");
                }
                else if (now.after(startDate) && endDate.after(now))
                {
                    map.put("timeStatus", "进行中");
                }
                else if (now.after(endDate))
                {
                    map.put("timeStatus", "已结束");
                }
                else
                {
                    map.put("timeStatus", "状态异常");
                }
                map.put("startTime", DateTimeUtil.timestampStringToWebString(aope.getStartTime()));
                map.put("endTime", DateTimeUtil.timestampStringToWebString(aope.getEndTime()));
                map.put("name", aope.getName());
                map.put("role", String.format("%d元任选%d件", aope.getPrice(), aope.getCount()));
                map.put("price", aope.getPrice());
                map.put("count", aope.getCount());
                map.put("type", aope.getType());
                map.put("typeDesc", ActivityEnum.OPTION_PART_TYPE.getDescByCode(Byte.valueOf(aope.getType() + "")));
                map.put("typeId", aope.getTypeId());
                list.add(map);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("rows", list);
        return result;
    }
    
    @Override
    public Map<String, Object> saveActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception
    {
        Map<String, Object> result = validateParams(activity);
        if (Integer.valueOf(result.get("status") + "") == CommonConstant.COMMON_YES)
        {
            activity.setIsAvailable(1);
            int status = activityOptionalPartDao.saveActivityOptionalPart(activity);
            Map<String, Object> resultMap = new HashMap<>();
            if (status > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return resultMap;
        }
        else
        {
            return result;
        }
    }
    
    @Override
    public Map<String, Object> updateActivityOptionalPart(ActivitiesOptionalPartEntity activity)
        throws Exception
    {
        
        Map<String, Object> result = validateParams(activity);
        if (Integer.valueOf(result.get("status") + "") == CommonConstant.COMMON_YES)
        {
            int status = activityOptionalPartDao.updateActivityOptionalPart(activity);
            Map<String, Object> resultMap = new HashMap<>();
            if (status > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return resultMap;
        }
        else
        {
            return result;
        }
    }
    
    private Map<String, Object> validateParams(ActivitiesOptionalPartEntity activity)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(activity.getName()))
        {
            result.put("status", 0);
            result.put("msg", "活动名称不能为空");
            return result;
        }
        if (StringUtils.isEmpty(activity.getStartTime()) || StringUtils.isEmpty(activity.getEndTime()))
        {
            result.put("status", 0);
            result.put("msg", "活动开始时间和结束时间不能为空");
            return result;
        }
        
        Date startTime = DateTimeUtil.string2Date(activity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateTimeUtil.string2Date(activity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        if (startTime.after(endTime))
        {
            result.put("status", 0);
            result.put("msg", "活动开始时间不能晚于结束时间");
            return result;
        }
        if (activity.getType() == null)
        {
            result.put("status", 0);
            result.put("msg", "请选择活动关联类型");
            return result;
        }
        else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.GROUP.getCode())
        {
            ActivitiesCommonEntity ac = activitiesCommonDao.findAcCommonById(activity.getTypeId());
            if (ac == null)
            {
                result.put("status", 0);
                result.put("msg", String.format("Id=%d的组合特卖不存在", activity.getTypeId()));
                return result;
            }
            else
            {
                List<Integer> productIdList = activitiesCommonDao.findAllProductIdByActivitiesCommonId(activity.getTypeId());
                if (productIdList == null || productIdList.isEmpty())
                {
                    result.put("status", 0);
                    result.put("msg", String.format("Id=%d的组合特卖没有关联商品，请先关联商品", activity.getTypeId()));
                    return result;
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
                return result;
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
                        return result;
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
                        return result;
                    }
                }
                else if (ca.getType() == (byte)CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode())
                {
                    
                    List<Integer> productIdList = activitySimplifyDao.findActivitySimplifyProductIdBySimplifyActivityId(ca.getTypeId());
                    if (productIdList == null || productIdList.isEmpty())
                    {
                        result.put("status", 0);
                        result.put("msg", String.format("Id=%d的精品特卖活动没有商品", activity.getTypeId()));
                        return result;
                    }
                }
                else if (ca.getType() == (byte)CustomEnum.CUSTOM_ACTIVITY_RELATION.NEW_SIMPLIFY_ACTIVITY.getCode())
                {
                    List<Integer> productIdList = promotionActivityDao.findSpecialActivityNewProductByid(ca.getTypeId());
                    if (productIdList == null || productIdList.isEmpty())
                    {
                        result.put("status", 0);
                        result.put("msg", String.format("Id=%d的新情景活动没有商品", activity.getTypeId()));
                        return result;
                    }
                }
                else
                {
                    result.put("status", 0);
                    result.put("msg", "只能关联情景特卖自定义活动或其他自定义活动");
                    return result;
                }
            }
        }
        else
        {
            result.put("status", 0);
            result.put("msg", "不支持的活动类型");
            return result;
        }
        result.put("status", 1);
        return result;
    }
    
    @Override
    public Map<String, Object> updateDisplayStatus(int id, int isAvailable)
        throws Exception
    {
        ActivitiesOptionalPartEntity aope = new ActivitiesOptionalPartEntity();
        aope.setIsAvailable(isAvailable);
        aope.setId(id);
        int status = activityOptionalPartDao.updateActivityOptionalPart(aope);
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("msg", status > 0 ? "保存成功" : "保存失败");
        return result;
        
    }
}
