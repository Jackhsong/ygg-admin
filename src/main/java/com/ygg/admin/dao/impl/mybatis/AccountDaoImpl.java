package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.AccountCouponEntity;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.UserStatisticView;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDaoImpl implements AccountDao
{
    
    @Override
    public List<AccountEntity> findAllAccountByPara(Map<String, Object> para)
        throws Exception
    {
        List<AccountEntity> relList = getSqlSessionRead().selectList("AccountMapper.findAllAccountByPara", para);
        return relList == null ? new ArrayList<AccountEntity>() : relList;
    }
    
    @Override
    public AccountEntity findAccountById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("start", 0);
        para.put("max", 1);
        List<AccountEntity> reList = getSqlSession().selectList("AccountMapper.findAllAccountByPara", para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public int countAccountByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.countAccountByPara", para);
    }
    
    @Override
    public int updatePWD(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("AccountMapper.updatePWD", para);
    }
    
    @Override
    public int updateRecommendedCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("AccountMapper.updateRecommendedCode", para);
    }
    
    @Override
    public List<Integer> findAllAccountIdsByPara(Map<String, Object> para)
        throws Exception
    {
        List<Integer> relList = getSqlSessionRead().selectList("AccountMapper.findAllAccountIdsByPara", para);
        return relList == null ? new ArrayList<Integer>() : relList;
    }
    
    @Override
    public List<Map<String, Object>> findAllAccountCard(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("AccountMapper.findAllAccountCard", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countAllAccountCard(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.countAllAccountCard", para);
    }
    
    @Override
    public int updateIntegral(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("AccountMapper.updateIntegral", para);
    }
    
    @Override
    public int insertIntegralRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("AccountMapper.insertAccountAvailablePointRecord", para);
    }
    
    @Override
    public List<Map<String, Object>> findAccountAvailablePointRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("AccountMapper.findAccountAvailablePointRecord", para);
    }
    
    @Override
    public List<Map<String, Object>> findLastIntegralUpdateTime(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("AccountMapper.findLastIntegralUpdateTime", idList);
    }
    
    @Override
    public int countAccountAvailablePointRecord(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.countAccountAvailablePointRecord", Integer.valueOf(para.get("accountId") + ""));
    }
    
    @Override
    public AccountEntity findAccountByName(String name)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.findAccountByName", name);
    }
    
    @Override
    public int updateAccountIntegral(Integer accountId, int point)
    {
        Map<String, Integer> para = new HashMap<String, Integer>();
        para.put("accountId", accountId);
        para.put("point", point);
        return getSqlSession().update("AccountMapper.updateAccountIntegralByAccountId", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllAccountPushByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("AccountMapper.findAllAccountPushByPara", para);
    }
    
    @Override
    public int countAllAccountPushByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.countAllAccountPushByPara", para);
    }
    
    @Override
    public List<String> findAccountPushIdByAccountId(int id)
        throws Exception
    {
        return getSqlSessionRead().selectList("AccountMapper.findAccountPushIdByAccountId", id);
    }
    
    @Override
    public int deleteAccountAvailablePointRecord(int id)
        throws Exception
    {
        return getSqlSession().delete("AccountMapper.deleteAccountAvailablePointRecord", id);
    }
    
    @Override
    public int updateAccount(AccountEntity account)
        throws Exception
    {
        return getSqlSession().update("AccountMapper.updateAccount", account);
    }
    
    @Override
    public int saveAccountCard(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("AccountMapper.saveAccountCard", para);
    }
    
    @Override
    public int countProposes(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.countProposes", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllPropose(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> list = getSqlSessionRead().selectList("AccountMapper.findAllPropose", para);
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }
    
    @Override
    public int updateAccountPropose(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("AccountMapper.updateAccountPropose", para);
    }
    
    @Override
    public AccountEntity findAccountByIdAndType(int id, int type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        para.put("type", type);
        para.put("start", 0);
        para.put("max", 1);
        List<AccountEntity> reList = findAllAccountByPara(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public AccountCouponEntity findAccountCouponById(int accountCouponId)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("AccountMapper.findAccountCouponById", accountCouponId);
    }
    
    @Override
    public int insertAccountCoupon(AccountCouponEntity ace)
        throws Exception
    {
        return this.getSqlSession().insert("AccountMapper.insertAccountCoupon", ace);
    }
    
    @Override
    public String finAccountTotalMoney(int id)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("AccountMapper.finAccountTotalMoney", id);
    }
    
    @Override
    public AccountEntity findAccountByNameAndType(String phoneNumber, int type)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("name", phoneNumber);
        para.put("type", type);
        return this.getSqlSessionRead().selectOne("AccountMapper.findAccountByNameAndType", para);
    }
    
    @Override
    public int updateDealContent(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSession().update("AccountMapper.updateDealContent", para);
    }
    
    @Override
    public int countAccountCouponInfo(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectOne("AccountMapper.countAccountCouponInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findAccountCouponInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = this.getSqlSessionRead().selectList("AccountMapper.findAccountCouponInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Integer> findIdListByName(String name)
        throws Exception
    {
        return getSqlSessionRead().selectList("AccountMapper.findIdListByName", name);
    }
    
    @Override
    public int batchInsertIntegralRecord(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().insert("AccountMapper.batchInsertIntegralRecord", list);
    }
    
    @Override
    public int batchUpdateIntegral(List<Map<String, Object>> list)
        throws Exception
    {
        return getSqlSession().update("AccountMapper.batchUpdateIntegral", list);
    }
    
    @Override
    public List<Map<String, Object>> findAccountBlacklist(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("AccountMapper.findAccountBlacklist", para);
    }
    
    @Override
    public int countAccountBlacklist(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.countAccountBlacklist", para);
    }
    
    @Override
    public int deleteBlacklist(int accountId)
        throws Exception
    {
        return getSqlSession().delete("AccountMapper.deleteBlacklist", accountId);
    }
    
    @Override
    public int insertBlacklist(int accountId, String remark)
        throws Exception
    {
        Map<String, Object> para = new HashMap<>();
        para.put("accountId", accountId);
        para.put("remark", remark);
        return getSqlSession().insert("AccountMapper.insertBlacklist", para);
    }
    
    @Override
    public Map<String, Object> findAccountBlacklistByAccountId(int accountId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("AccountMapper.findAccountBlacklistByAccountId", accountId);
    }
    
    @Override
    public List<UserStatisticView> findRegistUserStatisticByChannel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("AnalyzeMapper.findRegistUserStatisticByChannel", para);
    }
    
    @Override
    public List<UserStatisticView> findOrderUserStatisticByChannel(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("AnalyzeMapper.findOrderUserStatisticByChannel", para);
    }
    
    @Override
    public List<Map<String, Object>> viewStatisticDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("AnalyzeMapper.findUserStatisticDetail", para);
    }
    
    @Override
    public List<AccountEntity> findAccountsByAccountIds(List<Integer> accountIds)
    {
        return getSqlSessionRead().selectList("AccountMapper.findAccountsByAccountIds", accountIds);
    }
    
}
