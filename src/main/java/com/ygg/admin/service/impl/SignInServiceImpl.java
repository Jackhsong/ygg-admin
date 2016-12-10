package com.ygg.admin.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SignInRewardStyleTypeEnum;
import com.ygg.admin.code.SignInRewardTypeEnum;
import com.ygg.admin.dao.CouponDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SignInDao;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.SignInService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;

@Service("signInService")
public class SignInServiceImpl implements SignInService
{
    @Resource
    private SignInDao signInDao;
    
    @Resource
    private CouponDao couponDao;
    
    @Resource
    private ProductDao productDao;
    
    @Override
    public int beginYearMonth()
        throws Exception
    {
        Integer beginYearMonth = signInDao.getBeginYearMonth();
        return beginYearMonth == null ? Integer.valueOf(DateTime.now().toString("yyyyMM")).intValue() : beginYearMonth;
    }
    
    @Override
    public Map<String, Object> findAllSignSetting(Map<String, Object> para)
        throws Exception
    {
        int currentYearMonth = Integer.valueOf(DateTime.now().toString("yyyyMM"));
        int currentDay = Integer.valueOf(DateTime.now().getDayOfMonth());
        int searchYearMonth = Integer.valueOf(para.get("yearMonth") + "");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = signInDao.findAllSignSetting(para);
        List<Map<String, Object>> accountSignList = signInDao.findAccountSignCount(searchYearMonth);
        Map<Integer, Integer> signCountMap = countAccountSign(accountSignList);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int yearMonth = Integer.valueOf(map.get("yearMonth") + "").intValue();
                int day = Integer.valueOf(map.get("day") + "").intValue();
                int type = Integer.valueOf(map.get("type") + "").intValue();
                int style = Integer.valueOf(map.get("style") + "").intValue();
                if (yearMonth < currentYearMonth)
                {
                    map.put("canEdit", 0);
                }
                else if (yearMonth > currentYearMonth)
                {
                    map.put("canEdit", 1);
                }
                else
                {
                    if (day <= currentDay)
                    {
                        map.put("canEdit", 0);
                    }
                    else
                    {
                        map.put("canEdit", 1);
                    }
                }
                map.put("day", "第" + day + "次");
                map.put("typeStr", SignInRewardTypeEnum.getDescriptionByOrdinal(type));
                map.put("styleStr", SignInRewardStyleTypeEnum.getDescriptionByOrdinal(style));
                if (SignInRewardTypeEnum.SIGNIN_REWARD_TYPE_SCORE.ordinal() == type)
                {
                    map.put("score", map.get("point"));
                    map.put("coupon", "-");
                }
                else if (SignInRewardTypeEnum.SIGNIN_REWARD_TYPE_COUPON.ordinal() == type)
                {
                    map.put("score", map.get("-"));
                    
                    int couponId = Integer.valueOf(map.get("couponId") + "").intValue();
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("couponId", couponId);
                    param.put("start", 0);
                    param.put("max", 1);
                    List<Map<String, Object>> result = couponDao.queryCouponInfo(param);
                    if (result.size() == 0)
                    {
                        map.put("coupon", "");
                    }
                    else
                    {
                        param = result.get(0);
                        int couponType = Integer.valueOf(param.get("couponType") + "");
                        StringBuilder sb = new StringBuilder();
                        if (couponType == 1)
                        {
                            sb.append("满").append(param.get("threshold")).append("减").append(param.get("reduce")).append("优惠券");
                        }
                        else if (couponType == 2)
                        {
                            sb.append(param.get("reduce")).append("元现金券");
                        }
                        map.put("coupon", sb.toString());
                    }
                }
                map.put("signCount", signCountMap.get(day) == null ? 0 : signCountMap.get(day));
            }
            total = signInDao.countSignSetting(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> para)
        throws Exception
    {
        String yearMonth = para.get("yearMonth") + "";
        Date time = CommonUtil.string2Date(yearMonth, "yyyyMM");
        Calendar a = Calendar.getInstance();
        a.setTime(time);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDayOfMonth = a.get(Calendar.DATE);
        int id = (int)para.get("id");
        int status = 0;
        if (id == 0)
        {
            //新增
            int day = signInDao.findCurrentDayByYearMonth(Integer.parseInt(yearMonth));
            if (day > maxDayOfMonth)
                return 2;
            para.put("day", day);
            status = signInDao.insert(para);
        }
        else
        {
            //修改
            status = signInDao.update(para);
        }
        return status;
    }
    
