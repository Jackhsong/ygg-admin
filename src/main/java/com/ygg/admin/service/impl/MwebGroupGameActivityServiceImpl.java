package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.MwebGroupCouponDao;
import com.ygg.admin.dao.MwebGroupGameActivityDao;
import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.entity.MwebGroupCouponEntity;
import com.ygg.admin.entity.MwebGroupGameEntity;
import com.ygg.admin.service.MwebGroupGameActivityService;
import com.ygg.admin.util.MathUtil;

@Service("mwebGroupGameActivityService")
public class MwebGroupGameActivityServiceImpl implements MwebGroupGameActivityService
{
    
    @Resource
    private MwebGroupCouponDao mwebGroupCouponDao;
    
    @Resource
    private MwebGroupGameActivityDao mwebGroupGameActivityDao;
    
    @Override
    public Map<String, Object> findAllGames(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = mwebGroupGameActivityDao.findAllGames(para);
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            int isSendMsg = Integer.valueOf(map.get("isSendMsg") + "").intValue();
            map.put("msgStatus", isSendMsg == 1 ? "开启" : "关闭");
            map.put("isAvailable", ((int)map.get("isAvailableCode") == 1) ? "可用" : "停用");// 使用状态
            map.put("gameURL", "<a href='" + map.get("gameURL") + "'  target='_blank'>" + map.get("gameURL") + "</a>");
            map.put("gameLogoImage",
                "<a href='" + map.get("gameLogo") + "' target='_blank'><img alt='' src='" + map.get("gameLogo") + "' style='max-width: 100px; max-width: 100px;'/></a>");
            map.put("mwebGroupProductImage_img", "<a href='" + map.get("mwebGroupProductImage") + "' target='_blank'><img alt='' src='" + map.get("mwebGroupProductImage")
                + "' style='max-width: 100px; max-width: 100px;'/></a>");
            map.put("totalMoney", MathUtil.round(map.get("totalMoney") + "", 2));
            map.put("newBuyerAmount", MathUtil.round(map.get("newBuyerAmount") + "", 2));
            int couponId = Integer.valueOf(map.get("couponId") + "").intValue();
            MwebGroupCouponEntity coupon = mwebGroupCouponDao.findCouponById(couponId);
            MwebGroupCouponDetailEntity couponDetail = mwebGroupCouponDao.findCouponDetailById(coupon.getCouponDetailId());
            StringBuilder sb = new StringBuilder();
            if (couponDetail.getType() == 1)
            {
                sb.append("满").append(couponDetail.getThreshold()).append("减").append(couponDetail.getReduce()).append("优惠券");
            }
            else if (couponDetail.getType() == 2)
            {
                sb.append(couponDetail.getReduce()).append("元现金券");
            }
            map.put("coupon", sb.toString());
            int dateType = Integer.valueOf(map.get("dateType") + "").intValue();
            if (dateType == 1)
            {
                map.put("validityDate", coupon.getStartTime().replace(".0", "") + "~" + coupon.getEndTime().replace(".0", ""));
            }
            else if (dateType == 2)
            {
                map.put("validityDate", "自领券之日" + map.get("days") + "天内有效");
            }
            
        }
        total = mwebGroupGameActivityDao.countGames(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateGame(Map<String, Object> para)
        throws Exception
    {
        MwebGroupGameEntity game = (MwebGroupGameEntity)para.get("game");
        if (game.getId() == 0)
        {
            mwebGroupGameActivityDao.addGame(game);
            para.put("gameId", game.getId());
            mwebGroupGameActivityDao.addGamePrize(para);
            game.setGameURL(YggAdminProperties.getInstance().getPropertie("gegetuan_game_url") + game.getId());
            mwebGroupGameActivityDao.setGameURL(game);
            
        }
        else
        {
            // 更新游戏
            mwebGroupGameActivityDao.updateGame(game);
        }
        return 1;
    }
    
    @Override
    public int updateGameAvailable(Map<String, Object> para)
        throws Exception
    {
        return mwebGroupGameActivityDao.updateGameStatus(para);
    }
    
    @Override
    public int updateGameSendMsg(Map<String, Object> para)
        throws Exception
    {
        return mwebGroupGameActivityDao.updateGameStatus(para);
    }
    
    @Override
    public int deleteGame(int id)
        throws Exception
    {
        int status = mwebGroupGameActivityDao.deleteGame(id);
        if (status == 1)
            return mwebGroupGameActivityDao.deleteGamePrize(id);
        return 0;
    }
    
    @Override
    public int editMsgContent(Map<String, Object> para)
        throws Exception
    {
        return mwebGroupGameActivityDao.updateGameStatus(para);
    }
    
}
