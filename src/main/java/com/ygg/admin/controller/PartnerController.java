package com.ygg.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.PartnerEnum;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.service.AccountService;
import com.ygg.admin.service.PartnerService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;

/**
 * 账号合伙人相关
 * @author xiongl
 *
 */
@Controller
@RequestMapping("/partner")
public class PartnerController
{
    private static Logger logger = Logger.getLogger(PartnerController.class);
    
    @Resource
    private PartnerService partnerService;
    
    @Resource
    private AccountService accountService;
    
    @Resource
    private SystemLogService logService;
    
    /**
     * 合伙人列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView partnerList()
    {
        ModelAndView mv = new ModelAndView("partner/partnerList");
        return mv;
    }
    
    /**
     * 合伙人申请列表
     * @return
     */
    @RequestMapping("/applyList")
    public ModelAndView partnerApplyList()
    {
        ModelAndView mv = new ModelAndView("partner/partnerApplyList");
        return mv;
    }
    
    /**
     * 合伙人信息列表
     * @param page
     * @param rows
     * @param userId：用户Id
     * @param username：用户名
     * @param inviteCode：邀请码
     * @param realName：真实姓名
     * @return
     */
    @RequestMapping(value = "/jsonPartnerInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPartnerInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,
        @RequestParam(value = "username", required = false, defaultValue = "") String username,
        @RequestParam(value = "inviteCode", required = false, defaultValue = "") String inviteCode,
        @RequestParam(value = "realName", required = false, defaultValue = "") String realName)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (userId != 0)
            {
                para.put("accountId", userId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (!"".equals(inviteCode))
            {
                para.put("inviteCode", "%" + inviteCode + "%");
            }
            if (!"".equals(realName))
            {
                para.put("realName", "%" + realName + "%");
            }
            result = partnerService.jsonPartnerInfo(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 合伙人申请列表
     * @param page
     * @param rows
     * @param userId
     * @param username
     * @param realName
     * @param auditStatus
     * @return
     */
    @RequestMapping(value = "/jsonPartnerApplyInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonPartnerApplyInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,
        @RequestParam(value = "username", required = false, defaultValue = "") String username,
        @RequestParam(value = "realName", required = false, defaultValue = "") String realName,
        @RequestParam(value = "auditStatus", required = false, defaultValue = "-1") int auditStatus)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (userId != 0)
            {
                para.put("accountId", userId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (auditStatus != -1)
            {
                para.put("auditStatus", auditStatus);
            }
            if (!"".equals(realName))
            {
                para.put("realName", "%" + realName + "%");
            }
            result = partnerService.jsonPartnerApplyInfo(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 取消或恢复合伙人资格
     * @param accountId：用户Id
     * @param status：状态
     * @return
     */
    @RequestMapping(value = "/updatePartnerStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "合伙人管理-取消或恢复合伙人资格")
    public String updatePartnerStatus(@RequestParam(value = "accountId", required = true) int accountId, @RequestParam(value = "status", required = true) int status)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("accountId", accountId);
            para.put("status", status);
            int code = partnerService.updatePartnerStatus(para);
            if (code == 1)
            {
                result.put("status", 1);
                // 记日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_THREE.ordinal());
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.FENXIAO_MANAGEMENT.ordinal());
                    if (status == 2)
                    {
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.RESET_PARTNER_STATUS.ordinal());
                    }
                    else
                    {
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.CANCEL_PARTNER_STATUS.ordinal());
                    }
                    logInfoMap.put("objectId", accountId);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 通过或拒绝合伙人申请
     * @param accountId：用户Id
     * @param status：状态 status=4:通过申请；status=3:拒绝申请
     * @return
     */
    @RequestMapping(value = "/updatePartnerApplyStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "合伙人管理-合伙人申请")
    public String updatePartnerApplyStatus(@RequestParam(value = "accountId", required = true) int accountId,//
        @RequestParam(value = "status", required = true) int status)
    {
        Map<String, Object> para = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        try
        {
            para.put("accountId", accountId);
            para.put("status", status);
            int code = partnerService.updatePartnerApplyStatus(para);
            if (code == 1)
            {
                result.put("status", 1);
                // 记日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_THREE.ordinal());
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.FENXIAO_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.PARTNER_AUDIT_STATUS.ordinal());
                    logInfoMap.put("objectId", accountId);
                    logInfoMap.put("status", status);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "操作失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "操作失败");
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 手动增加合伙人
     * @param username
     * @param wxAccount
     * @param realName
     * @param remark
     * @return
     */
    @RequestMapping(value = "/addPartnerByHandle", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "合伙人管理-手动增加合伙人")
    public String addPartnerByHandle(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "wxAccount", required = true) String wxAccount,
        @RequestParam(value = "realName", required = true) String realName, @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        try
        {
            para.put("username", username);
            para.put("wxAccount", wxAccount);
            para.put("realName", realName);
            para.put("remark", remark);
            AccountEntity account = accountService.findAccountByName(username);
            if (account == null)
            {
                result.put("status", 0);
                result.put("msg", "用户不存在");
                return JSON.toJSONString(result);
            }
            if (account.getPartnerStatus() != PartnerEnum.PARTNER_STATUS.NO.getCode())
            {
                result.put("status", 0);
                result.put("msg", "该用户已经是合伙人");
                return JSON.toJSONString(result);
            }
            para.put("account", account);
            int code = partnerService.addPartnerByHandle(para);
            if (code == 1)
            {
                result.put("status", 1);
                // 记日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_THREE.ordinal());
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.FENXIAO_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.CREATE_PARTNER.ordinal());
                    logInfoMap.put("objectId", account.getId());
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                
            }
            else if (code == 0)
            {
                result.put("status", 0);
                result.put("msg", "该用户有推荐人，不允许手动置为合伙人。");
            }
            else
            {
                result.put("status", 0);
                result.put("msg", "增加失败");
            }
        }
        catch (Exception e)
        {
            result.put("status", 0);
            result.put("msg", "增加失败");
            logger.error(e.getMessage(), e);
        }
        
        return JSON.toJSONString(result);
    }
    
    /**
     * 邀请关系列表
     * @return
     */
    @RequestMapping("/inviteList")
    public ModelAndView inviteList()
    {
        ModelAndView mv = new ModelAndView("/partner/inviteList");
        return mv;
    }
    
    /**
     * 邀请关系列表
     * @param page
     * @param rows
     * @param userId:邀请用户Id
     * @param username:邀请用户名
     * @param inviteId:被邀请用户Id
     * @param inviteName:被邀请用户名
     * @param inviteCode:邀请码
     * @return
     */
    @RequestMapping(value = "/jsonInviteRelationInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonInviteRelationInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,
        @RequestParam(value = "username", required = false, defaultValue = "") String username,
        @RequestParam(value = "inviteId", required = false, defaultValue = "0") int inviteId,
        @RequestParam(value = "inviteName", required = false, defaultValue = "") String inviteName,
        @RequestParam(value = "inviteCode", required = false, defaultValue = "") String inviteCode)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (userId != 0)
            {
                para.put("accountId", userId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (inviteId != 0)
            {
                para.put("inviteId", inviteId);
            }
            if (!"".equals(inviteName))
            {
                para.put("inviteName", "%" + inviteName + "%");
            }
            if (!"".equals(inviteCode))
            {
                para.put("inviteCode", "%" + inviteCode + "%");
            }
            result = partnerService.jsonInviteRelationInfo(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 返积分记录查询
     * @return
     */
    @RequestMapping("/returnIntegral")
    public ModelAndView returnIntegral()
    {
        ModelAndView mv = new ModelAndView("/partner/returnIntegral");
        return mv;
    }
    
    /**
     * 返积分记录列表
     * @param page
     * @param rows
     * @param userId:邀请人用户Id
     * @param username:邀请人用户名
     * @param inviteCode:邀请码
     * @param returnType:返积分类型
     * @return
     */
    @RequestMapping(value = "/jsonReturnIntegralInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonReturnIntegralInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,
        @RequestParam(value = "username", required = false, defaultValue = "") String username,
        @RequestParam(value = "invitedName", required = false, defaultValue = "") String invitedName,
        @RequestParam(value = "returnType", required = false, defaultValue = "-1") int returnType)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (userId != 0)
            {
                para.put("accountId", userId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (!"".equals(invitedName))
            {
                para.put("invitedName", "%" + invitedName + "%");
            }
            if (returnType != -1)
            {
                para.put("returnType", returnType);
            }
            result = partnerService.jsonReturnIntegralInfo(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/exportReturnIntegral")
    @ResponseBody
    @ControllerLog(description = "合伙人管理-导出合伙人返积分记录")
    public void exportReturnIntegral(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,//
        @RequestParam(value = "username", required = false, defaultValue = "") String username,//
        @RequestParam(value = "invitedName", required = false, defaultValue = "") String invitedName,//
        @RequestParam(value = "returnType", required = false, defaultValue = "-1") int returnType)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        PrintWriter ot = null;
        String errorMessage = "";
        try
        {
            if (userId != 0)
            {
                para.put("accountId", userId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            if (!"".equals(invitedName))
            {
                para.put("invitedName", "%" + invitedName + "%");
            }
            if (returnType != -1)
            {
                para.put("returnType", returnType);
            }
            if (partnerService.getAllReturnIntegralNums(para) > CommonConstant.workbook_num_3w)
            {
                errorMessage = "数据量超过" + CommonConstant.workbook_num_3w + "，请缩小范围！";
                throw new RuntimeException();
            }
            result = partnerService.jsonReturnIntegralInfo(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            if (rows.size() > 0)
            {
                response.setContentType("application/vnd.ms-excel");
                String codedFileName = "合伙人返积分记录";
                // 进行转码，使其支持中文文件名
                codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
                // 产生工作簿对象
                workbook = new HSSFWorkbook();
                // 产生工作表对象
                HSSFSheet sheet = workbook.createSheet();
                String[] str = {"用户ID", "用户名", "身份", "邀请码", "被邀请人用户名", "订单编号", "订单总价", "实付金额", "返积分类型", "积分数量", "积分变动时间"};
                Row row = sheet.createRow(0);
                HSSFCellStyle cellStyle = workbook.createCellStyle();
                // 水平方向上居中对齐
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                // 垂直方向上居中对齐
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                cellStyle.setWrapText(false);
                for (int i = 0; i < str.length; i++)
                {
                    
                    Cell cell = row.createCell(i);
                    cell.setCellValue(str[i]);
                    cell.setCellStyle(cellStyle);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    sheet.setColumnWidth(i, str[i].getBytes().length * 2 * 256);
                }
                for (int i = 0; i < rows.size(); i++)
                {
                    Map<String, Object> currMap = rows.get(i);
                    Row r = sheet.createRow(i + 1);
                    r.createCell(0).setCellStyle(cellStyle);
                    r.createCell(0).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(0).setCellValue(currMap.get("userId") + "");
                    r.createCell(1).setCellStyle(cellStyle);
                    r.createCell(1).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(1).setCellValue(currMap.get("username") + "");
                    r.createCell(2).setCellStyle(cellStyle);
                    r.createCell(2).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(2).setCellValue(currMap.get("identity") + "");
                    r.createCell(3).setCellStyle(cellStyle);
                    r.createCell(3).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(3).setCellValue(currMap.get("inviteCode") + "");
                    r.createCell(4).setCellStyle(cellStyle);
                    r.createCell(4).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(4).setCellValue(currMap.get("invitedName") + "");
                    r.createCell(5).setCellStyle(cellStyle);
                    r.createCell(5).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(5).setCellValue(currMap.get("orderNumber") + "");
                    r.createCell(6).setCellStyle(cellStyle);
                    r.createCell(6).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(6).setCellValue(currMap.get("totalPrice") + "");
                    r.createCell(7).setCellStyle(cellStyle);
                    r.createCell(7).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(7).setCellValue(currMap.get("realPrice") + "");
                    r.createCell(8).setCellStyle(cellStyle);
                    r.createCell(8).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(8).setCellValue(currMap.get("returnType") + "");
                    r.createCell(9).setCellStyle(cellStyle);
                    r.createCell(9).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(9).setCellValue(currMap.get("point") + "");
                    r.createCell(10).setCellStyle(cellStyle);
                    r.createCell(10).setCellType(HSSFCell.CELL_TYPE_STRING);
                    r.createCell(10).setCellValue(currMap.get("createTime") + "");
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
                logger.error(e.getMessage(), e);
                errorMessage = "系统出错";
            }
            response.setHeader("content-disposition", "");
            response.setContentType("text/html");
            try
            {
                if (fOut == null)
                {
                    fOut = response.getOutputStream();
                }
                String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
                fOut.write(errorStr.getBytes());
                fOut.flush();
            }
            catch (Exception e1)
            {
                logger.error(e1.getMessage(), e1);
            }
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
     * 积分提现View
     * @return
     */
    @RequestMapping("/exchangeIntegral")
    public ModelAndView exchangeIntegral()
    {
        ModelAndView mv = new ModelAndView("partner/exchangeIntegral");
        return mv;
    }
    
    /**
     * 积分提现列表
     * @param page
     * @param rows
     * @param userId
     * @param username
     * @return
     */
    @RequestMapping(value = "/jsonExchangeIntegralInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonExchangeIntegralInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "userId", required = false, defaultValue = "0") int userId,
        @RequestParam(value = "username", required = false, defaultValue = "") String username, @RequestParam(value = "status", required = false, defaultValue = "1") int status)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (userId != 0)
            {
                para.put("accountId", userId);
            }
            if (!"".equals(username))
            {
                para.put("username", "%" + username + "%");
            }
            para.put("status", status);
            result = partnerService.jsonExchangeIntegralInfo(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 处理积分提现
     * @param id
     * @param transferAccountId
     * @return
     */
    @RequestMapping(value = "/dealWithExchange", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "合伙人管理-积分提现")
    public String dealWithExchange(@RequestParam(value = "dealWithId", required = true) int id,
        @RequestParam(value = "financialAffairsCardId", required = true) int transferAccountId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            para.put("id", id);
            para.put("status", 2);
            para.put("transferAccountId", transferAccountId);
            
            int result = partnerService.dealWithExchange(para);
            if (result > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "打款失败");
            }
        }
        catch (Exception e)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "打款出现异常");
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 分销订单View
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderDetailList/{id}")
    public ModelAndView orderDetailList(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("partner/orderDetailList");
        mv.addObject("id", id + "");
        mv.addObject("account", accountService.findAccountById(id));
        return mv;
    }
    
    /**
     * 分销订单列表
     * @param page
     * @param rows
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/jsonOrderDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderDetail(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "id", required = true) int accountId)
    {
        Map<String, Object> result = null;
        Map<String, Object> para = new HashMap<String, Object>();
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("accountId", accountId);
            result = partnerService.jsonOrderDetail(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 手动创建邀请关系
     * @param type：type=1,表示根据Id创建邀请关系，type=2，表示根据用户名创建邀请关系
     * @param faterAccount：type=1表示邀请人Id,type=2表示被邀请人用户名
     * @param currentAccount：type=1表示邀请人用户名，type=2表示被邀请人用户名
     * @return
     */
    @RequestMapping(value = "/createInviteRelation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "合伙人管理-手动创建邀请关系")
    public String createInviteRelation(//
        @RequestParam(value = "type", required = true) int type,//
        @RequestParam(value = "parentAccount", required = true) String faterAccount,//
        @RequestParam(value = "invitedAccount", required = true) String currentAccount)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            int faterAccountId = 0;
            int currentAccountId = 0;
            if (type == 1)
            {
                faterAccountId = Integer.valueOf(faterAccount).intValue();
                currentAccountId = Integer.valueOf(currentAccount).intValue();
            }
            else if (type == 2)
            {
                AccountEntity fae = accountService.findAccountByName(faterAccount);
                if (fae == null)
                {
                    resultMap.put("status", 2);
                    return JSON.toJSONString(resultMap);
                }
                
                AccountEntity cae = accountService.findAccountByName(currentAccount);
                if (cae == null)
                {
                    resultMap.put("status", 3);
                    return JSON.toJSONString(resultMap);
                }
                faterAccountId = fae.getId();
                currentAccountId = cae.getId();
            }
            int status = partnerService.createInviteRelation(faterAccountId, currentAccountId);
            resultMap.put("status", status);
            
            // 记录日志
            if (status == 1)
            {
                // 记日志
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_THREE.ordinal());
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.FENXIAO_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.CREATE_INVITED_RELATION.ordinal());
                    logInfoMap.put("faterAccountId", faterAccountId);
                    logInfoMap.put("currentAccountId", currentAccountId);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器发生异常");
        }
        return JSON.toJSONString(resultMap);
    }
}
