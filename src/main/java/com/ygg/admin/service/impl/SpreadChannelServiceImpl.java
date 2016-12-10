package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.CouponDao;
import com.ygg.admin.dao.SpreadChannelDao;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.SpreadChannelEntity;
import com.ygg.admin.service.SpreadChannelService;
import com.ygg.admin.util.MathUtil;

@Service("spreadChannelService")
public class SpreadChannelServiceImpl implements SpreadChannelService
{
    @Resource
    private SpreadChannelDao spreadChannelDao;
    
    @Resource
    private CouponDao couponDao;
    
    @Override
    public Map<String, Object> findAllSpreadChannels(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = spreadChannelDao.findAllSpreadChannels(para);
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            int channelId = Integer.valueOf(map.get("id") + "").intValue();
            int isSendMsg = Integer.valueOf(map.get("isSendMsg") + "").intValue();
            map.put("msgStatus", isSendMsg == 1 ? "开启" : "关闭");
            map.put("isAvailable", ((int)map.get("isAvailableCode") == 1) ? "可用" : "停用");// 使用状态
            map.put("totalMoney", MathUtil.round(map.get("totalMoney") + "", 2));
            map.put("newBuyerAmount", MathUtil.round(map.get("newBuyerMoney") + "", 2));
            map.put("url", "<a href='" + map.get("url") + "'  target='_blank'>" + map.get("url") + "</a>");
            map.put("shareImageURL", "<a href='" + map.get("shareImage") + "' target='_blank'><img alt='' src='" + map.get("shareImage")
                + "' style='max-width: 100px; max-width: 100px;'/></a>");
            List<Map<String, Object>> prizeList = spreadChannelDao.findPrizeByChannelId(channelId);
            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> curr : prizeList)
            {
                int couponId = Integer.valueOf(curr.get("couponId") + "").intValue();
                CouponEntity coupon = couponDao.findCouponById(couponId);
                CouponDetailEntity couponDetail = couponDao.findCouponDetailById(coupon.getCouponDetailId());
                if (couponDetail.getType() == 1)
                {
                    sb.append("满").append(couponDetail.getThreshold()).append("减").append(couponDetail.getReduce()).append("优惠券");
                }
                else if (couponDetail.getType() == 2)
                {
                    sb.append(couponDetail.getReduce()).append("元现金券");
                }
                sb.append(curr.get("couponAmount")).append("张;");
                int dateType = Integer.valueOf(curr.get("dateType") + "").intValue();
                if (dateType == 1)
                {
                    map.put("validityDate", coupon.getStartTime() + "~" + coupon.getEndTime());
                }
                else if (dateType == 2)
                {
                    map.put("validityDate", "自领券之日" + curr.get("days") + "天内有效");
                }
            }
            sb.replace(sb.length() - 1, sb.length(), "");
            map.put("coupon", sb.toString());
            
        }
        total = spreadChannelDao.countSpreadChannels(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdate(Map<String, Object> para)
        throws Exception
    {
        String couponIdAndCount = para.get("couponIdAndCount") + "";
        SpreadChannelEntity channel = (SpreadChannelEntity)para.get("channel");
        Map<Integer, Integer> couponInfo = new HashMap<Integer, Integer>();
        if (channel.getId() == 0)
        {
            String[] coupons = couponIdAndCount.split(",");
            for (String coupon : coupons)
            {
                //如果输入的优惠券Id有相同，则将相同Id的优惠券数量相加，直插入一次
                int couponId = Integer.valueOf(coupon.split(":")[0]).intValue();
                int couponAmount = Integer.valueOf(coupon.split(":")[1]).intValue();
                if (couponInfo.get(couponId) == null)
                {
                    couponInfo.put(couponId, couponAmount);
                }
                else
                {
                    couponInfo.put(couponId, couponInfo.get(couponId).intValue() + couponAmount);
                }
                
            }
            
            //新增渠道
            spreadChannelDao.addSpreadChannel(channel);
            channel.setUrl("http://m.gegejia.com/ygg/spreadChannel/web/" + channel.getId());
            spreadChannelDao.updateSpreadChannelURL(channel);
            para.put("spreadChannelId", channel.getId());
            for (Integer couponId : couponInfo.keySet())
            {
                //新增渠道奖励
                para.put("couponId", couponId);
                para.put("couponAmount", couponInfo.get(couponId));
                spreadChannelDao.addSpreadChannelPrize(para);
            }
        }
        else
        {
            //修改渠道,修改时不允许修改优惠券信息
            spreadChannelDao.updateSpreadChannel(channel);
            //修改渠道奖励，先删掉，再新增，简单粗暴
            //            spreadChannelDao.deleteSpreadChannelPrizeById(channel.getId());
            //            para.put("spreadChannelId", channel.getId());
            //            for (Integer couponId : couponInfo.keySet())
            //            {
            //                //新增渠道奖励
            //                para.put("couponId", couponId);
            //                para.put("couponAmount", couponInfo.get(couponId));
            //                spreadChannelDao.addSpreadChannelPrize(para);
            //            }
        }
        return 1;
    }
    
    @Override
    public int deleteSpreadChannel(int id)
        throws Exception
    {
        int status = spreadChannelDao.deleteSpreadChannel(id);
        if (status == 1)
            return spreadChannelDao.deleteSpreadChannelPrizeById(id);
        return 0;
    }
    
    @Override
    public int updateChannelAvailable(Map<String, Object> para)
        throws Exception
    {
        return spreadChannelDao.updateChannelStatus(para);
    }
    
    @Override
    public int updateChannelSendMsg(Map<String, Object> para)
        throws Exception
    {
        return spreadChannelDao.updateChannelStatus(para);
    }
    
    @Override
    public int editMsgContent(Map<String, Object> para)
        throws Exception
    {
        return spreadChannelDao.updateChannelStatus(para);
    }
}
