package com.ygg.admin.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ygg.admin.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.KdCompanyEnum;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.entity.SellerExpandEntity;
import com.ygg.admin.entity.SellerMasterAndSlaveEntity;
import com.ygg.admin.service.LogisticsTimeoutService;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.service.SellerAdminOrderService;
import com.ygg.admin.service.SellerExpandService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.service.TimeoutOrderService;

/**
 * 商家后台订单相关控制器
 * 
 * @author Administrator
 *         
 */
@Controller
@RequestMapping(value = "/sellerAdminOrder")
public class SellerAdminOrderController
{
    
    Logger log = Logger.getLogger(SellerAdminOrderController.class);
    
    @Resource(name = "orderService")
    private OrderService orderService = null;
    
    @Resource(name = "sellerExpandService")
    private SellerExpandService sellerExpandService;
    
    @Resource(name = "sellerService")
    private SellerService sellerService;
    
    @Resource
    private SellerAdminOrderService sellerAdminOrderService;
    
    @Resource
    private SystemLogService logService;
    
    @Resource
    private TimeoutOrderService timeoutOrderService;
    
    @Resource
    private LogisticsTimeoutService logisticsTimeoutService;
    
    /**
     * 特定商家订单列表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
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
        mv.setViewName("sellerAdmin/listOrder");
        mv.addObject("sellerId", se.getId() + "");
        mv.addObject("orderIdList", orderIdList);
        return mv;
    }
    
    /**
     * 导出未结算订单
     */
    @RequestMapping(value = "/exportUnSettlementOrder")
    @ControllerLog(description = "商家-订单管理-导出未结算订单")
    public void exportUnSettlementOrder(
        HttpServletResponse response,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "timeType", required = false, defaultValue = "0") int timeType, // 订单时间类型，1：最近一个时间段，2：自定义时间
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2, //
        @RequestParam(value = "isSettlement", required = false, defaultValue = "0") int isSettlement // 是否结算： 0:未结算
    )
        throws Exception
    {
        OutputStream fOut = null;
        String errorMessage = "";
        try
        {
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
            if (isSettlement != -1)
            {
                // 默认值
                para.put("isSettlement", isSettlement);
            }
            // 需要关联product表 3
            if (!"".equals(productName))
            {
                
                para.put("productName", "%" + productName + "%");
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
            
            // 所属商家
            List<Integer> sellerIdList = new ArrayList<>();
            sellerIdList.add(sellerId);
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (!slaveList.isEmpty())
            {
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
            }
            para.put("sellerIdList", sellerIdList);
            
            para.put("sqlType", sqlType);
            para.put("datasource", "slave");
            
            // 导出订单明细
            String dowName = "商家未结算订单明细";
            // 1 判断总条数
            if (orderService.getExportOrderNums(para) > CommonConstant.workbook_num_3w)
            {
                errorMessage = "数据量大于" + CommonConstant.workbook_num_3w + "，请缩小范围！";
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
        catch (Exception e)
        {
            if (errorMessage.equals(""))
            {
                log.error(e.getMessage(), e);
                errorMessage = "导出出错";
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
        }
    }
    
    /**
     * 获取 特定商家订单列表 数据
     *
     */
    @RequestMapping(value = "/jsonOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderInfo(
            HttpServletRequest request,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品ID
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 商品编码
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "sellerId", required = true) int sellerId, // 主商家ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "timeType", required = false, defaultValue = "0") int timeType, // 订单时间类型，1：最近一个时间段，2：自定义时间
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList,
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout,
        @RequestParam(value = "orderType", required = false, defaultValue = "0") int orderType, // 0:全部，1：左岸城堡，2：左岸城堡，4：左岸城堡

        @RequestParam(value = "logisticsNumber", required = false, defaultValue = "") String logisticsNumber)
        throws Exception
    {
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null || (sellerId != se.getId()))
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

        List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(se.getId());
        if (slaveList.isEmpty())
        {
            para.put("sellerId", se.getId());
        }
        else
        {
            List<Integer> sellerIdList = new ArrayList<>();
            sellerIdList.add(se.getId());
            for (SellerMasterAndSlaveEntity entity : slaveList)
            {
                sellerIdList.add(entity.getSellerSlaveId());
            }
            para.put("sellerIdList", sellerIdList);
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
        if (orderType > 0){
            para.put("orderType", orderType);
        }
        para.put("checkStatus", 2);
        if (!"".equals(logisticsNumber))
        {
            para.put("logisticsNumber", logisticsNumber);
        }
        Map<String, Object> result = orderService.ajaxOrderInfo(para);
        List<Map<String, Object>> r = (List<Map<String, Object>>)result.get("rows");
        for (Map<String, Object> map : r)
        {
            map.remove("isSettlement");
            map.remove("oRealPrice");
            map.remove("oTotalPrice");
            map.remove("orderChannel");
            map.remove("sSellerName");
            map.remove("sSendAddress");
            map.remove("oPayChannel");
        }
        return JSON.toJSONString(result);
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
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") int id,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        List<Integer> sellerIds = new ArrayList<>();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        Map<String, Object> result = orderService.findOrderDetailInfo(id, 2);
        if (result.get("id") == null)
        {
            mv.setViewName("error/404");
            return mv;
        }

        //只能查看属于商家自己的订单
        sellerIds.add(se.getId());
        List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(se.getId());
        for (SellerMasterAndSlaveEntity entity : slaveList)
        {
            sellerIds.add(entity.getSellerSlaveId());
        }
        Integer sellerId = Integer.valueOf(result.get("sellerId").toString());
        if (!sellerIds.contains(sellerId))
        {
            mv.setViewName("error/404");
            return mv;
        }

        mv.addObject("sellerId", se.getId());
        mv.addObject("detail", result);
        mv.setViewName("sellerAdmin/orderDetail");
        return mv;
    }
    
    /**
     * 订单发货管理
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderSendGoods")
    public ModelAndView orderSendGoods(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.addObject("sellerId", se.getId() + "");
        mv.setViewName("sellerAdmin/sendGoods");
        return mv;
    }
    
    /**
     * 异步得到订单商品具体信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String orderProductInfo(@RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        String jsonStr = orderService.orderProductJsonStr(id);
        return jsonStr;
    }
    
    /**
     * 得到物流公司编码以json格式返回
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonCompanyCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonCompanyCode()
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
    @ControllerLog(description = "商家-订单管理-订单发货")
    public String sendOrder(HttpServletRequest request, @RequestParam(value = "orderId", required = true) int orderId, // 订单ID
        @RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "sendType", required = false, defaultValue = "1") int sendType, // 是否有物流
                                                                                              // 1：有，0：没有
        @RequestParam(value = "channel", required = false, defaultValue = "") String channel, // 物流渠道
        @RequestParam(value = "number", required = false, defaultValue = "") String number, // 物流单号
        @RequestParam(value = "money", required = false, defaultValue = "0.0") float money// 物流运费
    )
        throws Exception
    {
        try
        {
            SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
            if (se == null)
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 3);
                result.put("msg", "发货失败-登陆链接已失效");
                return JSON.toJSONString(result);
            }
            number = number.trim();
            if ((sendType == 1) && ("".equals(channel) || "".equals(number) || !StringUtils.isOnlyLettersAndNumber(number)))
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 3);
                result.put("msg", "请检查物流渠道和物流单号是否正确");
                return JSON.toJSONString(result);
            }
            
            // 所属商家
            List<Integer> sellerIdList = new ArrayList<>();
            sellerIdList.add(sellerId);
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (!slaveList.isEmpty())
            {
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
            }
            
            OrderEntity oe = orderService.findOrderById(orderId);
            if (oe == null || !sellerIdList.contains(oe.getSellerId()))
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("status", 3);
                result.put("msg", "发货失败-不允许的对该订单进行发货");
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
            Map<String, Object> result = new HashMap<>();
            result.put("status", resultStaus);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "发货失败");
            return JSON.toJSONString(result);
        }
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
    @ControllerLog(description = "商家-订单管理-批量发货")
    public String batchSendOrder(HttpServletRequest request, @RequestParam("orderFile") MultipartFile file, @RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "importType", required = false, defaultValue = "0") int importType)
        throws Exception
    {
        try
        {
            HttpSession session = request.getSession();
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> dataMap = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            List<Map<String, Object>> rowList = (List<Map<String, Object>>)dataMap.get("data");
            
            map.put("status", 1);
            
            List<Map<String, Object>> iList = new ArrayList<Map<String, Object>>();
            
            /** excel数据是否正确，该状态要传回客户端 */
            boolean isRight = true;
            int okNum = 0;
            int failNum = 0;
            for (Map<String, Object> info : rowList)
            {
                String orderNumber = info.get("cell0") == null ? "" : info.get("cell0") + "";
                String channel = info.get("cell1") == null ? "" : info.get("cell1") + "";
                String number = info.get("cell2") == null ? "" : info.get("cell2") + "";
                orderNumber = orderNumber.trim();
                channel = channel.trim();
                number = number.trim();
                if ("".equals(orderNumber))
                {
                    continue;
                }
                
                // 所属商家
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
                if (!slaveList.isEmpty())
                {
                    for (SellerMasterAndSlaveEntity entity : slaveList)
                    {
                        sellerIdList.add(entity.getSellerSlaveId());
                    }
                }
                
                Map<String, Object> para = new HashMap<>();
                para.put("sellerIdList", sellerIdList);
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
                para.put("sendTime", "");
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
        catch (Exception e)
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
        List<Map<String, Object>> rList = new ArrayList<>();
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
     * 导出订单发货结果
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportSendResult")
    @ControllerLog(description = "商家-订单管理-导出订单发货结果")
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
            codedFileName = URLEncoder.encode(codedFileName, "UTF-8");
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
     * 导出订单发货模板
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportPostageTemplate")
    public void exportPostageTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "支持物流公司";
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"物流公司"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            
            int index = 1;
            for (KdCompanyEnum e : KdCompanyEnum.values())
            {
                Row r = sheet.createRow(index++);
                r.createCell(0).setCellValue(e.getCompanyName());
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
    
    @RequestMapping(value = "/exportSendTemplate")
    @ControllerLog(description = "商家-订单管理-导出订单发货模板")
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
            codedFileName = URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"订单编号", "物流公司", "运单编号", " ", "物流公司请填写以下标准名称，否则会导致发货失败"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            
            String[] tips =
                {"顺丰速运", "EMS", "圆通速递", "申通快递", "汇通快运", "中通速递", "韵达快运", "天天快递", "宅急送", "优速物流", "全峰快递", "京东快递", "e邮宝", "如风达", "九曳供应链", "笨鸟海淘", "DHL", "德邦物流", "天地华宇", "国通快递",
                    "联邦快递", "快捷速递", "行必达","中外速运","中澳速递","泛捷国际速递", "若所发快递不在此列，请咨询左岸城堡运营", " ", "运单编号不能包含空格或符号，否则会上传失败"};
            for (int i = 1; i <= tips.length; i++)
            {
                sheet.createRow(i).createCell(4).setCellValue(tips[i - 1]);
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
     * 商家导出发货订单
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/sellerSendGoods")
    public ModelAndView sellerSendGoods(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.setViewName("sellerAdmin/exportSellerSendGoods");
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
        mv.addObject("sellerId", se.getId() + "");
        return mv;
    }
    
    @RequestMapping(value = "/exportSellerSendGoods")
    @ControllerLog(description = "商家-订单管理-导出商家发货表")
    public void exportSellerSendGoods(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session,
        @RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
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
        
        // 所属商家
        List<Integer> sellerIdList = new ArrayList<>();
        sellerIdList.add(sellerId);
        List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
        if (!slaveList.isEmpty())
        {
            for (SellerMasterAndSlaveEntity entity : slaveList)
            {
                sellerIdList.add(entity.getSellerSlaveId());
            }
        }
        
        para.put("sellerIdList", sellerIdList);
        OutputStream servletOutPutStream = null;
        try
        {
            String dowName = "待发货订单";
            para.put("session", session);
            para.put("from", 2);
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
             FileUtil.deleteFile(result);
             FileUtil.deleteFile(zipName);
        }
        catch (Exception e)
        {
            String errorStr = "<script>alert('无数据');window.history.back();</script>";
            if (e instanceof FileNotFoundException)
            {
                errorStr = "<script>alert('无订单');window.history.back();</script>";
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
     * 发货时效汇总列表
     * @param request
     * @param response
     * @param sellerinfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendTimeoutSummaryList")
    public ModelAndView sendTimeoutSummaryList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        
        mv.setViewName("sellerAdmin/sendTimeoutSummaryList");
        mv.addObject("sellerId", se.getId() + "");
        mv.addObject("sellerName", se.getRealSellerName());
        mv.addObject("sendTimeType", SellerEnum.SellerSendTimeTypeEnum.getDescByCode(se.getSendTimeType()));
        mv.addObject("weekendSenType", SellerEnum.WeekendSendTypeEnum.getDescByCode(se.getIsSendWeekend()));
        mv.addObject("selectDate", DateTime.now().toString("yyyy-MM"));
        return mv;
    }
    
    @RequestMapping(value = "/jsonSendTimeoutSummaryInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object jsonSendTimeoutSummaryInfo(@RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        try
        {
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            // 所属商家
            List<Integer> sellerIdList = new ArrayList<>();
            sellerIdList.add(sellerId);
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (!slaveList.isEmpty())
            {
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
            }
            return sellerAdminOrderService.findSendTimeoutOrderInfo(sellerIdList, selectDate);
        }
        catch (Exception e)
        {
            log.error("异步加载商家超时订单出错", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 订单售后问题列表
     * @param request
     * @param response
     * @param sellerinfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/orderQuestionList")
    public ModelAndView orderQuestionList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        
        mv.setViewName("sellerAdmin/orderQuestionList");
        mv.addObject("sellerId", se.getId() + "");
        return mv;
    }
    
    @RequestMapping(value = "/jsonQuestionListInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonQuestionListInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page,//
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,//
        @RequestParam(value = "orderNo", required = false, defaultValue = "") String orderNo,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "templateId", required = false, defaultValue = "-1") int templateId,//
        @RequestParam(value = "questionDesc", required = false, defaultValue = "") String questionDesc)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (sellerId <= 0)
        {
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(orderNo))
            {
                para.put("orderNo", orderNo);
            }
            if (templateId != -1)
            {
                para.put("templateId", templateId);
            }
            if (!"".equals(questionDesc))
            {
                para.put("questionDesc", "%" + questionDesc + "%");
            }
            
            List<Integer> sellerIdList = new ArrayList<>();
            sellerIdList.add(sellerId);
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (!slaveList.isEmpty())
            {
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
            }
            para.put("sellerIdList", sellerIdList);
            resultMap = sellerAdminOrderService.jsonSellerQuestionListInfo(para);
        }
        catch (Exception e)
        {
            log.error("异步加载订单问题模版列表出错了", e);
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
        }
        return JSON.toJSONString(resultMap);
    }
    
    /**
     * 订单售后问题明细
     * @param id
     * @return
     */
    @RequestMapping("/orderQuestionDetail/{questionId}")
    public ModelAndView orderQuestionDetail(@PathVariable("questionId") int id)
    {
        ModelAndView mv = new ModelAndView("sellerAdmin/orderQuestionDetail");
        try
        {
            Map<String, Object> detail = sellerAdminOrderService.findOrderQuestionDetailInfo(id);
            if (detail == null || detail.size() == 0)
            {
                mv.setViewName("error/404");
                return mv;
            }
            mv.addObject("detail", detail);
        }
        catch (Exception e)
        {
            log.error("查看问题questionId=" + id + "出错了", e);
            mv.setViewName("error/404");
        }
        return mv;
    }
    
    /**
     * 订单问题反馈
     * @param request
     * @param questionId
     * @param content
     * @return
     */
    @RequestMapping(value = "/sellerOrderQuestionFeedback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家-订单问题管理-订单问题反馈")
    public String sellerOrderQuestionFeedback(HttpServletRequest request, @RequestParam(value = "questionId", required = false, defaultValue = "0") int questionId,
        @RequestParam(value = "content", required = false, defaultValue = "") String content)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
            if (se == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "登陆链接已失效,请重新登录");
                return resultMap.toString();
            }
            return sellerAdminOrderService.updateSellerOrderQuestion(questionId, content, se.getRealSellerName());
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "保存出错");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 订单发货超时申诉
     * @param orderId：订单Id
     * @param reason：申诉理由
     * @return
     */
    @RequestMapping(value = "/sendTimeOutComplain", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家-订单管理-订单发货超时申诉")
    public String sendTimeOutComplain(HttpServletRequest request, @RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,
        @RequestParam(value = "reason", required = false, defaultValue = "") String reason)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
            if (se == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "登陆链接已失效,请重新登录");
                return resultMap.toString();
            }
            if (orderId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的订单");
                return JSON.toJSONString(resultMap);
            }
            if (reason.isEmpty())
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入申诉理由");
                return JSON.toJSONString(resultMap);
            }
            return timeoutOrderService.sendTimeOutComplain(orderId, reason);
        }
        catch (Exception e)
        {
            log.error(String.format("商家-订单管理-订单发货超时申诉出错，订单=%d", orderId), e);
            
            resultMap.put("status", 0);
            resultMap.put("msg", "申诉失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 物流更新时效汇总列表
     * @param request
     * @param response
     * @param sellerinfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logisticsTimeoutSummaryList")
    public ModelAndView logisticsTimeoutSummaryList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        
        mv.setViewName("sellerAdmin/logisticsTimeoutSummaryList");
        mv.addObject("sellerId", se.getId() + "");
        mv.addObject("sellerName", se.getRealSellerName());
        mv.addObject("sellerType", SellerEnum.SellerTypeEnum.getDescByCode(se.getSellerType()));
        mv.addObject("bonedType", se.getBondedNumberType() == 1 ? "先有物流单号后报关" : "先报关后有物流单号");
        mv.addObject("selectDate", DateTime.now().toString("yyyy-MM"));
        return mv;
    }
    
    @RequestMapping(value = "/jsonLogisticsTimeoutSummaryInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object jsonLogisticsTimeoutSummaryInfo(@RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate)
    {
        try
        {
            if ("".equals(selectDate))
            {
                selectDate = DateTime.now().toString("yyyy-MM");
            }
            List<Integer> sellerIdList = new ArrayList<>();
            sellerIdList.add(sellerId);
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (!slaveList.isEmpty())
            {
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
            }
            return sellerAdminOrderService.findLogisticsTimeoutOrderInfo(sellerIdList, selectDate);
        }
        catch (Exception e)
        {
            log.error("异步加载商家超时订单出错", e);
            return new ArrayList<>();
        }
    }
    
    @RequestMapping(value = "/logisticsTimeOutComplain", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "商家-订单管理-订单物流超时申诉")
    public String logisticsTimeOutComplain(HttpServletRequest request, @RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId,
        @RequestParam(value = "reason", required = false, defaultValue = "") String reason)
    {
        Map<String, Object> resultMap = new HashMap<>();
        try
        {
            SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
            if (se == null)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "登陆链接已失效,请重新登录");
                return resultMap.toString();
            }
            if (orderId == 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请选择要操作的订单");
                return JSON.toJSONString(resultMap);
            }
            if (reason.isEmpty())
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "请输入申诉理由");
                return JSON.toJSONString(resultMap);
            }
            return logisticsTimeoutService.logisticsTimeOutComplain(orderId, reason);
        }
        catch (Exception e)
        {
            log.error(String.format("商家-订单管理-订单物流超时申诉出错，订单=%d", orderId), e);
            
            resultMap.put("status", 0);
            resultMap.put("msg", "申诉失败");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 物流更新超时订单明细列表
     * @param request
     * @param response
     * @param sellerinfo
     * @param orderIdList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logisticsTimeoutOrderList")
    public ModelAndView logisticsTimeoutOrderList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList)
        throws Exception
    {
        if (request.getSession() == null)
        {
            
        }
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.setViewName("sellerAdmin/logisticsTimeoutOrderList");
        mv.addObject("sellerId", se.getId() + "");
        mv.addObject("orderIdList", orderIdList);
        return mv;
    }
    
    @RequestMapping(value = "/jsonLogisticsTimeoutOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLogisticsTimeoutOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品ID
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 商品编码
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "sellerId", required = true) int sellerId, // 主商家ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "payTimeBegin", required = false, defaultValue = "") String payTimeBegin,//付款开始时间
        @RequestParam(value = "payTimeEnd", required = false, defaultValue = "") String payTimeEnd,//付款结束时间
        @RequestParam(value = "sendTimeBegin", required = false, defaultValue = "") String sendTimeBegin,//发货开始时间
        @RequestParam(value = "sendTimeEnd", required = false, defaultValue = "") String sendTimeEnd,//发货结束时间
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList,//订单Id
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout,//是否超时
        @RequestParam(value = "logisticsNumber", required = false, defaultValue = "") String logisticsNumber)
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
        
        long sqlType = 1;
        // 纯order表 1
        if (!"".equals(payTimeBegin))
        {
            para.put("payTimeBegin", payTimeBegin);
        }
        if (!"".equals(payTimeEnd))
        {
            para.put("payTimeEnd", payTimeEnd);
        }
        if (!"".equals(sendTimeBegin))
        {
            para.put("sendTimeBegin", sendTimeBegin);
        }
        if (!"".equals(sendTimeEnd))
        {
            para.put("sendTimeEnd", sendTimeEnd);
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
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (slaveList.isEmpty())
            {
                para.put("sellerId", sellerId);
            }
            else
            {
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
                para.put("sellerIdList", sellerIdList);
            }
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
        if (!"".equals(logisticsNumber))
        {
            para.put("logisticsNumber", logisticsNumber);
        }
        para.put("source", 1);
        Map<String, Object> result = logisticsTimeoutService.jsonLogisticsOrders(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 发货时效订单明细列表
     * @param request
     * @param response
     * @param sellerinfo
     * @param orderIdList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendTimeoutOrderList")
    public ModelAndView sendTimeoutOrderList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo,
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.setViewName("sellerAdmin/sendTimeoutOrderList");
        mv.addObject("sellerId", se.getId() + "");
        mv.addObject("orderIdList", orderIdList);
        return mv;
    }
    
    @RequestMapping(value = "/jsonSendTimeoutOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSendTimeoutOrderInfo(
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus, // 订单状态
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId, // 商品ID
        @RequestParam(value = "code", required = false, defaultValue = "") String code, // 商品编码
        @RequestParam(value = "productName", required = false, defaultValue = "") String productName, // 商品名称
        @RequestParam(value = "sellerId", required = true) int sellerId, // 主商家ID
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName, // 收货人姓名
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone, // 收货人手机号
        @RequestParam(value = "payTimeBegin", required = false, defaultValue = "") String payTimeBegin,//付款开始时间
        @RequestParam(value = "payTimeEnd", required = false, defaultValue = "") String payTimeEnd,//付款结束时间
        @RequestParam(value = "sendTimeBegin", required = false, defaultValue = "") String sendTimeBegin,//发货开始时间
        @RequestParam(value = "sendTimeEnd", required = false, defaultValue = "") String sendTimeEnd,//发货结束时间
        @RequestParam(value = "orderIdList", required = false, defaultValue = "") String orderIdList,
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout,
        @RequestParam(value = "logisticsNumber", required = false, defaultValue = "") String logisticsNumber)
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
        
        long sqlType = 1;
        // 纯order表 1
        if (!"".equals(payTimeBegin))
        {
            para.put("payTimeBegin", payTimeBegin);
        }
        if (!"".equals(payTimeEnd))
        {
            para.put("payTimeEnd", payTimeEnd);
        }
        if (!"".equals(sendTimeBegin))
        {
            para.put("sendTimeBegin", sendTimeBegin);
        }
        if (!"".equals(sendTimeEnd))
        {
            para.put("sendTimeEnd", sendTimeEnd);
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
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (slaveList.isEmpty())
            {
                para.put("sellerId", sellerId);
            }
            else
            {
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
                para.put("sellerIdList", sellerIdList);
            }
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
        if (!"".equals(logisticsNumber))
        {
            para.put("logisticsNumber", logisticsNumber);
        }
        para.put("checkStatus", OrderEnum.ORDER_CHECK_STATUS.CHECK_PASS.getCode()); // 查询通过审核的订单
        Map<String, Object> result = orderService.ajaxOrderInfo(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 发货时效申诉列表
     * @param request
     * @param response
     * @param sellerinfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendTimeoutComplainList")
    public ModelAndView sendTimeoutComplainList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.setViewName("sellerAdmin/sendTimeoutComplainList");
        mv.addObject("sellerId", se.getId() + "");
        return mv;
    }
    
    @RequestMapping(value = "/jsonSendTimeoutComplainOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSendTimeoutComplainOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "sellerId", required = true) int sellerId, // 主商家ID
        @RequestParam(value = "complainStatus", required = false, defaultValue = "-1") int complainStatus,//申诉状态
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        
        if (sellerId != -1)
        {
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (slaveList.isEmpty())
            {
                para.put("sellerId", sellerId);
            }
            else
            {
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
                para.put("sellerIdList", sellerIdList);
            }
        }
        if (orderNumber != 0)
        {
            para.put("orderNumber", orderNumber);
        }
        if (isTimeout != -1)
        {
            para.put("isTimeout", isTimeout);
        }
        if (complainStatus != -1)
        {
            para.put("complainStatus", complainStatus);
        }
        Map<String, Object> result = sellerAdminOrderService.findSendTimeoutComplainOrder(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 物流时效申诉列表
     * @param request
     * @param response
     * @param sellerinfo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/logisticsTimeoutComplainList")
    public ModelAndView logisticsTimeoutComplainList(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.setViewName("sellerAdmin/logisticsTimeoutComplainList");
        mv.addObject("sellerId", se.getId() + "");
        return mv;
    }
    
    @RequestMapping(value = "/jsonLogisticsTimeoutComplainOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonLogisticsTimeoutComplainOrderInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber, // 订单编号
        @RequestParam(value = "sellerId", required = true) int sellerId, // 主商家ID
        @RequestParam(value = "isTimeout", required = false, defaultValue = "-1") int isTimeout,//是否超时
        @RequestParam(value = "complainStatus", required = false, defaultValue = "-1") int complainStatus)
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
        
        if (orderNumber != 0)
        {
            para.put("orderNumber", orderNumber);
        }
        if (sellerId != -1)
        {
            List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
            if (slaveList.isEmpty())
            {
                para.put("sellerId", sellerId);
            }
            else
            {
                List<Integer> sellerIdList = new ArrayList<>();
                sellerIdList.add(sellerId);
                for (SellerMasterAndSlaveEntity entity : slaveList)
                {
                    sellerIdList.add(entity.getSellerSlaveId());
                }
                para.put("sellerIdList", sellerIdList);
            }
        }
        
        if (isTimeout != -1)
        {
            para.put("isTimeout", isTimeout);
        }
        para.put("orderDesc", 1);
        List<Integer> complainStatusList = new ArrayList<>();
        complainStatusList.add(OrderEnum.LogisticsTimeoutComplainResultEnum.PROCESSING.getCode());
        complainStatusList.add(OrderEnum.LogisticsTimeoutComplainResultEnum.SUCCESS.getCode());
        complainStatusList.add(OrderEnum.LogisticsTimeoutComplainResultEnum.FAILED.getCode());
        para.put("complainStatusList", complainStatusList);
        Map<String, Object> result = logisticsTimeoutService.jsonLogisticsOrders(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 商家导出结算订单
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/sellerUnSettleOrders")
    public ModelAndView sellerUnSettleOrders(HttpServletRequest request, HttpServletResponse response,
        @CookieValue(value = "sellerinfo", required = false, defaultValue = "") String sellerinfo)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        SellerEntity se = SessionUtil.getCurrentSellerAdminUser(request.getSession());
        if (se == null)
        {
            if ("".equals(sellerinfo))
            {
                mv.addObject("errorMsg", "登陆链接已失效");
                mv.setViewName("sellerAdmin/error");
                return mv;
            }
            else
            {
                String[] arr = sellerinfo.split("_");
                String sellerId = arr[0];
                String sign = arr[1];
                SellerExpandEntity sellerExpand = sellerExpandService.findSellerExpandBysid(Integer.valueOf(sellerId));
                if (sellerExpand == null)
                {
                    log.warn("商家信息不存在");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                se = sellerService.findSellerById(Integer.valueOf(sellerId));
                String originStr = sellerId + sellerExpand.getPassword();
                String signStr = SignUtil.md5Uppercase(originStr);
                if (se == null || !signStr.equals(sign))
                {
                    log.warn("签名错误");
                    mv.addObject("errorMsg", "登陆链接已失效");
                    mv.setViewName("sellerAdmin/error");
                    return mv;
                }
                
                Cookie cookie = new Cookie("sellerinfo", sellerId + "_" + sign);
                cookie.setMaxAge(SessionUtil.SELLER_INFO_COOKIES_KEEP_TIME); // 保留一个月
                cookie.setPath("/");
                response.addCookie(cookie);
                
                SessionUtil.addSellerAdminUserToSession(request.getSession(), se);
            }
        }
        mv.setViewName("sellerAdmin/sellerUnSettleOrders");
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
        mv.addObject("sellerId", se.getId() + "");
        return mv;
    }
    
    @RequestMapping(value = "/exportSellerUnSettleOrders")
    @ControllerLog(description = "商家-订单管理-导出商家结算订单表")
    public void exportSellerUnSettleOrders(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session,
        @RequestParam(value = "sellerId", required = true) int sellerId, // 商家ID
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        String fileName = null,zipName = null;
        para.put("payTimeBegin", startTime);
        para.put("payTimeEnd", endTime);
        // 所属商家
        List<Integer> sellerIdList = new ArrayList<>();
        sellerIdList.add(sellerId);
        List<SellerMasterAndSlaveEntity> slaveList = sellerService.findSellerSlaveListByMasterId(sellerId);
        if (CollectionUtils.isNotEmpty(slaveList))
        {
            for (SellerMasterAndSlaveEntity entity : slaveList)
            {
                sellerIdList.add(entity.getSellerSlaveId());
            }
        }
        
        para.put("sellerIdList", sellerIdList);
        OutputStream servletOutPutStream = null;
        FileInputStream downFile = null;
        try
        {
            String dowName = "结算订单";
            para.put("session", session);
            para.put("from", 2);
            fileName = orderService.exportForSellerUnSettleOrders(para);
            zipName = fileName + ".zip";
            ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
            zca.compressExe(fileName);
            downFile = new FileInputStream(zipName);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
            servletOutPutStream = response.getOutputStream();
            byte bytes[] = new byte[1024];
            int len = 0;
            while ((len = downFile.read(bytes)) != -1)
            {
                servletOutPutStream.write(bytes, 0, len);
            }
        }
        catch (Exception e)
        {
            String errorStr = "<script>alert('无数据');window.history.back();</script>";
            if (e instanceof FileNotFoundException)
            {
                errorStr = "<script>alert('无订单');window.history.back();</script>";
            }else
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
//            servletOutPutStream.write(errorStr.getBytes());
          servletOutPutStream.write(errorStr.getBytes("utf-8"));
            return;
        }finally{
            if(servletOutPutStream!=null){
                servletOutPutStream.close();
            }
            if(downFile!=null){
                downFile.close();
            }
            FileUtil.deleteFile(fileName);
            FileUtil.deleteFile(zipName);
        }
        
    }
}
