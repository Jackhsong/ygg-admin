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
import com.ygg.admin.service.MwebGroupCouponService;
import com.ygg.admin.service.MwebGroupProductService;
import com.ygg.admin.service.MwebGroupTeamHeadFreeOrderService;

@Controller
@RequestMapping("/mwebGroupTeamHeadFreeOrder")
public class MwebGroupTeamHeadFreeOrderController
{
    private Logger logger = Logger.getLogger(MwebGroupTeamHeadFreeOrderController.class);
    
    @Resource
    private MwebGroupTeamHeadFreeOrderService mwebGroupTeamHeadFreeOrderService;
    
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
        ModelAndView mv = new ModelAndView("mwebGroupTeamHeadFreeOrder/teamHeadFreeOrderList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonTeamHeadFreeOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonTeamHeadFreeOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
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
            
            resultMap = mwebGroupTeamHeadFreeOrderService.findTeamHeadFreeOrder(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/saveTeamHeadFreeOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "左岸城堡团长免单管理")
    public String saveTeamHeadFreeOrder(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "giveChance", required = false, defaultValue = "0") int giveChance,
        @RequestParam(value = "couponId", required = false, defaultValue = "0") int couponId,
        @RequestParam(value = "mwebGroupProductId", required = false, defaultValue = "0") int mwebGroupProductId,
        @RequestParam(value = "dateType", required = false, defaultValue = "1") int dateType, @RequestParam(value = "days", required = false, defaultValue = "0") int days)
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
            JSONObject j = new JSONObject();
            j.put("id", id);
            j.put("giveChance", giveChance);
            j.put("couponId", couponId);
            j.put("mwebGroupProductId", mwebGroupProductId);
            j.put("dateType", dateType);
            j.put("days", days);
            int result = mwebGroupTeamHeadFreeOrderService.saveOrUpdateTeamHeadFreeOrder(j);
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
    
    @RequestMapping(value = "/updateIsOpenGive", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "游戏管理-修改游戏可用状态")
    public String updateGameAvailable(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isOpenGive", required = true) int isOpenGive)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isOpenGive", isOpenGive);
            int result = mwebGroupTeamHeadFreeOrderService.updateIsOpenGive(para);
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
}
