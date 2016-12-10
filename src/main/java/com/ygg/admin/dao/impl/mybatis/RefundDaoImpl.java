package com.ygg.admin.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ygg.admin.entity.*;
import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.RefundDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;

@Repository("refundDao")
public class RefundDaoImpl extends BaseDaoImpl implements RefundDao
{
    @Override
    public int saveRefund(RefundEntity refund)
        throws Exception
    {
        return getSqlSession().insert("RefundMapper.saveRefund", refund);
    }
    
    @Override
    public List<Integer> findAllOrderIdsByUserInfo(Map<String, Object> para)
        throws Exception
    {
        List<Integer> reList = getSqlSessionRead().selectList("RefundMapper.findAllOrderIdsByUserInfo", para);
        return reList == null ? new ArrayList<Integer>() : reList;
    }
    
    @Override
    public List<RefundEntity> findAllRefundByPara(Map<String, Object> para)
        throws Exception
    {
        List<RefundEntity> reList = getSqlSession().selectList("RefundMapper.findAllRefundByPara", para);
        return reList == null ? new ArrayList<RefundEntity>() : reList;
    }
    
    @Override
    public int countAllRefundByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.countAllRefundByPara", para);
    }
    
    @Override
    public Map<String, Map<String, Object>> findAllRefundIsReceiveStatusByIds(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSession().selectList("RefundMapper.findAllRefundIsReceiveStatusByIds", para);
        if (reList == null || reList.size() < 1)
        {
            return new HashMap<>();
        }
        // refundId为key ，放入map
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Map<String, Object> currMap : reList)
        {
            String id = currMap.get("refundId") + "";
            result.put(id, currMap);
        }
        return result;
    }
    
    @Override
    public Map<String, Map<String, Object>> findAllOrderProductInfoByIds(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSession().selectList("RefundMapper.findAllOrderProductInfoByIds", para);
        if (reList == null || reList.size() < 1)
        {
            return null;
        }
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        // id为key ，放入map
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> currMap : reList)
        {
            String id = currMap.get("id") + "";
            result.put(id, currMap);
        }
        return result;
    }
    
    @Override
    public Map<String, Map<String, Object>> findAllOrderReceiveInfoByIds(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSession().selectList("RefundMapper.findAllOrderReceiveInfoByIds", para);
        if (reList == null || reList.size() < 1)
        {
            return null;
        }
        // orderId为key ，放入map
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> currMap : reList)
        {
            String id = currMap.get("orderId") + "";
            result.put(id, currMap);
        }
        return result;
    }
    
    @Override
    public RefundEntity findRefundById(int id)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("id", id);
        List<RefundEntity> reList = findAllRefundByPara(para);
        if (reList.size() > 0)
        {
            return reList.get(0);
        }
        return null;
    }
    
    @Override
    public Map<String, Object> findOrderProductInfoByOrderProductId(int id)
    {
        Map<String, Object> map = getSqlSession().selectOne("RefundMapper.findOrderProductInfoByOrderProductId", id);
        int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
        if (activityType == 1)
        {
            double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
            map.put("salesPrice", groupPrice);
        }
        return map;
    }
    
    @Override
    public Map<String, Object> findAccountCardById(int id)
    {
        return getSqlSessionRead().selectOne("RefundMapper.findAccountCardById", id);
    }
    
    @Override
    public int updateRefund(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("RefundMapper.updateRefund", para);
    }
    
    @Override
    public int updateOrderProductRefundTeack(OrderProductRefundTeackEntity entity)
        throws Exception
    {
        return getSqlSession().update("RefundMapper.updateOrderProductRefundTeack", entity);
    }
    
    @Override
    public int saveOrderProductRefundTeack(OrderProductRefundTeackEntity entity)
        throws Exception
    {
        return getSqlSession().insert("RefundMapper.saveOrderProductRefundTeack", entity);
    }
    
    @Override
    public List<OrderProductRefundTeackEntity> findOrderProductRefundTeack(Map<String, Object> para)
        throws Exception
    {
        List<OrderProductRefundTeackEntity> reList = getSqlSession().selectList("RefundMapper.findOrderProductRefundTeack", para);
        return reList == null ? new ArrayList<OrderProductRefundTeackEntity>() : reList;
    }
    
    @Override
    public int saveFinancialAffairsCard(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().insert("RefundMapper.saveFinancialAffairsCard", para);
    }
    
    @Override
    public int deleteFinancialAffairsCardByIds(List<Integer> idList)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idList", idList);
        return getSqlSessionAdmin().delete("RefundMapper.deleteFinancialAffairsCardById", map);
    }
    
    @Override
    public List<Map<String, Object>> findAllFinancialAffairsCard(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionAdmin().selectList("RefundMapper.findAllFinancialAffairsCard", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int countAllFinancialAffairsCard(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectOne("RefundMapper.countAllFinancialAffairsCard", para);
    }
    
    @Override
    public Map<String, Object> findRefundLogisticsByRefundId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.findRefundLogisticsByRefundId", id);
    }
    
    @Override
    public int saveRefundLogistics(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("RefundMapper.saveRefundLogistics", para);
    }
    
    @Override
    public List<Map<String, Object>> findRefundLogisticsByPara(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSession().selectList("RefundMapper.findRefundLogisticsByPara", para);
        return reList == null ? new ArrayList<Map<String, Object>>() : reList;
    }
    
    @Override
    public int updateRefundLogistics(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("RefundMapper.updateRefundLogistics", para);
    }
    
    @Override
    public List<Map<String, Object>> findAllFinancialAffairsCardById(int transferAccount)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("RefundMapper.findAllFinancialAffairsCardById", transferAccount);
    }

    @Override
    public FinancialAffairsCardEntity findFinancialAffairsCardById(int id) throws Exception {
        return getSqlSessionAdmin().selectOne("RefundMapper.findFinancialAffairsCardById", id);
    }

    @Override
    public RefundProportionEntity findRefundProportionByRefundId(int refundId)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.findRefundProportionByRefundId", refundId);
    }
    
    @Override
    public int updateRefundProportionByRefundId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().update("RefundMapper.updateRefundProportionByRefundId", para);
    }
    
    @Override
    public int saveRefundProportionByRefundId(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().insert("RefundMapper.saveRefundProportionByRefundId", para);
    }
    
    @Override
    public Map<String, Object> findOrderProductCostById(int id)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.findOrderProductCostById", id);
    }
    
    @Override
    public List<RefundProportionEntity> findAllRefundProportionByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("RefundMapper.findAllRefundProportionByPara", para);
    }
    
    @Override
    public int findOtherNotExistsRefund(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.findOtherNotExistsRefund", para);
    }
    
    @Override
    public int findOtherNotExistsRefundForCancelOrder(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.findOtherNotExistsRefundForCancelOrder", para);
    }
    
    @Override
    public int findOtherNotExistsRefundForCancelOrderForStep1(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.findOtherNotExistsRefundForCancelOrderForStep1", para);
    }
    
    @Override
    public int countRefundByOrderProductId(int id)
        throws Exception
    {
        return getSqlSession().selectOne("RefundMapper.countRefundByOrderProductId", id);
    }
    
    @Override
    public List<Map<String, Object>> findRefundForEveryday(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("RefundMapper.findRefundForEveryday", param);
    }
    
    @Override
    public List<Map<String, Object>> findRefundSellerIdForSeller(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("RefundMapper.findRefundSellerIdForSeller", param);
    }
    
    @Override
    public int countRefundSellerIdForSeller(Map<String, Object> param)
        throws Exception
    {
        Integer result = getSqlSessionRead().selectOne("RefundMapper.countRefundSellerIdForSeller", param);
        return result == null ? 0 : result;
    }
    
    @Override
    public List<Map<String, Object>> findRefundForSeller(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("RefundMapper.findRefundForSeller", param);
    }
    
    @Override
    public List<MwebAutomaticRefundEntity> findMwebAutomaticRefund(Map<String, Object> param)
        throws Exception
    {
        return getSqlSessionRead().selectList("RefundMapper.findMwebAutomaticRefund", param);
    }
}
