package com.ygg.admin.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.FreightTemplateEntity;
import com.ygg.admin.service.FreightService;

@Controller
@RequestMapping("/postage")
public class PostageController
{
    
    @Resource(name = "freightService")
    private FreightService freightService;
    
    /**
     * 进入模板管理页面
     * 
     * @return
     */
    @RequestMapping("/manage")
    public ModelAndView templateManage(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("freight/freightTemplateManage");
        return mv;
    }
    
    /**
     * 得到所有邮费模板信息
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        
        String jsonInfoString = freightService.jsonfreightTemplateInfo(para);
        return jsonInfoString;
    }
    
    /**
     * 得到特定邮费模板对应的所有省份邮费信息
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProvinceFreightInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProvinceFreightInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        String jsonProvinceFreightInfoString = freightService.jsonProvinceFreightInfo(id);
        return jsonProvinceFreightInfoString;
    }
    
    /**
     * 异步 保存模板信息
     * 
     * @param request
     * @param name
     * @param desc
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveTemplate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "运费模版管理-新增/编辑运费模版")
    public String saveTemplate(HttpServletRequest request, FreightTemplateEntity freightTemplate)
        throws Exception
    {
        int result = freightService.saveOrUpdate(freightTemplate);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (result == 1)
        {
            resultMap.put("status", 1);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 同步 保存模板信息
     * 
     * @param request
     * @param name
     * @param desc
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping("/addTemplate")
    public ModelAndView addTemplate(HttpServletRequest request, FreightTemplateEntity freightTemplate)
        throws Exception
    {
        freightService.saveOrUpdate(freightTemplate);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/postage/manage");
        return mv;
    }
    
    /**
     * 跳转到id对应的template更新页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/edit/{id}")
    public ModelAndView editTemplate(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("freight/editTemplate");
        FreightTemplateEntity fEntity = freightService.findFreightTemplateById(id);
        mv.addObject("freightTemplate", fEntity);
        return mv;
    }
    
    /**
     * id对应模板下的各省份邮费信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/province/{id}")
    public ModelAndView province(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("freight/freightWithProvince");
        mv.addObject("id", id);
        return mv;
    }
    
    /**
     * 修改省份邮费信息
     * 
     * @param request
     * @param money
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveProvinceFreight")
    @ResponseBody
    @ControllerLog(description = "运费模版管理-修改省份邮费信息")
    public String saveProvinceFreight(HttpServletRequest request, @RequestParam(value = "money", required = true) int money, @RequestParam(value = "id", required = true) String id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        String[] idStrArr = id.split(",");
        para.put("idList", Arrays.asList(idStrArr));
        para.put("money", money);
        int result = freightService.updateProvinceFreight(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (result == 1)
        {
            resultMap.put("status", 1);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
}
