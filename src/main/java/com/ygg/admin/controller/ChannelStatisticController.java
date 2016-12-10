package com.ygg.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.ChannelStatisticService;


/**
 * 第三方平台管理-数据统计-渠道统计
 * @author Qiu,Yibo
 *
 */
@Controller
@RequestMapping("/channelStatistic")
public class ChannelStatisticController
{
    private static Logger logger = Logger.getLogger(ChannelStatisticController.class);

    @Resource
    private ChannelStatisticService channelStatisticService;
    // type(-1,0,1)->(渠道统计,商品统计-金额,商品统计-数量)
    private int type = -1;
    
    @RequestMapping("/channelStatistic")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("channel/channelStatistic");
        return mv;
    }
    
    
    @RequestMapping(value = "/getAllChannelName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计-获取所有渠道名称")
    public String getAllChannelName(HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            List<String> list = channelStatisticService.getAllChannelNameAndId(type);
            List<Map<String,String>> channeNameAndIdList = new ArrayList<Map<String,String>>();
            Map<String,String> orderTimeMap  = new HashMap<String,String>();
            orderTimeMap.put("orderTime","日期");
            channeNameAndIdList.add(orderTimeMap);
            for(String nameAndId:list){
                Map<String,String> map  = new HashMap<String,String>();
                map.put("channelNameAndId", nameAndId);
                channeNameAndIdList.add(map);
            }
            Map<String,String> totalPriceMap  = new HashMap<String,String>();
            totalPriceMap.put("totalPrice", "总计");
            channeNameAndIdList.add(totalPriceMap);
            return JSON.toJSONString(channeNameAndIdList);
        }
        catch (Exception e)
        {
            logger.error("获取所有渠道名称", e);
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    
    @RequestMapping(value = "/jsonChannelStatisticInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "渠道统计-获取渠道信息")
    public String jsonChannelProInfo(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,//
            @RequestParam(value = "rows", required = true, defaultValue = "50") int rows,// 
            @RequestParam(value = "orderTime", required = true) String orderTime
           )
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0) page = 1;
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            //默认当月，取最小和最大日期
            String[] yearAndMon = StringUtils.split(orderTime,"-");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,Integer.valueOf(yearAndMon[0]));
            cal.set(Calendar.MONTH, Integer.valueOf(yearAndMon[1])-1);
            int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            para.put("startTime", orderTime + "-"+firstDay+" 00:00:00");
            para.put("endTime", orderTime + "-"+lastDay+" 23:59:59");
            resultMap = channelStatisticService.jsonChannelStatisticInfo(para);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/exportChannelStatistic")
    @ResponseBody
    @ControllerLog(description = "渠道统计-导出渠道信息")
    public void exportChannelPro(@RequestParam(value = "orderTime", required = false) String orderTime,
            HttpServletResponse response) throws IOException
    {
        
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        Map<String, Object> statusMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if(StringUtils.isNotEmpty(orderTime)){
                para.put("orderTime", orderTime);
            }
            @SuppressWarnings("unchecked")
            List<String> list = channelStatisticService.getAllChannelNameAndId(type);
            Map<String,String> channelIdAndNameMap = new LinkedHashMap<String,String>();
            channelIdAndNameMap.put("orderTime", "日期");
            for(String channel:list){
                String[] nameAndId = StringUtils.split(channel, "%");
                channelIdAndNameMap.put("channelId"+nameAndId[1], nameAndId[0]);
            }
            channelIdAndNameMap.put("totalPrice", "合计");
            
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) channelStatisticService.jsonChannelStatisticInfo(para).get("rows");
            
            //TODO refact
            //建立动态列
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("渠道统计");
            Row row = sheet.createRow(0);
            int headIndex = 0;
            for(Map.Entry<String, String> entry:channelIdAndNameMap.entrySet()){
                sheet.setColumnWidth(headIndex, 5500);
                Cell cell = row.createCell(headIndex);
                cell.setCellValue(entry.getValue());
                headIndex++;
            }
            
            for(int i=0;i<resultList.size();i++){
                Row r = sheet.createRow(i + 1);
                Map<String, Object> map = (Map<String, Object>)resultList.get(i);
                List<String> valueList = new ArrayList<String>();
                for(Map.Entry<String, String> entry:channelIdAndNameMap.entrySet()){
                    String key = entry.getKey();
                    if(!map.containsKey(key)) map.put(key,"");
                    valueList.add(String.valueOf(map.get(key)));
                }
                for(int cellInde=0;cellInde<headIndex;cellInde++){
                    r.createCell(cellInde).setCellValue(valueList.get(cellInde));;
                }
            }
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "渠道统计";
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            String errorStr = "<script>alert('导出失败');window.history.back();</script>";
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
