package com.ygg.admin.controller;

import java.io.OutputStream;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.OverseasDelayRemindDateService;

@Controller
@RequestMapping("/delayDate")
public class OverseasDelayRemindDateController
{
    @Resource
    private OverseasDelayRemindDateService delayRemindDateService;
    
    private static final Logger log = Logger.getLogger(OverseasDelayRemindDateController.class);
    
    @RequestMapping("/timeList")
    public ModelAndView sendSmsTimeList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sms/timeList");
        return mv;
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-删除短信发送时间")
    public String delete(@RequestParam(value = "idStr") String ids)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            List<Integer> idList = new ArrayList<Integer>();
            if ("".equals(ids) || ids == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "该分类正在使用中");
                return JSON.toJSONString(resultMap);
            }
            String[] idArr = ids.split(",");
            if (idArr != null && idArr.length > 0)
            {
                for (String id : idArr)
                {
                    idList.add(Integer.valueOf(id));
                }
            }
            para.put("idList", idList);
            int result = delayRemindDateService.delete(para);
            if (result > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "删除失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "删除失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-新增/编辑短信发送时间")
    public String saveOrUpdate(@RequestParam(value = "id", required = true, defaultValue = "0") int id, @RequestParam(value = "sendDate", required = true) String day,
        @RequestParam(value = "beginTime", required = true, defaultValue = "0") int startHour, @RequestParam(value = "endTime", required = true, defaultValue = "24") int endHour)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        para.put("id", id);
        para.put("day", day);
        para.put("startHour", startHour);
        para.put("endHour", endHour);
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id == 0 ? null : id + "");
            map.put("day", day);
            if (delayRemindDateService.isExist(map))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "[" + day + "]已经存在");
                return JSON.toJSONString(resultMap);
            }
            if (endHour <= startHour)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "结束时间不能小于开始时间");
                return JSON.toJSONString(resultMap);
            }
            int resultStatus = delayRemindDateService.saveOrUpdate(para);
            if (resultStatus == 1)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "保存成功");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        Map<String, Object> info = delayRemindDateService.jsonInfo(para);
        return JSON.toJSONString(info);
    }
    
    @RequestMapping(value = "/importDelayDate", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "短信管理-导入短信发送时间")
    public String importDelayDate(HttpServletRequest request, @RequestParam("orderFile") MultipartFile file, @RequestParam(value = "importYear", required = true) String year)
        throws Exception
    {
        
        Map<String, Object> map = new HashMap<String, Object>();
        try
        {
            List<String> permitType = new ArrayList<String>()
            {
                {
                    add("xls");
                    // add("xlsx");
                }
            };
            
            if (file == null)
            {
                map.put("status", 0);
                map.put("msg", "文件不能为空");
                return JSON.toJSONString(map);
            }
            String realName = file.getOriginalFilename();
            String fileSuffix = realName.substring(realName.lastIndexOf(".") + 1);
            if (!permitType.contains(fileSuffix))
            {
                map.put("status", 0);
                map.put("msg", "只允许上传excel文件");
                return JSON.toJSONString(map);
            }
            
            if ("".equals(year) || year == null)
            {
                map.put("status", 0);
                map.put("msg", "请选择年份");
                return JSON.toJSONString(map);
            }
            
            map = delayRemindDateService.importDelayDate(file, year);
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            map.put("status", 0);
            map.put("msg", e.getMessage());
            return JSON.toJSONString(map);
        }
    }
    
    @RequestMapping(value = "/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "模板";
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
            String[] str = {"日期", "开始时间(小时)", "结束时间(小时)"};
            Row row = sheet.createRow(1);
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
            e.printStackTrace();
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
