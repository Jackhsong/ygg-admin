package com.ygg.admin.controller;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.WareHouseEnum;
import com.ygg.admin.dao.ChannelDao;
import com.ygg.admin.dao.ChannelOrderDao;
import com.ygg.admin.dao.ChannelProductManageDao;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.ChannelOrderService;
import com.ygg.admin.util.BeanUtil;
import com.ygg.admin.util.Excel.ExcelExtractor;
import com.ygg.admin.util.Excel.ExcelMaker;
import com.ygg.admin.util.Verifier;
import com.ygg.admin.util.ZipUtil;
import com.ygg.admin.view.channel.BenBridChannelOrderRetView;
import com.ygg.admin.view.channel.ChannelOrderExcelView;
import com.ygg.admin.view.channel.EDBChannelOrderRetView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lorabit
 * @since 16-4-13
 *        <p>
 *        第三方订单管理
 */
@Controller
@RequestMapping("/channelOrder")
public class ChannelOrderController
{
    
    private static Logger logger = Logger.getLogger(ChannelOrderController.class);
    
    @Resource
    private ChannelOrderService channelOrderService;
    
    @Resource
    private ChannelDao channelDao;
    
    @Resource
    private ChannelOrderDao channelOrderDao;

    @Resource
    private ChannelProductManageDao channelProductManageDao;
    
    @RequestMapping("/list")
    public ModelAndView list()
    {
        ModelAndView mv = new ModelAndView("channel/order/list");
        return mv;
    }
    
