
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
package com.ygg.admin.controller.qqbscash;

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
import com.ygg.admin.service.qqbscash.QqbsCashService;

/**
  * 左岸城堡
提现控制器
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: QqbsCashController.java 11679 2016-05-12 09:45:38Z qiuyibo $   
  * @since 2.0
  */
@Controller
@RequestMapping("/qqbsCash")
public class QqbsCashController {
	
	/**日志工具    */
	Logger logger = Logger.getLogger(QqbsCashController.class);
	
	@Resource(name="qqbsCashService")
	private QqbsCashService qqbsCashService;
	/**
	 * 格格福利团推荐
	 * @return ModelAndView
	 */
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("qqbscash/list");
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
			return JSON.toJSONString(qqbsCashService.findListInfo(param));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("status", 0);
			resultMap.put("msg", e.getMessage());
			return resultMap;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/refuse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "拒绝")
    public String refuse(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
    		 @RequestParam(value = "type", required = true) int type)
    {
        Map resultMap = new HashMap();
        try{
            String fla = qqbsCashService.updateLog(id,type);
            if(StringUtils.isNotBlank(fla)){
                resultMap.put("status", 0);
                resultMap.put("msg", "ID="+id+" 拒绝失败 原因:"+fla);
            }else{
                resultMap.put("status", 1);
                resultMap.put("msg", "拒绝成功");
            }
        }catch(Exception e){
        	logger.error("拒绝失败",e);
        	resultMap.put("status", 0);
            resultMap.put("msg", "拒绝失败");
        }
        return JSON.toJSONString(resultMap);
    }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/accept", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "打款")
    public String accept(HttpServletRequest request, @RequestParam(value = "id", required = true) int id,
    		 @RequestParam(value = "type", required = true) int type)
    {
        Map resultMap = new HashMap();
        try{
        	String fla = qqbsCashService.updateLog(id,type);
        	if(StringUtils.isNotBlank(fla)){
        		resultMap.put("status", 0);
                resultMap.put("msg", "ID="+id+" 打款失败 原因:"+fla);
        	}else{
        		resultMap.put("status", 1);
                resultMap.put("msg", "打款成功");
        	}
        }catch(Exception e){
        	logger.error("打款失败",e);
        	resultMap.put("status", 0);
            resultMap.put("msg", "打款失败");
        }
        return JSON.toJSONString(resultMap);
    }
	 @RequestMapping(value = "/deleteBat", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	    @ResponseBody
	    @ControllerLog(description = "批量打款")
    public String deleteProductGroupStatus(@RequestParam(value = "ids", required = false, defaultValue = "0") String ids)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        String[] arr = ids.split(",");
        for (String it : arr)
        {
            try{
                String fla = qqbsCashService.updateLog(Integer.valueOf(it),1);
                if(StringUtils.isNotBlank(fla)){
                    sb.append("ID="+it+" 打款失败原因："+fla+"\n");
                }
            }catch (Exception e){
                logger.error("ID="+it+" 打款失败",e);
                sb.append("ID="+it+" 打款失败"+"\n");
            }
        }
        resultMap.put("status", 1);
        if(StringUtils.isBlank(sb.toString())){
            sb.append("批量打款成功");
        }
        resultMap.put("msg", sb.toString());
        return JSON.toJSONString(resultMap);
    }
}
