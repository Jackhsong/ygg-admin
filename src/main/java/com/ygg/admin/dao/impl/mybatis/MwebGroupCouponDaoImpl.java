package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MwebGroupCouponDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.entity.MwebGroupCouponEntity;

@Repository("mwebGroupCouponDao")
public class MwebGroupCouponDaoImpl extends BaseDaoImpl implements MwebGroupCouponDao
{
    @Override
    public int countCouponDetailInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.countCouponDetailInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponDetailInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponMapper.queryCouponDetailInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int addCouponDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponMapper.addCouponDetail", para);
    }
    
    @Override
    public int updateCouponDetail(Map<String, Object> para)
        throws Exception
    {
        
        return getSqlSession().update("MwebGroupCouponMapper.updateCouponDetail", para);
    }
    
    @Override
    public int countCouponDetailInUse(int couponDetailId)
        throws Exception
    {
        int count1 = getSqlSessionRead().selectOne("MwebGroupCouponMapper.countCouponDetailFromCoupon", couponDetailId);
        int count2 = getSqlSessionRead().selectOne("MwebGroupCouponMapper.countCouponDetailFromCouponCode", couponDetailId);
        return count1 + count2;
    }
    
    @Override
    public int countCouponInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.countCouponInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponMapper.queryCouponInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int insertCoupon(MwebGroupCouponEntity coupon)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponMapper.insertCoupon", coupon);
    }
    
    @Override
    public int addCouponAccout(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponMapper.addCouponAccout", para);
    }
    
    @Override
    public int insertRegisterMobileCoupon(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponMapper.insertRegisterMobileCoupon", para);
    }
    
    @Override
    public int countCouponAccountInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.countCouponAccountInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccountInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponMapper.queryCouponAccountInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.countCouponOrderDetailInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponOrderDetailInfo(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponMapper.queryCouponOrderDetailInfo", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccount(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponMapper.queryCouponAccount", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public MwebGroupCouponDetailEntity findCouponDetailById(int couponDetailId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.findCouponDetailById", couponDetailId);
    }
    
    @Override
    public int updateCouponDetailStatus(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().delete("MwebGroupCouponMapper.updateCouponDetailStatus", para);
    }
    
    @Override
    public MwebGroupCouponEntity findCouponById(int couponId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.findCouponById", couponId);
    }
    
    @Override
    public String findCouponTotalMoney(int couponId)
        throws Exception
    {
        Float totalMoney = getSqlSessionRead().selectOne("MwebGroupCouponMapper.findCouponTotalMoney", couponId);
        return totalMoney == null ? "0" : totalMoney.floatValue() + "";
    }
    
    @Override
    public int findCouponUsedInfo(int couponId)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponMapper.findCouponUsedInfo", couponId);
    }
    
    @Override
    public int batchAddCouponAccout(List<Map<String, Object>> couponList)
        throws Exception
    {
        return this.getSqlSession().insert("MwebGroupCouponMapper.batchAddCouponAccout", couponList);
    }
    
    @Override
    public int batchInsertRegisterMobileCoupon(List<Map<String, Object>> couponList)
        throws Exception
    {
        return this.getSqlSession().insert("MwebGroupCouponMapper.batchInsertRegisterMobileCoupon", couponList);
    }
    
    @Override
    public int updateCouponTotalNum(MwebGroupCouponEntity coupon)
        throws Exception
    {
        return this.getSqlSession().update("MwebGroupCouponMapper.updateCouponTotalNum", coupon);
    }
    
    @Override
    public List<Integer> findRandomCouponIdByPara(Map<String, Object> searchPara)
        throws Exception
    {
        List<Integer> reList = this.getSqlSessionRead().selectList("MwebGroupCouponMapper.findRandomCouponIdByPara", searchPara);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public List<Integer> findReduceCouponIdByPara(Map<String, Object> searchPara)
        throws Exception
    {
        List<Integer> reList = this.getSqlSessionRead().selectList("MwebGroupCouponMapper.findReduceCouponIdByPara", searchPara);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public Map<String, Object> findCouponAccountInfoByCouponAccountId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("MwebGroupCouponMapper.findReducePriceByCouponAccountId", id);
    }
}
