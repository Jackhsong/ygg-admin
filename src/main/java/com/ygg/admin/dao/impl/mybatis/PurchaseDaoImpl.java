package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.PurchaseDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ProviderEntity;
import com.ygg.admin.entity.PurchaseOrderInfoEntity;

@Repository("purchaseDao")
public class PurchaseDaoImpl extends BaseDaoImpl implements PurchaseDao
{
    
    @Override
    public int saveStorage(String name, String detailAddress, String contactPerson, String contactPhone, String sellerId, short type)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", name);
        param.put("sellerId", sellerId);
        param.put("detailAddress", detailAddress);
        param.put("contactPerson", contactPerson);
        param.put("contactPhone", contactPhone);
        param.put("type", type);
        return getSqlSessionAdmin().insert("PurchaseMapper.saveStorage", param);
    }
    
    @Override
    public Map<String, Object> findStorageByParam(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = getSqlSessionAdmin().selectOne("PurchaseMapper.findStorageByParam", param);
        return result == null ? new HashMap<String, Object>() : result;
    }
    
    @Override
    public int countStorageByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countStorageByParam", param);
    }
    
    @Override
    public int updateStorage(int id, String name, String detailAddress, String contactPerson, String contactPhone, String sellerId, short type)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("name", name);
        param.put("sellerId", sellerId);
        param.put("detailAddress", detailAddress);
        param.put("contactPerson", contactPerson);
        param.put("contactPhone", contactPhone);
        param.put("type", type);
        return getSqlSessionAdmin().update("PurchaseMapper.updateStorage", param);
    }
    
    @Override
    public List<Map<String, Object>> findStorageListInfo(int page, int rows, int type)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("type", type);
        List<Map<String, Object>> result = getSqlSessionAdmin().selectList("PurchaseMapper.findStorageByParam", param);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findStorageList(int type)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", type);
        return getSqlSessionAdmin().selectList("PurchaseMapper.findStorageList", param);
    }
    
    @Override
    public int saveProvider(ProviderEntity entity)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.saveProvider", entity);
    }
    
    @Override
    public int updateProvider(ProviderEntity entity)
        throws Exception
    {
        return getSqlSessionAdmin().update("PurchaseMapper.updateProvider", entity);
    }
    
    @Override
    public ProviderEntity findProviderByParam(Map<String, Object> param)
        throws Exception
    {
        ProviderEntity result = getSqlSessionAdmin().selectOne("PurchaseMapper.findProviderByParam", param);
        return result == null ? new ProviderEntity() : result;
    }
    
    @Override
    public List<ProviderEntity> findProviderListInfoByParam(Map<String, Object> param)
        throws Exception
    {
        List<ProviderEntity> result = getSqlSessionAdmin().selectList("PurchaseMapper.findProviderByParam", param);
        return result == null ? new ArrayList<ProviderEntity>() : result;
    }
    
    public int countTotalProviderByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalProviderByParam", param);
    }
    
    @Override
    public int countProviderByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalProviderByParam", param);
    }
    
    @Override
    public List<Map<String, Object>> findProviderList(int type)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type", type);
        List<Map<String, Object>> result = getSqlSessionAdmin().selectList("PurchaseMapper.findProviderList", param);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findProviderProductListInfoByParam(Map<String, Object> param)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionAdmin().selectList("PurchaseMapper.findProviderProductByParam", param);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    public int countTotalProviderProductByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalProviderProductByParam", param);
    }
    
    @Override
    public Map<String, Object> findProviderProductById(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        Map<String, Object> result = getSqlSessionAdmin().selectOne("PurchaseMapper.findProviderProductByParam", param);
        return result == null ? new HashMap<String, Object>() : result;
    }
    
    @Override
    //public int saveProviderProduct(List<Map<String, Object>> list) throws Exception {
    public int saveProviderProduct(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.saveProviderProduct", param);
    }
    
    @Override
    public int updateProviderProduct(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().update("PurchaseMapper.updateProviderProduct", param);
    }
    
    @Override
    public List<Map<String, Object>> findOrderListInfoByParam(Map<String, Object> param)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionAdmin().selectList("PurchaseMapper.findOrderListInfoByParam", param);
        return result;
    }
    
    @Override
    public int deleteOrderById(int orderId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("PurchaseMapper.deleteOrderById", orderId);
    }
    
    @Override
    public int deleteOrderProductByOrderId(int orderId)
        throws Exception
    {
        return getSqlSessionAdmin().delete("PurchaseMapper.deleteOrderProductByOrderId", orderId);
    }
    
    @Override
    public int savePurchaseOrder(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.savePurchaseOrder", param);
    }
    
    @Override
    public int updatePurchaseOrder(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().update("PurchaseMapper.updatePurchaseOrder", param);
    }
    
    @Override
    public Map<String, Object> findOrderByPurchaseCode(String purchaseCode)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("purchaseCode", purchaseCode);
        Map<String, Object> result = getSqlSessionAdmin().selectOne("PurchaseMapper.findOrderListInfoByParam", param);
        return result == null ? new HashMap<String, Object>() : result;
    }
    
    @Override
    public int countTotalOrder(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalOrder", param);
    }
    
    @Override
    public List<Map<String, Object>> findOrderProductListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseMapper.findOrderProductListInfo", param);
    }
    
    @Override
    public List<Map<String, Object>> findProviderProduct4OrderListInfo(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode,
        String productName)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("providerId", providerId);
        param.put("storageId", storageId);
        param.put("purchaseCode", purchaseCode);
        param.put("brandId", brandId);
        param.put("productId", productId);
        param.put("barcode", barcode);
        param.put("productName", productName);
        return getSqlSessionAdmin().selectList("PurchaseMapper.findProviderProduct4OrderListInfo", param);
    }
    
    public int countTotalProviderProduct4OrderList(int providerId, int storageId, String purchaseCode, String brandId, String productId, String barcode, String productName)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("providerId", providerId);
        param.put("storageId", storageId);
        param.put("purchaseCode", purchaseCode);
        param.put("brandId", brandId);
        param.put("productId", productId);
        param.put("barcode", barcode);
        param.put("productName", productName);
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalProviderProduct4OrderList", param);
    }
    
    @Override
    public int saveOrderProduct(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.saveOrderProduct", param);
    }
    
    @Override
    public int saveOrderProductForImport(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.saveOrderProductForImport", list);
    }
    
    @Override
    public int updateOrderProduct(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().update("PurchaseMapper.updateOrderProduct", param);
    }
    
    @Override
    public int removeOrderProduct(List<Integer> list)
        throws Exception
    {
        return getSqlSessionAdmin().delete("PurchaseMapper.removeOrderProduct", list);
    }
    
    @Override
    public int findPurchaseCode(String day)
        throws Exception
    {
        Object o = getSqlSessionAdmin().selectOne("PurchaseMapper.findPurchaseCode", day);
        if (o == null)
            return 0;
        else
            return (int)o;
    }
    
    @Override
    public int updatePurchaseCode(String day, int code)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("day", day);
        param.put("code", code);
        return getSqlSessionAdmin().update("PurchaseMapper.updatePurchaseCode", param);
    }
    
    @Override
    public int savePurchaseCode(String day)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.savePurchaseCode", day);
    }
    
    @Override
    public Map<String, Object> sumPurchasePayDetail(String purchaseCode)
        throws Exception
    {
        Map<String, Object> result = getSqlSessionAdmin().selectOne("PurchaseMapper.sumPurchasePayDetail", purchaseCode);
        return result == null ? new HashMap<String, Object>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findPurchasePayDetailByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseMapper.findPurchasePayDetailByParam", param);
    }
    
    @Override
    public int countTotalPurchasePayDetail(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalPurchasePayDetail", param);
    }
    
    @Override
    public int deletePurchasePayDetail(int id)
        throws Exception
    {
        return getSqlSessionAdmin().delete("PurchaseMapper.deletePurchasePayDetail", id);
    }
    
    @Override
    public int savePurchasePayDetail(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.savePurchasePayDetail", param);
    }
    
    @Override
    public int updatePurchasePayDetail(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().update("PurchaseMapper.updatePurchasePayDetail", param);
    }
    
    @Override
    public List<Map<String, Object>> findPurchaseProductStoring(String purchaseCode)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("purchaseCode", purchaseCode);
        return getSqlSessionAdmin().selectList("PurchaseMapper.findPurchaseProductStoring", param);
    }
    
    @Override
    public int savePurchaseProductStoring(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.savePurchaseProductStoring", list);
    }
    
    @Override
    public List<Map<String, Object>> findBatchListInfo(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseMapper.findBatchListInfo", param);
    }
    
    @Override
    public int countTotalBatch(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.countTotalBatch", param);
    }
    
    @Override
    public List<Map<String, Object>> findBatchStoringTime(String providerProductId, List<Object> list)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("providerProductId", providerProductId);
        param.put("list", list);
        return getSqlSessionAdmin().selectList("PurchaseMapper.findBatchStoringTime", param);
    }
    
    @Override
    public List<PurchaseOrderInfoEntity> findExportPurchaseOrder(List<String> list)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseMapper.findExportPurchaseOrder", list);
    }
    
    @Override
    public List<Map<String, Object>> findRelationProviderStorageProviderProductByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseMapper.findRelationProviderStorageProviderProductByParam", param);
    }
    
    @Override
    public int saveRelationProviderStorageProviderProduct(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSessionAdmin().insert("PurchaseMapper.saveRelationProviderStorageProviderProduct", list);
    }
    
    @Override
    public int deleteRelationProviderStorageProviderProduct(String barcode)
        throws Exception
    {
        return getSqlSessionAdmin().delete("PurchaseMapper.deleteRelationProviderStorageProviderProduct", barcode);
    }
    
    @Override
    public int sumStoringCountByParam(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("PurchaseMapper.sumStoringCountByParam", param);
    }
    
    @Override
    public Map<String, Object> findProviderProductByBarCode(String productCode)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("barcode", productCode);
        List<Map<String, Object>> result = getSqlSessionAdmin().selectList("PurchaseMapper.findProviderProductByParam", param);
        return result.isEmpty() ? new HashMap<String, Object>() : result.get(0);
    }
    
    @Override
    public List<Map<String, Object>> findAllProviderProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionAdmin().selectList("PurchaseMapper.findAllProviderProduct", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findStorageByBarCode(String barCode)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("PurchaseMapper.findStorageByBarCode", barCode);
    }
}
