package com.ygg.admin.controller;

import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.OrderProductRefundReasonService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单商品退款原因
 *
 * @author xiongl
 * @create 2016-05-18 14:57
 */
@Controller
@RequestMapping("/orderProductRefundReason")
public class OrderProductRefundReasonController
{
    private static Logger logger = Logger.getLogger(OrderProductRefundReasonController.class);
    
    @Resource
    private OrderProductRefundReasonService orderProductRefundReasonService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("refund/refundReasonList");
        return mv;
    }
    
    @RequestMapping("/findList")
    @ResponseBody
    public Object findList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<>();
            param.put("start", rows * (page - 1));
            param.put("max", rows);
            return orderProductRefundReasonService.findOrderProductRefundReasonList(param);
        }
        catch (Exception e)
        {
            logger.info("异步加载订单退款原因列表出错了", e);
            return ResultEntity.getFailResultList();
        }
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public Object save(@RequestParam(value = "reason", required = false, defaultValue = "") String reason,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        try
        {
            return orderProductRefundReasonService.save(reason, isDisplay);
        }
        catch (Exception e)
        {
            logger.error("新增订单退款原因出错", e);
            return ResultEntity.getFailResult("新增失败");
        }
    }
    
    @RequestMapping("/update")
    @ResponseBody
    public Object update(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "reason", required = false, defaultValue = "") String reason, @RequestParam(value = "isDisplay", required = false, defaultValue = "1") int isDisplay)
    {
        try
        {
            return orderProductRefundReasonService.upadte(id, reason, isDisplay);
        }
        catch (Exception e)
        {
            logger.error("更新订单退款原因出错", e);
            return ResultEntity.getFailResult("更新失败");
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable(value = "id") int id)
    {
        try
        {
            return orderProductRefundReasonService.delete(id);
        }
        catch (Exception e)
        {
            logger.error("删除订单退款原因出错", e);
            return ResultEntity.getFailResult("删除失败");
        }
    }
}
