package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.service.RefundReasonService;

/**
 * 订单退款售后归档原因
 */
@Controller
@RequestMapping("refundReason")
public class RefundReasonController
{
    @Resource
    private RefundReasonService refundReasonService;
    
    @RequestMapping("toList")
    public ModelAndView toList() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("refundReason/list");
        return mv;
    }
    
    @RequestMapping("save")
    @ResponseBody
    public Object save(String reason, String isAvailable) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("reason", reason);
            param.put("isAvailable", isAvailable);
            refundReasonService.saveRefundReason(param);
            param.clear();
            param.put("status", 1);
            return param;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    @RequestMapping("findList")
    @ResponseBody
    public Object findList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, String isAvailable) throws Exception {
        page = page == 0 ? 1 : page;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("isAvailable", isAvailable == "" ? null : isAvailable);
        return refundReasonService.findRefundReasonList(param);
    }
    
    @RequestMapping("update/{id}")
    @ResponseBody
    public Object update(@PathVariable String id, String isAvailable) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", id);
            param.put("isAvailable", isAvailable);
            refundReasonService.updateRefundReason(param);
            param.clear();
            param.put("status", 1);
            return param;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    

}
