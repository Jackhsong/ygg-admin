package com.ygg.admin.controller;

import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.FortuneWheelPrizeEnum;
import com.ygg.admin.entity.CouponEntity;
import com.ygg.admin.entity.FortuneWheelEntity;
import com.ygg.admin.entity.FortuneWheelPrizeEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.CouponService;
import com.ygg.admin.service.FortuneWheelPrizeService;
import com.ygg.admin.service.FortuneWheelService;
import com.ygg.admin.util.BeanUtil;
import com.ygg.admin.util.CgiUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-5-17
 */
@Controller
@RequestMapping("/fortuneWheel")
public class FortuneWheelController {

    private Logger logger = Logger.getLogger(FortuneWheelController.class);

    @Resource
    private FortuneWheelService fortuneWheelService;

    @Resource
    private FortuneWheelPrizeService fortuneWheelPrizeService;

    @Resource
    private CouponService couponService;

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("fortuneWheel/list");
        return mv;
    }

    @RequestMapping("/jsonInfo")
    @ResponseBody
    public Object jsonInfo(
            HttpServletRequest req,
            @RequestParam(defaultValue = "0") int id,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "-1") int isAvailable,
            @RequestParam(defaultValue = "") String createTimeBegin,
            @RequestParam(defaultValue = "") String createTimeEnd,
            @RequestParam(defaultValue = "") String startTimeBegin,
            @RequestParam(defaultValue = "") String startTimeEnd

    ) {
        Map<String, Object> para = CgiUtil.getPageParaMap(req, 50);
        if (id != 0) {
            para.put("id", id);
        }
        if (StringUtils.isNotEmpty(name)) {
            para.put("name", name);
        }
        if (isAvailable != -1) {
            para.put("isAvailable", isAvailable);
        }
        if (StringUtils.isNotEmpty(createTimeBegin)) {
            para.put("createTimeBegin", createTimeBegin);
        }
        if (StringUtils.isNotEmpty(createTimeEnd)) {
            para.put("createTimeEnd", createTimeEnd);
        }
        if (StringUtils.isNotEmpty(startTimeBegin)) {
            para.put("startTimeBegin", startTimeBegin);
        }
        if (StringUtils.isNotEmpty(startTimeEnd)) {
            para.put("startTimeEnd", startTimeEnd);
        }
        try {
            return fortuneWheelService.jsonInfo(para);
        } catch (Exception e) {
            logger.error("异步加载大转盘出错", e);
            Map<String, Object> result = new HashMap<>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<>());
            return result;
        }
    }

    @RequestMapping("/addUpdate")
    public ModelAndView add(
            @RequestParam(value = "id", required = false, defaultValue = "-1") int id
    ) {
        ModelAndView mv = new ModelAndView("fortuneWheel/saveOrEdit");
        try {
            Map para = new HashMap();
            if (id > 0) {
                para.put("id", id);
                FortuneWheelEntity entity = fortuneWheelService.findById(id);
                if (entity != null) {
                    mv.addObject("fortuneWheel", entity);
                    mv.addObject("fortuneWheelId", id);
                    Map m = BeanUtil.copyProperties(entity, Map.class);
                    mv.addAllObjects(m);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return mv;

    }


    @RequestMapping("/editOrSave")
    @ResponseBody
    @ControllerLog(description = "大转盘管理————保存或者编辑大转盘")
    public ResultEntity editOrSave(
            FortuneWheelEntity entity,
            HttpServletRequest req
    ) {
        try {
            String[] topPics = req.getParameterValues("topPics");
            if (entity.getId() != 0) {
                fortuneWheelService.update(entity);
            } else {
                fortuneWheelService.save(entity);
            }
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            logger.error("保存或者编辑大转盘失败", e);
            return ResultEntity.getFailResult(e.getMessage());
        }

    }


    @RequestMapping("/prizeJsonInfo")
    @ResponseBody
    public Map<String, Object> prizeJsonInfo(
            @RequestParam(value = "fortuneWheelId", defaultValue = "0", required = false) int fortuneWheelId
    ) {
        Map para = new HashMap();
        if (fortuneWheelId <= 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<>());
            return result;
        }
        para.put("fortuneWheelId", fortuneWheelId);
        try {
            return fortuneWheelPrizeService.jsonInfo(para);
        } catch (Exception e) {
            logger.error("异步加载大转盘prize出错", e);
            Map<String, Object> result = new HashMap<>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<>());
            return result;
        }
    }

    @RequestMapping("/savePrize")
    @ResponseBody
    public ResultEntity savePrize(
            FortuneWheelPrizeEntity prize
    ) {
        try {
            if (prize.getId() == 0) {
                //todo check illegal
                if (prize.getType() == FortuneWheelPrizeEnum.TYPE.COUPON.getCode()) {
                    CouponEntity coupon = couponService.findCouponById(prize.getCouponId());
                    if (coupon == null) return ResultEntity.getFailResult("优惠券不存在 id: " + prize.getCouponId());
                }
                fortuneWheelPrizeService.save(prize);

            } else {
                fortuneWheelPrizeService.update(prize);
            }
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            logger.error(prize, e);
            return ResultEntity.getFailResult(e.getMessage());
        }
    }

    @RequestMapping("updatePrizeAvailable")
    @ResponseBody
    public ResultEntity updatePrizeAvailable(
            @RequestParam(required = true) int id,
            @RequestParam(required = true) int isAvailable
    ) {
        try {
            fortuneWheelPrizeService.updateIsAvailable(id, isAvailable);
            return ResultEntity.getSuccessResult();
        } catch (Exception e) {
            logger.error(id + " | " + isAvailable, e);
            return ResultEntity.getFailResult(e.getMessage());
        }


    }


}
