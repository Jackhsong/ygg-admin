package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.ProductBaseDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ProductBaseEntity;
import com.ygg.admin.entity.ProductBaseMobileDetailEntity;
import com.ygg.admin.entity.RelationProductBaseDeliverAreaEntity;

@Repository("productBaseDao")
public class ProductBaseDaoImpl extends BaseDaoImpl implements ProductBaseDao
{
    
    @Override
    public List<Map<String, Object>> queryAllProductBaseInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSession().selectList("ProductBaseMapper.queryAllProductBaseInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countProductBaseInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("ProductBaseMapper.countProductBaseInfo", para);
    }
    
    @Override
    public int saveProductBase(ProductBaseEntity product)
        throws Exception
    {
        return getSqlSession().insert("ProductBaseMapper.saveProductBase", product);
    }
    
    @Override
    public int saveProductMobileDetail(ProductBaseMobileDetailEntity entity)
        throws Exception
    {
        return getSqlSession().insert("ProductBaseMapper.saveProductMobileDetail", entity);
        
    }

    @Override
    public List<ProductBaseMobileDetailEntity> findProductBaseMobileDetailsByProductBaseId(int productBaseId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productBaseId);
        return getSqlSession().selectList("ProductBaseMapper.findProductBaseMobileDetailsByPara", params);
    }

    @Override
    public ProductBaseEntity queryProductBaseById(int editId)
        throws Exception
    {
        return getSqlSession().selectOne("ProductBaseMapper.queryProductBaseById", editId);
    }
    
    @Override
    public int updateProductBase(ProductBaseEntity product)
        throws Exception
    {
        return getSqlSession().update("ProductBaseMapper.updateProductBase", product);
    }
    
    @Override
    public int updateProductBaseCost(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductBaseMapper.updateProductBaseCost", para);
    }
    
    @Override
    public int deleteProductBaseMobileDetail(int id)
        throws Exception
    {
        return getSqlSession().delete("ProductBaseMapper.deleteProductBaseMobileDetail", id);
    }
    
    @Override
    public ProductBaseMobileDetailEntity findProductBaseMobileDetailById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        List<ProductBaseMobileDetailEntity> list = getSqlSession().selectList("ProductBaseMapper.findProductBaseMobileDetailsByPara", para);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    @Override
    public int updateProducBasetMobileDetail(ProductBaseMobileDetailEntity entity)
        throws Exception
    {
        return getSqlSession().update("ProductBaseMapper.updateProducBasetMobileDetail", entity);
    }
    
    @Override
    public List<ProductBaseEntity> queryAllProductBase(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("ProductBaseMapper.queryAllProductBase", para);
    }
    
    @Override
    public int forAvailable(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductBaseMapper.updateisAvailable", para);
    }
    
    @Override
    public List<Map<String, Object>> querySaleProductInfoByBaseId(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = this.getSqlSession().selectList("ProductBaseMapper.querySaleProductInfoByBaseId", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int adjustSaleStock(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductBaseMapper.adjustSalestock", para);
    }
    
    @Override
    public int addTotalStock(Map<String, Object> map)
        throws Exception
    {
        return this.getSqlSession().update("ProductBaseMapper.addTotalStock", map);
    }
    
    @Override
    public int findMaxProductId()
        throws Exception
    {
        
        return getSqlSession().selectOne("ProductBaseMapper.findMaxProductId");
    }
    
    @Override
    public List<Integer> checkCodeAndBarCode(Map<String, Object> para)
        throws Exception
    {
        List<Integer> result = getSqlSession().selectList("ProductBaseMapper.checkCodeAndBarCode", para);
        return result == null ? new ArrayList<Integer>() : result;
    }
    
    @Override
    public int countSaleProductInfoByBaseId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("ProductBaseMapper.countSaleProductInfoByBaseId", para);
    }
    
    @Override
    public int updateRelationProductByPara(Map<String, Object> map)
        throws Exception
    {
        return this.getSqlSession().update("ProductBaseMapper.updateRelationProductByPara", map);
    }
    
    @Override
    public int checkIsInUse(int id)
        throws Exception
    {
        Integer result = this.getSqlSession().selectOne("ProductBaseMapper.checkIsInUse", id);
        return result == null ? 0 : result;
    }
    
    @Override
    public int deleteProductBaseMobileDetailIdInList(Map<String, Object> paraMap)
        throws Exception
    {
        return this.getSqlSession().delete("ProductBaseMapper.deleteProductBaseMobileDetailIdInList", paraMap);
    }
    
    @Override
    public int adjustMallStock(Map<String, Object> paraMap)
        throws Exception
    {
        return this.getSqlSession().update("ProductBaseMapper.adjustMallStock", paraMap);
    }
    
    @Override
    public List<Integer> findProductIdByProductBaseId(int id, Integer type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        if (type != null)
        {
            para.put("type", type);
        }
        return this.getSqlSessionRead().selectList("ProductBaseMapper.findProductIdByProductBaseId", para);
    }
    
    @Override
    public List<RelationProductBaseDeliverAreaEntity> findRelationSellerDeliverAreaByProductBaseId(int id)
        throws Exception
    {
        List<RelationProductBaseDeliverAreaEntity> reList = this.getSqlSessionRead().selectList("ProductBaseMapper.findRelationSellerDeliverAreaByProductBaseId", id);
        return reList == null ? new ArrayList<RelationProductBaseDeliverAreaEntity>() : reList;
    }

    @Override
    public int insertRelationProductBaseDeliverArea(List<RelationProductBaseDeliverAreaEntity> list)
        throws Exception
    {
        return this.getSqlSession().insert("ProductBaseMapper.insertRelationProductBaseDeliverArea", list);
    }
    
    @Override
    public int deleteRelationProductBaseDeliverArea(List<Integer> productIdList)
        throws Exception
    {
        return this.getSqlSession().delete("ProductBaseMapper.deleteRelationProductBaseDeliverArea", productIdList);
    }
    
    @Override
    public int countQualityPromise(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductBaseMapper.countQualityPromise", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllQualityPromise(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ProductBaseMapper.findAllQualityPromise", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public boolean IsExistQualityPromise(Map<String, Object> para)
        throws Exception
    {
        // 同一类型只允许存在一个，存在多个则重复
        List<Map<String, Object>> reList = findAllQualityPromise(para);
        int oldId = Integer.valueOf(para.get("id") + "");
        if (reList == null || reList.size() == 0)
        {
            return false;
        }
        else if (reList.size() > 1)
        {
            return true;
        }
        else
        {
            int id = Integer.valueOf(reList.get(0).get("id") + "");
            if (oldId == id)
                return false;
            return true;
        }
    }
    
    @Override
    public int insertQualityPromise(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("ProductBaseMapper.insertQualityPromise", para);
    }
    
    @Override
    public int updateQualityPromise(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductBaseMapper.updateQualityPromise", para);
    }
    
    @Override
    public int countAllExpireProduct(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductBaseMapper.countAllExpireProduct", para);
        
    }
    
    @Override
    public List<Map<String, Object>> findAllExpireProduct(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("ProductBaseMapper.findAllExpireProduct", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int batchUpdateProductBaseDeliverAreaType(byte type, List<Integer> productBaseIdList)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("type", type);
        para.put("idList", productBaseIdList);
        return getSqlSession().update("ProductBaseMapper.batchUpdateProductBaseDeliverAreaType", para);
    }
    
    @Override
    public List<Integer> findProductBaseIdBySellerTemplateId(int id)
        throws Exception
    {
        List<Integer> reList = getSqlSession().selectList("ProductBaseMapper.findProductBaseIdBySellerTemplateId", id);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public int findAllottedStockById(int productBaseId)
        throws Exception
    {
        Integer sum = getSqlSessionRead().selectOne("ProductBaseMapper.findAllottedStockById", productBaseId);
        return sum == null ? 0 : sum.intValue();
    }
    
    @Override
    public List<Map<String, Object>> findProductBaseRelationInfoByProductBaseId(List<Integer> productBaseIds)
    {
        return this.getSqlSessionRead().selectList("ProductBaseMapper.findProductBaseRelationInfoByProductBaseId", productBaseIds);
    }
    
    @Override
    public ProductBaseEntity findProductBaseByIdForUpdate(int productBaseId)
        throws Exception
    {
        return getSqlSession().selectOne("ProductBaseMapper.findProductBaseByIdForUpdate", productBaseId);
    }
    
    @Override
    public int updateProductBaseStock(int productBaseId, int change)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", productBaseId);
        para.put("change", change);
        return getSqlSession().update("ProductBaseMapper.updateProductBaseStock", para);
    }
    
    @Override
    public int insertWholesalePriceUpdatelog(Map<String, Object> params)
        throws Exception
    {
        return getSqlSession().insert("ProductBaseMapper.insertWholesalePriceUpdatelog", params);
    }
    
    @Override
    public int countWholeSalePriceLogByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductBaseMapper.countWholeSalePriceLogByPara", para);
    }
    
    @Override
    public List<Map<String, Object>> findWholeSalePriceLogByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductBaseMapper.findWholeSalePriceLogByPara", para);
    }
    
    @Override
    public int updateProductCommonImage(int id, String mediumImage, String smallImage)
        throws Exception
    {
        Map<String, Object> params = new HashMap<>();
        params.put("productBaseId", id);
        params.put("mediumImage", mediumImage);
        params.put("smallImage", smallImage);
        return getSqlSession().update("ProductBaseMapper.updateProductCommonImage", params);
    }
    
    @Override
    public List<Map<String, Object>> findProductBaseIdBySellerProductId(List<Object> list)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductBaseMapper.findProductBaseIdBySellerProductId", list);
    }

    @Override
    public List<Map<String, Object>> findProductBaseSalesVolumeByPara(Map<String, Object> params) throws Exception {
        return getSqlSessionRead().selectList("ProductBaseMapper.findProductBaseSalesVolumeByPara", params);
    }

    @Override
    public List<Map<String, Object>> findProductBaseHistorySalesVolume(Map<String,Object> para) throws Exception {
        return getSqlSessionRead().selectList("ProductBaseMapper.findProductBaseHistorySalesVolume", para);
    }
}
