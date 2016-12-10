package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.entity.MwebGroupCouponEntity;
import com.ygg.admin.entity.MwebGroupGameEntity;
import com.ygg.admin.service.MwebGroupCouponService;
import com.ygg.admin.service.MwebGroupGameActivityService;
import com.ygg.admin.service.MwebGroupProductService;

@Controller
@RequestMapping("/mwebGroupGame")
public class MwebGroupGameActivityController
{
    private Logger logger = Logger.getLogger(MwebGroupGameActivityController.class);
    
    @Resource
    private MwebGroupGameActivityService mwebGroupGameActivityService;
    
    @Resource
    private MwebGroupCouponService mwebGroupCouponService;
    
    @Resource
    private MwebGroupProductService mwebGroupProductService;
    
    /**
     * 游戏管理列表
     * 
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView gameList()
    {
        ModelAndView mv = new ModelAndView("mwebGroupGame/gameList");
        return mv;
    }
    
    /**
     * 游戏JSON
     * 
     * @param page
     * @param rows
     * @param id
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonGameInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGameInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "gameName", required = false, defaultValue = "") String gameName,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (id != 0)
            {
                para.put("id", id);
            }
            if (!"".equals(gameName))
            {
                para.put("gameName", "%" + gameName + "%");
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = mwebGroupGameActivityService.findAllGames(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 
     * @param id：id
     * @param gameName：游戏名称
     * @param gameLogo：游戏logo
     * @param introduce：游戏简介
     * @param couponId：优惠券Id
     * @param dateType：优惠券有效时间,1：使用原优惠券时间，2：发放日顺延N天
     * @param isAvailable：是否可用
     * @param day：发放日顺延天数
     * @return
     */
    @RequestMapping(value = "/saveGame", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-新增任游戏")
    public String saveGame(@RequestParam(value = "gameId", required = false, defaultValue = "0") int id,
        @RequestParam(value = "gameName", required = false, defaultValue = "") String gameName,
        @RequestParam(value = "gameLogo", required = false, defaultValue = "") String gameLogo,
        @RequestParam(value = "mwebGroupProductImage", required = false, defaultValue = "") String mwebGroupProductImage,
        @RequestParam(value = "introduce", required = false, defaultValue = "") String introduce,
        @RequestParam(value = "couponId", required = false, defaultValue = "0") int couponId,
        @RequestParam(value = "mwebGroupProductId", required = false, defaultValue = "0") int mwebGroupProductId,
        @RequestParam(value = "dateType", required = false, defaultValue = "1") int dateType, @RequestParam(value = "days", required = false, defaultValue = "0") int days,
        @RequestParam(value = "prizeId", required = false, defaultValue = "0") int prizeId,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            
            MwebGroupCouponEntity coupon = mwebGroupCouponService.findCouponById(couponId);
            if (coupon == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "优惠券不存在");
                return JSON.toJSONString(resultMap);
            }
            MwebGroupCouponDetailEntity detail = mwebGroupCouponService.findCouponDetailById(coupon.getCouponDetailId());
            if (detail == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "优惠券不存在");
                return JSON.toJSONString(resultMap);
            }
            
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", mwebGroupProductId);
            JSONObject res = mwebGroupProductService.findProductAndStockForTeamById(param);
            if (res.getString("name") == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "对应的商品不存在");
                return JSON.toJSONString(resultMap);
            }
            
            Map<String, Object> para = new HashMap<String, Object>();
            MwebGroupGameEntity game = new MwebGroupGameEntity();
            game.setId(id);
            game.setMwebGroupProductId(mwebGroupProductId);
            game.setMwebGroupProductImage(mwebGroupProductImage);
            game.setGameName(gameName);
            game.setGameLogo(gameLogo);
            game.setIntroduce(introduce);
            game.setIsAvailable(isAvailable);
            para.put("game", game);
            para.put("couponId", couponId);
            para.put("dateType", dateType);
            para.put("days", days);
            para.put("prizeId", prizeId);
            para.put("mwebGroupProductId", mwebGroupProductId);
            para.put("mwebGroupProductImage", mwebGroupProductImage);
            
            int result = mwebGroupGameActivityService.saveOrUpdateGame(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateGame", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-编辑游戏")
    public String updateGame(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "mwebGroupProductId", required = false, defaultValue = "0") int mwebGroupProductId,
        @RequestParam(value = "gameName", required = false, defaultValue = "") String gameName,
        @RequestParam(value = "gameLogo", required = false, defaultValue = "") String gameLogo,
        @RequestParam(value = "mwebGroupProductImage", required = false, defaultValue = "") String mwebGroupProductImage,
        @RequestParam(value = "introduce", required = false, defaultValue = "") String introduce)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            MwebGroupGameEntity game = new MwebGroupGameEntity();
            game.setId(id);
            game.setMwebGroupProductId(mwebGroupProductId);
            game.setMwebGroupProductImage(mwebGroupProductImage);
            game.setGameName(gameName);
            game.setGameLogo(gameLogo);
            game.setIntroduce(introduce);
            para.put("game", game);
            
            int result = mwebGroupGameActivityService.saveOrUpdateGame(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateGameAvailable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-修改游戏可用状态")
    public String updateGameAvailable(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            // List<Integer> idList = new ArrayList<Integer>();
            // String[] idArr = id.split(",");
            // for (String idStr : idArr)
            // {
            // idList.add(Integer.valueOf(idStr));
            // }
            //
            para.put("id", id);
            para.put("isAvailable", isAvailable);
            int result = mwebGroupGameActivityService.updateGameAvailable(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "更新失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateGameSendMsg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-修改游戏是否发送短信")
    public String updateGameSendMsg(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "status", required = true) int status)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isSendMsg", status);
            int result = mwebGroupGameActivityService.updateGameSendMsg(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/deleteGame", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-删除游戏")
    public String deleteGame(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = mwebGroupGameActivityService.deleteGame(id);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "删除失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/editMsgContent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-修改游戏发送短信内容")
    public String editMsgContent(@RequestParam(value = "gameId", required = true) int gameId, @RequestParam(value = "msgContent", required = true) String msgContent)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", gameId);
            para.put("msgContent", msgContent);
            int result = mwebGroupGameActivityService.editMsgContent(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
}
