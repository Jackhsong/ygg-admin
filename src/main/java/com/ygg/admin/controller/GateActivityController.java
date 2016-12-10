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
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.GateEntity;
import com.ygg.admin.service.CouponService;
import com.ygg.admin.service.GateActivityService;

/**
 * 任意门管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/gate")
public class GateActivityController
{
    private Logger logger = Logger.getLogger(GateActivityController.class);
    
    @Resource
    private GateActivityService gateActivityService;
    
    @Resource
    private CouponService couponService;
    
    /**
     * 游戏管理列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView gateList()
    {
        ModelAndView mv = new ModelAndView("gateActivity/gateList");
        return mv;
    }
    
    /**
     * 游戏JSON
     * @param page
     * @param rows
     * @param id
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/jsonGateInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGateInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "answer", required = false, defaultValue = "") String answer, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay)
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
            if (!"".equals(answer))
            {
                para.put("answer", "%" + answer + "%");
            }
            if (!"".equals(desc))
            {
                para.put("desc", "%" + desc + "%");
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            resultMap = gateActivityService.findAllGates(para);
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
     * @param gateName：游戏名称
     * @param gateLogo：游戏logo
     * @param introduce：游戏简介
     * @param couponId：优惠券Id
     * @param dateType：优惠券有效时间,1：使用原优惠券时间，2：发放日顺延N天
     * @param isAvailable：是否可用
     * @param day：发放日顺延天数
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateGate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "任意门管理-新增/编辑任意门")
    public String saveGate(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "theme", required = false, defaultValue = "") String theme, @RequestParam(value = "answer", required = false, defaultValue = "") String answer,
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "validTimeStart", required = false, defaultValue = "0000-00-00 00:00:00") String validTimeStart,
        @RequestParam(value = "validTimeEnd", required = false, defaultValue = "0000-00-00 00:00:00") String validTimeEnd,
        @RequestParam(value = "couponId", required = false, defaultValue = "0") int couponId,
        @RequestParam(value = "validTimeType", required = false, defaultValue = "1") int validTimeType,
        @RequestParam(value = "days", required = false, defaultValue = "0") int days, @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            
            Map<String, Object> para = new HashMap<String, Object>();
            GateEntity gate = new GateEntity();
            gate.setTheme(theme);
            gate.setId(id);
            gate.setAnswer(answer);
            gate.setDesc(desc);
            gate.setValidTimeStart(validTimeStart);
            gate.setValidTimeEnd(validTimeEnd);
            gate.setIsDisplay(isDisplay);
            if (id == 0)
            {
                CouponEntity coupon = couponService.findCouponById(couponId);
                if (coupon == null)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "Id=" + couponId + "的优惠券不存在");
                    return JSON.toJSONString(resultMap);
                }
                CouponDetailEntity couponDetail = couponService.findCouponDetailById(coupon.getCouponDetailId());
                StringBuilder sb = new StringBuilder("任意门打开了，哇~是");
                if (couponDetail.getType() == 1)
                {
                    sb.append("满").append(couponDetail.getThreshold()).append("减").append(couponDetail.getReduce()).append("优惠券");
                }
                else if (couponDetail.getType() == 2)
                {
                    sb.append(couponDetail.getReduce()).append("元现金券");
                }
                sb.append("耶！");
                gate.setReceiveTip(sb.toString());
                gate.setUrl("http://m.gegejia.com/gate/activity/web");
            }
            
            para.put("gate", gate);
            para.put("couponId", couponId);
            para.put("validTimeType", validTimeType);
            para.put("days", days);
            
            int result = gateActivityService.saveOrUpdateGate(para);
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
    
    @RequestMapping(value = "/updateGateDisplay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "任意门管理-修改任意门展现状态")
    public String updateGateDisplay(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isDisplay", isDisplay);
            int result = gateActivityService.updateGateDisplay(para);
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
    
    @RequestMapping(value = "/deleteGate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "任意门管理-删除任意门")
    public String deleteGate(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = gateActivityService.deleteGate(id);
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
    
}
