package com.ygg.admin.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ygg.admin.dao.CustomFunctionDao;
import com.ygg.admin.entity.CustomFunctionEntity;
import com.ygg.admin.service.CustomFunctionService;

/**
 * @author Administrator
 *
 */
@Service
public class CustomFunctionServiceImpl implements CustomFunctionService
{
    @Resource
    private CustomFunctionDao customFunctionDao;
    
    @Override
    public Map<String, Object> jsonCustomFunctionInfo(Map<String, Object> para)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", customFunctionDao.countCustomFunction(para));
        result.put("rows", customFunctionDao.findAllCustomFunction(para));
        return result;
    }
    
    @Override
    public int saveCustomFunction(CustomFunctionEntity function)
        throws Exception
    {
        if (StringUtils.isEmpty(function.getFiveImage()))
        {
            function.setFiveImage("");
            function.setFiveType(Byte.parseByte("1"));
            function.setFiveId(0);
            function.setFiveRemark("");
        }
        if (customFunctionDao.saveCustomFunction(function) > 0)
        {
            return function.getId();
        }
        else
        {
            return -1;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int updateCustomFunction(CustomFunctionEntity function)
        throws Exception
    {
        if (StringUtils.isEmpty(function.getFiveImage()))
        {
            function.setFiveImage("");
            function.setFiveType(Byte.parseByte("1"));
            function.setFiveId(0);
            function.setFiveRemark("");
        }
        Map<String, Object> map = new BeanMap(function);
        if (customFunctionDao.updateCustomFunction(map) > 0)
        {
            return function.getId();
        }
        else
        {
            return -1;
        }
    }
    
    @Override
    public int updateDisplayStatus(Map<String, Object> para)
        throws Exception
    {
        return customFunctionDao.updateDisplayStatus(para);
    }
    
}
