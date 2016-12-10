package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.service.AnalyzeService;
import com.ygg.admin.service.FinanceSerivce;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.Excel.ExcelMaker;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.view.UserBehaviorView;
import com.ygg.admin.util.CommonEnum;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analyze")
public class AnalyzeController
{
    @Resource
    AnalyzeService analyzeService;
    
    @Resource
    private FinanceSerivce financeSerivce;
    
    private static Logger logger = Logger.getLogger(AnalyzeController.class);
    
    /**
     * 商家统计
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/seller")
    @ControllerLog(description = "数据统计-商家统计")
    public ModelAndView seller(HttpServletRequest request, @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("analyze/seller");
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Map<String, Object> lastRow = new HashMap<String, Object>();
        if ((isSearch == 1) && (!"".equals(selectDate)) && (start != 0) && (stop != 0) && (start <= stop))
        {
            DateTime dt = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            int maxDay = dt.plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;
            String e = "" + stop;
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            Map<String, Object> result = analyzeService.sellerDataCustom(para);
            rows = (List<Map<String, Object>>)result.get("rows");
            lastRow = (Map<String, Object>)result.get("lastRow");
        }
        else
        {
            selectDate = DateTime.now().toString("yyyy-MM");
            start = DateTime.now().getDayOfMonth();
            stop = DateTime.now().getDayOfMonth();
        }
        mv.addObject("selectDate", selectDate);
        mv.addObject("rows", rows);
        mv.addObject("lastRow", lastRow);
        mv.addObject("start", start);
        mv.addObject("stop", stop);
        if (start <= stop)
        {
            for (int i = start; i <= stop; i++)
            {
                mv.addObject("num" + i, 1);
            }
        }
        List<Integer> dateList = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++)
        {
            dateList.add(i);
        }
        mv.addObject("dateList", dateList);
        return mv;
    }
    
    /**
     * 导出商家统计
     */
    @RequestMapping(value = "/exportSellerResult")
    @ControllerLog(description = "数据统计-导出商家统计")
    public void exportSellerResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
            Map<String, Object> lastRow = new HashMap<String, Object>();
            if ((isSearch == 1) && (!"".equals(selectDate)) && (start != 0) && (stop != 0) && (start <= stop))
            {
                DateTime dt = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                int maxDay = dt.plusMonths(1).plusDays(-1).getDayOfMonth();
                Map<String, Object> para = new HashMap<String, Object>();
                DateTime begin = null;
                DateTime end = null;
                String s = "" + start;
                String e = "" + stop;
                if (start < 10)
                {
                    s = "0" + start;
                }
                if (stop < 10)
                {
                    e = "0" + stop;
                }
                else if (stop > maxDay)
                {
                    e = "" + maxDay;
                }
                
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
                String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
                String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
                para.put("payTimeBegin", payTimeBegin);
                para.put("payTimeEnd", payTimeEnd);
                Map<String, Object> result = analyzeService.sellerDataCustom(para);
                rows = (List<Map<String, Object>>)result.get("rows");
                lastRow = (Map<String, Object>)result.get("lastRow");
            }
            else
            {
                selectDate = DateTime.now().toString("yyyy-MM");
                start = DateTime.now().getDayOfMonth();
                stop = DateTime.now().getDayOfMonth();
            }
            workbook = new HSSFWorkbook();
            writeSellerSheet(workbook, rows, "num", lastRow);
            writeSellerSheet(workbook, rows, "tp", lastRow);
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "商家统计结果";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    private void writeSellerSheet(HSSFWorkbook workbook, List<Map<String, Object>> rows, String type, Map<String, Object> lastRow)
    {
        String name = "num".equals(type) ? "订单数量" : "订单金额";
        String count = "num".equals(type) ? "Num" : "Price";
        HSSFSheet sheet = workbook.createSheet(name);
        String[] str = {"商家", "发货地"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < str.length; i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue(str[i]);
        }
        for (int i = 1; i <= 31; i++)
        {
            Cell cell = row.createCell(i + 1);
            cell.setCellValue(i + "日");
        }
        Cell cell = row.createCell(33);
        cell.setCellValue("合计");
        
        for (int i = 0; i < rows.size(); i++)
        {
            Row r = sheet.createRow(i + 1);
            Map<String, Object> om = rows.get(i);
            r.createCell(0).setCellValue(om.get("sellerName") + "");
            r.createCell(1).setCellValue(om.get("sendAddress") + "");
            for (int j = 1; j <= 31; j++)
            {
                Object o = om.get(type + j);
                if (o != null)
                {
                    r.createCell(j + 1).setCellValue(new BigDecimal(o.toString()).doubleValue());
                }
                else
                {
                    r.createCell(j + 1).setCellValue("");
                }
            }
            r.createCell(33).setCellValue(new BigDecimal(om.get("total" + count) + "").doubleValue());
        }
        
        // 插入统计
        Row r = sheet.createRow(rows.size() + 1);
        r.createCell(0).setCellValue("合计");
        r.createCell(1).setCellValue("");
        for (int j = 1; j <= 31; j++)
        {
            Object o = lastRow.get(type + j);
            if (o != null)
            {
                r.createCell(j + 1).setCellValue(new BigDecimal(o.toString()).doubleValue());
            }
            else
            {
                r.createCell(j + 1).setCellValue("");
            }
        }
        
    }
    
