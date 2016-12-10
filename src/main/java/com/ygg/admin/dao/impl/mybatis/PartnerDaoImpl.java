package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.PartnerDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.AccountRecommendRelationEntity;

@Repository("partnerDao")
public class PartnerDaoImpl extends BaseDaoImpl implements PartnerDao
{
    
    @Override
    public List<Map<String, Object>> findJsonPartnerInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findJsonPartnerInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countPartnerInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.countPartnerInfo", para);
    }
    
    @Override
    public int updatePartnerStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.updatePartnerStatus", para);
    }
    
    @Override
    public int insertPartnerApply(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("PartnerMapper.insertPartnerApply", para);
    }
    
    @Override
    public int countPartnerApplyInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.countPartnerApplyInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findJsonPartnerApplyInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findJsonPartnerApplyInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countInviteRelationInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.countInviteRelationInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findJsonInviteRelationInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findJsonInviteRelationInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countJsonReturnIntegralInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.countJsonReturnIntegralInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findJsonReturnIntegralInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findJsonReturnIntegralInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int countJsonExchangeIntegralInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.countJsonExchangeIntegralInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findJsonExchangeIntegralInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findJsonExchangeIntegralInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int dealWithExchange(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.dealWithExchange", para);
    }
    
    @Override
    public int countJsonOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.countJsonOrderDetailInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findJsonOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findJsonOrderDetailInfo", para);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findRecommendFriendsByAccountId(int accountId)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findRecommendFriendsByAccountId", accountId);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public List<Map<String, Object>> findInvitedFriendsByAccountId(int accountId)
        throws Exception
    {
        List<Map<String, Object>> result = getSqlSessionRead().selectList("PartnerMapper.findInvitedFriendsByAccountId", accountId);
        return result == null ? new ArrayList<Map<String, Object>>() : result;
    }
    
    @Override
    public int insertAccountPartnerRelation(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("PartnerMapper.insertAccountPartnerRelation", para);
    }
    
    @Override
    public int passPartnerApply(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.passPartnerApply", para);
    }
    
    @Override
    public int refusePartnerApply(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.refusePartnerApply", para);
    }
    
    @Override
    public int deleteFromAccountPartnerRelation(Map<String, Object> map)
        throws Exception
    {
        return getSqlSession().delete("PartnerMapper.deleteFromAccountPartnerRelation", map);
    }
    
    @Override
    public int updateAccountInvitedAmount(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.updateAccountInvitedAmount", para);
    }
    
    @Override
    public int updateAccountRecommendRelation(int accountId)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.updateAccountRecommendRelation", accountId);
    }
    
    @Override
    public int updateAccountIndirectInvitedAmount(int accountId)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.updateAccountIndirectInvitedAmount", accountId);
    }
    
    @Override
    public Map<String, Object> findPartnerInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("PartnerMapper.findPartnerInfo", para);
    }
    
    @Override
    public int findFatherAccountByAccountId(int id)
        throws Exception
    {
        Integer fatherId = getSqlSessionRead().selectOne("PartnerMapper.findFatherAccountByAccountId", id);
        return fatherId == null ? 0 : fatherId.intValue();
    }
    
    @Override
    public int updateAccountRecommendedCount(int parentId)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.updateAccountRecommendedCount", parentId);
    }
    
    @Override
    public int updateAccountSubRecommendedCount(int parentId)
        throws Exception
    {
        return getSqlSession().update("PartnerMapper.updateAccountSubRecommendedCount", parentId);
    }
    
    @Override
    public List<Map<String, Object>> findAccountInfoByAccountRecommendedReturnPoint(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("PartnerMapper.findAccountInfoByAccountRecommendedReturnPoint", para);
    }
    
    @Override
    public int deleteAccountInfoByAccountRecommendedReturnPoint(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().delete("PartnerMapper.deleteAccountInfoByAccountRecommendedReturnPoint", para);
    }
    
    @Override
    public int findFatherPartnerAccountIdById(int faterAccountId)
        throws Exception
    {
        Integer fatherPartnerId = this.getSqlSessionRead().selectOne("PartnerMapper.findFatherPartnerAccountIdById", faterAccountId);
        return fatherPartnerId == null ? -1 : fatherPartnerId.intValue();
    }
    
    @Override
    public int updateAccountIsRecommend(int accountId)
        throws Exception
    {
        return this.getSqlSession().update("PartnerMapper.updateAccountIsRecommend", accountId);
    }
    
    @Override
    public int insertRecommendRelation(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().insert("PartnerMapper.insertRecommendRelation", para);
    }
    
    @Override
    public int updateAccountRecommendedCountAddOne(int accountId)
        throws Exception
    {
        return this.getSqlSession().update("PartnerMapper.updateAccountRecommendedCountAddOne", accountId);
    }
    
    @Override
    public boolean findRecommendRelationIsExist(int currentAccountId, int faterAccountId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("currentAccountId", currentAccountId);
        para.put("faterAccountId", faterAccountId);
        int count = this.getSqlSessionRead().selectOne("PartnerMapper.findRecommendRelationIsExist", para);
        return count > 0;
    }
    
    @Override
    public List<AccountRecommendRelationEntity> findRecommendRelationByFatherId(int accountId)
        throws Exception
    {
        List<AccountRecommendRelationEntity> reList = this.getSqlSessionRead().selectList("PartnerMapper.findRecommendRelationByFatherId", accountId);
        return reList == null ? new ArrayList<AccountRecommendRelationEntity>() : reList;
    }
    
    @Override
    public AccountRecommendRelationEntity findRecommendRelationByCurrentId(int accountId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("PartnerMapper.findRecommendRelationByCurrentId;", accountId);
    }
    
    @Override
    public int updateSubRecommendedCount(int faterAccountId, int size)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("accountId", faterAccountId);
        para.put("count", size);
        return this.getSqlSession().update("PartnerMapper.updateSubRecommendedCount", para);
    }
    
    @Override
    public int saveAccountPartnerTrainRelation(int fatherAccountId, int currAccountId)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("fatherAccountId", fatherAccountId);
        para.put("currAccountId", currAccountId);
        return this.getSqlSession().insert("PartnerMapper.saveAccountPartnerTrainRelation", para);
    }
    
    @Override
    public int updateAccountRecommendedCountForAddOne(int id)
        throws Exception
    {
        return this.getSqlSession().update("PartnerMapper.updateAccountRecommendedCountForAddOne", id);
    }
}
