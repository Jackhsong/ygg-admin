package com.ygg.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.SaleTagEntity;
import com.ygg.admin.service.SaleTagService;

@Controller
@RequestMapping("/saleTag")
public class SaleTagController
{
    
    @Resource(name = "saleTagService")
    private SaleTagService saleTagService = null;
    
    /**
     * 跳转到特卖标签新增页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/add")
    public ModelAndView toAdd(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("saleTag/update");
        SaleTagEntity saleTag = (SaleTagEntity)request.getSession().getAttribute("wrongSaleTagInfo");
        if (saleTag == null)
        {
            saleTag = new SaleTagEntity();
        }
        else
        {
            request.getSession().setAttribute("wrongSaleTagInfo", null);
        }
        mv.addObject("saleTag", saleTag);
        return mv;
    }
    
    /**
     * 保存特卖标签
     * 
     * @param request
     * @param editId
     * @param name
     * @param isAvailable
     * @param image
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ControllerLog(description = "特卖标签管理-新增/编辑标签")
    public ModelAndView save(HttpServletRequest request, @RequestParam(value = "editId", required = false, defaultValue = "0") int editId, SaleTagEntity saleTag)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        saleTag.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        saleTag.setId(editId);
        int resultStatus = saleTagService.saveOrUpdate(saleTag);
        if (resultStatus != 1)
        {
            request.getSession().setAttribute("wrongSaleTagInfo", saleTag);
            mv.setViewName("redirect:/saleTag/add");
            return mv;
        }
        mv.setViewName("redirect:/saleTag/list");
        return mv;
    }
    
    /**
     * 特卖标签管理页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("saleTag/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        
        String jsonSaleTagString = saleTagService.jsonSaleTagInfo(para);
        return jsonSaleTagString;
    }
    
    /**
     * 跳转至特卖编辑页面
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit/{id}")
    public ModelAndView editTemplate(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("saleTag/update");
        SaleTagEntity saleTag = saleTagService.findSaleTagById(id);
        if (saleTag == null)
        {
            mv.setViewName("forward:/error/404");
            return mv;
        }
        mv.addObject("saleTag", saleTag);
        return mv;
    }
    
}
