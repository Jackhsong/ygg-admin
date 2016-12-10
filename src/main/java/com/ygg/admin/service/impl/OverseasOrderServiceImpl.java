package com.ygg.admin.service.impl;

import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.*;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.OverseasOrderInfoForManage;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.OverseasOrderService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.FavoriteUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.ygg.admin.dao.ProductDao;

@Service("overseasOrderService")
public class OverseasOrderServiceImpl implements OverseasOrderService
{
    /** 临时缓存 */
    private Map<String, Object> tempCache = new HashMap<>();
    
    Logger log = Logger.getLogger(OverseasOrderServiceImpl.class);
    
    @Resource
    private OverseasOrderDao overseasOrderDao;
    
    //    @Resource
    //    private ProductDao productDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource(name = "orderDao")
    private OrderDao orderDao;

    @Resource
    private IndexSettingDao indexSettingDao;

    @Override
    public Map<String, Object> jsonOverseasOrder(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        Integer operaStatus = (Integer)para.get("operaStatus");
        if (operaStatus != null && operaStatus == 1)
        {// 已导出
            List<OverseasOrderInfoForManage> reList = overseasOrderDao.findAllOverseasOrderWithExport(para);
            int total = 0;
            if (reList.size() > 0)
            {
                total = overseasOrderDao.countAllOverseasOrderWithExport(para);
            }
            result.put("rows", reList);
            result.put("total", total);
        }
        else
        {
            if (operaStatus != null)
            {// 未导出
                para.put("notExport", 1);
            }
            List<OverseasOrderInfoForManage> reList = overseasOrderDao.findAllOverseasOrder(para);
            int total = 0;
            if (reList.size() > 0)
            {
                total = overseasOrderDao.countAllOverseasOrder(para);
            }
            result.put("rows", reList);
            result.put("total", total);
        }
        return result;
    }
    
    @Override
    public int deletePro()
        throws Exception
    {
        // 先删除 海外购商品信息表 中的 状态为待添加的 记录
        overseasOrderDao.deleteOverseasProByStatusEqualsZero();
        return 1;
    }
    
    @Override
    public int deleteIdCardByStatusEqualsZero()
        throws Exception
    {
        // 先删除 带添加的 真实姓名与身份证号映射信息
        overseasOrderDao.deleteIdcardRealnameMappingByStatusEqualsZero();
        return 1;
    }
    
    @Override
    public boolean checkOrderExportPriceAndName()
        throws Exception
    {
        List<Map<String, Object>> reList = overseasOrderDao.findAllWithOutExportInfo();
        if (!FavoriteUtil.isUseful(reList))
        {
            // 即将导出的海外购商品都有导出名和导出价格
            return true;
        }
        
        for (Map<String, Object> map : reList)
        {
            // 逐个 将缺少 海外购信息的商品 写入海外购商品信息表
            if (!FavoriteUtil.isUseful(overseasOrderDao.findOverseasProductInfoByProductCode(map.get("pCode") + "", map.get("sellerName") + "")))
            {
                Map<String, Object> para = new HashMap<>();
                para.put("code", map.get("pCode") + "");
                para.put("name", map.get("pName") + "");
                para.put("exportName", "");
                para.put("exportPrice", 0);
                para.put("status", 0);
                para.put("remark", "");
                para.put("orderNumber", map.get("oNumber") + "");
                para.put("sellerName", map.get("sellerName") + "");
                String sendAddress = map.get("sendAddress") + "(" + map.get("warehouse") + ")";
                para.put("sendAddress", sendAddress);
                overseasOrderDao.insertOverseasProInfo(para);
            }
        }
        return false;
    }
    
