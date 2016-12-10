package com.ygg.admin.controller;

import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.service.OrderService;
import com.ygg.admin.util.CSVUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.POIUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tools")
public class ToolsController
{
    private static Logger log = Logger.getLogger(ToolsController.class);
    
    @Resource
    private OrderService orderService;
    
    /**
     * 外部订单工具
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/orderToolsIndex")
    public ModelAndView orderToolsIndex()
        throws Exception
    {
        ModelAndView mv = new ModelAndView("tools/orderToolsIndex");
        return mv;
    }
    
    /**
     * 贝贝网订单合并for笨鸟
     * 
     * @throws Exception
     *             
     */
    @RequestMapping(value = "/orderHBFromBeiBeiForBirdex", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "订单工具-订单合并-贝贝网for笨鸟")
    public void orderHBFromBeiBeiForBirdex(@RequestParam(value = "orderFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String fileName = "贝贝网转笨鸟订单_" + DateTimeUtil.now("yyyy_MM_dd_HH_mm_ss");
        String[] exportTitle = {"关联单号*", "所属仓库*", "管道代码*", "发货人", "收货人名*", "身份证号码*", "是否进行身份验证", "身份证正面", "身份证反面", "联系方式*", "收货省份*", "收货城市*", "收货地区*", "收货联系地址*", "邮政编码", "商品编号*",
            "商品名称*", "数量*", "品牌", "规格", "价格*", "单位*", "支付公司", "支付时间", "支付流水号", "增值服务（以，隔开）"};
        String[] exportTitle2 = {"客户单号", "发货仓库", "服务方式", "商品编码", "数量", "申报价值", "申报价值单位", "收货人姓名", "手机号码", "固定电话", "所在省份", "所在城市", "所在区域", "详细地址", "邮政编码", "身份证号码", "是否进行身份证验证",
            "身份证正面URL", "身份证反面URL", "支付公司", "支付时间", "支付流水号", "支付金额", "支付金额单位", "是否购买保险", "是否包装加固", "发货人", "订单备注", "第三方单号"};
        Workbook workbook = null;
        OutputStream fOut = null;
        String errorMsg = "";
        try
        {
            String orderFileName = file.getOriginalFilename();
            Map<String, Object> fileDate = null;
            if (orderFileName.endsWith("xls") || orderFileName.endsWith("xlsx"))
            {
                fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            }
            else if (orderFileName.endsWith("csv"))
            {
                fileDate = CSVUtil.getCVSData(file.getInputStream(), true);
            }
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                workbook = orderService.saveOrderHBFromBeiBeiForBirdex(data, exportTitle2);
            }
            else
            {
                workbook = POIUtil.createHSSFWorkbookTemplate(exportTitle);
                Sheet sheet = workbook.getSheetAt(0);
                Row r = sheet.createRow(1);
                r.createCell(0).setCellValue("表格为空或不是excel文件");
            }
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("贝贝网订单转笨鸟订单失败！orderHBFromBeiBeiForBirdex", e);
            workbook = POIUtil.createHSSFWorkbookTemplate(exportTitle);
            Sheet sheet = workbook.getSheetAt(0);
            Row r = sheet.createRow(1);
            if ("".equals(errorMsg))
            {
                errorMsg = "贝贝网订单转笨鸟订单失败";
            }
            r.createCell(0).setCellValue(errorMsg);
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
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
     * 贝贝网订单发货导出
     *
     * @throws Exception
     *             
     */
    @RequestMapping(value = "/orderSendGoodsFromBirdexForBeiBei", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "订单工具-发货文件导出-笨鸟for贝贝网")
    public void orderSendGoodsFromBirdexForBeiBei(@RequestParam(value = "logisticsFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String fileName = "笨鸟转贝贝物流文件_" + DateTimeUtil.now("yyyy_MM_dd_HH_mm_ss");
        String[] exportTitle = {"订单号", "快递单号", "快递类型"};
        Workbook workbook = null;
        OutputStream fOut = null;
        String errorMsg = "";
        try
        {
            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                workbook = orderService.saveOrderSendGoodsFromBirdexForBeiBei(data, exportTitle);
            }
            else
            {
                workbook = POIUtil.createHSSFWorkbookTemplate(exportTitle);
                Sheet sheet = workbook.getSheetAt(0);
                Row r = sheet.createRow(1);
                r.createCell(0).setCellValue("表格为空！");
            }
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("笨鸟转贝贝物流文件失败！orderSendGoodsFromBirdexForBeiBei", e);
            workbook = POIUtil.createHSSFWorkbookTemplate(exportTitle);
            Sheet sheet = workbook.getSheetAt(0);
            Row r = sheet.createRow(1);
            if ("".equals(errorMsg))
            {
                errorMsg = "发货文件导出-笨鸟for贝贝网失败";
            }
            r.createCell(0).setCellValue(errorMsg);
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
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
     * 根据订单号导出发货文件
     *
     * @throws Exception
     *
     */
    @RequestMapping(value = "/exportSendGoodsInfoByNumbers", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ControllerLog(description = "订单工具-根据订单号导出发货文件")
    public void exportSendGoodsInfoByNumbers(@RequestParam(value = "orderNumberFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String fileName = "发货文件_" + DateTimeUtil.now("yyyy_MM_dd_HH_mm_ss");
        String[] exportTitle =
            {"订单编号", "订单日期", "付款时间", "支付交易号", "支付企业名称", "发货人", "联系电话", "收货人", "身份证号码", "省", "市", "区", "详细收货地址", "联系电话", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "备注", "保价费",
                "用户名","单价2","总价2"};
        Workbook workbook = null;
        OutputStream fOut = null;
        String errorMsg = "";
        try
        {
            Map<String, Object> fileDate = POIUtil.getSheetDataAtIndex(file.getInputStream(), 0, true);
            if (fileDate != null)
            {
                List<Map<String, Object>> data = (List<Map<String, Object>>)fileDate.get("data");
                workbook = orderService.exportSendGoodsInfoByNumbers(data, exportTitle);
            }
            else
            {
                workbook = POIUtil.createHSSFWorkbookTemplate(exportTitle);
                Sheet sheet = workbook.getSheetAt(0);
                Row r = sheet.createRow(1);
                r.createCell(0).setCellValue("表格为空！");
            }
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        }
        catch (Exception e)
        {
            log.error("根据订单号导出发货文件失败！", e);
            workbook = POIUtil.createHSSFWorkbookTemplate(exportTitle);
            Sheet sheet = workbook.getSheetAt(0);
            Row r = sheet.createRow(1);
            if ("".equals(errorMsg))
            {
                errorMsg = "根据订单号导出发货文件失败！";
            }
            r.createCell(0).setCellValue(errorMsg);
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
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
