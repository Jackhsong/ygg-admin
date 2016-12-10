package com.ygg.admin.dao.impl.mybatis;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ygg.admin.dao.FinanceDao;
import com.ygg.admin.dao.OrderDao;
import com.ygg.admin.dao.OrderManualDao;
import com.ygg.admin.dao.impl.mybatis.base.BaseDaoImpl;
import com.ygg.admin.entity.OrderListDetailView;
import com.ygg.admin.entity.OrderListView;
import com.ygg.admin.util.CommonUtil;
import com.ygg.admin.util.StringUtils;

@Repository("financeDao")
public class FinanceDaoImpl extends BaseDaoImpl implements FinanceDao
{
    
    @Resource
    private OrderDao orderDao;
    
    @Resource
    private OrderManualDao orderManualDao;
    
    @Override
    public boolean existsOrderNumber(String number)
        throws Exception
    {
        if (!StringUtils.isNumeric(number))
        {
            return false;
        }
        int orderId = -1;
        int nType = CommonUtil.estimateOrderNumber(number);
        if (nType == 1)
        {
            // 手工订单
            orderId = orderManualDao.findOrderManualIdByNumber(Long.valueOf(number));
        }
        else
        {
            // 正常订单
            orderId = orderDao.findOrderIdByNumber(Long.valueOf(number));
        }
        if (orderId == -1)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isSettlement(String number)
        throws Exception
    {
        int isSettlement = 0;
        Map<String, Object> reMap = null;
        int nType = CommonUtil.estimateOrderNumber(number);
        if (nType == 1)
        {
            // 手工订单
            reMap = orderManualDao.findOrderManualSettlementByNumber(Long.valueOf(number));
        }
        else
        {
            // 正常订单
            reMap = orderDao.findOrderSettlementByNumber(Long.valueOf(number));
        }
        if (reMap != null)
        {
            int postageIsSettlement = Integer.parseInt(reMap.get("postageIsSettlement") == null ? "0" : reMap.get("postageIsSettlement") + "");
            if (postageIsSettlement == 1)
            {
                isSettlement = 1;
            }
        }
        return isSettlement == 1 ? true : false;
    }
    
    @Override
    public int insertOrderSettlement(long number, String freightMoney, String date)
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("postageIsSettlement", 1);
        para.put("freightMoney", freightMoney);
        para.put("postageComfirmDate", date);
        int returnStatus = 0;
        int nType = CommonUtil.estimateOrderNumber(String.valueOf(number));
        if (nType == 1)
        {
            // 手工订单
            int orderId = orderManualDao.findOrderManualIdByNumber(Long.valueOf(number));
            para.put("orderMmanualId", orderId);
            returnStatus = orderManualDao.insertOrderManualSettlement(para);
        }
        else
        {
            // 正常订单
            int orderId = orderDao.findOrderIdByNumber(Long.valueOf(number));
            para.put("orderId", orderId);
            returnStatus = orderDao.insertOrderSettlement(para);
        }
        return returnStatus;
    }
    
    @Override
    public int deleteOrderSettlement(long number)
        throws Exception
    {
        int returnStatus = 0;
        int nType = CommonUtil.estimateOrderNumber(String.valueOf(number));
        if (nType == 1)
        {
            // 手工订单
            int orderId = orderManualDao.findOrderManualIdByNumber(Long.valueOf(number));
            returnStatus = orderManualDao.deleteOrderManualSettlement(orderId);
        }
        else
        {
            // 正常订单
            int orderId = orderDao.findOrderIdByNumber(Long.valueOf(number));
            returnStatus = orderDao.deleteOrderSettlement(orderId);
        }
        return returnStatus;
    }
    