    @Override
    public String overseasAllCanExport(Date lastDate, boolean isBigPrice)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("lastDate", CommonUtil.date2String(lastDate, "yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> reList = overseasOrderDao.findAll(map);
        try
        {
            if (reList.size() > 0)
            {
                // 组合
                File file = groupOrder(reList, isBigPrice);
                tempCache.clear();
                return file.getAbsolutePath();
            }
            return null;
        }
        catch (Exception e)
        {
            log.error("导出所有可导海外购订单失败", e);
            return null;
        }
    }
    
    /**
     * 过滤并按 (商家+发货地 eg: 左岸城堡_广州保税仓_2015-4-7_18_32_22.xls） 分组订单
     * 
     * @param isBigPrice 查出总价大于2000元的订单
     * @return
     * @throws Exception
     */
    private File groupOrder(List<Map<String, Object>> reList, boolean isBigPrice)
        throws Exception
    {

        List<Integer> un500SellerIdList = new ArrayList<>();
        Map<String, Object> sPara = new HashMap<>();
        sPara.put("key", "not_display_unionpay_sellerid");
        List<Map<String, Object>> value = indexSettingDao.findSetting(sPara);
        if (value.size() > 0)
        {
            String sellers = value.get(0).get("value") + "";
            String[] arr = sellers.split(",");
            for (String s : arr)
            {
                if (StringUtils.isNumeric(s))
                {
                    un500SellerIdList.add(Integer.valueOf(s));
                }
            }
        }

        // ------------begin-----------------按导出价格重新计算订单金额---------------------------------------------------------------------
        
        Map<String, Integer> orderPriceMappping = new HashMap<>();
        for (Map<String, Object> currMap : reList)
        {
            String orderId = currMap.get("oId") + "";
            Integer totalMoney = orderPriceMappping.get(orderId);
            if (totalMoney == null)
            {
                totalMoney = 0;
            }
            Integer productCount = Integer.valueOf(currMap.get("productCount") + "");
            Integer exportPrice = Integer.valueOf(currMap.get("exportPrice") + "");
            String code = currMap.get("code") + "";
            
            int index = code.lastIndexOf("%");
            if ((index > -1) && (index < code.length() - 1))
            {
                // 有效
                String num = code.substring(index + 1);
                if (StringUtils.isNumeric(num))
                {
                    productCount = productCount * Integer.valueOf(num);
                }
            }
            totalMoney += (productCount * exportPrice);
            
            orderPriceMappping.put(orderId, totalMoney);
        }
        for (Map<String, Object> currMap : reList)
        {
            String orderId = currMap.get("oId") + "";
            currMap.put("totalPrice", orderPriceMappping.get(orderId));
        }
        
        // ------------end-----------------按导出价格重新计算订单金额---------------------------------------------------------------------
        
        String nowStr = DateTime.now().toString("yyyy-MM-dd_HH_mm_ss");
        Map<String, Object> groupOrderMap = new HashMap<>();
        // ------------begin-----------------将订单按商家分组---------------------------------------------------------------------
        for (Map<String, Object> currMap : reList)
        {
            String groupKey = currMap.get("sellerName") + "_" + currMap.get("sendAddress") + "_" + currMap.get("sellerId");// 此key后面需作为excel名字
            List<Map<String, Object>> groupValue = (List<Map<String, Object>>)groupOrderMap.get(groupKey);
            if (groupValue == null)
            {
                groupValue = new ArrayList<>();
                groupOrderMap.put(groupKey, groupValue);
            }
            groupValue.add(currMap);
        }
        // ------------end-----------------将订单按商家分组-----------------------------------------------------------------------
        
        // ------------begin-------写入xls----------遍历groupOrderMap数据，判断条件，加入到resultMap
        // ， 该删的删，该改的改-------------------
        File fileDir = new File(YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
        File newDir = new File(fileDir, nowStr + "_" + new Random().nextInt(10000) + "oversons");
        newDir.mkdir();
        for (Map.Entry<String, Object> entry : groupOrderMap.entrySet())
        {
            String ke = entry.getKey();
            List<Map<String, Object>> val = (List<Map<String, Object>>)entry.getValue();
            // 筛选
            boolean isToHb_no = ke.contains("_199");
            List<Map<String, Object>> newVal = validateItemList(val, !isToHb_no, un500SellerIdList, isBigPrice);
            // 将newVal按 warehouse 分仓 分组
            Map<String, Object> groupByNewKeyMap = new HashMap<>();
            for (Map<String, Object> map : newVal)
            {
                String key = ke + "_" + map.get("warehouse") + "_" + nowStr;
                List<Map<String, Object>> groupValue = (List<Map<String, Object>>)groupByNewKeyMap.get(key);
                if (groupValue == null)
                {
                    groupValue = new ArrayList<>();
                    groupByNewKeyMap.put(key, groupValue);
                }
                groupValue.add(map);
            }
            for (Map.Entry<String, Object> entry2 : groupByNewKeyMap.entrySet())
            {
                String ke2 = entry2.getKey();
                List<Map<String, Object>> val2 = (List<Map<String, Object>>)entry2.getValue();
                if(isBigPrice) {
                    writeToExcel(newDir, "(2000)"+ke2 + ".xls", val2);
                } else {
                    writeToExcel(newDir, ke2 + ".xls", val2);
                }
            }
        }
        // ------------end-------写入xls-----------遍历groupOrderMap数据，判断条件，加入到resultMap
        // ，该删的删，该改的改---------------------
        
        return newDir;
    }
    
    Map<String, Map<String, String>> findPayTid(List<Map<String, Object>> data)
        throws Exception
    {
        Map<String, Map<String, String>> payPidMap = new HashMap<>();
        List<Long> orderAliPay = new ArrayList<>();
        List<Long> orderUnionPay = new ArrayList<>();
        List<Long> orderWeixinPay = new ArrayList<>();
        for (Map<String, Object> it : data)
        {
            int payChannel = Integer.parseInt(it.get("payChannel") + "");
            if (payChannel == OrderEnum.PAY_CHANNEL.UNION.getCode())
            {
                orderUnionPay.add(Long.valueOf(it.get("oId") + ""));
            }
            else if (payChannel == OrderEnum.PAY_CHANNEL.ALIPAY.getCode())
            {
                orderAliPay.add(Long.valueOf(it.get("oId") + ""));
            }
            else if (payChannel == OrderEnum.PAY_CHANNEL.WEIXIN.getCode())
            {
                orderWeixinPay.add(Long.valueOf(it.get("oId") + ""));
            }
        }
        if (orderUnionPay.size() > 0)
        {
            Map<String, Object> unionPara = new HashMap<>();
            unionPara.put("idList", orderUnionPay);
            List<Map<String, Object>> rel = overseasOrderDao.findOrderUnionPay(unionPara);
            for (Map<String, Object> it : rel)
            {
                String orderId = it.get("orderId") + "";
                if (payPidMap.get(orderId) == null)
                {
                    Map<String, String> map = new HashMap<>();
                    map.put("payTid", it.get("payTid") + "");
                    map.put("payMark", it.get("payMark") + "");
                    map.put("payChannel", OrderEnum.PAY_CHANNEL.UNION.getDesc());
                    payPidMap.put(orderId, map);
                }
            }
        }
        if (orderWeixinPay.size() > 0)
        {
            Map<String, Object> weixinPara = new HashMap<>();
            weixinPara.put("idList", orderWeixinPay);
            List<Map<String, Object>> rel = overseasOrderDao.findOrderWeixinPay(weixinPara);
            for (Map<String, Object> it : rel)
            {
                String orderId = it.get("orderId") + "";
                if (payPidMap.get(orderId) == null)
                {
                    Map<String, String> map = new HashMap<>();
                    map.put("payTid", it.get("payTid") + "");
                    map.put("payMark", it.get("payMark") + "");
                    map.put("payChannel", OrderEnum.PAY_CHANNEL.WEIXIN.getDesc());
                    payPidMap.put(orderId, map);
                }
            }
        }
        if (orderAliPay.size() > 0)
        {
            Map<String, Object> aliPara = new HashMap<>();
            aliPara.put("idList", orderAliPay);
            List<Map<String, Object>> rel = overseasOrderDao.findOrderAliPay(aliPara);
            for (Map<String, Object> it : rel)
            {
                String orderId = it.get("orderId") + "";
                if (payPidMap.get(orderId) == null)
                {
                    Map<String, String> map = new HashMap<>();
                    map.put("payTid", it.get("payTid") + "");
                    map.put("payMark", it.get("payMark") + "");
                    map.put("payChannel", OrderEnum.PAY_CHANNEL.ALIPAY.getDesc());
                    payPidMap.put(orderId, map);
                }
            }
        }
        return payPidMap;
    }
    
    Map<String, String> findHBOrderPayInfoByHBNumber(String hb)
        throws Exception
    {
        String exportPayId = "";
        String exportChannel = "";
        String sonNumber = orderDao.findHBOrderByParentNumber(hb);
        String[] sons = sonNumber.split(";");
        for (String it : sons)
        {
            OrderEntity order = orderDao.findOrderByNumber(it);
            if (order.getPayChannel() == OrderEnum.PAY_CHANNEL.ALIPAY.getCode())
            {
                String payId = orderDao.findPayTidOrderAliPay(order.getId());
                if (payId != null && !"".equals(payId))
                {
                    exportPayId += (it + ":" + payId + ";");
                }
                else
                {
                    exportPayId += (it + ": 系统查下不到;");
                }
                exportChannel += "支付宝;";
            }
            else if (order.getPayChannel() == OrderEnum.PAY_CHANNEL.WEIXIN.getCode())
            {
                String payId = orderDao.findPayTidOrderWeixinPay(order.getId());
                if (payId != null && !"".equals(payId))
                {
                    exportPayId += (it + ":" + payId + ";");
                }
                else
                {
                    exportPayId += (it + ": 系统查下不到;");
                }
                exportChannel += "微信;";
            }
            else
            {
                String payId = orderDao.findPayTidOrderUnionPay(order.getId());
                if (payId != null && !"".equals(payId))
                {
                    exportPayId += (it + ":" + payId + ";");
                }
                else
                {
                    exportPayId += (it + ": 系统查下不到;");
                }
                exportChannel += "银联;";
            }
        }
        Map<String, String> result = new HashMap<>();
        result.put("exportPayId", exportPayId);
        result.put("exportChannel", exportChannel);
        return result;
    }
    
    /**
     * 将数据写入到硬盘中
     *
     * @param data
     * @throws Exception
     */
    private void writeToExcel(File dir, String fileName, List<Map<String, Object>> data)
        throws Exception
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            //查询交易号
            Map<String, Map<String, String>> payPidMap = findPayTid(data);
            String nowDate = DateTime.now().toString("yyyy-MM-dd 00:00:00");
            Date nowDateD = CommonUtil.string2Date(nowDate, "yyyy-MM-dd HH:mm:ss");
            workbook = new HSSFWorkbook();
            CreationHelper creationHelper = workbook.getCreationHelper();
            HSSFSheet sheet = workbook.createSheet();
            CellStyle cellStyleDate = workbook.createCellStyle();
            cellStyleDate.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
            String[] str =
                {"订单编号", "订单日期", "付款时间", "支付交易号", "支付企业名称", "发货人", "联系电话", "收货人", "身份证号码", "省", "市", "区", "详细收货地址", "联系电话", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "备注", "保价费",
                    "用户名", "单价2", "总价2", "平台支付号"};
            Row row = sheet.createRow(1);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            // 在最终生成的导单文件中，如果不同订单不同身份证号有相同收货地址，相同地址合计导单总金额超过2000元，则将这些地址，按不同的订单，分别加上（1）
            // （2） （3）这样的后缀
            for (int i = 0; i < data.size(); i++)
            {
                Map<String, Object> currMap = data.get(i);
                String orderId = currMap.get("oId") + "";
                String oNumber = currMap.get("oNumber") + "";
                int orderType = Integer.parseInt(currMap.get("orderType").toString());
                
                //支付信息
                Map<String, String> payInfo = payPidMap.get(orderId);
                String excelPayTid = "";
                String excelPayMark = "";
                String excelPayChannel = "";
                //                if (!oNumber.startsWith("GGJ"))
                //                {
                if (payInfo != null && payInfo.get("payTid") != null && !"".equals(payInfo.get("payTid")))
                {
                    excelPayMark = payInfo.get("payMark") + "";
                    excelPayTid = payInfo.get("payTid") + "";
                    excelPayChannel = payInfo.get("payChannel") + "";
                }
                else
                {
                    excelPayTid = "系统中不存在。";
                    excelPayMark = "";
                    excelPayChannel = "";
                }
                //                }
                //                else
                //                {
                //                    Map<String,String> rr = findHBOrderPayInfoByHBNumber(oNumber);
                //                    if(rr != null){
                //                        excelPayTid = rr.get("exportPayId")+"";
                //                        excelPayChannel = rr.get("exportChannel")+"";
                //                    }
                //                }
                
                Date payTime =
                    CommonUtil.string2Date((currMap.get("payTime") == null || "".equals(currMap.get("payTime"))) ? "0000-00-00 00:00:00.000"
                        : ((Timestamp)currMap.get("payTime")).toString(), "yyyy-MM-dd HH:mm:ss.SSS");
                
                //订单实际售卖数量和售价
                //                Integer realProductCount = Integer.parseInt(currMap.get("realProductCount") == null ? "0" : currMap.get("realProductCount")+"");
                double realSalesPrice = Double.parseDouble(currMap.get("realSalesPrice") == null ? "0.0" : currMap.get("realSalesPrice") + "");
                
                String exportCode = currMap.get("code") + "";
                String exportName = currMap.get("exportName") + "";
                String exportPirce = currMap.get("exportPrice") + "";
                // ------end-----导出名称
                
                // ------begin-----价格相关
                int count = Integer.valueOf(currMap.get("productCount") + "");
                double price = Integer.valueOf(exportPirce) / 100.0;
                double totalPrice = price * count;
                // ------end-----价格相关
                
                if (!oNumber.startsWith("GGJ"))
                {
                    oNumber = "GGJ" + oNumber;
                }
                
                // ------begin-----收货地址
                String province = currMap.get("province") + "";
                String city = currMap.get("city") + "";
                String district = currMap.get("district") + "";
                String detailAddress = currMap.get("detailAddress") + "";
                Map<String, Map<String, Object>> addressMap = new HashMap<>();
                String[] address = getReceiveAddress(detailAddress, province, city, district, addressMap, oNumber);
                // ------end-----收货地址
                int cellIndex = 0;
                Row r = sheet.createRow(i + 2);
                r.createCell(cellIndex++).setCellValue(oNumber);
                Cell c1 = r.createCell(cellIndex++);
                c1.setCellValue(nowDateD);
                c1.setCellStyle(cellStyleDate);
                Cell c2 = r.createCell(cellIndex++);
                c2.setCellValue(payTime);//付款时间
                c2.setCellStyle(cellStyleDate);
                r.createCell(cellIndex++).setCellValue(excelPayTid);
                r.createCell(cellIndex++).setCellValue(excelPayChannel);
                r.createCell(cellIndex++).setCellValue(OrderEnum.ORDER_TYPE.getDescByCode(orderType));
                r.createCell(cellIndex++).setCellValue("0571-86888702");
                r.createCell(cellIndex++).setCellValue(currMap.get("fullName") + "");
                r.createCell(cellIndex++).setCellValue((currMap.get("idCard") + "").replaceAll("x", "X"));
                r.createCell(cellIndex++).setCellValue(address[1]);
                r.createCell(cellIndex++).setCellValue(address[2]);
                r.createCell(cellIndex++).setCellValue(address[3]);
                r.createCell(cellIndex++).setCellValue(address[0]);
                r.createCell(cellIndex++).setCellValue(currMap.get("mobileNumber") + "");
                r.createCell(cellIndex++).setCellValue(exportCode);
                r.createCell(cellIndex++).setCellValue(exportName);
                r.createCell(cellIndex++).setCellValue(count);
                r.createCell(cellIndex++).setCellValue(new BigDecimal(price).doubleValue());
                r.createCell(cellIndex++).setCellValue(new BigDecimal(totalPrice).doubleValue());
                r.createCell(cellIndex++).setCellValue(0);
                r.createCell(cellIndex++).setCellValue(OrderEnum.ORDER_TYPE.getDescByCode(orderType));
                r.createCell(cellIndex++).setCellValue(0);
                r.createCell(cellIndex++).setCellValue(currMap.get("accountName") + "");
                r.createCell(cellIndex++).setCellValue(new BigDecimal(realSalesPrice).doubleValue());
                r.createCell(cellIndex++).setCellValue(new BigDecimal(realSalesPrice * count).doubleValue());
                r.createCell(cellIndex++).setCellValue(excelPayMark);
            }
            fileName = fileName.replaceAll("/", "_");
            File file = new File(dir, fileName);
            file.createNewFile();
            fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception e)
        {
            throw new Exception(e);
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
    
    private String[] getReceiveAddress(String detail, String province, String city, String district, Map<String, Map<String, Object>> addressMap, String key)
        throws Exception
    {
        String[] addressArr = new String[4];
        try
        {
            String provinceName = areaDao.findProvinceNameByProvinceId(Integer.valueOf(province));
            addressArr[1] = provinceName;
            String cityName = areaDao.findCityNameByCityId(Integer.valueOf(city));
            addressArr[2] = cityName;
            String districtName = areaDao.findDistrictNameByDistrictId(Integer.valueOf(district));
            addressArr[3] = districtName;
            String address = provinceName + cityName + districtName + detail;
            Map<String, Object> sameAddressMap = addressMap.get(address);
            if (sameAddressMap == null)
            {
                String newAddress = address + "(1)";
                sameAddressMap = new HashMap<>();
                sameAddressMap.put(key, newAddress);
                sameAddressMap.put("num", 1);// 相同地址下的订单数量
                addressMap.put(address, sameAddressMap);
                addressArr[0] = newAddress;
                return addressArr;
            }
            String newAddress = (String)sameAddressMap.get(key);
            if (newAddress != null)
            {
                addressArr[0] = newAddress;
                return addressArr;
            }
            // 放入
            Integer num = (Integer)sameAddressMap.get("num") + 1;
            newAddress = address + "(" + num + ")";
            sameAddressMap.put(key, newAddress);
            sameAddressMap.put("num", num);// 相同地址下的订单数量
            addressArr[0] = newAddress;
            return addressArr;
        }
        catch (Exception e)
        {
            log.error("更新地址出错", e);
            String provinceName = areaDao.findProvinceNameByProvinceId(Integer.valueOf(province));
            String cityName = areaDao.findCityNameByCityId(Integer.valueOf(city));
            String districtName = areaDao.findDistrictNameByDistrictId(Integer.valueOf(district));
            String address = provinceName + cityName + districtName + detail;
            addressArr[0] = address;
            addressArr[1] = provinceName;
            addressArr[2] = cityName;
            addressArr[3] = districtName;
            return addressArr;
        }
    }
    
    /**
     * 对海外购订单进行相关条件判断，归并操作,返回有效可导出的订单list
     * 
     * @param reList 某个商家下的所有订单
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> validateItemList(List<Map<String, Object>> reList, boolean isToHb, List<Integer> un500SellerIdList, boolean isBigPrice)
        throws Exception
    {
        // key为身份证号，value为该身份证下所有订单 被包装的orderInfo list
        Map<String, List<Map<String, Object>>> canExportMap = new HashMap<>();
        // --------begin--------按人分组，并做相关约束（例如，一个人每天不得超过2000）--------------------------
        for (Map<String, Object> map : reList)
        {
            List<Map<String, Object>> personalList = canExportMap.get(map.get("idCard") + "");
            if (personalList == null)
            {
                personalList = new ArrayList<>();
                canExportMap.put(map.get("idCard") + "", personalList);
            }
            groupSamleOrder(personalList, map, un500SellerIdList, isBigPrice);
        }
        // --------end--------按人分组，并做相关约束（例如，一个人每天不得超过2000，根据身份证号查找姓名。收货人姓名不可以包含"小姐"、"女士"、"先生"...）--------
        
        List<Map<String, Object>> canExportList = new ArrayList<>();
        // 合并订单记录，最后写入合并订单记录表中
        List<Map<String, Object>> hbOrderList = new ArrayList<>();
        List<String> hbNumberList = new ArrayList<>();
        
        // ------begin-------合并订单，商品编码末尾包含%n;订单号的开头，全部加上GGJ------------
        for (Map.Entry<String, List<Map<String, Object>>> entry : canExportMap.entrySet())
        {
            String idCard = entry.getKey();
            List<Map<String, Object>> personalList = entry.getValue();// 这里的map是经过组装后的map，
            if (personalList.size() > 1 && isToHb)
            {
                // 超过两个订单，按身份证号和收货地址合并 分组 ，合并订单，订单号为GGJHB +
                // 一串自动生成的数字，并将合并订单的记录写到 导出时合并订单记录 ；
                
                // 先对personalList 按 收货地址合并 +
                // 商家ID分组（前面有一步是按商家（name+sendAddress）来分组的，存在商家ID不同，但是前两者都相关的情况）
                // 分组
                // 这里为什么要在address 后面 再加上sellerId 分组 ? 因为后面导出的时候还要按照 分仓 来分组。故这里再按sellerId分下组，就能保证下面合并的订单一定是 sellerName +
                // sendAddress + 分仓 唯一
                Map<String, List<Map<String, Object>>> groupByAddress = new HashMap<>();
                for (Map<String, Object> currMap : personalList)
                {
                    String addressIdAndSellerId = currMap.get("address") + "-" + currMap.get("sellerId");
                    List<Map<String, Object>> currList = groupByAddress.get(addressIdAndSellerId);
                    if (currList == null)
                    {
                        currList = new ArrayList<>();
                        groupByAddress.put(addressIdAndSellerId, currList);
                    }
                    currList.add(currMap);
                }
                // 对按addressId & sellerId 分组后的数据进行订单合并及相关操作
                for (Map.Entry<String, List<Map<String, Object>>> e : groupByAddress.entrySet())
                {
                    List<Map<String, Object>> newV = e.getValue();
                    
                    if (newV.size() > 1)
                    { // 大于1时，需要合并
                      // begin 记录 合并订单
                        Map<String, Object> currmap = new HashMap<>();
                        List<String> numberList = new ArrayList<>();
                        String newNumber = "GGJHB" + new Random().nextInt(100) + System.currentTimeMillis() + new Random().nextInt(100);
                        while (true)
                        {
                            if (!hbNumberList.contains(newNumber))
                            {
                                break;
                            }
                            newNumber = "GGJHB" + new Random().nextInt(100) + System.currentTimeMillis() + new Random().nextInt(100);
                        }
                        hbNumberList.add(newNumber);
                        currmap.put("newNumber", newNumber);
                        currmap.put("numberList", numberList);
                        hbOrderList.add(currmap);
                        // end 记录 合并订单
                        
                        // key 为 p.code 用来叠加
                        Map<String, List<Map<String, Object>>> willAddList = new HashMap<>();
                        for (Map<String, Object> map : newV)
                        {
                            numberList.add(map.get("number") + "");// 记录合并订单，后面用来插入
                            List<Map<String, Object>> itemMapList = (List<Map<String, Object>>)map.get("itemMapList");
                            for (Map<String, Object> itemMap : itemMapList)
                            {
                                String code = itemMap.get("code") + "";
                                
                                int index = code.lastIndexOf("%");
                                if ((index > -1) && (index < code.length() - 1))
                                {
                                    // 有效
                                    String num = code.substring(index + 1);
                                    if (StringUtils.isNumeric(num))
                                    {
                                        // 数量*num
                                        Integer productCount = Integer.valueOf(itemMap.get("productCount") + "");
                                        double realSalesPrice = Double.parseDouble(itemMap.get("realSalesPrice") == null ? "0.0" : itemMap.get("realSalesPrice") + "");
                                        productCount = productCount * Integer.valueOf(num);
                                        itemMap.put("productCount", productCount);
                                        itemMap.put("code", code.substring(0, index));
                                        itemMap.put("realSalesPrice", realSalesPrice / Integer.valueOf(num));
                                    }
                                }
                                
                                itemMap.put("oNumber", newNumber);
                                // canExportList.add(itemMap);
                                List<Map<String, Object>> codeList = willAddList.get(code);
                                if (codeList == null)
                                {
                                    codeList = new ArrayList<>();
                                    willAddList.put(code, codeList);
                                }
                                codeList.add(itemMap);
                            }
                        }
                        // 将list<itemMap> 叠加 按相同商品编码来
                        for (Map.Entry<String, List<Map<String, Object>>> en : willAddList.entrySet())
                        {
                            List<Map<String, Object>> enValue = en.getValue();
                            Map<String, Object> oneMap = enValue.get(0);
                            int productCount = 0;
                            for (Map<String, Object> map : enValue)
                            {
                                productCount += Integer.valueOf(map.get("productCount") + "");
                            }
                            // 这里会导致oneMap 的totalPrice 不准确了，，，但是之后已经不会用到了，所有就不管了
                            // !!!!!!
                            oneMap.put("productCount", productCount);
                            canExportList.add(oneMap);
                        }
                    }
                    else
                    {// 只有一个，不需要合并操作
                        for (Map<String, Object> map : newV)
                        {
                            List<Map<String, Object>> itemMapList = (List<Map<String, Object>>)map.get("itemMapList");
                            for (Map<String, Object> itemMap : itemMapList)
                            {
                                String code = itemMap.get("code") + "";
                                int index = code.lastIndexOf("%");
                                if ((index > -1) && (index < code.length() - 1))
                                {
                                    // 有效
                                    String num = code.substring(index + 1);
                                    if (StringUtils.isNumeric(num))
                                    {
                                        // 数量*num
                                        Integer productCount = Integer.valueOf(itemMap.get("productCount") + "");
                                        productCount = productCount * Integer.valueOf(num);
                                        itemMap.put("productCount", productCount);
                                        itemMap.put("code", code.substring(0, index));
                                        
                                        double realSalesPrice = Double.parseDouble(itemMap.get("realSalesPrice") == null ? "0.0" : itemMap.get("realSalesPrice") + "");
                                        itemMap.put("realSalesPrice", realSalesPrice / Integer.valueOf(num));
                                    }
                                }
                                canExportList.add(itemMap);
                            }
                        }
                    }
                }
            }
            else
            {
                for (Map<String, Object> map : personalList)
                {
                    List<Map<String, Object>> itemMapList = (List<Map<String, Object>>)map.get("itemMapList");
                    for (Map<String, Object> itemMap : itemMapList)
                    {
                        String code = itemMap.get("code") + "";
                        
                        // int index = code.lastIndexOf("%2");
                        // if (index > -1)
                        // {
                        // // 数量*2
                        // Integer productCount =
                        // Integer.valueOf(itemMap.get("productCount") + "");
                        // itemMap.put("productCount", productCount * 2);
                        // itemMap.put("code", code.substring(0, index));
                        // }
                        
                        int index = code.lastIndexOf("%");
                        if ((index > -1) && (index < code.length() - 1))
                        {
                            // 有效
                            String num = code.substring(index + 1);
                            if (StringUtils.isNumeric(num))
                            {
                                // 数量*num
                                Integer productCount = Integer.valueOf(itemMap.get("productCount") + "");
                                productCount = productCount * Integer.valueOf(num);
                                itemMap.put("productCount", productCount);
                                itemMap.put("code", code.substring(0, index));
                                double realSalesPrice = Double.parseDouble(itemMap.get("realSalesPrice") == null ? "0.0" : itemMap.get("realSalesPrice") + "");
                                itemMap.put("realSalesPrice", realSalesPrice / Integer.valueOf(num));
                            }
                        }
                        
                        canExportList.add(itemMap);
                    }
                }
            }
        }
        
        // 将数据今日导出记录写入到数据库中
        for (Map.Entry<String, List<Map<String, Object>>> entry : canExportMap.entrySet())
        {
            String idCard = entry.getKey();// 身份证号
            List<Map<String, Object>> personalList = entry.getValue();
            
            if (personalList.size() > 1)
            {
                for (Map<String, Object> currMap : personalList)
                {
                    Map<String, Object> para = new HashMap<>();
                    para.put("idCard", idCard);
                    para.put("exportTime", DateTime.now().toString("yyyyMMdd"));
                    para.put("sellerId", currMap.get("sellerId") + "");
                    para.put("orderId", currMap.get("orderId") + "");
                    para.put("price", new BigDecimal(currMap.get("totalPrice") + "").intValue());
                    if (!FavoriteUtil.isUseful(overseasOrderDao.findOverseasBuyerRecordByIdCard(para)))
                    {
                        overseasOrderDao.insertOverseasBuyerRecord(para);
                    }
                }
            }
            else if (personalList.size() == 1)
            {
                // 只插一条即可
                Map<String, Object> currMap = personalList.get(0);
                Map<String, Object> para = new HashMap<>();
                para.put("idCard", idCard);
                para.put("exportTime", DateTime.now().toString("yyyyMMdd"));
                para.put("sellerId", currMap.get("sellerId") + "");
                para.put("orderId", currMap.get("orderId") + "");
                para.put("price", new BigDecimal(currMap.get("totalPrice") + "").intValue());
                if (!FavoriteUtil.isUseful(overseasOrderDao.findOverseasBuyerRecordByIdCard(para)))
                {
                    overseasOrderDao.insertOverseasBuyerRecord(para);
                }
            }
            
        }
        
        // 将合并订单记录写入到合并订单记录表中
        for (Map<String, Object> map : hbOrderList)
        {
            String newNumber = map.get("newNumber") + "";
            Map<String, Object> para = new HashMap<>();
            para.put("hbNumber", newNumber);
            String sonNumber = "";
            List<String> numberList = (List<String>)map.get("numberList");
            for (String number : numberList)
            {
                sonNumber += number + ";";
            }
            para.put("sonNumber", sonNumber);
            overseasOrderDao.insertHBOrderRecord(para);
        }
        return canExportList;
    }
    
    /**
     * 同一个用户下的订单分组处理
     * 
     * @param personalList
     * @param itemMap
     */
    private Map<String, Object> groupSamleOrder(List<Map<String, Object>> personalList, Map<String, Object> itemMap, List<Integer> un500SellerIdList, boolean isBigPrice)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        // 订单编号
        String number = itemMap.get("oNumber") + "";
        // 收货地址
        String address = "" + itemMap.get("district") + itemMap.get("detailAddress");
        // number 对应的 在personalList的Map
        Map<String, Object> orderMap = null;
        for (Map<String, Object> currMap : personalList)
        {
            if (number.equals(currMap.get("number") + ""))
            {
                orderMap = currMap;
                break;
            }
        }
        
        // 新增
        if (orderMap == null)
        {
            // 订单总价，处理成以分为单位
            Integer totalPrice = Integer.valueOf(itemMap.get("totalPrice") + "");

            Integer sellerId = Integer.valueOf(itemMap.get("sellerId") + "");
            if (!un500SellerIdList.contains(sellerId))
            {
                if (isBigPrice) {  //导出订单总价大于2000的订单  2000以上要特殊处理
                    if (totalPrice <= 200000)
                    {
                        return result;
                    }
                } else {
                    // 计算 个人今日已导（数据库中） + personalList中的 + 当前订单总额
                    int exportTotalMoney = totalPrice(personalList, itemMap);
                    if (exportTotalMoney > 200000)
                    {
                        return result;
                    }
                }
            }
            if (isBigPrice) {  //导出订单总价大于2000的订单
                if (totalPrice <= 200000)
                {
                    return result;
                }
            }

            // 根据身份证号查找姓名。
            // 替换姓名
            String mappingName = getIdCardMappingName(itemMap.get("idCard") + "");
            if (!"".equals(mappingName))
            {
                itemMap.put("fullName", mappingName);
            }
            else
            {
                // 收货人姓名不可以包含"小姐"、"女士"、"先生"
                String fullName = itemMap.get("fullName") + "";
                if (!CommonUtil.validateReceiveName(fullName))
                {
                    // 将插入数据库的idCard存起来，防止未提交事务导致多次插入引起错误
                    List<String> tempIdCardList = (List<String>)tempCache.get("insertIdcardRealnameMappingIdCards");
                    if (tempIdCardList == null)
                    {
                        tempIdCardList = new ArrayList<>();
                        tempCache.put("insertIdcardRealnameMappingIdCards", tempIdCardList);
                    }
                    
                    if (!tempIdCardList.contains(itemMap.get("idCard") + ""))
                    {
                        // 验证失败
                        if (!FavoriteUtil.isUseful(overseasOrderDao.findIdcardRealnameMappingByIdCard(itemMap.get("idCard") + "")))
                        {
                            tempIdCardList.add(itemMap.get("idCard") + "");
                            Map<String, Object> map = new HashMap<>();
                            map.put("idCard", itemMap.get("idCard"));
                            map.put("fullName", itemMap.get("fullName"));
                            map.put("orderNumber", itemMap.get("oNumber"));
                            map.put("sellerName", itemMap.get("sellerName"));
                            map.put("sendAddress", itemMap.get("sendAddress") + "(" + itemMap.get("warehouse") + ")");
                            map.put("status", 0);
                            overseasOrderDao.insertIdcardRealnameMapping(map);
                        }
                    }
                    itemMap.put("fullName", fullName + "(姓名非法)");
                }
            }
            orderMap = new HashMap<>();
            orderMap.put("number", number);
            orderMap.put("address", address);
            orderMap.put("totalPrice", String.valueOf(totalPrice));
            orderMap.put("sellerId", itemMap.get("sellerId") + "");
            orderMap.put("orderId", itemMap.get("oId") + "");
            List<Map<String, Object>> itemMapList = new ArrayList<>();
            itemMapList.add(itemMap);
            orderMap.put("itemMapList", itemMapList);
            personalList.add(orderMap);
        }
        else
        {
            List<Map<String, Object>> itemMapList = (List<Map<String, Object>>)orderMap.get("itemMapList");
            itemMap.put("fullName", itemMapList.get(0).get("fullName"));
            itemMapList.add(itemMap);
        }
        return result;
    }
    
    /** 计算当日导出总价格 */
    private int totalPrice(List<Map<String, Object>> personalList, Map<String, Object> itemMap)
        throws Exception
    {
        String idCard = itemMap.get("idCard") + "";
        String sellerId = itemMap.get("sellerId") + "";
        String dateStr = DateTime.now().toString("yyyyMMdd");
        
        // key为orderId，value为改订单对于总价
        Map<String, Object> map = new HashMap<>();
        Integer exportPrice = Integer.valueOf(itemMap.get("totalPrice") + "");
        map.put(itemMap.get("oId") + "", exportPrice);
        for (Map<String, Object> currMap : personalList)
        {
            // 过滤掉了重复orderId
            exportPrice = Integer.valueOf(currMap.get("totalPrice") + "");
            map.put(currMap.get("orderId") + "", exportPrice);
        }
        
        Map<String, Object> para = new HashMap<>();
        para.put("idCard", idCard);
        para.put("sellerId", Integer.valueOf(sellerId));
        para.put("exportTime", dateStr);
        List<Map<String, Object>> result = overseasOrderDao.findOverseasBuyerRecordByIdCard(para);
        if (result.size() > 0)
        {
            for (Map<String, Object> currMap : result)
            {
                // 一样，这里能保证 和 上面的去重
                map.put(currMap.get("orderId") + "", currMap.get("price"));
            }
        }
        Integer totalPrice = 0;
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            Integer cuPrice = Integer.valueOf(entry.getValue() + "");
            totalPrice += cuPrice;
        }
        return totalPrice;
    }
    
