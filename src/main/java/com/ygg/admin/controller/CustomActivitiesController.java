package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.CustomEnum;
import com.ygg.admin.entity.PageCustomEntity;
import com.ygg.admin.service.CustomActivitiesService;
import com.ygg.admin.service.PageCustomService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 自定义特卖
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/customActivities")
public class CustomActivitiesController
{
    private Logger logger = Logger.getLogger(CustomActivitiesController.class);
    
    @Resource
    private CustomActivitiesService customActivitiesService;
    
    @Resource
    private PageCustomService pageCustomService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("customActivities/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonCustomActivitiesInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCustomActivitiesInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark
    )
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
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            if (StringUtils.isNotEmpty(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            resultMap = customActivitiesService.jsonCustomActivitiesInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateCustomActivitiesStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-更新特卖活动状态")
    public String updateCustomActivitiesStatus(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> idList = new ArrayList<Integer>();
            String[] idArr = id.split(",");
            for (String idStr : idArr)
            {
                idList.add(Integer.valueOf(idStr));
            }
            
            para.put("idList", idList);
            para.put("isAvailable", isAvailable);
            int result = customActivitiesService.updateCustomActivitiesStatus(para);
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
    
    @RequestMapping("/add")
    public ModelAndView add()
    {
        ModelAndView mv = new ModelAndView("customActivities/update");
        Map<String, Object> customActivities = new HashMap<String, Object>();
        customActivities.put("typeCode", 1);
        customActivities.put("isAvailableCode", 0);
        customActivities.put("shareType", 1);//APP分享支持类型，默认全部
        customActivities.put("headType", 1);//APP顶部头区域显示类型,默认底色
        customActivities.put("isHideShareButton", 0);//是否隐藏分享按钮，默认是
        mv.addObject("customActivities", customActivities);
        return mv;
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable(value = "id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            Map<String, Object> customActivities = customActivitiesService.findCustomActivitiesById(id);
            if (customActivities == null)
            {
                mv.setViewName("error/404");
            }
            else
            {
                String shareContentSina = customActivities.get("shareContentSina") + "";
                //新浪分享内容：xxx+"两个空格"+http://xxx.xxx.xx,从数据库取出内容展示在页面之前，去掉空格后面拼接的URL
                customActivities.put("shareContentSina", shareContentSina.substring(0, shareContentSina.indexOf("http") - 2));
                customActivities.put("version", customActivities.get("version") + "");
                mv.setViewName("customActivities/update");
                mv.addObject("customActivities", customActivities);
                mv.addObject("typeId", customActivities.get("typeId") + "");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "特卖活动管理-新增/编辑自定义活动")
    public String saveOrUpdate(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "version", required = false, defaultValue = "1.0") float version,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "type", required = false, defaultValue = "1") int type,
        @RequestParam(value = "lotteryActivity", required = false, defaultValue = "0") int lotteryActivity,
        @RequestParam(value = "saleActivity", required = false, defaultValue = "0") int saleActivity,
        @RequestParam(value = "newSaleActivity", required = false, defaultValue = "0") int newSaleActivity,
        @RequestParam(value = "giftActivity", required = false, defaultValue = "0") int giftPackageActivity,
        @RequestParam(value = "otherActivity", required = false, defaultValue = "0") int otherActivity,
        @RequestParam(value = "specialGroupActivity", required = false, defaultValue = "0") int specialGroupActivity,
        @RequestParam(value = "specialActivityModel", required = false, defaultValue = "0") int specialActivityModel,
        @RequestParam(value = "simplifyActivity", required = false, defaultValue = "0") int simplifyActivity,
        @RequestParam(value = "shareTitle", required = false, defaultValue = "") String shareTitle,
        @RequestParam(value = "shareContentTencent", required = false, defaultValue = "") String shareContentTencent,
        @RequestParam(value = "shareContentSina", required = false, defaultValue = "") String shareContentSina,
        @RequestParam(value = "sharePengYouQuanTitle", required = false, defaultValue = "") String sharePengYouQuanTitle,
        @RequestParam(value = "shareImage", required = false, defaultValue = "") String shareImage,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "0") int isAvailable,
        @RequestParam(value = "headType", required = false, defaultValue = "1") int headType,//
        @RequestParam(value = "shareType", required = false, defaultValue = "1") int shareType,
        @RequestParam(value = "isHideShareButton", required = false, defaultValue = "0") int isHideShareButton)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("version", version);
            para.put("remark", remark);
            para.put("type", type);
            para.put("isHideShareButton", isHideShareButton);
            //关联抽奖活动
            if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.LOTTERY_ACTIVITY.getCode())
            {
                para.put("typeId", lotteryActivity);
                para.put("url", "http://m.gegejia.com/ygg/activity/lottery/appOpen?lotteryId=" + lotteryActivity);
                para.put("shareUrl", "http://m.gegejia.com/ygg/activity/lottery/web/" + lotteryActivity);
            }
            //关联特卖活动
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.SALE_ACTIVITY.getCode())
            {
                para.put("typeId", saleActivity);
                para.put("url", "http://m.gegejia.com/ygg/special/activity/appOpen?activityId=" + saleActivity);
                para.put("shareUrl", "http://m.gegejia.com/ygg/special/activity/web/" + saleActivity);
            }
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.NEW_SIMPLIFY_ACTIVITY.getCode())
            {
                para.put("typeId", newSaleActivity);
                para.put("url", "http://m.gegejia.com/ygg/special/sceneApp/" + newSaleActivity);
                para.put("shareUrl", "http://m.gegejia.com/ygg/special/sceneWeb/" + newSaleActivity);
            }
            //关联礼包活动
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.GIFT_PACKAGE_ACTIVITY.getCode())
            {
                para.put("typeId", giftPackageActivity);
                para.put("url", "http://m.gegejia.com/ygg/activity/gift/appOpen");
                para.put("shareUrl", "http://m.gegejia.com/ygg/activity/gift/web/" + giftPackageActivity);
            }
            //自定义页面
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.OTHER_ACTIVITY.getCode())
            {
                PageCustomEntity pageCustom = pageCustomService.findPageCustomById(otherActivity);
                para.put("typeId", otherActivity);
                para.put("url", pageCustom.getPcUrl());
                para.put("shareUrl", pageCustom.getMobileUrl());
            }
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.ANY_DOOR.getCode())
            {
                para.put("typeId", 0);
                para.put("url", "http://m.gegejia.com/ygg/gate/activity/appOpen");
                para.put("shareUrl", "http://m.gegejia.com/ygg/gate/activity/web");
            }
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.SIMPLIFY_ACTIVITY.getCode())
            {
                
                para.put("typeId", simplifyActivity);
                para.put("url", "http://m.gegejia.com/ygg/activity/simplify/appOpen?activityId=" + simplifyActivity);
                para.put("shareUrl", "http://m.gegejia.com/ygg/activity/simplify/web/" + simplifyActivity);
            }
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.SPECIAL_GROUP_ACTIVITY.getCode())
            {
                para.put("typeId", specialGroupActivity);
                para.put("url", "http://m.gegejia.com/ygg/special/groupSceneApp/" + specialGroupActivity);
                para.put("shareUrl", "http://m.gegejia.com/ygg/special/groupSceneWeb/" + specialGroupActivity);
            }
            else if (type == CustomEnum.CUSTOM_ACTIVITY_RELATION.SPECIAL_ACTIVITY_MODEL.getCode())
            {
                para.put("typeId", specialActivityModel);
                para.put("url", "http://m.gegejia.com/ygg/special/newSceneApp/" + specialActivityModel);
                para.put("shareUrl", "http://m.gegejia.com/ygg/special/newSceneWeb/" + specialActivityModel);
            }
            para.put("shareTitle", shareTitle);
            para.put("shareContentTencent", shareContentTencent);
            para.put("shareContentSina", shareContentSina + "  " + para.get("shareUrl"));
            para.put("sharePengYouQuanTitle", sharePengYouQuanTitle);
            para.put("shareImage", shareImage);
            para.put("isAvailable", isAvailable);
            para.put("headType", headType);
            para.put("shareType", shareType);
            int result = customActivitiesService.saveOrUpdate(para);
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
    
    @RequestMapping(value = "/jsonCustomActivitiesCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCustomActivitiesCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable, @RequestParam(value = "type", required = false, defaultValue = "") String type)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("isAvailable", isAvailable);
            if (StringUtils.isNotEmpty(type))
            {
                para.put("typeList", Arrays.asList(type.split(",")));
            }
            List<Map<String, Object>> resultList = customActivitiesService.findAllCustomActivities(para);
            Map<String, String> mapAll = new HashMap<String, String>();
            if (isAvailable == -1)
            {
                mapAll.put("code", "0");
                mapAll.put("text", "全部");
                codeList.add(mapAll);
            }
            for (Map<String, Object> tmp : resultList)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", tmp.get("id") + "");
                map.put("text", tmp.get("remark") + "");
                int lotteryId = Integer.valueOf(tmp.get("id") + "").intValue();
                if (id == lotteryId)
                {
                    map.put("selected", "true");
                }
                codeList.add(map);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(codeList);
    }
    
    @RequestMapping(value = "/findCustomActivitiesInfoById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findCustomActivitiesInfoById(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> customActivities = customActivitiesService.findCustomActivitiesById(id);
        if (customActivities == null)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "id=" + id + "的活动不存在");
        }
        else
        {
            int type = Integer.valueOf(customActivities.get("typeCode") + "").intValue();
            resultMap.put("status", 1);
            resultMap.put("name", CustomEnum.CUSTOM_ACTIVITY_RELATION.getDescrByCode(type));
            resultMap.put("remark", customActivities.get("remark"));
        }
        return JSON.toJSONString(resultMap);
    }
}
