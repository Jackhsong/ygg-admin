package com.ygg.admin.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.WareHouseEnum;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.ChannelProductEntity;
import com.ygg.admin.service.ChannelProductManageService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.Excel.ExcelMaker;


/**
 * 第三方平台管理-商品管理
 * @author Qiu,Yibo
 *
 */
@Controller
@RequestMapping("/channelProManage")
public class ChannelProductManageController
{
    private static Logger logger = Logger.getLogger(ChannelProductManageController.class);
    
    @Resource
    private ChannelProductManageService channelProductManageService;
    
    @RequestMapping("/channelProManage")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("channel/channelProManage");
        return mv;
    }
    
    @RequestMapping(value = "/jsonChannelProInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonChannelProInfo(  
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,//
            @RequestParam(value = "rows", required = true, defaultValue = "50") int rows,// 
            @RequestParam(value = "id", required = false,defaultValue = "0") int id,//
            @RequestParam(value = "channelId", required = false,defaultValue = "0") int channelId,//
            @RequestParam(value = "wareHouseId", required = false,defaultValue = "0") int wareHouseId,
            @RequestParam(value = "productCode", required = false) String productCode,
            @RequestParam(value = "productName", required = false) String productName)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            
            if (page == 0) page = 1;
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if(id!=0) para.put("id", id);
            if(channelId!=0) para.put("channelId", channelId);
            if(wareHouseId!=0) para.put("wareHouseId", wareHouseId);
            if(StringUtils.isNotEmpty(productCode)) para.put("productCode", productCode);
            if(StringUtils.isNotEmpty(productName)) para.put("productName", productName);
            resultMap = channelProductManageService.jsonChannelProInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-编辑商品")
    public String saveOrUpdate(HttpServletRequest request)
    {
        Map<String, Object> statusMap = new HashMap<String, Object>();
        try
        {
            ChannelProductEntity product = new ChannelProductEntity();
            CommonUtil.wrapParamter2Entity(product, request);
            if(0==product.getId()){
                statusMap = channelProductManageService.addChannelProduct(product);
            }else{
                statusMap = channelProductManageService.updateChannelProduct(product);
            }
        }
        catch (Exception e)
        {
            statusMap.put("status", 0);
            statusMap.put("msg", "保存失败");
            logger.error("编辑商品出错", e);
        }
        return JSON.toJSONString(statusMap);
    }
    
    @RequestMapping(value = "/getProductName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商品管理-获取商品名称")
    public String getProductName(HttpServletRequest request)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            ChannelProductEntity product = new ChannelProductEntity();
            CommonUtil.wrapParamter2Entity(product, request);
            int[] sellerIdArr = WareHouseEnum.getSellerIdById(product.getWareHouseId());
            List<Integer> sellerIdList = new ArrayList<Integer>();  
            for(int sellerId:sellerIdArr) sellerIdList.add(sellerId);
            para.put("sellerIdList",sellerIdList);
            para.put("productCode", product.getProductCode());
            Map<String, Object> map = channelProductManageService.getProductName(para);
            return JSON.toJSONString(map);
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
    
    
    @RequestMapping(value = "/jsonWareHouseCode")
    @ResponseBody
    public String jsonWareHouseCode(){
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (WareHouseEnum e : WareHouseEnum.values())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("code", e.getId());
            para.put("text", e.getValue());
            resultList.add(para);
        }
        return JSON.toJSONString(resultList);
    }
    
    @RequestMapping(value = "/downloadTemplate")
    @ResponseBody
    public void downloadTemplate(HttpServletRequest request,HttpServletResponse response){
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "第三方商品模板";
            OutputStream fOut = null;
            HSSFWorkbook workbook = null;
            try
            {
                codedFileName = URLEncoder.encode(codedFileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
                workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet();
                String[] str = new String[] {"渠道名","仓库名", "商品编码","商品名称"};
                Row row = sheet.createRow(0);
                for (int i = 0; i < str.length; i++)
                {
                    sheet.setColumnWidth(i, 5500);
                    row.createCell(i).setCellValue(str[i]);
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
    
    @RequestMapping(value = "/uploadChannelProFile")
    @ResponseBody
    public Object uploadChannelProFile(HttpServletRequest request, MultipartHttpServletRequest multipartRequest)
    {
        Map<String, Object> statusMap = new HashMap<String,Object>();
        try
        {
            MultipartFile file = multipartRequest.getFile("uploadChannelProFile");
            statusMap = channelProductManageService.uploadChannelProFile(file);
        }
        catch (Exception ex)
        {
            logger.error("导入供应商商品失败", ex);
            statusMap.put("status", 1);
            statusMap.put("msg", ex.getMessage());
        }
        return statusMap;

    }
    
    
    @RequestMapping(value = "/exportChannelPro")
    @ResponseBody
    public void exportChannelPro(
            @RequestParam(value = "id", required = false,defaultValue = "0") int id,//
            @RequestParam(value = "channelId", required = false,defaultValue = "0") int channelId,//
            @RequestParam(value = "wareHouseId", required = false,defaultValue = "0") int wareHouseId,
            @RequestParam(value = "productCode", required = false) String productCode,
            @RequestParam(value = "productName", required = false) String productName,
            HttpServletResponse response) throws IOException
    {
        
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        Map<String, Object> statusMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if(id!=0) para.put("id", id);
            if(channelId!=0) para.put("channelId", channelId);
            if(wareHouseId!=0) para.put("wareHouseId", wareHouseId);
            if(StringUtils.isNotEmpty(productCode)) para.put("productCode", productCode);
            if(StringUtils.isNotEmpty(productName)) para.put("productName", productName);
            
            List<Map<String, Object>> resultMap = (List<Map<String, Object>>) channelProductManageService.jsonChannelProInfo(para).get("rows");
            
            String[] displayHeaders = new String[]{"第三方商品ID", "渠道名", "仓库名", "商品编码", "商品名称", "组合销售件数"};
//            List<String> headers = Lists.newArrayList("id", "channelName", "wareHouseName", "produceCode", "produceName", "assembleCount");
            workbook = new HSSFWorkbook();;
            HSSFSheet sheet = workbook.createSheet("商品管理");
            Row row = sheet.createRow(0);
            for (int i = 0; i < displayHeaders.length; i++)
            {
                sheet.setColumnWidth(i, 5500);
                Cell cell = row.createCell(i);
                cell.setCellValue(displayHeaders[i]);
            }
            for (int i = 0; i < resultMap.size(); i++)
            {
                Map<String, Object> map = (Map<String, Object>)resultMap.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(map.get("id") + "");
                r.createCell(1).setCellValue(map.get("channelName") + "");
                r.createCell(2).setCellValue(map.get("wareHouseName") + "");
                r.createCell(3).setCellValue(map.get("productCode") + "");
                r.createCell(4).setCellValue(map.get("productName") + "");
                r.createCell(5).setCellValue(map.get("assembleCount") + "");
            }
            String codedFileName = "商品管理";
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            String errorStr = "<script>alert('无数据');window.history.back();</script>";
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "");
            if (fOut == null)
                fOut = response.getOutputStream();
            fOut.write(errorStr.getBytes("utf-8"));
        }finally{
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
