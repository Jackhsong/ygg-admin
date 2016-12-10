package com.ygg.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.AppPushInfoTypeEnum;
import com.ygg.admin.code.ProposeTypeEnum;
import com.ygg.admin.service.AccountService;
import com.ygg.admin.service.AppPushService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.util.StringUtils;

@Controller
@RequestMapping("/account")
public class AccountController
{
    
    Logger log = Logger.getLogger(AccountController.class);
    
    @Resource
    AccountService accountService;
    
    @Resource
    private AppPushService appPushService;
    
    /**
     * 用户列表
     * 
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("account/search");
        return mv;
    }
    
    /**
     * 异步获取用户信息
     * 
     * @param request
     * @param page
     * @param rows
     * @param name
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,// 
        @RequestParam(value = "name", required = false, defaultValue = "") String name,//
        @RequestParam(value = "id", required = false, defaultValue = "") String id,//
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,//
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,//
        @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname,//
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber//
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(id.trim()) && StringUtils.isNumeric(id.trim()))
        {
            para.put("id", id.trim());
        }
        if (!"".equals(nickname))
        {
            para.put("nickname", "%" + nickname + "%");
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("createTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("createTimeEnd", startTimeEnd);
        }
        Map<String, Object> result = accountService.jsonAccountInfo(para, 1);
        return JSON.toJSONString(result);
    }
    
    /**
     * 用户列表导出
     * @param request
     * @param response
     * @param name
     * @param id
     * @param startTimeBegin
     * @param startTimeEnd
     * @param nickname
     * @param mobileNumber
     * @throws Exception
     */
    @RequestMapping(value = "/exportAccount")
    @ControllerLog(description = "用户管理-导出用户")
    public void exportAccount(HttpServletRequest request, HttpServletResponse response,// 
        @RequestParam(value = "name", required = false, defaultValue = "") String name,//
        @RequestParam(value = "id", required = false, defaultValue = "") String id,//
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,//
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,//
        @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname,//
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber//
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(id.trim()) && StringUtils.isNumeric(id.trim()))
        {
            para.put("id", id.trim());
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("createTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("createTimeEnd", startTimeEnd);
        }
        if (!"".equals(nickname))
        {
            para.put("nickname", "%" + nickname + "%");
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        OutputStream fOut = null;
        Workbook workbook = null;
        String errorMessage = "";
        try
        {
            if (accountService.getJsonAccountInfoNums(para, 1) > CommonConstant.workbook_num_5w)
            {
                errorMessage = "数据量超过" + CommonConstant.workbook_num_5w + "，请缩小范围！";
                throw new RuntimeException();
            }
            
            Map<String, Object> result = accountService.jsonAccountInfo(para, 1);
            List<Map<String, Object>> rowsList = (List<Map<String, Object>>)result.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "用户列表";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            String[] str = {"ID", "创建时间", "用户名", "用户类型", "昵称", "手机号码", "累计成交金额"};
            workbook = POIUtil.createXSSFWorkbookTemplate(str);
            Sheet sheet = workbook.getSheetAt(0);
            int index = 1;
            for (Map<String, Object> currMap : rowsList)
            {
                Row r = sheet.createRow(index++);
                r.createCell(0).setCellValue(currMap.get("id") + "");
                r.createCell(1).setCellValue(currMap.get("createTime") + "");
                r.createCell(2).setCellValue(currMap.get("name") + "");
                r.createCell(3).setCellValue(currMap.get("type") + "");
                r.createCell(4).setCellValue(currMap.get("nickname") + "");
                r.createCell(5).setCellValue(currMap.get("mobileNumber") + "");
                r.createCell(6).setCellValue(currMap.get("totalAmount") == null ? "0.0" : currMap.get("totalAmount") + "");
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
    
    /**
     * 修改密码
     * 
     * @param request
     * @param pwd
     * @param _pwd
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatePWD", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-修改用户密码")
    public String updatePWD(HttpServletRequest request, @RequestParam(value = "pwd", required = false, defaultValue = "") String pwd,
        @RequestParam(value = "pwd1", required = false, defaultValue = "") String _pwd, @RequestParam(value = "editId", required = false, defaultValue = "0") int id)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (!pwd.equals(_pwd))
        {
            result.put("status", 0);
            result.put("msg", "两次输入的密码不一致");
            return JSON.toJSONString(result);
        }
        if (!StringUtils.conformPWD(pwd))
        {
            result.put("status", 0);
            result.put("msg", "密码只允许6-12位的数字和26个英文字符的组合");
            return JSON.toJSONString(result);
        }
        try
        {
            Map<String, Object> map = accountService.updatePWD(id, pwd);
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 用户账户信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/accountCards")
    public ModelAndView accountCards(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("account/accountCards");
        return mv;
    }
    
    /**
     * 异步获取用户账户信息
     * 
     * @param request
     * @param page
     * @param rows
     * @param cardName
     * @param accountName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/accountCardsJsonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String accountCardsJsonInfo(HttpServletRequest request,// 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "data", required = false, defaultValue = "1") int data,//
        @RequestParam(value = "cardName", required = false, defaultValue = "") String cardName,//
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(cardName))
        {
            para.put("cardName", "%" + cardName + "%");
        }
        if (!"".equals(accountName))
        {
            para.put("accountName", "%" + accountName + "%");
        }
        
        Map<String, Object> re = null;
        if (data == 1)
        {
            re = accountService.findAllAccountCard(para);
        }
        else
        {
            re = new HashMap<String, Object>();
            re.put("rows", new ArrayList());
            re.put("total", 0);
        }
        return JSON.toJSONString(re);
    }
    
    /**
     * 用户积分管理
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralList")
    public ModelAndView integralList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("account/integralList");
        return mv;
    }
    
    /**
     * 异步获取用户积分信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("jsonIntegral")
    @ResponseBody
    public String jsonIntegral(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,//
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,//
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId, // 用户ID
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber, // 手机号
        @RequestParam(value = "name", required = false, defaultValue = "") String name // 用户名
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (accountId != 0)
        {
            para.put("id", accountId);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        if (!"".equals(startTimeBegin))
        {
            para.put("createTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("createTimeEnd", startTimeEnd);
        }
        para.put("orderByIntegral", 1);
        Map<String, Object> resultMap = accountService.findAllAccountIntegral(para);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 用户积分导出
     * @throws Exception
     */
    @RequestMapping(value = "/export")
    @ControllerLog(description = "用户管理-导出用户积分")
    public void export(HttpServletRequest request,
        HttpServletResponse response, //
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,//
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,//
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId, // 用户ID
        @RequestParam(value = "searchName", required = false, defaultValue = "") String name,
        @RequestParam(value = "searchMobileNumber", required = false, defaultValue = "") String searchMobileNumber)
        // 用户名
        throws Exception
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        PrintWriter ot = null;
        String errorMessage = "";
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (accountId != 0)
            {
                para.put("id", accountId);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (!"".equals(searchMobileNumber))
            {
                para.put("mobileNumber", searchMobileNumber);
            }
            if (!"".equals(startTimeBegin))
            {
                para.put("createTimeBegin", startTimeBegin);
            }
            if (!"".equals(startTimeEnd))
            {
                para.put("createTimeEnd", startTimeEnd);
            }
            para.put("orderByIntegral", 1);
            if (accountService.getAllAccountIntegralNums(para) > CommonConstant.workbook_num_5w)
            {
                errorMessage = "数据量超过" + CommonConstant.workbook_num_5w + "，请缩小范围！";
                throw new RuntimeException();
            }
            Map<String, Object> resultMap = accountService.findAllAccountIntegral(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)resultMap.get("rows");
            if (rows.size() > 0)
            {
                response.setContentType("application/vnd.ms-excel");
                String codedFileName = "用户积分记录";
                // 进行转码，使其支持中文文件名
                codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
                // 产生工作簿对象
                workbook = new HSSFWorkbook();
                // 产生工作表对象
                HSSFSheet sheet = workbook.createSheet();
                String[] str = {"ID", "用户名", "手机号", "用户类型", "积分余额"/*, "最后更新时间"*/};
                Row row = sheet.createRow(0);
                for (int i = 0; i < str.length; i++)
                {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(str[i]);
                }
                for (int i = 0; i < rows.size(); i++)
                {
                    Map<String, Object> currMap = rows.get(i);
                    Row r = sheet.createRow(i + 1);
                    r.createCell(0).setCellValue(currMap.get("id") + "");
                    r.createCell(1).setCellValue(currMap.get("name") + "");
                    r.createCell(2).setCellValue(currMap.get("mobileNumber") + "");
                    r.createCell(3).setCellValue(currMap.get("typeStr") + "");
                    r.createCell(4).setCellValue(currMap.get("integral") + "");
                    //r.createCell(5).setCellValue(currMap.get("lastIntegralUpdateTime") + "");
                }
                fOut = response.getOutputStream();
                workbook.write(fOut);
                fOut.flush();
                return;
            }
            else
            {
                // 无数据 返回空的zip
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=utf-8");
                ot = response.getWriter();
                ot.println("<script>alert('无数据');window.history.back();</script>");
            }
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
                catch (IOException e)
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
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (ot != null)
            {
                ot.close();
            }
        }
    }
    
    /**
     * 调整用户积分
     * 
     * @param request
     * @param id
     * @param addIntegral
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateIntegral", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-调整用户积分")
    public String updateIntegral(HttpServletRequest request, //
        @RequestParam(value = "id", required = true) int id, // 用户ID
        @RequestParam(value = "addIntegral", required = true) int addIntegral, // 调整积分数
        @RequestParam(value = "isForcibly", required = false, defaultValue = "0") int isForcibly, // 积分不足时 是否 扣除 至0
        @RequestParam(value = "source", required = false, defaultValue = "accountList") String source, //积分调整来源
        @RequestParam(value = "reason", required = false, defaultValue = "") String reason//积分调整原因
    )
        throws Exception
    {
        Map<String, Object> resultMap = accountService.updateAccountIntegral(id, addIntegral, isForcibly == 1, source, reason);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 用户积分详细操作记录
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/integralRecordList/{id}")
    public ModelAndView integralRecordList(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("account/integralRecordList");
        mv.addObject("id", id + "");
        mv.addObject("account", accountService.findAccountById(id));
        return mv;
    }
    
    /**
     * 异步获取用户积分变动明细
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonIntegralRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonIntegralRecord(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "id", required = true) int accountId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (accountId != 0)
        {
            para.put("accountId", accountId);
        }
        Map<String, Object> resultMap = accountService.findAccountIntegralRecord(para);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 用户app推送用户列表
     * @return
     * @throws Exception
     */
    @RequestMapping("/appPushList")
    public ModelAndView appPushList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView("account/appPushList");
        List<Map<String, Object>> accountTypeList = new ArrayList<Map<String, Object>>();
        for (AccountEnum.ACCOUNT_TYPE e : AccountEnum.ACCOUNT_TYPE.values())
        {
            int code = e.getCode();
            String name = e.getDesc();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", code);
            map.put("name", name);
            accountTypeList.add(map);
        }
        accountTypeList.add(0, new HashMap<String, Object>()
        {
            {
                put("code", "0");
                put("name", "全部");
            }
        });
        mv.addObject("accountTypeList", accountTypeList);
        List<Map<String, Object>> pushTypeList = new ArrayList<Map<String, Object>>();
        for (AppPushInfoTypeEnum e : AppPushInfoTypeEnum.values())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", e.ordinal());
            map.put("name", e.getDescription());
            pushTypeList.add(map);
        }
        mv.addObject("pushTypeList", pushTypeList);
        return mv;
    }
    
    /**
     * 异步获取app推送用户列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonAppPushList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonAppPushList(
        HttpServletRequest request, //
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,//
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "cid", required = false, defaultValue = "") String cid, // 个推cid
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber, // 
        @RequestParam(value = "type", required = false, defaultValue = "0") byte type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(startTimeBegin))
        {
            para.put("createTimeBegin", startTimeBegin);
        }
        if (!"".equals(startTimeEnd))
        {
            para.put("createTimeEnd", startTimeEnd);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        if (id != 0)
        {
            para.put("id", id);
        }
        if (!"".equals(cid))
        {
            para.put("cid", cid);
        }
        if (type != 0)
        {
            para.put("type", type);
        }
        
        Map<String, Object> result = accountService.jsonAccountInfo(para, 2);
        return JSON.toJSONString(result);
    }
    
    /**
     * 导出推送用户列表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportPushAccount")
    @ControllerLog(description = "用户管理-导出推送用户列表")
    public void exportPushAccount(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "name", required = false, defaultValue = "") String name,//
        @RequestParam(value = "startTimeBegin", required = false, defaultValue = "") String startTimeBegin,//
        @RequestParam(value = "startTimeEnd", required = false, defaultValue = "") String startTimeEnd,//
        @RequestParam(value = "id", required = false, defaultValue = "0") int id, //
        @RequestParam(value = "cid", required = false, defaultValue = "") String cid, // 个推cid
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber, // 
        @RequestParam(value = "type", required = false, defaultValue = "0") byte type)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "推送用户列表";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        String errorMessage = "";
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(startTimeBegin))
            {
                para.put("createTimeBegin", startTimeBegin);
            }
            if (!"".equals(startTimeEnd))
            {
                para.put("createTimeEnd", startTimeEnd);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (!"".equals(mobileNumber))
            {
                para.put("mobileNumber", mobileNumber);
            }
            if (id != 0)
            {
                para.put("id", id);
            }
            if (!"".equals(cid))
            {
                para.put("cid", cid);
            }
            if (type != 0)
            {
                para.put("type", type);
            }
            if (accountService.getJsonAccountInfoNums(para, 2) > CommonConstant.workbook_num_5w)
            {
                errorMessage = "数据量超过" + CommonConstant.workbook_num_5w + "，请缩小范围！";
                throw new RuntimeException();
            }
            Map<String, Object> result = accountService.jsonAccountInfo(para, 2);
            List<Map<String, Object>> rowsList = (List<Map<String, Object>>)result.get("rows");
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"个推CID", "用户ID", "用户名", "用户类型"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            int index = 1;
            for (Map<String, Object> currMap : rowsList)
            {
                Row r = sheet.createRow(index++);
                r.createCell(0).setCellValue(currMap.get("pushId") + "");
                r.createCell(1).setCellValue(currMap.get("id") + "");
                r.createCell(2).setCellValue(currMap.get("name") + "");
                r.createCell(3).setCellValue(currMap.get("typeStr") + "");
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.close();
        }
        catch (Exception e)
        {
            if (errorMessage.equals(""))
            {
                log.error(e.getMessage(), e);
                errorMessage = "系统出错";
            }
            response.setHeader("content-disposition", "");
            response.setContentType("text/html;charset=utf-8");
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
                catch (IOException e)
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
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 推送action
     * @return
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-推送消息")
    public String push(
        HttpServletRequest request, //
        @RequestParam(value = "operationType", required = false, defaultValue = "1") int operationType, // 1为根据pids推送，2为根据userFile推送
        @RequestParam(value = "ids", required = false, defaultValue = "") String ids, //
        @RequestParam(value = "pids", required = false, defaultValue = "") String pids, //
        @RequestParam(value = "userIdFile", required = false) MultipartFile file,
        @RequestParam(value = "pushType", required = true) byte pushType, //
        @RequestParam(value = "pushUrl", required = false, defaultValue = "") String pushUrl,
        @RequestParam(value = "pushNumber", required = false, defaultValue = "") String pushNumber,
        @RequestParam(value = "pushProductId", required = false, defaultValue = "") String pushProductId,
        @RequestParam(value = "pushWindowId", required = false, defaultValue = "") String pushWindowId,
        @RequestParam(value = "pushTitle", required = false, defaultValue = "") String pushTitle,
        @RequestParam(value = "pushContent", required = false, defaultValue = "") String pushContent)
        throws Exception
    {
        // 先封装用户ID数据
        List<String> idList = new ArrayList<String>();
        Set<String> pidList = new HashSet<String>();
        if (operationType == 1)
        {
            if (ids.indexOf(",") > -1)
            {
                idList = Arrays.asList(ids.split(","));
                for (String it : Arrays.asList(pids.split(",")))
                {
                    pidList.add(it);
                }
            }
            else
            {
                idList.add(ids);
                pidList.add(pids);
            }
        }
        else
        {
            boolean flag = true;
            String msg = "";
            if (file == null)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "文件有误");
                return JSON.toJSONString(result);
            }
            try
            {
                Workbook workbook = new HSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);
                int startNum = sheet.getFirstRowNum();
                int lastNum = sheet.getLastRowNum();
                if (startNum == lastNum)
                {// 可过滤第一行，因为第一行是标题
                    flag = false;
                    msg = "文件为空";
                }
                else
                {
                    for (int i = startNum + 1; i <= lastNum; i++)
                    {
                        Cell cell0 = sheet.getRow(i).getCell(0);
                        if (cell0 != null)
                        {
                            cell0.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        String accountId = cell0 == null ? null : cell0.getStringCellValue().trim();
                        if (accountId == null || !StringUtils.isNumeric(accountId))
                        {
                            flag = false;
                            msg = "文件第" + (i + 1) + "行有错误";
                            break;
                        }
                        else
                        {
                            List<String> pushIdList = accountService.findAccountPushIdByAccountId(Integer.valueOf(accountId));
                            if (pushIdList.size() == 0)
                            {
                                flag = false;
                                msg = "文件第" + (i + 1) + "行用户ID没有对应的个推CID";
                                break;
                            }
                            idList.add(accountId);
                            pidList.addAll(pushIdList);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                flag = false;
                msg = "文件格式有误";
            }
            if (!flag)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", msg);
                return JSON.toJSONString(result);
            }
        }
        
        //验证表单数据
        if ((pushType == AppPushInfoTypeEnum.CUSTOM_PAGE.ordinal()) && ("".equals(pushUrl)))
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "推送类型为自定义页面时，URL必填。");
            return JSON.toJSONString(result);
        }
        else if (pushType == AppPushInfoTypeEnum.ORDER_DETAIL.ordinal())
        {
            Map<String, Object> result = new HashMap<String, Object>();
            boolean flag = false;
            if ("".equals(pushNumber))
            {
                flag = true;
                result.put("status", 0);
                result.put("msg", "推送类型为订单详情页时，订单编号必填。");
            }
            else if (idList.size() > 1)
            {
                flag = true;
                result.put("status", 0);
                result.put("msg", "不可以同时给多个用户推送订单详情页");
            }
            if (flag)
            {
                return JSON.toJSONString(result);
            }
        }
        else if (pushType == AppPushInfoTypeEnum.SALE_PRODUCT_SELLING.ordinal() || pushType == AppPushInfoTypeEnum.MALL_PRODUCT_SELLING.ordinal())
        {
            if ("".equals(pushProductId) || Pattern.matches("/^\\d+$/", pushProductId))
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "推送类型为特卖或商城商品促销时，商品Id必填。");
                return JSON.toJSONString(result);
            }
        }
        else if (pushType == AppPushInfoTypeEnum.SALE_WINDOW_SELLING.ordinal() || pushType == AppPushInfoTypeEnum.MALL_WINDOW_SELLING.ordinal())
        {
            if ("".equals(pushWindowId) || Pattern.matches("/^\\d+$/", pushWindowId))
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "推送类型为特卖或商城专场促销时，专场Id必填。");
                return JSON.toJSONString(result);
            }
        }
        //发起推送请求
        Map<String, Object> result = appPushService.pushMessage(idList, pidList, pushType, pushUrl, pushNumber, pushTitle, pushContent, pushProductId, pushWindowId);
        return JSON.toJSONString(result);
    }
    
    /**
     * 下载用户推送模板
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/downloadAppPushTemplate")
    public void downloadAppPushTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "用户推送导入模板";
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
            String[] str = null;
            str = new String[] {"用户Id"};
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue(str[0]);
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
                catch (IOException e)
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
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 新增用户账户
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveAccountCard", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-新增用户支付账户")
    public String saveAccountCard(HttpServletRequest request, //
        @RequestParam(value = "accountId", required = true) int accountId, // 用户ID
        @RequestParam(value = "bankOrAlipay", required = true) int bankOrAlipay, //  账户类型，1银行 2支付宝
        @RequestParam(value = "cardName", required = false, defaultValue = "") String cardName, //
        @RequestParam(value = "cardNumber", required = true) String cardNumber,//
        @RequestParam(value = "bankType", required = true) int bankType // 银行类型
    )
        throws Exception
    {
        try
        {
            int id = accountService.saveAccountCard(accountId, bankOrAlipay, cardName, cardNumber, bankType);
            Map<String, Object> map = new HashMap<String, Object>();
            if (id > 0)
            {
                map.put("status", 1);
                String name = "";
                if (bankOrAlipay == 1)
                {
                    name = "银行-" + CommonEnum.BankTypeEnum.getDescriptionByOrdinal(bankType) + "-" + cardNumber;
                }
                else
                {
                    name = "支付宝-" + cardNumber;
                }
                map.put("name", name);
                map.put("id", id);
            }
            else
            {
                map.put("status", 0);
                map.put("msg", "保存失败");
            }
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败，请检查是否已经添加过改账户信息。");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 修正没有recommended_code的用户记录
     */
    @RequestMapping(value = "/resetRecommendedCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String resetRecommendedCode()
        throws Exception
    {
        try
        {
            int result = accountService.resetRecommendedCode();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 1);
            map.put("msg", "成功更新" + result + "个用户。");
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "修改失败！");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 用户反馈列表
     * @return
     */
    @RequestMapping("/proposeList")
    public ModelAndView proposeList()
    {
        ModelAndView mv = new ModelAndView("account/proposeList");
        return mv;
    }
    
    /**
     * 用户反馈积分列表
     * @param page
     * @param rows
     * @param accountId：用户ID
     * @param accountName：用户名
     * @param proposeType：反馈类型
     * @return
     */
    @RequestMapping(value = "/jsonProposeInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonGateInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,
        @RequestParam(value = "proposeType", required = false, defaultValue = "0") int proposeType,
        @RequestParam(value = "isDeal", required = false, defaultValue = "-1") int isDeal,
        @RequestParam(value = "isBackPoint", required = false, defaultValue = "-1") int isBackPoint)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (accountId != 0)
            {
                para.put("accountId", accountId);
            }
            if (!"".equals(accountName))
            {
                para.put("accountName", accountName);
            }
            if (proposeType != 0)
            {
                para.put("proposeType", proposeType);
            }
            if (isDeal != -1)
            {
                para.put("isDeal", isDeal);
            }
            if (isBackPoint != -1)
            {
                para.put("isBackPoint", isBackPoint);
            }
            resultMap = accountService.findAllProposeInfo(para);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/exportProposeInfo")
    @ControllerLog(description = "用户管理-导出用户反馈")
    public void exportProposeInfo(
        HttpServletRequest request,
        HttpServletResponse response,//
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,
        @RequestParam(value = "proposeType", required = false, defaultValue = "0") int proposeType,
        @RequestParam(value = "isDeal", required = false, defaultValue = "-1") int isDeal,
        @RequestParam(value = "isBackPoint", required = false, defaultValue = "-1") int isBackPoint)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (accountId != 0)
        {
            para.put("accountId", accountId);
        }
        if (!"".equals(accountName))
        {
            para.put("accountName", accountName);
        }
        if (proposeType != 0)
        {
            para.put("proposeType", proposeType);
        }
        if (isDeal != -1)
        {
            para.put("isDeal", isDeal);
        }
        if (isBackPoint != -1)
        {
            para.put("isBackPoint", isBackPoint);
        }
        OutputStream fOut = null;
        Workbook workbook = null;
        String errorMessage = "";
        try
        {
            Map<String, Object> resultMap = accountService.findAllProposeInfo(para);
            int total = Integer.valueOf(resultMap.get("total") + "");
            
            if (total > CommonConstant.workbook_num_3w)
            {
                errorMessage = "数据量超过" + CommonConstant.workbook_num_3w + "，请缩小范围！";
                throw new RuntimeException();
            }
            
            List<Map<String, Object>> rowsList = (List<Map<String, Object>>)resultMap.get("rows");
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "用户反馈列表";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            String[] str = {"用户Id", "用户名", "联系方式", "反馈类型", "操作系统", "应用版本", "反馈时间", "反馈内容", "是否处理", "处理人", "处理说明", "是否已返积分"};
            workbook = POIUtil.createXSSFWorkbookTemplate(str);
            Sheet sheet = workbook.getSheetAt(0);
            int index = 1;
            for (Map<String, Object> currMap : rowsList)
            {
                Row r = sheet.createRow(index++);
                r.createCell(0).setCellValue(currMap.get("accountId") + "");
                r.createCell(1).setCellValue(currMap.get("accountName") + "");
                r.createCell(2).setCellValue(currMap.get("contact") + "");
                r.createCell(3).setCellValue(currMap.get("proposeType") + "");
                r.createCell(4).setCellValue(currMap.get("os") + "");
                r.createCell(5).setCellValue(currMap.get("version") + "");
                r.createCell(6).setCellValue(currMap.get("createTime") + "");
                r.createCell(7).setCellValue(currMap.get("content") + "");
                r.createCell(8).setCellValue(currMap.get("isDealDesc") + "");
                r.createCell(9).setCellValue(currMap.get("dealUser") + "");
                r.createCell(10).setCellValue(currMap.get("dealContent") + "");
                r.createCell(11).setCellValue(currMap.get("isBackPointDesc") + "");
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
    
    /**
     * 反馈返积分
     * @param id
     * @param accountId
     * @param backPoint
     * @return
     */
    @RequestMapping(value = "/returnScoreForPropose", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-用户反馈返积分")
    public String returnScoreForPropose(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,
        @RequestParam(value = "backPoint", required = false, defaultValue = "") String backPoint)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (id == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
                return JSON.toJSONString(resultMap);
            }
            if (accountId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "用户不存在");
                return JSON.toJSONString(resultMap);
            }
            if (!Pattern.matches("\\d+", backPoint))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "积分必须为正确的数字");
                return JSON.toJSONString(resultMap);
            }
            int score = Integer.valueOf(backPoint).intValue();
            int result = accountService.returnScoreForPropose(id, accountId, score);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else if (result == -1)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "用户不存在");
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 处理反馈
     * @param id
     * @param dealContent
     * @return
     */
    @RequestMapping(value = "/updateDealContent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-处理用户反馈")
    public String updateDealContent(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "dealContent", required = false, defaultValue = "") String dealContent)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (id == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
                return JSON.toJSONString(resultMap);
            }
            if (StringUtils.isEmpty(dealContent))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "处理说明不能为空");
                return JSON.toJSONString(resultMap);
            }
            int result = accountService.updateDealContent(id, dealContent);
            if (result == 1)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "保存失败");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/jsonProposeTypeCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProposeTypeCode(@RequestParam(value = "code", required = false, defaultValue = "0") int code)
        throws Exception
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        for (ProposeTypeEnum e : ProposeTypeEnum.values())
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", e.getCode() + "");
            map.put("text", e.getDesc());
            if (e.getCode() == code)
            {
                map.put("selected", "true");
            }
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 用户优惠券列表
     * @return
     */
    @RequestMapping("accountCouponList")
    public ModelAndView accountCouponList()
    {
        ModelAndView mv = new ModelAndView("account/accountCouponList");
        return mv;
    }
    
    /**
     * 异步加载用户优惠券列表
     * @param accountId：用户ID
     * @param accountName：用户名
     * @param phoneNumber：手机号
     * @param couponRemark：优惠券备注
     * @param acquireTimeBegin：优惠券获得起始时间
     * @param acquireTimeEnd：优惠券获得结束时间
     * @param isUsed：是否使用，1使用，0未使用
     * @param couponDetailType：优惠券详情类型：1：满xx减x，2：满0减x
     * @param reduceMin：优惠券面额最小值
     * @param reduceMax：优惠券面额最大值
     * @param validTimeBeginStart：有效期开始起始时间
     * @param validTimeBeginEnd：有效期开始结束时间
     * @param validTimeEndStart：有效期终止开始时间
     * @param validTimeEndEnd：有效期终止结束时间
     * @param couponDetailRemark：优惠券详情备注
     * @return
     */
    @RequestMapping(value = "/jsonAccountCouponList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonAccountCouponList(
        //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows,//
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,//
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,//
        @RequestParam(value = "phoneNumber", required = false, defaultValue = "") String phoneNumber,//
        @RequestParam(value = "couponRemark", required = false, defaultValue = "") String couponRemark,//
        @RequestParam(value = "acquireTimeBegin", required = false, defaultValue = "") String acquireTimeBegin,//
        @RequestParam(value = "acquireTimeEnd", required = false, defaultValue = "") String acquireTimeEnd,//
        @RequestParam(value = "isUsed", required = false, defaultValue = "-1") int isUsed,//
        @RequestParam(value = "couponDetailType", required = false, defaultValue = "-1") int couponDetailType,//
        @RequestParam(value = "reduceMin", required = false, defaultValue = "-1") int reduceMin,//
        @RequestParam(value = "reduceMax", required = false, defaultValue = "-1") int reduceMax,//
        @RequestParam(value = "validTimeBeginStart", required = false, defaultValue = "") String validTimeBeginStart,//
        @RequestParam(value = "validTimeBeginEnd", required = false, defaultValue = "") String validTimeBeginEnd,//
        @RequestParam(value = "validTimeEndStart", required = false, defaultValue = "") String validTimeEndStart,//
        @RequestParam(value = "validTimeEndEnd", required = false, defaultValue = "") String validTimeEndEnd,//
        @RequestParam(value = "couponDetailRemark", required = false, defaultValue = "") String couponDetailRemark,
        @RequestParam(value = "isSearch", required = false, defaultValue = "0") int isSearch)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            
            if (accountId != 0)
            {
                para.put("accountId", accountId);
            }
            
            if (!"".equals(accountName))
            {
                para.put("accountName", accountName);
            }
            if (!"".equals(phoneNumber))
            {
                para.put("phoneNumber", phoneNumber);
            }
            if (!"".equals(couponRemark))
            {
                para.put("couponRemark", "%" + couponRemark + "%");
            }
            if (!"".equals(acquireTimeBegin))
            {
                para.put("acquireTimeBegin", acquireTimeBegin);
            }
            if (!"".equals(acquireTimeEnd))
            {
                para.put("acquireTimeEnd", acquireTimeEnd);
            }
            if (isUsed != -1)
            {
                para.put("isUsed", isUsed);
            }
            if (couponDetailType != -1)
            {
                para.put("couponDetailType", couponDetailType);
            }
            if (reduceMin != -1)
            {
                para.put("reduceMin", reduceMin);
            }
            if (reduceMax != -1)
            {
                para.put("reduceMax", reduceMax);
            }
            if (!"".equals(validTimeBeginStart))
            {
                para.put("validTimeBeginStart", validTimeBeginStart);
            }
            if (!"".equals(validTimeBeginEnd))
            {
                para.put("validTimeBeginEnd", validTimeBeginEnd);
            }
            if (!"".equals(validTimeEndStart))
            {
                para.put("validTimeEndStart", validTimeEndStart);
            }
            if (!"".equals(validTimeEndEnd))
            {
                para.put("validTimeEndEnd", validTimeEndEnd);
            }
            if (!"".equals(couponDetailRemark))
            {
                para.put("couponDetailRemark", "%" + couponDetailRemark + "%");
            }
            if (isSearch == 1)
            {
                resultMap = accountService.findAllAccountCouponInfo(para);
                
            }
            else
            {
                resultMap.put("rows", new ArrayList<Map<String, Object>>());
                resultMap.put("total", 0);
            }
        }
        catch (Exception e)
        {
            log.error("异步加载用户优惠券信息出错了！！！", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/downloadIntegralTemplate")
    public void downloadIntegralTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "导入模板样式";
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
            String[] str = new String[] {"用户ID", "操作类型(1表示加，2表示减)", "积分(正整数)"};
            
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
    
    @RequestMapping(value = "/importAccountIntegral", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-导入积分")
    public String importAccountIntegral(HttpServletRequest request, @RequestParam("userFile") MultipartFile file)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (file == null)
            {
                resultMap.put("status", 1);
                resultMap.put("msg", "请选择文件");
                return JSON.toJSONString(resultMap);
            }
            return accountService.importAccountIntegral(file);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "导入出错了");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 用户黑名单
     * @return
     * @throws Exception
     */
    @RequestMapping("/blackList")
    public ModelAndView blackList()
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("account/blackList");
        return mv;
    }
    
    /**
     * 异步加载用户黑名单信息
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ajaxAccountBlacklist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String ajaxAccountBlacklist(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
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
        
        Map<String, Object> result = accountService.findAccountBlacklist(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 删除黑名单用户
     * @param accountId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteBlacklist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-删除黑名单用户")
    public String deleteBlacklist(@RequestParam(value = "accountId", required = true) int accountId)
        throws Exception
    {
        try
        {
            int status = accountService.deleteBlacklist(accountId);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("mag", status == 1 ? "删除成功" : "删除失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("删除黑名单用户失败！", e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "删除失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 添加黑名单用户
     * @param accountId
     * @param remark
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addBlacklist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "用户管理-添加黑名单用户")
    public String addBlacklist(@RequestParam(value = "accountId", required = true) int accountId, @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
        throws Exception
    {
        try
        {
            if (accountService.findAccountById(accountId) == null)
            {
                Map<String, Object> result = new HashMap<>();
                result.put("status", 0);
                result.put("msg", "用户不存在");
                return JSON.toJSONString(result);
            }
            int status = accountService.addBlacklist(accountId, remark);
            Map<String, Object> result = new HashMap<>();
            result.put("status", status);
            result.put("mag", status == 1 ? "添加成功" : "添加失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("添加黑名单用户失败！", e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "添加失败");
            return JSON.toJSONString(result);
        }
    }
    
}
