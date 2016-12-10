package com.ygg.admin.service.qqbs.user.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ygg.admin.dao.qqbs.QqbsAccountDao;
import com.ygg.admin.entity.qqbs.QqbsAccountEntity;
import com.ygg.admin.service.qqbs.user.IQqbsUserService;

/**
 * @author wuhy
 * @date 创建时间：2016年5月16日 上午10:00:18
 */
@Service
public class QqbsUserService implements IQqbsUserService
{
    
    @Resource
    private QqbsAccountDao qqbsAcountDao;
    
    public QqbsAccountDao getQqbsAcountDao()
    {
        return qqbsAcountDao;
    }
    
    public void setQqbsAcountDao(QqbsAccountDao qqbsAcountDao)
    {
        this.qqbsAcountDao = qqbsAcountDao;
    }
    
    @Override
    public Map<String, Object> findQqbsUsersAndCount(QqbsAccountEntity qqbsUserQueryCriteria)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (qqbsUserQueryCriteria.isEmptyQueryCriteria())
        {
            resultMap.put("rows", null);
            resultMap.put("total", 0);
        }
        else
        {
            resultMap.put("rows", qqbsAcountDao.findAccountsByQqbsUserQueryCriteria(qqbsUserQueryCriteria));
            resultMap.put("total", qqbsAcountDao.countQqbsAccountByQqbsUserQueryCriteria(qqbsUserQueryCriteria));
        }
        
        return resultMap;
    }
    
}
