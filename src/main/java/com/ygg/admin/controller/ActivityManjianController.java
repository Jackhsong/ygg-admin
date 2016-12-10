package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.ActivityEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.entity.ActivitiyRelationProductEntity;
import com.ygg.admin.entity.ActivityManjianEntity;
import com.ygg.admin.service.ActivityManjianService;
import com.ygg.admin.util.POIUtil;

/**
 * 满减活动
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/activityManjian")
public class ActivityManjianController
{
    private static Logger logger = Logger.getLogger(ActivityManjianController.class);
    
    @Resource
    private ActivityManjianService activityManjianService;
    
    /**
     * 全场满减活动列表
     * @return
     */
    @RequestMapping("/allList")
    public ModelAndView allList()
    {
        ModelAndView mv = new ModelAndView("activityManjian/allList");
        mv.addObject("type", "1");
        mv.addObject("status", "2,3");
        return mv;
    }
    
    /**
     * 专场满减活动列表
     * @return
     */
    @RequestMapping("/partList")
    public ModelAndView partList()
    {
        ModelAndView mv = new ModelAndView("activityManjian/partList");
        mv.addObject("type", "2,3,4");
        mv.addObject("status", "2,3");
        return mv;
    }
    
    @RequestMapping(value = "/jsonActivityManjianInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonActivityManjianInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "status", required = false, defaultValue = "") String status,//
        @RequestParam(value = "type", required = false, defaultValue = "1") String type)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("status", status);
            para.put("typeList", Arrays.asList(type.split(",")));
            Map<String, Object> result = activityManjianService.jsonActivityManjianInfo(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            logger.error("异步加载自定定义功能列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String saveOrUpdate(ActivityManjianEntity activity, HttpServletRequest request)
    {
        try
        {
            String groupSaleId = StringUtils.isEmpty(request.getParameter("groupSaleId")) ? "0" : request.getParameter("groupSaleId");
            String customSaleId = StringUtils.isEmpty(request.getParameter("customSaleId")) ? "0" : request.getParameter("customSaleId");
            String customPageId = StringUtils.isEmpty(request.getParameter("customPageId")) ? "0" : request.getParameter("customPageId");
            if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.ALL.getCode())
            {
                activity.setTypeId(0);
            }
            else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.GROUP.getCode())
            {
                activity.setTypeId(Integer.parseInt(groupSaleId));
            }
            else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.CUSTOM_ACTIVITY.getCode())
            {
                activity.setTypeId(Integer.parseInt(customSaleId));
            }
            else if (activity.getType() == ActivityEnum.MANJIAN_RELATION_TYPE.CUSTOM_PAGE.getCode())
            {
                activity.setTypeId(Integer.parseInt(customPageId));
            }
            if (activity.getId() == 0)
            {
                return activityManjianService.saveActivityManjian(activity);
            }
            else
            {
                return activityManjianService.updateActivityManjian(activity);
            }
        }
        catch (Exception e)
        {
            logger.error("编辑满减活动出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateDisplayStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateDisplayStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "code", required = true) int isAvailable)
    {
        
        try
        {
            return activityManjianService.updateDisplayStatus(id, isAvailable);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping("/manageProduct/{type}/{typeId}")
    public ModelAndView manageProduct(@PathVariable("type") int type, @PathVariable("typeId") int typeId)
    {
        ModelAndView mv = new ModelAndView("activityManjian/manageProduct");
        mv.addObject("type", type + "");
        mv.addObject("typeId", typeId + "");
        return mv;
    }
    
    @RequestMapping(value = "/jsonManjianProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonManjianProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "type", required = false, defaultValue = "2") int type,
        @RequestParam(value = "typeId", required = false, defaultValue = "") int typeId)
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("type", type);
            para.put("typeId", typeId);
            return activityManjianService.jsonProductInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载" + ActivityEnum.MANJIAN_RELATION_TYPE.getDescByCode((byte)type) + "商品列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("total", 0);
            result.put("rows", new ArrayList<Object>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/jsonProductList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductList(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, //
        @RequestParam(value = "name", required = false, defaultValue = "") String name, //
        @RequestParam(value = "status", required = false, defaultValue = "0") int status, //
        @RequestParam(value = "brandId", required = false, defaultValue = "0") int brandId, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId, //
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark, //
        @RequestParam(value = "type", required = false, defaultValue = "2") int type, //
        @RequestParam(value = "typeId", required = false, defaultValue = "0") int typeId, //
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable, //
        @RequestParam(value = "isOffShelves", required = false, defaultValue = "0") int isOffShelves)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (status == 0)
            {
                resultMap.put("rows", new ArrayList<Object>());
                resultMap.put("total", 0);
                return JSON.toJSONString(resultMap);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(code))
            {
                para.put("code", code);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (brandId != 0)
            {
                para.put("brandId", brandId);
            }
            if (sellerId != 0)
            {
                para.put("sellerId", sellerId);
            }
            if (productId != 0)
            {
                para.put("productId", productId);
            }
            if (!"".equals(remark))
            {
                para.put("remark", "%" + remark + "%");
            }
            para.put("type", type);
            para.put("typeId", typeId);
            para.put("isAvailable", isAvailable);
            para.put("isOffShelves", isOffShelves);
            return activityManjianService.jsonProductListForAdd(para);
        }
        catch (Exception e)
        {
            logger.error("满减活动添加商品异步加载出错", e);
            resultMap.put("rows", new ArrayList<Object>());
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/addProductForActivityManjian", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addProductForActivityManjian(//
        @RequestParam(value = "productIds", required = false, defaultValue = "") String productIds,//
        @RequestParam(value = "typeId", required = false, defaultValue = "0") int typeId,//
        @RequestParam(value = "type", required = false, defaultValue = "1") int type)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (productIds.isEmpty())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请输入商品Id");
            return JSON.toJSONString(resultMap);
        }
        if (typeId == 0)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要添加商品的活动");
            return JSON.toJSONString(resultMap);
        }
        try
        {
            return activityManjianService.addProductForActivityManjian(type, typeId, productIds);
        }
        catch (Exception e)
        {
            logger.error("组合批量添加商品失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/deleteActivityManjianProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteActivityManjianProduct(@RequestParam(value = "ids", required = false, defaultValue = "") String ids)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (ids.isEmpty())
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "请选择要删除的商品");
            return JSON.toJSONString(resultMap);
        }
        try
        {
            int status = activityManjianService.deleteActivityManjianProduct(Arrays.asList(ids.split(",")));
            resultMap.put("status", status > 0 ? 1 : status);
            resultMap.put("msg", status > 0 ? "删除成功" : "删除失败");
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            logger.error("删除商品失败", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "删除出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateProductTime", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "满减活动商品管理-修改商品时间")
    public String updateProductTime(@RequestParam(value = "productIds", required = false, defaultValue = "") String productIds,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(productIds))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要修改的商品");
                return JSON.toJSONString(resultMap);
            }
            
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "开始时间和结束时间不能为空");
                return JSON.toJSONString(resultMap);
            }
            int status = activityManjianService.updateProductTime(productIds, startTime, endTime);
            resultMap.put("status", status > 0 ? 1 : status);
            resultMap.put("msg", status > 0 ? "修改成功" : "修改失败");
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            logger.error("满减活动修改商品时间出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/exportProduct", produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "满减活动-导出商品")
    public void exportProduct(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "type", required = false, defaultValue = "2") int type, @RequestParam(value = "typeId", required = false, defaultValue = "0") int typeId)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("type", type);
            para.put("typeId", typeId);
            List<ActivitiyRelationProductEntity> reList = activityManjianService.findActivitiyRelationProduct(para);
            String[] title = {"商品ID", "商品编码", "商品状态", "商品类型", "长名称", "短名称", "商品备注", "销量", "库存", "原价", "现价", "商家", "发货地"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (ActivitiyRelationProductEntity product : reList)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(product.getProductId() + "");
                r.createCell(cellIndex++).setCellValue(product.getCode());
                r.createCell(cellIndex++).setCellValue(product.getIsOffShelves() == 1 ? "下架" : "上架");
                r.createCell(cellIndex++).setCellValue(ProductEnum.PRODUCT_TYPE.getDescByCode(product.getProductType()));
                r.createCell(cellIndex++).setCellValue(product.getProductName());
                r.createCell(cellIndex++).setCellValue(product.getProductShortName());
                r.createCell(cellIndex++).setCellValue(product.getRemark());
                r.createCell(cellIndex++).setCellValue(product.getSell() + "");
                r.createCell(cellIndex++).setCellValue(product.getStock() + "");
                r.createCell(cellIndex++).setCellValue(product.getMarketPrice() + "");
                r.createCell(cellIndex++).setCellValue(product.getSalesPrice() + "");
                r.createCell(cellIndex++).setCellValue(product.getSellerName());
                r.createCell(cellIndex++).setCellValue(product.getSendAddress());
            }
            response.setContentType("application/vnd.ms-excel");
            String fileName = java.net.URLEncoder.encode("满减活动商品", "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出满减活动商品出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            fOut.write(errorStr.getBytes());
            fOut.flush();
        }
        finally
        {
            if (fOut != null)
            {
                try
                {
                    fOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
