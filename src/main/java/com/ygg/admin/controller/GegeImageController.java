package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.ygg.admin.entity.GegeImageEntity;
import com.ygg.admin.service.GegeImageService;

@Controller
@RequestMapping("/image")
public class GegeImageController
{
    
    private static Logger logger = Logger.getLogger(GegeImageController.class);
    
    @Resource
    private GegeImageService geGeImageService;
    
    @RequestMapping("/list")
    public ModelAndView gegeMange(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView();
        String type = request.getParameter("type");
        mv.setViewName(type + "/list_image");
        return mv;
    }
    
    @RequestMapping(value = "/jsonImageInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonImageInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "name", required = false, defaultValue = "") String name,
        @RequestParam(value = "type", required = true) String type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("type", type);
        
        Map<String, Object> imageMap = geGeImageService.jsonImageInfo(para);
        
        return JSON.toJSONString(imageMap);
    }
    
    @RequestMapping("/add")
    public ModelAndView toAdd(HttpServletRequest request)
        throws Exception
    {
        String type = request.getParameter("type");
        ModelAndView mv = new ModelAndView();
        mv.setViewName(type + "/update_image");
        GegeImageEntity geGeImage = (GegeImageEntity)request.getSession().getAttribute("wrongGegeImageInfo");
        if (geGeImage == null)
        {
            geGeImage = new GegeImageEntity();
        }
        else
        {
            request.getSession().setAttribute("wrongGegeImageInfo", null);
        }
        mv.addObject("gegeImage", geGeImage);
        return mv;
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "格格头像管理-新增/编辑格格头像")
    public String save(HttpServletRequest request, @RequestParam(value = "editId", required = false, defaultValue = "-1") int editId,
        @RequestParam(value = "type", required = true) String type, GegeImageEntity image)
        throws Exception
    {
        image.setCreateTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        image.setId(editId);
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (geGeImageService.checkIsExist(image, type))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", image.getCategoryName() + "已经存在");
                return JSON.toJSONString(resultMap);
            }
            image.setIsAvailable(1);
            int resultStatus = geGeImageService.saveOrUpdate(image, type);
            if (resultStatus == 1)
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
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "新增失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView editTemplate(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        String type = request.getParameter("type");
        ModelAndView mv = new ModelAndView();
        mv.setViewName(type + "/update_image");
        GegeImageEntity image = geGeImageService.findGegeImageById(id, type);
        if (image == null)
        {
            mv.setViewName("forward:/error/404");
            return mv;
        }
        mv.addObject("gegeImage", image);
        return mv;
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "格格头像管理-删除格格头像")
    public String delete(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isAvailable", required = false, defaultValue = "0") int isAvailable,
        @RequestParam(value = "type", required = true) String type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("isAvailable", isAvailable);
        para.put("type", type);
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            boolean isUse = geGeImageService.checkIsUse(id, type);
            if (isUse)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "该分类正在使用中");
                return JSON.toJSONString(resultMap);
            }
            int result = geGeImageService.delete(para);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "删除失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "格格头像管理-批量删除格格头像")
    public String batchDelete(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids,
        @RequestParam(value = "isAvailable", required = true) int isAvailable, @RequestParam(value = "type", required = true) String type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        List<Integer> idList = new ArrayList<Integer>();
        StringBuffer inUseIdSb = new StringBuffer("[");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (ids.indexOf(",") > 0)
        {
            String[] arr = ids.split(",");
            for (String cur : arr)
            {
                idList.add(Integer.valueOf(cur));
            }
        }
        else
        {
            idList.add(Integer.valueOf(ids));
        }
        for (Iterator<Integer> it = idList.iterator(); it.hasNext();)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            int id = it.next();
            map.put("id", id);
            if (geGeImageService.checkIsUse(id, type))
            {
                it.remove();
                inUseIdSb.append(id).append(",");
            }
        }
        inUseIdSb.setCharAt(inUseIdSb.length() - 1, ']');
        if (idList.size() == 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "id为" + inUseIdSb + "的类别正在使用中");
            return JSON.toJSONString(resultMap);
        }
        para.put("isAvailable", isAvailable);
        para.put("idList", idList);
        para.put("type", type);
        // 结果
        
        int resultStatus = geGeImageService.batchDelete(para);
        if (resultStatus > 0)
        {
            resultMap.put("status", 1);
            resultMap.put("msg", "id为" + inUseIdSb + "的类别正在使用中");
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/getGegeImageById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getGegeImageById(@RequestParam(value = "imageId", required = true) int imageId, @RequestParam(value = "type", required = true) String type)
        throws Exception
    {
        GegeImageEntity image = geGeImageService.findGegeImageById(imageId, type);
        Map<String, Object> map = new HashMap<String, Object>();
        if (image == null)
        {
            map.put("status", 0);
        }
        else
        {
            map.put("status", 1);
            map.put("url", image.getImage());
        }
        return JSON.toJSONString(map);
    }
}
