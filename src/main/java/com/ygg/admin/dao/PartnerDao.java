package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.AccountRecommendRelationEntity;

public interface PartnerDao
{
    /**
     * 合伙人信息列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findJsonPartnerInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计合伙人信息
     * @param para
     * @return
     * @throws Exception
     */
    int countPartnerInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 取消或恢复合伙人资格
     * @param para
     * @return
     * @throws Exception
     */
    int updatePartnerStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 新增合伙人记录
     * @param para
     * @return
     * @throws Exception
     */
    int insertPartnerApply(Map<String, Object> para)
        throws Exception;
    
    /**
     * 合伙人申请信息
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findJsonPartnerApplyInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计合伙人申请信息
     * @param para
     * @return
     * @throws Exception
     */
    int countPartnerApplyInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 邀请关系列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findJsonInviteRelationInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计邀请关系
     * @param para
     * @return
     * @throws Exception
     */
    int countInviteRelationInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 返积分记录列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findJsonReturnIntegralInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计返积分记录
     * @param para
     * @return
     * @throws Exception
     */
    int countJsonReturnIntegralInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 提现列表
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findJsonExchangeIntegralInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计提现列表
     * @param para
     * @return
     * @throws Exception
     */
    int countJsonExchangeIntegralInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 处理积分提现
     * @param para
     * @return
     */
    int dealWithExchange(Map<String, Object> para)
        throws Exception;
    
    /**
     * 分销订单明细
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findJsonOrderDetailInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计分销订单
     * @param para
     * @return
     * @throws Exception
     */
    int countJsonOrderDetailInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查找上级推荐人是否是合伙人
     * @param accountId
     * @return
     */
    List<Map<String, Object>> findRecommendFriendsByAccountId(int accountId)
        throws Exception;
    
    /**
     * 查找下级推荐人
     * @param accountId
     * @return
     */
    List<Map<String, Object>> findInvitedFriendsByAccountId(int accountId)
        throws Exception;
    
    /**
     * 插入合伙人关系表
     * @param para
     * @return
     * @throws Exception
     */
    int insertAccountPartnerRelation(Map<String, Object> para)
        throws Exception;
    
    /**
     * 通过合伙人申请
     * @param para
     * @return
     * @throws Exception
     */
    int passPartnerApply(Map<String, Object> para)
        throws Exception;
    
    /**
     * 拒绝合伙人申请
     * @param para
     * @return
     */
    int refusePartnerApply(Map<String, Object> para)
        throws Exception;
    
    /**
     * 删除合伙人关联关系
     * @param map
     * @return
     */
    int deleteFromAccountPartnerRelation(Map<String, Object> map)
        throws Exception;
    
    /**
     * 更新自己推荐人数、小伙伴推荐人数
     * @param map
     * @return
     */
    int updateAccountInvitedAmount(Map<String, Object> map)
        throws Exception;
    
    int updateAccountRecommendRelation(int accountId)
        throws Exception;
    
    int updateAccountIndirectInvitedAmount(int accountId)
        throws Exception;
    
    /**
     * 合伙人信息
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findPartnerInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 通过当前账号查找上级推荐人账号
     * @param id
     * @return
     * @throws Exception
     */
    int findFatherAccountByAccountId(int id)
        throws Exception;
    
    /**
     * 将合伙人直接推荐人数减1
     * @param parentId
     * @return
     */
    int updateAccountRecommendedCount(int parentId)
        throws Exception;
    
    /**
     * 将合伙人间接推荐人数减1
     * @param parentId
     * @return
     */
    int updateAccountSubRecommendedCount(int parentId)
        throws Exception;
    
    List<Map<String, Object>> findAccountInfoByAccountRecommendedReturnPoint(Map<String, Object> para)
        throws Exception;
    
    int deleteAccountInfoByAccountRecommendedReturnPoint(Map<String, Object> para)
        throws Exception;
    
    int findFatherPartnerAccountIdById(int faterAccountId)
        throws Exception;
    
    int updateAccountIsRecommend(int currentAccountId)
        throws Exception;
    
    int insertRecommendRelation(Map<String, Object> para)
        throws Exception;
    
    int updateAccountRecommendedCountAddOne(int faterAccountId)
        throws Exception;
    
    boolean findRecommendRelationIsExist(int currentAccountId, int faterAccountId)
        throws Exception;
    
    List<AccountRecommendRelationEntity> findRecommendRelationByFatherId(int currentAccountId)
        throws Exception;
    
    AccountRecommendRelationEntity findRecommendRelationByCurrentId(int accountId)
        throws Exception;
    
    int updateSubRecommendedCount(int faterAccountId, int size)
        throws Exception;
    
    /**
     * 插入合伙人培养关系表
     * 
     * @param fatherAccountId
     * @param currAccountId
     * @return
     * @throws Exception
     */
    int saveAccountPartnerTrainRelation(int fatherAccountId, int currAccountId)
        throws Exception;
    
    /**
     * 将直接邀请人数+1
     * @param id
     * @return
     * @throws Exception
     */
    int updateAccountRecommendedCountForAddOne(int id)
        throws Exception;
    
}
