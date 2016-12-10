package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ImageTypeEnum;
import com.ygg.admin.service.SaleFlagService;
import com.ygg.admin.util.ImageUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特卖国旗管理
 * @author xiongl
 *
 */
@Controller
@RequestMapping("flag")
public class SaleFlagController
{
    private Logger logger = Logger.getLogger(SaleFlagController.class);
    
    @Resource
    private SaleFlagService saleFlagService;
    
    /**
     * 国旗管理列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sale/flagList");
        return mv;
    }
    
    /**
     * 国旗管理列表
     * @param page
     * @param rows
     * @param id：id
     * @param name：国家名称
     * @param isAvailable：是否可用；1可用，0不可用
     * @return
     */
    @RequestMapping(value = "/jsonSaleFlagInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSaleFlagInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "-1") int id,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (id != -1)
            {
                para.put("id", id);
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            resultMap = saleFlagService.jsonSaleFlagInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更改国旗可用状态
     * @param id
     * @param isAvailable：1启用，0停用
     * @return
     */
    @RequestMapping(value = "/updateFlagStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖国旗管理-设置国旗可用状态")
    public String updateFlagStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("isAvailable", isAvailable);
            int status = saleFlagService.updateFlag(para);
            if (status == 1)
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
    
    /**
     * 编辑或新增国旗
     * @param id
     * @param name
     * @param image
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateFlag", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖国旗管理-新增/编辑国旗")
    public String saveOrUpdateFlag(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "flagEnName", required = true) String flagEnName,
        @RequestParam(value = "image", required = true) String image,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("name", name);
            para.put("flagEnName", flagEnName);
            image = image.indexOf(ImageUtil.getPrefix()) > -1 ? image : (image + ImageUtil.getPrefix() + ImageUtil.getSuffix(ImageTypeEnum.nationalflag.ordinal()));
            para.put("image", image);
            para.put("isAvailable", isAvailable);
            
            int result = saleFlagService.saveOrUpdateFlag(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 检查国旗是否在使用中
     * @param id
     * @return
     */
    @RequestMapping(value = "/checkFlagIsInUse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkFlagIsInUse(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            boolean isInUse = saleFlagService.checkFlagIsInUse(id);
            if (isInUse)
            {
                result.put("status", 0);
                result.put("msg", "该国旗正在使用中，不能编辑");
            }
            else
            {
                result.put("status", 1);
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "该国旗正在使用中，不能编辑");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/jsonSaleFlagCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSaleFlagCode(@RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "needAll", required = false, defaultValue = "0") int needAll)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> list = saleFlagService.jsonSaleFlagCode(para);
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> entity : list)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", entity.get("id") + "");
                map.put("text", entity.get("name") + "");
                if (id == Integer.valueOf(entity.get("id") + "").intValue())
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
            if(needAll != 0){
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", "-1");
                map.put("text", "不限");
                codeList.add(0, map);
            }

            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
}
