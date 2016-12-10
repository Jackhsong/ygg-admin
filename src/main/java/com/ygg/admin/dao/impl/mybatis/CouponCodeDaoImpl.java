package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.CouponCodeDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.CouponCodeEntity;
import com.ygg.admin.entity.CouponDetailEntity;
import com.ygg.admin.exception.DaoException;

@Repository("couponCodeDao")
public class CouponCodeDaoImpl extends BaseDaoImpl implements CouponCodeDao
{
    // *************************优惠码相关 begin************************
    @Override
    public String findCouponDetailDesc(int id)
        throws Exception
    {
        String desc = getSqlSession().selectOne("CouponCodeMapper.findCouponDetailDesc", id);
        if (desc == null)
        {
            return null;
        }
        return desc;
    }
    
    @Override
    public List<String> findCouponDetailCode()
        throws Exception
    {
        return getSqlSession().selectList("CouponCodeMapper.findCouponDetailCode");
    }
    
    @Override
    public int insertCouponCode(CouponCodeEntity couponCode)
        throws Exception
    {
        return getSqlSession().insert("CouponCodeMapper.insertCouponCode", couponCode);
    }
    
    @Override
    public int updateCouponCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("CouponCodeMapper.updateCouponCode", para);
    }
    
    @Override
    public int insertCouponCodeDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CouponCodeMapper.insertCouponCodeDetail", para);
    }
    
    @Override
    public List<CouponCodeEntity> findAllCouponCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findAllCouponCode", para);
    }
    
    @Override
    public int countAllCouponCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.countAllCouponCode", para);
    }
    
    // @Override
    // public List<Map<String, Object>> findDetailByCouponCodeId(Map<String, Object> para)
    // throws Exception
    // {
    // return getSqlSession().selectList("CouponCodeMapper.findDetailByCouponCodeId", para);
    // }
    //
    // @Override
    // public int countDetailByCouponCodeId(Map<String, Object> para)
    // throws Exception
    // {
    // return getSqlSession().selectOne("CouponCodeMapper.countDetailByCouponCodeId", para);
    // }
    
    @Override
    public List<Map<String, Object>> findUsedNumsByCouponCodeId(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findUsedNumsByCouponCodeId", idList);
    }
    
    @Override
    public List<Map<String, Object>> findConvertNumsByCouponCodeIdWithTypeOne2One(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findConvertNumsByCouponCodeIdWithTypeOne2One", idList);
    }
    
    @Override
    public List<Map<String, Object>> findConvertNumsByCouponCodeIdWithTypeOne2Many(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findConvertNumsByCouponCodeIdWithTypeOne2Many", idList);
    }
    
    @Override
    public CouponCodeEntity findCouponCodeById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        List<CouponCodeEntity> codes = findAllCouponCode(para);
        if (codes == null || codes.size() == 0)
            return null;
        return codes.get(0);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findCouponCodeCommonByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.countCouponCodeCommonByCouponCodeId", para);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeLiBaoCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findCouponCodeLiBaoCommonByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeLiBaoCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.countCouponCodeLiBaoCommonByCouponCodeId", para);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findCouponCodeDetailByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.countCouponCodeDetailByCouponCodeId", para);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeLiBaoDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("CouponCodeMapper.findCouponCodeLiBaoDetailByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeLiBaoDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.countCouponCodeLiBaoDetailByCouponCodeId", para);
    }
    
    @Override
    public CouponDetailEntity findCouponDetailById(int id)
        throws Exception
    {
        return getSqlSession().selectOne("CouponCodeMapper.findCouponDetailById", id);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccountWithTypeOne2Many(Map<String, Object> para)
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponCodeMapper.queryCouponAccountWithTypeOne2Many", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccountWithTypeOne2One(Map<String, Object> para)
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("CouponCodeMapper.queryCouponAccountWithTypeOne2One", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int queryCouponAccountId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.queryCouponAccountId", para);
    }
    
    @Override
    public int insertCouponCodeGiftBag(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("CouponCodeMapper.insertCouponCodeGiftBag", para);
    }
    
    @Override
    public List<CouponDetailEntity> findCouponDetailByCouponCodeId(int couponCodeId)
        throws Exception
    {
        return getSqlSession().selectList("CouponCodeMapper.findCouponDetailByCouponCodeId", couponCodeId);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeGiftBag(int couponCodeId)
        throws DaoException
    {
        return getSqlSession().selectList("CouponCodeMapper.findCouponCodeGiftBag", couponCodeId);
    }
    
    @Override
    public String findCouponCodeTotoalMoney(String id)
        throws DaoException
    {
        return getSqlSessionRead().selectOne("CouponCodeMapper.findCouponCodeTotoalMoney", id);
    }
    // *************************优惠码相关 end************************
}
