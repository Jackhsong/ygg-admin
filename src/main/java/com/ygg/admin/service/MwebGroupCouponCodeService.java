package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

public interface MwebGroupCouponCodeService
{
    
    /**
     * 新增优惠码
     * 
     * @param couponDetailId 优惠券类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param remark 备注
     * @param nums 生成数量
     * @param customCode 自定义优惠码
     * @return
     * @throws Exception
     */
    Map<String, Object> addCouponCode(List<Map<String, Object>> couponDetailIdAndCountList, String startTime, String endTime, String remark, int nums, int type, String customCode)
        throws Exception;
    
    /**
     * 查询优惠码列表
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> findCouponCode(Map<String, Object> para)
        throws Exception;
    
    /**
     * 更新优惠码可用状态
     * 
     * @param id
     * @param isAvailable
     * @return
     * @throws Exception
     */
    Map<String, Object> updateCouponAvailable(int id, byte isAvailable)
        throws Exception;
    
    /**
     * 查询优惠码 详情列表
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> findCouponCodeDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询优惠码 礼包 详情列表
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> findCouponCodeLiBaoDetail(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询优惠码
     * 
     * @param para
     * @return
     * @throws Exception
     */
    Map<String, Object> queryCouponAccount(Map<String, Object> para)
        throws Exception;
    
    /**
     * 查询优惠码礼包信息
     * @param couponCodeId
     * @return
     * @throws Exception
     */
    Map<String, Object> findCouponCodeLiBaoInfo(int couponCodeId)
        throws Exception;
    
    /**
     * 统计优惠码带来订单金额
     * @param id
     * @return
     * @throws Exception
     */
    String findCouponCodeTotoalMoney(String id)
        throws Exception;
}
