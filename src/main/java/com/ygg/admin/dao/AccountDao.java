package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.AccountCouponEntity;
import com.ygg.admin.entity.AccountEntity;
import com.ygg.admin.entity.UserStatisticView;

public interface AccountDao
{
    /**
     * 根据para查询Account信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<AccountEntity> findAllAccountByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查询Account信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    AccountEntity findAccountById(int id)
        throws Exception;
    
    /**
     * 根据para查询Account数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAccountByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改用户密码
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updatePWD(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新用户邀请码
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateRecommendedCode(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询所有Account.ids
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Integer> findAllAccountIdsByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询 用户退款账号
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllAccountCard(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计用户退款账号
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllAccountCard(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新用户积分
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateIntegral(Map<String, Object> para)
        throws Exception;
    
    /**
     * 插入用户积分变动记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertIntegralRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询积分变动记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAccountAvailablePointRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计总数
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAccountAvailablePointRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询用户最新积分变动时间
     * 
     * @param idList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findLastIntegralUpdateTime(List<Integer> idList)
        throws Exception;
    
    /**
     * 根据name查询AccountEntity
     * 
     * @param name
     * @return
     * @throws Exception
     */
    AccountEntity findAccountByName(String name)
        throws Exception;
    
    /**
     * 根据用户消费记录分配积分
     * 
     * @return
     */
    int updateAccountIntegral(Integer accountId, int point);
    
    /**
     * 根据para查询pushAccount信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllAccountPushByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para count pushAccount信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllAccountPushByPara(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据用户ID查询pushId 可能会有多个
     * 
     * @param idList
     * @return
     * @throws Exception
     */
    List<String> findAccountPushIdByAccountId(int id)
        throws Exception;
    
    int deleteAccountAvailablePointRecord(int id)
        throws Exception;
    
    /**
     * 更新合伙人
     * 
     * @param account
     * @return
     */
    int updateAccount(AccountEntity account)
        throws Exception;
    
    /**
     * 插入用户账户
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveAccountCard(Map<String, Object> para)
        throws Exception;
    
    /**
     * 用户反馈列表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllPropose(Map<String, Object> para)
        throws Exception;
    
    /**
     * 统计用户反馈列表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countProposes(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新用户反馈记录
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateAccountPropose(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据Id和类型查找用户
     * 
     * @param faterAccountId
     * @param ordinal
     * @return
     */
    AccountEntity findAccountByIdAndType(int faterAccountId, int ordinal)
        throws Exception;
    
    /**
     * 查找用户优惠券信息
     * 
     * @param accountCouponId
     * @return
     */
    AccountCouponEntity findAccountCouponById(int accountCouponId)
        throws Exception;
    
    /**
     * 添加用户优惠券信息
     * 
     * @param ace
     * @return
     */
    int insertAccountCoupon(AccountCouponEntity ace)
        throws Exception;
    
    /**
     * 查询用户累计成交金额
     * 
     * @param id
     * @return
     */
    String finAccountTotalMoney(int id)
        throws Exception;
    
    /**
     * 根据手机号和类型查找用户
     * 
     * @param phoneNumber
     * @param type
     * @return
     */
    AccountEntity findAccountByNameAndType(String phoneNumber, int ordinal)
        throws Exception;
    
    /**
     * 处理反馈
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateDealContent(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> findAccountCouponInfo(Map<String, Object> para)
        throws Exception;
    
    int countAccountCouponInfo(Map<String, Object> para)
        throws Exception;
    
    List<Integer> findIdListByName(String name)
        throws Exception;
    
    int batchUpdateIntegral(List<Map<String, Object>> list)
        throws Exception;
    
    int batchInsertIntegralRecord(List<Map<String, Object>> list)
        throws Exception;
    
    List<Map<String, Object>> findAccountBlacklist(Map<String, Object> para)
        throws Exception;
    
    int countAccountBlacklist(Map<String, Object> para)
        throws Exception;
    
    int deleteBlacklist(int accountId)
        throws Exception;
    
    int insertBlacklist(int accountId, String remark)
        throws Exception;
    
    Map<String, Object> findAccountBlacklistByAccountId(int accountId)
        throws Exception;
    
    /***
     * 根据para-startTime,endTime,type,appchannel 按照日期查找成交明细
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> viewStatisticDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询用户成交信息By渠道
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<UserStatisticView> findRegistUserStatisticByChannel(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据para查询用户成交信息By渠道
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<UserStatisticView> findOrderUserStatisticByChannel(Map<String, Object> para)
        throws Exception;
    
    List<AccountEntity> findAccountsByAccountIds(List<Integer> accountIds);
}
