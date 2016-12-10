package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.entity.MwebGroupBannerWindowEntity;
import com.ygg.admin.service.MwebGroupBannerWindowService;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ImageUtil;

@Controller
@RequestMapping("mwebGroupBanner")
public class MwebGroupBannerController
{
    
    @Resource(name = "mwebGroupBannerWindowService")
    private MwebGroupBannerWindowService mwebGroupBannerWindowService;
    
    @RequestMapping("/addBanner")
    public ModelAndView addBanner(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        MwebGroupBannerWindowEntity bannerWindow = new MwebGroupBannerWindowEntity();
        mv.addObject("bannerWindow", bannerWindow);
        mv.setViewName("wechatGroup/bannerForm");
        return mv;
    }
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("wechatGroup/bannerManage");
        return mv;
    }
    
    @RequestMapping("/save")
    @ControllerLog(description = "banner管理-新增/修改banner")
    public ModelAndView save(HttpServletRequest request, //
        @RequestParam(value = "editId", required = false, defaultValue = "0") int editId,//
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "image", required = true) String image,//
        @RequestParam(value = "order", required = false, defaultValue = "0") short order,// 排序值
        @RequestParam(value = "product", required = false, defaultValue = "0") int product, // 商品ID
        @RequestParam(value = "groupSale", required = false, defaultValue = "0") int groupSale,//
        @RequestParam(value = "customSale", required = false, defaultValue = "0") int customSale,//
        @RequestParam(value = "customPage", required = false, defaultValue = "0") int customPage,//
        @RequestParam(value = "startTime", required = true) String startTime,//
        @RequestParam(value = "endTime", required = true) String endTime, //
        @RequestParam(value = "isDisplay", required = true) byte isDisplay)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        MwebGroupBannerWindowEntity bannerWindow = new MwebGroupBannerWindowEntity();
        bannerWindow.setDesc(desc);
        bannerWindow.setImage((image.indexOf(ImageUtil.getPrefix()) > 0) ? image : (image + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v1banner.ordinal())));
        bannerWindow.setIsDisplay(isDisplay);

        bannerWindow.setDisplayId(product);
        bannerWindow.setOrder(order);
        bannerWindow.setStartTime(startTime);
        bannerWindow.setEndTime(endTime);
        bannerWindow.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        
        if (editId != 0)
        {
            // update
            bannerWindow.setId(editId);
            mwebGroupBannerWindowService.update(bannerWindow);
        }
        else
        {
            mwebGroupBannerWindowService.save(bannerWindow);
        }
        
        mv.setViewName("redirect:/mwebGroupBanner/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonBannerWindowInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBannerWindowInfo(
        HttpServletRequest request,// 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "bannerStatus", required = false, defaultValue = "1,2") String bannerStatus,// 1,2  // Banner状态：1等待开始，2进行中，3已结束
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") String isDisplay,//0,1  // 0不展示，1展示
        @RequestParam(value = "bannerDesc", required = false, defaultValue = "") String bannerDesc)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if ("-1".equals(isDisplay) || "-1".equals(bannerStatus))
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", new ArrayList<Object>());
            map.put("total", 0);
            return JSON.toJSONString(map);
        }
        if (!isDisplay.contains(","))
        {
            para.put("isDisplay", isDisplay);
        }
        //特卖状态
        if (!(bannerStatus.contains("1") && bannerStatus.contains("2") && bannerStatus.contains("3")))
        {
            if (bannerStatus.contains("1") && bannerStatus.contains("2"))
            {
                para.put("status", 12);
            }
            else if (bannerStatus.contains("2") && bannerStatus.contains("3"))
            {
                para.put("status", 23);
            }
            else if (bannerStatus.contains("1") && bannerStatus.contains("3"))
            {
                para.put("status", 13);
            }
            else
            {
                para.put("status", bannerStatus);
            }
        }
        
        if (!"".equals(bannerDesc))
        {
            para.put("bannerDesc", "%" + bannerDesc + "%");
        }
        para.put("now", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        
        List<MwebGroupBannerWindowEntity> bannerWindowList = mwebGroupBannerWindowService.findAllBannerWindow(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = mwebGroupBannerWindowService.packageBannerWindowList(bannerWindowList);
        resultMap.put("rows", resultList);
        int total = mwebGroupBannerWindowService.countBannerWindow(para);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateDisplayCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "banner管理-修改banner展现状态")
    public String updateDisplayCode(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("isDisplay", code == 1 ? 0 : 1);
        int resultStatus = mwebGroupBannerWindowService.updateDisplayCode(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        else
        {
            resultMap.put("status", 1);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView editTemplate(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        MwebGroupBannerWindowEntity bannerWindow = mwebGroupBannerWindowService.findBannerWindowById(id);
        if (bannerWindow == null)
        {
            mv.setViewName("error/404");
            return mv;
        }
        mv.addObject("bannerWindow", bannerWindow);
        // 转换时间
        mv.addObject("startTime", DateTimeUtil.timestampStringToWebString(bannerWindow.getStartTime()));
        mv.addObject("endTime", DateTimeUtil.timestampStringToWebString(bannerWindow.getEndTime()));
        
        mv.addObject("id", bannerWindow.getDisplayId());
        mv.setViewName("wechatGroup/bannerForm");
        return mv;
    }
    
    /**
     * 修改Banner排序值
     * 
     * @param request
     * @param order
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "banner管理-修改banner展现状态")
    public String updateOrder(HttpServletRequest request, @RequestParam(value = "order", required = true) int order, @RequestParam(value = "id", required = true) int id // bannerId
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("order", order);
        para.put("id", id);
        int resultStatus = mwebGroupBannerWindowService.updateOrder(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (resultStatus != 1)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        else
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "修改成功");
        }
        return JSON.toJSONString(resultMap);
    }
    
    
    
}
