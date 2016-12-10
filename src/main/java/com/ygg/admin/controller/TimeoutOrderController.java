package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.entity.OrderDetailInfoForSeller;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.service.TimeoutOrderService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.POIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/timeoutOrder")
public class TimeoutOrderController
{
    private static Logger logger = Logger.getLogger(TimeoutOrderController.class);
    
    @Resource
    private TimeoutOrderService timeoutOrderService;
    
    @Resource
    private OrderService orderService;
    
    @Resource
    private SellerService sellerService;
    
    /**
     * 商家订单汇总列表
     * @param orderType 1：左岸城堡
订单；2：左岸城堡订单，3：全球购，4：左岸城堡
，5：燕网
     * @return
     */
    @RequestMapping(value = "/summaryList")
    public ModelAndView summaryList(@RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        ModelAndView mv = new ModelAndView("timeoutOrder/summaryList");
        String startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        String endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("orderType", orderType);  //不区分左岸城堡 左岸城堡

        return mv;
    }
    
    /**
     * 异步加载商家订单汇总列表
     * @param startTime
     * @param endTime
     * @param orderType 1：左岸城堡
订单；2：左岸城堡订单，3：全球购，4：左岸城堡
，5：燕网
     * @return
     */
    @RequestMapping(value = "/jsonOrderSummaryInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object jsonOrderSummaryInfo(@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        try
        {
            if ("".equals(startTime))
            {
                startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
            }
            if ("".equals(endTime))
            {
                endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
            }
            return timeoutOrderService.findAllOrderSummaryInfo(startTime, endTime, orderType);
        }
        catch (Exception e)
        {
            logger.error("异步加载商家发货超时订单出错", e);
            return new ArrayList<>();
        }
    }

    /**
     *
     */
    @RequestMapping(value = "/exportResult")
    @ControllerLog(description = "订单申诉管理-导出商家发货时效")
    public void exportResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            if ("".equals(startTime))
            {
                startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
            }
            if ("".equals(endTime))
            {
                endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
            }
            
