package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.SellerDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("sellerDao")
public class SellerDaoImpl extends BaseDaoImpl implements SellerDao
{
    
    @Override
    public int save(SellerEntity seller)
        throws Exception
    {
        return this.getSqlSession().insert("SellerMapper.save", seller);
    }
    
    @Override
    public List<SellerEntity> findAllSellerByPara(Map<String, Object> para)
        throws Exception
    {
        List<SellerEntity> sellerList = this.getSqlSession().selectList("SellerMapper.findAllSellerByPara", para);
        return sellerList == null ? new ArrayList<SellerEntity>() : sellerList;
    }
    
    @Override
    public List<SellerEntity> findAllSellerSimpleByPara(Map<String, Object> para)
        throws Exception
    {
        List<SellerEntity> sellerList = this.getSqlSession().selectList("SellerMapper.findAllSellerSimpleByPara", para);
        return sellerList == null ? new ArrayList<SellerEntity>() : sellerList;
    }
    
    @Override
    public SellerEntity findSellerById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<SellerEntity> sellerList = findAllSellerByPara(para);
        if (sellerList != null && sellerList.size() > 0)
        {
            return sellerList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public SellerEntity findSellerSimpleById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<SellerEntity> sellerList = findAllSellerSimpleByPara(para);
        if (sellerList != null && sellerList.size() > 0)
        {
            return sellerList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int updateSellerByPara(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("SellerMapper.updateSellerByPara", para);
    }
    
    @Override
    public int countSellerByPara(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("SellerMapper.countSellerByPara", para);
    }
    
    @Override
    public int countSellerBySellerName(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().selectOne("SellerMapper.countSellerBySellerName", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllSellerSaleInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("SellerMapper.findAllSellerSaleInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countAllSellerSaleInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerMapper.countAllSellerSaleInfo", para);
    }
    
    @Override
    public List<Integer> findSellerIdByProductIdList(Map<String, Object> para)
        throws Exception
    {
        List<Integer> sellerIdList = getSqlSession().selectList("SellerMapper.findSellerIdByProductIdList", para);
        return sellerIdList == null ? new ArrayList<Integer>() : sellerIdList;
    }
    
    @Override
    public SellerEntity findSellerByProductId(int displayId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerMapper.findSellerByProductId", displayId);
    }
    
    @Override
    public SellerEntity findAllSellerByActivityCommonId(int displayId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerMapper.findAllSellerByActivityCommonId", displayId);
    }
    
    @Override
    public List<Map<String, Object>> findSellerInfoBySellerIdList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("SellerMapper.findSellerInfoBySellerIdList", para);
    }
    
    @Override
    public int insertRelationSellerDeliverArea(RelationSellerDeliverAreaEntity rsdae)
        throws Exception
    {
        return this.getSqlSession().insert("SellerMapper.insertRelationSellerDeliverArea", rsdae);
    }
    
    @Override
    public int updateRelationSellerDeliverArea(RelationSellerDeliverAreaEntity rsdae)
        throws Exception
    {
        return this.getSqlSession().update("SellerMapper.updateRelationSellerDeliverArea", rsdae);
    }
    
    @Override
    public int deleteRelationSellerDeliverArea(Map<String, Object> mp)
        throws Exception
    {
        return this.getSqlSession().delete("SellerMapper.deleteRelationSellerDeliverArea", mp);
    }
    
    @Override
    public List<RelationSellerDeliverAreaEntity> findRelationSellerDeliverAreaBySellerId(int id)
        throws Exception
    {
        List<RelationSellerDeliverAreaEntity> reList = this.getSqlSessionRead().selectList("SellerMapper.findRelationSellerDeliverAreaBySellerId", id);
        return reList == null ? new ArrayList<RelationSellerDeliverAreaEntity>() : reList;
    }
    
    @Override
    public List<SellerFinanceSettlementPictureEntity> findSellerFinancePictureBysid(int id)
        throws Exception
    {
        List<SellerFinanceSettlementPictureEntity> reList = this.getSqlSessionRead().selectList("SellerMapper.findSellerFinancePictureBysid", id);
        return reList == null ? new ArrayList<SellerFinanceSettlementPictureEntity>() : reList;
    }
    
    @Override
    public int saveSellerFinancePicture(SellerFinanceSettlementPictureEntity sfspe)
        throws Exception
    {
        return this.getSqlSession().insert("SellerMapper.saveSellerFinancePicture", sfspe);
    }
    
    @Override
    public int deleteSellerFinancePicture(Map<String, Object> it)
        throws Exception
    {
        return this.getSqlSession().delete("SellerMapper.deleteSellerFinancePicture", it);
    }
    
    @Override
    public int updateSellerFinancePicture(SellerFinanceSettlementPictureEntity sfspe)
        throws Exception
    {
        return this.getSqlSession().update("SellerMapper.updateSellerFinancePicture", sfspe);
    }
    
    @Override
    public List<Map<String, Object>> findSellerRealNameByIds(List<Integer> idList)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("SellerMapper.findSellerRealNameByIds", idList);
    }
    
    @Override
    public int saveSellerOnlineStoreAddress(List<SellerOnlineStoreAddressEntity> shopList)
        throws Exception
    {
        return this.getSqlSession().insert("SellerMapper.saveSellerOnlineStoreAddress", shopList);
    }
    
    @Override
    public int updateSellerOnlineStoreAddress(SellerOnlineStoreAddressEntity sosae)
        throws Exception
    {
        return this.getSqlSession().update("SellerMapper.updateSellerOnlineStoreAddress", sosae);
    }
    
    @Override
    public int deleteSellerOnlineStoreAddress(int sellerId)
        throws Exception
    {
        return this.getSqlSession().delete("SellerMapper.deleteSellerOnlineStoreAddress", sellerId);
    }
    
    @Override
    public List<SellerOnlineStoreAddressEntity> findSellerOnlineStoreBysid(int id)
        throws Exception
    {
        List<SellerOnlineStoreAddressEntity> reList = this.getSqlSessionRead().selectList("SellerMapper.findSellerOnlineStoreBysid", id);
        return reList == null ? new ArrayList<SellerOnlineStoreAddressEntity>() : reList;
    }
    
    @Override
    public List<BrandEntity> findSellerBrandBysid(int id)
        throws Exception
    {
        List<BrandEntity> reList = getSqlSessionRead().selectList("SellerMapper.findSellerBrandBysid", id);
        return reList == null ? new ArrayList<BrandEntity>() : reList;
    }
    
    @Override
    public int deleteSellerBrand(int selelrId)
        throws Exception
    {
        return getSqlSession().delete("SellerMapper.deleteSellerBrand", selelrId);
    }
    
    @Override
    public int saveSellerBrand(List<Map<String, Object>> brandList)
        throws Exception
    {
        return getSqlSession().insert("SellerMapper.saveSellerBrand", brandList);
    }
    
    @Override
    public int saveSellerMaster(SellerMasterEntity sellerMaster)
        throws Exception
    {
        return getSqlSession().insert("SellerMapper.saveSellerMaster", sellerMaster);
    }
    
    @Override
    public int updateSellerMaster(SellerMasterEntity sellerMaster)
        throws Exception
    {
        return getSqlSession().update("SellerMapper.updateSellerMaster", sellerMaster);
    }
    
    @Override
    public SellerEntity findSellerByRealSellerName(String realSellerName)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerMapper.findSellerByRealSellerName", realSellerName);
    }
    
