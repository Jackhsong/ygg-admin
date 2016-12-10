package com.ygg.admin.service.impl;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.config.AreaCache;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.dao.AreaDao;
import com.ygg.admin.dao.FinanceDao;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.OrderManualDao;
import com.ygg.admin.dao.OrderQuestionDao;
import com.ygg.admin.dao.ProductBaseDao;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.RefundDao;
import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.entity.OrderEntity;
import com.ygg.admin.entity.OrderListDetailView;
import com.ygg.admin.entity.OrderListView;
import com.ygg.admin.entity.OrderManualEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.RefundEntity;
import com.ygg.admin.exception.ServiceException;
import com.ygg.admin.service.FinanceSerivce;
import com.ygg.admin.util.CommonEnum;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.DateTimeUtil;
import com.ygg.admin.util.MathUtil;
import com.ygg.admin.util.POIUtil;
import com.ygg.admin.util.StringUtils;
import com.ygg.admin.view.LineItemView;
import com.ygg.admin.view.OrderFinanceView;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Service("financeSerivce")
public class FinanceSerivceImpl implements FinanceSerivce
{
    Logger log = Logger.getLogger(FinanceSerivceImpl.class);
    
    @Resource
    private FinanceDao financeDao;
    
    @Resource
    private RefundDao refundDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Resource
    private OrderManualDao orderManualDao;
    
    @Resource
    private SellerDao sellerDao;
    
    @Resource
    private AreaDao areaDao;
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private OrderQuestionDao orderQuestionDao;
    
    @Override
    public Map<String, Object> findOrderFinanceData(Map<String, Object> para)
        throws Exception
    {
        int orderType = Integer.parseInt(para.get("orderType") + "");// 订单类型，0：全部，1：正常订单，2：手动订单
        List<OrderListView> reList = new ArrayList<>();
        int total = 0;
        if (orderType == 1)
        {
            // 1：正常订单 需要判断是否有退款结算时间，手动订单默认没有
            if (para.get("hasRefundSettlementTime") != null)
            {
                List<Integer> queryOrderIdList = financeDao.findOrderIdByRefundSettlementComfirmDate(para);
                if (queryOrderIdList.size() == 0)
                {
                    Map<String, Object> result = new HashMap<>();
                    result.put("rows", new ArrayList<>());
                    result.put("total", 0);
                    return result;
                }
                para.put("orderIdList", queryOrderIdList);
            }
            reList = financeDao.findOrderInfo(para);
            if (reList.size() > 0)
            {
                total = financeDao.countOrderInfo(para);
                List<Integer> orderIdList = new ArrayList<Integer>();
                for (OrderListView it : reList)
                {
                    it.setStatusStr(OrderEnum.ORDER_STATUS.getDescByCode(it.getStatus()));
                    it.setIsSettlementStr(it.getIsNeedSettlement() == 0 ? "无需结算" : (it.getIsSettlement() == 0 ? "未结算" : "已结算"));
                    orderIdList.add(it.getId());
                }
                Map<String, Object> sPara = new HashMap<String, Object>();
                sPara.put("idList", orderIdList);
                List<Map<String, Object>> settlementList = financeDao.findOrderSettlementByPara(sPara);
                List<Integer> rOrderIdList = financeDao.findOrderIdFromRefundByPara(sPara);
                Map<String, Object> settlementMap = new HashMap<String, Object>();
                for (Map<String, Object> it : settlementList)
                {
                    String orderId = it.get("orderId") + "";
                    settlementMap.put(orderId, it);
                }
                for (OrderListView it : reList)
                {
                    String orderId = it.getId() + "";
                    Map<String, Object> data = settlementMap.get(orderId) == null ? null : (Map<String, Object>)(settlementMap.get(orderId));
                    int postageIsSettlement = 0;
                    String freightMoney = "";
                    if (data != null)
                    {
                        postageIsSettlement = Integer.parseInt(data.get("postageIsSettlement") + "");
                        freightMoney = data.get("freightMoney") + "";
                    }
                    it.setPostageIsSettlement(postageIsSettlement);
                    it.setPostageIsSettlementStr(postageIsSettlement == 1 ? "已结算" : "未结算");
                    it.setSettlementFreightMoney(freightMoney);
                    if (rOrderIdList.contains(Integer.valueOf(orderId)))
                    {
                        it.setContainsRefund("是");
                    }
                }
            }
        }
        else if (orderType == 2)
        {
            // 2：手动订单
            List<OrderManualEntity> omes = orderManualDao.findAllOrderManual(para);
            if (omes.size() > 0)
            {
                total = orderManualDao.countOrderManual(para);
                // List<Integer> orderIdList = new ArrayList<Integer>();
                List<Integer> sellerIdList = new ArrayList<Integer>();
                for (OrderManualEntity it : omes)
                {
                    // orderIdList.add(it.getId());
                    sellerIdList.add(it.getSellerId());
                }
                Map<String, Object> sPara = new HashMap<String, Object>();
                sPara.put("idList", sellerIdList);
                List<Map<String, Object>> sellerInfoList = sellerDao.findSellerInfoBySellerIdList(sPara);
                Map<String, Object> sellerInfoMap = new HashMap<String, Object>();
                for (Map<String, Object> it : sellerInfoList)
                {
                    String id = it.get("id") + "";
                    sellerInfoMap.put("seller" + id, it);
                }
                for (OrderManualEntity it : omes)
                {
                    OrderListView olv = new OrderListView();
                    olv.setOrderType("手工订单");
                    olv.setId(it.getId());
                    olv.setNumber(it.getNumber());
                    olv.setStatus(it.getStatus());
                    olv.setIsSettlement(it.getIsSettlement());
                    olv.setCreateTime(it.getCreateTime());
                    olv.setPayTime(it.getCreateTime());
                    olv.setSendTime(it.getSendTime());
                    olv.setTotalPrice(it.getTotalPrice() + "");
                    olv.setRealPrice(it.getTotalPrice() + "");
                    olv.setStatusStr(it.getStatusDescripton());
                    olv.setIsSettlementStr(it.getIsNeedSettlement() == 0 ? "无需结算" : (it.getIsSettlement() == 0 ? "未结算" : "已结算"));
                    String sellerId = it.getSellerId() + "";
                    Map<String, Object> sellerInfo = sellerInfoMap.get("seller" + sellerId) == null ? null : (Map<String, Object>)sellerInfoMap.get("seller" + sellerId);
                    if (sellerInfo != null)
                    {
                        olv.setSellerName(sellerInfo.get("realSellerName") + "");
                        olv.setSendAddress(sellerInfo.get("sendAddress") + "");
                    }
                    reList.add(olv);
                }
                
            }
        }
        else
        {
            int start = Integer.parseInt(para.get("start") + "");
            int max = Integer.parseInt(para.get("max") + "");
            
            int orderManualTotal = orderManualDao.countOrderManual(para);
            int orderTotal = 0;
            // 全部订单
            // 1：手动订单
            List<OrderManualEntity> omes = orderManualDao.findAllOrderManual(para);
            if (omes.size() > 0)
            {
                List<Integer> sellerIdList = new ArrayList<Integer>();
                for (OrderManualEntity it : omes)
                {
                    sellerIdList.add(it.getSellerId());
                }
                Map<String, Object> sPara = new HashMap<String, Object>();
                sPara.put("idList", sellerIdList);
                List<Map<String, Object>> sellerInfoList = sellerDao.findSellerInfoBySellerIdList(sPara);
                Map<String, Object> sellerInfoMap = new HashMap<String, Object>();
                for (Map<String, Object> it : sellerInfoList)
                {
                    String id = it.get("id") + "";
                    sellerInfoMap.put(id, it);
                }
                for (OrderManualEntity it : omes)
                {
                    OrderListView olv = new OrderListView();
                    olv.setOrderType("手工订单");
                    olv.setId(it.getId());
                    olv.setNumber(it.getNumber());
                    olv.setStatus(it.getStatus());
                    olv.setIsSettlement(it.getIsSettlement());
                    olv.setCreateTime(it.getCreateTime());
                    olv.setPayTime(it.getCreateTime());
                    olv.setSendTime(it.getSendTime());
                    olv.setTotalPrice(it.getTotalPrice() + "");
                    olv.setRealPrice(it.getTotalPrice() + "");
                    olv.setStatusStr(it.getStatusDescripton());
                    olv.setIsSettlementStr(it.getIsNeedSettlement() == 0 ? "无需结算" : (it.getIsSettlement() == 0 ? "未结算" : "已结算"));
                    String sellerId = it.getSellerId() + "";
                    Map<String, Object> sellerInfo = sellerInfoMap.get(sellerId) == null ? null : (Map<String, Object>)sellerInfoMap.get(sellerId);
                    if (sellerInfo != null)
                    {
                        olv.setSellerName(sellerInfo.get("realSellerName") + "");
                        olv.setSendAddress(sellerInfo.get("sendAddress") + "");
                    }
                    reList.add(olv);
                }
            }
            boolean findTo = false;
            if (reList.size() == 0)
            {
                // 手工订单 没有信息了 。重新计算start & max
                start = start - (orderManualTotal);
                para.put("start", start);
                findTo = true;
            }
            else if (reList.size() < max)
            {
                // 手工订单 信息不足一页 。重新计算start & max
                max = max - (orderManualTotal % max);
                para.put("start", 0);
                para.put("max", max);
                findTo = true;
            }
            if (findTo)
            {
                // 2：正常订单
                List<OrderListView> list = financeDao.findOrderInfo(para);
                if (list.size() > 0)
                {
                    orderTotal = financeDao.countOrderInfo(para);
                    List<Integer> orderIdList = new ArrayList<Integer>();
                    for (OrderListView it : list)
                    {
                        it.setStatusStr(OrderEnum.ORDER_STATUS.getDescByCode(it.getStatus()));
                        it.setIsSettlementStr(it.getIsNeedSettlement() == 0 ? "无需结算" : (it.getIsSettlement() == 0 ? "未结算" : "已结算"));
                        orderIdList.add(it.getId());
                    }
                    Map<String, Object> sPara = new HashMap<String, Object>();
                    sPara.put("idList", orderIdList);
                    List<Map<String, Object>> settlementList = financeDao.findOrderSettlementByPara(sPara);
                    List<Integer> rOrderIdList = financeDao.findOrderIdFromRefundByPara(sPara);
                    Map<String, Object> settlementMap = new HashMap<String, Object>();
                    for (Map<String, Object> it : settlementList)
                    {
                        String orderId = it.get("orderId") + "";
                        settlementMap.put(orderId, it);
                    }
                    for (OrderListView it : list)
                    {
                        String orderId = it.getId() + "";
                        Map<String, Object> data = settlementMap.get(orderId) == null ? null : (Map<String, Object>)(settlementMap.get(orderId));
                        int postageIsSettlement = 0;
                        String freightMoney = "";
                        if (data != null)
                        {
                            postageIsSettlement = Integer.parseInt(data.get("postageIsSettlement") + "");
                            freightMoney = data.get("freightMoney") + "";
                        }
                        it.setPostageIsSettlement(postageIsSettlement);
                        it.setPostageIsSettlementStr(postageIsSettlement == 1 ? "已结算" : "未结算");
                        it.setSettlementFreightMoney(freightMoney);
                        if (rOrderIdList.contains(Integer.valueOf(orderId)))
                        {
                            it.setContainsRefund("是");
                        }
                    }
                }
                reList.addAll(list);
            }
            else
            {
                orderTotal = financeDao.countOrderInfo(para);
            }
            total = orderManualTotal + orderTotal;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", reList);
        result.put("total", total);
        return result;
    }
    
    @Override
    public int checkImportPostageSettlementData(String number, String postage, List<Map<String, Object>> simulationList, List<String> alreadyImportOrderList)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int status = 1;
        String msg = "成功";
        if (!financeDao.existsOrderNumber(number))
        {
            status = 0;
            msg = "订单号不存在";
        }
        else if (financeDao.isSettlement(number))
        {
            status = 0;
            msg = "订单号已结算";
        }
        else if (!Pattern.matches("[0-9]+(\\.?)[0-9]*", postage))
        {
            status = 0;
            msg = "运费金额不正确";
        }
        
        if (alreadyImportOrderList.contains(number))
        {
            status = 0;
            msg = "订单号重复";
        }
        else
        {
            alreadyImportOrderList.add(number);
        }
        
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("msg", msg);
        result.put("orderNumber", number);
        result.put("postage", postage);
        simulationList.add(result);
        return status;
    }
    
    @Override
    public int savePostageSettlementData(String number, String postage, String date, List<Map<String, Object>> confirmList)
        throws Exception
    {
        int status = financeDao.insertOrderSettlement(Long.valueOf(number), postage, date);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("orderNumber", number);
        result.put("postage", postage);
        result.put("status", status == 1 ? "成功" : "失败");
        confirmList.add(result);
        return status;
    }
    