    /**
     * 根据身份证名字映射表取得身份证对应的姓名
     * 
     * @param idCard
     * @return
     * @throws Exception
     */
    private String getIdCardMappingName(String idCard)
        throws Exception
    {
        Map<String, String> idCardNameMappingMap = (Map<String, String>)tempCache.get("idCardNameMapping");
        if (idCardNameMappingMap == null)
        {
            Map<String, String> cacheMapping = new HashMap<>();
            Map<String, Object> para = new HashMap<>();
            para.put("status", 1);
            List<Map<String, Object>> reList = overseasOrderDao.findAllIdcardRealnameMapping(para);
            for (Map<String, Object> map : reList)
            {
                cacheMapping.put(map.get("idCard") + "", map.get("realName") + "");
            }
            tempCache.put("idCardNameMapping", cacheMapping);
            idCardNameMappingMap = cacheMapping;
        }
        if (idCardNameMappingMap.containsKey(idCard))
        {
            return idCardNameMappingMap.get(idCard) + "";
        }
        // 临时缓存中没有，查找数据库
        Map<String, Object> para = new HashMap<>();
        para.put("status", 1);
        para.put("idCard", idCard);
        List<Map<String, Object>> reList = overseasOrderDao.findAllIdcardRealnameMapping(para);
        if (reList.size() > 0)
        {
            String realName = reList.get(0).get("realName") + "";
            idCardNameMappingMap.put(idCard, realName);
            return realName;
        }
        return "";
    }
    
