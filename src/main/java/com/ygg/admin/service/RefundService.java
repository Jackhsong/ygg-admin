package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.*;

public interface RefundService
{
    
    /**
     * 查询退款退货信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllRefundInfo(Map<String, Object> para)
        throws Exception;
        
    RefundEntity findRefundInfo(int refundId)
        throws Exception;
        
    /**
     * 获取 退款 记录 信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    Map<String, Object> refundDetail(int id)
        throws Exception;
        
    /**
     * 获取 仅退款记录 操作信息
     * 
     * @param refundId
     * @return
     * @throws Exception
     */
    Map<String, Object> findRefundOnlyReturnMoneyTeackMap(int refundId)
        throws Exception;
        
    /**
     * 获取 退款并退货记录 操作信息
     * 
     * @param refundId
     * @return
     * @throws Exception
     */
    Map<String, Object> findRefundReturnMoneyAndGoodsTeackMap(int refundId)
        throws Exception;
        
    /**
     * 更新退款退货信息
     * 
     * @param type 类型，1：仅退款；2：退货并退款
     * @param dealType 本次处理类型 1：ok ， 2： 关闭 ， 3：取消
     * @param step 本次处理步骤
     * @param manager 管理员
     * @param id 退款退货ID
     * @param remark 本次处理备注
     * @param money 本次修改后的金额
     * @param cardId 财务退款账户Id
     * @param modifyRefundType 是否将仅退款修改为退款并退货
     * @param sendGoods 该订单是否继续发货
     * @param realSendGoodsCount 客服修改发货数量
     * @param cancelOrder 是否同时取消订单
     * @param refundReasonId 退款原因
     * @return
     * @throws Exception
     */
    Map<String, Object> updateRefund(Integer type, Integer dealType, Byte step, User manager, Integer id, String remark, Double money, int cardId, boolean modifyRefundType,
        boolean sendGoods, String realSendGoodsCount, boolean cancelOrder, int refundReasonId)
            throws Exception;
            
    /**
     * 更新退款记录金额
     * 
     * @param id
     * @param money
     * @return
     * @throws Exception
     */
    Map<String, Object> updateRefundPrice(Integer id, Double money)
        throws Exception;
        
    Map<String, Object> updateRefund(Map<String, Object> para)
        throws Exception;
        
    /**
     * 查询财务打款账户列表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> findAllFinancialAffairsCard(Map<String, Object> para)
        throws Exception;
        
    /**
     * 查询财务打款账户列表 for 财务处理
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findAllFinancialAffairsCardForDeal()
        throws Exception;
        
    /**
     * 插入财务打款账户
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int saveFinancialAffairsCard(Map<String, Object> para)
        throws Exception;
        
    /**
     * 删除财务打款账户
     * 
     * @param id
     * @return
     * @throws Exception
     */
    int deleteFinancialAffairsCardByIds(List<Integer> idList)
        throws Exception;
        
    /**
     * 保存 退货商品物流信息
     * 
     * @param refundId 退款退货ID
     * @param number 物流单号
     * @param channel 物流渠道
     * @return
     * @throws Exception
     */
    Map<String, Object> saveRefundLogisticsInfo(int refundId, String number, String channel, User manager)
        throws Exception;
        
    /**
     * 退款退货商品 确认收货
     * 
     * @param id
     * @param remark ：备注
     * @param manager ：处理人
     * @return
     * @throws Exception
     */
    Map<String, Object> confirmGoods(int id, User manager, String remark)
        throws Exception;
        
    /**
     * 退款账户code
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getAdminBankInfoCode()
        throws Exception;
        
    /**
     * 查询打款账户
     * 
     * @param transferAccount
     * @return
     */
    List<Map<String, Object>> findAllFinancialAffairsCardById(int transferAccount)
        throws Exception;
        
    /**
     * 根据refundId 和 step 获取记录
     * 
     * @param refundId
     * @param step
     * @return
     * @throws Exception
     */
    OrderProductRefundTeackEntity findRefundTeackByRefundIdAndStep(int refundId, int step)
        throws Exception;
        
    /**
     * 根据accountCardId查询退款银行账户信息
     * 
     * @param refundId
     * @return
     * @throws Exception
     */
    Map<String, Object> findRefundCardInfo(int accountCardId)
        throws Exception;
        
    /**
     * 保存退款退货
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> saveRefund(RefundEntity refund)
        throws Exception;
        
    String findGeGeJiaCardByRefundId(int refundId, int type)
        throws Exception;
        
    /**
     * 保存分摊信息
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> saveOrUpdateFinanceShare(Map<String, Object> para)
        throws Exception;
        
    /**
     * 
     * @创建人: zero
     * @创建时间: 2016年3月19日 下午2:04:24
     * @描述:
     *      <p>
     *      (左岸城堡自动退款导出)
     *      </p>
     * @修改人: zero
     * @修改时间: 2016年3月19日 下午2:04:24
     * @修改备注:
     *        <p>
     *        (修改备注)
     *        </p>
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     * @returnType List<MwebAutomaticRefundEntity>
     * @version V1.0
     */
    List<MwebAutomaticRefundEntity> findMwebAutomaticRefund(String startTime, String endTime)
        throws Exception;
        
    /**
     * 立即打款
     * 
     * @param refundId：退款id
     * @return
     * @throws Exception
     */
    ResultEntity dealRefundImmediately(int refundId, int cardId)
        throws Exception;
}
