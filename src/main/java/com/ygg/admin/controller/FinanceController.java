package com.ygg.admin.controller;

import java.io.FileInputStream;
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

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.FinanceSerivce;
import com.ygg.admin.service.SystemLogService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.FileUtil;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.util.StringUtils;
import com.ygg.admin.util.ZipCompressorByAnt;

@Controller
@RequestMapping(value = "/finance")
public class FinanceController
{
    Logger log = Logger.getLogger(FinanceController.class);
    
    // 信号量
    final Semaphore semp = new Semaphore(2);
    
    @Resource
    private FinanceSerivce financeSerivce;
    
    @Resource
    private SystemLogService logService;
    
    /**
     * 订单结算 列表
     * 
     * @return
     */
    @RequestMapping("/orderFinanceList")
    public ModelAndView orderFinanceList()
    {
        return new ModelAndView("finance/orderFinanceList");
    }
    
    /**
     * 异步 获取 订单结算 列表 数据
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonOrderFinanceData", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOrderFinanceData(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, //
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, //
        @RequestParam(value = "settlementStartTime", required = false, defaultValue = "") String settlementStartTime, //
        @RequestParam(value = "settlementEndTime", required = false, defaultValue = "") String settlementEndTime, //
        @RequestParam(value = "refundSettlementStartTime", required = false, defaultValue = "") String refundSettlementStartTime, //
        @RequestParam(value = "refundSettlementEndTime", required = false, defaultValue = "") String refundSettlementEndTime, //
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId, // 商家
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber, // 订单编号
        @RequestParam(value = "sellerIds", required = false, defaultValue = "-1") String sellerIds, // 商家S
        @RequestParam(value = "orderType", required = false, defaultValue = "1,2") String orderTypeStr, // 订单类型 eg: 1,2
                                                                                                        // 。1：正常订单，2：手动订单
        @RequestParam(value = "settlementStatus", required = false, defaultValue = "0,1") String settlementStatusStr // 订单结算状态
                                                                                                                     // eg:1
                                                                                                                     // 1：已结算，0：未结算
    )
        throws Exception
    {
        try
        {
            if (orderTypeStr.equals("0") || settlementStatusStr.equals("-1"))
            {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("rows", new ArrayList());
                result.put("total", 0);
                return JSON.toJSONString(result);
            }
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
            if (!"".equals(settlementStartTime))
            {
                para.put("settlementStartTimeBegin", settlementStartTime);
            }
            if (!"".equals(settlementEndTime))
            {
                para.put("settlementEndTimeEnd", settlementEndTime);
            }
            if (sellerId != -1)
            {
                para.put("sellerId", sellerId);
            }
            int orderType = 0;
            int settlementStatus = -1;
            if (orderTypeStr.indexOf(",") == -1)
            {
                orderType = Integer.parseInt(orderTypeStr);
            }
            if (settlementStatusStr.indexOf(",") == -1)
            {
                settlementStatus = Integer.parseInt(settlementStatusStr);
            }
            if (!"".equals(sellerIds) && sellerIds.indexOf("-1") < 0)
            {
                List<Integer> sellerIdList = new ArrayList<>();
                if (sellerIds.indexOf(",") > -1)
                {
                    String[] arr = sellerIds.split(",");
                    for (String s : arr)
                    {
                        if (StringUtils.isNumeric(s))
                        {
                            sellerIdList.add(Integer.valueOf(s));
                        }
                    }
                }
                else
                {
                    if (StringUtils.isNumeric(sellerIds))
                    {
                        sellerIdList.add(Integer.valueOf(sellerIds));
                    }
                }
                para.put("sellerIdList", sellerIdList);
            }
            if (!"".equals(refundSettlementStartTime))
            {
                para.put("refundSettlementStartTime", refundSettlementStartTime);
                orderType = 1;//只有正常订单才有退款结算时间
                para.put("hasRefundSettlementTime", 1);
            }
            if (!"".equals(refundSettlementEndTime))
            {
                para.put("refundSettlementEndTime", refundSettlementEndTime);
                orderType = 1;//只有正常订单才有退款结算时间
                para.put("hasRefundSettlementTime", 1);
            }
            para.put("orderType", orderType);
            if (settlementStatus != -1)
            {
                para.put("isSettlement", settlementStatus);
            }
            if (!"".equals(orderNumber))
            {
                para.put("orderNumber", orderNumber);
            }
            Map<String, Object> result = financeSerivce.findOrderFinanceData(para);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("异步获取订单结算列表数据失败！！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("rows", new ArrayList());
            result.put("total", 0);
            return JSON.toJSONString(result);
        }
    }
    
    @RequestMapping(value = "/exportOrderFinanceData")
    @ControllerLog(description = "财务结算管理-导出订单结算")
    public void exportOrderFinanceData(
        HttpServletRequest request, //
        HttpServletResponse response,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, //
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, //
        /* @RequestParam(value = "sellerId", required = false, defaultValue = "-1") int sellerId,// 商家 */
        @RequestParam(value = "sellerId", required = false, defaultValue = "-1") String sellerIds, // 商家S
        @RequestParam(value = "orderType", required = false, defaultValue = "1,2") String orderTypeStr, // 订单类型 eg: 1,2
                                                                                                        // 。1：正常订单，2：手动订单
        @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber, // 订单编号
        @RequestParam(value = "settlementStatus", required = false, defaultValue = "0,1") String settlementStatusStr, // 订单结算状态
                                                                                                                      // eg:1
                                                                                                                      // 1：已结算，0：未结算
        @RequestParam(value = "refundSettlementStartTime", required = false, defaultValue = "") String refundSettlementStartTime, //
        @RequestParam(value = "refundSettlementEndTime", required = false, defaultValue = "") String refundSettlementEndTime, //
        @RequestParam(value = "settlementStartTime", required = false, defaultValue = "") String settlementStartTime, //
        @RequestParam(value = "settlementEndTime", required = false, defaultValue = "") String settlementEndTime, //
        @RequestParam(value = "source", required = false, defaultValue = "") String source, // analyze : 来着数据统计
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, @RequestParam(value = "start", required = false, defaultValue = "0") int start,
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop)
        throws Exception
    {
        if (semp.availablePermits() < 1)
        {
            String errorMessage = "已有多人在导出操作，请稍后再导...";
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Content-disposition", "");
            String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
            response.getOutputStream().write(errorStr.getBytes());
            response.getOutputStream().close();
            return;
        }

        OutputStream servletOutPutStream = null;
        String errorMessage = "系统出错或无导出数据";
        try
        {
            // 获取许可
            semp.acquire();
            if (orderTypeStr.equals("0") || settlementStatusStr.equals("-1"))
            {
                throw new RuntimeException();
            }
            Map<String, Object> para = new HashMap<String, Object>();
            if ("analyze".equals(source))
            {
                int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
                DateTime begin = null;
                DateTime end = null;
                String s = "" + start;// 1
                String e = "" + stop;// 1
                if (start < 10)
                {
                    s = "0" + start;
                }
                if (stop < 10)
                {
                    e = "0" + stop;
                }
                else if (stop > maxDay)
                {
                    e = "" + maxDay;
                }
                
                begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
                String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
                String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
                para.put("settlementStartTimeBegin", payTimeBegin);
                para.put("settlementEndTimeEnd", payTimeEnd);
            }
            else
            {
                if (!"".equals(settlementStartTime))
                {
                    para.put("settlementStartTimeBegin", settlementStartTime);
                }
                if (!"".equals(settlementEndTime))
                {
                    para.put("settlementEndTimeEnd", settlementEndTime);
                }
            }
            if (!"".equals(startTime))
            {
                para.put("startTimeBegin", startTime);
            }
            if (!"".equals(endTime))
            {
                para.put("startTimeEnd", endTime);
            }
            
            // if (sellerId != -1)
            // {
            // para.put("sellerId", sellerId);
            // }
            int orderType = 0;
            int settlementStatus = -1;
            if (orderTypeStr.indexOf(",") == -1)
            {
                orderType = Integer.parseInt(orderTypeStr);
            }
            if (settlementStatusStr.indexOf(",") == -1)
            {
                settlementStatus = Integer.parseInt(settlementStatusStr);
            }
            if (!"".equals(sellerIds) && sellerIds.indexOf("-1") < 0)
            {
                List<Integer> sellerIdList = new ArrayList<>();
                if (sellerIds.indexOf(",") > -1)
                {
                    String[] arr = sellerIds.split(",");
                    for (String s : arr)
                    {
                        if (StringUtils.isNumeric(s))
                        {
                            sellerIdList.add(Integer.valueOf(s));
                        }
                    }
                }
                else
                {
                    sellerIdList.add(Integer.valueOf(sellerIds));
                }
                para.put("sellerIdList", sellerIdList);
            }
            if (!"".equals(refundSettlementStartTime))
            {
                para.put("refundSettlementStartTime", refundSettlementStartTime);
                orderType = 1;//只有正常订单才有退款结算时间
                para.put("hasRefundSettlementTime", 1);
            }
            if (!"".equals(refundSettlementEndTime))
            {
                para.put("refundSettlementEndTime", refundSettlementEndTime);
                orderType = 1;//只有正常订单才有退款结算时间
                para.put("hasRefundSettlementTime", 1);
            }
            para.put("orderType", orderType);
            if (settlementStatus != -1)
            {
                para.put("isSettlement", settlementStatus);
            }
            if (!"".equals(orderNumber))
            {
                para.put("orderNumber", orderNumber);
            }

            // 详细日志记录
            Map<String, Object> logInfoMap = new HashMap<>();
            logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.FINACE_MANAGEMENT.ordinal());
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
            // 详细日志记录

            int exTotal = financeSerivce.countAllNum(para);
            if (exTotal == 0)
            {
                errorMessage = "无导出数据";
                throw new RuntimeException();
            }
            if (exTotal > (CommonConstant.workbook_max_nums_1w * 10))
            {
                errorMessage = "数据量超过" + (CommonConstant.workbook_max_nums_1w * 10) + "，请缩小范围！";
                throw new RuntimeException("财务导出数据量过大！");
            }
            String result = financeSerivce.exportOrderFinanceDataDetail(para);
            String zipName = result + ".zip";
            ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
            zca.compressExe(result);
            FileInputStream downFile = new FileInputStream(zipName);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("订单结算.zip", "utf-8"));
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
            log.error(e.getMessage(), e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("Content-disposition", "");
            String errorStr = "<script>alert('" + errorMessage + "');window.history.back();</script>";
            if (servletOutPutStream == null)
            {
                servletOutPutStream = response.getOutputStream();
            }
            servletOutPutStream.write(errorStr.getBytes());
            servletOutPutStream.close();
        }
        finally
        {
            // 访问完后，释放
            semp.release();
        }
    }
    
    /**
     * 运费结算
     * 
     * @return
     */
    @RequestMapping("/postageSettlement")
    public ModelAndView postageSettlement()
    {
        return new ModelAndView("finance/postageSettlement");
    }
    
    /**
     * 模拟or保存 运费结算
     * 
     * @param request
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importPostageSettlement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "财务结算管理-运费结算")
    public String importPostageSettlement(HttpServletRequest request, //
        @RequestParam("importFile") MultipartFile file, //
        @RequestParam(value = "confirmDate", required = true) String confirmDate, // 导入日期
        @RequestParam(value = "type", required = true) int type// 1 : 模拟 ； 2 : 确认 ；3：批量删除
    )
        throws Exception
    {
        try
        {
            int returnStatus = 1;
            String returnMsg = "";
            int successNum = 0;
            int failNum = 0;
            int repetitionNum = 0;
            String dataKey = "importPostageSettlementStatus" + System.currentTimeMillis() + "";
            
            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                if (type == 1)
                {
                    List<String> alreadyImportOrderList = new ArrayList<>();
                    // 模拟导入
                    List<Map<String, Object>> simulationList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        String shouldPayPostage = it.get("cell1") + "";
                        int status = financeSerivce.checkImportPostageSettlementData(number, shouldPayPostage, simulationList, alreadyImportOrderList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, simulationList);
                }
                else if (type == 2)
                {
                    List<String> alreadyImportOrderList = new ArrayList<>();
                    // 确认导入
                    List<Map<String, Object>> confirmList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        if (!alreadyImportOrderList.contains(number))
                        {
                            String shouldPayPostage = it.get("cell1") + "";
                            int status = financeSerivce.savePostageSettlementData(number, shouldPayPostage, confirmDate, confirmList);
                            if (status == 1)
                            {
                                successNum++;
                                alreadyImportOrderList.add(number);
                            }
                            else
                            {
                                failNum++;
                            }
                        }
                        else
                        {
                            repetitionNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, confirmList);
                }
                else if (type == 3)
                {
                    // 批量删除
                    List<Map<String, Object>> deleteList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        //                        String shouldPayPostage = it.get("cell1") + "";
                        int status = financeSerivce.deletePostageSettlementData(number, deleteList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, deleteList);
                }
            }
            else
            {
                returnStatus = 0;
                returnMsg = "文件有误";
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", returnStatus);
            result.put("isRight", (failNum == 0 && type == 1) ? 1 : 0);
            result.put("msg", returnMsg);
            result.put("okNum", successNum);
            result.put("failNum", failNum);
            result.put("repetitionNum", repetitionNum);
            result.put("dataKey", dataKey);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("模拟or保存 运费结算 -- 失败！！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "系统错误");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 异步获取模拟导入运费结算结果
     * 
     * @param request
     * @param page
     * @param rows
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonImportPostageSettlementResult", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonImportPostageSettlementResult(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "5000") int rows, //
        @RequestParam(value = "dataKey", required = true) String dataKey)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int total = 0;
        List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            if (page == 0)
            {
                page = 1;
            }
            int start = rows * (page - 1);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
            for (int i = start; (i < dataList.size() && i < (start + rows)); i++)
            {
                dataRows.add(dataList.get(i));
            }
            total = dataList.size();
        }
        result.put("total", total);
        result.put("rows", dataRows);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/downloadImportPostageSettlementResult")
    public void downloadImportPostageSettlementResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "dataKey", required = true) String dataKey)
        throws Exception
    {
        List<Map<String, Object>> dataList = null;
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
        }
        dataList = dataList == null ? new ArrayList<Map<String, Object>>() : dataList;
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "模拟结果导出";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"导入状态", "说明", "订单号", "运费金额"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> curr = dataList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(curr.get("status") + "");
                row.createCell(1).setCellValue(curr.get("msg") + "");
                row.createCell(2).setCellValue(curr.get("orderNumber") + "");
                row.createCell(3).setCellValue(curr.get("postage") + "");
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
     * 下载运费结算模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadPostageSettlementTemplate")
    public void downloadPostageSettlementTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "运费结算导入模板";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"订单号", "应付运费金额"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
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
     * 退款结算状态管理
     * 
     * @return
     */
    @RequestMapping("/refundSettlement")
    public ModelAndView refundSettlement()
    {
        return new ModelAndView("finance/refundSettlement");
    }
    
    /**
     * 下载退款结算模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadRefundSettlementTemplate")
    public void downloadRefundSettlementTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "退款结算导入模板";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"退款ID", "承担方", "承担金额"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
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
     * 模拟or保存 退款结算
     * 
     * @param request
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importRefundSettlement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "财务结算管理-退款结算")
    public String importRefundSettlement(HttpServletRequest request, //
        @RequestParam("importFile") MultipartFile file, //
        @RequestParam(value = "confirmDate", required = true) String confirmDate, // 导入日期
        @RequestParam(value = "type", required = true) int type// 1 : 模拟 ； 2 : 确认 ； 3 : 撤销
    )
        throws Exception
    {
        try
        {
            int returnStatus = 1;
            String returnMsg = "";
            int successNum = 0;
            int failNum = 0;
            String dataKey = "importRefundSettlementStatus" + System.currentTimeMillis() + "";
            
            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                if (type == 1)
                {
                    // 模拟导入
                    List<Map<String, Object>> simulationList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String refundId = it.get("cell0") + "";
                        String responsibilitySide = it.get("cell1") + "";
                        String money = it.get("cell2") + "";
                        int status = financeSerivce.checkimportRefundSettlementData(refundId, responsibilitySide, money, simulationList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, simulationList);
                }
                else if (type == 2)
                {
                    // 确认导入
                    List<Map<String, Object>> confirmList = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> it : data)
                    {
                        String refundId = it.get("cell0") + "";
                        String responsibilitySide = it.get("cell1") + "";
                        String money = it.get("cell2") + "";
                        int status = financeSerivce.saveRefundSettlementData(refundId, responsibilitySide, money, confirmDate, confirmList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, confirmList);
                }
                else if (type == 3)
                {
                    //撤销
                    List<Map<String, Object>> cancelList = new ArrayList<>();
                    for (Map<String, Object> it : data)
                    {
                        String refundId = it.get("cell0") + "";
                        int status = financeSerivce.cancelRefundSettlementData(refundId, cancelList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, cancelList);
                }
            }
            else
            {
                returnStatus = 0;
                returnMsg = "文件有误";
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", returnStatus);
            result.put("isRight", (failNum == 0 && type == 1) ? 1 : 0);
            result.put("msg", returnMsg);
            result.put("okNum", successNum);
            result.put("failNum", failNum);
            result.put("dataKey", dataKey);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("模拟or保存 退款结算 -- 失败！！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "系统错误");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 异步获取模拟导入退款结算结果
     * 
     * @param request
     * @param page
     * @param rows
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonimportRefundSettlementResult", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonimportRefundSettlementResult(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "5000") int rows, //
        @RequestParam(value = "dataKey", required = true) String dataKey)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int total = 0;
        List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            if (page == 0)
            {
                page = 1;
            }
            int start = rows * (page - 1);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
            for (int i = start; (i < dataList.size() && i < (start + rows)); i++)
            {
                dataRows.add(dataList.get(i));
            }
            total = dataList.size();
        }
        result.put("total", total);
        result.put("rows", dataRows);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/downloadimportRefundSettlementResult")
    public void downloadimportRefundSettlementResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "dataKey", required = true) String dataKey)
        throws Exception
    {
        List<Map<String, Object>> dataList = null;
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
        }
        dataList = dataList == null ? new ArrayList<Map<String, Object>>() : dataList;
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "模拟结果导出";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"导入状态", "说明", "退款ID", "承担方", "承担金额"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> curr = dataList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(curr.get("status") + "");
                row.createCell(1).setCellValue(curr.get("msg") + "");
                row.createCell(2).setCellValue(curr.get("refundId") + "");
                row.createCell(3).setCellValue(curr.get("responsibilitySide") + "");
                row.createCell(4).setCellValue(curr.get("money") + "");
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
     * 批量修改供货价管理
     * 
     * @return
     */
    @RequestMapping("/batchUpdateProductCostPrice")
    public ModelAndView batchUpdateProductCostPrice()
    {
        return new ModelAndView("finance/batchUpdateProductCostPrice");
    }
    
    /**
     * 下载批量修改供货价模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadBatchUpdateProductCostPrice")
    public void downloadBatchUpdateProductCostPrice(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "批量修改供货价导入模板";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"订单号", "商品类型", "商品ID", "单位供货价"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
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
     * 模拟or保存 批量修改供货价
     * 
     * @param request
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importBatchUpdateProductCostPrice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "财务结算管理-修改供货价")
    public String importBatchUpdateProductCostPrice(HttpServletRequest request, //
        @RequestParam("importFile") MultipartFile file, //
        @RequestParam(value = "type", required = true) int type// 1 : 模拟 ； 2 : 确认
    )
        throws Exception
    {
        try
        {
            int returnStatus = 1;
            String returnMsg = "";
            int successNum = 0;
            int failNum = 0;
            String dataKey = "importBatchUpdateProductCostPrice" + System.currentTimeMillis() + "";
            Map<String, String> productInfo = new HashMap<>();
            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                if (type == 1)
                {
                    // 模拟导入
                    List<Map<String, Object>> simulationList = new ArrayList<>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        String productType = it.get("cell1") + "";
                        String productId = it.get("cell2") + "";
                        String productCost = it.get("cell3") + "";
                        int status = financeSerivce.checkBatchUpdateProductCostPrice(number, productType, productId, productCost, simulationList, productInfo);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, simulationList);
                }
                else if (type == 2)
                {
                    // 确认导入
                    List<Map<String, Object>> confirmList = new ArrayList<>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        String productType = it.get("cell1") + "";
                        String productId = it.get("cell2") + "";
                        String productCost = it.get("cell3") + "";
                        int status = financeSerivce.saveBatchUpdateProductCostPrice(number, productType, productId, productCost, confirmList, productInfo);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, confirmList);
                }
            }
            else
            {
                returnStatus = 0;
                returnMsg = "文件有误";
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", returnStatus);
            result.put("isRight", (failNum == 0 && type == 1) ? 1 : 0);
            result.put("msg", returnMsg);
            result.put("okNum", successNum);
            result.put("failNum", failNum);
            result.put("dataKey", dataKey);
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("模拟or保存 批量修改供货价 -- 失败！！！", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "系统错误");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 异步获取模拟批量修改供货价结果
     * 
     * @param request
     * @param page
     * @param rows
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonBatchUpdateProductCostPriceResult", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonBatchUpdateProductCostPriceResult(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "5000") int rows, //
        @RequestParam(value = "dataKey", required = true) String dataKey)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int total = 0;
        List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            if (page == 0)
            {
                page = 1;
            }
            int start = rows * (page - 1);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
            for (int i = start; (i < dataList.size() && i < (start + rows)); i++)
            {
                dataRows.add(dataList.get(i));
            }
            total = dataList.size();
        }
        result.put("total", total);
        result.put("rows", dataRows);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/downloadBatchUpdateProductCostPriceResult")
    public void downloadBatchUpdateProductCostPriceResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "dataKey", required = true) String dataKey)
        throws Exception
    {
        List<Map<String, Object>> dataList = null;
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
        }
        dataList = dataList == null ? new ArrayList<Map<String, Object>>() : dataList;
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "模拟结果导出";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"导入状态", "说明", "订单号", "商品类型", "商品ID", "单位供货价", "原供货价"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> curr = dataList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(curr.get("status") + "");
                row.createCell(1).setCellValue(curr.get("msg") + "");
                row.createCell(2).setCellValue(curr.get("number") + "");
                row.createCell(3).setCellValue(curr.get("productType") + "");
                row.createCell(4).setCellValue(curr.get("productId") + "");
                row.createCell(5).setCellValue(curr.get("productCost") + "");
                row.createCell(6).setCellValue(curr.get("oldProductCost") + "");
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
     * 商家结算统计
     * 
     * @return
     */
    @RequestMapping("/sellerSettlementStatistics")
    public ModelAndView sellerSettlementStatistics()
    {
        try
        {
            ModelAndView mv = new ModelAndView("finance/sellerSettlementStatistics");
            mv.addObject("selectDate", DateTime.now().toString("yyyy-MM"));
            List<Integer> dateList = new ArrayList<Integer>();
            for (int i = 1; i <= 31; i++)
            {
                dateList.add(i);
            }
            mv.addObject("dateList", dateList);
            return mv;
        }
        catch (Exception e)
        {
            log.error("商家结算统计失败！", e);
            return new ModelAndView("error/404");
        }
    }
    
    @RequestMapping(value = "/jsonSellerSettlementStatistics", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerSettlementStatistics(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-04    //2015-10
        @RequestParam(value = "start", required = false, defaultValue = "0") int start, // 1-31                   //1
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop, // 1-31                     //8
        @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        throws Exception
    {
        try
        {
            if (search == 0)
            {
                return JSON.toJSONString(new ArrayList());
            }
            // selectDate = "".equals(selectDate) ? DateTime.now().toString("yyyy-MM") : selectDate;
            int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;// 1
            String e = "" + stop;// 1
            if (start < 10)
            {
                s = "0" + start; // "01"
            }
            if (stop < 10)
            {
                e = "0" + stop; // "08"
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }

            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("startTimeBegin", payTimeBegin);
            para.put("startTimeEnd", payTimeEnd);
            Map<String, Object> result = financeSerivce.findSellerSettlementStatistics(para);
            List<Map<String, Object>> re = (List<Map<String, Object>>)result.get("rows");
            if (re.size() > 0)
            {
                re.add((Map<String, Object>)result.get("lastRow"));
            }
            return JSON.toJSONString(re);
        }
        catch (Exception e)
        {
            log.error("商家结算统计失败！", e);
            return "";
        }
    }
    
    /**
     * 导出 商家结算统计
     * 
     * @param request
     * @param response
     * @param selectDate
     * @param start
     * @param stop
     */
    @RequestMapping(value = "/exportSellerSettlementStatistics")
    @ControllerLog(description = "财务结算管理-导出商家结算统计")
    public void exportSellerSettlementStatistics(HttpServletRequest request, HttpServletResponse response,
    // @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start, // 1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop// 1-31
    )
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;// 1
            String e = "" + stop;// 1
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("startTimeBegin", payTimeBegin);
            para.put("startTimeEnd", payTimeEnd);
            Map<String, Object> result = financeSerivce.findSellerSettlementStatistics(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            Map<String, Object> lastRow = (Map<String, Object>)result.get("lastRow");
            String[] title = {"真实商家名称", "订单实付", "应付商家", "毛利", "毛利率", "净毛利率", "运费", "总价(单位售价*数量)", "积分优惠", "优惠券优惠", "客服调价"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("realSellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalPaySellerPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalGross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("grossRate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("pureGrossRate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalFreightMoney") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalProductPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalpointProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalcouponProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totaladjustProportion") + "");
            }
            if (rows.size() > 0)
            {
                Row lastR = sheet.createRow(rows.size() + 1);
                int cellIndex = 0;
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("realSellerName") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalRealPrice") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalPaySellerPrice") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalGross") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("grossRate") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("pureGrossRate") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalFreightMoney") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalProductPrice") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalpointProportion") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalcouponProportion") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totaladjustProportion") + "");
            }
            String fileName = "商家结算统计" + payTimeBegin.replaceAll(" |:", "_") + "到" + payTimeEnd.replaceAll(" |:", "_");
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出出错", e);
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
     * 商家退款统计
     * 
     * @return
     */
    @RequestMapping("/sellerRefundStatistics")
    public ModelAndView sellerRefundStatistics()
    {
        ModelAndView mv = new ModelAndView("finance/newSellerRefundStatistics");
        mv.addObject("selectDate", DateTime.now().toString("yyyy-MM"));
        List<Integer> dateList = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++)
        {
            dateList.add(i);
        }
        mv.addObject("dateList", dateList);
        return mv;
    }
    
    @RequestMapping(value = "/jsonSellerRefundStatistics", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerRefundStatistics(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start, // 1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop, // 1-31
        @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        throws Exception
    {
        if (search == 0)
        {
            return JSON.toJSONString(new ArrayList());
        }
        // selectDate = "".equals(selectDate) ? DateTime.now().toString("yyyy-MM") : selectDate;
        int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
        Map<String, Object> para = new HashMap<String, Object>();
        DateTime begin = null;
        DateTime end = null;
        String s = "" + start;// 1
        String e = "" + stop;// 1
        if (start < 10)
        {
            s = "0" + start;
        }
        if (stop < 10)
        {
            e = "0" + stop;
        }
        else if (stop > maxDay)
        {
            e = "" + maxDay;
        }
        
        begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
        String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
        String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
        para.put("startTimeBegin", payTimeBegin);
        para.put("startTimeEnd", payTimeEnd);
        // Map<String, Object> result = financeSerivce.findSellerRefundStatistics(para);
        Map<String, Object> result = financeSerivce.findSellerRefundStatisticsNew(para);
        List<Map<String, Object>> re = (List<Map<String, Object>>)result.get("rows");
        if (re.size() > 0)
        {
            re.add((Map<String, Object>)result.get("lastRow"));
        }
        return JSON.toJSONString(re);
    }
    
    @RequestMapping(value = "/exportSellerRefundStatistics")
    @ControllerLog(description = "财务结算管理-导出商家退款结算统计")
    public void exportSellerRefundStatistics(HttpServletRequest request, HttpServletResponse response,
    // @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,
        @RequestParam(value = "selectDate", required = false, defaultValue = "") String selectDate, // 2015-04
        @RequestParam(value = "start", required = false, defaultValue = "0") int start, // 1-31
        @RequestParam(value = "stop", required = false, defaultValue = "0") int stop// 1-31
    )
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            int maxDay = new DateTime(CommonUtil.string2Date(selectDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusMonths(1).plusDays(-1).getDayOfMonth();
            Map<String, Object> para = new HashMap<String, Object>();
            DateTime begin = null;
            DateTime end = null;
            String s = "" + start;// 1
            String e = "" + stop;// 1
            if (start < 10)
            {
                s = "0" + start;
            }
            if (stop < 10)
            {
                e = "0" + stop;
            }
            else if (stop > maxDay)
            {
                e = "" + maxDay;
            }
            
            begin = new DateTime(CommonUtil.string2Date(selectDate + "-" + s + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            end = new DateTime(CommonUtil.string2Date(selectDate + "-" + e + " 00:00:00", "yyyy-MM-dd HH:mm:ss")).plusDays(1);
            String payTimeBegin = begin.toString("yyyy-MM-dd 00:00:00");
            String payTimeEnd = end.toString("yyyy-MM-dd 00:00:00");
            para.put("startTimeBegin", payTimeBegin);
            para.put("startTimeEnd", payTimeEnd);
            // Map<String, Object> result = financeSerivce.findSellerRefundStatistics(para);
            Map<String, Object> result = financeSerivce.findSellerRefundStatisticsNew(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            Map<String, Object> lastRow = (Map<String, Object>)result.get("lastRow");
            // String[] title = {"真实商家名称", "退款金额", "应付商家", "毛利", "运费", "商家承担运费"};
            String[] title = {"真实商家名称", "退款金额", "商家承担", "左岸城堡承担"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("realSellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalRefundPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalSellerMoney") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalGegejiaMoney") + "");
            }
            if (rows.size() > 0)
            {
                Row lastR = sheet.createRow(rows.size() + 1);
                int cellIndex = 0;
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("realSellerName") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalRefundPrice") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalSellerMoney") + "");
                lastR.createCell(cellIndex++).setCellValue(lastRow.get("totalGegejiaMoney") + "");
            }
            String fileName = "商家退款结算统计" + payTimeBegin.replaceAll(" |:", "_") + "到" + payTimeEnd.replaceAll(" |:", "_");
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出出错", e);
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
     * 商家结算周期管理
     *
     * @return
     */
    @RequestMapping("/sellerSettlementPeriodList")
    public ModelAndView sellerSettlementPeriodList()
    {
        return new ModelAndView("finance/sellerSettlementPeriodList");
    }
    
    /**
     * 异步获取 商家结算周期管理
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/jsonSellerSettlementPeriodData")
    @ResponseBody
    public String jsonSellerSettlementPeriodData(@RequestParam(value = "sellerIds", required = false, defaultValue = "-1") String sellerIds,
        @RequestParam(value = "remainsDays", required = false, defaultValue = "") String remainsDays)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        
        if (!"".equals(sellerIds) && sellerIds.indexOf("-1") < 0)
        {
            List<Integer> sellerIdList = new ArrayList<>();
            if (sellerIds.indexOf(",") > -1)
            {
                String[] arr = sellerIds.split(",");
                for (String s : arr)
                {
                    if (StringUtils.isNumeric(s))
                    {
                        sellerIdList.add(Integer.valueOf(s));
                    }
                }
            }
            else
            {
                sellerIdList.add(Integer.valueOf(sellerIds));
            }
            para.put("sellerIdList", sellerIdList);
        }
        
        if (!"".equals(remainsDays.trim()))
        {
            para.put("remainsDays", Integer.valueOf(remainsDays.trim()));
        }
        Map<String, Object> result = financeSerivce.findSellerSettlementPeriodData(para);
        return JSON.toJSONString((List<Map<String, Object>>)result.get("rows"));
    }
    
    /**
     * 导出 商家结算周期管理 数据
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/exportSellerSettlementPeriodData")
    @ControllerLog(description = "财务结算管理-导出商家结算周期数据")
    public void exportSellerSettlementPeriodData(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "sellerIds", required = false, defaultValue = "-1") String sellerIds,
        @RequestParam(value = "remainsDays", required = false, defaultValue = "") String remainsDays)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<>();
            
            if (!"".equals(sellerIds) && sellerIds.indexOf("-1") < 0)
            {
                List<Integer> sellerIdList = new ArrayList<>();
                if (sellerIds.indexOf(",") > -1)
                {
                    String[] arr = sellerIds.split(",");
                    for (String s : arr)
                    {
                        if (StringUtils.isNumeric(s))
                        {
                            sellerIdList.add(Integer.valueOf(s));
                        }
                    }
                }
                else
                {
                    sellerIdList.add(Integer.valueOf(sellerIds));
                }
                para.put("sellerIdList", sellerIdList);
            }
            
            if (!"".equals(remainsDays.trim()))
            {
                para.put("remainsDays", Integer.valueOf(remainsDays.trim()));
            }
            Map<String, Object> result = financeSerivce.findSellerSettlementPeriodData(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            String[] title = {"真实商家名称", "发货地", "最早未结算订单时间", "未结算订单总数量", "未结算订单总金额", "结算规则", "活动周期时长", "结算日", "距离结算日还有几天"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("realSellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sendAddress") + "");
                r.createCell(cellIndex++).setCellValue(it.get("earliestTime") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalNum") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("settlementRule") + "");
                r.createCell(cellIndex++).setCellValue(it.get("activityCycleDuration") + "");
                r.createCell(cellIndex++).setCellValue(it.get("settlementTime") + "");
                r.createCell(cellIndex++).setCellValue(it.get("remainsDays") + "");
            }
            String fileName = "商家结算管理";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出出错", e);
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
     * 商家结算汇总
     */
    @RequestMapping("/sellerSettlementSum")
    public ModelAndView sellerSettlementSum()
    {
        ModelAndView mv = new ModelAndView("finance/sellerSettlementSum");
        mv.addObject("selectDate", DateTime.now().toString("yyyy-MM"));
        List<Integer> dateList = new ArrayList<Integer>();
        for (int i = 1; i <= 31; i++)
        {
            dateList.add(i);
        }
        mv.addObject("dateList", dateList);
        return mv;
    }
    
    /**
     * 异步获取 商家结算汇总
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonSellerSettlementSum", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonSellerSettlementSum(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        // 为0表示不查询结果
        throws Exception
    {
        if (search == 0)
        {
            return JSON.toJSONString(new ArrayList());
        }
        
        Map<String, Object> para = new HashMap<String, Object>();
        if (!"".equals(startTime))
        {
            para.put("settlementTimeBegin", startTime);
        }
        if (!"".equals(endTime))
        {
            para.put("settlementTimeEnd", endTime);
        }
        
        Map<String, Object> result = financeSerivce.findSellerSettlementSum(para);
        List<Map<String, Object>> re = (List<Map<String, Object>>)result.get("rows");
        //        if (re.size() > 0)
        //        {
        //            re.add((Map<String, Object>)result.get("lastRow"));
        //        }
        //        System.out.println("result: " + result);
        return JSON.toJSONString(re);
    }
    
    /**
     * 导出 商家结算周期管理 数据
     *
     * @throws Exception
     */
    @RequestMapping(value = "/exportSellerSettlementSum")
    @ControllerLog(description = "财务结算管理-导出商家结算统计")
    public void exportSellerSettlementSum(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime,
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, @RequestParam(value = "search", required = false, defaultValue = "1") int search)
        // 为0表示不查询结果
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (!"".equals(startTime))
            {
                para.put("settlementTimeBegin", startTime);
            }
            if (!"".equals(endTime))
            {
                para.put("settlementTimeEnd", endTime);
            }
            Map<String, Object> result = financeSerivce.findSellerSettlementSum(para);
            List<Map<String, Object>> rows = (List<Map<String, Object>>)result.get("rows");
            String[] title = {"商家ID", "商家名称", "订单实付", "订单供货价", "订单结算运费", "结算退款中商家承担部分", "应付商家"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("sellerId") == null ? "" : it.get("sellerId") + "");
                r.createCell(cellIndex++).setCellValue(it.get("realSellerName") == null ? "" : it.get("realSellerName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalRealPrice") == null ? "" : it.get("totalRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalCost") == null ? "" : it.get("totalCost") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalFreight") == null ? "" : it.get("totalFreight") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalSellerResponsibility") == null ? "" : it.get("totalSellerResponsibility") + "");
                r.createCell(cellIndex++).setCellValue(it.get("totalPaySeller") == null ? "" : it.get("totalPaySeller") + "");
            }
            String fileName = "商家结算统计";
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("导出出错", e);
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
     * 订单罚款状态管理
     *
     * @return
     */
    @RequestMapping("/penaltySettlement")
    public ModelAndView penaltySettlement()
    {
        return new ModelAndView("finance/penaltySettlement");
    }

    /**
     * 下载罚款结算模板
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadPenaltySettlementTemplate")
    public void downloadPenaltySettlementTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "罚款结算导入模板";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"订单编号"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
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
     * 模拟or保存 罚款结算
     *
     * @param request
     * @param file
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importPenaltySettlement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "财务结算管理-罚款结算")
    public Object importPenaltySettlement(HttpServletRequest request, //
        @RequestParam("importFile") MultipartFile file, //
        @RequestParam(value = "confirmDate", required = true) String confirmDate, // 导入日期
        @RequestParam(value = "type", required = true) int type// 1 : 模拟 ； 2 : 确认 ； 3 : 撤销
    )
        throws Exception
    {
        try
        {
            int returnStatus = 1;
            String returnMsg = "";
            int successNum = 0;
            int failNum = 0;
            String dataKey = "importPenaltySettlementStatus" + System.currentTimeMillis() + "";

            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                if (type == 1)
                {
                    // 模拟导入
                    List<Map<String, Object>> simulationList = new ArrayList<>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        int status = financeSerivce.checkImportPenaltySettlementData(number, simulationList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, simulationList);
                }
                else if (type == 2)
                {
                    // 确认导入
                    List<Map<String, Object>> confirmList = new ArrayList<>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        int status = financeSerivce.savePenaltySettlementData(number, confirmList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, confirmList);
                }
                else if (type == 3)
                {
                    //撤销
                    List<Map<String, Object>> cancelList = new ArrayList<>();
                    for (Map<String, Object> it : data)
                    {
                        String number = it.get("cell0") + "";
                        int status = financeSerivce.deletePenaltySettlementData(number, cancelList);
                        if (status == 1)
                        {
                            successNum++;
                        }
                        else
                        {
                            failNum++;
                        }
                    }
                    request.getSession().setAttribute(dataKey, cancelList);
                }
            }
            else
            {
                returnStatus = 0;
                returnMsg = "文件有误";
            }
            Map<String, Object> result = new HashMap<>();
            result.put("status", returnStatus);
            result.put("isRight", (failNum == 0 && type == 1) ? 1 : 0);
            result.put("msg", returnMsg);
            result.put("okNum", successNum);
            result.put("failNum", failNum);
            result.put("dataKey", dataKey);
            return result;
        }
        catch (Exception e)
        {
            log.error("模拟or保存 罚款结算 -- 失败！！！", e);
            Map<String, Object> result = new HashMap<>();
            result.put("status", 0);
            result.put("msg", "系统错误");
            return result;
        }
    }

    /**
     * 异步获取模拟导入罚款结算结果
     *
     * @param request
     * @param page
     * @param rows
     * @param dataKey
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jsonImportPenaltySettlement", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonImportPenaltySettlement(HttpServletRequest request, //
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "5000") int rows, //
        @RequestParam(value = "dataKey", required = true) String dataKey)
            throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        int total = 0;
        List<Map<String, Object>> dataRows = new ArrayList<>();
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            if (page == 0)
            {
                page = 1;
            }
            int start = rows * (page - 1);
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
            for (int i = start; (i < dataList.size() && i < (start + rows)); i++)
            {
                dataRows.add(dataList.get(i));
            }
            total = dataList.size();
        }
        result.put("total", total);
        result.put("rows", dataRows);
        return JSON.toJSONString(result);
    }

    /**
     * 下载获取模拟导入罚款结算结果
     * @param request
     * @param response
     * @param dataKey
     * @throws Exception
     */
    @RequestMapping(value = "/downloadImportPenaltySettlement")
    public void downloadImportPenaltySettlement(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "dataKey", required = true) String dataKey)
            throws Exception
    {
        List<Map<String, Object>> dataList = null;
        if (!"".equals(dataKey) && (request.getSession().getAttribute(dataKey) != null))
        {
            dataList = (List<Map<String, Object>>)request.getSession().getAttribute(dataKey);
        }
        dataList = dataList == null ? new ArrayList<Map<String, Object>>() : dataList;
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "罚款结算模拟结果导出";
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            // 产生工作簿对象
            String[] title = {"导入状态", "说明", "订单编号", "是否超时"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> curr = dataList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(curr.get("status") + "");
                row.createCell(1).setCellValue(curr.get("msg") == null ? "" :curr.get("msg") + "");
                row.createCell(2).setCellValue(curr.get("number") + "");
                row.createCell(3).setCellValue(curr.get("isTimeOut") == null ? "" : curr.get("isTimeOut") + "");
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
    
}
