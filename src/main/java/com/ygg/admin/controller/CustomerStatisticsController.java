package com.ygg.admin.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ygg.admin.service.CustomerStatisticsService;

/**
 * 客服数据统计
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("customerStatistics")
public class CustomerStatisticsController
{
    Logger logger = Logger.getLogger(CustomerStatisticsController.class);
    
    @Resource
    private CustomerStatisticsService customerStatisticsService;
    
    /**
     * 跳日退款率统计
     * @return
     */
    @RequestMapping("toRefundOfDay")
    public ModelAndView toRefundOfDay()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("date", DateTime.now().toString("yyyy-MM"));
        mv.setViewName("customerStatistics/refundOfDay");
        return mv;
    }
    
    /**
     * 跳商家退款率统计
     * @return
     */
    @RequestMapping("toRefundOfSeller")
    public ModelAndView toRefundOfSeller()
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("endTime", DateTime.now().toString("yyyy-MM-dd 23:59:59"));
        mv.addObject("startTime", DateTime.now().plusHours(-24).toString("yyyy-MM-dd 00:00:00"));
        mv.setViewName("customerStatistics/refundOfSeller");
        return mv;
    }
    
    /**
     * 根据日期统计退款率
     * 
     * @param date
     * @return
     */
    @RequestMapping("refundListOfDay")
    @ResponseBody
    public Object refundListOfDay(String date)
    {
        try
        {
            date = StringUtils.isEmpty(date) ? DateTime.now().toString("yyyy-MM") : date;
            return customerStatisticsService.refundListOfDay(date);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 根据商家统计退款率
     * 
     * @param startTime
     * @param endTime
     * @param sellerId
     * @return
     */
    @RequestMapping("refundListOfSeller")
    @ResponseBody
    public Object refundListOfSeller(String startTime, String endTime,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0")int sellerId,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            startTime = StringUtils.isEmpty(startTime) ? DateTime.now().plusHours(-24).toString("yyyy-MM-dd HH:mm:ss") : startTime;
            endTime = StringUtils.isEmpty(endTime) ? DateTime.now().toString("yyyy-MM-dd HH:mm:ss") : endTime;
            return customerStatisticsService.refundListOfSeller(startTime, endTime, sellerId, page, rows, 0);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询采购单商品列表
     * @param purchaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("exportListOfDay")
    public Object exportListOfDay(HttpServletResponse response, String date)
    {
        try
        {
            date = StringUtils.isEmpty(date) ? DateTime.now().toString("yyyy-MM") : date;
            Map<String, Object> resultInfo = customerStatisticsService.refundListOfDay(date);
            if(resultInfo != null && resultInfo.size() > 0)
            {
                String displayName = "日退款率统计";
                String[] headContent = {"付款日期", "订单数", "实付金额", "仅退款金额", "仅退款率", "退款并退货金额", "退货并退款率", "合计退款金额", "合计退款率"};
                excel(response, displayName, headContent, map2arr4day((List<Map<String, Object>>)resultInfo.get("rows")));
            }
            return null;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询采购单商品列表
     * @param purchaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("exportListOfSeller")
    public Object exportListOfSeller(HttpServletResponse response, String startTime, String endTime,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0")int sellerId)
    {
        try
        {
            startTime = StringUtils.isEmpty(startTime) ? DateTime.now().plusHours(-24).toString("yyyy-MM-dd HH:mm:ss") : startTime;
            endTime = StringUtils.isEmpty(endTime) ? DateTime.now().toString("yyyy-MM-dd HH:mm:ss") : endTime;
            Map<String, Object> resultInfo = customerStatisticsService.refundListOfSeller(startTime, endTime, sellerId, 0, 0, 1);
            if(resultInfo != null && resultInfo.size() > 0)
            {
                String displayName = "商家退款率统计";
                String[] headContent = {"商家ID", "商家名称", "订单数", "实付金额", "仅退款金额", "仅退款率", "退款并退货金额", "退货并退款率", "合计退款金额", "合计退款率"};
                excel(response, displayName, headContent, map2arr4seller((List<Map<String, Object>>)resultInfo.get("rows")));
            }
            return null;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    private List<Object[]> map2arr4seller(List<Map<String, Object>> result)
    {
        List<Object[]> rowContents = new ArrayList<Object[]>();
        if (result != null && result.size() > 0)
        {
            for (Map<String, Object> map : result)
            {
                int i = 0;
                Object[] obj = new Object[10];
                obj[i++] = map.get("sellerId");
                obj[i++] = map.get("sellerName");
                obj[i++] = map.get("count");
                
                Object r = map.get("realMoney") == null ? 0 : map.get("realMoney");
                obj[i++] = r;
                
                Object r1 = map.get("realMoney1") == null ? 0 : map.get("realMoney1");
                obj[i++] = r1;
                
                BigDecimal realMoney = new BigDecimal(r.toString());
                BigDecimal realMoney1 = new BigDecimal(Double.valueOf(r1.toString()) * 100);
                obj[i++] = Double.valueOf(r.toString()).doubleValue() == 0 ? "" : realMoney1.divide(realMoney, 2, BigDecimal.ROUND_HALF_UP) + "%";
                
                Object r2 = map.get("realMoney2") == null ? 0 : map.get("realMoney2");
                obj[i++] = r2;
                
                BigDecimal realMoney2 = new BigDecimal(Double.valueOf(r2.toString()) * 100);
                obj[i++] = Double.valueOf(r.toString()).doubleValue() == 0 ? "" : realMoney2.divide(realMoney, 2, BigDecimal.ROUND_HALF_UP) + "%";
                
                obj[i++] = (realMoney1.add(realMoney2)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                obj[i++] = Double.valueOf(r.toString()).doubleValue() == 0 ? "" : (realMoney1.add(realMoney2)).divide(realMoney, 2, BigDecimal.ROUND_HALF_UP) + "%";
                rowContents.add(obj);
            }
        }
        return rowContents;
    }
    
    private List<Object[]> map2arr4day(List<Map<String, Object>> result)
    {
        List<Object[]> rowContents = new ArrayList<Object[]>();
        if (result != null && result.size() > 0)
        {
            for (Map<String, Object> map : result)
            {
                int i = 0;
                Object[] obj = new Object[9];
                obj[i++] = map.get("day");
                obj[i++] = map.get("count");
                
                Object r = map.get("realMoney") == null ? 0 : map.get("realMoney");
                obj[i++] = r;
                
                Object r1 = map.get("realMoney1") == null ? 0 : map.get("realMoney1");
                obj[i++] = r1;
                
                BigDecimal realMoney = new BigDecimal(r.toString());
                BigDecimal realMoney1 = new BigDecimal(Double.valueOf(r1.toString()) * 100);
                obj[i++] = realMoney1.divide(realMoney, 2, BigDecimal.ROUND_HALF_UP) + "%";
                
                Object r2 = map.get("realMoney2") == null ? 0 : map.get("realMoney2");
                obj[i++] = r2;
                
                BigDecimal realMoney2 = new BigDecimal(Double.valueOf(r2.toString()) * 100);
                obj[i++] = realMoney2.divide(realMoney, 2, BigDecimal.ROUND_HALF_UP) + "%";
                
                obj[i++] = (realMoney1.add(realMoney2)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                obj[i++] = (realMoney1.add(realMoney2)).divide(realMoney, 2, BigDecimal.ROUND_HALF_UP) + "%";
                rowContents.add(obj);
            }
        }
        return rowContents;
    }
    
    private void excel(HttpServletResponse response, String displayName, String[] headContent, List<Object[]> rowContents)
    {
        HSSFWorkbook workbook = null;
        OutputStream fOut = null;
        try
        {
            // 进行转码，使其支持中文文件名
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + new String(displayName.getBytes("UTF-8"), "ISO8859-1") + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            int rowCount = 0;
            // 写入excel文件头
            if (headContent != null && headContent.length > 0)
            {
                Row row = sheet.createRow(rowCount++);
                for (int i = 0; i < headContent.length; i++)
                {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(headContent[i]);
                }
            }
            // 写入excel内容
            if (rowContents != null && rowContents.size() > 0)
            {
                for (Object[] rowContent : rowContents)
                {
                    Row row = sheet.createRow(rowCount++);
                    for (int i = 0; i < rowContent.length; i++)
                    {
                        Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(String.valueOf(rowContent[i]));
                    }
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception ex)
        {
            logger.error("导出excle失败！！！", ex);
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
