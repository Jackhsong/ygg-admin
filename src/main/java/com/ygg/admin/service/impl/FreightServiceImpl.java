package com.ygg.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ygg.admin.dao.FreightDao;
import com.ygg.admin.entity.FreightTemplateEntity;
import com.ygg.admin.entity.ProvinceEntity;
import com.ygg.admin.entity.ProvinceFreightEntity;
import com.ygg.admin.service.FreightService;

@Service("freightService")
public class FreightServiceImpl implements FreightService
{
    
    Logger logger = Logger.getLogger(FreightServiceImpl.class);
    
    @Resource
    private FreightDao freightDao = null;
    
    @Override
    public int saveOrUpdate(FreightTemplateEntity freightTemplate)
        throws Exception
    {
        if (freightTemplate.getId() == 0)
        {
            logger.info("新增邮费模板" + freightTemplate);
            int status = freightDao.save(freightTemplate);
            // 保存各个省份对应的邮费
            if (status == 1)
            {
                List<ProvinceEntity> proList = freightDao.findAllProvince();
                for (ProvinceEntity curr : proList)
                {
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("money", 5);
                    para.put("provinceId", curr.getProvinceId());
                    para.put("templateId", freightTemplate.getId());
                    freightDao.saveProvinceFreight(para);
                }
            }
            else
            {
                return status;
            }
            return status;
        }
        else
        {
            logger.info("更新邮费模板" + freightTemplate);
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("id", freightTemplate.getId());
            para.put("name", freightTemplate.getName());
            para.put("desc", freightTemplate.getDesc());
            para.put("isAvailable", freightTemplate.getIsAvailable());
            return freightDao.update(para);
        }
        
    }
    
    @Override
    public String jsonfreightTemplateInfo(Map<String, Object> para)
        throws Exception
    {
        List<FreightTemplateEntity> freightTemplateList = freightDao.findAllFreightTemplateByPara(para);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        int total = 0;
        if (freightTemplateList != null && freightTemplateList.size() > 0)
        {
            for (FreightTemplateEntity pEntity : freightTemplateList)
            {
                if (pEntity.getId() != 1)
                {// 不显示包邮模板
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("desc", pEntity.getDesc());
                    map.put("id", pEntity.getId());
                    map.put("name", pEntity.getName());
                    map.put("available", pEntity.getIsAvailable() == 1 ? "可用" : "停用");
                    resultList.add(map);
                }
            }
            total = freightDao.countFreightTemplateByPara(para);
        }
        resultMap.put("rows", resultList);
        resultMap.put("total", total);
        return JSON.toJSONString(resultMap);
    }
    
    @Override
    public FreightTemplateEntity findFreightTemplateById(int id)
        throws Exception
    {
        return freightDao.findFreightTemplateById(id);
    }
    
    @Override
    public String jsonProvinceFreightInfo(int templateId)
        throws Exception
    {
        List<ProvinceFreightEntity> provinceFreightList = freightDao.findAllProvinceFreightByTemplateId(templateId);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (provinceFreightList != null && provinceFreightList.size() > 0)
        {
            for (ProvinceFreightEntity pEntity : provinceFreightList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", pEntity.getFreightMoney());
                map.put("id", pEntity.getId());
                map.put("province", freightDao.findProvinceNameByProvinceId(pEntity.getProvinceId()));
                resultList.add(map);
            }
        }
        return JSON.toJSONString(resultList);
    }
    
    @Override
    public int updateProvinceFreight(Map<String, Object> para)
        throws Exception
    {
        return freightDao.updateProvinceFreight(para) >= 1 ? 1 : 0;
    }
    
    @Override
    public List<FreightTemplateEntity> findFreightTemplateIsAvailable()
        throws Exception
    {
        Map<String, Object> para = new HashMap<String, Object>();
        para.put("isAvailable", 1);
        para.put("start", 0);
        para.put("max", 100);
        List<FreightTemplateEntity> reList = freightDao.findAllFreightTemplateByPara(para);
        if (reList.size() > 0)
        {
            reList.remove(0);
            return reList;
        }
        else
        {
            return reList;
        }
        
    }
    
}
