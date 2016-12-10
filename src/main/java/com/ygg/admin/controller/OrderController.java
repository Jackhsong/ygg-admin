package com.ygg.admin.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.KdCompanyEnum;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.ReceiveAddressEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.User;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.service.UserService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonEnum.OrderAppChannelEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.FileUtil;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.util.StringUtils;
import com.ygg.admin.util.ZipCompressorByAnt;

/**
 * 订单相关控制器
 * 
 * @author Administrator
 *         
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController
{
    
    Logger log = Logger.getLogger(OrderController.class);
    
    // 信号量
    final Semaphore semp = new Semaphore(2);
    
    @Resource(name = "orderService")
    private OrderService orderService = null;
    
    @Resource
    private SellerService sellerService;
    
    @Resource
    private SystemLogService logService;
    
    @Resource
    private UserService userService;
    
    /**
     * 订单列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, //
        @RequestParam(value = "source", required = false, defaultValue = "1") int source// 1:正常列表，2：手工wap列表
    )
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("order/list");
        if (source != 1)
        {
            mv.addObject("accountId", YggAdminProperties.getInstance().getPropertie("admin_account_id"));
        }
        else
        {
            mv.addObject("accountId", "");
            
        }
        // 加入最近一个时间段 1，当前时间在15点前：显示前一天15点到今天10点。2，当前时间在15点后，显示今天10点到15点
        int hour = DateTime.now().getHourOfDay();
        String startTimeBegin = "";
        String startTimeEnd = "";
        if (hour < 15)
        {
            startTimeBegin = DateTime.now().plusDays(-1).withTimeAtStartOfDay().plusHours(15).toString("yyyy-MM-dd HH:mm:ss");
            startTimeEnd = DateTime.now().withTimeAtStartOfDay().plusHours(10).toString("yyyy-MM-dd HH:mm:ss");
        }
        else
        {
            startTimeBegin = DateTime.now().withTimeAtStartOfDay().plusHours(10).toString("yyyy-MM-dd HH:mm:ss");
            startTimeEnd = DateTime.now().withTimeAtStartOfDay().plusHours(15).toString("yyyy-MM-dd HH:mm:ss");
        }
        mv.addObject("startTimeBegin", startTimeBegin);
        mv.addObject("startTimeEnd", startTimeEnd);
        
        // 客户端类型
        List<Map<String, Object>> channelList = new ArrayList<Map<String, Object>>();
        for (OrderAppChannelEnum e : OrderAppChannelEnum.values())
        {
            Map<String, Object> currMap = new HashMap<String, Object>();
            currMap.put("value", e.ordinal());
            currMap.put("desc", e.getDescription());
            channelList.add(currMap);
        }
        mv.addObject("channelList", channelList);
        return mv;
    }
    
    @RequestMapping(value = "/jsonOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderInfo(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "channelType", required = false, defaultValue = "-1") int channelType, // 客户端类型
        @RequestParam(value = "sourceType", required = false, defaultValue = "") String orderSource, // 订单来源
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品ID
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "sellerType", required = false, defaultValue = "0") int sellerType, // 发货类型
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, // 商家ID
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName, // 用户名
        @RequestParam(value = "logisticsNumber", required = false, defaultValue = "") String logisticsNumber, // 运单号
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId, // 用户ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "timeType", required = false, defaultValue = "0") int timeType, // 订单时间类型，1：最近一个时间段，2：自定义时间
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2, //
        @RequestParam(value = "fx", required = false, defaultValue = "0") int fx, // 是否是分销订单
        @RequestParam(value = "operaStatus", required = false, defaultValue = "-1") int operaStatus, // 导出状态:1:已导出;0:未导出
        @RequestParam(value = "isSettlement", required = false, defaultValue = "-1") int isSettlement, // 是否结算：1:已结算，0:未结算
        @RequestParam(value = "remark2Type", required = false, defaultValue = "-1") int remark2Type, // 有无客服备注：1:有，0:无，-1：全部
        @RequestParam(value = "code", required = false, defaultValue = "") String code)
        throws Exception
    {



        /**
         * 订单列表搜索条件太多了，
         *
         * 增加其他条件时请考虑有没有必要。。。改砍的砍掉。。。
         */



        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        
        long sqlType = 1;
        // 纯order表 1
        if (timeType == 1)
        { // 最近一个时间段
            if (!"".equals(startTime1))
            {
                para.put("startTimeBegin", startTime1);
            }
            if (!"".equals(endTime1))
            {
                para.put("startTimeEnd", endTime1);
            }
        }
        else
        { // 自定义时间
            if (!"".equals(startTime2))
            {
                para.put("startTimeBegin", startTime2);
            }
            if (!"".equals(endTime2))
            {
                para.put("startTimeEnd", endTime2);
            }
        }
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
        if (channelType != -1)
        {
            para.put("channelType", channelType);
        }
        if (operaStatus != -1)
        {
            para.put("operationStatus", operaStatus);
        }
        if (accountId != 0)
        {
            para.put("accountId", accountId);
        }
        if (isSettlement != -1)
        {
            para.put("isSettlement", isSettlement);
        }
        para.put("remark2Type", remark2Type);
        
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
        if (fx != 0)
        {
            para.put("productRemark", "分销%");
            sqlType = 123;
        }
        
        // 需要关联seller表 4
        if (sellerType != 0)
        {
            para.put("sellerType", sellerType);
        }
        
        // account 5
        if (!"".equals(accountName))
        {
            para.put("accountName", "%" + accountName + "%");
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
        
        // order_logistics 7
        if (!"".equals(logisticsNumber))
        {
            para.put("logisticsNumber", logisticsNumber);
        }
        
        para.put("sqlType", sqlType);
        
        if (!"".equals(orderSource))
        {
            para.put("orderSource", orderSource);
        }
        
        para.put("datasource", "master");
        Map<String, Object> result = orderService.ajaxOrderInfo(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 导出订单数据
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/export")
    @ControllerLog(description = "订单管理-导出订单")
    public void export(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "channelType", required = false, defaultValue = "-1") int channelType, // 客户端类型
        @RequestParam(value = "sourceType", required = false, defaultValue = "") String orderSource, // 订单来源
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品ID
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "sellerType", required = false, defaultValue = "0") int sellerType, // 发货类型
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, // 商家ID
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName, // 用户名
        @RequestParam(value = "logisticsNumber", required = false, defaultValue = "") String logisticsNumber, // 运单号
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId, // 用户ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "timeType", required = false, defaultValue = "0") int timeType, // 订单时间类型，1：最近一个时间段，2：自定义时间
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2, //
        @RequestParam(value = "fx", required = false, defaultValue = "0") int fx, // 是否是分销订单
        @RequestParam(value = "isSettlement", required = false, defaultValue = "-1") int isSettlement, // 是否结算：1:已结算，0:未结算
        @RequestParam(value = "remark2Type", required = false, defaultValue = "-1") int remark2Type, // 有无客服备注：1:有，0:无，-1：全部
        @RequestParam(value = "exportType", required = false, defaultValue = "result") String exportType, // result:导出结果表；sellerAllStatus:导出明细
        @RequestParam(value = "code", required = false, defaultValue = "") String code)
        throws Exception
    {
        if (semp.availablePermits() < 1)
        {
            String errorMessage = "已有多人在导出操作，请稍后...";
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Content-disposition", "");
            String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
            response.getOutputStream().write(errorStr.getBytes());
            response.getOutputStream().close();
            return;
        }
        
        OutputStream fOut = null;
        String errorMessage = "";
        try
        {
            // 获取许可
            semp.acquire();
            
            Map<String, Object> para = new HashMap<>();
            long sqlType = 1;
            // 纯order表 1
            if (timeType == 1)
            { // 最近一个时间段
                if (!"".equals(startTime1))
                {
                    para.put("startTimeBegin", startTime1);
                }
                if (!"".equals(endTime1))
                {
                    para.put("startTimeEnd", endTime1);
                }
            }
            else
            { // 自定义时间
                if (!"".equals(startTime2))
                {
                    para.put("startTimeBegin", startTime2);
                }
                if (!"".equals(endTime2))
                {
                    para.put("startTimeEnd", endTime2);
                }
            }
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
            if (channelType != -1)
            {
                para.put("channelType", channelType);
            }
            if (accountId != 0)
            {
                para.put("accountId", accountId);
            }
            if (isSettlement != -1)
            {
                para.put("isSettlement", isSettlement);
            }
            para.put("remark2Type", remark2Type);
            
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
            if (fx != 0)
            {
                para.put("productRemark", "分销%");
                sqlType = 123;
            }
            
            // 需要关联seller表 4
            if (sellerType != 0)
            {
                para.put("sellerType", sellerType);
            }
            
            // account 5
            if (!"".equals(accountName))
            {
                para.put("accountName", "%" + accountName + "%");
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
            
            // order_logistics 7
            if (!"".equals(logisticsNumber))
            {
                para.put("logisticsNumber", logisticsNumber);
            }
            
            para.put("sqlType", sqlType);
            
            if (!"".equals(orderSource))
            {
                para.put("orderSource", orderSource);
            }
            
            para.put("datasource", "slave");
            
            if ("result".equals(exportType))
            {
                // 导出列表结果
                // 1 判断总条数
                if (orderService.getExportOrderNums(para) > CommonConstant.workbook_num_6w)
                {
                    errorMessage = "数据量大于" + CommonConstant.workbook_num_6w + "，请缩小范围！";
                    throw new RuntimeException();
                }
                fOut = response.getOutputStream();
                response.setContentType("application/vnd.ms-excel");
                String codedFileName = "order";
                codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
                Map<String, Object> result = orderService.ajaxOrderInfo(para);
                List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
                String[] str = {"下单时间", "用户ID", "付款时间", "付款方式", "发货时间", "订单来源", "订单编号", "订单状态", "备注", "订单总价", "实付金额", "收货人", "收货手机", "商家", "发货地", "物流公司", "运单号"};
                SXSSFWorkbook workbook = POIUtil.createSXSSFWorkbookTemplate(str);
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 0; i < rows.size(); i++)
                {
                    int cellIndex = 0;
                    Map<String, Object> currMap = rows.get(i);
                    Row r = sheet.createRow(i + 1);
                    r.createCell(cellIndex++).setCellValue(currMap.get("createTime") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("accountId") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("payTime") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("payChannel") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sendTime") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("orderChannel") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("number") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("oStatusDescripton") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("totalPrice") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("realPrice") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("raFullName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("raMobileNumber") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sSellerName") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("sSendAddress") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("ologChannel") + "");
                    r.createCell(cellIndex++).setCellValue(currMap.get("ologNumber") + "");
                }
                workbook.write(fOut);
                fOut.close();
                workbook.close();
                workbook.dispose();
            }
            else
            {
                // 导出订单明细
                String dowName = "商家订单明细";
                // 1 判断总条数
                if (orderService.getExportOrderNums(para) > CommonConstant.workbook_num_6w)
                {
                    errorMessage = "数据量大于" + CommonConstant.workbook_num_6w + "，请缩小范围！";
                    throw new RuntimeException();
                }
                
                String result = orderService.exportAllStatusOrder(para);
                String zipName = result + ".zip";
                ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
                zca.compressExe(result);
                FileInputStream downFile = new FileInputStream(zipName);
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setContentType("application/x-msdownload;charset=utf-8");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
                fOut = response.getOutputStream();
                // 设置缓冲区为1024个字节，即1KB
                byte bytes[] = new byte[1024];
                int len = 0;
                // 读取数据。返回值为读入缓冲区的字节总数,如果到达文件末尾，则返回-1
                while ((len = downFile.read(bytes)) != -1)
                {
                    // 将指定 byte数组中从下标 0 开始的 len个字节写入此文件输出流,(即读了多少就写入多少)
                    fOut.write(bytes, 0, len);
                }
                fOut.close();
                downFile.close();
                FileUtil.deleteFile(result);
                FileUtil.deleteFile(zipName);
            }
            
            // 导出日志 begin
            Map<String, Object> logInfoMap = new HashMap<>();
            logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.ORDER_MANAGEMENT.ordinal());
            logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.EXPORT_ORDER.ordinal());
            logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal());
            StringBuffer params = new StringBuffer("");
            Enumeration<String> paramNames = request.getParameterNames();
            if (paramNames != null)
            {
                while (paramNames.hasMoreElements())
                {
                    String name = paramNames.nextElement();
                    String value = request.getParameter(name);
                    if ("".equals(params.toString()))
                    {
                        params.append(name).append("=").append(value);
                    }
                    else
                    {
                        params.append(",").append(name).append("=").append(value);
                    }
                }
            }
            logInfoMap.put("filter", params.toString());
            logService.logger(logInfoMap);
            // 导出日志 end
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
            // 访问完后，释放
            semp.release();
        }
    }
    
    /**
     * 订单发货管理
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderSendGoods")
    public ModelAndView orderSendGoods(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("order/sendGoods");
        return mv;
    }
    
    /**
     * 订单详情页
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail/{id}")
    public ModelAndView detail(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> result = orderService.findOrderDetailInfo(id, 1);
        if (result.get("id") == null)
        {
            mv.setViewName("error/404");
            return mv;
        }
        mv.addObject("detail", result);
        mv.setViewName("order/detail");
        return mv;
    }
    
    /**
     * 异步得到订单商品具体信息
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String orderProductInfo(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        String jsonStr = orderService.orderProductJsonStr(id);
        return jsonStr;
    }
    
    /**
     * 得到物流公司编码以json格式返回
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonCompanyCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCompanyCode(HttpServletRequest request)
        throws Exception
    {
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        for (KdCompanyEnum companyEnum : KdCompanyEnum.values())
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", companyEnum.getCompanyName());
            map.put("text", companyEnum.getCompanyName());
            codeList.add(map);
        }
        return JSON.toJSONString(codeList);
    }
    
    /**
     * 根据ID对订单进行发货处理
     * 
     * @param request
     * @param channel
     * @param number
     * @param money
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-订单发货")
    public String sendOrder(HttpServletRequest request, @RequestParam(value = "orderId", required = true) int orderId, // 订单ID
        @RequestParam(value = "sendType", required = false, defaultValue = "1") int sendType, // 是否有物流
                                                                                              // 1：有，0：没有
        @RequestParam(value = "channel", required = false, defaultValue = "") String channel, // 物流渠道
        @RequestParam(value = "number", required = false, defaultValue = "") String number, // 物流单号
        @RequestParam(value = "money", required = false, defaultValue = "0.0") float money// 物流运费
    )
        throws Exception
    {
        number = number.trim();
        if ((sendType == 1) && ("".equals(channel) || "".equals(number) || !StringUtils.isOnlyLettersAndNumber(number)))
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 3);
            result.put("msg", "请检查物流渠道和物流单号是否正确");
            return JSON.toJSONString(result);
        }
        int resultStaus = 0;
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("orderId", orderId);
        if (sendType == 0)
        {
            // 无物流信息
            para.put("channel", "无需物流");
            para.put("number", "");
            para.put("money", 0);
            resultStaus = orderService.sendOrderWithOutLogistics(para);
        }
        else
        {
            para.put("channel", channel);
            para.put("number", number.replaceAll(" ", ""));
            para.put("money", money);
            resultStaus = orderService.sendOrder(para);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", resultStaus);
        return JSON.toJSONString(result);
    }
    
    /**
     * 快递100订阅回调接口
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/kd100Callback", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String kd100Callback(HttpServletRequest request)
        throws Exception
    {
        String param = request.getParameter("param");
        String orderId = request.getParameter("orderId");
        boolean isOk = orderService.kd100CallBack(param, orderId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (isOk)
        {
            resultMap.put("result", true);
            resultMap.put("returnCode", 200);
            resultMap.put("message", "成功");
        }
        else
        {
            resultMap.put("result", false);
            resultMap.put("returnCode", 500);
            resultMap.put("message", "保存失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 批量发货处理
     * 
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/batchSendOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-批量发货")
    public String batchSendOrder(HttpServletRequest request, @RequestParam("orderFile") MultipartFile file,
        @RequestParam(value = "importType", required = false, defaultValue = "0") int importType)
        throws Exception
    {
        try
        {
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 1);
            Sheet sheet = workbook.getSheetAt(0);
            int startNum = sheet.getFirstRowNum();
            int lastNum = sheet.getLastRowNum();
            if (startNum == lastNum)
            {// 可过滤第一行，因为第一行是标题
                map.put("status", 0);
                map.put("msg", "文件为空请确认！");
                return JSON.toJSONString(map);
            }
            HttpSession session = request.getSession();
            List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
            /** excel数据是否正确，该状态要传回客户端 */
            boolean isRight = true;
            int okNum = 0;
            int failNum = 0;
            for (int i = startNum + 1; i <= lastNum; i++)
            {
                Row row = sheet.getRow(i);
                Cell cell0 = row.getCell(0);
                Cell cell1 = row.getCell(1);
                Cell cell2 = row.getCell(2);
                Cell cell3 = row.getCell(3);
                if (cell0 != null)
                {
                    cell0.setCellType(Cell.CELL_TYPE_STRING);
                }
                if (cell1 != null)
                {
                    cell1.setCellType(Cell.CELL_TYPE_STRING);
                }
                if (cell2 != null)
                {
                    cell2.setCellType(Cell.CELL_TYPE_STRING);
                }
                if (cell3 != null)
                {
                    cell3.setCellType(Cell.CELL_TYPE_STRING);
                }
                String orderNumber = cell0 == null ? "" : cell0.getStringCellValue();
                String channel = cell1 == null ? "" : cell1.getStringCellValue();
                String number = cell2 == null ? "" : cell2.getStringCellValue();
                String sendTime = cell3 == null ? "" : cell3.getStringCellValue();
                
                Map<String, Object> para = new HashMap<>();
                para.put("iList", iList);
                if (orderNumber.startsWith("HB"))
                {
                    orderNumber = "HB" + StringUtils.trimToNumber(orderNumber);
                }
                else
                {
                    orderNumber = StringUtils.trimToNumber(orderNumber);
                }
                para.put("orderNumber", orderNumber);
                para.put("channel", channel);
                para.put("number", number.replaceAll(" ", ""));
                para.put("sendTime", sendTime);
                para.put("money", 0);
                if (importType == 1)
                {
                    // 模拟导入
                    boolean resultBoolean = orderService.sendOrderTest(para);
                    if (!resultBoolean && isRight)
                    {
                        isRight = false;
                    }
                    if (resultBoolean)
                    {
                        okNum++;
                    }
                    else
                    {
                        failNum++;
                    }
                }
                else if (importType == 3)
                {
                    // 确认导入
                    if (orderNumber.startsWith("HB"))
                    {
                        orderService.sendOrderHB(para);
                    }
                    else
                    {
                        orderService.sendOrder(para);
                    }
                }
            }
            if (importType == 3)
            {
                map.put("status", 11);
                map.put("msg", "导入成功");
            }
            if (importType == 1)
            {
                // 将信息放入session中
                session.setAttribute("testSendList", iList);
                session.setAttribute("testSendListTime", System.currentTimeMillis());
                // session.setAttribute("okNum", okNum);
                // session.setAttribute("failNum", failNum);
                map.put("okNum", okNum);
                map.put("failNum", failNum);
                map.put("isRight", isRight ? 1 : 0);
                boolean canChangeIsRightFlag = true;
                if (!isRight)
                {
                    for (Map<String, Object> it : iList)
                    {
                        if (canChangeIsRightFlag)
                        {
                            Integer errorCode = Integer.valueOf(it.get("errorCode") + "");
                            // 当且仅当错误全部为"已发货"时，才可以确认导入。
                            if (errorCode != 2)
                            {
                                canChangeIsRightFlag = false;
                            }
                        }
                    }
                }
                map.put("canChangeIsRightFlag", canChangeIsRightFlag); // 是否可用修改isRight
            }
            return JSON.toJSONString(map);
        }
        catch (IOException e)
        {
            log.error("批量发货失败！", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "操作失败！");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 异步获得模拟订单导入结果 -- 成功与失败数量
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/receiveResultNum", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String receiveResultNum(HttpServletRequest request, @RequestParam(value = "delete", required = false, defaultValue = "1") int delete)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("okNum", 0);
        map.put("failNum", 0);
        HttpSession session = request.getSession();
        Object okNum = session.getAttribute("okNum");
        Object failNum = session.getAttribute("failNum");
        if (okNum != null && failNum != null)
        {
            map.put("okNum", (int)okNum);
            map.put("failNum", (int)failNum);
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 异步获得模拟订单导入结果 -- 结果列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/receiveResult", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String receiveResult(HttpServletRequest request, @RequestParam(value = "delete", required = false, defaultValue = "1") int delete)
        throws Exception
    {
        List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
        HttpSession session = request.getSession();
        Object iList = session.getAttribute("testSendList");
        Object timeLong = session.getAttribute("testSendListTime");
        if (iList != null && timeLong != null)
        {
            long rightTime = (Long)timeLong;
            // 30000ms内有效
            if (System.currentTimeMillis() - rightTime > 30000)
            {
                session.removeAttribute("testSendList");
                session.removeAttribute("testSendListTime");
                log.debug("超时，从session移除模拟订单导入结果");
                return JSON.toJSONString(rList);
            }
            if (delete == 1)
            {// 来自页面刷新
                session.removeAttribute("testSendList");
                session.removeAttribute("testSendListTime");
                return JSON.toJSONString(rList);
            }
            List<Map<String, Object>> rightList = (List<Map<String, Object>>)iList;
            return JSON.toJSONString(rightList);
        }
        return JSON.toJSONString(rList);
    }
    
    /**
     * 导出发货excel
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportSendResult")
    @ControllerLog(description = "订单管理-导出订单发货结果")
    public void exportSendResult(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "importResult";
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
            String[] str = {"导入状态", "说明", "订单号", "创建时间", "付款时间", "发货时间", "物流公司", "订单编号"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            HttpSession session = request.getSession();
            Object iList = session.getAttribute("testSendList");
            if (iList != null)
            {
                List<Map<String, Object>> rightList = (List<Map<String, Object>>)iList;
                int index = 1;
                for (Map<String, Object> currMap : rightList)
                {
                    Row r = sheet.createRow(index++);
                    r.createCell(0).setCellValue(currMap.get("status") + "");
                    r.createCell(1).setCellValue(currMap.get("msg") + "");
                    r.createCell(2).setCellValue(currMap.get("orderNumber") + "");
                    r.createCell(3).setCellValue(currMap.get("createTime") + "");
                    r.createCell(4).setCellValue(currMap.get("payTime") + "");
                    r.createCell(5).setCellValue(currMap.get("sendTime") + "");
                    r.createCell(6).setCellValue(currMap.get("channel") + "");
                    r.createCell(7).setCellValue(currMap.get("number") + "");
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
            response.setHeader("content-disposition", "");
            if (fOut == null)
            {
                fOut = response.getOutputStream();
            }
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
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
     * 导出发货excel
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportSendTemplate")
    @ControllerLog(description = "订单管理-导出订单发货信息")
    public void exportSendTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "importTemplate";
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
            String[] str = {"订单编号", "物流公司", "运单编号", "(可选)发货时间(格式（时间前面必须加上'）：'2014-01-02 10:00:02)"};
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
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            if (fOut == null)
                fOut = response.getOutputStream();
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
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
    
    /** 转发到修改订单状态页面 */
    @RequestMapping("/upTrade")
    public ModelAndView updateTradeStatus(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("order/updateTradeStatus");
        return mv;
    }
    
    /**
     * 查询订单信息
     */
    @RequestMapping(value = "/searchOrder", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String searchOrder(HttpServletRequest request, @RequestParam(value = "number", required = false, defaultValue = "0") String number)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("number", number);
        OrderEntity order = orderService.findOrderByNumber(number);
        Map<String, Object> result = new HashMap<String, Object>();
        if (order == null)
        {
            result.put("status", 0);
            result.put("msg", "无此订单信息");
            return JSON.toJSONString(result);
        }
        result.put("status", 1);
        result.put("number", order.getNumber());
        result.put("id", order.getId());
        result.put("msg", OrderEnum.ORDER_STATUS.getDescByCode(order.getStatus()));
        result.put("orderStatus", order.getStatus());
        return JSON.toJSONString(result);
    }
    
    /**
     * 
     * @param request
     * @param number：订单编号
     * @param id：订单Id
     * @param mark：备注
     * @param oldStatus：修改之前的状态
     * @param newStatus：修改之后的状态
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateStatus", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-更新订单状态")
    public String updateStatus(HttpServletRequest request, @RequestParam(value = "number", required = true) String number,
        @RequestParam(value = "orderId", required = true) int id, @RequestParam(value = "mark", required = true) String mark,
        @RequestParam(value = "oldStatus", required = true) int oldStatus, @RequestParam(value = "newStatus", required = true) int newStatus)
        throws Exception
    {
        try
        {
            String result = orderService.updateOrderStatus(id, number, oldStatus, newStatus, mark);
            JSONObject jsonResult = JSON.parseObject(result);
            if (jsonResult.getIntValue("status") == CommonConstant.COMMON_YES)
            {
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.ORDER_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_ORDER_STATUS.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal());
                    logInfoMap.put("objectId", id);
                    logInfoMap.put("newStatus", OrderEnum.ORDER_STATUS.getDescByCode(newStatus));
                    logInfoMap.put("oldStatus", OrderEnum.ORDER_STATUS.getDescByCode(oldStatus));
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
            return result;
        }
        catch (Exception e)
        {
            log.error("修改失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "修改失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 得到订单操作记录 findOrderOperation
     */
    @RequestMapping(value = "/findOrderOperation", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findOrderOperation(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
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
        String result = orderService.jsonOrderOperationInfo(para);
        return result;
    }
    
    /**
     * 修改订单备注
     * 
     * @param request
     * @param id：订单Id
     * @param type：type=1,表示修改商家备注；type=2表示修改客服备注
     * @param remark
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateMarks", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-修改订单备注")
    public String updateMarks(HttpServletRequest request, @RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "type", required = true) int type, //
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark//
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        if (type == 1)
        {
            para.put("remark", remark);
        }
        else if (type == 2)
        {
            String username = SecurityUtils.getSubject().getPrincipal() + "";
            User user = userService.findByUsername(username);
            para.put("remark2", remark + " from " + (user == null ? username : user.getRealname()) + DateTime.now().toString(" yyyy-MM-dd HH:mm:ss") + "&nbsp;&nbsp;&nbsp;");
        }
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            int result = orderService.updateOrder(para);
            
            // 修改备注 记录日志
            if (result == 1)
            {
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.ORDER_MANAGEMENT.ordinal());
                    if (type == 1)
                    {
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_ORDER_REMARK.ordinal());
                    }
                    else
                    {
                        logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_ORDER_REMARK2.ordinal());
                    }
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal());
                    logInfoMap.put("objectId", id);
                    logInfoMap.put("content", remark);
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error("订单修改备注，日志记录失败", e);
                }
            }
            map.put("status", result);
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 修改商品价格
     * 
     * @param id
     * @param price
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updatePrice", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-订单调价")
    public String updatePrice(@RequestParam(value = "id", required = true) int id, //
        @RequestParam(value = "isAvailableCoupon", required = false, defaultValue = "1") int isAvailableCoupon, //
        @RequestParam(value = "price", required = false, defaultValue = "0") String price//
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("isAvailableCoupon", isAvailableCoupon);
        float newPrice = 0;
        try
        {
            newPrice = Float.valueOf(price).floatValue();
        }
        catch (Exception e)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "价格异常");
            return JSON.toJSONString(map);
        }
        para.put("totalPrice", newPrice);
        try
        {
            Map<String, Object> map = new HashMap<String, Object>();
            OrderEntity order = orderService.findOrderById(id);
            // 系统暂不支持调高价格，请联系管理员
            if (newPrice < 0 || order.getTotalPrice() < newPrice || order.getRealPrice() < newPrice)
            {
                map.put("status", 0);
                map.put("msg", "系统暂不支持调高价格，请联系管理员");
                return JSON.toJSONString(map);
            }
            // 本次调整价格
            float adjustPrice = order.getRealPrice() - newPrice;
            para.put("adjustPrice", adjustPrice + order.getAdjustPrice());
            para.put("totalPrice", order.getTotalPrice() - adjustPrice);
            para.put("realPrice", order.getRealPrice() - adjustPrice);
            int result = orderService.updatePrice(para);
            map.put("status", result);
            map.put("msg", result == 1 ? "保存成功" : "保存失败，检查订单状态");
            // 订单改价时记录日志
            if (result == 1)
            {
                try
                {
                    Map<String, Object> logInfoMap = new HashMap<String, Object>();
                    logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.ORDER_MANAGEMENT.ordinal());
                    logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.MODIFY_ORDER_PRICE.ordinal());
                    logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal());
                    logInfoMap.put("object", order);
                    logInfoMap.put("newPrice", String.format("%.2f", newPrice));
                    logService.logger(logInfoMap);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage(), e);
                }
            }
            
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "保存失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 获取所有市信息
     */
    @RequestMapping(value = "/getAllCity", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAllCity(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        return orderService.jsonAllCityByProvinceId(Integer.valueOf(id));
    }
    
    /**
     * 获取所有地区信息
     */
    @RequestMapping(value = "/getAllDistrict", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAllDistrict(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        return orderService.jsonAllDistrictByCityId(Integer.valueOf(id));
    }
    
    /**
     * 修改订单收货信息
     */
    @RequestMapping(value = "/updateAddress", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-修改订单收货信息")
    public String updateAddress(HttpServletRequest request, ReceiveAddressEntity address, int tradeId)
        throws Exception
    {
        try
        {
            if (address.getProvince() == null || address.getCity() == null || address.getDistrict() == null || address.getFullName() == null || address.getMobileNumber() == null
                || address.getDetailAddress() == null || "0".equals(address.getProvince()) || "0".equals(address.getCity()) || "0".equals(address.getDistrict())
                || "".equals(address.getFullName()) || "".equals(address.getMobileNumber()) || "".equals(address.getDetailAddress()))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "请写完整信息");
                return JSON.toJSONString(map);
            }
            if (!CommonUtil.isMobile(address.getMobileNumber()))
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("status", 0);
                map.put("msg", "手机号码不符合要求");
                return JSON.toJSONString(map);
            }
            Map<String, Object> result = orderService.updateAddress(address, tradeId);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("修改订单收货信息失败！！！", e);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "修改失败");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 超24未发货订单统计页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unSendGoodsList")
    public ModelAndView unSendGoodsList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("nowTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        mv.addObject("selectTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        mv.setViewName("order/unSendGoodsList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonUnSendGoodsList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonhbOrderList(HttpServletRequest request, @RequestParam(value = "type", required = false, defaultValue = "1") int type)
        throws Exception
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (type == 1)
        {
            result = orderService.findAllUnSendGoodInfo();
        }
        return JSON.toJSONString(result);
    }
    
    /**
     * 导出未发货订单统计 按商家
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportUnSendGoods")
    @ControllerLog(description = "订单管理-按商家导出未发货订单统计")
    public void exportUnSendGoods(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        List<Map<String, Object>> result = orderService.findAllUnSendGoodInfo();
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "24小时以上未发货商家订单列表";
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
            String[] str = {"商家", "招商负责人", "发货地", "24小时-48小时未发货订单数", "24-48小时未发货订单金额", "超48小时未发货订单数", "超48小时未发货订单金额"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> currMap = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(currMap.get("sellerName") + "");
                r.createCell(1).setCellValue(currMap.get("responsibilityPerson") + "");
                r.createCell(2).setCellValue(currMap.get("sendAddress") + "");
                r.createCell(3).setCellValue(currMap.get("countIds_24_to_48") == null ? "" : currMap.get("countIds_24_to_48") + "");
                r.createCell(4).setCellValue(currMap.get("sumTotal_24_to_48") == null ? "" : currMap.get("sumTotal_24_to_48") + "");
                r.createCell(5).setCellValue(currMap.get("countIds_upper_48") == null ? "" : currMap.get("countIds_upper_48") + "");
                r.createCell(6).setCellValue(currMap.get("sumTotal_upper_48") == null ? "" : currMap.get("sumTotal_upper_48") + "");
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
            response.setHeader("content-disposition", "");
            if (fOut == null)
                fOut = response.getOutputStream();
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
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
     * 超24未发货 订单明细
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unSendGoodsListDetail")
    public ModelAndView unSendGoodsListDetail(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        String id = request.getParameter("id");
        String type = request.getParameter("operType") == null ? "2" : request.getParameter("operType");
        if (id != null)
        {
            mv.addObject("id", id);
            SellerEntity seller = sellerService.findSellerById(Integer.valueOf(id));
            mv.addObject("seller", seller);
        }
        else
        {
            mv.addObject("id", "0");
        }
        if ("1".equals(type)) // 查询当天15点之前未发货订单
        {
            String beginTime = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
            String endTime = DateTime.now().withHourOfDay(15).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss");
            mv.addObject("selectTime", beginTime + " ~ " + endTime);
        }
        else if ("2".equals(type)) // 查询超24小时未发货订单
        {
            mv.addObject("selectTime", DateTime.now().minusHours(24).toString("yyyy-MM-dd HH:mm:ss"));
        }
        mv.addObject("operType", type);
        mv.setViewName("order/unSendGoodsListDetail");
        return mv;
    }
    
    @RequestMapping(value = "/jsonUnSendGoodsListDetail", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonUnSendGoodsListDetail(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "type", required = false, defaultValue = "1") int type,
        @RequestParam(value = "fromId", required = false, defaultValue = "0") int fromId, @RequestParam(value = "sellerType", required = false, defaultValue = "0") int sellerType,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,
        @RequestParam(value = "operType", required = false, defaultValue = "2") String operType)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        if (type == 1 || fromId != 0)
        {
            if (fromId != 0)
            {
                sellerId = fromId;
            }
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if ("1".equals(operType))
            {
                para.put("payTimeBegin", DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
                para.put("payTimeEnd", DateTime.now().withHourOfDay(15).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
            }
            else if ("2".equals(operType))
            {
                para.put("payTimeEnd", DateTime.now().minusHours(24).toString("yyyy-MM-dd HH:mm:ss"));
            }
            if (sellerType != 0)
            {
                para.put("sellerType", sellerType);
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            result = orderService.findAllUnSendGoodsDetail(para);
        }
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/exportUnSendGoodsListDetail")
    @ControllerLog(description = "订单管理-导出未发货订单明细统计")
    public void exportUnSendGoodsListDetail(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "exportType", required = false, defaultValue = "result") String exportType,
        @RequestParam(value = "sellerType", required = false, defaultValue = "0") int sellerType,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, @RequestParam(value = "operType", required = false, defaultValue = "2") int operType)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("session", request.getSession());
        if (operType == 1) // 当天15小时
        {
            para.put("payTimeBegin", DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
            para.put("payTimeEnd", DateTime.now().withHourOfDay(15).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
            
            para.put("startTimeBegin", DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
            para.put("startTimeEnd", DateTime.now().withHourOfDay(15).withMinuteOfHour(0).withSecondOfMinute(0).toString("yyyy-MM-dd HH:mm:ss"));
            
        }
        else if (operType == 2) // 导出超24小时
        {
            para.remove("payTimeBegin");
            para.remove("startTimeBegin");
            para.put("payTimeEnd", DateTime.now().minusHours(24).toString("yyyy-MM-dd HH:mm:ss"));
            para.put("startTimeEnd", DateTime.now().plusDays(-1).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if (sellerType != 0)
        {
            para.put("sellerType", sellerType);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        
        if ("result".equals(exportType))
        {// 导出结果
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "超24小时未发货明细";
            OutputStream fOut = null;
            HSSFWorkbook workbook = null;
            try
            {
                Map<String, Object> result = orderService.findAllUnSendGoodsDetail(para);
                List<Map<String, Object>> reList = (List<Map<String, Object>>)result.get("rows");
                // 进行转码，使其支持中文文件名
                codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
                // 产生工作簿对象
                workbook = new HSSFWorkbook();
                // 产生工作表对象
                HSSFSheet sheet = workbook.createSheet();
                String[] str = {"下单时间", "付款时间", "订单来源", "订单编号", "订单状态", "订单总价", "收货人", "收货手机", "商家", "发货地"};
                Row row = sheet.createRow(0);
                for (int i = 0; i < str.length; i++)
                {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(str[i]);
                }
                for (int i = 0; i < reList.size(); i++)
                {
                    Map<String, Object> currMap = reList.get(i);
                    Row r = sheet.createRow(i + 1);
                    r.createCell(0).setCellValue(currMap.get("createTimeStr") + "");
                    r.createCell(1).setCellValue(currMap.get("payTimeStr") + "");
                    r.createCell(2).setCellValue(currMap.get("orderChannel") + "");
                    r.createCell(3).setCellValue(currMap.get("number") + "");
                    r.createCell(4).setCellValue(currMap.get("statusStr") + "");
                    r.createCell(5).setCellValue(currMap.get("totalPrice") + "");
                    r.createCell(6).setCellValue(currMap.get("fullName") + "");
                    r.createCell(7).setCellValue(currMap.get("mobileNumber") + "");
                    r.createCell(8).setCellValue(currMap.get("sellerName") + "");
                    r.createCell(9).setCellValue(currMap.get("sendAddress") + "");
                }
                response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
                fOut = response.getOutputStream();
                workbook.write(fOut);
                fOut.flush();
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                response.setHeader("content-disposition", "");
                response.setContentType("text/html");
                if (fOut == null)
                {
                    fOut = response.getOutputStream();
                }
                String errorStr = "<script>alert('系统出错');window.history.back();</script>";
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
        else
        {// 导出商家发货表
            FileInputStream downFile = null;
            OutputStream servletOutPutStream = null;
            try
            {
                para.put("orderStatus", OrderEnum.ORDER_STATUS.REVIEW.getCode());
                String dowName = "超24小时商家发货订单";
                String result = orderService.exportForSeller(para);
                String zipName = result + ".zip";
                ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
                zca.compressExe(result);
                downFile = new FileInputStream(zipName);
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setContentType("application/x-msdownload;charset=utf-8");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
                servletOutPutStream = response.getOutputStream();
                // 设置缓冲区为1024个字节，即1KB
                byte bytes[] = new byte[1024];
                int len = 0;
                // 读取数据。返回值为读入缓冲区的字节总数,如果到达文件末尾，则返回-1
                while ((len = downFile.read(bytes)) != -1)
                {
                    // 将指定 byte数组中从下标 0 开始的 len个字节写入此文件输出流,(即读了多少就写入多少)
                    servletOutPutStream.write(bytes, 0, len);
                }
                FileUtil.deleteFile(result);
                FileUtil.deleteFile(zipName);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                response.setHeader("content-disposition", "");
                response.setContentType("text/html");
                if (servletOutPutStream == null)
                {
                    servletOutPutStream = response.getOutputStream();
                }
                String errorStr = "<script>alert('系统出错');window.history.back();</script>";
                servletOutPutStream.write(errorStr.getBytes());
                servletOutPutStream.flush();
            }
            finally
            {
                if (servletOutPutStream != null)
                {
                    servletOutPutStream.close();
                }
                if (downFile != null)
                {
                    downFile.close();
                }
            }
        }
    }
    
    /**
     * 订单来源 渠道 list
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/otherSourceList")
    public ModelAndView sourceList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("order/sourceList");
        return mv;
    }
    
    /**
     * 异步 获取 订单来源 渠道 信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/otherSourceInfo")
    @ResponseBody
    public String otherSourceInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "mark", required = false, defaultValue = "") String mark, // url后缀
        @RequestParam(value = "name", required = false, defaultValue = "") String name, // 渠道
        @RequestParam(value = "responsibilityPerson", required = false, defaultValue = "") String responsibilityPerson// 负责人
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
        if (!"".equals(mark))
        {
            para.put("mark", mark);
        }
        if (!"".equals(name))
        {
            para.put("name", name);
        }
        if (!"".equals(responsibilityPerson))
        {
            para.put("responsibilityPerson", responsibilityPerson);
        }
        Map<String, Object> map = orderService.findAllOtherSource(para);
        return JSON.toJSONString(map);
    }
    
    @RequestMapping(value = "/searchInfo")
    @ResponseBody
    public String searchInfo(HttpServletRequest request, @RequestParam(value = "type", required = true) int type// 订单来源搜索条件信息类型
    )
        throws Exception
    {
        List<Map<String, Object>> list = orderService.searchOtherSourceNeedsInfo(type);
        return JSON.toJSONString(list);
    }
    
    /**
     * 异步保存渠道
     * 
     * @param request
     * @param name
     * @param responsibilityPerson
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveChannel")
    @ResponseBody
    @ControllerLog(description = "订单管理-新增/编辑物流渠道")
    public String saveChannel(HttpServletRequest request, @RequestParam(value = "name", required = true) String name,
        @RequestParam(value = "responsibilityPerson", required = true) String responsibilityPerson, @RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        Map<String, Object> result = orderService.saveOrUpdateChannel(id > 0 ? id : null, name, responsibilityPerson);
        return JSON.toJSONString(result);
    }
    
    /**
     * 异步删除渠道
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteChannel")
    @ResponseBody
    @ControllerLog(description = "订单管理-删除物流渠道")
    public String deleteChannel(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> result = orderService.deleteOrderChannel(id);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping("/fakeOrderList")
    public ModelAndView fakeOrderList()
    {
        ModelAndView mv = new ModelAndView("order/fakeOrderList");
        return mv;
    }
    
    /**
     * 查询假单列表
     * 
     * @param page
     * @param rows
     * @param orderNumber
     * @param channelType
     * @param channelNumber
     * @param sellerId
     * @param sellerType
     * @param orderCount
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fakeOrderJsonInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getFakeOrderJsonInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
        @RequestParam(value = "channelType", required = false, defaultValue = "") String channelType,
        @RequestParam(value = "channelNumber", required = false, defaultValue = "") String channelNumber,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,
        @RequestParam(value = "sellerType", required = false, defaultValue = "0") int sellerType,
        @RequestParam(value = "orderCount", required = false, defaultValue = "0") int orderCount, @RequestParam(value = "days", required = false, defaultValue = "0") int days)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(orderNumber))
        {
            para.put("orderNumber", orderNumber);
        }
        if (!"".equals(channelType))
        {
            para.put("channelType", channelType);
        }
        if (!"".equals(channelNumber))
        {
            para.put("channelNumber", channelNumber);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (sellerType != 0)
        {
            if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG.getCode())
            {
                para.put("sellerType", SellerEnum.SellerTypeEnum.HONG_KONG.getCode());
                /*
                 * para.put("isNeedIdcardNumber", 1); para.put("isNeedIdcardImage", 0);
                 */
            }
            // 发货类型为：香港（身份证照片）暂时不用
            /*
             * else if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()) { para.put("sellerType",
             * SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()); para.put("isNeedIdcardNumber", 1);
             * para.put("isNeedIdcardImage", 1); }
             */
            else
            {
                para.put("sellerType", sellerType);
            }
        }
        if (orderCount != 0)
        {
            para.put("orderCount", orderCount);
        }
        if (days != 0)
        {
            para.put("sendDate", DateTime.now().minusDays(days).toString("yyyy-MM-dd"));
        }
        Map<String, Object> result = orderService.getFakeOrderJsonInfo(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 假单重新发起订阅
     * 
     * @param id
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderLogisticsAgain", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-假单重新发起订阅")
    public String orderLogisticsAgain(@RequestParam(value = "id", required = false, defaultValue = "-1") int id,
        @RequestParam(value = "ids", required = false, defaultValue = "") String ids)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            List<Integer> idList = new ArrayList<Integer>();
            if (id != -1)
            {
                para.put("id", id);
            }
            if (!"".equals(ids))
            {
                if (ids.indexOf(",") > 0)
                {
                    String[] arr = ids.split(",");
                    for (String cur : arr)
                    {
                        idList.add(Integer.valueOf(cur));
                    }
                }
                else
                {
                    idList.add(Integer.valueOf(ids));
                }
                para.put("idList", idList);
            }
            int resultStatus = orderService.orderLogisticsAgain(para);
            if (resultStatus > 0)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "重新订阅失败");
            }
        }
        catch (Exception e)
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "重新订阅失败");
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 订单结算状态管理
     * 
     * @return
     */
    @RequestMapping("/settlementList")
    public ModelAndView orderSettlementList()
    {
        ModelAndView mv = new ModelAndView("/order/settlementList");
        return mv;
    }
    
    /**
     * 下载导单模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "订单结算状态导单模板";
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
            String[] str = {"订单编号"};
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
    
    /**
     * 确认导单文件
     * 
     * @param request
     * @param file
     * @param type
     * @param settlementDate
     * @param confirm
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importOrderSettlemet", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-确认导单文件")
    public String importOrderSettlemet(HttpServletRequest request, @RequestParam("orderFile") MultipartFile file,
        @RequestParam(value = "type", required = false, defaultValue = "0") int type, // 1：模拟导入结果；2：确认将订单设为已结算；3：确认将订单设为未结算；4：无需结算；5：取消无需结算
        @RequestParam(value = "sendDate", required = false, defaultValue = "") String settlementDate, //
        @RequestParam(value = "isRight", required = false, defaultValue = "0") int confirm)
        throws Exception
    {
        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("status", 1);
            Map<String, Object> sheetData = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            
            if (sheetData == null || (int)sheetData.get("rowNum") == 0)
            {
                map.put("status", 0);
                map.put("msg", "文件为空请确认！");
                return JSON.toJSONString(map);
            }
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)sheetData.get("data");
            
            HttpSession session = request.getSession();
            List<Map<String, Object>> resultList = new ArrayList<>();
            /** excel数据是否正确，该状态要传回客户端 */
            boolean isRight = true;
            int successNum = 0;
            int failNum = 0;
            
            for (Map<String, Object> it : dataList)
            {
                String orderNumber = it.get("cell0") + "";
                Map<String, Object> para = new HashMap<>();
                para.put("resultList", resultList);
                para.put("orderNumber", orderNumber);
                para.put("settlementDate", settlementDate);
                if (type == 1)
                {// 模拟导入
                    boolean resultBoolean = orderService.importTest(para);
                    if (!resultBoolean && isRight)
                    {// 模拟导入
                        isRight = false;
                    }
                    if (resultBoolean)
                    {
                        successNum++;
                    }
                    else
                    {
                        failNum++;
                    }
                }
                else if (type == 2)
                {
                    // 确认将订单设为已结算
                    para.put("isSettlement", 1);
                    orderService.updateOrderSettlement(para);
                }
                else if (type == 3)
                {
                    // 确认将订单设为未结算
                    para.put("isSettlement", 0);
                    // 未结算时间设为0
                    para.put("settlementDate", "0000-00-00 00:00:00");
                    orderService.updateOrderSettlement(para);
                }
                else if (type == 4)
                {
                    // 无需结算
                    para.put("updateIsNeedSettlement", 1);
                    para.put("isNeedSettlement", 0);
                    orderService.updateOrderSettlement(para);
                }
                else if (type == 5)
                {
                    // 取消 无需结算
                    para.put("updateIsNeedSettlement", 1);
                    para.put("isNeedSettlement", 1);
                    orderService.updateOrderSettlement(para);
                }
            }
            
            session.setAttribute("testOrderSettlementList", resultList);
            if (type == 1)
            {
                int canDelete = 1;
                int settlement_no = 0;
                int canCancelIsNeedSettlement = 0;
                int isNeedSettlement_no = 0;
                for (Map<String, Object> map2 : resultList)
                {
                    // 订单号不存在
                    if ("1".equals(map2.get("code") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                    // 订单未结算
                    else if ("2".equals(map2.get("code") + ""))
                    {
                        settlement_no++;
                    }
                    // 未知错误
                    else if ("4".equals(map2.get("code") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                    // 未知错误
                    else if ("5".equals(map2.get("code") + ""))
                    {
                        isNeedSettlement_no++;
                    }
                }
                if (resultList.size() > 0)
                {
                    // 当导入的订单全部为已结算时，才可以将订单设为未结算
                    if (resultList.size() == settlement_no)
                    {
                        canDelete = 1;
                    }
                    else
                    {
                        canDelete = 0;
                    }
                    if (isNeedSettlement_no == resultList.size())
                    {
                        canCancelIsNeedSettlement = 1;
                    }
                }
                session.setAttribute("testOrderSettlementListTime", System.currentTimeMillis());
                map.put("okNum", successNum);
                map.put("failNum", failNum);
                map.put("isRight", isRight ? 1 : 0);
                map.put("canDelete", canDelete);
                map.put("canCancelIsNeedSettlement", canCancelIsNeedSettlement);
            }
            else if (type == 2)
            {
                map.put("status", 11);
                map.put("msg", "操作成功");
            }
            else if (type == 3)
            {
                map.put("status", 12);
                map.put("msg", "操作成功");
            }
            
            return JSON.toJSONString(map);
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<>();
            map.put("status", 0);
            map.put("msg", "操作失败！");
            return JSON.toJSONString(map);
        }
    }
    
    /**
     * 导出模拟结果
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadReceiveResult")
    public void downloadReceiveResult(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "导入结果";
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
            String[] str = {"导入状态", "说明", "订单号"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            HttpSession session = request.getSession();
            Object iList = session.getAttribute("testOrderSettlementList");
            if (iList != null)
            {
                List<Map<String, Object>> rightList = (List<Map<String, Object>>)iList;
                for (int i = 0; i < rightList.size(); i++)
                {
                    Map<String, Object> currMap = rightList.get(i);
                    Row r = sheet.createRow(i + 1);
                    r.createCell(0).setCellValue(currMap.get("status") + "");
                    r.createCell(1).setCellValue(currMap.get("msg") + "");
                    r.createCell(2).setCellValue(currMap.get("orderNumber") + "");
                }
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
    
    /**
     * 异步获得模拟订单导入结果 -- 结果列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderSettlementResult", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String orderSettlementResult(HttpServletRequest request, @RequestParam(value = "delete", required = false, defaultValue = "1") int delete)
        throws Exception
    {
        List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
        HttpSession session = request.getSession();
        Object iList = session.getAttribute("testOrderSettlementList");
        Object timeLong = session.getAttribute("testOrderSettlementListTime");
        if (iList != null && timeLong != null)
        {
            long rightTime = (Long)timeLong;
            // 30000ms内有效
            if (System.currentTimeMillis() - rightTime > 30000)
            {
                session.removeAttribute("testOrderSettlementList");
                session.removeAttribute("testOrderSettlementListTime");
                log.debug("超时，从session移除模拟订单导入结果");
                return JSON.toJSONString(rList);
            }
            if (delete == 1)
            {// 来自页面刷新
                session.removeAttribute("testOrderSettlementList");
                session.removeAttribute("testOrderSettlementListTime");
                return JSON.toJSONString(rList);
            }
            List<Map<String, Object>> rightList = (List<Map<String, Object>>)iList;
            return JSON.toJSONString(rightList);
        }
        return JSON.toJSONString(rList);
    }
    
    /**
     * 获得订单信息 用于创建手动订单
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getOrderInfo(HttpServletRequest request, @RequestParam(value = "number", required = true) long number)
        throws Exception
    {
        Map<String, Object> result = orderService.getOrderRefundInfo(number);
        return JSON.toJSONString(result);
    }
    
    /**
     * 计算订单商品退款 对应相应的积分优惠券金额等信息
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/calOrderRefundInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String calOrderRefundInfo(HttpServletRequest request, //
        @RequestParam(value = "number", required = true) long number, // 订单编号
        @RequestParam(value = "selectProductCount", required = true) int selectProductCount, // 订单商品数量
        @RequestParam(value = "orderProductId", required = true) int orderProductId // 订单商品ID
    )
        throws Exception
    {
        Map<String, Object> result = orderService.calOrderRefundInfoByOrderNumber(number, selectProductCount, orderProductId, 1);
        return JSON.toJSONString(result);
    }
    
    /**
     * 订单冻结列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderFreezeList")
    public ModelAndView orderFreezeList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("order/orderFreezeList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonOrderFreezeInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderFreezeInfo(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "status", required = false, defaultValue = "-1") byte status)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (status != -1)
        {
            para.put("status", status);
        }
        
        Map<String, Object> result = orderService.findAllOrderFreeze(para);
        
        return JSON.toJSONString(result);
    }
    
    @RequestMapping("/exportOrderFreezeList")
    @ControllerLog(description = "订单管理-导出冻结订单")
    public void exportOrderFreezeList(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "status", required = false, defaultValue = "-1") byte status)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (status != -1)
        {
            para.put("status", status);
        }
        Map<String, Object> info = orderService.findAllOrderFreeze(para);
        List<Map<String, Object>> result = (List<Map<String, Object>>)info.get("rows");
        
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "冻结订单列表";
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
            String[] str = {"冻结状态", "付款时间", "冻结时间", "解冻时间", "订单号", "订单状态", "订单总价", "实付金额", "商家", "发货地"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            int cellIndex = 0;
            for (int i = 0; i < result.size(); i++)
            {
                Map<String, Object> currMap = result.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(cellIndex++).setCellValue(currMap.get("freezeStatusStr") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("payTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("freezeTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("unfreezeTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("number") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("orderStatusStr") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("totalPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("realPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sellerName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sendAddress") + "");
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
            response.setHeader("content-disposition", "");
            if (fOut == null)
                fOut = response.getOutputStream();
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
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
     * 订单发货导出
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/sellerSendGoods")
    public ModelAndView sellerSendGoods(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("order/exportSellerSendGoods");
        // 加入最近一个时间段 1，当前时间在15点前：显示前一天15点到今天10点。2，当前时间在15点后，显示今天10点到15点
        int hour = DateTime.now().getHourOfDay();
        String startTimeBegin = "";
        String startTimeEnd = "";
        if (hour < 15)
        {
            startTimeBegin = DateTime.now().plusDays(-1).withTimeAtStartOfDay().plusHours(15).toString("yyyy-MM-dd HH:mm:ss");
            startTimeEnd = DateTime.now().withTimeAtStartOfDay().plusHours(10).toString("yyyy-MM-dd HH:mm:ss");
        }
        else
        {
            startTimeBegin = DateTime.now().withTimeAtStartOfDay().plusHours(10).toString("yyyy-MM-dd HH:mm:ss");
            startTimeEnd = DateTime.now().withTimeAtStartOfDay().plusHours(15).toString("yyyy-MM-dd HH:mm:ss");
        }
        mv.addObject("startTimeBegin", startTimeBegin);
        mv.addObject("startTimeEnd", startTimeEnd);
        return mv;
    }
    
    /**
     * 导出商家发货表
     * 
     * @param request
     * @param response
     * @param timeType
     * @param startTime1
     * @param endTime1
     * @param startTime2
     * @param endTime2
     * @throws Exception
     */
    @RequestMapping(value = "/exportSellerSendGoods")
    @ControllerLog(description = "订单管理-导出商家发货表")
    public void exportSellerSendGoods(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session,
        @RequestParam(value = "timeType", required = false, defaultValue = "0") int timeType, // 订单时间类型，1：最近一个时间段，2：自定义时间
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        // 商家发货表，只导待发货的，其他状态都不要
        para.put("orderStatus", OrderEnum.ORDER_STATUS.REVIEW.getCode());
        if (timeType == 1)
        {// 最近一个时间段
            if (!"".equals(startTime1))
            {
                para.put("startTimeBegin", startTime1);
            }
            if (!"".equals(endTime1))
            {
                para.put("startTimeEnd", endTime1);
            }
        }
        else if (timeType == 2)
        {// 自定义时间
            if (!"".equals(startTime2))
            {
                para.put("startTimeBegin", startTime2);
            }
            if (!"".equals(endTime2))
            {
                para.put("startTimeEnd", endTime2);
            }
        }
        // 导出解冻订单信息
        para.put("exportFreezeOrder", 1);
        para.put("isFreeze", 0);// 未冻结
        OutputStream servletOutPutStream = null;
        try
        {
            String dowName = "商家发货订单";
            para.put("session", session);
            para.put("from", 1);
            String result = orderService.exportForSeller(para);
            String zipName = result + ".zip";
            ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
            zca.compressExe(result);
            FileInputStream downFile = new FileInputStream(zipName);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
            servletOutPutStream = response.getOutputStream();
            // 设置缓冲区为1024个字节，即1KB
            byte bytes[] = new byte[1024];
            int len = 0;
            // 读取数据。返回值为读入缓冲区的字节总数,如果到达文件末尾，则返回-1
            while ((len = downFile.read(bytes)) != -1)
            {
                // 将指定 byte数组中从下标 0 开始的 len个字节写入此文件输出流,(即读了多少就写入多少)
                servletOutPutStream.write(bytes, 0, len);
            }
            servletOutPutStream.close();
            downFile.close();
            // FileUtil.deleteFile(result);
            // FileUtil.deleteFile(zipName);
        }
        catch (Exception e)
        {
            String errorStr = "<script>alert('数据异常');window.history.back();</script>";
            if (e.getMessage().indexOf("系统找不到指定的文件") > -1)
            {
                errorStr = "<script>alert('无导出数据');window.history.back();</script>";
            }
            else
            {
                log.error(e.getMessage(), e);
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Content-disposition", "");
            if (servletOutPutStream == null)
            {
                servletOutPutStream = response.getOutputStream();
            }
            servletOutPutStream.write(errorStr.getBytes());
            servletOutPutStream.close();
            return;
        }
        
    }
    
    /**
     * 每日订单发货明细
     * 
     * @return
     */
    @RequestMapping("/orderSendTimeAnalyzeDetail")
    public ModelAndView orderSendTimeAnalyzeDetail(//
        @RequestParam(value = "payTime", required = true) String payTime, //
        @RequestParam(value = "beginHour", required = false, defaultValue = "") String beginHour, //
        @RequestParam(value = "endHour", required = false, defaultValue = "") String endHour)
    {
        ModelAndView mv = new ModelAndView("order/orderSendTimeAnalyzeDetail");
        if (StringUtils.isEmpty(payTime))
        {
            mv.setViewName("error/404");
        }
        mv.addObject("payTime", payTime);
        mv.addObject("beginHour", beginHour);
        mv.addObject("endHour", endHour);
        return mv;
    }
    
    /**
     * 异步加载每日订单发货明细
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonOrderSendTimeAnalyzeDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderSendTimeAnalyzeDetail(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "payTime", required = true) String payTime, //
        @RequestParam(value = "beginHour", required = false, defaultValue = "") String beginHour, //
        @RequestParam(value = "endHour", required = false, defaultValue = "") String endHour)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            para.put("payTimeStart", payTime + " 00:00:00");
            para.put("payTimeEnd", payTime + " 23:59:59");
            para.put("beginHour", beginHour);
            para.put("endHour", endHour);
            para.put("source", 1);
            resultMap = orderService.orderSendTimeAnalyzeDetail(para);
        }
        catch (Exception e)
        {
            log.error("异步加载每日订单发货时效：payTime=" + payTime + ",beginHour=" + beginHour + ",endHour=" + endHour + ",出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/exportOrderSendTimeAnalyzeDetail")
    @ControllerLog(description = "订单管理-导出订单发货时效统计分析")
    public void exportOrderSendTimeAnalyzeDetail(HttpServletRequest request, //
        HttpServletResponse response, @RequestParam(value = "payTime", required = true) String payTime, //
        @RequestParam(value = "beginHour", required = true) String beginHour, //
        @RequestParam(value = "endHour", required = true) String endHour)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("payTimeStart", payTime + " 00:00:00");
            para.put("payTimeEnd", payTime + " 23:59:59");
            para.put("beginHour", beginHour);
            para.put("endHour", endHour);
            para.put("source", 1);
            Map<String, Object> resultMap = orderService.orderSendTimeAnalyzeDetail(para);
            
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultMap.get("rows");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("每日订单发货时效统计");
            String[] str = {"下单时间", "用户ID", "付款时间", "付款方式", "发货时间", "订单来源", "订单编号", "订单状态", "商家备注", "客服备注", "订单总价", "实付金额", "收货人", "收货手机", "商家", "发货地", "是否导出", "物流公司", "运单号"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < resultList.size(); i++)
            {
                int cellIndex = 0;
                Map<String, Object> currMap = resultList.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(cellIndex++).setCellValue(currMap.get("oCreateTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("accountId") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oSendTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("orderChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("number") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oStatusDescripton") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark2") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raFullName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raMobileNumber") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSellerName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSendAddress") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("operaStatus") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologNumber") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = payTime + "每日订单发货时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error("【每日订单发货时效统计】导出日期" + payTime + "出错了！！！", e);
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
    
    @RequestMapping("/sellerSendTimeAnalyzeDetail")
    public ModelAndView sellerSendTimeAnalyzeDetail(//
        @RequestParam(value = "sellerId", required = true) String sellerId, //
        @RequestParam(value = "payTime", required = true) String payTime, //
        @RequestParam(value = "beginHour", required = false, defaultValue = "") String beginHour, //
        @RequestParam(value = "endHour", required = false, defaultValue = "") String endHour)
    {
        ModelAndView mv = new ModelAndView("order/sellerSendTimeAnalyzeDetail");
        if (StringUtils.isEmpty(payTime))
        {
            mv.setViewName("error/404");
        }
        mv.addObject("payTime", payTime);
        mv.addObject("beginHour", beginHour);
        mv.addObject("endHour", endHour);
        mv.addObject("sellerId", sellerId);
        return mv;
    }
    
    /**
     * 异步加载商家发货时效明细
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonSellerSendTimeAnalyzeDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerSendTimeAnalyzeDetail(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "sellerId", required = false) String sellerId, //
        @RequestParam(value = "payTime", required = true) String payTime, //
        @RequestParam(value = "beginHour", required = false, defaultValue = "") String beginHour, //
        @RequestParam(value = "endHour", required = false, defaultValue = "") String endHour)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            DateTime begin = null;
            if (!"".equals(payTime))
            {
                begin = new DateTime(CommonUtil.string2Date(payTime + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sellerId", sellerId);
            para.put("beginHour", beginHour);
            para.put("endHour", endHour);
            para.put("payTimeStart", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("source", 2);
            resultMap = orderService.orderSendTimeAnalyzeDetail(para);
        }
        catch (Exception e)
        {
            log.error("异步加载商家发货时效明细：payTime=" + payTime + ",beginHour=" + beginHour + ",endHour=" + endHour + ",出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping("/exportSellerSendTimeAnalyzeDetail")
    @ControllerLog(description = "订单管理-导出商家发货时效统计分析")
    public void exportSellerSendTimeAnalyzeDetail(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "payTime", required = true) String payTime, //
        @RequestParam(value = "beginHour", required = false, defaultValue = "") String beginHour, //
        @RequestParam(value = "endHour", required = false, defaultValue = "") String endHour, //
        @RequestParam(value = "sellerId", required = true) String sellerId)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            if (!"".equals(payTime))
            {
                begin = new DateTime(CommonUtil.string2Date(payTime + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }
            else
            {
                begin = DateTime.now();
            }
            String payTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String payTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("payTimeStart", payTimeBegin);
            para.put("payTimeEnd", payTimeEnd);
            para.put("beginHour", beginHour);
            para.put("endHour", endHour);
            para.put("sellerId", sellerId);
            para.put("source", 2);
            Map<String, Object> resultMap = orderService.orderSendTimeAnalyzeDetail(para);
            
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultMap.get("rows");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商家发货时效统计明细");
            String[] str = {"下单时间", "用户ID", "付款时间", "付款方式", "发货时间", "订单来源", "订单编号", "订单状态", "商家备注", "客服备注", "订单总价", "实付金额", "收货人", "收货手机", "商家", "发货地", "是否导出", "物流公司", "运单号"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < resultList.size(); i++)
            {
                int cellIndex = 0;
                Map<String, Object> currMap = resultList.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(cellIndex++).setCellValue(currMap.get("oCreateTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("accountId") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oSendTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("orderChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("number") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oStatusDescripton") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark2") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raFullName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raMobileNumber") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSellerName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSendAddress") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("operaStatus") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologNumber") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = payTime + "商家发货时效统计明细";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error("【商家发货时效统计明细】导出日期" + payTime + "出错了！！！", e);
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
    
    @RequestMapping("/orderLogisticAnalyzeDetail")
    public ModelAndView orderLogisticAnalyzeDetail(//
        @RequestParam(value = "sendTime", required = true) String sendTime, //
        @RequestParam(value = "hour", required = true) String hour)
    {
        ModelAndView mv = new ModelAndView("order/orderLogisticAnalyzeDetail");
        if (StringUtils.isEmpty(sendTime))
        {
            mv.setViewName("error/404");
        }
        mv.addObject("sendTime", sendTime);
        mv.addObject("hour", hour);
        return mv;
    }
    
    /**
     * 异步加载订单发货后有物流信息时效统计
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonOrderLogisticAnalyzeDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderLogisticAnalyzeDetail(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "sendTime", required = true) String sendTime, //
        @RequestParam(value = "hour", required = true) String hour)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            DateTime begin = new DateTime(CommonUtil.string2Date(sendTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            String sendTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String sendTimeEnd = begin.toString("yyyy-MM-dd 23:59:59");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("hour", hour);
            para.put("source", 1);
            resultMap = orderService.orderLogisticAnalyzeDetail(para);
        }
        catch (Exception e)
        {
            log.error("异步加载订单发货后有物流信息时效统计：sendTime=" + sendTime + ",hour=" + hour + ",出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 导出订单发货后有物流信息时效统计
     * 
     * @param request
     * @param response
     * @param sendTime
     * @param hour
     */
    @RequestMapping("/exportOrderLogisticAnalyzeDetail")
    @ControllerLog(description = "订单管理-导出订单发货后有物流信息时效统计分析")
    public void exportOrderLogisticAnalyzeDetail(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "sendTime", required = true) String sendTime, //
        @RequestParam(value = "hour", required = true) String hour)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = new DateTime(CommonUtil.string2Date(sendTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            String sendTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String sendTimeEnd = begin.toString("yyyy-MM-dd 23:59:59");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("hour", hour);
            para.put("source", 1);
            Map<String, Object> resultMap = orderService.orderLogisticAnalyzeDetail(para);
            
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultMap.get("rows");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("订单发货后有物流信息时效统计");
            String[] str = {"下单时间", "用户ID", "付款时间", "付款方式", "发货时间", "订单来源", "订单编号", "订单状态", "商家备注", "客服备注", "订单总价", "实付金额", "收货人", "收货手机", "商家", "发货地", "是否导出", "物流公司", "运单号"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < resultList.size(); i++)
            {
                int cellIndex = 0;
                Map<String, Object> currMap = resultList.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(cellIndex++).setCellValue(currMap.get("oCreateTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("accountId") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oSendTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("orderChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("number") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oStatusDescripton") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark2") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raFullName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raMobileNumber") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSellerName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSendAddress") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("operaStatus") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologNumber") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = sendTime + "订单发货后有物流信息时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error("【订单发货后有物流信息时效统计】导出日期" + sendTime + "出错了！！！", e);
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
    
    @RequestMapping("/sellerLogisticAnalyzeDetail")
    public ModelAndView sellerLogisticAnalyzeDetail(//
        @RequestParam(value = "sellerId", required = true) String sellerId, //
        @RequestParam(value = "sendTime", required = true) String sendTime, //
        @RequestParam(value = "hour", required = true) String hour)
    {
        ModelAndView mv = new ModelAndView("order/sellerLogisticAnalyzeDetail");
        if (StringUtils.isEmpty(sendTime))
        {
            mv.setViewName("error/404");
        }
        mv.addObject("sendTime", sendTime);
        mv.addObject("hour", hour);
        mv.addObject("sellerId", sellerId);
        return mv;
    }
    
    /**
     * 异步加载商家发货后有物流信息时效统计
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/jsonSellerLogisticAnalyzeDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerLogisticAnalyzeDetail(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "sendTime", required = true) String sendTime, //
        @RequestParam(value = "sellerId", required = true) String sellerId, //
        @RequestParam(value = "hour", required = true) String hour)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            DateTime begin = new DateTime(CommonUtil.string2Date(sendTime + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            String sendTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String sendTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("hour", hour);
            para.put("source", 2);
            para.put("sellerId", sellerId);
            resultMap = orderService.orderLogisticAnalyzeDetail(para);
        }
        catch (Exception e)
        {
            log.error("异步加载商家发货后有物流信息时效统计：sendTime=" + sendTime + ",hour=" + hour + ",出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 导出商家发货后有物流信息时效统计
     * 
     * @param request
     * @param response
     * @param sendTime
     * @param hour
     */
    @RequestMapping("/exportSellerLogisticAnalyzeDetail")
    @ControllerLog(description = "订单管理-导出商家发货后有物流信息时效统计分析")
    public void exportSellerLogisticAnalyzeDetail(HttpServletRequest request, HttpServletResponse response, //
        @RequestParam(value = "sendTime", required = true) String sendTime, //
        @RequestParam(value = "sellerId", required = true) String sellerId, //
        @RequestParam(value = "hour", required = true) String hour)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = new DateTime(CommonUtil.string2Date(sendTime + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            String sendTimeBegin = begin.toString("yyyy-MM-01 00:00:00");
            String sendTimeEnd = begin.plusMonths(1).toString("yyyy-MM-01 00:00:00");
            para.put("sendTimeBegin", sendTimeBegin);
            para.put("sendTimeEnd", sendTimeEnd);
            para.put("hour", hour);
            para.put("source", 2);
            para.put("sellerId", sellerId);
            Map<String, Object> resultMap = orderService.orderLogisticAnalyzeDetail(para);
            
            List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultMap.get("rows");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("商家发货后有物流信息时效统计");
            String[] str = {"下单时间", "用户ID", "付款时间", "付款方式", "发货时间", "订单来源", "订单编号", "订单状态", "商家备注", "客服备注", "订单总价", "实付金额", "收货人", "收货手机", "商家", "发货地", "是否导出", "物流公司", "运单号"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < resultList.size(); i++)
            {
                int cellIndex = 0;
                Map<String, Object> currMap = resultList.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(cellIndex++).setCellValue(currMap.get("oCreateTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("accountId") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oPayChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oSendTime") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("orderChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("number") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oStatusDescripton") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("remark2") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("oRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raFullName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("raMobileNumber") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSellerName") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("sSendAddress") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("operaStatus") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologChannel") + "");
                r.createCell(cellIndex++).setCellValue(currMap.get("ologNumber") + "");
            }
            
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = sendTime + "商家发货后有物流信息时效统计";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            
        }
        catch (Exception e)
        {
            log.error("【商家发货后有物流信息时效统计】导出日期" + sendTime + "出错了！！！", e);
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
    
    @RequestMapping("/problemOrderList")
    public ModelAndView problemOrderList()
    {
        ModelAndView mv = new ModelAndView("order/problemOrderList");
        return mv;
    }
    
    /**
     * 查询假单列表
     * 
     * @param page
     * @param rows
     * @param orderNumber
     * @param channelType
     * @param channelNumber
     * @param sellerId
     * @param sellerType
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonProblemOrderListInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProblemOrderListInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber,
        @RequestParam(value = "channelType", required = false, defaultValue = "") String channelType,
        @RequestParam(value = "channelNumber", required = false, defaultValue = "") String channelNumber,
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,
        @RequestParam(value = "sellerType", required = false, defaultValue = "0") int sellerType)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(orderNumber))
        {
            para.put("orderNumber", orderNumber);
        }
        if (!"".equals(channelType))
        {
            para.put("channelType", channelType);
        }
        if (!"".equals(channelNumber))
        {
            para.put("channelNumber", channelNumber);
        }
        if (sellerId != -1)
        {
            para.put("sellerId", sellerId);
        }
        if (sellerType != 0)
        {
            if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG.getCode())
            {
                para.put("sellerType", SellerEnum.SellerTypeEnum.HONG_KONG.getCode());
                /*
                 * para.put("isNeedIdcardNumber", 1); para.put("isNeedIdcardImage", 0);
                 */
            }
            // 发货类型为：香港（身份证照片）暂时不用
            /*
             * else if (sellerType == SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()) { para.put("sellerType",
             * SellerEnum.SellerTypeEnum.HONG_KONG_1.getCode()); para.put("isNeedIdcardNumber", 1);
             * para.put("isNeedIdcardImage", 1); }
             */
            else
            {
                para.put("sellerType", sellerType);
            }
        }
        Map<String, Object> result = orderService.findAllProblemOrderList(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 订单锁定列表
     * @return
     */
    @RequestMapping("/orderLockedList")
    public ModelAndView orderLockedList()
    {
        ModelAndView mv = new ModelAndView("order/orderLockedList");
        return mv;
    }
    
    /**
     * 异步获取订单锁定 信息
     * @param page
     * @param rows
     * @param orderNumber
     * @param orderStatus
     * @param checkStatus
     * @param accountName
     * @param accountId
     * @param receiveName
     * @param reveivePhone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonOrderLockedInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderLockedInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "2") int orderStatus, // 订单状态
        @RequestParam(value = "checkStatus", required = false, defaultValue = "1") int checkStatus, // 锁定状态 。1：待审核；2：审核通过；3：审核不通过
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName, // 用户名
        @RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId, // 用户ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone // 收货人手机号
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        
        long sqlType = 1;
        if (orderNumber != 0)
        {
            para.put("orderNumber", orderNumber);
        }
        if (orderStatus != 0)
        {
            para.put("orderStatus", orderStatus);
        }
        if (checkStatus != 0)
        {
            para.put("checkStatus", checkStatus);
            if (checkStatus == 2 || checkStatus == 3)
            {
                // 审核通过 & 审核不通过
                para.put("lockedOrder", 1);
            }
        }
        if (accountId != 0)
        {
            para.put("accountId", accountId);
        }
        // account 5
        if (!"".equals(accountName))
        {
            para.put("accountName", "%" + accountName + "%");
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
        para.put("datasource", "master");
        para.put("checkOrder", "1");
        Map<String, Object> result = orderService.ajaxOrderInfo(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 低价订单审核
     * @param id
     * @param checkStatus
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "订单管理-低价订单审核")
    public String checkOrder(@RequestParam(value = "id", required = false, defaultValue = "0") int id, // 订单id
        @RequestParam(value = "checkStatus", required = false, defaultValue = "0") int checkStatus, // 审核结果
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
        throws Exception
    {
        try
        {
            return JSON.toJSONString(orderService.saveCheckOrder(id, checkStatus, remark));
        }
        catch (Exception e)
        {
            log.error("低价订单审核失败！", e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "订单审核失败");
            return JSON.toJSONString(result);
        }
    }
    
}
