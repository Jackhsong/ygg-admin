package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.QqbsOrderDao;
import com.ygg.admin.service.QqbsOrderService;


@Service("qqbsOrderService")
public class QqbsOrderServiceImpl implements QqbsOrderService
{
    
    @Resource
    private QqbsOrderDao qqbsOrderDao;
    
    @Override
    public Map<String, Object> findOrderList(String orderNumber, String orderStatus, String accountId, int page, int rows)
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("start", rows * (page - 1));
        param.put("size", rows);
        param.put("orderNumber", StringUtils.isBlank(orderNumber) ? null : orderNumber);
        param.put("orderStatus", StringUtils.equals("0", orderStatus) ? null : orderStatus);
        param.put("accountId", StringUtils.isBlank(accountId) ? null : accountId);
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", qqbsOrderDao.findOrderList(param));
        result.put("total", qqbsOrderDao.countOrderList(param));
        return result;
    }
    
}
