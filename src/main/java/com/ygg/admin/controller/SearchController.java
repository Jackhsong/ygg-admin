package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.SearchService;
import com.ygg.admin.util.POIUtil;

@Controller
@RequestMapping("/search")
public class SearchController
{
    
    Logger log = Logger.getLogger(SearchController.class);
    
    @Resource
    private SearchService searchService;
    
    /**
     * 热门搜索词
     * @return
     * @throws Exception
     */
    @RequestMapping("/hotKeywordList")
    public ModelAndView hotKeywordList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("search/hotKeywordList");
        return mv;
    }
    
    /**
     * 异步获取搜索词数据
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/jsonHotKeyword")
    @ResponseBody
    public String jsonHotKeyword(@RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        Map<String, Object> result = searchService.findAllSearchHotKeyword(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 异步保存
     * @param keyword
     * @param sequence
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveHotKeyword")
    @ResponseBody
    @ControllerLog(description = "搜索管理-新增热搜词汇")
    public String saveHotKeyword(@RequestParam(value = "id", required = false, defaultValue = "0") int id,//
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,//
        @RequestParam(value = "sequence", required = false, defaultValue = "0") int sequence)
        throws Exception
    {
        try
        {
            Map<String, Object> result = searchService.saveSearchHotKeyword(id, keyword, sequence);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("热搜词保存失败！" + e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 删除热搜词
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteHotKeyword")
    @ResponseBody
    @ControllerLog(description = "搜索管理-删除热搜词汇")
    public String deleteHotKeyword(@RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        try
        {
            int status = searchService.deleteSearchHotKeyword(id);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("msg", status == 1 ? "删除成功" : "删除失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("热搜词删除失败！" + e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "删除失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 搜索记录列表
     * @return
     * @throws Exception
     */
    @RequestMapping("/searchRecordList")
    public ModelAndView searchRecordList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("search/recordList");
        return mv;
    }
    
    /**
     * 异步获取搜索记录
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/jsonSearchRecord")
    @ResponseBody
    public String jsonSearchRecord(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(startTimeBegin))
        {
            para.put("startTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("startTimeEnd", startTimeEnd);
        }
        if (!"".equals(keyword))
        {
            para.put("keyword", keyword);
        }
        Map<String, Object> result = searchService.findAllSearchRecord(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 导出记录
     * @param response
     * @param startTimeBegin
     * @param startTimeEnd
     * @param keyword
     * @throws Exception
     */
    @RequestMapping(value = "/exportRecord")
    @ControllerLog(description = "搜索管理-导出热搜词汇")
    public void exportRecord(HttpServletResponse response,
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,
        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        String errorMessage = "";
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (!"".equals(startTimeBegin))
            {
                para.put("startTimeBegin", startTimeBegin);
            }
            if (!"".equals(startTimeEnd))
            {
                para.put("startTimeEnd", startTimeEnd);
            }
            if (!"".equals(keyword))
            {
                para.put("keyword", keyword);
            }
            int total = searchService.countAllSearchRecord(para);
            if (total > 50000)
            {
                errorMessage = "数据量超过5W，请缩小导出范围。";
                throw new RuntimeException("数据量超过5W，请缩小导出范围。");
            }
            Map<String, Object> result = searchService.findAllSearchRecord(para);
            List<Map<String, Object>> rowsList = (List<Map<String, Object>>)result.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "用户搜索记录";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            String[] str = {"用户ID", "用户名", "搜索词", "搜索结果数", "创建时间"};
            workbook = POIUtil.createXSSFWorkbookTemplate(str);
            Sheet sheet = workbook.getSheetAt(0);
            int index = 1;
            for (Map<String, Object> currMap : rowsList)
            {
                Row r = sheet.createRow(index++);
                r.createCell(0).setCellValue(currMap.get("accountId") + "");
                r.createCell(1).setCellValue(currMap.get("accountName") + "");
                r.createCell(2).setCellValue(currMap.get("keyword") + "");
                r.createCell(3).setCellValue(currMap.get("resultNum") + "");
                r.createCell(4).setCellValue(currMap.get("createTime") + "");
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            if (errorMessage.equals(""))
            {
                log.error(e.getMessage(), e);
                errorMessage = "系统出错";
            }
            response.setHeader("content-disposition", "");
            response.setContentType("text/html");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
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
