
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
package com.ygg.admin.controller.yw.error;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.service.qqbserror.QqbsErrorServcie;
import com.ygg.admin.service.yw.error.YwErrorSerivce;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsErrorController.java 10965 2016-04-28 05:54:24Z zhanglide $   
  * @since 2.0
  */
@Controller
@RequestMapping("/ywError")
public class YwErrorController
{
    /**日志工具    */
    Logger logger = Logger.getLogger(YwErrorController.class);
    
    @Resource(name="ywErrorServcie")
    private YwErrorSerivce ywErrorServcie;
    
    /**
     * 格格福利团推荐
     * @return ModelAndView
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("ywerror/list");
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
    public Object listInfo(@RequestParam(value = "tuiAccountId", defaultValue = "-1", required = false) int tuiAccountId,
            @RequestParam(value = "accountId", defaultValue = "-1", required = false) int accountId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
        try {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("accountId", accountId);
            param.put("tuiAccountId", tuiAccountId);
            param.put("start", rows * (page - 1));
            param.put("size", rows);
            return JSON.toJSONString(ywErrorServcie.findListInfo(param));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
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
            resultMap.put("status", 1);
            int accountId = Integer.valueOf(param.get("accountId")+"");
            int tuiAccountId = Integer.valueOf(param.get("tuiAccountId")+"");
            String remark = param.get("remark")+"";
            if(tuiAccountId<accountId){
                String s = ywErrorServcie.updateAccountRela(accountId, tuiAccountId,remark);
                if(!"处理成功".equals(s)){
                    resultMap.put("status", 0);
                    resultMap.put("msg", s);
                }
            }else{
                resultMap.put("status", 0);
                resultMap.put("msg", "推荐人ID必须小于用户ID");
            }
            return resultMap;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "新增失败");
            return resultMap;
        }
    }
    
}
