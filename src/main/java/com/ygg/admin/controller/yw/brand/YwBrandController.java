package com.ygg.admin.controller.yw.brand;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.ygg.admin.entity.yw.YwBrandEntity;
import com.ygg.admin.service.yw.brand.YwBrandService;
import com.ygg.admin.util.CommonUtil;


@Controller
@RequestMapping("ywBrand")
public class YwBrandController
{
    private static Logger logger = Logger.getLogger(YwBrandController.class);
    
    @Resource
    private YwBrandService ywBrandService;
    private int brandCategoryId;
    
    @RequestMapping(value = "/brandManage/{id}",produces = "application/json;charset=UTF-8")
    public ModelAndView list(@PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("ywBrand/ywBrand");
        this.brandCategoryId = id;
        String brandCategoryName = ywBrandService.getBrandCategoryName(id);
        mv.addObject("brandCategoryId", brandCategoryId+"");
        mv.addObject("brandCategoryName", brandCategoryName);
        return mv;
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "品牌管理-新增更新品牌")
    public String saveOrUpdate(HttpServletRequest request)
    {
        Map<String, Object> statusMap = new HashMap<String, Object>();
        int status = 0;
        String msg = null;
        try
        {
            YwBrandEntity brand = new YwBrandEntity();
            CommonUtil.wrapParamter2Entity(brand, request);
            if(0==brand.getId()){
                status = ywBrandService.addBrand(brand);
            }else{
                status = ywBrandService.updateBrand(brand);
            }
            msg = "保存成功";
        }
        catch (Exception e)
        {
            statusMap.put("status", 0);
            statusMap.put("msg", "保存失败");
            logger.error("编辑商品出错", e);
        }finally{
            statusMap.put("status", status);
            statusMap.put("msg", msg);
        }
        return JSON.toJSONString(statusMap);
    }
    
    @RequestMapping(value = "/jsonBrandInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBrandInfo(  
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = true, defaultValue = "50") int rows,
            @RequestParam(value = "categoryId", required = true, defaultValue = "0") int categoryId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            if (page == 0) page = 1;
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("categoryId", brandCategoryId);
            resultMap = ywBrandService.findBrandInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status",0);
            resultMap.put("msg","加载列表失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateBrandDisplay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "品牌管理-展现不展现")
    public String updateBrandDisplay(HttpServletRequest request)
    {
        Map<String, Object> statusMap = new HashMap<String, Object>();
        int status = 0;
        String msg = null;
        try
        {
            YwBrandEntity brand = new YwBrandEntity();
            CommonUtil.wrapParamter2Entity(brand, request);
            status = ywBrandService.updateBrandDisplay(brand);
            msg = "保存成功";
        }
        catch (Exception e)
        {
            statusMap.put("status", 0);
            statusMap.put("msg", "保存失败");
            logger.error("编辑品牌展现出错", e);
        }finally{
            statusMap.put("status", status);
            statusMap.put("msg", msg);
        }
        return JSON.toJSONString(statusMap);
    }
}
