package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.RefundReasonDao;
import com.ygg.admin.service.RefundReasonService;

@Service("refundReasonService")
public class RefundReasonServiceImpl implements RefundReasonService
{
    
    @Resource
    private RefundReasonDao refundReasonDao;
    
    @Override
    public int saveRefundReason(Map<String, Object> param)
        throws Exception
    {
        return refundReasonDao.saveRefundReason(param);
    }
    
    @Override
    public int updateRefundReason(Map<String, Object> param)
        throws Exception
    {
        return refundReasonDao.updateRefundReason(param);
    }
    
    @Override
    public Map<String, Object> findRefundReasonList(Map<String, Object> param)
        throws Exception
    {
        List<Map<String, Object>> rows = refundReasonDao.findRefundReasonList(param);
        int total = refundReasonDao.countRefundReason();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("rows", rows);
        return result;
    }
    
    @Override
    public Map<String, Object> findRefundReasonById(int id)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return refundReasonDao.findRefundReasonById(param);
    }
}