    @Override
    public int checkimportRefundSettlementData(String refundId, String responsibilitySide, String money, List<Map<String, Object>> simulationList)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int status = 1;
        String msg = "成功";
        RefundEntity refund = null;
        if (StringUtils.isNumeric(refundId))
        {
            refund = refundDao.findRefundById(Integer.valueOf(refundId));
        }
        if (refund == null)
        {
            status = 0;
            msg = "退款ID不存在";
        }
        else if (responsibilitySide == null || responsibilitySide.equals("") || (!"我方".equals(responsibilitySide) && !"商家".equals(responsibilitySide)))
        {
            status = 0;
            msg = "承担方不存在";
        }
        else if (!Pattern.matches("[0-9]+(\\.?)[0-9]*", money))
        {
            status = 0;
            msg = "承担金额不正确";
        }
        /*
         * else if (Double.valueOf(money) > refund.getRealMoney()) { status = 0; msg = "承担金额大于实际退款金额"; }
         */
        else if ("我方".equals(responsibilitySide) && (Double.valueOf(money) != refund.getRealMoney()))
        {
            status = 0;
            msg = "我方承担金额与实际退款不相等";
        }
        else if (refund.getIsSettlement() == 1)
        {
            // 检查是否已经结算
            status = 0;
            msg = "已结算";
        }
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("msg", msg);
        result.put("refundId", refundId);
        result.put("responsibilitySide", responsibilitySide);
        result.put("money", money);
        simulationList.add(result);
        return status;
    }
    
    @Override
    public int saveRefundSettlementData(String refundId, String responsibilitySide, String money, String date, List<Map<String, Object>> confirmList)
        throws Exception
    {
        Map<String, Object> updatePara = new HashMap<String, Object>();
        updatePara.put("id", refundId);
        updatePara.put("responsibilitySide", "我方".equals(responsibilitySide) ? 2 : 1);
        updatePara.put("responsibilityMoney", money);
        updatePara.put("isSettlement", 1);
        updatePara.put("settlementComfirmDate", date);
        int status = refundDao.updateRefund(updatePara);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("refundId", refundId);
        result.put("responsibilitySide", responsibilitySide);
        result.put("money", money);
        confirmList.add(result);
        return status;
    }
    
    @Override
    public int cancelRefundSettlementData(String refundId, List<Map<String, Object>> confirmList)
        throws Exception
    {
        Map<String, Object> updatePara = new HashMap<>();
        updatePara.put("id", refundId);
        updatePara.put("responsibilitySide", 1);
        updatePara.put("responsibilityMoney", 0.00);
        updatePara.put("isSettlement", 0);
        updatePara.put("settlementComfirmDate", "0000-00-00 00:00:00");
        int status = refundDao.updateRefund(updatePara);
        Map<String, Object> result = new HashMap<>();
        result.put("status", status == 1 ? "撤销成功" : "撤销失败");
        result.put("refundId", refundId);
        result.put("responsibilitySide", "");
        result.put("money", "");
        confirmList.add(result);
        return status;
    }
    
    @Override
    public int checkBatchUpdateProductCostPrice(String number, String productType, String productId, String productCost, List<Map<String, Object>> simulationList,
        Map<String, String> productInfo)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        int status = 1;
        String msg = "成功";
        String oldCost = "";
        ProductEntity product = null;
        if (StringUtils.isNumeric(productId))
        {
            product = productDao.findProductByID(Integer.valueOf(productId), null);
        }
        if (product == null)
        {
            status = 0;
            msg = "商品id不存在";
        }
        else if (!financeDao.existsOrderNumber(number))
        {
            status = 0;
            msg = "订单号不存在";
        }
        else if ((oldCost = financeDao.existsOrderProductByNumberAndProductId(Long.valueOf(number).longValue(), Integer.valueOf(productId))) == null)
        {
            status = 0;
            msg = "改订单中没有改商品";
        }
        else if (productType == null || productType.equals("") || (!"商城商品".equals(productType) && !"特卖商品".equals(productType) && !"团购商品".equals(productType))
            || ("团购商品".equals(productType) && product.getType() != 3) || ("商城商品".equals(productType) && product.getType() != 2)
            || ("特卖商品".equals(productType) && product.getType() != 1))
        {
            status = 0;
            msg = "商品类型不正确";
        }
        else if (!Pattern.matches("[0-9]+(\\.?)[0-9]*", productCost))
        {
            status = 0;
            msg = "供货价不正确";
        }
        // else if (Double.valueOf(productCost) > product.getSalesPrice())
        // {
        // status = 0;
        // msg = "供货价大于售卖价格";
        // }
        String code = "";
        double realCost = 0;
        if (product != null)
        {
            code = product.getCode();
            realCost = Double.valueOf(productCost);
            int index = code.lastIndexOf("%");
            if ((index > -1) && (index < code.length() - 1))
            {
                // 有效
                String num = code.substring(index + 1);
                if (StringUtils.isNumeric(num))
                {
                    realCost = realCost * Integer.valueOf(num);
                }
            }
        }
        
        productInfo.put(productId, code);
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("msg", msg);
        result.put("number", number);
        result.put("productType", productType);
        result.put("code", code);
        result.put("productId", productId);
        result.put("productCost", productCost);
        result.put("realCost", MathUtil.removeLastZero(MathUtil.round(realCost, 2)));
        result.put("oldProductCost", oldCost);
        simulationList.add(result);
        return status;
    }
    
    @Override
    public int saveBatchUpdateProductCostPrice(String number, String productType, String productId, String productCost, List<Map<String, Object>> confirmList,
        Map<String, String> productInfo)
        throws Exception
    {
        String code = productInfo.get(productId);
        if (code == null)
        {
            ProductEntity pe = productDao.findProductByID(Integer.valueOf(productId), null);
            code = pe.getCode();
        }
        
        int index = code.lastIndexOf("%");
        if ((index > -1) && (index < code.length() - 1))
        {
            double realCost = Double.valueOf(productCost);
            // 有效
            String num = code.substring(index + 1);
            if (StringUtils.isNumeric(num))
            {
                realCost = realCost * Integer.valueOf(num);
                productCost = MathUtil.removeLastZero(MathUtil.round(realCost, 2));
            }
        }
        
        Map<String, Object> updatePara = new HashMap<String, Object>();
        updatePara.put("number", number);
        updatePara.put("cost", productCost);
        updatePara.put("productId", productId);
        int status = financeDao.updateOrderProductCost(updatePara);
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("number", number);
        result.put("productType", productType);
        result.put("code", code);
        result.put("productId", productId);
        result.put("productCost", productCost);
        result.put("realCost", productCost);
        result.put("oldProductCost", "");
        confirmList.add(result);
        return status;
    }
    
    @Override
    public int countAllNum(Map<String, Object> para)
        throws Exception
    {
        int orderType = Integer.parseInt(para.get("orderType") + "");// 订单类型，0：全部，1：正常订单，2：手动订单
        int oldvs1 = 0;
        int oldvs2 = 0;
        if (orderType == 1)
        {
            if (para.get("hasRefundSettlementTime") != null)
            {
                List<Integer> queryOrderIdList = financeDao.findOrderIdByRefundSettlementComfirmDate(para);
                if (queryOrderIdList.size() == 0)
                {
                    return 0;
                }
                para.put("orderIdList", queryOrderIdList);
            }
            oldvs1 = financeDao.countOrderInfoDetail(para);
        }
        else if (orderType == 2)
        {
            oldvs2 = financeDao.countOrderManualInfoDetail(para);
        }
        else
        {
            oldvs1 = financeDao.countOrderInfoDetail(para);
            oldvs2 = financeDao.countOrderManualInfoDetail(para);
        }
        return oldvs1 + oldvs2;
    }
    
    @Override
    public String exportOrderFinanceDataDetail(Map<String, Object> para)
        throws Exception
    {
        int orderType = Integer.parseInt(para.get("orderType") + "");// 订单类型，0：全部，1：正常订单，2：手动订单
        // 所有待导出数据
        List<OrderFinanceView> ofvs = new ArrayList<>();
        if (orderType == 1)
        {
            // 正常订单
            if (para.get("hasRefundSettlementTime") != null)
            {
                List<Integer> queryOrderIdList = financeDao.findOrderIdByRefundSettlementComfirmDate(para);
                if (queryOrderIdList.size() == 0)
                {
                    throw new ServiceException("无导出数据");
                }
                para.put("orderIdList", queryOrderIdList);
            }
            setOrderFinanceViewForType1(ofvs, para);
        }
        else if (orderType == 2)
        {
            // 手动订单
            setOrderFinanceViewForType2(ofvs, para);
        }
        else
        {
            // 全部订单
            List<OrderFinanceView> ofvs1 = new ArrayList<>();
            setOrderFinanceViewForType2(ofvs1, para);
            List<OrderFinanceView> ofvs2 = new ArrayList<>();
            setOrderFinanceViewForType1(ofvs2, para);
            ofvs.addAll(ofvs1);
            ofvs.addAll(ofvs2);
            ofvs1.clear();
            ofvs2.clear();
        }
        // 分批写入excel
        // int sellerId = para.get("sellerId") == null ? -1 : Integer.parseInt(para.get("sellerId") + "");
        List<Integer> sellerIds = para.get("sellerIdList") == null ? null : (List<Integer>)para.get("sellerIdList");
        
        // writeToExcel_new 时用到
        // String realSellerName = "";
        // int sellerId = (sellerIds == null || sellerIds.isEmpty()) ? -1 : sellerIds.get(0);
        // if (sellerId != -1)
        // {
        // // 将商家作为文件名
        // SellerEntity se = sellerDao.findSellerSimpleById(sellerId);
        // if (se != null)
        // {
        // realSellerName = se.getRealSellerName();
        // if (sellerIds.size() > 1)
        // {
        // realSellerName += "等";
        // }
        //
        // }
        // }
        
        String nowStr = DateTime.now().toString("yyyy-MM-dd_HH_mm_ss");
        File fileDir = new File(YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
        File newDir = new File(fileDir, nowStr + "_" + new Random().nextInt(10000) + "finance");
        newDir.mkdir();
        
        int pageSize = 120000;
        if (ofvs.size() <= pageSize)
        {
            // 一个文件即可
            writeToExcel(newDir, ".xlsx", ofvs, sellerIds);
        }
        else
        {
            // 拆分多个文件
            int maxPage = ofvs.size() % pageSize == 0 ? ofvs.size() / pageSize : ofvs.size() / pageSize + 1;
            for (int page = 1; page <= maxPage; page++)
            {
                int from = 0;
                int to = from + pageSize;
                to = to > ofvs.size() ? ofvs.size() : to;
                writeToExcel(newDir, "-" + (page) + ".xlsx", ofvs.subList(from, to), sellerIds);
                ofvs.subList(from, to).clear();
            }
        }
        return newDir.getAbsolutePath();
    }
    
    //    private void writeToExcel_new(File dir, String postfix, List<OrderFinanceView> ofvs, String sellerName)
    //        throws Exception
    //    {
    //        StringTemplateGroup stGroup = new StringTemplateGroup("stringTemplate");
    //        stGroup.setFileCharEncoding("UTF8");
    //        // 写入excel文件头部信息
    //        // StringTemplate head = stGroup.getInstanceOf("com/ygg/admin/excel/head");
    //        String fileName = "结算清单_" + sellerName;
    //        StringTemplate head = stGroup.getInstanceOf("head");
    //        File file = new File(dir, fileName + postfix);
    //        file.createNewFile();
    //        // PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
    //
    //        FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
    //        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));
    //
    //        writer.write(head.toString());
    //        writer.flush();
    //
    //        List<com.ygg.admin.excel.Row> rows = new ArrayList<>();
    //        int maxRowNum = 0;
    //        try
    //        {
    //            // 产生工作簿对象
    //            String[] title = {"订单类型", "订单编号", "合并订单号", "订单状态", "结算状态", "结算时间", "付款渠道", "创建时间", "付款日期", "发货时间", "商家", "发货地", "收货人", "身份证号码", "收货地址", "省", "市", "区", "详细地址", "联系电话",
    //                "商品类型", "商品ID", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "实付金额", "积分优惠", "优惠券优惠", "客服调价", "单位实付金额", "单位供货价", "单位实付毛利", "总实付金额", "总供货价", "总实付毛利", "客服备注 ",
    //                "卖家备注", "发货时间", "物流公司", "物流单号", "用户类型", "下单用户名", "运费结算状态", "运费结算时间", "运费金额", "退款ID", "退款类型", "退款数量", "退款金额", "商家退款金额", "左岸城堡退款金额", "退款状态", "是否已打款", "是否已收货", "结算状态",
    //                "结算时间", "承担方", "承担金额"};
    //            maxRowNum++;
    //            com.ygg.admin.excel.Row titleRow = new com.ygg.admin.excel.Row();
    //            titleRow.setOrderType(title[0]);
    //            titleRow.setNumber(title[1]);
    //            titleRow.setHbNumber(title[2]);
    //            titleRow.setStatus(title[3]);
    //            titleRow.setSettlement(title[4]);
    //            titleRow.setSettlementTime(title[5]);
    //            titleRow.setPayChannel(title[6]);
    //            titleRow.setCreateTime(title[7]);
    //            titleRow.setPayTime(title[8]);
    //            titleRow.setSendTime(title[9]);
    //            titleRow.setSellerName(title[10]);
    //            titleRow.setSendAddress(title[11]);
    //            titleRow.setReceiveFullName(title[12]);
    //            titleRow.setIdCard(title[13]);
    //            titleRow.setReceiveAddress(title[14]);
    //            titleRow.setProvince(title[15]);
    //            titleRow.setCity(title[16]);
    //            titleRow.setDistrict(title[17]);
    //            titleRow.setDetailAddress(title[18]);
    //            titleRow.setMobileNumber(title[19]);
    //            titleRow.setType(title[20]);
    //            titleRow.setProductId(title[21]);
    //            titleRow.setCode(title[22]);
    //            titleRow.setProductName(title[23]);
    //            titleRow.setCount(title[24]);
    //            titleRow.setSalePrice(title[25]);
    //            titleRow.setSalePriceMulCount(title[26]);
    //            titleRow.setFreightMoney(title[27]);
    //            titleRow.setTotalPrice(title[28]);
    //            titleRow.setRealPrice(title[29]);
    //            titleRow.setAccountPointPrice(title[30]);
    //            titleRow.setCouponPrice(title[31]);
    //            titleRow.setAdjustPrice(title[32]);
    //            titleRow.setSinglePayPrice(title[33]);
    //            titleRow.setCost(title[34]);
    //            titleRow.setSingleGross(title[35]);
    //            titleRow.setTotalSinglePayPrice(title[36]);
    //            titleRow.setTotalCost(title[37]);
    //            titleRow.setTotalGross(title[38]);
    //            titleRow.setBuyerRemark(title[39]);
    //            titleRow.setSellerRemark(title[40]);
    //            titleRow.setSendTime(title[41]);
    //            titleRow.setLogisticsChannel(title[42]);
    //            titleRow.setLogisticsNumber(title[43]);
    //            titleRow.setUserType(title[44]);
    //            titleRow.setAccountName(title[45]);
    //            titleRow.setPostageSettlementStatus(title[46]);
    //            titleRow.setPostageConfirmDate(title[47]);
    //            titleRow.setPostage(title[48]);
    //            titleRow.setRefundId(title[49]);
    //            titleRow.setRefundType(title[50]);
    //            titleRow.setRefundCount(title[51]);
    //            titleRow.setRefundPrice(title[52]);
    //            titleRow.setSellerRefundPrice(title[53]);
    //            titleRow.setGegeRefundPrice(title[54]);
    //            titleRow.setRefundStatus(title[55]);
    //            titleRow.setMoneyStatus(title[56]);
    //            titleRow.setReceiveGoodsStatus(title[57]);
    //            titleRow.setRefundSettlementStatus(title[58]);
    //            titleRow.setSettlementComfirmDate(title[59]);
    //            titleRow.setResponsibilityPosition(title[60]);
    //            titleRow.setResponsibilityMoney(title[61]);
    //            rows.add(titleRow);
    //
    //            for (OrderFinanceView ofv : ofvs)
    //            {
    //                List<LineItemView> livs = ofv.getLineItems();
    //                for (LineItemView liv : livs)
    //                {
    //                    maxRowNum++;
    //                    com.ygg.admin.excel.Row row = new com.ygg.admin.excel.Row();
    //                    row.setOrderType(ofv.getOrderType());//
    //                    row.setNumber(ofv.getNumber());//
    //                    row.setHbNumber(ofv.getHbNumber());//
    //                    row.setStatus(ofv.getStatus());//
    //                    row.setSettlement(ofv.getSettlement());//
    //                    row.setSettlementTime(ofv.getSettlementTime());
    //                    row.setPayChannel(ofv.getPayChannel());//
    //                    row.setCreateTime(ofv.getCreateTime());//
    //                    row.setPayTime(ofv.getPayTime());//
    //                    row.setSendTime(ofv.getSendTime());//
    //                    row.setSellerName("<![CDATA[" + ofv.getSellerName() + "]]>");//
    //                    row.setSendAddress("<![CDATA[" + ofv.getSendAddress() + "]]>");//
    //                    row.setReceiveFullName("<![CDATA[" + ofv.getReceiveFullName() + "]]>");//
    //                    row.setIdCard(ofv.getIdCard());//
    //                    row.setReceiveAddress("<![CDATA[" + ofv.getReceiveAddress() + "]]>");//
    //                    row.setProvince(ofv.getProvince());//
    //                    row.setCity(ofv.getCity());//
    //                    row.setDistrict(ofv.getDistrict());//
    //                    row.setDetailAddress("<![CDATA[" + ofv.getDetailAddress() + "]]>");//
    //                    row.setMobileNumber(ofv.getMobileNumber());//
    //                    row.setType(liv.getType());//
    //                    row.setProductId(liv.getProductId());//
    //                    row.setCode(liv.getCode());//
    //                    row.setProductName("<![CDATA[" + liv.getName() + "]]>");//
    //                    row.setCount(liv.getCount() + "");//
    //                    row.setSalePrice(liv.getSalePrice());
    //                    row.setSalePriceMulCount(liv.getSalePriceMulCount());
    //                    row.setFreightMoney(ofv.getFreightMoney() + "");
    //                    row.setTotalPrice(ofv.getTotalPrice());//
    //                    row.setRealPrice(ofv.getRealPrice());//
    //                    row.setAccountPointPrice(liv.getAccountPointPrice());//
    //                    row.setCouponPrice(liv.getCouponPrice());//
    //                    row.setAdjustPrice(liv.getAdjustPrice());//
    //                    row.setSinglePayPrice(liv.getSinglePayPrice());
    //                    row.setCost(liv.getCost());
    //                    row.setSingleGross(liv.getSingleGross());//
    //                    row.setTotalSinglePayPrice(liv.getTotalSinglePayPrice());//
    //                    row.setTotalCost(liv.getTotalCost());//
    //                    row.setTotalGross(liv.getTotalGross());//
    //                    row.setBuyerRemark("<![CDATA[" + ofv.getBuyerRemark() + "]]>");
    //                    row.setSellerRemark("<![CDATA[" + ofv.getSellerRemark() + "]]>");
    //                    row.setSendTime(ofv.getSendTime());//
    //                    row.setLogisticsChannel(ofv.getLogisticsChannel());
    //                    row.setLogisticsNumber(ofv.getLogisticsNumber());
    //                    row.setUserType(ofv.getUserType());
    //                    row.setAccountName("<![CDATA[" + ofv.getName() + "]]>");//
    //                    row.setPostageSettlementStatus(ofv.getPostageSettlementStatus());
    //                    row.setPostageConfirmDate(ofv.getPostageConfirmDate());
    //                    row.setPostage(ofv.getPostage());
    //                    row.setRefundId(liv.getRefundId() == 0 ? "" : liv.getRefundId() + "");//
    //                    row.setRefundType(liv.getRefundType());
    //                    row.setRefundCount(liv.getRefundCount() == 0 ? "" : liv.getRefundCount() + "");//
    //                    row.setRefundPrice(liv.getRefundPrice());
    //                    row.setSellerRefundPrice(liv.getSellerRefundPrice());
    //                    row.setGegeRefundPrice(liv.getGegeRefundPrice());
    //                    row.setRefundStatus(liv.getRefundStatus());
    //                    row.setMoneyStatus(liv.getMoneyStatus());
    //                    row.setReceiveGoodsStatus(liv.getReceiveGoodsStatus());
    //                    row.setRefundSettlementStatus(liv.getRefundSettlementStatus());
    //                    row.setSettlementComfirmDate(liv.getSettlementComfirmDate());
    //                    row.setResponsibilityPosition(liv.getResponsibilityPosition());
    //                    row.setResponsibilityMoney(liv.getResponsibilityMoney());
    //                    rows.add(row);
    //                }
    //            }
    //
    //            StringTemplate body = stGroup.getInstanceOf("body");
    //            WorksheetTest worksheet = new WorksheetTest();
    //            worksheet.setSheet("1");
    //            worksheet.setColumnNum(title.length);
    //            worksheet.setRowNum(maxRowNum);
    //
    //            worksheet.setRows(rows);
    //            body.setAttribute("worksheet", worksheet);
    //            writer.write(body.toString());
    //            writer.flush();
    //            rows.clear();
    //            rows = null;
    //            worksheet = null;
    //            body = null;
    //            Runtime.getRuntime().gc();
    //
    //            // 写入excel文件尾部
    //            writer.write("</Workbook>");
    //            writer.flush();
    //            writer.close();
    //            System.out.println("生成excel文件完成");
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("导出发货excel出错", e);
    //            throw new Exception(e);
    //        }
    //        finally
    //        {
    //
    //        }
    //    }
    
    private void writeToExcel(File dir, String postfix, List<OrderFinanceView> ofvs, List<Integer> sellerIdList)
        throws Exception
    {
        int sellerId = (sellerIdList == null || sellerIdList.isEmpty()) ? -1 : sellerIdList.get(0);
        OutputStream fOut = null;
        SXSSFWorkbook workbook = null;
        boolean alreadyFileName = false;
        String fileName = "结算清单_";
        try
        {
            // 产生工作簿对象
            String[] title =
                {"订单渠道", "订单类型", "订单编号", "合并订单号", "订单状态", "结算状态", "结算时间", "付款渠道", "创建时间", "付款日期", "发货时间", "商家", "发货地", "收货人", "身份证号码", "收货地址", "省", "市", "区", "详细地址", "联系电话",
                    "商品类型", "商品ID", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "实付金额", "积分优惠", "优惠券优惠", "满减金额", "客服调价", "单位实付金额", "单位供货价", "单位实付毛利", "总实付金额", "总供货价", "总实付毛利",
                    "客服备注 ", "卖家备注", "发货时间", "物流公司", "物流单号", "用户类型", "下单用户名", "运费结算状态", "运费结算时间", "运费金额", "退款ID", "退款类型", "退款数量", "退款金额", "商家退款金额", "左岸城堡退款金额", "退款状态", "是否已打款",
                    "是否已收货", "结算状态", "结算时间", "承担方", "承担金额", "是否超时", "罚款金额", "罚款状态", "罚款时间", "应付商家", "买家实付", "佣金", "核对", "左岸城堡佣金", "N元任选优惠"};
            workbook = POIUtil.createSXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            
            int rowIndex = 0;
            for (OrderFinanceView ofv : ofvs)
            {
                if (sellerId != -1 && !alreadyFileName)
                {
                    // 将商家作为文件名
                    alreadyFileName = true;
                    fileName += ofv.getSellerName() + "_" + ofv.getSendAddress();
                    if (sellerIdList.size() > 1)
                    {
                        fileName += "等";
                    }
                }
                int _index = 0;
                List<LineItemView> livs = ofv.getLineItems();
                for (LineItemView liv : livs)
                {
                    int cellIndex = 0;
                    Row row = sheet.createRow(rowIndex + 1);
                    row.createCell(cellIndex++).setCellValue(ofv.getOrderChannel());//
                    row.createCell(cellIndex++).setCellValue(ofv.getOrderType());//
                    row.createCell(cellIndex++).setCellValue(ofv.getNumber());//
                    row.createCell(cellIndex++).setCellValue(ofv.getHbNumber());//
                    row.createCell(cellIndex++).setCellValue(ofv.getStatus());//
                    row.createCell(cellIndex++).setCellValue(ofv.getSettlement());//
                    row.createCell(cellIndex++).setCellValue(ofv.getSettlementTime());
                    row.createCell(cellIndex++).setCellValue(ofv.getPayChannel());//
                    row.createCell(cellIndex++).setCellValue(ofv.getCreateTime());//
                    row.createCell(cellIndex++).setCellValue(ofv.getPayTime());//
                    row.createCell(cellIndex++).setCellValue(ofv.getSendTime());//
                    row.createCell(cellIndex++).setCellValue(ofv.getSellerName());//
                    row.createCell(cellIndex++).setCellValue(ofv.getSendAddress());//
                    row.createCell(cellIndex++).setCellValue(ofv.getReceiveFullName());//
                    row.createCell(cellIndex++).setCellValue(ofv.getIdCard());//
                    row.createCell(cellIndex++).setCellValue(ofv.getReceiveAddress());//
                    row.createCell(cellIndex++).setCellValue(ofv.getProvince());//
                    row.createCell(cellIndex++).setCellValue(ofv.getCity());//
                    row.createCell(cellIndex++).setCellValue(ofv.getDistrict());//
                    row.createCell(cellIndex++).setCellValue(ofv.getDetailAddress());//
                    row.createCell(cellIndex++).setCellValue(ofv.getMobileNumber());//
                    row.createCell(cellIndex++).setCellValue(liv.getType());//
                    row.createCell(cellIndex++).setCellValue(liv.getProductId());//
                    row.createCell(cellIndex++).setCellValue(liv.getCode());//
                    row.createCell(cellIndex++).setCellValue(liv.getName());//
                    row.createCell(cellIndex++).setCellValue(liv.getCount());//
                    row.createCell(cellIndex++).setCellValue(liv.getSalePrice());
                    row.createCell(cellIndex++).setCellValue(liv.getSalePriceMulCount());
                    row.createCell(cellIndex++).setCellValue(_index == 0 ? ofv.getFreightMoney() : 0);
                    row.createCell(cellIndex++).setCellValue(ofv.getTotalPrice());//
                    row.createCell(cellIndex++).setCellValue(ofv.getRealPrice());//
                    row.createCell(cellIndex++).setCellValue(liv.getAccountPointPrice());//
                    row.createCell(cellIndex++).setCellValue(liv.getCouponPrice());//
                    row.createCell(cellIndex++).setCellValue(liv.getActivitiesPrice());//
                    row.createCell(cellIndex++).setCellValue(liv.getAdjustPrice());//
                    row.createCell(cellIndex++).setCellValue(liv.getSinglePayPrice());
                    row.createCell(cellIndex++).setCellValue(liv.getCost());
                    row.createCell(cellIndex++).setCellValue(liv.getSingleGross());//
                    row.createCell(cellIndex++).setCellValue(liv.getTotalSinglePayPrice());//
                    row.createCell(cellIndex++).setCellValue(liv.getTotalCost());//
                    row.createCell(cellIndex++).setCellValue(liv.getTotalGross());//
                    row.createCell(cellIndex++).setCellValue(ofv.getBuyerRemark());
                    row.createCell(cellIndex++).setCellValue(ofv.getSellerRemark());
                    row.createCell(cellIndex++).setCellValue(ofv.getSendTime());//
                    row.createCell(cellIndex++).setCellValue(ofv.getLogisticsChannel());
                    row.createCell(cellIndex++).setCellValue(ofv.getLogisticsNumber());
                    row.createCell(cellIndex++).setCellValue(ofv.getUserType());
                    row.createCell(cellIndex++).setCellValue(ofv.getName());//
                    row.createCell(cellIndex++).setCellValue(ofv.getPostageSettlementStatus());
                    row.createCell(cellIndex++).setCellValue(ofv.getPostageConfirmDate());
                    row.createCell(cellIndex++).setCellValue(ofv.getPostage());
                    row.createCell(cellIndex++).setCellValue(liv.getRefundId() == 0 ? "" : liv.getRefundId() + "");//
                    row.createCell(cellIndex++).setCellValue(liv.getRefundType());
                    row.createCell(cellIndex++).setCellValue(liv.getRefundCount() == 0 ? "" : liv.getRefundCount() + "");//
                    row.createCell(cellIndex++).setCellValue(liv.getRefundPrice());
                    row.createCell(cellIndex++).setCellValue(liv.getSellerRefundPrice());
                    row.createCell(cellIndex++).setCellValue(liv.getGegeRefundPrice());
                    row.createCell(cellIndex++).setCellValue(liv.getRefundStatus());
                    row.createCell(cellIndex++).setCellValue(liv.getMoneyStatus());
                    row.createCell(cellIndex++).setCellValue(liv.getReceiveGoodsStatus());
                    row.createCell(cellIndex++).setCellValue(liv.getRefundSettlementStatus());
                    row.createCell(cellIndex++).setCellValue(liv.getSettlementComfirmDate());
                    row.createCell(cellIndex++).setCellValue(liv.getResponsibilityPosition());
                    row.createCell(cellIndex++).setCellValue(liv.getResponsibilityMoney());
                    row.createCell(cellIndex++).setCellValue(ofv.getIsTimeOut());
                    row.createCell(cellIndex++).setCellValue(_index == 0 ? ofv.getPenaltyMoney() : "0");
                    row.createCell(cellIndex++).setCellValue(ofv.getIsPenalty());
                    row.createCell(cellIndex++).setCellValue(ofv.getPenaltyTime());
                    row.createCell(cellIndex + 4).setCellValue(liv.getTotalWithdrawCash());
                    row.createCell(cellIndex + 5).setCellValue(liv.getActivitiesOptionalPartPrice());
                    rowIndex++;
                    _index++;
                }
            }
            fileName += postfix;
            File file = new File(dir, fileName);
            file.createNewFile();
            fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.flush();
            Runtime.getRuntime().gc();
        }
        catch (Exception e)
        {
            log.error("导出发货excel出错", e);
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
                    workbook.dispose();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //    private void writeToExcel(File dir, String postfix, List<OrderFinanceView> ofvs, List<Integer> sellerIdList)
    //        throws Exception
    //    {
    //        int sellerId = (sellerIdList == null || sellerIdList.isEmpty()) ? -1 : sellerIdList.get(0);
    //        OutputStream fOut = null;
    //        Workbook workbook = null;
    //        boolean alreadyFileName = false;
    //        String fileName = "结算清单_";
    //        try
    //        {
    //            // 产生工作簿对象
    //            String[] title = {"订单渠道", "订单类型", "订单编号", "合并订单号", "订单状态", "结算状态", "结算时间", "付款渠道", "创建时间", "付款日期", "发货时间", "商家", "发货地", "收货人", "身份证号码", "收货地址", "省", "市", "区", "详细地址",
    //                "联系电话", "商品类型", "商品ID", "商品编号", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "实付金额", "积分优惠", "优惠券优惠", "满减金额", "客服调价", "单位实付金额", "单位供货价", "单位实付毛利", "总实付金额", "总供货价",
    //                "总实付毛利", "客服备注 ", "卖家备注", "发货时间", "物流公司", "物流单号", "用户类型", "下单用户名", "运费结算状态", "运费结算时间", "运费金额", "退款ID", "退款类型", "退款数量", "退款金额", "商家退款金额", "左岸城堡退款金额", "退款状态", "是否已打款",
    //                "是否已收货", "结算状态", "结算时间", "承担方", "承担金额"};
    //            workbook = POIUtil.createXSSFWorkbookTemplate(title);
    //            Sheet sheet = workbook.getSheetAt(0);
    //
    //            int rowIndex = 0;
    //            for (OrderFinanceView ofv : ofvs)
    //            {
    //                if (sellerId != -1 && !alreadyFileName)
    //                {
    //                    // 将商家作为文件名
    //                    alreadyFileName = true;
    //                    fileName += ofv.getSellerName() + "_" + ofv.getSendAddress();
    //                    if (sellerIdList.size() > 1)
    //                    {
    //                        fileName += "等";
    //                    }
    //                }
    //                List<LineItemView> livs = ofv.getLineItems();
    //                for (LineItemView liv : livs)
    //                {
    //                    int cellIndex = 0;
    //                    Row row = sheet.createRow(rowIndex + 1);
    //                    row.createCell(cellIndex++).setCellValue(ofv.getOrderChannel());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getOrderType());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getNumber());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getHbNumber());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getStatus());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSettlement());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSettlementTime());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getPayChannel());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getCreateTime());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getPayTime());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSendTime());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSellerName());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSendAddress());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getReceiveFullName());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getIdCard());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getReceiveAddress());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getProvince());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getCity());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getDistrict());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getDetailAddress());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getMobileNumber());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getType());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getProductId());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getCode());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getName());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getCount());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getSalePrice());
    //                    row.createCell(cellIndex++).setCellValue(liv.getSalePriceMulCount());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getFreightMoney());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getTotalPrice());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getRealPrice());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getAccountPointPrice());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getCouponPrice());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getActivitiesPrice());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getAdjustPrice());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getSinglePayPrice());
    //                    row.createCell(cellIndex++).setCellValue(liv.getCost());
    //                    row.createCell(cellIndex++).setCellValue(liv.getSingleGross());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getTotalSinglePayPrice());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getTotalCost());//
    //                    row.createCell(cellIndex++).setCellValue(liv.getTotalGross());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getBuyerRemark());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSellerRemark());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getSendTime());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getLogisticsChannel());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getLogisticsNumber());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getUserType());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getName());//
    //                    row.createCell(cellIndex++).setCellValue(ofv.getPostageSettlementStatus());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getPostageConfirmDate());
    //                    row.createCell(cellIndex++).setCellValue(ofv.getPostage());
    //                    row.createCell(cellIndex++).setCellValue(liv.getRefundId() == 0 ? "" : liv.getRefundId() + "");//
    //                    row.createCell(cellIndex++).setCellValue(liv.getRefundType());
    //                    row.createCell(cellIndex++).setCellValue(liv.getRefundCount() == 0 ? "" : liv.getRefundCount() + "");//
    //                    row.createCell(cellIndex++).setCellValue(liv.getRefundPrice());
    //                    row.createCell(cellIndex++).setCellValue(liv.getSellerRefundPrice());
    //                    row.createCell(cellIndex++).setCellValue(liv.getGegeRefundPrice());
    //                    row.createCell(cellIndex++).setCellValue(liv.getRefundStatus());
    //                    row.createCell(cellIndex++).setCellValue(liv.getMoneyStatus());
    //                    row.createCell(cellIndex++).setCellValue(liv.getReceiveGoodsStatus());
    //                    row.createCell(cellIndex++).setCellValue(liv.getRefundSettlementStatus());
    //                    row.createCell(cellIndex++).setCellValue(liv.getSettlementComfirmDate());
    //                    row.createCell(cellIndex++).setCellValue(liv.getResponsibilityPosition());
    //                    row.createCell(cellIndex++).setCellValue(liv.getResponsibilityMoney());
    //                    rowIndex++;
    //                }
    //            }
    //            fileName += postfix;
    //            File file = new File(dir, fileName);
    //            file.createNewFile();
    //            fOut = new FileOutputStream(file);
    //            workbook.write(fOut);
    //            fOut.flush();
    //            Runtime.getRuntime().gc();
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("导出发货excel出错", e);
    //            throw new Exception(e);
    //        }
    //        finally
    //        {
    //            if (fOut != null)
    //            {
    //                try
    //                {
    //                    fOut.close();
    //                }
    //                catch (Exception e)
    //                {
    //                    e.printStackTrace();
    //                }
    //            }
    //            if (workbook != null)
    //            {
    //                try
    //                {
    //                    workbook.close();
    //                }
    //                catch (Exception e)
    //                {
    //                    e.printStackTrace();
    //                }
    //            }
    //            workbook = null;
    //        }
    //    }
    
    private void setOrderFinanceViewForType2(List<OrderFinanceView> ofvs, Map<String, Object> para)
        throws Exception
    {
        List<OrderListDetailView> oldvs = financeDao.findOrderManualInfoDetail(para);
        if (oldvs.size() > 0)
        {
            // 商品ID集合
            Set<Integer> productIdList = new HashSet<Integer>();
            // 商家ID集合
            Set<Integer> sellerIdList = new HashSet<Integer>();
            for (OrderListDetailView oldv : oldvs)
            {
                productIdList.add(oldv.getProductId());
                sellerIdList.add(oldv.getSellerId());
            }
            
            // 获得商家信息
            // key 为商家id
            Map<String, Map<String, Object>> sellerInfoMap = new HashMap<String, Map<String, Object>>();
            if (sellerIdList.size() > 0)
            {
                Map<String, Object> sellerSearchMap = new HashMap<String, Object>();
                sellerSearchMap.put("idList", CommonUtil.setToList(sellerIdList));
                List<Map<String, Object>> sellerInfoList = sellerDao.findSellerInfoBySellerIdList(sellerSearchMap);
                for (Map<String, Object> seller : sellerInfoList)
                {
                    String id = seller.get("id") + "";
                    sellerInfoMap.put(id, seller);
                }
            }
            // 获得商品信息
            Map<String, Object> smap = new HashMap<String, Object>();
            smap.put("idList", CommonUtil.setToList(productIdList));
            List<Map<String, Object>> pinfoList = financeDao.findProductInfoDetail(smap);
            // key 为商品id
            Map<String, Map<String, Object>> productInfoMap = new HashMap<String, Map<String, Object>>();
            Set<Integer> productBaseIdList = new HashSet<Integer>();
            for (Map<String, Object> p : pinfoList)
            {
                String id = p.get("id") + "";
                productInfoMap.put(id, p);
                String productBaseId = p.get("productBaseId") + "";
                productBaseIdList.add(Integer.parseInt(productBaseId));
            }
            // 获得基本商品信息
            // Map<String, Object> map = new HashMap<String, Object>();
            // map.put("idList", productBaseIdList);
            // List<Map<String, Object>> pbaseinfoList = financeDao.findProductBaseInfoDetail(map);
            // key 为基本商品ID
            // Map<String, Map<String, Object>> productBaseInfoMap = new HashMap<String, Map<String, Object>>();
            // for (Map<String, Object> p : pbaseinfoList)
            // {
            // String id = p.get("id") + "";
            // productBaseInfoMap.put(id, p);
            // }
            // 将oldvs 按订单ID分组
            List<OrderListDetailView> newOldvs = new ArrayList<OrderListDetailView>();
            Map<String, Integer> indexmap = new HashMap<String, Integer>();
            for (int i = 0, j = 0; i < oldvs.size(); i++)
            {
                OrderListDetailView cv = oldvs.get(i);
                String orderId = cv.getId() + "";
                List<Map<String, Object>> productList = null;
                if (indexmap.containsKey(orderId))
                {
                    Integer index = indexmap.get(orderId);
                    OrderListDetailView ocv = newOldvs.get(index);
                    productList = ocv.getProductList();
                }
                else
                {
                    indexmap.put(orderId, j);
                    productList = new ArrayList<>();
                    cv.setProductList(productList);
                    newOldvs.add(cv);
                    j++;
                }
                Map<String, Object> productInfo = new HashMap<String, Object>();
                productInfo.put("id", cv.getOrderManualProductId());
                productInfo.put("salesPrice", cv.getSalesPrice());
                productInfo.put("productId", cv.getProductId());
                productInfo.put("productCount", cv.getProductCount());
                productInfo.put("cost", cv.getCost());
                Map<String, Object> p = productInfoMap.get(cv.getProductId() + "");
                productInfo.put("code", p.get("code"));
                productInfo.put("name", p.get("name"));
                productInfo.put("type", p.get("type"));
                productInfo.put("productBaseId", p.get("productBaseId"));
                productList.add(productInfo);
            }
            oldvs = null;
            for (OrderListDetailView it : newOldvs)
            {
                // 封装基本订单信息
                OrderFinanceView ofv = getOrderFinanceViewByOrderListDetailView(it);
                ofv.setOrderType("手工订单");
                ofv.setStatus(it.getStatus() == 1 ? "待发货" : (it.getStatus() == 2 ? "已发货" : "客服取消"));
                
                // 商家信息
                Map<String, Object> sellerInfo = sellerInfoMap.get(it.getSellerId() + "");
                if (sellerInfo != null)
                {
                    ofv.setSellerName(sellerInfo.get("realSellerName") + "");
                    ofv.setSendAddress(sellerInfo.get("sendAddress") + "");
                }
                // 商品信息
                List<LineItemView> lineItems = new ArrayList<LineItemView>();
                List<Map<String, Object>> orderProductList = it.getProductList();
                for (Map<String, Object> cit : orderProductList)
                {
                    // 循环订单商品
                    // String productBaseId = cit.get("productBaseId") + "";
                    // Map<String, Object> base = productBaseInfoMap.get(productBaseId);
                    LineItemView liv = getLineItemView(it, cit, orderProductList);
                    lineItems.add(liv);
                }
                ofv.setLineItems(lineItems);
                ofvs.add(ofv);
            }
        }
    }
    
    private void setOrderFinanceViewForType1(List<OrderFinanceView> ofvs, Map<String, Object> para)
        throws Exception
    {
        List<OrderListDetailView> oldvs = financeDao.findOrderInfoDetail(para);
        if (oldvs.size() > 0)
        {
            List<Integer> orderIdList = new ArrayList<Integer>();
            List<Integer> hqbsOrderIdList = new ArrayList<Integer>();
            for (OrderListDetailView it : oldvs)
            {
                orderIdList.add(it.getId());
                if (it.getType() == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                {
                    hqbsOrderIdList.add(it.getId());
                }
            }
            Map<String, Object> orderIdListMap = new HashMap<String, Object>();
            orderIdListMap.put("idList", orderIdList);
            
            // 获取售后问题 复制到客服备注中
            List<Map<String, Object>> questionList = orderQuestionDao.findOrderQuestionDescListByOrderId(orderIdList);
            Map<String, String> questionMap = new HashMap<>();
            for (Map<String, Object> map : questionList)
            {
                String orderId = map.get("orderId") + "";
                String desc = map.get("desc") == null ? null : map.get("desc") + "";
                if (desc != null)
                {
                    String remark = questionMap.get(orderId) == null ? "" : questionMap.get(orderId) + "--问题分隔符--";
                    remark += desc;
                    questionMap.put(orderId, remark);
                }
            }

            // 获取左岸城堡返利信息
            List<Map<String, Object>> hqbsList = financeDao.findOrderHQBSInfoDetail(hqbsOrderIdList);
            Map<Integer, Double> hqbsMap = new HashMap<>();
            for (Map<String, Object> map : hqbsList)
            {
                Double totalWithdrawCash = Double.valueOf(map.get("totalWithdrawCash") + "");
                Integer orderId = Integer.valueOf(map.get("orderId") + "");
                hqbsMap.put(orderId, totalWithdrawCash);
            }
            for (OrderListDetailView it : oldvs)
            {
                if (it.getType() == OrderEnum.ORDER_TYPE.HUANQIUBUSHOU.getCode())
                {
                    Integer oId = it.getId();
                    if (hqbsMap.get(oId) != null && hqbsMap.get(oId) > 0)
                    {
                        it.setTotalWithdrawCash(hqbsMap.get(oId));
                    }
                }
            }
            
            // 获取订单商品信息
            List<Map<String, Object>> oplist = financeDao.findOrderProductInfoDetail(orderIdListMap);
            // 基本商品ID集合
            Set<Integer> productBaseIdList = new HashSet<>();
            // key为订单ID
            Map<String, List<Map<String, Object>>> opMap = new HashMap<>();
            for (Map<String, Object> it : oplist)
            {
                productBaseIdList.add(Integer.parseInt(it.get("productBaseId") + ""));
                //
                String orderId = it.get("orderId") + "";
                List<Map<String, Object>> curr = opMap.get(orderId);
                if (curr == null)
                {
                    curr = new ArrayList<>();
                    opMap.put(orderId, curr);
                }
                curr.add(it);
            }
            // 商品成本计算
            // Map<String, Object> basePara = new HashMap<String, Object>();
            // basePara.put("idList", CommonUtil.setToList(productBaseIdList));
            // List<Map<String, Object>> productBaseInfo = financeDao.findProductBaseInfoDetail(basePara);
            // key为productBaseId
            // Map<String, Map<String, Object>> costMap = new HashMap<String, Map<String, Object>>();
            // for (Map<String, Object> it : productBaseInfo)
            // {
            // String baseId = it.get("id") + "";
            // costMap.put(baseId, it);
            // }
            
            // 获取退款退货信息
            List<Map<String, Object>> refundInfo = financeDao.findOrderRefundInfoDetail(orderIdListMap);
            // key为订单order_product.id
            Map<String, Map<String, Object>> orderProductRefundMap = new HashMap<String, Map<String, Object>>();
            for (Map<String, Object> it : refundInfo)
            {
                String orderProductId = it.get("orderProductId") + "";
                orderProductRefundMap.put(orderProductId, it);
            }
            
            for (OrderListDetailView it : oldvs)
            {
                String orderId = it.getId() + "";
                
                // 客服备注
                it.setRemark2(questionMap.get(orderId) == null ? "" : questionMap.get(orderId));
                
                // 封装基本订单信息
                OrderFinanceView ofv = getOrderFinanceViewByOrderListDetailView(it);
                // 商品信息
                List<LineItemView> lineItems = new ArrayList<LineItemView>();
                List<Map<String, Object>> orderProductList = opMap.get(orderId);
                if (orderProductList == null)
                { // 过滤历史垃圾数据
                    System.out.println("发现垃圾订单，orderId:" + orderId);
                    continue;
                }
                for (Map<String, Object> cit : orderProductList)
                {
                    // 循环订单商品
                    // String productBaseId = cit.get("productBaseId") + "";
                    String orderProductId = cit.get("id") + "";
                    // Map<String, Object> base = costMap.get(productBaseId);
                    // System.out.println(orderProductList);
                    LineItemView liv = getLineItemView(it, cit, orderProductList);
                    Map<String, Object> refund = orderProductRefundMap.get(orderProductId);
                    // 设置退款退货信息
                    setLineItemViewRefundInfo(liv, refund);
                    lineItems.add(liv);
                }
                ofv.setLineItems(lineItems);
                ofvs.add(ofv);
            }
        }
    }
    
    private void setLineItemViewRefundInfo(LineItemView liv, Map<String, Object> refund)
        throws Exception
    {
        if (refund != null)
        {
            int id = Integer.parseInt(refund.get("id") + "");
            int type = Integer.parseInt(refund.get("type") + "");
            int count = Integer.parseInt(refund.get("count") + "");
            int status = Integer.parseInt(refund.get("status") + "");
            double realMoney = Double.parseDouble(refund.get("realMoney") + "");
            int isSettlement = Integer.parseInt(refund.get("isSettlement") + "");
            int responsibilitySide = Integer.parseInt(refund.get("responsibilitySide") + "");
            double responsibilityMoney = Double.parseDouble(refund.get("responsibilityMoney") + "");
            String sellerRefundPrice = refund.get("sellerRefundPrice") == null ? "" : refund.get("sellerRefundPrice") + "";
            String gegeRefundPrice = refund.get("gegeRefundPrice") == null ? "" : refund.get("gegeRefundPrice") + "";
            String settlementComfirmDate = refund.get("settlementComfirmDate") == null ? "" : DateTimeUtil.timestampObjectToString(refund.get("settlementComfirmDate"));
            liv.setRefundId(id);
            liv.setRefundType(type == 1 ? "仅退款" : "退款并退货");
            liv.setRefundCount(count);
            liv.setRefundPrice(realMoney + "");
            liv.setSellerRefundPrice(sellerRefundPrice);
            liv.setGegeRefundPrice(gegeRefundPrice);
            liv.setRefundStatus(CommonEnum.RefundStatusEnum.getDescriptionByOrdinal(status));
            liv.setSettlementComfirmDate(settlementComfirmDate);
            if (status == CommonEnum.RefundStatusEnum.SUCCESS.ordinal())
            {
                liv.setMoneyStatus("是");
            }
            else
            {
                liv.setMoneyStatus("否");
            }
            if (type == 2)
            {
                if (status == CommonEnum.RefundStatusEnum.APPLY.ordinal() || status == CommonEnum.RefundStatusEnum.WAIT_RETURN_OF_GOODS.ordinal())
                {
                    liv.setReceiveGoodsStatus("否");
                }
                else
                {
                    liv.setReceiveGoodsStatus("是");
                }
            }
            if (isSettlement == 1)
            {
                liv.setRefundSettlementStatus("已结算");
                liv.setResponsibilityPosition(responsibilitySide == 1 ? "商家" : "我方");
                liv.setResponsibilityMoney(responsibilityMoney + "");
            }
            else
            {
                liv.setRefundSettlementStatus("未结算");
            }
        }
    }
    
    private LineItemView getLineItemViewSimple(OrderListDetailView oldv, Map<String, Object> orderProduct)
        throws Exception
    {
        LineItemView liv = new LineItemView();
        // 刚写完了就要改！！！！！
        // int submitType = Integer.parseInt(base.get("submitType") + "");
        // double wholesalePrice = Double.parseDouble(base.get("wholesalePrice") + "");
        // double proposalPrice = Double.parseDouble(base.get("proposalPrice") + "");
        // double deduction = Double.parseDouble(base.get("deduction") + "");
        
        double salesPrice = Double.parseDouble(orderProduct.get("salesPrice") + "");
        double cost = Double.parseDouble(orderProduct.get("cost") + "");
        String name = orderProduct.get("name") + "";
        String code = orderProduct.get("code") + "";
        String newcode = orderProduct.get("code") + "";
        String productId = orderProduct.get("productId") + "";
        int productType = Integer.parseInt(orderProduct.get("type") + "");
        int productCount = Integer.parseInt(orderProduct.get("productCount") + "");
        // 计算 单位供货价
        // double cost = 0;
        // if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
        // {
        // cost = wholesalePrice;
        // }
        // else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
        // {
        // cost = (100 - deduction) * proposalPrice / 100;
        // }
        int index = code.lastIndexOf("%");
        if ((index > -1) && (index < code.length() - 1))
        {
            // 有效
            String num = code.substring(index + 1);
            if (StringUtils.isNumeric(num))
            {
                productCount = productCount * Integer.valueOf(num);
                salesPrice = salesPrice / Integer.valueOf(num);
                cost = cost / Integer.valueOf(num);
                newcode = newcode.substring(0, index);
            }
        }
        liv.setType(ProductEnum.PRODUCT_TYPE.getDescByCode(productType));
        liv.setProductId(productId);
        liv.setCode(newcode);
        liv.setName(name);
        liv.setCount(productCount);
        liv.setSalePrice(MathUtil.round(salesPrice, 5));
        liv.setSalePriceMulCount(MathUtil.round(salesPrice * productCount, 5));
        liv.setCost(cost + "");
        return liv;
    }
    
    private LineItemView getLineItemView(OrderListDetailView oldv, Map<String, Object> orderProduct, List<Map<String, Object>> orderProductList)
        throws Exception
    {
        LineItemView liv = getLineItemViewSimple(oldv, orderProduct);
        int orderProductId = Integer.parseInt(orderProduct.get("id") + "");
        calOrderProduct(liv, oldv, orderProductId, orderProductList);
        return liv;
    }
    
    public int calOrderProduct(LineItemView liv, OrderListDetailView oldv, int orderProductId, List<Map<String, Object>> orderProductList)
        throws Exception
    {
        double adjustPrice = oldv.getAdjustPrice();
        double freightMoney = oldv.getFreightMoney();
        double totalPrice = oldv.getTotalPrice() - freightMoney; // 订单商品总金额
        double couponPrice = oldv.getCouponPrice(); // 订单优惠券金额
        int accountPoint = oldv.getAccountPoint(); // 订单使用积分
        double activitiesPrice = oldv.getActivitiesPrice(); // 订单满减金额
        double activitiesOptionalPartPrice = oldv.getActivitiesOptionalPartPrice(); // N元任选优惠金额
        double totalWithdrawCash = oldv.getTotalWithdrawCash(); // 左岸城堡优惠金额
        double realPrice = oldv.getRealPrice() - freightMoney;// 订单是否金额 - 邮费

        
        double orderProductTotalPrice = 0;
        // 获取订单商品相关信息
        double salesPrice = 0;
        int productCount = 0;
        for (Map<String, Object> it : orderProductList)
        {
            Integer itId = Integer.parseInt(it.get("id") + "");
            double currsalesPrice = Float.parseFloat(it.get("salesPrice") + "");
            int currproductCount = Integer.parseInt(it.get("productCount") + "");
            if (itId == orderProductId)
            {
                salesPrice = currsalesPrice;
                productCount = currproductCount;
            }
            orderProductTotalPrice += (currsalesPrice * currproductCount);
        }
        
        if (salesPrice > 0 && productCount > 0)
        {
            if (totalPrice == 0)
            {
                totalPrice = orderProductTotalPrice;
            }
            if (totalPrice != 0)
            {
                double selectPrice = productCount * salesPrice;
                double adjustProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * adjustPrice, 5));// 分摊客服改价
                double freightMoneyProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * freightMoney, 5));// 分摊邮费
                // double freightMoneyProportion = 0;//分摊邮费
                double couponProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * couponPrice, 5));// 分摊优惠券金额
                double totalWithdrawCashProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * totalWithdrawCash, 5));// 分摊左岸城堡优惠金额
                double activitiesProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * activitiesPrice, 5));// 分摊满减金额
                double activitiesOptionalPartProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * activitiesOptionalPartPrice, 5));// 分摊满减金额
                double pointProportion = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * accountPoint / 100.0, 5));// 分摊积分抵扣
                liv.setPostage(freightMoneyProportion + "");
                liv.setAccountPointPrice(pointProportion + "");
                liv.setCouponPrice(couponProportion + "");
                liv.setTotalWithdrawCash(totalWithdrawCashProportion > 0 ? totalWithdrawCashProportion + "" :  "");
                liv.setAdjustPrice(adjustProportion + "");
                liv.setActivitiesPrice(activitiesProportion + "");
                liv.setActivitiesOptionalPartPrice(activitiesOptionalPartProportion + "");
                
                double cost = Double.parseDouble(liv.getCost());// 假如商品编码有%2，这里已经除了2
                productCount = liv.getCount();// 假如商品编码有%2，这里已经乘了2
                double singlePayPrice = Double.parseDouble(MathUtil.round((selectPrice / totalPrice) * realPrice / productCount, 5));// 单位实付金额
                liv.setSinglePayPrice(singlePayPrice + "");
                liv.setSingleGross(MathUtil.round((singlePayPrice - cost), 5));
                liv.setTotalSinglePayPrice(MathUtil.round((singlePayPrice * productCount), 5));
                liv.setTotalCost(MathUtil.round((cost * productCount), 5));
                liv.setTotalGross(MathUtil.round(((singlePayPrice - cost) * productCount), 5));
            }
            return 1;
        }
        return 0;
    }
    
    private OrderFinanceView getOrderFinanceViewByOrderListDetailView(OrderListDetailView it)
        throws Exception
    {
        OrderFinanceView ofv = new OrderFinanceView();
        ofv.setOrderType("正常订单");
        if (it.getAppChannel() == CommonEnum.OrderAppChannelEnum.GEGETUAN_WECHAT.ordinal())
        {
            ofv.setOrderChannel("左岸城堡微信");
        }
        else if (it.getAppChannel() == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP.ordinal())
        {
            ofv.setOrderChannel("左岸城堡APP");
        }
        else if (it.getAppChannel() == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_IOS.ordinal())
        {
            ofv.setOrderChannel("左岸城堡APP");
        }
        else if (it.getAppChannel() == CommonEnum.OrderAppChannelEnum.GEGETUAN_APP_ANDROID.ordinal())
        {
            ofv.setOrderChannel("左岸城堡APP");
        }
        else if (it.getAppChannel() == CommonEnum.OrderAppChannelEnum.QUAN_QIU_BU_SHOU.ordinal())
        {
            ofv.setOrderChannel("左岸城堡");
        }
        else
        {
            ofv.setOrderChannel("");
        }
        ofv.setId(it.getId());
        ofv.setNumber(it.getNumber() + "");
        ofv.setHbNumber(it.getHbNumber());
        ofv.setStatus(OrderEnum.ORDER_STATUS.getDescByCode(it.getStatus()));
        if (it.getIsNeedSettlement() == 1)
        {
            ofv.setSettlement(it.getIsSettlement() == 1 ? "已结算" : "未结算");
            ofv.setSettlementTime(it.getSettlementTime());
        }
        else
        {
            ofv.setSettlement("无需结算");
            ofv.setSettlementTime("");
        }
        if (it.getPayChannel() == 0)
        {
            ofv.setPayChannel("");
        }
        else
        {
            ofv.setPayChannel(OrderEnum.PAY_CHANNEL.getDescByCode(it.getPayChannel()));
        }
        ofv.setCreateTime(DateTimeUtil.timestampStringToWebString(it.getCreateTime()));
        ofv.setPayTime(DateTimeUtil.timestampStringToWebString(it.getPayTime()));
        ofv.setSendTime(DateTimeUtil.timestampStringToWebString(it.getSendTime()));
        ofv.setTotalPrice(MathUtil.round(it.getTotalPrice(), 5));
        ofv.setRealPrice(MathUtil.round(it.getRealPrice(), 5));
        ofv.setAccountPointPrice(MathUtil.round(it.getAccountPoint() / 100.0, 5));
        ofv.setCouponPrice(MathUtil.round(it.getCouponPrice(), 5));
        ofv.setAdjustPrice(MathUtil.round(it.getAdjustPrice(), 5));
        ofv.setActivitiesPrice(MathUtil.round(it.getActivitiesPrice(), 5));
        ofv.setSellerRemark(it.getRemark());
        ofv.setBuyerRemark(it.getRemark2());
        ofv.setReceiveFullName(it.getFullName());
        ofv.setIdCard(it.getIdCard());
        //        ofv.setProvince(areaDao.findProvinceNameByProvinceId(Integer.valueOf("".equals(it.getProvince()) ? "0" : it.getProvince())));
        ofv.setProvince(AreaCache.getInstance().getProvinceName("".equals(it.getProvince()) ? "0" : it.getProvince()));
        //        ofv.setCity(areaDao.findCityNameByCityId(Integer.valueOf("".equals(it.getCity()) ? "0" : it.getCity())));
        ofv.setCity(AreaCache.getInstance().getCityName("".equals(it.getCity()) ? "0" : it.getCity()));
        //        ofv.setDistrict(areaDao.findDistrictNameByDistrictId(Integer.valueOf("".equals(it.getDistrict()) ? "0" : it.getDistrict())));
        ofv.setDistrict(AreaCache.getInstance().getDistinctName("".equals(it.getDistrict()) ? "0" : it.getDistrict()));
        ofv.setDetailAddress(it.getDetailAddress());
        String address = ofv.getProvince() + ofv.getCity() + ofv.getDistrict() + ofv.getDetailAddress();
        ofv.setReceiveAddress(address);
        ofv.setMobileNumber(it.getMobileNumber());
        ofv.setSellerName(it.getRealSellerName());
        ofv.setSendAddress(it.getSendAddress());
        /*
         * if (CommonEnum.Kd100CompanyEnum.findCompanyNameByName(it.getLogisticsChannel()).equals("")) {
         * ofv.setLogisticsChannel(it.getLogisticsChannel()); } else {
         * ofv.setLogisticsChannel(CommonEnum.Kd100CompanyEnum.findCompanyNameByName(it.getLogisticsChannel())); }
         */
        ofv.setLogisticsChannel(it.getLogisticsChannel());
        ofv.setLogisticsNumber(it.getLogisticsNumber());
        if (it.getAccountType() == 0)
        {
            ofv.setUserType("");
        }
        else
        {
            ofv.setUserType(AccountEnum.ACCOUNT_TYPE.getDescByCode(it.getAccountType()));
        }
        ofv.setName(it.getAccountName());
        ofv.setFreightMoney(it.getFreightMoney());
        ofv.setPostage(it.getOrderSettlementFreightMoney() + "");
        ofv.setPostageSettlementStatus(it.getOrderSettlementPostageIsSettlement() == 1 ? "已结算" : "未结算");
        ofv.setPostageConfirmDate(it.getPostageConfirmDate());
        // 罚款结算相关
        if (it.getPenaltyTime() != null && !"".equals(it.getPenaltyTime()))
        {
            ofv.setPenaltyTime(DateTimeUtil.timestampStringToWebString(it.getPenaltyTime()));
            ofv.setIsPenalty("已罚款");
            ofv.setIsTimeOut("是");
            ofv.setPenaltyMoney("10");
        }
        else
        {
            if (it.getIsTimeout() == 1)
            {
                ofv.setIsTimeOut("是");
                ofv.setPenaltyMoney("10");
                ofv.setIsPenalty("未罚款");
            }
        }

        return ofv;
    }
    
    @Override
    public Map<String, Object> findSellerSettlementStatistics(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> oes = financeDao.findSellerSettlementStatistics(para);
        Set<Integer> orderIdSet = new HashSet<Integer>();
        for (Map<String, Object> oe : oes)
        {
            Integer orderId = Integer.parseInt(oe.get("id") + "");
            orderIdSet.add(orderId);
        }
        // 将订单按商家ID分组
        Map<String, List<Map<String, Object>>> orderMapByKeySellerId = new HashMap<String, List<Map<String, Object>>>();
        for (Map<String, Object> oe : oes)
        {
            String sellerId = oe.get("sellerId") + "";
            List<Map<String, Object>> oesList = orderMapByKeySellerId.get(sellerId);
            if (oesList == null)
            {
                oesList = new ArrayList<Map<String, Object>>();
                orderMapByKeySellerId.put(sellerId, oesList);
            }
            oesList.add(oe);
        }
        // 获取订单商品信息
        Map<String, List<Map<String, Object>>> orderProductMapByKeyOrderId = new HashMap<String, List<Map<String, Object>>>();
        // 计算商品供货价
        // Map<String, String> productCostMap = new HashMap<String, String>();
        if (orderIdSet.size() > 0)
        {
            Map<String, Object> smap = new HashMap<String, Object>();
            smap.put("idList", CommonUtil.setToList(orderIdSet));
            List<Map<String, Object>> pes = financeDao.findOrderProductInfoForSellerSettlement(smap);
            for (Map<String, Object> pe : pes)
            {
                String orderId = pe.get("orderId") + "";
                List<Map<String, Object>> ops = orderProductMapByKeyOrderId.get(orderId);
                if (ops == null)
                {
                    ops = new ArrayList<>();
                    orderProductMapByKeyOrderId.put(orderId, ops);
                }
                ops.add(pe);
            }
            // for (Map<String, Object> pe : pes)
            // {
            // String id = pe.get("id") + "";
            // if (productCostMap.get(id) == null)
            // {
            // int submitType = Integer.parseInt(pe.get("submitType") + "");
            // double wholesalePrice = Double.parseDouble(pe.get("wholesalePrice") + "");
            // double proposalPrice = Double.parseDouble(pe.get("proposalPrice") + "");
            // double deduction = Double.parseDouble(pe.get("deduction") + "");
            //
            // //计算 单位供货价
            // double cost = 0;
            // if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
            // {
            // cost = wholesalePrice;
            // }
            // else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
            // {
            // cost = (100 - deduction) * proposalPrice / 100;
            // }
            // productCostMap.put(id, cost + "");
            // }
            // }
        }
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        String allRealSellerName = "总计";// 真实商家名称 -- 所有商家
        double allTotalRealPrice = 0;// 总订单实付 -- 所有商家
        double allTotalPaySellerPrice = 0;// 总应付商家 -- 所有商家
        double allTotalGross = 0;// 总毛利 -- 所有商家
        double allTotalFreightMoney = 0;// 总运费 -- 所有商家
        double allTotalProductPrice = 0;// 商品总价 -- 所有商家
        double allTotaladjustProportion = 0;// 总客服改价 -- 所有商家
        double allTotalcouponProportion = 0;// 总优惠券金额 -- 所有商家
        double allTotalActivitiesProportion = 0;// 总满减金额 -- 所有商家
        double allTotalpointProportion = 0;// 总积分抵扣 -- 所有商家
        double allGrossRate = 0;// 毛利率： （ 商品总价 + 总运费 - 总应付商家 ） / （商品总价 + 总运费）
        double allPureGrossRate = 0;// 净毛利率： 总毛利 / 总订单实付
        for (Map.Entry<String, List<Map<String, Object>>> entry : orderMapByKeySellerId.entrySet())
        {
            String realSellerName = "";// 真实商家名称
            double totalRealPrice = 0;// 总订单实付
            double totalPaySellerPrice = 0;// 总应付商家
            double totalGross = 0;// 总毛利
            double totalFreightMoney = 0;// 总运费
            double totalProductPrice = 0;// 商品总价
            double totaladjustProportion = 0;// 总客服改价
            double totalcouponProportion = 0;// 总优惠券金额
            double totalActivitiesProportion = 0; // 总满减金额
            double totalpointProportion = 0;// 总积分抵扣
            double grossRate = 0;// 毛利率： （ 商品总价 + 总运费 - 总应付商家 ） / （商品总价 + 总运费）
            double pureGrossRate = 0;// 净毛利率： 总毛利 / 总订单实付
            List<Map<String, Object>> value = entry.getValue();
            for (Map<String, Object> it : value)
            {
                String orderId = it.get("id") + "";
                double freightMoney = Double.parseDouble(it.get("freightMoney") == null ? "0" : it.get("freightMoney") + "");
                double couponPrice = Double.parseDouble(it.get("couponPrice") == null ? "0" : it.get("couponPrice") + "");
                double activitiesPrice = Double.parseDouble(it.get("activitiesPrice") == null ? "0" : it.get("activitiesPrice") + "");
                double realPrice = Double.parseDouble(it.get("realPrice") == null ? "0" : it.get("realPrice") + "");
                double accountPoint = Integer.parseInt(it.get("accountPoint") == null ? "0" : it.get("accountPoint") + "") / 100.0;
                double adjustPrice = Double.parseDouble(it.get("adjustPrice") == null ? "0" : it.get("adjustPrice") + "");
                realSellerName = "".equals(realSellerName) ? it.get("sellerName") + "" : realSellerName;
                totalRealPrice += realPrice;
                totalFreightMoney += freightMoney;
                totaladjustProportion += adjustPrice;
                totalcouponProportion += couponPrice;
                totalpointProportion += accountPoint;
                totalActivitiesProportion += activitiesPrice;
                
                if (orderProductMapByKeyOrderId.get(orderId) == null)
                {
                    continue;
                }
                
                List<Map<String, Object>> orderProductList = orderProductMapByKeyOrderId.get(orderId);
                for (Map<String, Object> orderProduct : orderProductList)
                {
                    // 商品总价，应付商家
                    // String id = orderProduct.get("id") + "";
                    // double cost = Double.parseDouble(productCostMap.get(id) == null ? "0" : productCostMap.get(id) +
                    // "");
                    int productCount = Integer.parseInt(orderProduct.get("productCount") + "");
                    double cost = Double.parseDouble(orderProduct.get("cost") == null ? "0" : orderProduct.get("cost") + "");
                    double salesPrice = Double.parseDouble(orderProduct.get("salesPrice") + "");
                    totalProductPrice += (salesPrice * productCount);
                    totalPaySellerPrice += (cost * productCount);
                }
            }
            totalGross = totalRealPrice - totalPaySellerPrice;
            Map<String, Object> row = new HashMap<>();
            row.put("realSellerName", realSellerName);
            row.put("totalRealPrice", MathUtil.round(totalRealPrice, 2));
            row.put("totalPaySellerPrice", MathUtil.round(totalPaySellerPrice, 2));
            row.put("totalGross", MathUtil.round(totalGross, 2));
            row.put("totalFreightMoney", MathUtil.round(totalFreightMoney, 2));
            row.put("totalProductPrice", MathUtil.round(totalProductPrice, 2));
            row.put("totaladjustProportion", MathUtil.round(totaladjustProportion, 2));
            row.put("totalcouponProportion", MathUtil.round(totalcouponProportion, 2));
            row.put("totalpointProportion", MathUtil.round(totalpointProportion, 2));
            row.put("totalActivitiesProportion", MathUtil.round(totalActivitiesProportion, 2));
            // 计算毛利率
            grossRate = (totalProductPrice + totalFreightMoney) == 0 ? 0 : (totalProductPrice + totalFreightMoney - totalPaySellerPrice) / (totalProductPrice + totalFreightMoney);
            row.put("grossRate", MathUtil.round(grossRate, 2));
            // 计算净毛利率
            pureGrossRate = totalRealPrice == 0 ? 0 : totalGross / totalRealPrice;
            row.put("pureGrossRate", MathUtil.round(pureGrossRate, 2));
            
            rows.add(row);
            allTotalRealPrice += totalRealPrice;
            allTotalPaySellerPrice += totalPaySellerPrice;
            allTotalGross += totalGross;
            allTotalFreightMoney += totalFreightMoney;
            allTotalProductPrice += totalProductPrice;
            allTotaladjustProportion += totaladjustProportion;
            allTotalcouponProportion += totalcouponProportion;
            allTotalpointProportion += totalpointProportion;
            allTotalActivitiesProportion += totalActivitiesProportion;
        }
        Map<String, Object> lastRow = new HashMap<>();
        lastRow.put("realSellerName", allRealSellerName);
        lastRow.put("totalRealPrice", MathUtil.round(allTotalRealPrice, 2));
        lastRow.put("totalPaySellerPrice", MathUtil.round(allTotalPaySellerPrice, 2));
        lastRow.put("totalGross", MathUtil.round(allTotalGross, 2));
        lastRow.put("totalFreightMoney", MathUtil.round(allTotalFreightMoney, 2));
        lastRow.put("totalProductPrice", MathUtil.round(allTotalProductPrice, 2));
        lastRow.put("totaladjustProportion", MathUtil.round(allTotaladjustProportion, 2));
        lastRow.put("totalcouponProportion", MathUtil.round(allTotalcouponProportion, 2));
        lastRow.put("totalpointProportion", MathUtil.round(allTotalpointProportion, 2));
        lastRow.put("totalActivitiesProportion", MathUtil.round(allTotalActivitiesProportion, 2));
        // 计算毛利率
        allGrossRate =
            (allTotalProductPrice + allTotalFreightMoney) == 0 ? 0 : (allTotalProductPrice + allTotalFreightMoney - allTotalPaySellerPrice)
                / (allTotalProductPrice + allTotalFreightMoney);
        lastRow.put("grossRate", MathUtil.round(allGrossRate, 4));
        // 计算净毛利率
        allPureGrossRate = (allTotalRealPrice == 0) ? 0 : allTotalGross / allTotalRealPrice;
        lastRow.put("pureGrossRate", MathUtil.round(allPureGrossRate, 4));
        
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("lastRow", lastRow);
        return result;
    }
    
    @Override
    public Map<String, Object> findSellerRefundStatistics(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> sellerRefundList = financeDao.findSellerRefundSettlementStatistics(para);
        // key为sellerId
        Map<String, Map<String, List<Map<String, Object>>>> refundMapBySellerId = new HashMap<String, Map<String, List<Map<String, Object>>>>();
        for (Map<String, Object> it : sellerRefundList)
        {
            String sellerId = it.get("sellerId") + "";
            String orderId = it.get("orderId") + "";
            Map<String, List<Map<String, Object>>> refundMapByOrderId = refundMapBySellerId.get(sellerId);
            if (refundMapByOrderId == null)
            {
                refundMapByOrderId = new HashMap<String, List<Map<String, Object>>>();
                refundMapBySellerId.put(sellerId, refundMapByOrderId);
            }
            List<Map<String, Object>> currRefundList = refundMapByOrderId.get(orderId);
            if (currRefundList == null)
            {
                currRefundList = new ArrayList<Map<String, Object>>();
                refundMapByOrderId.put(orderId, currRefundList);
            }
            currRefundList.add(it);
        }
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        String allRealSellerName = "总计";// 真实商家名称 -- 所有商家
        double allTotalRefundPrice = 0;// 总退款金额
        double allTotalPaySellerPrice = 0;// 总应付商家
        double allTotalGross = 0;// 总毛利
        double allTotalFreightMoney = 0;// 总运费
        double allTotalSellerFreightMoney = 0;// 总运费
        for (Map.Entry<String, Map<String, List<Map<String, Object>>>> entryBySellerId : refundMapBySellerId.entrySet())
        {
            String totalRealSellerName = "";// 真实商家名称
            double totalRefundPrice = 0;// 总退款金额
            double totalPaySellerPrice = 0;// 总应付商家
            double totalGross = 0;// 总毛利
            double totalFreightMoney = 0;// 总运费
            double totalSellerFreightMoney = 0;// 总运费
            Map<String, List<Map<String, Object>>> value = entryBySellerId.getValue();
            for (Map.Entry<String, List<Map<String, Object>>> entryByOderId : value.entrySet())
            {
                Set<Integer> orderProductIdSet = new HashSet<Integer>();
                List<Map<String, Object>> crRefundList = entryByOderId.getValue();
                for (Map<String, Object> it : crRefundList)
                {
                    if ("".endsWith(totalRealSellerName))
                    {
                        String realSellerName = it.get("realSellerName") + "";
                        totalRealSellerName = realSellerName;
                    }
                    double refundPrice = Double.parseDouble(it.get("refundMoney") == null ? "0" : it.get("refundMoney") + "");
                    double salesPrice = Double.parseDouble(it.get("salesPrice") == null ? "0" : it.get("salesPrice") + "");
                    double freightMoney = Double.parseDouble(it.get("freightMoney") == null ? "0" : it.get("freightMoney") + "");
                    double cost = Double.parseDouble(it.get("cost") == null ? "0" : it.get("cost") + "");
                    // Integer submitType = Integer.parseInt(it.get("submitType") + "");
                    // double wholesalePrice = Double.parseDouble(it.get("wholesalePrice") == null ? "0" :
                    // it.get("wholesalePrice") + "");
                    // double proposalPrice = Double.parseDouble(it.get("proposalPrice") == null ? "0" :
                    // it.get("proposalPrice") + "");
                    // double deduction = Double.parseDouble(it.get("deduction") == null ? "0" : it.get("deduction") +
                    // "");
                    Integer refundCount = Integer.parseInt(it.get("refundCount") + "");
                    // 计算 单位供货价
                    // double cost = 0;
                    // if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.MONEY_SUBMIT.getCode())
                    // {
                    // cost = wholesalePrice;
                    // }
                    // else if (submitType == ProductEnum.PRODUCT_SUBMIT_TYPE.PERCENT_SUBMIT.getCode())
                    // {
                    // cost = (100 - deduction) * proposalPrice / 100;
                    // }
                    totalRefundPrice += refundPrice;
                    totalPaySellerPrice += (cost * refundCount);
                    totalGross += (salesPrice - cost) * refundCount;
                    orderProductIdSet.add(Integer.parseInt(it.get("orderProductId") + ""));
                    Map<String, Object> smap = new HashMap<String, Object>();
                    smap.put("idList", CommonUtil.setToList(orderProductIdSet));
                    List<Integer> otherOrderProductIds = financeDao.findOrderProductIdByIdAndOrderIdNotInList(smap);
                    if (otherOrderProductIds.size() == 0)
                    {
                        totalFreightMoney += freightMoney;
                    }
                }
            }
            totalSellerFreightMoney = totalRefundPrice - totalPaySellerPrice - totalGross - totalFreightMoney;
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("realSellerName", totalRealSellerName);
            result.put("totalRefundPrice", MathUtil.round(totalRefundPrice, 2));
            result.put("totalPaySellerPrice", MathUtil.round(totalPaySellerPrice, 2));
            result.put("totalGross", MathUtil.round(totalGross, 2));
            result.put("totalFreightMoney", MathUtil.round(totalFreightMoney, 2));
            result.put("totalSellerFreightMoney", MathUtil.round(totalSellerFreightMoney, 2));
            rows.add(result);
            allTotalRefundPrice += totalRefundPrice;
            allTotalPaySellerPrice += totalPaySellerPrice;
            allTotalGross += totalGross;
            allTotalFreightMoney += totalFreightMoney;
            allTotalSellerFreightMoney += totalSellerFreightMoney;
        }
        Map<String, Object> lastRow = new HashMap<String, Object>();
        lastRow.put("realSellerName", allRealSellerName);
        lastRow.put("totalRefundPrice", MathUtil.round(allTotalRefundPrice, 2));
        lastRow.put("totalPaySellerPrice", MathUtil.round(allTotalPaySellerPrice, 2));
        lastRow.put("totalGross", MathUtil.round(allTotalGross, 2));
        lastRow.put("totalFreightMoney", MathUtil.round(allTotalFreightMoney, 2));
        lastRow.put("totalSellerFreightMoney", MathUtil.round(allTotalSellerFreightMoney, 2));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", rows);
        result.put("lastRow", lastRow);
        return result;
    }
    
    @Override
    public Map<String, Object> findSellerRefundStatisticsNew(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> sellerRefundList = financeDao.findSellerRefundProportion(para);
        // key为sellerId
        Map<String, Map<String, List<Map<String, Object>>>> refundMapBySellerId = new HashMap<>();
        for (Map<String, Object> it : sellerRefundList)
        {
            String sellerId = it.get("sellerId") + "";
            String orderId = it.get("orderId") + "";
            Map<String, List<Map<String, Object>>> refundMapByOrderId = refundMapBySellerId.get(sellerId);
            if (refundMapByOrderId == null)
            {
                refundMapByOrderId = new HashMap<>();
                refundMapBySellerId.put(sellerId, refundMapByOrderId);
            }
            List<Map<String, Object>> currRefundList = refundMapByOrderId.get(orderId);
            if (currRefundList == null)
            {
                currRefundList = new ArrayList<>();
                refundMapByOrderId.put(orderId, currRefundList);
            }
            currRefundList.add(it);
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        String allRealSellerName = "总计";// 真实商家名称 -- 所有商家
        double allTotalRefundPrice = 0;// 总退款金额
        double allTotalSellerMoney = 0;// 总商家分摊
        double allTotalGegejiaMoney = 0;// 总左岸城堡分摊
        for (Map.Entry<String, Map<String, List<Map<String, Object>>>> entryBySellerId : refundMapBySellerId.entrySet())
        {
            String totalRealSellerName = "";// 真实商家名称
            double totalRefundPrice = 0;// 总退款金额
            double totalSellerMoney = 0;// 总商家分摊
            double totalGegejiaMoney = 0;// 总左岸城堡分摊
            Map<String, List<Map<String, Object>>> value = entryBySellerId.getValue();
            for (Map.Entry<String, List<Map<String, Object>>> entryByOderId : value.entrySet())
            {
                List<Map<String, Object>> crRefundList = entryByOderId.getValue();
                for (Map<String, Object> it : crRefundList)
                {
                    if ("".endsWith(totalRealSellerName))
                    {
                        String realSellerName = it.get("realSellerName") + "";
                        totalRealSellerName = realSellerName;
                    }
                    double refundPrice = Double.parseDouble(it.get("refundMoney") == null ? "0" : it.get("refundMoney") + "");
                    double sellerMoney = Double.parseDouble(it.get("sellerMoney") == null ? "0" : it.get("sellerMoney") + "");
                    double gegejiaMoney = Double.parseDouble(it.get("gegejiaMoney") == null ? "0" : it.get("gegejiaMoney") + "");
                    totalRefundPrice += refundPrice;
                    totalSellerMoney += sellerMoney;
                    totalGegejiaMoney += gegejiaMoney;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("realSellerName", totalRealSellerName);
            result.put("totalRefundPrice", MathUtil.round(totalRefundPrice, 2));
            result.put("totalSellerMoney", MathUtil.round(totalSellerMoney, 2));
            result.put("totalGegejiaMoney", MathUtil.round(totalGegejiaMoney, 2));
            rows.add(result);
            allTotalRefundPrice += totalRefundPrice;
            allTotalSellerMoney += totalSellerMoney;
            allTotalGegejiaMoney += totalGegejiaMoney;
        }
        Map<String, Object> lastRow = new HashMap<>();
        lastRow.put("realSellerName", allRealSellerName);
        lastRow.put("totalRefundPrice", MathUtil.round(allTotalRefundPrice, 2));
        lastRow.put("totalSellerMoney", MathUtil.round(allTotalSellerMoney, 2));
        lastRow.put("totalGegejiaMoney", MathUtil.round(allTotalGegejiaMoney, 2));
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("lastRow", lastRow);
        return result;
    }
    
    @Override
    public int deletePostageSettlementData(String number, List<Map<String, Object>> confirmList)
        throws Exception
    {
        int status = financeDao.deleteOrderSettlement(Long.valueOf(number));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("orderNumber", number);
        result.put("postage", 0);
        result.put("status", status == 1 ? "撤销成功" : "撤销失败");
        confirmList.add(result);
        return status;
    }
    
    @Override
    public Map<String, Object> findSellerSettlementPeriodData(Map<String, Object> para)
        throws Exception
    {
        Integer remainsDays = para.get("remainsDays") == null ? null : Integer.valueOf(para.get("remainsDays") + "");
        Map<Double, Object> dataMap = new TreeMap<>();
        List<Map<String, Object>> data = financeDao.findOrderInfoForSellerSettlementPeriod(para);
        Map<String, Map<String, Object>> sumDataMapByKeySellerId = new HashMap<>();
        if (data.size() > 0)
        {
            List<Map<String, Object>> sumData = financeDao.sumOrderInfoForSellerSettlementPeriod(para);
            for (Map<String, Object> it : sumData)
            {
                String sellerId = it.get("sellerId") + "";
                sumDataMapByKeySellerId.put(sellerId, it);
            }
        }
        for (Map<String, Object> it : data)
        {
            // 计算数据
            DateTime payTime = new DateTime(DateTimeUtil.string2Date(it.get("payTime") + "").getTime());
            int settlementPeriod = Integer.valueOf(it.get("settlementPeriod") == null ? "1" : it.get("settlementPeriod") + "");
            int settlementDay = Integer.valueOf(it.get("settlementDay") == null ? "1" : it.get("settlementDay") + "");
            String sellerId = it.get("sellerId") + "";
            Map<String, Object> itSumData = sumDataMapByKeySellerId.get(sellerId);
            if (itSumData != null)
            {
                it.put("totalNum", itSumData.get("totalNum"));
                it.put("totalRealPrice", itSumData.get("totalRealPrice"));
            }
            else
            {
                it.put("totalNum", 0);
                it.put("totalRealPrice", 0);
            }
            String settlementRule = "";
            int activityCycleDuration = 0;// 活动周期时长
            DateTime settlementTime = null;// 结算日
            int itRemainsDays = 0;// 距离结算日还有几天
            
            settlementPeriod = 2; // 业务发生变化
            
            if (settlementPeriod == 1)
            {
                settlementRule = "日结";
                activityCycleDuration = 1;
                settlementTime = payTime;
            }
            else if (settlementPeriod == 2)
            {
                settlementRule = "活动结束后" + settlementDay + "天结";
                activityCycleDuration = 5;
                settlementTime = payTime.plusDays(settlementDay + activityCycleDuration - 1);
            }
            else
            {
                settlementRule = "无设置";
                if (remainsDays != null)
                {
                    if (itRemainsDays >= remainsDays)
                    {
                        continue;
                    }
                }
            }
            
            if (settlementTime != null)
            {
                itRemainsDays = DateTimeUtil.daysBetween(DateTime.now().toDate(), settlementTime.toDate());
                if (remainsDays != null)
                {
                    if (itRemainsDays >= remainsDays)
                    {
                        continue;
                    }
                }
            }
            it.put("settlementRule", settlementRule);
            it.put("activityCycleDuration", activityCycleDuration > 0 ? activityCycleDuration + "" : "");
            it.put("settlementTime", settlementTime == null ? "" : settlementTime.toString("yyyy-MM-dd HH:mm:ss"));
            it.put("remainsDays", itRemainsDays);
            it.put("earliestTime", payTime.toString("yyyy-MM-dd HH:mm:ss"));
            double dataMapKey = itRemainsDays;
            while (true)
            {
                if (dataMap.get(dataMapKey) == null)
                {
                    dataMap.put(dataMapKey, it);
                    break;
                }
                else
                {
                    dataMapKey = dataMapKey + 0.001;
                }
            }
        }
        
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map.Entry<Double, Object> entry : dataMap.entrySet())
        {
            Map<String, Object> value = (Map<String, Object>)entry.getValue();
            rows.add(value);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public Map<String, Object> findSellerSettlementSum(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Map<String, Object>> infoBySellerId = new HashMap<>();
        // 1 订单实付 、订单供货总价 正常订单 和 手工订单
        /**
         * o.number AS oNumber, o.real_price AS realPrice, o.seller_id AS sellerId, op.cost AS cost, op.product_count AS
         * productCount
         */
        List<Map<String, Object>> ops = financeDao.findOrderProductByOrderSettlementTime(para);
        List<Map<String, Object>> omps = financeDao.findOrderManualProductByOrderSettlementTime(para);
        Set<Integer> sellerIdSet = new HashSet<>();
        Map<String, Object> opsMapBySellerId = new HashMap<>();
        for (Map<String, Object> it : ops)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            List<Map<String, Object>> opList = (List<Map<String, Object>>)opsMapBySellerId.get(sellerId);
            if (opList == null)
            {
                opList = new ArrayList<>();
                opsMapBySellerId.put(sellerId, opList);
            }
            opList.add(it);
        }
        for (Map<String, Object> it : omps)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            List<Map<String, Object>> opList = (List<Map<String, Object>>)opsMapBySellerId.get(sellerId);
            if (opList == null)
            {
                opList = new ArrayList<>();
                opsMapBySellerId.put(sellerId, opList);
            }
            opList.add(it);
        }
        
        Map<String, String> tempMap = null;
        for (Map.Entry<String, Object> it : opsMapBySellerId.entrySet())
        {
            String sellerId = it.getKey();
            tempMap = new HashMap<>();
            Map<String, Object> currRow = new HashMap<>();
            double totalRealPrice = 0;// 订单实付
            double totalCost = 0; // 订单总供货价
            List<Map<String, Object>> value = (List<Map<String, Object>>)it.getValue();
            for (Map<String, Object> cit : value)
            {
                // 计算总订单供货价
                String number = cit.get("oNumber") + "";
                String realPrice = cit.get("realPrice") + "";
                double cost = Double.valueOf(cit.get("cost") + "");
                double productCount = Double.valueOf(cit.get("productCount") + "");
                totalCost += (cost * productCount);
                
                tempMap.put(number, realPrice);
            }
            
            for (Map.Entry<String, String> map : tempMap.entrySet())
            {
                double cRealPrice = Double.valueOf(map.getValue());
                totalRealPrice += cRealPrice;
            }
            
            currRow.put("sellerId", sellerId);
            currRow.put("totalRealPrice", MathUtil.round(totalRealPrice, 2));
            currRow.put("totalCost", MathUtil.round(totalCost, 2));
            infoBySellerId.put(sellerId, currRow);
            tempMap = null;
        }
        
        // 2 订单结算运费 正常订单 和 手工订单
        /**
         * o.seller_id AS sellerId, os.freight_money AS freightMoney, os.postage_is_settlement AS postageIsSettlement
         */
        List<Map<String, Object>> oss = financeDao.findOrderSettlementInfoByComfirmDate(para);
        List<Map<String, Object>> omss = financeDao.findOrderManualSettlementInfoByComfirmDate(para);
        Map<String, List<Map<String, Object>>> osMapBySellerId = new HashMap<>();
        for (Map<String, Object> it : oss)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            List<Map<String, Object>> currOs = osMapBySellerId.get(sellerId);
            if (currOs == null)
            {
                currOs = new ArrayList<>();
                osMapBySellerId.put(sellerId, currOs);
            }
            currOs.add(it);
        }
        
        for (Map<String, Object> it : omss)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            List<Map<String, Object>> currOs = osMapBySellerId.get(sellerId);
            if (currOs == null)
            {
                currOs = new ArrayList<>();
                osMapBySellerId.put(sellerId, currOs);
            }
            currOs.add(it);
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> it : osMapBySellerId.entrySet())
        {
            String sellerId = it.getKey();
            List<Map<String, Object>> value = it.getValue();
            double totalFreight = 0; // 订单结算运费
            for (Map<String, Object> cit : value)
            {
                int postageIsSettlement = Integer.valueOf(cit.get("postageIsSettlement") + "");
                if (postageIsSettlement == 1)
                {
                    double freightMoney = Double.valueOf(cit.get("freightMoney") + "");
                    totalFreight += freightMoney;
                }
            }
            Map<String, Object> currRow = infoBySellerId.get(sellerId);
            if (currRow == null)
            {
                currRow = new HashMap<>();
                currRow.put("sellerId", sellerId);
                currRow.put("totalRealPrice", "0");
                currRow.put("totalCost", "0");
                infoBySellerId.put(sellerId, currRow);
            }
            currRow.put("totalFreight", MathUtil.round(totalFreight, 2));
        }
        
        // 3 结算退款中商家承担部分 正常订单
        /**
         * o.seller_id AS sellerId, opr.order_product_id AS orderProductId, opr.responsibility_side AS
         * responsibilitySide, opr.responsibility_money AS responsibilityMoney
         */
        List<Map<String, Object>> rss = financeDao.findRefundSettlementInfoByComfirmDate(para);
        // System.out.println("rss: " + rss);
        Map<String, List<Map<String, Object>>> rsMapBySellerId = new HashMap<>();
        for (Map<String, Object> it : rss)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            List<Map<String, Object>> currRs = rsMapBySellerId.get(sellerId);
            if (currRs == null)
            {
                currRs = new ArrayList<>();
                rsMapBySellerId.put(sellerId, currRs);
            }
            currRs.add(it);
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> it : rsMapBySellerId.entrySet())
        {
            String sellerId = it.getKey();
            List<Map<String, Object>> value = it.getValue();
            double totalSellerResponsibility = 0; // 结算退款中商家承担部分
            for (Map<String, Object> rit : value)
            {
                int responsibilitySide = Integer.valueOf(rit.get("responsibilitySide") + "");
                if (responsibilitySide == 1)
                {
                    double responsibilityMoney = Double.valueOf(rit.get("responsibilityMoney") + "");
                    totalSellerResponsibility += responsibilityMoney;
                }
            }
            if (totalSellerResponsibility != 0)
            {
                Map<String, Object> currRow = infoBySellerId.get(sellerId);
                if (currRow == null)
                {
                    currRow = new HashMap<>();
                    currRow.put("sellerId", sellerId);
                    currRow.put("totalRealPrice", "0");
                    currRow.put("totalCost", "0");
                    currRow.put("totalFreight", "0");
                    infoBySellerId.put(sellerId, currRow);
                }
                currRow.put("totalSellerResponsibility", MathUtil.round(totalSellerResponsibility, 2));
            }
        }
        
        // 查询商家信息
        Map<String, String> sellerInfoMap = new HashMap<>();
        if (sellerIdSet.size() > 0)
        {
            List<Map<String, Object>> sellerInfo = sellerDao.findSellerRealNameByIds(CommonUtil.setToList(sellerIdSet));
            for (Map<String, Object> seller : sellerInfo)
            {
                String id = seller.get("id") + "";
                String realSellerName = seller.get("realSellerName") + "";
                sellerInfoMap.put(id, realSellerName);
            }
        }
        
        // 转换数据到行
        for (Map.Entry<String, Map<String, Object>> it : infoBySellerId.entrySet())
        {
            String realSellerName = sellerInfoMap.get(it.getKey()) == null ? "" : sellerInfoMap.get(it.getKey());
            Map<String, Object> value = it.getValue();
            double totalCost = Double.valueOf(value.get("totalCost") + "");
            double totalFreight = Double.valueOf(value.get("totalFreight") == null ? "0" : value.get("totalFreight") + "");
            double totalSellerResponsibility = Double.valueOf(value.get("totalSellerResponsibility") == null ? "0" : value.get("totalSellerResponsibility") + "");
            double totalPaySeller = totalCost + totalFreight - totalSellerResponsibility;
            value.put("totalPaySeller", MathUtil.round(totalPaySeller, 2));
            value.put("realSellerName", realSellerName);
            rows.add(value);
        }
        
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public Map<String, Object> findSellerGrossSettlement(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        
        // 普通订单
        List<Map<String, Object>> ops = financeDao.findOrderProductAndSettlementByOrderSettlementTime(para);
        // System.out.println(ops);
        Map<String, List<Map<String, Object>>> opsMapByKeySellerId = new HashMap<>();
        Set<Integer> sellerIdSet = new HashSet<>();
        for (Map<String, Object> it : ops)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            List<Map<String, Object>> currList = opsMapByKeySellerId.get(sellerId);
            if (currList == null)
            {
                currList = new ArrayList<>();
                opsMapByKeySellerId.put(sellerId, currList);
            }
            currList.add(it);
        }
        
        // 手工订单
        List<Map<String, Object>> omps = financeDao.findOrderManualProductAndSettlementByOrderSettlementTime(para);
        // System.out.println(omps);
        for (Map<String, Object> it : omps)
        {
            String sellerId = it.get("sellerId") + "";
            sellerIdSet.add(Integer.valueOf(sellerId));
            // 防止ID和正常订单重复，处理一下，只要不重复就可以了
            String id = "01" + it.get("id");
            it.put("id", id);
            
            List<Map<String, Object>> currList = opsMapByKeySellerId.get(sellerId);
            if (currList == null)
            {
                currList = new ArrayList<>();
                opsMapByKeySellerId.put(sellerId, currList);
            }
            currList.add(it);
        }
        
        // 查询商家信息
        Map<String, Map<String, Object>> sellerInfoMap = new HashMap<>();
        if (sellerIdSet.size() > 0)
        {
            List<Map<String, Object>> sellerInfo = sellerDao.findSellerRealNameByIds(CommonUtil.setToList(sellerIdSet));
            for (Map<String, Object> seller : sellerInfo)
            {
                String id = seller.get("id") + "";
                sellerInfoMap.put(id, seller);
            }
        }
        
        for (Map.Entry<String, List<Map<String, Object>>> it : opsMapByKeySellerId.entrySet())
        {
            Map<String, Object> row = new HashMap<>();
            String sellerId = it.getKey();
            Map<String, Object> seller = sellerInfoMap.get(sellerId);
            String realSellerName = seller == null ? "" : seller.get("realSellerName") + "";
            String sendAddress = seller == null ? "" : seller.get("sendAddress") + "";
            double totalSalesPrice = 0;
            double totalCost = 0;
            double totalFreight = 0;
            double totalGrossDivTotalSalesPrice = 0;
            double totalGross = 0;
            List<Map<String, Object>> value = it.getValue();
            Map<String, String> freightMap = new HashMap<>();
            for (Map<String, Object> cit : value)
            {
                // 计算总订单供货价
                double cost = Double.valueOf(cit.get("cost") + "");
                double salesPrice = Double.valueOf(cit.get("salesPrice") + "");
                double productCount = Double.valueOf(cit.get("productCount") + "");
                totalCost += (cost * productCount);
                totalSalesPrice += (salesPrice * productCount);
                
                // 结算邮费
                String freightMoney = cit.get("freightMoney") == null ? "0" : cit.get("freightMoney") + "";
                String orderId = cit.get("id") + "";
                freightMap.put(orderId, freightMoney);
            }
            
            for (Map.Entry<String, String> v : freightMap.entrySet())
            {
                totalFreight += Double.valueOf(v.getValue()).doubleValue();
            }
            
            totalGross = totalSalesPrice - totalCost - totalFreight;
            totalGrossDivTotalSalesPrice = totalGross / totalSalesPrice;
            
            row.put("sellerId", sellerId);
            row.put("realSellerName", realSellerName);
            row.put("sendAddress", sendAddress);
            row.put("totalSalesPrice", MathUtil.round(totalSalesPrice, 2));
            row.put("totalCost", MathUtil.round(totalCost, 2));
            row.put("totalFreight", MathUtil.round(totalFreight, 2));
            row.put("totalGross", MathUtil.round(totalGross, 2));
            row.put("totalGrossDivTotalSalesPrice", MathUtil.round(totalGrossDivTotalSalesPrice, 2));
            
            rows.add(row);
        }
        
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public Map<String, Object> findSellerGrossCalculation(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        
        List<Map<String, Object>> orderInfoList = orderDao.findSimpleOrderInfoByPara(para);
        List<Integer> orderIdList = new ArrayList<>();
        List<Integer> sellerIdList = new ArrayList<>();
        if (orderInfoList.size() > 0)
        {
            Map<String, List<Map<String, Object>>> orderGroupMapBySellerId = new HashMap<>();
            for (Map<String, Object> order : orderInfoList)
            {
                orderIdList.add(Integer.valueOf(order.get("id") + ""));
                sellerIdList.add(Integer.valueOf(order.get("sellerId") + ""));
                
                List<Map<String, Object>> list = orderGroupMapBySellerId.get(order.get("sellerId") + "");
                if (list == null)
                {
                    list = new ArrayList<>();
                    orderGroupMapBySellerId.put(order.get("sellerId") + "", list);
                }
                
                list.add(order);
            }
            
            // 查询商家信息
            List<Map<String, Object>> sellerInfoList = sellerDao.findSellerRealNameByIds(sellerIdList);
            Map<String, Map<String, Object>> sellerInfoMap = new HashMap<>();
            for (Map<String, Object> it : sellerInfoList)
            {
                sellerInfoMap.put(it.get("id") + "", it);
            }
            // 查询订单商品信息
            Map<String, Object> opPara = new HashMap<>();
            opPara.put("orderIdList", orderIdList);
            List<Map<String, Object>> opList = orderDao.findAllOrderProductInfoByPara(opPara);
            Map<String, List<Map<String, Object>>> orderProductMap = new HashMap<>();
            for (Map<String, Object> it : opList)
            {
                String orderId = it.get("orderId") + "";
                List<Map<String, Object>> ops = orderProductMap.get(orderId);
                if (ops == null)
                {
                    ops = new ArrayList<>();
                    orderProductMap.put(orderId, ops);
                }
                ops.add(it);
            }
            
            /**
             * 显示字段
             *
             * 真实商家名称 订单实付 应付商家 模拟运费金额 订单净毛利 订单净毛利率 积分优惠 优惠券优惠 客服调价 商品净毛利 商品净毛利率 导出明细
             */
            for (Map.Entry<String, List<Map<String, Object>>> info : orderGroupMapBySellerId.entrySet())
            {
                Map<String, Object> row = new HashMap<>();
                String sellerId = info.getKey();
                List<Map<String, Object>> currOrderList = info.getValue();
                String realSellerName = ""; // 真实商家名称
                double totalPayPrice = 0; // 订单实付
                double totalSellerPrice = 0; // 应付商家
                double totalFreight = 0; // 模拟运费金额
                double totalGross = 0; // 订单净毛利
                double grossRate = 0; // 订单净毛利率
                double totalpointProportion = 0; // 积分优惠
                double totalcouponProportion = 0; // 优惠券优惠
                double totaladjustProportion = 0; // 客服调价
                
                for (Map<String, Object> order : currOrderList)
                {
                    double currTotalSellerPrice = 0; // 应付商家
                    
                    String orderId = order.get("id") + "";
                    double realPrice = Double.valueOf(order.get("realPrice") == null ? "0.00" : order.get("realPrice") + "");
                    // double totalPrice = Double.valueOf(order.get("totalPrice") == null ? "0.00" :
                    // order.get("totalPrice") + "" );
                    // double freightMoney = Double.valueOf(order.get("freightMoney") == null ? "0.00" :
                    // order.get("freightMoney") + "" );
                    double adjustPrice = Double.valueOf(order.get("adjustPrice") == null ? "0.00" : order.get("adjustPrice") + "");
                    double accountPoint = Double.valueOf(order.get("accountPoint") == null ? "0.00" : order.get("accountPoint") + "");
                    double couponPrice = Double.valueOf(order.get("couponPrice") == null ? "0.00" : order.get("couponPrice") + "");
                    Map<String, Object> sellerInfo = sellerInfoMap.get(sellerId);
                    List<Map<String, Object>> ops = orderProductMap.get(orderId);
                    if (sellerInfo == null || ops == null)
                    {
                        log.warn("订单未查询到商家信息或订单商品信息，sellerInfo:" + sellerInfo + "; orderProductList:" + ops);
                        continue;
                    }
                    if ("".equals(realSellerName))
                    {
                        realSellerName = sellerInfo.get("realSellerName") + "";
                    }
                    
                    // 运费类型；1：包邮，2：满x包邮，3：不包邮，4：其它',
                    int freightType = Integer.valueOf(sellerInfo.get("freightType") + "");
                    if (freightType == 2)
                    {
                        double sellerFreightMoney = Double.valueOf(sellerInfo.get("freightMoney") == null ? "0" : sellerInfo.get("freightMoney") + "");
                        if (sellerFreightMoney > realPrice)
                        {
                            freightType = 3;
                        }
                    }
                    if (freightType == 3)
                    {
                        int sellerType = Integer.valueOf(sellerInfo.get("sellerType") + "");
                        if (sellerType == 1)
                        {
                            // 国内按10元一单粗略计算
                            totalFreight += 10;
                        }
                        else
                        {
                            // 国外按25元一单粗略计算
                            totalFreight += 25;
                        }
                    }
                    
                    totalPayPrice += realPrice;
                    totalpointProportion += (accountPoint / 100);
                    totalcouponProportion += couponPrice;
                    totaladjustProportion += adjustPrice;
                    // totalPriceWithoutFreight += (totalPrice - freightMoney);
                    
                    // 计算商品价格信息 : 应付商家
                    for (Map<String, Object> op : ops)
                    {
                        double cost = Double.valueOf(op.get("cost") + "");
                        int productCount = Integer.valueOf(op.get("productCount") + "");
                        currTotalSellerPrice += (cost * productCount);
                    }
                    totalSellerPrice += currTotalSellerPrice;
                }
                
                row.put("sellerId", sellerId);
                row.put("realSellerName", realSellerName);
                row.put("totalPayPrice", MathUtil.round(totalPayPrice, 2));
                row.put("totalSellerPrice", MathUtil.round(totalSellerPrice, 2));
                row.put("totalFreight", MathUtil.round(totalFreight, 2));
                totalGross = totalPayPrice - totalSellerPrice - totalFreight;
                grossRate = totalPayPrice == 0 ? 0 : totalGross / totalPayPrice;
                row.put("totalGross", StringUtils.removeLastZero(MathUtil.round(totalGross, 4)));
                row.put("grossRate", StringUtils.removeLastZero(MathUtil.round(grossRate, 4)));
                row.put("totalpointProportion", MathUtil.round(totalpointProportion, 2));
                row.put("totalcouponProportion", MathUtil.round(totalcouponProportion, 2));
                row.put("totaladjustProportion", MathUtil.round(totaladjustProportion, 2));
                rows.add(row);
            }
        }
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public Map<String, Object> findSellerGrossCalculationDetail(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        
        List<Map<String, Object>> orderInfoList = orderDao.findSimpleOrderInfoByPara(para);
        List<Integer> orderIdList = new ArrayList<>();
        List<Integer> sellerIdList = new ArrayList<>();
        if (orderInfoList.size() > 0)
        {
            Map<String, List<Map<String, Object>>> orderGroupMapBySellerId = new HashMap<>();
            for (Map<String, Object> order : orderInfoList)
            {
                orderIdList.add(Integer.valueOf(order.get("id") + ""));
                sellerIdList.add(Integer.valueOf(order.get("sellerId") + ""));
                
                List<Map<String, Object>> list = orderGroupMapBySellerId.get(order.get("sellerId") + "");
                if (list == null)
                {
                    list = new ArrayList<>();
                    orderGroupMapBySellerId.put(order.get("sellerId") + "", list);
                }
                
                list.add(order);
            }
            
            // 查询商家信息
            List<Map<String, Object>> sellerInfoList = sellerDao.findSellerRealNameByIds(sellerIdList);
            Map<String, Map<String, Object>> sellerInfoMap = new HashMap<>();
            for (Map<String, Object> it : sellerInfoList)
            {
                sellerInfoMap.put(it.get("id") + "", it);
            }
            // 查询订单商品信息
            Map<String, Object> opPara = new HashMap<>();
            opPara.put("orderIdList", orderIdList);
            List<Map<String, Object>> opList = orderDao.findAllOrderProductInfoByPara(opPara);
            Map<String, List<Map<String, Object>>> orderProductMap = new HashMap<>();
            for (Map<String, Object> it : opList)
            {
                String orderId = it.get("orderId") + "";
                List<Map<String, Object>> ops = orderProductMap.get(orderId);
                if (ops == null)
                {
                    ops = new ArrayList<>();
                    orderProductMap.put(orderId, ops);
                }
                ops.add(it);
            }
            /**
             * 导出字段
             *
             * 订单编号 订单状态 付款日期 商品ID 商品名称 件数 单价 总价 运费 订单总价 订单实付 应付商家 模拟运费金额 订单净毛利 订单净毛利率 分摊总价 总供货价 分摊模拟运费 商品毛利 商品毛利率
             * 分摊积分优惠 分摊优惠券优惠 分摊客服调价 商品实付 商品净毛利 商品净毛利率
             */
            
            for (Map.Entry<String, List<Map<String, Object>>> info : orderGroupMapBySellerId.entrySet())
            {
                String sellerId = info.getKey();
                List<Map<String, Object>> currOrderList = info.getValue();
                
                Map<String, Object> sellerInfo = sellerInfoMap.get(sellerId);
                if (sellerInfo == null)
                {
                    log.warn("订单未查询到商家信息，sellerInfo:" + sellerInfo);
                    continue;
                }
                // 运费类型；1：包邮，2：满x包邮，3：不包邮，4：其它',
                int freightType = Integer.valueOf(sellerInfo.get("freightType") + "");
                double sellerFreightMoney = Double.valueOf(sellerInfo.get("freightMoney") == null ? "0" : sellerInfo.get("freightMoney") + "");
                int sellerType = Integer.valueOf(sellerInfo.get("sellerType") + "");
                
                for (Map<String, Object> order : currOrderList)
                {
                    
                    double sellerPrice = 0; // 应付商家
                    double freight = 0; // 模拟运费金额
                    double gross = 0; // 订单净毛利
                    double grossRate = 0; // 订单净毛利率
                    
                    String orderId = order.get("id") + "";
                    String number = order.get("number") + "";
                    double realPrice = Double.valueOf(order.get("realPrice") == null ? "0.00" : order.get("realPrice") + "");
                    double totalPrice = Double.valueOf(order.get("totalPrice") == null ? "0.00" : order.get("totalPrice") + ""); // 订单实付
                    double orderFreightMoney = Double.valueOf(order.get("freightMoney") == null ? "0.00" : order.get("freightMoney") + "");
                    double totalPriceMinusFreightMoney = totalPrice - orderFreightMoney;
                    double adjustPrice = Double.valueOf(order.get("adjustPrice") == null ? "0.00" : order.get("adjustPrice") + "");
                    double accountPoint = Double.valueOf(order.get("accountPoint") == null ? "0.00" : order.get("accountPoint") + "");
                    double couponPrice = Double.valueOf(order.get("couponPrice") == null ? "0.00" : order.get("couponPrice") + "");
                    String status = OrderEnum.ORDER_STATUS.getDescByCode(Integer.valueOf(order.get("status") + ""));
                    Date payTime = CommonUtil.string2Date(order.get("payTime") + "", "yyyy-MM-dd HH:mm:ss");
                    
                    List<Map<String, Object>> ops = orderProductMap.get(orderId);
                    if (ops == null)
                    {
                        log.warn("订单未查询到商品信息 orderProductList:" + ops);
                        continue;
                    }
                    
                    if (freightType == 2)
                    {
                        if (sellerFreightMoney > realPrice)
                        {
                            freightType = 3;
                        }
                    }
                    if (freightType == 3)
                    {
                        if (sellerType == 1)
                        {
                            // 国内按10元一单粗略计算
                            freight = 10;
                        }
                        else
                        {
                            // 国外按25元一单粗略计算
                            freight += 25;
                        }
                    }
                    
                    List<Map<String, Object>> currOrderRows = new ArrayList<>();
                    for (Map<String, Object> op : ops)
                    {
                        Map<String, Object> row = new HashMap<>();
                        
                        double cost = Double.valueOf(op.get("cost") + "");
                        String productName = op.get("productName") + "";
                        int productCount = Integer.valueOf(op.get("productCount") + "");
                        double salesPrice = Double.valueOf(op.get("salesPrice") + "");
                        String productId = op.get("productId") + "";
                        double opSellerPrice = cost * productCount;// 应付商家
                        sellerPrice += opSellerPrice;
                        // 分摊信息
                        double selectPrice = productCount * salesPrice;
                        double coe = selectPrice / totalPriceMinusFreightMoney;
                        
                        row.put("number", number);
                        row.put("status", status);
                        row.put("payTime", CommonUtil.date2String(payTime, "yyyy-MM-dd HH:mm:ss"));
                        row.put("productId", productId);
                        row.put("productName", productName);
                        row.put("productCount", productCount);
                        row.put("productSalePrice", salesPrice);
                        row.put("productTotalSalePrice", MathUtil.round(salesPrice * productCount, 2));
                        row.put("orderFreightMoney", orderFreightMoney);
                        row.put("orderTotalPrice", MathUtil.round(totalPrice, 2));
                        row.put("orderPayPrice", MathUtil.round(realPrice, 2));
                        row.put("freight", MathUtil.round(freight, 2));
                        
                        double coeTotalPrice = Double.parseDouble(MathUtil.round(coe * totalPriceMinusFreightMoney, 2));// 分摊总价
                        double coeFreight = Double.parseDouble(MathUtil.round(coe * freight, 2));// 分摊模拟运费
                        double opGross = coeTotalPrice - opSellerPrice - coeFreight; // 商品毛利
                        double opGrossRate = coeTotalPrice == 0 ? 0 : opGross / coeTotalPrice;
                        row.put("coeTotalPrice", MathUtil.round(coeTotalPrice, 2));
                        row.put("opSellerPrice", MathUtil.round(opSellerPrice, 2));
                        row.put("coeFreight", MathUtil.round(coeFreight, 2));
                        row.put("opGross", MathUtil.round(opGross, 2));
                        row.put("opGrossRate", MathUtil.round(opGrossRate, 2));
                        
                        double adjustProportion = Double.parseDouble(MathUtil.round(coe * adjustPrice, 2));// 分摊客服改价
                        double couponProportion = Double.parseDouble(MathUtil.round(coe * couponPrice, 2));// 分摊优惠券金额
                        double pointProportion = Double.parseDouble(MathUtil.round(coe * accountPoint / 100.0, 2));// 分摊积分抵扣
                        double productRealPrice = coeTotalPrice - adjustProportion - couponProportion - pointProportion;// 商品实付
                        double productGross = opGross - adjustProportion - couponProportion - pointProportion;// 商品净毛利
                        double productGrossRate = productRealPrice == 0 ? 0 : productGross / productRealPrice;// 商品净毛利率
                        row.put("adjustProportion", MathUtil.round(adjustProportion, 2));
                        row.put("couponProportion", MathUtil.round(couponProportion, 2));
                        row.put("pointProportion", MathUtil.round(pointProportion, 2));
                        row.put("productRealPrice", MathUtil.round(productRealPrice, 2));
                        row.put("productGross", MathUtil.round(productGross, 2));
                        row.put("productGrossRate", MathUtil.round(productGrossRate, 4));
                        
                        currOrderRows.add(row);
                    }
                    gross = realPrice - sellerPrice - freight;
                    grossRate = realPrice == 0 ? 0 : gross / realPrice;
                    for (Map<String, Object> it : currOrderRows)
                    {
                        it.put("sellerPrice", sellerPrice);
                        it.put("gross", MathUtil.round(gross, 2));
                        it.put("grossRate", MathUtil.round(grossRate, 2));
                    }
                    rows.addAll(currOrderRows);
                }
            }
        }
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public String exportAllSellerGrossCalculationDetail(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> orderInfoList = orderDao.findSimpleOrderInfoByPara(para);
        List<Integer> orderIdList = new ArrayList<>();
        List<Integer> sellerIdList = new ArrayList<>();
        String nowStr = DateTime.now().toString("yyyy-MM-dd_HH_mm_ss");
        File fileDir = new File(YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
        File newDir = new File(fileDir, nowStr + "_" + new Random().nextInt(10000) + "sellerGross");
        newDir.mkdir();
        if (orderInfoList.size() > 0)
        {
            Map<String, List<Map<String, Object>>> orderGroupMapBySellerId = new HashMap<>();
            for (Map<String, Object> order : orderInfoList)
            {
                orderIdList.add(Integer.valueOf(order.get("id") + ""));
                sellerIdList.add(Integer.valueOf(order.get("sellerId") + ""));
                
                List<Map<String, Object>> list = orderGroupMapBySellerId.get(order.get("sellerId") + "");
                if (list == null)
                {
                    list = new ArrayList<>();
                    orderGroupMapBySellerId.put(order.get("sellerId") + "", list);
                }
                
                list.add(order);
            }
            
            // 查询商家信息
            List<Map<String, Object>> sellerInfoList = sellerDao.findSellerRealNameByIds(sellerIdList);
            Map<String, Map<String, Object>> sellerInfoMap = new HashMap<>();
            for (Map<String, Object> it : sellerInfoList)
            {
                sellerInfoMap.put(it.get("id") + "", it);
            }
            // 查询订单商品信息
            Map<String, Object> opPara = new HashMap<>();
            opPara.put("orderIdList", orderIdList);
            List<Map<String, Object>> opList = orderDao.findAllOrderProductInfoByPara(opPara);
            Map<String, List<Map<String, Object>>> orderProductMap = new HashMap<>();
            for (Map<String, Object> it : opList)
            {
                String orderId = it.get("orderId") + "";
                List<Map<String, Object>> ops = orderProductMap.get(orderId);
                if (ops == null)
                {
                    ops = new ArrayList<>();
                    orderProductMap.put(orderId, ops);
                }
                ops.add(it);
            }
            /**
             * 导出字段
             *
             * 订单编号 订单状态 付款日期 商品ID 商品名称 件数 单价 总价 运费 订单总价 订单实付 应付商家 模拟运费金额 订单净毛利 订单净毛利率 分摊总价 总供货价 分摊模拟运费 商品毛利 商品毛利率
             * 分摊积分优惠 分摊优惠券优惠 分摊客服调价 商品实付 商品净毛利 商品净毛利率
             */
            
            for (Map.Entry<String, List<Map<String, Object>>> info : orderGroupMapBySellerId.entrySet())
            {
                List<Map<String, Object>> rows = new ArrayList<>();
                String sellerId = info.getKey();
                List<Map<String, Object>> currOrderList = info.getValue();
                
                Map<String, Object> sellerInfo = sellerInfoMap.get(sellerId);
                if (sellerInfo == null)
                {
                    log.warn("订单未查询到商家信息，sellerInfo:" + sellerInfo);
                    continue;
                }
                String realSellerName = sellerInfo.get("realSellerName") + "";
                // 运费类型；1：包邮，2：满x包邮，3：不包邮，4：其它',
                int freightType = Integer.valueOf(sellerInfo.get("freightType") + "");
                double sellerFreightMoney = Double.valueOf(sellerInfo.get("freightMoney") == null ? "0" : sellerInfo.get("freightMoney") + "");
                int sellerType = Integer.valueOf(sellerInfo.get("sellerType") + "");
                
                for (Map<String, Object> order : currOrderList)
                {
                    
                    double sellerPrice = 0; // 应付商家
                    double freight = 0; // 模拟运费金额
                    double gross = 0; // 订单净毛利
                    double grossRate = 0; // 订单净毛利率
                    
                    String orderId = order.get("id") + "";
                    String number = order.get("number") + "";
                    double realPrice = Double.valueOf(order.get("realPrice") == null ? "0.00" : order.get("realPrice") + "");
                    double totalPrice = Double.valueOf(order.get("totalPrice") == null ? "0.00" : order.get("totalPrice") + ""); // 订单实付
                    double orderFreightMoney = Double.valueOf(order.get("freightMoney") == null ? "0.00" : order.get("freightMoney") + "");
                    double totalPriceMinusFreightMoney = totalPrice - orderFreightMoney;
                    double adjustPrice = Double.valueOf(order.get("adjustPrice") == null ? "0.00" : order.get("adjustPrice") + "");
                    double accountPoint = Double.valueOf(order.get("accountPoint") == null ? "0.00" : order.get("accountPoint") + "");
                    double couponPrice = Double.valueOf(order.get("couponPrice") == null ? "0.00" : order.get("couponPrice") + "");
                    String status = OrderEnum.ORDER_STATUS.getDescByCode(Integer.valueOf(order.get("status") + ""));
                    Date payTime = CommonUtil.string2Date(order.get("payTime") + "", "yyyy-MM-dd HH:mm:ss");
                    
                    List<Map<String, Object>> ops = orderProductMap.get(orderId);
                    if (ops == null)
                    {
                        log.warn("订单未查询到商品信息 orderProductList:" + ops);
                        continue;
                    }
                    
                    if (freightType == 2)
                    {
                        if (sellerFreightMoney > realPrice)
                        {
                            freightType = 3;
                        }
                    }
                    if (freightType == 3)
                    {
                        if (sellerType == 1)
                        {
                            // 国内按10元一单粗略计算
                            freight = 10;
                        }
                        else
                        {
                            // 国外按25元一单粗略计算
                            freight += 25;
                        }
                    }
                    
                    List<Map<String, Object>> currOrderRows = new ArrayList<>();
                    for (Map<String, Object> op : ops)
                    {
                        Map<String, Object> row = new HashMap<>();
                        
                        double cost = Double.valueOf(op.get("cost") + "");
                        String productName = op.get("productName") + "";
                        int productCount = Integer.valueOf(op.get("productCount") + "");
                        double salesPrice = Double.valueOf(op.get("salesPrice") + "");
                        String productId = op.get("productId") + "";
                        double opSellerPrice = cost * productCount;// 应付商家
                        sellerPrice += opSellerPrice;
                        // 分摊信息
                        double selectPrice = productCount * salesPrice;
                        double coe = selectPrice / totalPriceMinusFreightMoney;
                        
                        row.put("number", number);
                        row.put("status", status);
                        row.put("payTime", CommonUtil.date2String(payTime, "yyyy-MM-dd HH:mm:ss"));
                        row.put("productId", productId);
                        row.put("productName", productName);
                        row.put("productCount", productCount);
                        row.put("productSalePrice", salesPrice);
                        row.put("productTotalSalePrice", MathUtil.round(salesPrice * productCount, 2));
                        row.put("orderFreightMoney", orderFreightMoney);
                        row.put("orderTotalPrice", MathUtil.round(totalPrice, 2));
                        row.put("orderPayPrice", MathUtil.round(realPrice, 2));
                        row.put("freight", MathUtil.round(freight, 2));
                        
                        double coeTotalPrice = Double.parseDouble(MathUtil.round(coe * totalPriceMinusFreightMoney, 2));// 分摊总价
                        double coeFreight = Double.parseDouble(MathUtil.round(coe * freight, 2));// 分摊模拟运费
                        double opGross = coeTotalPrice - opSellerPrice - coeFreight; // 商品毛利
                        double opGrossRate = coeTotalPrice == 0 ? 0 : opGross / coeTotalPrice;
                        row.put("coeTotalPrice", MathUtil.round(coeTotalPrice, 2));
                        row.put("opSellerPrice", MathUtil.round(opSellerPrice, 2));
                        row.put("coeFreight", MathUtil.round(coeFreight, 2));
                        row.put("opGross", MathUtil.round(opGross, 2));
                        row.put("opGrossRate", MathUtil.round(opGrossRate, 2));
                        
                        double adjustProportion = Double.parseDouble(MathUtil.round(coe * adjustPrice, 2));// 分摊客服改价
                        double couponProportion = Double.parseDouble(MathUtil.round(coe * couponPrice, 2));// 分摊优惠券金额
                        double pointProportion = Double.parseDouble(MathUtil.round(coe * accountPoint / 100.0, 2));// 分摊积分抵扣
                        double productRealPrice = coeTotalPrice - adjustProportion - couponProportion - pointProportion;// 商品实付
                        double productGross = opGross - adjustProportion - couponProportion - pointProportion;// 商品净毛利
                        double productGrossRate = productRealPrice == 0 ? 0 : productGross / productRealPrice;// 商品净毛利率
                        row.put("adjustProportion", MathUtil.round(adjustProportion, 2));
                        row.put("couponProportion", MathUtil.round(couponProportion, 2));
                        row.put("pointProportion", MathUtil.round(pointProportion, 2));
                        row.put("productRealPrice", MathUtil.round(productRealPrice, 2));
                        row.put("productGross", MathUtil.round(productGross, 2));
                        row.put("productGrossRate", MathUtil.round(productGrossRate, 4));
                        
                        currOrderRows.add(row);
                    }
                    gross = realPrice - sellerPrice - freight;
                    grossRate = realPrice == 0 ? 0 : gross / realPrice;
                    for (Map<String, Object> it : currOrderRows)
                    {
                        it.put("sellerPrice", sellerPrice);
                        it.put("gross", MathUtil.round(gross, 2));
                        it.put("grossRate", MathUtil.round(grossRate, 2));
                    }
                    rows.addAll(currOrderRows);
                }
                // 写入到硬盘
                writeToExcel(newDir, realSellerName + "_id_" + sellerId + ".xlsx", rows);
                rows = null;
            }
        }
        return newDir.getAbsolutePath();
    }
    
    private void writeToExcel(File dir, String fileName, List<Map<String, Object>> rows)
        throws Exception
    {
        OutputStream fOut = null;
        Workbook workbook = null;
        try
        {
            String[] title =
                {"订单编号", "订单状态", "付款日期", "商品ID", "商品名称", "件数", "单价", "总价", "运费", "订单总价", "订单实付", "应付商家", "模拟运费金额", "订单净毛利", "订单净毛利率", "分摊总价", "总供货价", "分摊模拟运费", "商品毛利", "商品毛利率",
                    "分摊积分优惠", "分摊优惠券优惠", "分摊客服调价", "商品实付", "商品净毛利", "商品净毛利率"};
            workbook = POIUtil.createXSSFWorkbookTemplate(title);
            Sheet sheet = workbook.getSheetAt(0);
            int rIndex = 1;
            for (Map<String, Object> it : rows)
            {
                int cellIndex = 0;
                Row r = sheet.createRow(rIndex++);
                r.createCell(cellIndex++).setCellValue(it.get("number") + "");
                r.createCell(cellIndex++).setCellValue(it.get("status") + "");
                r.createCell(cellIndex++).setCellValue(it.get("payTime") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productId") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productName") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productCount") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productSalePrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productTotalSalePrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("orderFreightMoney") + "");
                r.createCell(cellIndex++).setCellValue(it.get("orderTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("orderPayPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sellerPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("freight") + "");
                r.createCell(cellIndex++).setCellValue(it.get("gross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("grossRate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("coeTotalPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("sellerPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("coeFreight") + "");
                r.createCell(cellIndex++).setCellValue(it.get("opGross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("opGrossRate") + "");
                r.createCell(cellIndex++).setCellValue(it.get("pointProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("couponProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("adjustProportion") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productRealPrice") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productGross") + "");
                r.createCell(cellIndex++).setCellValue(it.get("productGrossRate") + "");
            }
            File file = new File(dir, fileName);
            file.createNewFile();
            fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.flush();
            Runtime.getRuntime().gc();
        }
        catch (Exception e)
        {
            log.error("导出发货excel出错", e);
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

    @Override
    public int checkImportPenaltySettlementData(String number, List<Map<String, Object>> simulationList)
        throws Exception
    {

        Map<String, Object> result = new HashMap<>();
        int status = 1;
        String msg = "成功";
        OrderEntity oe = null;
        // 非短路判断
        if (!StringUtils.isNumeric(number) | (oe = orderDao.findOrderByNumber(number)) == null)
        {
            status = 0;
            msg = "订单编号不存在";
        }
        else if (oe.getIsTimeOut() == 0)
        {
            status = 0;
            msg = "该订单不超时";
        }
        else if (orderDao.findOrderTimeoutSettlementByOrderId(oe.getId()) != null)
        {
            status = 0;
            msg = "该订单已罚款";
        }
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("msg", msg);
        result.put("number", number);
        result.put("isTimeOut", oe == null ? "" : oe.getIsTimeOut() == 1 ? "是" : "");
        simulationList.add(result);
        return status;
    }

    @Override
    public int savePenaltySettlementData(String number, List<Map<String, Object>> confirmList)
        throws Exception
    {
        Map<String, Object> insertPara = new HashMap<>();
        insertPara.put("orderId", orderDao.findOrderIdByNumber(Long.valueOf(number)));
        insertPara.put("penaltyMoney", "10");
        insertPara.put("confirmDate", DateTimeUtil.now());
        int status = orderDao.saveOrderTimeoutSettlement(insertPara);

        Map<String, Object> result = new HashMap<>();
        result.put("status", status == 1 ? "成功" : "失败");
        result.put("number", number);
        confirmList.add(result);
        return status;
    }

    @Override
    public int deletePenaltySettlementData(String number, List<Map<String, Object>> cancelList)
        throws Exception
    {
        int id = orderDao.findOrderIdByNumber(Long.valueOf(number));
        int status = 0;
        if (id != -1)
        {
            status = orderDao.deleteOrderTimeoutSettlement(id);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("number", number);
        result.put("status", status == 1 ? "撤销成功" : "撤销失败");
        cancelList.add(result);
        return status;
    }
}
