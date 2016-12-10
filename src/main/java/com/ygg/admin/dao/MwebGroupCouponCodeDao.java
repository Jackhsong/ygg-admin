package com.ygg.admin.dao;

import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.MwebGroupCouponCodeEntity;
import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.exception.DaoException;

public interface MwebGroupCouponCodeDao
{
    
    // *************************优惠码相关 begin************************
    /**
     * 根据id查询优惠券desc
     * 
     * @param id
     * @return
     * @throws Exception
     */
    String findCouponDetailDesc(int id)
        throws Exception;
        
    /**
     * 根据优惠券code
     * 
     * @return
     * @throws Exception
     */
    List<String> findCouponDetailCode()
        throws Exception;
        
    /**
     * 插入优惠码
     * 
     * @param couponCode
     * @return
     * @throws Exception
     */
    int insertCouponCode(MwebGroupCouponCodeEntity couponCode)
        throws Exception;
        
    /**
     * 更新优惠码
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int updateCouponCode(Map<String, Object> para)
        throws Exception;
        
    /**
     * 插入优惠码详情
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertCouponCodeDetail(Map<String, Object> para)
        throws Exception;
        
    /**
     * 查询优惠码列表
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<MwebGroupCouponCodeEntity> findAllCouponCode(Map<String, Object> para)
        throws Exception;
        
    /**
     * 统计优惠码数量
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int countAllCouponCode(Map<String, Object> para)
        throws Exception;
        
    // /**
    // * 查询优惠码详情
    // *
    // * @param para
    // * @return
    // * @throws Exception
    // */
    // List<Map<String, Object>> findDetailByCouponCodeId(Map<String, Object> para)
    // throws Exception;
    //
    // /**
    // * 统计某优惠码下详情数量
    // *
    // * @param para
    // * @return
    // * @throws Exception
    // */
    // int countDetailByCouponCodeId(Map<String, Object> para)
    // throws Exception;
    
    /**
     * 优惠码实际使用情况
     * 
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findUsedNumsByCouponCodeId(List<Integer> idList)
        throws Exception;
        
    /**
     * 1对1 优惠码实际兑换情况
     * 
     * @param idList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findConvertNumsByCouponCodeIdWithTypeOne2One(List<Integer> idList)
        throws Exception;
        
    /**
     * 1对多 优惠码实际兑换情况
     * 
     * @param idList
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findConvertNumsByCouponCodeIdWithTypeOne2Many(List<Integer> idList)
        throws Exception;
        
    /**
     * 根据ID查询优惠码
     * 
     * @param para
     * @return
     * @throws Exception
     */
    MwebGroupCouponCodeEntity findCouponCodeById(int id)
        throws Exception;
        
    /**
     * 根据优惠码ID(类型为1对多)查询所有已经兑换信息
     * 
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findCouponCodeCommonByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    int countCouponCodeCommonByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    List<Map<String, Object>> findCouponCodeLiBaoCommonByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    int countCouponCodeLiBaoCommonByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据优惠码ID(类型为1对1)查询所有已经兑换信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findCouponCodeDetailByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    int countCouponCodeDetailByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    List<Map<String, Object>> findCouponCodeLiBaoDetailByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    int countCouponCodeLiBaoDetailByCouponCodeId(Map<String, Object> para)
        throws Exception;
        
    /**
     * 根据id查询coupon_detail
     * 
     * @param id
     * @return
     * @throws Exception
     */
    MwebGroupCouponDetailEntity findCouponDetailById(int id)
        throws Exception;
        
    /**
     * 1对1 优惠码订单使用情况
     * 
     * @param para
     * @return
     */
    List<Map<String, Object>> queryCouponAccountWithTypeOne2One(Map<String, Object> para);
    
    /**
     * 1对n 优惠码订单使用情况
     * 
     * @param para
     * @return
     */
    List<Map<String, Object>> queryCouponAccountWithTypeOne2Many(Map<String, Object> para);
    
    /**
     * 获取用户优惠Id
     * 
     * @param para
     * @return
     */
    int queryCouponAccountId(Map<String, Object> para)
        throws Exception;
        
    /**
     * 插入优惠券礼包
     * 
     * @param para
     * @return
     * @throws Exception
     */
    int insertCouponCodeGiftBag(Map<String, Object> para)
        throws Exception;
        
    /**
     * 查询优惠码关联的所有优惠券详情信息
     * 
     * @param couponCodeId
     * @return
     * @throws Exception
     */
    List<MwebGroupCouponDetailEntity> findCouponDetailByCouponCodeId(int couponCodeId)
        throws Exception;
        
    /**
     * 根据优惠码ID查询关联的优惠券列表
     * 
     * @param couponCodeId
     * @return
     * @throws DaoException
     */
    List<Map<String, Object>> findCouponCodeGiftBag(int couponCodeId)
        throws DaoException;
        
    /**
     * 统计优惠码带来订单金额
     * 
     * @param id
     * @return
     * @throws DaoException
     */
    String findCouponCodeTotoalMoney(String id)
        throws DaoException;
        
    // *************************优惠码相关 end************************
}
