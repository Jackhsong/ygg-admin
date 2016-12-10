
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
package com.ygg.admin.controller.categorybanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
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
import com.ygg.admin.controller.SaleWindowController;
import com.ygg.admin.entity.ActivitiesCommonEntity;
import com.ygg.admin.entity.categorybanner.CategoryBannerEntity;
import com.ygg.admin.service.ActivitiesCommonService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.service.categorybanner.CategoryBannerService;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.ImageUtil;

/**
  * 品类馆Banner
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: CategoryBannerController.java 8535 2016-03-08 12:45:02Z zhanglide $   
  * @since 2.0
  */
@Controller
@RequestMapping("/categoryBanner")
public class CategoryBannerController {
	
    @Resource(name = "activitiesCommonService")
    private ActivitiesCommonService activitiesCommonService;
    
    @Resource
    private SystemLogService logService;
    
    private Logger log = Logger.getLogger(SaleWindowController.class);
    
     /**    */
    @Resource(name="categoryBannerService")
    private CategoryBannerService categoryBannerService;
    
    @RequestMapping("/addBanner/{modelId}")
    public ModelAndView addBanner(HttpServletRequest request,
    		@PathVariable("modelId") int modelId)
        throws Exception{
        ModelAndView mv = new ModelAndView();
        CategoryBannerEntity bannerWindow = new CategoryBannerEntity();
        mv.addObject("bannerWindow", bannerWindow);
        
        // 获取可用组合特卖数据
//        Map<String, Object> para = new HashMap<String, Object>();
//        para.put("start", 0);
//        para.put("max", 1000);
//        para.put("isAvailable", 1);
//        List<ActivitiesCommonEntity> acList = activitiesCommonService.findAllAcCommonByPara(para);
//        mv.addObject("acList", acList);
        mv.addObject("modelId", modelId + "");
        mv.setViewName("categorybanner/bannerForm");
        return mv;
    }
    
    /**
     * 异步获取 获取可用组合特卖数据
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonAcCommonCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonAcCommonCode(HttpServletRequest request, @RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        // 获取可用组合特卖数据
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", 0);
        para.put("max", 1000);
        para.put("isAvailable", 1);
        List<ActivitiesCommonEntity> acList = activitiesCommonService.findAllAcCommonByPara(para);
        List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
        if (id == 0)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("selected", true);
            map.put("code", 0);
            map.put("text", "--请选择--");
            codeList.add(map);
        }
        for (ActivitiesCommonEntity it : acList)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            if (it.getId() == id)
            {
                map.put("selected", true);
            }
            map.put("code", it.getId());
            map.put("text", it.getName());
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("categorybanner/bannerManage");
        return mv;
    }
    
    /** 自定义拼装页面管理 */
    @RequestMapping("/listBanner/{modelId}")
    public ModelAndView pageManage(@PathVariable("modelId") int modelId)
        throws Exception
    {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("categorybanner/bannerManage");
        mv.addObject("modelId", modelId + "");
        return mv;
    }
    
    @RequestMapping("/save")
    @ControllerLog(description = "banner管理-新增/修改banner")
    public ModelAndView save(HttpServletRequest request, //
        @RequestParam(value = "editId", required = false, defaultValue = "0") int editId,//
        @RequestParam(value = "type", required = true) byte type,// 1单品；2组合特卖；3自定义活动;4自定义页面（暂时不用）
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "image", required = true) String image,//
        @RequestParam(value = "sequence", required = false, defaultValue = "0") int sequence,// 排序值
        @RequestParam(value = "product", required = false, defaultValue = "0") int product, // 商品ID
        @RequestParam(value = "groupSale", required = false, defaultValue = "0") int groupSale,//
        @RequestParam(value = "customSale", required = false, defaultValue = "0") int customSale,//
        @RequestParam(value = "customPage", required = false, defaultValue = "0") int customPage,//
        @RequestParam(value = "startTime", required = true) String startTime,//
        @RequestParam(value = "endTime", required = true) String endTime, //
        @RequestParam(value = "isDisplay", required = true) byte isDisplay,
    	@RequestParam(value = "page2ModelId", required = true) int page2ModelId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        CategoryBannerEntity bannerWindow = new CategoryBannerEntity();
        bannerWindow.setType(type);
        bannerWindow.setDesc(desc);
        bannerWindow.setImage((image.indexOf(ImageUtil.getPrefix()) > 0) ? image : (image + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.v1banner.ordinal())));
        bannerWindow.setIsDisplay(isDisplay);
        bannerWindow.setPage2ModelId(page2ModelId);
//        bannerWindow.setSaleTimeType(saleTimeType);
        if (type == 1)
        {
            bannerWindow.setDisplayId(product);
        }
        else if (type == 2)
        {
            bannerWindow.setDisplayId(groupSale);
        }
        else if (type == 3)
        {
            bannerWindow.setDisplayId(customSale);
        }
        else if (type == 5)
        {
            bannerWindow.setDisplayId(customPage);
        }
        else if (type == 6)
        {
            bannerWindow.setDisplayId(0);
        }