    @RequestMapping(value = "/jsonChannelInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> jsonChannelInfo(//
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "channelId", required = false, defaultValue = "0") int channelId, //
        @RequestParam(value = "warehouseType", required = false, defaultValue = "0") int warehouseType, @RequestParam(value = "paytimeStart", required = false) String paytimeStart,
        @RequestParam(value = "paytimeEnd", required = false) String paytimeEnd, @RequestParam(value = "receiver", required = false) String receiver,
        @RequestParam(value = "number", required = false) String number, //
        @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "code", required = false) String code,
        @RequestParam(value = "name", required = false) String name)
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
            if (channelId != 0)
            {
                para.put("channelId", channelId);
            }
            if (warehouseType != 0)
            {
                para.put("warehouseType", warehouseType);
            }
            if (StringUtils.isNotEmpty(number))
                para.put("number", number);
            if (StringUtils.isNotEmpty(paytimeStart))
                para.put("paytimeStart", paytimeStart);
            if (StringUtils.isNotEmpty(paytimeEnd))
                para.put("paytimeEnd", paytimeEnd);
            if (StringUtils.isNotEmpty(receiver))
                para.put("receiver", receiver);
            if (StringUtils.isNotEmpty(phone))
                para.put("phone", phone);
            if (StringUtils.isNotEmpty(code))
                para.put("code", code);
            if (StringUtils.isNotEmpty(name))
                para.put("name", name);
            return channelOrderService.jsonChannelOrderInfo(para);
        }
        catch (Exception e)
        {
            logger.error("异步加载渠道订单列表出错", e);
            Map<String, Object> result = new HashMap<>();
            result.put("totals", 0);
            result.put("rows", new ArrayList<>());
            return result;
        }
    }
    
    @RequestMapping("/import")
    public ModelAndView importView()
    {
        ModelAndView mv = new ModelAndView("channel/order/import");
        return mv;
    }
    
    @RequestMapping("/importChannelOrder")
    @ControllerLog(description = "第三方订单管理-导入订单")
    public void importChannelOrder(@RequestParam(value = "channel_order_file", required = true) MultipartFile file, HttpServletResponse response)
        throws IOException
    {
        OutputStream out = response.getOutputStream();
        try
        {
            List<ChannelOrderExcelView> channelOrders = ExcelExtractor.extractFrom(file, ChannelOrderExcelView.class, ChannelOrderExcelView.headerMapper);
//            checkRequiredCol(channelOrders);
            covertToImportEntity(channelOrders);
            checkIlleagel(channelOrders);
            channelOrderService.importChannelOrders(channelOrders);
            // 根据商品仓库分类
            List<EDBChannelOrderRetView> edbOrders = new ArrayList<>();
            List<BenBridChannelOrderRetView> benBridOrders = new ArrayList<>();
            List<BenBridChannelOrderRetView> japanOrders = new ArrayList<>();
            for (ChannelOrderExcelView order : channelOrders)
            {
                WareHouseEnum warehouse = WareHouseEnum.of(order.getWarehouseType());
                if (warehouse == null)
                    continue;
                if (warehouse.getId() == WareHouseEnum.HZWH.getId())
                {
                    EDBChannelOrderRetView edb = BeanUtil.copyProperties(order, EDBChannelOrderRetView.class, new String[] {"code", "count"});
                    edb.setReceiveAddress(order.getProvince() + " " + order.getCity() + " " + order.getDistrict() + " " + order.getDetail_address());
                    List<String> codeAndCount = Splitter.on("%").splitToList(order.getCode());
                    edb.setCode(codeAndCount.get(0));
                    if (codeAndCount.size() > 1)
                    {
                        edb.setCount(Integer.valueOf(codeAndCount.get(1)) * order.getCount());
                    }
                    else
                    {
                        edb.setCount(order.getCount());
                    }
                    edbOrders.add(edb);
                }
                else if (warehouse.getId() == WareHouseEnum.BirWH.getId())
                {
                    BenBridChannelOrderRetView bird = BeanUtil.copyProperties(order, BenBridChannelOrderRetView.class);
                    bird.setSend_warehouse(warehouse.getDisplayInRetExcel());
                    bird.setServer_way(warehouse.getServerWay());
                    List<String> codeAndCount = Splitter.on("%").splitToList(order.getCode());
                    bird.setCode(codeAndCount.get(0));
                    if (codeAndCount.size() > 1)
                    {
                        bird.setCount(Integer.valueOf(codeAndCount.get(1)) * order.getCount());
                    }
                    else
                    {
                        bird.setCount(order.getCount());
                    }
                    benBridOrders.add(bird);
                }
                else if (warehouse.getId() == WareHouseEnum.JAPANWH.getId())
                {
                    BenBridChannelOrderRetView bird = BeanUtil.copyProperties(order, BenBridChannelOrderRetView.class);
                    bird.setSend_warehouse(warehouse.getDisplayInRetExcel());
                    bird.setServer_way(warehouse.getServerWay());
                    List<String> codeAndCount = Splitter.on("%").splitToList(order.getCode());
                    bird.setCode(codeAndCount.get(0));
                    if (codeAndCount.size() > 1)
                    {
                        bird.setCount(Integer.valueOf(codeAndCount.get(1)) * order.getCount());
                    }
                    else
                    {
                        bird.setCount(order.getCount());
                    }
                    japanOrders.add(bird);
                }
            }
            List<File> zipFiles = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(edbOrders))
            {
                File edb = ExcelMaker.from(edbOrders, EDBChannelOrderRetView.getHeaders())
                    .displayHeaders(EDBChannelOrderRetView.getDisplayHeaders())
                    .create(edbOrders.get(0).getChannel() + "-E店宝.xls");
                zipFiles.add(edb);
            }
            if (CollectionUtils.isNotEmpty(benBridOrders))
            {
                File bird = ExcelMaker.from(benBridOrders, BenBridChannelOrderRetView.getHeaders())
                    .displayHeaders(BenBridChannelOrderRetView.getDisplayHeaders())
                    .create(benBridOrders.get(0).getChannel() + "-笨鸟.xls");
                zipFiles.add(bird);
            }
            if (CollectionUtils.isNotEmpty(japanOrders))
            {
                File japan = ExcelMaker.from(japanOrders, BenBridChannelOrderRetView.getHeaders())
                    .displayHeaders(BenBridChannelOrderRetView.getDisplayHeaders())
                    .create(japanOrders.get(0).getChannel() + "-日本埼玉仓库.xls");
                zipFiles.add(japan);
            }
            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            if (zipFiles.size() == 1)
            {
                String name = URLEncoder.encode(zipFiles.get(0).getName().substring(0, zipFiles.get(0).getName().indexOf(".xls")), "UTF-8");
                response.setHeader("Content-Disposition", String.format("attachment;filename=%s", name + ".xls"));
                OutputStream os = response.getOutputStream();
                InputStream is = new FileInputStream(zipFiles.get(0));
                ByteStreams.copy(is, os);
                is.close();
                os.flush();
                for (File f : zipFiles)
                {
                    f.delete();
                }
                os.close();
            }
            else
            {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("导出订单", "UTF-8") + ".zip");
                File zip = ZipUtil.zipFilesByAnt("导出订单.zip", zipFiles);
                OutputStream os = response.getOutputStream();
                InputStream is = new FileInputStream(zip);
                ByteStreams.copy(is, os);
                is.close();
                os.flush();
                for (File f : zipFiles)
                {
                    f.delete();
                }
                zip.delete();
                os.close();
            }
        }
        catch (IllegalArgumentException ie)
        {
            logger.error("导入第三方订单出错", ie);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            String msg = ie.getMessage();
            if (StringUtils.isNotEmpty(msg))
            {
                msg = msg.substring(0, msg.length() > 60 ? 60 : msg.length());
            }
            String errorStr = "<script>alert(' 数据有误 " + msg + " ');window.history.back();</script>";
            if (out == null)
                out = response.getOutputStream();
            out.write(errorStr.getBytes());
            out.flush();
        }
        catch (Exception e)
        {
            logger.error("导入第三方订单出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            String errorStr = "<script>alert('系统出错 重试或者联系开发');window.history.back();</script>";
            if (out == null)
                out = response.getOutputStream();
            out.write(errorStr.getBytes());
            out.flush();
        }
        finally
        {
            if (out != null)
                out.close();
        }
    }

    @RequestMapping("/importChannelOrderCheck")
    @ControllerLog(description = "第三方订单管理-导入订单检查")
    @ResponseBody
    public ResultEntity importChannelOrderCheck(@RequestParam(value = "channel_order_file", required = true) MultipartFile file, HttpServletResponse response)
        throws IOException
    {
        try
        {
            List<ChannelOrderExcelView> channelOrders = ExcelExtractor.extractFrom(file, ChannelOrderExcelView.class, ChannelOrderExcelView.headerMapper);
            String errMsg = checkRequiredCol(channelOrders);
            if (StringUtils.isNotEmpty(errMsg)) {
                return  ResultEntity.getFailResult(errMsg);
            }
            covertToImportEntity(channelOrders);
            errMsg = checkIlleagel(channelOrders);
            if (StringUtils.isNotEmpty(errMsg)) {
                return  ResultEntity.getFailResult(errMsg);
            }
            return ResultEntity.getSuccessResult();
        }
        catch (IllegalArgumentException ie)
        {
            logger.error("导入第三方订单检查格式出错", ie);
            return ResultEntity.getFailResult("格式错误 或者 请重试 " + ie.getMessage());
        }
        catch (Exception e)
        {
            logger.error("导入第三方订单出错", e);
            return ResultEntity.getFailResult("出错 重试或者联系开发" + e.getMessage());
        }
    }
    
    @RequestMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
        throws IOException
    {
        OutputStream os = null;
        HSSFWorkbook workbook = null;
        try
        {
            response.setCharacterEncoding(Charsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("第三方订单模板", "UTF-8") + ".xls");
            os = response.getOutputStream();
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);
            
            HSSFCellStyle cellStyleRed = workbook.createCellStyle();
            HSSFCellStyle cellStyleBlue = workbook.createCellStyle();
            Font fontRed = workbook.createFont();
            fontRed.setColor(HSSFColor.RED.index);
            Font fontBlue = workbook.createFont();
            fontBlue.setColor(HSSFColor.BLUE.index);
            cellStyleRed.setFont(fontRed);
            cellStyleBlue.setFont(fontBlue);
            int i = 0;
            List<Integer> redIndex = Lists.newArrayList(0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13, 14, 15, 15, 17, 31);
            List<Integer> blueIndex = Lists.newArrayList(6, 28, 29);
            for (String key : Lists.newArrayList(ChannelOrderExcelView.headerMapper.keySet()))
            {
                Cell cell = row.createCell(i);
                if (redIndex.contains(i))
                {
                    cell.setCellStyle(cellStyleRed);
                }
                else if (blueIndex.contains(i))
                {
                    cell.setCellStyle(cellStyleBlue);
                }
                cell.setCellValue(key);
                i++;
            }
            workbook.write(os);
            os.flush();
        }
        catch (Exception e)
        {
            logger.error("下载第三方导入订单模板", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            String errorStr = "<script>alert('系统出错 重试或者联系开发');window.history.back();</script>";
            if (os == null)
                os = response.getOutputStream();
            os.write(errorStr.getBytes());
            os.flush();
        }
        finally
        {
            if (workbook != null)
                workbook.close();
            if (os != null)
                os.close();
        }
    }
    
    @RequestMapping("/export")
    @ControllerLog(description = "第三方订单列表-导出订单")
    public void exportChannelOrder(HttpServletResponse response, @RequestParam(value = "page", required = false, defaultValue = "1") int page, //
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, //
        @RequestParam(value = "channelId", required = false, defaultValue = "0") int channelId, //
        @RequestParam(value = "warehouseType", required = false, defaultValue = "0") int warehouseType, @RequestParam(value = "paytimeStart", required = false) String paytimeStart,
        @RequestParam(value = "paytimeEnd", required = false) String paytimeEnd, @RequestParam(value = "receiver", required = false) String receiver,
        @RequestParam(value = "number", required = false) String number, //
        @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "code", required = false) String code,
        @RequestParam(value = "name", required = false) String name)
            throws IOException
    {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("订单列表", "UTF-8") + ".xls");
        try (OutputStream fOut = response.getOutputStream())
        {
            Map<String, Object> para = new HashMap<>();
            if (channelId != 0)
            {
                para.put("channelId", channelId);
            }
            if (warehouseType != 0)
            {
                para.put("warehouseType", warehouseType);
            }
            if (StringUtils.isNotEmpty(number))
                para.put("number", number);
            if (StringUtils.isNotEmpty(paytimeStart))
                para.put("paytimeStart", paytimeStart);
            if (StringUtils.isNotEmpty(paytimeEnd))
                para.put("paytimeEnd", paytimeEnd);
            if (StringUtils.isNotEmpty(receiver))
                para.put("receiver", receiver);
            if (StringUtils.isNotEmpty(phone))
                para.put("phone", phone);
            if (StringUtils.isNotEmpty(code))
                para.put("code", code);
            if (StringUtils.isNotEmpty(name))
                para.put("name", name);
            List<ChannelOrderExcelView> orders = channelOrderService.getChannelOrderAllInfo(para);
            List<String> displayHeaders = Lists.newArrayList(ChannelOrderExcelView.headerMapper.keySet());
            List<String> headers = Lists.newArrayList(ChannelOrderExcelView.headerMapper.values());
            ExcelMaker.from(orders, headers).displayHeaders(displayHeaders).writeTo(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            logger.error("异步加载渠道订单列表出错", e);
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("content-disposition", "");
            String errorStr = "<script>alert('系统出错');window.history.back();</script>";
            response.getOutputStream().write(errorStr.getBytes());
            response.getOutputStream().flush();
        }
    }
    
    private void covertToImportEntity(List<ChannelOrderExcelView> channelOrders)
    {
        for (ChannelOrderExcelView order : channelOrders)
        {
            if (order.getOrderStatus() != null)
            {
                OrderEnum.ORDER_STATUS status = OrderEnum.ORDER_STATUS.getOrderStatusEnum(order.getOrderStatus().trim());
                if (status != null)
                    order.setStatus(status.getCode());
            }
            if (order.getPayCompany() != null)
            {
                OrderEnum.PAY_CHANNEL payChannel = OrderEnum.PAY_CHANNEL.getEnumByDesc(order.getPayCompany().trim());
                if (payChannel != null)
                    order.setPay_channel(payChannel.getCode());
            }
            if (order.getIsFreezeString() != null)
            {
                order.setIs_freeeze(isCheck(order.getIsFreezeString()));
            }
//            if(order.getCode().indexOf("%") > -1){
//                List<String> codeAndId = Splitter.on("%").splitToList(order.getCode());
//                Integer assembleCount = Integer.valueOf(codeAndId.get(1));
//                order.setCode(codeAndId.get(0));
//                order.setCount(order.getCount() * assembleCount);
//            }
        }
    }
    
    public Integer isCheck(String s)
    {
        if (StringUtils.isNotEmpty(s))
        {
            if (s.trim().equalsIgnoreCase("Y"))
                return 1;
        }
        return 0;
    }

    private String checkRequiredCol(List<ChannelOrderExcelView> channelOrders) throws Exception {
        int excelLineNum = 2;
        StringBuilder errMsg = new StringBuilder();
        if(CollectionUtils.isEmpty(channelOrders)){
            return "请填写数据!";
        }
        for (ChannelOrderExcelView order : channelOrders) {
            Verifier verifier = new Verifier();
            verifier.isTrue(StringUtils.isNotEmpty(order.getChannel()), " 渠道必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getNumber()), " 订单编号必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getOrderStatus()), " 订单状态必填");
            if (StringUtils.isNotEmpty(order.getOrderStatus())) {
                OrderEnum.ORDER_STATUS status = OrderEnum.ORDER_STATUS.getOrderStatusEnum(order.getOrderStatus().trim());
                verifier.isTrue(status != null, " 订单状态只能为填" + OrderEnum.ORDER_STATUS.getDescString());
            }
            verifier.isTrue(StringUtils.isNotEmpty(order.getOrder_create_time())
                    && !order.getOrder_create_time().equals("0000-00-00 00:00:00"), " 创建时间必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getOrder_pay_time())
                    && !order.getOrder_pay_time().equals("0000-00-00 00:00:00"), " 付款时间必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getReceiver()), " 收货人必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getProvince()), " 省必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getCity()), " 市必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getDistrict()), " 区必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getDetail_address()), " 详细地址必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getPhone()), " 联系电话必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getCode()), " 商品编号必填");
            verifier.isTrue(StringUtils.isNotEmpty(order.getProductName()), " 商品名称必填");
            verifier.isTrue(order.getCount() > 0, " 件数必填大于0");
            verifier.isTrue(order.getProduct_total_price() >= 0f, " 总价必填大于等于0");
            verifier.isTrue(order.getOrder_real_price() >= 0f, " 总金额必填大于等于0");
            try {
                verifier.throwIfError("导入文件第" + excelLineNum + "行数数据: ");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("\n");
            }finally {
                excelLineNum++ ;
            }
        }
        return errMsg.toString();
    }

    private String checkIlleagel(List<ChannelOrderExcelView> channelOrders)
        throws Exception
    {
        StringBuilder errMsg = new StringBuilder();
        int excelLineNum = 2;
        for (ChannelOrderExcelView order : channelOrders)
        {
            Verifier verifier = new Verifier();

            Map para = new HashMap();
            para.put("name", order.getChannel());
            // 判断渠道是否存在
            List<Map<String, Object>> channels = channelDao.findAllChannel(para);
            verifier.isTrue(CollectionUtils.isNotEmpty(channels), "渠道：" + order.getChannel() + " 不存在");
            long channelId = (Long)channels.get(0).get("id");
            para.put("code", order.getCode());
            para.put("channel_id", channelId);
            // 判断商品是否存在
            List<Map<String, Object>> products = channelProductManageDao.findChannelProductByPara(para);
            verifier.isTrue(CollectionUtils.isNotEmpty(products), "渠道:" + order.getChannel() + " 商品编码:" + order.getCode() + "商品不存在");
            // 设置相关商品Id 信息。。
            if(CollectionUtils.isNotEmpty(products)){
                order.setProduct_id((Long)products.get(0).get("id"));
                order.setWarehouseType((Integer)products.get(0).get("warehouse_type"));
                order.setChannel_id(channelId);
            }
            try {
                verifier.throwIfError("导入文件第" + excelLineNum + "行数数据: ");
            } catch (Exception e) {
                errMsg.append(e.getMessage()).append("\n");
            }finally {
                excelLineNum++ ;
            }
        }
        return errMsg.toString();
    }


    
}
