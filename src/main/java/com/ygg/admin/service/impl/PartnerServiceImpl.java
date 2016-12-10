package com.ygg.admin.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.code.AccountEnum;
import com.ygg.admin.code.OrderEnum;
import com.ygg.admin.code.PartnerEnum;
import com.ygg.admin.dao.AccountDao;
import com.ygg.admin.dao.PartnerDao;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.AccountRecommendRelationEntity;
import com.ygg.admin.service.PartnerService;
import com.ygg.admin.util.CommonEnum;

@Service("PartnerService")
public class PartnerServiceImpl implements PartnerService
{
    @Resource
    private PartnerDao partnerDao;
    
    @Resource
    private AccountDao accountDao;
    
    private Set<Integer> recommendRelationAccountIdSet = new HashSet<Integer>();
    
    @Override
    public Map<String, Object> jsonPartnerInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> partnerInfoList = partnerDao.findJsonPartnerInfo(para);
        int total = 0;
        if (partnerInfoList.size() > 0)
        {
            for (Map<String, Object> map : partnerInfoList)
            {
                map.put("job", PartnerEnum.JOB_TYPE.getDescByCode((int)(map.get("jobType"))));
                map.put("monthlyIncome", PartnerEnum.MONTHLY_INCOME.getDescByCode((int)(map.get("monthlyIncomeType"))));
            }
            total = partnerDao.countPartnerInfo(para);
        }
        resultMap.put("rows", partnerInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updatePartnerStatus(Map<String, Object> para)
        throws Exception
    {
        return partnerDao.updatePartnerStatus(para);
    }
    
    @Override
    public int addPartnerByHandle(Map<String, Object> para)
        throws Exception
    {
        /**
         * 手动添加的合伙人将partner_status设为STATUS_OF_IS_PARTNER，
         * apply_partner_status设为APPLY_STATUS_OF_PASSED
         */
        AccountEntity account = (AccountEntity)para.get("account");
        
        //如果有推荐人，不允许手动变成合伙人
        if (partnerDao.findFatherAccountByAccountId(account.getId()) > 0)
        {
            return 0;
        }
        
        account.setPartnerStatus(PartnerEnum.PARTNER_STATUS.YES.getCode());
        account.setApplyPartnerStatus(PartnerEnum.APPLY_STATUS.PASSED.getCode());
        accountDao.updateAccount(account);
        para.put("accountId", account.getId());
        para.put("isApply", 0);
        partnerDao.insertPartnerApply(para);
        
        // 查找当前账号的上级推荐人中是否有合伙人
        int accountId = account.getId();
        if (findParentIsPartner(accountId) != -1)
        {
            
            int recommendFatherId = partnerDao.findFatherAccountByAccountId(accountId);
            int partnerFatherId = partnerDao.findFatherPartnerAccountIdById(accountId);
            
            Map<String, Object> map = new HashMap<String, Object>();
            //删除当前账号与上级合伙人的关联关系
            map.put("accountId", accountId);
            map.put("parentId", partnerFatherId);
            if (partnerDao.deleteFromAccountPartnerRelation(map) == 1)
            {
                if (recommendFatherId != partnerFatherId)
                {
                    //非直接邀请关系 ，将partnerFatherId对应的账号的间接邀请人减一
                    partnerDao.updateAccountSubRecommendedCount(partnerFatherId);
                }
            }
            
            // 将当前账号与邀请的小伙伴关联关系插入到合伙人关联表中
            findInvitedFriends(accountId, true);
            
            // 插入 账号合伙人培养关系
            // 1. 假如邀请人是合伙人，则插入合伙人培养关系
            AccountEntity ae = accountDao.findAccountById(recommendFatherId);
            if (ae.getPartnerStatus() == PartnerEnum.PARTNER_STATUS.YES.getCode())
            {
                partnerDao.saveAccountPartnerTrainRelation(recommendFatherId, accountId);
            }
        }
        // 如果当前账号的上级推荐人中没有合伙人，则在将当前账号与其邀请的小伙伴放在合伙人关系表中
        else
        {
            // 将当前账号与邀请的小伙伴关联关系插入到合伙人关联表中
            findInvitedFriends(accountId, false);
        }
        // 将当前账号在account_recommend_relation表中的记录curr_is_partner设为1
        partnerDao.updateAccountRecommendRelation(accountId);
        
        // 2. 当前和下级合伙人关系
        List<Map<String, Object>> directInvitedList = partnerDao.findInvitedFriendsByAccountId(accountId);// 直接邀请小伙伴
        for (Map<String, Object> it : directInvitedList)
        {
            int itPar = Integer.valueOf(it.get("isPartner") + "").intValue();
            if (itPar == 1)
            {
                int itAid = Integer.valueOf(it.get("accountId") + "").intValue();
                partnerDao.saveAccountPartnerTrainRelation(accountId, itAid);
            }
        }
        
        return 1;
    }
    
    @Override
    public Map<String, Object> jsonPartnerApplyInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> partnerInfoList = partnerDao.findJsonPartnerApplyInfo(para);
        int total = 0;
        if (partnerInfoList.size() > 0)
        {
            for (Map<String, Object> map : partnerInfoList)
            {
                map.put("createTime", ((Timestamp)map.get("createTime")).toString());
                map.put("updateTime", ((Timestamp)map.get("updateTime")).toString());
                map.put("auditStatus", PartnerEnum.AUDIT_STATUS.getDescByCode((int)(map.get("applyStatus"))));
                map.put("job", PartnerEnum.JOB_TYPE.getDescByCode((int)(map.get("jobType"))));
                map.put("monthlyIncome", PartnerEnum.MONTHLY_INCOME.getDescByCode((int)(map.get("monthlyIncomeType"))));
            }
            total = partnerDao.countPartnerApplyInfo(para);
        }
        resultMap.put("rows", partnerInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int updatePartnerApplyStatus(Map<String, Object> para)
        throws Exception
    {
        String status = para.get("status") + "";
        int accountId = (int)para.get("accountId");
        
        // 通过合伙人申请
        if ("4".equals(status))
        {
            // 将当前账号的partner_status设为2（是合伙人），apply_partner_status设为4（审核通过）
            partnerDao.passPartnerApply(para);
            
            // 查找当前账号的上级推荐人中是否有合伙人
            
            if (findParentIsPartner(accountId) != -1)
            {
                /**
                 * 注意一种情况： A -> B -> C ->D(27032) : 变合伙人先后顺序。先A 、B  、C (直接邀请的人可能指向A或B)
                 *
                 *  带走自己的邀请人，
                 *  1 : 上级合伙人的间接邀请人减掉x人数。
                 *  2 : 计算当前合伙人的间接邀请人
                 */
                int recommendFatherId = partnerDao.findFatherAccountByAccountId(accountId);
                int partnerFatherId = partnerDao.findFatherPartnerAccountIdById(accountId);
                
                Map<String, Object> map = new HashMap<String, Object>();
                //删除当前账号与上级合伙人的关联关系
                map.put("accountId", accountId);
                map.put("parentId", partnerFatherId);
                if (partnerDao.deleteFromAccountPartnerRelation(map) == 1)
                {
                    if (recommendFatherId != partnerFatherId)
                    {
                        //非直接邀请关系 ，将partnerFatherId对应的账号的间接邀请人减一
                        partnerDao.updateAccountSubRecommendedCount(partnerFatherId);
                    }
                }
                
                // 将当前账号与邀请的小伙伴关联关系插入到合伙人关联表中
                findInvitedFriends(accountId, true);
                
                // 插入 账号合伙人培养关系
                // 1. 假如邀请人是合伙人，则插入合伙人培养关系
                AccountEntity ae = accountDao.findAccountById(recommendFatherId);
                if (ae.getPartnerStatus() == PartnerEnum.PARTNER_STATUS.YES.getCode())
                {
                    partnerDao.saveAccountPartnerTrainRelation(recommendFatherId, accountId);
                }
            }
            // 如果当前账号的上级推荐人中没有合伙人，则在将当前账号与其邀请的小伙伴放在合伙人关系表中
            else
            {
                // 将当前账号与邀请的小伙伴关联关系插入到合伙人关联表中
                findInvitedFriends(accountId, false);
            }
            // 将当前账号在account_recommend_relation表中的记录curr_is_partner设为1
            partnerDao.updateAccountRecommendRelation(accountId);
            
            // 2. 当前和下级合伙人关系
            List<Map<String, Object>> directInvitedList = partnerDao.findInvitedFriendsByAccountId(accountId);// 直接邀请小伙伴
            for (Map<String, Object> it : directInvitedList)
            {
                int itPar = Integer.valueOf(it.get("isPartner") + "").intValue();
                if (itPar == 1)
                {
                    int itAid = Integer.valueOf(it.get("accountId") + "").intValue();
                    partnerDao.saveAccountPartnerTrainRelation(accountId, itAid);
                }
            }
            
        }
        // 拒绝合伙人申请
        else
        {
            // 将当前账号的apply_partner_status设为3（申请未通过）
            partnerDao.refusePartnerApply(para);
        }
        return 1;
    }
    
    @Override
    public Map<String, Object> jsonInviteRelationInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> partnerInfoList = partnerDao.findJsonInviteRelationInfo(para);
        int total = 0;
        if (partnerInfoList.size() > 0)
        {
            for (Map<String, Object> map : partnerInfoList)
            {
                byte partnerStatus = Byte.parseByte(map.get("partnerStatus") + "");
                String realname = "";
                if (partnerStatus != 1)
                {
                    Map<String, Object> searchPara = new HashMap<String, Object>();
                    searchPara.put("accountId", map.get("userId"));
                    Map<String, Object> r = partnerDao.findPartnerInfo(searchPara);
                    realname = r.get("realname") + "";
                }
                map.put("identity", ((int)map.get("partnerStatus")) == 2 ? "合伙人" : "普通用户");
                map.put("realname", realname);
                map.put("createTime", ((Timestamp)map.get("createTime")).toString());
            }
            total = partnerDao.countInviteRelationInfo(para);
        }
        resultMap.put("rows", partnerInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> jsonReturnIntegralInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> partnerInfoList = partnerDao.findJsonReturnIntegralInfo(para);
        int total = 0;
        if (partnerInfoList.size() > 0)
        {
            for (Map<String, Object> map : partnerInfoList)
            {
                map.put("statusDesc", OrderEnum.ORDER_STATUS.getDescByCode(Integer.valueOf(map.get("status") + "")));
                map.put("identity", ((int)map.get("partnerStatus")) == 2 ? "合伙人" : "普通用户");
                map.put("returnType", AccountEnum.RECOMMEND_RETURNPOINT_TYPE.getDescByCode(((int)map.get("type"))));
                map.put("createTime", ((Timestamp)map.get("createTime")).toString());
                //被邀请人用户名
                /*long recommendedAccountId = Long.parseLong(map.get("recommendedAccountId") + "");
                AccountEntity a = accountDao.findAccountById((int)recommendedAccountId);
                map.put("coveredUsername", a == null ? "" : a.getName());*/
            }
            total = partnerDao.countJsonReturnIntegralInfo(para);
        }
        resultMap.put("rows", partnerInfoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public Map<String, Object> jsonExchangeIntegralInfo(Map<String, Object> para)
        throws Exception
    {
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = partnerDao.findJsonExchangeIntegralInfo(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("statusStr", ((int)map.get("status")) == 1 ? "待提现" : "已打款");
                map.put("money", String.format("%.2f", Float.valueOf(map.get("point") + "") / 100));
                map.put("bank", ((int)map.get("type")) == 2 ? "支付宝" : CommonEnum.BankTypeEnum.getDescriptionByOrdinal((int)map.get("bankType")));
                map.put("bankAccount", map.get("cardNumber"));
                map.put("createTime", ((Timestamp)map.get("createTime")).toString());
            }
            total = partnerDao.countJsonExchangeIntegralInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    @Override
    public int dealWithExchange(Map<String, Object> para)
        throws Exception
    {
        return partnerDao.dealWithExchange(para);
    }
    
    @Override
    public Map<String, Object> jsonOrderDetail(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = partnerDao.findJsonOrderDetailInfo(para);
        int total = 0;
        if (infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                map.put("isInvitedStr", ((int)map.get("isInvited")) == 1 ? "直接" : "间接");
                map.put("orderStatusStr", OrderEnum.ORDER_STATUS.getDescByCode((int)map.get("orderStatus")));
                map.put("payTime", ((Timestamp)map.get("payTime")).toString());
                map.put("sendTime", ((Timestamp)map.get("sendTime")).toString());
            }
            total = partnerDao.countJsonOrderDetailInfo(para);
        }
        resultMap.put("rows", infoList);
        resultMap.put("total", total);
        return resultMap;
    }
    
    private int findParentIsPartner(int accountId)
        throws Exception
    {
        int partnerId = -1;
        List<Map<String, Object>> parentPartners = partnerDao.findRecommendFriendsByAccountId(accountId);
        // size=0说明已经在推荐关系中找到最上层记录，找到最上层记录之后去account表中查找改用户是否是合伙人
        if (parentPartners.size() == 0)
        {
            AccountEntity account = accountDao.findAccountById(accountId);
            if (account == null)
            {
                return partnerId;
            }
            //如果顶层推荐人是合伙人，则返回顶层合伙人Id
            if (account.getPartnerStatus() == PartnerEnum.PARTNER_STATUS.YES.getCode())
            {
                return accountId;
            }
            
        }
        else
        {
            for (Map<String, Object> map : parentPartners)
            {
                int currAccountId = Integer.valueOf(map.get("accountId") + "").intValue();
                int parentId = Integer.valueOf(map.get("parentId") + "").intValue();
                int isPartnerInt = Integer.valueOf(map.get("isPartner") + "").intValue();
                // 如果上级邀请人是合伙人，则返回上级邀请人的accountId
                if (isPartnerInt == 1)
                {
                    partnerId = currAccountId;
                    break;
                }
                // 如果上级邀请人不是合伙人，则继续向上寻找
                else
                {
                    partnerId = findParentIsPartner(parentId);
                }
            }
        }
        return partnerId;
    }
    
    private void findInvitedFriends(int id, boolean deleteFirst)
        throws Exception
    {
        // 直接邀请小伙伴
        List<Map<String, Object>> directInvitedList = partnerDao.findInvitedFriendsByAccountId(id);
        for (Map<String, Object> map : directInvitedList)
        {
            int isPartnerInt = Integer.valueOf(map.get("isPartner") + "").intValue();
            int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
            if (isPartnerInt == 1)
            {
                continue;
            }
            else
            {
                Map<String, Object> para = new HashMap<>();
                para.put("accountId", accountId);
                para.put("parentId", id);
                if (deleteFirst)
                {
                    int partnerFatherId = partnerDao.findFatherPartnerAccountIdById(accountId);
                    Map<String, Object> deleteMap = new HashMap<>();
                    deleteMap.put("parentId", partnerFatherId);
                    deleteMap.put("accountId", accountId);
                    if (partnerDao.deleteFromAccountPartnerRelation(deleteMap) == 1)
                    {
                        // 同时将parentId对应的账号的间接邀请人减一
                        partnerDao.updateAccountSubRecommendedCount(partnerFatherId);
                    }
                }
                partnerDao.insertAccountPartnerRelation(para);
                findIndirectInvitedFriends(accountId, deleteFirst, id);
            }
        }
    }
    
    private void findIndirectInvitedFriends(int id, boolean deleteFirst, int nowApplyAccountId)
        throws Exception
    {
        List<Map<String, Object>> childRecommends = partnerDao.findInvitedFriendsByAccountId(id);
        for (Map<String, Object> map : childRecommends)
        {
            int isPartnerInt = Integer.valueOf(map.get("isPartner") + "").intValue();
            int accountId = Integer.valueOf(map.get("accountId") + "").intValue();
            if (isPartnerInt == 1)
            {
                continue;
            }
            else
            {
                Map<String, Object> para = new HashMap<>();
                para.put("accountId", accountId);
                para.put("parentId", nowApplyAccountId);
                if (deleteFirst)
                {
                    int partnerFatherId = partnerDao.findFatherPartnerAccountIdById(accountId);
                    Map<String, Object> deleteMap = new HashMap<>();
                    deleteMap.put("parentId", partnerFatherId);
                    deleteMap.put("accountId", accountId);
                    if (partnerDao.deleteFromAccountPartnerRelation(deleteMap) == 1)
                    {
                        // 同时将parentId对应的账号的间接邀请人减一
                        partnerDao.updateAccountSubRecommendedCount(partnerFatherId);
                    }
                }
                partnerDao.insertAccountPartnerRelation(para);
                // 更新当前账号的的小伙伴邀请人数
                partnerDao.updateAccountIndirectInvitedAmount(nowApplyAccountId);
                findIndirectInvitedFriends(accountId, deleteFirst, nowApplyAccountId);
            }
        }
    }
    
    @Override
    public int createInviteRelation(int faterAccountId, int currentAccountId)
        throws Exception
    {
        AccountEntity father = accountDao.findAccountByIdAndType(faterAccountId, AccountEnum.ACCOUNT_TYPE.MOBILE.getCode());
        if (father == null)
        {
            return 2;
        }
        AccountEntity current = accountDao.findAccountByIdAndType(currentAccountId, AccountEnum.ACCOUNT_TYPE.MOBILE.getCode());
        if (current == null)
        {
            return 3;
        }
        
        if (current.getPartnerStatus() == PartnerEnum.PARTNER_STATUS.YES.getCode())
        {
            return 4;
        }
        if (current.getIsRecommended() == 1)
        {
            return 5;
        }
        // 检查邀请关系是否存在（currentAccountId已经被faterAccountId）
        boolean isExist = partnerDao.findRecommendRelationIsExist(currentAccountId, faterAccountId);
        if (isExist)
        {
            return 6;
        }
        
        //检查faterAccountId是否已经被currentAccountId邀请
        boolean isInvited = partnerDao.findRecommendRelationIsExist(faterAccountId, currentAccountId);
        if (isInvited)
        {
            return 7;
        }
        // 查找邀请人上级是否存在合伙人
        int faterPartnerId = partnerDao.findFatherPartnerAccountIdById(faterAccountId);
        if (faterPartnerId != -1)
        {
            //将邀请人上级合伙人小伙伴推荐人数+1
            partnerDao.updateAccountIndirectInvitedAmount(faterPartnerId);
        }
        
        //邀请人的直接邀请人数+1
        partnerDao.updateAccountRecommendedCountAddOne(faterAccountId);
        
        //被邀请人更该为填写过邀请码
        partnerDao.updateAccountIsRecommend(currentAccountId);
        
        if (father.getPartnerStatus() == PartnerEnum.PARTNER_STATUS.YES.getCode())
        {
            //如果邀请人是合伙人，则在合伙人关系表中增加一条记录
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountId", currentAccountId);
            para.put("parentId", faterAccountId);
            partnerDao.insertAccountPartnerRelation(para);
            
            // 如果当前被邀请人已经邀请了小伙伴并且邀请人是合伙人,全部变量，防止并发访问
            synchronized (recommendRelationAccountIdSet)
            {
                findParentRecommendAccountId(currentAccountId);
                for (Integer accountId : recommendRelationAccountIdSet)
                {
                    para.put("accountId", accountId);
                    para.put("parentId", faterAccountId);
                    partnerDao.insertAccountPartnerRelation(para);
                }
                partnerDao.updateSubRecommendedCount(faterAccountId, recommendRelationAccountIdSet.size());
                
                recommendRelationAccountIdSet.clear();
            }
        }
        else if (faterPartnerId != -1)
        {
            //如果邀请人上级是合伙人，则在合伙人关系表中增加一条记录
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("accountId", currentAccountId);
            para.put("parentId", faterPartnerId);
            partnerDao.insertAccountPartnerRelation(para);
        }
        
        //新增邀请关系中记录
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("currentAccountId", currentAccountId);
        para.put("faterAccountId", faterAccountId);
        partnerDao.insertRecommendRelation(para);
        
        return 1;
    }
    
    private void findParentRecommendAccountId(int accountId)
        throws Exception
    {
        List<AccountRecommendRelationEntity> arreList = partnerDao.findRecommendRelationByFatherId(accountId);
        for (AccountRecommendRelationEntity arre : arreList)
        {
            recommendRelationAccountIdSet.add(arre.getCurrentAccountId());
            findParentRecommendAccountId(arre.getCurrentAccountId());
        }
    }
    
    @Override
    public int getAllReturnIntegralNums(Map<String, Object> para)
        throws Exception
    {
        return partnerDao.countJsonReturnIntegralInfo(para);
    }
}
