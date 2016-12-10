package com.ygg.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.ygg.admin.entity.RelationDeliverAreaTemplateEntity;
import com.ygg.admin.entity.SellerDeliverAreaTemplateEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.SellerDeliverAreaService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.util.StringUtils;

/**
 * 商家配送地区控制器
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/sellerDeliverArea")
public class SellerDeliverAreaController
{
    private static final Logger logger = Logger.getLogger(SellerDeliverAreaController.class);
    
    @Resource
    private SellerDeliverAreaService sellerDeliverAreaService;
    
    @Resource
    private SellerService sellerService;
    
    /**
     * 控制器参数设为requird = true时，当参数类型不匹配时，会出现400错误，而用户不知道，所以统一默认设为required = false，然后给默认值
     * @param id：模版Id
     * @param sellerId：商家Id
     * @param type：模版类型，1配送地区，2不配送地区
     * @param name：模版名称
     * @param desc：地区描述
     * @param areaCodes：地区
     * @param other：例外地区
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdate(//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "type", required = false, defaultValue = "1") byte type,//
        @RequestParam(value = "name", required = false, defaultValue = "") String name,//
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "areaCodes", required = false, defaultValue = "") String areaCodes,//
        @RequestParam(value = "other", required = false, defaultValue = "") String other)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            //参数检查
            if (sellerId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择商家");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isBlank(name))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入模版名称");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isBlank(desc))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入地区描述");
                return JSON.toJSONString(resultMap);
            }
            
            //新增
            SellerDeliverAreaTemplateEntity areaTemplate = sellerDeliverAreaService.findSellerDeliverAreaTemplateByName(name);
            if (id == 0 && areaTemplate != null && areaTemplate.getSellerId() == sellerId)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", String.format("所选商家已经存在模板名称【%s】", name));
                return JSON.toJSONString(resultMap);
            }
            
            areaTemplate = new SellerDeliverAreaTemplateEntity();
            areaTemplate.setId(id);
            areaTemplate.setSellerId(sellerId);
            areaTemplate.setName(name);
            areaTemplate.setType(type);
            areaTemplate.setDesc(desc);
            
            int result = 0;
            if (id == 0)
            {
                result = sellerDeliverAreaService.insertSellerDeliverAreaTemplate(areaTemplate, areaCodes, other);
                resultMap.put("msg", result > 0 ? "保存成功" : "保存失败");
                resultMap.put("status", result > 0 ? 1 : result);
            }
            else
            {
                result = sellerDeliverAreaService.updateSellerDeliverAreaTemplate(areaTemplate, areaCodes, other);
                resultMap.put("msg", result > 0 ? "保存成功" : (result == -1 ? "改模版正在使用中，不能编辑" : "保存失败"));
                resultMap.put("status", result > 0 ? 1 : result);
            }
            
        }
        catch (Exception ex)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            logger.error(ex.getMessage(), ex);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delete(@PathVariable(value = "id") int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (sellerDeliverAreaService.countDeliverAreaTemplateFromProductBase(id) > 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "该模版正在使用中，不能删除");
            }
            else
            {
                int result = sellerDeliverAreaService.deleteSellerDeliverAreaTemplate(id);
                resultMap.put("status", result > 0 ? 1 : result);
                resultMap.put("msg", result > 0 ? "删除成功" : "删除失败");
            }
        }
        catch (Exception e)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/jsonSellerDeliverAreaTemplateCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerDeliverAreaTemplateCode(@RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "templateId", required = false, defaultValue = "0") int templateId)
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        try
        {
            List<SellerDeliverAreaTemplateEntity> list = sellerDeliverAreaService.findSellerDeliverAreaTemplateBysid(sellerId);
            for (SellerDeliverAreaTemplateEntity entity : list)
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("code", entity.getId() + "");
                map.put("text", entity.getName() + "");
                if (templateId == entity.getId())
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
    
    /**
     * 商家配送地区模版
     * @param sellerId
     * @return
     */
    @RequestMapping(value = "/sellerDeliverAreaTemplateList")
    public ModelAndView sellerDeliverAreaTemplateList(@RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId)
    {
        ModelAndView mv = new ModelAndView("seller/sellerDeliverAreaTemplateList");
        mv.addObject("sellerId", sellerId + "");
        return mv;
    }
    
    /**
     * 异步加载商家配送地区模版
     * @param page
     * @param rows
     * @param sellerId
     * @param name
     * @param desc
     * @param type
     * @return
     */
    @RequestMapping(value = "/jsonSellerDeliverAreaTemplate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerDeliverAreaTemplate(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "desc", required = false, defaultValue = "") String desc,//
        @RequestParam(value = "type", required = false, defaultValue = "0") int type)
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
            
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (!"".equals(desc))
            {
                para.put("desc", "%" + desc + "%");
            }
            if (type != 0)
            {
                para.put("type", type);
            }
            resultMap = sellerDeliverAreaService.jsonSellerDeliverAreaTemplate(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载商家配送地区模版列表出错了", e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/getSellerDeliverAreaTemplate/{id}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getSellerDeliverAreaTemplate(@PathVariable("id") int id)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            SellerDeliverAreaTemplateEntity template = sellerDeliverAreaService.getSellerDeliverAreaTemplateAndRelationArea(id);
            resultMap.put("id", id);
            resultMap.put("sellerId", template == null ? "0" : template.getSellerId() + "");
            SellerEntity seller = sellerService.findSellerById(template == null ? 0 : template.getSellerId());
            resultMap.put("sellerName", seller == null ? "" : seller.getRealSellerName());
            resultMap.put("name", template == null ? "" : template.getName());
            resultMap.put("desc", template == null ? "" : template.getDesc());
            resultMap.put("type", template == null ? "" : template.getType() > 2 ? (template.getType() - 2) : template.getType());
            resultMap.put("areaCodes", template == null ? new ArrayList<RelationDeliverAreaTemplateEntity>() : template.getAreas());
            resultMap.put("exceptAreaCodes", template == null ? new ArrayList<RelationDeliverAreaTemplateEntity>() : template.getExceptAreas());
        }
        catch (Exception e)
        {
            logger.error("异步加载商家配送地区模版列表出错了", e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<SellerDeliverAreaTemplateEntity>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/commonPostageList")
    public ModelAndView commonPostageList()
    {
        ModelAndView mv = new ModelAndView("seller/commonPostageList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonCommonPostage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCommonPostage(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
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
            resultMap = sellerDeliverAreaService.jsonCommonPostage(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载通用偏远地区运费列表出错了", e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/addCommonPostage", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addCommonPostage(@RequestParam(value = "provinceCode", required = false, defaultValue = "") String provinceCode,
        @RequestParam(value = "freightMoney", required = false, defaultValue = "0") int freightMoney)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(provinceCode))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择省份");
                return JSON.toJSONString(resultMap);
            }
            if (freightMoney <= 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "运费必须大于0");
                return JSON.toJSONString(resultMap);
            }
            return sellerDeliverAreaService.addCommonPostage(provinceCode, freightMoney);
        }
        catch (Exception e)
        {
            logger.error("新增通用偏远地区运费出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙，请稍后再试");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateCommonPostage", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateCommonPostage(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "freightMoney", required = false, defaultValue = "0") int freightMoney)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (freightMoney <= 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "运费必须大于0");
                return JSON.toJSONString(resultMap);
            }
            return sellerDeliverAreaService.updateCommonPostage(id, freightMoney);
        }
        catch (Exception e)
        {
            logger.error("更新通用偏远地区运费出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙，请稍后再试");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/extraPostage/{id}")
    public ModelAndView extraPostage(@PathVariable("id") int id)
    {
        ModelAndView mv = new ModelAndView("seller/extraPostage");
        try
        {
            SellerDeliverAreaTemplateEntity template = sellerDeliverAreaService.getSellerDeliverAreaTemplateAndRelationArea(id);
            mv.addObject("templateId", id + "");
            mv.addObject("templateName", template.getName());
            SellerEntity seller = sellerService.findSellerById(template == null ? 0 : template.getSellerId());
            mv.addObject("sellerName", seller == null ? "" : seller.getRealSellerName());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonExtraPostage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonExtraPostage(//
        @RequestParam(value = "templateId", required = false, defaultValue = "0") int templateId,//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
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
            para.put("templateId", templateId + "");
            resultMap = sellerDeliverAreaService.jsonExtraPostage(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载通用偏远地区运费列表出错了", e);
            resultMap.put("total", 0);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateExtraPostage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateExtraPostage(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isExtra", required = false, defaultValue = "0") int isExtra,
        @RequestParam(value = "freightMoney", required = false, defaultValue = "0") int freightMoney)
    {
        try
        {
            return sellerDeliverAreaService.updateExtraPostage(id, isExtra, freightMoney);
        }
        catch (Exception e)
        {
            logger.error("修改偏远地区运费出错了", e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙，请稍后");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/findCommonExtraPostage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object findCommonExtraPostage(@RequestParam(value = "provinceCode", required = false, defaultValue = "") String provinceCode)
    {
        try
        {
            return sellerDeliverAreaService.findCommonExtraPostage(provinceCode);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return 0;
        }
    }
}