    /**
     * 商品统计
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/product")
    @ControllerLog(description = "数据统计-商品统计")
    public ModelAndView product(HttpServletRequest request,//
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch, @RequestParam(value = "type", required = false, defaultValue = "0") int type)
        // 0：数量统计，1：金额统计
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        if (type == 0)
        {
            mv.setViewName("analyze/productNum");
        }
        else
        {
            mv.setViewName("analyze/productMoney");
        }
        
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Map<String, Object> lastRow = new HashMap<String, Object>();
        if ((isSearch == 1) && (!"".equals(selectDate)) && (start != 0) && (stop != 0) && (start <= stop))
        {
            DateTime dt = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            int maxDay = dt.plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;
            String e = "" + stop;
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            Map<String, Object> result = analyzeService.productDataCustom(para);
            rows = (List<Map<String, Object>>)result.get("rows");
            lastRow = (Map<String, Object>)result.get("lastRow");
        }
        else
        {
            selectDate = DateTime.now().toString("yyyy-MM");
            start = DateTime.now().getDayOfMonth();
            stop = DateTime.now().getDayOfMonth();
        }
        mv.addObject("selectDate", selectDate);
        mv.addObject("rows", rows);
        mv.addObject("lastRow", lastRow);
        mv.addObject("start", start);
        mv.addObject("stop", stop);
        if (start <= stop)
        {
            for (int i = start; i <= stop; i++)
            {
                mv.addObject("num" + i, 1);
            }
        }
        List<Integer> dateList = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++)
        {
            dateList.add(i);
        }
        mv.addObject("dateList", dateList);
        return mv;
    }
    
    /**
     * 导出商品统计
     */
    @RequestMapping(value = "/exportProductResult")
    @ControllerLog(description = "数据统计-导出商品统计")
    public void exportProductResult(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            DateTime dt = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            int maxDay = dt.plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;
            String e = "" + stop;
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            Map<String, Object> result = analyzeService.productDataCustom(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            Map<String, Object> lastRow = (Map<String, Object>)result.get("lastRow");
            workbook = new HSSFWorkbook();
            writeProductSheet(workbook, rows, "num", lastRow);
            writeProductSheet(workbook, rows, "tp", lastRow);
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "商品统计结果";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    private void writeProductSheet(HSSFWorkbook workbook, List<Map<String, Object>> rows, String type, Map<String, Object> lastRow)
    {
        String name = "num".equals(type) ? "销售数量" : "销售金额";
        String count = "num".equals(type) ? "Num" : "Price";
        HSSFSheet sheet = workbook.createSheet(name);
        String[] str = {"商品ID", "基本商品ID", "商品类型", "品牌", "一级分类", "编码", "短名", "商家", "发货地"};
        Row row = sheet.createRow(0);
        for (int i = 0; i < str.length; i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue(str[i]);
        }
        for (int i = 1; i <= 31; i++)
        {
            Cell cell = row.createCell(i + 8);
            cell.setCellValue(i + "日");
        }
        Cell cell = row.createCell(40);
        cell.setCellValue("合计");
        for (int i = 0; i < rows.size(); i++)
        {
            Row r = sheet.createRow(i + 1);
            Map<String, Object> om = rows.get(i);
            r.createCell(0).setCellValue(om.get("productId") + "");
            r.createCell(1).setCellValue(om.get("productBaseId") + "");
            r.createCell(2).setCellValue(om.get("productType") + "");
            r.createCell(3).setCellValue(om.get("brandName") + "");
            r.createCell(4).setCellValue(om.get("categoryName") + "");
            r.createCell(5).setCellValue(om.get("code") + "");
            r.createCell(6).setCellValue(om.get("shortName") + "");
            r.createCell(7).setCellValue(om.get("sellerName") + "");
            r.createCell(8).setCellValue(om.get("sendAddress") + "");
            for (int j = 1; j <= 31; j++)
            {
                Object o = om.get(type + j);
                if (o != null)
                {
                    r.createCell(j + 8).setCellValue(new BigDecimal(o.toString()).doubleValue());
                }
                else
                {
                    r.createCell(j + 8).setCellValue("");
                }
            }
            r.createCell(40).setCellValue(new BigDecimal(om.get("total" + count) + "").doubleValue());
        }
        
        // 插入统计
        Row r = sheet.createRow(rows.size() + 1);
        r.createCell(0).setCellValue("合计");
        r.createCell(1).setCellValue("");
        r.createCell(2).setCellValue("");
        r.createCell(3).setCellValue("");
        r.createCell(4).setCellValue("");
        r.createCell(5).setCellValue("");
        r.createCell(6).setCellValue("");
        r.createCell(7).setCellValue("");
        r.createCell(8).setCellValue("");
        for (int j = 1; j <= 31; j++)
        {
            Object o = lastRow.get(type + j);
            if (o != null)
            {
                r.createCell(j + 8).setCellValue(new BigDecimal(o.toString()).doubleValue());
            }
            else
            {
                r.createCell(j + 8).setCellValue("");
            }
        }
        
    }
    
    /**
     * 左岸城堡
月度数据统计
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/monthAnalyze")
    @ControllerLog(description = "数据统计-左岸城堡月度数据统计")
    public ModelAndView monthAnalyze(HttpServletRequest request, @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView("analyze/monthAnalyze");
        Map<String, Object> para = new HashMap<>();
        DateTime begin = null;
        if (!"".equals(selectDate))
        {
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }
        else
        {
            begin = DateTime.now();
            selectDate = DateTime.now().toString("yyyy-MM");
        }
        String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
        String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
        para.put("payTimeBegin", payTimeBegin);
        para.put("payTimeEnd", payTimeEnd);
        para.put("selectDate", selectDate);
        Map<String, Object> rowsInfo = analyzeService.monthAnalyze(para);
        mv.addObject("rows", rowsInfo.get("rows"));
        mv.addObject("lastRow", rowsInfo.get("lastRow"));
        mv.addObject("selectDate", selectDate);
        return mv;
    }


    /**
     * 全平台月度销售统计
     */
    @RequestMapping("/platformMonthAnalyze")
    @ControllerLog(description = "数据统计-全平台月度数据统计")
    public ModelAndView platformMonthAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
            throws Exception
    {
        ModelAndView mv = new ModelAndView("analyze/platformMonthAnalyze");
        Map<String, Object> para = new HashMap<>();
        DateTime begin ;
        if (!"".equals(selectDate))
        {
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }
        else
        {
            begin = DateTime.now();
            selectDate = DateTime.now().toString("yyyy-MM");
        }
        String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
        String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
        para.put("payTimeBegin", payTimeBegin);
        para.put("payTimeEnd", payTimeEnd);
        para.put("selectDate", selectDate);
        Map<String, Object> rowsInfo = analyzeService.platformMonthAnalyze(para);
        mv.addObject("rows", rowsInfo.get("rows"));
        mv.addObject("selectDate", selectDate);
        return mv;
    }


    
    /**
     * 导出月度统计
     */
    @RequestMapping(value = "/exportMonthAnalyze")
    @ControllerLog(description = "数据统计-导出月度统计")
    public void exportMonthAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            Map<String, Object> resultList = analyzeService.monthAnalyze(para);
            List<Object> rows = (List<Object>)resultList.get("rows");
            Map<String, Object> lastRow = (Map<String, Object>)resultList.get("lastRow");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("月度统计");
            String[] str = {"日期", "创建订单数", "支付订单数", "支付成功率", "订单实付金额", "笔单价", "成交人数（已去重）", "客单价"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < rows.size(); i++)
            {
                Map<String, Object> om = (Map<String, Object>)rows.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("dateStr") + "");
                r.createCell(1).setCellValue(om.get("totalOrderCount") + "");
                r.createCell(2).setCellValue(om.get("payOrderCount") + "");
                r.createCell(3).setCellValue(om.get("divPayOrderCount") + "");
                r.createCell(4).setCellValue(om.get("totalPrice") + "");
                r.createCell(5).setCellValue(om.get("divOrderCountPrice") + "");
                r.createCell(6).setCellValue(om.get("totalPersonCount") + "");
                r.createCell(7).setCellValue(om.get("divPersonCountPrice") + "");
            }
            Row r = sheet.createRow(rows.size() + 1);
            r.createCell(0).setCellValue("总计");
            r.createCell(1).setCellValue(lastRow.get("totalOrderCount") + "");
            r.createCell(2).setCellValue(lastRow.get("payOrderCount") + "");
            r.createCell(3).setCellValue(lastRow.get("divPayOrderCount") + "");
            r.createCell(4).setCellValue(lastRow.get("totalPrice") + "");
            r.createCell(5).setCellValue(lastRow.get("divOrderCountPrice") + "");
            r.createCell(6).setCellValue(lastRow.get("totalPersonCount") + "");
            r.createCell(7).setCellValue(lastRow.get("divPersonCountPrice") + "");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "月度统计结果";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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


    /**
     * 导出月度统计
     */
    @RequestMapping(value = "/exportPlatformMonthAnalyze")
    @ControllerLog(description = "数据统计-导出全平台月度销量统计")
    public void exportPlatformMonthAnalyze(HttpServletResponse response,
                                   @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    ) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        // 进行转码，使其支持中文文件名
        String name = URLEncoder.encode("全平台月度统计结果","utf-8");
        response.setHeader("content-disposition", "attachment;filename=" + name + ".xls");
        try(OutputStream fOut = response.getOutputStream())
        {
            Map<String, Object> para = new HashMap<>();
            DateTime begin;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            Map<String, Object> resultList = analyzeService.platformMonthAnalyze(para);
            List<Object> rows = (List<Object>)resultList.get("rows");
            List displayHeader = Lists.newArrayList("日期", "左岸城堡支付订单数", "左岸城堡销售额", "左岸城堡成功订单数",
                    "左岸城堡销售额", "行动派支付订单数", "行动派销售额", "合计订单数","合计销售额");
            List header = Lists.newArrayList(
                    "dateStr","ggjPayOrderCount","ggjTotalPrice","ggtPayOrderCount",
                "ggtTotalPrice","qqbsPayOrderCount","qqbsTotalPrice","payOrderCount","totalPrice"
            );
            ExcelMaker.from(rows,header).displayHeaders(displayHeader).writeTo(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * 特卖销售数据
     * 
     * 档期特卖（包括单品和组合）的销售数量、金额、库存等情况
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/todaySale")
    @ControllerLog(description = "数据统计-特卖销售数据")
    public ModelAndView todaySale(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,//
        @RequestParam(value = "searchType", required = false, defaultValue = "1") int searchType)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("analyze/todayAnalyze");
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        para.put("searchType", searchType);
        List<Map<String, Object>> resultRows = analyzeService.todayAnalyze(para);
        // System.out.println("今日数据结果：" + resultRows);
        mv.addObject("rows", resultRows);
        mv.addObject("searchType", searchType);
        return mv;
    }
    
    /**
     * 导出特卖销售数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportTodaySale")
    @ControllerLog(description = "数据统计-导出特卖销售数据")
    public void exportTodaySale(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "searchType", required = false, defaultValue = "1") int searchType)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("searchType", searchType);
            List<Map<String, Object>> resultRows = analyzeService.todayAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("注册用户统计");
            String[] str = {"名称", "类型", "销售数量", "总金额", "剩余库存"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < resultRows.size(); i++)
            {
                Map<String, Object> om = resultRows.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("name") + "");
                r.createCell(1).setCellValue(om.get("source") + "");
                r.createCell(2).setCellValue(om.get("saleTotalCount") + "");
                r.createCell(3).setCellValue(om.get("saleToalPrice") + "");
                r.createCell(4).setCellValue(om.get("totalStock") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "特卖销售数据统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    /**
     * 详细信息 今日在售组合特卖信息售卖情况
     * 
     * @param request
     * @param type 1：banner；2：特卖 ;
     * @param id bannerWindow.id or saleWindow.id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/todaySaleDetail")
    @ControllerLog(description = "数据统计-今日在售组合特卖信息售卖情况-详细信息")
    public ModelAndView todaySaleDetail(HttpServletRequest request, @RequestParam(value = "type", required = true) int type, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("analyze/todayAnalyze");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("type", type);
        para.put("id", id);
        List<Map<String, Object>> resultRows = analyzeService.todayAnalyzeDetail(type, id);
        mv.addObject("rows", resultRows);
        return mv;
    }
    
    /**
     * 用户注册分析
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/registAnalyze")
    @ControllerLog(description = "数据统计-用户注册分析")
    public ModelAndView registAnalyze(HttpServletRequest request,//
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "channel", required = false, defaultValue = "") String channel
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("analyze/registAnalyze");
        if ("".equals(selectDate))
        {
            selectDate = DateTime.now().toString("yyyy-MM");
        }
        Map<String, Object> result = analyzeService.registAnalyze(selectDate, channel);
        mv.addObject("selectDate", selectDate);
        mv.addObject("rows", result.get("rows"));
        mv.addObject("lastRow", result.get("lastRow"));
        List<Map<String, String>> channelList = new ArrayList<>();
        for (CommonEnum.OrderAppChannelEnum anEnum : CommonEnum.OrderAppChannelEnum.values())
        {
            Map<String, String> map = new HashMap<>();
            map.put("channel", anEnum.ordinal() + "");
            map.put("name", anEnum.getDescription());
            if (!"".equals(channel) && channel.equals(anEnum.ordinal() + ""))
            {
                map.put("selected", "1");
            }
            else
            {
                map.put("selected", "0");
            }
            channelList.add(map);
        }
        mv.addObject("channelList", channelList);
        return mv;
    }
    
    /**
     * 导出注册用户统计
     */
    @RequestMapping(value = "/exportRegistAnalyze")
    @ControllerLog(description = "数据统计-导出注册用户统计")
    public void exportRegistAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "channel", required = false, defaultValue = "") String channel
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            Map<String, Object> result = analyzeService.registAnalyze(selectDate, channel);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            Map<String, Object> lastRow = (Map<String, Object>)result.get("lastRow");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("注册用户统计");
            String[] str = {"日期", "新注册用户数", "手机用户", "微信用户", "QQ用户", "微博用户", "支付宝用户"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < rows.size(); i++)
            {
                Map<String, Object> om = rows.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("dateStr") + "");
                r.createCell(1).setCellValue(om.get("total") + "");
                r.createCell(2).setCellValue(om.get("mobile") + "");
                r.createCell(3).setCellValue(om.get("weixin") + "");
                r.createCell(4).setCellValue(om.get("qq") + "");
                r.createCell(5).setCellValue(om.get("weibo") + "");
                r.createCell(6).setCellValue(om.get("alipay") + "");
            }
            Row lastR = sheet.createRow(rows.size() + 1);
            lastR.createCell(0).setCellValue("总计");
            lastR.createCell(1).setCellValue(lastRow.get("total") + "");
            lastR.createCell(2).setCellValue(lastRow.get("mobile") + "");
            lastR.createCell(3).setCellValue(lastRow.get("weixin") + "");
            lastR.createCell(4).setCellValue(lastRow.get("qq") + "");
            lastR.createCell(5).setCellValue(lastRow.get("weibo") + "");
            lastR.createCell(6).setCellValue(lastRow.get("alipay") + "");
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "注册用户统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    /**
     * 新用户成交统计
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/newAccountBuyingAnalyze")
    @ControllerLog(description = "数据统计-新用户成交统计")
    public ModelAndView newAccountBuyingAnalyze(HttpServletRequest request,//
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("analyze/newAccountBuyingAnalyze");
        Map<String, Object> para = new HashMap<String, Object>();
        DateTime begin = null;
        if (!"".equals(selectDate))
        {
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }
        else
        {
            begin = DateTime.now();
            selectDate = DateTime.now().toString("yyyy-MM");
        }
        String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
        String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
        para.put("payTimeBegin", payTimeBegin);
        para.put("payTimeEnd", payTimeEnd);
        para.put("selectDate", selectDate);
        List<Object> result = analyzeService.newAccountBuyingAnalyze(para);
        mv.addObject("selectDate", selectDate);
        mv.addObject("rows", result);
        return mv;
    }
    
