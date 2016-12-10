/**************************************************************************
* Copyright (c) 2014-2016 浙江格家网络技术有限公司.
* All rights reserved.
* 
* 项目名称：左岸城堡APP
* 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
*           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
*           识产权保护的内容。                            
***************************************************************************/
package com.ygg.admin.service.hotlist.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ygg.admin.code.ProductEnum;
import com.ygg.admin.dao.hotlist.SaleSingleDao;
import com.ygg.admin.entity.hotlist.SaleSingleProductEntity;
import com.ygg.admin.service.hotlist.SaleSingleService;

/**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: SaleSingleServiceImpl.java 9206 2016-03-24 09:13:16Z xiongliang $   
  * @since 2.0
  */
@Service("saleSingleService")
public class SaleSingleServiceImpl implements SaleSingleService
{
    
    /**    */
    @Autowired(required = false)
    @Qualifier("saleSingleDao")
    private SaleSingleDao saleSingleDao;
    
    @Override
    public Map<String, Object> findListInfo(Map<String, Object> param)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> infoList = saleSingleDao.findListInfo(param);
        if (infoList != null && infoList.size() > 0)
        {
            for (Map<String, Object> map : infoList)
            {
                int type = Integer.valueOf(map.get("type") + "").intValue();
                String s = ProductEnum.PRODUCT_TYPE.getDescByCode(type);
                if (StringUtils.isNotBlank(s))
                {
                    map.put("typeName", s);
                }
            }
        }
        result.put("rows", infoList);
        result.put("total", saleSingleDao.getCountByParam(param));
        return result;
    }
    
    @Override
    public int saveOrUpdateInfo(Map<String, Object> param)
        throws Exception
    {
        Object id = param.get("id");
        if (id == null || StringUtils.isBlank(id.toString()))
        {
            return saleSingleDao.save(param);
        }
        else
        {
            Object isDisplay = param.get("isDisplay");
            if (isDisplay == null)
            {
                int ids = Integer.valueOf(id + "").intValue();
                Map<String, Object> map = findByIdOrProductBaseId(ids, -1);
                int dbDisplaySales = Integer.valueOf(map.get("displaySales") + "").intValue();
                int artificialIncrement = Integer.valueOf(param.get("artificialIncrement") + "").intValue();
                param.put("displaySales", dbDisplaySales + artificialIncrement);
            }
            return saleSingleDao.update(param);
        }
    }
    
    @Override
    public Map<String, Object> findByIdOrProductBaseId(int id, int productBaseId)
        throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        if (id != -1)
        {
            param.put("id", id);
        }
        if (productBaseId != -1)
        {
            param.put("productBaseId", productBaseId);
        }
        List<Map<String, Object>> list = saleSingleDao.findListInfo(param);
        if (list != null && list.size() > 0)
            return list.get(0);
        return new HashMap<String, Object>();
    }
    
    @Override
    public int delete(int id)
        throws Exception
    {
        return saleSingleDao.delete(id);
    }
    
    public int getCount(Map<String, Object> para)
        throws Exception
    {
        return saleSingleDao.getCountByParam(para);
    }
    
    /**
    * 根据基本商品id获取符合要求的热卖单品
    * @param productBaseId
    * @return
    * @throws Exception
    */
    public SaleSingleProductEntity getProductInfo(Map<String, Object> para)
        throws Exception
    {
        return saleSingleDao.getProductInfo(para);
    }
}