            List<Map<String, Object>> result = timeoutOrderService.findAllOrderSummaryInfo(startTime, endTime, orderType);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商家订单发货时效");
            String[] str = {"商家名称", "当日订单个数", "准时发货", "发货超时发货", "发货超时未发货", "未超时未发货", "发货超时罚款", "发货准时率", "最优发货准时率", "发货完成进度"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> om = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(om.get("sellerName") + "");
                r.createCell(1).setCellValue(om.get("totalAmount") + "");
                r.createCell(2).setCellValue(om.get("ontimeAmount") + "");
                r.createCell(3).setCellValue(om.get("timeoutSendAmount") + "");
                r.createCell(4).setCellValue(om.get("timeoutNoSendAmount") + "");
                r.createCell(5).setCellValue(om.get("waitSendAmount") + "");
                r.createCell(6).setCellValue(om.get("amerceAmount") + "");
                r.createCell(7).setCellValue(om.get("sendOntimePercent") + "");
                r.createCell(8).setCellValue(om.get("bestSendOntimePercent") + "");
                r.createCell(9).setCellValue(om.get("sendProgressPercent") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "商家订单发货时效";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("导出商家订单发货时效出错了！！！", e);
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
     * 商家订单列表
     * @param startTime
     * @param endTime
     * @param sellerId
     * @param orderIdList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/timeoutOrderList")
    public ModelAndView timeoutOrderList(@RequestParam(value = "exportType", required = false, defaultValue = "1") int exportType,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
        @RequestParam(value = "sellerId", required = false, defaultValue = "") String sellerId,
        @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("timeoutOrder/timeoutOrderList");
        if ("".equals(startTime))
        {
            startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        }
        if ("".equals(endTime))
        {
            endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        }
        
        mv.addObject("exportType", exportType);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("sellerId", sellerId);
        mv.addObject("orderIdList", orderIdList);
        mv.addObject("orderType", orderType);
        return mv;
    }
    
    /**
     * 异步加载商家订单列表
     * @param page
     * @param rows
     * @param orderNumber
     * @param orderStatus
     * @param productId
     * @param code
     * @param productName
     * @param sellerId
     * @param receiveName
     * @param reveivePhone
     * @param startTime
     * @param endTime
     * @param orderIdList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderInfo(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品ID
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 商品编码
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,//开始时间
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,//结束时间
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList,
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout)
        throws Exception
    {
        if (sellerId <= 0)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("rows", new ArrayList<>());
            return JSON.toJSONString(result);
        }
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        
        if ("".equals(startTime))
        {
            startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        }
        if ("".equals(endTime))
        {
            endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        }
        
        para.put("startTimeBegin", startTime);
        para.put("startTimeEnd", endTime);
        long sqlType = 1;
        if (orderNumber != 0)
        {
            para.put("orderNumber", orderNumber);
        }
        if (orderStatus != 0)
        {
            para.put("orderStatus", orderStatus);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        
        // 需要关联order_product表 2
        if (productId != 0)
        {
            para.put("productId", productId);
            sqlType = 12;
        }
        
        // 需要关联product表 3
        if (!"".equals(productName))
        {
            
            para.put("productName", "%" + productName + "%");
            sqlType = 123;
        }
        if (!"".equals(code))
        {
            para.put("code", code);
            sqlType = 123;
        }
        
        // order_receive_address 6
        if (!"".equals(receiveName))
        {
            para.put("fullName", "%" + receiveName + "%");
        }
        if (!"".equals(reveivePhone))
        {
            para.put("mobileNumber", reveivePhone);
        }
        
        para.put("sqlType", sqlType);
        
        if (!"".equals(orderIdList))
        {
            para.put("orderIdList", Arrays.asList(orderIdList.split(",")));
        }
        
        if (isTimeout != -1)
        {
            para.put("isTimeout", isTimeout);
        }
        
        Map<String, Object> result = orderService.ajaxOrderInfo(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 订单申诉列表
     * @return
     */
    @RequestMapping("/complainList")
    public ModelAndView complainList(@RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        ModelAndView mv = new ModelAndView("timeoutOrder/complainList");
        mv.addObject("orderType", orderType);
        return mv;
    }
    
    /**
     * 异步加载订单申诉列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonComplainOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonComplainOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType,
                                        @RequestParam(value = "appChannel", required = false, defaultValue = "-1") int appChannel)
    
    {
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if(orderType != -1){
                para.put("orderType", orderType);
            }
            para.put("appChannel", appChannel);
            return timeoutOrderService.jsonComplainOrderInfo(para);
        }
        catch (Exception e)
        {
            logger.info("异步加载订单申诉列表出错", e);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("rows", new ArrayList<>());
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/jsonTimeoutReasonCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonTimeoutReasonCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "-1") int isAvailable)
    {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            if (isAvailable != -1)
            {
                para.put("isAvailable", isAvailable);
            }
            List<Map<String, Object>> info = timeoutOrderService.findAllTimeoutReason(para);
            for (Map<String, Object> it : info)
            {
                Map<String, Object> mp = new HashMap<>();
                mp.put("code", it.get("id"));
                mp.put("text", it.get("name"));
                if (id == Integer.parseInt(it.get("id").toString()))
                {
                    mp.put("selected", "true");
                }
                resultList.add(mp);
            }
            return JSON.toJSONString(resultList);
        }
        catch (Exception e)
        {
            logger.error("加载订单发货超时原因发生异常", e);
        }
        return JSON.toJSONString(resultList);
    }
    
    /**
     * 
     * @param orderId：订单ID
     * @param complainId：申诉ID
     * @param timeoutReasonId：发货超时原因Id
     * @param dealResult：处理结果，2成功，3失败
     * @param remark：处理备注
     * @return
     */
    @RequestMapping(value = "/dealComplain", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单申诉管理-订单申诉处理")
    public String dealComplain(@RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,//
        @RequestParam(value = "complainId", required = false, defaultValue = "0") int complainId,//
        @RequestParam(value = "timeoutReasonId", required = false, defaultValue = "0") int timeoutReasonId,//
        @RequestParam(value = "dealResult", required = false, defaultValue = "3") int dealResult, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (orderId == 0 || complainId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的订单");
                return JSON.toJSONString(resultMap);
            }
            if (timeoutReasonId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择发货超时原因");
                return JSON.toJSONString(resultMap);
            }
            return timeoutOrderService.dealComplain(orderId, complainId, timeoutReasonId, dealResult, remark);
        }
        catch (Exception e)
        {
            logger.error("加载订单发货超时原因发生异常", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping("/complainDetail/{orderId}")
    public ModelAndView complainDetail(@PathVariable("orderId") int orderId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("timeoutOrder/complainDetail");
        OrderEntity order = orderService.findOrderById(orderId);
        if (order != null)
        {
            mv.addObject("orderNumber", order.getNumber() + "");
            SellerEntity seller = sellerService.findSellerById(order.getSellerId());
            if (seller != null)
            {
                mv.addObject("sellerName", seller.getRealSellerName());
            }
        }
        List<Map<String, Object>> complainList = timeoutOrderService.findComplainDetailsByOrderId(orderId);
        if (complainList != null && complainList.size() > 0)
        {
            mv.addObject("complainList", complainList);
        }
        
        Map<String, Object> complainResult = timeoutOrderService.findOrderTimeoutComplainResultByOId(orderId);
        if (complainResult != null)
        {
            mv.addObject("timeoutReason", timeoutOrderService.findOrderTimeoutReasonNameById(Integer.parseInt(complainResult.get("timeoutReasonId").toString())));
        }
        return mv;
    }
    
    /**
     * 发货超时原因列表
     * @return
     */
    @RequestMapping("/timeoutReasonList")
    public ModelAndView timeoutReasonList()
    {
        ModelAndView mv = new ModelAndView("timeoutOrder/timeoutReasonList");
        return mv;
    }
    
    /**
     * 异步加载发货超时原因列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonTimeoutReasonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonTimeoutReasonInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
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
            resultMap = timeoutOrderService.jsonTimeoutReasonInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载发货超时原因列表出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增问题模板
     * @param name
     * @return
     */
    @RequestMapping(value = "/saveTimeoutReason", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单申诉管理-新增订单发货超时原因")
    public String saveTimeoutReason(@RequestParam(value = "id", required = false, defaultValue = "0") int id, @RequestParam(value = "name", required = true) String name)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            name = name.trim();
            return timeoutOrderService.saveTimeoutReason(id, name);
        }
        catch (Exception e)
        {
            logger.error("新增订单发货超时原因:" + name + "出错了", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateTimeoutReasonStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单申诉管理-修改订单发货超时原因状态")
    public String updateTimeoutReasonStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isAvailable", isAvailable);
            
            return timeoutOrderService.updateTimeoutReasonStatus(para);
            
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param exportType：导出类型，1：订单总数，2：准时发货，3：超时发货，4：超时未发货 ，5：未超时未发货
     * @param startTime
     * @param endTime
     * @param orderIdList
     * @param sellerId
     * @throws Exception
     */
    @RequestMapping(value = "/exportTimeoutResult")
    @ControllerLog(description = "订单申诉管理-导出商家订单")
    public void exportTimeoutResult(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "exportType", required = false, defaultValue = "0") int exportType,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList,
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus,
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName,
        @RequestParam(value = "receivePhone", required = false, defaultValue = "") String receivePhone,
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
        @RequestParam(value = "productCode", required = false, defaultValue = "") String productCode,
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout,
        @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType,
        @RequestParam(value = "appChannel", required = false, defaultValue = "-1") int appChannel
    )
        throws Exception
    {
        OutputStream fOut = null;
        String errorMessage = "";
        try
        {
            Map<String, Object> para = new HashMap<>();
            if ("".equals(startTime))
            {
                startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
            }
            if ("".equals(endTime))
            {
                endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
            }
            String fileNamePrefix = "";
            String fileNameSuffix = "(" + startTime + "——" + endTime + ")";
            fOut = response.getOutputStream();
            if (exportType == 0)
            {
                //默认导出{startTime}至{endTime}超时未发货订单
                para.put("expireTimeBegin", startTime);
                para.put("expireTimeEnd", endTime);
                para.put("isTimeout", 1);
                para.put("orderStatus", OrderEnum.ORDER_STATUS.REVIEW.getCode());
                fileNamePrefix = "全部商家超时未发货订单";
            }
            else
            {
                SellerEntity se = sellerService.findSellerById(sellerId);
                if (se == null)
                {
                    errorMessage = "商家不存在";
                    response.setHeader("content-disposition", "");
                    response.setContentType("text/html");
                    String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
                    fOut.write(errorStr.getBytes());
                    fOut.flush();
                    return;
                }
                if ("".equals(orderIdList))
                {
                    errorMessage = "订单不存在";
                    response.setHeader("content-disposition", "");
                    response.setContentType("text/html");
                    String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
                    fOut.write(errorStr.getBytes());
                    fOut.flush();
                    return;
                }
                if (exportType == 1)
                {
                    //导出{startTime}至{endTime}时间内{sellerId}的全部订单
                    fileNamePrefix = "全部商家订单";
                    
                }
                else if (exportType == 2)
                {
                    //导出{startTime}至{endTime}时间内{sellerId}的准时发货
                    fileNamePrefix = se.getRealSellerName() + "的准时发货订单";
                }
                else if (exportType == 3)
                {
                    //导出{startTime}至{endTime}时间内{sellerId}的超时发货订单
                    fileNamePrefix = se.getRealSellerName() + "的超时发货订单";
                }
                else if (exportType == 4)
                {
                    //导出{startTime}至{endTime}时间内{sellerId}的超时未发货订单
                    fileNamePrefix = se.getRealSellerName() + "的超时未发货订单";
                }
                else if (exportType == 5)
                {
                    //导出{startTime}至{endTime}时间内{sellerId}的未超时未发货订单
                    fileNamePrefix = se.getRealSellerName() + "的未超时未发货订单";
                }
                para.put("orderIdList", Arrays.asList(orderIdList.split(",")));
                if (sellerId != 0)
                {
                    para.put("sellerId", sellerId);
                }
                if (!"".equals(orderNumber))
                {
                    para.put("orderNumber", orderNumber);
                }
                if (orderStatus > 0)
                {
                    para.put("orderStatus", orderStatus);
                }
                if (!"".equals(receiveName))
                {
                    para.put("receiveName", "%" + receiveName + "%");
                }
                if (!"".equals(receivePhone))
                {
                    para.put("receivePhone", receivePhone);
                }
                if (!"".equals(productName))
                {
                    para.put("productName", "%" + productName + "%");
                }
                if (!"".equals(productCode))
                {
                    para.put("productCode", productCode);
                }
                if (isTimeout != -1)
                {
                    para.put("isTimeout", isTimeout);
                }
            }
            para.put("orderType", orderType);
            
            // 导出列表结果
            // 1 判断总条数
            if (timeoutOrderService.getExportOrderNums(para) > CommonConstant.workbook_num_6w)
            {
                errorMessage = "数据量大于" + CommonConstant.workbook_num_6w + "，请缩小范围！";
                throw new RuntimeException();
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileNamePrefix, "UTF-8") + fileNameSuffix + ".xlsx");
            List<OrderDetailInfoForSeller> rows = timeoutOrderService.findAllTimeoutOrderDetail(para);
            String[] str = {"下单时间", "用户ID", "付款时间", "付款方式", "发货时间", "订单来源", "订单编号", "订单状态", "备注", "订单总价", "实付金额", "收货人", "收货手机", "商家", "商品编码", "商品名称", "件数", "发货地", "物流公司", "运单号"};
            SXSSFWorkbook workbook = POIUtil.createSXSSFWorkbookTemplate(str);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < rows.size(); i++)
            {
                int cellIndex = 0;
                OrderDetailInfoForSeller currMap = rows.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(cellIndex++).setCellValue(currMap.getoCreateTime() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getAccountId() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getoPayTime() + "");
                r.createCell(cellIndex++).setCellValue(OrderEnum.PAY_CHANNEL.getDescByCode(Integer.parseInt(currMap.getPayChannel())));
                r.createCell(cellIndex++).setCellValue(currMap.getoSendTime() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getOrderChannel() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getoNumber() + "");
                r.createCell(cellIndex++).setCellValue(OrderEnum.ORDER_STATUS.getDescByCode(currMap.getoStatus()));
                r.createCell(cellIndex++).setCellValue(currMap.getSellerMarks());
                r.createCell(cellIndex++).setCellValue(currMap.getoTotalPrice() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getoRealPrice() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getRaFullName() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getRaMobileNumber() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getSellerName() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getProductCode() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getProductName() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getProductCount() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getSendAddress() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getOlogChannel() + "");
                r.createCell(cellIndex++).setCellValue(currMap.getOlogNumber() + "");
            }
            workbook.write(fOut);
            fOut.close();
            workbook.close();
            workbook.dispose();
            
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
        }
    }
    
    /**
     * 
     * @param orderId：订单Id
     * @param reasonId：超时原因ID
     * @param result：处理结果
     * @param remark：处理备注
     * @return
     */
    @RequestMapping(value = "/timeoutOrderQcDeal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单申诉管理-QC处理超时订单")
    public String timeoutOrderQcDeal(@RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,
        @RequestParam(value = "reasonId", required = false, defaultValue = "0") int reasonId, @RequestParam(value = "result", required = false, defaultValue = "") String result,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (orderId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的订单");
                return JSON.toJSONString(resultMap);
            }
            if (reasonId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择发货超时原因");
                return JSON.toJSONString(resultMap);
            }
            return timeoutOrderService.addTimeoutOrderQcDeal(orderId, reasonId, result, remark);
        }
        catch (Exception e)
        {
            logger.error("QC处理超时订单发生异常", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/timeoutOrderQcBatchDeal", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单申诉管理-QC批量处理超时订单")
    public String timeoutOrderQcBatchDeal(@RequestParam(value = "orderIds", required = false, defaultValue = "") String orderIds,
        @RequestParam(value = "reasonId", required = false, defaultValue = "0") int reasonId, @RequestParam(value = "result", required = false, defaultValue = "") String result,
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            if (StringUtils.isEmpty(orderIds))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的订单");
                return JSON.toJSONString(resultMap);
            }
            if (reasonId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择发货超时原因");
                return JSON.toJSONString(resultMap);
            }
            return timeoutOrderService.addBatchTimeoutOrderQcDeal(orderIds, reasonId, result, remark);
        }
        catch (Exception e)
        {
            logger.error("QC处理超时订单发生异常", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
            return JSON.toJSONString(resultMap);
        }
    }
}
