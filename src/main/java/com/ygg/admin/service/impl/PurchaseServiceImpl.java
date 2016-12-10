package com.ygg.admin.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygg.admin.code.PurchaseConstant;
import com.ygg.admin.dao.ProductBaseDao;
import com.ygg.admin.dao.PurchaseDao;
import com.ygg.admin.dao.ThirdPartyProductDao;
import com.ygg.admin.entity.BrandEntity;
import com.ygg.admin.entity.ProviderEntity;
import com.ygg.admin.entity.PurchaseOrderInfoEntity;
import com.ygg.admin.entity.PurchaseOrderProductEntity;
import com.ygg.admin.service.BrandService;
import com.ygg.admin.service.PurchaseService;
import com.ygg.admin.util.CommonUtil;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService
{
    
    @Resource
    private PurchaseDao purchaseDao;
    
    @Resource
    private BrandService brandService;
    
    @Resource
    private ProductBaseDao productBaseDao;
    
    @Resource
    private ThirdPartyProductDao thirdPartyProductDao;
    
    //@Resource
    //private PurchaseStoringService purchaseStoringService;
    
    private static Map<String, String> SELLERIDNAME = new HashMap<String, String>();
    
    public static Map<String, String> BRANDINFO = new HashMap<String, String>();
    
    @Override
    public int saveOrUpdateStorage(int id, String name, String detailAddress, String concatPerson, String concatPhone, String sellerId, short type)
        throws Exception
    {
        // 验证仓库名称是否存在
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", name);
        param.put("id", id);
        param.put("countName", 1);
        int count = purchaseDao.countStorageByParam(param);
        if (count > 0)
            throw new RuntimeException("仓库名称已经存在");
        
        // 验证商家是否已经绑定了仓库
        param.clear();
        param.put("sellerId", sellerId);
        param.put("countName", 1);
        param.put("id", id);
        count = purchaseDao.countStorageByParam(param);
        if (count > 0)
            throw new RuntimeException("商家已经和其他仓库关联");
        
        if (id < 1)
        {
            return purchaseDao.saveStorage(name, detailAddress, concatPerson, concatPhone, sellerId, type);
        }
        else
        {
            return purchaseDao.updateStorage(id, name, detailAddress, concatPerson, concatPhone, sellerId, type);
        }
    }
    
    @Override
    public Map<String, Object> findStorageById(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return purchaseDao.findStorageByParam(param);
    }
    
    @Override
    public List<Map<String, Object>> findStorageListInfo(int page, int rows, int type)
        throws Exception
    {
        if (SELLERIDNAME.size() < 1)
        {
            for (int i = 0; i < PurchaseConstant.NAME.length; i++)
            {
                SELLERIDNAME.put(PurchaseConstant.ID[i], PurchaseConstant.NAME[i]);
            }
        }
        List<Map<String, Object>> result = purchaseDao.findStorageListInfo(page, rows, type);
        if (result != null && result.size() > 0)
        {
            for (Map<String, Object> map : result)
            {
                map.put("sellerName", SELLERIDNAME.get(map.get("sellerId") + ""));
            }
        }
        return result;
    }
    
    @Override
    public List<Map<String, Object>> findStorageList(int type)
        throws Exception
    {
        return purchaseDao.findStorageList(type);
    }
    
    @Override
    public Map<String, Object> findProviderListInfo(int id, String remark, int purchaseSubmitType, int page, int rows, int type)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("remark", remark);
        param.put("purchaseSubmitType", purchaseSubmitType);
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("type", type);
        result.put("rows", purchaseDao.findProviderListInfoByParam(param));
        result.put("total", purchaseDao.countTotalProviderByParam(param));
        return result;
    }
    
    @Override
    public int saveOrUpdateProvider(ProviderEntity entity)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", entity.getName());
        param.put("id", entity.getId());
        param.put("countName", 1);
        int count = purchaseDao.countProviderByParam(param);
        entity.setDay(StringUtils.replace(entity.getDay(), ",", ""));
        entity.setPercent(StringUtils.replace(entity.getPercent(), ",", ""));
        entity.setDay(entity.getDay() == "" ? "0" : entity.getDay());
        if (count > 0)
            throw new RuntimeException("供货商名称已经存在");
        if (entity.getId() < 1)
        {
            return purchaseDao.saveProvider(entity);
        }
        else
        {
            return purchaseDao.updateProvider(entity);
        }
    }
    
    @Override
    public ProviderEntity findProviderById(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return purchaseDao.findProviderByParam(param);
    }
    
    @Override
    public List<Map<String, Object>> findProviderList(int type)
        throws Exception
    {
        return purchaseDao.findProviderList(type);
    }
    
    @Override
    public Map<String, Object> findProviderProductListInfo(int id, String barcode, String name, int brandId, int providerId, int storageId, String remark, int page, int rows, int type)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("barcode", barcode);
        param.put("id", id == 0 ? null : id);
        param.put("name", name);
        param.put("brandId", brandId);
        param.put("providerId", providerId);
        param.put("storageId", storageId);
        param.put("remark", remark);
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("type", type);
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> providerProductList = purchaseDao.findProviderProductListInfoByParam(param);
        appendInfo(providerProductList);
        //appendStoring(providerProductList);
        result.put("rows", providerProductList);
        result.put("total", purchaseDao.countTotalProviderProductByParam(param));
        return result;
    }
    
    /*private void appendStoring(List<Map<String, Object>> providerProductList)
    throws Exception
    {
    if (providerProductList == null || providerProductList.isEmpty())
        return;
    List<Object> list = new ArrayList<Object>();
    for (Map<String, Object> map : providerProductList)
    {
        list.add(map.get("id"));
    }
    List<Map<String, Object>> providerProductStoringList = purchaseStoringService.findPurchaseStoringByIds(list);
    Map<String, Object> unpush = purchaseStoringService.statisticsUnpush();
    for (Map<String, Object> product : providerProductList)
    {
        String providerProductId = String.valueOf(product.get("id"));
        for (Map<String, Object> storing : providerProductStoringList)
        {
            if (StringUtils.equals(providerProductId, String.valueOf(storing.get("providerProductId"))))
            {
                // 待入库数
                product.put("gegeWatingStoring", storing.get("gegeWatingStoring"));
                // 实物库存
                product.put("storageRealStoring", storing.get("storageRealStoring"));
                // 可用库存
                product.put("storageUsableStoring", storing.get("storageUsableStoring"));
                // 已付款未推送订单
                product.put("unpush", unpush.get("providerProductId"));
                // 未分配库存
                product.put("unallocationStoring", storing.get("unallocationStoring"));
                // 渠道剩余库存总和
                product.put("channelRemainStoring", storing.get("channelRemainStoring"));
            }
        }
        
    }
    }*/
    
    @Override
    public Map<String, Object> findProviderProductInfoById(int id)
        throws Exception
    {
        Map<String, Object> result = purchaseDao.findProviderProductById(id);
        result.put("formBrandId", result.get("brandId"));
        return result;
    }
    
    @Override
    public int saveOrUpdateProviderProduct(HttpServletRequest req, Map<String, Object> param)
        throws Exception
    {
        // 兑换率如果没有填写。赋默认值
        param.put("ratio", StringUtils.isBlank((String)param.get("ratio")) ? 1 : param.get("ratio"));
        param.put("barcode", StringUtils.substringBefore((String)param.get("barcode"), "%"));
        
        // 采购商品是否已经存在
        String barcode = (String)param.get("barcode");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("barcode", barcode);
        map.put("storageId", param.get("formStorageId"));
        List<Map<String, Object>> providerProductList = purchaseDao.findRelationProviderStorageProviderProductByParam(map);
        if (providerProductList != null && providerProductList.size() > 0 && param.get("id") == null)
        {
            // 已经有供应商为基本商品ID供货，
            // 不允许新增操作
            throw new RuntimeException("该商品条形码已关联采购商品，请勿重复关联，如需操作，可以修改。");
        }
        else if (providerProductList != null && providerProductList.size() > 0)
        {
            // 删除供应商品以有关联关系
            purchaseDao.deleteRelationProviderStorageProviderProduct(barcode);
            // 保存新的关联关系
            param.put("id", providerProductList.get(0).get("provider_product_id"));
            saveRelationProviderStorageProviderProduct(req, param);
            // 更新供应商品信息
            return purchaseDao.updateProviderProduct(param);
        }
        else
        {
            // 保存供应商品信息
            int result = purchaseDao.saveProviderProduct(param);
            if (result < 1)
                throw new RuntimeException("保存供应商商品失败");
            // 保存关联关系
            saveRelationProviderStorageProviderProduct(req, param);
            if (result < 1)
                throw new RuntimeException("保存供应商、仓库、供应商商品关联关系失败");
            
            return result;
        }
    }
    
    /**
     * 保存采购商品与供应商、仓库的关联关系
     * 
     * @param req
     * @param param
     * @return
     * @throws Exception
     */
    private int saveRelationProviderStorageProviderProduct(HttpServletRequest req, Map<String, Object> param)
        throws Exception
    {
        String[] formProviderIds = req.getParameterValues("formProviderId");
        if (formProviderIds == null || formProviderIds.length < 1)
            throw new RuntimeException("保存供应商、仓库、供应商商品关联关系失败，没有找到供应商名称");
        
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (String formProviderId : formProviderIds)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("providerId", formProviderId);
            map.put("storageId", param.get("formStorageId"));
            map.put("barcode", param.get("barcode"));
            map.put("providerProductId", param.get("id"));
            list.add(map);
        }
        return purchaseDao.saveRelationProviderStorageProviderProduct(list);
    }
    
    private String getKey(String value)
    {
        Set<Entry<String, String>> entrySet = BRANDINFO.entrySet();
        Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext())
        {
            Entry<String, String> next = iterator.next();
            if (StringUtils.equals(next.getValue(), value))
            {
                return next.getKey();
            }
        }
        return "";
    }
    
    @Override
    public int importProviderProduct(MultipartFile file)
        throws Exception
    {
        HSSFWorkbook workbook = null;
        int k = 0;
        try
        {
            workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet = workbook.getSheetAt(0);
            int count = sheet.getLastRowNum() + 1 - sheet.getFirstRowNum();
            // 刷新品牌
            refreshBrandMap();
            for (int i = 1; i < count; i++)
            {
                int j = 0;
                k = i;
                HSSFRow row = sheet.getRow(i);
                Map<String, Object> providerProduct = new HashMap<String, Object>();
                // 基本商品ID
                HSSFCell cell = row.getCell(j++);
                if (cell == null)
                    continue;
                String brandName = getValue(cell);
                if (StringUtils.isBlank(brandName))
                    continue;
                
                providerProduct.put("formBrandId", getKey(brandName));
                // 读取excel中的其他信息
                String barcode = getValue(row.getCell(j++));
                barcode = StringUtils.substringBefore(barcode, "%");
                
                providerProduct.put("barcode", barcode);
                providerProduct.put("name", getValue(row.getCell(j++)));
                providerProduct.put("specification", getValue(row.getCell(j++)));
                providerProduct.put("purchaseUnit", getValue(row.getCell(j++)));
                providerProduct.put("sellingUnit", getValue(row.getCell(j++)));
                
                // 验证兑换率是否存在
                int ratio = (int)row.getCell(j++).getNumericCellValue();
                if (ratio < 1)
                    throw new RuntimeException(String.format("第%s行换算倍数：【%s】不对，请重新填写。", i, ratio));
                providerProduct.put("ratio", ratio);
                providerProduct.put("boxSpecification", getValue(row.getCell(j++)));
                
                // 验证供应商是否存在
                String providerName = getValue(row.getCell(j++));
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("name", providerName);
                ProviderEntity providerEntity = purchaseDao.findProviderByParam(param);
                if (providerEntity == null || providerEntity.getId() < 1)
                    throw new RuntimeException(String.format("第%s行供应商名称：【%s】不对，请重新填写。", i, providerName));
                providerProduct.put("formProviderId", providerEntity.getId());
                
                // 验证仓库是否存在
                String storageName = getValue(row.getCell(j++));
                param.put("name", storageName);
                Map<String, Object> storageInfo = purchaseDao.findStorageByParam(param);
                if (storageInfo == null || storageInfo.size() < 1)
                    throw new RuntimeException(String.format("第%s行仓库名称：【%s】不对，请重新填写。", i, storageName));
                
                // 商品条码关联的所有供应商
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("barcode", barcode);
                map.put("storageId", storageInfo.get("id"));
                List<Map<String, Object>> providerProductList = purchaseDao.findRelationProviderStorageProviderProductByParam(map);
                
                // 基本商品ID已关联采购商品
                if (providerProductList != null && providerProductList.size() > 0)
                {
                    // 判断已经关联的供货商中
                    // 是否已经存在该供货商
                    if (checkProviderId(providerProductList, providerEntity.getId()))
                    {
                        // 不需要坐任何操作
                        continue;
                    }
                    else
                    {
                        saveRelationProviderStorageProviderProduct(providerEntity.getId(), storageInfo.get("id"), barcode, providerProductList.get(0).get("provider_product_id"));
                    }
                }
                else
                {
                    // 采购商品第一次入库
                    providerProduct.put("formStorageId", storageInfo.get("id"));
                    providerProduct.put("remark", getValue(row.getCell(j++)));
                    providerProduct.put("isAvailable", 1);
                    // 保存采购商品信息
                    providerProduct.put("id", "");
                    purchaseDao.saveProviderProduct(providerProduct);
                    
                    // 新建采购商品与该供货商关系
                    List<Map<String, Object>> relateionList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> relateion = new HashMap<String, Object>();
                    relateion.put("providerId", providerEntity.getId());
                    relateion.put("storageId", storageInfo.get("id"));
                    relateion.put("barcode", barcode);
                    relateion.put("providerProductId", providerProduct.get("id"));
                    relateionList.add(relateion);
                    purchaseDao.saveRelationProviderStorageProviderProduct(relateionList);
                }
            }
            return 1;
        }
        catch (Exception ex)
        {
            throw new RuntimeException(String.format("第%s行请重新填写。%s", k, ex.getMessage()));
        }
        finally
        {
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
     * 保存采购商品与供应商、仓库的关联关系
     * @param providerId
     * @param storageId
     * @param barcode
     * @param providerProductId
     * @throws Exception
     */
    private void saveRelationProviderStorageProviderProduct(int providerId, Object storageId, String barcode, Object providerProductId)
        throws Exception
    {
        // 新建采购商品与该供货商关系
        List<Map<String, Object>> relateionList = new ArrayList<Map<String, Object>>();
        Map<String, Object> relateion = new HashMap<String, Object>();
        relateion.put("providerId", providerId);
        relateion.put("storageId", storageId);
        relateion.put("barcode", barcode);
        relateion.put("providerProductId", providerProductId);
        relateionList.add(relateion);
        purchaseDao.saveRelationProviderStorageProviderProduct(relateionList);
    }
    
    private String getValue(HSSFCell hssfCell)
    {
        if (hssfCell == null)
            return "";
        hssfCell.setCellType(Cell.CELL_TYPE_STRING);
        String a = hssfCell.getStringCellValue();
        return String.valueOf(a);
    }
    
    private boolean checkProviderId(List<Map<String, Object>> list, int providerId)
    {
        if (list == null || list.size() < 1)
            return false;
        for (Map<String, Object> map : list)
        {
            Object temp = map.get("provider_id");
            if (StringUtils.equals(temp.toString(), String.valueOf(providerId)))
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Map<String, Object> findOrderListInfo(String purchaseCode, String status, String providerId, String storageId, String startTime, String endTime, int page, int rows, int type)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("purchaseCode", purchaseCode);
        param.put("status", status);
        param.put("providerId", providerId);
        param.put("storageId", storageId);
        param.put("startTime", startTime);
        if (StringUtils.isNotBlank(endTime))
            param.put("endTime", endTime + " 23:59:59");
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("type", type);
        result.put("rows", purchaseDao.findOrderListInfoByParam(param));
        result.put("total", purchaseDao.countTotalOrder(param));
        return result;
    }
    
    @Override
    public int deleteOrderById(int orderId)
        throws Exception
    {
        purchaseDao.deleteOrderProductByOrderId(orderId);
        return purchaseDao.deleteOrderById(orderId);
    }
    
    @Override
    public Map<String, Object> findOrderByPurchaseCode(String purchaseCode)
        throws Exception
    {
        return purchaseDao.findOrderByPurchaseCode(purchaseCode);
    }
    
    @Override
    public List<Map<String, Object>> findOrderProductListInfo(String purchaseCode, String isDetail)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("purchaseCode", purchaseCode);
        param.put("isDetail", isDetail);
        List<Map<String, Object>> result = purchaseDao.findOrderProductListInfo(param);
        appendInfo(result);
        return result;
    }
    
    /**
     * 给对象添加基本商品对应的品牌信息和基本商品信息
     * @param infoList
     * @throws Exception
     */
    private void appendInfo(List<Map<String, Object>> infoList)
        throws Exception
    {
        refreshBrandMap();
        for (Map<String, Object> info : infoList)
        {
            info.put("brandName", BRANDINFO.get(info.get("brandId") + ""));
        }
    }
    
    @Override
    public int savePurchaseOrder(Map<String, Object> param)
        throws Exception
    {
        int result = purchaseDao.savePurchaseOrder(param);
        return result;
    }
    
    @Override
    public int updatePurchaseOrder(Map<String, Object> param)
        throws Exception
    {
        Object obj = param.get("status");
        // 状态等于4，完结采购单时，计算采购商品中运费相关信息
        if (obj != null && StringUtils.equals("4", obj.toString()))
        {
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("purchaseCode", param.get("purchaseCode"));
            List<Map<String, Object>> orderProduct = purchaseDao.findOrderProductListInfo(temp);
            for (Map<String, Object> map : orderProduct)
            {
                Object o = map.get("isFinished");
                if (o == null || StringUtils.equals("0", o.toString()))
                {
                    throw new RuntimeException("该采购单存在未完结的采购商品，请先完结采购的商品，在完结采购单。");
                }
            }
            Map<String, Object> map = purchaseDao.findOrderByPurchaseCode(param.get("purchaseCode").toString());
            Object unpaidMoney = map.get("unpaidMoney");
            if (Math.abs(Float.valueOf(unpaidMoney.toString())) >= 0.01)
            {
                throw new RuntimeException("该采购单未付金额不等于0，不能完结采购单。");
            }
            
            Map<String, Object> tp = new HashMap<String, Object>();
            tp.put("purchaseCode", param.get("purchaseCode"));
            tp.put("paidRMB", map.get("paidRMB"));
            tp.put("purchaseTotalMoney", map.get("purchaseTotalMoney"));
            tp.put("processShareFreightMoney", map.get("freightMoney"));
            purchaseDao.updateOrderProduct(tp);
        }
        // 状态的5，确定采购该采购单
        // 库存、待入库数增加
        //if (obj != null && StringUtils.equals("5", obj.toString()))
        //{
        //    List<Map<String, Object>> result = purchaseDao.findOrderProductListInfo((String)param.get("purchaseCode"), null);
        //    purchaseStoringService.purchaseOrder(result);
        //}
        // 更新采购信息
        int result = purchaseDao.updatePurchaseOrder(param);
        if (result < 1)
            throw new RuntimeException("操作失败");
        return result;
    }
    
    @Override
    public Map<String, Object> findProviderProduct4OrderListInfo(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode,
        String productName)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = purchaseDao.findProviderProduct4OrderListInfo(providerId, storageId, purchaseCode, brandId, productId, barcode, productName);
        appendInfo(infoList);
        result.put("rows", infoList);
        result.put("total", purchaseDao.countTotalProviderProduct4OrderList(providerId, storageId, purchaseCode, brandId, productId, barcode, productName));
        return result;
    }
    
    @Override
    public int saveOrderProduct(String purchaseCode, String providerProductIds)
        throws Exception
    {
        if (StringUtils.isNotBlank(providerProductIds))
        {
            List<String> list = new ArrayList<String>();
            String[] idArr = StringUtils.split(providerProductIds, ",");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("purchaseCode", purchaseCode);
            for (String providerProductId : idArr)
            {
                list.add(providerProductId);
            }
            map.put("list", list);
            return purchaseDao.saveOrderProduct(map);
        }
        throw new RuntimeException("未选中商品");
    }
    
    @Override
    public int updateOrderProduct(int id, String barcode, String purchaseQuantity, String providerPrice, String manufacturerDate, String durabilityPeriod)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("barcode", barcode);
        param.put("purchaseQuantity", purchaseQuantity);
        param.put("providerPrice", providerPrice);
        if (StringUtils.isNotBlank(providerPrice) && StringUtils.isNotBlank(purchaseQuantity))
        {
            BigDecimal providerPriceDecimal = new BigDecimal(providerPrice);
            BigDecimal purchaseQuantityDecimal = new BigDecimal(purchaseQuantity);
            param.put("totalPrice", providerPriceDecimal.multiply(purchaseQuantityDecimal));
        }
        param.put("manufacturerDate", manufacturerDate);
        param.put("durabilityPeriod", durabilityPeriod);
        return purchaseDao.updateOrderProduct(param);
    }
    
    @Override
    public int removeOrderProduct(String ids)
        throws Exception
    {
        if (StringUtils.isNotBlank(ids))
        {
            String[] idArr = StringUtils.split(ids, ",");
            List<Integer> list = new ArrayList<Integer>();
            for (String id : idArr)
            {
                list.add(Integer.valueOf(id));
            }
            return purchaseDao.removeOrderProduct(list);
        }
        throw new RuntimeException("不要参数没有找到");
    }
    
    @Override
    public int createPurchaseCode(String day)
        throws Exception
    {
        int code = purchaseDao.findPurchaseCode(day);
        if (code == 0)
        {
            int result = savePurchaseCode(day);
            if (result > 0)
            {
                return 1;
            }
            throw new RuntimeException("生成采购单编码失败");
        }
        updatePurchaseCode(day, code + 1);
        return code;
    }
    
    @Override
    public int updatePurchaseCode(String day, int code)
        throws Exception
    {
        return purchaseDao.updatePurchaseCode(day, code);
    }
    
    @Override
    public int savePurchaseCode(String day)
        throws Exception
    {
        return purchaseDao.savePurchaseCode(day);
    }
    
    @Override
    public Map<String, Object> sumPurchasePayDetail(String purchaseCode)
        throws Exception
    {
        return purchaseDao.sumPurchasePayDetail(purchaseCode);
    }
    
    @Override
    public Map<String, Object> findPurchasePayDetailByParam(int id, String purchaseCode, String status, String providerId, String startTime, String endTime, int page, int rows)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("purchaseCode", purchaseCode);
        param.put("status", status);
        param.put("providerId", providerId);
        param.put("startTime", startTime);
        if (StringUtils.isNotBlank(endTime))
            param.put("endTime", endTime + " 23:59:59");
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", purchaseDao.findPurchasePayDetailByParam(param));
        result.put("total", purchaseDao.countTotalPurchasePayDetail(param));
        return result;
    }
    
    @Override
    public int saveOrUpdatePurchasePayDetail(Map<String, Object> param)
        throws Exception
    {
        Object id = param.get("id");
        if (id == null || StringUtils.isBlank(id.toString()) || StringUtils.equals("0", id.toString()))
        {
            // 保存付款明细
            return purchaseDao.savePurchasePayDetail(param);
        }
        else if (param.get("isPaid") != null)
        {
            // 财务付款
            int result = purchaseDao.updatePurchasePayDetail(param);
            if (result < 1)
                throw new RuntimeException("确认付款失败");
            // 更新采购单已付款相关信息
            Map<String, Object> tp = new HashMap<String, Object>();
            tp.put("id", id);
            List<Map<String, Object>> payList = purchaseDao.findPurchasePayDetailByParam(tp);
            if (payList == null || payList.size() < 1)
                throw new RuntimeException("更新已付款失败");
            Map<String, Object> map = payList.get(0);
            Map<String, Object> t = new HashMap<String, Object>();
            t.put("paidMoney", map.get("payMoney"));
            t.put("paidRMB", map.get("payRMB"));
            t.put("purchaseCode", map.get("purchaseCode"));
            purchaseDao.updatePurchaseOrder(t);
            return result;
        }
        else
        {
            // 更新付款明细
            return purchaseDao.updatePurchasePayDetail(param);
        }
    }
    
    @Override
    public int deletePurchasePayDetail(int id)
        throws Exception
    {
        return purchaseDao.deletePurchasePayDetail(id);
    }
    
    @Override
    public List<Map<String, Object>> findPurchaseProductStoring(String purchaseCode)
        throws Exception
    {
        return purchaseDao.findPurchaseProductStoring(purchaseCode);
    }
    
    @Override
    public int savePurchaseProductStoring(String params, String status, String storingRemark)
        throws Exception
    {
        if (StringUtils.isBlank(params) || StringUtils.isBlank(status))
            throw new RuntimeException("必要参数不存在");
        JSONArray parseArray = JSON.parseArray(params);
        String purchaseCode = null;
        int isFinished = StringUtils.equals(status, "1") ? 0 : 1;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < parseArray.size(); i++)
        {
            JSONObject object = parseArray.getJSONObject(i);
            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("id", object.get("id"));
            param.put("storingCount", object.get("currentStoringCount"));
            param.put("isFinished", isFinished);
            param.put("manufacturerDate", object.get("manufacturerDate"));
            param.put("durabilityPeriod", object.get("durabilityPeriod"));
            
            // 更新采购单商品信息
            purchaseDao.updateOrderProduct(param);
            // 维护库存数据
            //updateStoring(object, status);
            
            purchaseCode = object.getString("purchaseCode");
            map.put("purchaseCode", purchaseCode);
            map.put("providerProductId", object.get("providerProductId"));
            map.put("purchaseQuantity", object.get("purchaseQuantity"));
            map.put("barcode", object.get("barcode"));
            map.put("brandName", object.get("brandName"));
            // 仅完结时，入库数为0
            if (StringUtils.equals("2", status))
            {
                map.put("storingCount", 0);
            }
            else
            {
                map.put("storingCount", object.get("currentStoringCount"));
            }
            map.put("totalPrice", object.get("totalPrice"));
            map.put("providerPrice", object.get("providerPrice"));
            map.put("manufacturerDate", object.get("manufacturerDate"));
            map.put("durabilityPeriod", object.get("durabilityPeriod"));
            map.put("status", status);
            list.add(map);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("storingRemark", storingRemark);
        param.put("purchaseCode", purchaseCode);
        param.put("status", 3);
        // 跟新采购单
        purchaseDao.updatePurchaseOrder(param);
        return purchaseDao.savePurchaseProductStoring(list);
    }
    
    /**
     * 维护库存数据
     * @param param
     * @param status
     * @return
     * @throws Exception 
     */
    /*private int updateStoring(JSONObject object, String status)
        throws Exception
    {
        // 入库操作
        if (StringUtils.equals("1", status))
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("providerProductId", object.get("providerProductId"));
            param.put("storingCount", object.get("currentStoringCount"));
            return purchaseStoringService.updatePurchaseStoring(param);
        }
        // 完结sku
        else if (StringUtils.equals("2", status))
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("providerProductId", object.get("providerProductId"));
            param.put("purchaseCode", object.get("purchaseCode"));
            int storingCount = purchaseDao.sumStoringCountByParam(param);
            int purchaseQuantity = object.getIntValue("purchaseQuantity");
            int offsetCount = purchaseQuantity - storingCount;
            // 所有入库数
            param.put("isFinished", 1);
            param.put("offsetCount", offsetCount);
            return purchaseStoringService.updatePurchaseStoring(param);
        }
        // 入库并完结sku
        else if (StringUtils.equals("3", status))
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("providerProductId", object.get("providerProductId"));
            param.put("purchaseCode", object.get("purchaseCode"));
            int storingCount = purchaseDao.sumStoringCountByParam(param);
            // 加上本次入库数
            storingCount += object.getIntValue("currentStoringCount");
            int purchaseQuantity = object.getIntValue("purchaseQuantity");
            int offsetCount = purchaseQuantity - storingCount;
            // 本次入库数
            param.put("storingCount", object.getIntValue("currentStoringCount"));
            // 实际入库数与采购数存在的偏差
            param.put("offsetCount", offsetCount);
            param.put("isFinished", 1);
            return purchaseStoringService.updatePurchaseStoring(param);
        }
        return 0;
    }*/
    
    @Override
    public Map<String, Object> findBatchListInfo(String providerProductId, int page, int rows)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("providerProductId", providerProductId);
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> batchListInfo = purchaseDao.findBatchListInfo(param);
        if (batchListInfo != null && batchListInfo.size() > 0)
        {
            List<Object> codes = new ArrayList<Object>();
            for (Map<String, Object> batch : batchListInfo)
            {
                codes.add(batch.get("purchaseCode"));
            }
            // 批次第一次入库时间
            List<Map<String, Object>> batchStoringTime = purchaseDao.findBatchStoringTime(providerProductId, codes);
            for (Map<String, Object> batch : batchListInfo)
            {
                for (Map<String, Object> storingTime : batchStoringTime)
                {
                    if (StringUtils.equals(String.valueOf(batch.get("purchaseCode")), String.valueOf(storingTime.get("purchaseCode"))))
                    {
                        batch.put("storingTime", storingTime.get("storageTime"));
                    }
                }
            }
            // 收货仓库名称
            List<Map<String, Object>> storageList = purchaseDao.findStorageList(0);
            for (Map<String, Object> batch : batchListInfo)
            {
                for (Map<String, Object> storage : storageList)
                {
                    if (StringUtils.equals(String.valueOf(batch.get("storageId")), String.valueOf(storage.get("id"))))
                    {
                        batch.put("storageName", storage.get("name"));
                    }
                }
            }
        }
        result.put("total", purchaseDao.countTotalBatch(param));
        result.put("rows", batchListInfo);
        return result;
    }
    
    /**
     * 刷新品牌
     * 
     * @throws Exception
     */
    private void refreshBrandMap()
        throws Exception
    {
        List<BrandEntity> brandList = brandService.findBrandIsAvailable();
        if (brandList != null && brandList.size() > 0)
        {
            for (BrandEntity brandEntity : brandList)
            {
                BRANDINFO.put(String.valueOf(brandEntity.getId()), brandEntity.getName());
            }
        }
    }
    
    @Override
    public List<PurchaseOrderInfoEntity> findExportPurchaseOrder(String ids)
        throws Exception
    {
        if (StringUtils.isBlank(ids))
            throw new RuntimeException("没有指定导出那些采购单");
        List<String> list = Arrays.asList(ids.split(","));
        List<PurchaseOrderInfoEntity> result = purchaseDao.findExportPurchaseOrder(list);
        
        refreshBrandMap();
        
        if (result != null && result.size() > 0)
        {
            for (PurchaseOrderInfoEntity purchaseOrderInfoEntity : result)
            {
                List<PurchaseOrderProductEntity> orderProductList = purchaseOrderInfoEntity.getOrderProductList();
                if (orderProductList == null || orderProductList.isEmpty())
                    continue;
                for (PurchaseOrderProductEntity purchaseOrderProductEntity : orderProductList)
                {
                    purchaseOrderProductEntity.setBrandId(BRANDINFO.get(purchaseOrderProductEntity.getBrandId()));
                }
            }
        }
        
        return result;
    }
    
    public int appendPurchaseInfo(HSSFWorkbook workbook, HSSFSheet sheet, PurchaseOrderInfoEntity orderInfo, int rowCount)
        throws Exception
    {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setFontHeightInPoints((short)10);
        cellStyle.setFont(font);// 选择需要用到的字体格式
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        // 第一行
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 25 * 256);
        Row row = sheet.createRow(rowCount++);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 13));
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("采购日期");
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(orderInfo.getCreateTime().substring(0, 10));
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("供应商");
        cell = row.createCell(3, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderCompanyName());
        cell.setCellStyle(cellStyle);
        
        // 第二行
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        row = sheet.createRow(rowCount++);
        sheet.addMergedRegion(new CellRangeAddress(1, 5, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 13));
        cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("采购方信息");
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("公司名称");
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("浙江格家网络技术有限公司");
        
        // 第三行
        row = sheet.createRow(rowCount++);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 13));
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("公司地址");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue("杭州市西湖区浙商财富中心4幢307室（发票/样品 邮寄地址）");
        cell.setCellStyle(cellStyle);
        
        // 第四行
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 13));
        row = sheet.createRow(rowCount++);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("交货地址");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getStorageDetailAddress());
        cell.setCellStyle(cellStyle);
        
        // 第五行
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 4));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 13));
        row = sheet.createRow(rowCount++);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("交货联系人");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getStorageContactPerson());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5, Cell.CELL_TYPE_STRING);
        cell.setCellValue("联系方式");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getStorageContactPhone());
        cell.setCellStyle(cellStyle);
        
        // 第五行
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 4));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 8));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 10, 13));
        row = sheet.createRow(rowCount++);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("采购联系人");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getPurchasingLeader());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5, Cell.CELL_TYPE_STRING);
        cell.setCellValue("联系方式");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6, Cell.CELL_TYPE_STRING);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9, Cell.CELL_TYPE_STRING);
        cell.setCellValue("联系微信");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10, Cell.CELL_TYPE_STRING);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
        return rowCount;
    }
    
    public int appendOrderProductInfo(HSSFWorkbook workbook, HSSFSheet sheet, PurchaseOrderInfoEntity orderInfo, int rowCount)
        throws Exception
    {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        List<PurchaseOrderProductEntity> orderProductList = orderInfo.getOrderProductList();
        if (orderProductList == null || orderProductList.size() < 1)
            return rowCount;
        
        // 采购单商品条数
        int size = orderProductList.size();
        // 第一行
        Row row = sheet.createRow(rowCount);
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount + size + 2, 0, 0));
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("货物信息");
        cell.setCellStyle(cellStyle);
        String productTitle[] = {"品牌", "商品条形码", "商品名称", "规格", "单位", "数量", "单价", "税后价", "金额", "生产日期", "保质期", "售价", "毛利"};
        for (int i = 0; i < productTitle.length; i++)
        {
            cell = row.createCell(i + 1, Cell.CELL_TYPE_STRING);
            cell.setCellValue(productTitle[i]);
            cell.setCellStyle(cellStyle);
        }
        // 商品信息
        for (PurchaseOrderProductEntity orderProduct : orderProductList)
        {
            rowCount++;
            int i = 1;
            row = sheet.createRow(rowCount);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getBrandId());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getBarcode());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getName());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getSpecification());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getPurchaseUnit());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getPurchaseQuantity());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getProviderPrice());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue("");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getTotalMoney());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getManufacturerDate());
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i++, Cell.CELL_TYPE_STRING);
            cell.setCellValue(orderProduct.getDurabilityPeriod());
            cell.setCellStyle(cellStyle);
        }
        
        rowCount++;
        row = sheet.createRow(rowCount);
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 13));
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("运费");
        cell.setCellStyle(cellStyle);
        
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
        cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getFreightMoney());
        cell.setCellStyle(cellStyle2);
        rowCount++;
        
        HSSFCellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        cellStyle1.setDataFormat(dataFormat.getFormat("#,###.00"));
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setFontHeightInPoints((short)14);
        cellStyle1.setFont(font);// 选择需要用到的字体格式
        row = sheet.createRow(rowCount);
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 13));
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("合计金额");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(orderInfo.getTotalMoney());
        cell.setCellStyle(cellStyle1);
        rowCount++;
        
        return rowCount;
    }
    
    public int appendProviderInfo(HSSFWorkbook workbook, HSSFSheet sheet, PurchaseOrderInfoEntity orderInfo, int rowCount)
        throws Exception
    {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        
        // 第一行
        Row row = sheet.createRow(rowCount);
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount + 2, 0, 0));
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("供方信息");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("公司名称");
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 13));
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderCompanyName());
        cell.setCellStyle(cellStyle);
        rowCount++;
        
        // 第二行
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 4));
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 6, 13));
        row = sheet.createRow(rowCount);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("联系人");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderContactPerson());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5, Cell.CELL_TYPE_STRING);
        cell.setCellValue("联系方式");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderContactPhone());
        cell.setCellStyle(cellStyle);
        rowCount++;
        
        // 第三行
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 4));
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 6, 8));
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 10, 13));
        row = sheet.createRow(rowCount);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("收款账号");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderReceiveBankAccount());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5, Cell.CELL_TYPE_STRING);
        cell.setCellValue("开户行");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderReceiveBank());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9, Cell.CELL_TYPE_STRING);
        cell.setCellValue("收款人");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderReceiveName());
        cell.setCellStyle(cellStyle);
        rowCount++;
        
        // 第四行
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 2, 4));
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 6, 8));
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 10, 13));
        row = sheet.createRow(rowCount);
        cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("海外账号信息");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue("swift code");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getSwiftCode());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5, Cell.CELL_TYPE_STRING);
        cell.setCellValue("银行地址");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getProviderBankAddress());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9, Cell.CELL_TYPE_STRING);
        cell.setCellValue("当日汇率");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10, Cell.CELL_TYPE_STRING);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
        rowCount++;
        
        // 第四行
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 13));
        row = sheet.createRow(rowCount);
        cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("备注");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1, Cell.CELL_TYPE_STRING);
        cell.setCellValue(orderInfo.getRemark());
        //cell.setCellStyle(cellStyle);
        return rowCount;
    }
    
    public void exportOrder(File parentFile, PurchaseOrderInfoEntity orderInfo)
        throws Exception
    {
        HSSFWorkbook workbook = null;
        OutputStream fOut = null;
        try
        {
            // 产生工作簿对象
            workbook = new HSSFWorkbook();
            // 产生工作表对象
            HSSFSheet sheet = workbook.createSheet();
            // 添加供应商信息到sheet
            int rowCount = appendPurchaseInfo(workbook, sheet, orderInfo, 0);
            rowCount = appendOrderProductInfo(workbook, sheet, orderInfo, rowCount);
            appendProviderInfo(workbook, sheet, orderInfo, rowCount);
            File file =
                new File(parentFile,
                    String.format("%s%s%s%s%s", CommonUtil.date2String(new Date(), "MM.dd"), orderInfo.getProviderName(), "-", CommonUtil.generateCode(), "-采购单.xls"));
            file.createNewFile();
            fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.flush();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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
    public void importOrderProduct(MultipartFile file)
        throws Exception
    {
        HSSFWorkbook workbook = null;
        try
        {
            workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet = workbook.getSheetAt(0);
            int count = sheet.getLastRowNum() + 1 - sheet.getFirstRowNum();
            for (int i = 1; i < count; i++)
            {
                HSSFRow row = sheet.getRow(i);
                if (row == null)
                    continue;
                
                // 读取excel中的其他信息
                String id = getValue(row.getCell(0));
                if (StringUtils.isBlank(id))
                    continue;
                
                Map<String, Object> orderProduct = new HashMap<String, Object>();
                orderProduct.put("id", id);
                orderProduct.put("barcode", getValue(row.getCell(1)));
                String purchaseQuantity = getValue(row.getCell(4));
                orderProduct.put("purchaseQuantity", purchaseQuantity);
                String providerPrice = getValue(row.getCell(5));
                orderProduct.put("providerPrice", providerPrice);
                orderProduct.put("manufacturerDate", getDate(row.getCell(10)));
                orderProduct.put("durabilityPeriod", getValue(row.getCell(11)));
                if (StringUtils.isNotBlank(providerPrice) && StringUtils.isNotBlank(purchaseQuantity))
                {
                    BigDecimal providerPriceDecimal = new BigDecimal(providerPrice);
                    BigDecimal purchaseQuantityDecimal = new BigDecimal(purchaseQuantity);
                    orderProduct.put("totalPrice", providerPriceDecimal.multiply(purchaseQuantityDecimal));
                }
                purchaseDao.updateOrderProduct(orderProduct);
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
        finally
        {
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
    
    public void importNewOrderProduct(MultipartFile file, String purchaseCode, String providerId)
        throws Exception
    {
        HSSFWorkbook workbook = null;
        try
        {
            workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet = workbook.getSheetAt(0);
            int count = sheet.getLastRowNum() + 1 - sheet.getFirstRowNum();
            List<Map<String, Object>> newProviderProduct = new ArrayList<Map<String, Object>>();
            for (int i = 1; i < count; i++)
            {
                HSSFRow row = sheet.getRow(i);
                if (row == null)
                    continue;
                
                // 读取excel中的其他信息
                String barcode = getValue(row.getCell(0));
                if (StringUtils.isBlank(barcode))
                    continue;
                
                Map<String, Object> newOrderProduct = new HashMap<String, Object>();
                newOrderProduct.put("barcode", barcode);
                // 验证条形码是否有效
                List<Map<String, Object>> providerProductList = purchaseDao.findProviderProductListInfoByParam(newOrderProduct);
                if (providerProductList == null || providerProductList.isEmpty())
                {
                    throw new RuntimeException(String.format("第%s行条形码【%s】填写不对", i, barcode));
                }
                Map<String, Object> providerProduct = providerProductList.get(0);
                String providerProductId = (String)providerProduct.get("providerId");
                String[] providerProductIds = StringUtils.split(providerProductId, ",");
                boolean flag = false;
                for (String item : providerProductIds)
                {
                    if (StringUtils.equals(providerId, item))
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                {
                    throw new RuntimeException(String.format("第%s行条形码【%s】填写不对，跟供应商不匹配", i, barcode));
                }
                newOrderProduct.put("providerProductId", providerProduct.get("id"));
                newOrderProduct.put("purchaseCode", purchaseCode);
                String purchaseQuantity = getValue(row.getCell(1));
                newOrderProduct.put("purchaseQuantity", purchaseQuantity);
                String providerPrice = getValue(row.getCell(2));
                newOrderProduct.put("providerPrice", providerPrice);
                newOrderProduct.put("manufacturerDate", getDate(row.getCell(3)));
                newOrderProduct.put("durabilityPeriod", getValue(row.getCell(4)));
                if (StringUtils.isNotBlank(providerPrice) && StringUtils.isNotBlank(purchaseQuantity))
                {
                    BigDecimal providerPriceDecimal = new BigDecimal(providerPrice);
                    BigDecimal purchaseQuantityDecimal = new BigDecimal(purchaseQuantity);
                    newOrderProduct.put("totalPrice", providerPriceDecimal.multiply(purchaseQuantityDecimal));
                }
                
                // 验证对应的商品是否已经在订单中存在，
                // 存在更新
                // 不存在，将商品关联到采购单上
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("purchaseCode", purchaseCode);
                temp.put("barcode", barcode);
                List<Map<String, Object>> orderProduct = purchaseDao.findOrderProductListInfo(temp);
                if (orderProduct == null || orderProduct.isEmpty())
                {
                    newProviderProduct.add(newOrderProduct);
                }
                else
                {
                    newOrderProduct.put("id", orderProduct.get(0).get("id"));
                    purchaseDao.updateOrderProduct(newOrderProduct);
                }
            }
            // 批量写入
            if (newProviderProduct.size() > 0)
                purchaseDao.saveOrderProductForImport(newProviderProduct);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
        finally
        {
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
     * 万能处理方案<br>
     * 所有日期格式都可以通过getDataFormat()值来判断<br>
     * yyyy-MM-dd-----	14<br>
     * yyyy年m月d日---	31<br>
     * yyyy年m月-------	57<br>
     * m月d日  ----------	58<br>
     * HH:mm-----------	20<br>
     * h时mm分  -------	32<br>
     * @param cell
     * @return
     */
    private String getDate(HSSFCell cell)
    {
        short format = cell.getCellStyle().getDataFormat();
        SimpleDateFormat sdf = null;
        if (format == 14 || format == 31 || format == 57 || format == 58 || format == 185)
        {
            sdf = new SimpleDateFormat("yyyy-MM-dd");// 日期
        }
        else if (format == 20 || format == 32)
        {
            sdf = new SimpleDateFormat("HH:mm");// 时间
        }
        if (format == 0)
            return cell.getStringCellValue();
        if (sdf == null)
            sdf = new SimpleDateFormat("yyyy-MM-dd");// 日期
        double value = cell.getNumericCellValue();
        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
        return sdf.format(date);
    }
    
    @Override
    public Map<String, Object> findProviderProductInfoByBarCode(String barCode)
        throws Exception
    {
        Map<String, Object> resultMap = purchaseDao.findProviderProductByBarCode(barCode);
        List<Map<String, Object>> storageList = purchaseDao.findStorageByBarCode(barCode);
        resultMap.put("storageList", storageList);
        return resultMap;
    }
    
    @Override
    public String findProductInfoByppId(int start, int max, int id)
        throws Exception
    {
        /*Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("start", start);
        para.put("max", max);
        para.put("ppId", id);
        int productBaseTotal = productBaseDao.counProductBaseByParam(para);
        int productThirdPartyTotal = thirdPartyProductDao.countThirdPartyProduct(para);
        int total = productBaseTotal + productThirdPartyTotal;
        List<Map<String, Object>> productBaseList = productBaseDao.findProductBaseByParam(para);
        for (Map<String, Object> it : productBaseList)
        {
            int productType = Integer.parseInt(it.get("productType").toString());
            int totalStock = Integer.parseInt(it.get("totalStock").toString());
            int totalSales = Integer.parseInt(it.get("totalSales").toString());
            if (productType == 1)
            {
                it.put("channelName", "左岸城堡");
            }
            else if (productType == 2)
            {
                it.put("channelName", "左岸城堡");
            }
            else
            {
                it.put("channelName", "");
            }
            it.put("remainStock", totalStock - totalSales);
            it.put("type", 1);
        }
        
        if (productBaseList.size() < max)
        {
            para.put("start", 0);
            para.put("max", max - productBaseList.size());
            List<Map<String, Object>> productThirdPartyList = thirdPartyProductDao.findAllThirdPartyProduct(para);
            for (Map<String, Object> it : productThirdPartyList)
            {
                int providerProductId = Integer.parseInt(it.get("providerProductId").toString());
                int totalStock = Integer.parseInt(it.get("totalStock").toString());
                int totalSales = Integer.parseInt(it.get("totalSales").toString());
                Map<String, Object> ppMap = purchaseDao.findProviderProductById(providerProductId);
                if (ppMap != null)
                {
                    it.put("productName", ppMap.get("name"));
                }
                else
                {
                    it.put("productName", "");
                }
                it.put("productId", it.get("id"));
                it.put("remainStock", totalStock - totalSales);
                it.put("type", 2);
            }
            productBaseList.addAll(productThirdPartyList);
        }
        resultMap.put("total", total);
        resultMap.put("rows", productBaseList);
        return JSON.toJSONString(resultMap);*/
        return null;
    }
    
    @Override
    public Map<String, Object> findProviderProductStockInfoById(int id)
        throws Exception
    {
        Map<String, Object> result = purchaseDao.findProviderProductById(id);
        /*if (result != null && !result.isEmpty())
        {
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("ppId", id);
            List<Map<String, Object>> productBaseList = productBaseDao.findProductBaseByParam(para);
            List<Map<String, Object>> productThirdPartyList = thirdPartyProductDao.findAllThirdPartyProduct(para);
            productBaseList.addAll(productThirdPartyList);
            
            int remainStock = 0;
            for (Map<String, Object> it : productBaseList)
            {
                int totalStock = Integer.parseInt(it.get("totalStock").toString());
                int totalSales = Integer.parseInt(it.get("totalSales").toString());
                int groupCount = Integer.parseInt(it.get("groupCount").toString());
                remainStock = +(totalStock - totalSales) * groupCount;
            }
            result.put("remainStock", remainStock);
        }*/
        return result;
    }
    
    @Override
    public List<Map<String, Object>> findAllProviderProduct(Map<String, Object> para)
        throws Exception
    {
        return purchaseDao.findAllProviderProduct(para);
    }
}
