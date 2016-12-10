package com.ygg.admin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.NotSendMsgProductDao;
import com.ygg.admin.service.NotSendMsgProductService;

@Service("notSendMsgService")
public class NotSendMsgProductServiceImpl implements NotSendMsgProductService
{
    @Resource
    private NotSendMsgProductDao notSendMsgDao;
    
    @Override
    public List<Map<String, Object>> queryAllProductId()
        throws Exception
    {
        
        return notSendMsgDao.queryAllProductId();
    }
    
    @Override
    public int add(Map<String, Object> para)
        throws Exception
    {
        return notSendMsgDao.add(para);
    }
    
    @Override
    public int delete(Map<String, Object> para)
        throws Exception
    {
        return notSendMsgDao.delete(para);
    }
    
    @Override
    public boolean checkIsExist(String productId)
        throws Exception
    {
        return notSendMsgDao.checkIsExist(productId);
    }
}
