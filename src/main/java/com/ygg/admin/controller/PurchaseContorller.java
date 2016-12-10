package com.ygg.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygg.admin.annotation.ControllerLog;
import com.ygg.admin.code.PurchaseConstant;
import com.ygg.admin.config.YggAdminProperties;
import com.ygg.admin.entity.ProviderEntity;
import com.ygg.admin.entity.PurchaseOrderInfoEntity;
import com.ygg.admin.service.PurchaseService;
import com.ygg.admin.service.PurchaseStoringService;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.FileUtil;
import com.ygg.admin.util.ZipCompressorByAnt;

@Controller
@RequestMapping("purchase")
public class PurchaseContorller
{
    
    Logger logger = Logger.getLogger(PurchaseContorller.class);
    
    @Resource
    private PurchaseService purchaseService;
    
    @Resource
    private PurchaseStoringService purchaseStoringService;
    
    /**
     * 跳供货商列表
     * 
     * @return
     */
    @RequestMapping("toProviderList")
    public ModelAndView toProviderList(int type)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("purchase/providerList");
        mv.addObject("type", type);
        return mv;
    }
    
    /**
     * 跳采购单列表
     * 
     * @return
     */
    @RequestMapping("toOrderList")
    public ModelAndView toOrderList(int type)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("purchase/orderList");
        mv.addObject("type", type);
        return mv;
    }
    
    /**
     * 跳新增，修改供应商页面
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("toSaveOrUpdateProvider")
    public ModelAndView toSaveOrUpdateProvider(@RequestParam(defaultValue = "0", required = false) int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("purchase/saveOrUpdateProvider");
        if (id > 0)
        {
            mv.addObject("provider", purchaseService.findProviderById(id));
        }
        return mv;
    }
    
    /**
     * 跳分仓列表
     * 
     * @return
     */
    @RequestMapping("toStorageList")
    public ModelAndView toStorageList(int type)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("purchase/storageList");
        mv.addObject("type", type);
        return mv;
    }
    
    /**
     * 跳供应商商品列表
     * 
     * @return
     */
    @RequestMapping("toProviderProductList")
    public ModelAndView toProviderProductList(int type)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("purchase/providerProductList");
        mv.addObject("type", type);
        return mv;
    }
    
    /**
     * 跳结款页面
     * @return
     */
    @RequestMapping("toPurchasePayDetailList")
    public ModelAndView toPurchasePayDetailList()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("finance/purchasePayDetailList");
        return mv;
    }
    
    /**
     * 跳批次页面
     * 
     * @param providerProductId
     * @return
     */
    @RequestMapping("toBatch/{providerProductId}")
    public ModelAndView toPurchasePayDetailList(@PathVariable int providerProductId)
    {
        ModelAndView mv = new ModelAndView();
        mv.addObject("providerProductId", providerProductId);
        mv.setViewName("purchase/batchList");
        return mv;
    }
    
    @RequestMapping("sellerId")
    @ResponseBody
    public Object sellerId()
        throws UnsupportedEncodingException
    {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < PurchaseConstant.NAME.length; i++)
        {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", PurchaseConstant.ID[i]);
            item.put("name", PurchaseConstant.NAME[i]);
            result.add(item);
        }
        return result;
    }
    
    /**
     * 采购单列表详情 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("orderListInfo")
    @ResponseBody
    public Object orderListInfo(String purchaseCode, String status, String providerId, String storageId, String startTime, String endTime, int type, 
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return purchaseService.findOrderListInfo(purchaseCode, status, providerId, storageId, startTime, endTime, page, rows, type);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 根据ID删除一条采购单
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteOrderById/{orderId}")
    @ResponseBody
    @ControllerLog(description = "删除采购单")
    public Object deleteOrderById(@PathVariable int orderId)
        throws Exception
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.deleteOrderById(orderId));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 跳新增、修改采购单
     * @return
     */
    @RequestMapping("toSaveOrUpdateOrder")
    public ModelAndView toSaveOrUpdateOrder(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required = true, defaultValue = "0", value = "providerId") int providerId,
        @RequestParam(required = true, defaultValue = "0", value = "storageId") int storageId, String purchaseCode)
        throws Exception
    {
        if (StringUtils.isBlank(purchaseCode))
        {
            Calendar curr = Calendar.getInstance();
            String day = CommonUtil.date2String(curr.getTime(), "yyyyMMdd");
            int code = purchaseService.createPurchaseCode(day);
            String newPurchaseCode = String.format("%s%s%s%s", day, providerId, storageId, code < 10 ? ("0" + code) : code);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("purchaseCode", newPurchaseCode);
            param.put("providerId", providerId);
            param.put("storageId", storageId);
            param.put("purchaseTotalCount", 0);
            param.put("purchaseTotalMoney", 0);
            param.put("freightMoney", 0);
            param.put("totalMoney", 0);
            param.put("isTax", 0);
            param.put("payableMoney", 0);
            param.put("payableMoneyRemark", "");
            param.put("remark", "");
            param.put("desc", "");
            purchaseService.savePurchaseOrder(param);
            response.sendRedirect(request.getContextPath() + "/purchase/toSaveOrUpdateOrder?providerId=" + providerId + "&storageId=" + storageId + "&purchaseCode="
                + newPurchaseCode);
            return null;
        }
        else
        {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("purchase/saveOrUpdateOrder");
            Map<String, Object> order = purchaseService.findOrderByPurchaseCode(purchaseCode);
            if (order.get("purchaseCode") == null)
            {
                mv.addObject("msg", "采购单已经删除了");
            }
            else
            {
                mv.addObject("provider", purchaseService.findProviderById(providerId));
                mv.addObject("storage", purchaseService.findStorageById(storageId));
                mv.addObject("purchase", order);
                mv.addObject("purchase_purchaseTotalMoney", String.valueOf(order.get("purchaseTotalMoney")));
                mv.addObject("purchase_totalMoney", String.valueOf(order.get("totalMoney")));
                mv.addObject("purchase_payableMoney", String.valueOf(order.get("payableMoney")));
                mv.addObject("purchase_realMoney", String.valueOf(order.get("realMoney")));
                mv.addObject("purchaseCode", order.get("purchaseCode"));
            }
            return mv;
        }
    }
    
    /**
     * 跳采购单明细
     * @return
     */
    @RequestMapping("toOrderDetail")
    public ModelAndView toOrderDetail(@RequestParam(required = true, defaultValue = "0", value = "providerId") int providerId,
        @RequestParam(required = true, defaultValue = "0", value = "storageId") int storageId, String purchaseCode)
        throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("purchase/orderDetail");
        mv.addObject("provider", purchaseService.findProviderById(providerId));
        mv.addObject("storage", purchaseService.findStorageById(storageId));
        
        Map<String, Object> order = purchaseService.findOrderByPurchaseCode(purchaseCode);
        mv.addObject("purchase_purchaseTotalCount", String.valueOf(order.get("purchaseTotalCount")));
        mv.addObject("purchase_purchaseTotalMoney", String.valueOf(order.get("purchaseTotalMoney")));
        mv.addObject("purchase_freightMoney", String.valueOf(order.get("freightMoney")));
        mv.addObject("purchase_totalMoney", String.valueOf(order.get("totalMoney")));
        mv.addObject("purchase_payableMoney", String.valueOf(order.get("payableMoney")));
        mv.addObject("purchase_adjustMoney", String.valueOf(order.get("adjustMoney")));
        mv.addObject("purchase_realMoney", String.valueOf(order.get("realMoney")));
        mv.addObject("purchaseCode", purchaseCode);
        mv.addObject("purchase", order);
        
        Map<String, Object> payDetail = purchaseService.sumPurchasePayDetail(purchaseCode);
        if(payDetail.get("sumPaidMoney") != null)
        {
            mv.addObject("payDetail_sumPaidMoney", String.valueOf(payDetail.get("sumPaidMoney")));
        }
        else
        {
            mv.addObject("payDetail_sumPaidMoney", 0);
        }
        if(payDetail.get("sumPaidMoneyRMB") != null)
        {
            mv.addObject("payDetail_sumPaidMoneyRMB", String.valueOf(payDetail.get("sumPaidMoneyRMB")));
        }
        else 
        {
            mv.addObject("payDetail_sumPaidMoneyRMB", 0);
        }
        BigDecimal realMoneyDecimal = new BigDecimal(order.get("realMoney").toString());
        if (payDetail.get("sumPaidMoney") != null)
        {
            BigDecimal sumPaidMoneyDecimal = new BigDecimal(payDetail.get("sumPaidMoney").toString());
            mv.addObject("payDetail_unPaidMoney", realMoneyDecimal.subtract(sumPaidMoneyDecimal).toString());
        }
        return mv;
    }
    
    /**
     * 新增修改采购单
     * @param param
     * @return
     */
    @RequestMapping("saveOrUpdateOrder")
    @ResponseBody
    @ControllerLog(description = "新增修改采购单")
    public Object saveOrUpdateOrder(@RequestParam Map<String, Object> param)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            Object id = param.get("id");
            if (id == null || id.toString() == "" || Integer.valueOf(id.toString()).intValue() == 0)
            {
                resultMap.put("data", purchaseService.savePurchaseOrder(param));
            }
            else
            {
                resultMap.put("data", purchaseService.updatePurchaseOrder(param));
            }
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询采购单商品列表
     * @param purchaseCode
     * @return
     */
    @RequestMapping("findOrderProductListInfo")
    @ResponseBody
    public Object findOrderProductListInfo(HttpServletResponse response, String purchaseCode, String isDetail, String isExport)
    {
        try
        {
            List<Map<String, Object>> result = purchaseService.findOrderProductListInfo(purchaseCode, isDetail);
            if (StringUtils.isNotBlank(isExport))
            {
                String displayName = "采购单商品";
                String[] headContent = {"采购单商品ID", "商品条码", "商品名称", "品牌", "采购数", "供货单价", "合计金额", "规格", "采购单位", "箱规", "生产日期", "保质期"};
                excel(response, displayName, headContent, map2arr(result));
                return null;
            }
            return result;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    @RequestMapping("importOrderProduct")
    @ResponseBody
    public Object importOrderProduct(MultipartHttpServletRequest multipartRequest)
        throws Exception
    {
        try
        {
            MultipartFile file = multipartRequest.getFile("orderProductFile");
            purchaseService.importOrderProduct(file);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error("导入采购单商品失败", ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    @RequestMapping("importNewOrderProduct")
    @ResponseBody
    public Object importNewOrderProduct(MultipartHttpServletRequest multipartRequest, String purchaseCode, String providerId)
        throws Exception
    {
        try
        {
            MultipartFile file = multipartRequest.getFile("orderProductFile");
            purchaseService.importNewOrderProduct(file, purchaseCode, providerId);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error("导入采购单商品失败", ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    private List<Object[]> map2arr(List<Map<String, Object>> result)
    {
        List<Object[]> rowContents = new ArrayList<Object[]>();
        if (result != null && result.size() > 0)
        {
            for (Map<String, Object> map : result)
            {
                int i = 0;
                Object[] obj = new Object[12];
                obj[i++] = map.get("id");
                obj[i++] = map.get("barcode");
                obj[i++] = map.get("productName");
                obj[i++] = map.get("brandName");
                obj[i++] = map.get("purchaseQuantity");
                obj[i++] = map.get("providerPrice");
                obj[i++] = map.get("totalPrice");
                obj[i++] = map.get("specification");
                obj[i++] = map.get("purchaseUnit");
                obj[i++] = map.get("boxSpecification");
                obj[i++] = map.get("manufacturerDate");
                obj[i++] = map.get("durabilityPeriod");
                rowContents.add(obj);
            }
        }
        return rowContents;
    }
    
    /**
     * 查询采购单可新增的商品
     * @param providerId
     * @param purchaseCode
     * @return
     */
    @RequestMapping("findProviderProduct4OrderListInfo")
    @ResponseBody
    public Object findProviderProduct4OrderListInfo(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode, String productName)
    {
        try
        {
            return purchaseService.findProviderProduct4OrderListInfo(providerId, storageId, purchaseCode, brandId, productId, barcode, productName);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 保存采购单商品
     * @param purchaseCode
     * @param providerProductIds
     * @return
     */
    @RequestMapping("saveOrderProduct")
    @ResponseBody
    @ControllerLog(description = "保存采购单商品")
    public Object saveOrderProduct(String purchaseCode, String providerProductIds)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("msg", purchaseService.saveOrderProduct(purchaseCode, providerProductIds));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 更新采购单商品
     * @param id
     * @param barcode
     * @param purchaseQuantity
     * @param providerPrice
     * @param manufacturerDate
     * @param durabilityPeriod
     * @return
     */
    @RequestMapping("updateOrderProduct")
    @ResponseBody
    @ControllerLog(description = "更新采购单商品")
    public Object updateOrderProduct(int id, String barcode, String purchaseQuantity, String providerPrice, String manufacturerDate, String durabilityPeriod)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("msg", purchaseService.updateOrderProduct(id, barcode, purchaseQuantity, providerPrice, manufacturerDate, durabilityPeriod));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 删除采购单商品
     * @param ids
     * @return
     */
    @RequestMapping("removeOrderProduct")
    @ResponseBody
    @ControllerLog(description = "删除采购单商品")
    public Object removeOrderProduct(String ids)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("msg", purchaseService.removeOrderProduct(ids));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 供应商商品列表详情
     * @param productBaseId
     * @param barcode
     * @param name
     * @param brandId
     * @param providerId
     * @param remark
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("providerProductListInfo")
    @ResponseBody
    public Object providerProductListInfo(@RequestParam(defaultValue = "0", required = false, value = "id") int id,
        @RequestParam(defaultValue = "", required = false, value = "barcode") String barcode, @RequestParam(defaultValue = "", required = false, value = "name") String name,
        @RequestParam(defaultValue = "0", required = false, value = "brandId") int brandId,
        @RequestParam(defaultValue = "0", required = false, value = "providerId") int providerId, int type,
        @RequestParam(defaultValue = "0", required = false, value = "storageId") int storageId, @RequestParam(defaultValue = "", required = false, value = "remark") String remark,
        @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return purchaseService.findProviderProductListInfo(id, barcode, name, brandId, providerId, storageId, remark, page, rows, type);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 根据ID查询供应商商品详情
     * @param id
     * @return
     */
    @RequestMapping("providerProductInfoById/{id}")
    @ResponseBody
    public Object providerProductInfoById(@PathVariable int id)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.findProviderProductInfoById(id));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 新增、修改供应商商品信息
     * @param param
     * @return
     */
    @RequestMapping("saveOrUpateProviderProduct")
    @ResponseBody
    @ControllerLog(description = "新增、修改供应商商品信息")
    public Object saveOrUpateProviderProduct(HttpServletRequest req, @RequestParam Map<String, Object> param)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.saveOrUpdateProviderProduct(req, param));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询供应商列表信息
     * 
     * @param id
     *            分仓ID
     * @return
     */
    @RequestMapping("providerListInfo")
    @ResponseBody
    public Object providerListInfo(@RequestParam(defaultValue = "0", required = false) int id, String remark,
        @RequestParam(defaultValue = "0", required = false) int purchaseSubmitType, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, int type)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return purchaseService.findProviderListInfo(id, remark, purchaseSubmitType, page, rows, type);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 供应商列表
     * @return
     */
    @RequestMapping("providerList")
    @ResponseBody
    public Object providerList(@RequestParam(value = "type", required = false, defaultValue = "0") int type)
    {
        try
        {
            return purchaseService.findProviderList(type);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    @RequestMapping("saveOrUpdateProvider")
    @ResponseBody
    @ControllerLog(description = "修改或保存供应商信息")
    public Object saveOrUpdateProvider(ProviderEntity entity)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.saveOrUpdateProvider(entity));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询分仓列表信息
     * 
     * @param id
     *            分仓ID
     * @return
     */
    @RequestMapping("storageListInfo")
    @ResponseBody
    public Object storageListInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows, int type)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return purchaseService.findStorageListInfo(page, rows, type);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 分仓列表
     * @return
     */
    @RequestMapping("storageList")
    @ResponseBody
    public Object storageList(@RequestParam(value = "type", required = false, defaultValue = "0") int type)
    {
        try
        {
            return purchaseService.findStorageList(type);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 根据ID查询分仓信息
     * 
     * @param id
     *            分仓ID
     * @return
     */
    @RequestMapping("findStorageById/{id}")
    @ResponseBody
    public Object findStorageById(@PathVariable int id)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.findStorageById(id));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 新增或更新分仓信息
     * 
     * @param id
     *            新增时ID为0
     * @param name
     *            名称
     * @param detailAddress
     *            详细地址
     * @param contactPerson
     *            联系人呢
     * @param contactPhone
     *            联系方式
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("saveOrUpdateStorage")
    @ResponseBody
    @ControllerLog(description = "修改或保存分仓信息")
    public Object saveOrUpdateStorage(@RequestParam(defaultValue = "0", required = false) int id, String name, String detailAddress, String contactPerson, String contactPhone,
        String sellerId, short type)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.saveOrUpdateStorage(id, name, detailAddress, contactPerson, contactPhone, sellerId, type));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 导入供应商商品
     * @param request
     * @return
     */
    @RequestMapping("importProviderProduct")
    @ResponseBody
    public Object importProviderProduct(HttpServletRequest request, MultipartHttpServletRequest multipartRequest)
    {
        try
        {
            MultipartFile file = multipartRequest.getFile("productFile");
            purchaseService.importProviderProduct(file);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error("导入供应商商品失败", ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 导出小的excel文件，只适合小的excel文件，如果文件内容太大。容易内存溢出
     * @param response
     * @param displayName
     * @param headContent
     * @param rowContent
     */
    private void excel(HttpServletResponse response, String displayName, String[] headContent, List<Object[]> rowContents)
    {
        HSSFWorkbook workbook = null;
        OutputStream fOut = null;
        try
        {
            // 进行转码，使其支持中文文件名
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + new String(displayName.getBytes("UTF-8"), "ISO8859-1") + ".xls");
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            int rowCount = 0;
            // 写入excel文件头
            if (headContent != null && headContent.length > 0)
            {
                Row row = sheet.createRow(rowCount++);
                for (int i = 0; i < headContent.length; i++)
                {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(headContent[i]);
                }
            }
            // 写入excel内容
            if (rowContents != null && rowContents.size() > 0)
            {
                for (Object[] rowContent : rowContents)
                {
                    Row row = sheet.createRow(rowCount++);
                    for (int i = 0; i < rowContent.length; i++)
                    {
                        Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                        cell.setCellValue(String.valueOf(rowContent[i]));
                    }
                }
            }
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception ex)
        {
            logger.error("导出excle失败！！！", ex);
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
     * 批量导入供应商商品模板
     * @param request
     * @param response
     */
    @RequestMapping("downloadTemplate")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response)
    {
        String displayName = "批量导入供应商商品模板";
        String[] headContent = {"品牌名称", "商品条码", "商品名称", "规格", "采购单位", "售卖单位", "换算倍数", "箱规", "供应商名称", "入库仓库", "备注"};
        excel(response, displayName, headContent, null);
    }
    
    /**
     * 查询采购单付款信息，可以不指定ID，默认值为0，表示查询付款信息列表。指定ID表示查询单条查询付款
     * @param id
     * @return
     */
    @RequestMapping("findPurchasePayDetailByParam")
    @ResponseBody
    public Object findPurchasePayDetailByParam(String purchaseCode, String status, String providerId, String startTime, String endTime,
        @RequestParam(required = false, defaultValue = "0") int id, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            if (id < 1)
            {
                page = page == 0 ? 1 : page;
                return JSON.toJSONStringWithDateFormat(purchaseService.findPurchasePayDetailByParam(id, purchaseCode, status, providerId, startTime, endTime, page, rows), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
            }
            else
            {
                Map<String, Object> payDetail = purchaseService.findPurchasePayDetailByParam(id, purchaseCode, status, providerId, startTime, endTime, page, rows);
                Map<String, Object> resultMap = new HashMap<String, Object>();
                if (payDetail != null && payDetail.get("rows") != null)
                {
                    resultMap.put("status", 1);
                    resultMap.put("data", JSON.toJSONStringWithDateFormat(((List<?>)payDetail.get("rows")).get(0), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
                }
                else
                {
                    resultMap.put("status", 0);
                    resultMap.put("msg", "找不到对应的采购结款记录！");
                }
                return resultMap;
            }
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 删除一条采购单付款信息
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("deletePurchasePay/{id}")
    @ResponseBody
    @ControllerLog(description = "删除一条采购单付款信息")
    public Object deletePurchasePayDetail(@PathVariable int id)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.deletePurchasePayDetail(id));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 保存、修改采购单付款信息
     * @param param
     * @return
     */
    @RequestMapping("saveOrUpdatePurchasePayDetail")
    @ResponseBody
    @ControllerLog(description = "保存、修改采购单付款信息")
    public Object saveOrUpdatePurchasePayDetail(String id, String purchaseCode, String type, String providerId, String storageId, String payMoney, String currentRate,
        String remark, String isPaid)
    {
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", id);
            param.put("purchaseCode", purchaseCode);
            param.put("type", type);
            param.put("providerId", providerId);
            param.put("storageId", storageId);
            param.put("payMoney", payMoney);
            param.put("currentRate", currentRate);
            if (StringUtils.isNotBlank(payMoney) && StringUtils.isNotBlank(currentRate))
            {
                BigDecimal payMoneyDecimal = new BigDecimal(payMoney);
                BigDecimal currentRateDecimal = new BigDecimal(currentRate);
                param.put("payRMB", payMoneyDecimal.multiply(currentRateDecimal));
            }
            param.put("remark", remark);
            param.put("isPaid", isPaid);
            
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.saveOrUpdatePurchasePayDetail(param));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 采购单商品入库列表信息
     * @param purchaseCode
     * @return
     */
    @RequestMapping("purchaseProductStoringListInfo")
    @ResponseBody
    public Object purchaseProductStoringListInfo(String purchaseCode)
    {
        try
        {
            return JSON.toJSONStringWithDateFormat(purchaseService.findPurchaseProductStoring(purchaseCode), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    @RequestMapping("savePurchaseProductStoring")
    @ResponseBody
    @ControllerLog(description = "采购单商品入库")
    public Object savePurchaseProductStoring(String params, String status, String storingRemark)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.savePurchaseProductStoring(params, status, storingRemark));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 金额调整信息
     * @param purchaseCode
     * @return
     */
    @RequestMapping("findAdjustMoneyInfo")
    @ResponseBody
    public Object findAdjustMoneyInfo(String purchaseCode)
    {
        try
        {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 1);
            resultMap.put("data", purchaseService.findOrderByPurchaseCode(purchaseCode));
            return resultMap;
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 查询批次列表
     * @param provider
     * @return
     */
    @RequestMapping("batchListInfo")
    @ResponseBody
    public Object batchListInfo(String providerProductId, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "50") int rows)
    {
        try
        {
            page = page == 0 ? 1 : page;
            return JSON.toJSONStringWithDateFormat(purchaseService.findBatchListInfo(providerProductId, page, rows), "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
            return resultMap;
        }
    }
    
    /**
     * 导出采购单
     * @param response
     */
    @RequestMapping("exportOrder")
    public void exportOrder(HttpServletResponse response, String param)
    {
        OutputStream fOut = null;
        FileInputStream fInput = null;
        try
        {
            if (StringUtils.isBlank(param))
                return;
            
            List<PurchaseOrderInfoEntity> exportPurchaseOrderList = purchaseService.findExportPurchaseOrder(param);
            if (exportPurchaseOrderList == null || exportPurchaseOrderList.size() < 1)
            {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().print("<html><h1>采购单商品不存在，赶紧联系管理员<h1></html>");
                return;
            }
            
            // 进行转码，使其支持中文文件名
            String codedFileName = CommonUtil.date2String(new Date(), "MM.dd") + "-采购单.zip";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + new String(codedFileName.getBytes("UTF-8"), "ISO8859-1"));
            
            String nowStr = DateTime.now().toString("yyyy-MM-dd_HH_mm_ss");
            File fileDir = new File(YggAdminProperties.getInstance().getPropertie("order_zip_download_dir"));
            File newDir = new File(fileDir, nowStr + "_" + new Random().nextInt(10000) + "_send");
            newDir.mkdirs();
            
            if (exportPurchaseOrderList != null && exportPurchaseOrderList.size() > 0)
            {
                for (PurchaseOrderInfoEntity orderInfo : exportPurchaseOrderList)
                {
                    purchaseService.exportOrder(newDir, orderInfo);
                }
            }
            
            if (exportPurchaseOrderList != null && exportPurchaseOrderList.size() > 0)
            {
                for (PurchaseOrderInfoEntity orderInfo : exportPurchaseOrderList)
                {
                    Map<String, Object> tp = new HashMap<String, Object>();
                    tp.put("id", orderInfo.getId());
                    int status = orderInfo.getStatus();
                    if (status > 1)
                        continue;
                    tp.put("status", 2);
                    purchaseService.updatePurchaseOrder(tp);
                }
            }
            
            String zipFileName = newDir + codedFileName;
            ZipCompressorByAnt zip = new ZipCompressorByAnt(zipFileName);
            zip.compressExe(newDir.getPath());
            fOut = response.getOutputStream();
            fInput = new FileInputStream(zipFileName);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fInput.read(buf)) != -1)
            {
                fOut.write(buf, 0, len);
            }
            fOut.flush();
            fOut.close();
            fInput.close();
            
            FileUtil.deleteFile(newDir.getPath());
            FileUtil.deleteFile(zipFileName);
        }
        catch (Exception ex)
        {
            logger.error("导出采购单失败！！！", ex);
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
            if (fInput != null)
            {
                try
                {
                    fInput.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping(value = "/findProviderProductByBarcode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findProviderProductByBarcode(@RequestParam(value = "barCode", required = true) String barCode)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try
        {
            if (barCode.indexOf("%") > 0)
            {
                barCode = barCode.substring(0, barCode.lastIndexOf("%"));
            }
            resultMap.put("status", 1);
            resultMap.putAll(purchaseService.findProviderProductInfoByBarCode(barCode));
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
        }
        return JSON.toJSONString(resultMap);
    }
    
    @RequestMapping(value = "/editStock", produces = "application/json;charset=UTF-8")
    public ModelAndView editStock(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("purchase/editStock");
        Map<String, Object> info = purchaseService.findProviderProductStockInfoById(id);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("providerProductId", id);
        List<Map<String, Object>> storeList = purchaseStoringService.findPurchaseStoringByParam(param);
        if (info == null || info.isEmpty())
        {
            return mv;
        }
        mv.addObject("id", id + "");
        mv.addObject("name", info.get("name").toString());
        mv.addObject("barcode", info.get("barcode").toString());
        mv.addObject("storageName", info.get("storageName").toString());
        mv.addObject("remainStock", info.get("remainStock").toString());
        
        if (storeList != null && !storeList.isEmpty())
        {
            mv.addObject("gegeWatingStoring", storeList.get(0).get("gegeWatingStoring") == null ? "0" : storeList.get(0).get("gegeWatingStoring").toString());
            mv.addObject("unallocationStoring", storeList.get(0).get("unallocationStoring") == null ? "0" : storeList.get(0).get("unallocationStoring").toString());
            mv.addObject("gegeUsableStoring", storeList.get(0).get("gegeUsableStoring") == null ? "0" : storeList.get(0).get("gegeUsableStoring").toString());
        }
        return mv;
    }
    
    @RequestMapping(value = "/jsonProductInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProductInfo(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
        @RequestParam(value = "rows", required = false, defaultValue = "30") int rows, @RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        try
        {
            if (page == 0)
            {
                page = 1;
            }
            return purchaseService.findProductInfoByppId(rows * (page - 1), rows, id);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("rows", new ArrayList<Map<String, Object>>());
            resultMap.put("total", 0);
            return JSON.toJSONString(resultMap);
        }
    }
    
    @RequestMapping(value = "/jsonProviderProductCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String jsonProviderProductCode(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
        @RequestParam(value = "isAvailable", required = false, defaultValue = "1") int isAvailable)
    {
        List<Map<String, String>> resultList = new ArrayList<>();
        try
        {
            Map<String, Object> para = new HashMap<>();
            para.put("isAvailable", isAvailable);
            List<Map<String, Object>> providerProductList = purchaseService.findAllProviderProduct(para);
            for (Map<String, Object> it : providerProductList)
            {
                Map<String, String> mp = new HashMap<>();
                mp.put("code", it.get("id").toString());
                mp.put("text", it.get("name") + "(" + it.get("specification") + "/" + it.get("purchaseUnit") + ")" + "-" + it.get("storageName"));
                if (Integer.parseInt(it.get("id").toString()) == id)
                {
                    mp.put("selected", "true");
                }
                resultList.add(mp);
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return JSON.toJSONString(resultList);
    }
    
    @RequestMapping(value = "/findProviderProductById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String findProviderProductById(@RequestParam(value = "id", required = false, defaultValue = "0") int id)
    {
        Map<String, Object> resultMap = null;
        try
        {
            resultMap = purchaseService.findProviderProductInfoById(id);
            if (resultMap != null)
            {
                resultMap.put("status", 1);
            }
            else
            {
                resultMap = new HashMap<String, Object>();
                resultMap.put("status", 0);
            }
            
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage(), ex);
            resultMap.put("status", 0);
            resultMap.put("msg", ex.getMessage());
        }
        return JSON.toJSONString(resultMap);
    }
    
    public static void main(String[] args)
    {
        ZipCompressorByAnt zip = new ZipCompressorByAnt("d:/a.b");
        zip.compressExe("d:/新建文件夹");
    }
}
