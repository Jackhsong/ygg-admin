package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.SpreadChannelEntity;
import com.ygg.admin.service.SpreadChannelService;

/**
 * 渠道统计相关
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/spreaChannel")
public class SpreadChannelController
{
    private static Logger logger = Logger.getLogger(SpreadChannelController.class);
    
    @Resource
    private SpreadChannelService spreadChannelService;
    
    @RequestMapping("/list")
    public ModelAndView gameList()
    {
        ModelAndView mv = new ModelAndView("spreadChannel/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonSpreadChannelInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSpreadChannelInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "channelName", required = false, defaultValue = "") String channelName,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
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
            if (id != 0)
            {
                para.put("id", id);
            }
            if (!"".equals(channelName))
            {
                para.put("channelName", "%" + channelName + "%");
            }
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            resultMap = spreadChannelService.findAllSpreadChannels(para);
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
     * 
     * @param id
     * @param channelName：渠道名称
     * @param shareTitle：分享标题
     * @param shareImage：分享内容
     * @param shareContent：分享图标
     * @param dateType
     * @param days
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计管理-新增/修改渠道")
    public String saveChannel(@RequestParam(value = "channelId", required = false, defaultValue = "0") int id,
        @RequestParam(value = "channelName", required = false, defaultValue = "") String channelName,
        @RequestParam(value = "shareTitle", required = false, defaultValue = "") String shareTitle,
        @RequestParam(value = "shareImage", required = false, defaultValue = "") String shareImage,
        @RequestParam(value = "shareContent", required = false, defaultValue = "") String shareContent,
        @RequestParam(value = "dateType", required = false, defaultValue = "1") int dateType, @RequestParam(value = "days", required = false, defaultValue = "0") int days,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable,
        @RequestParam(value = "couponIdAndCount", required = false, defaultValue = "") String couponIdAndCount)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            SpreadChannelEntity channel = new SpreadChannelEntity();
            channel.setId(id);
            channel.setChannelName(channelName);
            channel.setShareTitle(shareTitle);
            channel.setShareImage(shareImage);
            channel.setShareContent(shareContent);
            para.put("channel", channel);
            para.put("dateType", dateType);
            para.put("days", days);
            para.put("couponIdAndCount", couponIdAndCount);
            int result = spreadChannelService.saveOrUpdate(para);
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
     * 
     * @param id
     * @param channelName：渠道名称
     * @param shareTitle：分享标题
     * @param shareImage：分享内容
     * @param shareContent：分享图标
     * @param dateType
     * @param days
     * @param isAvailable
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计管理-更新渠道")
    public String updateChannel(@RequestParam(value = "channelId", required = false, defaultValue = "0") int id,
        @RequestParam(value = "channelName", required = false, defaultValue = "") String channelName,
        @RequestParam(value = "shareTitle", required = false, defaultValue = "") String shareTitle,
        @RequestParam(value = "shareImage", required = false, defaultValue = "") String shareImage,
        @RequestParam(value = "shareContent", required = false, defaultValue = "") String shareContent)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            SpreadChannelEntity channel = new SpreadChannelEntity();
            channel.setId(id);
            channel.setChannelName(channelName);
            channel.setShareTitle(shareTitle);
            channel.setShareImage(shareImage);
            channel.setShareContent(shareContent);
            para.put("channel", channel);
            int result = spreadChannelService.saveOrUpdate(para);
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
    
    @RequestMapping(value = "/deleteSpreadChannel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计管理-删除渠道")
    public String deleteSpreadChannel(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int result = spreadChannelService.deleteSpreadChannel(id);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "删除失败");
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
    
    @RequestMapping(value = "/updateChannelAvailable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计管理-设置渠道可用状态")
    public String updateChannelAvailable(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isAvailable", isAvailable);
            int result = spreadChannelService.updateChannelAvailable(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "更新失败");
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
    
    @RequestMapping(value = "/updateChannelSendMsg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计管理-修改渠道发送短信状态")
    public String updateChannelSendMsg(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "status", required = true) int status)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isSendMsg", status);
            int result = spreadChannelService.updateChannelSendMsg(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "操作失败");
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
    
    @RequestMapping(value = "/editMsgContent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计管理-修改渠道发送短信内容")
    public String editMsgContent(@RequestParam(value = "channelId", required = true) int channelId, @RequestParam(value = "msgContent", required = true) String msgContent)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", channelId);
            para.put("msgContent", msgContent);
            int result = spreadChannelService.editMsgContent(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "操作失败");
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
}
