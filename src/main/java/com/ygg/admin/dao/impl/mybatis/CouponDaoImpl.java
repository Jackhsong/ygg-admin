package com.ygg.admin.dao.impl.mybatis;

import com.ygg.admin.dao.CouponDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("couponDao")
public class CouponDaoImpl extends BaseDaoImpl implements CouponDao
{
    @Override
    public int countCouponDetailInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.countCouponDetailInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponDetailInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponMapper.queryCouponDetailInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int addCouponDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CouponMapper.addCouponDetail", para);
    }
    
    @Override
    public int updateCouponDetail(Map<String, Object> para)
        throws Exception
    {
        
        return getSqlSession().update("CouponMapper.updateCouponDetail", para);
    }
    
    @Override
    public int countCouponDetailInUse(int couponDetailId)
        throws Exception
    {
        int count1 = getSqlSessionRead().selectOne("CouponMapper.countCouponDetailFromCoupon", couponDetailId);
        int count2 = getSqlSessionRead().selectOne("CouponMapper.countCouponDetailFromCouponCode", couponDetailId);
        return count1 + count2;
    }
    
    @Override
    public int countCouponInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.countCouponInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponMapper.queryCouponInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int insertCoupon(CouponEntity coupon)
        throws Exception
    {
        return getSqlSession().insert("CouponMapper.insertCoupon", coupon);
    }
    
    @Override
    public int addCouponAccout(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CouponMapper.addCouponAccout", para);
    }
    
    @Override
    public int insertRegisterMobileCoupon(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CouponMapper.insertRegisterMobileCoupon", para);
    }
    
    @Override
    public int countCouponAccountInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.countCouponAccountInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccountInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponMapper.queryCouponAccountInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.countCouponOrderDetailInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponMapper.queryCouponOrderDetailInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccount(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponMapper.queryCouponAccount", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public CouponDetailEntity findCouponDetailById(int couponDetailId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.findCouponDetailById", couponDetailId);
    }
    
    @Override
    public int updateCouponDetailStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().delete("CouponMapper.updateCouponDetailStatus", para);
    }
    
    @Override
    public CouponEntity findCouponById(int couponId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.findCouponById", couponId);
    }
    
    @Override
    public String findCouponTotalMoney(int couponId)
        throws Exception
    {
        Float totalMoney = getSqlSessionRead().selectOne("CouponMapper.findCouponTotalMoney", couponId);
        return totalMoney == null ? "0" : totalMoney.floatValue() + "";
    }
    
    @Override
    public int findCouponUsedInfo(int couponId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponMapper.findCouponUsedInfo", couponId);
    }
    
    @Override
    public int batchAddCouponAccout(List<Map<String, Object>> couponList)
        throws Exception
    {
        return this.getSqlSession().insert("CouponMapper.batchAddCouponAccout", couponList);
    }
    
    @Override
    public int batchInsertRegisterMobileCoupon(List<Map<String, Object>> couponList)
        throws Exception
    {
        return this.getSqlSession().insert("CouponMapper.batchInsertRegisterMobileCoupon", couponList);
    }
    
    @Override
    public int updateCouponTotalNum(CouponEntity coupon)
        throws Exception
    {
        return this.getSqlSession().update("CouponMapper.updateCouponTotalNum", coupon);
    }
    
    @Override
    public List<Integer> findRandomCouponIdByPara(Map<String, Object> searchPara)
        throws Exception
    {
        List<Integer> reList = this.getSqlSessionRead().selectList("CouponMapper.findRandomCouponIdByPara", searchPara);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public List<Integer> findReduceCouponIdByPara(Map<String, Object> searchPara)
        throws Exception
    {
        List<Integer> reList = this.getSqlSessionRead().selectList("CouponMapper.findReduceCouponIdByPara", searchPara);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public Map<String, Object> findCouponAccountInfoByCouponAccountId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("CouponMapper.findReducePriceByCouponAccountId", id);
    }

    @Override
    public List<Map<String, Object>> getUsedCouponOrderInfoByCouponId(Map<String, Object> para) throws Exception {
        return getSqlSessionRead().selectList("CouponMapper.getUsedCouponOrderInfoByCouponId", para);
    }
}
