package com.ygg.admin.controller;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.entity.OverseasOrderInfoForManage;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.service.OverseasOrderService;
import com.ygg.admin.service.SellerService;
import com.ygg.admin.util.CgiUtil;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.util.ZipCompressorByAnt;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 海外购订单相关控制器
 * 
 * @author zhangyb
 *
 */

@Controller
@RequestMapping(value = "/overseasOrder")
public class OverseasOrderController
{
    
    Logger log = Logger.getLogger(OverseasOrderController.class);
    
    @Resource(name = "overseasOrderService")
    private OverseasOrderService overseasOrderService = null;
    
    @Resource(name = "orderService")
    private OrderService orderService = null;
    
    @Resource(name = "sellerService")
    private SellerService sellerService;
    
    /**
     * 海外购订单list
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("overseas/overseasOrderList");
        return mv;
    }
    
    /**
     * 获取订单list
     */
    @RequestMapping(value = "/jsonOverseasOrderInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOverseasOrderInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber,
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus,
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName,
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone,
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2,
        @RequestParam(value = "operaStatus", required = false, defaultValue = "0") int operaStatus // 导出状态:1:已导出;0:未导出
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
        if (orderNumber != 0)
        {
            para.put("orderNumber", orderNumber);
        }
        if (orderStatus != 0)
        {
            para.put("orderStatus", orderStatus);
        }
        if (!"".equals(accountName))
        {
            para.put("accountName", "%" + accountName + "%");
        }
        if (!"".equals(receiveName))
        {
            para.put("fullName", "%" + receiveName + "%");
        }
        if (!"".equals(reveivePhone))
        {
            para.put("mobileNumber", reveivePhone);
        }
        if (!"".equals(startTime1))
        {
            para.put("startTimeBegin", startTime1);
        }
        if (!"".equals(endTime1))
        {
            para.put("startTimeEnd", endTime1);
        }
        if (!"".equals(startTime2))
        {
            para.put("exportTimeBegin", startTime2);
        }
        if (!"".equals(endTime2))
        {
            para.put("exportTimeEnd", endTime2);
        }
        if (operaStatus != -1)
        {// 已导出
            para.put("operaStatus", operaStatus);
        }
        Map<String, Object> result = overseasOrderService.jsonOverseasOrder(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 导出所有可导海外购订单
     */
    @RequestMapping(value = "/exportResult")
    @ControllerLog(description = "海外购订单管理-导出海外购订单")
    public void exportResult(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "orderNumber", required = false, defaultValue = "0") long orderNumber,
        @RequestParam(value = "orderStatus", required = false, defaultValue = "0") int orderStatus,
        @RequestParam(value = "accountName", required = false, defaultValue = "") String accountName,
        @RequestParam(value = "receiveName", required = false, defaultValue = "") String receiveName,
        @RequestParam(value = "reveivePhone", required = false, defaultValue = "") String reveivePhone,
        @RequestParam(value = "startTime1", required = false, defaultValue = "") String startTime1,
        @RequestParam(value = "endTime1", required = false, defaultValue = "") String endTime1,
        @RequestParam(value = "startTime2", required = false, defaultValue = "") String startTime2,
        @RequestParam(value = "endTime2", required = false, defaultValue = "") String endTime2,
        @RequestParam(value = "operaStatus", required = false, defaultValue = "-1") int operaStatus, // 导出状态:1:已导出;0:未导出
        @RequestParam(value = "minimumTotalPrice", required = false, defaultValue = "-1") Integer minimumTotalPrice
    )
    
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (orderNumber != 0)
            {
                para.put("orderNumber", orderNumber);
            }
            if (orderStatus != 0)
            {
                para.put("orderStatus", orderStatus);
            }
            if (!"".equals(accountName))
            {
                para.put("accountName", "%" + accountName + "%");
            }
            if (!"".equals(receiveName))
            {
                para.put("fullName", "%" + receiveName + "%");
            }
            if (!"".equals(reveivePhone))
            {
                para.put("mobileNumber", reveivePhone);
            }
            if (!"".equals(startTime1))
            {
                para.put("startTimeBegin", startTime1);
            }
            if (!"".equals(endTime1))
            {
                para.put("startTimeEnd", endTime1);
            }
            if (!"".equals(startTime2))
            {
                para.put("exportTimeBegin", startTime2);
            }
            if (!"".equals(endTime2))
            {
                para.put("exportTimeEnd", endTime2);
            }
            if (operaStatus != -1)
            {// 已导出
                para.put("operaStatus", operaStatus);
            }
            if (minimumTotalPrice > 0 ){
                para.put("minimumTotalPrice", minimumTotalPrice);
            }
            Map<String, Object> result = overseasOrderService.jsonOverseasOrder(para);
            List<OverseasOrderInfoForManage> reList = (List<OverseasOrderInfoForManage>)result.get("rows");
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"下单时间", "付款时间", "发货时间", "是否导出", "确定导出成功时间", "订单编号", "订单状态", "订单总价", "收货人", "收货手机", "商家", "发货地"};
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < reList.size(); i++)
            {
                Row r = sheet.createRow(i + 1);
                OverseasOrderInfoForManage om = reList.get(i);
                int index = 0;
                r.createCell(index++).setCellValue(om.getCreateTime());
                r.createCell(index++).setCellValue(om.getPayTime());
                r.createCell(index++).setCellValue(om.getSendTime());
                r.createCell(index++).setCellValue(om.getExportStatusStr());
                r.createCell(index++).setCellValue(om.getExportTime());
                r.createCell(index++).setCellValue(om.getNumber());
                r.createCell(index++).setCellValue(om.getStatusStr());
                r.createCell(index++).setCellValue(om.getTotalPrice());
                r.createCell(index++).setCellValue(om.getFullName());
                r.createCell(index++).setCellValue(om.getMobileNumber());
                r.createCell(index++).setCellValue(om.getSellerName());
                r.createCell(index++).setCellValue(om.getSendAddress());
            }
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "海外购订单查询结果";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
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
     * 检查海外购商品信息是否完整
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkOverseasExport", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String checkOverseasExport(HttpServletRequest request)
        throws Exception
    {
        /*
         * 根据导单名称和价格内设置的，对导单名称和价格进行替换； 如果有无法找到，直接导出失败，然后将失败的记录写到“设置导单名称和价格”内， 弹窗提示“有商品无法找到导单名称和导单价格，请至设置导单名称和价格内补充后再次导出！
         */
        
        // 检查订单内的海外商品是否都设置了导出名称和价格。
        overseasOrderService.deletePro();
        overseasOrderService.deleteIdCardByStatusEqualsZero();
        String lastTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");// 截止到当前时间点，作为下次导出截止时间点。
        boolean checkStatus = overseasOrderService.checkOrderExportPriceAndName();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 1);
        map.put("lastTime", lastTime);
        if (!checkStatus)
        {
            map.put("status", 0);
            map.put("lastTime", 0);
            map.put("msg", "有商品无法找到导单名称和导单价格，请至设置导单名称和价格内补充后再次导出！");
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 导出所有可导海外购订单
     */
    @RequestMapping(value = "/exportAll")
    @ControllerLog(description = "海外购订单管理-导出所有海外购订单")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, String lastTime)
        throws Exception
    {
        boolean isBigPrice = CgiUtil.getRequiredValue(request,"isBigPrice", 0) == 0 ? false : true; //过滤只返回2000元以上的订单
        Date lastDate = CommonUtil.string2Date(lastTime, "yyyy-MM-dd HH:mm:ss");
        String result = overseasOrderService.overseasAllCanExport(lastDate, isBigPrice);
        String zipName = result + ".zip";
        ZipCompressorByAnt zca = new ZipCompressorByAnt(zipName);
        zca.compressExe(result);
        FileInputStream downFile = new FileInputStream(zipName);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/x-msdownload;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("海外购订单.zip", "utf-8"));
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
//        FileUtil.deleteFile(result);
//        FileUtil.deleteFile(zipName);
    }
    

    /**
     * 海外购商品信息list
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/overseasProductList")
    public ModelAndView overseasProductList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("overseas/overseasProductInfoList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonOverseasProductInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonOverseasProductInfo(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, @RequestParam(value = "exportName", required = false, defaultValue = "") String exportName,
        @RequestParam(value = "code", required = false, defaultValue = "") String code)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(exportName))
        {
            para.put("exportName", "%" + exportName + "%");
        }
        if (!"".equals(code))
        {
            para.put("code", code);
        }
        String jsonStr = JSON.toJSONString(overseasOrderService.findOverseasProductInfo(para));
        return jsonStr;
    }
    
    /**
     * 更新 or 新增海外购商品信息
     */
    @RequestMapping(value = "/saveProduct", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-新增/编辑海外购商品信息")
    public String saveProduct(
        HttpServletRequest request,
        @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "code", required = false, defaultValue = "") String code,//
        @RequestParam(value = "sellerId", required = false, defaultValue = "0") int sellerId,//
        @RequestParam(value = "remark", required = false, defaultValue = "") String remark,
        @RequestParam(value = "exportName", required = false, defaultValue = "") String exportName,
        @RequestParam(value = "exportPrice", required = false, defaultValue = "0") double exportPrice,// 单位为元，插入时需转为分
        @RequestParam(value = "name", required = false, defaultValue = "") String name)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if ("".equals(code) || "".equals(exportName) || exportPrice == 0)
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "商品编码，导出名称，导出价格必填！");
            return JSON.toJSONString(result);
        }
        if (id != 0)
        {
            map.put("id", id);
        }
        map.put("code", code);
        map.put("remark", remark);
        map.put("exportName", exportName);
        BigDecimal exportPrice_big = new BigDecimal(exportPrice);
        map.put("exportPrice", exportPrice_big.multiply(new BigDecimal("100")).intValue());
        map.put("name", name);
        map.put("sellerId", sellerId);
        try
        {
            int status = overseasOrderService.insertOrUpdateOverseasProductInfo(map);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", status == 1 ? "保存成功" : "保存失败,请检查是否已经存在该商家编码对应的信息");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("保存失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 删除海外购商品信息
     */
    @RequestMapping(value = "/deleteProduct", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-删除海外购商品信息")
    public String deleteProduct(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        try
        {
            int status = overseasOrderService.deleteOverseasProductInfoById(id);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", status == 1 ? "删除成功" : "删除失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("删除海外购商品信息失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "删除失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 导出海外购商品信息
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportOverseasProductInfo")
    @ControllerLog(description = "海外购订单管理-导出海外购商品")
    public void exportOverseasProductInfo(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "exportName", required = false, defaultValue = "") String exportName, @RequestParam(value = "code", required = false, defaultValue = "") String code)
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> para = new HashMap<String, Object>();
            if (!"".equals(exportName))
            {
                para.put("exportName", "%" + exportName + "%");
            }
            if (!"".equals(code))
            {
                para.put("code", code);
            }
            Map<String, Object> map = overseasOrderService.findOverseasProductInfo(para);
            List<Map<String, Object>> reList = (List<Map<String, Object>>)map.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "overseasOrder";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"状态", "商品编码", "原名称", "导单名称", "导单售价", "来源订单号", "商家", "发货地", "备注"};
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
                r.createCell(0).setCellValue(currMap.get("statusMsgForExport") + "");
                r.createCell(1).setCellValue(currMap.get("code") + "");
                r.createCell(2).setCellValue(currMap.get("name") + "");
                r.createCell(3).setCellValue(currMap.get("exportName") + "");
                r.createCell(4).setCellValue(new BigDecimal(currMap.get("exportPrice") + "").intValue());
                r.createCell(5).setCellValue(currMap.get("orderNumber") + "");
                r.createCell(6).setCellValue(currMap.get("sellerName") + "");
                r.createCell(7).setCellValue(currMap.get("sendAddress") + "");
                r.createCell(8).setCellValue(currMap.get("remark") + "");
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
     * 导出发货excel
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exportImportOverseasProTemplate")
    @ControllerLog(description = "海外购订单管理-导出海外购发货订单")
    public void exportImportOverseasProTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        response.setContentType("application/vnd.ms-excel");
        String codedFileName = "OverseasProTemplate";
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
            String[] str = {"商品编码", "原名称", "导单名称", "导单售价", "备注"};
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
            e.printStackTrace();
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
     * 批量导入
     * 
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/importOverseasPro", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-导入海外购信息")
    public String importOverseasPro(HttpServletRequest request, @RequestParam("orderFile") MultipartFile file, @RequestParam(value = "sellerId", required = true) int sellerId)
        throws Exception
    {
        try
        {
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Map<String, Object> map = new HashMap<String, Object>();
            Sheet sheet = workbook.getSheetAt(0);
            int startNum = sheet.getFirstRowNum();
            int lastNum = sheet.getLastRowNum();
            if (startNum == lastNum)
            {// 可过滤第一行，因为第一行是标题
                map.put("status", 0);
                map.put("msg", "文件为空请确认！");
                return JSON.toJSONString(map);
            }
            int rNum = 0;
            int wNum = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = startNum + 1; i <= lastNum; i++)
            {
                Row row = sheet.getRow(i);
                Cell cell0 = row.getCell(0);
                Cell cell1 = row.getCell(1);
                Cell cell2 = row.getCell(2);
                Cell cell3 = row.getCell(3);
                Cell cell4 = row.getCell(4);
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
                if (cell4 != null)
                {
                    cell4.setCellType(Cell.CELL_TYPE_STRING);
                }
                String code = cell0 == null ? "" : cell0.getStringCellValue();
                String name = cell1 == null ? "" : cell1.getStringCellValue();
                String exportName = cell2 == null ? "" : cell2.getStringCellValue();
                String exportPrice = cell3 == null ? "" : cell3.getStringCellValue();
                String remark = cell4 == null ? "" : cell4.getStringCellValue();
                
                Map<String, Object> currMap = new HashMap<String, Object>();
                currMap.put("code", code);
                currMap.put("remark", remark);
                currMap.put("exportName", exportName);
                BigDecimal exportPrice_big = new BigDecimal(exportPrice);
                currMap.put("exportPrice", exportPrice_big.multiply(new BigDecimal("100")).intValue());
                currMap.put("name", name);
                currMap.put("sellerId", sellerId);
                try
                {
                    int status = overseasOrderService.insertOrUpdateOverseasProductInfo(currMap);
                    if (status == 1)
                    {
                        rNum++;
                    }
                    else
                    {
                        wNum++;
                        sb.append(code);
                        sb.append(";");
                    }
                }
                catch (Exception e)
                {
                    wNum++;
                }
            }
            map.put("status", 1);
            map.put("msg", "成功" + rNum + "条，失败" + wNum + "条，商品编号为：" + sb.toString());
            return JSON.toJSONString(map);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 0);
            map.put("msg", "操作失败！");
            return JSON.toJSONString(map);
        }
    }
    
    @RequestMapping(value = "/jsonIdCardMapping", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonIdCardMapping(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "startTime", required = false, defaultValue = "") String startTime, //
        @RequestParam(value = "endTime", required = false, defaultValue = "") String endTime, //
        @RequestParam(value = "oldName", required = false, defaultValue = "") String oldName, //
        @RequestParam(value = "realName", required = false, defaultValue = "") String realName, //
        @RequestParam(value = "idCard", required = false, defaultValue = "") String idCard, //
        @RequestParam(value = "source", required = false, defaultValue = "-1") int source, //
        @RequestParam(value = "status", required = false, defaultValue = "-1") int status)
            throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (page == 0)
        {
            page = 1;
        }
        para.put("start", rows * (page - 1));
        para.put("max", rows);
        if (!"".equals(oldName))
        {
            para.put("oldName", "%" + oldName + "%");
        }
        if (!"".equals(realName))
        {
            para.put("realName", "%" + realName + "%");
        }
        if (status != -1)
        {
            para.put("status", status);
        }
        if (source != -1)
        {
            para.put("source", source);
        }
        if (!"".equals(startTime))
        {
            para.put("startTimeBegin", startTime);
        }
        if (!"".equals(endTime))
        {
            para.put("startTimeEnd", endTime);
        }
        if (!"".equals(idCard))
        {
            para.put("idCard", idCard);
        }
        Map<String, Object> result = overseasOrderService.jsonIdCardMapping(para);
        return JSON.toJSONString(result);
    }
    
    @RequestMapping(value = "/idCardList")
    public ModelAndView idCardList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("overseas/idCardMappingList");
        return mv;
    }
    
    /**
     * 导出海外购商品信息
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportIdCardMapping")
    @ControllerLog(description = "海外购订单管理-导出海外购用户信息")
    public void exportIdCardMapping(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "oldName", required = false, defaultValue = "") String oldName,
        @RequestParam(value = "realName", required = false, defaultValue = "") String realName, @RequestParam(value = "idCard", required = false, defaultValue = "") String idCard,
        @RequestParam(value = "status", required = false, defaultValue = "-1") int status)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (!"".equals(oldName))
        {
            para.put("oldName", "%" + oldName + "%");
        }
        if (!"".equals(realName))
        {
            para.put("realName", "%" + realName + "%");
        }
        if (status != -1)
        {
            para.put("status", status);
        }
        if (!"".equals(idCard))
        {
            para.put("idCard", idCard);
        }
        
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> result = overseasOrderService.jsonIdCardMapping(para);
            
            List<Map<String, Object>> reList = (List<Map<String, Object>>)result.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "idCardMapping";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            String[] str = {"状态", "身份证号", "原姓名", "修改后姓名", "来源订单号", "商家", "发货地"};
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
                r.createCell(0).setCellValue(currMap.get("statusMsgForExcel") + "");
                r.createCell(1).setCellValue(currMap.get("idCard") + "");
                r.createCell(2).setCellValue(currMap.get("oldName") + "");
                r.createCell(3).setCellValue(currMap.get("realName") + "");
                r.createCell(4).setCellValue(currMap.get("orderNumber") + "");
                r.createCell(5).setCellValue(currMap.get("sellerName") + "");
                r.createCell(6).setCellValue(currMap.get("sendAddress") + "");
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
    
    @RequestMapping(value = "/deleteIdcard", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-删除海外购用户信息")
    public String deleteIdcard(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        try
        {
            int status = overseasOrderService.deleteIdcardRealnameMappingById(id);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", status == 1 ? "删除成功" : "删除失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("删除身份证姓名对应关系信息失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "删除失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 更新 or 新增 身份证姓名对应关系
     */
    @RequestMapping(value = "/saveIdCard", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-新增/编辑身份证信息")
    public String saveIdCard(HttpServletRequest request, @RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "idCard", required = false, defaultValue = "") String idCard, @RequestParam(value = "oldName", required = false, defaultValue = "") String oldName,
        @RequestParam(value = "realName", required = false, defaultValue = "") String realName)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if ("".equals(idCard) || "".equals(oldName) || "".equals(realName))
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "信息必须填写完整！");
            return JSON.toJSONString(result);
        }
        if (id != 0)
        {
            map.put("id", id);
        }
        map.put("idCard", idCard);
        map.put("oldName", oldName);
        map.put("realName", realName);
        try
        {
            int status = overseasOrderService.insertOrUpdateIdcardRealnameMapping(map);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", status == 1 ? "保存成功" : "保存失败,请检查是否已经存在该信息");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("保存失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 合并订单列表
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hbOrderList")
    public ModelAndView hbOrderList(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("overseas/hbOrderList");
        return mv;
    }
    
    @RequestMapping(value = "/jsonhbOrderList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonhbOrderList(HttpServletRequest request, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows,
        @RequestParam(value = "hbNumber", required = false, defaultValue = "") String hbNumber,
        @RequestParam(value = "sonNumber", required = false, defaultValue = "") String sonNumber,
        @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd
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
        if (!"".equals(sonNumber))
        {
            para.put("sonNumber", "%" + sonNumber + "%");
        }
        if (!"".equals(hbNumber))
        {
            para.put("hbNumber", hbNumber);
        }
        if (!"".equals(createTimeBegin))
        {
            para.put("createTimeBegin", createTimeBegin);
        }
        if (!"".equals(createTimeEnd))
        {
            para.put("createTimeEnd", createTimeEnd);
        }
        
        Map<String, Object> result = overseasOrderService.findAllHBOrderRecord(para);
        return JSON.toJSONString(result);
    }
    
    /**
     * 删除订单合并记录
     */
    @RequestMapping(value = "/deleteHBOrderRecord", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-删除合并记录")
    public String deleteHBOrderRecord(HttpServletRequest request, @RequestParam(value = "id", required = true) int id)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        try
        {
            int status = overseasOrderService.deleteHBOrderRecordById(id);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", status);
            result.put("msg", status == 1 ? "删除成功" : "删除失败");
            return JSON.toJSONString(result);
        }
        catch (Exception e)
        {
            log.error("删除订单合并记录失败", e);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "删除失败");
            return JSON.toJSONString(result);
        }
    }
    
    /**
     * 导出订单合并记录
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportHBOrderRecord")
    @ControllerLog(description = "海外购订单管理-导出订单合并记录")
    public void exportHBOrderRecord(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "sonNumber", required = false, defaultValue = "") String sonNumber,
        @RequestParam(value = "hbNumber", required = false, defaultValue = "") String hbNumber,
        @RequestParam(value = "createTimeBegin", required = false, defaultValue = "") String createTimeBegin,
        @RequestParam(value = "createTimeEnd", required = false, defaultValue = "") String createTimeEnd
        )
    {
        Map<String, Object> para = new HashMap<String, Object>();
        if (!"".equals(sonNumber))
        {
            para.put("sonNumber", "%" + sonNumber + "%");
        }
        if (!"".equals(hbNumber))
        {
            para.put("hbNumber", hbNumber);
        }
        if (!"".equals(createTimeBegin))
        {
            para.put("createTimeBegin", createTimeBegin);
        }
        if (!"".equals(createTimeEnd))
        {
            para.put("createTimeEnd", createTimeEnd);
        }
        OutputStream fOut = null;
        SXSSFWorkbook workbook = null;
        try
        {
            Map<String, Object> result = overseasOrderService.findAllHBOrderRecord(para);
            
            List<Map<String, Object>> reList = (List<Map<String, Object>>)result.get("rows");
            response.setContentType("application/vnd.ms-excel");
            String codedFileName = "合并订单列表";
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode(codedFileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            String[] title = {"添加时间", "合并订单号", "子订单号"};
            workbook = POIUtil.createSXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int count = 0;
            for (int i = 0; i < reList.size(); i++)
            {
                Map<String, Object> currMap = reList.get(i);
                String[] sonNumberArr = (currMap.get("sonNumber") + "").split(";");
                for (int j = 0; j < sonNumberArr.length; j++)
                {
                    count++;
                    Row r = sheet.createRow(i + count);
                    r.createCell(0).setCellValue(currMap.get("createTimeStr") + "");
                    r.createCell(1).setCellValue(currMap.get("hbNumber") + "");
                    r.createCell(2).setCellValue(sonNumberArr[j]);
                    
                }
                count--;
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
                    workbook.dispose();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 确认导入页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/confirmOverseas")
    public ModelAndView confirmOverseas(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("overseas/confirmOverseasOrder");
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
        String codedFileName = "导单模板";
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
            String[] str = {"订单编号", "订单日期", "发货人", "联系电话", "收货人", "身份证号码", "收货地址", "联系电话", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "备注", "保价费"};
            Row row = sheet.createRow(1);
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
     */
    @RequestMapping(value = "/importOverseasOrder", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-确认导单文件")
    public String importOverseasOrder(HttpServletRequest request, @RequestParam("orderFile") MultipartFile file,
        @RequestParam(value = "type", required = false, defaultValue = "0") int type, @RequestParam(value = "sendDate", required = false, defaultValue = "") String sendDate,
        @RequestParam(value = "isRight", required = false, defaultValue = "0") int confirm)
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
                
                if (cell0 != null)
                {
                    cell0.setCellType(Cell.CELL_TYPE_STRING);
                }
                if (cell1 != null)
                {
                    cell1.setCellType(Cell.CELL_TYPE_STRING);
                }
                
                String orderNumber = cell0 == null ? "" : cell0.getStringCellValue();
                String cellSendDate = cell1 == null ? "" : cell1.getStringCellValue();
                
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("iList", iList);
                para.put("orderNumber", orderNumber);
                para.put("cellSendDate", cellSendDate);
                para.put("sendDate", sendDate);
                if (type == 1)
                {// 模拟导入
                    boolean resultBoolean = overseasOrderService.importTest(para);
                    if (!resultBoolean && isRight)
                    {// 模拟导入
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
                else if (type == 3)
                {// 导入
                    overseasOrderService.saveOverseasOrder(para);
                }
                else if (type == 4)
                {
                    // 订单 为已导入才能删除
                    overseasOrderService.deleteOverseasOrderExportRecord(orderNumber);
                }
                
            }
            if (type == 1)
            {
                session.setAttribute("testSendList", iList);
                int canDelete = 1;
                for (Map<String, Object> map2 : iList)
                {
                    if ("成功".equals(map2.get("status") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                    else if ("订单号不已GGJ开头".equals(map2.get("msg") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                    else if ("设置的导单日期与表格内日期不一致".equals(map2.get("msg") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                    else if ("合并订单不存在".equals(map2.get("msg") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                    else if ("订单不存在".equals(map2.get("msg") + ""))
                    {
                        canDelete = 0;
                        break;
                    }
                }
                session.setAttribute("testSendListTime", System.currentTimeMillis());
                map.put("okNum", okNum);
                map.put("failNum", failNum);
                map.put("isRight", isRight ? 1 : 0);
                map.put("canDelete", canDelete);
            }
            else if (type == 3)
            {
                map.put("status", 11);
                map.put("msg", "导入成功");
            }
            else if (type == 4)
            {
                map.put("status", 12);
                map.put("msg", "删除成功");
            }
            
            return JSON.toJSONString(map);
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
     * 下载导单模板
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
            String[] str = {"'导入状态", "说明", "订单号", "表格内导单日期"};
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
                for (int i = 0; i < rightList.size(); i++)
                {
                    Map<String, Object> currMap = rightList.get(i);
                    Row r = sheet.createRow(i + 1);
                    r.createCell(0).setCellValue(currMap.get("status") + "");
                    r.createCell(1).setCellValue(currMap.get("msg") + "");
                    r.createCell(2).setCellValue(currMap.get("orderNumber") + "");
                    r.createCell(3).setCellValue(currMap.get("cellSendDate") + "");
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
     * 批量修改 海外购订单导出状态
     * 
     * @param request
     * @param ids
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/changeExport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerLog(description = "海外购订单管理-批量修改海外购订单导出状态")
    public String changeExport(HttpServletRequest request, @RequestParam(value = "ids", required = true) String ids, @RequestParam(value = "code", required = true) int code// 1:修改为已导出；0：修改为未导出
    )
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        List<Long> idList = new ArrayList<Long>();
        if (ids.indexOf(",") > 0)
        {
            String[] arr = ids.split(",");
            for (String cur : arr)
            {
                idList.add(Long.valueOf(cur));
            }
        }
        else
        {
            idList.add(Long.valueOf(ids));
        }
        para.put("code", code);
        para.put("idList", idList);
        // 结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int resultStatus = overseasOrderService.updateOverseasStatusExport(para);
        if (resultStatus > 0)
        {
            resultMap.put("status", 1);
        }
        else
        {
            resultMap.put("status", 0);
            resultMap.put("msg", "修改失败");
        }
        return JSON.toJSONString(resultMap);
    }
    
}