    /**
     * 导出新用户成交统计
     */
    @RequestMapping(value = "/exportNewAccountBuyingAnalyze")
    @ControllerLog(description = "数据统计-导出注册用户统计")
    public void exportNewAccountBuyingAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Object> rows = analyzeService.newAccountBuyingAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("新用户成交统计");
            String[] str = {"日期", "成交人数", "其中首次成交用户人数", "首次成交用户人数占比", "成交金额", "其中首次成交用户金额", "首次成交用户金额占比"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < rows.size(); i++)
            {
                Map<String, Object> om = (Map<String, Object>)rows.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("dateStr") + "");
                r.createCell(1).setCellValue(om.get("totalPersonCount") + "");
                r.createCell(2).setCellValue(om.get("newPersonCount") + "");
                r.createCell(3).setCellValue(om.get("newPersonCountDivtotalPersonCount") + "");
                r.createCell(4).setCellValue(om.get("totalPrice") + "");
                r.createCell(5).setCellValue(om.get("newPersonTotalPrice") + "");
                r.createCell(6).setCellValue(om.get("newPersonTotalPriceDivtotalPrice") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "新用户成交统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    /**
     * 今日销售top
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/todaySaleTop")
    @ControllerLog(description = "数据统计-今日销售top")
    public ModelAndView todaySaleTop(HttpServletRequest request,//
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-07-01
    )
        throws Exception
    {
        // selectDate = "2015-05-22";
        ModelAndView mv = new ModelAndView("analyze/todaySaleTopAnalyze");
        Map<String, Object> para = new HashMap<String, Object>();
        DateTime begin = null;
        if (!"".equals(selectDate))
        {
            begin = new DateTime(CommonUtil.string2Date(selectDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }
        else
        {
            begin = DateTime.now();
            selectDate = DateTime.now().toString("yyyy-MM-dd");
        }
        String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
        String payTimeEnd = begin.plusDays(1).toString("yyyy-MM-dd 00:00:00");
        para.put("payTimeBegin", payTimeBegin);
        para.put("payTimeEnd", payTimeEnd);
        // 获得商品销售排行榜
        List<Map<String, Object>> rowsInfo = analyzeService.todaySaleTop(para);
        mv.addObject("rows", rowsInfo);
        // 获得折线图数据
        Map<String, Object> data = analyzeService.saleLineData(begin);
        List<Double> yesterdaySaleData = (List<Double>)data.get("yesterdaySaleData");
        List<Double> nowSaleData = (List<Double>)data.get("nowSaleData");
        double nowTotal = Double.parseDouble(data.get("nowTotal") + "");
        double yesterdayTotal = Double.parseDouble(data.get("yesterdayTotal") + "");
        mv.addObject("yesterdaySaleData", JSON.toJSONString(yesterdaySaleData));
        mv.addObject("nowSaleData", JSON.toJSONString(nowSaleData));
        mv.addObject("nowTotal", JSON.toJSONString(nowTotal));
        mv.addObject("yesterdayTotal", JSON.toJSONString(yesterdayTotal));
        mv.addObject("now", begin.toString("yyyy-MM-dd"));
        mv.addObject("prev", begin.plusDays(-1).toString("yyyy-MM-dd"));
        mv.addObject("next", begin.plusDays(1).toString("yyyy-MM-dd"));
        return mv;
    }
    
    @RequestMapping(value = "/exportTodaySaleTop")
    @ControllerLog(description = "数据统计-导出今日销售排行榜")
    public void exportTodaySaleTop(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-07-01
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM-dd");
            }
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = begin.plusDays(1).toString("yyyy-MM-dd 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            List<Map<String, Object>> rowsInfo = analyzeService.todaySaleTop(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("今日销售排行榜");
            String[] str = {"排序", "商品ID", "商品类型", "商品名称", "销售数量", "销售金额", "剩余库存"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < rowsInfo.size(); i++)
            {
                Map<String, Object> om = (Map<String, Object>)rowsInfo.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("index") + "");
                r.createCell(1).setCellValue(om.get("productId") + "");
                r.createCell(2).setCellValue(om.get("type") + "");
                r.createCell(3).setCellValue(om.get("name") + "");
                r.createCell(4).setCellValue(om.get("totalCount") + "");
                r.createCell(5).setCellValue(om.get("totalSalePrice") + "");
                r.createCell(6).setCellValue(om.get("stock") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "今日销售排行榜";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    /**
     * 客户端成交统计
     * @param selectDate：日期，格式yyyy-MM
     * @return
     */
    @RequestMapping("/clientBuyAnalyze")
    @ControllerLog(description = "数据统计-客户端成交统计")
    public ModelAndView clientBuyAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/clientBuyAnalyze");
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            Map<String, Object> result = analyzeService.clientBuyAnalyze(selectDate);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result.get("rows"));
            mv.addObject("lastRow", result.get("lastRow"));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/exportClientBuyAnalyze")
    @ControllerLog(description = "数据统计-导出客户端成交统计")
    public void exportClientBuyAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            Map<String, Object> result = analyzeService.clientBuyAnalyze(selectDate);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            Map<String, Object> lastRow = (Map<String, Object>)result.get("lastRow");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("客户端成交统计");
            String[] str = {"日期", "总成交金额", "IOS成交", "IOS占比", "Android成交", "Android占比", "Web成交", "Web占比"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < rows.size(); i++)
            {
                Map<String, Object> om = rows.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("dateStr") + "");
                r.createCell(1).setCellValue(om.get("total") + "");
                r.createCell(2).setCellValue(om.get("ios") + "");
                r.createCell(3).setCellValue(om.get("iosPercent") + "");
                r.createCell(4).setCellValue(om.get("android") + "");
                r.createCell(5).setCellValue(om.get("androidPercent") + "");
                r.createCell(6).setCellValue(om.get("web") + "");
                r.createCell(7).setCellValue(om.get("webPercent") + "");
            }
            Row lastR = sheet.createRow(rows.size() + 1);
            lastR.createCell(0).setCellValue("总计");
            lastR.createCell(1).setCellValue(lastRow.get("total") + "");
            lastR.createCell(2).setCellValue(lastRow.get("ios") + "");
            lastR.createCell(3).setCellValue(lastRow.get("iosPercent") + "");
            lastR.createCell(4).setCellValue(lastRow.get("android") + "");
            lastR.createCell(5).setCellValue(lastRow.get("androidPercent") + "");
            lastR.createCell(6).setCellValue(lastRow.get("web") + "");
            lastR.createCell(7).setCellValue(lastRow.get("webPercent") + "");
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "客户端成交统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
    
    /**
     * 客户端成交明细
     * @param selectDate：格式yyyy-MM-dd
     * @return
     */
    @RequestMapping("/clientBuyDetailAnalyze/{selectDate}")
    @ControllerLog(description = "数据统计-客户端成交明细")
    public ModelAndView clientBuyDetailAnalyze(@PathVariable(value = "selectDate") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/clientBuyDetailAnalyze");
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            List<Map<String, Object>> result = analyzeService.clientBuyDetailAnalyze(selectDate);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 用户成交行为统计
     * @return
     */
    @RequestMapping("/userBehaviorAnalyze")
    @ControllerLog(description = "数据统计-用户成交行为统计")
    public ModelAndView userBehaviorAnalyze(@RequestParam(value = "prevStartTime", required = false, defaultValue = "") String prevStartTime,
        @RequestParam(value = "prevEndTime", required = false, defaultValue = "") String prevEndTime,
        @RequestParam(value = "nextStartTime", required = false, defaultValue = "") String nextStartTime,
        @RequestParam(value = "nextEndTime", required = false, defaultValue = "") String nextEndTime)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            // 当前月2015-08-03
            DateTime currentDate = DateTime.now();
            mv.setViewName("analyze/userBehaviorAnalyze");
            if ("".equals(prevStartTime))
            {
                // 2015-06-01
                prevStartTime = currentDate.minusMonths(2).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(prevEndTime))
            {
                // 2015-06-30
                prevEndTime = currentDate.minusMonths(1).withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextStartTime))
            {
                // 2015-07-01
                nextStartTime = currentDate.minusMonths(1).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextEndTime))
            {
                // 2015-07-31
                nextEndTime = currentDate.withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            Map<String, String> para = new HashMap<String, String>();
            para.put("prevStartTime", prevStartTime);
            para.put("prevEndTime", prevEndTime);
            para.put("nextStartTime", nextStartTime);
            para.put("nextEndTime", nextEndTime);
            List<UserBehaviorView> result = analyzeService.userBehaviorAnalyze(para);
            
            mv.addObject("rows", result);
            mv.addObject("prevStartTime", prevStartTime);
            mv.addObject("prevEndTime", prevEndTime);
            mv.addObject("nextStartTime", nextStartTime);
            mv.addObject("nextEndTime", nextEndTime);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/exportUserBehaviorAnalyze")
    @ControllerLog(description = "数据统计-导出用户购买行为分析统计")
    public void exportUserBehaviorAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "prevStartTime", required = false, defaultValue = "") String prevStartTime,
        @RequestParam(value = "prevEndTime", required = false, defaultValue = "") String prevEndTime,
        @RequestParam(value = "nextStartTime", required = false, defaultValue = "") String nextStartTime,
        @RequestParam(value = "nextEndTime", required = false, defaultValue = "") String nextEndTime)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            // 当前月2015-08-03
            DateTime currentDate = DateTime.now();
            if ("".equals(prevStartTime))
            {
                // 2015-06-01
                prevStartTime = currentDate.minusMonths(2).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(prevEndTime))
            {
                // 2015-06-30
                prevEndTime = currentDate.minusMonths(1).withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextStartTime))
            {
                // 2015-07-01
                nextStartTime = currentDate.minusMonths(1).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextEndTime))
            {
                // 2015-07-31
                nextEndTime = currentDate.withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            Map<String, String> para = new HashMap<String, String>();
            para.put("prevStartTime", prevStartTime);
            para.put("prevEndTime", prevEndTime);
            para.put("nextStartTime", nextStartTime);
            para.put("nextEndTime", nextEndTime);
            List<UserBehaviorView> result = analyzeService.userBehaviorAnalyze(para);
            
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("用户购买行为分析统计");
            String[] str = {"购物次数", prevStartTime + "~" + prevEndTime, "人数占比", "总成交额", "金额占比", "人均成交金额", nextStartTime + "~" + nextEndTime, "总成交额"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                UserBehaviorView uv = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(uv.getTimes() + "");
                r.createCell(1).setCellValue(uv.getUserCount());
                r.createCell(2).setCellValue(uv.getUserCountPercent());
                r.createCell(3).setCellValue(uv.getTotalAmount());
                r.createCell(4).setCellValue(uv.getTotalAmountPercent());
                r.createCell(5).setCellValue(uv.getAveragePrice());
                r.createCell(6).setCellValue(uv.getNextUserCount());
                r.createCell(7).setCellValue(uv.getNextTotalAmount());
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "用户购买行为分析统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("导出用户购买行为分析统计出错了！！！", e);
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
    
    /**
     * 用户首次购买
     * @param prevStartTime
     * @param prevEndTime
     * @param nextStartTime
     * @param nextEndTime
     * @return
     */
    @RequestMapping("/userFirstBehaviorAnalyze")
    @ControllerLog(description = "数据统计-用户首次购买")
    public ModelAndView userFirstBehaviorAnalyze(@RequestParam(value = "prevStartTime", required = false, defaultValue = "") String prevStartTime,
        @RequestParam(value = "prevEndTime", required = false, defaultValue = "") String prevEndTime,
        @RequestParam(value = "nextStartTime", required = false, defaultValue = "") String nextStartTime,
        @RequestParam(value = "nextEndTime", required = false, defaultValue = "") String nextEndTime)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            // 当前月2015-08-03
            DateTime currentDate = DateTime.now();
            mv.setViewName("analyze/userFirstBehaviorAnalyze");
            if ("".equals(prevStartTime))
            {
                // 2015-06-01
                prevStartTime = currentDate.minusMonths(2).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(prevEndTime))
            {
                // 2015-06-30
                prevEndTime = currentDate.minusMonths(1).withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextStartTime))
            {
                // 2015-07-01
                nextStartTime = currentDate.minusMonths(1).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextEndTime))
            {
                // 2015-07-31
                nextEndTime = currentDate.withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            Map<String, String> para = new HashMap<String, String>();
            para.put("prevStartTime", prevStartTime);
            para.put("prevEndTime", prevEndTime);
            para.put("nextStartTime", nextStartTime);
            para.put("nextEndTime", nextEndTime);
            List<UserBehaviorView> result = analyzeService.userFirstBehaviorAnalyze(para);
            
            mv.addObject("rows", result);
            mv.addObject("prevStartTime", prevStartTime);
            mv.addObject("prevEndTime", prevEndTime);
            mv.addObject("nextStartTime", nextStartTime);
            mv.addObject("nextEndTime", nextEndTime);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/exportUserFirstBehaviorAnalyze")
    @ControllerLog(description = "数据统计-导出用户首次购买行为分析统计")
    public void exportUserFirstBehaviorAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "prevStartTime", required = false, defaultValue = "") String prevStartTime,
        @RequestParam(value = "prevEndTime", required = false, defaultValue = "") String prevEndTime,
        @RequestParam(value = "nextStartTime", required = false, defaultValue = "") String nextStartTime,
        @RequestParam(value = "nextEndTime", required = false, defaultValue = "") String nextEndTime)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            // 当前月2015-08-03
            DateTime currentDate = DateTime.now();
            if ("".equals(prevStartTime))
            {
                // 2015-06-01
                prevStartTime = currentDate.minusMonths(2).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(prevEndTime))
            {
                // 2015-06-30
                prevEndTime = currentDate.minusMonths(1).withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextStartTime))
            {
                // 2015-07-01
                nextStartTime = currentDate.minusMonths(1).withDayOfMonth(1).toString("yyyy-MM-dd");
            }
            if ("".equals(nextEndTime))
            {
                // 2015-07-31
                nextEndTime = currentDate.withDayOfMonth(1).minusDays(1).toString("yyyy-MM-dd");
            }
            Map<String, String> para = new HashMap<String, String>();
            para.put("prevStartTime", prevStartTime);
            para.put("prevEndTime", prevEndTime);
            para.put("nextStartTime", nextStartTime);
            para.put("nextEndTime", nextEndTime);
            List<UserBehaviorView> result = analyzeService.userBehaviorAnalyze(para);
            
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("用户首次购买行为分析统计");
            String[] str = {"购物次数", prevStartTime + "~" + prevEndTime, "人数占比", "总成交额", "金额占比", "人均成交金额", nextStartTime + "~" + nextEndTime, "总成交额"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                UserBehaviorView uv = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(uv.getTimes() + "");
                r.createCell(1).setCellValue(uv.getUserCount());
                r.createCell(2).setCellValue(uv.getUserCountPercent());
                r.createCell(3).setCellValue(uv.getTotalAmount());
                r.createCell(4).setCellValue(uv.getTotalAmountPercent());
                r.createCell(5).setCellValue(uv.getAveragePrice());
                r.createCell(6).setCellValue(uv.getNextUserCount());
                r.createCell(7).setCellValue(uv.getNextTotalAmount());
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "用户首次购买行为分析统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("导出用户购买行为分析统计出错了！！！", e);
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
    
    @RequestMapping("/userBehaviorDetailAnalyze")
    public ModelAndView userBehaviorDetailAnalyze(@RequestParam(value = "times", required = true) int times, @RequestParam(value = "userCount", required = true) int userCount,
        @RequestParam(value = "prevStartTime", required = true) String prevStartTime, @RequestParam(value = "prevEndTime", required = true) String prevEndTime,
        @RequestParam(value = "nextStartTime", required = true) String nextStartTime, @RequestParam(value = "nextEndTime", required = true) String nextEndTime)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            
            mv.setViewName("analyze/userBehaviorDetailAnalyze");
            Map<String, String> para = new HashMap<String, String>();
            para.put("prevStartTime", prevStartTime);
            para.put("prevEndTime", prevEndTime);
            para.put("nextStartTime", nextStartTime);
            para.put("nextEndTime", nextEndTime);
            para.put("times", times + "");
            
            List<UserBehaviorView> result = analyzeService.userBehaviorDetailAnalyze(para);
            mv.addObject("rows", result);
            mv.addObject("prevStartTime", prevStartTime);
            mv.addObject("prevEndTime", prevEndTime);
            mv.addObject("nextStartTime", nextStartTime);
            mv.addObject("nextEndTime", nextEndTime);
            mv.addObject("times", times + "");
            mv.addObject("userCount", userCount + "");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 临时导出销售数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportTodaySaleTopTemp")
    @ControllerLog(description = "数据统计-导出商品销售数据")
    public void exportTodaySaleTopTemp(HttpServletRequest request,//
        HttpServletResponse response)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "商品销售数据";
        OutputStream fOut = null;
        Workbook workbook = null;
        // 进行转码，使其支持中文文件名
        codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
        // 产生工作簿对象
        String[] title = {"排序", "商品ID", "商品类型", "商品名称", "销售数量", "销售金额"};
        workbook = POIUtil.createXSSFWorkbookTemplate(title);
        
        Map<String, Object> para = new HashMap<>();
        para.put("payTimeBegin", "2015-08-18 00:00:00");
        para.put("payTimeEnd", "2015-08-22 00:00:00");
        List<Map<String, Object>> rowsInfo = analyzeService.todaySaleTop(para);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < rowsInfo.size(); i++)
        {
            Map<String, Object> om = rowsInfo.get(i);
            Row r = sheet.createRow(i + 1);
            r.createCell(0).setCellValue(om.get("index") + "");
            r.createCell(1).setCellValue(om.get("productId") + "");
            r.createCell(2).setCellValue(om.get("type") + "");
            r.createCell(3).setCellValue(om.get("name") + "");
            r.createCell(4).setCellValue(om.get("totalCount") + "");
            r.createCell(5).setCellValue(om.get("totalSalePrice") + "");
        }
        fOut = response.getOutputStream();
        workbook.write(fOut);
        fOut.flush();
    }
    
    /**
     * 各版本实付金额统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/appVersionBuyAnalyze")
    @ControllerLog(description = "数据统计-各版本实付金额统计")
    public ModelAndView appVersionBuyAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/appVersionBuyAnalyze");
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            Map<String, Object> result = analyzeService.appVersionBuyAnalyze(selectDate);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result.get("rows"));
            mv.addObject("lastRow", result.get("lastRow"));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 每日订单发货时效统计
     * @param selectDate：2015-09
     * @return
     * @throws Exception
     */
    @RequestMapping("/eachDayOrderSendTimeAnalyze")
    @ControllerLog(description = "数据统计-每日订单发货时效统计")
    public ModelAndView eachDayOrderSendTimeAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/eachDayOrderSendTimeAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.eachDayOrderSendTimeAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【每日订单发货时效统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出每日订单发货时效统计
     * @param request
     * @param response
     * @param selectDate
     */
    @RequestMapping(value = "/exportEachDayOrderSendTimeAnalyze")
    @ControllerLog(description = "数据统计-导出每日订单发货时效统计")
    public void exportEachDayOrderSendTimeAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            
            List<Map<String, Object>> result = analyzeService.eachDayOrderSendTimeAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("每日订单发货时效统计");
            String[] str = {"日期", "付款订单总量", "0.5天", "1天", "2天", "3天", "4天", "5天", "6天", "7天", "8天及更多", "未发货"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("date") + "");
                r.createCell(1).setCellValue(om.get("totalAmount") + "");
                r.createCell(2).setCellValue(om.get("dayOfHalfAmount") + "");
                r.createCell(3).setCellValue(om.get("dayOf1Amount") + "");
                r.createCell(4).setCellValue(om.get("dayOf2Amount") + "");
                r.createCell(5).setCellValue(om.get("dayOf3Amount") + "");
                r.createCell(6).setCellValue(om.get("dayOf4Amount") + "");
                r.createCell(7).setCellValue(om.get("dayOf5Amount") + "");
                r.createCell(8).setCellValue(om.get("dayOf6Amount") + "");
                r.createCell(9).setCellValue(om.get("dayOf7Amount") + "");
                r.createCell(10).setCellValue(om.get("dayOf8Amount") + "");
                r.createCell(11).setCellValue(om.get("notSendAmount") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "每日订单发货时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("【每日订单发货时效统计】导出日期" + selectDate + "出错了！！！", e);
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
    
    /**
     * 每日发货后有物流信息时效统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/eachDayOrderLogisticAnalyze")
    @ControllerLog(description = "数据统计-每日发货后有物流信息时效统计")
    public ModelAndView eachDayOrderLogisticAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        long a = System.currentTimeMillis();
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/eachDayOrderLogisticAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String sendTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String sendTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.eachDayOrderLogisticAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【每日发货后有物流信息时效统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - a) + "ms");
        return mv;
    }
    
    /**
     * 导出每日发货后有物流信息时效统计
     * @param request
     * @param response
     * @param selectDate
     */
    @RequestMapping(value = "/exportEachDayOrderLogisticAnalyze")
    @ControllerLog(description = "数据统计-导出每日发货后有物流信息时效统计")
    public void exportEachDayOrderLogisticAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String sendTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String sendTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("selectDate", selectDate);
            
            List<Map<String, Object>> result = analyzeService.eachDayOrderLogisticAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("每日发货后有物流信息时效统计");
            String[] str = {"日期", "发货订单总量", "0.5天", "1天", "2天", "3天", "4天", "5天及更多", "尚无物流信息"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("date") + "");
                r.createCell(1).setCellValue(om.get("totalAmount") + "");
                r.createCell(2).setCellValue(om.get("dayOfHalfAmount") + "");
                r.createCell(3).setCellValue(om.get("dayOf1Amount") + "");
                r.createCell(4).setCellValue(om.get("dayOf2Amount") + "");
                r.createCell(5).setCellValue(om.get("dayOf3Amount") + "");
                r.createCell(6).setCellValue(om.get("dayOf4Amount") + "");
                r.createCell(7).setCellValue(om.get("dayOf5Amount") + "");
                r.createCell(8).setCellValue(om.get("noLogisticsAmount") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "每日发货后有物流信息时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("【导出每日发货后有物流信息时效统计】导出日期" + selectDate + "出错了！！！", e);
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
    
    /**
     * 商家发货时效统计
     * @param selectDate：2015-09
     * @return
     * @throws Exception
     */
    @RequestMapping("/sellerSendTimeAnalyze")
    @ControllerLog(description = "数据统计-商家发货时效统计")
    public ModelAndView sellerSendTimeAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/sellerSendTimeAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.sellerSendTimeAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【商家发货时效统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出商家发货时效统计
     * @param request
     * @param response
     * @param selectDate
     */
    @RequestMapping(value = "/exportSellerSendTimeAnalyze")
    @ControllerLog(description = "数据统计-导出商家发货时效统计")
    public void exportSellerSendTimeAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            
            List<Map<String, Object>> result = analyzeService.sellerSendTimeAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("每日订单发货时效统计");
            String[] str = {"商家Id", "商家真实名称", "发货地", "分仓", "报关顺序", "付款订单总量", "0.5天", "1天", "2天", "3天", "4天", "5天", "6天", "7天", "8天及更多", "未发货"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("sellerId") + "");
                r.createCell(1).setCellValue(om.get("sellerName") + "");
                r.createCell(2).setCellValue(om.get("sendAddress") + "");
                r.createCell(3).setCellValue(om.get("warehouse") + "");
                r.createCell(4).setCellValue(om.get("bondedNumberType") + "");
                r.createCell(5).setCellValue(om.get("totalAmount") + "");
                r.createCell(6).setCellValue(om.get("dayOfHalfAmount") + "");
                r.createCell(7).setCellValue(om.get("dayOf1Amount") + "");
                r.createCell(8).setCellValue(om.get("dayOf2Amount") + "");
                r.createCell(9).setCellValue(om.get("dayOf3Amount") + "");
                r.createCell(10).setCellValue(om.get("dayOf4Amount") + "");
                r.createCell(11).setCellValue(om.get("dayOf5Amount") + "");
                r.createCell(12).setCellValue(om.get("dayOf6Amount") + "");
                r.createCell(13).setCellValue(om.get("dayOf7Amount") + "");
                r.createCell(14).setCellValue(om.get("dayOf8Amount") + "");
                r.createCell(15).setCellValue(om.get("notSendAmount") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "商家发货时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("【导出商家发货时效统计】导出日期" + selectDate + "出错了！！！", e);
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
    
    /**
     * 商家发货后有物流信息时效统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/sellerOrderLogisticAnalyze")
    @ControllerLog(description = "数据统计-商家发货后有物流信息时效统计")
    public ModelAndView sellerOrderLogisticAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/sellerOrderLogisticAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String sendTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String sendTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.sellerOrderLogisticAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【商家发货后有物流信息时效统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出商家发货后有物流信息时效统计
     * @param request
     * @param response
     * @param selectDate
     */
    @RequestMapping(value = "/exportSellerOrderLogisticAnalyze")
    @ControllerLog(description = "数据统计-导出商家发货后有物流信息时效统计")
    public void exportSellerOrderLogisticAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String sendTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String sendTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.sellerOrderLogisticAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商家发货后有物流信息时效统计");
            String[] str = {"商家Id", "商家真实名称", "发货地", "分仓", "报关顺序", "付款订单总量", "0.5天", "1天", "2天", "3天", "4天", "5天及更多", "尚无物流信息"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("sellerId") + "");
                r.createCell(1).setCellValue(om.get("sellerName") + "");
                r.createCell(2).setCellValue(om.get("sendAddress") + "");
                r.createCell(3).setCellValue(om.get("warehouse") + "");
                r.createCell(4).setCellValue(om.get("bondedNumberType") + "");
                r.createCell(5).setCellValue(om.get("totalAmount") + "");
                r.createCell(6).setCellValue(om.get("dayOfHalfAmount") + "");
                r.createCell(7).setCellValue(om.get("dayOf1Amount") + "");
                r.createCell(8).setCellValue(om.get("dayOf2Amount") + "");
                r.createCell(9).setCellValue(om.get("dayOf3Amount") + "");
                r.createCell(10).setCellValue(om.get("dayOf4Amount") + "");
                r.createCell(11).setCellValue(om.get("dayOf5Amount") + "");
                r.createCell(12).setCellValue(om.get("noLogisticsAmount") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "商家发货后有物流信息时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("【导出商家发货后有物流信息时效统计】导出日期" + selectDate + "出错了！！！", e);
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
    
    /**
     * 全国省份成交统计
     * @param selectDate：查询日期：yyyy-MM(2015-09)
     * @return
     */
    @RequestMapping("/provinceTurnOverAnalyze")
    @ControllerLog(description = "数据统计-全国省份成交统计")
    public ModelAndView provinceTurnOverAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/provinceTurnOverAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.provinceTurnOverAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【全国省份成交统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 全国城市成交统计
     * @param selectDate：查询日期：yyyy-MM(2015-09)
     * @return
     */
    @RequestMapping("/cityTurnOverAnalyze")
    @ControllerLog(description = "数据统计-全国城市成交统计")
    public ModelAndView cityTurnOverAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/cityTurnOverAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.cityTurnOverAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【全国城市成交统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出全国城市成交统计
     * @param request
     * @param response
     * @param selectDate
     */
    @RequestMapping(value = "/exportCityTurnOverAnalyze")
    @ControllerLog(description = "数据统计-全国城市成交统计")
    public void exportCityTurnOverAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.cityTurnOverAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("全国城市成交统计");
            String[] str = {"城市", "所属省份", "成交人数", "人数占比", "成交金额", "金额占比"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, String> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("cityName"));
                r.createCell(1).setCellValue(om.get("provinceName"));
                r.createCell(2).setCellValue(om.get("totalPerson"));
                r.createCell(3).setCellValue(om.get("totalPersonPercent"));
                r.createCell(4).setCellValue(om.get("totalPrice"));
                r.createCell(5).setCellValue(om.get("totalPricePercent"));
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "全国城市成交统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("【导出全国城市成交统计】导出日期" + selectDate + "出错了！！！", e);
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
    
    /**
     * 商品(特卖/商城)成交统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/productTurnOverAnalyze")
    @ControllerLog(description = "数据统计-商品(特卖/商城)成交统计")
    public ModelAndView productTurnOverAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/productTurnOverAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.productTurnOverAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            logger.error("【商品(特卖/商城)成交统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出商品(特卖/商城)成交统计
     * @param request
     * @param response
     * @param selectDate
     */
    @RequestMapping(value = "/exportProductTurnOverAnalyze")
    @ControllerLog(description = "数据统计-导出商品成交统计")
    public void exportProductTurnOverAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.productTurnOverAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商品分类成交统计");
            String[] str = {"日期", "总数量", "总金额", "特卖数量", "特卖金额", "特卖金额占比", "商城数量", "商城金额", "商城金额占比"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, String> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("date"));
                r.createCell(1).setCellValue(om.get("totalProductCount"));
                r.createCell(2).setCellValue(om.get("totalPrice"));
                r.createCell(3).setCellValue(om.get("saleProductCount"));
                r.createCell(4).setCellValue(om.get("saleProductTotalPrice"));
                r.createCell(5).setCellValue(om.get("saleProductTotalPricePrecent"));
                r.createCell(6).setCellValue(om.get("mallProductCount"));
                r.createCell(7).setCellValue(om.get("mallProductRealPrice"));
                r.createCell(8).setCellValue(om.get("mallProductTotalPricePrecent"));
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "商品成交统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("导出商品成交统计出错了！！！", e);
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
    
    /**
     * 商品分类成交统计
     * @param selectDate
     * @param start
     * @param stop
     * @param isSearch
     * @return
     */
    @RequestMapping("/productCategoryTurnOverAnalyze")
    @ControllerLog(description = "数据统计-商品分类成交统计")
    public ModelAndView productCategoryTurnOverAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/productCategoryTurnOverAnalyze");
            List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
            if ((isSearch == 1) && (!"".equals(selectDate)) && (start != 0) && (stop != 0) && (start <= stop))
            {
                DateTime dt = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                int maxDay = dt.plusMonths(1).plusDays(-1).getDayOfMonth();
                Map<String, Object> para = new HashMap<String, Object>();
                DateTime begin = null;
                DateTime end = null;
                String s = "" + start;
                String e = "" + stop;
                if (start < 10)
                {
                    s = "0" + start;
                }
                if (stop < 10)
                {
                    e = "0" + stop;
                }
                else if (stop > maxDay)
                {
                    e = "" + maxDay;
                }
                
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
                String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
                String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
                para.put("payTimeBegin", payTimeBegin);
                para.put("payTimeEnd", payTimeEnd);
                rows = analyzeService.productCategoryTurnOverAnalyze(para);
                
            }
            else
            {
                selectDate = DateTime.now().toString("yyyy-MM");
                start = DateTime.now().getDayOfMonth();
                stop = DateTime.now().getDayOfMonth();
            }
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", rows);
            mv.addObject("start", start);
            mv.addObject("stop", stop);
            if (start <= stop)
            {
                for (int i = start; i <= stop; i++)
                {
                    mv.addObject("num" + i, 1);
                }
            }
            List<Integer> dateList = new ArrayList<Integer>();
            for (int i = 1; i <= 31; i++)
            {
                dateList.add(i);
            }
            mv.addObject("dateList", dateList);
        }
        catch (Exception e)
        {
            logger.error("【商品分类成交统计】查看日期" + selectDate + "出错了！！！", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出商品分类成交统计
     * @param request
     * @param response
     * @param selectDate
     * @param start
     * @param stop
     * @param isSearch
     */
    @RequestMapping(value = "/exportProductCategoryTurnOverAnalyze")
    @ControllerLog(description = "数据统计-导出商品分类成交统计")
    public void exportProductCategoryTurnOverAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch// 2015-04
    )
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            DateTime dt = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            int maxDay = dt.plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;
            String e = "" + stop;
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            
            List<Map<String, String>> result = analyzeService.productCategoryTurnOverAnalyze(para);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商品分类成交统计");
            String[] str = {"一级分类", "二级分类", "三级分类", "总数量", "总SKU", "总金额", "特卖数量", "特卖SKU", "特卖金额", "特卖金额占比", "商城数量", "商城SKU", "商城金额", "商城金额占比"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, String> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("categoryFirstName"));
                r.createCell(1).setCellValue(om.get("categorySecondName"));
                r.createCell(2).setCellValue(om.get("categorythirdName"));
                r.createCell(3).setCellValue(om.get("totalCount"));
                r.createCell(4).setCellValue(om.get("totalCountSKU"));
                r.createCell(5).setCellValue(om.get("totalPrice"));
                r.createCell(6).setCellValue(om.get("saleProductCount"));
                r.createCell(7).setCellValue(om.get("saleProductCountSKU"));
                r.createCell(8).setCellValue(om.get("saleProductTotalPrice"));
                r.createCell(9).setCellValue(om.get("saleProductTotalPricePrecent"));
                r.createCell(10).setCellValue(om.get("mallProductCount"));
                r.createCell(11).setCellValue(om.get("mallProductCountSKU"));
                r.createCell(12).setCellValue(om.get("mallProductRealPrice"));
                r.createCell(13).setCellValue(om.get("mallProductTotalPricePrecent"));
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = selectDate + "（" + s + "-" + e + "）商品分类成交统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("【导出商品分类成交统计】导出日期" + selectDate + start + "-" + selectDate + stop + "出错了！！！", e);
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
    
    /**
     * 商家毛利统计
     */
    @RequestMapping("/sellerGrossSettlement")
    @ControllerLog(description = "数据统计-商家毛利统计")
    public ModelAndView sellerGrossSettlement()
    {
        ModelAndView mv = new ModelAndView("finance/sellerGrossSettlement");
        mv.addObject("selectDate", DateTime.now().toString("yyyy-MM"));
        List<Integer> dateList = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++)
        {
            dateList.add(i);
        }
        mv.addObject("dateList", dateList);
        return mv;
    }
    
    /**
     * 异步获取 商家结算汇总
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonSellerGrossSettlement", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "数据统计-异步获取-商家结算汇总")
    public String jsonSellerGrossSettlement(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start, // 1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop, // 1-31
        @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        // 为0表示不查询结果
        throws Exception
    {
        if (search == 0)
        {
            return JSON.toJSONString(new ArrayList());
        }
        int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
        DateTime begin = null;
        DateTime end = null;
        String s = "" + start;// 1
        String e = "" + stop;// 1
        if (start < 10)
        {
            s = "0" + start;
        }
        if (stop < 10)
        {
            e = "0" + stop;
        }
        else if (stop > maxDay)
        {
            e = "" + maxDay;
        }
        
        begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
        String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
        String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("settlementTimeBegin", payTimeBegin);
        para.put("settlementTimeEnd", payTimeEnd);
        Map<String, Object> result = financeSerivce.findSellerGrossSettlement(para);
        List<Map<String, Object>> re = (List<Map<String, Object>>)result.get("rows");
        //        System.out.println(result);
        //        if (re.size() > 0)
        //        {
        //            re.add((Map<String, Object>)result.get("lastRow"));
        //        }
        //        System.out.println("result: " + result);
        return JSON.toJSONString(re);
    }
    
    @RequestMapping(value = "/exportSellerGrossSettlement")
    @ControllerLog(description = "数据统计-导出商家结算统计")
    public void exportSellerGrossSettlement(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start, // 1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop, // 1-31
        @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        // 为0表示不查询结果
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;// 1
            String e = "" + stop;// 1
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("settlementTimeBegin", payTimeBegin);
            para.put("settlementTimeEnd", payTimeEnd);
            Map<String, Object> result = financeSerivce.findSellerGrossSettlement(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            String[] title = {"商家ID", "商家名称", "发货地", "订单商品总价", "订单供货总价", "订单结算运费", "扣除运费毛利", "毛利率"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("sellerId") == null ? "" : it.get("sellerId") + "");
                r.createCell(cellIndex++).setCellValue(it.get("realSellerName") == null ? "" : it.get("realSellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sendAddress") == null ? "" : it.get("sendAddress") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalSalesPrice") == null ? "" : it.get("totalSalesPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalCost") == null ? "" : it.get("totalCost") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalFreight") == null ? "" : it.get("totalFreight") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalGross") == null ? "" : it.get("totalGross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalGrossDivTotalSalesPrice") == null ? "" : it.get("totalGrossDivTotalSalesPrice") + "");
            }
            String fileName = "商家结算统计";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
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
    
    /**
     * 一级分类成交统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/firstCategoryTurnover")
    @ControllerLog(description = "数据统计-一级分类成交统计")
    public ModelAndView firstCategoryTurnover(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/firstCategoryTurnover");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            int currMonth = Integer.parseInt(begin.toString("yyyyMM"));
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            if (currMonth <= 201503)//2015年3月26上线的，之前的数据不统计
            {
                payTimeBegin = begin.withYear(2015).withMonthOfYear(3).withDayOfMonth(26).toString("yyyy-MM-dd 00:00:00");
            }
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.firstCategoryTurnoverAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 统计一级分类成交用户数
     * @param request
     * @param response
     * @param selectDate
     * @throws Exception
     */
    @RequestMapping(value = "/exportFirstCategoryTurnover")
    @ControllerLog(description = "数据统计-导出一级分类成交用户数统计")
    public void exportFirstCategoryTurnover(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
        // 为0表示不查询结果
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            int currMonth = Integer.parseInt(begin.toString("yyyyMM"));
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            if (currMonth <= 201503)//2015年3月26上线的，之前的数据不统计
            {
                payTimeBegin = begin.withYear(2015).withMonthOfYear(3).withDayOfMonth(26).toString("yyyy-MM-dd 00:00:00");
            }
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, Object>> result = analyzeService.firstCategoryTurnoverAnalyze(para);
            String[] title = {"一级分类", "成交用户数"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : result)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("categoryName") == null ? "" : it.get("categoryName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalCount") == null ? "" : it.get("totalCount") + "");
            }
            String fileName = selectDate + "成交用户数";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
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
    
    /**
     * 客户平均提篮量统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/customerAverageAnalyze")
    public ModelAndView customerAverageAnalyze(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/customerAverageAnalyze");
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            int currMonth = Integer.parseInt(begin.toString("yyyyMM"));
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            if (currMonth <= 201503)//2015年3月26上线的，之前的数据不统计
            {
                payTimeBegin = begin.withYear(2015).withMonthOfYear(3).withDayOfMonth(26).toString("yyyy-MM-dd 00:00:00");
            }
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.customerAverageAnalyze(para);
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", result);
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出客户平均提篮数量统计
     * @param request
     * @param response
     * @param selectDate
     * @throws Exception
     */
    @RequestMapping(value = "/exportCustomerAverageAnalyze")
    @ControllerLog(description = "数据统计-导出客户平均提篮数量统计")
    public void exportCustomerAverageAnalyze(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
        // 为0表示不查询结果
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(selectDate))
            {
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            int currMonth = Integer.parseInt(begin.toString("yyyyMM"));
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            if (currMonth <= 201503)//2015年3月26上线的，之前的数据不统计
            {
                payTimeBegin = begin.withYear(2015).withMonthOfYear(3).withDayOfMonth(26).toString("yyyy-MM-dd 00:00:00");
            }
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeBegin", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("selectDate", selectDate);
            List<Map<String, String>> result = analyzeService.customerAverageAnalyze(para);
            String[] title = {"成交时间", "用户名", "SKU种类数", "SKU数量合计"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, String> it : result)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("date") == null ? "" : it.get("date") + "");
                r.createCell(cellIndex++).setCellValue(it.get("name") == null ? "" : it.get("name") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productCount") == null ? "" : it.get("productCount") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalCount") == null ? "" : it.get("totalCount") + "");
            }
            String fileName = selectDate + "客户平均提篮量";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            logger.error("导出出错", e);
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
    
    /**
     * 每月订单发货时效统计
     * @param selectDate
     * @return
     */
    @RequestMapping("/orderSendTimeAnalyzeByMonth")
    public ModelAndView orderSendTimeAnalyzeByMonth(@RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/orderSendTimeAnalyzeByMonth");
            String payTimeBegin = null;
            String payTimeEnd = null;
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
                payTimeBegin = DateTime.now().toString("yyyy-MM-01 00:00:00");
                payTimeEnd = DateTime.now().plusMonths(1).toString("yyyy-MM-01 00:00:00");
            }
            else
            {
                payTimeBegin = new DateTime(CommonUtil.string2Date(selectDate, "yyyy-MM").getTime()).toString("yyyy-MM-01 00:00:00");
                payTimeEnd = new DateTime(CommonUtil.string2Date(selectDate, "yyyy-MM").getTime()).plusMonths(1).toString("yyyy-MM-01 00:00:00");
            }
            mv.addObject("selectDate", selectDate);
            mv.addObject("rows", analyzeService.orderSendTimeAnalyzeByMonth(selectDate, payTimeBegin, payTimeEnd));
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    @RequestMapping(value = "/exportOrderSendTimeAnalyzeByMonth")
    @ControllerLog(description = "数据统计-导出每日发货时效统计")
    public void exportOrderSendTimeAnalyzeByMonth(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            String payTimeBegin = null;
            String payTimeEnd = null;
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
                payTimeBegin = DateTime.now().toString("yyyy-MM-01 00:00:00");
                payTimeEnd = DateTime.now().plusMonths(1).toString("yyyy-MM-01 00:00:00");
            }
            else
            {
                payTimeBegin = new DateTime(CommonUtil.string2Date(selectDate, "yyyy-MM").getTime()).toString("yyyy-MM-01 00:00:00");
                payTimeEnd = new DateTime(CommonUtil.string2Date(selectDate, "yyyy-MM").getTime()).plusMonths(1).toString("yyyy-MM-01 00:00:00");
            }
            
            List<Map<String, Object>> result = analyzeService.orderSendTimeAnalyzeByMonth(selectDate, payTimeBegin, payTimeEnd);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商家订单发货时效");
            String[] str = {"日期", "应发订单量", "准时发货", "发货超时发货", "发货超时未发货", "未超时未发货", "发货准时率", "最优发货准时率", "发货完成进度"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("sellerName") + "");
                r.createCell(1).setCellValue(om.get("totalAmount") + "");
                r.createCell(2).setCellValue(om.get("ontimeAmount") + "");
                r.createCell(3).setCellValue(om.get("timeoutSendAmount") + "");
                r.createCell(4).setCellValue(om.get("timeoutNoSendAmount") + "");
                r.createCell(5).setCellValue(om.get("waitSendAmount") + "");
                r.createCell(6).setCellValue(om.get("sendOntimePercent") + "");
                r.createCell(7).setCellValue(om.get("bestSendOntimePercent") + "");
                r.createCell(8).setCellValue(om.get("sendProgressPercent") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "商家订单发货时效（" + selectDate + "）";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("导出商家订单发货时效出错了！！！", e);
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
    
    @RequestMapping("/userStatisticByChannel")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("analyze/userStatisticByChannel");
        return mv;
    }
    
    @RequestMapping(value = "/jsonUserStatisticByChannelInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道用户注册与成交统计 -注册成交明细")
    public String jsonUserStatisticByChannelInfo(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,//
            @RequestParam(value = "rows", required = true, defaultValue = "50") int rows,// 
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime
           )
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0) page = 1;
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if(StringUtils.isNotEmpty(startTime)&&StringUtils.isNotEmpty(endTime)){
                para.put("startTime", startTime);
                para.put("endTime", endTime);
            }
          resultMap = analyzeService.getUserStatisticByChannel(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    
    @RequestMapping(value = "/viewStatisticDetail")
    @ControllerLog(description = "渠道用户注册与成交统计 -获取成交明细")
    public ModelAndView viewStatisticDetail(
            @RequestParam(value = "startTime", required = false) String startTime,//
            @RequestParam(value = "endTime", required = false) String endTime,// 
            @RequestParam(value = "type", required = false) int type,
            @RequestParam(value = "platform", required = false) String platform,
            @RequestParam(value = "appChannel", required = false) int appChannel,
            @RequestParam(value = "registUserCount", required = false) int registUserCount,
            @RequestParam(value = "orderUserCount", required = false) int orderUserCount,
            @RequestParam(value = "totalPriceDisplay", required = false) String totalPriceDisplay
           )
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.setViewName("analyze/userStatisticDetail");
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> typeList = new ArrayList<Integer>();
            switch(type){
            case 1:
            typeList = Arrays.asList(1,2,3,4,5);
            break;
            case 6:
            typeList = Arrays.asList(6,7);
            break;
            case 8:
            typeList = Arrays.asList(8);
            break;
            }
            para.put("typeList", typeList);
            para.put("appChannel", appChannel);
            if(StringUtils.isNotEmpty(startTime)&&StringUtils.isNotEmpty(endTime)){
                para.put("startTime", startTime);
                para.put("endTime", endTime);
            }
            
            List<Map<String,Object>> result = analyzeService.viewStatisticDetail(para);
            mv.addObject("rows", result);
            mv.addObject("startTime", startTime);
            mv.addObject("endTime", endTime);
            mv.addObject("type", AccountEnum.ACCOUNT_CHANNEL_TYPE.getDescByCode(type));
            mv.addObject("platform",platform);
            String appChannelName = 999 ==appChannel?CommonConstant.HISTORY_VERSION:
                CommonEnum.OrderAppChannelEnum.getDescriptionByOrdinal(appChannel);
            mv.addObject("appChannel",appChannelName);
            mv.addObject("registUserCount", registUserCount);
            mv.addObject("orderUserCount", orderUserCount);
            mv.addObject("totalPriceDisplay", totalPriceDisplay);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }

}
