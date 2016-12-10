package com.ygg.admin.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.code.SellerEnum;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.OrderManualDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.SystemLogDao;
import com.ygg.admin.entity.OrderDetailInfoForSeller;
import com.ygg.admin.entity.OrderManualEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.entity.SellerEntity;
import com.ygg.admin.service.OrderManualService;
import com.ygg.admin.util.CommonConstant;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;

@Service("orderManualService")
public class OrderManualServiceImpl implements OrderManualService
{
    private static Logger logger = Logger.getLogger(OrderManualServiceImpl.class);
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private OrderManualDao orderManualDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private SystemLogDao logDao;
    
    @Override
    public boolean validateProduct(int sellerId, List<Integer> productIdList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", productIdList);
        List<Integer> sellerIdList = sellerDao.findSellerIdByProductIdList(para);
        if (sellerIdList.size() != 1)
        {
            return false;
        }
        int querySellerId = sellerIdList.get(0);
        if (sellerId != querySellerId)
        {
            return false;
        }
        for (Integer pid : productIdList)
        {
            ProductEntity e = productDao.findProductByID(pid, null);
            if (e == null)
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Map<String, Object> saveOrderManual(Map<String, Object> para)
        throws Exception
    {
        OrderManualEntity orderManual = (OrderManualEntity)para.get("orderManual");
        List<Map<String, Object>> pIdAndNumList = (List<Map<String, Object>>)para.get("pIdAndNumList");
        
        // 生成 number
        orderManual.setNumber(Long.valueOf(CommonUtil.generateOrderNumber()));
        
        // 生成 totalPrice
        BigDecimal totalPrice = new BigDecimal(0);
        for (Map<String, Object> currMap : pIdAndNumList)
        {
            int pid = Integer.valueOf(currMap.get("pid") + "");
            int num = Integer.valueOf(currMap.get("num") + "");
            BigDecimal currTotalPrice = new BigDecimal(num);
            ProductEntity e = productDao.findProductByID(pid, null);
            
            BigDecimal marketPrice = new BigDecimal(e.getSalesPrice());
            
            currTotalPrice = currTotalPrice.multiply(marketPrice);
            
            totalPrice = totalPrice.add(currTotalPrice);
            currMap.put("salesPrice", e.getSalesPrice());
        }
        //当手动创建订单的类型为售后补发货时，需要计算totalPrice,否则totalPrice由用户输入
        if (orderManual.getSendType() == 1)
        {
            orderManual.setTotalPrice(totalPrice.doubleValue());
        }
        
        // 插入数据库
        int status = orderManualDao.insertOrderManual(orderManual);
        if (status == 1)
        {
            // 插入订单商品信息
            for (Map<String, Object> currMap : pIdAndNumList)
            {
                Integer pid = Integer.valueOf(currMap.get("pid") + "");
                Integer num = Integer.valueOf(currMap.get("num") + "");
                Float salesPrice = Float.valueOf(currMap.get("salesPrice") + "");
                Map<String, Object> rderManualProductPara = new HashMap<String, Object>();
                rderManualProductPara.put("orderManualId", orderManual.getId());
                rderManualProductPara.put("productId", pid);
                rderManualProductPara.put("productCount", num);
                rderManualProductPara.put("salesPrice", salesPrice);
                orderManualDao.insertOrderManualProduct(rderManualProductPara);
            }
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 1);
            
            // 手动创建订单加日志
            try
            {
                Map<String, Object> logInfoMap = new HashMap<String, Object>();
                String username = SecurityUtils.getSubject().getPrincipal() + "";
                logInfoMap.put("username", username);
                logInfoMap.put("businessType", CommonEnum.BusinessTypeEnum.SALESERVICE_MANAGEMENT.ordinal());
                logInfoMap.put("operationType", CommonEnum.OperationTypeEnum.HAND_CREATE_ORDER.ordinal());
                logInfoMap.put("level", CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal());
                String content = "用户【" + username + "】手动创建了一条订单号=" + orderManual.getNumber() + " 的订单";
                logInfoMap.put("content", content);
                logDao.logger(logInfoMap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }
        else
        {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", 0);
            result.put("msg", "新增失败");
            return result;
        }
    }
    
    @Override
    public Map<String, Object> findAllOrderManual(Map<String, Object> para)
        throws Exception
    {
        List<OrderManualEntity> reList = orderManualDao.findAllOrderManual(para);
        int total = 0;
        if (reList.size() > 0)
        {
            total = orderManualDao.countOrderManual(para);
            for (OrderManualEntity om : reList)
            {
                Integer sellerId = om.getSellerId();
                SellerEntity seller = sellerDao.findSellerById(sellerId);
                om.setSellerName(seller.getRealSellerName());
                om.setSendAddress(seller.getSendAddress());
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", reList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public Map<String, Object> sendOrderManual(int orderId, String channel, String number)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sendTime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        para.put("logisticsChannel", channel);
        para.put("logisticsNumber", number);
        para.put("status", 2);// 已发货
        para.put("id", orderId);
        int status = orderManualDao.updateOrderManual(para);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", status == 1 ? "保存成功" : "保存失败");
        return result;
    }
    
    @Override
    public Map<String, Object> updateOrderManual(Map<String, Object> para)
        throws Exception
    {
        int status = orderManualDao.updateOrderManual(para);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status);
        result.put("msg", status == 1 ? "取消成功" : "取消失败");
        return result;
    }
    
    @Override
    public Map<String, Object> findOrderDetailInfo(int orderManualId)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        OrderManualEntity orderManual = orderManualDao.findOrderManualById(orderManualId);
        if (orderManual == null)
        {
            return result;
        }
        result.put("id", orderManual.getId());
        // 订单编号
        result.put("number", orderManual.getNumber());
        // 订单状态
        int status = orderManual.getStatus();
        String statusDescripton = "";
        if (status == 1)
        {
            statusDescripton = "待发货";
        }
        else if (status == 2)
        {
            statusDescripton = "已发货";
            
        }
        else if (status == 3)
        {
            statusDescripton = "客服取消";
        }
        result.put("status", statusDescripton);
        result.put("createTime", orderManual.getCreateTime());
        result.put("sendTime", orderManual.getSendTime());
        result.put("receiveName", orderManual.getFullName());
        result.put("receiveMobile", orderManual.getMobileNumber());
        result.put("receiveIdCart", orderManual.getIdCard());
        result.put("remark", orderManual.getRemark());
        result.put("desc", orderManual.getDesc());
        // 详细地址
        StringBuffer sb = new StringBuffer("");
        if (orderManual.getProvince() != null && !"".equals(orderManual.getProvince()))
        {
            sb.append(areaDao.findProvinceNameByProvinceId(Integer.valueOf(orderManual.getProvince()))).append(" ");
        }
        if (orderManual.getCity() != null && !"".equals(orderManual.getCity()))
        {
            sb.append(areaDao.findCityNameByCityId(Integer.valueOf(orderManual.getCity()))).append(" ");
        }
        if (orderManual.getDistrict() != null && !"".equals(orderManual.getDistrict()))
        {
            sb.append(areaDao.findDistrictNameByDistrictId(Integer.valueOf(orderManual.getDistrict()))).append(" ");
        }
        if (orderManual.getDetailAddress() != null && !"".equals(orderManual.getDetailAddress()))
        {
            sb.append(orderManual.getDetailAddress());
        }
        result.put("address", sb.toString());
        SellerEntity seller = sellerDao.findSellerById(orderManual.getSellerId());
        result.put("sellerName", seller.getSellerName());
        
        StringBuffer sellerType = new StringBuffer();
        SellerEnum.SellerTypeEnum sellerTypeEnum = SellerEnum.SellerTypeEnum.getSellerTypeEnumByCode(seller.getSellerType());
        sellerType.append(sellerTypeEnum.getDesc());
        if (sellerTypeEnum.getCode() == SellerEnum.SellerTypeEnum.HONG_KONG.getCode())
        {
            sb.append(sellerTypeEnum.getIsNeedIdCardImage() == 1 ? "(身份证照片)" : sellerTypeEnum.getIsNeedIdCardNumber() == 1 ? "(仅身份证号)" : "");
        }
        result.put("sellerType", sellerType);
        result.put("sendAddress", seller.getSendAddress());
        result.put("totalProductPrice", new BigDecimal(orderManual.getTotalPrice()).doubleValue());
        // 获得商品数据
        List<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> salePros = orderManualDao.findAllProductInfoByOrderManualId(orderManualId);
        for (Map<String, Object> p : salePros)
        {
            Long productId = (Long)p.get("productId");
            double salePrice = new BigDecimal(p.get("salesPrice") + "").doubleValue();
            int productCount = (int)p.get("productCount");
            ProductEntity product = productDao.findProductByID(productId.intValue(), null);
            Map<String, Object> currP = new HashMap<String, Object>();
            currP.put("id", productId);
            currP.put("code", product.getCode());
            currP.put("name", product.getName());
            currP.put("sellerName", seller.getSellerName());
            currP.put("sellerType", SellerEnum.SellerTypeEnum.getDescByCode(seller.getSellerType()));
            currP.put("sendAddress", seller.getSendAddress());
            currP.put("salePrice", salePrice);
            currP.put("productCount", productCount);
            currP.put("productTotalPrice", productCount * salePrice);
            products.add(currP);
        }
        result.put("products", products);
        return result;
    }
    
    @Override
    public String getExportForSeller(Map<String, Object> para)
        throws Exception
    {
        Map<String, List<OrderDetailInfoForSeller>> resultMap = findAllOrderInfoForSeller(para);
        if (resultMap == null || resultMap.size() == 0)
        {
            return null;
        }
        String nowStr = DateTime.now().toString("yyyy-MM-dd_HH_mm_ss");
        File fileDir = new File(YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
        File newDir = new File(fileDir, nowStr + "_" + new Random().nextInt(10000) + "send");
        newDir.mkdir();
        // 导出参数选择
        Integer exportPostage = para.get("exportPostage") != null ? (Integer)para.get("exportPostage") : 0;
        if (exportPostage == 1)
        {// 导出详细 一个excel
            List<OrderDetailInfoForSeller> reListForDetail = new ArrayList<OrderDetailInfoForSeller>();
            for (Map.Entry<String, List<OrderDetailInfoForSeller>> entry : resultMap.entrySet())
            {
                String key = entry.getKey();
                List<OrderDetailInfoForSeller> cuD = entry.getValue();
                for (OrderDetailInfoForSeller nu : cuD)
                {
                    nu.setSellerName(key);
                }
                reListForDetail.addAll(cuD);
            }
            writeToExcel(newDir, "all" + ".xls", reListForDetail, exportPostage);
        }
        else
        {
            for (Map.Entry<String, List<OrderDetailInfoForSeller>> entry : resultMap.entrySet())
            {
                writeToExcel(newDir, entry.getKey() + ".xls", entry.getValue(), exportPostage);
            }
        }
        return newDir.getAbsolutePath();
    }
    
    private void writeToExcel(File dir, String fileName, List<OrderDetailInfoForSeller> resultList, int type)
        throws Exception
    {
        OutputStream fOut = null;
        HSSFWorkbook workbook = null;
        try
        {
            workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            String[] str = null;
            if (type == 1)
            {
                str =
                    new String[] {"订单编号", "订单状态", "创建时间", "付款日期", "收货人", "身份证号码", "收货地址", "省", "市", "区", "详细地址", "联系电话", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "买家备注",
                        "卖家备注", "发货时间", "物流公司", "物流单号", "类型"};
            }
            else
            {
                str =
                    new String[] {"订单编号", "订单状态", "创建时间", "付款日期", "收货人", "身份证号码", "收货地址", "省", "市", "区", "详细地址", "联系电话", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "买家备注",
                        "卖家备注", "发货时间", "物流公司", "物流单号"};
            }
            Row row = sheet.createRow(0);
            for (int i = 0; i < str.length; i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue(str[i]);
            }
            for (int i = 0; i < resultList.size(); i++)
            {
                OrderDetailInfoForSeller curr = resultList.get(i);
                Row r = sheet.createRow(i + 1);
                r.createCell(0).setCellValue(curr.getoNumber() + "");
                r.createCell(1).setCellValue(curr.getoStatusDescripton() + "");
                r.createCell(2).setCellValue(curr.getoCreateTime() + "");
                r.createCell(3).setCellValue(curr.getoPayTime() + "");
                r.createCell(4).setCellValue(curr.getRaFullName() + "");
                r.createCell(5).setCellValue(curr.getRaIdCard() + "");
                r.createCell(6).setCellValue(curr.getAddress() + "");
                r.createCell(7).setCellValue(curr.getProvince() + "");
                r.createCell(8).setCellValue(curr.getCity() + "");
                r.createCell(9).setCellValue(curr.getDistrict() + "");
                r.createCell(10).setCellValue(curr.getDetailAddress() + "");
                r.createCell(11).setCellValue(curr.getRaMobileNumber() + "");
                String code = curr.getProductCode();
                Integer productCount = Integer.valueOf(curr.getProductCount() + "");
                int index = code.lastIndexOf("%");
                boolean isdiv = false;
                if ((index > -1) && (index < code.length() - 1))
                {
                    String num = code.substring(index + 1);
                    if (StringUtils.isNumeric(num))
                    {
                        code = code.substring(0, index);
                        productCount = productCount * Integer.valueOf(num);
                        isdiv = true;
                    }
                }
                r.createCell(12).setCellValue(code);
                r.createCell(13).setCellValue(curr.getProductName() + "");
                r.createCell(14).setCellValue(productCount);
                if (isdiv)
                {
                    r.createCell(15).setCellValue(new DecimalFormat("0.00").format(curr.getSalesPrice() / productCount));
                }
                else
                {
                    r.createCell(15).setCellValue(curr.getSalesPrice() + "");
                }
                r.createCell(16).setCellValue(curr.getSmailTotalPrice() + "");
                r.createCell(17).setCellValue(0);
                r.createCell(18).setCellValue(curr.getoTotalPrice() - curr.getoFreightMoney());
                // 买家备注，暂停用
                r.createCell(19).setCellValue("");
                // 卖家备注，
                r.createCell(20).setCellValue(curr.getSellerMarks() == null ? "" : curr.getSellerMarks());
                r.createCell(21).setCellValue(curr.getoSendTime() + "");
                r.createCell(22).setCellValue(curr.getOlogChannel() + "");
                r.createCell(23).setCellValue(curr.getOlogNumber() + "");
                if (type == 1)
                {
                    r.createCell(24).setCellValue(curr.getSendType() + "");
                }
            }
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
    
    public Map<String, List<OrderDetailInfoForSeller>> findAllOrderInfoForSeller(Map<String, Object> para)
        throws Exception
    {
        Map<String, List<OrderDetailInfoForSeller>> returnList = new HashMap<String, List<OrderDetailInfoForSeller>>();
        List<OrderDetailInfoForSeller> reList = findAllOrderManualAndProduct(para);
        if (reList.size() > 0)
        {
            for (OrderDetailInfoForSeller curr : reList)
            {
                String cuKey = curr.getSellerName() + "_" + curr.getSendAddress() + "_" + curr.getWarehouse();
                List<OrderDetailInfoForSeller> newList = returnList.get(cuKey);
                if (newList == null)
                {
                    newList = new ArrayList<OrderDetailInfoForSeller>();
                    returnList.put(cuKey, newList);
                }
                int ost = curr.getoStatus();
                curr.setoStatusDescripton(ost == 1 ? "待发货" : (ost == 2 ? "已发货" : (ost == 3 ? "客服取消" : "状态错误")));
                if (!"".equals(curr.getProvince()))
                {
                    curr.setProvince(areaDao.findProvinceNameByProvinceId(Integer.valueOf(curr.getProvince()).intValue()));
                }
                if (!"".equals(curr.getCity()))
                {
                    curr.setCity(areaDao.findCityNameByCityId(Integer.valueOf(curr.getCity()).intValue()));
                }
                if (!"".equals(curr.getDistrict()))
                {
                    curr.setDistrict(areaDao.findDistrictNameByDistrictId(Integer.valueOf(curr.getDistrict())));
                }
                String address = curr.getProvince() + curr.getCity() + curr.getDistrict() + curr.getDetailAddress();
                curr.setAddress(address);
                BigDecimal bigDecimal1 = new BigDecimal(curr.getSalesPrice());
                BigDecimal bigDecimal2 = new BigDecimal(curr.getProductCount());
                curr.setSmailTotalPrice(bigDecimal1.multiply(bigDecimal2).doubleValue());
                newList.add(curr);
            }
        }
        return returnList;
    }
    
    @Override
    public List<OrderDetailInfoForSeller> findAllOrderManualAndProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = orderManualDao.findAllOrderManualAndProduct(para);
        List<OrderDetailInfoForSeller> orderDetails = null;
        OrderDetailInfoForSeller orderInfo = null;
        if (reList.size() > 0)
        {
            orderDetails = new ArrayList<OrderDetailInfoForSeller>();
            for (Map<String, Object> entity : reList)
            {
                Integer sellerId = Integer.parseInt(entity.get("sellerId") + "");
                Integer productId = Integer.parseInt(entity.get("productId") + "");
                SellerEntity seller = sellerDao.findSellerById(sellerId);
                ProductEntity product = productDao.findProductByID(productId, null);
                orderInfo = new OrderDetailInfoForSeller();
                orderInfo.setoTotalPrice(Float.valueOf(entity.get("totalPrice") + ""));
                orderInfo.setoNumber(entity.get("orderNumber") + "");
                orderInfo.setoStatus(Integer.parseInt(entity.get("status") + ""));
                orderInfo.setoCreateTime(entity.get("createTime") + "");
                orderInfo.setoPayTime(entity.get("createTime") + "");
                orderInfo.setRaFullName(entity.get("fullName") + "");
                orderInfo.setRaIdCard(entity.get("idCard") + "");
                orderInfo.setProvince(entity.get("province") + "");
                orderInfo.setCity(entity.get("city") + "");
                orderInfo.setDistrict(entity.get("district") + "");
                orderInfo.setDetailAddress(entity.get("detailAddress") + "");
                orderInfo.setRaMobileNumber(entity.get("mobileNumber") + "");
                if (product != null)
                {
                    orderInfo.setProductCode(product.getCode());
                    orderInfo.setProductName(product.getName());
                }
                else
                {
                    orderInfo.setProductCode("");
                    orderInfo.setProductName("");
                }
                if (seller != null)
                {
                    orderInfo.setSellerName(seller.getRealSellerName());
                    orderInfo.setSendAddress(seller.getSendAddress());
                    orderInfo.setWarehouse(seller.getWarehouse());
                }
                else
                {
                    orderInfo.setSellerName("");
                    orderInfo.setSendAddress("");
                    orderInfo.setWarehouse("");
                }
                orderInfo.setProductCount(Integer.parseInt(entity.get("productCount") + ""));
                orderInfo.setSalesPrice(Double.parseDouble(entity.get("salesPrice") + ""));
                orderInfo.setOlogChannel(entity.get("logisticsChannel") + "");
                orderInfo.setOlogNumber(entity.get("logisticsNumber") + "");
                orderInfo.setSellerMarks(entity.get("desc") + "");
                int type = Integer.valueOf(entity.get("sendType") + "");
                orderInfo.setSendType(type == 1 ? "售后补发货" : type == 2 ? "顾客打款请求发货" : "");
                orderDetails.add(orderInfo);
            }
        }
        return orderDetails;
    }
    
    @Override
    public String jsonOverseasManualProduct(int page, int rows, String code, String name)
        throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        if (page == 0)
        {
            page = 1;
        }
        params.put("start", rows * (page - 1));
        params.put("max", rows);
        
        Map<String, Object> productPara = new HashMap<>();
        if (!"".equals(name))
        {
            productPara.put("name", "%" + name + "%");
        }
        if (!"".equals(code))
        {
            if (code.indexOf("%") > -1)
            {
                code = code.replace("%", "\\%");
            }
            productPara.put("code", "%" + code + "%");
        }
        if (!productPara.isEmpty())
        {
            List<Integer> productIds = new ArrayList<>();
            List<ProductEntity> pes = productDao.findProductByPara(productPara);
            for (ProductEntity pe : pes)
            {
                productIds.add(pe.getId());
            }
            if (productIds.isEmpty())
            {
                return JSON.toJSONString(ResultEntity.getFailResultList());
            }
            params.put("idList", productIds);
        }
        
        List<Map<String, Object>> reList = orderManualDao.findOverseasManualProduct(params);
        
        List<Integer> productIds = new ArrayList<>();
        for (Map<String, Object> mp : reList)
        {
            productIds.add(Integer.parseInt(mp.get("productId") + ""));
        }
        
        List<ProductEntity> pes = new ArrayList<>();
        if (!productIds.isEmpty())
        {
            productPara.clear();
            productPara.put("idList", productIds);
            pes.addAll(productDao.findProductByPara(productPara));
        }
        
        Map<String, ProductEntity> productMap = new HashMap<>();
        for (ProductEntity pe : pes)
        {
            productMap.put(pe.getId() + "", pe);
        }
        
        for (Map<String, Object> it : reList)
        {
            ProductEntity p = productMap.get(it.get("productId") + "");
            SellerEntity se = sellerDao.findSellerSimpleById(p == null ? 0 : p.getSellerId());
            it.put("realSellerName", se != null ? se.getRealSellerName() : "");
            it.put("name", p == null ? "" : p.getName());
            it.put("code", p == null ? "" : p.getCode());
            it.put("baseId", p == null ? "" : p.getProductBaseId());
            it.put("type", p == null ? "" : p.getType());
            if (p != null && p.getType() == ProductEnum.PRODUCT_TYPE.SALE.getCode())
            {
                it.put("url", String.format(CommonConstant.SALE_PRODUCT_WAP_URL, p.getId()));
            }
            else if (p != null && p.getType() == ProductEnum.PRODUCT_TYPE.MALL.getCode())
            {
                it.put("url", String.format(CommonConstant.MALL_PRODUCT_WAP_URL, p.getId()));
            }
            else
            {
                it.put("url", "#");
            }
        }
        int total = orderManualDao.countOverseasManualProduct(params);
        return JSON.toJSONString(ResultEntity.getResultList(total, reList));
    }
    
    @Override
    public Map<String, Object> addOverseasManualProduct(int sellerId, int productId, int nums, String remark)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("sellerId", sellerId);
        para.put("productId", productId);
        para.put("productCount", nums);
        para.put("remark", remark);
        int status = orderManualDao.addOverseasManualProduct(para);
        Map<String, Object> result = new HashMap<String, Object>();
        if (status != 1)
        {
            result.put("status", 0);
            result.put("msg", "保存失败");
        }
        else
        {
            result.put("status", 1);
            try
            {
                if (status == 1)
                {
                    String username = SecurityUtils.getSubject().getPrincipal() + "";
                    Map<String, Object> logPara = new HashMap<String, Object>();
                    logPara.put("username", username);
                    logPara.put("businessType", CommonEnum.BusinessTypeEnum.SALESERVICE_MANAGEMENT.ordinal());
                    logPara.put("operationType", CommonEnum.OperationTypeEnum.HAND_CREATE_ORDER.ordinal());
                    logPara.put("content", "用户【" + username + "】手动创建了wap订单，商品Id=" + productId + ",商家Id：" + sellerId + ",商品数量：" + nums);
                    logPara.put("level", CommonEnum.LogLevelEnum.LEVEL_ONE.ordinal());
                    logDao.logger(logPara);
                }
            }
            catch (Exception e)
            {
                logger.error("手动创建wap订单记录日志出错了", e);
            }
        }
        return result;
    }
    
}
