package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.ThirdPartyProductEntity;
import com.ygg.admin.service.ThirdPartyProductService;
import com.ygg.admin.util.CommonUtil;

/**
 * 第三方平台商品
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/thirdProduct")
public class ThirdPartyProductController
{
    private static Logger logger = Logger.getLogger(ThirdPartyProductController.class);
    
    @Resource
    private ThirdPartyProductService thirdPartyProductService;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("productThirdParty/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonThirdProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonThirdProductInfo(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "channelId", required = false, defaultValue = "0") int channelId,//
        @RequestParam(value = "productCode", required = false, defaultValue = "") String productCode,
        @RequestParam(value = "providerProductId", required = false, defaultValue = "0") int ppId,
        @RequestParam(value = "providerProductName", required = false, defaultValue = "") String ppName)
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
            if (id != 0)
            {
                para.put("id", id);
            }
            if (channelId != 0)
            {
                para.put("channelId", channelId);
            }
            if (!"".equals(productCode))
            {
                para.put("productCode", productCode);
            }
            if (ppId != 0)
            {
                para.put("ppId", ppId);
            }
            if (!"".equals(ppName))
            {
                para.put("ppName", ppName);
            }
            return thirdPartyProductService.jsonThirdProductInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载第三方商品列表出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<Map<String, Object>>());
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "第三方商品管理-编辑商品")
    public String saveOrUpdate(HttpServletRequest request)
    {
        try
        {
            ThirdPartyProductEntity product = new ThirdPartyProductEntity();
            CommonUtil.wrapParamter2Entity(product, request);
            if (product.getId() == 0)
            {
                return thirdPartyProductService.saveThirdPartyProduct(product);
            }
            else
            {
                return thirdPartyProductService.updateThirdPartyProduct(product);
            }
        }
        catch (Exception e)
        {
            logger.error("编辑商品出错", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/importProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "第三方商品管理-导入商品")
    public String importProduct(@RequestParam("userFile") MultipartFile file)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            if (file == null)
            {
                result.put("status", 0);
                result.put("msg", "请上传文件");
                return JSON.toJSONString(result);
            }
            return thirdPartyProductService.importThirdPartyProduct(file);
        }
        catch (Exception e)
        {
            logger.error("导入商品出错", e);
            result.put("status", 0);
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_product_code"))
            {
                result.put("msg", "商品编码重复");
            }
            else
            {
                result.put("msg", "保存失败");
            }
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/updateThirdPartyProductStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "第三方商品管理-修改商品可用状态")
    public String updateThirdPartyProductStatus(@RequestParam(value = "id", required = false, defaultValue = "") String ids,
        @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (StringUtils.isEmpty(ids))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的商品");
                return JSON.toJSONString(resultMap);
            }
            return thirdPartyProductService.updateThirdPartyProductStatus(ids, isAvailable);
        }
        catch (Exception e)
        {
            logger.error("修改商品可用状态出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "导入第三方商品模板";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = new String[] {"渠道名称", "商品编码"};
            
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
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
    
    @RequestMapping(value = "/updateThirdPartyProductStock", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "第三方商品管理-修改商品库存")
    public String updateThirdPartyProductStock(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "stock", required = false, defaultValue = "0") int stock,
        @RequestParam(value = "providerProductId", required = false, defaultValue = "0") int providerProductId,
        @RequestParam(value = "groupCount", required = false, defaultValue = "1") int groupCount)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            return thirdPartyProductService.updateThirdPartyProductStock(id, stock, providerProductId, groupCount);
        }
        catch (Exception e)
        {
            logger.error("修改商品库存出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/updateThirdPartyProductSales", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "第三方商品管理-修改商品销量")
    public String updateThirdPartyProductSales(@RequestParam(value = "id", required = false, defaultValue = "0") int id, @RequestParam(value = "sales", required = true) int sales)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            return thirdPartyProductService.updateThirdPartyProductSales(id, sales);
        }
        catch (Exception e)
        {
            logger.error("修改商品销量出错", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
}