    @Override
    public List<OrderListView> findOrderInfo(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findOrderInfo", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderSettlementByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findOrderSettlementByPara", para);
    }
    
    @Override
    public List<Integer> findOrderIdFromRefundByPara(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findOrderIdFromRefundByPara", para);
    }
    
    @Override
    public List<OrderListView> findOrderInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findOrderInfoHasOrderSettlement", para);
    }
    
    @Override
    public int countOrderInfo(Map<String, Object> para)
        throws Exception
    {
        Integer total = getSqlSessionRead().selectOne("FinanceMapper.countOrderInfo", para);
        return total == null ? 0 : total;
    }
    
    @Override
    public int countOrderInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception
    {
        Integer total = getSqlSessionRead().selectOne("FinanceMapper.countOrderInfoHasOrderSettlement", para);
        return total == null ? 0 : total;
    }
    
    @Override
    public List<OrderListView> findOrderManualInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("FinanceMapper.findOrderManualInfoHasOrderSettlement", para);
    }
    
    @Override
    public int countOrderManualInfoHasOrderSettlement(Map<String, Object> para)
        throws Exception
    {
        Integer total = getSqlSessionAdmin().selectOne("FinanceMapper.countOrderManualInfoHasOrderSettlement", para);
        return total == null ? 0 : total;
    }
    
    @Override
    public List<OrderListDetailView> findOrderInfoDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findOrderInfoDetail", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderProductInfoDetail(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("FinanceMapper.findOrderProductInfoDetail", para);
        for (Map<String, Object> map : reList)
        {
            double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }

    @Override
    public List<Map<String, Object>> findOrderHQBSInfoDetail(List<Integer> idList)
        throws Exception
    {
        if (idList.size() == 0)
        {
            return new ArrayList<>();
        }
        return getSqlSessionRead().selectList("FinanceMapper.findOrderHQBSInfoDetail", idList);
    }

    @Override
    public List<Map<String, Object>> findOrderRefundInfoDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findOrderRefundInfoDetail", para);
    }
    
    @Override
    public List<OrderListDetailView> findOrderManualInfoDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionAdmin().selectList("FinanceMapper.findOrderManualInfoDetail", para);
    }
    
    @Override
    public List<Map<String, Object>> findProductInfoDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findProductInfoDetail", para);
    }
    
    @Override
    public List<Map<String, Object>> findProductBaseInfoDetail(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findProductBaseInfoDetail", para);
    }
    
    @Override
    public int countOrderInfoDetail(Map<String, Object> para)
        throws Exception
    {
        Integer total = getSqlSessionRead().selectOne("FinanceMapper.countOrderInfoDetail", para);
        return total == null ? 0 : total;
    }
    
    @Override
    public int countOrderManualInfoDetail(Map<String, Object> para)
        throws Exception
    {
        Integer total = getSqlSessionAdmin().selectOne("FinanceMapper.countOrderManualInfoDetail", para);
        return total == null ? 0 : total;
    }
    
    @Override
    public List<Map<String, Object>> findSellerSettlementStatistics(Map<String, Object> para)
        throws Exception
    {
        return getSqlSessionRead().selectList("FinanceMapper.findSellerSettlementStatistics", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderProductInfoForSellerSettlement(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("FinanceMapper.findOrderProductInfoForSellerSettlement", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    @Override
    public List<Map<String, Object>> findSellerRefundSettlementStatistics(Map<String, Object> para)
        throws Exception
    {
        List<Map<String, Object>> reList = getSqlSessionRead().selectList("FinanceMapper.findSellerRefundSettlementStatistics", para);
        for (Map<String, Object> map : reList)
        {
            int activityType = Integer.parseInt(map.get("isGroup") == null ? "0" : map.get("isGroup") + "");
            if (activityType == 1)
            {
                double groupPrice = Double.parseDouble(map.get("groupPrice") + "");
                map.put("salesPrice", groupPrice);
            }
        }
        return reList;
    }
    
    @Override
    public List<Integer> findOrderProductIdByIdAndOrderIdNotInList(Map<String, Object> para)
        throws Exception
    {
        return getSqlSession().selectList("FinanceMapper.findOrderProductIdByIdAndOrderIdNotInList", para);
    }
    
    @Override
    public String existsOrderProductByNumberAndProductId(long number, int productId)
        throws Exception
    {
        int nType = CommonUtil.estimateOrderNumber(String.valueOf(number));
        if (nType == 1)
        {
            // 手工订单
            int omid = orderManualDao.findOrderManualIdByNumber(number);
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("orderId", omid);
            para.put("productId", productId);
            List<String> dd = getSqlSessionAdmin().selectList("FinanceMapper.findOrderProductByOrderManualNumberAndProductId", para);
            if (dd.size() == 0)
            {
                return null;
            }
            else
            {
                return dd.get(0);
            }
        }
        else
        {
            // 正常订单
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("number", number);
            para.put("productId", productId);
            List<String> dd = getSqlSessionRead().selectList("FinanceMapper.findOrderProductByOrderNumberAndProductId", para);
            if (dd.size() == 0)
            {
                return null;
            }
            else
            {
                return dd.get(0);
            }
        }
        
    }
    
    @Override
    public int updateOrderProductCost(Map<String, Object> para)
        throws Exception
    {
        String number = para.get("number") + "";
        int returnStatus = 0;
        int nType = CommonUtil.estimateOrderNumber(String.valueOf(number));
        if (nType == 1)
        {
            // 手工订单
            int orderId = orderManualDao.findOrderManualIdByNumber(Long.valueOf(number));
            para.put("orderManualId", orderId);
            returnStatus = orderManualDao.updateOrderProductCost(para);
        }
        else
        {
            // 正常订单
            int orderId = orderDao.findOrderIdByNumber(Long.valueOf(number));
            para.put("orderId", orderId);
            returnStatus = orderDao.updateOrderProductCost(para);
        }
        return returnStatus;
    }

    @Override
    public List<Map<String, Object>> findOrderInfoForSellerSettlementPeriod(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findOrderInfoForSellerSettlementPeriod",para);
    }

    @Override
    public List<Map<String, Object>> sumOrderInfoForSellerSettlementPeriod(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.sumOrderInfoForSellerSettlementPeriod",para);
    }
    
    @Override
    public List<Map<String, Object>> findSellerRefundProportion(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findSellerRefundProportion", para);
    }

    @Override
    public List<Integer> findOrderIdByRefundSettlementComfirmDate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findOrderIdByRefundSettlementComfirmDate", para);
    }

    @Override
    public List<Map<String, Object>> findOrderProductByOrderSettlementTime(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findOrderProductByOrderSettlementTime", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderSettlementInfoByComfirmDate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findOrderSettlementInfoByComfirmDate", para);
    }
    
    @Override
    public List<Map<String, Object>> findRefundSettlementInfoByComfirmDate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findRefundSettlementInfoByComfirmDate", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderManualProductByOrderSettlementTime(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().selectList("FinanceMapper.findOrderManualProductByOrderSettlementTime", para);
    }
    
    @Override
    public List<Map<String, Object>> findOrderManualSettlementInfoByComfirmDate(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().selectList("FinanceMapper.findOrderManualSettlementInfoByComfirmDate", para);
    }

    @Override
    public List<Map<String, Object>> findOrderProductAndSettlementByOrderSettlementTime(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionRead().selectList("FinanceMapper.findOrderProductAndSettlementByOrderSettlementTime", para);
    }

    @Override
    public List<Map<String, Object>> findOrderManualProductAndSettlementByOrderSettlementTime(Map<String, Object> para)
        throws Exception
    {
        return this.getSqlSessionAdmin().selectList("FinanceMapper.findOrderManualProductAndSettlementByOrderSettlementTime", para);
    }
}
