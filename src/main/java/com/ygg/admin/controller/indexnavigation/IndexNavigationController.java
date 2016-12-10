
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
package com.ygg.admin.controller.indexnavigation;

 import com.alibaba.fastjson.JSON;
 import com.ygg.admin.annotation.ControllerLog;
 import com.ygg.admin.config.AreaCache;
 import com.ygg.admin.controller.CustomNavigationController;
 import com.ygg.admin.service.indexnavigation.IndexNavigationService;
 import org.apache.log4j.Logger;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.ResponseBody;
 import org.springframework.web.servlet.ModelAndView;

 import javax.annotation.Resource;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

/**
  * 新版首页自定义导航
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: IndexNavigationController.java 9333 2016-03-28 08:07:03Z zhangyibo $   
  * @since 2.0
  */
@Controller
@RequestMapping("/indexNavigation")
public class IndexNavigationController {
	
	Logger logger = Logger.getLogger(CustomNavigationController.class);

     /**新版首页自定义导航服务接口*/
    @Resource(name="indexNavigationService")
    private IndexNavigationService indexNavigationService;
    /**
     * 首页导航入口
     * 
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("indexnavigation/list");
        return mv;
    }
    
    /**
     * 查询自定义首页导航列表
     * 
     * @param id
     *            导航ID
     * @param name
     *            导航名称
     * @param isDisplay
     *            是否展示
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public Object info(@RequestParam(value = "id", defaultValue = "0", required = false) int id, @RequestParam(value = "name", defaultValue = "", required = false) String name,
        @RequestParam(value = "isDisplay", defaultValue = "1", required = false) int isDisplay, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return indexNavigationService.findNavigationList(id, name, isDisplay, page, rows);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询单条
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    @ResponseBody
    public Object findById(int id)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", indexNavigationService.findById(id));
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 根据条件更新导航
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping("/updateByParam")
    @ResponseBody
    public Object updateByParam(@RequestParam(value = "id", defaultValue = "0", required = true) int id,
        @RequestParam(value = "sequence", defaultValue = "-1", required = false) int sequence,
        @RequestParam(value = "isDisplay", defaultValue = "-1", required = false) int isDisplay)
    {
        try
        {
            indexNavigationService.updateByParam(id, sequence, isDisplay);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 保存或修改首页导航信息
     * 
     * @param customNavigationId
     * @param name
     * @param remark
     * @param customNavigationType
     * @param activitiesCommonId
     * @param activitiesCustomId
     * @param pageId
     * @param isDisplay
     * @return
     */
    @RequestMapping("/saveOrUpdate")
    @ResponseBody
    @ControllerLog(description = "保存或修改首页导航信息")
    public Object saveOrUpdate(int customNavigationId, String name, String remark, int customNavigationType,
        @RequestParam(defaultValue = "0", required = false) int activitiesCommonId, 
        @RequestParam(defaultValue = "0", required = false) int activitiesCustomId,
        @RequestParam(defaultValue = "0", required = false) int pageId2,
        @RequestParam(defaultValue = "0", required = false) int pageId, int isDisplay)
    {
        try
        {
            int displayId = 0;
            if (customNavigationType == 1)
            {
                displayId = activitiesCommonId;
            }
            else if (customNavigationType == 2)
            {
                displayId = activitiesCustomId;
            }
            else if (customNavigationType == 3)
            {
                displayId = pageId;
            }else if(customNavigationType == 4){
            	displayId = pageId2;
            }
            if (customNavigationId < 1)
            {
                indexNavigationService.save(name, remark, customNavigationType, displayId, isDisplay);
            }
            else
            {
                indexNavigationService.update(customNavigationId, name, remark, customNavigationType, displayId, isDisplay);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 修改限制地区
     *
     * @param id 首页导航ID
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/editAreaForm", produces = "application/json;charset=UTF-8")
    public ModelAndView editAreaForm(@RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("indexnavigation/editAreaForm");
        Map<String, Object> result = indexNavigationService.findNavigationSupportAreaInfo(id);
        mv.addObject("id", id + "");
        mv.addObject("supportAreaType", Integer.valueOf(result.get("supportAreaType") + ""));
        mv.addObject("pList", (List<Map<String, Object>>)result.get("pList"));
        mv.addObject("hotCityList", (List<Map<String, Object>>)result.get("hotCityList"));
        return mv;
    }
    
    @RequestMapping(value = "/editArea", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String editArea(@RequestParam(value = "provinceStr", required = true) String provinceStr, // 支持省份列表
        @RequestParam(value = "supportAreaType", required = true) int supportAreaType, //
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "provinceCityStr", required = false, defaultValue = "") String provinceCityStr // 省份下对应城市列表： p1:c1,c2,c3,;p2:c1,c2,c3,;
    )
        throws Exception
    {
        try
        {
            List<Integer> provinceIdList = new ArrayList<>();
            if (provinceStr.indexOf(",") > 0)
            {
                String[] arr = provinceStr.split(",");
                for (String cur : arr)
                {
                    provinceIdList.add(Integer.valueOf(cur));
                }
            }
            List<Map<String, Object>> provinceHotCityList = new ArrayList<>();
            if (!"".equals(provinceCityStr))
            {
                String[] arr = provinceCityStr.split(";");
                for (String s : arr)
                {
                    Map<String, Object> map = new HashMap<>();
                    String[] arr2 = s.split(":");
                    map.put("provinceId", arr2[0]);
                    map.put("provinceCityList", Arrays.asList(arr2[1].split(",")));
                    provinceHotCityList.add(map);
                }
            }
            int status = indexNavigationService.updateNavAreaInfo(id, provinceIdList, supportAreaType, provinceHotCityList);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status > 0 ? "1" : 0);
            result.put("msg", status > 0 ? "保存成功" : "保存失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            logger.error("修改导航展示地区失败", e);
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }

    /**
     * 热卖城市
     * 
     * @return
     */
    @RequestMapping("/hotCityList")
    public ModelAndView hotCityList()
    {
        Map<String,Object> model = new HashMap<>();
        // 所有省份信息
        Map<String, String> provinceMap = AreaCache.getInstance().getProvinceMap();
        List<Map<String, String>> provinceList = new ArrayList<>();
        for (Map.Entry<String, String> e : provinceMap.entrySet())
        {
            Map<String,String> map = new HashMap<>();
            map.put("provinceId", e.getKey());
            map.put("name", e.getValue());
            provinceList.add(map);
        }

        model.put("provinceList", provinceList);
        return new ModelAndView("indexnavigation/hotCityList", model);
    }
    
    @RequestMapping(value = "/ajaxHotCityList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object ajaxHotCityList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
        throws Exception
    {
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            int start = rows * (page - 1);
            return indexNavigationService.findHotCityInfo(start, rows);
        }
        catch (Exception e)
        {
            logger.error("异步加载热门城市失败", e);
            return new ArrayList<>();
        }
    }

    /**
     * 保存热门城市信息
     * @param provinceId
     * @param cityId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveHotCityList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object saveHotCityList(@RequestParam(value = "provinceId", required = true) String provinceId, //
                                  @RequestParam(value = "cityId", required = true) String cityId)
        throws Exception
    {
        try
        {
            if (indexNavigationService.existsHotCityInfo(provinceId, cityId))
            {
                return new HashMap<String, Object>()
                {
                    {
                        put("status", 0);
                        put("msg", "已经存在该热门城市");
                    }
                };
            }
            Map<String,Object> result = new HashMap<>();
            int status = indexNavigationService.saveHotCityInfo(provinceId, cityId);
            result.put("status", status > 0 ? 1 : 0);
            result.put("msg", status > 0 ? "保存成功" : "保存失败");
            return result;
        }
        catch (Exception e)
        {
            logger.error("保存热门城市失败", e);
            return new HashMap<String, Object>()
            {
                {
                    put("status", 0);
                    put("msg", "服务器忙");
                }
            };
        }
    }

    /**
     * 删除热门城市信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteHotCityList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object deleteHotCityList(@RequestParam(value = "id", required = true) int id)
            throws Exception
    {
        try
        {
            Map<String,Object> result = new HashMap<>();
            int status = indexNavigationService.updateHotCityInfo(id, 0, 0);
            result.put("status", status > 0 ? 1 : 0);
            result.put("msg", status > 0 ? "删除成功" : "删除失败");
            return result;
        }
        catch (Exception e)
        {
            logger.error("删除热门城市失败", e);
            return new HashMap<String, Object>()
            {
                {
                    put("status", 0);
                    put("msg", "服务器忙");
                }
            };
        }
    }

    /**
     * 根据条件更新热门城市
     *
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping("/updateHotCityByParam")
    @ResponseBody
    public Object updateHotCityByParam(@RequestParam(value = "id", defaultValue = "0", required = true) int id,
        @RequestParam(value = "sequence", defaultValue = "-1", required = false) int sequence)
    {
        try
        {
            Map<String,Object> result = new HashMap<>();
            int status = indexNavigationService.updateHotCityInfo(id,1, sequence);
            result.put("status", status > 0 ? 1 : 0);
            result.put("msg", status > 0 ? "修改成功" : "修改失败");
            return result;
        }
        catch (Exception e)
        {
            logger.error("修改热门城市失败", e);
            return new HashMap<String, Object>()
            {
                {
                    put("status", 0);
                    put("msg", "服务器忙");
                }
            };
        }
    }
	
}
