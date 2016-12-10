package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.SignInService;

/**
 * 签到功能
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/signin")
public class SignInController
{
    private Logger logger = Logger.getLogger(SignInController.class);
    
    @Resource
    private SignInService signInService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("signin/signinSetting");
            List<Map<String, String>> yearMonthList = new ArrayList<Map<String, String>>();
            
            //可供查询的时间默认为活动起始时间到当前时间之后3个月
            int beingYearMonth = signInService.beginYearMonth();
            DateTime beginTime = DateTime.parse(beingYearMonth + "", DateTimeFormat.forPattern("yyyyMM"));
            DateTime endTime = DateTime.now().plusMonths(3);
            int month = Months.monthsBetween(beginTime, endTime).getMonths();
            for (int i = 0; i < month; i++)
            {
                Map<String, String> map = new HashMap<String, String>();
                String timeName = beginTime.plusMonths(i).toString("yyyy年MM月");
                String timeValue = beginTime.plusMonths(i).toString("yyyyMM");
                map.put("name", timeName);
                map.put("value", timeValue);
                yearMonthList.add(map);
            }
            mv.addObject("yearMonthList", yearMonthList);
            mv.addObject("currentMonth", DateTime.now().toString("yyyyMM"));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonSettingInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSettingInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "month", required = false, defaultValue = "-1") int month)
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
            if (month == -1)
            {
                month = Integer.valueOf(DateTime.now().toString("yyyyMM"));
            }
            para.put("yearMonth", month);
            resultMap = signInService.findAllSignSetting(para);
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
     * @param id：id=0,表示新增
     * @param yearMonth：新增到哪年哪月，等于-1表示当前年月
     * @param signType：奖励类型，1积分，2优惠券，默认1
     * @param score：signType=1时使用该字段，奖励分数，默认0
     * @param couponId：signType=2时使用该字段，优惠券Id，默认0
     * @param signStyle：样式，1普通，2高亮，3月底大奖，默认1
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "签到管理-新增/修改签到奖励")
    public String saveOrUpdate(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "yearMonth", required = false, defaultValue = "-1") int yearMonth,
        @RequestParam(value = "signType", required = false, defaultValue = "1") int signType, @RequestParam(value = "score", required = false, defaultValue = "0") int score,
        @RequestParam(value = "couponId", required = false, defaultValue = "0") int couponId, @RequestParam(value = "signStyle", required = false, defaultValue = "1") int signStyle)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            if (yearMonth == -1)
            {
                yearMonth = Integer.valueOf(DateTime.now().toString("yyyyMM"));
            }
            para.put("yearMonth", yearMonth);
            para.put("type", signType);
            para.put("point", score);
            para.put("couponId", couponId);
            para.put("style", signStyle);
            int status = signInService.saveOrUpdate(para);
            if (status == 1)
            {
                resultMap.put("status", status);
                resultMap.put("msg", "保存成功");
            }
            else if (status == 2)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "本月天数已经全部配置好，无需再配置");
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
            resultMap.put("msg", "服务器发生异常，请刷新后重试.");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/copyFromLastMonth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "签到管理-从上月复制签到奖励")
    public String copyFromLastMonth(@RequestParam(value = "yearMonth", required = false, defaultValue = "-1") int yearMonth)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            DateTime currMonth = DateTime.parse(yearMonth + "", DateTimeFormat.forPattern("yyyyMM"));
            DateTime lastMonth = currMonth.minusMonths(1);
            
            para.put("yearMonth", currMonth.toString("yyyyMM"));
            resultMap = signInService.findAllSignSetting(para);
            int total = Integer.valueOf(resultMap.get("total") + "");
            
            int lastMonthMaxDay = lastMonth.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
            int currMonthMaxDay = currMonth.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
            if (lastMonthMaxDay <= currMonthMaxDay)
            {
                para.put("max", lastMonthMaxDay);
            }
            else
            {
                para.put("max", currMonthMaxDay);
            }
            para.put("start", 0);
            para.put("lastMonth", lastMonth.toString("yyyyMM"));
            para.put("currMonth", currMonth.toString("yyyyMM"));
            Map<String, Integer> statusMap = signInService.copyFromLastMonth(para);
            if (statusMap == null || statusMap.get("status") == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "复制失败");
            }
            else
            {
                int status = statusMap.get("status").intValue();
                int count = statusMap.get("count").intValue();
                if (status == 1)
                {
                    resultMap.put("status", status);
                    StringBuffer sb = new StringBuffer();
                    if (currMonthMaxDay > lastMonthMaxDay)
                    {
                        if (total <= 0)
                        {
                            sb.append("已成功从").append(lastMonth.toString("yyyy年MM月")).append("复制").append(count).append("条数据，本月还需您配置").append(currMonthMaxDay - count).append("条");
                        }
                        else
                        {
                            sb.append("已成功从").append(lastMonth.toString("yyyy年MM月")).append("复制").append(count).append("条数据");
                        }
                        
                    }
                    else if (currMonthMaxDay < lastMonthMaxDay)
                    {
                        sb.append("已成功从").append(lastMonth.toString("yyyy年MM月")).append("复制前").append(count).append("条数据");
                    }
                    else
                    {
                        sb.append("已成功从").append(lastMonth.toString("yyyy年MM月")).append("复制").append(count).append("条数据");
                    }
                    resultMap.put("msg", sb.toString());
                }
                else if (status == 2)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", lastMonth.toString("yyyy年MM月") + "的数据还未配置，不能复制");
                }
                else if (status == 3)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", currMonth.toString("yyyy年MM月") + "的数据已经全部配置好，无需配置");
                }
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
    
    @RequestMapping(value = "/checkCurrentMonthDays", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkCurrentMonthDays(@RequestParam(value = "yearMonth", required = true) int yearMonth)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime currMonth = DateTime.parse(yearMonth + "", DateTimeFormat.forPattern("yyyyMM"));
            DateTime lastMonth = currMonth.minusMonths(1);
            int currMonthMaxDay = currMonth.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth();
            para.put("yearMonth", currMonth.toString("yyyyMM"));
            resultMap = signInService.findAllSignSetting(para);
            int total = Integer.valueOf(resultMap.get("total") + "");
            if (total == 0)
            {
                resultMap.put("status", 1);
            }
            else if (total == currMonthMaxDay)
            {
                resultMap.put("status", 2);
                resultMap.put("msg", currMonth.toString("yyyy年MM月") + "已经全部配置好，继续复制将覆盖本月配置。确认复制吗？");
            }
            else
            {
                resultMap.put("status", 3);
                resultMap.put("msg", currMonth.toString("yyyy年MM月") + "已经配置了" + total + "天，确定继续复制吗？继续复制将从" + lastMonth.toString("yyyy年MM月") + "复制前" + (currMonthMaxDay - total)
                    + "天");
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
    
    @RequestMapping("/signinProductList")
    public ModelAndView signinProductList()
    {
        ModelAndView mv = new ModelAndView("signin/signinProductList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonSigninProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSigninProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "productId", required = false, defaultValue = "0") Long productId,
        @RequestParam(value = "productCode", required = false, defaultValue = "") String productCode,
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
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
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (!"".equals(productCode))
            {
                para.put("productCode", productCode);
            }
            if (!"".equals(productName))
            {
                para.put("productName", "%" + productName + "%");
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            resultMap = signInService.findAllSigninProduct(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/saveOrUpdateProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "签到管理-新增签到商品")
    public String saveOrUpdateProduct(@RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "sequence", required = false, defaultValue = "0") int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (productId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入商品ID");
                return JSON.toJSONString(resultMap);
            }
            
            if (sequence < 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "排序值必须大于或等于0");
                return JSON.toJSONString(resultMap);
            }
            return signInService.addSigninProduct(productId, sequence);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/deleteSigninProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "签到管理-删除签到商品")
    public String deleteSigninProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要删除的商品");
                return JSON.toJSONString(resultMap);
            }
            return signInService.deleteSigninProduct(Arrays.asList(ids.split(",")));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateSigninProductDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "签到管理-更新签到商品展现状态")
    public String updateSigninProductDisplayStatus(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isDisplay", required = true) int isDisplay)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的商品");
                return JSON.toJSONString(resultMap);
            }
            return signInService.updateSigninProductDisplayStatus(Arrays.asList(ids.split(",")), isDisplay);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateSigninProductSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "签到管理-更新签到商品排序值")
    public String updateSigninProductSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            return signInService.updateSigninProductSequence(id, sequence);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
}
