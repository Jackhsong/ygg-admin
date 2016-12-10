package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.dao.CustomerProblemDao;
import com.ygg.admin.service.CommonService;
import com.ygg.admin.service.CustomerProblemService;

@Service
public class CustomerProblemServiceImpl implements CustomerProblemService
{
    @Resource
    private CustomerProblemDao customerProblemDao;
    
    @Resource
    private CommonService commonService;
    
    @Override
    public String jsonCustomerProblemInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        result.put("rows", customerProblemDao.findAllCustomerProblem(para));
        result.put("total", customerProblemDao.countCustomerProblem(para));
        return JSON.toJSONString(result);
    }
    
    @Override
    public String saveCustomerProblem(String question, String answer, int sequence, int isDisplay)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        if (StringUtils.isEmpty(question))
        {
            result.put("status", 0);
            result.put("msg", "问题不能为空");
            return JSON.toJSONString(result);
        }
        if (StringUtils.isEmpty(answer))
        {
            result.put("status", 0);
            result.put("msg", "答案不能为空");
            return JSON.toJSONString(result);
        }
        if (sequence < 0)
        {
            result.put("status", 0);
            result.put("msg", "排序值不能为负数");
            return JSON.toJSONString(result);
        }
        para.put("question", question);
        para.put("answer", answer);
        para.put("sequence", sequence);
        para.put("isDisplay", isDisplay);
        para.put("createUser", commonService.getCurrentUserId());
        if (customerProblemDao.saveCustomerProblem(para) > 0)
        {
            result.put("status", 1);
            result.put("msg", "保存成功");
            return JSON.toJSONString(result);
        }
        else
        {
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @Override
    public String updateCustomerProblem(int id, String question, String answer, int sequence, int isDisplay)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        if (StringUtils.isEmpty(question))
        {
            result.put("status", 0);
            result.put("msg", "问题不能为空");
            return JSON.toJSONString(result);
        }
        if (StringUtils.isEmpty(answer))
        {
            result.put("status", 0);
            result.put("msg", "答案不能为空");
            return JSON.toJSONString(result);
        }
        if (sequence < 0)
        {
            result.put("status", 0);
            result.put("msg", "排序值不能为负数");
            return JSON.toJSONString(result);
        }
        para.put("id", id);
        para.put("question", question);
        para.put("answer", answer);
        para.put("sequence", sequence);
        para.put("isDisplay", isDisplay);
        para.put("updateUser", commonService.getCurrentUserId());
        if (customerProblemDao.updateCustomerProblem(para) > 0)
        {
            result.put("status", 1);
            result.put("msg", "保存成功");
            return JSON.toJSONString(result);
        }
        else
        {
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
    @Override
    public String updateCustomerProblemStatus(int id, int isDisplay)
        throws Exception
    {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> para = new HashMap<>();
        para.put("id", id);
        para.put("isDisplay", isDisplay);
        if (customerProblemDao.updateCustomerProblem(para) > 0)
        {
            result.put("status", 1);
            result.put("msg", "保存成功");
            return JSON.toJSONString(result);
        }
        else
        {
            result.put("status", 0);
            result.put("msg", "保存失败");
            return JSON.toJSONString(result);
        }
    }
    
}
