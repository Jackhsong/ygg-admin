package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.entity.AccountEntity;

public interface AccountService
{
    /**
     * 根据para查询用户信息，并封装成json字符串返回
     * @param para
     * @param searchType   1：正常用户列表  ;  2：用户推送列表
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonAccountInfo(Map<String, Object> para, int searchType)
        throws Exception;
    
    /**
     * 导出前先统计数量
     * @param para
     * @param searchType
     * @return
     * @throws Exception
     */
    int getJsonAccountInfoNums(Map<String, Object> para, int searchType)
        throws Exception;
    
    /**
     * 修改用户密码
     * 
     * @param accountId
     * @param pwd
     * @return
     * @throws Exception
     */
    Map<String, Object> updatePWD(int accountId, String pwd)
        throws Exception;
    
    /**
     * 查询 用户退款账号查询
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllAccountCard(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询用户积分信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllAccountIntegral(Map<String, Object> para)
        throws Exception;
    
    int getAllAccountIntegralNums(Map<String, Object> para)
        throws Exception;
    
    /**
     * 修改用户积分
     * @param accountId
     * @param integral
     * @param isForcibly  积分不足时是否强制删除至0
     * @param source    积分调整来源 
     * @param reason 调整原因
     * @return
     * @throws Exception
     */
    Map<String, Object> updateAccountIntegral(int accountId, int integral, boolean isForcibly, String source, String reason)
        throws Exception;
    
    /**
     * 查询用户积分变动明细
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAccountIntegralRecord(Map<String, Object> para)
        throws Exception;
    
    /**
     * 根据ID查询AccountEntity
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> findAccountById(int id)
        throws Exception;
    
    /**
     * 根据用户名查询AccountEntity
     * 
     * @param name
     * @return
     * @throws Exception
     */
    AccountEntity findAccountByName(String name)
        throws Exception;
    
    /**
     * 根据用户的消费情况增加积分
     * 
     * @param list
     * @return
     * @throws Exception
     */
    int updateAccountIntegralBySpending(List<Map<String, Object>> list)
        throws Exception;
    
    /**
     * 根据用户ID查询pushId  可能会有多个
     * @param id
     * @return
     * @throws Exception
     */
    List<String> findAccountPushIdByAccountId(int id)
        throws Exception;
    
    /**
     * 保存用户账户信息
     * @return
     * @throws Exception
     */
    int saveAccountCard(int accountId, int bankOrAlipay, String cardName, String cardNumber, int bankType)
        throws Exception;
    
    /**
     * 修正没有recommended_code的用户记录
     * @return
     * @throws Exception
     */
    int resetRecommendedCode()
        throws Exception;
    
    /**
     * 用户反馈列表
     * @param para
     * @return
     */
    Map<String, Object> findAllProposeInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 用户建议返积分
     * @param id：建议Id
     * @param accountId：用户Id
     * @param score：返积分数量
     * @return
     */
    int returnScoreForPropose(int id, int accountId, int score)
        throws Exception;
    
    /**
     * 处理反馈
     * @param id
     * @param dealContent
     * @return
     * @throws Exception
     */
    int updateDealContent(int id, String dealContent)
        throws Exception;
    
    /**
     * 查找所有用户优惠券信息
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllAccountCouponInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 导入用户积分
     * @param file
     * @return
     * @throws Exception
     */
    String importAccountIntegral(MultipartFile file)
        throws Exception;

    /**
     * 查询用户黑名单
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAccountBlacklist(Map<String, Object> para)
        throws Exception;

    /**
     * 删除用户黑名单
     * @param accountId
     * @return
     * @throws Exception
     */
    int deleteBlacklist(int accountId)
        throws Exception;

    int addBlacklist(int accountId, String remark)
        throws Exception;
}