    @Override
    public SellerMasterAndSlaveEntity findSellerSlaveBySlaveId(int sellerId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerMapper.findSellerSlaveBySlaveId", sellerId);
    }
    
    @Override
    public List<Map<String, Object>> finAllSlaveSeller(String masterId)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("SellerMapper.finAllSlaveSeller", masterId);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<SellerMasterAndSlaveEntity> findSellerSlaveListByMasterId(int sellerId)
        throws Exception
    {
        List<SellerMasterAndSlaveEntity> reList = getSqlSessionRead().selectList("SellerMapper.findSellerSlaveListByMasterId", sellerId);
        return reList == null ? new ArrayList<SellerMasterAndSlaveEntity>() : reList;
    }
    
    @Override
    public int saveSellerMasterAndSlave(SellerMasterAndSlaveEntity slave)
        throws Exception
    {
        return getSqlSession().insert("SellerMapper.saveSellerMasterAndSlave", slave);
    }
    
    @Override
    public int deleteSellerMasterAndSlave(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().delete("SellerMapper.deleteSellerMasterAndSlave", para);
    }
    
    @Override
    public int synchronousSellerInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("SellerMapper.synchronousSellerInfo", para);
    }
    
    @Override
    public List<SellerEntity> findMasterSellerByPara(Map<String, Object> para)
        throws Exception
    {
        List<SellerEntity> reList = getSqlSessionRead().selectList("SellerMapper.findMasterSellerByPara", para);
        return reList == null ? new ArrayList<SellerEntity>() : reList;
    }
    
    @Override
    public int countMergeSeller(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("SellerMapper.countMergeSeller", para);
    }
    
    @Override
    public List<Integer> findIdListBySellerType(int sellerType)
        throws Exception
    {
        return getSqlSessionRead().selectList("SellerMapper.findIdListBySellerType", sellerType);
    }
    
    @Override
    public int updateSellerOwnerStatus(List<String> slaveIdList, int status)
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("idList", slaveIdList);
        para.put("status", status);
        return getSqlSession().update("SellerMapper.updateSellerOwnerStatus", para);
    }

    @Override
    public List<Integer> findEdbSellerIdList()
        throws Exception
    {
        return getSqlSession().selectList("SellerMapper.findEdbSellerIdList");
    }

    @Override
    public List<Integer> findSellerIdWhereIsOwnerEqualsOne()
        throws Exception
    {
        return getSqlSession().selectList("SellerMapper.findSellerIdWhereIsOwnerEqualsOne");
    }

    @Override
    public List<Map<String, Object>> findSellerCategoryByRelation(int id) {
        return getSqlSessionRead().selectList("SellerMapper.findSellerCategoryByRelation", id);
    }

    @Override
    public List<Map<String, Object>> findSellerCategoryByBaseProduct(int id) {
        return getSqlSessionRead().selectList("SellerMapper.findSellerCategoryByBaseProduct", id);
    }

    @Override
    public int saveSellerCategoryRelation(Map<String, Object> para) {
        return getSqlSession().insert("SellerMapper.saveSellerCategoryRelation", para);
    }

    @Override
    public int deleteAllSellerCategoryBySellerId(int sellerId) {
        return getSqlSession().delete("SellerMapper.deleteAllSellerCategoryBySellerId", sellerId);
    }
}
