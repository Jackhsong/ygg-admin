package com.ygg.admin.dao.impl.mybatis;

import com.google.common.collect.Lists;
import com.ygg.admin.dao.ProductDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.ProductCommonEntity;
import com.ygg.admin.entity.ProductCountEntity;
import com.ygg.admin.entity.ProductEntity;
import com.ygg.admin.entity.ProductMobileDetailEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("productDao")
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao
{

    @Override
    public int save(ProductEntity product)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.save", product);
    }

    @Override
    public int saveProductCommon(ProductCommonEntity productCommon)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.saveProductCommon", productCommon);
    }

    @Override
    public int saveProductCount(ProductCountEntity productCount)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.saveProductCount", productCount);
    }

    @Override
    public int saveProductMobileDetail(ProductMobileDetailEntity mobileDetail)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.saveProductMobileDetail", mobileDetail);
    }

    @Override
    public int updateProduct(ProductEntity product)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.update", product);
    }

    @Override
    public int updateProductCommon(ProductCommonEntity productCommon)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductCommon", productCommon);
    }

    @Override
    public int updateProductCount(ProductCountEntity productCount)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductCount", productCount);
    }

    @Override
    public ProductEntity findProductByID(int id, Integer type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("ids", Lists.newArrayList(id));
        if (type != null)
            para.put("type", type);
        para.put("start", 0);
        para.put("max", 1);
        List<ProductEntity> productList = findAllProductByPara(para);
        if (productList.size() > 0)
        {
            return productList.get(0);
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<ProductEntity> batchFindProductByIDs(List<Integer> ids, Integer type, Integer max)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("ids", ids);
        if (type != null)
            para.put("type", type);
        para.put("start", 0);
        para.put("max", max);
        List<ProductEntity> productList = findAllProductByPara(para);
        return productList;
    }

    @Override
    public List<ProductEntity> findAllProductByPara(Map<String, Object> para)
        throws Exception
    {
        List<ProductEntity> reReList = getSqlSessionRead().selectList("ProductMapper.findAllProductByPara", para);
        return reReList == null ? new ArrayList<ProductEntity>() : reReList;
    }

    @Override
    public ProductCommonEntity findProductCommonByProductId(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.findProductCommonByProductId", id);
    }

    @Override
    public ProductCountEntity findProductCountByProductId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("ProductMapper.findProductCountByProductId", id);
    }

    @Override
    public List<ProductMobileDetailEntity> findProductMobileDetailByPara(Map<String, Object> para)
        throws Exception
    {
        List<ProductMobileDetailEntity> reList = getSqlSessionRead().selectList("ProductMapper.findProductMobileDetail", para);
        return reList == null ? new ArrayList<ProductMobileDetailEntity>() : reList;
    }

    @Override
    public ProductMobileDetailEntity findProductMobileDetailById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        List<ProductMobileDetailEntity> productList = findProductMobileDetailByPara(para);
        if (productList.size() > 0)
        {
            return productList.get(0);
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> findProductInfoForManage(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findProductInfoForManage", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }

    @Override
    public int countProductInfoForManage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.countProductInfoForManage", para);
    }

    @Override
    public int deleteProductMobileDetail(int id)
        throws Exception
    {
        return getSqlSession().delete("ProductMapper.deleteProductMobileDetail", id);
    }

    @Override
    public int updateProductMobileDetail(ProductMobileDetailEntity entity)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductMobileDetail", entity);
    }

    @Override
    public int forSale(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductMapper.updateIsOffShelves", para);
    }

    @Override
    public int forAvailable(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("ProductMapper.updateisAvailable", para);
    }

    @Override
    public int countStockByProductIds(List<Integer> ids)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductMapper.countStockByProductIds", ids);
    }

    @Override
    public ProductEntity findProductSaleTimeById(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("ProductMapper.findProductSaleTimeById", id);
    }

    @Override
    public int updateProductTime(ProductEntity productEntity)
        throws Exception
    {
        return this.getSqlSession().update("ProductMapper.updateProductTime", productEntity);
    }

    @Override
    public Map<String, Object> findProductAndCountInfoByProductId(int id)
        throws Exception
    {
        Map<String, Object> reMap = this.getSqlSessionRead().selectOne("ProductMapper.findProductAndCountInfoByProductId", id);
        return reMap == null ? new HashMap<String, Object>() : reMap;
    }

    @Override
    public int addProductSellNum(Map<String, Object> para)
        throws Exception
    {
        int result = getSqlSession().update("ProductMapper.addProductSellNum", para);
        if (result == 1)
        {
            getSqlSession().update("ProductMapper.addProductCommonSellNum", para);
        }
        return result;
    }

    @Override
    public int updateProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductByPara", para);
    }

    @Override
    public int updateProductCommonByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductCommonByPara", para);
    }

    @Override
    public Map<String, Object> findProductTipById(int id)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.findTipById", id);
    }

    @Override
    public int updateProductTipByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateTipById", para);
    }

    @Override
    public int updatePartnerDistributionPriceByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updatePartnerDistributionPriceByPara", para);
    }

    @Override
    public Map<String, Object> findProAndSellerInfoByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.findProAndSellerInfoByPara", para);
    }

    // -------------------------------- 商品数据统计 begin-------------------

    @Override
    public List<Map<String, Object>> findProductSalesRecord(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findProductSalesRecord", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }

    // -------------------------------- 商品数据统计 end-------------------

    @Override
    public int findMaxProductId()
        throws Exception
    {

        return getSqlSessionRead().selectOne("ProductMapper.findMaxProductId");
    }

    @Override
    public List<String> findRealSellerNameByProductIdList(List<Integer> para)
        throws Exception
    {
        Map<String, Object> map = new HashMap<>();
        map.put("idList", para);
        List<String> reList = getSqlSessionRead().selectList("ProductMapper.findRealSellerNameByProductIdList", map);
        return reList == null ? new ArrayList<String>() : reList;
    }

    @Override
    public int changeEmailRemind(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.changeEmailRemind", para);
    }

    @Override
    public List<Map<String, Object>> findAllProductSimpleByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findAllProductSimpleByPara", para);
    }

    @Override
    public List<Map<String, Object>> findAllGegeWelfareProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findAllGegeWelfareProductByPara", para);
    }

    @Override
    public int countAllGegeWelfareProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.countAllGegeWelfareProductByPara", para);
    }

    @Override
    public int addGegeWelfareProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.addGegeWelfareProduct", para);
    }

    @Override
    public int updateGegeWelfareProduct(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateGegeWelfareProduct", para);
    }

    @Override
    public int deleteGegeWelfareProductByProductId(int id)
        throws Exception
    {
        return getSqlSession().delete("ProductMapper.deleteGegeWelfareProductByProductId", id);
    }

    @Override
    public List<Map<String, Object>> findAllProductInfoForElasticsearchIndex(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findAllProductInfoForElasticsearchIndex", para);
    }

    @Override
    public List<Map<String, Object>> findAllProductCategoryInfoByProductBaseIds(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findAllProductCategoryInfoByProductBaseIds", para);
    }

    @Override
    public int updateProductCountByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductCountByPara", para);
    }

    @Override
    public int countMallProductForManage(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.countMallProductForManage", para);
    }

    @Override
    public List<Map<String, Object>> findMallProductForManage(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findMallProductForManage", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }

    @Override
    public int updateShowInMall(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateShowInMall", para);
    }

    @Override
    public int updateProductReturnDistributionProportionTypeByBrandId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductReturnDistributionProportionTypeByBrandId", para);
    }

    @Override
    public List<Map<String, Object>> findAllProductStock(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findAllProductStock", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }

    @Override
    public int countMallProductStock(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.countMallProductStock", para);
    }

    @Override
    public int countSaleProductStock(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.countSaleProductStock", para);
    }

    @Override
    public List<Map<String, Object>> findMallProductStock(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findMallProductStock", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }

    @Override
    public List<Map<String, Object>> findSaleProductStock(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findSaleProductStock", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }

    @Override
    public List<Map<String, Object>> findProductBrandsByCodeList(List<String> codeList)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findProductBrandsByCodeList", codeList);
    }

    @Override
    public List<Integer> findProductIdByNameAndRemark(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findProductIdByNameAndRemark", para);
    }

    @Override
    public ProductCountEntity findProductCountByProductIdForUpdate(Integer productId)
        throws Exception
    {
        return getSqlSession().selectOne("ProductMapper.findProductCountByProductIdForUpdate", productId);
    }

    @Override
    public int updateProductStock(Integer productId, int oldStock, int changeStock)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("productId", productId);
        para.put("oldStock", oldStock);
        para.put("changeStock", changeStock);
        return getSqlSession().update("ProductMapper.updateProductStock", para);
    }

    @Override
    public int updateProductRemark(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductRemark", para);
    }

    @Override
    public int deleteProductNewbie(List<String> idList)
        throws Exception
    {
        return getSqlSession().delete("ProductMapper.deleteProductNewbie", idList);
    }

    @Override
    public int countProductNewbie(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("ProductMapper.countProductNewbie", para);
    }

    @Override
    public List<Map<String, Object>> findAllProductNewbie(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findAllProductNewbie", para);
    }

    @Override
    public int insertProductNewbie(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.insertProductNewbie", para);
    }

    @Override
    public int updateProductNewbie(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductNewbie", para);
    }

    @Override
    public List<ProductEntity> findProductByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("ProductMapper.findProductByPara", para);
    }

    @Override
    public List<Map<String, Object>> findQqbsProductSalesRecord(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findQqbsProductSalesRecord", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    
    @Override
    public List<Map<String, Object>> findYWProductSalesRecord(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("ProductMapper.findYWProductSalesRecord", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }

    @Override
    public List<Integer> findProductIdsByPid(int productBaseId)
    {
        return getSqlSessionRead().selectList("ProductMapper.findProductIdsByPid", productBaseId);
    }

    @Override
    public int deleteProductMobileDetailByProductId(Integer productId)
        throws Exception
    {
        return getSqlSession().delete("ProductMapper.deleteProductMobileDetailByProductId", productId);
    }

    @Override
    public int insertProductMobileDetail(List<ProductMobileDetailEntity> list)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.insertProductMobileDetail", list);
    }

    @Override
    public List<Map<String, Object>> getSaleVolumeInfoByPayTime(Map<String, Object> para) {
        return getSqlSessionRead().selectList("ProductMapper.getSaleVolumeInfoByPayTime", para);
    }

    @Override
    public int saveProductSaleVolume(Map<String, Object> data) {
        return getSqlSession().insert("ProductMapper.saveProductSaleVolume", data);
    }

    @Override
    public int updateProductSaleVolume(Map<String, Object> data) {
        return getSqlSession().update("ProductMapper.updateProductSaleVolume", data);
    }

    @Override
    public List<Map<String, Object>> selectSaleVolumeInfoByDays(Map<String, Object> para) {
        return getSqlSessionRead().selectList("ProductMapper.selectSaleVolumeInfoByDays", para);
    }

    @Override
    public int countSaleVolumeByDayHours(Map<String, Object> para) {
        return getSqlSessionRead().selectOne("ProductMapper.countSaleVolumeByDayHours", para);
    }

    @Override
    public int updateProductActivityWholesalePrice(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().update("ProductMapper.updateProductActivityWholesalePrice", param);
    }
    
    @Override
    public int inserProductActivityWholesalePriceLog(Map<String, Object> param)
        throws Exception
    {
        return getSqlSession().insert("ProductMapper.inserProductActivityWholesalePriceLog", param);
    }
}
