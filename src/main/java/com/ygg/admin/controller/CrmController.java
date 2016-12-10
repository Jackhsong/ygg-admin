package com.ygg.admin.controller;

import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.service.CrmService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.MontnetsGGJCRMUtil;

/**
 * 用户关系管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("crm")
public class CrmController
{
    Logger logger = Logger.getLogger(CrmController.class);
    
    @Resource
    private CrmService crmService;
    
    
    @RequestMapping("toFilterUser")
    public ModelAndView toSelectUser() {
        ModelAndView mv = new ModelAndView("crm/filterUser");
        return mv;
    }
    
    @RequestMapping("toFilterList")
    public ModelAndView toFilterList() {
        ModelAndView mv = new ModelAndView("crm/filterList");
        return mv;
    }
    
    @RequestMapping("toFilterDetailList")
    public ModelAndView toFilterDetailList(int groupId) {
        ModelAndView mv = new ModelAndView("crm/filterDetailList");
        mv.addObject("groupId", groupId);
        return mv;
    }
    
    @RequestMapping("toSms")
    public ModelAndView toSms() throws Exception {
        ModelAndView mv = new ModelAndView("crm/sms");
        int montnets = new MontnetsGGJCRMUtil().getBalance();
        //int montnetsTuan = MontnetsTuanUtil.getBalance();
        //int montnetsGlobal = MontnetsGlobalUtil.getBalance();
        mv.addObject("montnets", (montnets > 0) ? montnets + "" : "查询失败");
        //mv.addObject("montnetsTuan", (montnetsTuan > 0) ? montnetsTuan + "" : "查询失败");
        //mv.addObject("montnetsGlobal", (montnetsGlobal > 0) ? montnetsGlobal + "" : "查询失败");
        return mv;
    }
    
    @RequestMapping("toSmsList")
    public ModelAndView toSmsData() throws Exception {
        ModelAndView mv = new ModelAndView("crm/smsList");
        return mv;
    }
    
    
    /**
     * 过滤用户
     * 
     * @param param
     * @param level
     * @param commentLevel
     * @param province
     * @param saleFlag
     * @param brand
     * @return
     */
    @RequestMapping("filterByParam")
    @ResponseBody
    public Object filterByParam(@RequestParam HashMap<String, String> param, String[] level, String[] commentLevel,
            String[] province, String[] saleFlag, String[] brand) {
        
        try
        {
            Map<String, Object> result = crmService.filterByParam(param, level, commentLevel, province, saleFlag, brand);
            result.put("status", 1);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    
    @RequestMapping("save")
    @ResponseBody
    public Object save(@RequestParam HashMap<String, String> param, String[] level, String[] commentLevel,
        String[] province, String[] saleFlag, String[] brand) {
        try
        {
            param.put("level", StringUtils.join(level));
            param.put("commentLevel", StringUtils.join(commentLevel));
            param.put("province", StringUtils.join(province));
            param.put("saleFlag", StringUtils.join(saleFlag));
            param.put("brand", StringUtils.join(brand));
            
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("data", crmService.saveFilterResult(param));
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    @RequestMapping("filterList")
    @ResponseBody
    public Object filterList(String startCount, String endCount, String groupId, String type,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, 
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
        try {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("startCount", StringUtils.isBlank(startCount) ? null : startCount);
            param.put("endCount", StringUtils.isBlank(endCount) ? null : endCount);
            param.put("id", StringUtils.isBlank(groupId) ? null : groupId);
            param.put("type", StringUtils.equals("-1", type) ? null : type);
            param.put("start", (page - 1) * rows);
            param.put("size", rows);
            
            return crmService.findGroupList(param);
        } catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    /**
     * 删除用户分组
     * 
     * @param id 分组id
     * @return
     */
    @RequestMapping("delete/{id}")
    @ResponseBody
    public Object deleteGroup(@PathVariable("id") int id) {
        try {
            return crmService.deleteGroupInfo(id);
        } catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    
    @RequestMapping("filterDetailList/{groupId}")
    @ResponseBody
    public Object filterDetailList(HttpServletResponse response,
        @PathVariable("groupId") int groupId, String export,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
        try {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("groupId", groupId);
            if(StringUtils.isBlank(export)) {
                param.put("start", (page - 1) * rows);
                param.put("size", rows);
                Map<String, Object> result = crmService.findGroupDetailByGroupId(param);
                return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
            } else {
                Map<String, Object> result = crmService.findGroupDetailByGroupId(param);
                exportGroupDetail(response, result);
                return null;
            }
        } catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    @SuppressWarnings("unchecked")
    private void exportGroupDetail(HttpServletResponse response, Map<String, Object> resultInfo) {
        String displayName = "筛选结果导出";
        String[] headContent = {"用户ID", "创建时间", "用户名", "用户类型", "昵称", "手机号码"};
        excel(response, displayName, headContent, map2arr((List<Map<String, Object>>)resultInfo.get("rows")));
    }
    
    private List<Object[]> map2arr(List<Map<String, Object>> result)
    {
        List<Object[]> rowContents = new ArrayList<Object[]>();
        if (result != null && result.size() > 0)
        {
            for (Map<String, Object> map : result)
            {
                int i = 0;
                Object[] obj = new Object[6];
                obj[i++] = map.get("accountId");
                obj[i++] = map.get("createTime");
                obj[i++] = map.get("name");
                obj[i++] = getTypeDesc(map.get("type"));
                obj[i++] = map.get("nickname");
                obj[i++] = map.get("phone");
                rowContents.add(obj);
            }
        }
        return rowContents;
    }
    
    private String getTypeDesc(Object type) {
        String s = String.valueOf(type);
        if(StringUtils.equals("1", s)) return "手机";
        else if(StringUtils.equals("2", s)) return "QQ";
        else if(StringUtils.equals("3", s)) return "微信";
        else if(StringUtils.equals("4", s)) return "新浪";
        else if(StringUtils.equals("5", s)) return "支付宝";
        else if(StringUtils.equals("6", s)) return "微信拼团";
        else if(StringUtils.equals("7", s)) return "APP拼团";
        else if(StringUtils.equals("8", s)) return "左岸城堡";
        else if(StringUtils.equals("9", s)) return "燕网";
        else return s;
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
            ex.printStackTrace();
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
     * 发送短息
     * @param phone
     * @param content
     * @param groupId
     * @param contentType
     * @param filterType
     * @param filterDay
     * @return
     */
    @RequestMapping("sendMessage")
    @ResponseBody
    public Object sendMessage(String sendType, String phone,String content, int groupId, int contentType, 
            int filterType, String filterDay, String longUrl, String shortUrl, int linkInfoId, String sendTime) {
        try
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("data", crmService.saveSms(sendType, phone, content, groupId, contentType, filterType, filterDay, longUrl, shortUrl, linkInfoId, sendTime));
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    /**
     * 生成短连接
     * @param url
     * @return
     */
    @RequestMapping("shortUrl")
    @ResponseBody
    public Object shortUrl(HttpServletRequest request, String longUrl, String groupId) {
        try
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("data", crmService.generateShortUrl(longUrl, groupId));
            return result;
        }
        catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    /**
     * 短信中点击短链接回调统计
     * @param request
     * @param id
     */
    @RequestMapping("statistics/{id}")
    @ResponseBody
    public void statistics(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") int id) {
        try
        {
            response.addHeader("Access-Control-Allow-origin", "*");
            crmService.updateStatistics(CommonUtil.getRemoteIpAddr(request), id);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
        return ;
    }
    
    /**
     * 短信列表
     * @param startTime
     * @param endTime
     * @param id
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("smsList")
    @ResponseBody
    public Object smsList(String startTime, String endTime, String id,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, 
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows) {
        try {
            page = page == 0 ? 1 : page;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("startTime", startTime);
            param.put("endTime", endTime);
            param.put("id", id);
            param.put("start", (page - 1) * rows);
            param.put("size", rows);
            return JSON.toJSONStringWithDateFormat(crmService.smsList(param), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
    
    /**
     * 统计短信id对应的数据
     * @param id
     * @return
     */
    @RequestMapping("statisticsResult/{id}")
    @ResponseBody
    public Object statisticsResult(@PathVariable("id") int id) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            result.put("data", crmService.statisticsResult(id));
            return result;
        } catch (Exception e)
        {
            logger.error(e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", e.getMessage());
            return result;
        }
    }
}
