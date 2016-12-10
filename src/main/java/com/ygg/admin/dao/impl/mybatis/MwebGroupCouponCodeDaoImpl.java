package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.MwebGroupCouponCodeDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.MwebGroupCouponCodeEntity;
import com.ygg.admin.entity.MwebGroupCouponDetailEntity;
import com.ygg.admin.exception.DaoException;

@Repository("mwebGroupCouponCodeDao")
public class MwebGroupCouponCodeDaoImpl extends BaseDaoImpl implements MwebGroupCouponCodeDao
{
    // *************************优惠码相关 begin************************
    @Override
    public String findCouponDetailDesc(int id)
        throws Exception
    {
        String desc = getSqlSession().selectOne("MwebGroupCouponCodeMapper.findCouponDetailDesc", id);
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
        return getSqlSession().selectList("MwebGroupCouponCodeMapper.findCouponDetailCode");
    }
    
    @Override
    public int insertCouponCode(MwebGroupCouponCodeEntity couponCode)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponCodeMapper.insertCouponCode", couponCode);
    }
    
    @Override
    public int updateCouponCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("MwebGroupCouponCodeMapper.updateCouponCode", para);
    }
    
    @Override
    public int insertCouponCodeDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponCodeMapper.insertCouponCodeDetail", para);
    }
    
    @Override
    public List<MwebGroupCouponCodeEntity> findAllCouponCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findAllCouponCode", para);
    }
    
    @Override
    public int countAllCouponCode(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.countAllCouponCode", para);
    }
    
    // @Override
    // public List<Map<String, Object>> findDetailByCouponCodeId(Map<String, Object> para)
    // throws Exception
    // {
    // return getSqlSession().selectList("MwebGroupCouponCodeMapper.findDetailByCouponCodeId", para);
    // }
    //
    // @Override
    // public int countDetailByCouponCodeId(Map<String, Object> para)
    // throws Exception
    // {
    // return getSqlSession().selectOne("MwebGroupCouponCodeMapper.countDetailByCouponCodeId", para);
    // }
    
    @Override
    public List<Map<String, Object>> findUsedNumsByCouponCodeId(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findUsedNumsByCouponCodeId", idList);
    }
    
    @Override
    public List<Map<String, Object>> findConvertNumsByCouponCodeIdWithTypeOne2One(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findConvertNumsByCouponCodeIdWithTypeOne2One", idList);
    }
    
    @Override
    public List<Map<String, Object>> findConvertNumsByCouponCodeIdWithTypeOne2Many(List<Integer> idList)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findConvertNumsByCouponCodeIdWithTypeOne2Many", idList);
    }
    
    @Override
    public MwebGroupCouponCodeEntity findCouponCodeById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        List<MwebGroupCouponCodeEntity> codes = findAllCouponCode(para);
        if (codes == null || codes.size() == 0)
            return null;
        return codes.get(0);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findCouponCodeCommonByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.countCouponCodeCommonByCouponCodeId", para);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeLiBaoCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findCouponCodeLiBaoCommonByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeLiBaoCommonByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.countCouponCodeLiBaoCommonByCouponCodeId", para);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findCouponCodeDetailByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.countCouponCodeDetailByCouponCodeId", para);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeLiBaoDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.findCouponCodeLiBaoDetailByCouponCodeId", para);
    }
    
    @Override
    public int countCouponCodeLiBaoDetailByCouponCodeId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.countCouponCodeLiBaoDetailByCouponCodeId", para);
    }
    
    @Override
    public MwebGroupCouponDetailEntity findCouponDetailById(int id)
        throws Exception
    {
        return getSqlSession().selectOne("MwebGroupCouponCodeMapper.findCouponDetailById", id);
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccountWithTypeOne2Many(Map<String, Object> para)
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.queryCouponAccountWithTypeOne2Many", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public List<Map<String, Object>> queryCouponAccountWithTypeOne2One(Map<String, Object> para)
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("MwebGroupCouponCodeMapper.queryCouponAccountWithTypeOne2One", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int queryCouponAccountId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.queryCouponAccountId", para);
    }
    
    @Override
    public int insertCouponCodeGiftBag(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("MwebGroupCouponCodeMapper.insertCouponCodeGiftBag", para);
    }
    
    @Override
    public List<MwebGroupCouponDetailEntity> findCouponDetailByCouponCodeId(int couponCodeId)
        throws Exception
    {
        return getSqlSession().selectList("MwebGroupCouponCodeMapper.findCouponDetailByCouponCodeId", couponCodeId);
    }
    
    @Override
    public List<Map<String, Object>> findCouponCodeGiftBag(int couponCodeId)
        throws DaoException
    {
        return getSqlSession().selectList("MwebGroupCouponCodeMapper.findCouponCodeGiftBag", couponCodeId);
    }
    
    @Override
    public String findCouponCodeTotoalMoney(String id)
        throws DaoException
    {
        return getSqlSessionRead().selectOne("MwebGroupCouponCodeMapper.findCouponCodeTotoalMoney", id);
    }
    // *************************优惠码相关 end************************
}
