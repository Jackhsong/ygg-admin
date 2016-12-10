package com.ygg.admin.service;

import java.util.Map;

public interface PartnerService
{
    /**
     * 合伙人信息列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonPartnerInfo(Map<String, Object> para)
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
     * 手动增加合伙人
     * @param para
     * @return
     * @throws Exception
     */
    int addPartnerByHandle(Map<String, Object> para)
        throws Exception;
    
    /**
     * 合伙人申请列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonPartnerApplyInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 通过或拒绝合伙人申请
     * @param para
     * @return
     * @throws Exception
     */
    int updatePartnerApplyStatus(Map<String, Object> para)
        throws Exception;
    
    /**
     * 邀请关系列表
     * @param para
     * @return
     */
    Map<String, Object> jsonInviteRelationInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 返积分记录列表
     * @param para
     * @return
     */
    Map<String, Object> jsonReturnIntegralInfo(Map<String, Object> para)
        throws Exception;
    
    /**
     * 积分提现列表
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> jsonExchangeIntegralInfo(Map<String, Object> para)
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
    Map<String, Object> jsonOrderDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 手动创建邀请关系
     * @param faterAccountId
     * @param currentAccountId
     * @return 1：成功；2：邀请人不存在；3：被邀请人Id不存在；4：被邀请人是合伙人不能邀请；5：被邀请人已经填写过邀请码
     */
    int createInviteRelation(int faterAccountId, int currentAccountId)
        throws Exception;
    
    /**
     * 统计返积分记录条数
     * @param para
     * @return
     * @throws Exception
     */
    int getAllReturnIntegralNums(Map<String, Object> para)
        throws Exception;
    
}
