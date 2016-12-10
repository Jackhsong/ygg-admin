package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.CustomPageEntity;
import com.ygg.admin.entity.PageCustomEntity;
import com.ygg.admin.service.PageCustomService;

@Controller
@RequestMapping("/pageCustom")
public class PageCustomController
{
    private Logger logger = Logger.getLogger(PageCustomController.class);
    
    @Resource
    private PageCustomService pageCustomService;
    
    @RequestMapping("/addPageCustom")
    public ModelAndView addGroupSale(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pageCustom/pageCustomForm");
        CustomPageEntity pageCustom = new CustomPageEntity();
        mv.addObject("pageCustom", pageCustom);
        return mv;
    }
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pageCustom/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "fileName", required = false, defaultValue = "") String fileName,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 特卖名称
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable // 展现状态
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(fileName))
        {
            para.put("fileName", "%" + fileName + "%");
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        
        List<PageCustomEntity> pageCustomList = pageCustomService.findPageCustom(para);
        Map resultMap = new HashMap();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (PageCustomEntity pageCustomEntity : pageCustomList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", pageCustomEntity.getId());
            map.put("isAvailable", (pageCustomEntity.getIsAvailable() == (byte)1) ? "可用" : "停用");
            map.put("name", pageCustomEntity.getName());
            map.put("fileName", pageCustomEntity.getFileName());
            map.put("pcUrl", pageCustomEntity.getMobileUrl());
            map.put("desc", pageCustomEntity.getDesc());
            resultList.add(map);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", pageCustomService.countPageCustom(para));
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        PageCustomEntity pageCustom = pageCustomService.findPageCustomById(id);
        if (pageCustom == null)
        {
            mv.setViewName("forward:/error/404");
            return mv;
        }
        mv.addObject("pageCustom", pageCustom);
        mv.setViewName("pageCustom/pageCustomForm");
        return mv;
    }
    
    @RequestMapping("/save")
    @ControllerLog(description = "自定义页面管理-新增/编辑自定义页面")
    public ModelAndView save(HttpServletRequest request, @RequestParam(value = "editId", required = false, defaultValue = "0") int editId,
        @RequestParam(value = "name", required = true) String name, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "fileName", required = false, defaultValue = "") String fileName,
        @RequestParam(value = "pcDetail", required = false, defaultValue = "") String pcDetail,
        @RequestParam(value = "mobileDetail", required = false, defaultValue = "") String mobileDetail, @RequestParam(value = "isAvailable", required = true) byte isAvailable)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        if ("".equals(mobileDetail))
        {
            mobileDetail = pcDetail;
        }
        
        PageCustomEntity pageCustom = new PageCustomEntity();
        pageCustom.setDesc(desc);
        pageCustom.setMobileDetail(mobileDetail);
        pageCustom.setName(name);
        pageCustom.setPcDetail(pcDetail);
        pageCustom.setFileName(fileName);
        // 设置访问url
        pageCustom.setPcUrl("http://static.gegejia.com/custom/" + fileName + "_app.html");
        pageCustom.setMobileUrl("http://static.gegejia.com/custom/" + fileName + "_web.html");
        pageCustom.setIsAvailable(isAvailable);
        
        pageCustomService.write(pageCustom);
        
        if (editId != 0)
        {
            pageCustom.setId(editId);
            pageCustomService.update(pageCustom);
        }
        else
        {
            pageCustomService.save(pageCustom);
        }
        mv.setViewName("redirect:/pageCustom/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonPageCustomCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPageCustomCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<PageCustomEntity> pageCustomList = pageCustomService.findPageCustom(para);
            
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (PageCustomEntity pageCustomEntity : pageCustomList)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", pageCustomEntity.getId() + "");
                map.put("text", pageCustomEntity.getName());
                if (pageCustomEntity.getId() == id)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, String> mapAll = new HashMap<String, String>();
            mapAll.put("code", "0");
            mapAll.put("text", "暂无数据");
            codeList.add(mapAll);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/findPageCustom", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findPageCustom(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            PageCustomEntity pce = pageCustomService.findPageCustomById(id);
            if (pce == null)
            {
                resultMap.put("msg", "Id=" + id + "的自定义页面不存在");
            }
            else if (pce.getIsAvailable() == 0)
            {
                resultMap.put("msg", "Id=" + id + "的自定义页面不可用");
            }
            else
            {
                resultMap.put("msg", pce.getName());
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("msg", "服务器发生异常");
        }
        return JSON.toJSONString(resultMap);
    }
}
