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
package com.ygg.admin.controller.yw.data;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.QqbsAnalyzeService;
import com.ygg.admin.service.yw.data.YwAnalyzeDataService;
import com.ygg.admin.util.CommonUtil;



@Controller
@RequestMapping("/ywDataAnalyze")
public class YwAnalyzeDataController {
    
	   Logger logger = Logger.getLogger(YwAnalyzeDataController.class);
	   
	   @Resource
	   private YwAnalyzeDataService ywAnalyzeDataService;
	   
	   /**
	     * 
	     * @创建人: Qiu,Yibo
	     * @创建时间: 2016年04月07日 下午1:36:36
	     * @描述:
	     *      <p>
	     *      (燕网月度数据统计)
	     *      </p>
	     * @param request
	     * @param selectDate
	     * @return
	     * @returnType ModelAndView
	     * @version V1.0
	     * @throws Exception 
	     */
	    @RequestMapping("/monthAnalyze")
	    @ControllerLog(description = "数据统计-燕网月度数据统计")
	    public ModelAndView monthAnalyze(HttpServletRequest request, @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,
	        @RequestParam(value = "nodeId", required = false, defaultValue = "0") String nodeId) throws Exception
	    {
	        ModelAndView mv = new ModelAndView("ywDataAnalyze/ywMonthAnalyze");
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
	        Map<String, Object> rowsInfo = ywAnalyzeDataService.monthAnalyze(para);
	        mv.addObject("rows", rowsInfo.get("rows"));
	        mv.addObject("lastRow", rowsInfo.get("lastRow"));
	        mv.addObject("selectDate", selectDate);
	        return mv;
	    }
	    
	    /**
         * 导出燕网月度统计
         */
        @SuppressWarnings("unchecked")
        @RequestMapping(value = "/exportMonthAnalyze")
        @ControllerLog(description = "数据统计-导出燕网月度统计")
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
                Map<String, Object> resultList = ywAnalyzeDataService.monthAnalyze(para);
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
                String codedFileName = "燕网月度统计结果";
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
         * 
         * @创建人: Qiu,Yibo
         * @创建时间: 2016年04月08日 上午10:36:10
         * @描述:
         *      <p>
         *      (燕网数据魔方)
         *      </p>
         * @param request
         * @param selectDate
         * @return
         * @returnType ModelAndView
         * @version V1.0
         * @throws Exception 
         */
	    @SuppressWarnings("unchecked")
        @RequestMapping("/todaySaleTop")
	    @ControllerLog(description = "数据统计-今日销售top")
	    public ModelAndView todaySaleTop(HttpServletRequest request,//
	        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate// 2015-07-01
	    )
	        throws Exception
	    {
	        ModelAndView mv = new ModelAndView("ywDataAnalyze/todaySaleTopAnalyze");
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
	        List<Map<String, Object>> rowsInfo = ywAnalyzeDataService.todaySaleTop(para);
	        mv.addObject("rows", rowsInfo);
	        // 获得折线图数据
	        Map<String, Object> data = ywAnalyzeDataService.saleLineData(begin);
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
	    
	    
	       /**
         * 
         * @创建人: Qiu,Yibo
         * @创建时间: 2016年04月08日 上午10:36:10
         * @描述:
         *      <p>
         *      (导出燕网数据魔方)
         *      </p>
         * @param request
         * @param selectDate
         * @return
         * @returnType ModelAndView
         * @version V1.0
         * @throws Exception 
         */
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
	            List<Map<String, Object>> rowsInfo = ywAnalyzeDataService.todaySaleTop(para);
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
         * 
         * @创建人: Qiu,Yibo
         * @创建时间: 2016年04月08日 上午10:36:10
         * @描述:
         *      <p>
         *      (燕网商品统计)
         *      </p>
         * @param request
         * @param selectDate
         * @return
         * @returnType ModelAndView
         * @version V1.0
         * @throws Exception 
         */
	    @SuppressWarnings("unchecked")
        @RequestMapping("/product")
	    @ControllerLog(description = "数据统计-商品统计")
	    public ModelAndView product(HttpServletRequest request,//
	        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,// 2015-04
	        @RequestParam(value = "start", required = false, defaultValue = "0") int start,//1-31
	        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop,//1-31
	        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch, @RequestParam(value = "type", required = false, defaultValue = "0") int type)
	        throws Exception
	    {
	        ModelAndView mv = new ModelAndView();
	        if (type == 0)
	        {
	            mv.setViewName("ywDataAnalyze/productNum");
	        }
	        else
	        {
	            mv.setViewName("ywDataAnalyze/productMoney");
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
	            Map<String, Object> result = ywAnalyzeDataService.productDataCustom(para);
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
	     * 导出燕网商品统计
	     */
	    @SuppressWarnings("unchecked")
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
	            Map<String, Object> result = ywAnalyzeDataService.productDataCustom(para);
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
	    
}
