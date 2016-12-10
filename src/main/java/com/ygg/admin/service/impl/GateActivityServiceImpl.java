package com.ygg.admin.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.CouponDao;
import com.ygg.admin.dao.GateActivityDao;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.GateEntity;
import com.ygg.admin.service.GateActivityService;
import com.ygg.admin.util.MathUtil;

@Service("gateActivityService")
public class GateActivityServiceImpl implements GateActivityService
{
    
    @Resource
    private CouponDao couponDao;
    
    @Resource
    private GateActivityDao gateActivityDao;
    
    @Override
    public Map<String, Object> findAllGates(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = gateActivityDao.findAllGates(para);
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            map.put("isDisplay", ((int)map.get("isDisplayCode") == 1) ? "展现" : "不展现");
            map.put("totalMoney", MathUtil.round(map.get("totalMoney") + "", 2));
            map.put("newBuyerAmount", MathUtil.round(map.get("newBuyerAmount") + "", 2));
            map.put("validTimeStart", ((Timestamp)map.get("validTimeStart")).toString());
            map.put("validTimeEnd", ((Timestamp)map.get("validTimeEnd")).toString());
            int couponId = Integer.valueOf(map.get("couponId") + "").intValue();
            CouponEntity coupon = couponDao.findCouponById(couponId);
            CouponDetailEntity couponDetail = couponDao.findCouponDetailById(coupon.getCouponDetailId());
            StringBuilder sb = new StringBuilder();
            if (couponDetail.getType() == 1)
            {
                sb.append("满").append(couponDetail.getThreshold()).append("减");
                if (couponDetail.getIsRandomReduce() == 1)
                {
                    sb.append(couponDetail.getLowestReduce()).append("到").append(couponDetail.getMaximalReduce()).append("随机优惠券");
                }
                else
                {
                    sb.append(couponDetail.getReduce()).append("优惠券");
                }
            }
            else if (couponDetail.getType() == 2)
            {
                if (couponDetail.getIsRandomReduce() == 1)
                {
                    sb.append(couponDetail.getLowestReduce()).append("到").append(couponDetail.getMaximalReduce()).append("元随机现金券");
                }
                else
                {
                    sb.append(couponDetail.getReduce()).append("元现金券");
                }
            }
            map.put("coupon", sb.toString());
            int dateType = Integer.valueOf(map.get("validTimeType") + "").intValue();
            if (dateType == 1)
            {
                map.put("validTimeType", coupon.getStartTime() + "~" + coupon.getEndTime());
            }
            else if (dateType == 2)
            {
                map.put("validTimeType", "自领券之日" + map.get("days") + "天内有效");
            }
            
        }
        total = gateActivityDao.countGates(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateGate(Map<String, Object> para)
        throws Exception
    {
        GateEntity gate = (GateEntity)para.get("gate");
        if (gate.getId() == 0)
        {
            gateActivityDao.addGate(gate);
            para.put("gateId", gate.getId());
            gateActivityDao.addGatePrize(para);
        }
        else
        {
            //更新游戏
            gateActivityDao.updateGate(gate);
        }
        return 1;
    }
    
    @Override
    public int updateGateDisplay(Map<String, Object> para)
        throws Exception
    {
        return gateActivityDao.updateGateStatus(para);
    }
    
    @Override
    public int deleteGate(int id)
        throws Exception
    {
        int status = gateActivityDao.deleteGate(id);
        if (status == 1)
            return gateActivityDao.deleteGatePrize(id);
        return 0;
    }
    
}