//        bannerWindow.setOrder(order);
        bannerWindow.setSequence(sequence);
        bannerWindow.setStartTime(startTime);
        bannerWindow.setEndTime(endTime);
        bannerWindow.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        
        if (editId != 0)
        {
            // update
            bannerWindow.setId(editId);
            CategoryBannerEntity bwe = categoryBannerService.findBannerWindowById(editId);
            categoryBannerService.update(bannerWindow);
            // banner关联的商品Id有修改时记录日志
            if (!((bwe.getType() == bannerWindow.getType()) && (bwe.getDisplayId() == bannerWindow.getDisplayId())))
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SELL_MANAGEMENT.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_TWO.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_BANNER_PRODUCT.ordinal());
                    logInfoMap.put("objectId", editId);
                    logInfoMap.put("old", bwe);
                    logInfoMap.put("new", bannerWindow);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
        }
        else
        {
            // insert
            categoryBannerService.save(bannerWindow);
        }
        
        mv.setViewName("redirect:/categoryBanner/listBanner/"+page2ModelId);
        return mv;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/jsonBannerWindowInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBannerWindowInfo(
        HttpServletRequest request,// 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, 
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "bannerStatus", required = false, defaultValue = "1,2") String bannerStatus,// 1,2  // Banner状态：1等待开始，2进行中，3已结束
        @RequestParam(value = "isDisplay", required = false, defaultValue = "1") String isDisplay,//0,1  // 0不展示，1展示
        @RequestParam(value = "bannerDesc", required = false, defaultValue = "") String bannerDesc,
        @RequestParam(value = "modelId", required = false, defaultValue = "") String modelId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0){
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if ("-1".equals(isDisplay) || "-1".equals(bannerStatus)){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", new ArrayList());
            map.put("total", 0);
            return JSON.toJSONString(map);
        }
        if (!isDisplay.contains(",")){
            para.put("isDisplay", isDisplay);
        }
        
        //特卖状态
        if (!(bannerStatus.contains("1") && bannerStatus.contains("2") && bannerStatus.contains("3"))){
            if (bannerStatus.contains("1") && bannerStatus.contains("2")){
                para.put("status", 12);
            }else if (bannerStatus.contains("2") && bannerStatus.contains("3")){
                para.put("status", 23);
            }else if (bannerStatus.contains("1") && bannerStatus.contains("3")){
                para.put("status", 13);
            }else{
                para.put("status", bannerStatus);
            }
        }
        if (!"".equals(bannerDesc)){
            para.put("bannerDesc", "%" + bannerDesc + "%");
        }
        para.put("now", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        if (!"".equals(modelId)){
        	para.put("page2ModelId",modelId);
        }
        List<CategoryBannerEntity> bannerWindowList = categoryBannerService.findAllBannerWindow(para);
        Map resultMap = new HashMap();
        List<Map<String, Object>> resultList = categoryBannerService.packageBannerWindowList(bannerWindowList);
        resultMap.put("rows", resultList);
        int total = categoryBannerService.countBannerWindow(para);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/updateDisplayCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "banner管理-修改banner展现状态")
    public String updateDisplayCode(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("isDisplay", code == 1 ? 0 : 1);
        int resultStatus = categoryBannerService.updateDisplayCode(para);
        Map resultMap = new HashMap();
        if (resultStatus != 1){
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }else{
            resultMap.put("status", 1);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView editTemplate(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        CategoryBannerEntity bannerWindow = categoryBannerService.findBannerWindowById(id);
        if (bannerWindow == null){
            mv.setViewName("error/404");
            return mv;
        }
        mv.addObject("bannerWindow", bannerWindow);
        mv.addObject("modelId", bannerWindow.getPage2ModelId());
        // 转换时间
        mv.addObject("startTime", DateTimeUtil.timestampStringToWebString(bannerWindow.getStartTime()));
        mv.addObject("endTime", DateTimeUtil.timestampStringToWebString(bannerWindow.getEndTime()));
        
        // 获取可用组合特卖数据
//        Map<String, Object> para = new HashMap<String, Object>();
//        para.put("start", 0);
//        para.put("max", 1000);
//        para.put("isAvailable", 1);
//        List<ActivitiesCommonEntity> acList = activitiesCommonService.findAllAcCommonByPara(para);
//        mv.addObject("acList", acList);
        mv.addObject("id", bannerWindow.getDisplayId());
        mv.setViewName("categorybanner/bannerForm");
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
    public String updateOrder(HttpServletRequest request, 
    		@RequestParam(value = "sequence", required = true) int sequence, 
    		@RequestParam(value = "id", required = true) int id // bannerId
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sequence", sequence);
        para.put("id", id);
        int resultStatus = categoryBannerService.updateOrder(para);
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
    
    @RequestMapping(value = "/checkProductTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkProductTime(HttpServletRequest request,
        @RequestParam(value = "type", required = true) int type, // 1单品；2组合特卖；3自定义特卖
        @RequestParam(value = "subjectId", required = true) int subjectId, @RequestParam(value = "startTime", required = false) String startTime,
        @RequestParam(value = "endTime", required = false) String endTime)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        para.put("type", type);
        para.put("subjectId", subjectId);
        boolean isExist = categoryBannerService.checkIsExist(para);
        if (!isExist)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "id=" + subjectId + "的" + (type == 1 ? "单品" : type == 2 ? "组合特卖" : "自定义特卖") + "不存在");
            return JSON.toJSONString(resultMap);
        }
        if (startTime != null)
        {
            para.put("startTime", startTime);
        }
        if (endTime != null)
        {
            para.put("endTime", endTime);
        }
        resultMap = categoryBannerService.checkProductTime(para);
        return JSON.toJSONString(resultMap);
        
    }
}
