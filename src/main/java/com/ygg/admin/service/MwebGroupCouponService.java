package com.ygg.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.entity.MwebGroupCouponEntity;

public interface MwebGroupCouponService
{
    Map<String, Object> jsonCouponDetailInfo(Map<String, Object> para)
        throws Exception;
        
    int saveOrUpdate(Map<String, Object> para)
        throws Exception;
        
    boolean checkIsInUse(int couponDetailId)
        throws Exception;
        
    int updateCouponDetailStatus(int id, int isAvailable)
        throws Exception;
        
    Map<String, Object> jsonCouponInfo(Map<String, Object> para)
        throws Exception;
        
    List<Map<String, Object>> queryAllCouponType(int isAvailable, boolean needRandomReduce, int id)
        throws Exception;
        
    int insertCoupon(MwebGroupCouponEntity coupon)
        throws Exception;
        
    int addCouponAccout(Map<String, Object> para)
        throws Exception;
        
    /**
     * 向用户发送优惠券
     * 
     * @param coupon:优惠券
     * @param file:批量导入文件，当operType=1时，文件中只含有accountId;当operType=2时，文件中只含有name并且name都为手机号;当operType=3时，file=null
     * @param operType:操作类型，operType=1批量向accoutId发放优惠券;operType=2批量向name发放优惠券;operType=3向单个accoutId发放优惠券
     * @param accountId:当operType=3时有效
     * @return
     * @throws Exception
     */
    int sendCoupon(MwebGroupCouponEntity coupon, MultipartFile file, int operType, Integer accountId)
        throws Exception;
        
    Map<String, Object> jsonCouponAccountInfo(Map<String, Object> para)
        throws Exception;
        
    Map<String, Object> jsonCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception;
        
    Map<String, Object> queryCouponAccount(Map<String, Object> para)
        throws Exception;
        
    MwebGroupCouponDetailEntity findCouponDetailById(int couponDetailId)
        throws Exception;
        
    MwebGroupCouponEntity findCouponById(int couponId)
        throws Exception;
        
    /**
     * 统计优惠券带来交易额
     * 
     * @param couponId
     * @return
     */
    String findCouponTotalMoney(int couponId)
        throws Exception;
        
    /**
     * 查询优惠券描述信息
     * 
     * @return
     * @throws Exception
     */
    Map<String, Object> findCouponInfoByCouponAccountId(int id)
        throws Exception;
        
    int findCouponUsedInfo(int couponId)
        throws Exception;
}
