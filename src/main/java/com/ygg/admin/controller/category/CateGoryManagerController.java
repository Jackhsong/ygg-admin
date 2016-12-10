
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡
APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.controller.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.ygg.admin.controller.PageController;
import com.ygg.admin.service.categorymanager.CateGoryManagerService;

/**
  * 品类馆管理
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CateGoryManagerController.java 8654 2016-03-10 10:04:07Z zhanglide $   
  * @since 2.0
  */
@Controller
@RequestMapping("/cateGoryManager")
public class CateGoryManagerController {
	
	
	 /** 日志工具   */
	private Logger log = Logger.getLogger(PageController.class);
    
     /**品类馆管理服务接口*/
    @Resource(name="cateGoryManagerService")
    private CateGoryManagerService cateGoryManagerService;
    
    /** 自定义拼装页面列表 */
    @RequestMapping("/list")
    public ModelAndView list()
        throws Exception{
        ModelAndView mv = new ModelAndView("categorymanager/list");
        return mv;
    }
    
    /** 异步 获取列表信息 */
    @RequestMapping(value = "/ajaxPageData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxPageData(@RequestParam(value = "page", required = false, defaultValue = "1") int page, // 页码
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, // 每页行数
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
        throws Exception{
        Map<String, Object> para = new HashMap<>();
        if (page == 0){
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(name)){
            para.put("name", "%" + name + "%");
        }
        Map<String, Object> result = cateGoryManagerService.findAllPageByPara(para);
        return JSON.toJSONString(result);
    }
    /** 更新 or 保存数据 */
    @RequestMapping(value = "/updatePage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑页面")
    public String updatePage(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "name", required = false, defaultValue = "") String name,
        @RequestParam(value = "categoryFirstId", required = false, defaultValue = "") String categoryFirstId,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay
    ) throws Exception{
        Map<String, Object> result = new HashMap<>();
        try{
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            if (!"".equals(name)){
                para.put("name", name);
            }
            if (!"".equals(categoryFirstId)){
                para.put("categoryFirstId", categoryFirstId);
            }
            if (isDisplay != -1){
                para.put("isDisplay", isDisplay);
            }
            int status = cateGoryManagerService.insertOrUpdatePage(para);
            result.put("status", status);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }catch (Exception e){
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    /** 自定义拼装页面管理 */
    @RequestMapping("/pageManage/{pageId}")
    public ModelAndView pageManage(@PathVariable("pageId") int pageId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("categorymanager/pageManage");
        Map<String, Object> pageInfo = cateGoryManagerService.findPageById(pageId);
        mv.addObject("pageId", pageId + "");
        mv.addObject("pageName", pageInfo.get("name") + "");
        return mv;
    }
    /** 异步 获取页面模块信息 */
    @RequestMapping(value = "/ajaxPageModel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxPageModel(@RequestParam(value = "pageId", required = true) int pageId // 页面ID
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("pageId", pageId);
        Map<String, Object> result = cateGoryManagerService.findPageModelByPara(para);
        return JSON.toJSONString(result);
    }
    
    /** 更新 or 保存数据 */
    @RequestMapping(value = "/updatePageModel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "自定义拼装页面-编辑模块")
    public String updatePageModel(@RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "isDisplay", required = false, defaultValue = "-1") int isDisplay // 模块展现状态
    )throws Exception{
        Map<String, Object> result = new HashMap<>();
        try{
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            if (isDisplay != -1){
                para.put("isDisplay", isDisplay);
            }
            int status = cateGoryManagerService.insertOrUpdatePageModel(para);
            result.put("status", status);
            result.put("msg", status >= 1 ? "保存成功" : "保存失败");
        }catch (Exception e){
            log.error("更新 or 保存自定义拼装页面失败！", e);
            result.put("status", 0);
            result.put("msg", "保存失败！");
        }
        return JSON.toJSONString(result);
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/ajaxAppCustomPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxAppCustomPage(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> codeList = new ArrayList<>();
        try
        {
            Map<String, Object> pages = cateGoryManagerService.findAllPageByPara(null);
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> tmp : (List<Map<String, Object>>)pages.get("rows"))
            {
                Map<String, String> map = new HashMap<>();
                map.put("code", tmp.get("id") + "");
                map.put("text", tmp.get("name") + "");
                int cId = Integer.valueOf(tmp.get("id") + "").intValue();
                if (id == cId)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            log.error("异步获取自定义页面信息失败", e);
        }
        return JSON.toJSONString(codeList);
    }
    
    
}
