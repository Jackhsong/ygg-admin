package com.ygg.admin.controller;

import java.io.OutputStream;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.WechatGroupDataService;
import com.ygg.admin.util.DateTimeUtil;

@Controller
@RequestMapping("/wechatGroupData")
public class WeChatGroupDataController
{
    Logger log = Logger.getLogger(WeChatGroupDataController.class);
    
    @Resource
    private WechatGroupDataService wechatGroupDataService;
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2015年12月9日 下午1:36:36
     * @描述:
     *      <p>
     *      (左岸城堡与月数据统计)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年12月9日 下午1:36:36
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param request
     * @param selectDate
     * @return
     * @returnType ModelAndView
     * @version V1.0
     */
    @RequestMapping("/monthList")
    @ControllerLog(description = "数据统计-左岸城堡月度数据统计")
    public ModelAndView monthList(HttpServletRequest request, @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate,
        @RequestParam(value = "nodeId", required = false, defaultValue = "0") String nodeId)
    {
        ModelAndView mv = new ModelAndView("wechatGroupData/monthList");
        try
        {
            // String date = DateTime.now().toString("yyyy-MM") + "-01";
            // if (!"".equals(selectDate))
            // {
            // date = selectDate + "-01";
            // }
            // else
            // {
            // selectDate = DateTime.now().toString("yyyy-MM");
            // }
            
            String date = DateTime.now().toString("yyyy-MM");
            if ("".equals(selectDate))
            {
                selectDate = date;
            }
            
            mv.addAllObjects(wechatGroupDataService.monthList2(selectDate));
            mv.addObject("selectDate", selectDate);
            mv.addObject("nodeId", nodeId);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 
     * @创建人: zero
     * @创建时间: 2015年12月9日 下午1:37:00
     * @描述:
     *      <p>
     *      (左岸城堡商品统计)
     *      </p>
     * @修改人: zero
     * @修改时间: 2015年12月9日 下午1:37:00
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param request
     * @param startTime
     * @param endTime
     * @return
     * @returnType ModelAndView
     * @version V1.0
     */
    @RequestMapping("/productDateList")
    @ControllerLog(description = "数据统计-左岸城堡商品统计")
    public ModelAndView productDateList(HttpServletRequest request, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, @RequestParam(value = "nodeId", required = false, defaultValue = "0") String nodeId)
    {
        ModelAndView mv = new ModelAndView("wechatGroupData/productDateList");
        try
        {
            
            if ("".equals(startTime))
            {
                startTime = DateTime.now().toString("yyyy-MM-dd");
            }
            
            if ("".equals(endTime))
            {
                endTime = DateTime.now().toString("yyyy-MM-dd");
            }
            
            mv.addAllObjects(wechatGroupDataService.productDateList2(startTime, endTime));
            mv.addObject("startTime", startTime);
            mv.addObject("endTime", endTime);
            mv.addObject("nodeId", nodeId);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 导出左岸城堡月数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportMonthList")
    @ControllerLog(description = "数据统计-导出月度统计")
    public void exportMonthList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        
        // String date = DateTime.now().toString("yyyy-MM") + "-01";
        // if (!"".equals(selectDate))
        // {
        // date = selectDate + "-01";
        // }
        // else
        // {
        // selectDate = DateTime.now().toString("yyyy-MM");
        // }
        //
        String date = DateTime.now().toString("yyyy-MM");
        if ("".equals(selectDate))
        {
            selectDate = date;
        }
        
        try
        {
            JSONObject jsonObject = wechatGroupDataService.monthList2(selectDate);
            
            JSONArray array = jsonObject.getJSONArray("list");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("左岸城堡月度数据统计");
            String[] str = {"日期", "创建拼团数", "创建订单数", "创建金额", "成功拼团数", "成功订单数", "成功金额", "拼团成功率"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < array.size(); i++)
            {
                JSONObject j = array.getJSONObject(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(j.getString("createTime"));
                r.createCell(1).setCellValue(j.getString("createGroupCount"));
                r.createCell(2).setCellValue(j.getString("createOrderCount"));
                r.createCell(3).setCellValue(j.getString("createRealPrice"));
                r.createCell(4).setCellValue(j.getString("successGroupCount"));
                r.createCell(5).setCellValue(j.getString("successOrderCount"));
                r.createCell(6).setCellValue(j.getString("successRealPrice"));
                r.createCell(7).setCellValue(j.getString("successRate"));
            }
            Row r = sheet.createRow(array.size() + 1);
            r.createCell(0).setCellValue("总计");
            r.createCell(1).setCellValue(jsonObject.getString("totalCreateGroupCount"));
            r.createCell(2).setCellValue(jsonObject.getString("totalCreateOrderCount"));
            r.createCell(3).setCellValue(jsonObject.getString("totalCreateRealPrice"));
            r.createCell(4).setCellValue(jsonObject.getString("totalSuccessGroupCount"));
            r.createCell(5).setCellValue(jsonObject.getString("totalSuccessOrderCount"));
            r.createCell(6).setCellValue(jsonObject.getString("totalSuccessRealPrice"));
            r.createCell(7).setCellValue(jsonObject.getString("totalSuccessRate"));
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "左岸城堡月度数据统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
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
     * 导出左岸城堡商品数据统计
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportProductDateList")
    @ControllerLog(description = "数据统计-导出商品统计")
    public void exportProductDateList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        if ("".equals(startTime))
        {
            startTime = DateTime.now().toString("yyyy-MM-dd");
        }
        
        if ("".equals(endTime))
        {
            endTime = DateTime.now().toString("yyyy-MM-dd");
        }
        
        try
        {
            JSONObject jsonObject = wechatGroupDataService.productDateList2(startTime, endTime);
            
            JSONArray array = jsonObject.getJSONArray("list");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("左岸城堡商品数据统计");
            String[] str = {"拼团商品ID", "商品名称", "创建拼团数", "创建订单数", "创建金额", "成功拼团数", "成功订单数", "成功金额", "拼团成功率"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < array.size(); i++)
            {
                JSONObject j = array.getJSONObject(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(j.getString("mwebGroupProductId"));
                r.createCell(1).setCellValue(j.getString("name"));
                r.createCell(2).setCellValue(j.getString("createGroupCount"));
                r.createCell(3).setCellValue(j.getString("createOrderCount"));
                r.createCell(4).setCellValue(j.getString("createRealPrice"));
                r.createCell(5).setCellValue(j.getString("successGroupCount"));
                r.createCell(6).setCellValue(j.getString("successOrderCount"));
                r.createCell(7).setCellValue(j.getString("successRealPrice"));
                r.createCell(8).setCellValue(j.getString("successRate"));
            }
            Row r = sheet.createRow(array.size() + 1);
            r.createCell(0).setCellValue("总计");
            r.createCell(1).setCellValue(jsonObject.getString("totalProductCount"));
            r.createCell(2).setCellValue(jsonObject.getString("totalCreateGroupCount"));
            r.createCell(3).setCellValue(jsonObject.getString("totalCreateOrderCount"));
            r.createCell(4).setCellValue(jsonObject.getString("totalCreateRealPrice"));
            r.createCell(5).setCellValue(jsonObject.getString("totalSuccessGroupCount"));
            r.createCell(6).setCellValue(jsonObject.getString("totalSuccessOrderCount"));
            r.createCell(7).setCellValue(jsonObject.getString("totalSuccessRealPrice"));
            r.createCell(8).setCellValue(jsonObject.getString("totalSuccessRate"));
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "左岸城堡商品数据统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
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
     * 数据魔方
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/todaySaleTop")
    @ControllerLog(description = "数据统计-左岸城堡数据魔方")
    public ModelAndView todaySaleTop(HttpServletRequest request, //
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-07-01
        @RequestParam(value = "nodeId", required = false, defaultValue = "0") String nodeId)
            throws Exception
    {
        
        ModelAndView mv = new ModelAndView("wechatGroupData/todaySaleTopAnalyze");
        if ("".equals(selectDate))
        {
            selectDate = DateTime.now().toString("yyyy-MM-dd");
        }
        DateTime prev = DateTimeUtil.string2DateTime(selectDate, "yyyy-MM-dd");
        DateTime next = prev;
        prev = prev.plusDays(-1);
        next = next.plusDays(1);
        // 获得折线图数据
        JSONObject j1 = wechatGroupDataService.saleLineData(prev.toString("yyyy-MM-dd"));
        JSONObject j2 = wechatGroupDataService.saleLineData(selectDate);
        JSONArray data1 = (JSONArray)j1.get("array");
        JSONArray data2 = (JSONArray)j2.get("array");
        String yesterdayTotal = j1.getString("s");
        String nowTotal = j2.getString("s");
        mv.addAllObjects(wechatGroupDataService.todaySaleTop2(selectDate));
        mv.addObject("selectDate", selectDate);
        mv.addObject("prev", prev.toString("yyyy-MM-dd"));
        mv.addObject("next", next.toString("yyyy-MM-dd"));
        mv.addObject("nodeId", nodeId);
        mv.addObject("data1", data1.toJSONString());
        mv.addObject("data2", data2.toJSONString());
        mv.addObject("yesterdayTotal", yesterdayTotal);
        mv.addObject("nowTotal", nowTotal);
        return mv;
    }
    
    /**
     * 导出左岸城堡数据魔方
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportTodaySaleTop")
    @ControllerLog(description = "数据统计-导出左岸城堡数据魔方统计")
    public void exportTodaySaleTop(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        if ("".equals(selectDate))
        {
            selectDate = DateTime.now().toString("yyyy-MM-dd");
        }
        DateTime prev = DateTimeUtil.string2DateTime(selectDate, "yyyy-MM-dd");
        DateTime next = prev;
        prev = prev.plusDays(-1);
        next = next.plusDays(1);
        
        try
        {
            JSONObject jsonObject = wechatGroupDataService.todaySaleTop(selectDate);
            
            JSONArray array = jsonObject.getJSONArray("list");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("左岸城堡商品数据统计");
            String[] str = {"拼团商品ID", "商品名称", "创建拼团数", "创建订单数", "创建金额"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < array.size(); i++)
            {
                JSONObject j = array.getJSONObject(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(j.getString("mwebGroupProductId"));
                r.createCell(1).setCellValue(j.getString("name"));
                r.createCell(2).setCellValue(j.getString("createGroupCount"));
                r.createCell(3).setCellValue(j.getString("createOrderCount"));
                r.createCell(4).setCellValue(j.getString("createRealPrice"));
                
            }
            Row r = sheet.createRow(array.size() + 1);
            r.createCell(0).setCellValue("总计");
            r.createCell(1).setCellValue(jsonObject.getString("totalProductCount"));
            r.createCell(2).setCellValue(jsonObject.getString("totalCreateGroupCount"));
            r.createCell(3).setCellValue(jsonObject.getString("totalCreateOrderCount"));
            r.createCell(4).setCellValue(jsonObject.getString("totalCreateRealPrice"));
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "左岸城堡数据魔方统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
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
