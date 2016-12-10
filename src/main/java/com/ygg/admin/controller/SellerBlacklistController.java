package com.ygg.admin.controller;

import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.service.SellerBlacklistService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 商家黑名单 : 活动黑名单 ， 邮费黑名单
 */
@Controller
@RequestMapping("/sellerBlacklist")
public class SellerBlacklistController
{

    Logger log = Logger.getLogger(SellerBlacklistController.class);

    @Resource
    private SellerBlacklistService sellerBlacklistService;


    @RequestMapping("/list/{type}")
    public ModelAndView list(@PathVariable("type") int type)
    {
        ModelAndView mv = new ModelAndView();
        if (type == SellerEnum.SellerBlackTypeEnum.POSTAGE.getCode())
        {
            mv.setViewName("sellerBlacklist/list1");
        }
        else if (type == SellerEnum.SellerBlackTypeEnum.ACTIVITY.getCode())
        {
            mv.setViewName("sellerBlacklist/list2");
        }
        return mv;
    }

    @RequestMapping("/ajaxData")
    @ResponseBody
    public Object ajaxData(@RequestParam(value = "sellerId", defaultValue = "0", required = false) int sellerId, //
        @RequestParam(value = "type", defaultValue = "0", required = false) int type, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows //
    )
    {
        try
        {
            page = page == 0 ? 1 : page;
            return sellerBlacklistService.findSellerBlackInfo(type, sellerId, 1, page, rows);
        }
        catch (Exception e)
        {
            log.error("异步加载商家黑名单失败！", e);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("total", 0);
            resultMap.put("rows", new HashMap<>());
            return resultMap;
        }
    }

    @RequestMapping("/save")
    @ResponseBody
    @ControllerLog(description = "商家管理-新增黑名单商家")
    public Object save(
            @RequestParam Map<String, Object> param
//            @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
//        @RequestParam(value = "sellerId", defaultValue = "0", required = false) int sellerId, //
//        @RequestParam(value = "type", defaultValue = "1", required = false) int type, //
//        @RequestParam(value = "freightMoney", required = false, defaultValue = "0") int freightMoney, //
//        @RequestParam(value = "thresholdPrice", required = false, defaultValue = "0") int thresholdPrice, // 包邮门槛
//        @RequestParam(value = "displayType", required = false, defaultValue = "0") int displayType, // 凑单跳转类型
//        @RequestParam(value = "onePageId", required = false, defaultValue = "0") int onePageId, //
//        @RequestParam(value = "oneActivitiesCustomId", required = false, defaultValue = "0") int oneActivitiesCustomId, //
//        @RequestParam(value = "oneActivitiesCommonId", required = false, defaultValue = "0") int oneActivitiesCommonId, //
//        @RequestParam(value = "oneProductId", required = false, defaultValue = "0") int oneProductId //
    )
    {
        try
        {
            return sellerBlacklistService.saveOrUpdateSellerBlackInfo(param);
        }
        catch (Exception e)
        {
            log.error("保存商家黑名单失败", e);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    @ControllerLog(description = "商家管理-删除黑名单商家")
    public Object delete(@RequestParam(value = "id", required = true) int id)
    {
        try
        {
            int status = sellerBlacklistService.deleteSellerBlackInfo(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status > 0 ? 1 : 0);
            result.put("msg", status > 0 ? "删除成功" : "删除失败");
            return result;
        }
        catch (Exception e)
        {
            log.error("删除商家黑名单失败", e);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
}
