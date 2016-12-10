package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
import com.ygg.admin.code.CustomLayoutRelationTypeEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.service.IndexSettingService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.util.ImageUtil;

/**
 * 首页配置
 * 
 * @author Administrator
 *         
 */
@Controller
@RequestMapping("index")
public class IndexSettingController
{
    private Logger logger = Logger.getLogger(IndexSettingController.class);
    
    @Resource
    private IndexSettingService indexSettingService;
    
    @Resource
    private ProductService productService;
    
    @RequestMapping("/set")
    public ModelAndView list()
    {
        
        ModelAndView mv = new ModelAndView();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            para.put("key", "is_register_coupon");
            Map<String, String> map1 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "popup_ad_version");
            Map<String, String> map2 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "support_last_version");
            Map<String, String> map3 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "weixin_gege");
            Map<String, String> map4 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "shopping_back_point_factor");
            Map<String, String> map5 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "left_page_id");
            Map<String, String> map6 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "right_page_id");
            Map<String, String> map7 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "brand_page_id");
            Map<String, String> map8 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "not_display_unionpay_sellerid");
            Map<String, String> map9 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "nationwide_version");
            Map<String, String> map10 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "gege_recommend_title_display_status");
            Map<String, String> map11 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "tuan_weixin_gege");
            Map<String, String> map12 = indexSettingService.findSettingByKey(para);
            
            para.put("key", "sale_single_product_image");
            Map<String, String> map13 = indexSettingService.findSettingByKey(para);
            
            mv.setViewName("index/setting");
            mv.addObject("map1", map1);
            mv.addObject("map2", map2);
            mv.addObject("map3", map3);
            mv.addObject("map4", map4);
            mv.addObject("map5", map5);
            mv.addObject("map6", map6);
            mv.addObject("map7", map7);
            mv.addObject("map8", map8);
            mv.addObject("map9", map9);
            mv.addObject("map10", map10);
            mv.addObject("map11", map11);
            mv.addObject("map12", map12);
            mv.addObject("map13", map13);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/updateConfigStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-修改首页配置")
    public String updateConfigStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "status", required = true) int status)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("status", status);
            int result = indexSettingService.updateConfigStatus(para);
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
            resultMap.put("msg", "操作失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/saveImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-修改首页配置")
    public String saveImage(@RequestParam(value = "id", required = true) int id, 
    		@RequestParam(value = "image", required = true) String image)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            if(image.contains("#")){
            	para.put("status", image);
            }else{
            	Map<String, Object> propertysMap = ImageUtil.getProperty(image);
        		short height = Short.valueOf((String)propertysMap.get("height"));
        		para.put("status", image+"#"+height);
            }
            int result = indexSettingService.updateConfigStatus(para);
            if (result == 1){
                resultMap.put("status", 1);
            }else{
                resultMap.put("status", 0);
                resultMap.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "操作失败");
        }
        return JSON.toJSONString(resultMap);
    }
    /**
     * 首页广告管理
     * 
     * @return
     */
    @RequestMapping("/advList")
    public ModelAndView advList()
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("key", "is_popup_ad");
            Map<String, String> advMap = indexSettingService.findSettingByKey(para);
            Map<String, Object> resultMap = indexSettingService.jsonAdvertiseInfo(para);
            int total = Integer.valueOf(resultMap.get("total") + "").intValue();
            mv.setViewName("index/advertiseList");
            mv.addObject("advMap", advMap);
            if (total >= 3)
            {
                mv.addObject("canAdd", 0);
            }
            else
            {
                mv.addObject("canAdd", 1);
            }
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
            logger.error(e.getMessage(), e);
        }
        return mv;
    }
    
    /**
     * 首页广告管理列表
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonAdvertiseInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonAdvertiseInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
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
            resultMap = indexSettingService.jsonAdvertiseInfo(para);
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
     * 更改首页广告排序
     * 
     * @param id
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/updateAdvertiseSequence", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更改首页广告排序")
    public String updateAdvertiseSequence(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "sequence", required = true) int sequence)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("sequence", sequence);
            int status = indexSettingService.updateAdvertiseSequence(para);
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
     * 更改首页广告展现状态
     * 
     * @param id
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateAdvertiseDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更改首页广告展现状态")
    public String updateAdvertiseDisplayStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int code)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("isDisplay", code);
            para.put("id", id);
            int status = indexSettingService.updateAdvertiseDisplayStatus(para);
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
     * 删除首页广告
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteAdvertise", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-删除首页广告")
    public String deleteAdvertise(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            
            int status = indexSettingService.deleteAdvertise(id);
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
    
    @RequestMapping("/addAdv")
    public ModelAndView addAdv()
    {
        ModelAndView mv = new ModelAndView("index/update");
        Map<String, Object> advMap = new HashMap<String, Object>();
        advMap.put("isDisplay", 0);
        mv.addObject("advMap", advMap);
        return mv;
    }
    
    /**
     * 保存/修改首页广告
     * 
     * @param id：id
     * @param remark：备注
     * @param image：图片
     * @param type：类型：1单品（单品为特卖时，存数数据库type=1;当单品为商城商品是，存入数据库type=4）,2组合特卖,3自定义特卖
     * @param productId：关联单品Id
     * @param groupSale：关联特卖组合
     * @param customSale：关联自定义特卖
     * @param isDisplay：是否展现
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateAdvertise", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-新增/编辑首页广告")
    public String saveOrUpdateAdvertise(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, @RequestParam(value = "image", required = false, defaultValue = "") String image,
        @RequestParam(value = "type", required = false, defaultValue = "1") int type, @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,
        @RequestParam(value = "groupSale", required = false, defaultValue = "0") int groupSale,
        @RequestParam(value = "customSale", required = false, defaultValue = "0") int customSale,
        @RequestParam(value = "isDisplay", required = false, defaultValue = "0") int isDisplay)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("remark", remark);
            para.put("image", image);
            para.put("type", type);
            
            if (type == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal())
            {
                ProductEntity product = productService.findProductById(productId);
                if (product == null)
                {
                    result.put("status", 0);
                    result.put("msg", "Id=" + productId + "的商品不存在");
                    return JSON.toJSONString(result);
                }
                // 特卖商品，关联的类型=1
                if (product.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
                {
                    para.put("type", CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_SALEPRODUCT.ordinal());
                    
                }
                // 商城商品，关联的类型=4
                else if (product.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
                {
                    para.put("type", CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_MALLPRODUCT.ordinal());
                }
                para.put("displayId", productId);
            }
            else if (type == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_COMMONACTIVITY.ordinal())
            {
                para.put("displayId", groupSale);
            }
            else if (type == CustomLayoutRelationTypeEnum.LAYOUT_RELATION_OF_CUSTOMSALE.ordinal())
            {
                para.put("displayId", customSale);
            }
            para.put("isDisplay", isDisplay);
            
            int status = indexSettingService.saveOrUpdateAdvertise(para);
            if (status == 1)
            {
                result.put("status", 1);
            }
            else if (status == 2)
            {
                result.put("status", 0);
                result.put("msg", "请检查图片的尺寸，尺寸长宽比必须是 750:1234，允许误差0.01");
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
    
    @RequestMapping("/editAdv/{id}")
    public ModelAndView editAdv(@PathVariable(value = "id") int id)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            Map<String, Object> advMap = indexSettingService.findAdvertiseById(id);
            if (advMap == null)
            {
                mv.setViewName("error/404");
            }
            else
            {
                mv.setViewName("index/update");
                mv.addObject("advMap", advMap);
                mv.addObject("displayId", advMap.get("displayId"));
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 更新广告版本号
     * 
     * @param id
     * @param version
     * @return
     */
    @RequestMapping(value = "/updateAdvertiseVersion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更新广告版本号")
    public String updateAdvertiseVersion(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "version", required = true) int version)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("version", version);
            int status = indexSettingService.updateAdvertiseVersion(para);
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
     * 更新平台支持版本号
     * 
     * @param id
     * @param version
     * @return
     */
    @RequestMapping(value = "/updatePlatformVersion", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更新平台支持版本号")
    public String updatePlatformVersion(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "version", required = true) String version)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("version", version);
            int status = indexSettingService.updatePlatformVersion(para);
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
     * 马甲APP管理
     * 
     * @return
     */
    @RequestMapping("/vestList")
    public ModelAndView vestList()
    {
        
        ModelAndView mv = new ModelAndView("index/vestAppList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonVestAppInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonVestAppInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "value", required = false, defaultValue = "-1") int value, @RequestParam(value = "value1", required = false, defaultValue = "-1") int value1)
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
            if (!"".equals(desc))
            {
                para.put("desc", "%" + desc + "%");
            }
            if (value != -1)
            {
                para.put("value", value);
            }
            if (value1 != -1)
            {
                para.put("value1", value1);
            }
            resultMap = indexSettingService.jsonVestAppInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/saveOrUpdateVestApp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-新增/编辑马甲App")
    public String saveOrUpdateVestApp(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "key", required = false, defaultValue = "") String key, @RequestParam(value = "desc", required = false, defaultValue = "") String desc,
        @RequestParam(value = "value", required = false, defaultValue = "0") int value)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            para.put("id", id);
            para.put("key", key);
            para.put("desc", desc);
            para.put("value", value);
            int status = indexSettingService.saveOrUpdateVestApp(para);
            if (status == 1)
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
    
    @RequestMapping(value = "/updateVestAppStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-修改马甲App状态")
    public String updateVestAppStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "value", required = true) int value)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            para.put("id", id);
            para.put("value", value);
            int status = indexSettingService.updateVestAppStatus(para);
            if (status == 1)
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
            resultMap.put("msg", "操作失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateVestAppCustomLayoutDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-修改马甲App布局状态")
    public String updateVestAppCustomLayoutDisplayStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "value", required = true) int value)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            para.put("id", id);
            para.put("value1", value);
            int status = indexSettingService.updateVestAppCustomLayoutDisplayStatus(para);
            if (status == 1)
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
            resultMap.put("msg", "操作失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/deleteVestApp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-删除马甲app")
    public String deleteVestApp(@RequestParam(value = "id", required = true) int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int status = indexSettingService.deleteVestApp(id);
            if (status == 1)
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
            resultMap.put("msg", "操作失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateWeiXin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更新微信号")
    public String updateWeiXin(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "weixin", required = true) String weixin)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("newValue", weixin);
            int status = indexSettingService.updatePlatformConfigById(para);
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
    
    @RequestMapping(value = "/updateGroupWeiXin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更新左岸城堡微信号")
    public String updateGroupWeiXin(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "groupWeiXin", required = true) String groupWeiXin)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("newValue", groupWeiXin);
            int status = indexSettingService.updatePlatformConfigById(para);
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
    
    @RequestMapping(value = "/updateShoppingReturnPointFactor", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "首页配置管理-更新购物返积分倍数")
    public String updateShoppingReturnPointFactor(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "factor", required = true) String factor)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("newValue", factor);
            int status = indexSettingService.updatePlatformConfigById(para);
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
    
    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateConfig(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "value", required = true) String value)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("newValue", value);
            int status = indexSettingService.updatePlatformConfigById(para);
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
}
