package com.ygg.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.CouponCodeEntity;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.service.MwebGroupCouponCodeService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.StringUtils;

/**
 * 优惠码 控制器
 * 
 * @author zhangyb
 *         
 */
@Controller
@RequestMapping("/mwebGroupCouponCode")
public class MwebGroupCouponCodeController
{
    
    private Logger log = Logger.getLogger(MwebGroupCouponCodeController.class);
    
    @Resource
    private MwebGroupCouponCodeService mwebGroupCouponCodeService;
    
    @Resource
    private SystemLogService logService;
    
    /**
     * 优惠码列表
     * 
     * @return
     */
    @RequestMapping("/listCode")
    public String couponList()
    {
        return "mwebGroupCoupon/codeList";
    }
    
    /**
     * 新增优惠码
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "addCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠码管理-新增优惠码")
    public String addCode(HttpServletRequest request, //
        @RequestParam(value = "couponDetail", required = true) String couponDetail, // 举例，一张优惠券：23 ； 两张：12,44
        @RequestParam(value = "mIdAndCount", required = true) String mIdAndCount, // 举例，一张优惠券："23:1," ； 两张："23:1,44:3;"
        @RequestParam(value = "startTime", required = true) String startTime, //
        @RequestParam(value = "endTime", required = true) String endTime, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String desc, //
        @RequestParam(value = "customCode", required = false, defaultValue = "") String customCode, //
        @RequestParam(value = "nums", required = true) int nums, //
        @RequestParam(value = "type", required = true) int type //
    )
        throws Exception
    {
        try
        {
            List<Map<String, Object>> couponDetailIdAndCountList = new ArrayList<Map<String, Object>>();
            String[] arr = mIdAndCount.split(",");
            for (String it : arr)
            {
                Map<String, Object> curr = new HashMap<String, Object>();
                if (!StringUtils.isNumeric(it.split(":")[0]) || !StringUtils.isNumeric(it.split(":")[1]))
                {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result = new HashMap<String, Object>();
                    result.put("status", 0);
                    result.put("msg", "生成失败，请刷新页面重试。");
                    return JSON.toJSONString(result);
                }
                curr.put("couponDetailId", it.split(":")[0]);
                curr.put("count", it.split(":")[1]);
                couponDetailIdAndCountList.add(curr);
            }
            if (couponDetailIdAndCountList.size() != couponDetail.split(",").length)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "生成失败，请检查优惠券数量是否正确。");
                return JSON.toJSONString(result);
            }
            
            Map<String, Object> result = mwebGroupCouponCodeService.addCouponCode(couponDetailIdAndCountList, startTime, endTime, desc, nums, type, customCode);
            if ("1".equals((result.get("status") + "")))
            {
                // 发放优惠码记录日志
                try
                {
                    CouponCodeEntity couponCode = new CouponCodeEntity();
                    // couponCode.setCouponDetailId(couponDetailId);
                    couponCode.setSameMaxCount(1);
                    couponCode.setStartTime(startTime);
                    couponCode.setEndTime(endTime);
                    couponCode.setIsAvailable((byte)1);
                    couponCode.setCreateTime(DateTimeUtil.now());
                    couponCode.setType((byte)type);
                    couponCode.setTotal(nums);
                    couponCode.setDesc(desc);
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALE_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.SEND_COUPON_CODE.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_FOUR.ordinal());
                    logInfoMap.put("object", couponCode);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("生成优惠码失败！", e);
            Map<String, Object> result = null;
            if (e instanceof ServiceException)
            {
                ServiceException se = (ServiceException)e;
                result = se.getMap();
            }
            if (result == null)
            {
                result = new HashMap<String, Object>();
                result.put("status", 0);
                result.put("msg", "生成失败");
            }
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 异步获取优惠码列表
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "jsonCouponCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponCode(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 优惠码
        @RequestParam(value = "remark", required = false, defaultValue = "") String desc, // 备注
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable, // 优惠码是否可用
        @RequestParam(value = "type", required = false, defaultValue = "0") int type // 优惠码类型
    )
        throws Exception
    {
        Date dateTmp1 = new Date();
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (isAvailable != -1)
        {
            para.put("isAvailable", isAvailable);
        }
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        if (!"".equals(desc))
        {
            para.put("desc", "%" + desc + "%");
        }
        if (type != 0)
        {
            para.put("type", type);
        }
        Map<String, Object> resultMap = mwebGroupCouponCodeService.findCouponCode(para);
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        log.error("jsonCouponCode数据查询完成，一共运行时间【" + (time) + "毫秒】");
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 更新优惠码可用状态
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "available", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "优惠码管理-修改优惠码可用状态")
    public String available(HttpServletRequest request, //
        @RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "isAvailable", required = true) byte isAvailable //
    )
        throws Exception
    {
        try
        {
            Map<String, Object> result = mwebGroupCouponCodeService.updateCouponAvailable(id, isAvailable);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("生成优惠码失败！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 优惠码详细
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/couponCodeDetail/{type}/{id}")
    public ModelAndView couponCodeDetail(HttpServletRequest request, //
        @PathVariable("type") String type, // 优惠码类型
        @PathVariable("id") String id // 优惠码ID
    )
    {
        ModelAndView mv = new ModelAndView();
        try
        {
            mv.addObject("id", id);
            if ("1".equals(type))
            {
                mv.addObject("te", 1);
            }
            mv.setViewName("mwebGroupCoupon/codeDetail");
            
            String totalAmount = mwebGroupCouponCodeService.findCouponCodeTotoalMoney(id);
            mv.addObject("totalAmount", totalAmount);
        }
        catch (Exception e)
        {
            mv.setViewName("error/404");
            log.error(e.getMessage(), e);
        }
        return mv;
    }
    
    /**
     * 优惠码详细 列表 json
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "jsonCouponCodeDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponCodeDetail(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "couponCodeId", required = true) int couponCodeId, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 优惠码
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 备注
        @RequestParam(value = "convertType", required = false, defaultValue = "-1") int convertType, // 兑换状态
        @RequestParam(value = "usedType", required = false, defaultValue = "-1") int usedType // 使用状态
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
        para.put("couponCodeId", couponCodeId);
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (convertType != -1)
        {
            para.put("convertType", convertType);
        }
        if (usedType != -1)
        {
            para.put("usedType", usedType);
        }
        Map<String, Object> resultMap = mwebGroupCouponCodeService.findCouponCodeDetail(para);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 优惠码礼包详细
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/couponCodeLiBaoDetail/{type}/{id}")
    public ModelAndView couponCodeLiBaoDetail(HttpServletRequest request, //
        @PathVariable("type") String type, // 优惠码类型
        @PathVariable("id") String id // 优惠码ID
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("id", id);
        if ("1".equals(type))
        {
            mv.addObject("te", 1);
        }
        // Map<String, Object> result = mwebGroupCouponCodeService.findCouponCodeLiBaoInfo(Integer.parseInt(id));
        // mv.addObject("couponDetailIdList", result.get("couponDetailIdList"));
        mv.setViewName("mwebGroupCoupon/codeLiBaoDetail");
        return mv;
    }
    
    @RequestMapping(value = "jsonCouponCodeLiBaoDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCouponCodeLiBaoDetail(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "couponCodeId", required = true) int couponCodeId, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 优惠码
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 备注
        @RequestParam(value = "convertType", required = false, defaultValue = "-1") int convertType // 兑换状态
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
        para.put("couponCodeId", couponCodeId);
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        if (!"".equals(name))
        {
            para.put("name", "%" + name + "%");
        }
        if (convertType != -1)
        {
            para.put("convertType", convertType);
        }
        
        Map<String, Object> resultMap = mwebGroupCouponCodeService.findCouponCodeLiBaoDetail(para);
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportCouponCodeDetail")
    @ControllerLog(description = "优惠码管理-导出优惠码")
    public void exportCouponCodeDetail(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "couponCodeId", required = true) int couponCodeId, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 优惠码
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 备注
        @RequestParam(value = "convertType", required = false, defaultValue = "-1") int convertType, // 兑换状态
        @RequestParam(value = "usedType", required = false, defaultValue = "-1") int usedType) // 使用状态
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "优惠码";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("couponCodeId", couponCodeId);
            if (!"".equals(code))
            {
                para.put("code", code);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (convertType != -1)
            {
                para.put("convertType", convertType);
            }
            if (usedType != -1)
            {
                para.put("usedType", usedType);
            }
            Map<String, Object> resultMap = mwebGroupCouponCodeService.findCouponCodeDetail(para);
            
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            
            String[] str = {"优惠券码", "用户名", "用户类型", "优惠券类型", "金额", "限用人数", "兑换状态", "使用状态"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            List<Map<String, Object>> rows = (List<Map<String, Object>>)resultMap.get("rows");
            if (rows.size() > 0)
            {
                int index = 1;
                for (Map<String, Object> it : rows)
                {
                    Row r = sheet.createRow(index++);
                    r.createCell(0).setCellValue(it.get("code") + "");
                    r.createCell(1).setCellValue(it.get("accountName") != null ? it.get("accountName") + "" : "");
                    r.createCell(2).setCellValue(it.get("accountTypeStr") + "");
                    r.createCell(3).setCellValue(it.get("couponTypeStr") + "");
                    r.createCell(4).setCellValue(it.get("reducePrice") + "");
                    r.createCell(5).setCellValue(it.get("limitPeople") + "");
                    r.createCell(6).setCellValue(it.get("convert") + "");
                    r.createCell(7).setCellValue(it.get("used") + "");
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter ot = null;
            try
            {
                ot = response.getWriter();
                ot.println("<script>alert('系统出错');window.history.back();</script>");
                ot.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            finally
            {
                if (ot != null)
                {
                    ot.close();
                }
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
    
    @RequestMapping(value = "/exportCouponCodeLiBaoDetail")
    @ControllerLog(description = "优惠码管理-导出礼包")
    public void exportCouponCodeLiBaoDetail(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "couponCodeId", required = true) int couponCodeId, //
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 优惠码
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 备注
        @RequestParam(value = "convertType", required = false, defaultValue = "-1") int convertType)// 兑换状态
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "优惠码";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("couponCodeId", couponCodeId);
            if (!"".equals(code))
            {
                para.put("code", code);
            }
            if (!"".equals(name))
            {
                para.put("name", "%" + name + "%");
            }
            if (convertType != -1)
            {
                para.put("convertType", convertType);
            }
            
            Map<String, Object> resultMap = mwebGroupCouponCodeService.findCouponCodeLiBaoDetail(para);
            
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            
            String[] str = {"优惠券码", "用户名", "用户类型", "礼包信息", "限用人数", "兑换状态"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            List<Map<String, Object>> rows = (List<Map<String, Object>>)resultMap.get("rows");
            if (rows.size() > 0)
            {
                int index = 1;
                for (Map<String, Object> it : rows)
                {
                    Row r = sheet.createRow(index++);
                    r.createCell(0).setCellValue(it.get("code") + "");
                    r.createCell(1).setCellValue(it.get("accountName") != null ? it.get("accountName") + "" : "");
                    r.createCell(2).setCellValue(it.get("accountTypeStr") + "");
                    r.createCell(3).setCellValue(it.get("couponTypeStr") + "");
                    r.createCell(4).setCellValue(it.get("limitPeople") + "");
                    r.createCell(5).setCellValue(it.get("convert") + "");
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter ot = null;
            try
            {
                ot = response.getWriter();
                ot.println("<script>alert('系统出错');window.history.back();</script>");
                ot.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            finally
            {
                if (ot != null)
                {
                    ot.close();
                }
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
     * 优惠码订单关联表
     * 
     * @param type：优惠码类型；1：不同账号同一优惠码一次使用，2：不同账号同一优惠码无限次使用
     * @param couponAccountId：用户优惠券ID
     * @return
     * @throws Exception
     */
    @RequestMapping("/couponCodeOrderDetail/{type}/{couponAccountId}")
    public ModelAndView couponCodeOrderDetail(@PathVariable("type") String type, @PathVariable("couponAccountId") int couponAccountId)
        throws Exception
    {
        
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("type", type);
        para.put("couponAccountId", couponAccountId);
        Map<String, Object> map = mwebGroupCouponCodeService.queryCouponAccount(para);
        ModelAndView mv = new ModelAndView();
        mv.addObject("couponAccountId", couponAccountId);
        mv.addObject("coupon", map);
        mv.setViewName("mwebGroupCoupon/couponOrderDetailList");
        return mv;
    }
    
}