    @Override
    public Map<String, Integer> copyFromLastMonth(Map<String, Object> para)
        throws Exception
    {
        Map<String, Integer> result = new HashMap<String, Integer>();
        DateTime lastMonth = DateTime.parse(para.get("lastMonth") + "", DateTimeFormat.forPattern("yyyyMM"));
        DateTime currMonth = DateTime.parse(para.get("currMonth") + "", DateTimeFormat.forPattern("yyyyMM"));
        int currentMaxDay = currMonth.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
        //查询上个月的所有配置
        para.put("yearMonth", lastMonth.toString("yyyyMM"));
        List<Map<String, Object>> infoList = signInDao.findAllSignSetting(para);
        int status = 1;
        int count = 0;
        if (infoList == null || infoList.size() == 0)
        {
            status = 2;
        }
        else
        {
            //查询当前月是否已经配置过，如果一天都没配置，则全部从上个月复制
            para.put("yearMonth", currMonth.toString("yyyyMM"));
            List<Map<String, Object>> list = signInDao.findAllSignSetting(para);
            if (list.size() == 0)
            {
                for (Map<String, Object> map : infoList)
                {
                    map.put("yearMonth", currMonth.toString("yyyyMM"));
                    signInDao.insert(map);
                    count++;
                }
            }
            // 如果全部配置，则删除本月数据，重新复制
            else if (list.size() == currentMaxDay)
            {
                signInDao.delete(para);
                for (Map<String, Object> map : infoList)
                {
                    map.put("yearMonth", currMonth.toString("yyyyMM"));
                    signInDao.insert(map);
                    count++;
                }
            }
            else
            {
                //如果配置部分，则接着配置后的部分添加
                para.put("max", currentMaxDay - list.size());
                para.put("yearMonth", lastMonth.toString("yyyyMM"));
                int day = list.size() + 1;
                infoList = signInDao.findAllSignSetting(para);
                for (Map<String, Object> map : infoList)
                {
                    map.put("yearMonth", currMonth.toString("yyyyMM"));
                    map.put("day", day);
                    signInDao.insert(map);
                    count++;
                    day++;
                }
            }
        }
        result.put("status", status);
        result.put("count", count);
        return result;
    }
    
    private Map<Integer, Integer> countAccountSign(List<Map<String, Object>> signList)
    {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        for (Map<String, Object> map : signList)
        {
            Integer times = Integer.valueOf(map.get("times") + "");
            Integer total = Integer.valueOf(map.get("total") + "");
            resultMap.put(times, total);
            while (times > 0)
            {
                times--;
                Integer count = resultMap.get(times);
                if (count != null)
                {
                    resultMap.put(times, count + total);
                }
            }
        }
        return resultMap;
    }
    
    @Override
    public Map<String, Object> findAllSigninProduct(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> infoList = signInDao.findAllSigninProduct(para);
        for (Map<String, Object> map : infoList)
        {
            int type = Integer.valueOf(map.get("type") + "");
            map.put("typeStr", ProductEnum.PRODUCT_TYPE.getDescByCode(type));
            if (type == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                map.put("saleStartTime", DateTimeUtil.timestampObjectToString(map.get("startTime")));
                map.put("saleEndTime", DateTimeUtil.timestampObjectToString(map.get("endTime")));
            }
            else
            {
                map.put("saleStartTime", "");
                map.put("saleEndTime", "");
            }
        }
        int total = signInDao.countSigninProduct(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public String addSigninProduct(int productId, int sequence)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<>();
        ProductEntity pe = productDao.findProductByID(productId, null);
        if (pe == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", String.format("ID=%d的商品不存在", productId));
            return JSON.toJSONString(resultMap);
        }
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("productId", productId);
        para.put("sequence", sequence);
        if (signInDao.addSigninProduct(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
            return JSON.toJSONString(resultMap);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @Override
    public String deleteSigninProduct(List<String> idList)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (signInDao.deleteSigninProduct(idList) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateSigninProductDisplayStatus(List<String> idList, int isDisplay)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", idList);
        para.put("isDisplay", isDisplay);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (signInDao.updateSigninProductDisplayStatus(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public String updateSigninProductSequence(int id, int sequence)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("sequence", sequence);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (signInDao.updateSigninProductSequence(para) > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "保存成功");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
}
