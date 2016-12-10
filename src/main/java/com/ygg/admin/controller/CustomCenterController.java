package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.CustomCenterTypeEnum;
import com.ygg.admin.entity.CustomCenterEntity;
import com.ygg.admin.service.CustomCenterService;
import com.ygg.admin.util.CommonUtil;

/**
 * 个人中心管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/customCenter")
public class CustomCenterController
{
    private static Logger logger = Logger.getLogger(CustomCenterController.class);
    
    @Resource
    private CustomCenterService customCenterService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("customCenter/list");
        return mv;
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView("customCenter/update");
        try
        {
            CustomCenterEntity center = customCenterService.findCustomCenterById(id);
            if (center == null)
            {
                center = new CustomCenterEntity();
            }
            mv.addObject("center", center);
            mv.addObject("oneDisplayId", center.getOneDisplayId());
            mv.addObject("twoDisplayId", center.getTwoDisplayId());
            mv.addObject("threeDisplayId", center.getThreeDisplayId());
            mv.addObject("fourDisplayId", center.getFourDisplayId());
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
            logger.error("编辑个人中心出错", e);
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsoncustomCenterInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsoncustomCenterInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,//
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            if (isDisplay != -1)
            {
                para.put("isDisplay", isDisplay);
            }
            Map<String, Object> result = customCenterService.jsonCustomCenterInfo(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            logger.error("异步加载自定定义功能列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdate(HttpServletRequest request)
    {
        try
        {
            String oneType = StringUtils.isEmpty(request.getParameter("oneType")) ? "1" : request.getParameter("oneType");
            String oneProductId = StringUtils.isEmpty(request.getParameter("oneProductId")) ? "0" : request.getParameter("oneProductId");
            String oneGroupSale = StringUtils.isEmpty(request.getParameter("oneGroupSale")) ? "0" : request.getParameter("oneGroupSale");
            String oneCustomSale = StringUtils.isEmpty(request.getParameter("oneCustomSale")) ? "0" : request.getParameter("oneCustomSale");
            String oneAppCustomPage = StringUtils.isEmpty(request.getParameter("oneAppCustomPage")) ? "0" : request.getParameter("oneAppCustomPage");
            String twoType = StringUtils.isEmpty(request.getParameter("twoType")) ? "1" : request.getParameter("twoType");
            String twoProductId = StringUtils.isEmpty(request.getParameter("twoProductId")) ? "0" : request.getParameter("twoProductId");
            String twoGroupSale = StringUtils.isEmpty(request.getParameter("twoGroupSale")) ? "0" : request.getParameter("twoGroupSale");
            String twoCustomSale = StringUtils.isEmpty(request.getParameter("twoCustomSale")) ? "0" : request.getParameter("twoCustomSale");
            String twoAppCustomPage = StringUtils.isEmpty(request.getParameter("twoAppCustomPage")) ? "0" : request.getParameter("twoAppCustomPage");
            String threeType = StringUtils.isEmpty(request.getParameter("threeType")) ? "1" : request.getParameter("threeType");
            String threeProductId = StringUtils.isEmpty(request.getParameter("threeProductId")) ? "0" : request.getParameter("threeProductId");
            String threeGroupSale = StringUtils.isEmpty(request.getParameter("threeGroupSale")) ? "0" : request.getParameter("threeGroupSale");
            String threeCustomSale = StringUtils.isEmpty(request.getParameter("threeCustomSale")) ? "0" : request.getParameter("threeCustomSale");
            String threeAppCustomPage = StringUtils.isEmpty(request.getParameter("threeAppCustomPage")) ? "0" : request.getParameter("threeAppCustomPage");
            String fourType = StringUtils.isEmpty(request.getParameter("fourType")) ? "1" : request.getParameter("fourType");
            String fourProductId = StringUtils.isEmpty(request.getParameter("fourProductId")) ? "0" : request.getParameter("fourProductId");
            String fourGroupSale = StringUtils.isEmpty(request.getParameter("fourGroupSale")) ? "0" : request.getParameter("fourGroupSale");
            String fourCustomSale = StringUtils.isEmpty(request.getParameter("fourCustomSale")) ? "0" : request.getParameter("fourCustomSale");
            String fourAppCustomPage = StringUtils.isEmpty(request.getParameter("fourAppCustomPage")) ? "0" : request.getParameter("fourAppCustomPage");
            
            CustomCenterEntity center = new CustomCenterEntity();
            CommonUtil.wrapParamter2Entity(center, request);
            
            if (Byte.parseByte(oneType) == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                center.setOneDisplayId(Integer.parseInt(oneProductId));
            }
            else if (Byte.parseByte(oneType) == CustomCenterTypeEnum.GROUP.getCode())
            {
                center.setOneDisplayId(Integer.parseInt(oneGroupSale));
            }
            else if (Byte.parseByte(oneType) == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                center.setOneDisplayId(Integer.parseInt(oneCustomSale));
            }
            else if (Byte.parseByte(oneType) == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                center.setOneDisplayId(Integer.parseInt(oneAppCustomPage));
            }
            if (Byte.parseByte(twoType) == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                center.setTwoDisplayId(Integer.parseInt(twoProductId));
            }
            else if (Byte.parseByte(twoType) == CustomCenterTypeEnum.GROUP.getCode())
            {
                center.setTwoDisplayId(Integer.parseInt(twoGroupSale));
            }
            else if (Byte.parseByte(twoType) == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                center.setTwoDisplayId(Integer.parseInt(twoCustomSale));
            }
            else if (Byte.parseByte(twoType) == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                center.setTwoDisplayId(Integer.parseInt(twoAppCustomPage));
            }
            if (Byte.parseByte(threeType) == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                center.setThreeDisplayId(Integer.parseInt(threeProductId));
            }
            else if (Byte.parseByte(threeType) == CustomCenterTypeEnum.GROUP.getCode())
            {
                center.setThreeDisplayId(Integer.parseInt(threeGroupSale));
            }
            else if (Byte.parseByte(threeType) == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                center.setThreeDisplayId(Integer.parseInt(threeCustomSale));
            }
            else if (Byte.parseByte(threeType) == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                center.setThreeDisplayId(Integer.parseInt(threeAppCustomPage));
            }
            if (Byte.parseByte(fourType) == CustomCenterTypeEnum.SINGLE_SALE_PRODUCT.getCode())
            {
                center.setFourDisplayId(Integer.parseInt(fourProductId));
            }
            else if (Byte.parseByte(fourType) == CustomCenterTypeEnum.GROUP.getCode())
            {
                center.setFourDisplayId(Integer.parseInt(fourGroupSale));
            }
            else if (Byte.parseByte(fourType) == CustomCenterTypeEnum.CUSTOM_ACTIVITY.getCode())
            {
                center.setFourDisplayId(Integer.parseInt(fourCustomSale));
            }
            else if (Byte.parseByte(fourType) == CustomCenterTypeEnum.CUSTOM_PAGE.getCode())
            {
                center.setFourDisplayId(Integer.parseInt(fourAppCustomPage));
            }
            if (center.getId() == 0)
            {
                return customCenterService.saveCustomCenter(center);
            }
            else
            {
                return customCenterService.updateCustomCenter(center);
            }
            
        }
        catch (Exception e)
        {
            logger.error("编辑首页自定义功能出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDisplayStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isDisplay", code);
            List<Integer> idList = new ArrayList<Integer>();
            String[] idStrArr = id.split(",");
            for (String idStr : idStrArr)
            {
                idList.add(Integer.valueOf(idStr.trim()).intValue());
            }
            para.put("idList", idList);
            int status = customCenterService.updateDisplayStatus(para);
            if (status > 0)
            {
                result.put("status", 1);
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
}
