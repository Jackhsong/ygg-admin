package com.ygg.admin.service.impl;

import com.ygg.admin.dao.OrderProductRefundReasonDao;
import com.ygg.admin.entity.ResultEntity;
import com.ygg.admin.service.OrderProductRefundReasonService;
import com.ygg.admin.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiongl
 * @create 2016-05-18 15:22
 */
@Service
public class OrderProductRefundReasonServiceImpl implements OrderProductRefundReasonService
{
    @Resource
    private OrderProductRefundReasonDao orderProductRefundReasonDao;
    
    @Override
    public Object findOrderProductRefundReasonList(Map<String, Object> param)
        throws Exception
    {
        int total = orderProductRefundReasonDao.countOrderProductRefundReason(param);
        List<Map<String, Object>> rows = orderProductRefundReasonDao.findOrderProductRefundReasonList(param);
        return ResultEntity.getResultList(total, rows);
    }
    
    @Override
    public Object save(String reason, int isDisplay)
        throws Exception
    {
        if (StringUtils.isEmpty(reason))
        {
            return ResultEntity.getFailResult("退款原因不能为空");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("reason", reason);
        param.put("isDisplay", isDisplay);
        try
        {
            return orderProductRefundReasonDao.save(param) > 0 ? ResultEntity.getSuccessResult() : ResultEntity.getFailResult("新增失败");
        }
        catch (Exception e)
        {
            if (e.getMessage().contains("Duplicate") && e.getMessage().contains("uniq_reason"))
            {
                return ResultEntity.getFailResult("退款原因【" + reason + "】已经存在，不能重复添加");
            }
            else
            {
                throw new Exception(e);
            }
        }
    }
    
    @Override
    public Object upadte(int id, String reason, int isDisplay)
        throws Exception
    {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        if (StringUtils.isNotEmpty(reason))
        {
            param.put("reason", reason);
        }
        param.put("isDisplay", isDisplay);
        return orderProductRefundReasonDao.update(param) > 0 ? ResultEntity.getSuccessResult() : ResultEntity.getFailResult("更新失败");
    }

    @Override
    public Object delete(int id) throws Exception {
        return orderProductRefundReasonDao.delete(id) >0 ? ResultEntity.getSuccessResult() : ResultEntity.getFailResult("删除失败");
    }
}