    @Override
    public Map findOverseasProductInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = overseasOrderDao.findAllOverseasProductInfo(para);
        for (Map<String, Object> map : reList)
        {
            Integer exportPrice = Integer.valueOf(map.get("exportPrice") + "");
            map.put("exportPrice", exportPrice / 100.0);
            if ("1".equals(map.get("status") + ""))
            {
                // 已完善
                map.put("statusMsg", "OK");
                map.put("statusMsgForExport", "OK");
                map.put("orderNumber", "");
            }
            else
            {
                map.put("statusMsg", "<span style=\"color:red\">待添加</span>");
                map.put("statusMsgForExport", "待添加");
            }
            
        }
        Map<String, Object> map = new HashMap<>();
        map.put("rows", reList);
        int total = 0;
        if (reList.size() > 1)
        {
            total = overseasOrderDao.countAllOverseasProductInfo(para);
        }
        map.put("total", total);
        return map;
    }
    
    @Override
    public int insertOrUpdateOverseasProductInfo(Map<String, Object> para)
        throws Exception
    {
        try
        {
            String code = para.get("code") + "";
            
            int status = 0;
            para.put("status", 1);
            if (para.get("id") != null)
            {
                status = overseasOrderDao.updateOverseasProInfoForYY(para);
            }
            else
            {
                int sellerId = Integer.parseInt(para.get("sellerId") + "");
                SellerEntity seller = sellerDao.findSellerSimpleById(sellerId);
                if (seller == null)
                {
                    return 0;
                }
                Map<String, Object> re = overseasOrderDao.findOverseasProductInfoByProductCode(code, seller.getRealSellerName());
                if (!FavoriteUtil.isUseful(re))
                {
                    para.put("sellerName", seller.getRealSellerName());
                    para.put("sendAddress", seller.getSendAddress() + "(" + seller.getWarehouse() + ")");
                    para.put("orderNumber", "");
                    status = overseasOrderDao.insertOverseasProInfo(para);
                }
                else
                {
                    // 修改
                    para.put("id", re.get("id"));
                    status = overseasOrderDao.updateOverseasProInfoForYY(para);
                }
            }
            return status;
        }
        catch (Exception e)
        {
            log.error("保存导单名称失败", e);
            return 0;
        }
        
    }
    
    @Override
    public int deleteOverseasProductInfoById(int id)
        throws Exception
    {
        return overseasOrderDao.deleteOverseasProductInfoById(id);
    }
    
    @Override
    public Map<String, Object> jsonIdCardMapping(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reList = overseasOrderDao.findAllIdcardRealnameMapping(para);
        int total = 0;
        if (reList.size() > 0)
        {
            for (Map<String, Object> currMap : reList)
            {
                Integer status = Integer.valueOf(currMap.get("status") + "");
                currMap.put("statusMsg", status == 1 ? "OK" : "<span style=\"color:red\">待添加真名</span>");
                currMap.put("statusMsgForExcel", status == 1 ? "OK" : "待添加真名");
                currMap.put("updateTime", DateTimeUtil.timestampStringToWebString(currMap.get("updateTime") + ""));
                if (status == 1)
                {
                    currMap.put("orderNumber", "");
                    currMap.put("sellerName", "");
                    currMap.put("sendAddress", "");
                }
                currMap.put("sourceDesc", Integer.valueOf(currMap.get("source") + "") == 0 ? "系统" : "用户");
            }
            total = overseasOrderDao.countAllIdcardRealnameMapping(para);
        }
        map.put("rows", reList);
        map.put("total", total);
        return map;
    }
    
    @Override
    public int deleteIdcardRealnameMappingById(int id)
        throws Exception
    {
        try
        {
            int status = overseasOrderDao.deleteIdcardRealnameMappingById(id);
            return status;
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    @Override
    public int insertOrUpdateIdcardRealnameMapping(Map<String, Object> para)
        throws Exception
    {
        try
        {
            Integer id = (Integer)para.get("id");
            int status = 0;
            if (id == null)
            {
                // 新增
                if (!FavoriteUtil.isUseful(overseasOrderDao.findIdcardRealnameMappingByIdCard(para.get("idCard") + "")))
                {
                    Map<String, Object> map = new HashMap<>();
                    para.put("status", 1);
                    status = overseasOrderDao.insertIdcardRealnameMappingForYY(para);
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                // 更新
                para.put("status", 1);
                status = overseasOrderDao.updateIdcardRealnameMapping(para);
            }
            return status;
        }
        catch (Exception e)
        {
            log.error("插入身份证对应真实姓名出错", e);
            return 0;
        }
        
    }
    
    @Override
    public Map<String, Object> findAllHBOrderRecord(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> reList = overseasOrderDao.findAllHBOrderRecord(para);
        int total = 0;
        if (reList.size() > 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map<String, Object> currMap : reList)
            {
                currMap.put("createTimeStr", sdf.format((Timestamp)currMap.get("createTime")));
            }
            total = overseasOrderDao.countAllHBOrderRecord(para);
        }
        map.put("rows", reList);
        map.put("total", total);
        return map;
    }
    
    @Override
    public int deleteHBOrderRecordById(int id)
        throws Exception
    {
        return overseasOrderDao.deleteHBOrderRecordById(id);
    }
    
    @Override
    public boolean importTest(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> iList = (List<Map<String, Object>>)para.get("iList");
        Map<String, Object> sendStatusMap = new HashMap<>();
        String orderNumber = para.get("orderNumber") + "";
        String cellSendDate = para.get("cellSendDate") + "";
        String sendDate = para.get("sendDate") + "";
        if (!orderNumber.startsWith("GGJHB") && !orderNumber.startsWith("GGJ"))
        {
            sendStatusMap.put("status", "失败");
            sendStatusMap.put("msg", "订单号不已GGJ开头");
            sendStatusMap.put("orderNumber", orderNumber);
            sendStatusMap.put("cellSendDate", cellSendDate);
            iList.add(sendStatusMap);
            return false;
        }
        else
        {
            /**
             * 1 订单号不存在 2 订单已导入 3 设置的导单日期与表格内日期不一致
             */
            if (!cellSendDate.equals(sendDate))
            {
                sendStatusMap.put("status", "失败");
                sendStatusMap.put("msg", "设置的导单日期与表格内日期不一致");
                sendStatusMap.put("orderNumber", orderNumber);
                sendStatusMap.put("cellSendDate", cellSendDate);
                iList.add(sendStatusMap);
                return false;
            }
            
            if (orderNumber.startsWith("GGJHB"))
            {
                
                Map<String, Object> para1 = new HashMap<>();
                para1.put("hbNumber", orderNumber);
                List<Map<String, Object>> re = overseasOrderDao.findAllHBOrderRecord(para1);
                if (re.size() < 1)
                {
                    sendStatusMap.put("status", "失败");
                    sendStatusMap.put("msg", "合并订单不存在");
                    sendStatusMap.put("orderNumber", orderNumber);
                    sendStatusMap.put("cellSendDate", cellSendDate);
                    iList.add(sendStatusMap);
                    return false;
                }
                String sonNumber = re.get(0).get("sonNumber") + "";
                if (overseasOrderDao.findOverseasOrderExportRecordByNumber((sonNumber.split(";")[0])) != null)
                {
                    sendStatusMap.put("status", "失败");
                    sendStatusMap.put("msg", "订单已导入");
                    sendStatusMap.put("orderNumber", orderNumber);
                    sendStatusMap.put("cellSendDate", cellSendDate);
                    iList.add(sendStatusMap);
                    return false;
                }
                sendStatusMap.put("status", "成功");
                sendStatusMap.put("msg", "ok");
                sendStatusMap.put("orderNumber", orderNumber);
                sendStatusMap.put("cellSendDate", cellSendDate);
                iList.add(sendStatusMap);
                return true;
            }
            else if (orderNumber.startsWith("GGJ"))
            {
                // 查询订单信息
                OrderEntity order = orderDao.findOrderByNumber(orderNumber.substring(3));
                if (order == null)
                {
                    sendStatusMap.put("status", "失败");
                    sendStatusMap.put("msg", "订单不存在");
                    sendStatusMap.put("orderNumber", orderNumber);
                    sendStatusMap.put("cellSendDate", cellSendDate);
                    iList.add(sendStatusMap);
                    return false;
                }
                if (overseasOrderDao.findOverseasOrderExportRecordByNumber(order.getNumber()) != null)
                {
                    sendStatusMap.put("status", "失败");
                    sendStatusMap.put("msg", "订单已导入");
                    sendStatusMap.put("orderNumber", orderNumber);
                    sendStatusMap.put("cellSendDate", cellSendDate);
                    iList.add(sendStatusMap);
                    return false;
                }
                sendStatusMap.put("status", "成功");
                sendStatusMap.put("msg", "ok");
                sendStatusMap.put("orderNumber", orderNumber);
                sendStatusMap.put("cellSendDate", cellSendDate);
                iList.add(sendStatusMap);
                return true;
            }
            else
            {
                sendStatusMap.put("status", "失败");
                sendStatusMap.put("msg", "未知错误");
                sendStatusMap.put("orderNumber", orderNumber);
                sendStatusMap.put("cellSendDate", cellSendDate);
                iList.add(sendStatusMap);
                return false;
            }
        }
    }
    
    @Override
    public boolean saveOverseasOrder(Map<String, Object> para)
        throws Exception
    {
        String orderNumber = para.get("orderNumber") + "";
        String cellSendDate = para.get("cellSendDate") + "";
        if (orderNumber.startsWith("GGJHB"))
        {
            // 从合并订单中找出子订单
            Map<String, Object> map = new HashMap<>();
            map.put("hbNumber", orderNumber);
            List<Map<String, Object>> reList = overseasOrderDao.findAllHBOrderRecord(map);
            String sonNumber = reList.get(0).get("sonNumber") + "";
            String[] numbers = sonNumber.split(";");
            for (String n : numbers)
            {
                if (!FavoriteUtil.isUseful(overseasOrderDao.findOverseasOrderExportRecordByNumber(n)))
                {
                    Map<String, Object> cmap = new HashMap<>();
                    cmap.put("number", Long.valueOf(n));
                    cmap.put("exportTime", cellSendDate);
                    overseasOrderDao.insertOverseasOrderExportRecord(cmap);
                }
            }
        }
        else if (orderNumber.startsWith("GGJ"))
        {
            String order_number = orderNumber.substring(3);
            if (!FavoriteUtil.isUseful(overseasOrderDao.findOverseasOrderExportRecordByNumber(order_number)))
            {
                Map<String, Object> cmap = new HashMap<>();
                cmap.put("number", Long.valueOf(order_number));
                cmap.put("exportTime", cellSendDate);
                overseasOrderDao.insertOverseasOrderExportRecord(cmap);
            }
        }
        return true;
    }
    
    @Override
    public int deleteOverseasOrderExportRecord(String orderNumber)
        throws Exception
    {
        if (orderNumber.startsWith("GGJHB"))
        {
            // 从合并订单中找出子订单
            Map<String, Object> map = new HashMap<>();
            map.put("hbNumber", orderNumber);
            List<Map<String, Object>> reList = overseasOrderDao.findAllHBOrderRecord(map);
            String sonNumber = reList.get(0).get("sonNumber") + "";
            String[] numbers = sonNumber.split(";");
            for (String n : numbers)
            {
                overseasOrderDao.deleteOverseasOrderExportRecord(Long.valueOf(n));
            }
        }
        else if (orderNumber.startsWith("GGJ"))
        {
            String order_number = orderNumber.substring(3);
            overseasOrderDao.deleteOverseasOrderExportRecord(Long.valueOf(order_number));
        }
        return 1;
    }
    
    @Override
    public int deleteOverseasBuyerRecord()
        throws Exception
    {
        return overseasOrderDao.deleteOverseasBuyerRecord();
    }
    
    @Override
    public int updateOverseasStatusExport(Map<String, Object> para)
        throws Exception
    {
        List<Long> idList = (List<Long>)para.get("idList");
        Integer code = (Integer)para.get("code");
        if (code == 0)
        {// 修改为未导出状态
            for (Long number : idList)
            {
                overseasOrderDao.deleteOverseasOrderExportRecord(number);
            }
        }
        else if (code == 1)
        {// 修改为已导出状态
            for (Long number : idList)
            {
                Map<String, Object> cmap = new HashMap<>();
                cmap.put("number", number);
                cmap.put("exportTime", DateTime.now().toString("yyyy-MM-dd 00:00:00"));
                overseasOrderDao.insertOverseasOrderExportRecord(cmap);
            }
        }
        return 1;
    }
    
}
