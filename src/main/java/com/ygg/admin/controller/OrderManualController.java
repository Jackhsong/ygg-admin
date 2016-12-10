package com.ygg.admin.controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.OrderManualEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.AreaService;
import com.ygg.admin.service.BirdexService;
import com.ygg.admin.service.OrderManualService;
import com.ygg.admin.service.ProductService;
import com.ygg.admin.service.RefundService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.FileUtil;
import com.ygg.admin.util.StringUtils;
import com.ygg.admin.util.ZipCompressorByAnt;

/**
 * 手动建单相关控制器
 * 
 * @author zhangyb
 *
 */
@Controller
@RequestMapping(value = "/orderManual")
public class OrderManualController
{
    
    Logger log = Logger.getLogger(OrderManualController.class);
    
    @Resource
    private AreaService areaService;
    
    @Resource
    private OrderManualService orderManualService;
    
    @Resource
    private RefundService refundService;
    
    @Resource
    private BirdexService birdexService;
    
    @Resource
    private ProductService productService;
    
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("orderManual/list");
        return mv;
    }
    
    /**
     * 异步获取手动订单信息
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/manualJson")
    @ResponseBody
    public String manualJson(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber,// 订单编号
        @RequestParam(value = "status", required = false, defaultValue = "0") int status,// 状态
        @RequestParam(value = "fullName", required = false, defaultValue = "") String fullName,// 收货人姓名
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,
        @RequestParam(value = "sendType", required = false, defaultValue = "-1") int sendType)
        // 收货人手机号
        throws Exception
    {
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (page == 0)
            {
                page = 1;
            }
            para.put("start", rows * (page - 1));
            para.put("max", rows);
            if (!"".equals(startTime))
            {
                para.put("startTimeBegin", startTime);
            }
            if (!"".equals(endTime))
            {
                para.put("startTimeEnd", endTime);
            }
            if (orderNumber != 0)
            {
                para.put("number", orderNumber);
            }
            if (status != 0)
            {
                para.put("status", status);
            }
            if (!"".equals(fullName))
            {
                para.put("fullName", "%" + fullName + "%");
            }
            if (!"".equals(mobileNumber))
            {
                para.put("mobileNumber", mobileNumber);
            }
            if (sendType != -1)
            {
                para.put("sendType", sendType);
            }
            Map<String, Object> result = orderManualService.findAllOrderManual(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList());
            result.put("total", 0);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping("/addForm")
    public ModelAndView addForm(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("orderManual/add");
        mv.addObject("provinceList", areaService.allProvince());
        //笨鸟商家列表
        List<Integer> bsids = birdexService.findBirdexSellerId();
        String nonsupportIds = StringUtils.listJoin(bsids, ",");
        mv.addObject("nonsupportIds", nonsupportIds);
        return mv;
    }
    
    /**
     * 验证商品是否是sellerID下额商品
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/validateProduct")
    @ResponseBody
    public String validateProduct(HttpServletRequest request, @RequestParam(value = "sellerId", required = true) int sellerId,
        @RequestParam(value = "pIdAndNums", required = true) String pIdAndNums)
        throws Exception
    {
        try
        {
            List<Integer> productIdList = new ArrayList<Integer>();
            String[] infoArr = pIdAndNums.split(";");
            for (String info : infoArr)
            {
                productIdList.add(Integer.valueOf(info.split(",")[0]));
            }
            boolean status = orderManualService.validateProduct(sellerId, productIdList);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            if (!status)
            {
                result.put("status", 0);
                result.put("msg", "所填商品都必须属于所选商家");
            }
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "服务器忙");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 保存手动订单
     * 
     * @param request
     * @param orderManual
     * @param pIdAndNums 123,1;256,2;
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @ResponseBody
    @ControllerLog(description = "手动订单管理-新增手动订单")
    public String save(HttpServletRequest request, OrderManualEntity orderManual, String pIdAndNums)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            // 当为打款发货时检查打款账户是否存在
            if (orderManual.getSendType() == 2)
            {
                List<Map<String, Object>> transferAccountList = refundService.findAllFinancialAffairsCardById(orderManual.getTransferAccount());
                if (transferAccountList == null || transferAccountList.size() == 0)
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "打款账户不存在");
                    return JSON.toJSONString(resultMap);
                }
            }
            if (!CommonUtil.isMobile(orderManual.getMobileNumber()))
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "手机号错误");
                return JSON.toJSONString(resultMap);
            }
            List<Map<String, Object>> pIdAndNumList = new ArrayList<Map<String, Object>>();
            String[] infoArr = pIdAndNums.split(";");
            for (String info : infoArr)
            {
                String[] currArr = info.split(",");
                Map<String, Object> curr = new HashMap<String, Object>();
                curr.put("pid", Integer.valueOf(currArr[0].trim()));
                curr.put("num", Integer.valueOf(currArr[1].trim()));
                pIdAndNumList.add(curr);
            }
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("orderManual", orderManual);
            para.put("pIdAndNumList", pIdAndNumList);
            Map<String, Object> result = orderManualService.saveOrderManual(para);
            
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙");
            return JSON.toJSONString(resultMap);
        }
    }
    
    /**
     * 手动订单发货处理
     * 
     * @param request
     * @param orderId
     * @param channel
     * @param number
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sendOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "手动订单管理-手动订单发货")
    public String sendOrder(HttpServletRequest request, @RequestParam(value = "id", required = true) int orderId,// 订单ID
        @RequestParam(value = "channel", required = true) String channel,// 物流渠道
        @RequestParam(value = "number", required = true) String number)
        throws Exception
    {
        number = number.trim();
        if ("".equals(channel) || "".equals(number))
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 3);
            result.put("msg", "物流渠道和物流单号为必填项");
            return JSON.toJSONString(result);
        }
        Map<String, Object> result = orderManualService.sendOrderManual(orderId, channel, number);
        return JSON.toJSONString(result);
    }
    
    /**
     * 取消手动订单
     * 
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "手动订单管理-取消手动订单")
    public String cancelOrder(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("status", 3);// 客服取消
        Map<String, Object> result = orderManualService.updateOrderManual(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 导出
     * 
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     * @param orderNumber：订单编号
     * @param status：状态
     * @param fullName：收货人姓名
     * @param mobileNumber：收货人手机号
     * @param exportType： seller:导出商家发货表；sellerAllStatus:导出明细
     * @throws Exception
     */
    @RequestMapping(value = "/export")
    @ControllerLog(description = "手动订单管理-导出手动订单")
    public void export(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime,
        @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber,
        @RequestParam(value = "status", required = false, defaultValue = "0") int status, @RequestParam(value = "fullName", required = false, defaultValue = "") String fullName,
        @RequestParam(value = "mobileNumber", required = false, defaultValue = "") String mobileNumber,
        @RequestParam(value = "exportType", required = false, defaultValue = "seller") String exportType,
        @RequestParam(value = "sendType", required = false, defaultValue = "-1") int sendType)
        
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (!"".equals(startTime))
        {
            para.put("startTimeBegin", startTime);
        }
        if (!"".equals(endTime))
        {
            para.put("startTimeEnd", endTime);
        }
        if (orderNumber != 0)
        {
            para.put("number", orderNumber);
        }
        // 商家发货表，只导待发货的，其他状态都不要
        if ("seller".equals(exportType))
        {
            para.put("status", 1);
        }
        else if (status != 0)
        {
            para.put("status", status);
        }
        if ("sellerAllStatus".equals(exportType) && sendType != -1)
        {
            para.put("sendType", sendType);
        }
        if (!"".equals(fullName))
        {
            para.put("fullName", "%" + fullName + "%");
        }
        if (!"".equals(mobileNumber))
        {
            para.put("mobileNumber", mobileNumber);
        }
        
        try
        {
            String dowName = "商家发货订单";
            if ("sellerAllStatus".equals(exportType))
            {
                dowName = "商家订单明细";
            }
            if ("sellerAllStatus".equals(exportType))
            {
                para.put("exportPostage", 1);// 导出运费
            }
            
            String result = orderManualService.getExportForSeller(para);
            if (result == null)
            {
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/html;charset=utf-8");
                PrintWriter ot = response.getWriter();
                ot.println("<script>alert('无数据');window.history.back();</script>");
                ot.close();
                return;
            }
            String zipName = result + ".zip";
            ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
            zca.compressExe(result);
            FileInputStream downFile = new FileInputStream(zipName);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dowName + ".zip", "utf-8"));
            OutputStream servletOutPutStream = response.getOutputStream();
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
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter ot = response.getWriter();
            ot.println("<script>alert('无数据');window.history.back();</script>");
            ot.close();
            log.error(e.getMessage(), e);
        }
        
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
    public ModelAndView orderDetail(HttpServletRequest request, @PathVariable("id") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Map<String, Object> result = orderManualService.findOrderDetailInfo(id);
        if (result.get("id") == null)
        {
            mv.setViewName("error/404");
            return mv;
        }
        mv.addObject("detail", result);
        mv.setViewName("orderManual/detail");
        return mv;
    }
    
    /**
     * 自动创建海外购商品记录
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/overseasManualProduct")
    public ModelAndView overseasManualProduct(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("orderManual/overseasManualProduct");
        return mv;
    }
    
    /**
     * 异步获取 自动创建海外购商品记录
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonOverseasManualProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOverseasManualProduct(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "code", required = false, defaultValue = "") String code,
        @RequestParam(value = "name", required = false, defaultValue = "") String name)
    {
        try
        {
            return orderManualService.jsonOverseasManualProduct(page, rows, code, name);
        }
        catch (Exception e)
        {
            return JSON.toJSONString(ResultEntity.getFailResultList());
        }
    }
    
    /**
     * 新建商品链接
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveOverseasManualProduct")
    @ResponseBody
    @ControllerLog(description = "手动订单管理-新增海外购商品链接")
    public String saveOverseasManualProduct(HttpServletRequest request,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "productId", required = false, defaultValue = "0") int productId,//
        @RequestParam(value = "nums", required = false, defaultValue = "0") int nums,//
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            //检查商品是否属于这个商家。
            List<Integer> productIdList = new ArrayList<Integer>();
            productIdList.add(productId);
            boolean status = orderManualService.validateProduct(sellerId, productIdList);
            if (!status)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "所填商品必须属于所选商家");
                return JSON.toJSONString(resultMap);
            }
            //复制商品，生成新链接
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("nums", nums);
            ProductEntity pe = productService.findProductById(productId);
            int returnProductId = productService.copyProduct(productId, pe.getType(), 2, para);
            if (returnProductId <= 0)
            {
                resultMap.put("status", 0);
                resultMap.put("msg", "创建失败");
                return JSON.toJSONString(resultMap);
            }
            resultMap = orderManualService.addOverseasManualProduct(sellerId, returnProductId, nums, remark);
            return JSON.toJSONString(resultMap);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            resultMap.put("status", 0);
            resultMap.put("msg", "服务器忙");
            return JSON.toJSONString(resultMap);
        }
    }
}
