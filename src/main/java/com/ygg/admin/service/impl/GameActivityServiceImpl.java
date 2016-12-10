package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.CouponDao;
import com.ygg.admin.dao.GameActivityDao;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.GameEntity;
import com.ygg.admin.service.GameActivityService;
import com.ygg.admin.util.MathUtil;

@Service("gameActivityService")
public class GameActivityServiceImpl implements GameActivityService
{
    
    @Resource
    private CouponDao couponDao;
    
    @Resource
    private GameActivityDao gameActivityDao;
    
    @Override
    public Map<String, Object> findAllGames(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = gameActivityDao.findAllGames(para);
        int total = 0;
        for (Map<String, Object> map : infoList)
        {
            int isSendMsg = Integer.valueOf(map.get("isSendMsg") + "").intValue();
            map.put("msgStatus", isSendMsg == 1 ? "开启" : "关闭");
            map.put("isAvailable", ((int)map.get("isAvailableCode") == 1) ? "可用" : "停用");// 使用状态
            map.put("gameURL", "<a href='" + map.get("gameURL") + "'  target='_blank'>" + map.get("gameURL") + "</a>");
            map.put("gameLogoImage", "<a href='" + map.get("gameLogo") + "' target='_blank'><img alt='' src='" + map.get("gameLogo")
                + "' style='max-width: 100px; max-width: 100px;'/></a>");
            map.put("totalMoney", MathUtil.round(map.get("totalMoney") + "", 2));
            map.put("newBuyerAmount", MathUtil.round(map.get("newBuyerAmount") + "", 2));
            int couponId = Integer.valueOf(map.get("couponId") + "").intValue();
            CouponEntity coupon = couponDao.findCouponById(couponId);
            CouponDetailEntity couponDetail = couponDao.findCouponDetailById(coupon.getCouponDetailId());
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
                map.put("validityDate", coupon.getStartTime() + "~" + coupon.getEndTime());
            }
            else if (dateType == 2)
            {
                map.put("validityDate", "自领券之日" + map.get("days") + "天内有效");
            }
            
        }
        total = gameActivityDao.countGames(para);
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int saveOrUpdateGame(Map<String, Object> para)
        throws Exception
    {
        GameEntity game = (GameEntity)para.get("game");
        if (game.getId() == 0)
        {
            gameActivityDao.addGame(game);
            para.put("gameId", game.getId());
            gameActivityDao.addGamePrize(para);
            game.setGameURL("http://m.gegejia.com/ygg/game/web/" + game.getId());
            gameActivityDao.setGameURL(game);
            
        }
        else
        {
            //更新游戏
            gameActivityDao.updateGame(game);
        }
        return 1;
    }
    
    @Override
    public int updateGameAvailable(Map<String, Object> para)
        throws Exception
    {
        return gameActivityDao.updateGameStatus(para);
    }
    
    @Override
    public int updateGameSendMsg(Map<String, Object> para)
        throws Exception
    {
        return gameActivityDao.updateGameStatus(para);
    }
    
    @Override
    public int deleteGame(int id)
        throws Exception
    {
        int status = gameActivityDao.deleteGame(id);
        if (status == 1)
            return gameActivityDao.deleteGamePrize(id);
        return 0;
    }
    
    @Override
    public int editMsgContent(Map<String, Object> para)
        throws Exception
    {
        return gameActivityDao.updateGameStatus(para);
    }
    
}
