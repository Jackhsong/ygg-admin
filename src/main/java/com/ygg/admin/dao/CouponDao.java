package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;

public interface CouponDao
{
    
    List<Map<String, Object>> queryCouponDetailInfo(Map<String, Object> para)
        throws Exception;
    
    int countCouponDetailInfo(Map<String, Object> para)
        throws Exception;
    
    int addCouponDetail(Map<String, Object> para)
        throws Exception;
    
    int updateCouponDetail(Map<String, Object> para)
        throws Exception;
    
    int countCouponDetailInUse(int couponDetailId)
        throws Exception;
    
    List<Map<String, Object>> queryCouponInfo(Map<String, Object> para)
        throws Exception;
    
    int countCouponInfo(Map<String, Object> para)
        throws Exception;
    
    int insertCoupon(CouponEntity coupon)
        throws Exception;
    
    int addCouponAccout(Map<String, Object> para)
        throws Exception;
    
    int insertRegisterMobileCoupon(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> queryCouponAccountInfo(Map<String, Object> para)
        throws Exception;
    
    int countCouponAccountInfo(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> queryCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception;
    
    int countCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception;
    
    List<Map<String, Object>> queryCouponAccount(Map<String, Object> para)
        throws Exception;
    
    CouponDetailEntity findCouponDetailById(int couponDetailId)
        throws Exception;
    
    int updateCouponDetailStatus(Map<String, Object> para)
        throws Exception;
    
    CouponEntity findCouponById(int couponId)
        throws Exception;
    
    String findCouponTotalMoney(int couponId)
        throws Exception;
    
    /**
     * 统计优惠券使用情况
     * @return
     * @throws Exception
     */
    int findCouponUsedInfo(int couponId)
        throws Exception;
    
    /**
     * 批量发送优惠券
     * @param addCouponAccoutList
     * @return
     */
    int batchAddCouponAccout(List<Map<String, Object>> couponList)
        throws Exception;
    
    /**
     * 批量预发放优惠券
     * @param insertRegisterMobileCouponList
     * @return
     */
    int batchInsertRegisterMobileCoupon(List<Map<String, Object>> couponList)
        throws Exception;
    
    /**
     * 更新优惠券发放的总数量
     * @param coupon
     * @return
     * @throws Exception
     */
    int updateCouponTotalNum(CouponEntity coupon)
        throws Exception;
    
    /**
     * 根据面额筛选随机优惠券Id
     * @param searchPara
     * @return
     * @throws Exception
     */
    List<Integer> findRandomCouponIdByPara(Map<String, Object> searchPara)
        throws Exception;
    
    /**
     * 根据面额筛选现金券Id
     * @param searchPara
     * @return
     * @throws Exception
     */
    List<Integer> findReduceCouponIdByPara(Map<String, Object> searchPara)
        throws Exception;
    
    Map<String, Object> findCouponAccountInfoByCouponAccountId(int id)
        throws Exception;

    List<Map<String, Object>> getUsedCouponOrderInfoByCouponId(Map<String, Object> para)
        throws Exception;
    
}
