package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.entity.OrderDetailInfoForSeller;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.LogisticsTimeoutService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.Excel.ExcelMaker;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/logisticsTimeout")
public class LogisticsTimeoutController
{
    private static Logger logger = Logger.getLogger(LogisticsTimeoutController.class);
    
    @Resource
    private LogisticsTimeoutService logisticsTimeoutService;
    
    @Resource
    private SellerService sellerService;
    
    @RequestMapping(value = "/summaryList")
    public ModelAndView list(@RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        ModelAndView mv = new ModelAndView("logisticsTimeout/summaryList");
        String startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        String endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("orderType", orderType);
        return mv;
    }
    
    @RequestMapping(value = "/jsonLogisticsTimeout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object jsonLogisticsTimeout(@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
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
            return logisticsTimeoutService.findAllLogisticsTimeoutOrders(startTime, endTime, orderType);
        }
        catch (Exception e)
        {
            logger.error("异步加载物流超时订单列表出错", e);
            return new ArrayList<>();
        }
    }
    
    @RequestMapping(value = "/exportResult")
    @ControllerLog(description = "商家物流时效更新管理-导出商家商家物流时效")
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
            
            List<Map<String, Object>> result = logisticsTimeoutService.findAllLogisticsTimeoutOrders(startTime, endTime, orderType);
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商家物流更新时效");
            String[] str = {"商家名称", "发货订单数", "时效内物流更新", "超时物流更新", "超时未更新", "总体未更新", "超时罚款（元）"};
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
                r.createCell(3).setCellValue(om.get("timeoutUpateAmount") + "");
                r.createCell(4).setCellValue(om.get("timeoutNoUpdateAmount") + "");
                r.createCell(5).setCellValue(om.get("notUpdateAmount") + "");
                r.createCell(6).setCellValue(om.get("amerceAmount") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "商家物流更新时效";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            logger.error("导出商家物流更新时效出错了！！！", e);
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
     * 
     * @param startTime
     * @param endTime
     * @param sellerId
     * @param orderIdList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/timeoutOrderList")
    public ModelAndView timeoutOrderList(@RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
        @RequestParam(value = "sellerId", required = false, defaultValue = "") String sellerId,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("logisticsTimeout/timeoutOrderList");
        if ("".equals(startTime))
        {
            startTime = DateTime.now().withTimeAtStartOfDay().toString("yyyy-MM-dd HH:mm:ss");
        }
        if ("".equals(endTime))
        {
            endTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toString("yyyy-MM-dd HH:mm:ss");
        }
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("sellerId", sellerId);
        mv.addObject("orderIdList", orderIdList);
        return mv;
    }
    
    /**
     * 异步加载商家订单列表
     * 
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
    @RequestMapping(value = "/jsonLogisticsOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLogisticsOrderInfo(
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
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, // 开始时间
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, // 结束时间
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
        
        para.put("sendTimeBegin", startTime);
        para.put("sendTimeEnd", endTime);
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
        para.put("orderDesc", 1);
        Map<String, Object> result = logisticsTimeoutService.jsonLogisticsOrders(para);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/exportLogisticsOrder")
    public void exportLogisticsOrder(
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
            @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, // 开始时间
            @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, // 结束时间
            @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList,
            @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout,
            HttpServletResponse response
    ) throws IOException {
        if (sellerId <= 0)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("rows", new ArrayList<>());
            //todo
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

        para.put("sendTimeBegin", startTime);
        para.put("sendTimeEnd", endTime);
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
        para.put("orderDesc", 1);
        OutputStream os = null;
        try {
            Map<String, Object> result = logisticsTimeoutService.jsonLogisticsOrders(para);
            List<Map<String, Object>> datas = (List<Map<String, Object>>) result.get("rows");
            for(Map<String, Object> data : datas) {
                data.put("isTimeout",((Integer)data.get("isTimeout")) == 0 ? "否" : "是");
                int complainStatus =  data.get("complainStatus") == null ? 0 : (Integer)data.get("complainStatus");
                if(complainStatus == 1) {
                    data.put("complainStatus", "处理中");
                } else if(complainStatus == 2 || complainStatus == 3 ){
                    data.put("complainStatus", "申诉明细");
                } else {
                    data.put("complainStatus", "未申诉");
                }
            }
            List<String> headers = Lists.newArrayList("orderId", "createTime", "payTime", "sendTime", "expireTime",
                    "sSellerName","isTimeout","number","oStatusDescripton", "raFullName", "raMobileNumber", "ologChannel"
            ,"ologNumber", "timeOutreason", "complainStatus");
            List<String> displayHeaders = Lists.newArrayList("序号","下单时间","付款时间","发货时间","超时日期",
                    "商家","是否超时","订单编号","订单状态","收货人","收货手机","物流公司","运单号","超时原因","申诉状态");

            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            String name = URLEncoder.encode("超时订单", "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s", name + ".xls"));
            os=response.getOutputStream();
            ExcelMaker.from(datas, headers).displayHeaders(displayHeaders).writeTo(os);
        } catch (Exception e) {
            logger.error("下载超时订单失败", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            String errorStr = "<script>alert('系统出错 重试或者联系开发');window.history.back();</script>";
            if (os == null)
                os = response.getOutputStream();
            os.write(errorStr.getBytes());
        } finally {
          if(os != null)
              os.close();
        }
    }

    
    /**
     * 物流超时原因列表
     * 
     * @return
     */
    @RequestMapping("/timeoutReasonList")
    public ModelAndView timeoutReasonList()
    {
        ModelAndView mv = new ModelAndView("logisticsTimeout/timeoutReasonList");
        return mv;
    }
    
    /**
     * 异步加载物流超时原因列表
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonLogisticsTimeoutReasonInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLogisticsTimeoutReasonInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
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
            resultMap = logisticsTimeoutService.jsonLogisticsTimeoutReasonInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载物流超时原因列表出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 新增问题模板
     * 
     * @param name
     * @return
     */
    @RequestMapping(value = "/saveTimeoutReason", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家物流时效更新管理-新增物流超时原因")
    public String saveTimeoutReason(@RequestParam(value = "id", required = false, defaultValue = "0") int id, @RequestParam(value = "name", required = true) String name)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            name = name.trim();
            return logisticsTimeoutService.saveTimeoutReason(id, name);
        }
        catch (Exception e)
        {
            logger.error("新增物流超时原因:" + name + "出错了", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/updateLogisticsTimeoutReasonStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家物流时效更新管理-修改物流超时原因状态")
    public String updateLogisticsTimeoutReasonStatus(@RequestParam(value = "id", required = true) int id, @RequestParam(value = "isAvailable", required = true) int isAvailable)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", id);
            para.put("isAvailable", isAvailable);
            
            return logisticsTimeoutService.updateLogisticsTimeoutReasonStatus(para);
            
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
     * 订单申诉列表
     * 
     * @return
     */
    @RequestMapping("/complainList")
    public ModelAndView complainList(@RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    {
        ModelAndView mv = new ModelAndView("logisticsTimeout/complainList");
        mv.addObject("orderType", orderType);
        return mv;
    }
    
    /**
     * 异步加载订单申诉列表
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonComplainOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonComplainOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
    
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
            para.put("orderDesc", 2);
            para.put("orderType", orderType);
            para.put("complainStatus", OrderEnum.LogisticsTimeoutComplainResultEnum.PROCESSING.getCode());
            return logisticsTimeoutService.jsonComplainOrderInfo(para);
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
    
    @RequestMapping("/complainDetail/{orderId}")
    public ModelAndView complainDetail(@PathVariable("orderId") int orderId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("logisticsTimeout/complainDetail");
        Map<String, Object> complainResult = logisticsTimeoutService.findLogisticsTimeoutByOid(orderId);
        if (complainResult == null || complainResult.isEmpty())
        {
            mv.addObject("orderNumber", "");
            mv.addObject("sellerName", "");
            mv.addObject("timeoutReason", "");
        }
        else
        {
            mv.addObject("orderNumber", complainResult.get("orderNumber"));
            mv.addObject("sellerName", complainResult.get("sellerName"));
            
            int reasonId = Integer.parseInt(complainResult.get("reasonId") == null ? "0" : complainResult.get("reasonId").toString());
            mv.addObject("timeoutReason", logisticsTimeoutService.findLogisticsTimeoutReasonById(reasonId));
        }
        List<Map<String, Object>> complainList = logisticsTimeoutService.findComplainDetailsByOrderId(orderId);
        if (complainList != null && complainList.size() > 0)
        {
            mv.addObject("complainList", complainList);
        }
        return mv;
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
            List<Map<String, Object>> info = logisticsTimeoutService.findAllTimeoutReason(para);
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
            logger.error("加载物流更新时效超时原因发生异常", e);
        }
        return JSON.toJSONString(resultList);
    }
    
    @RequestMapping(value = "/dealComplain", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家物流时效更新管理-申诉处理")
    public String dealComplain(@RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId, //
        @RequestParam(value = "complainId", required = false, defaultValue = "0") int complainId, //
        @RequestParam(value = "timeoutReasonId", required = false, defaultValue = "0") int timeoutReasonId, //
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
            return logisticsTimeoutService.dealComplain(orderId, complainId, timeoutReasonId, dealResult, remark);
        }
        catch (Exception e)
        {
            logger.error("商家物流时效更新管理-申诉处理发生异常", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
            return JSON.toJSONString(resultMap);
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
    @ControllerLog(description = "商家物流时效更新管理-QC处理超时订单")
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
            return logisticsTimeoutService.addTimeoutOrderQcDeal(orderId, reasonId, result, remark);
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
    @ControllerLog(description = "商家物流时效更新管理-QC批量处理超时订单")
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
            return logisticsTimeoutService.addBatchTimeoutOrderQcDeal(orderIds, reasonId, result, remark);
        }
        catch (Exception e)
        {
            logger.error("QC处理超时订单发生异常", e);
            resultMap.put("status", 0);
            resultMap.put("msg", "处理失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
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
        @RequestParam(value = "orderType", required = false, defaultValue = "1") int orderType)
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
                // 默认导出{startTime}至{endTime}超时物流未更新的订单
                para.put("expireTimeBegin", startTime);
                para.put("expireTimeEnd", endTime);
                para.put("isTimeout", 1);
                para.put("logisticsTime", "0000-00-00 00:00:00");
                fileNamePrefix = "全部商家超时未更新订单";
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
                    // 导出{startTime}至{endTime}时间内{sellerId}的全部订单
                    fileNamePrefix = se.getRealSellerName() + "的全部发货订单";
                    
                }
                else if (exportType == 2)
                {
                    // 导出{startTime}至{endTime}时间内{sellerId}的准时发货
                    fileNamePrefix = se.getRealSellerName() + "的时效内物流更新订单";
                }
                else if (exportType == 3)
                {
                    // 导出{startTime}至{endTime}时间内{sellerId}的超时发货订单
                    fileNamePrefix = se.getRealSellerName() + "的超时物流更新订单";
                }
                else if (exportType == 4)
                {
                    // 导出{startTime}至{endTime}时间内{sellerId}的超时未发货订单
                    fileNamePrefix = se.getRealSellerName() + "的超时未更新订单";
                }
                else if (exportType == 5)
                {
                    // 导出{startTime}至{endTime}时间内{sellerId}的未超时未发货订单
                    fileNamePrefix = se.getRealSellerName() + "的总体未更新订单";
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
            if (logisticsTimeoutService.getExportOrderNums(para) > CommonConstant.workbook_num_6w)
            {
                errorMessage = "数据量大于" + CommonConstant.workbook_num_6w + "，请缩小范围！";
                throw new RuntimeException();
            }
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileNamePrefix, "UTF-8") + fileNameSuffix + ".xlsx");
            List<OrderDetailInfoForSeller> rows = logisticsTimeoutService.findAllTimeoutOrderDetail(para);
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
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * 更新订单物流超时状态
     * @param id：订单Id
     * @param isTimeout：是否超时，1是，0否
     * @return
     */
    @RequestMapping(value = "/updateTimeOutStatus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "物流管理-更新物流超时状态")
    public ResultEntity updateTimeOutStatus(@RequestParam(value = "orderId", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isTimeout", required = false, defaultValue = "0") int isTimeout)
    {
        try
        {
            Map<String, Object> param = new HashMap<>();
            param.put("orderId", id);
            param.put("isTimeout", isTimeout);
            Preconditions.checkArgument(logisticsTimeoutService.updateLogisticsTimeout(param) > 0);
            return ResultEntity.getSuccessResult("更新成功");
        }
        catch (Exception e)
        {
            logger.error("更新物流超时状态！", e);
            return ResultEntity.getFailResult("更新物流超时状态失败");
        }
    }
}
