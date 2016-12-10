
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
package com.ygg.admin.controller.qqbs;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.controller.brandrecommend.BrandRecommendController;
import com.ygg.admin.service.qqbswhitelist.QqbsWhiteListService;

/**
  * 商务中心白名单管理
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsWhiteListController.java 12411 2016-05-20 05:32:56Z zhanglide $   
  * @since 2.0
  */
@Controller
@RequestMapping("qqbsWhiteList")
public class QqbsWhiteListController
{
    /**日志工具    */
    Logger logger = Logger.getLogger(BrandRecommendController.class);
    
     /**审核服务*/
    @Resource(name="qqbsWhiteListService")
    private QqbsWhiteListService qqbsWhiteListService;
    /**
     * 格格福利团推荐
     * @return ModelAndView
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("qqbswhitelist/list");
        return mv;
    }
    
    /**
     * 列表查询
     * @param status
     * @param isDisplay
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/listInfo")
    @ResponseBody
    public Object listInfo(String name,
            @RequestParam(value = "accountId", defaultValue = "-1", required = false) int accountId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
        try {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountId", accountId);
            if(StringUtils.isNotBlank(name)){
                param.put("name", "%" + name + "%");
            }
            param.put("start", rows * (page - 1));
            param.put("size", rows);
            return JSON.toJSONString(qqbsWhiteListService.findListInfo(param));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/accept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "通过")
    public String accept(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
             @RequestParam(value = "type", required = true) int type)
    {
        Map resultMap = new HashMap();
        String msg = "";
        if(type == 1){
            //审核通过
            msg = "审核通过";
        }else if(type == 2){
            //审核不通过
            msg = "审核不通过";
        }else if(type == 3){
            //移除
            msg = "移除";
        }else if(type == 4){
            //加入白名单
            msg = "加入白名单";
            type = 1;
        }
        try{
            qqbsWhiteListService.updateLog(id,type);
            resultMap.put("status", 1);
            resultMap.put("msg", msg+"成功");
        }catch(Exception e){
            logger.error(msg+"失败",e);
            resultMap.put("status", 0);
            resultMap.put("msg", msg+"失败");
        }
        return JSON.toJSONString(resultMap);
    }
     /**
      * 新增
      * @param param
      * @return
      */
     @RequestMapping("/save")
     @ResponseBody
     public Object save(@RequestParam Map<String, Object> param) {
         try {
             Map<String, Object> resultMap = new HashMap<String, Object>();
             
             int count = qqbsWhiteListService.getCountByParam(param);
             if(count >0){
                 resultMap.put("status", 0);
                 resultMap.put("msg", "添加白名单失败：改用户存在白名单中，请查实审核状态");
             }else{
                 param.put("status", 1);
                 qqbsWhiteListService.save(param);
                 resultMap.put("status", 1);
                 resultMap.put("msg", "添加白名单成功");
             }
             return resultMap;
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
             Map<String, Object> resultMap = new HashMap<String, Object>();
             resultMap.put("status", 0);
             resultMap.put("msg", "添加白名单失败");
             return resultMap;
         }
     }
     
}
